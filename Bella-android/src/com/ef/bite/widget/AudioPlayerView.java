package com.ef.bite.widget;

import java.io.File;
import com.ef.bite.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.utils.TimeFormatUtil;

/**
 * audio播放控件
 * 
 * @author Allen
 * 
 */
public class AudioPlayerView extends View implements View.OnClickListener {
	final static int Msg_SetProgress = 1;
	private final int MSG_EndTime = 6;

	/** 已准备播放状态 */
	final public static int Status_Ready = 0;
	/** 正在播放状态 */
	final public static int Status_Playing = 1;
	/** 处于暂停播放状态 */
	final public static int Status_Paused = 2;
	/** 处于播放结束 */
	final public static int Status_PlayEnd = 3;
	/** 恢复播放状态 **/
	public static int Status_PlayAgain = 4;

	/** 处于mini播放状态的的外环厚度 */
	static int MiniStatus_Thinkness = 8;
	/** 处于正常播放状态的的外环厚度 */
	static int NormalStatus_Thinkness = 16;

	private Paint paint;
	/** The back color */
	private int backColor;
	/** The front color */
	private int fillColor;
	/** The progress value (0 - 100) */
	private int progress;
	/** The drawing area */
	private RectF rectF;
	/** Bar thickness */
	private int thickness = NormalStatus_Thinkness;

