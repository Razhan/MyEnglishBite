package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpShareLink;
import com.ef.bite.utils.AppLanguageHelper;


public class GetShareChunkLinkTask extends
		BaseAsyncTask<String, Void, HttpShareLink> {
	
	private Context context;
	
	public final static String COURSE_VERSION = "1";

	public GetShareChunkLinkTask(Context context,
			PostExecuting<HttpShareLink> executing) {
		super(context, executing);
		this.context = context;
	}

	/**
	 * params[0] bella_id
	 * params[1] course_id 
	 * params[2] language 当前的语言
	 */
	@Override
	protected HttpShareLink doInBackground(String... params) {
		return new UserServerAPI(context).getShareChunkLink(params[0],params[1], COURSE_VERSION, params[2],AppLanguageHelper.EN );
	}

}
