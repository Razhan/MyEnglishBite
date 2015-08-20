package com.ef.bite.business.openapi;
//package com.ef.myword2014.business.openapi;
//
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.ef.myword2014.AppConst;
//import com.ef.myword2014.R;
//import com.ef.myword2014.model.sso.LoginResponse;
//import com.ef.myword2014.model.sso.LogoutResponse;
//import com.ef.myword2014.model.sso.ShareRequest;
//import com.ef.myword2014.model.sso.UserInfoModel;
//import com.ef.myword2014.utils.DialogHelper;
//import com.ef.myword2014.utils.DialogHelper.Clicking;
//import com.tencent.connect.UserInfo;
//import com.tencent.connect.auth.QQAuth;
//import com.tencent.connect.share.QQShare;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
//public class TencentOpenAPI extends BaseOpenAPI {
//	Tencent mTencent;
//	QQAuth mQQAuth;
//	UserInfo mQQUserInfo;
//	QQShare mQQShare = null;
//	
//	public TencentOpenAPI(Activity activity) {
//		super(activity);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void login(final RequestCallback<LoginResponse> callback) {
//		mQQAuth = QQAuth.createInstance(AppConst.ThirdPart.QQ_AppID, mContext);
//		mTencent = Tencent.createInstance(AppConst.ThirdPart.QQ_AppID, mContext);
//		if (!mQQAuth.isSessionValid()) {
//			mTencent.login(mActivity, "all", new IUiListener(){
//				@Override
//				public void onCancel() {
//				}
//				
//				@Override
//				public void onComplete(Object response) {
//					if(response != null){
//						try{
//							JSONObject jsonObj = (JSONObject)response;
//							LoginResponse login = new LoginResponse();
//							if(jsonObj.has("ret"))
//								login.Code = jsonObj.getInt("ret");
//							if(jsonObj.has("expires_in"))
//								login.Expires_In = jsonObj.getLong("expires_in");
//							if(jsonObj.has("access_token"))
//								login.Access_Token = jsonObj.getString("access_token");
//							callback.callback(login);
//						}catch(Exception ex){
//							ex.printStackTrace();
//						}
//					}
//				}
//
//				@Override
//				public void onError(UiError e) {
//					DialogHelper.MessageBox(mContext, "登录错误", e.errorDetail, "确定", new Clicking(){
//						@Override public void Click() {}});
//				}
//			});
//			
//		} else {
//			mQQAuth.logout(mContext);
//		}
//	}
//
//	@Override
//	public void logout(final RequestCallback<LogoutResponse> callback) {
//		
//	}
//
//	@Override
//	public void getUserInfo(final RequestCallback<UserInfoModel> callback) {
//		if(mQQAuth == null)
//			mQQAuth = QQAuth.createInstance(AppConst.ThirdPart.QQ_AppID, mContext);
//		if(mQQAuth.isSessionValid()){
//			IUiListener listener = new IUiListener() {
//				@Override
//				public void onError(UiError e) {
//					Toast.makeText(mContext, "获得用户信息失败！", Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onComplete(final Object response) {
//					if(response!=null){
//						try {
//							JSONObject json = (JSONObject)response;
//							UserInfoModel user = new UserInfoModel();
//							if(json.has("figureurl_qq_2")){
//								String photoPath = json.getString("figureurl_qq_2");
//								user.AvatarUrl = photoPath;
//							}
//							if(json.has("nickname")){
//								String nickname = json.getString("nickname");
//								user.TrueName = nickname;
//							} 
//							if(json.has("gender")){
//								user.Gender = json.getString("gender").equals("男")? 0:1;
//							}
//							callback.callback(user);
//						}catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					
//				}
//				@Override
//				public void onCancel() {
//					
//				}
//			};
//			mQQUserInfo = new UserInfo(mActivity, mQQAuth.getQQToken());
//			mQQUserInfo.getUserInfo(listener);
//		}
//		
//	}
//
//	@Override
//	public void share(ShareRequest share,final RequestCallback<Boolean> callback) {
//		mQQAuth = QQAuth.createInstance(AppConst.ThirdPart.QQ_AppID, mContext);
//		if (mQQAuth.isSessionValid()) {
//			mQQShare = new QQShare(mActivity, mQQAuth.getQQToken());
//			final Bundle params = new Bundle();
//			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT );
//			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,mContext.getString(R.string.app_name));
//			params.putString(QQShare.SHARE_TO_QQ_TITLE, share.Title);
//			if(share.Link != null && !share.Link.isEmpty())
//				params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.Link);
//			if(share.Description != null && !share.Description.isEmpty())
//				params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.Description);
//			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
//	        mQQShare.shareToQQ(mActivity, params, new IUiListener() {
//	              @Override
//	              public void onCancel() { }
//
//	              @Override
//	              public void onComplete(Object response) {
//	                    Toast.makeText(mContext, "onComplete: " + response.toString(), Toast.LENGTH_SHORT).show();
//	              }
//
//	              @Override
//	              public void onError(UiError e) {
//	                    Toast.makeText(mContext, "onError: " + e.errorMessage, Toast.LENGTH_SHORT).show();
//	              }
//	        });
//		}
//	}
//
//}
