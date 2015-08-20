package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpStudyPlans;

/**
 * 获得用户的StudyPlan
 * 
 * @author Allen
 * 
 */
public class GetStudyPlanTask extends BaseAsyncTask<String, Void, HttpStudyPlans> {

	private Context context;

	public GetStudyPlanTask(Context context, PostExecuting<HttpStudyPlans> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpStudyPlans doInBackground(String... params) {
		UserServerAPI userServerAPI = new UserServerAPI(context);
		return userServerAPI.getStudyPlan(params[0]);
	}

}
