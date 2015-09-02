package com.ef.bite.Tracking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.ef.bite.AppConst;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.LogManager;
import com.ef.bite.utils.TraceHelper;
//import com.umeng.analytics.AnalyticsConfig;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.analytics.ReportPolicy;

public class MobclickTracking {

    @SuppressLint("SimpleDateFormat")
    public static class OmnitureTrack {
        static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        public static void CreateContext(Context context) {
            Config.setContext(context);
        }

        public static void ResumeCollectingLifecycleData() {
            Config.collectLifecycleData();
        }

        public static void PauseCollectingLifecycleData() {
            Config.pauseCollectingLifecycleData();
        }

        // public static void SplashTrackState(Context context, String PageView,
        // String WooPage, String UserId, String Language) {
        // HashMap<String, Object> contextData = new HashMap<String, Object>();
        // if (WooPage != null || WooPage != "") {
        // contextData.put(ContextDataMode.Keydata.pageName, WooPage);
        // }
        // if (UserId != null || UserId != "") {
        // contextData.put(ContextDataMode.Keydata.userMemberId, UserId);
        // }
        // contextData.put(ContextDataMode.Keydata.pageName,
        // ContextDataMode.SplashValues.pageNameValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSection,
        // ContextDataMode.SplashValues.pageSiteSectionValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
        // ContextDataMode.SplashValues.pageSiteSubSectionValue);
        // contextData.put(ContextDataMode.Keydata.userLanguage, Language);
        // contextData.put(ContextDataMode.Keydata.globalAppName,
        // ContextDataMode.globalAppNameValue);
        // contextData.put(ContextDataMode.Keydata.globalPlatformName,
        // ContextDataMode.globalPlatformNameValue);
        // contextData.put(ContextDataMode.Keydata.userDate,
        // formatter.format(new Date()));
        //
        // Analytics.trackState(PageView, contextData);
        // }
        //
        // public static void ExternalHomeTrackState(Context context,
        // String UserId, String language) {
        // HashMap<String, Object> contextData = new HashMap<String, Object>();
        // contextData.put(ContextDataMode.Keydata.pageName,
        // ContextDataMode.ExternalHomeValues.pageNameValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
        // ContextDataMode.ExternalHomeValues.pageSiteSubSectionValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSection,
        // ContextDataMode.ExternalHomeValues.pageSiteSectionValue);
        // if (ContextDataMode.pagePreviousNameString != null
        // || ContextDataMode.pagePreviousNameString != "") {
        // {
        // contextData.put(ContextDataMode.Keydata.pagePreviousName,
        // ContextDataMode.pagePreviousNameString);
        // }
        // if (!UserId.equals("0")) {
        // contextData.put(ContextDataMode.Keydata.userMemberId,
        // UserId);
        // }
        //
        // contextData.put(ContextDataMode.Keydata.userLanguage, language);
        // contextData.put(ContextDataMode.Keydata.globalAppName,
        // ContextDataMode.globalAppNameValue);
        // contextData.put(ContextDataMode.Keydata.globalPlatformName,
        // ContextDataMode.globalPlatformNameValue);
        // contextData.put(ContextDataMode.Keydata.userDate,
        // formatter.format(new Date()));
        // Analytics.trackState(ContextDataMode.Keydata.External_home,
        // contextData);
        // }
        // }
        //
        // public static void LoginTrackState(Context context,
        // String previousName, String UserId, String Language) {
        // HashMap<String, Object> contextData = new HashMap<String, Object>();
        // contextData.put(ContextDataMode.Keydata.pageName,
        // ContextDataMode.LoginValues.pageNameValue_phone);
        // contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
        // ContextDataMode.LoginValues.pageSiteSubSectionValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSection,
        // ContextDataMode.LoginValues.pageSiteSectionValue);
        // contextData.put(ContextDataMode.Keydata.pagePreviousName,
        // previousName);
        // contextData.put(ContextDataMode.Keydata.userMemberId, UserId);
        // contextData.put(ContextDataMode.Keydata.userLanguage, Language);
        // contextData.put(ContextDataMode.Keydata.globalAppName,
        // ContextDataMode.globalAppNameValue);
        // contextData.put(ContextDataMode.Keydata.globalPlatformName,
        // ContextDataMode.globalPlatformNameValue);
        // contextData.put(ContextDataMode.Keydata.userDate,
        // formatter.format(new Date()));
        // Analytics.trackState(ContextDataMode.Keydata.Login, contextData);
        // }
        //
        // public static void ForgotTrackState(Context context,
        // String previousName, String UserId, String Language) {
        // HashMap<String, Object> contextData = new HashMap<String, Object>();
        // contextData.put(ContextDataMode.Keydata.pageName,
        // ContextDataMode.LoginValues.pageNameValue_password);
        // contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
        // ContextDataMode.LoginValues.pageSiteSubSectionValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSection,
        // ContextDataMode.LoginValues.pageSiteSectionValue);
        // contextData.put(ContextDataMode.Keydata.pagePreviousName,
        // previousName);
        // contextData.put(ContextDataMode.Keydata.userMemberId, UserId);
        // contextData.put(ContextDataMode.Keydata.userLanguage, Language);
        // contextData.put(ContextDataMode.Keydata.globalAppName,
        // ContextDataMode.globalAppNameValue);
        // contextData.put(ContextDataMode.Keydata.globalPlatformName,
        // ContextDataMode.globalPlatformNameValue);
        // contextData.put(ContextDataMode.Keydata.userDate,
        // formatter.format(new Date()));
        // Analytics.trackState(ContextDataMode.Keydata.Login, contextData);
        // }

