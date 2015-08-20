package com.ef.bite.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.apptentive.android.sdk.Log;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apptentive.android.sdk.Apptentive;
import com.ef.bite.AppSession;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.TutorialConfigBiz;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.task.GetProfileTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.DashboardCache;
import com.ef.bite.dataacces.ProfileCache;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.dataacces.mode.PushData;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.model.TutorialConfig;
import com.ef.bite.ui.main.MainActivity;
import com.ef.bite.ui.user.EFLoginWelcomeActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.StringUtils;
import org.json.JSONObject;

import java.io.Serializable;

public class BaseActivity extends FragmentActivity {
	protected Context mContext;
	protected ProfileCache profileCache;
	protected DashboardCache dashboardCache;
	protected ProgressDialog mProgress;
	protected UserScoreBiz userScoreBiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		profileCache = new ProfileCache(mContext);
		dashboardCache = DashboardCache.getInstance();
		userScoreBiz = new UserScoreBiz(mContext);
		// if(!this.getClass().getName().equals(MainActivity.class.getName()))
		AppSession.getInstance().addActivity(this);
		MobclickTracking.OmnitureTrack.CreateContext(mContext);
	}

	// @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickTracking.OmnitureTrack.ResumeCollectingLifecycleData();
//		MobclickTracking.UmengTrack.setResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickTracking.OmnitureTrack.PauseCollectingLifecycleData();
//		MobclickTracking.UmengTrack.setPause(mContext);
//		Apsalar.unregisterApsalarReceiver();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Apptentive.onStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Apptentive.onStop(this);
	}

	protected void mPageIndicat(LinearLayout layout, int index, int total) {
		// TODO Auto-generated method stub
		if (index > total) {
			return;
		}
		for (int i = 1; i <= total; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(60, 60));
			if (i == 1) {
				imageView.setImageResource(R.drawable.progress_begin);
			} else if (i == total) {
				if (index == total) {
					imageView.setImageResource(R.drawable.progress_end_event);
				} else {
					imageView.setImageResource(R.drawable.progress_end_default);
				}
			} else {
				imageView.setImageResource(R.drawable.progress_ing_default);
				if (i <= index) {
					imageView.setImageResource(R.drawable.progress_ing_event);
				}
			}
			layout.setGravity(Gravity.CENTER);
			layout.addView(imageView);
		}
	}

	protected void BI_Tracking(int i) {

	}

	// Created by Yang
	protected Serializable getSerializableExtra(String value) {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return null;
		}
		return bundle.getSerializable(value);
	}

    // Created by Yang
    protected String getStringExtra(String value) {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return StringUtils.EMPTY;
        }
        return bundle.getString(value);
    }

	// Created by Yang
	protected boolean getBooleanExtra(String value) {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return false;
		}
		return bundle.getBoolean(value);
	}

	// Created by Yang
	protected void toastOnUI(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toast(message);
			}
		});
	}

	// Created by Yang
	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获得用户的profile
	 */
	protected void getUserProfile() {
		Activity a = this;
		while(a.getParent()!= null) {
			a = a.getParent();
		}
		mProgress = new ProgressDialog(a);
		mProgress.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(mContext, "loging_getting_profile"));
		mProgress.show();
		GetProfileTask profileTask = new GetProfileTask(mContext,
				new PostExecuting<HttpProfile>() {
					@Override
					public void executing(HttpProfile result) {
						mProgress.dismiss();
						if (result != null && result.status != null
								&& result.status.equals("0")
								&& result.data != null) {
							profileCache.setUserProfile(result);

							startActivity(new Intent(mContext, MainActivity.class).putExtra("com.parse.Data", getStringExtra("com.parse.Data")));
							finish();

						} else {
							toast("Getting profile failed");
							Intent intent = new Intent(mContext,
									EFLoginWelcomeActivity.class);
							startActivityForResult(intent, AppConst.RequestCode.EF_LOGIN);
							finish();
						}
					}
				});
		profileTask.execute(new String[]{AppConst.CurrUserInfo.UserId});
	}

	/**
	 * Get push data from intent extra
	 *
	 * @return
	 */
	public PushData getPushData() {
		try {
			PushData pushData = new PushData();
			JSONObject jsonObject = new JSONObject(
					getStringExtra("com.parse.Data"));
			//test json
//			JSONObject jsonObject = new JSONObject(
//					"{\"alert\": \"hello world\",\"alert_type\":\"new_lesson\",\"course_id\": \"9272bfef-36e9-411d-b120-398761304e12\"}");
			pushData.setType(PushData.Type.toType(jsonObject
					.optString("alert_type")));
			pushData.setCourse_id(jsonObject.optString("course_id"));
			getIntent().removeExtra("com.parse.Data");
			return pushData;
		} catch (Exception e) {
			return null;
		}
	}
}
