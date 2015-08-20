package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.PasswordForgetTask;
import com.ef.bite.business.task.PasswordResetTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.LoginInputLayout;

public class EFFindPWDActivity extends BaseActivity {
	private ActionbarLayout mActionbar;
	private LoginInputLayout mPhone;
	private Button mGetCodeBtn;
	private LoginInputLayout mVerifyCode;
	private LoginInputLayout mResetPWD;
	private LoginInputLayout mConfirmPWD;
	private Button mResetBtn;
	private LinearLayout mFirstLayout;
	private LinearLayout mSecondLayout;
	private ProgressDialog mProgress;
	private TextView textView2;
	private final static int ForgotPassword = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ef_find_pwd);
		mActionbar = (ActionbarLayout) findViewById(R.id.login_ef_actionbar);
		mPhone = (LoginInputLayout) findViewById(R.id.login_ef_findpwd_phone);
		mGetCodeBtn = (Button) findViewById(R.id.login_ef_findpwd_getcode);
		mGetCodeBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "foget_pwd_ef_get_verifycode_btn"));
		mVerifyCode = (LoginInputLayout) findViewById(R.id.login_ef_findpwd_verify_code);
		mResetPWD = (LoginInputLayout) findViewById(R.id.login_ef_findpwd_reset_pwd);
		mConfirmPWD = (LoginInputLayout) findViewById(R.id.login_ef_findpwd_confirm_pwd);
		mResetBtn = (Button) findViewById(R.id.login_ef_findpwd_submit);
		mFirstLayout = (LinearLayout) findViewById(R.id.login_ef_findpwd_first_layout);
		mSecondLayout = (LinearLayout) findViewById(R.id.login_ef_findpwd_second_layout);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView2.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"foget_pwd_ef_enter_phone_to_get_code"));
		// Actionbar初始化
		mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "foget_pwd_ef_title"), R.drawable.arrow_goback_black,
				-1, new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.activity_in_from_left,
								R.anim.activity_out_to_right);
					}
				}, null);
		mPhone.initialize(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"foget_pwd_ef_phone_hint"), InputType.TYPE_CLASS_PHONE, true);
		mVerifyCode.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "foget_pwd_ef_verify_code_hint"),
				InputType.TYPE_CLASS_TEXT, false);
		mResetPWD.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "foget_pwd_ef_reset_pwd_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mConfirmPWD.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "foget_pwd_ef_confirm_pwd_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mGetCodeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllErrors();
				String phone = mPhone.getInputString();
				attempGetVerifyCode(phone);
			}
		});
		mResetBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllErrors();
				String code = mVerifyCode.getInputString();
				String pwd = mResetPWD.getInputString();
				String confirm_pwd = mConfirmPWD.getInputString();
				attempResetPWD(code, pwd, confirm_pwd);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Umeng
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.RegisterNameValues.pageNameValue,
//				ContextDataMode.RegisterNameValues.pageSiteSubSectionValue,
//				ContextDataMode.RegisterNameValues.pageSiteSectionValue,
//				mContext);
		BI_Tracking(ForgotPassword);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.RegisterNameValues.pageNameValue,
//				ContextDataMode.RegisterNameValues.pageSiteSubSectionValue,
//				ContextDataMode.RegisterNameValues.pageSiteSectionValue,
//				mContext);
	}

	/**
	 * 获得短信验证码
	 * 
	 * @param phone
	 */
	private void attempGetVerifyCode(String phone) {
		if (phone == null || phone.isEmpty())
			mPhone.setError(getString(R.string.foget_pwd_ef_phone_null));
		else {
			mProgress = new ProgressDialog(this);
			mProgress
					.setMessage(getString(R.string.foget_pwd_ef_progress_get_verify_code));
			mProgress.show();
			PasswordForgetTask fogetTask = new PasswordForgetTask(mContext,
					new PostExecuting<HttpBaseMessage>() {
						@Override
						public void executing(HttpBaseMessage result) {
							mProgress.dismiss();
							if (result != null && result.status != null) {
								if (result.status.equals("0")) {
									Toast.makeText(
											mContext,
											getString(R.string.foget_pwd_ef_get_verify_code_success),
											Toast.LENGTH_SHORT).show();
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											showNextStep();
										}
									}, 200);
								} else
									mPhone.setError(result.message);
							} else
								Toast.makeText(mContext,
										getString(R.string.fail_to_get_result),
										Toast.LENGTH_SHORT).show();
						}
					});
			fogetTask.execute(new String[] { phone });
		}
	}

	/**
	 * 显示下一步
	 */
	private void showNextStep() {
		Animation fadeout = AnimationUtils.loadAnimation(mContext,
				R.anim.activity_out_to_left);
		fadeout.setDuration(200);
		fadeout.setFillAfter(true);
		Animation fadein = AnimationUtils.loadAnimation(mContext,
				R.anim.activity_in_from_right);
		fadein.setDuration(200);
		fadein.setFillAfter(true);
		fadein.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mFirstLayout.setVisibility(View.VISIBLE);
				mSecondLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mFirstLayout.setVisibility(View.GONE);
				mSecondLayout.setVisibility(View.VISIBLE);
			}
		});
		mFirstLayout.startAnimation(fadeout);
		mSecondLayout.startAnimation(fadein);
	}

	/**
	 * 通过短信验证码重置密码
	 * 
	 * @param code
	 * @param password
	 */
	private void attempResetPWD(String code, String password, String confirm_pwd) {
		if (code == null || code.isEmpty())
			mVerifyCode
					.setError(getString(R.string.foget_pwd_ef_verify_code_null));
		else if (password == null || password.isEmpty())
			mResetPWD.setError(getString(R.string.foget_pwd_ef_reset_pwd_null));
		else if (confirm_pwd == null || confirm_pwd.isEmpty())
			mConfirmPWD
					.setError(getString(R.string.foget_pwd_ef_confirm_pwd_null));
		else if (!password.equals(confirm_pwd)) {
			mConfirmPWD
					.setError(getString(R.string.foget_pwd_ef_password_not_match));
		} else {
			mProgress = new ProgressDialog(this);
			mProgress
					.setMessage(getString(R.string.foget_pwd_ef_progress_reset_password));
			mProgress.show();
			PasswordResetTask resetTask = new PasswordResetTask(mContext,
					new PostExecuting<HttpBaseMessage>() {
						@Override
						public void executing(HttpBaseMessage result) {
							mProgress.dismiss();
							if (result != null && result.status != null) {
								if (result.status.equals("0")) {
									Toast.makeText(
											mContext,
											getString(R.string.foget_pwd_ef_progress_reset_pwd_success),
											Toast.LENGTH_SHORT).show();
									finish();
									overridePendingTransition(
											R.anim.activity_in_from_left,
											R.anim.activity_out_to_right);
								} else
									mVerifyCode.setError(result.message);
							} else
								Toast.makeText(mContext,
										getString(R.string.fail_to_get_result),
										Toast.LENGTH_SHORT).show();
						}
					});
			resetTask.execute(new String[] { code, password });
		}
	}

	/** 清理所有的错误 **/
	private void clearAllErrors() {
		mPhone.cleanError();
		mVerifyCode.cleanError();
		mResetPWD.cleanError();
		mConfirmPWD.cleanError();
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.LoginValues.pageNameValue_password,
					ContextDataMode.LoginValues.pageSiteSubSectionValue,
					ContextDataMode.LoginValues.pageSiteSectionValue, mContext);
			break;
		}
	}
}
