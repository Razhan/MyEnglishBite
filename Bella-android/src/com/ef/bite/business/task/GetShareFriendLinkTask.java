package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpShareLink;

public class GetShareFriendLinkTask extends
		BaseAsyncTask<String, Void, HttpShareLink> {
	private Context context;

	public GetShareFriendLinkTask(Context context,
			PostExecuting<HttpShareLink> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] bella id params[1] language
	 */
	@Override
	protected HttpShareLink doInBackground(String... params) {
		return new UserServerAPI(context).getShareFriendLink(params[0],
				params[1]);
	}

}
