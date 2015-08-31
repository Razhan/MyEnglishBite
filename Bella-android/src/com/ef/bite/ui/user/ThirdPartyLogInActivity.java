package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.LoginTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.LoginMode;
import com.ef.bite.dataacces.mode.httpMode.HttpLogin;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.LoginInputLayout;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;

public class ThirdPartyLogInActivity extends BaseActivity {
    private LoginInputLayout mPhoneInput;
    private Spinner level_spinner;
    private Button mNextBtn;
    private ActionbarLayout mActionbar;
    private int mPositionLevel;
    private String mLevelChoice;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_log_in);

        final String token = getIntent().getStringExtra("token");

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        mPhoneInput = (LoginInputLayout)findViewById(R.id.ThirdParty_login_ef_phone);
        level_spinner = (Spinner)findViewById(R.id.ThirdParty_login_level_spinner);
        mNextBtn = (Button)findViewById(R.id.ThirdParty_login_ef_btn_next);
        mActionbar = (ActionbarLayout) findViewById(R.id.ThirdParty_login_ef_actionbar);


        mPhoneInput.initialize(JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, "register_ef_phone"), InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_CLASS_NUMBER, true);

        mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(mContext, "register_ef_register_title"),
                R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);
                    }
                }, null);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseLevel = mLevelChoice;
                String phoneNUm = mPhoneInput.getInputString();

                attemp2Login(token);
            }
        });

        SetupSpinner();
    }

    private void SetupSpinner() {
        List<String> spinnerList = new ArrayList<String>();
//        spinnerList2.add(JsonSerializeHelper.JsonLanguageDeserialize(mContext, "register_ef_level_group"));
        spinnerList.add("Your Choice");
        spinnerList.add("Beginner - Elementary");
        spinnerList.add("Intermediate - Advance");

        final ArrayAdapter<String> adapter_level = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter_level.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level_spinner.setAdapter(adapter_level);
        level_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                mLevelChoice = adapter_level.getItem(position).toString();
                mPositionLevel = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void attemp2Login(final String access_token) {
        LoginTask loginTask = new LoginTask(mContext,
                new PostExecuting<HttpLogin>() {
                    @Override
                    public void executing(HttpLogin result) {
                        progress.dismiss();
                        if (result != null && result.status != null
                                && result.status.equals("0")) {
                            progress.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(mContext, "loging_getting_profile"));
                            //Marked as logined
                            AppConst.CurrUserInfo.IsLogin = true;
                            AppConst.CurrUserInfo.UserId=result.data.bella_id;
                            PreferencesUtils.putString(mContext, AppConst.CacheKeys.Facebook_Access_Token, access_token);

                            if (result.data.is_new_user) {
                                MobclickTracking.OmnitureTrack
                                        .ActionRegisterSuccessful(ContextDataMode.ActionRegisterTypeValues.FACEBOOK);
                            }

                            getUserProfile();
                        }
                    }
                });

        LoginMode loginModel = new LoginMode();
        loginModel.login_type = LoginTask.LOGIN_TYPE_FACEBOOK;
        loginModel.provider_type = LoginTask.LOGIN_PROVIDER_FACEBOOK;
        loginModel.access_token = access_token;
        loginTask.execute(new LoginMode[]{loginModel});
    }
}
