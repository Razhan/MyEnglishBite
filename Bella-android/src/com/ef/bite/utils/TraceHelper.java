package com.ef.bite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import cn.trinea.android.common.util.StringUtils;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.TraceBiz;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.PostTracingTask;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;

/**
 * 追踪用户行为
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TraceHelper {

	public final static String KEY_QUESTION_ID = "question_id";
	public final static String KEY_QUESTION_TEXT = "question_text";
	public final static String KEY_ANWSER_ID = "answer_id";
	public final static String KEY_ANWSER_TEXT = "answer_text";
	public final static String Key_IS_Right = "is_correct";
	public final static String Key_Time_Spend = "time_spent";
	public final static String Key_ADs = "ad";
	public final static String KEY_COLLECT_AT = "collected_at";
	public final static String KEY_PAGE = "page";
	public final static String KEY_ACTION = "action";
	public final static String KEY_LABEL = "label";
	public final static String KEY_TARGET = "target";
	public final static String KEY_ERRORLOG = "errorLog";

	// Upsell values
	public final static String Value_ADs_Upsell = "upsell";
	public final static String Value_ADs_Call = "call";

	// Actions
	public final static String ACTION_CLICK = "click";
	public final static String ACTION_LOAD = "load";
	public final static String ACTION_ACTIVE = "active";

	// Pages
	public final static String PAGE_MAIN = "main";
	public final static String PAGE_LEADERBOARD = "leaderboard";
	public final static String PAGE_LOGIN = "login";
	public final static String PAGE_INVITE_FRIEND = "invite_friend";
	public final static String PAGE_WELL_DONE = "well_done";
	public final static String PAGE_LEVEL_UP = "level_up";
	public final static String PAGE_SHARE = "share";
	public final static String PAGE_SPLASH = "splash";
	public final static String PAGE_LANDING = "landing";
	public final static String PAGE_REGISTER = "register";
	public final static String PAGE_NOTIFICATION = "notification";

	// Targets
	public final static String TARGET_INVITE_FRIEND = "invite_friend";
	public final static String TARGET_LEADERBOARD = "leaderboard";
	public final static String TARGET_LOGIN = "login";
	public final static String TARGET_SHARE = "share";
	public final static String TARGET_WECHAT = "wechat";
	public final static String TARGET_SINA = "sina";
	public final static String TARGET_QQ = "qq";
	public final static String TARGET_WECHAT_MOMENTS = "wechat_moments";
	public final static String TARGET_FACEBOOK = "facebook";
	public final static String TARGET_TWITTER = "twitter";
	public final static String TARGET_CANCEL = "cancel";
	public final static String TARGET_REGISTER = "register";

	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS'Z'";

	/**
	 * 追踪多选题
	 * 
	 * @param questionId
	 *            问题ID： ChunkCode_QuestionOrder
	 * @param questionText
	 *            问题内容
	 * @param anwserId
	 *            答案ID：ChunkCode_QuetionOrder_AnwserOrder
	 * @param anwserText
	 *            答案内容
	 * @param isRight
	 *            是否正确
	 * @param timeSpend
	 *            消耗的时间
	 */
	// public static void tracingMultiChoice(Context context, boolean isPost,
	// String questionId, String questionText, String anwserId,
	// String anwserText, String isRight, String timeSpend) {
	// String groupId = UUID.randomUUID().toString();
	// String collect_date = getCollectDate();
	// TraceBiz traceBiz = new TraceBiz(context);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// KEY_QUESTION_ID, questionId);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// KEY_QUESTION_TEXT, questionText);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// KEY_ANWSER_ID, anwserId);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// KEY_ANWSER_TEXT, anwserText);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// Key_IS_Right, isRight);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// Key_Time_Spend, timeSpend);
	// traceBiz.insertTrace(AppConst.CurrUserInfo.UserId, groupId,
	// KEY_COLLECT_AT, collect_date);
	// if (isPost)
	// startPostTracing(context);
	// }

	/**
	 * 追踪广告的连接点击行为
	 * 
	 * @param context
	 */
	public static void tracingADsUpsell(Context context) {
		String groupId = UUID.randomUUID().toString();
		String collect_date = getCollectDate();
		TraceBiz traceBiz = new TraceBiz(context);
		// traceBiz.insertTrace(
		// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
		// : "0", groupId, Key_ADs, Value_ADs_Upsell);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.ActionADsUpsellKey.actionBannerCreativeClick,
				ContextDataMode.ActionADsUpsellValues.actionBannerCreativeClick);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, KEY_COLLECT_AT, collect_date);
		startPostTracing(context);

		MobclickTracking.OmnitureTrack.ActionTracingADsUpsell();
	}

	/**
	 * 追踪广告电话拨打行为
	 * 
	 * @param context
	 */
	public static void tracingADsCall(Context context) {
		String groupId = UUID.randomUUID().toString();
		String collect_date = getCollectDate();
		TraceBiz traceBiz = new TraceBiz(context);
		// traceBiz.insertTrace(
		// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
		// : "0", groupId, Key_ADs, Value_ADs_Call);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.ActionADsCallKey.actionBannerClick,
				ContextDataMode.ActionADsCallValues.actionBannerClick);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, KEY_COLLECT_AT, collect_date);
		startPostTracing(context);

		MobclickTracking.OmnitureTrack.ActionTracingADsCall();
	}

	/** 记录页面加载 **/
	// public static void tracingPage(Context context, String pageName) {
	// if (pageName == null || pageName.isEmpty())
	// return;
	// String groupId = UUID.randomUUID().toString();
	// String collect_date = getCollectDate();
	// TraceBiz traceBiz = new TraceBiz(context);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_PAGE, pageName);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_ACTION, ACTION_LOAD);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_COLLECT_AT, collect_date);
	// startPostTracing(context);
	// }

	/** 记录操作 **/
	// public static void tracingAction(Context context, String pageName,
	// String actionName, String label, String target) {
	// if (pageName == null || actionName == null || pageName.isEmpty()
	// || actionName.isEmpty())
	// return;
	// String groupId = UUID.randomUUID().toString();
	// String collect_date = getCollectDate();
	// TraceBiz traceBiz = new TraceBiz(context);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_PAGE, pageName);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_ACTION, actionName);
	// if (label != null && !label.isEmpty())
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_LABEL, label);
	// if (target != null && !target.isEmpty())
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_TARGET, target);
	// traceBiz.insertTrace(
	// AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
	// : "0", groupId, KEY_COLLECT_AT, collect_date);
	// startPostTracing(context);
	// }

	public static void tracingOmnAction(Context context, String actionKey,
			String actionValue) {
		if (StringUtils.isBlank(actionKey) || StringUtils.isBlank(actionValue)) {
			return;
		} else {
			String groupId = UUID.randomUUID().toString();
			String collect_date = getCollectDate();
			TraceBiz traceBiz = new TraceBiz(context);
			traceBiz.insertTrace(
					AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
							: "0", groupId, actionKey, actionValue);
			traceBiz.insertTrace(
					AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
							: "0", groupId, KEY_COLLECT_AT, collect_date);
			startPostTracing(context);
		}
	}

	/** 似omniture页面操作 **/
	public static void tracingOmnPage(Context context, String pageName,
			String sitesubsection, String sitesection) {
		if (pageName == null || sitesubsection == null || pageName.isEmpty()
				|| sitesection.isEmpty())
			return;
		String groupId = UUID.randomUUID().toString();
		String collect_date = getCollectDate();
		TraceBiz traceBiz = new TraceBiz(context);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, ContextDataMode.Keydata.pageName,
				pageName);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.Keydata.pageSiteSubSection, sitesubsection);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.Keydata.pageSiteSection, sitesection);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.Keydata.pagePreviousName,
				ContextDataMode.pagePreviousNameString);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, ContextDataMode.Keydata.userLanguage,
				AppLanguageHelper.getSystemLaunguage(context));
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, ContextDataMode.Keydata.globalAppName,
				ContextDataMode.globalAppNameValue);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId,
				ContextDataMode.Keydata.globalPlatformName, "Android");
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, KEY_COLLECT_AT, collect_date);
		startPostTracing(context);
	}

	/** 似omniture记录操作 **/
	public static void tracingErrorLog(Context context, String ErrorMessage) {
		if (ErrorMessage == null || ErrorMessage.isEmpty())
			return;
		String groupId = UUID.randomUUID().toString();
		String collect_date = getCollectDate();
		TraceBiz traceBiz = new TraceBiz(context);

		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, KEY_ERRORLOG, ErrorMessage);
		traceBiz.insertTrace(
				AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0", groupId, KEY_COLLECT_AT, collect_date);
		startPostTracing(context);
	}

	/** 开始上传 **/
	public static void startPostTracing(Context context) {
		if (!NetworkChecker.isConnected(context))
			return;
		if (AppConst.GlobalConfig.DeviceID == null
				|| AppConst.GlobalConfig.DeviceID == "")
			return;
		PostTracingTask postTracking = new PostTracingTask(context,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
						if (result != null && result.status != null)
							Log.i("PostTracingTask",
									"The tracing sync result: "
											+ (result.status.equals(0) ? "success!"
													: "fail!"));
					}
				});
		postTracking
				.execute(new String[] { AppConst.CurrUserInfo.IsLogin ? AppConst.CurrUserInfo.UserId
						: "0" });
	}

	private static String getCollectDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();
		return sdf.format(date);
	}
}
