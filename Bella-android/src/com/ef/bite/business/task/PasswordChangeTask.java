package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

public class PasswordChangeTask extends
		BaseAsyncTask<String, Void, HttpBaseMessage> {
	private Context context;

	public PasswordChangeTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] uid
	 * params[1] old password
	 * params[2] new password
	 */
	@Override
	protected HttpBaseMessage doInBackground(String... params) {
		return new UserServerAPI(context).passwordChange(params[0],params[1],params[2]);
	}

}
