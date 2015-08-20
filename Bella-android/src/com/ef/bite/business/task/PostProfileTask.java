package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;

public class PostProfileTask extends
		BaseAsyncTask<HttpProfile.ProfileData, Void, HttpBaseMessage> {
	private Context context;

	public PostProfileTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpBaseMessage doInBackground(HttpProfile.ProfileData... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		if(params!=null && params.length > 0)
			return userServerAPI.postUserProfile(params[0]);
		else
			return null;
	}

}
