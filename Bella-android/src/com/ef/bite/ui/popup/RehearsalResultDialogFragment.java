package com.ef.bite.ui.popup;

import android.app.Activity;
import com.ef.bite.R;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.SoundEffectUtils;
import com.ef.bite.widget.RehearsalResultView;

public class RehearsalResultDialogFragment extends BaseDialogFragment {

	private int mCorrectNum;
	private int mTotalNum;

	public final static int STRING_KEEP_TRYING = R.string.popup_rehearsal_result_title_keep_trying;
	public final static int STRING_NOT_BAD = R.string.popup_rehearsal_result_title_not_bad;
	public final static int STRING_VERY_GOOD = R.string.popup_rehearsal_result_title_very_good;
	public final static int STRING_GREAT = R.string.popup_rehearsal_result_title_great;
	public final static int STRING_AWESOME = R.string.popup_rehearsal_result_title_awesome;

	public RehearsalResultDialogFragment(Activity activity, int correct,
			int total) {
		super(activity);
		mCorrectNum = correct;
		mTotalNum = total;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View layout = mInflater.inflate(R.layout.popup_rehearsal_result, null,
				false);
		TextView title = (TextView) layout
				.findViewById(R.id.popup_rehearsal_result_title);
		TextView descrip = (TextView) layout
				.findViewById(R.id.popup_rehearsal_result_descrip);
		RehearsalResultView result = (RehearsalResultView) layout
				.findViewById(R.id.popup_rehearsal_result_progress);
		if (mCorrectNum > 2) {
			descrip.setText(mActivity.getResources().getString(
					R.string.popup_rehearsal_result_descrip_you_got)
					+ " "
					+ mCorrectNum
					+ " "
					+ mActivity
							.getResources()
							.getString(
									R.string.popup_rehearsal_result_descrip_right_answers));
		} else {
			descrip.setText(mActivity.getResources().getString(
					R.string.popup_rehearsal_result_descrip_you_got)
					+ " "
					+ mCorrectNum
					+ " "
					+ mActivity
							.getResources()
							.getString(
									R.string.popup_rehearsal_result_descrip_right_answer));
		}

		result.setResult(mCorrectNum, mTotalNum);
		// set title
		if (mCorrectNum == 0) {
			title.setText(mActivity.getResources()
					.getString(STRING_KEEP_TRYING));
			new SoundEffectUtils(mActivity)
					.play(SoundEffectUtils.REHEARSE_KEEP_TRYING);
		} else if (mCorrectNum == mTotalNum) {
			title.setText(mActivity.getResources().getString(STRING_AWESOME));
			new SoundEffectUtils(mActivity)
					.play(SoundEffectUtils.REHEARSE_VERY_GOOD);
		} else {
			float rate = (float) mCorrectNum / (float) mTotalNum;
			if (rate < 0.3333) {
				title.setText(mActivity.getResources()
						.getString(STRING_NOT_BAD));
				new SoundEffectUtils(mActivity)
						.play(SoundEffectUtils.REHEARSE_NOT_BAD);
			} else if (rate >= 0.3333 && rate < 0.6666) {
				title.setText(mActivity.getResources().getString(
						STRING_VERY_GOOD));
				new SoundEffectUtils(mActivity)
						.play(SoundEffectUtils.REHEARSE_VERY_GOOD);
			} else {
				title.setText(mActivity.getResources().getString(STRING_GREAT));
				new SoundEffectUtils(mActivity)
						.play(SoundEffectUtils.REHEARSE_VERY_GOOD);
			}
		}
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RehearsalResultDialogFragment.this.dismiss();
			}
		});
		mBuilder.setView(layout);
		return mBuilder.create();
	}
}
