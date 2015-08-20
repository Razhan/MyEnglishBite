package com.ef.bite.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 实现放大和缩小动画的ScrollView
 *
 */
public class ResizeScrollView extends ScrollView {
	
	private View mEffectView;
	private boolean isEnableScrolling = true;
	private int mCurrentHeight;
	private int mMaxHeight;
	private int mMinHeight;
	float startY;
	
	public ResizeScrollView(Context context) {  
        super(context);  
    }  
  
    public ResizeScrollView(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public ResizeScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    
    public void setEffectView(View view, int maxHeight, int minHeight){
    	mEffectView = view;	
    	mMaxHeight = maxHeight;
    	mMinHeight = minHeight;
    }
  
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnableScrolling) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            startY = event.getY();
	            return true;
	        case MotionEvent.ACTION_MOVE:
	             float currentY = event.getY();
	             float diffY = currentY - startY;
	             if(isAtTop()){
	            	 mCurrentHeight = mEffectView.getHeight();
	            	 if(diffY <= 0)
	            		 startResizeAnimation(true);
	            	 else
	            		 startResizeAnimation(false);
	             }
	             break;
	        default:
	            break;
    	}
    	return super.onTouchEvent(event);
    }
    
    /**
     * 
     * @return
     */
    public boolean isAtTop(){
      return getScrollY()<=0;
    }
      
    /**
     * 
     * @return
     */
    public boolean isAtBottom(){
      return getScrollY()==getChildAt(getChildCount()-1).getBottom()+getPaddingBottom()-getHeight();
    }
    
    /***
     * 放大缩小动画 Animation
     * @param smaller 是否缩小
     */
    private void startResizeAnimation(boolean smaller){
    	ValueAnimator resize = null;
    	if(smaller)
    		resize = ValueAnimator.ofInt(mCurrentHeight,mMinHeight);
    	else
    		resize = ValueAnimator.ofInt(mCurrentHeight, mMaxHeight);
    	resize.addUpdateListener(new AnimatorUpdateListener(){
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					 int height = (Integer)animation.getAnimatedValue();
					// mCurrentHeight = height;
					 ViewGroup.LayoutParams params = mEffectView.getLayoutParams();
					 params.height = height;
					 mEffectView.setLayoutParams(params);
				}
    	});
    	resize.setDuration(500);
    	resize.start();
    }
}