package com.ef.bite.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.ef.bite.AppConst;
import com.ef.bite.AppSession;
import com.ef.bite.R;
import com.ef.bite.business.LocalDashboardBLL;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.VersionUpdateBLL;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.*;
import com.ef.bite.dataacces.AchievementCache;
import com.ef.bite.dataacces.mode.PushData;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.DashBoardFriendView;
import com.ef.bite.ui.user.FriendNotificationActivity;
import com.ef.bite.ui.user.LeaderBoardActivity;
import com.ef.bite.ui.user.SettingsActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MainScreen for dashboard
 */
public class MainActivity extends BaseActivity {
	private ImageButton mSettingBtn;
	private RelativeLayout inboxView;
	private LinearLayout mFriendLayout; // 底部朋友layout
	private FriendsContainer mFriendContainer;
	private ImageButton mFirendMore;
	private TextView home_screen_leaderboard_title;
	private LocalDashboardBLL dashboardBLL;
	private UserScoreBiz mScoreBiz;
	private HttpGetFriendData currentUserSelected;
	private List<HttpGetFriendData> mFriendList = new ArrayList<HttpGetFriendData>();
	private int masteredChunkNum;
	// 通知
	private RelativeLayout mNotificationLayout; // 通知的layout
	private TextView mNotificationCount; // 通知未读数量

	private FragmentManager mFragmentManager;

	private List<Fragment> fragments = new ArrayList<Fragment>();

	private LinearLayout mPhraseLayout;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_home_screen);
		setupViews();
		saveUserProfileForPush();
//		showUpdateDialog();
	}

	private void init(){
		dashboardBLL = new LocalDashboardBLL(mContext);
		mScoreBiz = new UserScoreBiz(mContext);
	}

	private void setupViews() {
		mSettingBtn = (ImageButton) findViewById(R.id.home_screen_setting);
		inboxView = (RelativeLayout) findViewById(R.id.inbox_view);
		mFriendLayout = (LinearLayout) findViewById(R.id.home_screen_friend_layout);
		mFirendMore = (ImageButton) findViewById(R.id.home_screen_friend_more);
		mFriendContainer = new FriendsContainer();
		mNotificationLayout = (RelativeLayout) findViewById(R.id.home_screen_notification_layout);
		mNotificationCount = (TextView) findViewById(R.id.home_screen_notification_count);
		mPhraseLayout = (LinearLayout) findViewById(R.id.home_screen_chunk_layout);
		home_screen_leaderboard_title = (TextView) findViewById(R.id.home_screen_leaderboard_title);
		home_screen_leaderboard_title.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext,
						"home_screen_leaderboard_title"));
//		FontHelper.applyFont(mContext, layout, FontHelper.FONT_Museo300);
		// 好友通知
		inboxView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						FriendNotificationActivity.class));
			}
		});

		mFirendMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, LeaderBoardActivity.class));
				MainActivity.this.overridePendingTransition(
						R.anim.activity_in_from_right,
						R.anim.activity_out_to_left);


			}
		});
		mSettingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SettingsActivity.class));
			}
		});
		mPhraseLayout = (LinearLayout)findViewById(R.id.home_screen_chunk_layout);
		initFragment();
		switchFragment(3);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v("EnglishBite","Bella_id: "+AppConst.CurrUserInfo.UserId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateDashboard(dashboardCache.load());
		postUserAchievement();
	}

	private void  initFragment() {
		fragments.clear();
		fragments.add(new LearnFragment());
		fragments.add(new PracticeFragment());
		fragments.add(new AllDoneFragment());
	}

	private void switchFragment(int index) {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			if (i == index) {
				if (!fragment.isAdded()) {
					ft.add(R.id.home_screen_chunk_layout, fragment);
				}
				ft.show(fragment);//控制所选fragment显示
				currentIndex = index;
			} else {
				if (fragment.isAdded()) {
					ft.hide(fragment);
				}else {
					ft.add(R.id.home_screen_chunk_layout,fragment);
				}
			}
		}
		ft.commitAllowingStateLoss();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == AppConst.RequestCode.EF_LOGIN
