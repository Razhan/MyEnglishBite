package com.ef.bite.ui.popup;

import android.animation.Animator;
import com.ef.bite.R;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.GetShareChunkLinkTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.httpMode.HttpShareLink;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

public class ChunkDonePopWindow extends BasePopupWindow {
	private Context mContext;
	private String mChunkName;
	private String mChunkCode;
	private Button mShareBtn;
	private LinearLayout mChunkDoneLayout;
	private TextView mChunkContinue;
	private TextView popup_chunk_done_well_done;
	private TextView popup_chunk_done_learn_new_phrase;
	// Shared
	private RelativeLayout mShareLayout;
	private LinearLayout mWechatShare;
	private LinearLayout mWeiboShare;
	private LinearLayout mPengyouShare;
	private LinearLayout mQQShare;
	private LinearLayout mTwitterShare;
	private LinearLayout mFacebookShare;
	private TextView mCancel;
	private ProgressDialog mProgress;

	private String activityName;

	enum ShareType {
		Wechat, Weibo, Pengyouquan, QQ, Twitter, Facebook
	}

	public ChunkDonePopWindow(Activity activity, String chunkName,
			String chunkCode, Context context) {
		super(activity, R.layout.popup_chunk_prictice_done);
		mChunkName = chunkName;
		mChunkCode = chunkCode;
		mContext = context;

		activityName = activity.getClass().getSimpleName();
		// tracking
		// TraceHelper.tracingPage(mActivity, TraceHelper.PAGE_WELL_DONE);
	}

	@Override
	protected void initViews(View layout) {
		TextView chunkName = (TextView) layout
				.findViewById(R.id.dialog_chunk_done_chunkname);
		popup_chunk_done_well_done = (TextView) layout
				.findViewById(R.id.popup_chunk_done_well_done);
		popup_chunk_done_well_done
				.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"popup_chunk_done_well_done"));
		popup_chunk_done_learn_new_phrase = (TextView) layout
				.findViewById(R.id.popup_chunk_done_learn_new_phrase);
		popup_chunk_done_learn_new_phrase.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext,
						"popup_chunk_done_learn_new_phrase"));
		mChunkDoneLayout = (LinearLayout) layout
				.findViewById(R.id.dialog_chunk_done_layout);
		mShareLayout = (RelativeLayout) layout
				.findViewById(R.id.dialog_chunk_done_back);
		mChunkContinue = (TextView) layout
				.findViewById(R.id.dialog_chunk_done_continue);
		mChunkContinue.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "popup_chunk_done_continue"));
		mShareBtn = (Button) layout.findViewById(R.id.dialog_chunk_done_share);
		mShareBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_chunk_done_brag_friend"));
		mCancel = (TextView) layout.findViewById(R.id.popup_share_cancel);
		mWechatShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_wechat);
		mWeiboShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_weibo);
		mPengyouShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_pengyouquan);
		mQQShare = (LinearLayout) layout.findViewById(R.id.popup_share_qq);
		mTwitterShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_twitter);
		mFacebookShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_facebook);
		mCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		chunkName.setText(mChunkName);
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		mChunkContinue.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		mChunkContinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
			}
		});
		mShareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				flipAnimate(mChunkDoneLayout, mShareLayout);
				// tracking
				// TraceHelper.tracingAction(mActivity,
				// TraceHelper.PAGE_WELL_DONE, TraceHelper.ACTION_CLICK,
				// null, TraceHelper.TARGET_SHARE);
				MobclickTracking.OmnitureTrack
						.AnalyticsTrackState(
								ContextDataMode.ShareListChinaValues.pageNameValue,
								ContextDataMode.ShareListChinaValues.pageSiteSubSectionValue,
								ContextDataMode.ShareListChinaValues.pageSiteSectionValue,
								mContext);
//				MobclickTracking.UmengTrack
//						.setPageStart(
//								ContextDataMode.ShareListChinaValues.pageNameValue,
//								ContextDataMode.ShareListChinaValues.pageSiteSubSectionValue,
//								ContextDataMode.ShareListChinaValues.pageSiteSectionValue,
//								mContext);
//				MobclickTracking.UmengTrack
//						.setPageEnd(
//								ContextDataMode.ShareListChinaValues.pageNameValue,
//								ContextDataMode.ShareListChinaValues.pageSiteSubSectionValue,
//								ContextDataMode.ShareListChinaValues.pageSiteSectionValue,
//								mContext);

				if (activityName.contains("ChunkPracticeActivity")) {
					MobclickTracking.OmnitureTrack.ActionPhraseLearnedMessage(0);
				} else {
					MobclickTracking.OmnitureTrack.ActionPhraseLearnedMessage(1);
				}


				MobclickTracking.OmnitureTrack.ActionShareListChina(5);
				MobclickTracking.OmnitureTrack.ActionTrackingUserRecordShare();
