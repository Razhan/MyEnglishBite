package com.ef.bite.ui.popup;

import java.util.HashMap;
import com.ef.bite.R;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.LogManager;
import com.ef.bite.utils.SoundEffectUtils;

public class LevelUpPopWindow implements PlatformActionListener, Callback {

	private Activity mContext;
	private PopupWindow mPopUpWindow;
	private int mLevel;

	private RelativeLayout mLevelUpLayout;
	private RelativeLayout mShareLayout;

	// Level Up
	private RelativeLayout mFlowLayout; // 花飘的范围layout
	private TextView mLevelNumber;
	private Button mBtnYeah;
	private TextView mShare;
	private TextView popup_share_title;
	private TextView popup_social_share_wechat;
	private TextView popup_social_share_sina;
	private TextView bella_color_black_dark;
	private TextView popup_social_share_wechat_moments;

	// Shared
	private LinearLayout mWechatShare;
	private LinearLayout mWeiboShare;
	private LinearLayout mPengyouShare;
	private LinearLayout mQQShare;
	private LinearLayout mTwitterShare;
	private LinearLayout mFacebookShare;
	private TextView mCancel;
	private ScheduledExecutorService mPlayScheduler;

	private int CurFlowIndex = 0;
	private int MAX = 0; // 每一轮鲜花掉落下来的数量
	private static int Total = 0; // 总的鲜花数
	private final static int ScalSize = 10; // 总共的掉落轮数
	private ImageView[] FlowerCache;
	private OnDismissListener mDissmissListener;

	// Alpha动画 - 渐变透明度
	private static Animation alphaAnimation = null;

	// Translate动画 - 位置移动
	private static Animation translateAnimation = null;

	// Rotate动画 - 画面旋转
	private static Animation rotateAnimation = null;

	enum ShareType {
		Wechat, Weibo, Pengyouquan, QQ, Twitter, Facebook
	}

	public LevelUpPopWindow(Activity activity, int level) {
		mContext = activity;
		mLevel = level;
	}

	public void open() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popup_level_up_flip, null,
				false);
		mBtnYeah = (Button) layout.findViewById(R.id.level_up_btn_yeah);
		mLevelNumber = (TextView) layout.findViewById(R.id.level_up_number);
		bella_color_black_dark = (TextView) layout
				.findViewById(R.id.bella_color_black_dark);
		bella_color_black_dark.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "chunk_level_up_title"));
		mLevelUpLayout = (RelativeLayout) layout.findViewById(R.id.flip_front);
		mFlowLayout = (RelativeLayout) layout
				.findViewById(R.id.level_up_layout);
		mShareLayout = (RelativeLayout) layout.findViewById(R.id.flip_back);
		popup_share_title = (TextView) layout
				.findViewById(R.id.popup_share_title_text);
		popup_share_title.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "popup_social_share_title"));
		mShare = (TextView) layout.findViewById(R.id.level_up_btn_share);
		mShare.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"chunk_level_up_share"));
		mCancel = (TextView) layout.findViewById(R.id.popup_share_cancel);
		mCancel.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_social_share_cancel"));
		mWechatShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_wechat);
		popup_social_share_wechat = (TextView) layout
				.findViewById(R.id.popup_social_share_wechat);
		popup_social_share_wechat
				.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"popup_social_share_wechat"));
		mWeiboShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_weibo);
		popup_social_share_sina = (TextView) layout
				.findViewById(R.id.popup_social_share_sina);
		popup_social_share_sina.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "popup_social_share_sina"));
		mPengyouShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_pengyouquan);
		popup_social_share_wechat_moments = (TextView) layout
				.findViewById(R.id.popup_social_share_wechat_moments);
		popup_social_share_wechat_moments.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext,
						"popup_social_share_wechat_moments"));
		mQQShare = (LinearLayout) layout.findViewById(R.id.popup_share_qq);
		mTwitterShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_twitter);
		mFacebookShare = (LinearLayout) layout
				.findViewById(R.id.popup_share_facebook);
		mCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		FontHelper.applyFont(mContext, layout, FontHelper.FONT_Museo300);
		mPopUpWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		mLevelNumber.setText(Integer.toString(mLevel));
		if (mPlayScheduler == null)
			mPlayScheduler = Executors.newSingleThreadScheduledExecutor();
		mPlayScheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 0, 20, TimeUnit.MILLISECONDS);
		mBtnYeah.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});

		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopFlingFlowers();
				flipAnimate(mLevelUpLayout, mShareLayout);
				// tracking
				// TraceHelper.tracingAction(mContext,
				// TraceHelper.PAGE_LEVEL_UP,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_SHARE);
				MobclickTracking.OmnitureTrack.ActionLevelUpMessage();
