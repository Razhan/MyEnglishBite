package com.ef.bite.ui.popup;

import android.app.Activity;
import com.ef.bite.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * Chunk Practice以及Rehearsal错误提示
 * 
 * @author Admin
 * 
 */
public class PracticeErrorPopWindow {
	public final static int ERROR_TYPE_PRACTICE = 0;
	public final static int ERROR_TYPE_REHEASAL = 1;

	private PopupWindow mPopUpWindow;
	private Activity mActivity;
	private Button mBtnLearn;
	private Button mBtnTryAgain;
	private TextView mTitle;
	private TextView mErrorHint;
	private String wrong;
	private LinearLayout mPracticeBtnBar; // Practice失败显示
	private LinearLayout mReheasalBtnBar; // Reharsal失败显示
	private Button mBtnNext; // 属于Reharsal失败显示
	private View.OnClickListener mLearnListener;
	private View.OnClickListener mTryAgainListener;
	private View.OnClickListener mGoNextListener;
	int mCurrentErrorType = ERROR_TYPE_PRACTICE;
	String title;

	/**
	 * Practice Error
	 * 
	 * @param activity
	 * @param errorHint
	 * @param learnListener
	 * @param tryListener
	 */
	public PracticeErrorPopWindow(Activity activity, String errorHint,
			View.OnClickListener learnListener, View.OnClickListener tryListener) {
		mActivity = activity;
		wrong = errorHint;
		mLearnListener = learnListener;
		mTryAgainListener = tryListener;
		mCurrentErrorType = ERROR_TYPE_PRACTICE;
	}

	/**
	 * Rehearsal Error
	 * 
	 * @param activity
	 * @param errorHint
	 * @param nextListener
	 */
	public PracticeErrorPopWindow(Activity activity, String errorHint,
			View.OnClickListener nextListener) {
		mActivity = activity;
		wrong = errorHint;
		mGoNextListener = nextListener;
		mCurrentErrorType = ERROR_TYPE_REHEASAL;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 打开PopUp Window
	 * 
	 * @param window
	 */
	public void open() {
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.popup_message_chunk_prictice_error, null, false);
		mBtnLearn = (Button) layout
				.findViewById(R.id.popup_chunk_practice_btn_lean);
		mBtnLearn.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mActivity, "chunk_practice_error_popup_learn"));
		mBtnTryAgain = (Button) layout
				.findViewById(R.id.popup_chunk_practice_btn_again);
		mBtnTryAgain.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mActivity, "chunk_practice_error_popup_tryagain"));
		mTitle = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_error_title);
		mErrorHint = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_error_hint);
		mPracticeBtnBar = (LinearLayout) layout
				.findViewById(R.id.popup_chunk_practice_error_button_bar);
		mReheasalBtnBar = (LinearLayout) layout
				.findViewById(R.id.popup_chunk_rehease_error_botton_bar);
		mBtnNext = (Button) layout
				.findViewById(R.id.popup_chunk_rehease_error_next);
		if (title != null)
			mTitle.setText(title);
		if (wrong == null)
			mErrorHint.setVisibility(View.GONE);
		else
			mErrorHint.setText(wrong);
		mPopUpWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		initEvents();
	}

	/**
	 * 加载事件
	 */
	protected void initEvents() {
		if (mCurrentErrorType == ERROR_TYPE_PRACTICE) { // 如果是Practice Error
			mPracticeBtnBar.setVisibility(View.VISIBLE);
			mReheasalBtnBar.setVisibility(View.GONE);

			if (mLearnListener == null) {
				mBtnLearn.setVisibility(View.GONE);
				if (mTryAgainListener != null) {
					ViewGroup.LayoutParams params = mPracticeBtnBar
							.getLayoutParams();
					params.width = (int) mActivity.getResources().getDimension(
							R.dimen.popup_window_single_button_width);
					mPracticeBtnBar.setLayoutParams(params);
				}
			} else {
				mBtnLearn.setVisibility(View.VISIBLE);
				mBtnLearn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mPopUpWindow.dismiss();
						if (mLearnListener != null)
							mLearnListener.onClick(v);
					}
				});
			}

			if (mTryAgainListener == null) {
				mBtnTryAgain.setVisibility(View.GONE);
				if (mLearnListener != null) {
					ViewGroup.LayoutParams params = mPracticeBtnBar
							.getLayoutParams();
					params.width = (int) mActivity.getResources().getDimension(
							R.dimen.popup_window_single_button_width);
					mPracticeBtnBar.setLayoutParams(params);
				}
			} else {
				mBtnTryAgain.setVisibility(View.VISIBLE);
				mBtnTryAgain.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mPopUpWindow.dismiss();
						if (mTryAgainListener != null)
							mTryAgainListener.onClick(v);
					}
				});
			}
		} else if (mCurrentErrorType == ERROR_TYPE_REHEASAL) { // 如果是Reheasal
																// Error
			mPracticeBtnBar.setVisibility(View.INVISIBLE);
			mReheasalBtnBar.setVisibility(View.VISIBLE);
			mBtnNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopUpWindow.dismiss();
					if (mGoNextListener != null)
						mGoNextListener.onClick(v);
				}
			});
		}
	}

}
