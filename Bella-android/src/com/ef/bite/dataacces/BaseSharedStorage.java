package com.ef.bite.dataacces;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class BaseSharedStorage<T> {
	String mPreferenceKey;
	Context mContext;
	protected SharedPreferences mSharedPreference;
	protected SharedPreferences.Editor mEditor; 
	public BaseSharedStorage(Context context, String preferenceKey){
		mContext = context;
		mPreferenceKey = preferenceKey;
		mSharedPreference = mContext.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
		mEditor = mSharedPreference.edit();
	}
	
	public abstract T get();
	
	public abstract void put(T t);
	
}
