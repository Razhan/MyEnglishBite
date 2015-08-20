package com.ef.bite.ui.user;

import java.util.ArrayList;
import com.ef.bite.R;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ContactListAdapter;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.GetFriendListTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.FriendsSharedStorage;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.user.contactlistview.ContactItemComparator;
import com.ef.bite.ui.user.contactlistview.ContactItemInterface;
import com.ef.bite.ui.user.contactlistview.ContactListView;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.widget.ActionbarLayout;

public class FriendListActivity extends BaseActivity {
	ActionbarLayout mActionbar;
	TextView mFriendCount;
	ContactListView mFriendListView;
	ProgressDialog mProgress;
	ContactListAdapter mFriendListAdapter;
	List<HttpGetFriendData> mFriendList;
	List<ContactItemInterface> mContactList = new ArrayList<ContactItemInterface>();

	ProfileModel mProfileModel = null; // 当前用户的profile信息, null代表是本人
	FriendsSharedStorage mFriendChache;

	private final static int FriendsListMeValues = 1;
	private final static int FriendsListFriendsValues = 2;
	private final static int FriendsListOthersValues = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(AppConst.BundleKeys.Get_Friend_List_Profile)) {
				String json = bundle
						.getString(AppConst.BundleKeys.Get_Friend_List_Profile);
				mProfileModel = new ProfileModel();
				mProfileModel.parse(json);
			}
		}
		mActionbar = (ActionbarLayout) findViewById(R.id.friend_list_action_bar);
		mFriendCount = (TextView) findViewById(R.id.friend_list_friend_count);
		mFriendListView = (ContactListView) findViewById(R.id.friend_list_listview);
		mFriendListView.setFastScrollEnabled(true);
		if (mProfileModel != null) {
			mActionbar.initiWithTitle(mProfileModel.Alias
					+ getString(R.string.friend_list_s_friends),
					R.drawable.arrow_goback_black,
					R.drawable.leaderboard_invite_friend,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							finish();
						}
					}, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 邀请好友
							Intent intent = new Intent(mContext,
									AddFriendActivity.class);
							startActivity(intent);
						}
					});
		}
		mFriendChache = new FriendsSharedStorage(mContext,
				AppConst.CurrUserInfo.UserId,
				FriendsSharedStorage.TYPE_FRIEND_LIST);
		loadFriends();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mProfileModel == null) {
			BI_Tracking(FriendsListMeValues);
		}
		if (mProfileModel.IsFriend) {
			BI_Tracking(FriendsListFriendsValues);
		} else {
			BI_Tracking(FriendsListOthersValues);
		}
	}

	/** 加载朋友列表 **/
	private void loadFriendListView() {
		mFriendCount.setText(Integer.toString(mFriendList != null ? mFriendList
				.size() : 0));
		sortAndConvert(mFriendList);
		mFriendListAdapter = new ContactListAdapter(FriendListActivity.this,
				R.layout.friend_list_item, mContactList);
		mFriendListAdapter.setInSearchMode(true);
		mFriendListView.setAdapter(mFriendListAdapter);
		mFriendListView.getScroller().show();
		mFriendListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HttpGetFriendData user = (HttpGetFriendData) mContactList
						.get(position);
				if (user != null)
					new UserProfileOpenAction().open(mContext, user, false);
			}
		});

		mFriendListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});

	}

	/** 下载并加载朋友信息 **/
	private void loadFriends() {
		mFriendList = mFriendChache.get();
		loadFriendListView();
		if (!NetworkChecker.isConnected(mContext)) {
			Toast.makeText(mContext, R.string.network_not_available,
					Toast.LENGTH_SHORT).show();
			return;
		}

		mProgress = new ProgressDialog(this);
		mProgress.setMessage(getString(R.string.friend_list_loading_friends));
		mProgress.show();
		GetFriendListTask getTask = new GetFriendListTask(mContext,
				new PostExecuting<HttpGetFriends>() {
					@Override
					public void executing(HttpGetFriends result) {
						mProgress.dismiss();
						if (result != null && result.data != null) {
							mFriendList = result.data;
							mFriendChache.put(mFriendList);
							loadFriendListView();
						} else
							Toast.makeText(
									mContext,
									getString(R.string.friend_list_fail_loaded),
									Toast.LENGTH_SHORT).show();
					}
				});
		getTask.execute(new String[] { mProfileModel == null ? AppConst.CurrUserInfo.UserId
				: mProfileModel.UID });
	}

	private List<ContactItemInterface> sortAndConvert(
			List<HttpGetFriendData> friendList) {
		if (friendList != null && friendList.size() > 0) {
			mContactList.clear();
			for (int index = 0; index < friendList.size(); index++) {
				mContactList.add(friendList.get(index));
			}
			Collections.sort(mContactList, new ContactItemComparator());
			return mContactList;
		} else
			return null;
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.FriendsListMeValues.pageNameValue,
							ContextDataMode.FriendsListMeValues.pageSiteSubSectionValue,
							ContextDataMode.FriendsListMeValues.pageSiteSectionValue,
							mContext);
			break;
		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.FriendsListFriendsValues.pageNameValue,
							ContextDataMode.FriendsListFriendsValues.pageSiteSubSectionValue,
							ContextDataMode.FriendsListFriendsValues.pageSiteSectionValue,
							mContext);
			break;
		case 3:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.FriendsListOthersValues.pageNameValue,
							ContextDataMode.FriendsListOthersValues.pageSiteSubSectionValue,
							ContextDataMode.FriendsListOthersValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}
}