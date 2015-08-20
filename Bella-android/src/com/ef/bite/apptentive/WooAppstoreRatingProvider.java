package com.ef.bite.apptentive;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.apptentive.android.sdk.module.rating.IRatingProvider;
import com.apptentive.android.sdk.module.rating.InsufficientRatingArgumentsException;

public class WooAppstoreRatingProvider implements IRatingProvider {

	@Override
	public void startRating(Context context, Map<String, String> args)
			throws InsufficientRatingArgumentsException {
		if (!args.containsKey("package")) {
			throw new InsufficientRatingArgumentsException(
					"Missing required argument 'package'");
		}
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + context.getPackageName()));
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public String activityNotFoundMessage(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
