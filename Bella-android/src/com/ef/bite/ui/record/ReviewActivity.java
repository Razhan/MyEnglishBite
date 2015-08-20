package com.ef.bite.ui.record;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import cn.trinea.android.common.util.ToastUtils;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.CountryBLL;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.task.*;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.ReviewVoice;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.dataacces.mode.httpMode.HttpReviewVoiceRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpReviewVoiceResponse;
import com.ef.bite.dataacces.mode.httpMode.HttpVoiceRequest;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.popup.QuitPracticePopWindow;
import com.ef.bite.ui.popup.ReviewActivityPopWindow;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@SuppressWarnings("unused")
@SuppressLint({ "ShowToast", "SetJavaScriptEnabled" })
public class ReviewActivity extends BaseActivity implements
		View.OnClickListener {
	private static int SCORE=10;
	private int ReviewActivity_Status = 0;// 0:listen 1:like_soso
	private ImageView likeimage;
	private ImageView sosoimage;
	private WebView voiceimage;
	private ImageView reportvoice;
	private TextView family_given_name;
	private TextView review_chunkname;
	private TextView voicetime;
	private TextView voiceReminder;
	private TextView scores_up;
	private TextView scores_down;
	private TextView mNullMessage;
	private TextView likeBtn;
	private TextView dislikeBtn;
	private Button mNullBtn;
	private RoundedImageView voiceavater;
	private PagesIndicator reviewvoice_pageIndicator;
	private GifImageView gifvoice;
	private ImageButton goback;
	private RelativeLayout like_soso_layout;
	private LinearLayout review_chunk_all;
	private LinearLayout review_chunk_null;
	private UserLevelView uLevelView;
	private ProgressDialog progressDialog;

	private Chunk mChunkModel;
	private List<ReviewVoice> reviewVoices;
	private int reviewvoiceSum;
	private int VoiceNum = 0;
	private int voiceTimes;
	private boolean audio_status = true;
	private boolean isDataNull = false;
	private String audioUrl;
	private String voicefilename;
	private int HideBottomLay = 1; // 0:隱藏 1:現實
	private int refresh = 0;
	private boolean isLearning;

    private TextView country_name;
    private ImageView country_flag;
    private String country;
    private RelativeLayout rl_country;


	// 臨時參數
	// private String BELLAID = "9272bfef-36e9-411d-b120-398761304e12";
	// private String COURESID = "b9913cf9-ac36-446c-8bc7-8a169bcab3e7";

	private String BELLAID = "";
	private String COURESID = "";
	private String URL = AppConst.EFAPIs.BaseHost
			+ "/bella/webview/en/index.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_chunk);
		isLearning =getBooleanExtra(AppConst.BundleKeys.Is_Chunk_Learning);
		mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);
		BELLAID = AppConst.CurrUserInfo.UserId;

        country_name = (TextView)this.findViewById(R.id.country_name);
        country_flag = (ImageView)this.findViewById(R.id.country_flag);
        rl_country = (RelativeLayout)this.findViewById(R.id.country);

		if(mChunkModel == null){
			COURESID = getStringExtra(AppConst.BundleKeys.Course_id_list);
		} else{
			COURESID = mChunkModel.getChunkCode();
		}

		HideBottomLay = getIntent().getExtras().getInt(
				AppConst.BundleKeys.Hide_Bottom_Lay, 0);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "loading_data"));
		progressDialog.setCancelable(false);
		progressDialog.show();

		// tracking
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.ReviewActivityValue.pageNameValue,
				ContextDataMode.ReviewActivityValue.pageSiteSubSectionValue,
				ContextDataMode.ReviewActivityValue.pageSiteSectionValue,
				mContext);

		initView();
		loadData();
	}

    @Override
    protected void onStart() {
        super.onStart();
        getCountryInfo(BELLAID);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		voiceimage.loadUrl("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		voiceimage.loadUrl(URL);
		loadData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();

        openQuitPopUp();
	}

    private void openQuitPopUp() {
        QuitPracticePopWindow dialog = new QuitPracticePopWindow(
                ReviewActivity.this, QuitPracticePopWindow.Quit_Rate);
        dialog.setOnQuitListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setOnCancelListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            };
        });
        dialog.open();
    }

    private void loadData() {
		// TODO Auto-generated method stub
		HttpReviewVoiceRequest httpReviewVoiceRequest = new HttpReviewVoiceRequest();
		httpReviewVoiceRequest.setBella_id(BELLAID);
		httpReviewVoiceRequest.setCourse_id(COURESID);
		if (HideBottomLay == 0) {
			httpReviewVoiceRequest.setRows(GetVoiceReviewTask.ROW);
			httpReviewVoiceRequest.setStart(GetVoiceReviewTask.START);
		} else {
			httpReviewVoiceRequest.setRows(GetVoiceReviewTask.ROWS);
		}
		
		GetVoiceReviewTask getVoiceReviewTask = new GetVoiceReviewTask(
				mContext, new PostExecuting<HttpReviewVoiceResponse>() {
					@Override
					public void executing(HttpReviewVoiceResponse result) {
						progressDialog.dismiss();
						if (result != null && result.status.equals("0")) {
							initOnClickListener();
							review_chunk_all.setVisibility(View.VISIBLE);
							review_chunk_null.setVisibility(View.GONE);
							reviewVoices = result.getData();
							if (result.getData().size() == 0) {
								//UserRecordingActivity to ReviewActivity ,HideBottomLay is 0
								if (HideBottomLay == 0) {
									ReviewActivityPopWindow reviewActivityPopWindow = new ReviewActivityPopWindow(
											ReviewActivity.this);
									reviewActivityPopWindow
											.setOnNextClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													finish();
												}
											});
									reviewActivityPopWindow.open();
								}
							}

							reviewvoiceSum = reviewVoices.size();
							if (reviewvoiceSum > 0) {
								initSetViewValue(VoiceNum);
							}
						} else {
							goback.setOnClickListener(ReviewActivity.this);
							mNullBtn.setOnClickListener(ReviewActivity.this);
							review_chunk_all.setVisibility(View.GONE);
							review_chunk_null.setVisibility(View.VISIBLE);
						}
					}
				});

		getVoiceReviewTask.execute(httpReviewVoiceRequest);
	}

	private void initSetViewValue(final int voicenum) {
		reviewvoice_pageIndicator
				.setIndicator(VoiceNum + 1, reviewvoiceSum + 1);
		family_given_name.setText(reviewVoices.get(voicenum).getFamily_name()
				+ reviewVoices.get(voicenum).getGiven_name());
		AvatarHelper.LoadAvatar(voiceavater, reviewVoices.get(voicenum)
				.getBella_id(), reviewVoices.get(voicenum).getAvatar());

//		review_chunkname.setText(mChunkModel.getCoursename().toString());
		audioUrl = reviewVoices.get(voicenum).getVoice_url();
		voiceTimes = reviewVoices.get(voicenum).getVoice_length();
		voicetime.setText(String.valueOf(voiceTimes) + "''");
		if (ReviewActivity_Status == 0) {
			voiceReminder.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "rating_view_instruction"));
		} else {
			voiceReminder.setText(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "rating_view_instruction2"));
			// like_soso_layout.setVisibility(View.VISIBLE);
		}

		voicefilename = reviewVoices.get(voicenum).getVoice_file_name();

		voiceimage.loadUrl(URL);
		voiceimage.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (reviewVoices.size() > 0) {
					voiceimage.loadUrl("javascript:getAudioSrc('"
							+ reviewVoices.get(voicenum).getVoice_url() + "','"
							+ reviewVoices.get(voicenum).getVoice_length()
							+ "')");
				}
			}
		});
	}

	private void initOnClickListener() {
		// TODO Auto-generated method stub
		likeimage.setOnClickListener(this);
		sosoimage.setOnClickListener(this);
		goback.setOnClickListener(this);
		reportvoice.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		likeimage = (ImageView) findViewById(R.id.likeimage);
		sosoimage = (ImageView) findViewById(R.id.sosoimage);
		voiceimage = (WebView) findViewById(R.id.voiceimage);
		voiceavater = (RoundedImageView) findViewById(R.id.voiceavater);
		family_given_name = (TextView) findViewById(R.id.family_name_given_name);
		review_chunkname = (TextView) findViewById(R.id.review_chunkname_layout);
		voicetime = (TextView) this.findViewById(R.id.voicetime);
		reviewvoice_pageIndicator = (PagesIndicator) this
				.findViewById(R.id.reviewvoice_pageIndicator);
		gifvoice = (GifImageView) this.findViewById(R.id.gifvoice);
		voiceReminder = (TextView) this
				.findViewById(R.id.review_voice_reminder);
		like_soso_layout = (RelativeLayout) this
				.findViewById(R.id.like_soso_layout);
		goback = (ImageButton) this.findViewById(R.id.chunk_actionbar_goback);
		uLevelView = (UserLevelView) this
				.findViewById(R.id.chunk_actionbar_level);
		reportvoice = (ImageView) this.findViewById(R.id.reportvoice);
		scores_up = (TextView) this.findViewById(R.id.scores_up);
		scores_down = (TextView) this.findViewById(R.id.scores_down);
		review_chunk_all = (LinearLayout) this
				.findViewById(R.id.review_chunk_all);
		review_chunk_null = (LinearLayout) this
				.findViewById(R.id.review_chunk_null);
		mNullMessage = (TextView) this
				.findViewById(R.id.userrecording_chunk_title);
		mNullBtn = (Button) this.findViewById(R.id.userrecording_chunk_btn);

		mNullMessage.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "ratingvoice_fail_to_get_result"));
		mNullBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"ratingvoice_refresh"));

