package com.ef.bite.ui.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.CountryBLL;
import com.ef.bite.business.GlobalConfigBLL;
import com.ef.bite.business.task.GetGlobalLanguageTask;
import com.ef.bite.business.task.GetServerAddressTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.PostFingerPrintTask;
import com.ef.bite.dataacces.ProfileCache;
import com.ef.bite.dataacces.mode.httpMode.HttpAppResourceRequest;
import com.ef.bite.dataacces.mode.httpMode.NewHttpAppResourceRequest;
import com.ef.bite.lang.Closure;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.user.EFLoginWelcomeActivity;
import com.ef.bite.utils.*;
import com.ef.bite.widget.GifImageView;
import com.parse.ParseObject;
import org.json.JSONObject;

import java.io.*;

@SuppressLint("SimpleDateFormat")
public class SplashActivity extends BaseActivity {
    private GifImageView mLoadingImage;
    private TextView mLoadingText;
    private ImageView mWooLogoImage;
    private ProgressDialog progress;
    private final static int Splash_Woo = 1;
    private final static int Splash_1 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setupViews();

        int requestCode = 0;
        startActivityForResult(new Intent(this, SecondSplashActivity.class), requestCode);

//        startActivity(new Intent(this, SecondSplashActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            iniAppResources();
        }
    }

    private void setupViews() {
        mLoadingImage = (GifImageView) findViewById(R.id.splash_loading);
        mLoadingText = (TextView) findViewById(R.id.splash_loading_text);
        mWooLogoImage = (ImageView) findViewById(R.id.woo_store_logo);
        FontHelper.applyFont(mContext, mLoadingText, FontHelper.FONT_OpenSans);
        mLoadingImage.startGifFromAsset("gif/loading_black_small.gif", true);
        progress = new ProgressDialog(SplashActivity.this);
        progress.show();
        avoidMediaLibScan();
        initWoo();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickTracking.UmengTrack.setPageStart(
//                ContextDataMode.SplashValues.pageNameValue,
//                ContextDataMode.SplashValues.pageSiteSubSectionValue,
//                ContextDataMode.SplashValues.pageSiteSectionValue, mContext);

        if (AppConst.HeaderStore.StoreName.equals("woo")) {
//            MobclickTracking.UmengTrack
//                    .setPageStart(
//                            ContextDataMode.Keydata.splash_wo_store,
//                            ContextDataMode.SplashValues.pageSiteSubSectionValue,
//                            ContextDataMode.SplashValues.pageSiteSectionValue,
//                            mContext);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickTracking.UmengTrack.setPageEnd(
//                ContextDataMode.SplashValues.pageNameValue,
//                ContextDataMode.SplashValues.pageSiteSubSectionValue,
//                ContextDataMode.SplashValues.pageSiteSectionValue, mContext);
    }

    // Woo商店Logo显示
    private void initWoo() {
        if (AppConst.HeaderStore.StoreName.equals("woo")) {
            mWooLogoImage.setBackgroundResource(R.drawable.woo_store_logo);
            BI_Tracking(Splash_Woo);
        } else {
            mWooLogoImage.setVisibility(View.GONE);
            BI_Tracking(Splash_1);
        }
    }

    /**
     * 初始化系统环境
     */
    private void iniAppResources() {
        // 加载APP设置
        try {
            // Get device info
            AppConst.GlobalConfig.DeviceID = Secure.getString(
                    mContext.getContentResolver(), Secure.ANDROID_ID);
            AppConst.GlobalConfig.Device_Brand = Build.BRAND;
            AppConst.GlobalConfig.Device_Model = Build.MODEL;
            AppConst.GlobalConfig.OS_Version = Build.VERSION.RELEASE;
            if (getVersion(mContext) != null)
                AppConst.GlobalConfig.App_Version = getVersion(mContext);


            // Get Global config setting
            GlobalConfigBLL configbll = new GlobalConfigBLL(mContext);
            ConfigModel appConfig = configbll.getConfigModel();
            if (appConfig != null) {
                AppConst.GlobalConfig.WelComePageStarted = appConfig.IsWelPageLoaded;
                AppConst.GlobalConfig.Notification_Enable = appConfig.IsNotificationOn;
                AppConst.GlobalConfig.SoundEffect_Enable = appConfig.IsSoundEffectOn;
                AppConst.GlobalConfig.LanguageType = appConfig.MultiLanguageType;
                AppConst.CurrUserInfo.CourseLevel = appConfig.CourseLevel;

                if (appConfig.MultiLanguageType != AppConst.MultiLanguageType.Default)
                    AppLanguageHelper.loadLanguageFirstTime(mContext,
                            appConfig.MultiLanguageType);
                AppConst.GlobalConfig.Language = AppLanguageHelper.getSystemLaunguage(mContext);
            } else
                AppConst.GlobalConfig.Language = AppLanguageHelper.getSystemLaunguage(mContext);

            profileCache.loadUserProfile();

            // 检查网络
            if (!NetworkChecker.isConnected(mContext))
                Toast.makeText(mContext,
                        getString(R.string.network_not_available),
                        Toast.LENGTH_LONG).show();
            // 初始化server address
            getServerAddressAPI();
        } catch (Exception ex) {
            ex.printStackTrace();
            // deplayAndFinish();
        }

    }


    private void getServerAddressAPI() {
        progress.setMessage("Getting language pack...");
        GetServerAddressTask getServerAddressTask = new GetServerAddressTask(this, new Closure() {
            @Override
            public void execute(Object result) {
                String path = android.os.Environment.getExternalStorageDirectory() + File.separator +
                        AppConst.CacheKeys.RootStorage + File.separator
                        + AppConst.CacheKeys.Storage_Language + File.separator;
                File file=new File(path + "config.json");

                if (!file.exists()){
                    FileStorage.CopyAssets(AppConst.CacheKeys.Storage_Language, path, mContext);
                }
                getResourceInfo();
//                getLanguagePack();
                postFingerPrint();
            }
        });
        getServerAddressTask.execute();
    }

    private void postFingerPrint() {
        PostFingerPrintTask postFingerPrintTask = new PostFingerPrintTask(
                mContext,
                new PostExecuting<Boolean>() {

                    @Override
                    public void executing(
                            Boolean result) {
                    }
                });
        postFingerPrintTask.execute(new String[]{"android"
                + " "
                + Build.BRAND,
                Build.VERSION.RELEASE});
    }

//    private void getLanguagePack() {
//        progress.setMessage("Getting language pack...");
//        HttpAppResourceRequest appResourceRequest = new HttpAppResourceRequest();
//        appResourceRequest.app_ver = AppUtils.getVersion(mContext);
//        appResourceRequest.system = AppConst.GlobalConfig.OS;
//        appResourceRequest.res_ver = getTranslationVersion();
//        appResourceRequest.system_culture = AppLanguageHelper.getSystemLaunguage(mContext);
//        appResourceRequest.app_res_culture = PreferencesUtils.getString(mContext, AppLanguageHelper.App_Res_Culture, "");
//        GetGlobalLanguageTask getGlobalLanguageTask = new GetGlobalLanguageTask(
//                mContext,
//                new PostExecuting<Boolean>() {
//
//                    @Override
//                    public void executing(Boolean result) {
//                        progress.dismiss();
//                        if (result) {
//                            loginCheck();
//                        }
//                    }
//                });
//
//        getGlobalLanguageTask.execute(appResourceRequest);
//    }

    private void getResourceInfo() {
        NewHttpAppResourceRequest appResourceRequest = new NewHttpAppResourceRequest();

        JSONObject obj = JsonSerializeHelper.getJSONObjectFromSD(android.os.Environment.getExternalStorageDirectory() + File.separator +
                AppConst.CacheKeys.RootStorage + File.separator
                + AppConst.CacheKeys.Storage_Language + File.separator + "config.json");

        appResourceRequest.id = obj.optString("id");
        appResourceRequest.version = obj.optInt("version");
        GetGlobalLanguageTask getGlobalLanguageTask = new GetGlobalLanguageTask(
                mContext,
                new PostExecuting<Boolean>() {

                    @Override
                    public void executing(Boolean result) {
                        progress.dismiss();

                        // 加载上次登录信息
//                        if (result){
//                        }
                        newGetLanguagePack();
                        loginCheck();
                    }
                });

        getGlobalLanguageTask.execute(appResourceRequest);
    }

    private void newGetLanguagePack() {
        FileStorage languageStorage = new FileStorage(this, AppConst.CacheKeys.Storage_Language);

        String sys_language = AppLanguageHelper.getSystemLaunguage(mContext);
        sys_language = CountryBLL.countryMapping(sys_language);
        AppLanguageHelper.loadLanguageFromStorage(mContext, languageStorage.getStorageFolder() + "/system_text/", sys_language);

        initStudyPlanMap();
    }


    private void initStudyPlanMap() {
        if (AppConst.GlobalConfig.StudyPlans != null && AppConst.GlobalConfig.StudyPlansMap != null && AppConst.GlobalConfig.StudyPlansMap.size() <= 0) {

            for (int i = 0; i < AppConst.GlobalConfig.StudyPlans.size(); i++) {
                String studyplan = AppConst.GlobalConfig.StudyPlans.get(i);
                String temp = JsonSerializeHelper.JsonLanguageDeserialize(mContext, studyplan);
                AppConst.GlobalConfig.StudyPlansMap.put(studyplan, temp);
            }
        }
    }

    private String getTranslationVersion(){
        FileStorage languageStorage = new FileStorage(this,
                AppConst.CacheKeys.Storage_Language);
        try {
            InputStream is = new FileInputStream(new File(languageStorage.getStorageFolder()+File.separator+"translation.json"));
            if (is == null)
                return null;
            InputStreamReader inputStreamReader = new InputStreamReader(is,
                    "UTF-8");
            char[] buffer = new char[is.available()];
            String jsonString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStreamReader.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, buffer.length));
            }
            jsonString = stringBuffer.toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.optString("version");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private void loginCheck() {
        if (AppConst.CurrUserInfo.IsLogin) {
            getUserProfile();
        } else {
            Intent intent = new Intent(mContext,
                    EFLoginWelcomeActivity.class);
            startActivityForResult(intent, AppConst.RequestCode.EF_LOGIN);
            finish();
        }
    }



    /**
     * 获得当前的app版本 *
     */
    private String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void BI_Tracking(int i) {
        // TODO Auto-generated method stub
        switch (i) {
            case 1:
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SplashValues.pageNameValue_woo,
                                ContextDataMode.SplashValues.pageSiteSubSectionValue,
                                ContextDataMode.SplashValues.pageSiteSectionValue,
                                mContext);
                break;

            case 2:
                MobclickTracking.OmnitureTrack
                        .AnalyticsTrackState(
                                ContextDataMode.SplashValues.pageNameValue,
                                ContextDataMode.SplashValues.pageSiteSubSectionValue,
                                ContextDataMode.SplashValues.pageSiteSectionValue,
                                mContext);
                break;
        }
    }

    /**
     * avoid media scanning
     */
    private void avoidMediaLibScan() {
        FileStorage fileStorage = new FileStorage(this, "");
        fileStorage.createNomediaFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progress.dismiss();
    }

}
