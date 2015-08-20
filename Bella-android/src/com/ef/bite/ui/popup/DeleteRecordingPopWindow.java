package com.ef.bite.ui.popup;

import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DeleteRecordingPopWindow extends BasePopupWindow {
	View.OnClickListener mCancelClickListener;
	View.OnClickListener mDeleteClickListener;

	TextView popup_chunk_practice_title;
	TextView popup_chunk_practice_info;

	Button popup_recording_cancel;
	Button popup_recording_delete;

	public DeleteRecordingPopWindow(Activity activity) {
		super(activity, R.layout.popup_recording_layout);
		// TODO Auto-generated constructor stub
		// tracking
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.DeleteRecordingValue.pageNameValue,
				ContextDataMode.DeleteRecordingValue.pageSiteSubSectionValue,
				ContextDataMode.DeleteRecordingValue.pageSiteSectionValue,
				activity);
	}

	@Override
	protected void initViews(View layout) {
		// TODO Auto-generated method stub
		popup_chunk_practice_title = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_title);
		popup_chunk_practice_info = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_info);
		popup_recording_cancel = (Button) layout
				.findViewById(R.id.popup_recording_cancel);
		popup_recording_delete = (Button) layout
				.findViewById(R.id.popup_recording_delete);

		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mActivity.getApplicationContext(),
				popup_chunk_practice_info, FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mActivity.getApplicationContext(),
				popup_recording_cancel, FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mActivity.getApplicationContext(),
				popup_recording_delete, FontHelper.FONT_OpenSans);

		popup_chunk_practice_title.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity,
						"user_recording_delete_header"));
		popup_chunk_practice_info
				.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
						"user_recording_delete_body"));
		popup_recording_cancel.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity, "popup_practice_quit_no"));
		popup_recording_delete.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity,
						"user_recording_delete_button"));

		popup_recording_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
				if (mCancelClickListener != null) {
					mCancelClickListener.onClick(v);
				}
			}
		});

		popup_recording_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
				if (mDeleteClickListener != null) {
					mDeleteClickListener.onClick(v);
				}
			}
		});
	}

	public void setOnCancelListener(OnClickListener listener) {
		this.mCancelClickListener = listener;
	}

	public void setOnDeleteListener(OnClickListener listener) {
		this.mDeleteClickListener = listener;
	}

}
