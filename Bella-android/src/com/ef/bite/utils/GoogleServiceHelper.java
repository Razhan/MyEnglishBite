package com.ef.bite.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class GoogleServiceHelper {

	/**
	 * 检查Google Play Service是否运行
	 * @param context
	 * @return
	 */
	public static boolean checkPlayServicesRunning(Context context) {
    	
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals("com.google.android.gms.gcm.GcmService")) {
                return true;
            }
        }
    	
    	return false;
    }
}
