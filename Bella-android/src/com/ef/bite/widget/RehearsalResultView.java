package com.ef.bite.widget;

import android.content.Context;
import com.ef.bite.R;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RehearsalResultView extends View {
	private int circleThickness;
	private int correctColor;
	private int wrongColor;
	private int correctNum = 0;
	private int totalNum = 1;
	RectF rectF;
	Paint paint;
	
	public RehearsalResultView(Context context){
		super(context);
		initialize();
	}

	public RehearsalResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize(){
		paint = new Paint();
		correctColor = getContext().getResources().getColor(R.color.bella_color_green_light);
		wrongColor = getContext().getResources().getColor(R.color.bella_color_red);
		circleThickness = 20;
	}
	
	/**
	 * 设置Rehearsal的总数和正确数量
	 * @param correctNum
	 * @param totalNum
	 */
	public void setResult(int correctNum, int totalNum){
		this.correctNum = correctNum;
		this.totalNum = totalNum;
		invalidate();
	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int ht = circleThickness;
		ht = ht-ht/2+2;
		if(circleThickness%2!=0) ht-=1;
		rectF = new RectF(ht, ht, getWidth()-ht, getHeight()-ht);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(correctNum < 0 || correctNum > totalNum)
			return;
		
		// draw correct
		if(correctNum > 0){
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(circleThickness);
			paint.setColor(correctColor);
			if(correctNum==totalNum)	// 全对
				canvas.drawOval(rectF, paint);
			else{
				canvas.drawArc(rectF, 270+3, (correctNum * 360) / totalNum - 3, false, paint);
				// draw wrong
				paint.setColor(wrongColor);
				canvas.drawArc(rectF, 270 + (correctNum * 360) / totalNum + 3, ((totalNum - correctNum)* 360)/totalNum -3, false , paint);
			}
		}else{
			// only draw wrong
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(circleThickness);
			paint.setColor(wrongColor);
			canvas.drawOval(rectF, paint);			
		}
		
		// draw correct number
		paint.setStyle(Style.FILL);
		if(correctNum <= 0)
			paint.setColor(wrongColor);
		else
			paint.setColor(correctColor);
		paint.setTextSize(canvas.getWidth()/3);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(Integer.toString(correctNum), canvas.getWidth()/2, canvas.getHeight() * 5/8, paint);
	}
}
