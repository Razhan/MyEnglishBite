package com.ef.bite.ui.balloon;

import java.util.Random;
import com.ef.bite.R;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ef.bite.utils.SoundEffectUtils;

public class ThornDownView extends View {

	private int color = 0;
	private Paint paint = null;
	private Path path = null;
	private int layoutWidth = 0;
	private int layoutHeight = 0;
	private int MAX_THORN_WIDTH = 0;
	private int MAX_THORN_HEIGHT = 0;
	private final static int THORN_NUMBER = 11;
	private float[] thornWidthArray;

	// 动画
	BalloonSetLayout mBalloonLayout; // 气球布局
	int toplayoutHeight = 0; // 气球布局的上层高度
	int ballsetLayoutHeight = 0; // 气球布局高度
	final static int ERROR_THORN_DOWN_TIME = 3 * 1000; // 选择了错误的气球，倒刺下降时间缩短3秒
	final static int DURATION_THORN_DOWN = 20 * 1000; // 倒刺动画时间
	final static int DURATION_EXPLISION = 1 * 1000; // 爆炸动画时间
	ValueAnimator thornDownAnimator = null;
	ValueAnimator explisionAnimator = null;
	OnMissedListener mMissedListener = null; // 所有气球都炸掉后的事件
	boolean isThornDownAnimatorCanceled = false;
	boolean isExplisionAnimatorCanceled = false;
	int thornDownHeight = 0;
	long thornDownStartTime = 0; // 倒刺开始时间
	long thornDownLeftTime = DURATION_THORN_DOWN; // 倒刺动画剩余时间
	boolean isAnimatorStopped = false;
	// Sound Effect
	SoundEffectUtils mSoundEffect = null;
	boolean isTimeout = false;

	public ThornDownView(Context context) {
		super(context);
		initialize();
	}

	public ThornDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/** 初始化倒计时动画的一些参数 **/
	public void initAnimatorParams(BalloonSetLayout balloonLayout,
			int toplayoutheight, int ballsetLayoutHeight,
			OnMissedListener missListener) {
		this.mBalloonLayout = balloonLayout;
		this.toplayoutHeight = toplayoutheight;
		this.ballsetLayoutHeight = ballsetLayoutHeight;
		this.mMissedListener = missListener;
		isTimeout = false;
		thornDownAnimator = null;
		explisionAnimator = null;
		isAnimatorStopped = false;
		isThornDownAnimatorCanceled = false;
		isExplisionAnimatorCanceled = false;
		thornDownHeight = 0;
		thornDownStartTime = 0; // 倒刺开始时间
		thornDownLeftTime = DURATION_THORN_DOWN; // 倒刺动画剩余时间
		setThornDownHeight(thornDownHeight);
	}

	private void initialize() {
		layoutWidth = 0;
		layoutHeight = 0;
		color = getContext().getResources().getColor(
				R.color.bella_balloon_red_bg);
		MAX_THORN_WIDTH = getContext().getResources().getDimensionPixelSize(
				R.dimen.balloon_thorn_max_width);
		MAX_THORN_HEIGHT = MAX_THORN_WIDTH * 2;
		paint = new Paint();
		path = new Path();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color);

		thornWidthArray = new float[THORN_NUMBER + 1];
		int randomSeek = 1;
		for (int index = 0; index < THORN_NUMBER; index++) {
			randomSeek = randomSeek + index;
			thornWidthArray[index] = getRadomThornWidth(randomSeek);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		layoutWidth = right - left;
		layoutHeight = bottom - top;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw Line
		path.moveTo(0, 0);
		path.lineTo(0, layoutHeight - MAX_THORN_HEIGHT);
		float thornInterval = (float) layoutWidth / (THORN_NUMBER + 1);
		for (int index = 0; index < THORN_NUMBER; index++) {
			float curThornWidth = thornWidthArray[index];
			float curThornHeight = curThornWidth * 2;
			path.lineTo((index + 1) * thornInterval - curThornWidth / 2,
					layoutHeight - MAX_THORN_HEIGHT);
			path.lineTo((index + 1) * thornInterval, layoutHeight
					- MAX_THORN_HEIGHT + curThornHeight);
			path.lineTo((index + 1) * thornInterval + curThornWidth / 2,
					layoutHeight - MAX_THORN_HEIGHT);
		}
		path.lineTo(layoutWidth, layoutHeight - MAX_THORN_HEIGHT);
		path.lineTo(layoutWidth, 0);
		path.lineTo(0, 0);
		canvas.drawPath(path, paint);
	}

	private boolean containsRandmonThorn(int rand) {
		if (thornWidthArray != null && thornWidthArray.length > 0) {
			for (int index = 0; index < thornWidthArray.length; index++) {
				if (rand == thornWidthArray[index])
					return true;
			}
		}
		return false;
	}

	private float getRadomThornWidth(int seed) {
		Random rand = new Random(seed + System.currentTimeMillis());
		float rate = rand.nextFloat();
		int num = (int) (rate * MAX_THORN_WIDTH);
		while (containsRandmonThorn(num)) {
			rate = rand.nextFloat();
			num = (int) (rate * MAX_THORN_WIDTH);
		}
		return num;
	}

