package com.ef.bite.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public class SoftInputHelper {

	/**
	 * 临时隐藏键盘输入
	 * 
	 * @param activity
	 * @param edit
	 */
	public static void hideTemporarily(Activity activity) {
		try {
			if (activity == null)
				return;
			IBinder bind = activity.getCurrentFocus().getWindowToken();
			if (bind == null)
				return;
			InputMethodManager inputManager = ((InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE));
			if (inputManager == null)
				return;
			inputManager.hideSoftInputFromWindow(bind,
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
