package com.ef.bite.ui.balloon;

import java.util.ArrayList;

import android.os.Vibrator;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.R;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.chunk.ChunkPracticeActivity;
import com.ef.bite.ui.popup.PracticeErrorPopWindow;
import com.ef.bite.ui.popup.QuitPracticePopWindow;
import com.ef.bite.ui.preview.MutliChoicePreviewActivity;
import com.ef.bite.utils.*;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.UserLevelView;

public class BalloonActivity extends BaseActivity {
	Chunk mChunkModel;
	ThornDownView mThornDown;
	RelativeLayout mActivityLayout;
	View mFlashLayout; // 选择错误答案后边框闪烁
	ActionbarLayout mActionbar;
	RelativeLayout mTopLayout; // 上层(Actionbar content提示等等)布局
	LinearLayout mTitleDescripLayout; // 问题描述
	LinearLayout mSuccessTextLayout; // 成功的文本布局
	LinearLayout mPageIndicat; // 进度原点
	ImageView mTopLine;
	TextView mDescription;
	TextView balloon_good_job;
	LinearLayout mWorkspaceLayout; // 工作区域
	BalloonSetLayout mBalloonLayout; // 气球布局
	RelativeLayout mResultLayout; // 结果布局
	TextView mDragTextInfo; //
	TextView balloon_chunk_intro_text;
	// Button balloon_chunk_start;
	// CirclePageIndicator mPageIndicator;
	List<BalloonView> mBalloonSelectList = new ArrayList<BalloonView>(); // 选择了正确的ball
	List<BalloonTouchListener> mTouchListenList = new ArrayList<BalloonTouchListener>();
	String[] arrayCorrectWords = null;
	int CurCorrectWordIndex = 0;
	int toplayoutHeight = 0; // 气球布局的上层高度
	int ballsetLayoutHeight = 0; // 气球布局高度
	int balloonCheckHeight; // 气球检查高度，拖拽正确气球时，如果超过这个高度气球被拉下来；低于这个高度，气球还原

	// 气球选择正确后的视图
	RelativeLayout mSuccessLayout = null;
	int windowWidth = 0; // 手机的宽
	int successLayoutHeight = 0; // 庆祝布局的高度
	boolean isChunkForPreview = false;
	boolean isChunkLearning = false;
	// 是否处于QuitStatus状态；如果是，则不应该继续跳入下一页
	boolean isInQuitStatus = false;
	// 是否成功
	boolean isBaloonSuccess = false, isTimeOut = false;
	int curTryTimes = 1; // 当前重做一个题的次数， 默认是1，即代表第一次做这个题

	public static int ScoreAdded = 0;

	public Button startbButton;
	private RelativeLayout startlayout;
	private ImageView imageView;
	private final static int BallonQuestionValues = 1;
	private final static int BalloonSuccessValues = 2;
	private final static int BalloonFailureValues = 3;
	private final static int BallonStartValues = 4;
	private boolean isAnimation = false;
	private Vibrator vibrator;

