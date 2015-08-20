package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpNotification;

public class GetNotificationsTask extends
		BaseAsyncTask<String, Void, HttpNotification> {

	private Context context;

	public GetNotificationsTask(Context context,
			PostExecuting<HttpNotification> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected HttpNotification doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.getAllNotification(AppConst.CurrUserInfo.UserId);
	}

}
