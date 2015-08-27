package com.ef.bite.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;

import com.google.gson.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class JsonSerializeHelper {
	public final static String LANGUAGE_ASSETS_JSON = "language/translation.json";

	// / <summary>
	// / JSON反序列化
	// / </summary>
	public static Object JsonDeserialize(String jsonString, Type type) {
		try {
			GsonBuilder builder = new GsonBuilder();

			// Register an adapter to manage the date types as long values
			builder.registerTypeAdapter(Date.class,
					new JsonDeserializer<Date>() {
						public Date deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context)
								throws JsonParseException {
							try {
								String s = json.getAsString();
								SimpleDateFormat fomat = new SimpleDateFormat(
										"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
								Date date = fomat.parse(s);
								return date;
							} catch (ParseException e) {
								e.printStackTrace();
								return null;
							}
						}
					});

			Gson gson = builder.create();
			return gson.fromJson(jsonString, type);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// / <summary>
	// / JSON序列化
	// / </summary>
	public static String JsonSerializer(Object t) {
		try {
			return new Gson().toJson(t);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String JsonLanguageDeserialize(Context context, String params) {
		String item;
		item = getLanguageFromTranslation(context, params);
		if (StringUtils.isBlank(item)) {
			//item = getLanguageFromAssets(context, params);
		}
		return StringUtils.nullStrToEmpty(item);
	}

	private static String getLanguageFromAssets(Context context, String params) {
		String returnStr = "";
		String translation_soure = AssetResourceHelper.getJsonFromAssets(
				context, LANGUAGE_ASSETS_JSON);
		JSONObject jsonObject_tran;
		try {
			jsonObject_tran = new JSONObject(translation_soure);
			if (jsonObject_tran.has("data")) {
				returnStr = jsonObject_tran.getJSONObject("data").optString(
						params);
				if (!StringUtils.isBlank(returnStr)) {
					returnStr = returnStr.replaceAll("%li", "%1$d").trim();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStr;
	}

	private static String getLanguageFromTranslation(Context context,
			String params) {
		String returnLan = "";
		String translation = PreferencesUtils.getString(context,
                AppLanguageHelper.Translation, null);
		if (translation != null) {
			// .replaceAll("<[A-z/ =']*>", "")
			// .replaceAll("[A-z]*", "").replaceAll("%", "")
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(translation);
//				if (jsonObject.has("data")) {
//					returnLan = jsonObject.getJSONObject("data").optString(
//							params);
//					if (!StringUtils.isBlank(returnLan)) {
//						if (returnLan.contains("%@")) {
//							returnLan = returnLan.replace("%@", "%1$s").trim();
//						}
//						// returnLan = returnLan.replace("%li", "%1$d").trim();
//					}
//				}
                jsonObject = new JSONObject(translation);
                returnLan = jsonObject.optString(params);
                if (!StringUtils.isBlank(returnLan)) {
                    if (returnLan.contains("%@")) {
                        returnLan = returnLan.replace("%@", "%1$s").trim();
                    }
                    // returnLan = returnLan.replace("%li", "%1$d").trim();
                }

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnLan;
	}

    public static JSONObject getJSONObjectFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            InputStream is = new FileInputStream(new File(path));

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

            return languagejson;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


	// @SuppressWarnings({ "null" })
	// public static List<Chunk> getStatusChunk(Context context,
	// String statesString) {
	// List<Chunk> chunks = null;
	// FileStorage courseStorage = new FileStorage(context,
	// AppConst.CacheKeys.Storage_Course);
	// try {
	// String userprogress = PreferencesUtils.getString(context,
	// AppConst.CacheKeys.Storage_UserProGress, null);
	// if (userprogress != null && !userprogress.equals("")) {
	// HttpProgress httpProgress = (HttpProgress) JsonDeserialize(
	// userprogress, HttpProgress.class);
	// if (httpProgress.data.progress_list != null
	// && !httpProgress.data.progress_list.equals("")) {
	// for (int i = 0; i < httpProgress.data.progress_list.size(); i++) {
	// if (httpProgress.data.progress_list.get(i).status
	// .equals(statesString)) {
	// String lesson_id = httpProgress.data.progress_list
	// .get(i).lesson_id;
	// ChunkBiz chunkBiz = new ChunkBiz(context);
	// Chunk chunk = chunkBiz.loadChunkIDFromStorage(
	// context, courseStorage.getStorageFolder(),
	// lesson_id);
	// chunks.add(chunk);
	// }
	// }
	// } else {
	// String nextcourse = PreferencesUtils.getString(context,
	// AppConst.CacheKeys.Storage_NextCourse, null);
	// HttpCourseResponse httpCourseResponse = (HttpCourseResponse)
	// JsonSerializeHelper
	// .JsonDeserialize(nextcourse,
	// HttpCourseResponse.class);
	// ChunkBiz chunkBiz = new ChunkBiz(context);
	// if (httpCourseResponse.status == "0"
	// && httpCourseResponse.data != null) {
	// for (int i = 0; i < httpCourseResponse.data.size(); i++) {
	// Chunk chunk = chunkBiz.loadChunkIDFromStorage(
	// context, courseStorage.getStorageFolder(),
	// httpCourseResponse.data.get(i).course_id);
	// chunks.add(chunk);
	// }
	// }
	// }
	// }
	// } catch (NullPointerException e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return chunks;
	// }
}
