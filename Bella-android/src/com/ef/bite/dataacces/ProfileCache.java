package com.ef.bite.dataacces;

import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.model.ExistedLogin;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.StringUtils;

/**
 * The weird method to adapt previous code. To cache user profile and load it as app global variables.
 * The AppConst (including server address)info will be lost when app crash and restart.The original is such thing and I am not allow to
 * change base framework. So you should refactor all of these.
 * Created by yang on 15/6/17.
 */
public class ProfileCache {
    private Context context;

    public ProfileCache(Context context) {
        this.context = context;
    }

    public void setUserProfile(HttpProfile profile) {
        AppConst.CurrUserInfo.UserId = profile.data.bella_id;
        AppConst.CurrUserInfo.Alias = profile.data.alias;
        AppConst.CurrUserInfo.Avatar = profile.data.avatar_url;
        AppConst.CurrUserInfo.FirstName = profile.data.given_name;
        AppConst.CurrUserInfo.LastName = profile.data.family_name;
        AppConst.CurrUserInfo.Location = profile.data.market_code;

        AppConst.CurrUserInfo.Phone = profile.data.phone;
        AppConst.CurrUserInfo.Score = profile.data.score;
        AppConst.CurrUserInfo.Level = profile.data.Level;
        AppConst.CurrUserInfo.CourseLevel = profile.data.plan_id;

        save();
    }

    public void loadUserProfile() {
        ExistedLogin existedLogin = (ExistedLogin) JsonSerializeHelper.JsonDeserialize(
                PreferencesUtils.getString(context,AppConst.CacheKeys.CACHE_DASHBOARD),ExistedLogin.class);
        if (existedLogin != null) {
            AppConst.CurrUserInfo.IsLogin = existedLogin.IsLogin;
            AppConst.CurrUserInfo.UserId = existedLogin.UId;
            AppConst.CurrUserInfo.Alias = existedLogin.Alias;
            AppConst.CurrUserInfo.Avatar = existedLogin.Avatar;
            AppConst.CurrUserInfo.FirstName = existedLogin.FirstName;
            AppConst.CurrUserInfo.LastName = existedLogin.LastName;
            AppConst.CurrUserInfo.Phone = existedLogin.Phone;
            AppConst.CurrUserInfo.Score = existedLogin.Score;
            AppConst.CurrUserInfo.Level = existedLogin.Level;
            AppConst.CurrUserInfo.Location = existedLogin.Location;
//            AppConst.CurrUserInfo.CourseLevel = existedLogin.Plan_ID;

        }
    }


    public void save() {
        ExistedLogin existedLogin = new ExistedLogin();
        existedLogin.IsLogin = AppConst.CurrUserInfo.IsLogin;
        existedLogin.UId = AppConst.CurrUserInfo.UserId;
        existedLogin.Alias = AppConst.CurrUserInfo.Alias;
        existedLogin.Avatar = AppConst.CurrUserInfo.Avatar;
        existedLogin.FirstName = AppConst.CurrUserInfo.FirstName;
        existedLogin.LastName = AppConst.CurrUserInfo.LastName;
        existedLogin.Phone = AppConst.CurrUserInfo.Phone;
        existedLogin.Score = AppConst.CurrUserInfo.Score;
        existedLogin.Level = AppConst.CurrUserInfo.Level;
        existedLogin.Location = AppConst.CurrUserInfo.Location;
        existedLogin.Plan_ID = AppConst.CurrUserInfo.CourseLevel;

        PreferencesUtils.putString(context, AppConst.CacheKeys.CACHE_DASHBOARD, JsonSerializeHelper.JsonSerializer(existedLogin));
    }
}