//				&& resultCode == AppConst.ResultCode.APP_EXIT) {
//			finish();
//		} else if (requestCode == AppConst.RequestCode.WALKTHROUGH
//				&& resultCode == AppConst.ResultCode.APP_EXIT) {
//			finish();
//		}
//	}

	private long lastExitTime = 0;
	@Override
	public void onBackPressed() {
		long nowTime = new Date().getTime();
		if (nowTime - lastExitTime > 1000) { // 1秒
			lastExitTime = nowTime;
			Toast.makeText(
					getBaseContext(),
					JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"app_exist_click_again"), Toast.LENGTH_SHORT)
					.show();

			if (AppConst.CurrUserInfo.IsLogin == true) {

			}
		} else {
			AppConst.CurrUserInfo.IsLogin = false;
			AppSession.getInstance().exit();
		}
	}




	/**
	 * execute action with different type
	 */
	private void executePushAction() {
		PushData pushData = getPushData();
		if (pushData == null) {
			return;
		}
		switch (pushData.getType()) {
			case new_lesson:
				((BaseDashboardFragment)fragments.get(currentIndex)).getmLearnPhraseLayout().performClick();
				break;
			case new_rehearsal:
				((BaseDashboardFragment)fragments.get(currentIndex)).getmPracticePhraseLayout().performClick();
				break;
			case recording_rate:
				inboxView.performClick();
				break;
			default:
				break;
		}
	}

	/**
	 * send user id to Parse Server
	 */
	public void saveUserProfileForPush() {
		if (!AppConst.CurrUserInfo.IsLogin) {
			return;
		}
		ParseInstallation.getCurrentInstallation().put("bella_id",
				AppConst.CurrUserInfo.UserId);
		ParseInstallation.getCurrentInstallation().put("device_id",
				AppConst.GlobalConfig.DeviceID);
		ParseInstallation.getCurrentInstallation().saveInBackground(
				new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e != null) {
							// Toast toast = Toast.makeText(
							// getApplicationContext(),
							// "Push register failed", Toast.LENGTH_SHORT);
							// toast.show();
							Log.d("com.parse.push",
									"save error:" + e.getMessage());
							e.printStackTrace();
						} else {
							Log.d("com.parse.push", "save done:");
						}
					}
				});
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
							mContext);
					View view = friendView.getView(mFriendList.get(index));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							0, LinearLayout.LayoutParams.MATCH_PARENT);
					params.weight = 1;
					friendLayout.addView(view, params);
					friendView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (friendView.isSelected() == false) {
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
						mContext);
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
			new UserProfileOpenAction().open(mContext, profile);

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
						if ( httpDashboard != null && httpDashboard.data !=null) {
							dashboardCache.save(httpDashboard);
							updateDashboard(httpDashboard);
							executePushAction();
						}
					}
				});
		task.execute();
	}

	private void postUserAchievement() {
		AchievementCache.getInstance().postCache(new AchievementCache.OnFinishListener() {
			@Override
			public void doOnfinish(boolean result) {
				getDashboard();
			}
		});
	}



	private void updateDashboard(HttpDashboard httpDashboard){
		if(httpDashboard == null || httpDashboard.data==null){
			return;
		}
		mNotificationLayout.setVisibility(httpDashboard.data.inbox_count>0?View.VISIBLE:View.GONE);
		mNotificationCount.setText(httpDashboard.data.inbox_count + "");
		masteredChunkNum=httpDashboard.data.master_count;
		updateFragment(httpDashboard);
		//show friends list
		convertFriends(httpDashboard.data.rank_friends);
		mFriendContainer.initialize(mFriendLayout);

	}

	private void updateFragment(HttpDashboard httpDashboard){

		//switch fragment by states
		if(httpDashboard.data.new_lessons.size()>0){
			switchFragment(0);
		} else if (httpDashboard.data.new_rehearsals.size() >0){
			switchFragment(1);
		} else {
			switchFragment(2);
		}

		List<Fragment> fragmentList =this.getSupportFragmentManager().getFragments();
		if(fragmentList!=null){
			for (Fragment fragment : fragmentList) {
				((BaseDashboardFragment) fragment).update(httpDashboard);
			}
		}
	}


	private void convertFriends(List<HttpDashboard.friend> friends){
		mFriendList.clear();
		for (HttpDashboard.friend friend : friends) {
			HttpGetFriendData friendData = new HttpGetFriendData();
			friendData.alias = friend.alias;
			friendData.avatar = friend.avatar_url;
			friendData.bella_id = friend.bella_id;
			friendData.friend_count = friend.friend_count;
			friendData.rank = friend.rank;
			friendData.score = friend.score;
			mFriendList.add(friendData);
		}

	}

	private void onStartExecute(HttpDashboard httpDashboard){
		if(httpDashboard.data.new_lessons.size()>0){

		} else if (httpDashboard.data.new_rehearsals.size() >0){

		} else {

		}
	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				final String appName = "EnglishBite";
				final String downUrl = "http://gdown.baidu.com/data/wisegame/bd47bd249440eb5f/shenmiaotaowang2.apk";

				Intent intent = new Intent(MainActivity.this,VersionUpdateBLL.class);
				intent.putExtra("Key_App_Name",appName);
				intent.putExtra("Key_Down_Url", downUrl);
				startService(intent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}

}