//				MobclickTracking.UmengTrack.ActionShareListChina(5, mContext);
			}
		});
		
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_CANCEL);
				MobclickTracking.OmnitureTrack.ActionShareListChina(6);
//				MobclickTracking.UmengTrack.ActionShareListChina(6, mContext);
			}
		});
		mWechatShare
				.setOnClickListener(new ShareClickListener(ShareType.Wechat));
		mWeiboShare.setOnClickListener(new ShareClickListener(ShareType.Weibo));
		mPengyouShare.setOnClickListener(new ShareClickListener(
				ShareType.Pengyouquan));
		mQQShare.setOnClickListener(new ShareClickListener(ShareType.QQ));
		mTwitterShare.setOnClickListener(new ShareClickListener(
				ShareType.Twitter));
		mFacebookShare.setOnClickListener(new ShareClickListener(
				ShareType.Facebook));
	}

	/**
	 * 左右翻转
	 * 
	 * @param visibleLayout
	 * @param invisibleLayout
	 */
	private void flipAnimate(final View visibleLayout,
			final View invisibleLayout) {
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleLayout,
				"rotationY", 0f, 90f);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(
				invisibleLayout, "rotationY", -90f, 0f);
		visToInvis.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				visibleLayout.setVisibility(View.GONE);
				invisToVis.start();
				invisibleLayout.setVisibility(View.VISIBLE);
			}
		});
		visToInvis.start();
	}

	/**
	 * 共享
	 * 
	 */
	class ShareClickListener implements OnClickListener {
		ShareType mType;

		public ShareClickListener(ShareType type) {
			mType = type;
		}

		@Override
		public void onClick(View v) {
			if (mType == ShareType.Wechat) {
				showShare(false, Wechat.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_WECHAT);
				MobclickTracking.OmnitureTrack.ActionShareListChina(1);
//				MobclickTracking.UmengTrack.ActionShareListChina(1, mContext);
			} else if (mType == ShareType.Weibo) {
				showShare(false, SinaWeibo.NAME, false);
				// tracking
				// TraceHelper
				// .tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_SINA);
				MobclickTracking.OmnitureTrack.ActionShareListChina(2);
//				MobclickTracking.UmengTrack.ActionShareListChina(2, mContext);
			} else if (mType == ShareType.Pengyouquan) {
				showShare(false, WechatMoments.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_WECHAT_MOMENTS);
				MobclickTracking.OmnitureTrack.ActionShareListChina(3);
//				MobclickTracking.UmengTrack.ActionShareListChina(3, mContext);
			} else if (mType == ShareType.QQ) {
				showShare(false, QQ.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null, TraceHelper.TARGET_QQ);
				MobclickTracking.OmnitureTrack.ActionShareListChina(4);
//				MobclickTracking.UmengTrack.ActionShareListChina(4, mContext);
			} else if (mType == ShareType.Twitter) {
				showShare(false, Twitter.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_TWITTER);
			} else if (mType == ShareType.Facebook) {
				showShare(false, Facebook.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mActivity, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_FACEBOOK);
			}

		}

	}

	/** 共享 **/
	private void showShare(final boolean silent, final String platform,
			final boolean captureView) {
		mProgress = new ProgressDialog(mActivity);
		mProgress.setMessage(mActivity
				.getString(R.string.share_text_generating_link));
		mProgress.show();
		GetShareChunkLinkTask task = new GetShareChunkLinkTask(mActivity,
				new PostExecuting<HttpShareLink>() {
					@Override
					public void executing(HttpShareLink result) {
						mProgress.dismiss();
						if (result != null && result.status != null
								&& result.data != null) {
							final OnekeyShare oks = new OnekeyShare();
							// String shareText =
							// mActivity.getString(R.string.share_text_new_chunk);
							oks.setNotification(R.drawable.ic_launcher,
									mActivity.getString(R.string.app_name));
							oks.setTitle(result.data.message);
							oks.setText(result.data.message);
							oks.setUrl(result.data.url);
							if (platform != null) {
								oks.setPlatform(platform);
							}
							// 令编辑页面显示为Dialog模式
							oks.setDialogMode();
							// 在自动授权时可以禁用SSO方式
							// oks.disableSSOWhenAuthorize();
							oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(
									result.data.message, result.data.url));
							// 为EditPage设置一个背景的View
							// oks.setEditPageBackground(getPage());
							oks.show(mActivity);
						} else {
							Toast.makeText(
									mActivity,
									mActivity
											.getString(R.string.share_text_link_genrate_failed),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		task.execute(new String[] { AppConst.CurrUserInfo.UserId, mChunkCode,
				AppLanguageHelper.getSystemLaunguage(mActivity) });
	}
}
