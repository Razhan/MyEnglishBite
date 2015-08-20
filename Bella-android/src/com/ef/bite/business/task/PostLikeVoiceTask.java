package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpVoiceRequest;

public class PostLikeVoiceTask extends
		BaseAsyncTask<HttpVoiceRequest, Void, Boolean> {

	public PostLikeVoiceTask(Context context, PostExecuting<Boolean> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean doInBackground(HttpVoiceRequest... params) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		userServerAPI.PostLikeVoice(params[0]);
		return null;
	}

}
