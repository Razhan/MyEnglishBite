package com.ef.bite.ui.balloon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.ef.bite.utils.SoundEffectUtils;
import com.ef.bite.widget.GifImageView;

public class BalloonSetLayout extends RelativeLayout {
	private static int MATRIX_X_POINTS = 20;		// 布局中横向X轴点数
	private static int MATRIX_Y_POINTS = 20;		// 布局中纵向Y轴点数
	private int mLayoutWidth;		// 布局的宽
	private int mLayoutHeight;		// 布局的高
	private int mPointInterval;		// 点阵矩阵之间的间隔
	/**	布局矩阵list **/
	private List<MatrixPoint> mMatrixList ;
	private List<MatrixPoint> mAvailableMatrixList;
	/** 所有气球列表	 **/
	private List<BalloonView> mBalloonList = new ArrayList<BalloonView>();
	
	public BalloonSetLayout(Context context) {
		super(context);
	}
	
	public BalloonSetLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 初始化气球布局
	 * @param width
	 * @param height
	 * @param balloonList
	 */
	public void initialize(int width, int height, List<BalloonView> balloonList){
		this.mLayoutWidth = width;
		this.mLayoutHeight = height;
		if(width <= height){
			mPointInterval = width/MATRIX_X_POINTS;
			MATRIX_Y_POINTS = height / mPointInterval;
		}
		else{
			mPointInterval = height/MATRIX_Y_POINTS;
			MATRIX_X_POINTS = width/mPointInterval;
		}
		initiMatrix();
		addBalloons(balloonList);
	}
	
	/** 清理所有的气球  **/
	public void clearAllBalloons(){
		while(mBalloonList!=null && mBalloonList.size() > 0){
			BalloonView balloon = mBalloonList.get(0);
			if(balloon!=null){
				balloon.stopShaking();
				this.removeView(balloon);
				mBalloonList.remove(balloon);
			}
		}
	}
	
	
	/***
	 * 下降时爆炸的第一个气球
	 * @param thornHeight		倒刺在气球布局内下降的高度
	 */
	public void explosionBalloons(int thornHeight){
		if(mBalloonList==null ||mBalloonList.size() <= 0)
			return;
		for(int index=0;index<mBalloonList.size();index++){
			BalloonView balloon = mBalloonList.get(index);
			MatrixPoint point = balloon.getCenterPoint();
			int radius = (int)balloon.getWidthRadius();
			if(point.DistY - radius < thornHeight ){
				explosionBalloon(balloon);
			}
		}
	}
	
	
	/** 从布局中爆炸气球 **/
	private void explosionBalloon(BalloonView balloon){
		if(mBalloonList.contains(balloon)){
			MatrixPoint point = balloon.getCenterPoint();
			int radius = (int)balloon.getWidthRadius();
			// 删除气球视图
			balloon.stopShaking();
			mBalloonList.remove(balloon);
			this.removeView(balloon);
			// 加载爆炸的gif动画
			final GifImageView explisionImage = new GifImageView(getContext());
			explisionImage.startGifFromAsset("gif/balloon_explosion.gif", false);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(radius*2,radius*2);
			params.leftMargin = point.DistX - radius;
			params.topMargin = point.DistY - radius;
			this.addView(explisionImage,params);
			new Handler().postDelayed(new Runnable()
			{
			   @Override
			   public void run()
			   {
				   if(explisionImage.getDrawable()!=null)
					   explisionImage.getDrawable().setVisible(true, true);
			   }
			}, 200);
			// Add sound effect
			new SoundEffectUtils(this.getContext()).play(SoundEffectUtils.BALLOON_POPING);
		}
	}
	
	/**	初始化布局矩阵 **/
	private void initiMatrix(){
		mMatrixList = new ArrayList<MatrixPoint>();
		mAvailableMatrixList = new ArrayList<MatrixPoint>();
		for(int y = 0; y < MATRIX_Y_POINTS; y ++){
			for(int x = 0; x < MATRIX_X_POINTS; x++){
				MatrixPoint point = new MatrixPoint(x, y, x*mPointInterval, y*mPointInterval);
				mMatrixList.add(point);
				mAvailableMatrixList.add(point);
			}
		}
	}
	
