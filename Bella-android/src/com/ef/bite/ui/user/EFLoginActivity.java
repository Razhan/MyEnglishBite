package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.task.LoginTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.LoginMode;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpLogin;
import com.ef.bite.model.ExistedLogin;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.SoftInputHelper;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.LoginInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EFLoginActivity extends BaseActivity {
    private ActionbarLayout mActionbar;
    private LoginInputLayout mUserName;
    private LoginInputLayout mPassword;
    private Button mLogin;
    private TextView mForget;
    private TextView login_main_not_registered;
    private LinearLayout mSignUpLayout;
    private TextView mSignUp;
    private final static int Login_Phone = 1;
    private List<HttpCourseRequest> httpCourseRequests = new ArrayList<HttpCourseRequest>();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ef_login);
        mActionbar = (ActionbarLayout) findViewById(R.id.login_ef_actionbar);
        mUserName = (LoginInputLayout) findViewById(R.id.login_ef_username);
        mPassword = (LoginInputLayout) findViewById(R.id.login_ef_password);
        mLogin = (Button) findViewById(R.id.login_ef_login);
        mLogin.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "login_ef_login_button"));
        mForget = (TextView) findViewById(R.id.login_ef_forgot_pwd);

        //设置忘记密码功能是否可用
        if (!AppConst.GlobalConfig.ForgetPassWord) {
            mForget.setVisibility(View.INVISIBLE);
        }


        mSignUpLayout = (LinearLayout) findViewById(R.id.login_ef_signup_layout);
        mSignUp = (TextView) findViewById(R.id.login_ef_signup);
        login_main_not_registered = (TextView) findViewById(R.id.login_main_not_registered);
        login_main_not_registered
                .setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                        "login_main_not_registered_yet")
                        .substring(
                                0,
                                JsonSerializeHelper.JsonLanguageDeserialize(
                                        mContext,
                                        "login_main_not_registered_yet")
                                        .indexOf("<h>")));

        mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "login_ef_login"), R.drawable.arrow_goback_black, -1,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(R.anim.activity_in_from_left,
                                R.anim.activity_out_to_right);
                    }
                }, null);
        // 初始化 输入框
        // mUserName.initialize(getString(R.string.login_ef_username_hint),
        // InputType.TYPE_CLASS_TEXT| InputType.TYPE_CLASS_PHONE, true);
        mUserName.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "login_ef_username_hint"), InputType.TYPE_CLASS_TEXT,
                true);
        mPassword.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, "login_ef_password_hint"), InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD, false);
        // 字体设置
        FontHelper.applyFont(mContext, mForget, FontHelper.FONT_OpenSans);
        mForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mSignUp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForget.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "login_ef_forget_password").replaceAll("<[A-z/ =']*>", ""));
        mSignUp.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "login_main_sign_up"));
        initEvents();
        profileCache.loadUserProfile();
        mUserName.setInputString(AppConst.CurrUserInfo.Phone);
        // tracking
        // TraceHelper.tracingPage(mContext, TraceHelper.PAGE_LOGIN);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        MobclickTracking.UmengTrack.setPageStart(
//                ContextDataMode.LoginValues.pageNameValue_phone,
//                ContextDataMode.LoginValues.pageSiteSubSectionValue,
//                ContextDataMode.LoginValues.pageSiteSectionValue, mContext);
        BI_Tracking(Login_Phone);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        MobclickTracking.UmengTrack.setPageEnd(
//                ContextDataMode.LoginValues.pageNameValue_phone,
//                ContextDataMode.LoginValues.pageSiteSubSectionValue,
//                ContextDataMode.LoginValues.pageSiteSectionValue, mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConst.RequestCode.EF_REGISTER
                && resultCode == AppConst.ResultCode.REGISTER_SUCCESS) {
            setResult(AppConst.ResultCode.LOGIN_SUCCESS);
            finish();
        }
    }

    private void initEvents() {
        mForget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EFFindPWDActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            }
        });

        // 注册界面
        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext,
                                EFRegisterActivity.class),
                        AppConst.RequestCode.EF_REGISTER);
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            }
        });

        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                clearAllErrors();
                SoftInputHelper.hideTemporarily(EFLoginActivity.this);
                progress = new ProgressDialog(EFLoginActivity.this);
                progress.setMessage(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "loging"));
                String username = mUserName.getInputString();
                String password = mPassword.getInputString();
                if (username == null || username.isEmpty()) {
                    mUserName.setError(JsonSerializeHelper
                            .JsonLanguageDeserialize(mContext,
                                    "login_error_username_empty"));
                    return;
                }
                if (password == null || password.isEmpty()) {
                    mPassword.setError(JsonSerializeHelper
                            .JsonLanguageDeserialize(mContext,
                                    "login_error_password_empty"));
                    return;
                }
                if (NetworkChecker.isConnected(mContext)) {
                    progress.show();
                    login(username, password);
                } else {
                    toast(JsonSerializeHelper.JsonLanguageDeserialize(
                            mContext, "error_check_network_available"));
                }
            }
        });
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
                        progress.dismiss();
                        if (result != null && result.status != null) {
                            if (result.status.equals("0")) {
//                                toast("Login is successful");
                                //Marked as logined
                                AppConst.CurrUserInfo.IsLogin = true;
                                AppConst.CurrUserInfo.UserId = result.data.bella_id;
                                getUserProfile();
                            } else {
                                mUserName.setError(result.message);
                                MobclickTracking.OmnitureTrack.actionFormErrorType(result.message);
                            }
                        } else {
                            mUserName.setError(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                                    "error_check_network_available"));
                            MobclickTracking.OmnitureTrack
                                    .actionFormErrorType(getResources()
                                            .getString(
                                                    R.string.error_check_network_available));
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


    /**
     * 清理所有的错误 *
     */
    private void clearAllErrors() {
        mUserName.cleanError();
        mPassword.cleanError();
    }

    @Override
    protected void BI_Tracking(int i) {
        // TODO Auto-generated method stub
        switch (i) {
            case 1:
                MobclickTracking.OmnitureTrack.AnalyticsTrackState(
                        ContextDataMode.LoginValues.pageNameValue_phone,
                        ContextDataMode.LoginValues.pageSiteSubSectionValue,
                        ContextDataMode.LoginValues.pageSiteSectionValue, mContext);
                break;
        }
    }
}
