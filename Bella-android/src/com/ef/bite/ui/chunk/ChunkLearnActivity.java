package com.ef.bite.ui.chunk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ChunkLearnListAdapter;
import com.ef.bite.animation.ResizeAnimation;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.PresentationConversation;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.AudioPlayerView;
import com.ef.bite.widget.AudioPlayerView.OnCompletionListener;
import com.ef.bite.widget.AudioPlayerView.OnStopClickListener;
import com.ef.bite.widget.ExpandButtonView;

public class ChunkLearnActivity extends BaseChunkActivity {

	private AudioPlayerView mAudioPlayer;
	private AudioPlayerView mWeChatPlayer;
	int maxDiameter = 0; // 音频播放器最大的直径
	int minDiameter = 0; // 音频播放器最小的直径
	private RelativeLayout mTotalLayout;
	private ImageView mTeacher;
	private ImageView mCollapseButton;
	private ExpandButtonView mExpandButton;
	TextView mInfoText;
	ChunkLearnListAdapter mDialogueAdapter;
	private RelativeLayout mSlideTopLayout;
	private RelativeLayout mSlideMidLayout;
	ListView mDialogueListView;
	private LinearLayout mSlideBottomLayout;
	ChunkBLL mChunkBll;

	private int mExpanded = 0; // 0 - 未展开； 1 - 正在展开； 2 - 已展开
	AudioPlayerView anim_player;
	private ViewGroup anim_mask_layout;// 动画层
	int[] start_location = new int[2];
	int[] end_location = new int[2];
	int start_width = 0;
	int start_height = 0;
	int end_width = 0;
	int end_height = 0;
	final static int ANIMATION_DURATION = 500;
	int topSlideLayoutHeight = 0; // 上层高度
	int bottomSlideLayoutHeight = 0; // 下层的高度
	int expandButtonHeight = 0; // expandbutton的高度
	private int mposition = 0;
	boolean isChunkLearning = true;
	private final static int LearnAudioDialogueValues = 1;
	private final static int LearnAudioDialogueLookUpValues = 2;

