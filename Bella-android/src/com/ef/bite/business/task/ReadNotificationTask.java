package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.ReadNotificationHttpRequest;

public class ReadNotificationTask extends
		BaseAsyncTask<ReadNotificationHttpRequest, Void, HttpBaseMessage> {
	private Context context;

	public ReadNotificationTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		this.context = context;
		
	}

	@Override
	protected HttpBaseMessage doInBackground(
			ReadNotificationHttpRequest... params) {
		UserServerAPI api = new UserServerAPI(context);
		return api.readNotification(params[0]);
	}

}
