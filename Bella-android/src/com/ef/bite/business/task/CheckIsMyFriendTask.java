package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpIsMyFriend;

public class CheckIsMyFriendTask extends
		BaseAsyncTask<String, Void, HttpIsMyFriend> {
	private Context context;

	public CheckIsMyFriendTask(Context context,
			PostExecuting<HttpIsMyFriend> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected HttpIsMyFriend doInBackground(String... params) {
		return new UserServerAPI(this.context).isMyFriend(params[0],params[1]);
	}

}