	/**
	 * 开始倒计时动画
	 */
	public void startThronDown() {
		if (isAnimatorStopped)
			return;
		if (thornDownLeftTime <= 0)
			return;
		thornDownStartTime = System.currentTimeMillis();
		if (thornDownAnimator != null && thornDownAnimator.isRunning())
			thornDownAnimator.cancel();
		thornDownAnimator = ValueAnimator.ofInt(thornDownHeight,
				toplayoutHeight);
		thornDownAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						thornDownHeight = (Integer) animation
								.getAnimatedValue();
						setThornDownHeight(thornDownHeight);
					}
				});
		thornDownAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				isThornDownAnimatorCanceled = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// to do
				// You missed!
				if (!isThornDownAnimatorCanceled) {
					// 开始爆炸
					startExplosion();
					isTimeout = true;
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				isThornDownAnimatorCanceled = true;
			}
		});
		thornDownAnimator.setDuration(thornDownLeftTime);
		thornDownAnimator.start();
		// 添加声音效果
		if (mSoundEffect == null) {
			mSoundEffect = new SoundEffectUtils(getContext());
			mSoundEffect.play(SoundEffectUtils.TIMER_SOFT_20SECONDS);
		} else
			mSoundEffect.resume();
	}

	/** 加快倒计时一次 **/
	public void speedUpTronDown() {
		if (isAnimatorStopped)
			return;
		long playedTime = thornDownAnimator.getCurrentPlayTime();
		thornDownLeftTime = thornDownLeftTime - playedTime
				- ERROR_THORN_DOWN_TIME; // 剩余时间缩短了
		// 倒刺突然加速
		thornDownHeight += ERROR_THORN_DOWN_TIME * toplayoutHeight
				/ DURATION_THORN_DOWN;
		setThornDownHeight(thornDownHeight);
		mSoundEffect.forward(ERROR_THORN_DOWN_TIME);
		if (thornDownLeftTime <= 0) {
			// 开始爆炸
			cancelThronDown();
			startExplosion();
		} else
			startThronDown();
	}

	/**
	 * 开始爆炸动画
	 * 
	 * @param displayHeight
	 */
	public void startExplosion() {
		if (isAnimatorStopped)
			return;
		if (explisionAnimator != null && explisionAnimator.isRunning()) // 爆炸动画不能重复启动多个
			return;
		explisionAnimator = ValueAnimator.ofInt(thornDownHeight,
				toplayoutHeight + ballsetLayoutHeight);
		explisionAnimator.setDuration(DURATION_EXPLISION);
		explisionAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						thornDownHeight = (Integer) animation
								.getAnimatedValue();
						setThornDownHeight(thornDownHeight);
						// 下降过程中爆炸气球
						mBalloonLayout.explosionBalloons(thornDownHeight
								- toplayoutHeight);
					}
				});
		explisionAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				isExplisionAnimatorCanceled = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isExplisionAnimatorCanceled && mMissedListener != null)
					mMissedListener.OnMissed();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				isExplisionAnimatorCanceled = true;
			}
		});
		explisionAnimator.start();
	}

	/**
	 * 判断是否运行
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return thornDownAnimator != null && thornDownAnimator.isRunning();
	}

	private void cancelThronDown() {
		if (thornDownAnimator != null && thornDownAnimator.isRunning())
			thornDownAnimator.cancel();
		long runTime = System.currentTimeMillis() - thornDownStartTime;
		thornDownLeftTime = thornDownLeftTime - runTime;
	}

	private void cancelExplision() {
		if (explisionAnimator != null && explisionAnimator.isRunning())
			explisionAnimator.cancel();
	}

	/** 暂停动画 **/
	public void pauseAnimator() {
		this.cancelThronDown();
		this.cancelExplision();
		if (mSoundEffect != null)
			mSoundEffect.pause();
	}

	/** 暂停动画 **/
	public void resumeAnimator() {
		if (isThornDownAnimatorCanceled) {
			this.startThronDown();
		} else if (isExplisionAnimatorCanceled) {
			this.startExplosion();
		}
	}

	/**  **/
	public void stopAnimator() {
		pauseAnimator();
		isAnimatorStopped = true;
		if (mSoundEffect != null)
			mSoundEffect.stop();
		mSoundEffect = null;
	}

	/** 清理所有的动画 **/
	public void clearAnimator() {
		this.cancelThronDown();
		this.cancelExplision();
		mSoundEffect = null;
		initialize();
	}

	/**
	 * 是否倒计时到！
	 */
	public boolean isTimeout() {
		return isTimeout;
	}

	/** 获得剩余的时间 **/
	public long getLeftTime() {
		return thornDownLeftTime;
	}

	/** 设置倒刺的高度 **/
	private void setThornDownHeight(int height) {
		ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) getLayoutParams();
		params.height = height;
		setLayoutParams(params);
	}

	/**
	 * 失败
	 */
	public interface OnMissedListener {
		public void OnMissed();
	}

	/**
	 * 成功
	 */
	public interface OnSuccessListener {
		public void OnSuccess();
	}

}
