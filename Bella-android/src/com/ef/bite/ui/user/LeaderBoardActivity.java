package com.ef.bite.ui.user;

import java.util.ArrayList;
import com.ef.bite.R;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.LeaderboardListAdapter;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.GetLeaderboardListTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.FriendsSharedStorage;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.RoundedImageView;
import com.ef.bite.widget.SelectSwitcherLayout;
import com.ef.bite.widget.UserLevelView;

public class LeaderBoardActivity extends BaseActivity {
	/** 我的朋友排名 **/
	public final static int LEADERBOARD_FRIENDS = 0;
	/** 总区域排名 **/
	public final static int LEADERBOARD_GLOBAL = 1;
	/** 每次更新 **/
	public final static long UPDATE_INTERVAL_TIME = 1 * 60 * 1000;

	public static HttpGetFriendData mFriendFirstData = null;
	public static HttpGetFriendData mGlobalFirstData = null;
	public List<HttpGetFriendData> mFriendList = new ArrayList<HttpGetFriendData>();
	public static List<HttpGetFriendData> mGlobalList = new ArrayList<HttpGetFriendData>();
	ActionbarLayout mActionbar;
	SelectSwitcherLayout mSelectSwitcher;
	LinearLayout mFriendLayout;
	LinearLayout mGlobalLayout;
	RelativeLayout mFriendFirst;
	RelativeLayout mGlobalFirst;
	ListView mFriendListView;
	ListView mGlobalListView;
	ProgressDialog mProgress;
	LeaderboardListAdapter mFriendListAdapter;
	LeaderboardListAdapter mGlobalListAdapter;

	private static int leaderboard_type = LEADERBOARD_FRIENDS; // 当前leaderboard的类型：
	long lastLoadTime = 0; // 上次加载列表的时间
	FriendsSharedStorage mFriendChache;

