package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpProgressNextRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpProgressNextResponse;

public class GetProgressNextTask
		extends
		BaseAsyncTask<HttpProgressNextRequest, Integer, HttpProgressNextResponse> {

	public GetProgressNextTask(Context context,
			PostExecuting<HttpProgressNextResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HttpProgressNextResponse doInBackground(
			HttpProgressNextRequest... params) {
		// TODO Auto-generated method stub
		CourseServerAPI courseApi = new CourseServerAPI(mContext);
		return courseApi.getProgressNext(params[0]);
	}
}
