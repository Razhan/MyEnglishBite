package com.ef.bite.ui.user;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.preview.PreviewListActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

public class VersionAboutActivity extends BaseActivity {

	ImageButton mGoback;
	TextView mVersionTitle;
	TextView mVersionName;
	ImageView mAppNameImage;
	TextView mAppInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_about);
		mGoback = (ImageButton) findViewById(R.id.family_name);
		mAppNameImage = (ImageView) findViewById(R.id.ef_welcome_app_name);
		mAppInfo = (TextView) findViewById(R.id.ef_welcome_app_info);
		mAppInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"login_main_5_min_day"));
		mVersionTitle = (TextView) findViewById(R.id.version_about_version_title);
		mVersionTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "settings_android_version"));
		mVersionName = (TextView) findViewById(R.id.version_about_versionname);
		mVersionName.setText(getVersionName() + " Beta");
		// translation
		if (AppLanguageHelper.getSystemLaunguage(mContext).equals(
				AppLanguageHelper.ZH_CN)) { // 中文
			mAppNameImage.setImageResource(R.drawable.ef_welcome_app_name_zh);
			mAppInfo.setTypeface(null, Typeface.BOLD);
			mAppInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.welcome_app_info_zh_size));
		} else if (AppLanguageHelper.getSystemLaunguage(mContext).equals(
				AppLanguageHelper.FR)) {
			mAppNameImage.setImageResource(R.drawable.ef_welcome_app_name_fr);
			mAppInfo.setTypeface(null, Typeface.NORMAL);
			mAppInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.welcome_app_info_en_size));
		} else { // 英文
			mAppNameImage.setImageResource(R.drawable.ef_welcome_app_name);
			mAppInfo.setTypeface(null, Typeface.NORMAL);
			mAppInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.welcome_app_info_en_size));
		}
		mAppNameImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clicksCounter();
			}
		});

		FontHelper.applyFont(mContext, mAppInfo, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mVersionTitle, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mVersionName, FontHelper.FONT_Museo300);
		mGoback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.activity_in_from_left,
						R.anim.activity_out_to_right);
			}
		});

		// add omniture
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.AboutEnglishBiteValues.pageNameValue,
				ContextDataMode.AboutEnglishBiteValues.pageSiteSubSectionValue,
				ContextDataMode.AboutEnglishBiteValues.pageSiteSectionValue,
				mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.AboutEnglishBiteValues.pageNameValue,
//				ContextDataMode.AboutEnglishBiteValues.pageSiteSubSectionValue,
//				ContextDataMode.AboutEnglishBiteValues.pageSiteSectionValue,
//				mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.AboutEnglishBiteValues.pageNameValue,
//				ContextDataMode.AboutEnglishBiteValues.pageSiteSubSectionValue,
//				ContextDataMode.AboutEnglishBiteValues.pageSiteSectionValue,
//				mContext);
	}

	/**
	 * @return
	 */
	private String getVersionName() {
		try {
			return this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	private int count = 0;
	private long firstTime = 0;
	private long interval = 500;// 连击的时间间隔

	// 计数器
	private void clicksCounter() {
		long secondTime = System.currentTimeMillis();
		// 判断每次点击的事件间隔是否符合连击的有效范围
		// 不符合时，有可能是连击的开始，否则就仅仅是单击
		if (secondTime - firstTime <= interval) {
			++count;
			if (count == 3) {
				showPreview();
			}
		} else {
			count = 1;
		}
		firstTime = secondTime;
	}

	private void showPreview() {
		startActivity(new Intent(this, PreviewListActivity.class));
	}
}
