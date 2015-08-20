package com.ef.bite.animation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.ef.bite.widget.AudioPlayerView;

public class AudioPlayerMoveAnimation {

	Activity mContext;
	AudioPlayerView anim_player;
    private ViewGroup anim_mask_layout;//动画层
    int[] start_location = new int[2];
    int[] end_location = new int[2];
    int start_width = 0;
    int start_height = 0;
    int end_width = 0;
    int end_height = 0;
    AnimationEndListener mListener;
    public static int ANIMATION_DURATION = 500;
    
    public AudioPlayerMoveAnimation(Activity activity){
    	mContext = activity;
    }
    
    public interface AnimationEndListener{
    	void onEnd();
    }
    
    public void setAnimationEndListener(AnimationEndListener listener){
    	mListener = listener;
    }
    
    
	/**
	 * Move AudioPlayer
	 * @param bigger
	 * @param smaller
	 */
	public void movePlayer(final AudioPlayerView bigger, final AudioPlayerView smaller, final boolean isExpanded){
		if(isExpanded){
			bigger.getLocationOnScreen(start_location);
			smaller.getLocationOnScreen(end_location);
	    }else{
	    	int[] tmp = new int[2];
			tmp = start_location;
			start_location = end_location;
			end_location = tmp;
	    }
		
		if(isExpanded){
			start_width = bigger.getWidth();
			end_width = smaller.getWidth();
			start_height = bigger.getHeight();
			end_height = smaller.getHeight();
		}else{
			int tmp = start_width;
			start_width = end_width;
			end_width = tmp;
			tmp = start_height;
			start_height = end_height;
			end_height = tmp;
		}
	    anim_player = new AudioPlayerView(mContext);
	    if(anim_mask_layout == null)
	    	anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(anim_player);//把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, anim_player,start_width, start_height,start_location);
		// 计算位移
		int endX =end_location[0] - start_location[0] ;// 动画位移的X坐标
		int endY = end_location[1] - start_location[1] ;// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);
		final ResizeAnimation animation =new ResizeAnimation(anim_player,(float)start_width,(float)start_height, (float)end_width,(float)end_height); 
		animation.setDuration(ANIMATION_DURATION);
		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(true);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.addAnimation(animation);
		set.setDuration(ANIMATION_DURATION);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				anim_player.setVisibility(View.VISIBLE);
				if(isExpanded)
					bigger.setVisibility(View.GONE);
				else
					smaller.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				anim_mask_layout.removeView(anim_player);
				if(isExpanded){
					bigger.setVisibility(View.GONE);
					smaller.setVisibility(View.VISIBLE);
				}else{
					smaller.setVisibility(View.GONE);
					bigger.setVisibility(View.VISIBLE);
				}
				if(mListener!=null)
					mListener.onEnd();
			}
		});
	}
	
	//创建动画层
	private ViewGroup createAnimLayout() {
				ViewGroup rootView = (ViewGroup)mContext.getWindow().getDecorView();
				LinearLayout animLayout = new LinearLayout(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				animLayout.setLayoutParams(lp);
				animLayout.setId(Integer.MAX_VALUE);
				animLayout.setBackgroundResource(android.R.color.transparent);
				animLayout.setClickable(false);
				rootView.addView(animLayout);
				return animLayout;
			}
			
	private View addViewToAnimLayout(final ViewGroup vg, final View view,int width,int height,
					int[] location) {
				int x = location[0];
				int y = location[1];
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						width,
						height);
				lp.leftMargin = x;
				lp.topMargin = y;
				view.setLayoutParams(lp);
				return view;
			}
}
