package com.ef.bite.ui.guide;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.WalkthroughPagerAdapter;
import com.ef.bite.business.TutorialConfigBiz;
import com.ef.bite.dataacces.ConfigSharedStorage;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.model.TutorialConfig;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.CirclePageIndicator;

/**
 * Walkthrough and Tutorial page
 * 
 * @author Allen.Zhu
 * 
 */
public class WalkthroughActivity extends BaseActivity {

	ViewPager mPager;
	CirclePageIndicator mCircleIndicator;
	WalkthroughPagerAdapter mAdapter;
	TextView mContent;
	Button mStartButton;
	ImageButton mNextButton;
	int flagout = 0;
	List<Integer> mImageList = new ArrayList<Integer>();
	List<String> mContentList = new ArrayList<String>();
	int mCurPageIndex = 0;
	int mTotalPageCount = 0;
	boolean isResetTutorialMode = false; // 是否是教程模式
	// 教程模式
	boolean isInTutorialMode = false;
	int mTutorialType = -1;
	private final static int Onboarding1 = 1;
	private final static int Onboarding2 = 2;
	private final static int VocabularyMaster = 3;
	private final static int VocabularyVocab = 4;
	private final static int Dashboard = 5;
	private final static int PhraseLearn = 6;
	private final static int VocabularyMastered = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Reset_Tutorial_Mode))
			isResetTutorialMode = bundle.getBoolean(
					AppConst.BundleKeys.Reset_Tutorial_Mode, false);
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_In_Tutorial_Model))
			isInTutorialMode = bundle.getBoolean(
					AppConst.BundleKeys.Is_In_Tutorial_Model, false);
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Tutorial_TYPE))
			mTutorialType = bundle
					.getInt(AppConst.BundleKeys.Tutorial_TYPE, -1);
		setContentView(R.layout.activity_walkthrough);
		mPager = (ViewPager) findViewById(R.id.walkthrough_viewpager);
		mCircleIndicator = (CirclePageIndicator) findViewById(R.id.walkthrough_page_indicator);
		mContent = (TextView) findViewById(R.id.walkthrough_content);
		mNextButton = (ImageButton) findViewById(R.id.walkthrough_button_next);
		mStartButton = (Button) findViewById(R.id.walkthrough_button_start);
		mStartButton.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "walkthrough_button_start"));
		mCircleIndicator.setFillColor(mContext.getResources().getColor(
				R.color.bella_color_black_dark));
		// Font
		FontHelper.applyFont(mContext, mContent, FontHelper.FONT_OpenSans);

		initPages();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.Onboarding1.pageNameValue,
//				ContextDataMode.Onboarding1.pageSiteSubSectionValue,
//				ContextDataMode.Onboarding1.pageSiteSectionValue, mContext);
//
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.Onboarding2.pageNameValue,
//				ContextDataMode.Onboarding2.pageSiteSubSectionValue,
//				ContextDataMode.Onboarding2.pageSiteSectionValue, mContext);
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.VocabularyMaster.pageNameValue,
//						ContextDataMode.VocabularyMaster.pageSiteSubSectionValue,
//						ContextDataMode.VocabularyMaster.pageSiteSectionValue,
//						mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.Dashboard.pageNameValue,
//				ContextDataMode.Dashboard.pageSiteSubSectionValue,
//				ContextDataMode.Dashboard.pageSiteSectionValue, mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.VocabularyVocabValues.pageNameValue,
//				ContextDataMode.VocabularyVocabValues.pageSiteSubSectionValue,
//				ContextDataMode.VocabularyVocabValues.pageSiteSectionValue,
//				mContext);
	}

	private void initPages() {
		getPageList();
		mAdapter = new WalkthroughPagerAdapter(mContext, mImageList);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				mCurPageIndex = position;
				onPagerLoad();
			}

		});
		onPagerLoad();
	}

	@Override
	public void onBackPressed() {
		if (isInTutorialMode) {
			return;
		} else {
			if (isResetTutorialMode) {
				finish();
				overridePendingTransition(R.anim.activity_in_from_left,
						R.anim.activity_out_to_right);
			} else {
				if (flagout == 0) {
					Toast.makeText(getBaseContext(),
							"app will exit press once again!",
							Toast.LENGTH_SHORT).show();
					flagout = 1;
				} else {
					setResult(AppConst.ResultCode.APP_EXIT);
					finish();
				}
			}
		}
	}

	/**
	 * 获得页面列表
	 */
	private void getPageList() {
		mImageList.clear();
		mCurPageIndex = 0;
		if (isInTutorialMode
				&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_LERN_CHUNK) {
			mImageList.add(R.drawable.tutorial_1);
			mImageList.add(R.drawable.tutorial_2);
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "tutorial_1"));
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "tutorial_2"));
			mTotalPageCount = 2;

			BI_Tracking(Onboarding1);
			// Umeng
