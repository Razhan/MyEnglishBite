package com.ef.bite.ui.record;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ReviewHorizontalListAdapter;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.action.UserProfileOpenAction;
import com.ef.bite.business.task.*;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.UserRecord;
import com.ef.bite.dataacces.mode.VoiceReviewrs;
import com.ef.bite.dataacces.mode.httpMode.*;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.popup.DeleteRecordingPopWindow;
import com.ef.bite.ui.popup.ReviewActivityPopWindow;
import com.ef.bite.ui.popup.SharePopWindow;
import com.ef.bite.utils.*;
import com.ef.bite.widget.HorizontalListView;
import com.ef.bite.widget.RoundedImageView;
import com.ef.bite.widget.UserLevelView;

@SuppressLint({ "CutPasteId", "SetJavaScriptEnabled" })
public class UserRecordingActivity extends BaseActivity implements
		OnClickListener {

	private Chunk mChunkModel;

	private MediaPlayer mediaPlayer;

	private UserRecord userRecord;
	private List<VoiceReviewrs> voiceReviewrs;
	private ImageButton goback;
	private WebView playVoice;
	private LinearLayout rightLight;
	private LinearLayout leftLight;
	private ImageView deleteVoice;
	private TextView nameText;
	private TextView courseName;
	private TextView timeText;
	private TextView reminderText;
	private TextView likeNum;
	private TextView reviewCount;
	private TextView mNullTitle;
	private Button mNullBtn;
	private Button mRateOthers;
	private Button mRefresh;
	private Button shareBtn;
	private RelativeLayout mInviteFriends;
	private RoundedImageView userrecording_avatar;
	private UserLevelView uLevelView;
	private HorizontalListView reviewVoiceList;
	private ReviewHorizontalListAdapter reviewVoiceListAdapter;
	private LinearLayout userrecording_chunk_all;
	private LinearLayout userrecording_chunk_null;
	private RelativeLayout chunk_bottom_go_ahead_lay;

	private ProgressDialog progressDialog;

	// private String BELLAID = "9272bfef-36e9-411d-b120-398761304e12";
	// private String COURESID = "b9913cf9-ac36-446c-8bc7-8a169bcab3e7";

	private String BELLAID = "";
	private String COURESID = "";

	private final String DIRECTION_CURRENT = "current";
	private final String DIRECTION_NEXT = "next";
	private final String DIRECTION_PREVIOUS = "previous";
	private final int VOICEREVIEWERS_START = 0;
	private final int VOICEREVIEWERS_ROWS = 1000;
	private ProfileModel data;
	private HttpProfile httpProfile;
	private HttpFriendCount httpFriendCount;

	private int HideBottomLay = 1; // 0:隱藏 1:現實

	private String URL = AppConst.EFAPIs.BaseHost
			+ "/bella/webview/en/index.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userrecording_chunk);

		mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);
		HideBottomLay = getIntent().getExtras().getInt(
				AppConst.BundleKeys.Hide_Bottom_Lay, 0);

		BELLAID = getIntent().getExtras().getString(
				AppConst.BundleKeys.BELLAID, AppConst.CurrUserInfo.UserId);
