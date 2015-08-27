package com.ef.bite.Tracking;

public class ContextDataMode {

	public final static String globalAppNameValue = "bella";
	public final static String globalPlatformNameValue = "Android";
	public static String pagePreviousNameString = null;
	public final static String actionTabCancelLocation = "action.TabCancelLocation";

	/**
	 * contextdata key
	 */

	public static class Keydata {
		/**
		 * Custom HashMap_Key
		 */
		public final static String pageName = "page.name";
		public final static String pageSiteSection = "page.siteSection";
		public final static String pageSiteSubSection = "page.siteSubSection";
		public final static String pagePreviousName = "page.previousName";
		public final static String userMemberId = "user.memberId";
		public final static String userLanguage = "user.language";
		public final static String globalAppName = "global.appName";
		public final static String globalPlatformName = "global.platformName";
		public final static String userDate = "user.date";

		/**
		 * Custom TrackState_Key
		 */
		public final static String splashSplash1 = "Splash:splash 1";
		public final static String splash_wo_store = "splash wo store";

		public final static String External_home = "External home";
		public final static String Login = "Login:Login phone";
		public final static String Register = "enrollment:name";
		public final static String SignUp = "enrollment:account phone";
	}

	/**
	 * Custom Action_Key
	 */
	public static class ActionDialogueKey {

		public final static String actionParagraphOpen = "action.paragraphOpen";
		public final static String actionAudioPlay = "action.audioPlay";
		public final static String actionAudioPause = "action.audioPause";
		public final static String actionAudioPlayAgain = "action.audioPlayAgain";
		public final static String actionAudioPlayLocation = "action.audioPlayLocation";
	}

	public static class ActionPhrasepresentationKey {
		public final static String actionAudioPlay = "action.audioPlay";
		public final static String actionTranslationSee = "action.translationSee";
		public final static String actionScrollNavigation = "action.scrollNavigation";
	}

	public static class ActionBalloonQuestionKey {
		public final static String actionBallonClicksIncorrect = "action.ballonClicksIncorrect";
		public final static String actionBallonClicksCorrect = "action.ballonClicksCorrect";
	}

	public static class ActionBalloonFailureKey {
		public final static String actionTimeUpChoice = "action.timeUpChoice";
	}

	public static class ActionShareListChinaKey {
		public final static String actionSocialShare = "action.socialShare";
		public final static String actionSocialShareType = "action.socialShareType";
		public final static String actionSocialMessageCancel = "action.socialMessageCancel";
	}

	public static class ActionLevelUpMessageKey {
		public final static String actionLevelUpShare = "action.levelUpShare";
	}

	public static class ActionPhraseLearnedMessageKey {
		public final static String actionPhraseShare = "action.phraseShare";
        public final static String actionPhraseShareLocation = "action.phraseShare Location";

    }

	public static class ActionInboxKey {
		public final static String actionFriendsAdd = "action.friendsAdd";
	}

	public static class ActionFriendsLisKey {
		public final static String actionAlphabetClick = "action.alphabetClick";
	}

	public static class ActionProfileAddFriendsKey {
		public final static String actionFriendsAdd = "action.profileFriendAdd";
	}

	public static class ActionSearchLocationKey {
		public final static String actionSearchLocation = "action.SearchLocation";
	}

	public static class ActionSearchKey {
		public final static String actionSearch = "action.search";
        public final static String actionSearchCancel = "action.searchCancel";
    }

	public static class ActionSettingsKey {
		public final static String actionAvatarUpdateClick = "action.avatarUpdateClick";
		public final static String actionNotificationChoice = "action.notificationChoice";
		public final static String actionSoundEffectsChoice = "action.soundEffectsChoice";
		public final static String actionLogOut = "action.logOut";
	}

	public static class ActionInviteaFriendKey {
		public final static String actionWechatInvite = "action.WechatInvite";
		public final static String actionFriendSearch = "action.friendSearch";
	}



	public static class ActionADsUpsellKey {
		public final static String actionBannerCreativeClick = "action.bannerLearnMoreClick";
	}

	public static class ActionADsCallKey {
		public final static String actionBannerClick = "action.bannerClick";
        public final static String actionBannerLearnMoreClick = "action.bannerLearnMoreClick";
	}

	public static class ActionReviewKey {
		public final static String actionrate = "action.rate";
	}

