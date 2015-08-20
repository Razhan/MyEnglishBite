package com.ef.bite.dataacces;

import android.content.Context;

import com.ef.bite.model.ConfigModel;

public class ConfigSharedStorage extends BaseSharedStorage<ConfigModel> {
	
	final static String LogTag = "ConfigSharedStorage";
	public ConfigSharedStorage(Context context) {
		super(context, "Config_Preference");
		mPreferenceKey = "local_config_setting";
	}

	@Override
	public ConfigModel get() {
		try{
			String configJson = mSharedPreference.getString(mPreferenceKey, null);
			if(configJson == null || configJson.isEmpty())
				return null;
			ConfigModel config = new ConfigModel();
			config.parse(configJson);
			return config;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(ConfigModel t) {
		try{
			if(t == null)
				return;
			String configJson = t.toJson();
			mEditor.putString(mPreferenceKey, configJson);
			mEditor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
