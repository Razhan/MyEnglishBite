package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.ChunkBLL;

/***
 * 后台进程加载后续部分未加载的chunk
 * 
 * @author Allen.Zhu
 * 
 */
public class LoadLaterChunksTask extends BaseAsyncTask<Void, Void, Void> {

	public LoadLaterChunksTask(Context context, PostExecuting<Void> executing) {
		super(context, executing);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			new ChunkBLL(mContext).loadLaterinstallChunks();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
