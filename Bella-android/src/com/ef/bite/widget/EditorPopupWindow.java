package com.ef.bite.widget;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.ui.popup.BasePopupWindow;
import com.ef.bite.utils.JsonSerializeHelper;

public class EditorPopupWindow extends BasePopupWindow {

	View.OnClickListener mReportClickListener;
	TextView mReport;
	TextView mCancel;
	TextView mTitle;

	public EditorPopupWindow(Activity activity, int layoutResId) {
		super(activity, layoutResId);
		// TODO Auto-generated constructor stub
	}

	public EditorPopupWindow(Activity activity,
			View.OnClickListener mReportClickListener) {
		super(activity, R.layout.popup_record_editor);
		this.mReportClickListener = mReportClickListener;
		// tracking
		MobclickTracking.OmnitureTrack
				.AnalyticsTrackState(
						ContextDataMode.EditorPopupValue.pageNameValue,
						ContextDataMode.EditorPopupValue.pageSiteSubSectionValue,
						ContextDataMode.EditorPopupValue.pageSiteSectionValue,
						activity);
	}

	@Override
	protected void initViews(View layout) {
		// TODO Auto-generated method stub
		mReport = (TextView) layout.findViewById(R.id.popup_report_editor);
		mCancel = (TextView) layout
				.findViewById(R.id.popup_record_editor_cancel);
		mTitle = (TextView) layout.findViewById(R.id.popup_avatar_editor_title);

		mReport.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"report_button"));
		mCancel.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"settings_avatar_edit_cancel"));
		mTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"report_text"));

		mReport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
				if (mReportClickListener != null) {
					mReportClickListener.onClick(v);
				}
			}
		});

		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditorPopupWindow.this.close();
			}
		});

	}
}
