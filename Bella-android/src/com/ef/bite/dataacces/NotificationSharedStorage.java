package com.ef.bite.dataacces;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;

import com.ef.bite.dataacces.mode.httpMode.HttpNotification;
import com.ef.bite.dataacces.mode.httpMode.HttpNotification.NotificationData;
import com.ef.bite.utils.JsonSerializeHelper;

public class NotificationSharedStorage extends BaseSharedStorage<List<HttpNotification.NotificationData>> {

	public NotificationSharedStorage(Context context, String uid) {
		super(context, "Notification");
		mPreferenceKey = "notification_cache_" + uid;
	}

	@Override
	public List<NotificationData> get() {
		try{
			String json = mSharedPreference.getString(mPreferenceKey, null);
			if(json==null)
				return null;
			else{
				List<NotificationData> notifyList = new  ArrayList<NotificationData>();
				JSONArray jsonArray = new JSONArray(json);
				for(int index=0; index < jsonArray.length(); index++ ){
					String notifyJson = jsonArray.getString(index);
					NotificationData data = (NotificationData)JsonSerializeHelper.JsonDeserialize(notifyJson, NotificationData.class);
					notifyList.add(data);
				}
				return notifyList;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(List<NotificationData> t) {
		try{
			String json = JsonSerializeHelper.JsonSerializer(t);
			mEditor.putString(mPreferenceKey, json);
			mEditor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
