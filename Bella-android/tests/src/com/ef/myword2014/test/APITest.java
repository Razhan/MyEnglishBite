package com.ef.myword2014.test;

import org.junit.Assert;

import com.ef.myword2014.business.task.LoginTask;
import com.ef.myword2014.business.task.PostExecuting;
import com.ef.myword2014.dataacces.mode.httpMode.LoginMessage;
import com.ef.myword2014.dataacces.mode.httpMode.UserMode;

import android.test.AndroidTestCase;
import android.test.UiThreadTest;

public class APITest extends AndroidTestCase {

	@UiThreadTest
	public void test(){
		LoginTask task = new LoginTask(this.getContext() ,new PostExecuting<LoginMessage>(){
			@Override
			public void executing(LoginMessage result) {
				Assert.assertTrue(result!=null);
				Assert.assertTrue(result.status.equals("0"));
			}
			
		});
		UserMode mode = new UserMode();
		mode.username = "allen";
		mode.password = "asdf";
		mode.login_type = "bella";
		mode.provider_type = "wechat";
		task.execute(new UserMode[]{ mode});
	}
}
