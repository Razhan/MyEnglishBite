package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.LoginServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

/**
 * 注册EF账户
 * 
 */
public class EFRegisterTask extends
		BaseAsyncTask<Object, Void, HttpBaseMessage> {

	private Context context;

	public EFRegisterTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] - email params[1] - password
	 */
	@Override
	protected HttpBaseMessage doInBackground(Object... params) {
		LoginServerAPI loginServerAPI = new LoginServerAPI(context);
		return loginServerAPI.registerUser(params[0], params[1], params[2],
				params[3], params[4], params[5], params[6]);
	}

}