	/**	初始化气球	**/
	private void addBalloons(List<BalloonView> balloonList){
		if(balloonList!=null && balloonList.size() > 0){
			for(int index=0; index<balloonList.size(); index++){
				BalloonView balloonView = balloonList.get(index);
				MatrixPoint centerPoint = getAvailableCenterPoint(balloonView);
				// 如果找不到合适的点
				if(centerPoint == null)  
					continue;
				setOccupancyPoints(balloonView,centerPoint);
				measureBalloon(balloonView,index+1);
				mBalloonList.add(balloonView);
			}
		}
	}
	
	/**	
	 * 为新增balloon查找一个合适的中心坐标	
	 * 从列表中随机筛选
	 * **/
	private MatrixPoint getAvailableCenterPoint(BalloonView addedBalloon){
		// 找不到了
		if(mAvailableMatrixList==null || mAvailableMatrixList.size()<=0)
			return null;
		List<MatrixPoint> searchPointList = new ArrayList<MatrixPoint>();
		for(MatrixPoint point:mAvailableMatrixList)
			searchPointList.add(point);
		int index = 0;
		float radius = addedBalloon.getWidthRadius();
		while(searchPointList.size() > 0){
			Random generator = new Random(); 
			index = generator.nextInt(searchPointList.size());
			MatrixPoint point = searchPointList.get(index);
			// 判断和上下左右边框的距离,如果超过边框则去掉
			if(point.DistX < radius || point.DistY < radius || point.DistX  > mLayoutWidth-radius || point.DistY > mLayoutHeight - radius){
				searchPointList.remove(point);
				continue;
			}
			// 和其他的balloon比较，是否有重合，如果有则去掉
			boolean isCovered = isCoverOtherBalloons(point,radius);
			if(isCovered){
				searchPointList.remove(point);
				continue;
			}
			// 其他就是满足条件的
			return point;
		}
		searchPointList.clear();
		return null;
	}
	
	/** 在视图中显示Balloon  **/
	/** 在布局中显示BalloonView **/
	private void measureBalloon(final BalloonView balloon,int seed){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		MatrixPoint point = balloon.getCenterPoint();
		int radius = (int)balloon.getWidthRadius();
		params.leftMargin = point.DistX - radius;
		params.topMargin = point.DistY - radius;
		this.addView(balloon,params);
		// shaking animation
		Random rand = new Random(seed);
		int delayTime = rand.nextInt(1000);
		Log.i("Delaytime", "delay: " + delayTime);
		new Handler().postDelayed(new Runnable()
		{
		   @Override
		   public void run()
		   {
			   balloon.startShaking();
		   }
		}, delayTime);
		
	}
	
	
	/**
	 * 清理被占用的矩阵点。跟据添加的气球，设置气球面积覆盖的矩阵点为被占用
	 */
	private void setOccupancyPoints(BalloonView balloon, MatrixPoint centerPoint){
		centerPoint.IsOccupancy = true;
		balloon.setCenterPoint(centerPoint);
		mAvailableMatrixList.remove(centerPoint);
		float radius = balloon.getWidthRadius();
		int index=0;
		while(index < mAvailableMatrixList.size() && mAvailableMatrixList.size() > 0){
			MatrixPoint point = mAvailableMatrixList.get(index);
			// 如果这个点是balloon半径覆盖内，则设置为占用，并且从mAvailableMatrixList移除
			if(getDistance(centerPoint,point) <= radius){
					mAvailableMatrixList.remove(point);
					continue;
			}
			else{ // 没有被覆盖的
					index ++;
			}
		}
		
	}
	
	/** 计算两个矩阵点之间的距离   **/
	private double getDistance(MatrixPoint fromPoint, MatrixPoint toPoint){
		int x = fromPoint.DistX - toPoint.DistX;
		int y = fromPoint.DistY - toPoint.DistY;
		if(x==0 || y==0)
			return 0;
		return Math.sqrt(x*x + y*y);
	}
	
	/** 判断一个点为圆心的半径是否和其他balloon覆盖了   **/
	private boolean isCoverOtherBalloons(MatrixPoint point, float radius){
		if(mBalloonList==null || mBalloonList.size() <= 0)
			return false;
		for(int index=0; index<mBalloonList.size(); index++){
			MatrixPoint destP = mBalloonList.get(index).getCenterPoint();
			if(destP==null)
				continue;
			double distance = getDistance(point,destP);
			if(distance < radius + mBalloonList.get(index).getWidthRadius())
				return true;
		}
		return false;
	}

}