	public static class ActionRecordingKey {
		public final static String actionrecordStatus = "action.recordStatus";
        public final static String actionrecord = "action.record";
    }
	
	public static class ActionRecordingSuccessfulKey{
		public final static String actionrecordingstatus = "action.recordStatus";
	}
	
	public static class ActionUserRecordShareKey{
		public final static String actionShare = "action.share";
	}

	public static class ActionRegisterSuccessfulKey {
		public final static String actionRegisterSuccessful = "action.registerSuccessful";
	}

	public static class ActionRegisterTypeKey{
		public final static String registerType = "action.registerType";
	}

	/**
	 * contextdata Value
	 */

	public static class SplashValues {
		public final static String pageNameValue = "Splash:splash 1";
		public final static String pageNameValue_woo = "Splash:splash wo store";
		public final static String pageSiteSectionValue = "External";
		public final static String pageSiteSubSectionValue = "Splash";
	}

	public static class ExternalHomeValues {
		public final static String pageNameValue = "External home v1";
		public final static String pageSiteSubSectionValue = "External home";
		public final static String pageSiteSectionValue = "External";
	}

	public static class LoginValues {
		public final static String pageNameValue_phone = "external:Login phone";
		public final static String pageNameValue_password = "external:Forgot password";
		public final static String pageSiteSubSectionValue = "Login";
		public final static String pageSiteSectionValue = "External";
	}

	//
	// public static class RegisterNameValues {
	// public final static String pageNameValue = "Enrollment Profile:Name";
	// public final static String pageSiteSubSectionValue =
	// "Enrollment Profile";
	// public final static String pageSiteSectionValue = "Enrollment";
	// }
	public static class RegisterNameValues {
		public final static String pageNameValue = "Enrollment:Name";
		public final static String pageSiteSubSectionValue = "Account setup";
		public final static String pageSiteSectionValue = "Enrollment";
	}

	// public static class RegisterPhoneValues {
	// public final static String pageNameValue =
	// "Enrollment Profile:account phone";
	// public final static String pageSiteSubSectionValue =
	// "Enrollment Profile";
	// public final static String pageSiteSectionValue = "Enrollment";
	// }

	public static class RegisterPhoneValues {
		public final static String pageNameValue = "Enrollment:account phone";
		public final static String pageSiteSubSectionValue = "Account setup";
		public final static String pageSiteSectionValue = "Enrollment";
	}

	public static class RegisterTermsValues {
		public final static String pageNameValue = "Enrollment:terms and conditions";
		public final static String pageSiteSubSectionValue = "Account setup";
		public final static String pageSiteSectionValue = "Enrollment";
	}

	public static class SettingsTermsValues {
		public final static String pageNameValue = "settings:terms and conditions";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	// public static class SignUpValues {
	// public final static String pageNameValue = "enrollment:account phone";
	// public final static String pageSiteSubSectionValue = "Account setup";
	// public final static String pageSiteSectionValue = "Enrollment";
	// }

	public static class Onboarding1 {
		public final static String pageNameValue = "Onboard:1 welcome";
		public final static String pageSiteSubSectionValue = "Onboarding";
		public final static String pageSiteSectionValue = "Introductions";
	}

	public static class Onboarding2 {
		public final static String pageNameValue = "Onboard:2 dialogue";
		public final static String pageSiteSubSectionValue = "Onboarding";
		public final static String pageSiteSectionValue = "Introductions";
	}

	// public static class Dashboard {
	// public final static String pageNameValue = "dashboard introduction";
	// public final static String pageSiteSubSectionValue =
	// "Introductions Dashboard";
	// public final static String pageSiteSectionValue = "Introductions";
	// }

	public static class Dashboard {
		public final static String pageNameValue = "first use:dashboard introduction";
		public final static String pageSiteSubSectionValue = "On first use";
		public final static String pageSiteSectionValue = "Introductions";
	}

	// public static class PhraseLearn {
	// public final static String pageNameValue = "Phrase learned introduction";
	// public final static String pageSiteSubSectionValue = "Phrase learned";
	// public final static String pageSiteSectionValue = "Introductions";
	// }

	public static class PhraseLearn {
		public final static String pageNameValue = "first use:phrase learned introduction";
		public final static String pageSiteSubSectionValue = "On first use";
		public final static String pageSiteSectionValue = "Introductions";
	}

