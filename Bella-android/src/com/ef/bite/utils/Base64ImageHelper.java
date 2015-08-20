package com.ef.bite.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64ImageHelper {

	/*
	 * Image to String
	 */
	public static String encodeTobase64(Bitmap image, boolean isPNG)
	{
	    Bitmap immagex=image;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    if(isPNG)
	    	immagex.compress(CompressFormat.PNG, 100, baos);
	    else
	    	immagex.compress(CompressFormat.JPEG, 100, baos);
	    byte[] b = baos.toByteArray();
	    String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
	    return imageEncoded;
	}
	
	/*
	 * String to Image
	 */
	public static Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}
}
