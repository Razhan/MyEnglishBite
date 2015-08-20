package com.ef.bite.widget;

import android.content.Context;
import com.ef.bite.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.utils.FontHelper;

/**
 * 自定义的Actionbar
 * 
 * @author Allen
 * 
 */
public class ActionbarLayout extends RelativeLayout {

	ImageButton mLeft;
	TextView mTitle;
	ImageView mCenterImage;
	ImageButton mRight;
	UserLevelView mRightLevel;
	View.OnClickListener mLeftClick;
	View.OnClickListener mRightClick;
	private DotProgressbar progressbar;

	public ActionbarLayout(Context context) {
		super(context);
		getViews();
	}

	public ActionbarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		getViews();
	}

	public ActionbarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getViews();
	}

	private void getViews() {
		inflate(getContext(), R.layout.widget_action_bar_layout, this);
		mLeft = (ImageButton) findViewById(R.id.widget_actionbar_left);
		mTitle = (TextView) findViewById(R.id.widget_actionbar_title);
		mCenterImage = (ImageView) findViewById(R.id.widget_actionbar_center_img);
		mRight = (ImageButton) findViewById(R.id.widget_actionbar_right);
		mRightLevel = (UserLevelView) findViewById(R.id.widget_actionbar_right_level);
		progressbar = (DotProgressbar) findViewById(R.id.progressbar);
	}

	/**
	 * 初始化Actionbar，中间是标题
	 * 
	 * @param title
	 *            标题，如果null，则标题不显示
	 * @param leftImgId
	 *            左图标resourceId, 如果 <=0, 则不显示
	 * @param rightImgId
	 *            右图标resourceId, 如果<=0, 则不显示
	 * @param leftClick
	 *            左图标的点击事件，可为null
	 * @param rightClick
	 *            右图标的点击事件，可为null
	 */
	public void initiWithTitle(String title, int leftImgId, int rightImgId,
			View.OnClickListener leftClick, View.OnClickListener rightClick) {
		try {
			// 标题
			if (title == null)
				mTitle.setVisibility(View.INVISIBLE);
			else {
				mTitle.setVisibility(View.VISIBLE);
				mTitle.setText(title);
			}
			// 左按钮
			if (leftImgId <= 0)
				mLeft.setVisibility(View.INVISIBLE);
			else {
				mLeft.setVisibility(View.VISIBLE);
				mLeft.setImageResource(leftImgId);
				if (leftClick != null)
					mLeft.setOnClickListener(leftClick);
			}
			// 有按钮
			if (rightImgId <= 0)
				mRight.setVisibility(View.INVISIBLE);
			else {
				mRight.setVisibility(View.VISIBLE);
				mRight.setImageResource(rightImgId);
				if (rightClick != null)
					mRight.setOnClickListener(rightClick);
			}
			FontHelper
					.applyFont(getContext(), mTitle, FontHelper.FONT_OpenSans);
		} catch (Exception ex) {
			Log.e("ActionbarLayout", "Fail to initialize with title!", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 初始化，中间是一个图标
	 * 
	 * @param centerImgId
	 *            如果<=0,代表未指定任何图标
	 * @param leftImgId
	 *            左图标resourceId, 如果 <=0, 则不显示
	 * @param rightImgId
	 *            右图标resourceId, 如果<=0, 则不显示
	 * @param leftClick
	 *            左图标的点击事件，可为null
	 * @param rightClick
	 *            右图标的点击事件，可为null
	 */
	public void initiWithImage(int centerImgId, int leftImgId, int rightImgId,
			View.OnClickListener leftClick, View.OnClickListener rightClick) {
		try {
			mTitle.setVisibility(View.GONE);
			if (centerImgId <= 0)
				mCenterImage.setVisibility(View.GONE);
			else {
				mCenterImage.setVisibility(View.VISIBLE);
				mCenterImage.setImageResource(centerImgId);
			}
			// 左按钮
			if (leftImgId <= 0)
				mLeft.setVisibility(View.INVISIBLE);
			else {
				mLeft.setVisibility(View.VISIBLE);
				mLeft.setImageResource(leftImgId);
				if (leftClick != null)
					mLeft.setOnClickListener(leftClick);
			}
			// 有按钮
			if (rightImgId <= 0)
				mRight.setVisibility(View.INVISIBLE);
			else {
				mRight.setVisibility(View.VISIBLE);
				mRight.setImageResource(rightImgId);
				if (rightClick != null)
					mRight.setOnClickListener(rightClick);
			}
		} catch (Exception ex) {
			Log.e("ActionbarLayout", "Fail to initialize with image !", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 初始化Actionbar，中间是help图标，右边是user level
	 * 
	 * @param centerImgId
	 *            如果<=0,代表未指定任何图标
	 * @param left
	 *            左图标resourceId, 如果 <=0, 则不显示
	 * @param score
	 *            用户的分数
	 * @param leftClick
	 *            左图标的点击事件，可为null
	 */
	public void initiWithCenterImageRightLevel(int centerImgId, int leftImgId,
			int score, View.OnClickListener leftClick) {
		mTitle.setVisibility(View.GONE);
		mRight.setVisibility(View.GONE);
		if (centerImgId <= 0)
			mCenterImage.setVisibility(View.GONE);
		else {
			mCenterImage.setVisibility(View.GONE);
			mCenterImage.setImageResource(centerImgId);
		}
		// 左按钮
		if (leftImgId <= 0)
			mLeft.setVisibility(View.INVISIBLE);
		else {
			mLeft.setVisibility(View.VISIBLE);
			mLeft.setImageResource(leftImgId);
			mLeft.setBackgroundResource(R.drawable.button_action_bar_none_background);
			if (leftClick != null)
				mLeft.setOnClickListener(leftClick);
		}
		// 右边的userlevel
		if (score >= 0) {
			mRightLevel.setVisibility(View.VISIBLE);
			mRightLevel.initialize(score);
		}
	}

	/** 获得用户等级view **/
	public UserLevelView getUserLevelView() {
		return mRightLevel;
	}

	public DotProgressbar getProgressbar() {
		return progressbar;
	}
}
