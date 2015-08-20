package com.ef.bite.business.task;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpVoiceDeletRequest;

import android.content.Context;

public class PostVoiceDeleteTask extends
		BaseAsyncTask<HttpVoiceDeletRequest, Void, Boolean> {

	public PostVoiceDeleteTask(Context context, PostExecuting<Boolean> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean doInBackground(HttpVoiceDeletRequest... arg0) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		userServerAPI.PostVoiceDelete(arg0[0]);
		return true;
	}
}
