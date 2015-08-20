package com.ef.bite.business.task;

import android.content.Context;
import android.util.Log;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.HttpServerAddress;
import com.ef.bite.utils.AppUtils;
import com.ef.bite.utils.HttpRestfulClient;
import com.ef.bite.utils.StringUtils;
import com.litesuits.android.async.AsyncTask;
import com.litesuits.android.async.SimpleTask;
import com.litesuits.android.async.TaskExecutor;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yang on 15/5/12.
 */
public class HostCompetition {
	private final static String TAG = "EF_HOST";
	private String address;
	private onFinishListener onFinishListener;
	private List<AsyncTask> taskList = new LinkedList<AsyncTask>();
	private Context context;

	public HostCompetition(Context context) {
		this.context = context;
	}

	public void start() {
		taskList.clear();
//		SimpleTask<?> task1 = getTask(AppConst.EFAPIs.ETHost + "android/"
//				+ AppUtils.getVersion(context));
//		SimpleTask<?> task2 = getTask(AppConst.EFAPIs.HK_ETHost + "android/"
//				+ AppUtils.getVersion(context));

        SimpleTask<?> task1 = getTask("http://bella-live-web-lb-1387001753.us-west-2.elb.amazonaws.com/api/bella/2/config");
        SimpleTask<?> task2 = getTask("http://42.96.250.52/api/bella/2/config");


		taskList.add(task1);
		taskList.add(task2);

		SimpleTask<String> onFinishTask = new SimpleTask<String>() {
			@Override
			protected String doInBackground() {
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if (onFinishListener == null) {
					return;
				}
				onFinishListener.onFinish(address);
//				 Log.v(TAG,"---onFinish return:"+address);
			}
		};
		TaskExecutor.newCyclicBarrierExecutor().put(task1).put(task2)
				.start(onFinishTask);
	}

	private SimpleTask<HttpServerAddress> getTask(final String url) {
		SimpleTask<HttpServerAddress> task = new SimpleTask<HttpServerAddress>() {
			@Override
			protected HttpServerAddress doInBackground() {
				return postRequest(url);
			}

			@Override
			protected void onPostExecute(HttpServerAddress serverAddress) {
				if (serverAddress == null) {
					return;
				}
				if (StringUtils.isEquals(serverAddress.status, "0")
						&& !StringUtils.isBlank(serverAddress.data.domain)) {
					address = serverAddress.data.domain;

					//设置全局变量 判断是否显示忘记密码
					AppConst.GlobalConfig.ForgetPassWord = serverAddress.data.enable_forget_password;
//					 Log.v(TAG, "---return URL:"+url);
					cancelTaskList();
				}
			}
		};
		return task;
	}

	private void cancelTaskList() {
		for (AsyncTask asyncTask : taskList) {
			boolean isSuccess = asyncTask.cancel(true);
//			 Log.v(TAG, "---cancel:"+isSuccess);
		}

	}

	private HttpServerAddress postRequest(String url) {
//		 Log.v(TAG, "---post URL:" + url);
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs = null;
		HttpServerAddress address = (HttpServerAddress) HttpRestfulClient.Post(url, pairs, HttpServerAddress.class);
		return address;
	}

	public interface onFinishListener {
		void onFinish(String host);
	}

	public void setOnFinishListener(
			HostCompetition.onFinishListener onFinishListener) {
		this.onFinishListener = onFinishListener;
	}
}
