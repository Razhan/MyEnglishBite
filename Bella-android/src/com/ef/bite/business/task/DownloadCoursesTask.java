package com.ef.bite.business.task;

import android.content.Context;
import android.widget.Toast;
import com.ef.bite.AppConst;
import com.ef.bite.business.ChunkBiz;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;
import com.ef.bite.utils.*;

import java.io.File;

public class DownloadCoursesTask extends BaseAsyncTask<String, Void, Chunk> {
	private Context context;
	private ChunksHolder chunksHolder = ChunksHolder.getInstance();
	private HttpCourseResponse.HttpCourseData courseData;

	/**
	 * 下载课程资源
	 * 
	 * @param context
	 * @param executing
	 */
	public DownloadCoursesTask(Context context,
			HttpCourseResponse.HttpCourseData courseData,
			PostExecuting<Chunk> executing) {
		super(context, executing);
		this.context = context;
		this.courseData = courseData;
	}

	@Override
	protected Chunk doInBackground(String... params) {
		try {

			CourseServerAPI api = new CourseServerAPI(context);
			FileStorage downloadStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_DownloadChunk);
			FileStorage courseStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_Course_Preview);

			// 分配时间戳为文件名
			String key = KeyGenerator.getKeyFromDateTime();
			// 创建文件
			File saveFile = new File(downloadStorage.getStorageFolder(), key);
			// 通过url开始下载文件
			api.downloadCourses(saveFile.getAbsolutePath(),
					courseData.package_url);
			System.out.println("package_url"+courseData.package_url);
			if (downloadStorage.get(key) == null) { // 下载失败
				LogManager
						.definedLog("DownloadCoursesTask -> Fail to download course!");
				return null;
			}
			// 解压课程
			courseStorage.clearAll();
			if (!ZipUtil.decompress(saveFile.getAbsolutePath(),
					courseStorage.getStorageFolder(), "utf-8")) {
				LogManager
						.definedLog("DownloadCoursesTask -> Fail to decrompress the download course!");
				return null;
			}

			// 查找并返回该chunk
			Chunk chunk = chunksHolder.getSpecifyChunk(courseData.course_id,
					AppLanguageHelper.getSystemLaunguage(mContext),
					chunksHolder.loadPreviewChunks());
			return chunk;
		} catch (Exception ex) {
			Toast.makeText(context, "error" + ex.getMessage(),
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
			return null;
		}
	}

}
