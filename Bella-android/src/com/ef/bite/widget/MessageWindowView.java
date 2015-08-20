package com.ef.bite.widget;

import android.content.Context;
import com.ef.bite.R;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Not Used
 * @author Allen.Zhu
 *
 */
public class MessageWindowView extends View {

	private int bgColor = android.graphics.Color.BLACK;						// message Window 的背景颜色
	private int cornerPositionPercent = 75;		// 尖角出现位置的相对canvas宽度百分比
	private Paint paint = null;
	private Path path = null;
	
	public MessageWindowView(Context context){
		super(context);
		paint = new Paint();
		path = new Path();
		bgColor = context.getResources().getColor(R.color.bella_color_black_dark);
	}
	
	public MessageWindowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		path = new Path();
		bgColor = context.getResources().getColor(R.color.bella_color_black_dark);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(bgColor);
		//canvas.drawRect(new Rect(0,0,width, height), paint);
		// draw the corner
		float cornerWidth = width/10;
		path.moveTo(0, 0);
		path.lineTo(0,  height - cornerWidth);
		path.lineTo(width*cornerPositionPercent/100 - cornerWidth/2, height-cornerWidth);
		path.lineTo(width*cornerPositionPercent/100 - cornerWidth, height);
		path.lineTo(width*cornerPositionPercent/100 + cornerWidth/2, height - cornerWidth);
		path.lineTo(width,  height - cornerWidth);
		path.lineTo(width, 0);
		path.lineTo(0, 0);
		canvas.drawPath(path, paint);
	}
	

	
}
