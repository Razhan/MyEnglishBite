package com.ef.bite.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ef.bite.AppConst;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class AssetResourceHelper {

	/*
	 * 从Asset中获得Bitmap
	 */
	public static Bitmap getBitmapFromAssets(String fileName, Context context) {
		try {
			AssetManager assetManager = context.getAssets();
			InputStream istr = assetManager.open(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(istr);
			istr.close();
			return bitmap;
		} catch (Exception ex) {
			Log.e("AssetResourceHelper",
					"Fail to get bitmap from asset of path: " + fileName);
			return null;
		}
	}

	/**
	 * 从Storage中获得Bitmap
	 * 
	 */
	public static Bitmap getBitmapFromStorage(String chunkName,
			String CharacterAvater, Context mContext) {
		FileStorage courseStorage = new FileStorage(mContext,
				AppConst.CacheKeys.Storage_Course);
		String pathName = courseStorage.getStorageFolder() + File.separator
				+ chunkName + File.separator + CharacterAvater;
		Bitmap bitmap = null;
		try {
			try {
				bitmap = BitmapFactory.decodeFile(pathName);
				return bitmap;
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("getBitmapFromStorage", "OutOfMemoryError");
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("AssetResourceHelper_getBitmapFromStorage",
					"Fail to get bitmap from asset of path: " + chunkName
							+ File.separator + CharacterAvater);
			return null;
		}
	}

	public static Bitmap getScaleBitmapFromAssets(String fileName,
			Context context, int inSampleSize) {
		try {
			AssetManager assetManager = context.getAssets();
			InputStream istr = assetManager.open(fileName);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(istr, null, options);
			options.outWidth = options.outWidth / inSampleSize;
			options.outHeight = options.outHeight / inSampleSize;
			if (options.inSampleSize < 1)
				options.inSampleSize = 1;
			else
				options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeStream(istr, null, options);
			istr.close();
			return bitmap;
		} catch (Exception ex) {
			Log.e("AssetResourceHelper",
					"Fail to get bitmap from asset of path: " + fileName);
			return null;
		}
	}

	/*
	 * 获得Asset中资源Uri
	 */
	public static Uri getUriFromAssets(String fileName) {
		return Uri.parse("file:///android_asset/" + fileName);
	}

	/**
	 * 获得Asset中资源绝对路径
	 * 
	 * @param relativeName
	 * @return
	 */
	public static String getPathFromAssets(String relativeName) {
		return "file:///android_asset/" + relativeName;
	}

	/**
	 * 
	 * @param relativeFilePath
	 * @return
	 */
	public static String getJsonFromAssets(Context context,
			String relativeFilePath) {
		try {
			StringBuilder json = new StringBuilder();
			AssetManager assetManager = context.getAssets();
			InputStream istr = assetManager.open(relativeFilePath);
			int BUFFER_SIZE = 8192;
			String UTF8 = "utf8";
			BufferedReader br = new BufferedReader(new InputStreamReader(istr,
					UTF8), BUFFER_SIZE);
			String str;
			while ((str = br.readLine()) != null) {
				json.append(str);
			}
			istr.close();
			return json.toString();
		} catch (Exception ex) {
			Log.e("AssetResourceHelper",
					"Fail to get Json from asset of relative path: "
							+ relativeFilePath);
			return null;
		}
	}
}
