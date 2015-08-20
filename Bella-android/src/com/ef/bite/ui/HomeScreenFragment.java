package com.ef.bite.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.apptentive.android.sdk.Apptentive;
import com.ef.bite.R;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.apptentive.WooAppstoreRatingProvider;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.LocalDashboardBLL;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.action.NewChunkOpenAction;
import com.ef.bite.business.action.RehearseChunkOpenAction;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.GetDashboardFriendListTask;
import com.ef.bite.business.task.GetDashboardTask;
import com.ef.bite.business.task.GetUnreadNofiticationCountTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.SurveysRatingShareStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.PushData;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;
import com.ef.bite.dataacces.mode.httpMode.HttpUnreadNotificationCount;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.model.RatingModel;
import com.ef.bite.ui.chunk.ChunkListActivity;
import com.ef.bite.ui.main.MainActivity;
import com.ef.bite.ui.user.FriendNotificationActivity;
import com.ef.bite.ui.user.LeaderBoardActivity;
import com.ef.bite.ui.user.SettingsActivity;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.TimeFormatUtil;

public class HomeScreenFragment extends Fragment {
	FragmentActivity mActivity;
	Context mContext;
	LayoutInflater mInflater;
	ImageButton mSettingBtn;
	TextView home_screen_leaderboard_title;
	TextView mMasteredCount;
	// Phrase layout
	LinearLayout mPhraseLayout; // Phrase Layout: Learn,Practice,Mastered
	LinearLayout mLearnHighlightLayout; // Learn highlight layout
	LinearLayout mPracticeHighlightLayout; // Practice(Rehearsal) highlight
	// layout
	LinearLayout mAllDoneLayout; // 所有单词都学完
	RelativeLayout mLearnPhraseLayout;
	RelativeLayout mPracticePhraseLayout;
	RelativeLayout mMasteredPhraseLayout;
	ImageButton mLearnGoBtn;
	ImageButton mPracticeGoBtn;
	// 朋友
	List<HttpGetFriendData> mFriendList;
	UserScoreBiz mScoreBiz;
	LinearLayout mFriendLayout; // 底部朋友layout
	FriendsContainer mFriendContainer;
	ImageButton mFirendMore;
	// 通知
	RelativeLayout mNotificationLayout; // 通知的layout
	RelativeLayout inboxView; // inbox的layout
	TextView mNotificationCount; // 通知未读数量

	ChunkBLL mChunkBiz;
	List<Chunk> mNewChunks;
	List<Chunk> mRehearseChunkList;
	int masteredChunkNum;
	long learnAvailableLeftTime = 0;
	long practiceAvailableLeftTime = 0;

	HttpGetFriendData currentUserSelected; // 当前选中的人
	// DashBoard本地业务
	LocalDashboardBLL dashboardBLL = null;

	public final static int LAYOUT_TYPE_ALLDONE = 0;
	public final static int LAYOUT_TYPE_LEARN = 1;
	public final static int LAYOUT_TYPE_PRACTICE = 2;
	int layoutType = -1;

	private SurveysRatingShareStorage sRatingShareStorage;
	private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static HomeScreenFragment newInstance() {
		HomeScreenFragment fragment = new HomeScreenFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = this.getActivity();
		mContext = mActivity.getApplicationContext();
		View layout = inflater.inflate(R.layout.activity_home_screen, null);
		mInflater = inflater;
		mSettingBtn = (ImageButton) layout
				.findViewById(R.id.home_screen_setting);
		mFriendLayout = (LinearLayout) layout
				.findViewById(R.id.home_screen_friend_layout);
		mFirendMore = (ImageButton) layout
				.findViewById(R.id.home_screen_friend_more);
		mFriendContainer = new FriendsContainer();
		inboxView = (RelativeLayout) layout
				.findViewById(R.id.inbox_view);
		mNotificationLayout = (RelativeLayout) layout
				.findViewById(R.id.home_screen_notification_layout);
		mNotificationCount = (TextView) layout
				.findViewById(R.id.home_screen_notification_count);
		mPhraseLayout = (LinearLayout) layout
				.findViewById(R.id.home_screen_chunk_layout);
		home_screen_leaderboard_title = (TextView) layout
				.findViewById(R.id.home_screen_leaderboard_title);
		home_screen_leaderboard_title.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext,
						"home_screen_leaderboard_title"));
		FontHelper.applyFont(mContext, layout, FontHelper.FONT_Museo300);
		// 好友通知
		inboxView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						FriendNotificationActivity.class));
			}
		});

		mFirendMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, LeaderBoardActivity.class));
				mActivity.overridePendingTransition(
						R.anim.activity_in_from_right,
						R.anim.activity_out_to_left);

				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_MAIN,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_LEADERBOARD);

			}
		});
		mSettingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SettingsActivity.class));
			}
		});

		// made in Alan Apptentive
