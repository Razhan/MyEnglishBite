package com.ef.bite.ui.user;

import java.util.List;
import com.ef.bite.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;
import cn.sharesdk.wechat.friends.Wechat;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.SearchUserListAdapter;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.GetShareFriendLinkTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.SearchUserTask;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;
import com.ef.bite.dataacces.mode.httpMode.HttpShareLink;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.SoftInputHelper;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.SelectSwitcherLayout;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

public class AddFriendActivity extends BaseActivity {
	ActionbarLayout mActionbar;
	SelectSwitcherLayout mSelectSwitcher;
	// 搜索
	EditText mSearchEdit;
	ImageView mSearchCancel;
	ImageView mSearchIcon;
	// 搜索结果
	LinearLayout mSearchResultLayout;
	TextView mResultCount;
	ListView mSearchListView;
	SearchUserListAdapter mSearchAdapter;
	ProgressDialog mProgress;
	List<HttpGetFriendData> mSearchUserList;

	enum SearchType {
		Wechat, FEID
	}

	SearchType searchType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_add_friend);
		mActionbar = (ActionbarLayout) findViewById(R.id.add_friend_action_bar);
		mSelectSwitcher = (SelectSwitcherLayout) findViewById(R.id.add_friend_switcher_layout);
		mSearchEdit = (EditText) findViewById(R.id.add_friend_search_text);
		mSearchEdit.setHint(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "add_friend_search_hint"));
		mSearchCancel = (ImageView) findViewById(R.id.add_friend_search_cancel);
		mSearchIcon = (ImageView) findViewById(R.id.add_friend_search_icon);
		mSearchResultLayout = (LinearLayout) findViewById(R.id.add_friend_search_result_layout);
		mSearchListView = (ListView) findViewById(R.id.add_friend_search_result_listview);
		mResultCount = (TextView) findViewById(R.id.add_friend_search_result_count);
		mSearchResultLayout.setVisibility(View.GONE);
		// 字体设置
		FontHelper.applyFont(mContext, mResultCount, FontHelper.FONT_Museo500);

		mSelectSwitcher.initializeWithIcon(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "add_friend_wechat"),
				R.drawable.add_friend_wechat, R.drawable.add_friend_wechat_i,
				JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"add_friend_search_id"), R.drawable.leaderboard_global,
				R.drawable.leaderboard_global_i, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						searchType = SearchType.Wechat;

                        if (AppConst.CurrUserInfo.Location.equals("cn")) {
                            wechatInvite();
                        } else {
							wechatInvite();
                        }

						mSelectSwitcher.selectRight();

						MobclickTracking.OmnitureTrack.ActionInviteaFriend(1);
//						MobclickTracking.UmengTrack.actionInviteaFriend(1, mContext);
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						searchType = SearchType.FEID;
					}
				});
		mSelectSwitcher.selectRight();
		mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String key = mSearchEdit.getText().toString();
					if (key == null || key.isEmpty()) {
						Toast.makeText(
								mContext,
								getString(R.string.add_friend_search_error_no_key),
								Toast.LENGTH_SHORT).show();
						return false;
					}
					search(key);
					MobclickTracking.OmnitureTrack.ActionInviteaFriend(2);
					return false;
				}
				return false;
			}
		});
		// 初始化Actionbar
		mActionbar.initiWithTitle(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "add_friend_actionbar_title"),
				R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}, null);
		// Search by clicking icon
		mSearchIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = mSearchEdit.getText().toString();
				if (key != null && !key.isEmpty())
					search(key);
				MobclickTracking.OmnitureTrack.ActionInviteaFriend(2);
