package com.ef.bite.business.task;

import com.ef.bite.business.UserServerAPI;

import android.content.Context;

public class PostFingerPrintTask extends BaseAsyncTask<String, Void, Boolean> {

	public PostFingerPrintTask(Context context, PostExecuting<Boolean> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		return userServerAPI.PosetFingerPrint(params[0], params[1]);
	}

}
