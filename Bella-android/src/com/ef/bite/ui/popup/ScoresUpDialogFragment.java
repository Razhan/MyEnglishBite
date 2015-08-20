package com.ef.bite.ui.popup;

import android.app.Activity;
import com.ef.bite.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.ScoresUpView;

public class ScoresUpDialogFragment extends BaseDialogFragment {

	private int level;
	private int levelUpScore;
	private int existedScore;
	private int newScore;
	private Context mContext;

	public ScoresUpDialogFragment(Activity activity, int level,
			int levelUpScore, int existedScore, int newScore, Context context) {
		super(activity);
		this.level = level;
		this.levelUpScore = levelUpScore;
		this.existedScore = existedScore;
		this.newScore = newScore;
		this.mContext = context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.ScoresUpMessageValues.pageNameValue,
				ContextDataMode.ScoresUpMessageValues.pageSiteSubSectionValue,
				ContextDataMode.ScoresUpMessageValues.pageSiteSectionValue,
				mContext);
		View layout = mInflater.inflate(R.layout.popup_scores_up, null, false);
		ScoresUpView scoreup = (ScoresUpView) layout
				.findViewById(R.id.popup_scores_up_progress);
		TextView scoreText = (TextView) layout
				.findViewById(R.id.popup_scores_up_score);
		TextView popup_score_up_title = (TextView) layout.findViewById(R.id.popup_score_up_title);
		popup_score_up_title.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext, "popup_score_up_title"));
		scoreup.setScoreText(scoreText);
		scoreup.setScores(level, levelUpScore, existedScore, newScore);
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ScoresUpDialogFragment.this.dismiss();
			}
		});
		mBuilder.setView(layout);
		return mBuilder.create();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickTracking.UmengTrack.setResume(mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.ScoresUpMessageValues.pageNameValue,
//				ContextDataMode.ScoresUpMessageValues.pageSiteSubSectionValue,
//				ContextDataMode.ScoresUpMessageValues.pageSiteSectionValue,
//				mContext);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPause(mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.ScoresUpMessageValues.pageNameValue,
//				ContextDataMode.ScoresUpMessageValues.pageSiteSubSectionValue,
//				ContextDataMode.ScoresUpMessageValues.pageSiteSectionValue,
//				mContext);
	}

}
