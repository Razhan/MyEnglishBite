package com.ef.bite.utils;

import java.io.File;
import com.ef.bite.R;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.EFApplication;

@SuppressWarnings("unused")
public class LogManager {

	private static final String LOG_FILE = "bite.log";
	public static final int FILE_INDEX = 1;
	public static final int OMNITURE_INDEX = 2;

	private static final int SPACESIZELIMIT = 500;

	private static LooperThread looperThread = null;

	public static LooperThread getLooperThread() {
		if (looperThread == null) {
			looperThread = new LooperThread();
			looperThread.start();
		}
		return looperThread;
	}

	/**
	 * 自定义日志输出方法
	 * 
	 * @param content
	 */
	public static void definedLog(String content) {
		try {
			File myFile = getFile();
			String str = "\r\n" + getCurrentTime() + "-->";
			FileOutputStream fos = new FileOutputStream(myFile, true);
			fos.write(str.getBytes());
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 将异常信息转化成字符串的形式
	 * 
	 * @param e
	 * @return
	 */
	public static String getTrace(Throwable e) {
		// Call getLooperThread() first to give it sometime to start the thread
		getLooperThread();
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		Message m = Message.obtain();
		m.obj = e.getMessage();
		if (getLooperThread().mHandler != null)
			getLooperThread().mHandler.sendMessage(m);
		return buffer.toString().replaceAll("\n", "\r\n");
	}

	public static class LooperThread extends Thread {
		public Handler mHandler;

		public void run() {
			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					Toast.makeText(
							EFApplication.getInstance(),
							EFApplication.getInstance().getResources()
									.getString(R.string.app_issue_happen)
									+ " " + msg.obj, Toast.LENGTH_LONG).show();
				}
			};
			Looper.loop();
		}
	}

	/**
	 * 获得当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Time t = new Time();
		t.setToNow();
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;
		int hour = t.hour;
		int minute = t.minute;
		int second = t.second;
		String time = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;
		return time;
	}

	/**
	 * 判断文件是否超出大小上限
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void deleteOldFile(final String path) throws IOException {

		File logFile = new File(path + File.separatorChar + LOG_FILE);
		if (logFile.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(logFile);
			int size = fis.available();
			double memory = (double) size / 1024;
			// 占有空间达到500KB
			if (memory > SPACESIZELIMIT) {
				logFile.delete();
				logFile.createNewFile();
			}
		} else {
			logFile.createNewFile();
		}
	}

	/**
	 * 获得文件，考虑文件大小上限问题
	 * 
	 * @return
	 */
	public static File getFile() {
		String path = null;
		String state = Environment.getExternalStorageState();
		// 判断SdCard是否存在并且是可用的
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			path = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + AppConst.CacheKeys.RootStorage
					+ File.separator + AppConst.CacheKeys.Storage_Log;
		}
		// 创建一个logcat目录
		else
			path = EFApplication.getInstance().getCacheDir()
					+ File.separator + AppConst.CacheKeys.RootStorage
					+ File.separator + AppConst.CacheKeys.Storage_Log;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			deleteOldFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String time = getCurrentTime();
		File[] files = file.listFiles();
		File myFile = null;
		if (files.length > 0) {
			if (!files[0].isDirectory()) {
				myFile = files[0];
			} else {
				myFile = new File(path + "/" + LOG_FILE);

			}
		} else {
			myFile = new File(path + "/" + LOG_FILE);
		}
		return myFile;
	}
}
