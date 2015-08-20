package com.ef.bite.business.task;

import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.ef.bite.AppConst;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.HttpFileDownload;
import com.ef.bite.utils.KeyGenerator;

public class DownloadingFileTask extends BaseAsyncTask<String, Void, String> {

	private FileStorage storage = null;
	private String key = null;

	public DownloadingFileTask(Context context, PostExecuting<String> executing) {
		super(context, executing);
	}

	public DownloadingFileTask(Context context, FileStorage storage,
			String key, PostExecuting<String> executing) {
		super(context, executing);
		this.storage = storage;
		this.key = key;
	}

	/**
	 * @param params
	 *            [0] - 下载地址
	 */
	@Override
	protected String doInBackground(String... params) {
		try {
			HttpFileDownload httpDownload = new HttpFileDownload(params[0]);
			InputStream inputStream = httpDownload.Download();
			if (inputStream != null && storage != null) {
				try {
					if (key == null)
						key = KeyGenerator.getKeyFromDateTime();
					if (storage == null)
						storage = new FileStorage(mContext,
								AppConst.CacheKeys.Storage_Cache);
					storage.put(key, inputStream);
					return key;
				} catch (Exception ex) {
					return null;
				}
			}
			return null;
		} catch (Exception ex) {
			Log.e("DownloadingFileTask", "Fail to download file!");
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}

}