        // public static void RegisterTrackState(Context context, String UserId,
        // String Language) {
        // HashMap<String, Object> contextData = new HashMap<String, Object>();
        // contextData.put(ContextDataMode.Keydata.pageName,
        // ContextDataMode.RegisterNameValues.pageNameValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
        // ContextDataMode.RegisterNameValues.pageSiteSubSectionValue);
        // contextData.put(ContextDataMode.Keydata.pageSiteSection,
        // ContextDataMode.RegisterNameValues.pageSiteSectionValue);
        // contextData.put(ContextDataMode.Keydata.pagePreviousName,
        // ContextDataMode.pagePreviousNameString);
        // contextData.put(ContextDataMode.Keydata.userMemberId, UserId);
        // contextData.put(ContextDataMode.Keydata.userLanguage, Language);
        // contextData.put(ContextDataMode.Keydata.globalAppName,
        // ContextDataMode.globalAppNameValue);
        // contextData.put(ContextDataMode.Keydata.globalPlatformName,
        // ContextDataMode.globalPlatformNameValue);
        // contextData.put(ContextDataMode.Keydata.userDate,
        // formatter.format(new Date()));
        // Analytics.trackState(ContextDataMode.Keydata.Register, contextData);
        // }

        public static void AnalyticsTrackState(String PageName,
                                               String PageSiteSubSection, String PageSiteSection,
                                               Context context) {
            HashMap<String, Object> contextData = new HashMap<String, Object>();
            contextData.put(ContextDataMode.Keydata.pageName, PageName);
            contextData.put(ContextDataMode.Keydata.pageSiteSubSection,
                    PageSiteSubSection);
            contextData.put(ContextDataMode.Keydata.pageSiteSection,
                    PageSiteSection);
            contextData.put(ContextDataMode.Keydata.pagePreviousName,
                    ContextDataMode.pagePreviousNameString);
            contextData.put(ContextDataMode.Keydata.userMemberId,
                    AppConst.CurrUserInfo.UserId);
            contextData.put(ContextDataMode.Keydata.userLanguage,
                    AppLanguageHelper.getSystemLaunguage(context));
            contextData.put(ContextDataMode.Keydata.globalAppName,
                    ContextDataMode.globalAppNameValue);
            contextData.put(ContextDataMode.Keydata.globalPlatformName,
                    ContextDataMode.globalPlatformNameValue);
            contextData.put(ContextDataMode.Keydata.userDate,
                    formatter.format(new Date()));
            Analytics.trackState(PageName, contextData);
            // local_log
            LogManager.definedLog(PageName);
            // tracking
            TraceHelper.tracingOmnPage(context, PageName, PageSiteSubSection,
                    PageSiteSection);

            ContextDataMode.pagePreviousNameString = PageName;
        }