	/** 外面的环 */
	private int ringColor;
	// 播放器部分
	private MediaPlayer mPlayer;
	private long mAudioDuration;
	private ScheduledExecutorService mPlayScheduler;
	private ScheduledFuture mScheduledFuture;
	private OnCompletionListener mCompletionListener;
	private OnCloseAudioListener mCloseAudioListener;
	private OnStopClickListener mOnStopClickListener;
	private int mPlayStatus = Status_Ready; // 0 - Ready, 1 - Playing, 2 -
											// Paused, 3 - PlayEnd
	private Bitmap mPlaybackIcon;
	private String mTimeLeft = "00:00";
	private boolean mAllowReload = true; // 是否允许反复播放
	private boolean mShowTime = true; // 是否显示剩余时间
	private boolean isMiniStatus = false; // 是否迷你显示
	private int diameter; // 播放器的直径
	private Context mContext;
	private int mEndtime;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private final int delayTime = 300;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				if (msg.what == Msg_SetProgress) {
					long position = mPlayer.getCurrentPosition();
					long prog = 100L * position / mAudioDuration;
					progress = (int) prog;
					long timeLeft = mAudioDuration - position;
					mTimeLeft = TimeFormatUtil.convertSecondsToHMmSs(timeLeft);
					invalidate();
				}
				if (msg.what == MSG_EndTime) {
					if (mPlayer.getCurrentPosition() > mEndtime) {
						pause();
						mTimer.cancel();
						if (mOnStopClickListener != null) {
							mOnStopClickListener.OnStop();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public AudioPlayerView(Context context) {
		super(context);
		this.mContext = context;
		initialize();
	}

	public AudioPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initialize();
	}

	private void initialize() {
		paint = new Paint();
		backColor = getContext().getResources().getColor(
				R.color.bella_color_orange_light);
		fillColor = getContext().getResources().getColor(
				R.color.bella_color_orange_light_alpha);
		ringColor = getContext().getResources().getColor(
				R.color.bella_audio_player_ring_alpha);
		NormalStatus_Thinkness = (int) getContext().getResources()
				.getDimension(R.dimen.audioplayerview_lage_normal_thinkness);
		MiniStatus_Thinkness = (int) getContext().getResources().getDimension(
				R.dimen.audioplayerview_small_thinkness);
		progress = 0;
		thickness = NormalStatus_Thinkness;
		int ht = thickness / 2;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
	}

	/**
	 * Audio是在Asset文件夹中
	 */
	public void prepareInAsset(String audioPath) {
		try {
			AssetFileDescriptor descriptor = getContext().getAssets().openFd(
					audioPath);
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer player) {
					if (mCompletionListener != null)
						mCompletionListener.OnCompletion();
					mPlayStatus = Status_PlayEnd;
					mTimeLeft = "00:00";
					progress = 100;
					invalidate();
				}
			});
			mPlayer.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			// mPlayer.setDataSource(getContext(), Uri.parse(audioPath));
			mPlayer.prepare();
			mAudioDuration = mPlayer.getDuration();
			Message msg = new Message();
			msg.what = Msg_SetProgress;
			mHandler.sendMessage(msg);
			this.setOnClickListener(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Audio是在本地存储中
	 * 
	 * @param audioPath
	 */
	public void prepareInStorage(String audioPath) {
		try {
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer player) {
					if (mCompletionListener != null)
						mCompletionListener.OnCompletion();
					mPlayStatus = Status_PlayEnd;
					mTimeLeft = "00:00";
					progress = 100;
					invalidate();
				}
			});
			File audio = new File(audioPath);
			if (!audio.exists())
				return;
			mPlayer.setDataSource(audio.getAbsolutePath());
			mPlayer.prepare();
			mAudioDuration = mPlayer.getDuration();
			Message msg = new Message();
			msg.what = Msg_SetProgress;
			mHandler.sendMessage(msg);
			this.setOnClickListener(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Audio是在service中
	 * 
	 * @param audioPath
	 */
	public void prepareInAPI(String audioPath) {
		try {
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer player) {
					if (mCompletionListener != null)
						mCompletionListener.OnCompletion();
					mPlayStatus = Status_PlayEnd;
					mTimeLeft = "00:00";
					progress = 100;
					invalidate();
				}
			});
			mPlayer.setDataSource(audioPath);
			mPlayer.prepare();
			mAudioDuration = mPlayer.getDuration();
			Message msg = new Message();
			msg.what = Msg_SetProgress;
			mHandler.sendMessage(msg);
			this.setOnClickListener(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 播放结束监听事件，在prepare方法前使用
	 * 
	 * @param listener
	 */
	public void setOnCompletionListener(OnCompletionListener listener) {
		this.mCompletionListener = listener;
	}

	public void setOnCloseAudioListener(OnCloseAudioListener listener) {
		this.mCloseAudioListener = listener;
	}

	/**
	 * 设置是否允许反复播放
	 * 
	 * @param allow
	 */
	public void setAllowReplay(boolean allow) {
		mAllowReload = allow;
	}

	public void start() {
		if (mPlayer != null && !mPlayer.isPlaying()) {
			mPlayer.start();
			mPlayStatus = 1;
			// mPlayInfo = "PLAYING";
			showAudioProgress();
			if (mCloseAudioListener != null) {
				mCloseAudioListener.OnClose();
			}
			invalidate();
		}
	}

	public void start(int millseconds) {
		mPlayer.seekTo(millseconds);
//		if (mPlayer != null && !mPlayer.isPlaying()) {
//			mPlayStatus = 1;
//			mPlayer.stop();
//		}

		mPlayer.start();
		// showAudioProgress();
		invalidate();
		MobclickTracking.OmnitureTrack.ActionDialogue(5);
//		MobclickTracking.UmengTrack.ActionDialogue(5, mContext);
	}

	public void pause() {
		try {
			if (mPlayScheduler != null && !mPlayScheduler.isShutdown())
				mScheduledFuture.cancel(true);
			mPlayScheduler = null;
			if (mPlayer != null && mPlayer.isPlaying()) {
				mPlayer.pause();
				mPlayStatus = 2;
				// mPlayInfo = "PAUSE";
				invalidate();
			}

			// omniture
//			MobclickTracking.OmnitureTrack.ActionDialogue(4);
//			MobclickTracking.UmengTrack.ActionDialogue(4, mContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		try {
			if (mPlayScheduler != null && !mPlayScheduler.isShutdown())
				mScheduledFuture.cancel(true);
			mPlayScheduler = null;
			if (mPlayer != null && mPlayer.isPlaying()) {
				mPlayer.stop();
				mPlayer.release();
				mPlayStatus = Status_PlayEnd;
				mTimeLeft = "00:00";
				progress = 100;
				invalidate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void release() {
		mPlayer.stop();
		mPlayer.release();
	}

	/**
	 * 根据endtime设定stop
	 */

	public void stop(int millseconds, OnStopClickListener onStopClickListener) {
		this.mOnStopClickListener = onStopClickListener;
		try {
			if (mTimer != null) {
				mTimer.cancel();
				mTimerTask.cancel();
			}
			// made in Alan
			// delayTime控制延迟停止时间
			mEndtime = millseconds - delayTime;
			mTimer = new Timer();
			/*******************************************************
			 * 通过定时器和Handler来更新进度条
			 ******************************************************/
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					Message message = new Message();
					message.what = MSG_EndTime;
					mHandler.sendMessage(message);
				}
			};
			mTimer.schedule(mTimerTask, 1, 1000);
		} catch (IllegalStateException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 获得当前播放的状态
	 * 
	 * @return 0 - Ready, 1 - Playing, 2 - Paused, 3 - PlayEnd
	 */
	public int getStatus() {
		return mPlayStatus;
	}

	/*
	 * 每秒显示进度
	 */
	private void showAudioProgress() {
		mPlayScheduler = Executors.newSingleThreadScheduledExecutor();
		mScheduledFuture = mPlayScheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				// send message to handle
				Message msg = new Message();
				msg.what = Msg_SetProgress;
				mHandler.sendMessage(msg);
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	/**
	 * Define the color for the placeholder progress (background)
	 * 
	 * @param backColor
	 */
	public void setBackColor(int backColor) {
		this.backColor = backColor;
		invalidate();
	}

	/**
	 * Define the color for the filled progress (foreground)
	 * 
	 * @param fillColor
	 */
	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
		invalidate();
	}

	public void setMiniStatus(boolean isMini) {
		this.isMiniStatus = isMini;
		if (isMini)
			setThickness(MiniStatus_Thinkness);
		else
			setThickness(NormalStatus_Thinkness);
	}

	/**
	 * Define the progress thickness.
	 * 
	 * @param thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
		int ht = thickness;
		ht = ht - ht / 2 + 2;
		if (thickness % 2 != 0)
			ht -= 1;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
		invalidate();
	}

	/**
	 * Update drawing area on resize.
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int ht = thickness;
		ht = ht - ht / 2 + 2;
		if (thickness % 2 != 0)
			ht -= 1;
		int width = getWidth();
		int height = getHeight();
		if (width >= height) {
			int diff = width - height;
			rectF = new RectF(ht + diff / 2, ht, width - ht - diff / 2, height
					- ht);
			diameter = height;
		} else {
			int diff = height - width;
			rectF = new RectF(ht, ht + diff / 2, width - ht, height - ht - diff
					/ 2);
			diameter = width;
		}
	}

	/**
	 * When The view is draw.
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// canvas.drawPaint(paint);
		// Draw Inner Circle
		paint.setColor(Color.TRANSPARENT);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(backColor);
		RectF rectInnerOval;
		if (isMiniStatus)
			rectInnerOval = new RectF(
					rectF.left + MiniStatus_Thinkness * 3 / 2, rectF.top
							+ MiniStatus_Thinkness * 3 / 2, rectF.right
							- MiniStatus_Thinkness * 3 / 2, rectF.bottom
							- MiniStatus_Thinkness * 3 / 2);
		else {
			rectInnerOval = new RectF(rectF.left + NormalStatus_Thinkness * 3
					/ 2, rectF.top + NormalStatus_Thinkness * 3 / 2,
					rectF.right - NormalStatus_Thinkness * 3 / 2, rectF.bottom
							- NormalStatus_Thinkness * 3 / 2);
		}
		canvas.drawOval(rectInnerOval, paint);
		// Draw Player Status Icon
		if (mPlayStatus == AudioPlayerView.Status_Ready
				|| mPlayStatus == AudioPlayerView.Status_Paused) { // ready ||
																	// paused
			mPlaybackIcon = BitmapFactory.decodeResource(getResources(),
					R.drawable.audio_playback_listen);
			Bitmap scaleBitmap = scaleBitmap(mPlaybackIcon,
					(int) (rectF.width() * 2 / 5),
					(int) (rectF.height() * 2 / 5));
			if (!isMiniStatus)
				drawBitmap(canvas, scaleBitmap, canvas.getWidth() / 2,
						canvas.getHeight() * 2 / 5);
			else
				drawBitmap(canvas, scaleBitmap, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
		} else {
			if (mPlayStatus == AudioPlayerView.Status_Playing) // playing
			{
				mPlaybackIcon = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.audio_playback_pause);
			} else if (mPlayStatus == AudioPlayerView.Status_PlayEnd) // /stopped
				mPlaybackIcon = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.audio_playback_reload);
			Bitmap scaleBitmap = scaleBitmap(mPlaybackIcon,
					(int) (rectF.width() / 3), (int) (rectF.height() / 3));
			drawBitmap(canvas, scaleBitmap, canvas.getWidth() / 2,
					canvas.getHeight() / 2);
		}
		// Draw Ring
		// if(!isMiniStatus){
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(this.thickness);
		paint.setColor(ringColor);
		canvas.drawArc(rectF, 0, 360, false, paint);
		// }

		if (progress != 0) {
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(this.thickness);
			paint.setColor(fillColor);
			canvas.drawArc(rectF, 270, (progress * 365) / 100, false, paint);
		}
		// draw time left
		if (!isMiniStatus) {
			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(diameter / 8);
			canvas.drawText(mTimeLeft, canvas.getWidth() / 2,
					canvas.getHeight() * 3 / 4, paint);
		}
	}

	/**
	 * Draw bitmap at the center of point(x,y)
	 * 
	 * @param canvas
	 * @param map
	 * @param x
	 * @param y
	 */
	private void drawBitmap(Canvas canvas, Bitmap map, float x, float y) {
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		float boardPosX = x - mapWidth / 2;
		float boardPosY = y - mapHeight / 2;
		canvas.drawBitmap(map, boardPosX, boardPosY, null);
	}

	/**
	 * Scales the provided bitmap to have the height and width provided.
	 * (Alternative method for scaling bitmaps since
	 * Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
	 * 
	 * @param bitmap
	 *            is the bitmap to scale.
	 * @param newWidth
	 *            is the desired width of the scaled bitmap.
	 * @param newHeight
	 *            is the desired height of the scaled bitmap.
	 * @return the scaled bitmap.
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight,
				Config.ARGB_8888);

		float scaleX = newWidth / (float) bitmap.getWidth();
		float scaleY = newHeight / (float) bitmap.getHeight();
		float pivotX = 0;
		float pivotY = 0;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;
	}

	@Override
	public void onClick(View v) {
		switch (mPlayStatus) {
		case 0: // Ready
			start();
			MobclickTracking.OmnitureTrack.ActionDialogue(3);
            MobclickTracking.OmnitureTrack.ActionDialogue(6);
//			MobclickTracking.UmengTrack.ActionDialogue(3, mContext);
			break;
		case 1: // playing
			pause();
            MobclickTracking.OmnitureTrack.ActionDialogue(4);
			break;
		case 2: // paused
			start();
			break;
		case 3: // finished
			if (mAllowReload) {
				start();
				MobclickTracking.OmnitureTrack.ActionDialogue(5);
//				MobclickTracking.UmengTrack.ActionDialogue(5, mContext);
			}

			break;
		default:
			break;
		}
	}

	public interface OnCompletionListener {
		public void OnCompletion();
	}

	public interface OnCloseAudioListener {
		public void OnClose();
	}

	public interface OnStopClickListener {
		public void OnStop();
	}
}