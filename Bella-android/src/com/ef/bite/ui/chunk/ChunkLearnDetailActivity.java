package com.ef.bite.ui.chunk;

import java.io.File;
import com.ef.bite.R;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.ChunkBiz;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.HintDefinition;
import com.ef.bite.dataacces.mode.UserProgressStatus;
import com.ef.bite.ui.balloon.BalloonActivity;
import com.ef.bite.ui.record.ReviewActivity;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.HighLightStringHelper;
import com.ef.bite.widget.RehearseProgressView;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class ChunkLearnDetailActivity extends BaseChunkActivity {
	private ImageButton mBottomGoback;
	// Title layout
	private LinearLayout mTitleLayout;
	private TextView mChunkText;
	private LinearLayout mPronunceLayout;
	private LinearLayout mTranslateLayout;
	private TextView mPronounceText;
	private TextView mTranslatText;
	// Content layout
	private MediaPlayer mPlayer;
	private ScrollView mResizeScroll;
	private LinearLayout mStatusLayout;
	private TextView mDefinition;
	private LinearLayout mHintLayout;
	// private ChunkHintsListAdapter mHintListAdapter;
	boolean isChunkLearning = false;
	Chunk mTraslatChunk;

	int currentTitleHeight = 0;
	int maxTitleHeight = 0;
	int minTitleHeight = 0;
	float currentTextSize = 0;
	float maxTextSize = 0;
	float minTextSize = 0;
	final Handler translateHandler = new Handler(); // Handle of translation
	long translateClickTime = 0; // record the time of clicking translation link
									// "中"
	final static long Translate_Showing_Time = 3 * 1000; // The delay time of
															// showing
															// translation
	final static long Click_Time = 500; // The time to define a click envent

	private final static int PresentationLearnValues = 1;
	private final static int PresentationLookupValues = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setLayoutResourceId() {
		setContentView(R.layout.activity_chunk_learn_detail);
	}

	@Override
	protected void impNextClick() {
		if (isChunkForPreview) {
			Intent intent = new Intent(mContext, BalloonActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Is_Chunk_For_Preview,
					isChunkForPreview);
			startActivityForResult(intent, AppConst.RequestCode.CHUNK_PREVIEW);
			overridePendingTransition(R.anim.activity_in_from_right,
					R.anim.activity_out_to_left);
		} else if (isChunkLearning) {
			Intent intent = new Intent(mContext, BalloonActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning,
					isChunkLearning);
			startActivityForResult(intent, AppConst.RequestCode.CHUNK_LEARNING);
			overridePendingTransition(R.anim.activity_in_from_right,
					R.anim.activity_out_to_left);
		}

		// Intent intent = new Intent(mContext, ReviewActivity.class);
		// intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
		// startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.PresentationLearnValues.pageNameValue,
//						ContextDataMode.PresentationLearnValues.pageSiteSubSectionValue,
//						ContextDataMode.PresentationLearnValues.pageSiteSectionValue,
//						mContext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	protected void initComponents() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_Chunk_Learning)) {
			isChunkLearning = bundle
					.getBoolean(AppConst.BundleKeys.Is_Chunk_Learning);
		}
		if (isChunkForPreview || isChunkLearning) {
			enableNext(true);
		} else {
			enableNext(false);
		}

		progressbar.setVisibility(View.VISIBLE);
		progressbar.init(5, 2);

		mTitleLayout = (LinearLayout) this
				.findViewById(R.id.chunk_detail_title_layout);
		mStatusLayout = (LinearLayout) this
				.findViewById(R.id.chunk_detail_status_layout);
		mPronunceLayout = (LinearLayout) this
				.findViewById(R.id.chunk_detail_pronunce_layout);
		mTranslateLayout = (LinearLayout) this
				.findViewById(R.id.chunk_detail_translate_layout);
		mBottomGoback = (ImageButton) this
				.findViewById(R.id.chunk_bottom_go_back);
		mResizeScroll = (ScrollView) this
				.findViewById(R.id.chunk_detail_content_scrollview);
		mBottomGoback.setVisibility(View.VISIBLE);
		mGoBack.setImageResource(R.drawable.arrow_goback_black);
		mGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				if (isChunkLearning)
					overridePendingTransition(R.anim.activity_in_from_left,
							R.anim.activity_out_to_right);
			}
		});
		mBottomGoback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isChunkLearning) {
					finish();
				} else {
					Intent intent = new Intent(mContext,
							ChunkLearnActivity.class);
					intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning,
							isChunkLearning);
					intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
					startActivity(intent);
				}
				overridePendingTransition(R.anim.activity_in_from_left,
						R.anim.activity_out_to_right);
			}
		});
		// mResizeScroll.fullScroll(View.FOCUS_UP);
		mResizeScroll.pageScroll(View.FOCUS_UP);
		// 动画
		maxTitleHeight = (int) getResources().getDimension(
				R.dimen.chunk_detail_title_bigger_height);
		minTitleHeight = (int) getResources().getDimension(
				R.dimen.chunk_detail_title_smaller_height);
		maxTextSize = getResources().getDimension(
				R.dimen.chunk_detail_text_bigger_size);
		minTextSize = getResources().getDimension(
				R.dimen.chunk_detail_text_smaller_size);
		currentTitleHeight = maxTitleHeight;
		mResizeScroll.setOnTouchListener(new TouchListener());

		mChunkText = (TextView) this.findViewById(R.id.chunk_detail_chunk);
		mDefinition = (TextView) this
				.findViewById(R.id.chunk_detail_definition);
		mPronounceText = (TextView) this
				.findViewById(R.id.chunk_detail_pronunce_content);
		mTranslatText = (TextView) this
				.findViewById(R.id.chunk_detail_translation);
		mHintLayout = (LinearLayout) this
				.findViewById(R.id.chunk_detail_hint_layout);
		// mHintLayout.setOnTouchListener( new TouchListener());
		mTranslateLayout.setOnTouchListener(new TranslatTouchListener());
		// 字体设置
		FontHelper.applyFont(mContext, mChunkText, FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mDefinition, FontHelper.FONT_OpenSans);
		if (mChunkModel != null) {
			getTranslateChunk();
			showChunkDetail(mChunkModel);
		}
		displayChunkStatus(mChunkModel);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isChunkForPreview || isChunkLearning) {
			BI_Tracking(PresentationLearnValues);
		} else {
			BI_Tracking(PresentationLookupValues);
		}

	}

	/** 获得翻译chunk **/
	private void getTranslateChunk() {
		if (mChunkModel != null) {
			ChunkBLL chunkBiz = new ChunkBLL(mContext);
			mTraslatChunk = chunkBiz.getTranslatedChunk(mChunkModel
					.getChunkCode());
			if (mTraslatChunk == null)
				mTraslatChunk = mChunkModel;
		}
	}

	/** 显示Chunk的详情内容 **/
	private void showChunkDetail(final Chunk chunk) {
		if (chunk != null) {
			mHintLayout.removeAllViewsInLayout();
			mChunkText.setText(chunk.getChunkText());
			mDefinition.setText(chunk.getExplanation());
			mPronounceText.setText(chunk.getPronounce());
			List<HintDefinition> hints = chunk.getHintDefinitions();
			if (hints != null && hints.size() > 0) {
				for (int index = 0; index < hints.size(); index++) {
					HintDefinition data = hints.get(index);
					LayoutInflater inflater = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View layout = inflater.inflate(
							R.layout.chunk_hint_list_item, null);
					TextView title = (TextView) layout
							.findViewById(R.id.chunk_hint_list_item_title);
					TextView content = (TextView) layout
							.findViewById(R.id.chunk_hint_list_item_content);
					title.setText(data.getContent());
					if (data.getExample() != null
							&& !data.getExample().isEmpty()) {
						SpannableStringBuilder spanStr = HighLightStringHelper
								.getBoldString(mContext, data.getExample());
						if (spanStr != null)
							content.setText(spanStr);
						else
							content.setText(data.getExample());
					}
					FontHelper.applyFont(mContext, content,
							FontHelper.FONT_OpenSans);
					FontHelper.applyFont(mContext, title,
							FontHelper.FONT_Museo300);
					// Add hint list item into layout
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					mHintLayout.addView(layout, params);
				}
			}
			mPronunceLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MobclickTracking.OmnitureTrack.ActionPhrasepresentation(1);
					MobclickTracking.OmnitureTrack.ActionDialogue(7);


//					MobclickTracking.UmengTrack.ActionPhrasepresentation(1,	mContext);
					// if (chunk.getIsPreinstall()) {
					// prononcingFromAsset(chunk.getAudioFileName());
					// } else {
					// prononcingFromStorage(chunk.getAudioFileName());
					// }
				}
			});

			mPronounceText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					prononcingFromStorage(chunk.getAudioFileName());
				}
			});
		}
	}

	/**
	 * 从asset点播预安装的音频文件
	 * 
	 * @param audioPath
	 */
	@SuppressWarnings("unused")
	private void prononcingFromAsset(String audioPath) {
		try {
			AssetFileDescriptor descriptor = getAssets().openFd(audioPath);
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				mPlayer.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				mPlayer.prepareAsync();
			}
			mPlayer.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 从本地存储路径点播音频文件
	 * 
	 * @param audioPath
	 */
	private void prononcingFromStorage(String audioPath) {
		try {
			File audio = new File(audioPath);
			if (!audio.exists())
				return;
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				mPlayer.setDataSource(audio.getAbsolutePath());
				// mPlayer.prepareAsync();
				mPlayer.prepare();
			}
			mPlayer.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	float startY;
	boolean isAlreadySmaller = false;

	/** animation touch **/
	class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = event.getY();
				return true;
			case MotionEvent.ACTION_MOVE:
				float currentY = event.getY();
				float diffY = currentY - startY;
				if (isAtTop()) {
					currentTitleHeight = mTitleLayout.getHeight();
					currentTextSize = mChunkText.getTextSize();
					if (diffY <= 0 && !isAlreadySmaller) {
						startResizeAnimation(true);
						isAlreadySmaller = true;
					} else if (diffY > 0 && isAlreadySmaller) {
						startResizeAnimation(false);
						isAlreadySmaller = false;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				MobclickTracking.OmnitureTrack.ActionPhrasepresentation(3);
//				MobclickTracking.UmengTrack.ActionPhrasepresentation(3,	mContext);
				break;
			default:
				break;
			}
			return false;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isAtTop() {
			return mResizeScroll.getScrollY() <= 0;
		}

		/***
		 * 放大缩小动画 Animation
		 * 
		 * @param smaller
		 *            是否缩小
		 */
		private void startResizeAnimation(boolean smaller) {
			ValueAnimator heightResize = null;
			ValueAnimator textResize = null;
			if (smaller) {
				heightResize = ValueAnimator.ofInt(currentTitleHeight,
						minTitleHeight);
				textResize = ValueAnimator
						.ofFloat(currentTextSize, minTextSize);
			} else {
				heightResize = ValueAnimator.ofInt(currentTitleHeight,
						maxTitleHeight);
				textResize = ValueAnimator
						.ofFloat(currentTextSize, maxTextSize);
			}
			heightResize.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int height = (Integer) animation.getAnimatedValue();
					// mCurrentHeight = height;
					ViewGroup.LayoutParams params = mTitleLayout
							.getLayoutParams();
					params.height = height;
					mTitleLayout.setLayoutParams(params);
				}
			});
			textResize.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float size = (Float) animation.getAnimatedValue();
					if (mChunkText != null) {
						mChunkText
								.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
					}
				}
			});
			heightResize.setDuration(200);
			textResize.setDuration(200);
			heightResize.start();
			textResize.start();
		}

	}

	/** 翻译功能 **/
	class TranslatTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTranslatText.setTextColor(getResources().getColor(
						R.color.white));
				showChunkDetail(mTraslatChunk);
				translateClickTime = System.currentTimeMillis();
				return true;
			case MotionEvent.ACTION_UP:
				long currentTime = System.currentTimeMillis();
				if (currentTime - translateClickTime <= Click_Time) {
					// A click event, showing English back with delay time
					translateHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mTranslatText.setTextColor(getResources().getColor(
									R.color.bella_color_orange_light));
							showChunkDetail(mChunkModel);
						}
					}, Translate_Showing_Time);
				} else {
					// Long time touch event, showing English back at once
					mTranslatText.setTextColor(getResources().getColor(
							R.color.bella_color_orange_light));
					showChunkDetail(mChunkModel);
				}
				MobclickTracking.OmnitureTrack.ActionPhrasepresentation(2);
