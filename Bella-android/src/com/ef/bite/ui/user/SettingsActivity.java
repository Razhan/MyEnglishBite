package com.ef.bite.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.AppSession;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.GlobalConfigBLL;
import com.ef.bite.business.task.PostAvatarTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.UpdateUserProfile;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.guide.WalkthroughActivity;
import com.ef.bite.ui.main.SplashActivity;
import com.ef.bite.ui.popup.SettingChangeDialogFragment;
import com.ef.bite.ui.popup.TermsServicePopupWindow;
import com.ef.bite.utils.*;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.SettingItemLayout;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.Facebook;
//import com.facebook.android.FacebookError;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends BaseActivity {

    ActionbarLayout mActionbar;
    Button mLogout;
    // 个人信息
    SettingItemLayout mAvatarItem;
    SettingItemLayout mUseridItem;
    SettingItemLayout mNicknameItem;
    SettingItemLayout mFirstnameItem;
    SettingItemLayout mLastnameItem;
    SettingItemLayout mPhoneItem;
    SettingItemLayout mPasswordItem;
    SettingItemLayout mEmailItem;
    SettingItemLayout mLocationItem;
    // App设置
    SettingItemLayout mLanugageItem;
    SettingItemLayout mCourseLevel;
    SettingItemLayout mNotificationItem;
    SettingItemLayout mSoundItem;
    SettingItemLayout mResetItem;
    SettingItemLayout mTermServiceItem;
    SettingItemLayout mAboutVersionItem;

    ItemClickListener mItemClick;
    // 头像更新信息
    final static int PICK_FROM_GALLARY = 1;
    final static int PICK_FROM_CAMERA = 2;
    final static int CROP_FROM_CAMERA = 3;
    final static int AVATAR_WIDTH = 512; // 头像截图的长
    final static int AVATAR_HEIGHT = 512; // 头像截图的高
    Uri mImageCaptureUri; // 照相照片临时存储的地方
    GlobalConfigBLL configbll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_settings);
        mActionbar = (ActionbarLayout) findViewById(R.id.settings_actionbar);
        mLogout = (Button) findViewById(R.id.settings_log_out);
        mLogout.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "settings_logout"));
        // 个人信息
        this.mAvatarItem = (SettingItemLayout) findViewById(R.id.settings_person_avatar);
        this.mUseridItem = (SettingItemLayout) findViewById(R.id.settings_person_userid);
        this.mNicknameItem = (SettingItemLayout) findViewById(R.id.settings_person_nickname);
        this.mFirstnameItem = (SettingItemLayout) findViewById(R.id.settings_person_firstname);
        this.mLastnameItem = (SettingItemLayout) findViewById(R.id.settings_person_lastname);
        this.mPhoneItem = (SettingItemLayout) findViewById(R.id.settings_person_phone);
        this.mPasswordItem = (SettingItemLayout) findViewById(R.id.settings_person_password);
        this.mEmailItem = (SettingItemLayout) findViewById(R.id.settings_person_email);
        this.mLocationItem = (SettingItemLayout) findViewById(R.id.settings_person_location);
        // App信息
        this.mLanugageItem = (SettingItemLayout) findViewById(R.id.settings_app_language);
        this.mCourseLevel = (SettingItemLayout) findViewById(R.id.settings_app_courselevel);
        this.mNotificationItem = (SettingItemLayout) findViewById(R.id.settings_app_notification);
        this.mSoundItem = (SettingItemLayout) findViewById(R.id.settings_app_sound_effect);
        this.mResetItem = (SettingItemLayout) findViewById(R.id.settings_app_reset);
        this.mTermServiceItem = (SettingItemLayout) findViewById(R.id.settings_app_terms);
        this.mAboutVersionItem = (SettingItemLayout) findViewById(R.id.settings_app_about);
        mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "settings_page_title"),
                R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
        configbll = new GlobalConfigBLL(mContext);


        MobclickTracking.OmnitureTrack.AnalyticsTrackState(
                ContextDataMode.SettingsValues.pageNameValue,
                ContextDataMode.SettingsValues.pageSiteSubSectionValue,
                ContextDataMode.SettingsValues.pageSiteSectionValue, mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.SettingsValues.pageNameValue,
//				ContextDataMode.SettingsValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsValues.pageSiteSectionValue, mContext);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.SettingsValues.pageNameValue,
//				ContextDataMode.SettingsValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsValues.pageSiteSectionValue, mContext);
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.SettingsFirstNameValuse.pageNameValue,
//						ContextDataMode.SettingsFirstNameValuse.pageSiteSubSectionValue,
//						ContextDataMode.SettingsFirstNameValuse.pageSiteSectionValue,
//						mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.SettingsLastNameValuse.pageNameValue,
//				ContextDataMode.SettingsLastNameValuse.pageSiteSubSectionValue,
//				ContextDataMode.SettingsLastNameValuse.pageSiteSectionValue,
//				mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.SettingsNicknameValues.pageNameValue,
//				ContextDataMode.SettingsNicknameValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsNicknameValues.pageSiteSectionValue,
//				mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (mImageCaptureUri != null) {
                Uri corp_uri = Uri.fromFile(new File(getCacheDir(), "crop_"
                        + KeyGenerator.getKeyFromDateTime()));
                new Crop(mImageCaptureUri).output(corp_uri).asSquare()
                        .start(this);
            }
        } else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            Uri corp_uri = Uri.fromFile(new File(getCacheDir(), "crop_"
                    + KeyGenerator.getKeyFromDateTime()));
            new Crop(data.getData()).output(corp_uri).asSquare().start(this);
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                // 截图成功
                Uri outputUri = Crop.getOutput(data);
                if (outputUri != null) {
                    try {
                        Bitmap photo = ImageUtils.decodeFile(
                                outputUri.getPath(), AVATAR_WIDTH,
                                AVATAR_HEIGHT);
                        if (photo != null) {
                            uploadAvatar(photo);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(
                                mContext,
                                JsonSerializeHelper.JsonLanguageDeserialize(
                                        mContext,
                                        "settings_avatar_fail_to_upload"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(this, Crop.getError(data).getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** 加载数据 **/
    public void loadData() {
        if (mItemClick == null)
            mItemClick = new ItemClickListener();
        mLogout.setOnClickListener(mItemClick);
        mAvatarItem.initiWithAvatar(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_picture"),
                AppConst.CurrUserInfo.UserId, AppConst.CurrUserInfo.Avatar,
                true, mItemClick);
        mUseridItem.initiWithText(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "settings_user_id"), AppConst.CurrUserInfo.Alias,
                false, null);
        mNicknameItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_nickname"),
                AppConst.CurrUserInfo.Alias, true, mItemClick);
        mFirstnameItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_firstname"),
                AppConst.CurrUserInfo.FirstName, true, mItemClick);
        mLastnameItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_lastname"),
                AppConst.CurrUserInfo.LastName, true, mItemClick);
        mPhoneItem.initiWithText(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "settings_phone_number"),
                AppConst.CurrUserInfo.Phone, false, null);
        mPasswordItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_password"),
                "**********", false, mItemClick);
        mPasswordItem.showBottomLine(false);
        mEmailItem.initiWithText(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "settings_email"), AppConst.CurrUserInfo.Email,
                false, null);
        mLocationItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_location"),
                LocationHelper.getCountryNameByCode(mContext,
                        AppConst.CurrUserInfo.Location,
                        AppLanguageHelper.getSystemLaunguage(mContext)), true,
                mItemClick);
        mLocationItem.showBottomLine(false);
        mLanugageItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_language"),
                AppLanguageHelper.getLanguageDisplayByType(mContext,
                        AppConst.GlobalConfig.LanguageType), true, mItemClick);

        mCourseLevel.initiWithText("Course", JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, AppConst.CurrUserInfo.CourseLevel), true, mItemClick);

        mNotificationItem.initWithSwitch(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_notification"),
                AppConst.GlobalConfig.Notification_Enable,
                new SettingItemLayout.SwitchListener() {
                    @Override
                    public void onSwitch(boolean value) {
                        // 保存通知设置状态

                        if (value) {
                            MobclickTracking.OmnitureTrack.ActionSettings(2, 1);
                        } else {
                            MobclickTracking.OmnitureTrack.ActionSettings(2, 2);
                        }

                        AppConst.GlobalConfig.Notification_Enable = value;
                        ConfigModel appConfig = configbll.getConfigModel();
                        if (appConfig != null) {
                            if (appConfig.IsNotificationOn != value) {
                                appConfig.IsNotificationOn = value;
                                configbll.setConfigModel(appConfig);
                            }
                        } else {
                            appConfig = new ConfigModel();
                            appConfig.IsNotificationOn = value;
                            configbll.setConfigModel(appConfig);
                        }
                    }
                });
        mSoundItem.initWithSwitch(JsonSerializeHelper.JsonLanguageDeserialize(
                        mContext, "settings_sound_effect"),
                AppConst.GlobalConfig.SoundEffect_Enable,
                new SettingItemLayout.SwitchListener() {
                    @Override
                    public void onSwitch(boolean value) {
                        if (value) {
                            MobclickTracking.OmnitureTrack.ActionSettings(3, 1);
                        } else {
                            MobclickTracking.OmnitureTrack.ActionSettings(3, 2);
                        }
                        // 保存声音设置状态
                        AppConst.GlobalConfig.SoundEffect_Enable = value;
                        ConfigModel appConfig = configbll.getConfigModel();
                        if (appConfig != null) {
                            if (appConfig.IsSoundEffectOn != value) {
                                appConfig.IsSoundEffectOn = value;
                                configbll.setConfigModel(appConfig);
                            }
                        } else {
                            appConfig = new ConfigModel();
                            appConfig.IsSoundEffectOn = value;
                            configbll.setConfigModel(appConfig);
                        }
                    }
                });

        mResetItem.initiWithText(JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, "settings_reset_tutorial"), "", true, mItemClick);
        mResetItem.showBottomLine(false);
        mTermServiceItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_terms_service"),
                "", true, mItemClick);
        mAboutVersionItem.initiWithText(JsonSerializeHelper
                        .JsonLanguageDeserialize(mContext, "settings_about"), "", true,
                mItemClick);
        mAboutVersionItem.showBottomLine(false);
    }

    /**
     * 设置项点击
     **/
    public class ItemClickListener implements View.OnClickListener {
        private String selectedcourse = null;
        private int courseindex = -1;


        @Override
        public void onClick(View v) {
            if (v.getId() == mLogout.getId()) { // 退出
                onLogoutClick();
                MobclickTracking.OmnitureTrack.ActionSettings(4, 0);
            } else if (v.getId() == mAvatarItem.getId()) { // 更新头像
                onAvatarEditClick();
                MobclickTracking.OmnitureTrack.ActionSettings(1, 0);
            } else if (v.getId() == mNicknameItem.getId()) { // 更新昵称
                onItemValueChange(
                        SettingChangeDialogFragment.SETTINGS_NICKNAME,
                        JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                                "settings_nickname"),
                        AppConst.CurrUserInfo.Alias,
                        new SettingChangeDialogFragment.AfterItemChangeListener() {
                            @Override
                            public void afterChange(String changed) {
                                AppConst.CurrUserInfo.Alias = changed;
                                mNicknameItem.initiWithText(JsonSerializeHelper
                                                .JsonLanguageDeserialize(mContext,
                                                        "settings_nickname"),
                                        AppConst.CurrUserInfo.Alias, true,
                                        mItemClick);
                            }
                        });
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SettingsNicknameValues.pageNameValue,
                                ContextDataMode.SettingsNicknameValues.pageSiteSubSectionValue,
                                ContextDataMode.SettingsNicknameValues.pageSiteSectionValue,
                                mContext);
//				MobclickTracking.UmengTrack
//						.setPageStart(
//								ContextDataMode.SettingsNicknameValues.pageNameValue,
//								ContextDataMode.SettingsNicknameValues.pageSiteSubSectionValue,
//								ContextDataMode.SettingsNicknameValues.pageSiteSectionValue,
//								mContext);
            } else if (v.getId() == mFirstnameItem.getId()) { // 更新first name
                onItemValueChange(
                        SettingChangeDialogFragment.SETTINGS_FIRST_NAME,
                        JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                                "settings_firstname"),
                        AppConst.CurrUserInfo.FirstName,
                        new SettingChangeDialogFragment.AfterItemChangeListener() {
                            @Override
                            public void afterChange(String changed) {
                                AppConst.CurrUserInfo.FirstName = changed;
                                mFirstnameItem.initiWithText(
                                        JsonSerializeHelper
                                                .JsonLanguageDeserialize(
                                                        mContext,
                                                        "settings_firstname"),
                                        AppConst.CurrUserInfo.FirstName, true,
                                        mItemClick);
                            }
                        });
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SettingsFirstNameValuse.pageNameValue,
                                ContextDataMode.SettingsFirstNameValuse.pageSiteSubSectionValue,
                                ContextDataMode.SettingsFirstNameValuse.pageSiteSectionValue,
                                mContext);