        public static void ActionDialogue(int i) {
            HashMap<String, Object> actionDate = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionDate
                            .put(ContextDataMode.actionTabCancelLocation,
                                    ContextDataMode.ActionAudioDialogueValues.TabCancelLocation);
                    Analytics.trackAction(ContextDataMode.actionTabCancelLocation,
                            actionDate);
                    break;
                case 2:
                    actionDate
                            .put(ContextDataMode.ActionDialogueKey.actionParagraphOpen,
                                    ContextDataMode.ActionAudioDialogueValues.paragraphOpen);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionParagraphOpen,
                            actionDate);
                    break;
                case 3:
                    actionDate.put(
                            ContextDataMode.ActionDialogueKey.actionAudioPlay,
                            ContextDataMode.ActionAudioDialogueValues.audioPlay);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionAudioPlay,
                            actionDate);
                    break;
                case 4:
                    actionDate.put(
                            ContextDataMode.ActionDialogueKey.actionAudioPause,
                            ContextDataMode.ActionAudioDialogueValues.audioPause);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionAudioPause,
                            actionDate);
                    break;
                case 5:
                    actionDate
                            .put(ContextDataMode.ActionDialogueKey.actionAudioPlayAgain,
                                    ContextDataMode.ActionAudioDialogueValues.audioPlayAgain);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionAudioPlayAgain,
                            actionDate);
                    break;
                case 6:
                    actionDate
                            .put(ContextDataMode.ActionDialogueKey.actionAudioPlayLocation,
                                    ContextDataMode.ActionAudioDialogueValues.audioPlayLocation_dialogue);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionAudioPlayLocation,
                            actionDate);
                    break;
                case 7:
                    actionDate
                            .put(ContextDataMode.ActionDialogueKey.actionAudioPlayLocation,
                                    ContextDataMode.ActionAudioDialogueValues.audioPlayLocation_phrase);
                    Analytics.trackAction(
                            ContextDataMode.ActionDialogueKey.actionAudioPlayLocation,
                            actionDate);
                    break;

            }
            LogManager.definedLog(String.valueOf(i));
        }

        public static void ActionPhrasepresentation(int i) {
            HashMap<String, Object> actionDate = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionDate
                            .put(ContextDataMode.ActionPhrasepresentationKey.actionAudioPlay,
                                    ContextDataMode.ActionPhrasepresentationValues.audioPlay);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionPhrasepresentationKey.actionAudioPlay,
                                    actionDate);
                    break;

                case 2:
                    actionDate
                            .put(ContextDataMode.ActionPhrasepresentationKey.actionTranslationSee,
                                    ContextDataMode.ActionPhrasepresentationValues.translationSee);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionPhrasepresentationKey.actionTranslationSee,
                                    actionDate);
                    break;
                case 3:
                    actionDate
                            .put(ContextDataMode.ActionPhrasepresentationKey.actionScrollNavigation,
                                    ContextDataMode.ActionPhrasepresentationValues.scrollNavigation);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionPhrasepresentationKey.actionScrollNavigation,
                                    actionDate);
                    break;
            }
        }

        public static void ActionBallonQuestion(int i) {
            HashMap<String, Object> actionDate = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionDate
                            .put(ContextDataMode.actionTabCancelLocation,
                                    ContextDataMode.ActionBalloonQuestionValues.TabCancelLocation);
                    Analytics.trackAction(ContextDataMode.actionTabCancelLocation,
                            actionDate);
                    break;

                case 2:
                    actionDate
                            .put(ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksIncorrect,
                                    ContextDataMode.ActionBalloonQuestionValues.BallonClicksIncorrect);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksIncorrect,
                                    actionDate);
                    break;
                case 3:
                    actionDate
                            .put(ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksCorrect,
                                    ContextDataMode.ActionBalloonQuestionValues.BallonClicksCorrect);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksCorrect,
                                    actionDate);
                    break;
            }
        }

        /**
         * @param i 1:Tryagain 2:to learn
         */
        public static void ActionBalloonFailure(int i) {
            HashMap<String, Object> actionDate = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionDate
                            .put(ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
                                    ContextDataMode.ActionBalloonFailureValues.timeUpChoice_TryAgain);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
                                    actionDate);
                    break;

                case 2:
                    actionDate
                            .put(ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
                                    ContextDataMode.ActionBalloonFailureValues.timeUpchoice_Learn);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
                                    actionDate);
                    break;
            }
        }

        /**
         * @param i 1:Wechatfriends 2:Weibo 3:Wechatmoments 4:QQ
         */
        public static void ActionShareListChina(int i) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_WeChatfriends);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    actionData);
                    break;

                case 2:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_Weibo);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    actionData);
                    break;
                case 3:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_Wechatmoments);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    actionData);
                    break;
                case 4:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_QQ);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
                                    actionData);
                    break;
                case 5:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShare,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialShare);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialShare,
                                    actionData);
                    break;
                case 6:
                    actionData
                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialMessageCancel,
                                    ContextDataMode.ActionShareListChinaValues.actionSocialMessageCancel);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionShareListChinaKey.actionSocialMessageCancel,
                                    actionData);
                    break;
            }
            LogManager.definedLog(String.valueOf(i));
        }

        public static void ActionLevelUpMessage() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionLevelUpMessageKey.actionLevelUpShare,
                            ContextDataMode.ActionLevelUpMessageValues.actionLevelUpShare);
            Analytics.trackAction(
                    ContextDataMode.ActionLevelUpMessageKey.actionLevelUpShare,
                    actionData);
        }

        public static void ActionPhraseLearnedMessage(int index) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();

            actionData
                    .put(ContextDataMode.ActionPhraseLearnedMessageKey.actionPhraseShare,
                            ContextDataMode.ActionPhraseLearnedMessageValues.actionPhraseShare);
            Analytics.trackAction(
                    ContextDataMode.ActionPhraseLearnedMessageKey.actionPhraseShare,
                    actionData);
            actionData
                    .put(ContextDataMode.ActionPhraseLearnedMessageKey.actionPhraseShareLocation,
                            ContextDataMode.ActionPhraseLearnedMessageValues.actionPhraseShareLocation[index]);
            Analytics.trackAction(
                    ContextDataMode.ActionPhraseLearnedMessageKey.actionPhraseShareLocation,
                    actionData);
        }

        public static void ActionInbox() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(ContextDataMode.ActionInboxKey.actionFriendsAdd,
                    ContextDataMode.ActionInboxValues.actionFriendsAdd);
            Analytics
                    .trackAction(
                            ContextDataMode.ActionInboxKey.actionFriendsAdd,
                            actionData);
        }

        public static void ActionFriendsList() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionFriendsLisKey.actionAlphabetClick,
                            ContextDataMode.ActionFriendsListValues.actionAlphabetClick);
            Analytics.trackAction(
                    ContextDataMode.ActionFriendsLisKey.actionAlphabetClick,
                    actionData);
        }

        public static void ActionProfileAddFriends() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionProfileAddFriendsKey.actionFriendsAdd,
                            ContextDataMode.ActionAddFriendsValues.actionFriendsAdd);
            Analytics
                    .trackAction(
                            ContextDataMode.ActionProfileAddFriendsKey.actionFriendsAdd,
                            actionData);
        }

        public static void ActionSearch(int i) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 0:
                    actionData
                            .put(ContextDataMode.ActionSearchKey.actionSearch,
                                    ContextDataMode.ActionSearchVocabularylist.actionSearch);
                    Analytics.trackAction(
                            ContextDataMode.ActionSearchKey.actionSearch,
                            actionData);
                    break;

                case 1:
                    actionData
                            .put(ContextDataMode.ActionSearchKey.actionSearchCancel,
                                    ContextDataMode.ActionSearchVocabularylist.actionSearchcancel);
                    Analytics.trackAction(
                            ContextDataMode.ActionSearchKey.actionSearchCancel,
                            actionData);
                    break;

                case 2:
                    break;
            }
        }

        public static void ActionSearchLocation(int i) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 0:
                    actionData
                            .put(ContextDataMode.ActionSearchLocationKey.actionSearchLocation,
                                    ContextDataMode.ActionSearchVocabularylist.actionSearchLocation_rehearse);
                    Analytics.trackAction(
                            ContextDataMode.ActionSearchLocationKey.actionSearchLocation,
                            actionData);
                    break;

                case 1:
                    actionData
                            .put(ContextDataMode.ActionSearchLocationKey.actionSearchLocation,
                                    ContextDataMode.ActionSearchVocabularylist.actionSearchLocation_mastered);
                    Analytics.trackAction(
                            ContextDataMode.ActionSearchLocationKey.actionSearchLocation,
                            actionData);
                    break;

                case 2:
                    break;
            }
        }



        public static void ActionSettings(int i, int index) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionData
                            .put(ContextDataMode.ActionSettingsKey.actionAvatarUpdateClick,
                                    ContextDataMode.ActionSettingsValues.actionAvatarUpdateClickValues);
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionSettingsKey.actionAvatarUpdateClick,
                                    actionData);
                    break;
                case 2:
                    if (index == 1) {
                        actionData
                                .put(ContextDataMode.ActionSettingsKey.actionNotificationChoice,
                                        ContextDataMode.ActionSettingsValues.actionNotificationChoiceValues_ON);
                        Analytics
                                .trackAction(
                                        ContextDataMode.ActionSettingsKey.actionNotificationChoice,
                                        actionData);
                    } else if (index == 2) {
                        actionData
                                .put(ContextDataMode.ActionSettingsKey.actionNotificationChoice,
                                        ContextDataMode.ActionSettingsValues.actionNotificationChoiceValues_OFF);
                        Analytics
                                .trackAction(
                                        ContextDataMode.ActionSettingsKey.actionNotificationChoice,
                                        actionData);
                    }
                    break;
                case 3:
                    if (index == 1) {
                        actionData
                                .put(ContextDataMode.ActionSettingsKey.actionSoundEffectsChoice,
                                        ContextDataMode.ActionSettingsValues.actionSoundEffectsChoiceValues_ON);
                        Analytics
                                .trackAction(
                                        ContextDataMode.ActionSettingsKey.actionSoundEffectsChoice,
                                        actionData);
                    } else if (index == 2) {
                        actionData
                                .put(ContextDataMode.ActionSettingsKey.actionSoundEffectsChoice,
                                        ContextDataMode.ActionSettingsValues.actionSoundEffectsChoiceValues_OFF);
                        Analytics
                                .trackAction(
                                        ContextDataMode.ActionSettingsKey.actionSoundEffectsChoice,
                                        actionData);
                    }
                    break;
                case 4:
                    actionData
                            .put(ContextDataMode.ActionSettingsKey.actionLogOut,
                                    ContextDataMode.ActionSettingsValues.actionLogOutValues);
                    Analytics.trackAction(
                            ContextDataMode.ActionSettingsKey.actionLogOut,
                            actionData);
                    break;
            }
        }

        public static void ActionInviteaFriend(int i) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 1:
                    actionData
                            .put(ContextDataMode.ActionInviteaFriendKey.actionWechatInvite,
                                    "1");
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionInviteaFriendKey.actionWechatInvite,
                                    actionData);
                    break;

                case 2:
                    actionData
                            .put(ContextDataMode.ActionInviteaFriendKey.actionFriendSearch,
                                    "1");
                    Analytics
                            .trackAction(
                                    ContextDataMode.ActionInviteaFriendKey.actionFriendSearch,
                                    actionData);
                    break;
            }
        }

        public static void ActionRegisterSuccessful(String type) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionRegisterSuccessfulKey.actionRegisterSuccessful,
                            ContextDataMode.ActionRegisterSuccessfulValues.actionRegisterSuccessful);
            actionData
                    .put(ContextDataMode.ActionRegisterTypeKey.registerType, type);
            Analytics
                    .trackAction(
                            ContextDataMode.ActionRegisterSuccessfulKey.actionRegisterSuccessful,
                            actionData);
        }



        public static void ActionTracingADsCall() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(ContextDataMode.ActionADsCallKey.actionBannerClick,
                    ContextDataMode.ActionADsCallValues.actionBannerClick);
            Analytics.trackAction(
                    ContextDataMode.ActionADsCallKey.actionBannerClick,
                    actionData);
        }

        public static void ActionTracingADsUpsell() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionADsUpsellKey.actionBannerCreativeClick,
                            ContextDataMode.ActionADsUpsellKey.actionBannerCreativeClick);
            Analytics
                    .trackAction(
                            ContextDataMode.ActionADsUpsellKey.actionBannerCreativeClick,
                            actionData);
        }

        public static void ActionTracingReview(Context context) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(ContextDataMode.ActionReviewKey.actionrate,
                    ContextDataMode.ActionReviewValues.actionrate);
            Analytics.trackAction(ContextDataMode.ActionReviewKey.actionrate,
                    actionData);

            TraceHelper.tracingOmnAction(context,
                    ContextDataMode.ActionReviewKey.actionrate,
                    ContextDataMode.ActionReviewValues.actionrate);
        }

        public static void ActionTrackingRecording(int i) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            switch (i) {
                case 0:
                actionData.put(ContextDataMode.ActionRecordingKey.actionrecordStatus,
                        ContextDataMode.ActionRecordingValues.actionrecordStatus);
                Analytics.trackAction(ContextDataMode.ActionRecordingKey.actionrecordStatus,
                        actionData);
                    break;
                case 1:
                    actionData.put(ContextDataMode.ActionRecordingKey.actionrecord,
                            ContextDataMode.ActionRecordingValues.actionrecord);
                    Analytics.trackAction(ContextDataMode.ActionRecordingKey.actionrecord,
                            actionData);
                    break;
            }
        }

        public static void ActionTrackingRecordingSuccessful(Context context) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData
                    .put(ContextDataMode.ActionRecordingSuccessfulKey.actionrecordingstatus,
                            ContextDataMode.ActionRecordingSuccessfulValues.actionrecordStatus);
            Analytics
                    .trackAction(
                            ContextDataMode.ActionRecordingSuccessfulKey.actionrecordingstatus,
                            actionData);
            TraceHelper
                    .tracingOmnAction(
                            context,
                            ContextDataMode.ActionRecordingSuccessfulKey.actionrecordingstatus,
                            ContextDataMode.ActionRecordingSuccessfulValues.actionrecordStatus);
        }

        public static void ActionTrackingUserRecordShare() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(
                    ContextDataMode.ActionUserRecordShareKey.actionShare,
                    ContextDataMode.ActionUserRecordShareValues.actionshare);
            Analytics.trackAction(
                    ContextDataMode.ActionUserRecordShareKey.actionShare,
                    actionData);
        }

        public static void ActionTrackingBallonIntroduction() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(
                    ContextDataMode.BalloonIntroductionkeys.actionTabCancelLocationKey,
                    ContextDataMode.BalloonIntroductionValues.actionTabCancelLocationValue);
            Analytics.trackAction(
                    ContextDataMode.BalloonIntroductionkeys.actionTabCancelLocationKey,
                    actionData);
        }

        public static void ActionTrackingLearned() {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put(
                    ContextDataMode.ActionLearnedKey.actionlearnedkey,
                    ContextDataMode.ActionLearnedValue.actionlearnedvalue);
            Analytics.trackAction(
                    ContextDataMode.ActionLearnedKey.actionlearnedkey,
                    actionData);
        }

        public static void actionFormErrorType(String ErrorType) {
            HashMap<String, Object> actionData = new HashMap<String, Object>();
            actionData.put("action.formErrorType", ErrorType);
            Analytics.trackAction("action.formErrorType", actionData);
        }
    }

}

