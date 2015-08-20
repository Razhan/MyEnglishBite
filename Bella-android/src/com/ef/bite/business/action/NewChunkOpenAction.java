package com.ef.bite.business.action;

import android.content.Context;
import android.content.Intent;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.ui.chunk.ChunkLearnActivity;

public class NewChunkOpenAction extends BaseOpenAction<Chunk> {

	@Override
	public void open(Context context, Chunk data) {
		Intent intent = new Intent(context, ChunkLearnActivity.class);
		intent.putExtra(AppConst.BundleKeys.Chunk, data);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