	private TutorialConfigSharedStorage configSharedStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);
		// Bundle bundle = getIntent().getExtras();
		// if (bundle != null && bundle.containsKey(AppConst.BundleKeys.Chunk))
		// {
		// String json = bundle.getString(AppConst.BundleKeys.Chunk);
		// mChunkModel = new Chunk();
		// mChunkModel.parse(json);
		// }

		mChunkBll = new ChunkBLL(mContext);
		AppConst.GlobalConfig.IsChunkPerDayLearnLoaded = true;

		configSharedStorage = new TutorialConfigSharedStorage(mContext,
				AppConst.CurrUserInfo.UserId);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (configSharedStorage.get() != null) {
			if (configSharedStorage.get().Tutorial_Learn_Chunk) {
				if (isChunkForPreview || isChunkLearning) {
					BI_Tracking(LearnAudioDialogueValues);
				} else {
					BI_Tracking(LearnAudioDialogueLookUpValues);
				}
			}
		}
	}

	@Override
	protected void setLayoutResourceId() {
		setContentView(R.layout.activity_chunk_learn_wechat);
	}

	@Override
	protected void initComponents() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_Chunk_Learning)) {
			isChunkLearning = bundle
					.getBoolean(AppConst.BundleKeys.Is_Chunk_Learning);
		}

		// 方便测试，后期改过来
		// if (isChunkLearning) {
		// enableNext(false);
		// }

		progressbar.setVisibility(View.VISIBLE);
		progressbar.init(5, 1);

		mTotalLayout = (RelativeLayout) findViewById(R.id.chunk_presentation_layout);
		mTeacher = (ImageView) findViewById(R.id.chunk_actionbar_teacher);
		mInfoText = (TextView) findViewById(R.id.chunk_lesson_info);
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"chunk_learn_lets_learn_new_chunk") != null) {
			mInfoText.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "chunk_learn_lets_learn_new_chunk"));
		}

		mSlideTopLayout = (RelativeLayout) findViewById(R.id.chunk_lesson_side_top_layout);
		mSlideMidLayout = (RelativeLayout) findViewById(R.id.chunk_lesson_slide_mid_layout);
		mSlideBottomLayout = (LinearLayout) findViewById(R.id.chunk_lesson_side_bottom_layout);
		mExpandButton = (ExpandButtonView) findViewById(R.id.chunk_lesson_expand);
		mCollapseButton = (ImageView) findViewById(R.id.chunk_lesson_collapse);
		mAudioPlayer = (AudioPlayerView) findViewById(R.id.chunk_lesson_audioplayer);
		mWeChatPlayer = new AudioPlayerView(mContext);
		mDialogueListView = (ListView) findViewById(R.id.chunk_lesson_dialogue_listview);
		mBottomPlayer.setMiniStatus(true);
		if (mChunkModel != null && mChunkModel.getChunkPresentation() != null)
			mDialogueAdapter = new ChunkLearnListAdapter(this, mChunkModel
					.getChunkPresentation().getPresentationConversations(),
					mChunkModel);
		mDialogueListView.setAdapter(mDialogueAdapter);

		// 这个版本暂时不支持对话来回切换
		mDialogueListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {
				// made in Alan
				mposition = position;
				PresentationConversation conversation = mChunkModel
						.getChunkPresentation().getPresentationConversations()
						.get(position);
				Integer startTime = conversation.getStartTime();
				Integer endTime = conversation.getEndTime();
				mDialogueAdapter.initGif(position);
				if (startTime != null && endTime != null) {
					// mBottomPlayer.start(startTime);
					// mBottomPlayer.stop(endTime);
					mWeChatPlayer.start(startTime);
					mWeChatPlayer.stop(endTime, new OnStopClickListener() {

						@Override
						public void OnStop() {
							// TODO Auto-generated method stub
							// mDialogueAdapter.StopGif(position, false);
							mDialogueAdapter.closeTranslationGif(true);
						}
					});

					mAudioPlayer.pause();
					mBottomPlayer.pause();
				}

//				mDialogueAdapter.closeGif();
				// mDialogueAdapter.setTranslationMorn(true, position);
			}
		});

		mGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickTracking.OmnitureTrack.ActionDialogue(1);
//				MobclickTracking.UmengTrack.ActionDialogue(1, mContext);
				finish();
//				if (isChunkForPreview) {
//					finish();
//				} else
//					new HomeScreenOpenAction().open(mContext, null);
			}
		});

