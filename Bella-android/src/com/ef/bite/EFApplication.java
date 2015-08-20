package com.ef.bite;

import android.app.Application;

import android.os.Handler;
import android.util.Log;
import com.adobe.mobile.Config;
//import com.apsalar.sdk.Apsalar;
import com.crittercism.app.Crittercism;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.dataacces.AchievementCache;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.DashboardCache;
import com.ef.bite.model.SMSRecord;
import com.ef.bite.model.ServerErrorLog;
import com.ef.bite.utils.AppUtils;
import com.parse.*;

public class EFApplication extends Application {
	//Live push key
	private final static String APP_ID = "1Io6EnfkDiIEBpSQSzrgJjnQX9NgB6wLQxAhuBQm";
	private final static String CLIENT_ID = "J2s697NE70y2jY5Obt1cJefvBE6n6wmxOyaN4f2i";
	//Dev
//	private final static String APP_ID = "oripCYXbBQNGR42KSwYIp3BkmcNzsWn9uSKQ1HEv";
//	private final static String CLIENT_ID = "BHXcbWYYL4EjAScqtWLzU7U7dISYmjhux0XoPDTe";

	private static EFApplication mApp;

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
//			ApsalarSend();
			handler.removeCallbacks(runnable);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			// Initial Crittercism (Bug tracker)
			Crittercism.initialize(getApplicationContext(),
					"550a89c7e0697fa449637893");
			// Enable Local Datastore.
			Parse.enableLocalDatastore(this);
			// Initialize the Parse SDK.
            ParseObject.registerSubclass(SMSRecord.class);
            ParseObject.registerSubclass(ServerErrorLog.class);
            Parse.initialize(this, APP_ID, CLIENT_ID);
			ParseUser.enableAutomaticUser();
			ParsePush.subscribeInBackground("", new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Log.d("com.parse.push",
								"successfully subscribed to the broadcast channel.");
					} else {
						Log.e("com.parse.push", "failed to subscribe for push",
								e);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (AppUtils.UmengOmnitureInfo.isOpen) {
//			MobclickTracking.UmengTrack.openDurationTrack(false, getApplicationContext(), true);
			AppConst.HeaderStore.StoreName = AppUtils.UmengOmnitureInfo
					.getUmengChannel(getApplicationContext());
		}

		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
		mApp = new EFApplication();

		MobclickTracking.OmnitureTrack.CreateContext(this
				.getApplicationContext());
		Config.setDebugLogging(true);
		initChunksLoader();
		initAchievementCache();
		initDashboardCache();
	}

	/**
	 * ChunksLoader initialization Created by Yang.
	 */

	private void initChunksLoader() {
		ChunksHolder chunksLoader = ChunksHolder.getInstance();
		chunksLoader.init(this);
	}

	private void initAchievementCache() {
		AchievementCache cache = AchievementCache.getInstance();
		cache.init(this);
	}

	private void initDashboardCache() {
		DashboardCache cache = DashboardCache.getInstance();
		cache.init(this);
	}

//	private void ApsalarSend() {
//		Apsalar.setFBAppId(AppConst.ThirdPart.Facebook_Login_Appkey);
//		Apsalar.startSession(getApplicationContext(),
//				AppConst.ThirdPart.Apsalar_Api_Key,
//				AppConst.ThirdPart.Apsalar_Secret);
//	}

	public static EFApplication getInstance() {
		return mApp;
	}
}