	// public static class VocabularyVocab {
	// public final static String pageNameValue =
	// "Vocabulary list:vocab list introduction";
	// public final static String pageSiteSubSectionValue = "Vocabulary list";
	// public final static String pageSiteSectionValue = "Introductions";
	// }
	public static class VocabularyVocabValues {
		public final static String pageNameValue = "first use:vocab list introduction";
		public final static String pageSiteSubSectionValue = "On first use";
		public final static String pageSiteSectionValue = "Introductions";
	}

	public static class VocabularyMasteredValues {
		public final static String pageNameValue = "first use:mastered  introduction";
		public final static String pageSiteSubSectionValue = "On first use";
		public final static String pageSiteSectionValue = "Introductions";
	}

	// public static class VocabularyMaster {
	// public final static String pageNameValue =
	// "Vocabulary list:master introduction";
	// public final static String pageSiteSubSectionValue = "Vocabulary list";
	// public final static String pageSiteSectionValue = "Introductions";
	// }

	public static class VocabularyMaster {
		public final static String pageNameValue = "first use:mastered  introduction";
		public final static String pageSiteSubSectionValue = "On first use";
		public final static String pageSiteSectionValue = "Introductions";
	}

	public static class PresentationLearnValues {
		public final static String pageNameValue = "learn:phrase presentation1";
		public final static String pageSiteSubSectionValue = "Learn ";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class PresentationLookupValues {
		public final static String pageNameValue = "lookup:phrase presentation2";
		public final static String pageSiteSubSectionValue = "Lookup";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class LearnAudioDialogueValues {
		public final static String pageNameValue = "learn:audio dialogue1";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class LearnAudioDialogueLookUpValues {
		public final static String pageNameValue = "lookup:audio dialogue2";
		public final static String pageSiteSubSectionValue = "Lookup";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class BallonStartValues {
		public final static String pageNameValue = "Activity balloon:balloon start";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class BallonQuestionValues {
		public final static String pageNameValue = "learn:ballon question";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class BalloonFailureValues {
		public final static String pageNameValue = "Learn:ballon failure";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activity";
	}

	public static class BalloonSuccessValues {
		public final static String pageNameValue = "Learn:ballon success";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activity";
	}

	// public static class MultipleChioce_Form1Values {
	// public final static String pageNameValue = "activity multichoice";
	// public final static String pageSiteSubSectionValue = "Activity Learn";
	// public final static String pageSiteSectionValue = "Activities";
	// }

	public static class MultipleChioce_LearnMeaningValues {
		public final static String pageNameValue = "Learn:multichoice meaning";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activities";
	}

	public static class MultipleChioce_learnUseValues {
		public final static String pageNameValue = "Learn:multichoice use";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activities";
	}

	public static class MultipleChioce_PracticeMeaningValues {
		public final static String pageNameValue = "Practice:multichoice meaning";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activities";
	}

	public static class MultipleChioce_PracticeUseValues {
		public final static String pageNameValue = "Practice:multichoice use";
		public final static String pageSiteSubSectionValue = "Learn";
		public final static String pageSiteSectionValue = "Activities";
	}

	public static class ShareListChinaValues {
		public final static String pageNameValue = "share list china";
		public final static String pageSiteSubSectionValue = "Share list";
		public final static String pageSiteSectionValue = "Share";
	}

	public static class ScoresUpMessageValues {
		public final static String pageNameValue = "Celebration:Scores up";
		public final static String pageSiteSubSectionValue = "Scores up";
		public final static String pageSiteSectionValue = "Celebration";
	}

	public static class LevelUpMessageValues {
		public final static String pageNameValue = "celebration:level up:";
		public final static String pageSiteSubSectionValue = "Level up";
		public final static String pageSiteSectionValue = "Celebration";
	}

	public static class PhraseLearnedMessageValues {
		public final static String pageNameValue = "celebration:phrase learned";
		public final static String pageSiteSubSectionValue = "Vocabulary";
		public final static String pageSiteSectionValue = "Celebration";
	}

	public static class DashboardValues {
		public final static String pageNameValue = "dashboard";
		public final static String pageSiteSubSectionValue = "General Dashboard";
		public final static String pageSiteSectionValue = "General";
	}

	public static class LeaderboardFriendsValues {
		public final static String pageNameValue = "Leaderboard: friends";
		public final static String pageSiteSubSectionValue = "Leaderboard";
		public final static String pageSiteSectionValue = "General";
	}

	public static class LeaderboardGlobalValues {
		public final static String pageNameValue = "Leaderboard: global";
		public final static String pageSiteSubSectionValue = "Leaderboard";
		public final static String pageSiteSectionValue = "General";
	}

	public static class IndexDashboardValues {
		public final static String pageNameValue = "inbox";
		public final static String pageSiteSubSectionValue = "inbox";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsListMeValues {
		public final static String pageNameValue = "friends:my";
		public final static String pageSiteSubSectionValue = "Friends";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsListFriendsValues {
		public final static String pageNameValue = "friends:friends";
		public final static String pageSiteSubSectionValue = "Friends";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsListOthersValues {
		public final static String pageNameValue = "friends:others";
		public final static String pageSiteSubSectionValue = "Friends";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsProfileMyValues {
		public final static String pageNameValue = "Profile:my";
		public final static String pageSiteSubSectionValue = "Profile";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsProfileFriendsValues {
		public final static String pageNameValue = "Profile:friends";
		public final static String pageSiteSubSectionValue = "Profile";
		public final static String pageSiteSectionValue = "General";
	}

	public static class FriendsProfileOthersValues {
		public final static String pageNameValue = "Profile:others";
		public final static String pageSiteSubSectionValue = "Profile";
		public final static String pageSiteSectionValue = "General";
	}

	public static class VocabularylistTorehearseValues {
		public final static String pageNameValue = "VocabularyList:rehearse list";
		public final static String pageSiteSubSectionValue = "Vocabulary";
		public final static String pageSiteSectionValue = "General";
	}

	public static class VocabularylistMasteredValues {
		public final static String pageNameValue = "VocabularyList:mastered list";
		public final static String pageSiteSubSectionValue = "Vocabulary";
		public final static String pageSiteSectionValue = "General";
	}

	public static class VocabularySearchValues {
		public final static String pageNameValue = "Search";
		public final static String pageSiteSubSectionValue = "Search";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsValues {
		public final static String pageNameValue = "Settings";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsFirstNameValuse {
		public final static String pageNameValue = "Settings:firstname";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsLastNameValuse {
		public final static String pageNameValue = "Settings:lastname";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsNicknameValues {
		public final static String pageNameValue = "Settings:nickname";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsPasswordValues {
		public final static String pageNameValue = "Settings:change password";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsLocationValues {
		public final static String pageNameValue = "Settings:location";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class SettingsSystemLanguageValues {
		public final static String pageNameValue = "Settings:system language";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class AboutEnglishBiteValues {
		public final static String pageNameValue = "Settings:about bite";
		public final static String pageSiteSubSectionValue = "Settings";
		public final static String pageSiteSectionValue = "General";
	}

	public static class InviteaFriendValues {
		public final static String pageNameValue = "add friend:search friend";
		public final static String pageSiteSubSectionValue = "Add friend";
		public final static String pageSiteSectionValue = "General";
	}

	public static class GuideReviewRecordValue {
		public final static String pageNameValue = "introduction";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "rate";
	}

	public static class ReviewActivityValue {
		public final static String pageNameValue = "rate";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "rate";
	}

	public static class EditorPopupValue {
		public final static String pageNameValue = "report user seen";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "rate";
	}

	public static class ReportPromptValue {
		public final static String pageNameValue = "report user sent";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "rate";
	}

	public static class RecordingActivityValue {
		public final static String pageNameValue = "record";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "record";
	}

	public static class NoUserRecordingValue {
		public final static String pageNameValue = "no review";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "my recordings";
	}

	public static class HasUserRecordingValue {
		public final static String pageNameValue = "has review";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "my recordings";
	}

	public static class DeleteRecordingValue {
		public final static String pageNameValue = "delete confirm";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "my recordings";
	}

	public static class UserRecordingShareValue {
		public final static String pageNameValue = "share recording";
		public final static String pageSiteSubSectionValue = "Share";
		public final static String pageSiteSectionValue = "my recordings";
	}

	/** Action事件值 */
	public static class AudioDialogueValues {
		public final static String pageNameValue = "audio dialogue";
		public final static String pageSiteSubSectionValue = "activity dialogue";
		public final static String pageSiteSectionValue = "activity";
	}

	public static class ActionAudioDialogueValues {
		public final static String TabCancelLocation = "audio dialogue";
		public final static String paragraphOpen = "1";
		public final static String audioPlay = "1";
		public final static String audioPause = "1";
		public final static String audioPlayAgain = "1";
		public final static String audioPlayLocation_dialogue = "activity dialogue";
		public final static String audioPlayLocation_phrase = "activity phrase presentation";
	}

	public static class ActionPhrasepresentationValues {
		public final static String audioPlay = "1";
		public final static String translationSee = "1";
		public final static String scrollNavigation = "1";
	}

	public static class ActionBalloonQuestionValues {
		public final static String TabCancelLocation = "ballon question page";
		public final static String BallonClicksIncorrect = "1";
		public final static String BallonClicksCorrect = "1";
	}

	public static class ActionBalloonFailureValues {
		public final static String timeUpChoice_TryAgain = "try again";
		public final static String timeUpchoice_Learn = "learn";
	}

	public static class ActionShareListChinaValues {
		public final static String actionSocialShareType_WeChatfriends = "wechat friends";
		public final static String actionSocialShareType_Weibo = "weibo";
		public final static String actionSocialShareType_Wechatmoments = "wechat moments";
		public final static String actionSocialShareType_QQ = "qq";
		public final static String actionSocialShare = "1";
		public final static String actionSocialMessageCancel = "1";
	}

	public static class ActionLevelUpMessageValues {
		public final static String actionLevelUpShare = "1";
	}

	public static class ActionPhraseLearnedMessageValues {
		public final static String actionPhraseShare = "1";
        public final static String[] actionPhraseShareLocation = {"phrase learned page", "phrase mastered page"};
    }

	public static class ActionInboxValues {
		public final static String actionFriendsAdd = "1";
	}

	public static class ActionFriendsListValues {
		public final static String actionAlphabetClick = "1";
	}

	public static class ActionAddFriendsValues {
		public final static String actionFriendsAdd = "1";
	}

	public static class ActionSearchVocabularylist {
		public final static String actionSearchLocation_rehearse = "rehearse list";
		public final static String actionSearchLocation_mastered = "mastered list";
		public final static String actionSearch = "1";
        public final static String actionSearchcancel = "1";

    }

	public static class ActionSettingsValues {
		public final static String actionAvatarUpdateClickValues = "1";
		public final static String actionNotificationChoiceValues_ON = "ON";
		public final static String actionNotificationChoiceValues_OFF = "OFF";
		public final static String actionSoundEffectsChoiceValues_ON = "ON";
		public final static String actionSoundEffectsChoiceValues_OFF = "OFF";
		public final static String actionLogOutValues = "1";
	}

	public static class ActionRegisterSuccessfulValues {
		public final static String actionRegisterSuccessful = "1";
	}

	public static class ActionRegisterTypeValues {
		public final static String PHONE = "Phone";
		public final static String FACEBOOK = "Facebook";
		public final static String EMAIL = "Email";
	}

	public static class ActionADsUpsellValues {
		public final static String actionBannerCreativeClick = "1";
	}

	public static class ActionADsCallValues {
		public final static String actionBannerClick = "1";
	}

	public static class ActionReviewValues {
		public final static String actionrate = "1";
	}

	public static class ActionRecordingValues {
		public final static String actionrecordStatus = "No audio";
		public final static String actionrecord = "1";
	}
	
	public static class ActionRecordingSuccessfulValues{
		public final static String actionrecordStatus = "Successful";
	}
	
	public static class ActionUserRecordShareValues{
		public final static String actionshare = "1";
	}

	public static class RecordingValues {
		public final static String pageNameValue = "record";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "record";
	}

	public static class RecordGuideValues {
		public final static String pageNameValue = "introduction";
		public final static String pageSiteSubSectionValue = "Apply";
		public final static String pageSiteSectionValue = "record";
	}


	public static class BalloonIntroductionkeys {
		public final static String actionTabCancelLocationKey = "action.TabCancelLocation";
	}

	public static class BalloonIntroductionValues {
		public final static String actionTabCancelLocationValue = "balloon introduction";
	}



}
