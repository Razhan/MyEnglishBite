package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cn.trinea.android.common.util.ToastUtils;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.EFRegisterTask;
import com.ef.bite.business.task.LoginTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.LoginMode;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpLogin;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.popup.TermsServicePopupWindow;
import com.ef.bite.utils.*;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.LoginInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EFRegisterActivity extends BaseActivity {
	public static int MAX_LOGIN_TIMES = 5;
	ActionbarLayout mActionbar;
	LinearLayout mEnterNameLayout; // first name, last name
	LinearLayout mEnterPhoneLayout; // phone，password
	LoginInputLayout mFirstNameInput; // first name
	LoginInputLayout mLastNameInput; // last name
	Button mNextBtn;
	LoginInputLayout mPhoneInput; // phone
	LoginInputLayout mPWDInput; // password
	LoginInputLayout mConfirmPWDInput; // confirm password
	LinearLayout mTermsLayout;
	LinearLayout mIsCallLayout;
	CheckBox mTermsCheckbox;
	CheckBox mIsCallCheckbox;
	TextView mTermsText;
	TextView register_is_call_allowed_text;
	Button mBackBtn;
	Button mRegisterBtn;
	// 文字说明
	TextView mNameInfo;
	TextView mPhoneInfo;
	ProgressDialog mProgress;
	int curLoginTimes = 0; // 登录后直接login，最多尝试 MAX_LOGIN_TIMES 次
	boolean isTermsChecked = false; // 服务条款是否勾上了
	private List<HttpCourseRequest> httpCourseRequests = new ArrayList<HttpCourseRequest>();

	private final static int Enrollment_Name = 1;
	private final static int Enrollment_AccountPhone = 2;
	private final static int Enrollment_TermsAndConditions = 3;
	private Spinner spinner;
	private String mindexAge;
	private int mPositionAge;

    int step = 1;
    TermsServicePopupWindow popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ef_register);
		// Tracking loading
		// TraceHelper.tracingPage(mContext, TraceHelper.PAGE_REGISTER);

		BI_Tracking(Enrollment_Name);
		spinner = (Spinner) findViewById(R.id.register_age_spinner);
		mActionbar = (ActionbarLayout) findViewById(R.id.register_ef_actionbar);
		mEnterNameLayout = (LinearLayout) findViewById(R.id.register_ef_name_layout);
		mEnterPhoneLayout = (LinearLayout) findViewById(R.id.register_ef_phone_layout);
		mFirstNameInput = (LoginInputLayout) findViewById(R.id.register_ef_firstname);
		mLastNameInput = (LoginInputLayout) findViewById(R.id.register_ef_lastname);
		mPhoneInput = (LoginInputLayout) findViewById(R.id.register_ef_phone);
		mPWDInput = (LoginInputLayout) findViewById(R.id.register_ef_password);
		mConfirmPWDInput = (LoginInputLayout) findViewById(R.id.register_ef_password_confirm);
		mPhoneInput.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_phone"), InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER, true);
		mPWDInput.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "login_ef_password_hint"), InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mConfirmPWDInput.initialize(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext,
						"register_ef_confirm_password_hint"),
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
		mFirstNameInput.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_first_name"), InputType.TYPE_CLASS_TEXT,
				true);
		mLastNameInput.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_last_name"), InputType.TYPE_CLASS_TEXT,
				false);
		mTermsLayout = (LinearLayout) findViewById(R.id.register_terms_service_layout);
		mTermsCheckbox = (CheckBox) findViewById(R.id.register_terms_service_checkbox);
		mIsCallLayout = (LinearLayout) findViewById(R.id.register_is_call_allowed_layout);
		mIsCallCheckbox = (CheckBox) findViewById(R.id.register_is_call_allowed_checkbox);
		mTermsText = (TextView) findViewById(R.id.register_terms_service_text);
		register_is_call_allowed_text = (TextView) findViewById(R.id.register_is_call_allowed_text);
		register_is_call_allowed_text.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "register_ef_accept_call"));
		mNextBtn = (Button) findViewById(R.id.register_ef_btn_next);
		mNextBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"register_ef_next"));
		mBackBtn = (Button) findViewById(R.id.register_ef_btn_back);
		mBackBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"register_ef_back"));
		mRegisterBtn = (Button) findViewById(R.id.register_ef_btn_register);
		mRegisterBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_sign_up_button"));
		mNameInfo = (TextView) findViewById(R.id.register_ef_name_text_info);
		mNameInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"register_ef_enter_your_name"));
		mPhoneInfo = (TextView) findViewById(R.id.register_ef_phone_text_info);
		mPhoneInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_use_phone"));
		// Font setting
		FontHelper.applyFont(mContext, mNameInfo, FontHelper.FONT_OpenSans);
		FontHelper.applyFont(mContext, mPhoneInfo, FontHelper.FONT_OpenSans);
		// 初始化Actionbar
		mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_register_title"),
				R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (step == 2) {
							attempBack();
						} else {
							finish();
							overridePendingTransition(R.anim.activity_in_from_left,
									R.anim.activity_out_to_right);
						}
					}
				}, null);
		// 下一步设置手机号和密码
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllErrors();
				String firstname = mFirstNameInput.getInputString();
				String lastname = mLastNameInput.getInputString();
				if (mPositionAge != 0) {
					attempNext(firstname, lastname);
				} else {
					ToastUtils.show(mContext, JsonSerializeHelper
							.JsonLanguageDeserialize(mContext,
									"register_ef_empty_field"));
				}

				BI_Tracking(Enrollment_AccountPhone);
			}
		});

		mTermsLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkTermService();
			}
		});
		// 返回上一步
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attempBack();
			}
		});
		// 注册
		mRegisterBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllErrors();
				String phone = mPhoneInput.getInputString();
				String pwd = mPWDInput.getInputString();
				String confirmPWD = mConfirmPWDInput.getInputString();
				String firstname = mFirstNameInput.getInputString();
				String lastname = mLastNameInput.getInputString();
				if (!mTermsCheckbox.isChecked()) {
					Toast.makeText(mContext,
							getString(R.string.register_ef_not_accept_terms),
							Toast.LENGTH_LONG).show();
					return;
				}

				if(!NetworkChecker.isConnected(mContext)){
					toast(JsonSerializeHelper.JsonLanguageDeserialize(
							mContext, "error_check_network_available"));
					return;
				}

				attempRegister(phone, pwd, confirmPWD, firstname, lastname,
						mIsCallCheckbox.isChecked(),
						AppUtils.getRealPhone(mContext), mindexAge);
				// Tracking event
				// TraceHelper.tracingAction(mContext,
				// TraceHelper.PAGE_REGISTER,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_REGISTER);
			}
		});
		mTermsText.setText(HighLightStringHelper.getBoldString(mContext,
				JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"register_ef_accept_terms")));

		List<String> spinnerList = new ArrayList<String>();
		spinnerList.add(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"register_ef_age_group"));
		spinnerList.add("1-10");
		spinnerList.add("10-13");
		spinnerList.add("14-18");
		spinnerList.add("19-22");
		spinnerList.add("23-26");
		spinnerList.add("27-35");
		spinnerList.add("36-40");
		spinnerList.add("41-50");
		spinnerList.add("50+");

		// final ArrayAdapter<CharSequence> adapter = ArrayAdapter
		// .createFromResource(this, R.array.array_spinner_registerage,
		// android.R.layout.simple_spinner_item);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		/* set方法是来设置spinner中每个条目的样式 */
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mindexAge = adapter.getItem(position).toString();
				mPositionAge = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConst.RequestCode.TERMS_CONDITION) {
			if (resultCode == AppConst.ResultCode.TERMS_ACCEPT)
				mTermsCheckbox.setChecked(true);
			else if (resultCode == AppConst.ResultCode.TERMS_NOT_ACCEPT)
				mTermsCheckbox.setChecked(false);
		}
	}

	/** 点击服务条款 **/
	private void checkTermService() {
		// Intent termsIntent = new Intent(mContext,
		// TermConditionActivity.class);
		// termsIntent.putExtra(AppConst.BundleKeys.Terms_Btnbar_Show, true);
		// startActivityForResult(termsIntent,
		// AppConst.RequestCode.TERMS_CONDITION);
		popup = new TermsServicePopupWindow(this,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mTermsCheckbox.setChecked(true);
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mTermsCheckbox.setChecked(false);
					}
				});
		popup.open();
		SoftInputHelper.hideTemporarily(this);
		BI_Tracking(Enrollment_TermsAndConditions);
	}

	/** 下一步 **/
	private void attempNext(String firstname, String lastname) {
		if (firstname == null || firstname.isEmpty()) {
			mFirstNameInput.setError(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"regisetr_ef_error_firstname_null"));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(JsonSerializeHelper
							.JsonLanguageDeserialize(mContext,
									"regisetr_ef_error_firstname_null"));
			return;
		}
		if (lastname == null || lastname.isEmpty()) {
			mLastNameInput.setError(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"register_ef_error_lastname_null"));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(JsonSerializeHelper
							.JsonLanguageDeserialize(mContext,
									"register_ef_error_lastname_null"));
			return;
		}
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
                mEnterPhoneLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mEnterNameLayout.setVisibility(View.GONE);
            }
        });
		mEnterNameLayout.startAnimation(fadeout);
		mEnterPhoneLayout.startAnimation(fadein);
        step++;

	}

	/** 上一步 **/
	private void attempBack() {
		BI_Tracking(Enrollment_Name);
		Animation fadeout = AnimationUtils.loadAnimation(mContext,
				R.anim.activity_out_to_right);
		fadeout.setDuration(200);
		fadeout.setFillAfter(true);
		Animation fadein = AnimationUtils.loadAnimation(mContext,
				R.anim.activity_in_from_left);
		fadein.setDuration(200);
		fadein.setFillAfter(true);
		fadein.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mEnterPhoneLayout.setVisibility(View.GONE);
				mEnterNameLayout.setVisibility(View.VISIBLE);
			}
		});
		mEnterPhoneLayout.startAnimation(fadeout);
		mEnterNameLayout.startAnimation(fadein);
        step--;
	}

	@Override
	public void onBackPressed() {
        if (step == 2) {
            attempBack();
        } else {
            super.onBackPressed();
        }
    }

	/** 注册 **/
	private void attempRegister(final String phone, final String password,
			String confirmPWD, String firstname, String lastname,
			boolean iscall, String real_phone, String indexage) {
		// 判断输入
		if (phone == null || phone.isEmpty()) {
			mPhoneInput.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "login_error_username_empty"));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(JsonSerializeHelper
							.JsonLanguageDeserialize(mContext,
									"register_ef_error_username_null"));
			return;
		}

		if (phone.length() < 10) {
			mPhoneInput.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "regisetr_ef_error_username_length"));
		}

		if (!isNumeric(phone)) {
			mPhoneInput.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "regisetr_ef_error_username_invalid"));
		}

		if (password == null || password.isEmpty()) {
			mPWDInput.setError(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "register_ef_error_password_null"));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(JsonSerializeHelper
							.JsonLanguageDeserialize(mContext,
									"register_ef_error_password_null"));
			return;
		}
		if (confirmPWD == null || confirmPWD.isEmpty()) {
			mConfirmPWDInput
					.setError(getString(R.string.register_ef_error_confirm_password_null));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(getString(R.string.register_ef_error_confirm_password_null));
			return;
		}
		if (!password.equals(confirmPWD)) {
			mConfirmPWDInput
					.setError(getString(R.string.register_ef_error_password_not_match));
			MobclickTracking.OmnitureTrack
					.actionFormErrorType(getString(R.string.register_ef_error_password_not_match));
			return;
		}
		SoftInputHelper.hideTemporarily(EFRegisterActivity.this);
		mProgress = new ProgressDialog(EFRegisterActivity.this);
		mProgress.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "register_ef_registering"));
		mProgress.show();
		EFRegisterTask registerTask = new EFRegisterTask(mContext,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
						mProgress.dismiss();
						if (result != null && result.status != null) {
							if (result.status.equals("0")) {
								mProgress.setMessage(JsonSerializeHelper
										.JsonLanguageDeserialize(mContext,
												"register_ef_success")
										+ " "
										+ JsonSerializeHelper
												.JsonLanguageDeserialize(
														mContext, "loging"));
								login(phone, password);
								MobclickTracking.OmnitureTrack
										.ActionRegisterSuccessful(ContextDataMode.ActionRegisterTypeValues.PHONE);
							} else {
								mPhoneInput.setError(result.message);
								MobclickTracking.OmnitureTrack
										.actionFormErrorType(result.message);
								Toast.makeText(mContext, result.message,
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(mContext,
									getString(R.string.fail_to_get_result),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		registerTask.execute(new Object[]{phone, password, firstname,
				lastname, iscall, real_phone, indexage});
	}

	/**
	 * 直接登陆
	 * 
	 * @param phone
	 * @param password
	 */
	private void login(final String phone, final String password) {
		LoginTask loginTask = new LoginTask(mContext,
				new PostExecuting<HttpLogin>() {
					@Override
					public void executing(HttpLogin result) {
						mProgress.dismiss();
						if (result != null && result.status != null
								&& result.status.equals("0")) {
							Toast.makeText(mContext, JsonSerializeHelper.JsonLanguageDeserialize(mContext,"register_ef_success"), Toast.LENGTH_SHORT).show();
							//Marked as logined
							AppConst.CurrUserInfo.IsLogin = true;
							AppConst.CurrUserInfo.UserId=result.data.bella_id;
							getUserProfile();
						} else {
								Toast.makeText(
										mContext,
										"Register is failed"
												+ " " + result.message,
										Toast.LENGTH_SHORT).show();
								MobclickTracking.OmnitureTrack
										.actionFormErrorType(getString(R.string.register_ef_success)
												+ " " + result.message);
							}
						}
				});
		LoginMode loginModel = new LoginMode();
		loginModel.username = phone;
		loginModel.password = password;
		loginModel.login_type = LoginTask.LOGIN_TYPE_ETOWN;
		loginModel.provider_type = LoginTask.LOGIN_PROVIDER_ETOWN;
		loginTask.execute(new LoginMode[]{loginModel});
	}

	/** 清理所有的错误信息 **/
	private void clearAllErrors() {
		mFirstNameInput.cleanError();
		mLastNameInput.cleanError();
		mPhoneInput.cleanError();
		mPWDInput.cleanError();
		mConfirmPWDInput.cleanError();
	}

	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.RegisterNameValues.pageNameValue,
					ContextDataMode.RegisterNameValues.pageSiteSubSectionValue,
					ContextDataMode.RegisterNameValues.pageSiteSectionValue,
					mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.RegisterPhoneValues.pageNameValue,
							ContextDataMode.RegisterPhoneValues.pageSiteSubSectionValue,
							ContextDataMode.RegisterPhoneValues.pageSiteSectionValue,
							mContext);
			// Umeng
//			MobclickTracking.UmengTrack.setPageStart(
//					ContextDataMode.RegisterNameValues.pageNameValue,
//					ContextDataMode.RegisterNameValues.pageSiteSubSectionValue,
//					ContextDataMode.RegisterNameValues.pageSiteSectionValue,
//					mContext);
//			MobclickTracking.UmengTrack.setPageEnd(
//					ContextDataMode.RegisterNameValues.pageNameValue,
//					ContextDataMode.RegisterNameValues.pageSiteSubSectionValue,
//					ContextDataMode.RegisterNameValues.pageSiteSectionValue,
//					mContext);
			break;

		case 3:
			// omniture
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.RegisterTermsValues.pageNameValue,
							ContextDataMode.RegisterTermsValues.pageSiteSubSectionValue,
							ContextDataMode.RegisterTermsValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.RegisterTermsValues.pageNameValue,
//							ContextDataMode.RegisterTermsValues.pageSiteSubSectionValue,
//							ContextDataMode.RegisterTermsValues.pageSiteSectionValue,
//							mContext);
//			MobclickTracking.UmengTrack
//					.setPageEnd(
//							ContextDataMode.RegisterTermsValues.pageNameValue,
//							ContextDataMode.RegisterTermsValues.pageSiteSubSectionValue,
//							ContextDataMode.RegisterTermsValues.pageSiteSectionValue,
//							mContext);
			break;
		}
	}

}