package com.ef.bite.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public abstract class BasePopupWindow {

	private PopupWindow mPopUpWindow;
	protected Activity mActivity;
	private int mResId;
	private OnCloseListener mOnCloseListener;
	private View layout;

	public BasePopupWindow(Activity activity, int layoutResId) {
		mActivity = activity;
		mResId = layoutResId;
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(mResId, null, false);
	}

	/**
	 * 打开PopUp Window
	 * 
	 * @param window
	 */
	public void open() {
		initViews(layout);
		mPopUpWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setOutsideTouchable(true);
		mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}

	abstract protected void initViews(View layout);

	/**
	 * 设置OnClose事件
	 * 
	 * @param onCloseListener
	 */
	public void setOnCloseListener(
			BasePopupWindow.OnCloseListener onCloseListener) {
		mOnCloseListener = onCloseListener;
	}

	/**
	 * 关闭Pop up window
	 */
	public void close() {
		if (mPopUpWindow != null)
			mPopUpWindow.dismiss();
		if (mOnCloseListener != null)
			mOnCloseListener.onClose();
	}

	public interface OnCloseListener {
		void onClose();
	}



}
