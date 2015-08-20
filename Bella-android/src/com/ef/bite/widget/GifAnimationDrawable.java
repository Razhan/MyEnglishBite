/**
 * Copyright (C) 2013 Orthogonal Labs, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ef.bite.widget;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;

/**
 * DIF animation drawable
 * @author Allen.Zhu
 *
 */
public class GifAnimationDrawable extends AnimationDrawable
{
	private boolean decoded;
	
	private GifDecoder mGifDecoder;

	private Bitmap mTmpBitmap;

	private int height, width;
	
	private boolean isLoop;
	
	private InputStream mStream;
		
	
	public GifAnimationDrawable(File f) throws IOException
	{
		this(f, false);
	}
	
	public GifAnimationDrawable(InputStream is) throws IOException
	{
		this(is, false);
	}
	
	public GifAnimationDrawable(File f, boolean inline) throws IOException
	{
	    this(new BufferedInputStream(new FileInputStream(f), 32768), inline);
	}
	
	public GifAnimationDrawable(InputStream is, boolean inline) throws IOException
	{
		super();
		mStream = is;
		if(!BufferedInputStream.class.isInstance(mStream)) 
			mStream = new BufferedInputStream(is, 32768);
		decoded = false;
		mGifDecoder = new GifDecoder();
		mGifDecoder.read(mStream);
		mTmpBitmap = mGifDecoder.getFrame(0);
		height = mTmpBitmap.getHeight();
    	width = mTmpBitmap.getWidth();
        addFrame(new BitmapDrawable(mTmpBitmap), mGifDecoder.getDelay(0));
        mGifDecoder.complete();
	}	
	
	public void startAnimation(boolean loop){
		isLoop = loop;
		if(!loop){
			setOneShot(mGifDecoder.getLoopCount() != 0);
			new Thread(loader).start();
		}
		else{
			setOneShot(true);
			//scheduleFuture = scheduler.scheduleAtFixedRate(loader, 0, 800, TimeUnit.MILLISECONDS);
			resetHandler.post(loader);
		}
	}
	
	public void stop(){
		isLoop = false;
		resetHandler.removeCallbacks(loader);
	}
	
	private Handler resetHandler = new Handler();
	
	public boolean isDecoded(){ return decoded; }
	
	private Runnable loader = new Runnable(){
		public void run() 
		{	
			int totalDelay = 0;
			int i, n = mGifDecoder.getFrameCount(), t;
	        for(i=0;i<n;i++){
	            mTmpBitmap = mGifDecoder.getFrame(i);
	            t = mGifDecoder.getDelay(i);
//	            android.util.Log.v("GifAnimationDrawable", "===>Frame "+i+": "+t+"]");
	            addFrame(new BitmapDrawable(mTmpBitmap), t);
	            totalDelay += t;
	        }
	        if(isLoop){
	        	setOneShot(true);
	        	setVisible(true, true);
	        	resetHandler.postDelayed(loader, totalDelay);
	        }
	    }
	};
	
	public int getMinimumHeight(){ return height; }
	public int getMinimumWidth(){ return width; }
	public int getIntrinsicHeight(){ return height; }
	public int getIntrinsicWidth(){ return width; }
}
