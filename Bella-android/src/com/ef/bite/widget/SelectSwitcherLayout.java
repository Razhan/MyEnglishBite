package com.ef.bite.widget;

import android.content.Context;
import com.ef.bite.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.utils.FontHelper;

public class SelectSwitcherLayout extends RelativeLayout {

	RelativeLayout mTotalLayout;
	LinearLayout mSwitchLayout; // 总的layout
	LinearLayout mLeftLayout; // 左边区域
	LinearLayout mRightLayout; // 右边区域
	ImageView mLeftIcon; // 左边的icon
	ImageView mRightIcon; // 右边icon
	TextView mLeftText; // 左右的text
	TextView mRightText; // 右边的Text
	TextView mLeftInfo; // 左边文本
	TextView mRightInfo; // 右边文本
	ImageView mLeftCorner; // 左边的下角
	ImageView mRightCorner; // 右边下角

	// resource
	String leftInfo = ""; // 左边说明
	String rightInfo = ""; // 右边说明
	int leftIconResId = R.drawable.leaderboard_friends;
	int leftIconSelectedResId = R.drawable.leaderboard_friends_i;
	String leftText = "";
	String rightText = "";
	int rightIconResId = R.drawable.leaderboard_global;
	int rightIconSelectedResId = R.drawable.leaderboard_global_i;
	View.OnClickListener mLeftClick = null;
	View.OnClickListener mRightClick = null;
	boolean isLeftSelected = true; // 默认是左边选中

	public SelectSwitcherLayout(Context context) {
		super(context);
		getViews();
	}

