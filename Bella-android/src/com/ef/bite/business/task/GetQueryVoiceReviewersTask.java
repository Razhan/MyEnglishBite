package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpQueryVoiceReviewersRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpQueryVoiceReviewersResponse;

public class GetQueryVoiceReviewersTask
		extends
		BaseAsyncTask<HttpQueryVoiceReviewersRequest, Void, HttpQueryVoiceReviewersResponse> {

	public GetQueryVoiceReviewersTask(Context context,
			PostExecuting<HttpQueryVoiceReviewersResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HttpQueryVoiceReviewersResponse doInBackground(
			HttpQueryVoiceReviewersRequest... arg0) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		return userServerAPI.PostQueryVoiceReviewers(arg0[0]);
	}

}
