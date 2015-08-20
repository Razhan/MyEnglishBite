package com.ef.bite.dataacces;


import android.content.Context;

public class DashboardFriendsSharedStorage extends BaseSharedStorage<String> {

	public DashboardFriendsSharedStorage(Context context, String uid) {
		super(context, "Dashboard_Friends");
		mPreferenceKey = "dashboard_friends_cache_" + uid;
	}

	@Override
	public String get() {
		try{
			return mSharedPreference.getString(mPreferenceKey, null);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(String t) {
		try{
			if(t == null)
				return;
			mEditor.putString(mPreferenceKey, t);
			mEditor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
