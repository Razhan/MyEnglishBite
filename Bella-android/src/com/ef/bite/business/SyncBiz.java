package com.ef.bite.business;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.dao.TraceModeDao;
import com.ef.bite.dataacces.dao.UserProgressStatusDao;
import com.ef.bite.dataacces.mode.TraceMode;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpTrackData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class SyncBiz {

	/** 一次最多上传tracing的个数 **/
	public final static int MAX_TRACING_SYNC_NUMBER = 500;
	/** Trace的本地保存时间，1天 **/
	public final static int TRACE_CACHE_TIME = 1 * 24 * 60 * 60 * 1000;

	UserProgressStatusDao userProgressStatusDao;
	UserServerAPI userServerAPI;
	UserScoreBiz userScoreBiz;
	TraceModeDao traceModeDao;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public SyncBiz(Context context) {
		userProgressStatusDao = new UserProgressStatusDao(context);
		userServerAPI = new UserServerAPI(context);
		userScoreBiz = new UserScoreBiz(context);
		traceModeDao = new TraceModeDao(context);
	}


	/**
	 * 上传用户的行为信息
	 * 
	 * @param uid
	 * @return
	 */
	public HttpBaseMessage postTracking(String uid) {
		try {
			// group id - track data
			Map<String, HttpTrackData> groupTrackDataMap = new ArrayMap<String, HttpTrackData>();
			List<TraceMode> traceModes = traceModeDao.getUnSyncTopN(
					MAX_TRACING_SYNC_NUMBER, uid);
			List<HttpTrackData> traceDataList = new ArrayList<HttpTrackData>();
			for (TraceMode traceMode : traceModes) {
				String groupId = traceMode.getGroupId();
				HttpTrackData httpTrackData = null;
				if (groupTrackDataMap.containsKey(groupId)) {
					httpTrackData = groupTrackDataMap.get(groupId);
				} else {
					httpTrackData = new HttpTrackData();
					if (!uid.equals("0"))
						httpTrackData.bella_id = traceMode.getUid();
					httpTrackData.device_id = AppConst.GlobalConfig.DeviceID;
					httpTrackData.items = new ArrayMap<String, String>();
					httpTrackData.items.put("system", AppConst.GlobalConfig.OS);
					httpTrackData.items.put("device_id",
							AppConst.GlobalConfig.DeviceID);
					httpTrackData.items.put("device_brand",
							AppConst.GlobalConfig.Device_Brand);
					httpTrackData.items.put("device_model",
							AppConst.GlobalConfig.Device_Model);
					httpTrackData.items.put("os_version",
							AppConst.GlobalConfig.OS_Version);
					httpTrackData.items.put("app_store",
							AppConst.HeaderStore.StoreName);
					groupTrackDataMap.put(groupId, httpTrackData);
				}
				if (httpTrackData != null
						&& httpTrackData.items != null
						&& !httpTrackData.items.containsKey(traceMode
								.getEvent()))
					httpTrackData.items.put(traceMode.getEvent(),
							traceMode.getValue());
			}

			for (Object o : groupTrackDataMap.keySet()) {

				HttpTrackData httpTrackData = groupTrackDataMap.get(o);
				traceDataList.add(httpTrackData);
			}

			HttpBaseMessage httpBaseMessage = userServerAPI.postTrack(
					traceDataList, uid);
			if (httpBaseMessage != null) {
				if ("0".equals(httpBaseMessage.status)) {
					traceModeDao.setSync(uid, new Date().getTime());
				}
			}
			// 清理1天之前同步过的记录
			long sevendaysAgo = System.currentTimeMillis() - TRACE_CACHE_TIME;
			traceModeDao.clearTrace(uid, sevendaysAgo);
			return httpBaseMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