//			MobclickTracking.UmengTrack.setPageStart(
//					ContextDataMode.Onboarding1.pageNameValue,
//					ContextDataMode.Onboarding1.pageSiteSubSectionValue,
//					ContextDataMode.Onboarding1.pageSiteSectionValue, mContext);
		} else if (isInTutorialMode
				&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_HOME_SCREEN) {
			if (AppConst.GlobalConfig.IsChunkPerDayLearned) {
				mImageList.add(R.drawable.tutorial_3);
				mImageList.add(R.drawable.tutorial_4);
				mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "tutorial_3"));
				mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "tutorial_4"));
				mTotalPageCount = 2;
				BI_Tracking(PhraseLearn);
			} else {
				mImageList.add(R.drawable.tutorial_4);
				mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "tutorial_4"));
				mTotalPageCount = 1;
				BI_Tracking(Dashboard);
			}
		} else if (isInTutorialMode
				&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_REHEARSE_CHUNK) {
			mImageList.add(R.drawable.tutorial_5);
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "tutorial_5"));
			mTotalPageCount = 1;
			BI_Tracking(VocabularyVocab);

		} else if (isInTutorialMode
				&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_MASTER_CHUNK) {
			mImageList.add(R.drawable.tutorial_6);
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "tutorial_6"));
			mTotalPageCount = 1;
			BI_Tracking(VocabularyMastered);
		} else {
			mImageList.add(R.drawable.walkthroughs_1);
			mImageList.add(R.drawable.walkthroughs_2);
			mImageList.add(R.drawable.walkthroughs_3);
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "walkthrough_content_first"));
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "walkthrough_content_second"));
			mContentList.add(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "walkthrough_content_third"));
			mTotalPageCount = 3;
		}
	}

	private void onPagerLoad() {
		String content = mContentList.get(mCurPageIndex);
		mContent.setText(content);
		mCircleIndicator.setIndicator(mCurPageIndex, mTotalPageCount);

		if (mCurPageIndex >= mTotalPageCount - 1) {
			if (isInTutorialMode
					&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_LERN_CHUNK) {
				if (mCurPageIndex == 1) {
					BI_Tracking(Onboarding2);

				}
			} else if (isInTutorialMode
					&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_HOME_SCREEN) {
				if (AppConst.GlobalConfig.IsChunkPerDayLearned) {
					if (mCurPageIndex == 1) {
						BI_Tracking(Dashboard);
					}

				}
			} else if (isInTutorialMode
					&& mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_MASTER_CHUNK) {
			} else {
				if (mCurPageIndex == 1) {
					// TrackState Vocabulary list:master
					BI_Tracking(VocabularyMaster);
				}
			}

			mNextButton.setVisibility(View.GONE);
			mStartButton.setVisibility(View.VISIBLE);
			mStartButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onStartClick();
				}
			});
		} else {
			mNextButton.setVisibility(View.VISIBLE);
			mStartButton.setVisibility(View.GONE);
			mNextButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}
			});
		}
	}

	private void onStartClick() {
		if (isInTutorialMode) {
			TutorialConfig tutorialConfig = new TutorialConfigSharedStorage(
					mContext, AppConst.CurrUserInfo.UserId).get();
			if (tutorialConfig == null) {
				tutorialConfig = new TutorialConfig();
			}
			if (mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_LERN_CHUNK) {
				tutorialConfig.Tutorial_Learn_Chunk = true;
			} else if (mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_HOME_SCREEN) {
				tutorialConfig.Tutorial_Home_Screen = true;
			} else if (mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_REHEARSE_CHUNK) {
				tutorialConfig.Tutorial_Rehearse_Chunk = true;
			} else if (mTutorialType == TutorialConfigBiz.TUTORIAL_TYPE_MASTER_CHUNK) {
				tutorialConfig.Tutorial_Master_Chunk = true;
				AppConst.GlobalConfig.TutorialConfig = true;
			}
			new TutorialConfigSharedStorage(mContext,
					AppConst.CurrUserInfo.UserId).put(tutorialConfig);
			finish();
		} else {
			ConfigModel config = new ConfigSharedStorage(mContext).get();
			if (config != null)
				config.IsWelPageLoaded = true;
			else {
				config = new ConfigModel();
				config.IsWelPageLoaded = true;
			}
			new ConfigSharedStorage(mContext).put(config);
			finish();
		}
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			// omniture
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.Onboarding1.pageNameValue,
					ContextDataMode.Onboarding1.pageSiteSubSectionValue,
					ContextDataMode.Onboarding1.pageSiteSectionValue, mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.Onboarding2.pageNameValue,
					ContextDataMode.Onboarding2.pageSiteSubSectionValue,
					ContextDataMode.Onboarding2.pageSiteSectionValue, mContext);