//				MobclickTracking.UmengTrack.ActionLevelUpMessage(mContext);
				LogManager
						.definedLog(ContextDataMode.ActionLevelUpMessageKey.actionLevelUpShare);
			}
		});

		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_CANCEL);
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
		// Sound play effect
		SoundEffectUtils soundEffect = new SoundEffectUtils(mContext);
		soundEffect.play(SoundEffectUtils.LEVEL_UP);
		// tracking
		// TraceHelper.tracingPage(mContext, TraceHelper.PAGE_LEVEL_UP);
	}

	public void setOnDismissListener(OnDismissListener listener) {
		this.mDissmissListener = listener;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (MAX == 0 || Total == 0) {
				int width = mLevelUpLayout.getWidth();
				MAX = width / 20;
				Total = MAX * ScalSize;
				FlowerCache = new ImageView[Total];
			}
			if (CurFlowIndex >= 0 && CurFlowIndex < Total - 1)
				CurFlowIndex++;
			else
				CurFlowIndex = 0;
			flingFlowers(CurFlowIndex);
		}
	};

	public void close() {
		if (mPlayScheduler != null && !mPlayScheduler.isShutdown())
			mPlayScheduler.shutdown();
		mPlayScheduler = null;
		if (mDissmissListener != null)
			mDissmissListener.onDismiss();
		mPopUpWindow.dismiss();
	}

	private AnimationSet getFlyingAnimation() {
		// 初始化 Translate动画
		translateAnimation = new TranslateAnimation(0.1f,
				(float) (Math.random() * 300 + 1), -200f,
				(float) (Math.random() * 1000 + 1));
		// 初始化 Alpha动画
		alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

		rotateAnimation = new RotateAnimation((float) (Math.random() * 30 + 1),
				0f);

		// 动画集
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(translateAnimation);
		set.addAnimation(alphaAnimation);
		set.addAnimation(rotateAnimation);
		// 设置动画时间 (作用到每个动画)
		set.setDuration(3000);
		set.setFillAfter(true);
		return set;
	}

	private void flingFlowers(int index) {
		ImageView flower = FlowerCache[index];
		if (flower == null) {
			flower = new ImageView(mContext);
			flower.setPadding((index % MAX) * 20, 0, 0, 0);
			switch (index % 10) {
			case 0:
				flower.setImageResource(R.drawable.level_up_f_1);
				break;
			case 1:
				flower.setImageResource(R.drawable.level_up_f_2);
				break;
			case 2:
				flower.setImageResource(R.drawable.level_up_f_3);
				break;
			case 3:
				flower.setImageResource(R.drawable.level_up_f_4);
				break;
			case 4:
				flower.setImageResource(R.drawable.level_up_f_5);
				break;
			case 5:
				flower.setImageResource(R.drawable.level_up_f_6);
				break;
			case 6:
				flower.setImageResource(R.drawable.level_up_f_7);
				break;
			case 7:
				flower.setImageResource(R.drawable.level_up_f_8);
				break;
			case 8:
				flower.setImageResource(R.drawable.level_up_f_9);
				break;
			}
			FlowerCache[index] = flower;
			mFlowLayout.addView(flower);
		}
		AnimationSet animation = getFlyingAnimation();
		flower.startAnimation(animation);
	}

	private void stopFlingFlowers() {
		if (mPlayScheduler != null && !mPlayScheduler.isShutdown())
			mPlayScheduler.shutdown();
		mPlayScheduler = null;
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

	public interface OnDismissListener {
		void onDismiss();
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
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_WECHAT);
			} else if (mType == ShareType.Weibo) {
				showShare(false, SinaWeibo.NAME, false);
				// tracking
				// TraceHelper
				// .tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_SINA);
			} else if (mType == ShareType.Pengyouquan) {
				showShare(false, WechatMoments.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_WECHAT_MOMENTS);
			} else if (mType == ShareType.QQ) {
				showShare(false, QQ.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null, TraceHelper.TARGET_QQ);
			} else if (mType == ShareType.Twitter) {
				showShare(false, Twitter.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_TWITTER);
			} else if (mType == ShareType.Facebook) {
				showShare(false, Facebook.NAME, false);
				// tracking
				// TraceHelper.tracingAction(mContext, TraceHelper.PAGE_SHARE,
				// TraceHelper.ACTION_CLICK, null,
				// TraceHelper.TARGET_FACEBOOK);
			}
		}

	}

	/** 共享 **/
	private void showShare(boolean silent, String platform, boolean captureView) {
		PackageManager pm = mContext.getPackageManager();
		String appName = mContext.getApplicationInfo().loadLabel(pm).toString();
		final OnekeyShare oks = new OnekeyShare();
		String shareText = String.format(JsonSerializeHelper
				.JsonLanguageDeserialize(mContext, "share_text_level_reach_X"),
				mLevel)
				+ JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"share_text_level_catch_up");
		oks.setNotification(R.drawable.ic_launcher, appName);
		oks.setTitle(shareText);
		oks.setText(shareText);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(
				shareText, null));
		oks.show(mContext);
	}

	@Override
	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	/** 处理share的回调 **/
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			text = plat.getName() + " completed at " + text;
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + " caught error at " + text;
		}
			break;
		case 3: {
			// 取消
			text = plat.getName() + " canceled at " + text;
		}
			break;
		}

		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
		return false;
	}

	public String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}
}