//				MobclickTracking.UmengTrack
//						.setPageStart(
//								ContextDataMode.SettingsFirstNameValuse.pageNameValue,
//								ContextDataMode.SettingsFirstNameValuse.pageSiteSubSectionValue,
//								ContextDataMode.SettingsFirstNameValuse.pageSiteSectionValue,
//								mContext);
            } else if (v.getId() == mLastnameItem.getId()) { // 更新last name
                onItemValueChange(
                        SettingChangeDialogFragment.SETTINGS_LAST_NAME,
                        JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                                "settings_lastname"),
                        AppConst.CurrUserInfo.LastName,
                        new SettingChangeDialogFragment.AfterItemChangeListener() {
                            @Override
                            public void afterChange(String changed) {
                                AppConst.CurrUserInfo.LastName = changed;
                                mLastnameItem.initiWithText(JsonSerializeHelper
                                                .JsonLanguageDeserialize(mContext,
                                                        "settings_lastname"),
                                        AppConst.CurrUserInfo.LastName, true,
                                        mItemClick);
                            }
                        });
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SettingsLastNameValuse.pageNameValue,
                                ContextDataMode.SettingsLastNameValuse.pageSiteSubSectionValue,
                                ContextDataMode.SettingsLastNameValuse.pageSiteSectionValue,
                                mContext);
