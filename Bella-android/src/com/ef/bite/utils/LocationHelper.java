package com.ef.bite.utils;

import java.util.Map;

import android.content.Context;

/**
 * 地理位置工具类
 * 
 * @author Allen.Zhu
 * 
 */
public class LocationHelper {

	public final static String COUNTRY_EN_ASSET_JSON = "country/en_country.json";

	public final static String COUNTRY_ZH_ASSET_JSON = "country/zh_country.json";

	public static Map<String, String> countryMap;

	public static String mLanguage;

	@SuppressWarnings("unchecked")
	public static Map<String, String> loadCountryJson(Context context,
			String language) {
		String jsonString = null;
		mLanguage = language;
		if (language.equals(AppLanguageHelper.ZH_CN))
			jsonString = AssetResourceHelper.getJsonFromAssets(context,
					COUNTRY_ZH_ASSET_JSON);
		else
			jsonString = AssetResourceHelper.getJsonFromAssets(context,
					COUNTRY_EN_ASSET_JSON);
		if (jsonString != null) {
			try {
				countryMap = (Map<String, String>) JsonSerializeHelper
						.JsonDeserialize(jsonString, Map.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return countryMap;
	}

	/**
	 * 
	 * @param context
	 * @param code
	 * @param language
	 * @return
	 */
	public static String getCountryNameByCode(Context context, String code,
			String language) {
		if (code == null)
			return "";
		try {
			if (countryMap == null
					|| (mLanguage != null && !mLanguage.equals(language))) {
				loadCountryJson(context, language);
			}
			String countryName = countryMap.get(code.toUpperCase());
			return countryName == null ? code : countryName;
		} catch (Exception ex) {
			ex.printStackTrace();
			return code;
		}
	}

}
