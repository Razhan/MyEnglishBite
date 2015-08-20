package com.ef.bite.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.ef.bite.AppConst;

/**
 * 声音效果工具类
 * @author Allen.Zhu
 *
 */
public class SoundEffectUtils {
	
	public final static String NOTIFICATION = "sounds/notification/bella sound_28.mp3";
	
	public final static String BALLOON_WELL_DONE = "sounds/ballon_well_done/006222681-wind-gust-04.mp3";
	
	public final static String BALLOON_BUZZING = "sounds/balloon_buzzing/bella sound_5.mp3";
	
	public final static String BALLOON_POPING = "sounds/balloon_poping/008873955-pop-balloon.mp3";
	
	public final static String BALLOON_TIMES_UP = "sounds/balloon_times_up/TimeUP.mp3";
	
	public final static String RIGHT_ANWSER = "sounds/right_anwser/bella sound_28.mp3";
	
	public final static String WRONG_ANWSER = "sounds/wrong_anwser/bella sound_5.mp3";
	
	public final static String LEVEL_UP = "sounds/level_up/LevelUp.mp3";
	
	public final static String TIMER_SOFT_15SECONDS = "sounds/timer_soft/Timer 15s.mp3"; 
	
	public final static String TIMER_SOFT_20SECONDS = "sounds/timer_soft/Timer 20s.mp3"; 
	
	public final static String REHEARSE_KEEP_TRYING = "sounds/rehearse_keep_trying/bella sound_43.mp3";
	
	public final static String REHEARSE_NOT_BAD = "sounds/rehearse_not_bad/bella sound_44.mp3";
	
	public final static String REHEARSE_VERY_GOOD = "sounds/rehearse_verry_good/bella sound_42.mp3";
	
	/** 震动时间 **/
	public final static int VIBRATOR_TIME = 100; 
	private Context mContext;
	private MediaPlayer mPlayer;
	private long mAudioDuration;
	
	public SoundEffectUtils(Context context){
		mContext = context;
	}
	
	/**
	 * 播放指定路径下的音频
	 * @param assetFilePath
	 */
	public void play(String assetFilePath){
		if(AppConst.GlobalConfig.SoundEffect_Enable){
			prepare(assetFilePath);
			resume();
		}
	}
	
	/**
	 * 播放声音同时带震动
	 * @param assetFilePath
	 */
	public void playWithVibrator(String assetFilePath){
		if(AppConst.GlobalConfig.SoundEffect_Enable){
			prepare(assetFilePath);
			resume();
			Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
	        vib.vibrate(VIBRATOR_TIME);
		}
	}
	
	/**
	 * 暂停播放
	 */
	public void pause(){
		try{
			if(mPlayer!=null && mPlayer.isPlaying())
				mPlayer.pause();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 恢复播放音频
	 */
	public void resume(){
		try{
			if(mPlayer!=null && ! mPlayer.isPlaying())
				mPlayer.start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 快进一定的时间
	 * @param forwardTime
	 */
	public void forward(int forwardTime){
		try{
			int position = mPlayer.getCurrentPosition();
			mPlayer.seekTo(position + forwardTime);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 暂停播放音频
	 */
	public void stop(){
		try{
			if(mPlayer !=null && mPlayer.isPlaying()){
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 准备播放asset的音频文件
	 * @param audioPath
	 */
	private void prepare(String audioPath) {
		try{
			AssetFileDescriptor descriptor = mContext.getAssets().openFd(audioPath);
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        mPlayer.prepare();
	        mAudioDuration = mPlayer.getDuration();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer player) {
					player.stop();
					player.release();
				}
			});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
