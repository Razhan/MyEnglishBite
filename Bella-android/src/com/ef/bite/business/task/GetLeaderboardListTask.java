package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;

public class GetLeaderboardListTask extends
		BaseAsyncTask<Integer, Void, HttpGetFriends> {

	public final static int TYPE_GLOBAL = 0;

	public final static int TYPE_FRIEND = 1;
	private Context context;

	private int type = TYPE_GLOBAL;

	public GetLeaderboardListTask(Context context, int type,
			PostExecuting<HttpGetFriends> executing) {
		super(context, executing);
		this.type = type;
		this.context = context;
	}

	@Override
	protected HttpGetFriends doInBackground(Integer... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		if (type == TYPE_GLOBAL) {
			if (params == null || params.length == 0)
				return userServerAPI.leaderboardview();
			return userServerAPI.leaderboardview(params[0], params[1]);
		} else {
			return userServerAPI
					.leaderboardFriends(AppConst.CurrUserInfo.UserId);
		}
	}

}
