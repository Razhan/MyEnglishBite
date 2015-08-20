package com.ef.bite.business.task;

import android.content.Context;
import android.util.Log;

import com.ef.bite.business.LoginServerAPI;
import com.ef.bite.dataacces.mode.LoginMode;
import com.ef.bite.dataacces.mode.httpMode.HttpLogin;

public class LoginTask extends BaseAsyncTask<LoginMode, Void, HttpLogin> {
	public final static String LOGIN_TYPE_ETOWN = "etown";
	public final static String LOGIN_TYPE_BELLA = "bella";
	public final static String LOGIN_TYPE_FACEBOOK = "efid3rd";

	public final static String LOGIN_PROVIDER_WECHAT = "wechat";
	public final static String LOGIN_PROVIDER_ETOWN = "etown";
	public final static String LOGIN_PROVIDER_FACEBOOK = "facebook";
	private Context context;

	public LoginTask(Context context, PostExecuting<HttpLogin> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected HttpLogin doInBackground(LoginMode... params) {
		LoginServerAPI loginServerAPI = new LoginServerAPI(context);
		return loginServerAPI.login(params[0]);
		// LoginMessage login = new LoginMessage();
		// login.status = "0";
		// login.data.access_token = "1";
		// login.data.alias = "allen";
		// login.data.avatar = "1";
		// login.data.bella_id = "1";
		// return login;
	}

}
