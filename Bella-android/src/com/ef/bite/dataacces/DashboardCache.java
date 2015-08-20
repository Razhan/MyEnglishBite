package com.ef.bite.dataacces;

import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.StringUtils;

import java.util.List;

/**
 * The storage for caching dashboard
 * Created by yang on 15/6/17.
 */
public class DashboardCache {
    Context context;
    private static DashboardCache INSTANCE = new DashboardCache();

    private DashboardCache() {
    }

    public static DashboardCache getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * save dashboard into share preference
     * @param httpDashboard
     */
    public void save(HttpDashboard httpDashboard) {
        PreferencesUtils.putString(context, AppConst.CacheKeys.CACHE_DASHBOARD + AppConst.CurrUserInfo.UserId, JsonSerializeHelper.JsonSerializer(httpDashboard));
    }

    public HttpDashboard load() {
        HttpDashboard cache = (HttpDashboard) JsonSerializeHelper.JsonDeserialize(
                PreferencesUtils.getString(context, AppConst.CacheKeys.CACHE_DASHBOARD + AppConst.CurrUserInfo.UserId), HttpDashboard.class);
        if (cache == null) {
            cache = new HttpDashboard();
        }
        return cache;
    }

    public void removeLesson(String chunkId) {
        HttpDashboard httpDashboard = load();
        if (httpDashboard != null) {
            remove(httpDashboard.data.new_lessons, chunkId);
            httpDashboard.data.new_lesson_count--;
            save(httpDashboard);
        }
    }

    public void removeRehearsal(String chunkId) {
        HttpDashboard httpDashboard = load();
        if (httpDashboard != null) {
            remove(httpDashboard.data.new_rehearsals, chunkId);
            httpDashboard.data.new_rehearsal_count--;
            save(httpDashboard);
        }
    }

    private void remove(List<HttpDashboard.Lesson> lessons, String chunkId) {
        for (int i = 0; i < lessons.size(); i++) {
            if (StringUtils.isEquals(lessons.get(i).course_id, chunkId)) {
                lessons.remove(i);
            }
        }
    }
}
