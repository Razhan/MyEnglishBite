package com.ef.bite.utils;

import java.util.UUID;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AppUtils {
	public final static class UmengOmnitureInfo {
		public final static boolean isOpen = true;

		public static String getUmengChannel(Context context) {
			String UMENG_CHANNEL = "";
			try {
				ApplicationInfo ai = context.getPackageManager()
						.getApplicationInfo(context.getPackageName(),
								PackageManager.GET_META_DATA);
				UMENG_CHANNEL = ai.metaData.get("UMENG_CHANNEL").toString();
				Log.i("UMENG_CHANNEL", UMENG_CHANNEL);

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return UMENG_CHANNEL;
		}
	}

	/** 获得当前的app版本 **/
	public static String getVersion(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getRealPhone(Context context) {
		// 与手机建立连接
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getLine1Number();
		return phoneId;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getAndroidDeviceID(Context context) {
		String deviceID = null, tmDevice = null, tmSerial = null;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// tmDevice = tm.getDeviceId();
		// tmSerial = "" + tm.getSimSerialNumber();
		deviceID = ""
				+ Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
		// UUID deviceUuid = new UUID(deviceID.hashCode(),
		// tmDevice.hashCode() << 32 | tmSerial.hashCode());
		// deviceID = deviceUuid.toString();
		return deviceID;
	}
}