	public SelectSwitcherLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		getViews();
	}

	public SelectSwitcherLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		getViews();
	}

	private void getViews() {
		inflate(getContext(), R.layout.widget_switcher_layout, this);
		mTotalLayout = (RelativeLayout) findViewById(R.id.widget_switcher_total_layout);
		mSwitchLayout = (LinearLayout) findViewById(R.id.widget_switcher_layout);
		mLeftLayout = (LinearLayout) findViewById(R.id.widget_switcher_left);
		mRightLayout = (LinearLayout) findViewById(R.id.widget_switcher_right);
		mLeftIcon = (ImageView) findViewById(R.id.widget_switcher_left_icon);
		mRightIcon = (ImageView) findViewById(R.id.widget_switcher_right_icon);
		mLeftText = (TextView) findViewById(R.id.widget_switcher_left_text);
		mRightText = (TextView) findViewById(R.id.widget_switcher_right_text);
		mLeftInfo = (TextView) findViewById(R.id.widget_switcher_left_info);
		mRightInfo = (TextView) findViewById(R.id.widget_switcher_right_info);
		mLeftCorner = (ImageView) findViewById(R.id.widget_switcher_left_corner);
		mRightCorner = (ImageView) findViewById(R.id.widget_switcher_right_corner);

	}

	/**
	 * 初始化图标
	 * 
	 * @param leftInfo
	 * @param leftIcon
	 * @param leftSelIcon
	 * @param rightInfo
	 * @param rightIcon
	 * @param rightSelIcon
	 */
	public void initializeWithIcon(String leftInfo, int leftIcon,
			int leftSelIcon, String rightInfo, int rightIcon, int rightSelIcon,
			OnClickListener leftClick, OnClickListener rightClick) {
		this.leftInfo = leftInfo;
		this.leftIconResId = leftIcon;
		this.leftIconSelectedResId = leftSelIcon;
		this.rightInfo = rightInfo;
		this.rightIconResId = rightIcon;
		this.rightIconSelectedResId = rightSelIcon;
		this.mLeftClick = leftClick;
		this.mRightClick = rightClick;

		// 初始化左边被选中
		mLeftInfo.setText(leftInfo);
		mRightInfo.setText(rightInfo);
		// 字体
		FontHelper.applyFont(getContext(), mLeftInfo, FontHelper.FONT_Museo500);
		FontHelper
				.applyFont(getContext(), mRightInfo, FontHelper.FONT_Museo500);
		selectLeft();
		mLeftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLeftSelected)
					return;
				isLeftSelected = true;
				// Friends
				selectLeft();
				if (mLeftClick != null)
					mLeftClick.onClick(v);
			}
		});
		mRightLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isLeftSelected)
					return;
				isLeftSelected = false;
				// Global
				selectRight();
				if (mRightClick != null)
					mRightClick.onClick(v);
			}
		});
	}

	/**
	 * 初始化文本
	 * 
	 * @param leftInfo
	 * @param leftText
	 * @param rightInfo
	 * @param rightText
	 * @param isLeftSelected
	 *            是否默认左边选中
	 * @param leftClick
	 * @param rightClick
	 */
	public void initializeWithText(String leftInfo, String leftText,
			String rightInfo, String rightText, boolean leftSelected,
			OnClickListener leftClick, OnClickListener rightClick) {
		this.leftInfo = leftInfo;
		this.leftText = leftText;
		this.rightInfo = rightInfo;
		this.rightText = rightText;
		this.mLeftClick = leftClick;
		this.mRightClick = rightClick;
		this.isLeftSelected = leftSelected;
		// 初始化
		mLeftIcon.setVisibility(View.GONE);
		mRightIcon.setVisibility(View.GONE);
		mLeftText.setVisibility(View.VISIBLE);
		mRightText.setVisibility(View.VISIBLE);
		mLeftInfo.setText(leftInfo);
		mRightInfo.setText(rightInfo);
		mLeftText.setText(leftText);
		mRightText.setText(rightText);
		// 字体
		FontHelper.applyFont(getContext(), mLeftText, FontHelper.FONT_Museo500);
		FontHelper
				.applyFont(getContext(), mRightText, FontHelper.FONT_Museo500);
		FontHelper.applyFont(getContext(), mLeftInfo, FontHelper.FONT_Museo500);
		FontHelper
				.applyFont(getContext(), mRightInfo, FontHelper.FONT_Museo500);
		if (this.isLeftSelected)
			selectLeft();
		else
			selectRight();
		mLeftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLeftSelected)
					return;
				isLeftSelected = true;
				selectLeft();
				if (mLeftClick != null)
					mLeftClick.onClick(v);
			}
		});
		mRightLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isLeftSelected)
					return;
				isLeftSelected = false;
				selectRight();
				if (mRightClick != null)
					mRightClick.onClick(v);
			}
		});
	}

	/**
	 * 更新左边和右边的文本信息
	 * 
	 * @param leftText
	 * @param rightText
	 */
	public void updateText(String leftText, String rightText) {
		this.leftText = leftText;
		this.rightText = rightText;
		mLeftText.setText(leftText);
		mRightText.setText(rightText);
	}

	/**
	 * 设置switcher的背景色
	 */
	public void setBackgroundColor(int color) {
		mTotalLayout.setBackgroundColor(color);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mSwitchLayout != null)
			mSwitchLayout.getViewTreeObserver().addOnPreDrawListener(
					new OnPreDrawListener() {
						public boolean onPreDraw() {
							int width = mSwitchLayout.getWidth();
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLeftCorner
									.getLayoutParams();
							params.leftMargin = width / 4 - params.width / 2;
							mLeftCorner.setLayoutParams(params);
							params = (RelativeLayout.LayoutParams) mRightCorner
									.getLayoutParams();
							params.rightMargin = width / 4 - params.width / 2;
							mRightCorner.setLayoutParams(params);
							mSwitchLayout.getViewTreeObserver()
									.removeOnPreDrawListener(this);
							return true;
						}
					});
	}

	/**
	 * 选择了左边
	 */
	public void selectLeft() {
		isLeftSelected = true;
		mLeftInfo.setTextColor(getResources().getColor(R.color.white));
		mRightInfo.setTextColor(getResources().getColor(
				R.color.bella_color_black_dark));
		mLeftText.setTextColor(getResources().getColor(R.color.white));
		mRightText.setTextColor(getResources().getColor(
				R.color.bella_color_black_dark));
		mLeftIcon.setImageResource(leftIconSelectedResId);
		mRightIcon.setImageResource(rightIconResId);
		mLeftCorner.setVisibility(View.VISIBLE);
		mRightCorner.setVisibility(View.GONE);
	}

	/**
	 * 选择了右边
	 */
	public void selectRight() {
		isLeftSelected = false;
		mLeftInfo.setTextColor(getResources().getColor(
				R.color.bella_color_black_dark));
		mRightInfo.setTextColor(getResources().getColor(R.color.white));
		mLeftText.setTextColor(getResources().getColor(
				R.color.bella_color_black_dark));
		mRightText.setTextColor(getResources().getColor(R.color.white));
		mLeftIcon.setImageResource(leftIconResId);
		mRightIcon.setImageResource(rightIconSelectedResId);
		mLeftCorner.setVisibility(View.GONE);
		mRightCorner.setVisibility(View.VISIBLE);
	}
}
