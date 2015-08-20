package com.ef.bite.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.json.JSONObject;

import cn.trinea.android.common.util.PreferencesUtils;

import com.ef.bite.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ef.bite.AppConst;

/**
 * App当前语言工具类
 * 
 * @author Allen.Zhu
 * 
 */
public class AppLanguageHelper {

	public final static String EN = "en";

	public final static String FR = "fr";
	public final static String ZH_CN = "zh-cn";
	public final static String Translation = "translation";
	public final static String App_Res_Culture = "appresculture";

	/**
	 * 获得App当前的语言
	 * 
	 * @param context
	 * @return
	 */
	// public static String getLaunguage(Context context) {
	// String language = context.getResources().getConfiguration().locale
	// .getLanguage().toLowerCase();
	// if (language.equals(ZH_CN) || language.equals("zh")
	// || language.equals("cn"))
	// return ZH_CN;
	// else
	// return EN;
	// }

	/**
	 * 细分获得App系统的语言
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getSystemLaunguage(Context context) {
		String language = context.getResources().getConfiguration().locale
				.getLanguage().toLowerCase();
		String country = context.getResources().getConfiguration().locale
				.getCountry().toLowerCase();
		if (language.equals(country) || language.equals("en")) {
			Log.i("language", language);
			return language;
		} else {
			Log.i("language", language + "-" + country);
			return language + "-" + country;
		}
	}

	/**
	 * 第一次打开应用程序从设置中加载App语言
	 * 
	 * @param context
	 */
	public static void loadLanguageFirstTime(Context context, int multiLangType) {
		// String currentLang = getLaunguage(context);
		String currentLang = getSystemLaunguage(context);
		switch (multiLangType) {
		case AppConst.MultiLanguageType.Chinese:
			if (!currentLang.equals(ZH_CN))
				updateLaunguage(context, ZH_CN);
			break;
		case AppConst.MultiLanguageType.English:
			if (!currentLang.equals(EN))
				updateLaunguage(context, EN);
			break;
		default:
			if (!currentLang.equals(EN))
				updateLaunguage(context, EN);
			break;
		}
	}

	/**
	 * 更改App当前的语言
	 * 
	 * @param context
	 * @param targetLang
	 */
	public static void updateLaunguage(Context context, String targetLang) {
		String language = "";
		String country = "";
		try {
			if (targetLang.contains("-")) {
				String[] target = targetLang.split("-");
				language = target[0];
				country = target[1];
			} else {
				language = targetLang;
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
			language = targetLang;
			country = "";
		}

		Resources res = context.getResources();
		// Change locale settings in the app.
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(language, country);
		res.updateConfiguration(conf, dm);
	}

	/**
	 * 根据多语言类型获得语言文本显示
	 * 
	 * @param context
	 * @param multiLangType
	 * @return
	 */
	public static String getLanguageDisplayByType(Context context,
			int multiLangType) {
		switch (multiLangType) {
		case AppConst.MultiLanguageType.Chinese:
			return context.getString(R.string.settings_language_chinese);
		case AppConst.MultiLanguageType.English:
			return context.getString(R.string.settings_language_english);
		default:
			return context.getString(R.string.settings_language_default);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static String getSystemLanguage() {
		String language = java.util.Locale.getDefault().getLanguage()
				.toLowerCase();
		if (language.equals(ZH_CN) || language.equals("zh")
				|| language.equals("cn"))
			return ZH_CN;
		else
			return EN;
	}

	/**
	 * 从本地存储加载language
	 * 
	 * @param context
	 * @param languagePath
	 */
	public static void loadLanguageFromStorage(Context context,
			String languagePath) {
		String[] languageDirs = null;
		try {
			languageDirs = new File(languagePath).list();
			for (String dir : languageDirs) {
				Log.e("dir", dir);
				Log.e("file", languagePath);
				if (dir.contains(".json")) {
					InputStream is = new FileInputStream(new File(languagePath
							+ File.separator + dir));
					loadLanguageStream(context, dir, languagePath
							+ File.separator + dir, is, false);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public static void loadLanguageFromStorage(Context context, String languagePath, String country) {
        String[] languageDirs = null;
        try {
            languageDirs = new File(languagePath).list();
            for (String dir : languageDirs) {
                Log.e("dir", dir);
                Log.e("file", languagePath);
                if (dir.contains(country)) {
                    InputStream is = new FileInputStream(new File(languagePath + dir));
                    loadLanguageStream(context, dir, languagePath
                            + dir, is, false);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private static boolean loadLanguageStream(Context context,
			String languageFileName, String baseDir, InputStream is,
			boolean preinstall) {

		try {
			if (is == null)
				return false;
			InputStreamReader inputStreamReader = new InputStreamReader(is,
					"UTF-8");
			char[] buffer = new char[is.available()];
			String jsonString;
			StringBuffer stringBuffer = new StringBuffer();
			while ((inputStreamReader.read(buffer)) != -1) {
				stringBuffer.append(new String(buffer, 0, buffer.length));
			}
			jsonString = stringBuffer.toString();
			JSONObject languagejson = new JSONObject(jsonString);
			PreferencesUtils.putString(context, Translation,
					languagejson.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}



}
