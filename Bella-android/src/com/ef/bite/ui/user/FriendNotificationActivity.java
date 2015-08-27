package com.ef.bite.ui.user;

import java.util.ArrayList;

import android.widget.*;
import com.ef.bite.R;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.NotificationListAdapter;
import com.ef.bite.business.ChunkBiz;
import com.ef.bite.business.task.GetAdsLinkTask;
import com.ef.bite.business.task.GetNotificationsTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.ReadNotificationTask;
import com.ef.bite.dataacces.NotificationSharedStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpADsAddress;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpNotification;
import com.ef.bite.dataacces.mode.httpMode.ReadNotificationHttpRequest;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.record.UserRecordingActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.StringUtils;
import com.ef.bite.utils.TraceHelper;

public class FriendNotificationActivity extends BaseActivity {

	public List<HttpNotification.NotificationData> mNotificationList;
	ImageButton mGoback;
	ListView mListView;
	LinearLayout mAdsLayout; // Upsell广告页面
	WebView mAdsWebview;
	NotificationListAdapter mListAdapter;
	ProgressDialog progress;
	NotificationSharedStorage notifyCache;
	private final static int IndexDashboardValues = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_notification);
		mGoback = (ImageButton) findViewById(R.id.friend_notification_action_bar_goback);
		mListView = (ListView) findViewById(R.id.friend_notifiation_listview);
		mAdsLayout = (LinearLayout) findViewById(R.id.friend_notification_ads_layout);
		mAdsWebview = (WebView) findViewById(R.id.friend_notification_ads_webview);
		mGoback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		notifyCache = new NotificationSharedStorage(mContext,
				AppConst.CurrUserInfo.UserId);
		loadAdWeb();
		loadNotifications();
		// Add tracking
		// TraceHelper.tracingPage(mContext, TraceHelper.PAGE_NOTIFICATION);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BI_Tracking(IndexDashboardValues);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.DashboardValues.pageNameValue,
//				ContextDataMode.DashboardValues.pageSiteSubSectionValue,
//				ContextDataMode.DashboardValues.pageSiteSectionValue, mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.DashboardValues.pageNameValue,
//				ContextDataMode.DashboardValues.pageSiteSubSectionValue,
//				ContextDataMode.DashboardValues.pageSiteSectionValue, mContext);
	}

	/**
	 * 刷新好友通知
	 */
	private void loadNotifications() {
		// mNotificationList = notifyCache.get();

		if (mNotificationList != null && mNotificationList.size() > 0) {
			mListAdapter = new NotificationListAdapter(mContext,
					mNotificationList);
			mListView.setAdapter(mListAdapter);
		}

		if (!NetworkChecker.isConnected(mContext)) {
			Toast.makeText(
					mContext,
					JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"network_not_available"), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HttpNotification.NotificationData data = (HttpNotification.NotificationData) view
						.getTag();
				if (data.course_id == null) {
					// toast("No Course ID");
					return;
				}
				if (StringUtils.isEquals(data.notification_type,
						HttpNotification.Notification_type_ReView)) {
					Chunk chunk = new ChunkBiz(mContext)
							.getChunkByCode(data.course_id);
//					if (chunk == null) {
//						// toast("No Chunk");
//						return;
//					}
					startActivity(new Intent(mContext,
							UserRecordingActivity.class).putExtra(
							AppConst.BundleKeys.Chunk, chunk).putExtra(
							AppConst.BundleKeys.BELLAID,
							AppConst.CurrUserInfo.UserId).putExtra(AppConst.BundleKeys.Course_id_list, data.course_id));
				}
			}
		});
		progress = new ProgressDialog(FriendNotificationActivity.this);
		progress.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "friend_notification_loading"));
		progress.show();

		GetNotificationsTask task = new GetNotificationsTask(mContext,
				new PostExecuting<HttpNotification>() {
					@Override
					public void executing(HttpNotification result) {
						progress.dismiss();
						if (result != null && result.status != null) {
							mNotificationList = result.data;
							// notifyCache.put(result.data);
							mListAdapter = new NotificationListAdapter(
									mContext, mNotificationList);
							mListView.setAdapter(mListAdapter);
							if (mNotificationList != null
									&& mNotificationList.size() > 0) {
								List<String> IdsForRead = new ArrayList<String>();
								for (HttpNotification.NotificationData notification : mNotificationList) {
									if (!notification.has_read)
										IdsForRead
												.add(notification.notification_id);
								}
								readNotification(IdsForRead);
							}
						} else
							Toast.makeText(
									mContext,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mContext,
													"fail_to_get_result"),
									Toast.LENGTH_SHORT).show();
						;
					}
				});
		task.execute();
	}

	/** 读取通知 **/
	private void readNotification(List<String> notificationIds) {
		if (notificationIds == null || notificationIds.size() <= 0)
			return;
		ReadNotificationHttpRequest param = new ReadNotificationHttpRequest();
		param.bella_id = AppConst.CurrUserInfo.UserId;
		param.notification_ids = notificationIds;
		param.notification_type = "add_friend";
		ReadNotificationTask readTask = new ReadNotificationTask(mContext,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
					}
				});
		readTask.execute(new ReadNotificationHttpRequest[] { param });
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadAdWeb() {
		mAdsWebview.getSettings().setJavaScriptEnabled(true);
		mAdsWebview.getSettings()
				.setJavaScriptCanOpenWindowsAutomatically(true);
		mAdsWebview.getSettings().setCacheMode(
				WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mAdsWebview.setWebViewClient(new MyWebClient());
		GetAdsLinkTask getAdsTask = new GetAdsLinkTask(mContext,
				new PostExecuting<HttpADsAddress>() {
					@Override
					public void executing(HttpADsAddress result) {
						if (result != null && result.status != null
								&& result.status.equals("0")
								&& result.data != null) {
							mAdsWebview.getSettings().setCacheMode(
									WebSettings.LOAD_DEFAULT);
							mAdsWebview.loadUrl(result.data);
						}
					}
				});
		getAdsTask.execute();
	}

	class MyWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("call::")) {
				String phone = url.substring(6);
				call(phone);
			} else {
				link(url);
			}
			return true;
		}
	}

	private void link(String link) {
		// Add tracing
		TraceHelper.tracingADsCall(mContext);


		String site = link;
		Intent intent = new Intent("android.intent.action.VIEW",
				Uri.parse(site));
		startActivity(intent);

	}

	private void call(String phone) {
		// Add tracing
		TraceHelper.tracingADsUpsell(mContext);

		Intent intent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + phone));
		startActivity(intent);
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.IndexDashboardValues.pageNameValue,
							ContextDataMode.IndexDashboardValues.pageSiteSubSectionValue,
							ContextDataMode.IndexDashboardValues.pageSiteSectionValue,
							mContext);
			break;

		default:
			break;
		}
	}
}
