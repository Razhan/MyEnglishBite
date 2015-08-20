package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.dataacces.mode.Chunk;

public class ChunkMasteredListAdapter extends BaseListAdapter<Chunk> {
	
	public ChunkMasteredListAdapter(Activity context,List<Chunk> dataList) {
		super(context, R.layout.chunk_list_mastered_list_item, dataList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getView(View layout, int position, Chunk data) {
		TextView text = (TextView)layout.findViewById(R.id.chunk_list_mastered_list_item_content);
		ImageView line = (ImageView)layout.findViewById(R.id.chunk_list_mastered_list_item_line);
		if(data!=null &&data.getChunkText()!=null)
			text.setText(data.getChunkText());
		if(position== mDataList.size() - 1) // last
			line.setVisibility(View.INVISIBLE);
		else
			line.setVisibility(View.VISIBLE);
	}

}
