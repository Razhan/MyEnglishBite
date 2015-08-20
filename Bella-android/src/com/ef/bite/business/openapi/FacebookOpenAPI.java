package com.ef.bite.business.openapi;
//package com.ef.myword2014.business.openapi;
//
//import android.app.Activity;
//
//import com.ef.myword2014.model.sso.LoginResponse;
//import com.ef.myword2014.model.sso.LogoutResponse;
//import com.ef.myword2014.model.sso.ShareRequest;
//import com.ef.myword2014.model.sso.UserInfoModel;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionDefaultAudience;
//import com.facebook.SessionLoginBehavior;
//import com.facebook.SessionState;
//import com.facebook.internal.SessionAuthorizationType;
//import com.facebook.internal.SessionTracker;
//import com.facebook.internal.Utility;
//import com.facebook.model.GraphUser;
//
//public class FacebookOpenAPI extends BaseOpenAPI {
//
//	private String applicationId = null;
//    private SessionTracker sessionTracker;
//    private GraphUser user = null;
//    private Session userInfoSession = null; // the Session used to fetch the current user info
//	public FacebookOpenAPI(Activity activity) {
//		super(activity);
//	}
//
//	@Override
//	public void login(RequestCallback<LoginResponse> callback) {
//		applicationId = Utility.getMetadataApplicationId(mContext);
//		Session currentSession = sessionTracker.getSession();
//        if (currentSession == null || currentSession.getState().isClosed()) {
//            sessionTracker.setSession(null);
//            Session session = new Session.Builder(mContext).setApplicationId(applicationId).build();
//            Session.setActiveSession(session);
//            currentSession = session;
//        }
//        if (!currentSession.isOpened()) {
//            Session.OpenRequest openRequest = null;
//            if (mContext instanceof Activity) {
//                openRequest = new Session.OpenRequest((Activity)mContext);
//            }
//            
//            if (openRequest != null) {
//                openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
//                //openRequest.setPermissions(properties.permissions);
//                openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
//                openRequest.setCallback(new Session.StatusCallback(){
//					@Override
//					public void call(Session session, SessionState state,Exception exception) {
//						sessionTracker.setSession(session);
//					}
//                });
//                currentSession.openForRead(openRequest);
//            }
//        }
//	}
//
//	@Override
//	public void logout(RequestCallback<LogoutResponse> callback) {
//		final Session openSession = sessionTracker.getOpenSession();
//        if (openSession != null) {
//        	 openSession.closeAndClearTokenInformation();
//        }
//	}
//
//	@Override
//	public void getUserInfo(RequestCallback<UserInfoModel> callback) {
//		final Session currentSession = sessionTracker.getOpenSession();
//        if (currentSession != null) {
//            if (currentSession != userInfoSession) {
//                Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
//                    @Override
//                    public void onCompleted(GraphUser me,  Response response) {
//                        if (currentSession == sessionTracker.getOpenSession()) {
//                        	UserInfoModel model = new UserInfoModel();
//                            //model.UserId = Integer.toString(me.getId());
//                            
//                        }
//                        if (response.getError() != null) {
//                            
//                        }
//                    }
//                });
//                Request.executeBatchAsync(request);
//                userInfoSession = currentSession;
//            }
//        }
//	}
//
//	@Override
//	public void share(ShareRequest share, RequestCallback<Boolean> callback) {
//		 
//	}
//}
