package com.ef.bite.business.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.ChunkBiz;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.KeyGenerator;
import com.ef.bite.utils.LogManager;
import com.ef.bite.utils.ZipUtil;

public class UpdateCoursesTask extends BaseAsyncTask<String, Void, Boolean> {
	private Context context;

	/**
	 * 下载并保持课程资源
	 * 
	 * @param context
	 * @param executing
	 */
	public UpdateCoursesTask(Context context, PostExecuting<Boolean> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			ChunkBLL chunkBll = new ChunkBLL(mContext);
			List<Chunk> chunks = chunkBll.getAllChunksForUpdate();
			if (chunks == null || chunks.size() == 0) // 不存在任何chunk
				return false;
			List<HttpCourseRequest> requestList = new ArrayList<HttpCourseRequest>();
			for (Chunk chunk : chunks) {
				HttpCourseRequest reques = new HttpCourseRequest();
				reques.app_version = AppConst.GlobalConfig.App_Version;
				reques.course_id = chunk.getChunkCode();
				reques.course_version = chunk.getVersion();
				reques.system = AppConst.GlobalConfig.OS;
				// reques.source_language = AppLanguageHelper.ZH_CN;
				reques.source_language = AppLanguageHelper
						.getSystemLaunguage(mContext);
				reques.target_language = AppLanguageHelper.EN;
				requestList.add(reques);
			}

			CourseServerAPI api = new CourseServerAPI(context);
			FileStorage downloadStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_DownloadChunk);
			FileStorage courseStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_Course);
			HttpCourseResponse courseResponse = api.getAllCourses(requestList);

			if (courseResponse == null || courseResponse.status == null
					|| !"0".equals(courseResponse.status)
					|| courseResponse.data == null
					|| courseResponse.data.size() == 0) // 没有课程相应
				return false;
			for (HttpCourseResponse.HttpCourseData course : courseResponse.data) {

				if (course.has_update) {
					String key = KeyGenerator.getKeyFromDateTime();
					File saveFile = new File(
							downloadStorage.getStorageFolder(), key);
					api.downloadCourses(saveFile.getAbsolutePath(),
							course.package_url);
					if (downloadStorage.get(key) == null) { // 下载失败
						LogManager
								.definedLog("DownloadCoursesTask -> Fail to download course!");
						continue;
					}
					// 解压课程
					if (!ZipUtil.decompress(saveFile.getAbsolutePath(),
							courseStorage.getStorageFolder(), "utf-8")) {
						LogManager
								.definedLog("DownloadCoursesTask -> Fail to decrompress the download course!");
						continue;
					}

                    ChunksHolder.getInstance().loadAllChunks();
//					// chunk入库DB
//					ChunkBiz chunkBiz = new ChunkBiz(mContext);
//					chunkBiz.loadChunkFromStorage(mContext,
//							courseStorage.getStorageFolder());
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
