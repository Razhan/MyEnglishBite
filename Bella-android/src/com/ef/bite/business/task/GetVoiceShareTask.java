package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpVoiceShareRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpVoiceShareRespone;

public class GetVoiceShareTask extends
		BaseAsyncTask<HttpVoiceShareRequest, Void, HttpVoiceShareRespone> {
	Context mContext;
	private Context context;

	public GetVoiceShareTask(Context context,
								PostExecuting<HttpVoiceShareRespone> executing) {
		super(context, executing);
		this.context = context;

	}

	@Override
	protected HttpVoiceShareRespone doInBackground(
			HttpVoiceShareRequest... params) {
		UserServerAPI api = new UserServerAPI(context);
		return api.getVoiceShare(params[0]);
	}

}
