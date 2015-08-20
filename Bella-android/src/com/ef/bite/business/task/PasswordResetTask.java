package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

public class PasswordResetTask extends
		BaseAsyncTask<String, Void, HttpBaseMessage> {
	private Context context;

	public PasswordResetTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] verify code
	 * params[1] new password
	 */
	@Override
	protected HttpBaseMessage doInBackground(String... params) {
		return new UserServerAPI(context).passwordReset(params[0],params[1]);
	}

}