//		likeBtn = (TextView) this.findViewById(R.id.review_like_btn);
//		likeBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
//				"rate_like"));
//		dislikeBtn = (TextView) this.findViewById(R.id.review_dislike_btn);
//		dislikeBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(
//				mContext, "rate_dislike"));

		voiceimage.getSettings().setJavaScriptEnabled(true);

		UserScoreBiz userScoreBiz = new UserScoreBiz(mContext);
		int score = userScoreBiz.getUserScore();
		uLevelView.initialize(score);

		if (HideBottomLay == 0) {
			reviewvoice_pageIndicator.setVisibility(View.GONE);
		}
	}

	public void startDisplay(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (audio_status) {
					if (ReviewActivity_Status == 0) {
						ReviewActivity_Status++;
						loadvoice(VoiceNum, reviewvoiceSum);
						reviewvoice_pageIndicator.setIndicator(VoiceNum + 1,
								reviewvoiceSum + 1);
					}
					audio_status = false;
				} else {
					audio_status = true;
				}
			}
		});
	}

	private void loadvoice(int index, int voicesum) {
		if (index > voicesum - 1) {
			isDataNull = true;
			if(isLearning){
				//Going to record
				Intent intent = new Intent(mContext,
						RecordingActivity.class);
				intent.putExtra(AppConst.BundleKeys.Chunk,
						mChunkModel);
				intent.putExtra(
						AppConst.BundleKeys.Hide_Bottom_Lay, 1);
				startActivity(intent);
				finish();
			}else {

				ReviewActivityPopWindow reviewActivityPopWindow = new ReviewActivityPopWindow(
						ReviewActivity.this);
				reviewActivityPopWindow
						.setOnNextClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								finish();
							}
						});
				reviewActivityPopWindow.open();
			}

		} else {
			initSetViewValue(index);
			isDataNull = false;
		}
	}

	/**
	 * Increasing score
	 * 
	 * @param increasedScore
	 */
	private void increasingScore(int increasedScore, final TextView scoreView) {
		userScoreBiz.increaseScore(increasedScore);
		// Score Increasing Text initialized
		scoreView.setVisibility(View.VISIBLE);
		scoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.balloon_label_size));
		scoreView.setText("+" + Integer.toString(increasedScore));
		int cur_color = getResources().getColor(
				R.color.bella_color_cellobrate_yellow);
		scoreView.setTextColor(cur_color);
		// Start user level increasing animation
		uLevelView.startIncreasingScore(increasedScore, cur_color);
		// Start the increasing text animation
		scoreView.setVisibility(View.VISIBLE);
		ValueAnimator disappearAnim = ValueAnimator.ofFloat(1, 0);
		disappearAnim
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float alpha = (Float) animation.getAnimatedValue();
						// uLevelView.setAlpha(alpha);
						scoreView.setAlpha(alpha);
					}
				});

		disappearAnim.setDuration(1000);
		disappearAnim.start();
	}

	private void PostLikeVoice(int num) {
		if (reviewVoices.size() > 0) {
			HttpVoiceRequest httpVoiceRequest = new HttpVoiceRequest();
			httpVoiceRequest.setCourse_id(COURESID);
			httpVoiceRequest.setReviewer_bella_id(BELLAID);
			httpVoiceRequest.setReviewee_bella_id(reviewVoices.get(num)
					.getBella_id());
			httpVoiceRequest.setVoice_file_name(voicefilename);
			httpVoiceRequest.setScore(SCORE);
			PostLikeVoiceTask postLikeVoiceTask = new PostLikeVoiceTask(
					mContext, new PostExecuting<Boolean>() {
						@Override
						public void executing(Boolean result) {
							increasingScore(SCORE, scores_up);
							if (HideBottomLay == 0) {
								loadData();
							}
						}
					});
			postLikeVoiceTask.execute(httpVoiceRequest);
		}
	}

	private void PostUnlikeVoice(int num) {
		if (reviewVoices.size() > 0) {
			HttpVoiceRequest httpunVoiceRequest = new HttpVoiceRequest();
			httpunVoiceRequest.setCourse_id(COURESID);
			httpunVoiceRequest.setReviewer_bella_id(BELLAID);
			httpunVoiceRequest.setReviewee_bella_id(reviewVoices.get(num)
					.getBella_id());
			httpunVoiceRequest.setVoice_file_name(voicefilename);
			httpunVoiceRequest.setScore(SCORE);
			PostUnlikeVoiceTask postUnlikeVoiceTask = new PostUnlikeVoiceTask(
					mContext, new PostExecuting<Boolean>() {

						@Override
						public void executing(Boolean result) {
							// TODO Auto-generated method stub
							increasingScore(SCORE, scores_down);
							if (HideBottomLay == 0) {
								loadData();
							}
						}
					});
			postUnlikeVoiceTask.execute(httpunVoiceRequest);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.likeimage:
			if (HideBottomLay == 0) {
				VoiceNum = 0;
				GetVoiceReviewTask.START++;
				PostLikeVoice(0);
				progressDialog.show();
			} else {
				if (!isDataNull) {
					PostLikeVoice(VoiceNum);
					VoiceNum++;
					reviewvoice_pageIndicator.setIndicator(VoiceNum + 1,
							reviewvoiceSum + 1);
				}
				loadvoice(VoiceNum, reviewvoiceSum);
			}
			MobclickTracking.OmnitureTrack.ActionTracingReview(mContext);
			break;

		case R.id.sosoimage:
			if (HideBottomLay == 0) {
				VoiceNum = 0;
				GetVoiceReviewTask.START++;
				PostUnlikeVoice(0);
				progressDialog.show();
			} else {
				if (!isDataNull) {
					PostUnlikeVoice(VoiceNum);
					VoiceNum++;
					reviewvoice_pageIndicator.setIndicator(VoiceNum + 1,
							reviewvoiceSum + 1);
				}
				loadvoice(VoiceNum, reviewvoiceSum);
			}
			MobclickTracking.OmnitureTrack.ActionTracingReview(mContext);
			break;

		case R.id.chunk_actionbar_goback:
            //add prompt
            openQuitPopUp();
            break;

		case R.id.reportvoice:
			EditorPopupWindow editorPopupWindow = null;
			editorPopupWindow = new EditorPopupWindow(ReviewActivity.this,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							HttpVoiceRequest httpreportVoiceRequest = new HttpVoiceRequest();
							httpreportVoiceRequest.setCourse_id(COURESID);
							httpreportVoiceRequest
									.setReviewer_bella_id(BELLAID);
							httpreportVoiceRequest
									.setReviewee_bella_id(reviewVoices.get(
											VoiceNum).getBella_id());
							httpreportVoiceRequest
									.setVoice_file_name(voicefilename);
							PostReportVoiceTask postReportVoiceTask = new PostReportVoiceTask(
									mContext, new PostExecuting<Boolean>() {

										@Override
										public void executing(Boolean result) {
											// TODO Auto-generated method stub
											// tracking
											MobclickTracking.OmnitureTrack
													.AnalyticsTrackState(
															ContextDataMode.ReportPromptValue.pageNameValue,
															ContextDataMode.ReportPromptValue.pageSiteSubSectionValue,
															ContextDataMode.ReportPromptValue.pageSiteSectionValue,
															mContext);
											ToastUtils
													.show(mContext,
															JsonSerializeHelper
																	.JsonLanguageDeserialize(
																			mContext,
																			"report_confirm"));
										}
									});
							postReportVoiceTask.execute(httpreportVoiceRequest);
						}
					});
			editorPopupWindow.open();
			break;

		case R.id.userrecording_chunk_btn:
			loadData();
			break;
		}
	}
}
