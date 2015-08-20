package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;

/**
 * 获得用户的Profile
 * 
 * @author Allen
 * 
 */
public class GetProfileTask extends BaseAsyncTask<String, Void, HttpProfile> {

	private Context context;

	public GetProfileTask(Context context, PostExecuting<HttpProfile> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpProfile doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.getUserProfile(params[0]);
	}

}
