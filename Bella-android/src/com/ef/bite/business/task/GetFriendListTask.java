package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;

public class GetFriendListTask extends
		BaseAsyncTask<String, Void, HttpGetFriends> {

	private Context context;

	public GetFriendListTask(Context context,
			PostExecuting<HttpGetFriends> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * params[0] 寻找指定用户朋友的bella_id
	 */
	@Override
	protected HttpGetFriends doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.GetAllFiendsProfile(params[0]);
	}

}
