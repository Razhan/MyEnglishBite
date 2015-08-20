package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpUserRecordingRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpUserRecordingResponse;

public class GetUserRecordingTask
		extends
		BaseAsyncTask<HttpUserRecordingRequest, Void, HttpUserRecordingResponse> {

	public GetUserRecordingTask(Context context,
			PostExecuting<HttpUserRecordingResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HttpUserRecordingResponse doInBackground(
			HttpUserRecordingRequest... params) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		HttpUserRecordingResponse httpUserRecordingResponse = userServerAPI
				.GetUserRecordingView(params[0]);
		return httpUserRecordingResponse;
	}

}
