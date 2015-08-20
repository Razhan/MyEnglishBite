package com.ef.bite.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {

	public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Request the notification manager
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = intent.getParcelableExtra(NOTIFICATION);
	    int id = intent.getIntExtra(NOTIFICATION_ID, 0);
	    // 如果该通知不支持App运行时候，并且App没有运行，则发送通知
	    notificationManager.notify(id, notification);
	}
	
	

}
