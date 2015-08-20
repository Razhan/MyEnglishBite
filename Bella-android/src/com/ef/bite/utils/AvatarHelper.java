package com.ef.bite.utils;

import java.io.ByteArrayInputStream;

import cn.trinea.android.common.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ef.bite.AppConst;
import com.squareup.picasso.Picasso;

/**
 * 用户头像获得方法
 * 
 */
public class AvatarHelper {
	/**
	 * 头像缓存的过期时间: 暂时为1天
	 */
	public final static long AVATAR_EXPIRED_TIME = 1 * 60 * 60 * 1000;

	public final static int AVATAR_SIZE_100 = 100;

	public final static int AVATAR_SIZE_200 = 200;

	/**
	 * 根据头像名获得头像Drawable
	 * 
	 * @param avatarName
	 * @return
	 */
	public static Drawable getAvatar(Context context, String avatarName) {
		try {
			return context.getResources().getDrawable(
					context.getResources().getIdentifier(avatarName,
							"drawable", context.getPackageName()));
		} catch (Exception ex) {

			return null;
		}
	}

	/**
	 * 通过ID获得头像
	 * 
	 * @param context
	 * @param avatarId
	 * @return
	 */
	public static Drawable getAvatarById(Context context, String avatarId) {
		try {
			String name = "friend_icon_" + avatarId;
			return getAvatar(context, name);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 对ImageView加载头像
	 * 
	 * @param imageview
	 * @param uid
	 * @param avatarUrl
	 */
	public static void LoadAvatar(ImageView imageview, String uid,
			String avatarUrl) {
		final Context context = imageview.getContext();
		if (!StringUtils.isBlank(avatarUrl)) {
			Picasso.with(context).load(avatarUrl).into(imageview);
		}
		// final FileStorage storage = new FileStorage(context,
		// AppConst.CacheKeys.Storage_Avatar);
		// String key = AppConst.CacheKeys.Default_Avatart;
		// try {
		// if (!avatarUrl.endsWith(AppConst.Default_Avatar)) {
		// key = KeyGenerator.getKeyFromURL(avatarUrl);
		// key = key == null ? key : uid;
		// }
		// } catch (NullPointerException e) {
		// e.printStackTrace();
		// }
		//
		// // 判断缓存是否过期
		// storage.clearExpiredFiles(AVATAR_EXPIRED_TIME);
		// Bitmap localAvatar = storage.getScaledBitmap(key, AVATAR_SIZE_100,
		// AVATAR_SIZE_100);
		// if (localAvatar != null) {
		// imageview.setImageBitmap(localAvatar);
		// return;
		// }
		// if (avatarUrl == null || avatarUrl.isEmpty()) {
		// imageview.setImageResource(R.drawable.default_avatar);
		// return;
		// }
		// imageview.setImageResource(R.drawable.default_avatar);
		//
		// DownloadingFileTask task = new DownloadingFileTask(context, storage,
		// key, new PostExecuting<String>() {
		// @Override
		// public void executing(String result) {
		// if (result != null && !result.isEmpty()) {
		// Bitmap bitmap = storage.getScaledBitmap(result,
		// AVATAR_SIZE_100, AVATAR_SIZE_100);
		// ;
		// if (bitmap != null)
		// imageview.setImageBitmap(bitmap);
		// else
		// imageview
		// .setImageResource(R.drawable.default_avatar);
		// } else {
		// // url不存在，则使用默认
		// imageview
		// .setImageResource(R.drawable.default_avatar);
		// }
		// }
		// });
		// task.execute(new String[] { avatarUrl });
	}

	/**
	 * 加载大头像
	 */
	public static void LoadLargeAvatar(final ImageView imageview, String uid,
			String avatarUrl) {
		final Context context = imageview.getContext();
		if (!StringUtils.isBlank(avatarUrl)) {
			Picasso.with(context).load(avatarUrl).into(imageview);
		}
		// final FileStorage storage = new FileStorage(context,
		// AppConst.CacheKeys.Storage_Avatar);
		// String key = AppConst.CacheKeys.Default_Avatart;
		// if (!avatarUrl.endsWith(AppConst.Default_Avatar)) {
		// key = KeyGenerator.getKeyFromURL(avatarUrl);
		// key = key == null ? key : uid;
		// }
		// // 判断缓存是否过期
		// storage.clearExpiredFiles(AVATAR_EXPIRED_TIME);
		// Bitmap localAvatar = storage.getBitmap(key);
		// if (localAvatar != null) {
		// imageview.setImageBitmap(localAvatar);
		// return;
		// }
		// if (avatarUrl == null || avatarUrl.isEmpty()) {
		// imageview.setImageResource(R.drawable.default_avatar);
		// return;
		// }
		// imageview.setImageResource(R.drawable.default_avatar);
		// DownloadingFileTask task = new DownloadingFileTask(context, storage,
		// key, new PostExecuting<String>() {
		// @Override
		// public void executing(String result) {
		// if (result != null && !result.isEmpty()) {
		// Bitmap bitmap = storage.getBitmap(result);
		// if (bitmap != null)
		// imageview.setImageBitmap(bitmap);
		// else
		// imageview
		// .setImageResource(R.drawable.default_avatar);
		// } else {
		// // url不存在，则使用默认
		// imageview
		// .setImageResource(R.drawable.default_avatar);
		// }
		// }
		// });
		// task.execute(new String[] { avatarUrl });
	}

	/**
	 * 更新头像缓存
	 * 
	 * @param imageview
	 * @param uid
	 * @param bitmap
	 */
	public static void UpdateAvatar(final ImageView imageview, String uid,
			Bitmap bitmap) {
		if (bitmap != null) {
			Context context = imageview.getContext();
			imageview.setImageBitmap(bitmap);
			FileStorage storage = new FileStorage(context,
					AppConst.CacheKeys.Storage_Avatar);
			InputStream stream = bitmapToStream(bitmap);
			if (stream != null) {
				storage.put(uid, stream);
			}
		}
	}

	/**
	 * Convert Bitmap to InputStream
	 * 
	 * @param bitmap
	 * @return
	 */
	public static InputStream bitmapToStream(Bitmap bitmap) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			return isBm;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