//		COURESID = mChunkModel.getChunkCode();

		if(null == mChunkModel){
			COURESID = getIntent().getExtras().getString(AppConst.BundleKeys.Course_id_list);
		} else{
			COURESID = mChunkModel.getChunkCode();
		}

		progressDialog = new ProgressDialog(UserRecordingActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "resource_loading"));
		initView();
		initOnclickListener();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		playVoice.loadUrl(URL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		playVoice.loadUrl("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		userrecording_chunk_all = (LinearLayout) this
				.findViewById(R.id.userrecording_chunk_all);
		userrecording_chunk_null = (LinearLayout) this
				.findViewById(R.id.userrecording_chunk_null);
		chunk_bottom_go_ahead_lay = (RelativeLayout) this
				.findViewById(R.id.chunk_bottom_go_ahead_lay);
		nameText = (TextView) this.findViewById(R.id.userrecording_name_text);
		FontHelper.applyFont(mContext, nameText, FontHelper.FONT_Museo300);
		userrecording_avatar = (RoundedImageView) this
				.findViewById(R.id.userrecording_avatar);
		courseName = (TextView) this
				.findViewById(R.id.userrecording_coursename);
		FontHelper.applyFont(mContext, courseName, FontHelper.FONT_Museo300);
		playVoice = (WebView) this.findViewById(R.id.userrecording_playvoice);
		playVoice.setVerticalScrollBarEnabled(false);  //取消Vertical ScrollBar显示
		playVoice.setHorizontalScrollBarEnabled(false); //取消Horizontal ScrollBar显示

		leftLight = (LinearLayout) this
				.findViewById(R.id.userrecording_left_light);
		rightLight = (LinearLayout) this
				.findViewById(R.id.userrecording_right_light);
		reviewVoiceList = (HorizontalListView) this
				.findViewById(R.id.review_voice_list);
		timeText = (TextView) this.findViewById(R.id.time_textview);
		reminderText = (TextView) this.findViewById(R.id.reminder_textview);
		FontHelper.applyFont(mContext, reminderText, FontHelper.FONT_Museo300);
		likeNum = (TextView) this.findViewById(R.id.like_num);
		FontHelper.applyFont(mContext, likeNum, FontHelper.FONT_OpenSans);
		reviewCount = (TextView) this.findViewById(R.id.review_count);
		deleteVoice = (ImageView) this.findViewById(R.id.userrecording_delete);
		uLevelView = (UserLevelView) this
				.findViewById(R.id.chunk_actionbar_level);
		goback = (ImageButton) this.findViewById(R.id.chunk_actionbar_goback);
		goback.setImageResource(R.drawable.arrow_goback_black);
		mRateOthers = (Button) this.findViewById(R.id.rate_others_btn);
		shareBtn = (Button) this.findViewById(R.id.btn_share);
		mRateOthers.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "user_recoding_rate_others"));
		mInviteFriends = (RelativeLayout) this.findViewById(R.id.invite_friends_btn);
		((TextView)findViewById(R.id.tv_btn_invite)).setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "chunk_level_up_share"));
		mRefresh = (Button) this.findViewById(R.id.userrecording_chunk_btn);

		mNullTitle = (TextView) this
				.findViewById(R.id.userrecording_chunk_title);
		mNullTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "ratingvoice_fail_to_get_result"));
		mNullBtn = (Button) this.findViewById(R.id.userrecording_chunk_btn);
		mNullBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"ratingvoice_refresh"));

		if (HideBottomLay == 0) {
			chunk_bottom_go_ahead_lay.setVisibility(View.GONE);
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.NoUserRecordingValue.pageNameValue,
							ContextDataMode.NoUserRecordingValue.pageSiteSubSectionValue,
							ContextDataMode.NoUserRecordingValue.pageSiteSectionValue,
							mContext);
		} else {
			chunk_bottom_go_ahead_lay.setVisibility(View.VISIBLE);
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.HasUserRecordingValue.pageNameValue,
							ContextDataMode.HasUserRecordingValue.pageSiteSubSectionValue,
							ContextDataMode.HasUserRecordingValue.pageSiteSectionValue,
							mContext);
		}

		playVoice.getSettings().setJavaScriptEnabled(true);
		playVoice.addJavascriptInterface(this, "recording");
		// playVoice.loadUrl(URL);

		UserScoreBiz userScoreBiz = new UserScoreBiz(mContext);
		int uScore = userScoreBiz.getUserScore();
		uLevelView.initialize(uScore);

		if (!BELLAID.equals(AppConst.CurrUserInfo.UserId)) {
			deleteVoice.setVisibility(View.GONE);
		}
	}

