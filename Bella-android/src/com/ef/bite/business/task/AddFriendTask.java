package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

public class AddFriendTask extends BaseAsyncTask<String, Void, HttpBaseMessage> {

	private Context context;

	public AddFriendTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpBaseMessage doInBackground(String... params) {
		return new UserServerAPI(this.context).addFriend(
				AppConst.CurrUserInfo.UserId, params[0]);
	}

}
