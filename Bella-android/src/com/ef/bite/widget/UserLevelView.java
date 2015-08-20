package com.ef.bite.widget;

import android.animation.Animator;
import com.ef.bite.R;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ef.bite.utils.ScoreLevelHelper;

public class UserLevelView extends View {

	public final static int ANIMATION_DURATION = 1000;
	int drawColor;
	int increaseColor;
	int level = 20;
	int levelUpScore = 100;
	int currentScore = 75;
	int increaseScore = 0;
	int circleThickness = 5;
	int levelThinckness = 5;
	int increaseThickness = 10;
	RectF rectF;
	Paint paint;

	int latest_animator_value = 0;
	boolean isIncreaseProgressing = false;

	public UserLevelView(Context context) {
		super(context);
		paint = new Paint();
		drawColor = context.getResources().getColor(
				R.color.bella_color_black_dark);
		circleThickness = (int) context.getResources().getDimension(
				R.dimen.level_circle_thickness);
		increaseThickness = (int) context.getResources().getDimension(
				R.dimen.level_circle_increase_thickness);
	}

	public UserLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		drawColor = context.getResources().getColor(
				R.color.bella_color_black_dark);
		circleThickness = (int) context.getResources().getDimension(
				R.dimen.level_circle_thickness);
		increaseThickness = (int) context.getResources().getDimension(
				R.dimen.level_circle_increase_thickness);
	}

	/**
	 * 初始化
	 * 
	 * @param level
	 *            当前的级别
	 * @param currentScore
	 *            超过当前级别基准分的分数
	 * @param levelupScore
	 *            当前级别到下一个级别需要的分数
	 */
	public void initialize(int score) {
		this.level = ScoreLevelHelper.getDisplayLevel(score);
		this.currentScore = ScoreLevelHelper.getCurrentLevelExistedScore(score);
		this.levelUpScore = ScoreLevelHelper.getCurrentLevelScore(score);
		this.invalidate();
	}

	/**
	 * 开始增加分数的动画
	 * 
	 * @param increasedScore
	 */
	public void startIncreasingScore(int increasedScore, int increaseColor) {
		this.increaseColor = increaseColor;
		latest_animator_value = 0;
		ValueAnimator animator = ValueAnimator.ofInt(0, increasedScore);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int cur_value = (Integer) animation.getAnimatedValue();
				if ((currentScore + cur_value - latest_animator_value) >= levelUpScore) { // 升级
					level++;
					currentScore = currentScore
							+ (cur_value - latest_animator_value)
							- levelUpScore;
				} else {
					currentScore = currentScore
							+ (cur_value - latest_animator_value);
				}
				latest_animator_value = cur_value;
				invalidate();
			}
		});
		animator.addListener(new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				isIncreaseProgressing = true;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				isIncreaseProgressing = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				isIncreaseProgressing = false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		animator.setDuration(ANIMATION_DURATION);
		animator.start();
	}

	/**
	 * 设置显示的颜色
	 * 
	 * @param color
	 */
	public void setColor(int color, int lightColor) {
		drawColor = color;
		this.invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int ht = increaseThickness;
		ht = ht - ht / 2 + 2;
		if (increaseThickness % 2 != 0)
			ht -= 1;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Current Score in Level
		if (isIncreaseProgressing) {
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(increaseThickness);
			paint.setColor(increaseColor);
			canvas.drawArc(rectF, 270, (currentScore * 365) / levelUpScore,
					false, paint);
		} else {
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(circleThickness);
			paint.setColor(drawColor);
			canvas.drawArc(rectF, 270, (currentScore * 365) / levelUpScore,
					false, paint);
		}

		// score to Level up
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(circleThickness);
		paint.setColor(drawColor);
		paint.setAlpha(20);
		canvas.drawArc(rectF, 270 + (currentScore * 365) / levelUpScore,
				(levelUpScore - currentScore) * 365 / levelUpScore, false,
				paint);

		paint.setStyle(Style.FILL);
		paint.setTextSize(canvas.getWidth() / 2);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setStrokeWidth(levelThinckness);
		paint.setColor(drawColor);
		// paint.setTypeface(FontHelper.getFont(getContext(),
		// FontHelper.FONT_Museo300));
		int yPos = (int) (canvas.getHeight() / 2 - ((paint.descent() + paint
				.ascent()) / 2));
		canvas.drawText(Integer.toString(level), canvas.getWidth() / 2, yPos,
				paint);
	}

}
