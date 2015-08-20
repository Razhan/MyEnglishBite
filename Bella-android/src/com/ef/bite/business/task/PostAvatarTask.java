package com.ef.bite.business.task;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;

import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.ImageUtils;

public class PostAvatarTask extends
		BaseAsyncTask<Bitmap, Void, HttpBaseMessage> {

	private Context context;

	public PostAvatarTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpBaseMessage doInBackground(Bitmap... params) {
		HttpBaseMessage response = null;
		InputStream is = ImageUtils.bitmapToStream(params[0]);
		String key = "avatar_temp_" + System.currentTimeMillis() + ".jpg";
		if (is != null) {
			try {
				FileStorage storage = new FileStorage(mContext,
						AppConst.CacheKeys.Storage_Cache);
				storage.put(key, is);
				UserServerAPI userServerAPI = new UserServerAPI(context);
				response = userServerAPI.postAvatar(storage.findFilePath(key));
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return response;
	}

}
