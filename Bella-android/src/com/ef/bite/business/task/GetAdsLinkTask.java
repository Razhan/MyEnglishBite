package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpADsAddress;

public class GetAdsLinkTask extends BaseAsyncTask<Void, Void, HttpADsAddress> {

	private Context context;
	public GetAdsLinkTask(Context context,
			PostExecuting<HttpADsAddress> executing) {
		super(context, executing);
		this.context = context;
	}

	@Override
	protected HttpADsAddress doInBackground(Void... params) {
		return new UserServerAPI(this.context).getAdsLink();
	}

}
