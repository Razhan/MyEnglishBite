package com.ef.bite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyGenerator {

	/**
	 * 根据时间产生Key值
	 * @return
	 */
	public static String getKeyFromDateTime(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
		return format.format(date);
	}
	
	
	/**
	 * 通过图片地址产生一个hash key
	 * @return
	 */
	public static String getKeyFromURL(String url){
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(url.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
}
