package com.ef.bite.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;

import com.ef.bite.AppConst;
import com.ef.bite.business.task.DownloadingFileTask;
import com.ef.bite.business.task.PostExecuting;

public class ImageUtils {
	
	private final static String TAG = "ImageUtils";
	
	/**
	 * 下载图片并加载到ImageView中
	 * 
	 * @param imageView
	 * @param imageUri
	 */
//	public static void loadImageByUri(final ImageView imageView, String imageUri){
//		try{
//			final Context context = imageView.getContext();
//			final FileStorage storage = new FileStorage(context,AppConst.CacheKeys.Storage_Cache);
//			DownloadingFileTask task = new DownloadingFileTask(context,new PostExecuting<String>(){
//				@Override
//				public void executing(String result) {
//					if(result != null && !result.isEmpty()){
//						Bitmap bitmap = storage.getBitmap(result);
//						if(bitmap!=null)
//							imageView.setImageBitmap(bitmap);
//					}
//				}
//			});
//			task.execute(new String[]{ imageUri });
//		}catch (Exception e) {
//			e.printStackTrace();
//			Log.v(TAG, "getbitmap bmp fail---");
//		}
//	}
	
	/**
	 * Convert Bitmap to InputStream
	 * @param bitmap
	 * @return
	 */
	public static InputStream bitmapToStream(Bitmap bitmap){
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			return isBm;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获得正确的image方向的Bitmap
	 * @param path
	 * @return
	 */
	public static  Bitmap decodeFile(String path, int newWidth, int newHeight) {//you can provide file path here 
		try{
			Bitmap realImage = BitmapFactory.decodeFile(path);
	
			ExifInterface exif=new ExifInterface(path);
	
			Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
	
			    realImage=scaleAndRotateBitmap(realImage, 90, newWidth,newHeight);
			}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
			    realImage=scaleAndRotateBitmap(realImage, 270, newWidth,newHeight);
			}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
			    realImage=scaleAndRotateBitmap(realImage, 180, newWidth,newHeight);
			}else
				realImage = scaleBitmap(realImage,newWidth,newHeight);
			return realImage;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}

    }
	
	/**
	 * 按照指定大小缩放
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth, int newHeight ){
		 int w = bitmap.getWidth();
		  int h = bitmap.getHeight();
		  float scaleX = newWidth / (float) bitmap.getWidth();
		  float scaleY = newHeight / (float) bitmap.getHeight();
		  float pivotX = 0;
		  float pivotY = 0;
		  Matrix mtx = new Matrix();
		  mtx.setScale(scaleX, scaleY, pivotX, pivotY);
		  return Bitmap.createBitmap(bitmap, 0, 0,  w, h, mtx, true);
	}
	
	public static Bitmap scaleAndRotateBitmap(Bitmap bitmap, int degree, int newWidth, int newHeight) {
		 // Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
		  int w = bitmap.getWidth();
		  int h = bitmap.getHeight();
		  float scaleX = newWidth / (float) bitmap.getWidth();
		  float scaleY = newHeight / (float) bitmap.getHeight();
		  float pivotX = 0;
		  float pivotY = 0;

		  Matrix mtx = new Matrix();
		  mtx.setScale(scaleX, scaleY, pivotX, pivotY);
		  mtx.postRotate(degree);
		  
		  return Bitmap.createBitmap(bitmap, 0, 0,  w, h, mtx, true);
	}

}