//		mSlideMidLayout.getViewTreeObserver().addOnGlobalLayoutListener(
//				new OnGlobalLayoutListener() {
//					@SuppressWarnings("deprecation")
//					@Override
//					public void onGlobalLayout() {
//						topSlideLayoutHeight = mSlideTopLayout.getHeight();
//						bottomSlideLayoutHeight = mSlideBottomLayout
//								.getHeight();
//						mSlideMidLayout.getViewTreeObserver()
//								.removeOnGlobalLayoutListener(this);
//						// 设置Expand Button的位置
//						expandButtonHeight = mExpandButton.getHeight();
//						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mExpandButton
//								.getLayoutParams();
//						params.topMargin = topSlideLayoutHeight
//								+ expandButtonHeight / 2;
//						mExpandButton.setLayoutParams(params);
//						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//							mSlideMidLayout.getViewTreeObserver()
//									.removeGlobalOnLayoutListener(this);
//						} else {
//							mSlideMidLayout.getViewTreeObserver()
//									.removeOnGlobalLayoutListener(this);
//						}
//					}
//				});

		if (mChunkModel != null && mChunkModel.getChunkPresentation() != null) {
			if (mChunkModel.getIsPreinstall()) {
				mAudioPlayer.prepareInAsset(mChunkModel.getChunkPresentation()
						.getAudioFile());
				mBottomPlayer.prepareInAsset(mChunkModel.getChunkPresentation()
						.getAudioFile());
				mWeChatPlayer.prepareInAsset(mChunkModel.getChunkPresentation()
						.getAudioFile());
			} else {
				mAudioPlayer.prepareInStorage(mChunkModel
						.getChunkPresentation().getAudioFile());
				mBottomPlayer.prepareInStorage(mChunkModel
						.getChunkPresentation().getAudioFile());
				mWeChatPlayer.prepareInStorage(mChunkModel
						.getChunkPresentation().getAudioFile());
			}
		}

		mExpandButton.setDuration(ANIMATION_DURATION);
		mBottomPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void OnCompletion() {
				enableNext(true);
				mDialogueAdapter.setHighLight(true);
			}
		});

		mBottomPlayer
				.setOnCloseAudioListener(new AudioPlayerView.OnCloseAudioListener() {

					@Override
					public void OnClose() {
						// TODO Auto-generated method stub
						mWeChatPlayer.pause();
						mDialogueAdapter.closeTranslationGif(true);
					}
				});

		mAudioPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mExpanded == 0) {
					expand();
					mExpanded = 1;
					if (mBottomPlayer.getStatus() != AudioPlayerView.Status_Playing) {
						mBottomPlayer.start();
					}
					MobclickTracking.OmnitureTrack.ActionDialogue(3);
                    MobclickTracking.OmnitureTrack.ActionDialogue(6);

//                    MobclickTracking.UmengTrack.ActionDialogue(3, mContext);
				}
			}
		});

		mExpandButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// made in Alan
				// if (mExpanded == 0) {
				// expand();
				// mExpanded = 1;
				// if (mBottomPlayer.getStatus() !=
				// AudioPlayerView.Status_Playing) {
				// mBottomPlayer.start();
				// }
				// }
			}
		});

		mCollapseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAudioPlayer.pause();
				mBottomPlayer.pause();
				if (mExpanded == 2) {
					collapse();
					mExpanded = 1;
				}
			}
		});
		// 字体设置
		FontHelper.applyFont(mContext, mInfoText, FontHelper.FONT_Museo300);
	}

	@Override
	protected void impNextClick() {
		if (isChunkForPreview) {
			Intent intent = new Intent(mContext, ChunkLearnDetailActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Is_Chunk_For_Preview, true);
			startActivityForResult(intent, AppConst.RequestCode.CHUNK_PREVIEW);
			overridePendingTransition(R.anim.activity_in_from_right,
					R.anim.activity_out_to_left);
		} else if (isChunkLearning) {
			Intent intent = new Intent(mContext, ChunkLearnDetailActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning, true);
			startActivityForResult(intent, AppConst.RequestCode.CHUNK_LEARNING);
			overridePendingTransition(R.anim.activity_in_from_right,
					R.anim.activity_out_to_left);
		} else {
			finish();
			overridePendingTransition(R.anim.activity_in_from_right,
					R.anim.activity_out_to_left);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mBottomPlayer.getStatus() == 1)
			mBottomPlayer.pause();
		if (mWeChatPlayer != null) {
			mWeChatPlayer.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBottomPlayer!=null || mBottomPlayer.getStatus() == 1 || mBottomPlayer.getStatus() == 2)
			mBottomPlayer.stop();
		if(mWeChatPlayer!=null){
			mWeChatPlayer.release();
		}
		if(mAudioPlayer!=null){
			mAudioPlayer.stop();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	// 展开
	private void expand() {
		Animation tanimUp = new TranslateAnimation(0, 0, 0,
				-topSlideLayoutHeight);
		tanimUp.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mInfoText.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mSlideBottomLayout.setClickable(false);
				mTotalLayout.setBackgroundColor(getResources().getColor(
						R.color.white));
				mTeacher.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		Animation tanimDown = new TranslateAnimation(0, 0, 0,
				bottomSlideLayoutHeight);
		tanimUp.setDuration(ANIMATION_DURATION);
		tanimDown.setDuration(ANIMATION_DURATION);
		mSlideTopLayout.startAnimation(tanimUp);
		mSlideBottomLayout.startAnimation(tanimDown);
		mExpandButton.startAnimation(tanimUp);
		tanimUp.setFillAfter(true);
		tanimDown.setFillAfter(true);
		mExpandButton.startExpand();
		movePlayer(mAudioPlayer, mBottomPlayer, true);
	}

	// 收拢
	private void collapse() {
		Animation tanimUp = new TranslateAnimation(0, 0, -topSlideLayoutHeight,
				0);
		Animation tanimDown = new TranslateAnimation(0, 0,
				bottomSlideLayoutHeight, 0);
		tanimUp.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mInfoText.setVisibility(View.VISIBLE);
				mTeacher.setVisibility(View.VISIBLE);
				mTotalLayout.setBackgroundColor(getResources().getColor(
						R.color.bella_chunk_background));
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mSlideMidLayout.setClickable(false);
				mSlideBottomLayout.setClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		tanimUp.setDuration(ANIMATION_DURATION);
		tanimDown.setDuration(ANIMATION_DURATION);
		mSlideBottomLayout.startAnimation(tanimDown);
		mSlideTopLayout.startAnimation(tanimUp);
		mExpandButton.startAnimation(tanimUp);
		tanimUp.setFillAfter(true);
		tanimDown.setFillAfter(true);
		mExpandButton.startCollipse();
		movePlayer(mBottomPlayer, mAudioPlayer, false);
	}

	/**
	 * Move AudioPlayer
	 * 
	 * @param bigger
	 * @param smaller
	 */
	private void movePlayer(AudioPlayerView bigger, AudioPlayerView smaller,
			final boolean isExpanded) {
		if (isExpanded) {
			bigger.getLocationOnScreen(start_location);
			smaller.getLocationOnScreen(end_location);
		} else {
			int[] tmp = new int[2];
			tmp = start_location;
			start_location = end_location;
			end_location = tmp;
		}

		if (isExpanded) {
			start_width = bigger.getWidth();
			end_width = smaller.getWidth();
			start_height = bigger.getHeight();
			end_height = smaller.getHeight();
		} else {
			int tmp = start_width;
			start_width = end_width;
			end_width = tmp;
			tmp = start_height;
			start_height = end_height;
			end_height = tmp;
		}
		anim_player = new AudioPlayerView(this);
		if (anim_mask_layout == null)
			anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(anim_player);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, anim_player,
				start_width, start_height, start_location);
		// 计算位移
		int endX = end_location[0] - start_location[0];// 动画位移的X坐标
		int endY = end_location[1] - start_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);
		final ResizeAnimation animation = new ResizeAnimation(anim_player,
				(float) start_width, (float) start_height, (float) end_width,
				(float) end_height);
		animation.setDuration(ANIMATION_DURATION);
		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(true);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.addAnimation(animation);
		set.setDuration(ANIMATION_DURATION);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				anim_player.setVisibility(View.VISIBLE);
				if (isExpanded)
					mAudioPlayer.setVisibility(View.GONE);
				else
					mBottomPlayer.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				anim_mask_layout.removeView(anim_player);
				if (isExpanded) {
					mAudioPlayer.setVisibility(View.GONE);
					mBottomPlayer.setVisibility(View.VISIBLE);
					mExpanded = 2;
				} else {
					mBottomPlayer.setVisibility(View.GONE);
					mAudioPlayer.setVisibility(View.VISIBLE);
					mExpanded = 0;
				}
			}
		});
	}

	// 创建动画层
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		animLayout.setClickable(false);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg, final View view,
			int width, int height, int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.LearnAudioDialogueValues.pageNameValue,
							ContextDataMode.LearnAudioDialogueValues.pageSiteSubSectionValue,
							ContextDataMode.LearnAudioDialogueValues.pageSiteSectionValue,
							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.LearnAudioDialogueLookUpValues.pageNameValue,
							ContextDataMode.LearnAudioDialogueLookUpValues.pageSiteSubSectionValue,
							ContextDataMode.LearnAudioDialogueLookUpValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}

}