//			MobclickTracking.UmengTrack.setPageStart(
//					ContextDataMode.Onboarding2.pageNameValue,
//					ContextDataMode.Onboarding2.pageSiteSubSectionValue,
//					ContextDataMode.Onboarding2.pageSiteSectionValue, mContext);
			break;
		case 3:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.VocabularyMaster.pageNameValue,
					ContextDataMode.VocabularyMaster.pageSiteSubSectionValue,
					ContextDataMode.VocabularyMaster.pageSiteSectionValue,
					mContext);
//			MobclickTracking.UmengTrack.setPageStart(
//					ContextDataMode.VocabularyMaster.pageNameValue,
//					ContextDataMode.VocabularyMaster.pageSiteSubSectionValue,
//					ContextDataMode.VocabularyMaster.pageSiteSectionValue,
//					mContext);
			break;

		case 4:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.VocabularyVocabValues.pageNameValue,
							ContextDataMode.VocabularyVocabValues.pageSiteSubSectionValue,
							ContextDataMode.VocabularyVocabValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.VocabularyVocabValues.pageNameValue,
//							ContextDataMode.VocabularyVocabValues.pageSiteSubSectionValue,
//							ContextDataMode.VocabularyVocabValues.pageSiteSectionValue,
//							mContext);
			break;
		case 5:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.Dashboard.pageNameValue,
					ContextDataMode.Dashboard.pageSiteSubSectionValue,
					ContextDataMode.Dashboard.pageSiteSectionValue, mContext);
//			MobclickTracking.UmengTrack.setPageStart(
//					ContextDataMode.Dashboard.pageNameValue,
//					ContextDataMode.Dashboard.pageSiteSubSectionValue,
//					ContextDataMode.Dashboard.pageSiteSectionValue, mContext);
			break;
		case 6:
			MobclickTracking.OmnitureTrack.AnalyticsTrackState(
					ContextDataMode.PhraseLearn.pageNameValue,
					ContextDataMode.PhraseLearn.pageSiteSubSectionValue,
					ContextDataMode.PhraseLearn.pageSiteSectionValue, mContext);
			break;
		case 7:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.VocabularyMasteredValues.pageNameValue,
							ContextDataMode.VocabularyMasteredValues.pageSiteSubSectionValue,
							ContextDataMode.VocabularyMasteredValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}
}
