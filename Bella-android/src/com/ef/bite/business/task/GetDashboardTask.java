package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;

/**
 * 获得用户的 Dashboard Info
 * 
 * @author Allen
 * 
 */
public class GetDashboardTask extends BaseAsyncTask<String, Void, HttpDashboard> {

	private Context context;

	public GetDashboardTask(Context context, PostExecuting<HttpDashboard> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpDashboard doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.getDashboard();
	}

}
