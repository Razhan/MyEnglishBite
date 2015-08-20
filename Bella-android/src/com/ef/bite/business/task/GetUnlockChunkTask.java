package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpUnlockChunks;

/**
 * Unlock new chunk
 */
public class GetUnlockChunkTask extends BaseAsyncTask<String, Void, HttpUnlockChunks> {

	private Context context;

	public GetUnlockChunkTask(Context context, PostExecuting<HttpUnlockChunks> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpUnlockChunks doInBackground(String... params) {
		CourseServerAPI courseServerAPI = new CourseServerAPI(mContext);
		return courseServerAPI.getUnlockChunk(params[0]);
	}

}
