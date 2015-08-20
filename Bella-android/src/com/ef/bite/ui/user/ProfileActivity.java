package com.ef.bite.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.*;
import com.ef.bite.R;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.CountryBLL;
import com.ef.bite.business.GlobalConfigBLL;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.task.*;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpFriendCount;
import com.ef.bite.dataacces.mode.httpMode.HttpIsMyFriend;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.*;
import com.ef.bite.widget.RoundedImageView;
import com.ef.bite.widget.UserLevelView;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;

public class ProfileActivity extends BaseActivity {

	LinearLayout mContentLayout;
	TextView mName; // 姓名
	RoundedImageView mAvatar; // 头像
	ImageView mAvatarEdit; // 头像编辑图标
	ImageButton mDashBoard; // Teacher头像
	ImageButton mSetting; // 设置图标
	LinearLayout mScoreLevelInfoLayout;
	UserLevelView mLevel; // Level
	TextView mScoreXP; // 积分信息
	TextView mLevelInfo; // 升级提示信息
	TextView mChunksNum; // 掌握的Chunk数量
	TextView profile_friends;
	LinearLayout mPharaseLayout;
	LinearLayout mFriendLayout;
	TextView mFriendNum; // 收集到的礼物

    TextView country_name;
    ImageView country_flag;
    String country;
    RelativeLayout rl_country;

	ProfileModel mProfileModel; // 当前的profile信息
	GlobalConfigBLL configBLL;
	int friendNum = 0; // 朋友数量
//	private final static int Friends_My = 1;
//	private final static int Friends_Friends = 2;
//	private final static int Friends_Others = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Person_Profile)) {
			String json = bundle.getString(AppConst.BundleKeys.Person_Profile);
			mProfileModel = new ProfileModel();
			mProfileModel.parse(json);
		}
		mContentLayout = (LinearLayout) findViewById(R.id.profile_content_layout);
		mName = (TextView) findViewById(R.id.profile_person_name);
		mAvatar = (RoundedImageView) findViewById(R.id.profile_person_avatar);
		mAvatarEdit = (ImageView) findViewById(R.id.profile_avatar_edit);
		mDashBoard = (ImageButton) findViewById(R.id.profile_actionbar_home);
		mSetting = (ImageButton) findViewById(R.id.profile_actionbar_setting);
		mLevel = (UserLevelView) findViewById(R.id.profile_level);
		mLevelInfo = (TextView) findViewById(R.id.profile_level_up_info);
		mChunksNum = (TextView) findViewById(R.id.profile_chunks_number);
		mFriendNum = (TextView) findViewById(R.id.profile_objects_number);
		profile_friends = (TextView) findViewById(R.id.profile_friends);
		profile_friends.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "profile_friends"));
		mPharaseLayout = (LinearLayout) findViewById(R.id.profile_phrase_layout);
		mFriendLayout = (LinearLayout) findViewById(R.id.profile_friends_layout);
		mScoreLevelInfoLayout = (LinearLayout) findViewById(R.id.profile_score_level_layout);
		mScoreXP = (TextView) findViewById(R.id.profile_level_points);

        country_name = (TextView)this.findViewById(R.id.country_name);
        country_flag = (ImageView)this.findViewById(R.id.country_flag);
        rl_country = (RelativeLayout)this.findViewById(R.id.country);

        mScoreLevelInfoLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLevel
								.getLayoutParams();
						params.width = mLevel.getHeight();
						mLevel.setLayoutParams(params);
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							mScoreLevelInfoLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							mScoreLevelInfoLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						}
					}
				});
		configBLL = new GlobalConfigBLL(mContext);
		mDashBoard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDashBoardClick();
			}
		});
		// font setting
		FontHelper
				.applyFont(mContext, mContentLayout, FontHelper.FONT_OpenSans);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadingPersonInfo(mProfileModel);
	}

    @Override
    protected void onStart() {
        super.onStart();
        getCountryInfo(mProfileModel.UID);
    }


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.FriendsProfileMyValues.pageNameValue,
//				ContextDataMode.FriendsProfileMyValues.pageSiteSubSectionValue,
//				ContextDataMode.FriendsProfileMyValues.pageSiteSectionValue,
//				mContext);
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//                        ContextDataMode.FriendsProfileFriendsValues.pageNameValue,
//                        ContextDataMode.FriendsProfileFriendsValues.pageSiteSubSectionValue,
//                        ContextDataMode.FriendsProfileFriendsValues.pageSiteSectionValue,
//                        mContext);
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//                        ContextDataMode.FriendsProfileOthersValues.pageNameValue,
//                        ContextDataMode.FriendsProfileOthersValues.pageSiteSubSectionValue,
//                        ContextDataMode.FriendsProfileOthersValues.pageSiteSectionValue,
//                        mContext);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConst.RequestCode.SETTINGS
				&& resultCode == AppConst.ResultCode.LOG_OUT) {
			setResult(AppConst.ResultCode.LOG_OUT);
			finish();
		}
	}

	// loading person info
	private void loadingPersonInfo(final ProfileModel profile) {
		if (profile != null) {
			AvatarHelper.LoadLargeAvatar(mAvatar, profile.UID,
                    profile.AvatarPath);
			mName.setText(profile.Alias == null ? "" : profile.Alias);
			mFriendNum.setText(Integer.toString(profile.FriendsNum));
			friendNum = profile.FriendsNum;
			// 重新获得好友数量
			if (profile.FriendsNum <= 0) {
				getFriendCount(profile.UID, mFriendNum);
			}
			// 如果是本人
			if (profile.UID.equals(AppConst.CurrUserInfo.UserId)) {
				mSetting.setVisibility(View.VISIBLE);
				mDashBoard.setImageResource(R.drawable.actionbar_home);

//                country_flag.setVisibility(View.INVISIBLE);
//                country_name.setVisibility(View.INVISIBLE);

				mSetting.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onSettingClick();
					}
				});
				int userScore = new UserScoreBiz(mContext)
						.getUserScore();
				mScoreXP.setText(userScore + getString(R.string.profile_xp));
				mLevel.initialize(userScore);
				// mLevelInfo.setText(getString(R.string.profile_earn) + " "
				// + ScoreLevelHelper.getLevelUpScore(userScore) + " "
				// + getString(R.string.profile_more_to_level_up));
				mLevelInfo.setText(String.format(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext, "profile_earn"),
						ScoreLevelHelper.getLevelUpScore(userScore)));
				JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"profile_earn");
				mLevelInfo.setVisibility(View.VISIBLE);

				MobclickTracking.OmnitureTrack
						.AnalyticsTrackState(
								ContextDataMode.FriendsProfileMyValues.pageNameValue,
								ContextDataMode.FriendsProfileMyValues.pageSiteSubSectionValue,
								ContextDataMode.FriendsProfileMyValues.pageSiteSectionValue,
								mContext);
