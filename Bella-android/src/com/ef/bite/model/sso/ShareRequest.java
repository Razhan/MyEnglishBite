package com.ef.bite.model.sso;

import android.graphics.Bitmap;

public class ShareRequest {

	/**
	 * 分享类型：
	 * 文本/图片/网页/音频地址/视频地址
	 */
	public int Type;
	
	/**
	 * 分享文本
	 */
	public String Title;
	
	/**
	 * 描述信息
	 */
	public String Description;
	
	/**
	 * 分析图片
	 */
	public Bitmap Image;
	
	/**
	 * 分享链接
	 */
	public String Link;
	
	/**
	 * 图片连接
	 */
	public String LocalImage;
	
	/***
	 * 远程图片地址
	 */
	public String ImageURL;
	
}