/*	@JavascriptInterface
	public void startDisplay(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

			}
		});
	}*/

	private void initOnclickListener() {
		leftLight.setOnClickListener(this);
		rightLight.setOnClickListener(this);
		deleteVoice.setOnClickListener(this);
		goback.setOnClickListener(this);
		mRateOthers.setOnClickListener(this);
		mRefresh.setOnClickListener(this);
		mInviteFriends.setOnClickListener(this);
		shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// tracking
				MobclickTracking.OmnitureTrack
						.AnalyticsTrackState(
								ContextDataMode.UserRecordingShareValue.pageNameValue,
								ContextDataMode.UserRecordingShareValue.pageSiteSubSectionValue,
								ContextDataMode.UserRecordingShareValue.pageSiteSectionValue,
								mContext);

				getVoiceShare();
			}
		});
	}

	private void initData() {
		progressDialog.show();
		HttpUserRecordingRequest httpuserrecordingrequest = new HttpUserRecordingRequest();
		httpuserrecordingrequest.setBella_id(BELLAID);
//		httpuserrecordingrequest.setCourse_id(mChunkModel.getChunkCode());
        httpuserrecordingrequest.setCourse_id(COURESID);

		httpuserrecordingrequest.setSystem(AppConst.GlobalConfig.OS);
		httpuserrecordingrequest.setRows(1);
		httpuserrecordingrequest.setDirection(DIRECTION_CURRENT);
		GetUserRecordingTask getUserRecordingTask = new GetUserRecordingTask(
				mContext, new PostExecuting<HttpUserRecordingResponse>() {

					@Override
					public void executing(HttpUserRecordingResponse result) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						if (result != null && result.status.equals("0")
								&& result.getData() != null) {
							userrecording_chunk_all.setVisibility(View.VISIBLE);
							userrecording_chunk_null.setVisibility(View.GONE);
							userRecord = result.getData();
							voiceReviewrs = result.getData()
									.getVoice_reviewers();
							if (voiceReviewrs.size() > 0) {
								loadvoicereviewers(userRecord
										.getVoice_file_name());
							}
							setViewData();
						} else {
							// goback.setOnClickListener(UserRecordingActivity.this);
							// userrecording_chunk_null
							// .setVisibility(View.VISIBLE);
							// userrecording_chunk_all.setVisibility(View.GONE);
							finish();
						}
					}
				});

		getUserRecordingTask.execute(httpuserrecordingrequest);
	}

	private void setViewData() {
		nameText.setText(StringUtils.nullStrToEmpty(userRecord.getAlias()));
		AvatarHelper.LoadAvatar(userrecording_avatar,
				AppConst.CurrUserInfo.UserId, userRecord.getAvatar_url());
		courseName.setText("..." + userRecord.getCourse_name() + "...");

		timeText.setText(String.valueOf(userRecord.getVoice_length()));
		if (voiceReviewrs.size() > 0) {
			reminderText.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "rated_by_list"));
			reviewVoiceList.setVisibility(View.VISIBLE);
		} else {
			reminderText.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "user_recording_no_rating")
					+ JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"rate_invite_friends"));
			reviewVoiceList.setVisibility(View.INVISIBLE);
		}

		try {
			likeNum.setText((int) (Double.parseDouble(StringUtils
					.isBlank(userRecord.getLike_percentage()) ? "0"
					: userRecord.getLike_percentage()) * 100)
					+ "%");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if (userRecord.getReview_count() == 0) {
			reviewCount.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "user_recording_0rating"));
		} else if (userRecord.getReview_count() == 1) {
			reviewCount.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "user_recording_1rating"));
		} else {
			reviewCount.setText(String.format(
					JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"user_recording_ratings"), userRecord
							.getReview_count()));
		}

		playVoice.loadUrl(URL);
		playVoice.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				view.loadUrl("javascript:getAudioSrc('"
						+ userRecord.getVoice_url() + "','"
						+ userRecord.getVoice_length() + "')");
			}
		});
	}

	private void loadvoicereviewers(String voice_file_name) {
		HttpQueryVoiceReviewersRequest httpQueryVoiceReviewersRequest = new HttpQueryVoiceReviewersRequest();
		httpQueryVoiceReviewersRequest.setVoice_file_name(voice_file_name);
		httpQueryVoiceReviewersRequest.setStart(VOICEREVIEWERS_START);
		httpQueryVoiceReviewersRequest.setRows(VOICEREVIEWERS_ROWS);
		GetQueryVoiceReviewersTask getVoiceReviewersTask = new GetQueryVoiceReviewersTask(
				mContext, new PostExecuting<HttpQueryVoiceReviewersResponse>() {

					@Override
					public void executing(HttpQueryVoiceReviewersResponse result) {
						// TODO Auto-generated
						// method stub
						if (result != null && !result.getData().isEmpty()) {
							// voiceReviewrs.addAll(result.getData());
							voiceReviewrs = result.getData();
						}

						reviewVoiceListAdapter = new ReviewHorizontalListAdapter(
								mContext, voiceReviewrs);
						reviewVoiceList.setAdapter(reviewVoiceListAdapter);
						reviewVoiceList
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											final int position, long id) {
										progressDialog.show();
										GetProfileTask getProfileTask = new GetProfileTask(
												mContext,
												new PostExecuting<HttpProfile>() {

													@Override
													public void executing(
															HttpProfile result) {
														if (result != null
																&& result.data != null) {
															httpProfile = result;
															GetFriendCountTask getFriendCountTask = new GetFriendCountTask(
																	mContext,
																	new PostExecuting<HttpFriendCount>() {
																		@Override
																		public void executing(
																				HttpFriendCount result) {
																			if (result != null) {
																				httpFriendCount = result;
																				CheckIsMyFriendTask checkIsMyFriendTask = new CheckIsMyFriendTask(
																						mContext,
																						new PostExecuting<HttpIsMyFriend>() {

																							@Override
																							public void executing(
																									HttpIsMyFriend result) {
																								if (result != null) {
																									progressDialog
																											.dismiss();
																									data = new ProfileModel(
																											voiceReviewrs
																													.get(position)
																													.getBella_id(),
																											httpProfile.data.alias,
																											httpProfile.data.avatar_url,
																											httpProfile.data.score,
																											0,
																											httpFriendCount.data,
																											result.isMyFriend);
																									new UserProfileOpenAction()
																											.open(mContext,
																													data);
																								}
																							}
																						});
																				checkIsMyFriendTask
																						.execute(new String[] {
																								AppConst.CurrUserInfo.UserId,
																								voiceReviewrs
																										.get(position)
																										.getBella_id() });
																			}
																		}
																	});
															getFriendCountTask
																	.execute(voiceReviewrs
																			.get(position)
																			.getBella_id());
														}
													}
												});
										getProfileTask.execute(voiceReviewrs
												.get(position).getBella_id());
									}
								});
					}
				});
		getVoiceReviewersTask.execute(httpQueryVoiceReviewersRequest);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case R.id.userrecording_playvoice:
		// if (!StringUtils.isBlank(userRecord.getVoice_url())) {
		// mediaPlayer = new MediaPlayer();
		// try {
		// mediaPlayer.setDataSource(userRecord.getVoice_url());
		// mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
		//
		// @Override
		// public void onPrepared(MediaPlayer mp) {
		// // TODO Auto-generated method stub
		// mediaPlayer.start();
		// }
		// });
		// mediaPlayer.prepare();
		// mediaPlayer.setOnErrorListener(new OnErrorListener() {
		//
		// @Override
		// public boolean onError(MediaPlayer mp, int what,
		// int extra) {
		// // TODO Auto-generated method stub
		// mp.reset();
		// return false;
		// }
		// });
		// } catch (IllegalArgumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalStateException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// break;

		case R.id.userrecording_left_light:
			HttpUserRecordingRequest userrecord_previous = new HttpUserRecordingRequest();
			userrecord_previous.setBella_id(AppConst.CurrUserInfo.UserId);
			userrecord_previous.setCourse_id(COURESID);
			userrecord_previous.setSystem("android");
			userrecord_previous.setRows(1);
			userrecord_previous.setDirection(DIRECTION_PREVIOUS);
			GetUserRecordingTask getUserRecordingTask_previous = new GetUserRecordingTask(
					mContext, new PostExecuting<HttpUserRecordingResponse>() {

						@Override
						public void executing(HttpUserRecordingResponse result) {
							progressDialog.dismiss();
							if (result != null && result.getData() != null) {
								userRecord = result.getData();
								voiceReviewrs = result.getData()
										.getVoice_reviewers();
								if (voiceReviewrs.size() > 0) {
									loadvoicereviewers(userRecord
											.getVoice_file_name());
								}
								setViewData();
								COURESID = userRecord.getCourse_id();
							}
						}
					});

			getUserRecordingTask_previous.execute(userrecord_previous);
			progressDialog.show();
			break;

		case R.id.userrecording_right_light:
			HttpUserRecordingRequest userrecord_next = new HttpUserRecordingRequest();
			userrecord_next.setBella_id(AppConst.CurrUserInfo.UserId);
			userrecord_next.setCourse_id(COURESID);
			userrecord_next.setSystem("android");
			userrecord_next.setRows(1);
			userrecord_next.setDirection(DIRECTION_NEXT);
			GetUserRecordingTask getUserRecordingTask_next = new GetUserRecordingTask(
					mContext, new PostExecuting<HttpUserRecordingResponse>() {

						@Override
						public void executing(HttpUserRecordingResponse result) {
							progressDialog.dismiss();
							if (result != null && result.getData() != null
									&& StringUtils.isEquals(result.status, "0")) {
								userRecord = result.getData();
								voiceReviewrs = result.getData()
										.getVoice_reviewers();
								if (voiceReviewrs.size() > 0) {
									loadvoicereviewers(userRecord
											.getVoice_file_name());
								}
								setViewData();
								COURESID = userRecord.getCourse_id();
							} else {
								ReviewActivityPopWindow reviewActivityPopWindow = new ReviewActivityPopWindow(
										UserRecordingActivity.this);
								reviewActivityPopWindow
										.setOnNextClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												finish();
											}
										});
								reviewActivityPopWindow.open();
							}
						}
					});

			getUserRecordingTask_next.execute(userrecord_next);
			progressDialog.show();
			break;

		case R.id.userrecording_delete:
			DeleteRecordingPopWindow deleteRecordingPopWindow = new DeleteRecordingPopWindow(
					UserRecordingActivity.this);
			deleteRecordingPopWindow.setOnDeleteListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HttpVoiceDeletRequest voiceDeletRequest = new HttpVoiceDeletRequest();
					voiceDeletRequest.setBella_id(AppConst.CurrUserInfo.UserId);
					voiceDeletRequest.setCourse_id(COURESID);
					PostVoiceDeleteTask postVoiceDeleteTask = new PostVoiceDeleteTask(
							mContext, new PostExecuting<Boolean>() {
								@Override
								public void executing(Boolean result) {
									// TODO Auto-generated method stub
									// initData();
									finish();
								}
							});
					postVoiceDeleteTask.execute(voiceDeletRequest);
				}
			});
			deleteRecordingPopWindow.open();
			break;

		case R.id.chunk_actionbar_goback:
			finish();
			break;
		case R.id.rate_others_btn:
			Intent intent = new Intent(this, ReviewActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Hide_Bottom_Lay, HideBottomLay);
            intent.putExtra(AppConst.BundleKeys.Course_id_list, COURESID);
			startActivity(intent);
			finish();
			break;
		case R.id.userrecording_chunk_btn:
			initData();
			break;
		case R.id.invite_friends_btn:

			break;
		}
	}

	private void getVoiceShare() {
		if (userRecord == null) {
			return;
		}
		GetVoiceShareTask shareTask = new GetVoiceShareTask(mContext,
				new PostExecuting<HttpVoiceShareRespone>() {
					@Override
					public void executing(HttpVoiceShareRespone respone) {
						if (respone == null) {
							return;
						}
						// Share
						openShareWindow(respone.data.message, respone.data.url);
					}
				});
		// Packaging request
		HttpVoiceShareRequest request = new HttpVoiceShareRequest();
		request.setBella_id(AppConst.CurrUserInfo.UserId);
		request.setCourse_id(userRecord.getCourse_id());
		request.setVoice_file_name(userRecord.getVoice_file_name());
		request.setClient_culture_code(AppLanguageHelper
				.getSystemLaunguage(mContext));
		request.setSystem(AppConst.GlobalConfig.OS);

		shareTask.execute(request);
	}

	/**
	 * Open popup window to share voice
	 * 
	 * @param title
	 * @param content
	 */
	private void openShareWindow(String title, String content) {
		SharePopWindow sharePopWindow = new SharePopWindow(this, title, content);
		sharePopWindow.open();
	}
}