//				MobclickTracking.UmengTrack.ActionPhrasepresentation(2,	mContext);
				return true;
			}
			return false;
		}
	}

	/**
	 * 显示chunk的进度状态
	 */
	private void displayChunkStatus(Chunk chunk) {
		if (chunk == null) {
			mStatusLayout.setVisibility(View.GONE);
			return;
		}
		ChunkBLL chunkBiz = new ChunkBLL(mContext);
		UserProgressStatus uprogress = chunkBiz.getUserProgress(chunk
				.getChunkCode());
		if (uprogress == null
				|| uprogress.getChunkStatus() < ChunkBiz.CHUNK_STATUS_PRACTICED_NOT_REHARSE) {
			mStatusLayout.setVisibility(View.GONE);
			return;
		}
		mStatusLayout.setVisibility(View.VISIBLE);
		RelativeLayout innerStatusLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.chunk_learn_detail_status, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mStatusLayout.addView(innerStatusLayout, params);
		RehearseProgressView progress = (RehearseProgressView) innerStatusLayout
				.findViewById(R.id.chunk_learn_detail_status_progress);
		TextView descrip = (TextView) innerStatusLayout
				.findViewById(R.id.chunk_learn_detail_status_descrip);
		String descriptFormat = getString(R.string.chunk_learn_detail_status_descript);
		String descriptFormats = getString(R.string.chunk_learn_detail_status_descripts);
		FontHelper.applyFont(mContext, innerStatusLayout,
				FontHelper.FONT_Museo300);
		int daysLeft = 1;
		int days = 0;
		switch (chunk.getRehearsalStatus()) {
		case ChunkBiz.REHEARSE_R1:
			progress.setProgress(1, 4);
			if (daysLeft > 2) {
				descrip.setText(String.format(descriptFormats, daysLeft));
			} else {
				descrip.setText(String.format(descriptFormat, daysLeft));
			}

			break;
		case ChunkBiz.REHEARSE_R2:
			progress.setProgress(2, 4);
			days = (int) ((System.currentTimeMillis() - uprogress
					.getR1CostTime()) / ChunkBiz.CHUNK_UNLOCK_TIME);
			daysLeft = (int) (ChunkBiz.REHARSAL_R2_UNLOCK_TIME
					/ ChunkBiz.CHUNK_UNLOCK_TIME - days) < 0 ? daysLeft
					: (int) (ChunkBiz.REHARSAL_R2_UNLOCK_TIME
							/ ChunkBiz.CHUNK_UNLOCK_TIME - days);
			descrip.setText(String.format(descriptFormat, daysLeft));
			break;
		case ChunkBiz.REHARSE_R3:
			progress.setProgress(3, 4);
			days = (int) ((System.currentTimeMillis() - uprogress
					.getR2CostTime()) / ChunkBiz.CHUNK_UNLOCK_TIME);
			daysLeft = (int) (ChunkBiz.REHARSAL_R3_UNLOCK_TIME
					/ ChunkBiz.CHUNK_UNLOCK_TIME - days) < 0 ? daysLeft
					: (int) (ChunkBiz.REHARSAL_R3_UNLOCK_TIME
							/ ChunkBiz.CHUNK_UNLOCK_TIME - days);
			descrip.setText(String.format(descriptFormat, daysLeft));
			break;
		case ChunkBiz.REHEARSE_MARSTERED:
			progress.setProgress(4, 4);
			descrip.setText(getString(R.string.chunk_learn_detail_status_mastered));
			break;
		default:
			progress.setProgress(4, 4);
			break;
		}

	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.PresentationLearnValues.pageNameValue,
							ContextDataMode.PresentationLearnValues.pageSiteSubSectionValue,
							ContextDataMode.PresentationLearnValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.PresentationLearnValues.pageNameValue,
//							ContextDataMode.PresentationLearnValues.pageSiteSubSectionValue,
//							ContextDataMode.PresentationLearnValues.pageSiteSectionValue,
//							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.PresentationLookupValues.pageNameValue,
							ContextDataMode.PresentationLookupValues.pageSiteSubSectionValue,
							ContextDataMode.PresentationLookupValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}

}