//				MobclickTracking.UmengTrack.actionInviteaFriend(2, mContext);
			}
		});
		// Cancel Search
		mSearchCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchEdit.setText("");
			}
		});
		// tracking
		// TraceHelper.tracingPage(mContext, TraceHelper.PAGE_INVITE_FRIEND);
		// add omniture
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.InviteaFriendValues.pageNameValue,
				ContextDataMode.InviteaFriendValues.pageSiteSubSectionValue,
				ContextDataMode.InviteaFriendValues.pageSiteSectionValue,
				mContext);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MobclickTracking.UmengTrack.setPageStart(
		// ContextDataMode.InviteaFriendValues.pageNameValue,
		// ContextDataMode.InviteaFriendValues.pageSiteSubSectionValue,
		// ContextDataMode.InviteaFriendValues.pageSiteSectionValue,
		// mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// MobclickTracking.UmengTrack.setPageEnd(
		// ContextDataMode.InviteaFriendValues.pageNameValue,
		// ContextDataMode.InviteaFriendValues.pageSiteSubSectionValue,
		// ContextDataMode.InviteaFriendValues.pageSiteSectionValue,
		// mContext);
	}

	/** 在线搜索人员信息 **/
	private void search(final String key) {
		mProgress = new ProgressDialog(AddFriendActivity.this);
		mProgress.setMessage(getString(R.string.add_friend_search_hint));
		mProgress.show();
		SearchUserTask searchTask = new SearchUserTask(mContext,
				new PostExecuting<HttpGetFriends>() {
					@Override
					public void executing(HttpGetFriends result) {
						mProgress.dismiss();
						if (result != null) {
							mSearchResultLayout.setVisibility(View.VISIBLE);
							mSearchUserList = result.data;
							mResultCount.setText(Integer
									.toString(mSearchUserList == null ? 0
											: mSearchUserList.size())
									+ " "
									+ getString(R.string.add_friend_search_result));
							mSearchAdapter = new SearchUserListAdapter(
									AddFriendActivity.this, key,
									mSearchUserList);
							mSearchListView.setAdapter(mSearchAdapter);
							mSearchListView
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											HttpGetFriendData user = mSearchUserList
													.get(position);
											if (user != null)
												new UserProfileOpenAction()
														.open(mContext, user,
																false);
										}
									});
						} else
							Toast.makeText(mContext,
									getString(R.string.network_not_available),
									Toast.LENGTH_SHORT).show();
					}
				});
		searchTask.execute(new String[] { key });
		// 隐藏键盘
		SoftInputHelper.hideTemporarily(this);
	}

	/** 微信邀请好友 **/
	private void wechatInvite() {
		GetShareFriendLinkTask task = new GetShareFriendLinkTask(mContext,
				new PostExecuting<HttpShareLink>() {
					@Override
					public void executing(HttpShareLink result) {
						if (result != null && result.status != null
								&& result.data != null) {
							if (!result.status.equals("0")) {
								Toast.makeText(mContext, result.message,
										Toast.LENGTH_SHORT).show();
								return;
							}
							final OnekeyShare oks = new OnekeyShare();
							oks.setNotification(R.drawable.ic_launcher,
									mContext.getString(R.string.app_name));
							oks.setTitle(result.data.message);
							oks.setUrl(result.data.url);
							oks.setPlatform(Wechat.NAME);
							// 令编辑页面显示为Dialog模式
							oks.setDialogMode();
							// oks.setInstallUrl("http://www.mob.com");
							// 在自动授权时可以禁用SSO方式
							oks.disableSSOWhenAuthorize();
							oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(
									result.data.message, result.data.url));
							oks.show(mContext);
						} else
							Toast.makeText(
									mContext,
									getString(R.string.share_text_link_genrate_failed),
									Toast.LENGTH_SHORT).show();
					}
				});
		task.execute(new String[]{AppConst.CurrUserInfo.UserId,
                AppLanguageHelper.getSystemLaunguage(mContext)});
		// tracking
		// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_INVITE_FRIEND,
		// TraceHelper.ACTION_CLICK, null, TraceHelper.TARGET_WECHAT);
	}


    private void facebookInvite() {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/1603425726573202";
        previewImageUrl = null;

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }









    }
}
