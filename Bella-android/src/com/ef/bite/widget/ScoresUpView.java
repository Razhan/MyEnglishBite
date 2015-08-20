package com.ef.bite.widget;

import android.animation.ValueAnimator;
import com.ef.bite.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 分数增加过程图
 * @author Admin
 *
 */
public class ScoresUpView extends View implements ValueAnimator.AnimatorUpdateListener{
	
	private int level;			// current level
	private int totalScore; 	// total score for level up
	private int existedScore = 0; 	// exist score for level up
	private int addedScore = 0;
	private int excessScore = 0;		// excess score for level up
	private int newScore = 0;
	private int existedScoreColor;
	private int addedScoreColor;
	private int excessScoreColor;
	
	private int backThickness;
	private int frontThickness;
	private RectF rectFBack;
	private RectF rectFFront;
	private Paint paint;
	private ValueAnimator animation;
	private TextView scoreText;
	boolean isLevelUp = false;
	
	public final static int ANIMATION_DURACTION = 2000;
	
	public ScoresUpView(Context context){
		super(context);
		initialize();
	}
	
	public ScoresUpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public void setScoreText(TextView scoreText){
		this.scoreText = scoreText;
	}
	
	/**
	 * 设置积分，
	 * @param level 级别
	 * @param levelupScore	当前level升到下一level所需的所有积分
	 * @param existScore	当前level已经有的积分
	 * @param newScore	需要新增的积分
	 */
	public void setScores(int level, int levelupScore, int existScore, int newScore){
		this.level = level;
		this.totalScore = levelupScore;
		this.existedScore = existScore;
		this.newScore = newScore;
		animation = ValueAnimator.ofInt(0,this.newScore);
		animation.addUpdateListener(this);
		animation.setDuration(ANIMATION_DURACTION);
		animation.start();
	}
	
	private void initialize(){
		paint = new Paint();
		level = 19;
		existedScore = 0;
		totalScore = 100;
		backThickness = 20;
		frontThickness = 40;
		existedScoreColor = getContext().getResources().getColor(R.color.bella_color_orange_light);
		addedScoreColor = getContext().getResources().getColor(R.color.bella_color_cellobrate_yellow);
		excessScoreColor = getContext().getResources().getColor(R.color.bella_popup_score_up_added);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int ht = frontThickness/2;
		if(frontThickness%2!=0) ht-=1;
		rectFFront = new RectF(ht, ht, getWidth()-ht, getHeight()-ht);
		ht = ht + ht/2;
		rectFBack = new RectF(ht,ht,getWidth()-ht,getHeight()-ht);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// draw existed circle
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(backThickness);
		paint.setColor(existedScoreColor);
		canvas.drawArc(rectFBack, 270, (float)existedScore * 360 / (float)totalScore, false, paint);
		
		// draw added score circle
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(frontThickness);
		paint.setColor(addedScoreColor);
		canvas.drawArc(rectFFront, 270 + (float)existedScore * 360 / (float)totalScore, (float)addedScore * 360 / (float)totalScore, false, paint);
		
		// draw excess score arc
		if(excessScore > 0){
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(frontThickness);
			paint.setColor(excessScoreColor);
			canvas.drawArc(rectFFront, 270, (float)excessScore * 360 / (float)totalScore, false, paint );
		}
		
		// draw current level
		paint.setStyle(Style.FILL);
		paint.setColor(existedScoreColor);
		paint.setTextSize(canvas.getWidth()/3);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(Integer.toString(level), canvas.getWidth()/2, canvas.getHeight() * 5/8, paint);
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		 this.addedScore = (Integer)animation.getAnimatedValue();
		 if((this.addedScore + existedScore)>=totalScore){
			 excessScore = existedScore + this.addedScore - totalScore;
			 addedScore = totalScore - existedScore;
			 if(!isLevelUp){
				 level ++;
				 isLevelUp = true;
			 }
		 }
		 if(scoreText!=null)
			 scoreText.setText("+" + (addedScore + excessScore) + " xp");
		 invalidate();
	}
	
	
}
