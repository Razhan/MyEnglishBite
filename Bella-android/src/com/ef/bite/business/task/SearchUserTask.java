package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;

public class SearchUserTask extends BaseAsyncTask<String, Void, HttpGetFriends> {

	private Context context;
	public SearchUserTask(Context context,
			PostExecuting<HttpGetFriends> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpGetFriends doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.getSearchViewAll(params[0]);
	}

}
