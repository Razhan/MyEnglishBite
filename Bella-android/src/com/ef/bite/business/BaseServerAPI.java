package com.ef.bite.business;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.ef.bite.AppConst;

public abstract class BaseServerAPI {

	protected Map<String, String> headerMap = new HashMap<String, String>();

	public BaseServerAPI(Context context) {
		headerMap.put("bella_id", AppConst.CurrUserInfo.UserId);
//		headerMap.put("token", AppConst.CurrUserInfo.Token);
		headerMap.put("device_id", AppConst.GlobalConfig.DeviceID);
		headerMap.put("language", AppConst.GlobalConfig.Language);
		headerMap.put("app_version", AppConst.GlobalConfig.App_Version);
		headerMap.put("system", AppConst.GlobalConfig.OS);
		headerMap.put("app_store", AppConst.HeaderStore.StoreName);
	}

}
