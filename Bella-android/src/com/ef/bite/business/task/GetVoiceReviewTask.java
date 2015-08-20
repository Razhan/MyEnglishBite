package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpReviewVoiceRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpReviewVoiceResponse;

public class GetVoiceReviewTask extends
		BaseAsyncTask<HttpReviewVoiceRequest, Void, HttpReviewVoiceResponse> {
	public static final int ROWS = 3;
	public static final int ROW = 1;
	public static int START = 0;
	

	public GetVoiceReviewTask(Context context,
			PostExecuting<HttpReviewVoiceResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HttpReviewVoiceResponse doInBackground(
			HttpReviewVoiceRequest... arg0) {
		// TODO Auto-generated method stub
		UserServerAPI userServerAPI = new UserServerAPI(mContext);
		HttpReviewVoiceResponse httpReviewVoiceResponse = userServerAPI
				.PostVoiceReview(arg0[0]);
		return httpReviewVoiceResponse;
	}

}
