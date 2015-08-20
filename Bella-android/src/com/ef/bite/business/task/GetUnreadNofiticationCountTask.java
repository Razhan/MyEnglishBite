package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.LoginServerAPI;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpAppResourceRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpUnreadNotificationCount;
import com.ef.bite.utils.AppUtils;

public class GetUnreadNofiticationCountTask extends
		BaseAsyncTask<String, Void, HttpUnreadNotificationCount> {
	private Context context;

	public GetUnreadNofiticationCountTask(Context context,
			PostExecuting<HttpUnreadNotificationCount> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected HttpUnreadNotificationCount doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI
				.getUnreadNotificationCount(AppConst.CurrUserInfo.UserId);
	}

}
