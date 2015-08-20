package com.ef.bite.dataacces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.res.AssetManager;

public abstract class BaseJsonReader <E>{

	String jsonString;
	public BaseJsonReader(AssetManager assetManager, String uri){
		super();
		StringBuilder sb = new StringBuilder();
		try {
		 BufferedReader input = new BufferedReader(new InputStreamReader((InputStream)assetManager.open(uri, AssetManager.ACCESS_RANDOM)), 8192);
		        String strLine = null;
		        while ((strLine = input.readLine()) != null) {
		        	sb.append(strLine);
		        }
		        input.close();
		        jsonString = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public String getJsonString(){
		return jsonString;
	}
	
	
	public abstract List<E> get();
}
