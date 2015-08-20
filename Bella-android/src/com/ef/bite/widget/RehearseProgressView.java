package com.ef.bite.widget;

import android.annotation.SuppressLint;
import com.ef.bite.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Rehearse进度状态视图
 * @author Allen.Zhu
 */
public class RehearseProgressView extends View {

	Paint paint;
	int lightColor;
	int darkColor;
	int total = 4;
	int progress = 1;
	int thickness = 5;
	RectF rectF;
	
	public RehearseProgressView(Context context) {
		super(context);
		initialize();
	}

	public RehearseProgressView(Context context, AttributeSet attrs) {
		super(context,attrs);
		initialize();
	}
	
	private void initialize(){
		paint = new Paint();
		lightColor = getContext().getResources().getColor(R.color.bella_rehearse_prgress_yellow);
		darkColor = getContext().getResources().getColor(R.color.bella_rehearse_prgress_yellow);
	}
	
	public void setProgress(int progress,int total){
		this.progress = progress;
		this.total = total;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int ht = thickness;
		ht = ht-ht/2+2;
		if(thickness%2!=0) ht-=1;
		rectF = new RectF(ht, ht, getWidth()-ht, getHeight()-ht);
	}

	/**
	 * When The view is draw.
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// draw background circle
		paint.setColor(lightColor);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(thickness);
		canvas.drawOval(rectF, paint);
		
		// draw front end arc
		paint.setColor(darkColor);
		paint.setStyle(Style.FILL);
		canvas.drawArc(rectF, 270, (progress * 360)/total, true, paint);
		
//		// draw text
//		paint.setStyle(Style.FILL);
//		paint.setStrokeWidth(2);
//		paint.setColor(darkColor);
//		paint.setTextAlign(Align.CENTER);
//		paint.setTextSize(getWidth()/3);
//		String flag = progress + "/" + total;
//		canvas.drawText(flag, getWidth()/2,getHeight() * 5 / 8, paint);
	}
}
