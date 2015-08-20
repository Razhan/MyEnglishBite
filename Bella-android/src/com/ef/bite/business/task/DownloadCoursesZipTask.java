package com.ef.bite.business.task;

import java.io.File;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.KeyGenerator;
import com.ef.bite.utils.LogManager;
import com.ef.bite.utils.ZipUtil;

import android.app.ProgressDialog;
import android.content.Context;

public class DownloadCoursesZipTask extends
		BaseAsyncTask<HttpCourseResponse, Integer, Boolean> {

	ProgressDialog progressDialog;

	public DownloadCoursesZipTask(Context context,
			ProgressDialog progressDialog, PostExecuting<Boolean> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.progressDialog = progressDialog;
	}

	@Override
	protected Boolean doInBackground(HttpCourseResponse... params) {
		// TODO Auto-generated method stub
		try {
			CourseServerAPI api = new CourseServerAPI(mContext);
			FileStorage downloadStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_DownloadChunk);
			FileStorage courseStorage = new FileStorage(mContext,
					AppConst.CacheKeys.Storage_Course);

			// 分配时间戳为文件名
			String key = KeyGenerator.getKeyFromDateTime();
			// 创建文件
			File saveFile = new File(downloadStorage.getStorageFolder(), key);
			for (int i = 0; i < params[0].data.size(); i++) {
				publishProgress(i);
				// if (params[0].data.get(i).has_update) {
				// 通过url开始下载文件
				api.downloadCourses(saveFile.getAbsolutePath(),
						params[0].data.get(i).package_url);
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

				// // chunk入库DB
				// ChunkBiz chunkBiz = new ChunkBiz(mContext);
				// chunkBiz.loadChunkFromStorage(mContext,
				// courseStorage.getStorageFolder());
				// }
			}

			ChunksHolder.getInstance().loadAllChunks();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		progressDialog.setMessage(mContext.getResources().getString(
				R.string.loading_data));
	}

}
