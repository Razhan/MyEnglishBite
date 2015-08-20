package com.ef.myword2014.test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.ef.myword2014.business.task.LoginTask;
import com.ef.myword2014.business.task.PostExecuting;
import com.ef.myword2014.dataacces.mode.httpMode.LoginMessage;
import com.ef.myword2014.dataacces.mode.httpMode.UserMode;
import com.ef.myword2014.utils.AssetResourceHelper;
import com.ef.myword2014.utils.HighLightStringHelper;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.UiThreadTest;
import android.text.SpannableStringBuilder;

public class CommonMethodTest extends AndroidTestCase {

	@Test
	public void test() {
		try{
			Uri uri = AssetResourceHelper.getUriFromAssets("cn_zip_test.zip");
			File f = new File(uri.toString());
			boolean existedA = f.exists();
			File jfile = new File("file://android_asset/"+"cn_zip_test.zip");
			boolean existedB = jfile.exists();
			FileInputStream fis = new FileInputStream(f);
			fis.getFD();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Test
	public void highLightTest(){
        String normalTest = "<h>Can I get</h> some butter for my bread";
        SpannableStringBuilder ss = HighLightStringHelper.getHighLightString(mContext,normalTest);
        assertTrue(true);

	}
	
	
	@Test
	public void loginTest(){
		LoginTask task = new LoginTask(this.getContext() ,new PostExecuting<LoginMessage>(){
			@Override
			public void executing(LoginMessage result) {
				
			}
			
		});
		UserMode mode = new UserMode();
		mode.username = "allen";
		mode.password = "asdf";
		mode.login_type = "bella";
		mode.provider_type = "wechat";
		task.execute(new UserMode[]{ mode});
	}
	
	@Test
	public void dateFormatTest(){
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date now = new Date();
		String date = fmt.format(now);
		assertTrue(true);
	}
	
	public void zeroDateTest(){
		String zeroDate = "1970-01-01T00:00:00+00:00";
	}
}
