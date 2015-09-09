package com.ef.bite.business;

import java.util.List;

import com.ef.bite.dataacces.mode.Achievement;
import com.ef.bite.dataacces.mode.httpMode.*;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ef.bite.AppConst;
import com.ef.bite.utils.HttpRestfulClient;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.TraceHelper;

public class UserServerAPI extends BaseServerAPI {

	private Context context;

	public UserServerAPI(Context context) {
		super(context);
		this.context = context;
	}

	// 3.1.1 获取用户的profile *
	public HttpProfile getUserProfile(String id) {
		try {
			JSONObject param = new JSONObject();
			param.put("bella_id",id);
			String result =  HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "2/profile/", param.toString(),
					headerMap);
			HttpProfile httpProfile= (HttpProfile) JsonSerializeHelper
					.JsonDeserialize(result, HttpProfile.class);
			return httpProfile;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// post 上传用户的 profile *
	public HttpBaseMessage postUserProfile(HttpProfile.ProfileData profileData) {
		try {
			String dataString = JsonSerializeHelper.JsonSerializer(profileData);
			String userProfile = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "/profile/save", dataString,
					headerMap);
			HttpBaseMessage user = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(userProfile, HttpBaseMessage.class);
			return user;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// update用户的 profile *
	public HttpBaseMessage updateUserProfile(JSONObject profileData) {
		try {
			String dataString = profileData.toString();
			String userProfile = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "/profile/save", dataString,
					headerMap);
			HttpBaseMessage user = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(userProfile, HttpBaseMessage.class);
			return user;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}


	// 3.1.2 获取自己的friends profile friendsView *
	public HttpGetFriends GetAllFiendsProfile(String id) {
		try {
			HttpGetFriends user = (HttpGetFriends) HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "friend/" + id,
					HttpGetFriends.class);
			return user;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// 得到用户进度 get *
	public HttpProgress getProgress(String id) {
		try {
			HttpProgress data = (HttpProgress) HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "userprogress/" + id,
					HttpProgress.class);
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// 上传用户进度 *
	public HttpProgress postProgress(HttpProgressData data) {
		try {
			String dataString = JsonSerializeHelper.JsonSerializer(data);
			Log.i("Post Progress", dataString);
			String userProfile = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "userprogress/save",
					dataString, headerMap);
			HttpProgress user = (HttpProgress) JsonSerializeHelper
					.JsonDeserialize(userProfile, HttpProgress.class);
			return user;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// 指纹系统
	public boolean PosetFingerPrint(String os, String osversion) {
		try {
			JSONObject param = new JSONObject();
			param.put("os", os);
			param.put("osversion", osversion);
			HttpRestfulClient.JsonPost(AppConst.EFAPIs.BaseAddress
					+ "fingerprint", param.toString(), headerMap);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public HttpReviewVoiceResponse PostVoiceReview(
			HttpReviewVoiceRequest reviewVoiceRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(reviewVoiceRequest);
		String jsonString = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/review/", jsonParams,
				headerMap);
		HttpReviewVoiceResponse httpReviewVoiceResponse = (HttpReviewVoiceResponse) JsonSerializeHelper
				.JsonDeserialize(jsonString, HttpReviewVoiceResponse.class);
		return httpReviewVoiceResponse;
	}

	public HttpUserRecordingResponse GetUserRecordingView(
			HttpUserRecordingRequest httpUserRecordingRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpUserRecordingRequest);
		String jsonString = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "view/recording/", jsonParams,
				headerMap);
		HttpUserRecordingResponse httpUserRecordingResponse = (HttpUserRecordingResponse) JsonSerializeHelper
				.JsonDeserialize(jsonString, HttpUserRecordingResponse.class);
		return httpUserRecordingResponse;
	}

	// 贊record
	public void PostLikeVoice(HttpVoiceRequest httpLikeVoiceRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpLikeVoiceRequest);
		String jsonRestful = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/like/", jsonParams,
				headerMap);
		System.out.println("PostLikeVoice" + jsonRestful);
	}

	// 不贊record
	public void PostUnlikeVoice(HttpVoiceRequest httpLikeVoiceRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpLikeVoiceRequest);
		String jsonRestful = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/unlike/", jsonParams,
				headerMap);
		System.out.println("PostUnlikeVoice" + jsonRestful);
	}