	private Boolean isStart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballon);
		Bundle bundle = getIntent().getExtras();

		mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);
		// if (bundle != null && bundle.containsKey(AppConst.BundleKeys.Chunk))
		// {
		// String json = bundle.getString(AppConst.BundleKeys.Chunk);
		// mChunkModel = new Chunk();
		// mChunkModel.parse(json);
		// }
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_Chunk_For_Preview)) {
			isChunkForPreview = bundle
					.getBoolean(AppConst.BundleKeys.Is_Chunk_For_Preview);
		}
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_Chunk_Learning)) {
			isChunkLearning = bundle
					.getBoolean(AppConst.BundleKeys.Is_Chunk_Learning);
		}

		mActivityLayout = (RelativeLayout) findViewById(R.id.balloon_activity_layout);
		mFlashLayout = findViewById(R.id.balloon_flash_layout);
		mThornDown = (ThornDownView) findViewById(R.id.balloon_thorn_down);
		mTopLayout = (RelativeLayout) findViewById(R.id.balloon_top_layout);
		mActionbar = (ActionbarLayout) findViewById(R.id.balloon_actionbar);
		mActionbar.getProgressbar().setVisibility(View.VISIBLE);
		mActionbar.getProgressbar().init(5,3);
		mTitleDescripLayout = (LinearLayout) findViewById(R.id.balloon_title_description_layout);
		mSuccessTextLayout = (LinearLayout) findViewById(R.id.balloon_success_text_layout);
		mTopLine = (ImageView) findViewById(R.id.balloon_top_line);
		mWorkspaceLayout = (LinearLayout) findViewById(R.id.balloon_workspace_layout);
		// mPageIndicator = (CirclePageIndicator)
		// findViewById(R.id.balloon_pager_indicator);
		mPageIndicat = (LinearLayout) findViewById(R.id.pageindicat);
		mBalloonLayout = (BalloonSetLayout) findViewById(R.id.balloon_content_layout);
		mResultLayout = (RelativeLayout) findViewById(R.id.balloon_result_layout);
		mSuccessLayout = (RelativeLayout) findViewById(R.id.balloon_success_layout);
		mDescription = (TextView) findViewById(R.id.balloon_chunk_description);
		balloon_good_job = (TextView) findViewById(R.id.balloon_good_job);
		mDragTextInfo = (TextView) findViewById(R.id.balloon_drag_text);
		balloon_chunk_intro_text = (TextView) findViewById(R.id.balloon_chunk_intro_text);
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"balloon_drag_info") != null) {
			balloon_chunk_intro_text.setText(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext, "balloon_drag_info"));
		}

		startbButton = (Button) findViewById(R.id.balloon_chunk_start);
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"balloon_drag_start") != null) {
			startbButton.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "balloon_drag_start"));
		}

		startlayout = (RelativeLayout) findViewById(R.id.balloon_chunk_startLayout);
		imageView = (ImageView) findViewById(R.id.imageView1);
		// balloon_chunk_start = (Button)
		// findViewById(R.id.balloon_chunk_start);

		// mBalloonLayout.getViewTreeObserver().addOnPreDrawListener(
		// new OnPreDrawListener() {
		// public boolean onPreDraw() {
		// initilaize();
		// mBalloonLayout.getViewTreeObserver()
		// .removeOnPreDrawListener(this);
		// return true;
		// }
		// });

		// 初始化ActionBar
		int score = userScoreBiz.getUserScore();
		mActionbar.initiWithCenterImageRightLevel(R.drawable.map_roboto,
				R.drawable.chunk_multichoice_cancel, score,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onQuit();
						if (isStart) {
							MobclickTracking.OmnitureTrack.ActionTrackingBallonIntroduction();
						} else {
							MobclickTracking.OmnitureTrack.ActionBallonQuestion(0);
						}
//						MobclickTracking.UmengTrack.ActionBallonQuestion(1,	mContext);
					}
				});
		FontHelper.applyFont(mContext, mTopLayout, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mDragTextInfo, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, startbButton, FontHelper.FONT_OpenSans);
		// mPageIndicator.setIndicator(0, 3);
		mPageIndicat(mPageIndicat, 1, 3);

		BI_Tracking(BallonStartValues);

		final Animation fadeout = AnimationUtils.loadAnimation(mContext,
				R.anim.balloon_out_to_bottom);
		fadeout.setDuration(1000);
		fadeout.setFillAfter(true);

		final Animation fadein = AnimationUtils.loadAnimation(mContext,
				R.anim.balloon_in_from_bottom);
		fadein.setDuration(2000);
		fadein.setFillAfter(true);

		if (mChunkModel.getExplanation() != null)
			mDescription.setText(mChunkModel.getExplanation());
		else
			mDescription.setText(mChunkModel.getChunkText());

		startbButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fadeout.setAnimationListener(new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

						mBalloonLayout.setVisibility(View.VISIBLE);

						mBalloonLayout.getViewTreeObserver()
								.addOnPreDrawListener(new OnPreDrawListener() {
									public boolean onPreDraw() {
										initilaize();
										mBalloonLayout.getViewTreeObserver()
												.removeOnPreDrawListener(this);
										return true;
									}
								});

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}
				});

				fadein.setAnimationListener(new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						imageView.setVisibility(View.GONE);
						isAnimation = true;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}
				});
				startlayout.setAnimation(fadeout);
				startlayout.setVisibility(View.GONE);
				mBalloonLayout.setAnimation(fadein);
				isStart = true;
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isAnimation) {
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.BallonQuestionValues.pageNameValue,
//							ContextDataMode.BallonQuestionValues.pageSiteSubSectionValue,
//							ContextDataMode.BallonQuestionValues.pageSiteSectionValue,
//							mContext);

			BI_Tracking(BallonQuestionValues);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConst.RequestCode.CHUNK_LEARNING) {
			if (resultCode == AppConst.ResultCode.CHUNK_PRACTICE_QUIT) {
				finish();
				overridePendingTransition(R.anim.activity_in_from_left,
						R.anim.activity_out_to_right);
			}
		}
		if (requestCode == AppConst.RequestCode.CHUNK_PREVIEW) {
			if (resultCode == AppConst.ResultCode.CHUNK_PRACTICE_QUIT) {
				finish();
				overridePendingTransition(R.anim.activity_in_from_left,
						R.anim.activity_out_to_right);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.BallonQuestionValues.pageNameValue,
//				ContextDataMode.BallonQuestionValues.pageSiteSubSectionValue,
//				ContextDataMode.BallonQuestionValues.pageSiteSectionValue,
//				mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.BalloonFailureValues.pageNameValue,
//				ContextDataMode.BalloonFailureValues.pageSiteSubSectionValue,
//				ContextDataMode.BalloonFailureValues.pageSiteSectionValue,
//				mContext);
//		if (mThornDown.isRunning()) {
//			onQuit();
//		}
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.BalloonSuccessValues.pageNameValue,
//				ContextDataMode.BalloonSuccessValues.pageSiteSubSectionValue,
//				ContextDataMode.BalloonSuccessValues.pageSiteSectionValue,
//				mContext);
	}

	@Override
	public void onDestroy() {
		mThornDown.clearAnimator();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		onQuit();
	}

	private void initilaize() {
		ScoreAdded = 0;
		CurCorrectWordIndex = 0;
		mWorkspaceLayout.setVisibility(View.VISIBLE);
		mDragTextInfo.setVisibility(View.VISIBLE);
		mThornDown.setVisibility(View.VISIBLE);
		mSuccessLayout.setVisibility(View.GONE);
		mTitleDescripLayout.setVisibility(View.VISIBLE);
		mSuccessTextLayout.setVisibility(View.GONE);
		mTopLine.setVisibility(View.VISIBLE);
		if (ballsetLayoutHeight <= 0)
			ballsetLayoutHeight = mBalloonLayout.getHeight();
		if (toplayoutHeight <= 0)
			toplayoutHeight = mTopLayout.getHeight();
		if (windowWidth <= 0)
			windowWidth = mWorkspaceLayout.getWidth();
		if (successLayoutHeight <= 0)
			successLayoutHeight = mWorkspaceLayout.getHeight();
		int[] resultLayoutLocation = new int[2];
		mResultLayout.getLocationOnScreen(resultLayoutLocation);
		balloonCheckHeight = resultLayoutLocation[1];
		mBalloonLayout.clearAllBalloons();
		clearSelectBalloons();
		clearTouchAnimations();
		initBalloonView(windowWidth, ballsetLayoutHeight);
		// 初始化倒计时视图，并开启动画
		mThornDown.initAnimatorParams(mBalloonLayout, toplayoutHeight,
				ballsetLayoutHeight, new ThornDownView.OnMissedListener() {
					@Override
					public void OnMissed() {
						onMissed();
					}
				});
		mThornDown.startThronDown();
		BI_Tracking(BallonQuestionValues);
	}

	/** 初始化气球布局 **/
	private void initBalloonView(int width, int height) {
		if (mChunkModel == null || mChunkModel.getChunkText() == null) {
			return;
		}
		if (mChunkModel.getExplanation() != null)
			mDescription.setText(mChunkModel.getExplanation());
		else
			mDescription.setText(mChunkModel.getChunkText());
		BalloonTouchListener.SelectBalloonMargin = 20;
		arrayCorrectWords = mChunkModel.getChunkText().split(" ");
		if (arrayCorrectWords == null || arrayCorrectWords.length <= 0)
			return;
		int wordLength = arrayCorrectWords.length
				+ (mChunkModel.getErrorBalloonWords() == null ? 0 : mChunkModel
						.getErrorBalloonWords().size());
		String[] words = new String[wordLength];
		for (int index = 0; index < wordLength; index++) {
			if (index < arrayCorrectWords.length)
				words[index] = arrayCorrectWords[index];
			else
				words[index] = mChunkModel.getErrorBalloonWords().get(
						index - arrayCorrectWords.length);
		}

		List<BalloonView> balloonList = new ArrayList<BalloonView>();
		for (int index = 0; index < words.length; index++) {
			final BalloonView balloon1 = new BalloonView(this);
			balloon1.initialize(words[index]);
			balloonList.add(balloon1);
			BalloonTouchListener balloonTouch = new BalloonTouchListener(this,
					mBalloonLayout, mResultLayout, balloonCheckHeight);
			// 正确与否的检测事件
			balloonTouch
					.setResultChecker(new BalloonView.OnResultCheckListener() {
						@Override
						public boolean OnCheck(BalloonView selectBalloon) {
							String selectWord = selectBalloon.getLabel();
							return selectWord
									.equals(arrayCorrectWords[CurCorrectWordIndex]);
						}
					});
			// 正确气球选择后的处理事件
			balloonTouch
					.setCorrectSelect(new BalloonView.OnCorrectSelectListener() {
						@Override
						public void OnCorrect(BalloonView selectBalloon) {
							// omniture
							MobclickTracking.OmnitureTrack
									.ActionBallonQuestion(3);
							// MobclickTracking.UmengTrack.ActionBallonQuestion(3,
							// mContext);
							// 重新设置选中的ball的margin
							mDragTextInfo.setVisibility(View.GONE);
							BalloonTouchListener.SelectBalloonMargin += selectBalloon
									.getWidthRadius()
									* 2
									- BalloonSuccessView.Balloon_Overlap_Size;
							// 原
							// selectBalloon.setBalloonColor(getResources()
							// .getColor(R.color.bella_balloon_red_bg),
							// getResources().getColor(R.color.white));

							selectBalloon.setBalloonColor(
									R.drawable.balloon_orange, getResources()
											.getColor(R.color.white));

							mBalloonSelectList.add(selectBalloon);
							if (CurCorrectWordIndex == arrayCorrectWords.length - 1) {
								// 成功，但是要判断是否已经开始爆炸气球了，
								// 如果开始爆炸气球，就没有用了
								if (!mThornDown.isTimeout())
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											onSuccess();
										}
									}, 200);
							} else
								CurCorrectWordIndex++;
						}
					});
			// 错误气球选择后的处理事件
			balloonTouch
					.setWrongSelect(new BalloonView.OnWrongSelectListener() {
						@Override
						public void OnWrong(BalloonView balloon) {
							// omniture
							MobclickTracking.OmnitureTrack
									.ActionBallonQuestion(2);
							// MobclickTracking.UmengTrack.ActionBallonQuestion(2,
							// mContext);
							flash();
							vibrate();
							mThornDown.speedUpTronDown();
						}
					});
			balloon1.setOnTouchListener(balloonTouch);
			mTouchListenList.add(balloonTouch);
		}
		mBalloonLayout.initialize(width, height, balloonList);
		saveTimestamp();
	}

	/** 退出 **/
	private void onQuit() {
		isInQuitStatus = true;
		mThornDown.pauseAnimator();
		QuitPracticePopWindow dialog = new QuitPracticePopWindow(this);
		dialog.setOnQuitListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		dialog.setOnCancelListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mThornDown.resumeAnimator();
				isInQuitStatus = false;
				if (isBaloonSuccess == true)
					startMultiChoiceActivity();
			}
		});
		dialog.open();
	}

	/** 失败 **/
	private void onMissed() {
		BI_Tracking(BalloonFailureValues);
		String errorHint;
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"chunk_practice_error_popup_timeout") != null) {
			errorHint = JsonSerializeHelper.JsonLanguageDeserialize(mContext,
					"chunk_practice_error_popup_timeout");
		} else {
			errorHint = getResources().getString(
					R.string.chunk_practice_error_popup_timeout);
		}
		PracticeErrorPopWindow errorPopup = new PracticeErrorPopWindow(this,
				null, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MobclickTracking.OmnitureTrack.ActionBalloonFailure(2);
						// MobclickTracking.UmengTrack.ActionBalloonFailure(2,
						// mContext);
						// to learn
						mThornDown.clearAnimator();
						finish();
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MobclickTracking.OmnitureTrack.ActionBalloonFailure(1);
						// MobclickTracking.UmengTrack.ActionBalloonFailure(1,
						// mContext);
						// try again
						mThornDown.clearAnimator();
						curTryTimes++;
						initilaize();
					}
				});
		errorPopup.setTitle(errorHint);
		errorPopup.open();
		// Add sound effect
		new SoundEffectUtils(mContext).play(SoundEffectUtils.BALLOON_TIMES_UP);
	}

	/** 成功 **/
	private void onSuccess() {
		isBaloonSuccess = true;
		mThornDown.stopAnimator();
		mWorkspaceLayout.setVisibility(View.GONE);
		mThornDown.setVisibility(View.GONE);
		mSuccessLayout.setVisibility(View.VISIBLE);
		mTitleDescripLayout.setVisibility(View.GONE);
		mSuccessTextLayout.setVisibility(View.VISIBLE);
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"balloon_good_job") != null) {
			balloon_good_job.setText(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext, "balloon_good_job"));
		}

		mTopLine.setVisibility(View.GONE);
		int score = ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM;
