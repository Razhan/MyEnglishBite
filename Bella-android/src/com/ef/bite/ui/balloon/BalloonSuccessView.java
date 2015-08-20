package com.ef.bite.ui.balloon;

import android.annotation.SuppressLint;
import com.ef.bite.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ef.bite.utils.FontHelper;

public class BalloonSuccessView extends View {

	int layoutWidth = 0;
	int layoutHeight = 0;
	int color = 0;// new balloon修改
	private Paint mImagePaint = null;
	private Paint mNodePaint = null;
	private Paint mLabelPaint = null;
	private Paint mLinePaint = null;
	private Bitmap mBallBitmap = null; // 气球圆图像
	private Bitmap mParatrooperBitmap = null; // 伞兵的图像
	private float Label_Size = 40; // 气球单词的size
	public final static float Label_Padding_Size = 30; // 单词与气球两边的padding size
	public final static float Balloon_Overlap_Size = 20; // 气球直接重叠的长度

	private String[] mWords;
	private Point mHandPoint = new Point(); // 伞兵手的位置
	private Point[] mBallNodePoints; // 气球节点的中心位置
	private float mMaxBallWidth; // 最大气球的宽
	private float[] mBallWidthArray; // 气球圆的宽

	private float curBallsDrawingWidth = 0;
	private int paratrooperImageHeight = 0;

	private float linex;
	private float liney;

	public BalloonSuccessView(Context context) {
		super(context);
		prepare(context);
	}

	public BalloonSuccessView(Context context, AttributeSet attrs) {
		super(context, attrs);
		prepare(context);
	}

	private void prepare(Context context) {
		mImagePaint = new Paint();
		mNodePaint = new Paint();
		mLabelPaint = new Paint();
		mLinePaint = new Paint();
		// 气球画板设置
		color = context.getResources().getColor(R.color.bella_balloon_red_bg);
		// ColorFilter filter = new LightingColorFilter(color, 1);
		// mImagePaint.setColorFilter(filter);//new balloon修改
		// 文字画板设置
		int labelColor = context.getResources().getColor(R.color.white);
		Label_Size = context.getResources().getDimension(
				R.dimen.balloon_label_size);
		mLabelPaint.setColor(labelColor);
		mLabelPaint.setTextSize(Label_Size);
		mLabelPaint.setTypeface(FontHelper.getFont(context,
				FontHelper.FONT_Museo500));
		mLabelPaint.setTextAlign(Align.CENTER);
		// 线的画板设置
		mLinePaint.setColor(color);
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setStyle(Style.FILL);
		// 气球节点画报设置
		mNodePaint.setColor(color);
		mNodePaint.setStyle(Style.STROKE);
		// 图像准备
		try {
			// mBallBitmap =
			// BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.balloon_ball);
			mBallBitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.balloon_orange);
			mParatrooperBitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.balloon_paratrooper);
			paratrooperImageHeight = mParatrooperBitmap.getHeight();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获得helper icon的高度
	 * 
	 * @return
	 */
	public int getParatrooperHeight() {
		return paratrooperImageHeight;
	}

	/**
	 * 初始化
	 * 
	 * @param words
	 */
	public void initialize(String[] words) {
		if (words != null && words.length > 0) {
			mWords = new String[words.length];
			mBallNodePoints = new Point[words.length];
			mBallWidthArray = new float[words.length];
			for (int index = 0; index < words.length; index++) {
				String word = words[index];
				float labelWidth = mLabelPaint.measureText(word);
				mBallWidthArray[index] = labelWidth + Label_Padding_Size * 2;
				if (mMaxBallWidth < mBallWidthArray[index])
					mMaxBallWidth = mBallWidthArray[index];
				mWords[index] = words[index];
				mBallNodePoints[index] = new Point();
			}
			this.invalidate();
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		curBallsDrawingWidth = 0;
		if (mWords == null || mWords.length <= 0)
			return;

		// Drawing balls && label
		if (mBallBitmap != null) {
			for (int index = 0; index < mWords.length; index++) {
				float ballWidth = mBallWidthArray[index];
				float ballCenterX = curBallsDrawingWidth + ballWidth / 2; // 圆的中心点X
				float ballCenterY = mMaxBallWidth - ballWidth / 2; // 圆的中心点Y
				// 圆的宽 - 直径
				// draw ball
				Bitmap ball = scaleBitmap(mBallBitmap, (int) ballWidth,
						(int) ballWidth);
				canvas.drawBitmap(ball, curBallsDrawingWidth, mMaxBallWidth
						- ballWidth, mImagePaint);

				// draw word
				int yPos = (int) (ballCenterY - ((mLabelPaint.descent() + mLabelPaint
						.ascent()) / 2));
				canvas.drawText(mWords[index], ballCenterX, yPos, mLabelPaint);

				// draw node
				float nodeThinkness = ballWidth / 16;
				mNodePaint.setStrokeWidth(nodeThinkness * 3 / 2);
				// RectF nodeRect = new
				// RectF(ballCenterX-nodeThinkness*2,mMaxBallWidth-nodeThinkness*3
				// ,
				// ballCenterX+nodeThinkness*2 , mMaxBallWidth+nodeThinkness);
				RectF nodeRect = new RectF(ballCenterX - nodeThinkness * 2
						+ nodeThinkness / 2, mMaxBallWidth - nodeThinkness * 3
						+ nodeThinkness / 2, ballCenterX + nodeThinkness * 2
						- nodeThinkness / 2, mMaxBallWidth + nodeThinkness
						- nodeThinkness / 2);
				// 原扇形节点
				// canvas.drawArc(nodeRect, 60, 60, false, mNodePaint);

				linex = ballCenterX - nodeThinkness * 2 + nodeThinkness / 2;
				liney = mMaxBallWidth - nodeThinkness * 3 + nodeThinkness / 2;

				mBallNodePoints[index].x = (int) ballCenterX;
				mBallNodePoints[index].y = (int) (mMaxBallWidth + nodeThinkness);

				curBallsDrawingWidth = curBallsDrawingWidth + ballWidth
						- Balloon_Overlap_Size;
			}
		}

		// Drawing Paratrooper
		if (mParatrooperBitmap != null) {
			mHandPoint.x = (int) (curBallsDrawingWidth + Balloon_Overlap_Size) / 2;
			mHandPoint.y = layoutHeight
					- (mParatrooperBitmap == null ? 0 : mParatrooperBitmap
							.getHeight());
			canvas.drawBitmap(mParatrooperBitmap, mHandPoint.x
					- mParatrooperBitmap.getWidth(), mHandPoint.y, null);
		}

		// Drawing Lines
		for (int index = 0; index < mBallNodePoints.length; index++) {
			Point startPoint = mBallNodePoints[index];
			// 原直線
			// canvas.drawLine(startPoint.x, startPoint.y, mHandPoint.x - 4,
			// mHandPoint.y + 3, mLinePaint);
			canvas.drawLine(startPoint.x, liney, mHandPoint.x - 4,
					mHandPoint.y + 3, mLinePaint);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		layoutWidth = right - left;
		layoutHeight = bottom - top;
	}

	public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
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
}
