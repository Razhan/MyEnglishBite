/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */

package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 快捷分享项目现在添加为不同的平台添加不同分享内容的方法。 本类用于演示如何区别Twitter的分享内容和其他平台分享内容。 本类会在
 * {@link DemoPage#showShare(boolean, String)}方法 中被调用。
 */
public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

	String mUrl;
	String mText;

	public ShareContentCustomizeDemo(String shareText, String url) {
		mText = shareText;
		mUrl = url;
	}

	public void onShare(Platform platform, ShareParams paramsToShare) {

		if ((platform.getName().equals(Wechat.NAME) || platform.getName()
				.equals(WechatMoments.NAME)) && mUrl != null) {
			paramsToShare.setUrl(mUrl);
			paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
		} else if ((platform.getName().equals(SinaWeibo.NAME) || platform
				.getName().equals(QQ.NAME)) && mUrl != null) {
			mText = mText + " " + mUrl.toString();
		}

		paramsToShare.setText(mText);
	}

}
