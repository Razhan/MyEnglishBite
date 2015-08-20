package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;

/**
 * 获得dashboard中的朋友列表
 *
 */
public class GetDashboardFriendListTask extends
		BaseAsyncTask<Void, Void, HttpGetFriends> {
	
	private Context context;

	public GetDashboardFriendListTask(Context context,
			PostExecuting<HttpGetFriends> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpGetFriends doInBackground(Void... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.dashboard(AppConst.CurrUserInfo.UserId);
	}

}
