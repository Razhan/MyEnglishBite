package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

public class PasswordForgetTask extends
		BaseAsyncTask<String, Void, HttpBaseMessage> {
	private Context context;

	public PasswordForgetTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] phone number
	 */
	@Override
	protected HttpBaseMessage doInBackground(String... params) {
		return new UserServerAPI(context).passwordForget(params[0]);
	}

}
