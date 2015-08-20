package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import com.ef.bite.R;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.PasswordChangeTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.LoginInputLayout;

public class EFChangePWDActivity extends BaseActivity {

	ActionbarLayout mActionbar;
	LoginInputLayout mOldPWD;
	LoginInputLayout mNewPWD;
	LoginInputLayout mConfirmNewPWD;
	Button mChangeBtn;
	ProgressDialog mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_efchange_pwd);
		mActionbar = (ActionbarLayout) findViewById(R.id.change_pwd_actionbar);
		mOldPWD = (LoginInputLayout) findViewById(R.id.change_pwd_old_pwd);
		mNewPWD = (LoginInputLayout) findViewById(R.id.change_pwd_new_pwd);
		mConfirmNewPWD = (LoginInputLayout) findViewById(R.id.change_pwd_confirm_new_pwd);
		mChangeBtn = (Button) findViewById(R.id.change_pwd_change);
		mChangeBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "change_pwd_ef_change_button"));
		initViews();
		// add Omniture
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.SettingsPasswordValues.pageNameValue,
				ContextDataMode.SettingsPasswordValues.pageSiteSubSectionValue,
				ContextDataMode.SettingsPasswordValues.pageSiteSectionValue,
				mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.SettingsPasswordValues.pageNameValue,
//				ContextDataMode.SettingsPasswordValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsPasswordValues.pageSiteSectionValue,
//				mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.SettingsPasswordValues.pageNameValue,
//				ContextDataMode.SettingsPasswordValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsPasswordValues.pageSiteSectionValue,
//				mContext);
	}

	private void initViews() {
		mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "change_pwd_ef_title"),
				R.drawable.arrow_goback_black, 0, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.activity_in_from_left,
								R.anim.activity_out_to_right);
					}
				}, null);
		mOldPWD.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "change_pwd_ef_old_pwd_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, true);
		mNewPWD.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "change_pwd_ef_new_pwd_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mConfirmNewPWD.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "change_pwd_ef_confirm_new_pwd_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mChangeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllErrors();
				String oldPWD = mOldPWD.getInputString();
				String newPWD = mNewPWD.getInputString();
				String confirmPWD = mConfirmNewPWD.getInputString();
				attempChangePWD(oldPWD, newPWD, confirmPWD);
			}
		});
	}

	/**
	 * Change Password
	 * 
	 * @param oldPWD
	 * @param newPWD
	 * @param confirmNewPWD
	 */
	private void attempChangePWD(String oldPWD, String newPWD,
			String confirmNewPWD) {
		if (oldPWD == null || oldPWD.isEmpty())
			mOldPWD.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "change_pwd_ef_error_no_pwd"));
		else if (newPWD == null || newPWD.isEmpty())
			mNewPWD.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "change_pwd_ef_error_no_pwd"));
		else if (confirmNewPWD == null || confirmNewPWD.isEmpty())
			mConfirmNewPWD.setError(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"change_pwd_ef_error_no_pwd"));
		else if (!newPWD.equals(confirmNewPWD))
			mNewPWD.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "change_pwd_ef_error_pwd_not_match"));
		else {
			mProgress = new ProgressDialog(this);
			mProgress.setTitle(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "change_pwd_ef_changing_pwd"));
			PasswordChangeTask changePWDTask = new PasswordChangeTask(this,
					new PostExecuting<HttpBaseMessage>() {
						@Override
						public void executing(HttpBaseMessage result) {
							mProgress.dismiss();
							if (result != null && result.status != null) {
								if (result.status.equals("0")) {
									Toast.makeText(
											mContext,
											JsonSerializeHelper
													.JsonLanguageDeserialize(
															mContext,
															"change_pwd_ef_success"),
											Toast.LENGTH_SHORT).show();
									finish();
									overridePendingTransition(
											R.anim.activity_in_from_left,
											R.anim.activity_out_to_right);
								} else {
									mOldPWD.setError(result.message);
									Toast.makeText(
											mContext,
											JsonSerializeHelper
													.JsonLanguageDeserialize(
															mContext,
															"fail_to_get_result"),
											Toast.LENGTH_SHORT).show();
								}
							} else
								Toast.makeText(
										mContext,
										JsonSerializeHelper
												.JsonLanguageDeserialize(
														mContext,
														"fail_to_get_result"),
										Toast.LENGTH_SHORT).show();
						}
					});
			changePWDTask.execute(new String[] { AppConst.CurrUserInfo.UserId,
					oldPWD, newPWD });
		}
	}

	/** 清理错误 **/
	private void clearAllErrors() {
		mOldPWD.cleanError();
		mNewPWD.cleanError();
		mConfirmNewPWD.cleanError();
	}
}
