package com.ef.bite.dataacces;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;

import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.JsonSerializeHelper;

public class FriendsSharedStorage extends BaseSharedStorage<List<HttpGetFriendData>> {

	public final static int TYPE_FRIEND_LIST = 0;
	public final static int TYPE_FRIEND_LEADERBOARD = 1;
	
	private int currentType = TYPE_FRIEND_LIST;
	
	public FriendsSharedStorage(Context context,String uid, int type) {
		super(context, "FriendList");
		currentType = type;
		mPreferenceKey = "friend_list_"+ currentType + "_cache_" + uid;
	}

	@Override
	public List<HttpGetFriendData> get() {
		try{
			String json = mSharedPreference.getString(mPreferenceKey, null);
			if(json==null)
				return null;
			else{
				List<HttpGetFriendData> friendList = new  ArrayList<HttpGetFriendData>();
				JSONArray jsonArray = new JSONArray(json);
				for(int index=0; index < jsonArray.length(); index++ ){
					String notifyJson = jsonArray.getString(index);
					HttpGetFriendData data = (HttpGetFriendData)JsonSerializeHelper.JsonDeserialize(notifyJson, HttpGetFriendData.class);
					friendList.add(data);
				}
				return friendList;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(List<HttpGetFriendData> t) {
		if(t==null)
			return;
		try{
			String json = JsonSerializeHelper.JsonSerializer(t);
			mEditor.putString(mPreferenceKey, json);
			mEditor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