	private static int LeaderboardFriendsValues = 1;
	private static int LeaderboardGlobalValues = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		mActionbar = (ActionbarLayout) findViewById(R.id.leaderboard_action_bar);
		mSelectSwitcher = (SelectSwitcherLayout) findViewById(R.id.leaderboard_switcher_layout);
		mFriendLayout = (LinearLayout) findViewById(R.id.leaderboard_friends_layout);
		mGlobalLayout = (LinearLayout) findViewById(R.id.leaderboard_global_layout);
		mFriendFirst = (RelativeLayout) findViewById(R.id.leaderboard_friends_first);
		mGlobalFirst = (RelativeLayout) findViewById(R.id.leaderboard_global_first);
		mFriendListView = (ListView) findViewById(R.id.leaderboard_friends_listview);
		mGlobalListView = (ListView) findViewById(R.id.leaderboard_global_listview);
		// Actionbar初始化
		mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "home_screen_leaderboard_title"),
				R.drawable.arrow_goback_black,
				R.drawable.leaderboard_invite_friend,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(
								R.anim.activity_in_from_right,
								R.anim.activity_out_to_left);
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 邀请好友
						Intent intent = new Intent(mContext,
								AddFriendActivity.class);
						startActivity(intent);
						// tracking
						// TraceHelper.tracingAction(mContext,
						// TraceHelper.PAGE_LEADERBOARD,
						// TraceHelper.ACTION_CLICK, null,
						// TraceHelper.TARGET_INVITE_FRIEND);
					}
				});
		mSelectSwitcher.initializeWithIcon(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "leaderboard_friends"),
				R.drawable.leaderboard_friends,
				R.drawable.leaderboard_friends_i,
				JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"leaderboard_global"), R.drawable.leaderboard_global,
				R.drawable.leaderboard_global_i, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (leaderboard_type == LEADERBOARD_FRIENDS)
							return;
						leaderboard_type = LEADERBOARD_FRIENDS;
						loadData();
						BI_Tracking(LeaderboardFriendsValues);
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (leaderboard_type == LEADERBOARD_GLOBAL)
							return;
						leaderboard_type = LEADERBOARD_GLOBAL;
						loadData();
						BI_Tracking(LeaderboardGlobalValues);
					}
				});
		if (leaderboard_type == LEADERBOARD_FRIENDS) {
			mSelectSwitcher.selectLeft();
		}

		else {
			mSelectSwitcher.selectRight();
		}

		mFriendChache = new FriendsSharedStorage(mContext,
				AppConst.CurrUserInfo.UserId,
				FriendsSharedStorage.TYPE_FRIEND_LEADERBOARD);
		loadData();

		// tracking
		// TraceHelper.tracingPage(mContext, TraceHelper.PAGE_LEADERBOARD);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MobclickTracking.UmengTrack
		// .setPageStart(
		// ContextDataMode.LeaderboardFriendsValues.pageNameValue,
		// ContextDataMode.LeaderboardFriendsValues.pageSiteSubSectionValue,
		// ContextDataMode.LeaderboardFriendsValues.pageSiteSectionValue,
		// mContext);
		// MobclickTracking.UmengTrack
		// .setPageStart(
		// ContextDataMode.LeaderboardGlobalValues.pageNameValue,
		// ContextDataMode.LeaderboardGlobalValues.pageSiteSubSectionValue,
		// ContextDataMode.LeaderboardGlobalValues.pageSiteSectionValue,
		// mContext);
		if (leaderboard_type == LEADERBOARD_FRIENDS) {
			BI_Tracking(LeaderboardFriendsValues);
		}
		if (leaderboard_type == LEADERBOARD_GLOBAL) {
			BI_Tracking(LeaderboardGlobalValues);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// MobclickTracking.UmengTrack
		// .setPageEnd(
		// ContextDataMode.LeaderboardFriendsValues.pageNameValue,
		// ContextDataMode.LeaderboardFriendsValues.pageSiteSubSectionValue,
		// ContextDataMode.LeaderboardFriendsValues.pageSiteSectionValue,
		// mContext);
		// MobclickTracking.UmengTrack
		// .setPageEnd(
		// ContextDataMode.LeaderboardGlobalValues.pageNameValue,
		// ContextDataMode.LeaderboardGlobalValues.pageSiteSubSectionValue,
		// ContextDataMode.LeaderboardGlobalValues.pageSiteSectionValue,
		// mContext);
	}

	/** 加载数据 **/
	private void loadData() {
		mFriendList = mFriendChache.get();
		if (leaderboard_type == LEADERBOARD_FRIENDS) {
			mFriendLayout.setVisibility(View.VISIBLE);
			mGlobalLayout.setVisibility(View.GONE);
			loadFriendListView();
		} else if (leaderboard_type == LEADERBOARD_GLOBAL) {
			mFriendLayout.setVisibility(View.GONE);
			mGlobalLayout.setVisibility(View.VISIBLE);
			loadGolbalListView(null);
		}
		if (System.currentTimeMillis() - lastLoadTime < UPDATE_INTERVAL_TIME)
			return;
		if (!NetworkChecker.isConnected(mContext)) {
			Toast.makeText(mContext, getString(R.string.network_not_available),
					Toast.LENGTH_SHORT).show();
			return;
		}
		mProgress = new ProgressDialog(LeaderBoardActivity.this);
		if (leaderboard_type == LEADERBOARD_FRIENDS) {
			mProgress.setMessage(getResources()
					.getString(R.string.loading_data));
		} else if (leaderboard_type == LEADERBOARD_GLOBAL) {
			mProgress.setMessage(getResources()
					.getString(R.string.loading_data));
		}
		mProgress.show();
		// 加载我的朋友排名
		GetLeaderboardListTask friendtask = new GetLeaderboardListTask(
				mContext, GetLeaderboardListTask.TYPE_FRIEND,
				new PostExecuting<HttpGetFriends>() {
					@Override
					public void executing(HttpGetFriends result) {
						if (leaderboard_type == LEADERBOARD_FRIENDS)
							mProgress.dismiss();
						if (result != null && result.status != null) {
							if (result.status.equals("0")) {
								lastLoadTime = System.currentTimeMillis();
								if (result.data != null
										&& result.data.size() > 0) {
									mFriendList = result.data;
									mFriendChache.put(mFriendList);
									loadFriendListView();
								} else
									Toast.makeText(mContext,
											"No friends listed! ",
											Toast.LENGTH_SHORT).show();
							} else
								Toast.makeText(mContext, result.message,
										Toast.LENGTH_SHORT).show();
						} else
							Toast.makeText(
									mContext,
									getResources().getString(
											R.string.leaderboard_fail_to_load),
									Toast.LENGTH_LONG).show();
					}
				});
		friendtask.execute();
		// 加载区域总排名
		GetLeaderboardListTask leadertask = new GetLeaderboardListTask(
				mContext, GetLeaderboardListTask.TYPE_GLOBAL,
				new PostExecuting<HttpGetFriends>() {
					@Override
					public void executing(HttpGetFriends result) {
						if (leaderboard_type == LEADERBOARD_GLOBAL)
							mProgress.dismiss();
						if (result != null && result.status != null) {
							lastLoadTime = System.currentTimeMillis();
							if (result.status.equals("0")) {
								if (result.data != null
										&& result.data.size() > 0) {
									mGlobalList.clear();
									loadGolbalListView(result.data);
								} else
									Toast.makeText(mContext,
											"No leaderboard listed! ",
											Toast.LENGTH_SHORT).show();
							} else
								Toast.makeText(mContext, result.message,
										Toast.LENGTH_SHORT).show();
						} else
							Toast.makeText(
									mContext,
									getResources().getString(
											R.string.leaderboard_fail_to_load),
									Toast.LENGTH_LONG).show();
					}
				});
		leadertask.execute();
	}

	/**
	 * 加载朋友的排名
	 */
	private void loadFriendListView() {
		if (mFriendList == null || mFriendList.size() <= 0)
			return;
		if (leaderboard_type != LEADERBOARD_FRIENDS)
			return;
		// 重新按积分降序排序朋友列表
		Collections.sort(mFriendList, new FriendsScoreComparator());
		// 加载排名第一的朋友信息
		final HttpGetFriendData first = mFriendList.get(0);
		loadFirstProfile(mFriendFirst, first);
		mFriendFirst.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserProfileOpenAction openAction = new UserProfileOpenAction();
				openAction.open(mContext, first, true);
			}
		});
		// 加载其他朋友信息