//				MobclickTracking.UmengTrack
//						.setPageStart(
//								ContextDataMode.SettingsLastNameValuse.pageNameValue,
//								ContextDataMode.SettingsLastNameValuse.pageSiteSubSectionValue,
//								ContextDataMode.SettingsLastNameValuse.pageSiteSectionValue,
//								mContext);
            } else if (v.getId() == mPhoneItem.getId()) { // 更新手机
                onItemValueChange(
                        SettingChangeDialogFragment.SETTINGS_PHONE,
                        JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                                "settings_phone_number"),
                        AppConst.CurrUserInfo.Phone,
                        new SettingChangeDialogFragment.AfterItemChangeListener() {
                            @Override
                            public void afterChange(String changed) {
                                AppConst.CurrUserInfo.Phone = changed;
                                mPhoneItem.initiWithText(JsonSerializeHelper
                                                .JsonLanguageDeserialize(mContext,
                                                        "settings_phone_number"),
                                        AppConst.CurrUserInfo.Phone, true,
                                        mItemClick);
                            }
                        });
            } else if (v.getId() == mPasswordItem.getId()) { // 重置密码
                startActivity(new Intent(mContext, EFChangePWDActivity.class));
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            } else if (v.getId() == mLocationItem.getId()) { // 更新位置
                Intent intent = new Intent(mContext,
                        LocationSettingActivity.class);
                startActivityForResult(intent,
                        AppConst.RequestCode.LOCATION_SETTING);
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            } else if (v.getId() == mLanugageItem.getId()) { // 语言设置
                startActivity(new Intent(mContext,
                        LanguageSettingActivity.class));
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            } else if (v.getId() == mCourseLevel.getId()) { //更新课程难度
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Change Course");

                List<String> valueset = new ArrayList<String>();
                valueset = ListUtils.getValues(AppConst.GlobalConfig.StudyPlansMap);

                builder.setSingleChoiceItems(valueset.toArray(new CharSequence[valueset.size()]),
                        -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedcourse = AppConst.GlobalConfig.StudyPlans.get(which);
                                courseindex = which;
                            }
                        });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedcourse != null && courseindex != -1) {

                            updateProfile(selectedcourse, courseindex);

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            } else if (v.getId() == mResetItem.getId()) { // 教程模式
                Intent intent = new Intent(mContext, WalkthroughActivity.class);
                intent.putExtra(AppConst.BundleKeys.Reset_Tutorial_Mode, true);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            } else if (v.getId() == mTermServiceItem.getId()) { // 显示服务条款
                TermsServicePopupWindow termsPopup = new TermsServicePopupWindow(
                        SettingsActivity.this);
                termsPopup.open();
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SettingsTermsValues.pageNameValue,
                                ContextDataMode.SettingsTermsValues.pageSiteSubSectionValue,
                                ContextDataMode.SettingsTermsValues.pageSiteSectionValue,
                                mContext);
                // startActivity(new Intent(mContext,
                // TermConditionActivity.class));
            } else if (v.getId() == mAboutVersionItem.getId()) { // 显示app版本
                startActivity(new Intent(mContext, VersionAboutActivity.class));
                overridePendingTransition(R.anim.activity_in_from_right,
                        R.anim.activity_out_to_left);
            }
        }
    }


    private void updateProfile(final String courselevel, final int index) {
        UpdateUserProfile updateTask = new UpdateUserProfile(mContext,
                new PostExecuting<HttpBaseMessage>() {
                    @Override
                    public void executing(HttpBaseMessage result) {
                        if (result != null && "0".equals(result.status)) {

                            AppConst.CurrUserInfo.CourseLevel = courselevel;

                            ConfigModel appConfig = configbll.getConfigModel();
                            if (appConfig == null) {
                                appConfig = new ConfigModel();
                            }
                            appConfig.CourseLevel = courselevel;
                            configbll.setConfigModel(appConfig);

                            mCourseLevel.initiWithText("Course", AppConst.GlobalConfig.StudyPlansMap.get(courselevel), true, mItemClick);


                        } else {
                            Log.e("ThirdPartyLogin", "updateProfile error");
                            finish();
                        }
                    }
                });

        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("bella_id", AppConst.CurrUserInfo.UserId);
            jsonObj.put("plan_id", courselevel);
            updateTask.execute(jsonObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    AvataEditorPopupWindow avatarEditPopup = null;

    /** 编辑头像 **/
    private void onAvatarEditClick() {
        avatarEditPopup = new AvataEditorPopupWindow(this,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takeCamera();
                        if (avatarEditPopup != null)
                            avatarEditPopup.close();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(SettingsActivity.this);
                avatarEditPopup.close();
            }
        });
        avatarEditPopup.open();
    }

    /*** 从照相机拍摄照片 **/
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "tmp_camera_crop_" + String.valueOf(System.currentTimeMillis())
                        + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /** 上传用户头像 **/
    private void uploadAvatar(final Bitmap bitmap) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, "settings_avatar_uploading"));
        progress.show();
        PostAvatarTask task = new PostAvatarTask(mContext,
                new PostExecuting<HttpBaseMessage>() {
                    @Override
                    public void executing(HttpBaseMessage result) {
                        progress.dismiss();
                        if (result != null && result.status != null) {
                            if (result.status.equals("0")) {
                                mAvatarItem.updateAvatar(
                                        AppConst.CurrUserInfo.UserId, bitmap);
                                Toast.makeText(
                                        mContext,
                                        JsonSerializeHelper
                                                .JsonLanguageDeserialize(
                                                        mContext,
                                                        "settings_avatar_success_to_upload"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, result.message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(
                                    mContext,
                                    JsonSerializeHelper
                                            .JsonLanguageDeserialize(mContext,
                                                    "settings_avatar_fail_to_upload"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        task.execute(new Bitmap[]{bitmap});
    }

    /** 登出 **/
    private void onLogoutClick() {
        DialogHelper.ConfirmBox(
                this,
                JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                        "settings_logout_dialog_title"),
                JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                        "settings_logout_dialog_content"),
                JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                        "settings_logout_dialog_ok"),
                new DialogHelper.Clicking() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void Click() {
                        LoginManager.getInstance().logOut();
                        PreferencesUtils.putString(mContext, "access_token", null);

                        // call log out service
                        // to do
                        AppConst.CurrUserInfo.IsLogin = false;
                        AppConst.CurrUserInfo.UserId = "0";
                        profileCache.save();
                        AppSession.getInstance().clear();
                        startActivity(new Intent(mContext, SplashActivity.class));
                    }
                }, JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                        "settings_logout_dialog_cancel"),
                new DialogHelper.Clicking() {
                    @Override
                    public void Click() {
                    }
                });
    }

    /** 修改设置项的值 **/
    private void onItemValueChange(int itemId, String itemName,
                                   String itemValue,
                                   SettingChangeDialogFragment.AfterItemChangeListener listener) {
        if (itemValue == null || itemValue.isEmpty()) {
            Toast.makeText(
                    mContext,
                    JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                            "settings_item_value_null"), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        SettingChangeDialogFragment dialog = new SettingChangeDialogFragment(
                this, itemId, itemName, itemValue, listener);
        dialog.show(getSupportFragmentManager(), "item change");
    }


//    private List<String> getValues(HashMap<String, String> map) {
//        if (map.equals(null)) {
//            return null;
//        }
//
//        List<String> result = new ArrayList<String>();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//
//            result.add(entry.getValue());
//        }
//        ArrayList<String> rlist = new ArrayList<String>(result);
//
//        Collections.reverse(rlist);
//        return rlist;
//    }


}