package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.dataacces.mode.HintDefinition;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.HighLightStringHelper;

public class ChunkHintsListAdapter extends BaseListAdapter<HintDefinition> {

	public ChunkHintsListAdapter(Activity context, List<HintDefinition> dataList) {
		super(context, R.layout.chunk_hint_list_item, dataList);
	}

	@Override
	public void getView(View layout, int position, HintDefinition data) {
		TextView title = (TextView) layout
				.findViewById(R.id.chunk_hint_list_item_title);
		TextView content = (TextView) layout
				.findViewById(R.id.chunk_hint_list_item_content);
		title.setText(data.getContent());
		if (data.getExample() != null && !data.getExample().isEmpty()) {
			SpannableStringBuilder spanStr = HighLightStringHelper
					.getBoldString(mContext, data.getExample());
			if (spanStr != null)
				content.setText(spanStr);
			else
				content.setText(data.getExample());
		}
		FontHelper.applyFont(mContext, content, FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mContext, title, FontHelper.FONT_Museo300);
	}

}
