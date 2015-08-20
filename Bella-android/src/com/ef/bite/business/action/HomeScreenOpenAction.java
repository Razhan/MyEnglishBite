package com.ef.bite.business.action;

import android.content.Context;
import android.content.Intent;

import com.ef.bite.AppSession;
import com.ef.bite.ui.main.MainActivity;

/**
 * 关闭并且回到主界面
 * 
 * @author Allen.Zhu
 * 
 */
public class HomeScreenOpenAction extends BaseOpenAction<Void> {

	@Override
	public void open(Context context, Void data) {
		AppSession.getInstance().clear();
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * Restart and open home page
	 * 
	 * @param context
	 */
	public void restart(Context context) {
		AppSession.getInstance().clearAll();
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
