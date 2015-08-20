package com.ef.bite.widget;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * NOT USED
 * @author Allen.Zhu
 *
 */
public class TimerTextView extends TextView {

	private int totalSeconds;
	private int leftSeconds;
	private int interval = 1;		// 间隔时间，间隔多久时间变化
	private ScheduledExecutorService mPlayScheduler;
	private ScheduledFuture mScheduledFuture;
	OnTimerFinishListener mFinishListener;
	public TimerTextView(Context context){
		super(context);
	}
	
	public TimerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setTotalSeconds(int seconds){
		totalSeconds = seconds;
		leftSeconds = seconds;
	}
	
	public int getTotalSeconds(){
		return totalSeconds;
	}
	
	public int getLeftSeconds(){
		return leftSeconds;
	}
	
	public void start(){
		
	}
	
	public void stop(){
		
	}
	
	public interface OnTimerFinishListener{
		public void OnFinish();
	}
}