//		try {
//			if (AppConst.CurrUserInfo.RegisterDate != null) {
//				if (TimeFormatUtil.utc2Local(
//						AppConst.CurrUserInfo.RegisterDate, 10) == 1) {
//					Apptentive.engage(mActivity, "difficultySurvey_zh-Hans");
//				}
//				if (TimeFormatUtil.utc2Local(
//						AppConst.CurrUserInfo.RegisterDate, 5) == 1
//						&& mChunkBiz.getMarsterdChunkCount() > 3) {
//					Apptentive.engage(mActivity, "usefulnessSurvey_zh-Hans");
//
//				}
//			}
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		}

		loadingData();
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		executePushAction();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickTracking.UmengTrack.setResume(mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.DashboardValues.pageNameValue,
//				ContextDataMode.DashboardValues.pageSiteSubSectionValue,
//				ContextDataMode.DashboardValues.pageSiteSectionValue, mContext);
		Apptentive
				.setRatingProvider(new WooAppstoreRatingProvider());
		Apptentive.engage(getActivity(), "showRating_android");
		try {
			// Apptentive
			sRatingShareStorage = new SurveysRatingShareStorage(mContext);
			RatingModel ratingModel = sRatingShareStorage.get();
			if (ratingModel != null) {
				int temp_date = TimeFormatUtil.compare_date(ratingModel.date,
						sFormat.format(new Date()), 3);
				if (temp_date == 0) {
//					Apptentive
//							.setRatingProvider(new WooAppstoreRatingProvider());
//					Apptentive.engage(getActivity(), "showRating_android");
				} else {
					putRatingShareDate();
				}
			} else {
				putRatingShareDate();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPause(mContext);
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.DashboardValues.pageNameValue,
//				ContextDataMode.DashboardValues.pageSiteSubSectionValue,
//				ContextDataMode.DashboardValues.pageSiteSectionValue, mContext);
	}

	/**
	 * 加载数据 *
	 */
	public void loadingData() {
		if (mChunkBiz == null)
			mChunkBiz = new ChunkBLL(mContext);
		if (mScoreBiz == null)
			mScoreBiz = new UserScoreBiz(mContext);
		if (dashboardBLL == null)
			dashboardBLL = new LocalDashboardBLL(mContext);
		mNewChunks = mChunkBiz.getNewChunk();
		mRehearseChunkList = mChunkBiz.getRehearseChunkList();
		masteredChunkNum = mChunkBiz.getMarsterdChunkCount();
		learnAvailableLeftTime = mChunkBiz.getLearnAvailableTime();
		practiceAvailableLeftTime = mChunkBiz.getRehearsalAvailableTime();
		loadPhraseLayout();
		loadFriendBashboard();
		getUnreadNofiticationCount();
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.DashboardValues.pageNameValue,
				ContextDataMode.DashboardValues.pageSiteSubSectionValue,
				ContextDataMode.DashboardValues.pageSiteSectionValue, mContext);
	}

	/**
	 * Load Phrase Layout by chunk status *
	 */
	private void loadPhraseLayout() {
		TextView goodJob = null;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// chunk for learn is not 0 or chunk for practice is 0;
		// inflate the layout: fragment_home_screen_learn_highlight
		if ((mNewChunks == null || mNewChunks.size() == 0)
				&& (mRehearseChunkList == null || mRehearseChunkList.size() <= 0)) {
			if (mAllDoneLayout == null)
				mAllDoneLayout = (LinearLayout) mInflater.inflate(
						R.layout.fragment_home_sreen_all_done, null);
			switch (layoutType) {
			case LAYOUT_TYPE_ALLDONE:
				break;
			case LAYOUT_TYPE_LEARN:
				mPhraseLayout.removeView(mLearnHighlightLayout);
				mPhraseLayout.addView(mAllDoneLayout, params);
				break;
			case LAYOUT_TYPE_PRACTICE:
				mPhraseLayout.removeView(mPracticeHighlightLayout);
				mPhraseLayout.addView(mAllDoneLayout, params);
				break;
			default:
				mPhraseLayout.addView(mAllDoneLayout, params);
				break;
			}
			layoutType = LAYOUT_TYPE_ALLDONE;
			goodJob = (TextView) mAllDoneLayout
					.findViewById(R.id.home_screen_done_good);
			goodJob.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "home_screen_all_done_good_job"));
			TextView learnInfo = (TextView) mAllDoneLayout
					.findViewById(R.id.home_screen_learn_available_info);
			TextView practiceInfo = (TextView) mAllDoneLayout
					.findViewById(R.id.home_screen_practice_available_info);
			TextView goodJobInfo = (TextView) mAllDoneLayout
					.findViewById(R.id.home_screen_done_info);

			TextView practiceTitle = (TextView) mAllDoneLayout
					.findViewById(R.id.home_screen_practice_title);
			practiceTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "home_screen_practice_title"));
			goodJobInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "home_screen_all_done_stay_tuned"));
			if (mChunkBiz.getAllChunkCount() <= masteredChunkNum) // 全部掌握
			{
				learnInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "home_screen_all_done_avaiable_soon"));
				practiceInfo.setText(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"home_screen_all_done_avaiable_soon"));
				goodJobInfo.setText(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"home_screen_all_done_stay_tuned"));
			} else {
				learnInfo.setText(getAvailableLeftTimeText(
						learnAvailableLeftTime, true));
				practiceInfo.setText(getAvailableLeftTimeText(
						practiceAvailableLeftTime, false));
				goodJobInfo.setText(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"home_screen_done_for_today_stay_tuned"));
			}
		} else if (mNewChunks != null && mNewChunks.size() > 0) {
			if (mLearnHighlightLayout == null)
				mLearnHighlightLayout = (LinearLayout) mInflater.inflate(
						R.layout.fragment_home_sreen_learn_highlight, null);
			switch (layoutType) {
			case LAYOUT_TYPE_ALLDONE:
				mPhraseLayout.removeView(mAllDoneLayout);
				mPhraseLayout.addView(mLearnHighlightLayout, params);
				break;
			case LAYOUT_TYPE_LEARN:
				break;
			case LAYOUT_TYPE_PRACTICE:
				mPhraseLayout.removeView(mPracticeHighlightLayout);
				mPhraseLayout.addView(mLearnHighlightLayout, params);
				break;
			default:
				mPhraseLayout.addView(mLearnHighlightLayout, params);
				break;
			}
			layoutType = LAYOUT_TYPE_LEARN;
			mLearnGoBtn = (ImageButton) mPhraseLayout
					.findViewById(R.id.home_screen_learn_go);
			if (mNewChunks != null && mNewChunks.size() > 0) {
				mLearnGoBtn.setVisibility(View.VISIBLE);
				mLearnGoBtn.setOnClickListener(new OnPhraseClick());
			} else
				mLearnGoBtn.setVisibility(View.GONE);
		} else {
			// inflate the layout: fragment_home_screen_practice_highlight
			if (mPracticeHighlightLayout == null)
				mPracticeHighlightLayout = (LinearLayout) mInflater.inflate(
						R.layout.fragment_home_sreen_practice_highlight, null);
			switch (layoutType) {
			case LAYOUT_TYPE_ALLDONE:
				mPhraseLayout.removeView(mAllDoneLayout);
				mPhraseLayout.addView(mPracticeHighlightLayout, params);
				break;
			case LAYOUT_TYPE_LEARN:
				mPhraseLayout.removeView(mLearnHighlightLayout);
				mPhraseLayout.addView(mPracticeHighlightLayout, params);
				break;
			case LAYOUT_TYPE_PRACTICE:
				break;
			default:
				mPhraseLayout.addView(mPracticeHighlightLayout, params);
				break;
			}
			layoutType = LAYOUT_TYPE_PRACTICE;
			mPracticeGoBtn = (ImageButton) mPhraseLayout
					.findViewById(R.id.home_screen_practice_go);
			mPracticeGoBtn.setOnClickListener(new OnPhraseClick());
		}
		// get chunk layout views
		mLearnPhraseLayout = (RelativeLayout) mPhraseLayout
				.findViewById(R.id.home_screen_learn_layout);
		mPracticePhraseLayout = (RelativeLayout) mPhraseLayout
				.findViewById(R.id.home_screen_practice_layout);
		mMasteredPhraseLayout = (RelativeLayout) mPhraseLayout
				.findViewById(R.id.home_screen_mastered_layout);
		TextView mLearnCount = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_learn_count);
		TextView mPracticeCount = (TextView) mPhraseLayout
				.findViewById(R.id.home_sreen_practice_count);
		mMasteredCount = (TextView) mPhraseLayout
				.findViewById(R.id.home_sreen_mastered_count);
		TextView mLearnTitle = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_learn_title);
		mLearnTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "home_screen_learn_title"));
		TextView home_screen_everyday_learn_new = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_learn_available_info);
		TextView home_sreen_mastered_title = (TextView) mPhraseLayout
				.findViewById(R.id.home_sreen_mastered_title);
		home_sreen_mastered_title
				.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"home_screen_mastered_title"));
		TextView mPracticeTitle = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_practice_title);
		if (mPracticeTitle != null) {
			mPracticeTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "home_screen_practice_title"));
		}

		TextView mLearnAvailableInfo = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_learn_available_info);
		TextView practice_times_to_master = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_practice_available_info);
		TextView mPracticeAvaiableInfo = (TextView) mPhraseLayout
				.findViewById(R.id.home_screen_practice_available_info);
		// add click events
		mLearnPhraseLayout.setOnClickListener(new OnPhraseClick());
		mPracticePhraseLayout.setOnClickListener(new OnPhraseClick());
		mMasteredPhraseLayout.setOnClickListener(new OnPhraseClick());
		// update the number of new/rehearse chunk
		int learnChunkNum, practiceChunkNum = 0;
		learnChunkNum = mNewChunks == null ? 0 : mNewChunks.size();
		practiceChunkNum = mRehearseChunkList == null ? 0 : mRehearseChunkList
				.size();
		mLearnCount.setText(Integer.toString(learnChunkNum));
		mPracticeCount.setText(Integer.toString(practiceChunkNum));
		mMasteredCount.setText(Integer.toString(masteredChunkNum));

		if (masteredChunkNum > 0) {
			mMasteredCount.setTextColor(getResources().getColor(
					R.color.bella_color_orange_light));
		} else {
			mMasteredCount.setTextColor(getResources().getColor(
					R.color.bella_color_black_dark));
			mMasteredCount.setAlpha(0.5f);
		}

		if (layoutType == LAYOUT_TYPE_LEARN) {
			mPracticeAvaiableInfo
					.setText((mRehearseChunkList == null || mRehearseChunkList
							.size() == 0) ? getAvailableLeftTimeText(
							practiceAvailableLeftTime, false)
							: JsonSerializeHelper.JsonLanguageDeserialize(
							mContext,
							"home_screen_practice_available_now"));
			home_screen_everyday_learn_new.setText(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"home_screen_everyday_learn_new"));
		} else if (layoutType == LAYOUT_TYPE_PRACTICE) {
			mLearnAvailableInfo.setText((mNewChunks == null || mNewChunks
					.size() == 0) ? getAvailableLeftTimeText(
					learnAvailableLeftTime, true) : JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"home_screen_learn_available_now"));
			practice_times_to_master.setText(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"home_screen_practice_3_times_to_master"));
		}
		// set font for text
		FontHelper.applyFont(mContext, mPhraseLayout, FontHelper.FONT_Museo300);
		if (layoutType == LAYOUT_TYPE_LEARN) {
			if (practiceChunkNum > 0) {
				mPracticeCount.setTextColor(getResources().getColor(
						R.color.bella_color_orange_light));
			} else {
				mPracticeCount.setTextColor(getResources().getColor(
						R.color.bella_color_black_dark));
				mPracticeCount.setAlpha(0.5f);
			}

			FontHelper.applyFont(mContext, mLearnTitle,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mPracticeCount,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mMasteredCount,
					FontHelper.FONT_Museo500);
		} else if (layoutType == LAYOUT_TYPE_PRACTICE) {
			if (learnChunkNum > 0) {
				mLearnCount.setTextColor(getResources().getColor(
						R.color.bella_color_orange_light));
			} else {
				mLearnCount.setTextColor(getResources().getColor(
						R.color.bella_color_black_dark));
				mLearnCount.setAlpha(0.5f);
				mMasteredCount.setAlpha(0.5f);
			}

			FontHelper.applyFont(mContext, mPracticeTitle,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mLearnCount,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mMasteredCount,
					FontHelper.FONT_Museo500);
		} else {
			FontHelper.applyFont(mContext, mLearnCount,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mPracticeCount,
					FontHelper.FONT_Museo500);
			FontHelper.applyFont(mContext, mMasteredCount,
					FontHelper.FONT_Museo500);
			if (goodJob != null)
				FontHelper.applyFont(mContext, goodJob,
						FontHelper.FONT_Museo500);
		}
	}

	/**
	 * 加载首页朋友
	 */
	private void loadFriendBashboard() {
		mFriendList = dashboardBLL
				.getCachedDashboardFriends(AppConst.CurrUserInfo.UserId);
		mFriendList = dashboardBLL.calDashboardRankWithLatestScore(mFriendList,
				AppConst.CurrUserInfo.UserId,
				mScoreBiz.getUserScore());
		mFriendContainer.initialize(mFriendLayout);
		if (NetworkChecker.isConnected(mContext)) {
			GetDashboardFriendListTask dashboardtask = new GetDashboardFriendListTask(
					mContext, new PostExecuting<HttpGetFriends>() {
						@Override
						public void executing(HttpGetFriends result) {
							if (result != null && result.status != null
									&& result.status.equals("0")
									&& result.data != null
									&& result.data.size() > 0) {
								mFriendList = result.data;
								mFriendList = dashboardBLL
										.calDashboardRankWithLatestScore(
												mFriendList,
												AppConst.CurrUserInfo.UserId,
												mScoreBiz
														.getUserScore());
								dashboardBLL.cacheDashboardFriends(mFriendList,
										AppConst.CurrUserInfo.UserId);
								mFriendContainer.initialize(mFriendLayout);
							}
						}
					});
			dashboardtask.execute();
		}
	}

	/**
	 * 显示通知数量
	 */
	private void getUnreadNofiticationCount() {
		GetUnreadNofiticationCountTask countTask = new GetUnreadNofiticationCountTask(
				mContext, new PostExecuting<HttpUnreadNotificationCount>() {
					@Override
					public void executing(HttpUnreadNotificationCount result) {
						if (result == null || result.status == null) {
							mNotificationLayout.setVisibility(View.GONE);
							return;
						}
						if (!result.status.equals("0")) {
							Toast.makeText(
									mContext,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mContext,
													"friend_notification_fail_to_get"),
									Toast.LENGTH_SHORT).show();
							mNotificationLayout.setVisibility(View.GONE);
							return;
						}
						if (result.count > 0) {
							mNotificationLayout.setVisibility(View.VISIBLE);
							mNotificationCount.setText(Integer
									.toString(result.count));
						} else
							mNotificationLayout.setVisibility(View.GONE);
					}
				});
		countTask.execute();
	}

	/**
	 * Chunk layout click envents *
	 */
	class OnPhraseClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == mLearnPhraseLayout.getId()
					|| (mLearnGoBtn != null && v.getId() == mLearnGoBtn.getId())) {
				if (mNewChunks != null && mNewChunks.size() > 0) {
					Chunk curentChunk = mNewChunks.get(0);
					// Chunk curentChunk = JsonSerializeHelper.getStatusChunk(
					// mContext, "1").get(0);
					new NewChunkOpenAction().open(mActivity, curentChunk);
				} else
					Toast.makeText(
							mContext,
							JsonSerializeHelper.JsonLanguageDeserialize(
									mContext, "home_screen_no_new_chunks"),
							Toast.LENGTH_SHORT).show();
			} else if (v.getId() == mPracticePhraseLayout.getId()
					|| (mPracticeGoBtn != null && v.getId() == mPracticeGoBtn
							.getId())) {
				if (mRehearseChunkList != null && mRehearseChunkList.size() > 0)
					new RehearseChunkOpenAction().open(mActivity,
							mRehearseChunkList);
				else
					Toast.makeText(
							mContext,
							JsonSerializeHelper.JsonLanguageDeserialize(
									mContext, "home_screen_no_rehearse_chunks"),
							Toast.LENGTH_SHORT).show();
			} else if (v.getId() == mMasteredPhraseLayout.getId()) {
				Intent intent = new Intent(mActivity, ChunkListActivity.class);
				intent.putExtra(AppConst.BundleKeys.Chunk_List_Type, 1);
				mActivity.startActivity(intent);
			}
		}
	}

	/**
	 * 最下排朋友内容
	 */
	class FriendsContainer {

		List<DashBoardFriendView> friendsListViews = new ArrayList<DashBoardFriendView>();
		LinearLayout mFriendLayout;

		public void initialize(LinearLayout friendLayout) {
			int friendNum = 0;
			if (mFriendLayout != null) {
				clear();
			}
			if (mFriendList != null && mFriendList.size() > 0) {
				for (int index = 0; index < (mFriendList.size() > 4 ? 4
						: mFriendList.size()); index++) {
					final DashBoardFriendView friendView = new DashBoardFriendView(
							mActivity);
					View view = friendView.getView(mFriendList.get(index));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							0, LinearLayout.LayoutParams.MATCH_PARENT);
					params.weight = 1;
					friendLayout.addView(view, params);
					friendView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (friendView.isSelected == false) {
								HttpGetFriendData profile = friendView
										.getProfile();
								currentUserSelected = profile;
								openProfile(profile);
							}
						}
					});
					friendsListViews.add(friendView);
					friendNum++;
				}
			}
			for (int index = friendNum; index < 4; index++) {
				DashBoardFriendView inviteView = new DashBoardFriendView(
						mActivity);
				View view = inviteView.getInviteView();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.MATCH_PARENT);
				params.weight = 1;
				friendLayout.addView(view, params);
			}

			mFriendLayout = friendLayout;
		}

		/**
		 * clear friend dashboard layout *
		 */
		public void clear() {
			if (mFriendLayout != null)
				mFriendLayout.removeAllViews();
			friendsListViews.clear();
		}

		/**
		 * Open the profile with data *
		 */
		public void openProfile(HttpGetFriendData data) {
			if (data == null)
				return;
			ProfileModel profile = null;
			if (data.bella_id.equals(AppConst.CurrUserInfo.UserId)) {
				// myself
				profile = new ProfileModel(AppConst.CurrUserInfo.UserId,
						AppConst.CurrUserInfo.Alias,
						AppConst.CurrUserInfo.Avatar,
						mScoreBiz.getUserScore(),
						masteredChunkNum, data.friend_count, false);
				AppConst.CurrUserInfo.Avatar = data.avatar;
			} else
				// one friend
				profile = new ProfileModel(currentUserSelected.bella_id,
						currentUserSelected.alias, currentUserSelected.avatar,
						currentUserSelected.score, masteredChunkNum,
						currentUserSelected.friend_count, true);
			profile.IsOpenFromHomeScreen = true;
			new UserProfileOpenAction().open(mActivity, profile);

		}
	}

	long ONE_MINITUE = 60 * 1000;
	long ONE_HOUR = 60 * ONE_MINITUE;
	long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 显示将来可用的具体剩余时间
	 *
	 * @param availableTime
	 * @param isLearn
	 *            是否是learn，或者practice
	 * @return
	 */
	public String getAvailableLeftTimeText(Long availableTime, boolean isLearn) {
		// String TimeText = null;
		try {
			if (availableTime == null)
				return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "home_screen_learn_available_tomorrow")
						: JsonSerializeHelper.JsonLanguageDeserialize(mContext,
								"home_screen_practice_available_tomorrow");
			if (availableTime >= ONE_DAY)
				return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "home_screen_learn_available_tomorrow")
						: JsonSerializeHelper.JsonLanguageDeserialize(mContext,
								"home_screen_practice_available_tomorrow");
			if (availableTime < ONE_DAY && availableTime >= ONE_HOUR) {
				int leftHour = 24 - (int) (availableTime / ONE_HOUR);
				return String.format(
						isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
								mContext, "home_screen_learn_available_hours")
								: JsonSerializeHelper.JsonLanguageDeserialize(
										mContext,
										"home_screen_practice_available_hour"),
						leftHour);
			}
			if (availableTime < ONE_HOUR && availableTime > 0) {
				int leftMinutes = 60 - (int) (availableTime / ONE_MINITUE);
				return String
						.format(isLearn ? JsonSerializeHelper
								.JsonLanguageDeserialize(mContext,
										"home_screen_learn_available_tomorrow")
								: JsonSerializeHelper
										.JsonLanguageDeserialize(mContext,
												"home_screen_practice_available_minutes"),
								leftMinutes);
			}
			if (availableTime <= 0) {
				return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
						mContext, "home_screen_learn_available_now")
						: JsonSerializeHelper.JsonLanguageDeserialize(mContext,
								"home_screen_practice_available_now");
			}
			return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "home_screen_learn_available_tomorrow")
					: JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"home_screen_practice_available_tomorrow");
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "0";
		}

	}

	/**
	 * execute action with different type
	 */
	private void executePushAction() {
		PushData pushData = ((MainActivity) getActivity()).getPushData();
		if (pushData == null) {
			return;
		}
		switch (pushData.getType()) {
		case new_lesson:
			mLearnPhraseLayout.performClick();
			break;
		case new_rehearsal:
			mPracticePhraseLayout.performClick();
			break;
		case recording_rate:
			inboxView.performClick();
			break;
		default:
			break;
		}
	}

	/**
	 * Get dashboard info
	 */
	public void getDashboard() {
		GetDashboardTask task = new GetDashboardTask(mContext,
				new PostExecuting<HttpDashboard>() {
					@Override
					public void executing(HttpDashboard httpDashboard) {
						if (mMasteredCount == null || httpDashboard == null || httpDashboard.data==null) {
							return;
						}
						mMasteredCount.setText(httpDashboard.data.master_count
								+ "");
					}
				});
		task.execute();
	}

	public void putRatingShareDate() {
		RatingModel ratingModel2 = new RatingModel();
		ratingModel2.date = sFormat.format(new Date());
		sRatingShareStorage.put(ratingModel2);
	}
}
