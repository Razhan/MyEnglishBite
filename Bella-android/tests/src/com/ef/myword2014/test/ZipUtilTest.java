package com.ef.myword2014.test;

import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.ef.myword2014.utils.*;


public class ZipUtilTest extends AndroidTestCase {

	@Test
	public void test() {
		// copy zip to local
		try{
			Context context = this.getContext();
			InputStream is = context.getResources().getAssets().open("cn_zip_test.zip");
			FileStorage storage = new FileStorage(context, "zip_test");
			String key = "ziputiltest_test";
			storage.put(key, is);
			Map<String, String> result = ZipUtil.getContentMapByZip(storage.findFilePath(key),"UTF-8");
			String content = result.get("json_cn.json");
			
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
		
	}
	
	
	public void testDecompress(){
		try{
			Context context = this.getContext();
			InputStream is = context.getResources().getAssets().open("cn_zip_test.zip");
			FileStorage storage = new FileStorage(context, "zip_test");
			String key = "cn_zip_test.zip";
			storage.put(key, is);
			ZipUtil.decompress(storage.findFilePath(key), storage.getStorageFolder(), "UTF-8");
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
	}
}