//    public static class UmengTrack {
//        static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//        public static void openDurationTrack(boolean isDuration,
//                                             Context context, boolean isDebugMode) {
//            MobclickAgent.openActivityDurationTrack(isDuration);
//            MobclickAgent.updateOnlineConfig(context);
//            AnalyticsConfig.setReportPolicy(ReportPolicy.BATCH_AT_LAUNCH,
//                    ReportPolicy.REALTIME);
//            MobclickAgent.setDebugMode(isDebugMode);
//        }
//
//        public static void setResume(Context context) {
//            MobclickAgent.onResume(context);
//
//        }
//
//        public static void setPause(Context context) {
//
//            MobclickAgent.onPause(context);
//
//        }
//
//        public static void setPageStart(String PageName,
//                                        String PageSiteSubSection, String PageSiteSection,
//                                        Context context) {
//            MobclickAgent.onPageStart(PageName);
//            MobclickAgent.onPageStart(PageSiteSubSection);
//            MobclickAgent.onPageStart(PageSiteSection);
//            MobclickAgent.onPageStart(ContextDataMode.pagePreviousNameString);
//            MobclickAgent.onPageStart(AppConst.CurrUserInfo.UserId);
//            MobclickAgent.onPageStart(AppLanguageHelper
//                    .getSystemLaunguage(context));
//            MobclickAgent.onPageStart(ContextDataMode.globalAppNameValue);
//            MobclickAgent.onPageStart(ContextDataMode.globalPlatformNameValue);
//            MobclickAgent.onPageStart(formatter.format(new Date()));
//        }
//
//        public static void setPageEnd(String PageName,
//                                      String PageSiteSubSection, String PageSiteSection,
//                                      Context context) {
//            MobclickAgent.onPageEnd(PageName);
//            MobclickAgent.onPageEnd(PageSiteSubSection);
//            MobclickAgent.onPageEnd(PageSiteSection);
//            MobclickAgent.onPageEnd(ContextDataMode.pagePreviousNameString);
//            MobclickAgent.onPageEnd(AppConst.CurrUserInfo.UserId);
//            MobclickAgent.onPageEnd(AppLanguageHelper
//                    .getSystemLaunguage(context));
//            MobclickAgent.onPageEnd(ContextDataMode.globalAppNameValue);
//            MobclickAgent.onPageEnd(ContextDataMode.globalPlatformNameValue);
//            MobclickAgent.onPageEnd(formatter.format(new Date()));
//        }
//
//        public static void ActionDialogue(int i, Context context) {
//            HashMap<String, String> actionDate = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionDate
//                            .put(ContextDataMode.actionTabCancelLocation,
//                                    ContextDataMode.ActionAudioDialogueValues.TabCancelLocation);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.actionTabCancelLocation, actionDate);
//                    break;
//                case 2:
//                    actionDate
//                            .put(ContextDataMode.ActionDialogueKey.actionParagraphOpen,
//                                    ContextDataMode.ActionAudioDialogueValues.paragraphOpen);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.ActionDialogueKey.actionParagraphOpen,
//                            actionDate);
//                    break;
//                case 3:
//                    actionDate.put(
//                            ContextDataMode.ActionDialogueKey.actionAudioPlay,
//                            ContextDataMode.ActionAudioDialogueValues.audioPlay);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.ActionDialogueKey.actionAudioPlay,
//                            actionDate);
//                    break;
//                case 4:
//                    actionDate.put(
//                            ContextDataMode.ActionDialogueKey.actionAudioPause,
//                            ContextDataMode.ActionAudioDialogueValues.audioPause);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.ActionDialogueKey.actionAudioPause,
//                            actionDate);
//                    break;
//                case 5:
//                    actionDate
//                            .put(ContextDataMode.ActionDialogueKey.actionAudioPlayAgain,
//                                    ContextDataMode.ActionAudioDialogueValues.audioPlayAgain);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.ActionDialogueKey.actionAudioPlayAgain,
//                            actionDate);
//                    break;
//            }
//        }
//
//        public static void ActionPhrasepresentation(int i, Context context) {
//            HashMap<String, String> actionDate = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionDate
//                            .put(ContextDataMode.ActionPhrasepresentationKey.actionAudioPlay,
//                                    ContextDataMode.ActionPhrasepresentationKey.actionAudioPlay);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionPhrasepresentationKey.actionAudioPlay,
//                                    actionDate);
//                    break;
//
//                case 2:
//                    actionDate
//                            .put(ContextDataMode.ActionPhrasepresentationKey.actionTranslationSee,
//                                    ContextDataMode.ActionPhrasepresentationValues.translationSee);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionPhrasepresentationKey.actionTranslationSee,
//                                    actionDate);
//                    break;
//                case 3:
//                    actionDate
//                            .put(ContextDataMode.ActionPhrasepresentationKey.actionScrollNavigation,
//                                    ContextDataMode.ActionPhrasepresentationValues.scrollNavigation);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionPhrasepresentationKey.actionScrollNavigation,
//                                    actionDate);
//                    break;
//            }
//
//        }
//
//        public static void ActionBallonQuestion(int i, Context context) {
//            HashMap<String, String> actionDate = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionDate
//                            .put(ContextDataMode.actionTabCancelLocation,
//                                    ContextDataMode.ActionBalloonQuestionValues.TabCancelLocation);
//                    MobclickAgent.onEvent(context,
//                            ContextDataMode.actionTabCancelLocation, actionDate);
//                    break;
//
//                case 2:
//                    actionDate
//                            .put(ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksIncorrect,
//                                    ContextDataMode.ActionBalloonQuestionValues.BallonClicksIncorrect);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksIncorrect,
//                                    actionDate);
//                    break;
//                case 3:
//                    actionDate
//                            .put(ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksCorrect,
//                                    ContextDataMode.ActionBalloonQuestionValues.BallonClicksCorrect);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionBalloonQuestionKey.actionBallonClicksCorrect,
//                                    actionDate);
//                    break;
//            }
//        }
//
//        public static void ActionBalloonFailure(int i, Context context) {
//            HashMap<String, String> actionDate = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionDate
//                            .put(ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
//                                    ContextDataMode.ActionBalloonFailureValues.timeUpChoice_TryAgain);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
//                                    actionDate);
//                    break;
//
//                case 2:
//                    actionDate
//                            .put(ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
//                                    ContextDataMode.ActionBalloonFailureValues.timeUpchoice_Learn);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionBalloonFailureKey.actionTimeUpChoice,
//                                    actionDate);
//                    break;
//            }
//        }
//
//        /**
//         * @param i 1:Wechatfriends 2:Weibo 3:Wechatmoments 4:QQ
//         */
//        public static void ActionShareListChina(int i, Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_WeChatfriends);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    actionData);
//                    break;
//
//                case 2:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_Weibo);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    actionData);
//                    break;
//                case 3:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_Wechatmoments);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    actionData);
//                    break;
//                case 4:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialShareType_QQ);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialShareType,
//                                    actionData);
//                    break;
//                case 5:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialShare,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialShare);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialShare,
//                                    actionData);
//                    break;
//                case 6:
//                    actionData
//                            .put(ContextDataMode.ActionShareListChinaKey.actionSocialMessageCancel,
//                                    ContextDataMode.ActionShareListChinaValues.actionSocialMessageCancel);
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionShareListChinaKey.actionSocialMessageCancel,
//                                    actionData);
//                    break;
//            }
//            LogManager.definedLog(String.valueOf(i));
//        }
//
//        public static void ActionLevelUpMessage(Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            actionData
//                    .put(ContextDataMode.ActionLevelUpMessageKey.actionLevelUpShare,
//                            ContextDataMode.ActionLevelUpMessageValues.actionLevelUpShare);
//            MobclickAgent.onEvent(context,
//                    ContextDataMode.ActionLevelUpMessageKey.actionLevelUpShare,
//                    actionData);
//        }
//
//        public static void ActionInbox(Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            actionData.put(ContextDataMode.ActionInboxKey.actionFriendsAdd,
//                    ContextDataMode.ActionInboxValues.actionFriendsAdd);
//            MobclickAgent
//                    .onEvent(context,
//                            ContextDataMode.ActionInboxKey.actionFriendsAdd,
//                            actionData);
//        }
//
//        public static void ActionFriendsList(Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            actionData
//                    .put(ContextDataMode.ActionFriendsLisKey.actionAlphabetClick,
//                            ContextDataMode.ActionFriendsListValues.actionAlphabetClick);
//            MobclickAgent.onEvent(context,
//                    ContextDataMode.ActionFriendsLisKey.actionAlphabetClick,
//                    actionData);
//        }
//
//        public static void ActionSearch(Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            actionData.put(ContextDataMode.ActionSearchKey.actionSearch,
//                    ContextDataMode.ActionSearchVocabularylist.actionSearch);
//            MobclickAgent.onEvent(context,
//                    ContextDataMode.ActionSearchKey.actionSearch, actionData);
//        }
//
//        public static void actionInviteaFriend(int i, Context context) {
//            HashMap<String, String> actionData = new HashMap<String, String>();
//            switch (i) {
//                case 1:
//                    actionData
//                            .put(ContextDataMode.ActionInviteaFriendKey.actionWechatInvite,
//                                    "1");
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionInviteaFriendKey.actionWechatInvite,
//                                    actionData);
//                    break;
//
//                case 2:
//                    actionData
//                            .put(ContextDataMode.ActionInviteaFriendKey.actionFriendSearch,
//                                    "1");
//                    MobclickAgent
//                            .onEvent(
//                                    context,
//                                    ContextDataMode.ActionInviteaFriendKey.actionFriendSearch,
//                                    actionData);
//                    break;
//            }
//        }
//
//        public static void onKillProcess(Context context) {
//            MobclickAgent.onKillProcess(context);
//        }
//    }

//}
