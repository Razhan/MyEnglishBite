package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpFriendCount;

public class GetFriendCountTask extends
		BaseAsyncTask<String, Void, HttpFriendCount> {

	private Context context;

	public GetFriendCountTask(Context context,
			PostExecuting<HttpFriendCount> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpFriendCount doInBackground(String... params) {
		UserServerAPI server = new UserServerAPI(context);
		return server.getFriendCount(params[0]);
	}

}
