package com.ef.bite.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ef.bite.AppConst;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.JsonSerializeHelper;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Ran on 8/11/2015.
 */
public class CountryBLL {

    public static String countryMapping(String countrycode) {
        JSONObject obj = JsonSerializeHelper.getJSONObjectFromSD(android.os.Environment.getExternalStorageDirectory() + File.separator +
                AppConst.CacheKeys.RootStorage + File.separator
                + AppConst.CacheKeys.Storage_Language + File.separator + "language_mapping.json");

        String country = obj.optString(countrycode);

        if (country.isEmpty()) {
            return "cn";
        } else {
            return country;
        }
    }

    public static String getCountryCode(FileStorage languageStorage, String country){
        try {
            InputStream is = new FileInputStream(new File(languageStorage.getStorageFolder()+File.separator+"language_mapping.json"));
            if (is == null)
                return null;
            InputStreamReader inputStreamReader = new InputStreamReader(is,
                    "UTF-8");
            char[] buffer = new char[is.available()];
            String jsonString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStreamReader.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, buffer.length));
            }
            jsonString = stringBuffer.toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.optString(country);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getLocalCountry(String countrykey, String path, Context mContext) {
        String sys_language = AppLanguageHelper.getSystemLaunguage(mContext);
        sys_language = CountryBLL.countryMapping(sys_language);
        JSONObject obj = JsonSerializeHelper.getJSONObjectFromSD(path + sys_language + ".json"
        );

        String countryvalue = obj.optString(countrykey);

        if (countryvalue.isEmpty()) {
            return "UN";
        } else {
            return countryvalue;
        }
    }


    public static Bitmap getLoacalBitmap(String country, String path) {
        try {
            String url = path + "image" + File.separator + country + ".png";

            if (! (new File(url).exists())) {
                url = path + "image" + File.separator + "uk" + ".png";
            }

            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
