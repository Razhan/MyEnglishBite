package com.ef.bite.ui.balloon;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BalloonTouchListener implements  OnTouchListener{
	public static int SelectBalloonMargin = 20;
	private Activity mActivity;
	private int ResultLayoutTopY;
	private BalloonSetLayout  mBalloonLayout;
	private RelativeLayout mResultLayout;
	private BalloonView.OnCorrectSelectListener mCorrectSelect;
	private BalloonView.OnWrongSelectListener mWrongSelect;
	private BalloonView.OnResultCheckListener mResultChecker;
	private float touch_x = 0;
	private float touch_y = 0;
	private ViewGroup mAnimationLayout;
	private BalloonView mAnimationBalloon;
	private int[] start_location = new int[2];			// 气球初始位置
	private int[] up_location = new int[2];				// 
	
	private long touchTime = 0;
	/**
	 * 
	 * @param activity
	 * @param balloonLayout
	 * @param resultLayout
	 * @param resultLayoutTopY
	 */
	public BalloonTouchListener(Activity activity, BalloonSetLayout balloonLayout ,RelativeLayout resultLayout, 
			int resultLayoutTopY){
		this.mActivity = activity;
		this.ResultLayoutTopY = resultLayoutTopY;
		this.mResultLayout = resultLayout;
		this.mBalloonLayout = balloonLayout;
	}
	
	/**
	 * 设置气球选中是否正确的事件
	 * @param listener
	 */
	public void setResultChecker(BalloonView.OnResultCheckListener listener){
		this.mResultChecker = listener;
	}
	
	public void setWrongSelect(BalloonView.OnWrongSelectListener listener){
		this.mWrongSelect = listener;
	}
	
	public void setCorrectSelect(BalloonView.OnCorrectSelectListener listner){
		this.mCorrectSelect = listner;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final BalloonView balloon = (BalloonView)v;
		if(balloon==null)
			return false;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	        	// 判断是否选择了正确的气球
	        	if(mResultChecker!=null && !mResultChecker.OnCheck(balloon)){
	        		// 如果选择了错误的气球，则处理，并直接返回
	        		if(mWrongSelect!=null)
	        			mWrongSelect.OnWrong(balloon);
	        		return false;
	        	}
	        	
	        	// stop shaking
	        	balloon.stopShaking();
	        	// 创建移动动画
	        	balloon.getLocationOnScreen(start_location);
	        	if(mAnimationLayout==null)
	        		mAnimationLayout = createAnimLayout(mActivity);
	        	mAnimationBalloon = new BalloonView(mActivity);
	        	mAnimationBalloon.initialize(balloon.getLabel());
	        	addViewToAnimLayout(mAnimationLayout,mAnimationBalloon,balloon.getWidth(),balloon.getHeight(),start_location);
	        	balloon.setVisibility(View.INVISIBLE);
	        	// 当前的touch点
	        	touch_x = event.getX();
	        	touch_y = event.getY();
	        	touchTime = System.currentTimeMillis();
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	int xDelta = (int)(event.getX() - touch_x);
	        	int yDelta = (int)(event.getY() - touch_y);
	        	moveView(mAnimationBalloon, xDelta, yDelta);
	        	touch_x = event.getX();
	        	touch_y = event.getY();
	        	break;
	        case MotionEvent.ACTION_UP:
	        	
	        	// 判断move结束放掉的点,超过balloon_layout范围，则拖动到下层result_layout，如果没有，则恢复原来位置
	        	mAnimationBalloon.getLocationOnScreen(up_location);
	        	int y_position = up_location[1];
	        	int balloonHeight = mAnimationBalloon.getMeasuredHeight();
	        	// 成功拖下来
	        	if(y_position + balloonHeight - 10 >= ResultLayoutTopY){
	        		// 将气球从布局中删除
	        		balloon.setOnTouchListener(null);
	        		mBalloonLayout.removeView(balloon);
	        		final BalloonView selectBalloon = selectRightBalloon(balloon,mResultLayout);
	        		xDelta = SelectBalloonMargin - up_location[0];
	        		yDelta = ResultLayoutTopY - up_location[1];
	        		selectBalloon.setVisibility(View.INVISIBLE);
	        		animateMoveView(mAnimationBalloon,xDelta,yDelta,new AnimationListener(){ 
						@Override
						public void onAnimationStart(Animation animation) { } 
						@Override
						public void onAnimationEnd(Animation animation) {
							clearAnimLayout();
							selectBalloon.setVisibility(View.VISIBLE);
							// 成功选择
			        		if(mCorrectSelect!=null){
			        			mCorrectSelect.OnCorrect(selectBalloon);
			        		}
						} 
						@Override
						public void onAnimationRepeat(Animation animation) { } 
	        		});
	        	}else{
	        		// 时间太短，看作单击事件
		        	if(System.currentTimeMillis() - touchTime < 100){ 
		        		clearAnimLayout();
						balloon.setVisibility(View.VISIBLE);
						balloon.startShaking();
		        		return true;
		        	}
		        	
	        		// 还原原先位置
	        		xDelta = start_location[0] - up_location[0];
	        		yDelta = start_location[1] - up_location[1];
	        		animateMoveView(mAnimationBalloon,xDelta,yDelta,new AnimationListener(){ 
						@Override
						public void onAnimationStart(Animation animation) { } 
						@Override
						public void onAnimationEnd(Animation animation) {
							clearAnimLayout();
							balloon.setVisibility(View.VISIBLE);
							balloon.startShaking();
						} 
						@Override
						public void onAnimationRepeat(Animation animation) { } 
	        		});
	        	}
	        	break;
		 }
	 
	 return true;
	}
	
	/** 清除动画效果  **/
	public void clearAnimLayout(){
		if(mAnimationLayout!=null){
			ViewGroup rootView = (ViewGroup)mActivity.getWindow().getDecorView();
			mAnimationLayout.removeAllViews();
			rootView.removeView(mAnimationLayout);
			mAnimationLayout = null;
			mAnimationBalloon = null;
		}
	}

	/** 创建动画层 **/
	private ViewGroup createAnimLayout(Activity context) {
					ViewGroup rootView = (ViewGroup)context.getWindow().getDecorView();
					RelativeLayout animLayout = new RelativeLayout(context);
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT);
					animLayout.setLayoutParams(lp);
					animLayout.setId(Integer.MAX_VALUE);
					animLayout.setBackgroundResource(android.R.color.transparent);
					animLayout.setClickable(false);
					rootView.addView(animLayout);
					return animLayout;
				}
	/** 把需要移动的视图添加到动画层 **/
	private View addViewToAnimLayout(final ViewGroup vg, final View view,int width,int height,
						int[] location) {
					int x = location[0];
					int y = location[1];
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
							width,
							height);
					lp.leftMargin = x;
					lp.topMargin = y;
					vg.addView(view,lp);
					return view;
				}	
	/** 跳动视图 **/
	private void moveView(View view, int xDelta, int yDelta){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
			params.leftMargin = params.leftMargin + xDelta;
			params.topMargin = params.topMargin + yDelta;
			view.setLayoutParams(params);
	}
	/** 一动画方式移动 **/
	private void animateMoveView(View view, int xDelta, int yDelta, AnimationListener animationListener){
		// 计算位移
		TranslateAnimation translateAnimationX = new TranslateAnimation(0, xDelta, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, yDelta);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);
		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(true);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(500);// 动画的执行时间
		// 动画监听事件
		if(animationListener!=null)
			set.setAnimationListener(animationListener);
		view.startAnimation(set);
	}
	
	/** 选择了正确的Balloon,创建新的BalloonView并添加到ResultLayout中  **/
	private BalloonView selectRightBalloon(BalloonView balloon,RelativeLayout resultLayout){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = SelectBalloonMargin;
		params.topMargin = 2;
		resultLayout.addView(balloon,params);
		return balloon;
	}
}