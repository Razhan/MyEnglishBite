package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.SyncBiz;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

/**
 * 同步用户追踪
 * 
 * @author Admin
 * 
 */
public class PostTracingTask extends
		BaseAsyncTask<String, Void, HttpBaseMessage> {

	public PostTracingTask(Context context,
			PostExecuting<HttpBaseMessage> executing) {
		super(context, executing);
	}

	@Override
	protected HttpBaseMessage doInBackground(String... params) {
		SyncBiz syncBiz = new SyncBiz(mContext);
		return syncBiz.postTracking(params[0]);
	}

}