//				MobclickTracking.UmengTrack
//						.setPageStart(
//								ContextDataMode.FriendsProfileMyValues.pageNameValue,
//								ContextDataMode.FriendsProfileMyValues.pageSiteSubSectionValue,
//								ContextDataMode.FriendsProfileMyValues.pageSiteSectionValue,
//								mContext);
            } else { // 不是本人
				mSetting.setVisibility(View.GONE);
                mDashBoard.setImageResource(R.drawable.arrow_goback_black);
				mLevelInfo.setVisibility(View.GONE);
				mScoreXP.setText(profile.Score + "xp");
				mLevel.initialize(profile.Score);
				mPharaseLayout.setVisibility(View.GONE);

				if (profile.IsFriend) // 已经是朋友
				{
					mAvatarEdit
							.setImageResource(R.drawable.fried_already_added);
					MobclickTracking.OmnitureTrack
							.AnalyticsTrackState(
									ContextDataMode.FriendsProfileFriendsValues.pageNameValue,
									ContextDataMode.FriendsProfileFriendsValues.pageSiteSubSectionValue,
									ContextDataMode.FriendsProfileFriendsValues.pageSiteSectionValue,
									mContext);
//					MobclickTracking.UmengTrack
//							.setPageStart(
//									ContextDataMode.FriendsProfileFriendsValues.pageNameValue,
//									ContextDataMode.FriendsProfileFriendsValues.pageSiteSubSectionValue,
//									ContextDataMode.FriendsProfileFriendsValues.pageSiteSectionValue,
//									mContext);
				} else {
					mAvatarEdit
							.setImageResource(R.drawable.leaderboard_invite_friend);
					checkIsFriend(mAvatarEdit, profile.UID);
					MobclickTracking.OmnitureTrack
							.AnalyticsTrackState(
									ContextDataMode.FriendsProfileOthersValues.pageNameValue,
									ContextDataMode.FriendsProfileOthersValues.pageSiteSubSectionValue,
									ContextDataMode.FriendsProfileOthersValues.pageSiteSectionValue,
									mContext);
//					MobclickTracking.UmengTrack
//							.setPageStart(
//									ContextDataMode.FriendsProfileOthersValues.pageNameValue,
//									ContextDataMode.FriendsProfileOthersValues.pageSiteSubSectionValue,
//									ContextDataMode.FriendsProfileOthersValues.pageSiteSectionValue,
//									mContext);
				}
			}
			// 查看朋友列表
			mFriendLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (friendNum <= 0) {
                        Toast.makeText(mContext,
                                getString(R.string.profile_no_friend),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(mContext,
                            FriendListActivity.class);
                    if (mProfileModel != null)
                        intent.putExtra(
                                AppConst.BundleKeys.Get_Friend_List_Profile,
                                mProfileModel.toJson());
                    startActivity(intent);
                }
            });
		}
	}

    private void getCountryInfo(String id) {
        GetProfileTask profileTask = new GetProfileTask(mContext,
                new PostExecuting<HttpProfile>() {
                    @Override
                    public void executing(HttpProfile result) {
                        if (result != null && result.status != null && result.status.equals("0") && result.data != null) {
                            country = result.data.market_code;

                            String path = android.os.Environment.getExternalStorageDirectory()
                                    + File.separator + AppConst.CacheKeys.RootStorage + File.separator
                                    + AppConst.CacheKeys.Storage_Language + File.separator + "country"
                                    + File.separator;

                            rl_country.setVisibility(View.VISIBLE);
                            country_name.setText(CountryBLL.getLocalCountry(country, path, mContext));
                            country_flag.setImageBitmap(CountryBLL.getLoacalBitmap(country, path));
                        }
                    }
                });
        profileTask.execute(id);
    }

	/** 打开设置页面 **/
	private void onSettingClick() {
		startActivityForResult(new Intent(mContext, SettingsActivity.class),
				AppConst.RequestCode.SETTINGS);
		overridePendingTransition(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
	}

	private void onDashBoardClick() {
		finish();
	}

	/** 检查是否是好友 **/
	public void checkIsFriend(final ImageView addImg, final String friend_id) {
		CheckIsMyFriendTask checkTask = new CheckIsMyFriendTask(mContext,
				new PostExecuting<HttpIsMyFriend>() {
					@Override
					public void executing(HttpIsMyFriend result) {
						if (result == null || result.status == null) {
							return;
						}
						if (!result.status.equals("0")) {
							Toast.makeText(mContext, result.message,
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (result.isMyFriend) {
							addImg.setImageResource(R.drawable.friend_added);
							addImg.setClickable(false);
						} else {
							addImg.setImageResource(R.drawable.friend_to_add);
							addImg.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									addFriend(addImg, friend_id);
									MobclickTracking.OmnitureTrack
											.ActionProfileAddFriends();
								}
							});
						}
					}
				});
		checkTask.execute(new String[] { AppConst.CurrUserInfo.UserId,
				friend_id });
	}

	/** 添加某用户为好友 **/
	public void addFriend(final ImageView addImg, final String friend_id) {
		AddFriendTask addTask = new AddFriendTask(mContext,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
						if (result == null || result.status == null) {
							Toast.makeText(
									mContext,
									mContext.getString(R.string.add_frend_failed),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!result.status.equals("0")) {
							Toast.makeText(
									mContext,
									mContext.getString(R.string.add_frend_failed)
											+ " " + result.message,
									Toast.LENGTH_SHORT).show();
							return;
						}
						Toast.makeText(
								mContext,
								mContext.getString(R.string.add_friend_success),
								Toast.LENGTH_SHORT).show();
						addImg.setImageResource(R.drawable.friend_added);
						addImg.setClickable(false);
					}
				});
		addTask.execute(new String[] { friend_id });
	}

	/**
	 * 获得朋友数量
	 * 
	 * @param uid
	 * @param friendCountText
	 */
	public void getFriendCount(String uid, final TextView friendCountText) {
		GetFriendCountTask task = new GetFriendCountTask(mContext,
				new PostExecuting<HttpFriendCount>() {
					@Override
					public void executing(HttpFriendCount result) {
						if (result != null && result.status != null
								&& result.status.equals("0")) {
							friendCountText.setText(Integer
									.toString(result.data));
							friendNum = result.data;
						}
					}
				});
		task.execute(new String[] { uid });
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:

			break;

		default:
			break;
		}
	}
}
