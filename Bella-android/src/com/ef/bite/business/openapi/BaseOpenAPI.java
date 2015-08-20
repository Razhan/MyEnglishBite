package com.ef.bite.business.openapi;

import android.app.Activity;
import android.content.Context;

import com.ef.bite.model.sso.LoginResponse;
import com.ef.bite.model.sso.LogoutResponse;
import com.ef.bite.model.sso.ShareRequest;
import com.ef.bite.model.sso.UserInfoModel;

public abstract class BaseOpenAPI {

	protected Activity mActivity;
	protected Context mContext;
	public BaseOpenAPI(Activity activity){
		mActivity = activity;
		mContext = mActivity.getApplicationContext();
	}
	
	public abstract void login(RequestCallback<LoginResponse> callback );
	
	public abstract void logout(RequestCallback<LogoutResponse> callback );
	
	public abstract void getUserInfo(RequestCallback<UserInfoModel> callback);
	
	public abstract void share(ShareRequest share, RequestCallback<Boolean> callback);
	
}
