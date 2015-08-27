package com.ef.bite.ui.popup;

import android.app.Activity;
import com.ef.bite.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

public class QuitPracticePopWindow extends BasePopupWindow {

	public final static int Quit_Practice = 0;
	public final static int Quit_Rehearse = 1;
    public final static int Quit_Rate = 2;

	TextView mTitle;
	TextView mInfo;
	Button mBtnNO;
	Button mBtnQuit;
	OnClickListener mQuitListener;
	OnClickListener mCancelListener;
	int mCurrentQuitType = Quit_Practice;

	public QuitPracticePopWindow(Activity activity) {
		super(activity, R.layout.popup_chunk_practice_quit);
	}

	public QuitPracticePopWindow(Activity activity, int quitType) {
		super(activity, R.layout.popup_chunk_practice_quit);
		mCurrentQuitType = quitType;
	}

	/**
	 * 设置退出事件
	 * 
	 * @param listener
	 */
	public void setOnQuitListener(OnClickListener listener) {
		mQuitListener = listener;
	}

	/** 取消事件 **/
	public void setOnCancelListener(OnClickListener listener) {
		mCancelListener = listener;
	}

	@Override
	protected void initViews(View layout) {
		mTitle = (TextView) layout
				.findViewById(R.id.popup_chunk_practice_title);
		mInfo = (TextView) layout.findViewById(R.id.popup_chunk_practice_info);
		mBtnNO = (Button) layout.findViewById(R.id.popup_chunk_practice_btn_no);
		mBtnNO.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"popup_practice_quit_no"));
		mBtnQuit = (Button) layout
				.findViewById(R.id.popup_chunk_practice_btn_quit);
		
		mBtnQuit.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"popup_practice_quit_quit"));
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		
		if (mCurrentQuitType == Quit_Practice) {
			mTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mActivity, "popup_practice_quit_title"));
			mInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mActivity, "popup_practice_quit_info"));
		} else if (mCurrentQuitType == Quit_Rehearse) {
			mTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mActivity, "popup_rehearsal_quit_title"));
			mInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mActivity, "popup_rehearsal_quit_info"));
		} else if (mCurrentQuitType == Quit_Rate) {
            String title = "Quit Rating";
            String info = JsonSerializeHelper.JsonLanguageDeserialize(mActivity, "popup_rating_quit_title");

            if (title.isEmpty()) {
                title = "Quit Rating";
            }
            if (info.isEmpty()) {
                info = "Are you sure that you want to quit?";
            }

            mTitle.setText(title);
            mInfo.setText(info);
        }

        FontHelper.applyFont(mActivity.getApplicationContext(), mBtnNO,
				FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mActivity.getApplicationContext(), mBtnQuit,
                FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mActivity.getApplicationContext(), mInfo,
				FontHelper.FONT_OpenSans);

		mBtnNO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                if (mCancelListener != null)
                    mCancelListener.onClick(v);
            }
		});
		mBtnQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                if (mQuitListener != null)
                    mQuitListener.onClick(v);
            }
		});
	}

}
