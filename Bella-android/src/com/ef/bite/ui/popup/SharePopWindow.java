package com.ef.bite.ui.popup;

import java.util.HashMap;

import com.ef.bite.R;
import com.ef.bite.utils.JsonSerializeHelper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.ef.bite.utils.StringUtils;

public class SharePopWindow implements PlatformActionListener, Callback {
	private Context mContext;
	private PopupWindow mPopUpWindow;

	private LinearLayout mWechatShare;
	private LinearLayout mWeiboShare;
	private LinearLayout mPengyouShare;
	private LinearLayout mQQShare;
	private LinearLayout mTwitterShare;
	private LinearLayout mFacebookShare;
	private TextView mCancel;
	private TextView mTitle;
	private String content;
	private String title;

	enum ShareType {
		Wechat, Weibo, Pengyouquan, QQ, Twitter, Facebook
	}

	public SharePopWindow(Activity activity) {
		mContext = activity;
	}


	public SharePopWindow(Activity activity, String title, String content) {
		mContext = activity;
		this.content = content;
		this.title = title;
	}

	public void open() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.share_popup_windows, null,
				false);

		mCancel = (TextView) layout.findViewById(R.id.popup_share_cancel);
		mCancel.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_social_share_cancel"));
		mTitle = (TextView) layout.findViewById(R.id.popup_share_title_text);
		mTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_social_share_title"));
		mCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mPopUpWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

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

		initOnClickListener();
	}

	private void initOnClickListener() {
		// TODO Auto-generated method stub
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

		oks.setNotification(R.drawable.ic_launcher, appName);
		oks.setTitle(StringUtils.nullStrToEmpty(title)+" ");
		oks.setText(StringUtils.nullStrToEmpty(content));
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo(
				StringUtils.nullStrToEmpty(title+content), null));
		oks.show(mContext);
	}

	public void close() {
		mPopUpWindow.dismiss();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
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

	@Override
	public void onCancel(Platform palt, int action) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform palt, int action, Throwable t) {
		// TODO Auto-generated method stub
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
