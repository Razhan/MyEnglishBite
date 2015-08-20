package com.ef.bite.business.action;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.ui.chunk.ChunkPracticeActivity;
import com.ef.bite.ui.chunk.ChunkRehearsalActivty;

public class RehearseChunkOpenAction extends BaseOpenAction<List<Chunk>> {

	@Override
	public void open(Context context, List<Chunk> data) {
//		String jsonArray = null;
//		try {
//			JSONArray array = new JSONArray();
//			for (int index = 0; index < data.size(); index++) {
//				array.put(data.get(index).toJson());
//			}
//			jsonArray = array.toString();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		Intent intent = new Intent(context, ChunkRehearsalActivty.class);
		intent.putExtra(AppConst.BundleKeys.Multi_Choice_Type,
				ChunkPracticeActivity.Multi_Choice_Type_Rehearsal);
		intent.putExtra(AppConst.BundleKeys.Chunk_Rehearse_List, (Serializable)data);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
