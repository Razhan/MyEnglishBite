package com.ef.bite.ui.popup;

import com.ef.bite.R;
import com.ef.bite.utils.JsonSerializeHelper;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReviewActivityPopWindow extends BasePopupWindow {

	View.OnClickListener mOnClickListener;

	TextView popup_chunk_practice_title;
	TextView popup_chunk_practice_info;
	Button popup_chunk_practice_btn_no;

	public ReviewActivityPopWindow(Activity activity) {
		super(activity, R.layout.popup_review_activity_layout);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initViews(View layout) {
		// TODO Auto-generated method stub
		popup_chunk_practice_title = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_error_title);
		popup_chunk_practice_info = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_info);
		popup_chunk_practice_btn_no = (Button) layout
				.findViewById(R.id.popup_chunk_practice_btn_no);

		popup_chunk_practice_info.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity, "rating_view_no_more"));
		popup_chunk_practice_btn_no.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity, "register_ef_next"));

		popup_chunk_practice_btn_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
				if (mOnClickListener != null) {
					mOnClickListener.onClick(v);
				}
			}
		});
	}

	public void setOnNextClickListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

}
