package com.ef.bite.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.ef.bite.ui.main.MainActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by yang on 15/4/15.
 */
public class ParsePushReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return MainActivity.class;
    }
}