//		if (mFriendList.size() < 2)
//			return;
		final List<HttpGetFriendData> otherList = new ArrayList<HttpGetFriendData>();
		for (int index = 0; index < mFriendList.size(); index++)
			otherList.add(mFriendList.get(index));
		mFriendListAdapter = new LeaderboardListAdapter(
				LeaderBoardActivity.this, otherList);
		mFriendListView.setAdapter(mFriendListAdapter);
		mFriendListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserProfileOpenAction openAction = new UserProfileOpenAction();
				openAction.open(mContext, otherList.get(position), true);
			}
		});
	}

	/**
	 * 加载全球排名
	 */
	private void loadGolbalListView(List<HttpGetFriendData> dataList) {
		if (dataList == null || dataList.size() <= 0)
			return;
		// if(leaderboard_type != LEADERBOARD_GLOBAL)
		// return;
		// 加载区域排名第一的人员信息
		final HttpGetFriendData first = dataList.get(0);
		loadFirstProfile(mGlobalFirst, first);
		mGlobalFirst.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new UserProfileOpenAction().open(mContext, first, false);
			}
		});
		// 加载其他人信息
		if (dataList.size() < 2)
			return;
		for (int index = 1; index < dataList.size(); index++)
			mGlobalList.add(dataList.get(index));
		mGlobalListAdapter = new LeaderboardListAdapter(
				LeaderBoardActivity.this, mGlobalList);
		mGlobalListView.setAdapter(mGlobalListAdapter);
		mGlobalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new UserProfileOpenAction().open(mContext,
						mGlobalList.get(position), false);
			}
		});
		mGlobalListView.setOnScrollListener(new FriendsScrollListener(dataList
				.size()));
	}

	/**
	 * 加载排名第一的人员信息
	 */
	private void loadFirstProfile(RelativeLayout firstLayout,
			final HttpGetFriendData friend) {
		if (friend != null) {
			TextView mIndex = (TextView) firstLayout
					.findViewById(R.id.leaderboard_list_item_index);
			RoundedImageView mAvatar = (RoundedImageView) firstLayout
					.findViewById(R.id.leaderboard_list_item_avatar);
			TextView mName = (TextView) firstLayout
					.findViewById(R.id.leaderboard_list_item_name);
			UserLevelView mLevel = (UserLevelView) firstLayout
					.findViewById(R.id.leaderboard_list_item_level);
			View mAvatarBg = firstLayout
					.findViewById(R.id.leaderboard_list_item_avatar_first_bg);
			ImageView mAvatarMedal = (ImageView) firstLayout
					.findViewById(R.id.leaderboard_list_item_avatar_first_medal);
			mAvatarBg.setVisibility(View.VISIBLE);
			mAvatarMedal.setVisibility(View.VISIBLE);
			mIndex.setText("1");
			mName.setText(friend.alias);
			if (friend.bella_id.equals(AppConst.CurrUserInfo.UserId)) {// 用户是本人，名字加粗
				mName.getPaint().setFakeBoldText(true);
			} else
				mName.getPaint().setFakeBoldText(false);
			AvatarHelper.LoadAvatar(mAvatar, friend.bella_id, friend.avatar);
			mLevel.initialize(friend.score);
			firstLayout.setVisibility(View.GONE);
			// 字体设置
			FontHelper.applyFont(mContext, mName, FontHelper.FONT_Museo300);
		} else
			firstLayout.setVisibility(View.GONE);
	}

	class FriendsScrollListener implements OnScrollListener {
		// The minimum amount of items to have below your current scroll
		// position
		// before loading more.
		private int visibleThreshold = 5;
		private Integer start = 0;
		// The total number of items in the dataset after the last load
		private int previousTotalItemCount = 0;
		// True if we are still waiting for the last set of data to load.
		private boolean loading = true;

		public FriendsScrollListener(int start) {
			this.start = start;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// If the total item count is zero and the previous isn't, assume
			// the
			// list is invalidated and should be reset back to initial state
			if (totalItemCount < previousTotalItemCount) {
				this.start = 0;
				this.previousTotalItemCount = totalItemCount;
				if (totalItemCount == 0) {
					this.loading = true;
				}
			}

			// If it’s still loading, we check to see if the dataset count has
			// changed, if so we conclude it has finished loading and update the
			// current page
			// number and total item count.
			if (loading && (totalItemCount > previousTotalItemCount)) {
				loading = false;
				previousTotalItemCount = totalItemCount;
			}

			// If it isn’t currently loading, we check to see if we have
			// breached
			// the visibleThreshold and need to reload more data.
			// If we do need to reload some more data, we execute onLoadMore to
			// fetch the data.
			if (start != null
					&& !loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				loading = true;
				onLoadMore();
			}
		}

		public void onLoadMore() {
			GetLeaderboardListTask loadTask = new GetLeaderboardListTask(
					mContext, GetLeaderboardListTask.TYPE_GLOBAL,
					new PostExecuting<HttpGetFriends>() {
						@Override
						public void executing(HttpGetFriends result) {
							if (result != null && result.status != null
									&& result.status.equals("0")) {
								for (int index = 0; index < result.data.size(); index++)
									mGlobalList.add(result.data.get(index));
								// mGlobalList.addAll(result.data);
								mGlobalListAdapter.notifyDataSetChanged();
								start += result.data.size();
							}
						}
					});
			loadTask.execute(new Integer[] { start,
					AppConst.GlobalConfig.PageSize });
		}

	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.LeaderboardFriendsValues.pageNameValue,
							ContextDataMode.LeaderboardFriendsValues.pageSiteSubSectionValue,
							ContextDataMode.LeaderboardFriendsValues.pageSiteSectionValue,
							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.LeaderboardGlobalValues.pageNameValue,
							ContextDataMode.LeaderboardGlobalValues.pageSiteSubSectionValue,
							ContextDataMode.LeaderboardGlobalValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}
}
