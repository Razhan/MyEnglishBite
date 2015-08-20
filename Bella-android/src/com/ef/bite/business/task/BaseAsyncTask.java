package com.ef.bite.business.task;

import com.litesuits.android.async.AsyncTask;

import android.content.Context;

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	protected PostExecuting<Result> mExecuting;
	protected Context mContext;

	public BaseAsyncTask(Context context, PostExecuting<Result> executing) {
		mContext = context;
		mExecuting = executing;
	}

	protected void onPostExecute(final Result result) {
		mExecuting.executing(result);
	}
}
