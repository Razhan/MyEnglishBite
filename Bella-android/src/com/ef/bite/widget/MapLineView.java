package com.ef.bite.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Not Used
 * @author Allen.Zhu
 *
 */
public class MapLineView extends View{
	List<PointF> mPointList = new ArrayList<PointF>();
	
	public MapLineView(Context context){
		super(context);
	}
	
	public MapLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setData(List<PointF> pointList){
		mPointList = pointList;
		invalidate();
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mPointList!= null && mPointList.size() > 0){
		    Paint paint  = new Paint();
		    paint.setAntiAlias(true);
		    paint.setStyle(Style.STROKE);
		    paint.setStrokeWidth(40);
		    //PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);  
		    //paint.setPathEffect(effects);  
		    paint.setColor(Color.WHITE);
		    PointF prePoint = null;
		    PointF currentPoint = null;
		    for(int index = 0; index<mPointList.size(); index ++){
		    	currentPoint = mPointList.get(index);
		    	if(index == 0){
		    		canvas.drawPath(getNodePath(canvas, paint, new PointF(canvas.getWidth()/2,0), currentPoint) , paint);
		    	}
		    	if(prePoint!=null && currentPoint!=null){
		    		Path  myPath = getNodePath(canvas, paint, prePoint, currentPoint);
		    		// 画路径
		    		canvas.drawPath(myPath, paint);
		    	}
		    	prePoint = currentPoint;
		    }
		    canvas.drawPath(getNodePath(canvas, paint, prePoint, new PointF(canvas.getWidth()/2,canvas.getHeight())), paint);
		}
	}
	
	private Path getNodePath(Canvas canvas, Paint paint, PointF mPointa, PointF mPointb) {
	    Path myPath = new Path();
	    myPath.moveTo(mPointa.x, mPointa.y);
	    PointF control1 = getRandomBezierControl(mPointa,mPointb);
	    myPath.quadTo(control1.x, control1.y, mPointb.x, mPointb.y);
	    canvas.drawPath(myPath, paint);
	    return myPath;  
	}
	
	/**
	 * 获得两点之间的随机贝塞尔曲线控制点
	 * @param a
	 * @param b
	 * @return
	 */
	private PointF getRandomBezierControl(PointF a, PointF b){
		float randomX = (float) (Math.random() *(b.x-a.x) + a.x);
		float randomY = (float) (Math.random() *(b.y-a.y) + a.y);
		return new PointF(randomX,randomY);
	}
}