//		userScoreBiz.increaseScore(score);
		balloonsFlying(score);
		cloudsAppearing();

		BI_Tracking(BalloonSuccessValues);
	}

	/** 选择错误后闪烁一下 **/
	private void flash() {
		mFlashLayout.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mFlashLayout.setVisibility(View.GONE);
			}
		}, 200);
		// Add sound effect
		new SoundEffectUtils(mContext)
				.playWithVibrator(SoundEffectUtils.BALLOON_BUZZING);
	}

	/** 清理被选中的balloon **/
	private void clearSelectBalloons() {
		try {
			if (mBalloonSelectList != null && mBalloonSelectList.size() > 0) {
				while (mBalloonSelectList.size() > 0) {
					BalloonView balloon = mBalloonSelectList.get(0);
					mResultLayout.removeView(balloon);
					mBalloonSelectList.remove(balloon);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** 清理残留的动画 **/
	private void clearTouchAnimations() {
		try {
			if (mTouchListenList != null && mTouchListenList.size() > 0) {
				while (mTouchListenList.size() > 0) {
					BalloonTouchListener touch = mTouchListenList.get(0);
					touch.clearAnimLayout();
					mTouchListenList.remove(touch);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** 云飘过来了 **/
	private void cloudsAppearing() {
		Bitmap cloudBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.balloon_cloud);
		int cloudWidth = windowWidth / 3;
		int cloudHeight = cloudBitmap.getHeight() * cloudWidth
				/ cloudBitmap.getWidth();
		;
		// 创建cloud imageview
		ImageView cloudA = new ImageView(mContext); // 左
		cloudA.setImageBitmap(cloudBitmap);
		ImageView cloudB = new ImageView(mContext); // 右
		cloudB.setImageBitmap(cloudBitmap);
		ImageView cloudC = new ImageView(mContext); // 下
		cloudC.setImageBitmap(cloudBitmap);
		RelativeLayout.LayoutParams paramsBig = new RelativeLayout.LayoutParams(
				cloudWidth, cloudHeight);
		RelativeLayout.LayoutParams paramsSmall = new RelativeLayout.LayoutParams(
				cloudWidth / 2, cloudHeight / 2);
		mSuccessLayout.addView(cloudA, paramsBig);
		mSuccessLayout.addView(cloudB, paramsBig);
		mSuccessLayout.addView(cloudC, paramsSmall);
		// 创建飘出动画
		TranslateAnimation animationA = new TranslateAnimation(-cloudWidth,
				-cloudWidth / 4, successLayoutHeight / 10,
				successLayoutHeight / 10);
		animationA.setDuration(1000);
		animationA.setFillAfter(true);
		cloudA.startAnimation(animationA);
		TranslateAnimation animationB = new TranslateAnimation(windowWidth,
				windowWidth * 3 / 5, successLayoutHeight / 3,
				successLayoutHeight / 3);
		animationB.setDuration(1000);
		animationB.setFillAfter(true);
		cloudB.startAnimation(animationB);
		TranslateAnimation animationC = new TranslateAnimation(-cloudWidth,
				windowWidth / 3, successLayoutHeight * 2 / 3,
				successLayoutHeight * 2 / 3);
		animationC.setDuration(1000);
		animationC.setFillAfter(true);
		cloudC.startAnimation(animationC);
	}

	/** 气球飘起来了 **/
	private void balloonsFlying(final int score) {
		if (mBalloonSelectList == null || mBalloonSelectList.size() <= 0)
			return;
		int successViewWidth = 0, successViewHeight = 0;
		int successViewStartX = 0, successViewStartY = 0;
		int maxBalloonHeight = 0;
		BalloonView firstSelectBalloon = mBalloonSelectList.get(0);
		successViewStartX = ((RelativeLayout.LayoutParams) firstSelectBalloon
				.getLayoutParams()).leftMargin;
		successViewStartY = ballsetLayoutHeight;
		for (int index = 0; index < mBalloonSelectList.size(); index++) {
			if (successViewWidth > 0)
				successViewWidth -= BalloonSuccessView.Balloon_Overlap_Size;
			int balloonLength = (int) (mBalloonSelectList.get(index)
					.getWidthRadius() * 2);
			successViewWidth += balloonLength;
			maxBalloonHeight = balloonLength > maxBalloonHeight ? balloonLength
					: maxBalloonHeight;
		}
		// 添加气球成功视图
		BalloonSuccessView successView = new BalloonSuccessView(mContext);
		successViewHeight = successViewWidth
				+ successView.getParatrooperHeight();
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				successViewWidth, successViewHeight);
		successView.initialize(arrayCorrectWords);
		mSuccessLayout.addView(successView, param);

		// 气球飘起的动画
		TranslateAnimation flyAnimation = new TranslateAnimation(
				successViewStartX, (windowWidth - successViewWidth) / 2,
				successViewStartY,
				(successLayoutHeight - successViewHeight) / 4);
		flyAnimation.setDuration(1000);
		flyAnimation.setFillAfter(true);
		flyAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				scoreIncreasing(score); // 增加50分
				// 到下一个多选题
				startMultiChoiceActivity();
			}
		});
		successView.startAnimation(flyAnimation);
		// Add sound effect
		new SoundEffectUtils(mContext).play(SoundEffectUtils.BALLOON_WELL_DONE);
	}

	/** 积分增加动画 **/
	private void scoreIncreasing(final int increasedScore) {
		ScoreAdded = increasedScore;
		// 创建动画层
		RelativeLayout scoreAnimationLayout = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		scoreAnimationLayout.setLayoutParams(lp);
		scoreAnimationLayout.setBackgroundResource(android.R.color.transparent);
		scoreAnimationLayout.setClickable(false);
		mActivityLayout.addView(scoreAnimationLayout);
		// 创建增分的TextView，设置样式
		final TextView scoreUpText = new TextView(mContext);
		scoreUpText.setText("+" + increasedScore + "xp");
		scoreUpText.setTextColor(getResources().getColor(
				R.color.bella_balloon_red_bg));
		scoreUpText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.balloon_label_size));
		FontHelper.applyFont(mContext, scoreUpText, FontHelper.FONT_Museo300);
		float marginLeft = windowWidth
				- getResources().getDimension(R.dimen.action_bar_goback_width)
				- scoreUpText.getPaint().measureText(
						scoreUpText.getText().toString()); // Right Margin
		int marginTop = toplayoutHeight + 50; // 修正
		final RelativeLayout.LayoutParams textlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		textlp.leftMargin = (int) marginLeft;
		textlp.topMargin = marginTop;
		scoreAnimationLayout.addView(scoreUpText, textlp);
		scoreUpText.setVisibility(View.VISIBLE);
		// 增分动画效果：先上飘至actionbar，然后消失，最后User Level的分数增加
		TranslateAnimation upAnimation = new TranslateAnimation(0, 0, 0,
				50 - marginTop);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true);
		upAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				textlp.topMargin = 50;
				scoreUpText.setLayoutParams(textlp);
				AlphaAnimation fadeoutAnimation = new AlphaAnimation(1, 0);
				fadeoutAnimation.setDuration(1000);
				fadeoutAnimation.setFillAfter(true);
				scoreUpText.startAnimation(fadeoutAnimation);
				UserLevelView userLevel = mActionbar.getUserLevelView();
				userLevel.startIncreasingScore(increasedScore, getResources()
						.getColor(R.color.bella_balloon_red_bg));
			}
		});
		scoreUpText.startAnimation(upAnimation);
	}

	/** 打开多选题activity **/
	private void startMultiChoiceActivity() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isInQuitStatus) {
					if (isChunkForPreview) {
						Intent intent = new Intent(mContext,
								MutliChoicePreviewActivity.class);
						intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
						intent.putExtra(
								AppConst.BundleKeys.Multi_Choice_Type,
								MutliChoicePreviewActivity.Multi_Choice_Type_Preview);
						startActivityForResult(intent,
								AppConst.RequestCode.CHUNK_PREVIEW);
						overridePendingTransition(
								R.anim.activity_in_from_right,
								R.anim.activity_out_to_left);
					} else if (isChunkLearning) {
						Intent intent = new Intent(mContext,
								ChunkPracticeActivity.class);
						intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
						intent.putExtra(
								AppConst.BundleKeys.Multi_Choice_Type,
								ChunkPracticeActivity.Multi_Choice_Type_Practice);
						startActivityForResult(intent,
								AppConst.RequestCode.CHUNK_LEARNING);
						overridePendingTransition(
								R.anim.activity_in_from_right,
								R.anim.activity_out_to_left);
					}
				}
			}
		}, 1000);
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.BallonQuestionValues.pageNameValue,
							ContextDataMode.BallonQuestionValues.pageSiteSubSectionValue,
							ContextDataMode.BallonQuestionValues.pageSiteSectionValue,
							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.BalloonSuccessValues.pageNameValue,
							ContextDataMode.BalloonSuccessValues.pageSiteSubSectionValue,
							ContextDataMode.BalloonSuccessValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.BalloonSuccessValues.pageNameValue,
//							ContextDataMode.BalloonSuccessValues.pageSiteSubSectionValue,
//							ContextDataMode.BalloonSuccessValues.pageSiteSectionValue,
//							mContext);
			break;
		case 3:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.BalloonFailureValues.pageNameValue,
							ContextDataMode.BalloonFailureValues.pageSiteSubSectionValue,
							ContextDataMode.BalloonFailureValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.BalloonFailureValues.pageNameValue,
//							ContextDataMode.BalloonFailureValues.pageSiteSubSectionValue,
//							ContextDataMode.BalloonFailureValues.pageSiteSectionValue,
//							mContext);
			break;

		case 4:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.BallonStartValues.pageNameValue,
					ContextDataMode.BallonStartValues.pageSiteSubSectionValue,
					ContextDataMode.BallonStartValues.pageSiteSectionValue,
					mContext);
			break;
		}
	}

	private void vibrate(){
		vibrator = (Vibrator)getSystemService(this.VIBRATOR_SERVICE);
		vibrator.vibrate(400);
	}

	private void saveTimestamp(){
		PreferencesUtils.putString(this,AppConst.CacheKeys.TIME_START, TimeFormatUtil.getUTCTimeStr());
	}
}
