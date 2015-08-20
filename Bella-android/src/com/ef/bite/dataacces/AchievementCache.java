package com.ef.bite.dataacces;

import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.Achievement;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.StringUtils;
import com.google.gson.reflect.TypeToken;
import com.litesuits.android.async.SimpleTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Using SharePreference to store undeliverable user progress
 * Created by yang on 15/6/16.
 */
public class AchievementCache {
    Context context;
    List<Achievement> mCache;
    private static AchievementCache INSTANCE = new AchievementCache();

    private AchievementCache() {
    }

    public static AchievementCache getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
        loadCache();
    }


    private void loadCache() {
        String cacheStr = PreferencesUtils.getString(context, AppConst.CacheKeys.CACHE_ACHIEVEMENT);
        try {
            mCache = (List<Achievement>) JsonSerializeHelper.JsonDeserialize(cacheStr, new TypeToken<List<Achievement>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mCache == null) {
            mCache = new ArrayList<Achievement>();
        }
    }

    private void saveCache() {
        String cacheStr = JsonSerializeHelper.JsonSerializer(mCache);
        PreferencesUtils.putString(context, AppConst.CacheKeys.CACHE_ACHIEVEMENT, cacheStr);
    }

    public List<Achievement> getPersnalCache(String id) {
        if (mCache == null) {
            return null;
        }
        List<Achievement> list = new ArrayList<Achievement>();
        for (Achievement achievement : mCache) {
            if (StringUtils.isEquals(achievement.getBella_id(), id)) {
                list.add(achievement);
            }
        }
        return list;
    }

    public void setPersnalCache(Achievement achievement) {
        mCache.add(achievement);
        saveCache();
    }

    public void removeCache(Achievement achievement) {
        for (int i = 0; i < mCache.size(); i++) {
            Achievement mAchievement = mCache.get(i);
            if (StringUtils.isEquals(achievement.getCourse_id(), mAchievement.getCourse_id())) {
                mCache.remove(i);
            }
        }
        saveCache();
    }

    public void postCache(final OnFinishListener onFinishListener) {
        if(mCache == null || mCache.size()<1){
            onFinishListener.doOnfinish(true);
            return;
        }
        SimpleTask<HttpBaseMessage> task = new SimpleTask<HttpBaseMessage>() {
            @Override
            protected HttpBaseMessage doInBackground() {
                try {
                    UserServerAPI serverAPI = new UserServerAPI(context);
                    return serverAPI.postAchievement(mCache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(HttpBaseMessage result) {
                boolean flag;
                if (result != null && StringUtils.isEquals(result.status, "0")) {
                    mCache.clear();
                    saveCache();
                    flag = true;
                } else {
                    flag = false;
                }
                onFinishListener.doOnfinish(flag);

            }
        };

        task.execute();
    }

    public interface OnFinishListener {
        void doOnfinish(boolean result);
    }
}
