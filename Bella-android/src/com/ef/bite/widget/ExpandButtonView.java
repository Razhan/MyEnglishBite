package com.ef.bite.widget;


import android.animation.ValueAnimator;
import com.ef.bite.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 头像image外部套一个圈
 * @author Allen
 *
 */
public class ExpandButtonView extends ImageView implements ValueAnimator.AnimatorUpdateListener {
	
	RectF rectF;
	Paint paint ;
	Paint imagePaint;
	int whiteColor;
	int blackColor;
	int fillColor_bg;
	int fillColor_front;
	int duration;
	ValueAnimator animimation ;
	boolean isExpand = true;
	
	final float[] from_bg_color = new float[3], to_bg_color =   new float[3], hsv_bg_color  = new float[3],
		from_front_color = new float[3], to_front_color = new float[3], hsv_front_color = new float[3];
	                 // transition color
	
	public ExpandButtonView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    paint = new Paint();
	    imagePaint = new Paint();
	    blackColor = context.getResources().getColor(R.color.bella_color_black_dark);
	    whiteColor = context.getResources().getColor(R.color.white);
	    fillColor_bg = blackColor;
	    fillColor_front = whiteColor;
	}
	
	public void setColor(int bgColor,int frontColor){
		fillColor_bg = bgColor;
		fillColor_front = frontColor;
		invalidate();
	}
	
	public void setDuration(int millseconds){
		duration = millseconds;
	}
	
	
	public void startExpand(){
		Color.colorToHSV(blackColor, from_bg_color);   // from black
		Color.colorToHSV(whiteColor, to_bg_color);     // to white
		Color.colorToHSV(whiteColor, from_front_color);  // from white
		Color.colorToHSV(blackColor, to_front_color);	// to black
		animimation = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
		animimation.addUpdateListener(this);
		animimation.setDuration(duration);
		animimation.start();
	}
	
	public void startCollipse(){
		Color.colorToHSV(whiteColor, from_bg_color);   // from white
		Color.colorToHSV(blackColor, to_bg_color);     // to black
		Color.colorToHSV(blackColor, from_front_color);  // from black
		Color.colorToHSV(whiteColor, to_front_color);	// to white
		animimation = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
		animimation.addUpdateListener(this);
		animimation.setDuration(duration);
		animimation.start();
	}
	
	/**
	 * 更新画图区域size
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		rectF = new RectF(0,0, getWidth(), getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw the background 
		paint.setStyle(Style.FILL);
		paint.setColor(fillColor_bg);
		canvas.drawCircle(rectF.width()/2, rectF.height()/2, rectF.width()/2, paint);

	    // draw the front bitmap
		Bitmap bitmap =  ((BitmapDrawable)this.getDrawable()).getBitmap() ;
		int bitmapW = bitmap.getWidth(), bitmapH = bitmap.getHeight();
		int canvasW = canvas.getWidth(), canvasH = canvas.getHeight();
		float ratioW = bitmapW/canvasW, ratioH = bitmapH/canvasH;
		//Bitmap scaledBitmap = scaleBitmap(bitmap,(int)(bitmapW*scaledSize), (int)(bitmapH*scaledSize));
		drawBitmap(canvas, bitmap, rectF.width()/2, rectF.height()/2, fillColor_front);
	}
	
	private void drawBitmap(Canvas canvas, Bitmap map, float x, float y, int color){
		ColorFilter filter = new LightingColorFilter(color, 1);
		imagePaint.setColorFilter(filter);
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
	    float boardPosX = x - mapWidth/2;
	    float boardPosY = y - mapHeight/2;
	    canvas.drawBitmap(map, boardPosX, boardPosY, imagePaint);
	}
	
	/**
	 * Scales the provided bitmap to have the height and width provided.
	 * (Alternative method for scaling bitmaps
	 * since Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
	 * 
	 * @param bitmap is the bitmap to scale.
	 * @param newWidth is the desired width of the scaled bitmap.
	 * @param newHeight is the desired height of the scaled bitmap.
	 * @return the scaled bitmap.
	 */
	 public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
	  Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

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
	 
	 public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		    Bitmap sbmp;
		    if(bmp.getWidth() != radius || bmp.getHeight() != radius)
		        sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		    else
		        sbmp = bmp;
		    Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
		            sbmp.getHeight(), Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);

		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		    paint.setAntiAlias(true);
		    paint.setFilterBitmap(true);
		    paint.setDither(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(Color.parseColor("#BAB399"));
		    canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
		            sbmp.getWidth() / 2+0.1f, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(sbmp, rect, rect, paint);


		            return output;
		}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		hsv_bg_color[0] = from_bg_color[0] + (to_bg_color[0] - from_bg_color[0])*animation.getAnimatedFraction();
        hsv_bg_color[1] = from_bg_color[1] + (to_bg_color[1] - from_bg_color[1])*animation.getAnimatedFraction();
        hsv_bg_color[2] = from_bg_color[2] + (to_bg_color[2] - from_bg_color[2])*animation.getAnimatedFraction();
        
        hsv_front_color[0] = from_front_color[0] + (to_front_color[0] - from_front_color[0]) *animation.getAnimatedFraction();
        hsv_front_color[1] = from_front_color[1] + (to_front_color[1] - from_front_color[1]) *animation.getAnimatedFraction();
        hsv_front_color[2] = from_front_color[2] + (to_front_color[2] - from_front_color[2]) *animation.getAnimatedFraction();
        
		setColor(Color.HSVToColor(hsv_bg_color),Color.HSVToColor(hsv_front_color));
	}
}
