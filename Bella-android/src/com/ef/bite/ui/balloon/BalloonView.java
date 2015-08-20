package com.ef.bite.ui.balloon;

import android.content.Context;
import com.ef.bite.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ef.bite.utils.FontHelper;

public class BalloonView extends View {
	public final static int LABEL_SIZE_RATIO = 20; // 字体大小和手机屏幕宽度的比例， 1/20

	private Paint mImagePaint;
	private Paint mLabelPaint;
	private String mLabel; // 气球上的单词
	private int mLabelColor; // 单词的颜色
	private int mImageColor = -1; // 气球背景图的颜色
	private int mImageColorId = -1;// 气球背景图颜色ID
	private int mImageResId = -1; // 气球背景图
	private Bitmap mImageBitmap;
	private float Label_Size = 40; // 气球单词的size
	private float Label_Padding_Size = 30; // 单词与气球两边的padding size

	private float layoutHeight = 0;
	private float layoutWidth = 0;
	private float labelWidth = 0;
	private float labelHeight = 0;
	private MatrixPoint centerPoint; // balloon中心点的坐标

	private Animation shakAnimation;

	public BalloonView(Context context) {
		super(context);
		mLabelPaint = new Paint();
		// mImageResId = R.drawable.balloon_ball;//原气球图片
		mImageResId = R.drawable.balloon_white;
		mLabelColor = context.getResources().getColor(
				R.color.bella_chunk_text_dark);
		// Text样式设置
		Label_Size = context.getResources().getDimension(
				R.dimen.balloon_label_size);
		mLabelPaint.setColor(mLabelColor);
		mLabelPaint.setTextSize(Label_Size);
		mLabelPaint.setTypeface(FontHelper.getFont(context,
				FontHelper.FONT_Museo500));
		mLabelPaint.setTextAlign(Align.CENTER);
		mLabelPaint.setAntiAlias(true);
		mImagePaint = new Paint();
	}

	/** 初始化气球单词 **/
	public void initialize(String label) {
		if (label == null || label.isEmpty())
			return;
		this.mLabel = label;
		// int count = label.length();
		this.labelWidth = mLabelPaint.measureText(label);
		this.layoutWidth = this.labelWidth + Label_Padding_Size * 2;
		this.layoutHeight = this.layoutWidth;
		try {
			if (mImageResId > 0)
                this.mImageBitmap = BitmapFactory.decodeResource(getContext()
                        .getResources(), mImageResId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.invalidate();
	}

	/**
	 * 设置气球背景颜色
	 * 
	 * @param ballColor
	 *            气球的颜色
	 * @param labelColor
	 *            文字的颜色
	 */
	// public void setBalloonColor(int ballColor, int labelColor) {// 原气球背景文字背景
	// this.mImageColor = ballColor;
	// ColorFilter filter = new LightingColorFilter(ballColor, 1);
	// mImagePaint.setColorFilter(filter);
	// mLabelColor = labelColor;
	// mLabelPaint.setColor(mLabelColor);
	// this.invalidate();
	// }

	/**
	 * 设置气球背景颜色
	 * 
	 * @param ballColor_orange
	 *            气球的颜色
	 * @param labelColor
	 *            文字的颜色
	 */
	public void setBalloonColor(int ballColorID, int labelColor) {
		this.mImageColorId = ballColorID;
		if (this.mImageColorId > 0) {
			this.mImageBitmap = BitmapFactory.decodeResource(getContext()
					.getResources(), mImageColorId);
		}
		mLabelColor = labelColor;
		mLabelPaint.setColor(mLabelColor);
		this.invalidate();
	}

	/** 获得单词 **/
	public String getLabel() {
		return this.mLabel;
	}

	/** 获得气球的宽半径 **/
	public float getWidthRadius() {
		return layoutWidth / 2;
	}

	/** 获得气球的高半径 **/
	public float getHeightRaius() {
		return layoutHeight / 2;
	}

	/** 设置气球的中心点坐标 **/
	public void setCenterPoint(MatrixPoint center) {
		this.centerPoint = center;
	}

	/** 获得气球的中心点坐标 **/
	public MatrixPoint getCenterPoint() {
		return centerPoint;
	}

	/** 开始漂浮动画 **/
	public void startShaking() {
		shakAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.balloon_shaking);
		startAnimation(shakAnimation);
	}

	/** 停止漂浮动画 **/
	public void stopShaking() {
		if (shakAnimation != null && !shakAnimation.hasEnded()) {
			shakAnimation.cancel();
			clearAnimation();
			shakAnimation = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mImageBitmap != null && mLabel != null && !mLabel.isEmpty()) {
			Bitmap scaled = scaleBitmap(mImageBitmap, (int) layoutWidth,
					(int) layoutHeight);
			drawBitmap(canvas, scaled, layoutWidth / 2, layoutHeight / 2);
			int yPos = (int) (layoutHeight / 2 - ((mLabelPaint.descent() + mLabelPaint
					.ascent()) / 2));
			canvas.drawText(mLabel, layoutWidth / 2, yPos, mLabelPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) layoutWidth, (int) layoutHeight);
	}

	/**
	 * Draw bitmap at the center of point(x,y)
	 * 
	 * @param canvas
	 * @param map
	 * @param x
	 * @param y
	 */
	private void drawBitmap(Canvas canvas, Bitmap map, float x, float y) {
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		float boardPosX = x - mapWidth / 2;
		float boardPosY = y - mapHeight / 2;
		if (mImageColor <= 0) {
			canvas.drawBitmap(map, boardPosX, boardPosY, mImagePaint);
		} else
			canvas.drawBitmap(map, boardPosX, boardPosY, null);
	}

	/**
	 * Scales the provided bitmap to have the height and width provided.
	 * (Alternative method for scaling bitmaps since
	 * Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
	 * 
	 * @param bitmap
	 *            is the bitmap to scale.
	 * @param newWidth
	 *            is the desired width of the scaled bitmap.
	 * @param newHeight
	 *            is the desired height of the scaled bitmap.
	 * @return the scaled bitmap.
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight,
				Config.ARGB_8888);

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

	/**
	 * 选择了错误的气球事件
	 * 
	 */
	public interface OnWrongSelectListener {
		void OnWrong(BalloonView balloon);
	}

	/**
	 * 选择了正确的气球事件
	 * 
	 */
	public interface OnCorrectSelectListener {
		void OnCorrect(BalloonView selectBalloon);
	}

	/**
	 * 检测是否选择正确的气球事件
	 * 
	 */
	public interface OnResultCheckListener {
		boolean OnCheck(BalloonView selectBalloon);
	}

}
