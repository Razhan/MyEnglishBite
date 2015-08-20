package com.ef.bite.business;

import android.content.Context;

import com.ef.bite.dataacces.ConfigSharedStorage;
import com.ef.bite.model.ConfigModel;

public class GlobalConfigBLL {
	Context mContext;
	public GlobalConfigBLL(Context context){ mContext = context;}
	
	
	/**
	 * 获得全局配置
	 * @return
	 */
	public ConfigModel getConfigModel(){
		ConfigSharedStorage storage = new ConfigSharedStorage(mContext);
		return storage.get();
	}
	
	/**
	 * 设置全局配置
	 * @param config
	 */
	public void setConfigModel(ConfigModel config){
		ConfigSharedStorage storage = new ConfigSharedStorage(mContext);
		storage.put(config);
	}
}
