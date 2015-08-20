package com.ef.bite;

import java.util.Stack;

import android.app.Activity;

import com.ef.bite.ui.main.MainActivity;

/***
 * 维持App作用域内的所有新建activity
 * @author Allen.Zhu
 *
 */
public class AppSession {

	private static AppSession appsession;
	private Stack<Activity>  activities = new Stack<Activity>();
	private Stack<Activity> homeActivity = new Stack<Activity>();
	public static AppSession getInstance(){
		if(appsession==null)
			appsession = new AppSession();
		return appsession;
	} 
	
	/**
	 * 判断main activity is alive
	 * @return
	 */
	public boolean isMainAlive(){
		return homeActivity!=null&&homeActivity.size()>0;
	}
	
	/**
	 * 栈存入activity对象
	 * @param activity
	 */
	public void addActivity(Activity activity){
		if(activity instanceof MainActivity)
			homeActivity.add(activity);
		else
			activities.push(activity);
	}
	
	/**
	 * finish所有的activity，并情况
	 */
	public void clear(){
		while(!activities.empty() && activities.size()>0){
			Activity activity = activities.pop();
			activity.finish();
		}
		activities.removeAllElements();
	}
	
	
	public void clearAll(){
		clear();
		while(!homeActivity.empty() && homeActivity.size()>0){
			Activity activity = homeActivity.pop();
			activity.finish();
		}
		homeActivity.removeAllElements();
	}
	
	/**
	 * 退出App
	 */
	public void exit(){
		clearAll();
		System.exit(0);  
	}
	

}
