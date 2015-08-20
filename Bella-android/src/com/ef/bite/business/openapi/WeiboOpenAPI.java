package com.ef.bite.business.openapi;
//package com.ef.myword2014.business.openapi;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.ef.myword2014.AppConst;
//import com.ef.myword2014.model.sso.LoginResponse;
//import com.ef.myword2014.model.sso.LogoutResponse;
//import com.ef.myword2014.model.sso.ShareRequest;
//import com.ef.myword2014.model.sso.UserInfoModel;
//import com.sina.weibo.sdk.AccessTokenKeeper;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMessage;
//import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuth;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.openapi.UsersAPI;
//
//public class WeiboOpenAPI extends BaseOpenAPI{
//	
//	/** 授权认证所需要的信息 */
//    private AuthInfo mAuthInfo;
//    /** SSO 授权认证实例 */
//    private SsoHandler mSsoHandler;
//    private static final String SCOPE = 
//            "email,direct_messages_read,direct_messages_write,"
//            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//            + "follow_app_official_microblog," + "invitation_write";
//    
//    /** 微博微博分享接口实例 */
//    private IWeiboShareAPI  mWeiboShareAPI = null;
//    
//	public WeiboOpenAPI(Activity activity) {
//		super(activity);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void login(final RequestCallback<LoginResponse> callback) {
//		
//		mAuthInfo = new AuthInfo(mActivity, AppConst.ThirdPart.Weibo_AppKey, AppConst.ThirdPart.Weibo_ReturnUrl, SCOPE);
//		if (null == mSsoHandler && mAuthInfo != null) {
//			WeiboAuth weiboAuth = new WeiboAuth(mActivity, mAuthInfo);
//			mSsoHandler = new SsoHandler((Activity)mActivity, weiboAuth);
//		}
//		
//        if (mSsoHandler != null) {
//            mSsoHandler.authorize(new WeiboAuthListener(){
//				@Override
//				public void onCancel() { }
//
//				@Override
//				public void onComplete(Bundle values) {
//					 Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token 
//			    	 if (mAccessToken.isSessionValid()) { 
//			    		 AccessTokenKeeper.writeAccessToken(mContext, mAccessToken); //保存Token
//			    		 String uid = values.getString("uid", ""); 
//			    		 String token = values.getString("access_token", ""); 
//			    		 long expire_in = values.getLong("expires_in", 0);
//			    		 LoginResponse login = new LoginResponse();
//			    		 login.Code = 0;
//			    		 login.UID = uid;
//			    		 login.Access_Token = token;
//			    		 login.Expires_In = expire_in;
//			    		 callback.callback(login);
//			    	 }
//				}
//			
//				@Override
//				public void onWeiboException(WeiboException ex) {
//					Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
//				}
//            });
//        } else {
//        }
//	}
//
//	@Override
//	public void logout(final RequestCallback<LogoutResponse> callback) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void getUserInfo(String uid, final RequestCallback<UserInfoModel> callback) {
//		 Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
//		 if (mAccessToken.isSessionValid()) { 
//			// 获取用户信息接口
//	    	 UsersAPI mUserAPI = new UsersAPI(mAccessToken);
//	    	 long uidlong = Long.parseLong(uid);
//			 mUserAPI.show(Long.parseLong(uid), new RequestListener() {
//	    	        @Override
//	    	        public void onComplete(String response) {
//	    	        	try {
//	    	        		String username = "";
//	    	        		String avatar = "";
//	    	        		int gender = 0;
//							JSONObject json = new JSONObject(response);
//							if(json.has("name"))
//								username = json.getString("screen_name");
//							if(json.has("avatar_large"))
//								avatar = json.getString("avatar_large");
//							if(json.has("gender"))
//								gender = json.getString("gender").equals("m")?0:1;
//							UserInfoModel userinfo = new UserInfoModel();
//							userinfo.TrueName = username;
//							userinfo.AvatarUrl = avatar;
//							userinfo.Gender = gender;
//							callback.callback(userinfo);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//	    	        }
//
//					@Override
//					public void onWeiboException(WeiboException e) {
//						Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
//					}
//	    	    });
//		 }
//	}
//
//	@Override
//	public void share(ShareRequest share,final RequestCallback<Boolean> callback) {
//		// 创建微博分享接口实例
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, AppConst.ThirdPart.Weibo_AppKey);
//        
//        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
//        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
//        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
//        mWeiboShareAPI.registerApp();
//        
//        // 如果未安装微博客户端，设置下载微博对应的回调
//        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
//            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//                @Override
//                public void onCancel() {Toast.makeText(mActivity,  "\t取消下载", Toast.LENGTH_SHORT).show();}
//            });
//        }
//        WeiboMessage weiboMessage = new WeiboMessage();
//        // 1. 初始化微博的分享消息
//        // 用户可以分享文本、图片、网页、音乐、视频中的一种
//		switch(share.Type){
//		
//			case 0:
//				 TextObject textObject = new TextObject();
//				 textObject.text = share.Title;
//				 weiboMessage.mediaObject = textObject;
//				break;
//			case 1:
//				ImageObject imageObject = new ImageObject();
//				imageObject.setImageObject(share.Image);
//				weiboMessage.mediaObject = imageObject;
//				break;
//		}
//		 // 2. 初始化从第三方到微博的消息请求
//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.message = weiboMessage;
//        
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.sendRequest(request);
//	}
//
//	@Override
//	/**
//	 * no implement
//	 */
//	public void getUserInfo(RequestCallback<UserInfoModel> callback) {
//		
//	}
//	
//	
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	        if (mSsoHandler != null) {
//	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//	        }
//	}
//
//}