	// 舉報record
	public void PostReportVoice(HttpVoiceRequest httpLikeVoiceRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpLikeVoiceRequest);
		String jsonRestful = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/report/", jsonParams,
				headerMap);
		System.out.println("PostReportVoice" + jsonRestful);
	}

	// 刪除uservoice
	public boolean PostVoiceDelete(HttpVoiceDeletRequest httpVoiceDeletRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpVoiceDeletRequest);
		String jsonRestful = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/delete/", jsonParams,
				headerMap);
		return true;
	}

	// QueryVoiceReviewers
	public HttpQueryVoiceReviewersResponse PostQueryVoiceReviewers(
			HttpQueryVoiceReviewersRequest httpQueryVoiceReviewersRequest) {
		String jsonParams = JsonSerializeHelper
				.JsonSerializer(httpQueryVoiceReviewersRequest);
		String jsonRestful = HttpRestfulClient.JsonPost(
				AppConst.EFAPIs.BaseAddress + "voice/reviewer/", jsonParams,
				headerMap);
		HttpQueryVoiceReviewersResponse httpQueryVoiceReviewersResponse = (HttpQueryVoiceReviewersResponse) JsonSerializeHelper
				.JsonDeserialize(jsonRestful,
						HttpQueryVoiceReviewersResponse.class);
		return httpQueryVoiceReviewersResponse;
	}

	// 搜索 所有
	public HttpGetFriends getSearchViewAll(String text) {
		if (text == null)
			return null;
		try {
			text = text.trim();
			text = text.replace(" ", "%20");
			HttpGetFriends data = (HttpGetFriends) HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "profile/search/" + text,
					HttpGetFriends.class);
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// 搜索 *
	public HttpGetFriends getSearchView(String text, Integer pageIndex,
			Integer pageSize) {
		try {
			HttpGetFriends data = (HttpGetFriends) HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "profile/search/" + text
							+ "/" + pageIndex + "/" + pageSize,
					HttpProfile.class);
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	// 查询朋友排名ByID
	public HttpGetFriends dashboard(String id) {
		try {
			String jsonFriends = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "dashboard/" + id, headerMap);
			HttpGetFriends httpGetFriends = (HttpGetFriends) JsonSerializeHelper
					.JsonDeserialize(jsonFriends, HttpGetFriends.class);
			return httpGetFriends;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	public HttpGetFriends leaderboardview(Integer start, Integer rows) {
		try {
			if (start == null || rows == null)
				return leaderboardview();
			String jsonFriends = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "leaderboard/global/" + start
							+ "/" + rows, headerMap);
			HttpGetFriends httpGetFriends = (HttpGetFriends) JsonSerializeHelper
					.JsonDeserialize(jsonFriends, HttpGetFriends.class);
			return httpGetFriends;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	public HttpGetFriends leaderboardview() {
		try {
			String jsonFriends = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "leaderboard/global",
					headerMap);
			HttpGetFriends httpGetFriends = (HttpGetFriends) JsonSerializeHelper
					.JsonDeserialize(jsonFriends, HttpGetFriends.class);
			return httpGetFriends;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	/**
	 * 朋友排名
	 * 
	 * @return
	 */
	public HttpGetFriends leaderboardFriends(String uid) {
		try {
			String jsonFriends = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "leaderboard/friend/" + uid,
					headerMap);
			HttpGetFriends httpGetFriends = (HttpGetFriends) JsonSerializeHelper
					.JsonDeserialize(jsonFriends, HttpGetFriends.class);
			return httpGetFriends;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	public HttpBaseMessage postTrack(List<HttpTrackData> dataList, String uid) {
		try {
			String jsonParams = JsonSerializeHelper.JsonSerializer(dataList);
			String jsonString = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "tracking/save", jsonParams,
					headerMap);
			HttpBaseMessage httpMessage = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpBaseMessage.class);
			return httpMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 提交头像
	 * 
	 * @return
	 */
	public HttpAvatarData postAvatar(String avatar) {
		try {
			String jsonString = HttpRestfulClient.uploadFile(
					AppConst.EFAPIs.BaseAddress + "avatar/upload", avatar,
					headerMap);
			HttpAvatarData httpMessage = (HttpAvatarData) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpAvatarData.class);
			return httpMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断一个用户是否是好友
	 * 
	 * @param uid
	 * @param friend_id
	 * @return
	 */
	public HttpIsMyFriend isMyFriend(String uid, String friend_id) {
		try {
			String jsonResponse = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "friend/is/" + uid + "/"
							+ friend_id, headerMap);
			HttpIsMyFriend httpResponse = (HttpIsMyFriend) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpIsMyFriend.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 添加好友，单向添加好友
	 * 
	 * @param uid
	 *            添加人
	 * @param friend_id
	 *            指定好友的bella id
	 * @return
	 */
	public HttpBaseMessage addFriend(String uid, String friend_id) {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("bella_id", uid);
			jsonObj.put("friend_bella_id", friend_id);
			jsonObj.put("start", 1);
			jsonObj.put("rows", 1);
			String jsonString = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "friend/add",
					jsonObj.toString(), headerMap);
			HttpBaseMessage httpMessage = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpBaseMessage.class);
			return httpMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过uid获得该用户的朋友数量
	 * 
	 * @param uid
	 * @return
	 */
	public HttpFriendCount getFriendCount(String uid) {
		try {
			String jsonString = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "friend/count/" + uid,
					headerMap);
			HttpFriendCount httpResponse = (HttpFriendCount) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpFriendCount.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得指定用户的所有通知列表
	 * 
	 * @param uid
	 * @return
	 */
	public HttpNotification getAllNotification(String uid) {
		try {
			String jsonString = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "notification/" + uid + "/"
							+ 200, headerMap);
			HttpNotification httpResponse = (HttpNotification) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpNotification.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得通知列表
	 * 
	 * @param uid
	 * @param rows
	 *            查询获得的页数
	 * @param start_id
	 *            从指定
	 * @return
	 */
	public HttpNotification getNotificationList(String uid, int rows,
			Integer start_id) {
		try {
			String url = null;
			if (start_id == null || start_id == 0)
				url = AppConst.EFAPIs.BaseAddress + "notification/" + uid + "/"
						+ rows;
			else
				url = AppConst.EFAPIs.BaseAddress + "notification/" + uid + "/"
						+ rows + "/" + start_id + "/";
			String jsonString = HttpRestfulClient.Get(url, headerMap);
			HttpNotification httpResponse = (HttpNotification) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpNotification.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取通知
	 * @return
	 */
	public HttpBaseMessage readNotification(ReadNotificationHttpRequest param) {
		try {
			String jsonParams = JsonSerializeHelper.JsonSerializer(param);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "notification/read",
					jsonParams, headerMap);
			HttpBaseMessage httpResponse = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpBaseMessage.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得未读取的通知数量
	 * 
	 * @return
	 */
	public HttpUnreadNotificationCount getUnreadNotificationCount(String uid) {
		try {
			String jsonResponse = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "notification/unread/count/"
							+ uid, headerMap);
			HttpUnreadNotificationCount httpResponse = (HttpUnreadNotificationCount) JsonSerializeHelper
					.JsonDeserialize(jsonResponse,
							HttpUnreadNotificationCount.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param uid
	 * @param old_pwd
	 * @param new_pwd
	 * @return
	 */
	public HttpBaseMessage passwordChange(String uid, String old_pwd,
			String new_pwd) {
		try {
			JSONObject params = new JSONObject();
			params.put("bella_id", uid);
			params.put("old_password", old_pwd);
			params.put("new_password", new_pwd);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "password/change",
					params.toString(), headerMap);
			HttpBaseMessage httpResp = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpBaseMessage.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 忘记密码，发送验证码到手机号
	 * 
	 * @param phone
	 * @return
	 */
	public HttpBaseMessage passwordForget(String phone) {
		try {
			JSONObject params = new JSONObject();
			params.put("phone", phone);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "password/forget",
					params.toString(), headerMap);
			HttpBaseMessage httpResp = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpBaseMessage.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过短信验证码重置密码
	 * 
	 * @param verify_code
	 * @param new_pwd
	 * @return
	 */
	public HttpBaseMessage passwordReset(String verify_code, String new_pwd) {
		try {
			JSONObject params = new JSONObject();
			params.put("verification_code", verify_code);
			params.put("new_password", new_pwd);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "password/reset",
					params.toString(), headerMap);
			HttpBaseMessage httpResp = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpBaseMessage.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得分享课程share link
	 * 
	 * @param uid
	 * @param course_id
	 * @param course_version
	 * @param src_language
	 * @param target_language
	 * @return
	 */
	public HttpShareLink getShareChunkLink(String uid, String course_id,
			String course_version, String src_language, String target_language) {
		try {
			JSONObject param = new JSONObject();
			param.put("bella_id", uid);
			param.put("course_id", course_id);
			param.put("course_version", course_version);
			param.put("source_language", src_language);
			param.put("target_language", target_language);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "course/share",
					param.toString(), headerMap);
			HttpShareLink httpResp = (HttpShareLink) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpShareLink.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得邀请好友的share link
	 * 
	 * @return
	 */
	public HttpShareLink getShareFriendLink(String uid, String language) {
		try {
			JSONObject param = new JSONObject();
			param.put("bella_id", uid);
			param.put("language", language);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "friend/invite",
					param.toString(), headerMap);
			HttpShareLink httpResp = (HttpShareLink) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpShareLink.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得广告链接
	 * 
	 * @return
	 */
	public HttpADsAddress getAdsLink() {
		try {
			String jsonResponse = HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "ad/"
							+ AppConst.CurrUserInfo.UserId + "/"
							+ AppConst.GlobalConfig.DeviceID, headerMap);
			HttpADsAddress httpResp = (HttpADsAddress) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpADsAddress.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得学习计划
	 * 
	 * @param id
	 * @return
	 */

	public HttpStudyPlans getStudyPlan(String id) {
		try {
			HttpStudyPlans studyPlan = (HttpStudyPlans) HttpRestfulClient.Get(
					AppConst.EFAPIs.BaseAddress + "studyplan/user/" + id,
					HttpStudyPlans.class);
			return studyPlan;
		} catch (Exception ex) {
			ex.printStackTrace();
			TraceHelper.tracingErrorLog(context, ex.getMessage());
			return null;
		}
	}

	/**
	 * get dashboard statistics info
	 * 
	 * @return
	 */
	public HttpDashboard getDashboard() {
		try {
			JSONObject param = new JSONObject();
			param.put("bella_id", AppConst.CurrUserInfo.UserId);
            param.put("plan_id", AppConst.CurrUserInfo.CourseLevel);

//			param.put("new_lesson_max_count", 5);
//			param.put("new_rehearsal_max_count", 10);
//			param.put("rank_friend_max_count", 4);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "2/dashboard/",
					param.toString(), headerMap);
			HttpDashboard httpResp = (HttpDashboard) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpDashboard.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * get Voice share info
	 * 
	 * @return
	 */
	public HttpVoiceShareRespone getVoiceShare(HttpVoiceShareRequest request) {
		try {
			String param = JsonSerializeHelper.JsonSerializer(request);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "voice/share/",
					param.toString(), headerMap);
			HttpVoiceShareRespone httpResp = (HttpVoiceShareRespone) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpVoiceShareRespone.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * post user practice achievement
	 *
	 * @return
	 */
	public HttpBaseMessage postAchievement(List<Achievement> achievements) {
		try {
			String param = JsonSerializeHelper.JsonSerializer(achievements);
			String jsonResponse = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "2/progress/update/",
					param.toString(), headerMap);
			HttpBaseMessage httpResp = (HttpBaseMessage) JsonSerializeHelper
					.JsonDeserialize(jsonResponse, HttpBaseMessage.class);
			return httpResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	//获取fb图片
    public HttpGetFBImageResponse getFBImage() {
        try {
            JSONObject param = new JSONObject();
            String result = HttpRestfulClient.JsonPost(
                    AppConst.EFAPIs.BaseAddress + "banner/facebook_invite_image/", param.toString(),
                    headerMap);
            HttpGetFBImageResponse fbImageResponse = (HttpGetFBImageResponse) JsonSerializeHelper
                    .JsonDeserialize(result, HttpGetFBImageResponse.class);
            return fbImageResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            TraceHelper.tracingErrorLog(context, ex.getMessage());
            return null;
        }
    }

}
