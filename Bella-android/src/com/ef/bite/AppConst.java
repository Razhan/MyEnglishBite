package com.ef.bite;

/**
 * 系统变量
 * 
 * @author Allen.Zhu
 * 
 */
public class AppConst {
	/**
	 * 当前用户信息
	 */
	public static class CurrUserInfo {
		public static boolean IsLogin = false;
		public static String Token = "";
		/** 过期时间，milliseconds */
		public static long ExpiresTime = 0;
		/** * 0 - EF login * 1 - Wechat * 2 - Facebook */
		public static int Login_Type = -1;
		public static String UserId = "0";
		public static String FirstName = "";
		public static String LastName = "";
		public static String Phone = "";
		public static String Email = "";
		public static String Alias = "Allen";
		public static String Avatar = "";
		public static String Location = "CN";
		public static int Score ;
		public static int Level ;
		public static String RegisterDate = null;
	}

	/**
	 * App全局设置
	 */
	public static class GlobalConfig {
		/** Welcome页面是否已经启动过 */
		public static boolean WelComePageStarted = false;
		/** 当前的android设备ID号 */
		public static String DeviceID = "";
		/** 当前的语言 */
		public static int LanguageType = MultiLanguageType.Default;
		/** 通知设置 **/
		public static boolean Notification_Enable = true;
		/** 声音效果 ***/
		public static boolean SoundEffect_Enable = true;
		/** 当前的操作系统类型 **/
		public static String OS = "android";
		/** 当前的操作系统版本 **/
		public static String OS_Version = "";
		/** 当前的手机型号 **/
		public static String Device_Model = "";
		/** 当前手机的 **/
		public static String Device_Brand = "";
		/** 当前APP_Version **/
		public static String App_Version = "1.0.2";
		/** splash页面是否已经加载 **/
		public static boolean IsSplashPageLoaded = false;
		/** 解锁chunk的alarm是否已经加载 **/
		public static boolean IsUnlockAlarmLoaded = false;
		/** 每天学习一个chunk是否加载 **/
		public static boolean IsChunkPerDayLearnLoaded = false;
		/** 每天一个chunk已经学习过了,用于控制打开APP就学习当天的chunk **/
		public static boolean IsChunkPerDayLearned = false;
		/** 当前App语言版本 **/
		public static String Language = "en";
		/** 通知的数量 **/
		public static int Notify_Count = 2;
		/** 用于控制chunk的迁移,一旦chunk content有变动，就会累计加1 **/
		public static int CHUNK_MIGRATION_VERSION = 0;
		/** 分页查询中，一页获得的数量 **/
		public static int PageSize = 20;
		/** Master欢迎页显示控制 **/
		public static boolean TutorialConfig = false;
		/**是否显示忘记密码**/
		public static boolean ForgetPassWord = false;

	}

	/**
	 * 提交Store的Header信息
	 */
	public static class HeaderStore {
		public static String StoreName = "";
	}

	/**
	 * 后台服务器API地址
	 */
	public final static class EFAPIs {
		public static int API_Status = 0;// 0:ETHost 1:DEV 2:QA
		public final static String ETHost = "http://www.englishtown.cn/community/dailylesson/environment/host/";
		public final static String HK_ETHost = "http://www.englishtown.com/community/dailylesson/environment/host/";
		public final static String Internal_Host = "b2cglobaluat.englishtown.com"; // inner
																					// address
		public final static String External_Host = "42.96.249.21";// QA
		public final static String Publish_Host_CN = "42.96.250.52";// Product
		public final static String Publish_Host_COM = "42.96.250.52";// Product

		public final static String HOST_COM = "http://bella-live-web-lb-1387001753.us-west-2.elb.amazonaws.com";//amazon
		public final static String HOST_CN = "http://42.96.250.52";//ali

		public final static String Internal_Address = "http://" + Internal_Host
				+ "/api/bella/";
		public final static String External_Address = "http://" + External_Host
				+ "/api/bella/";
		public final static String Publish_Address = "http://" + Publish_Host_CN
				+ "/api/bella/";
		public static String BaseHost = "";
		public static String BaseAddress = "";

		// Dev开发环境
		// public static String BaseAddress = Internal_Address;
		// QA测试环境
		// public static String BaseAddress = External_Address;
		// Pro真实环境
		// public static String BaseAddress = Publish_Address;
	}

	/**
	 * App信息集合
	 */
	public static class APPInfo {
		/** 下载链接 **/
		public final static String Dowload_Link = "http://download.bite.ef.com";
		/** 学了新chunk后，分享给好友的link **/
		public final static String Challenge_Link = "http://challenge.bite.ef.com";

	}

	/**
	 * 第三方登录类型
	 */
	public final static class ThirdPart {
		public final static String Weibo_AppKey = "1954270563";
		public final static String Weibo_ReturnUrl = "https://api.weibo.com/oauth2/default.html";

		// public final static String QQ_AppID = "101104169";
		// public final static String QQ_AppKey =
		// "e5d3be55dfd93e6af01ba72d835c8db5";
		public final static String QQ_AppID = "1101848102";
		public final static String QQ_AppKey = "jNGSsF8ivAYKTUp6";

		public final static String Facebook_AppID = "295838253922869";
		public final static String Facebook_AppKey = "7190c783e0be853c059d5f70907a2d04";

		public final static String UMENG_APPKEY = "548a9baafd98c5c91000104b";

		public final static String Apptentive_AppKey = "8346112617faceaf750ae81dc0b41015bb80d3639f37959e5fe373015f28cd70";

		// public final static String Facebook_Login_Appkey = "669845426449672";
		// public final static String Facebook_Login_Display_Name =
		// "Engl­i­s­h­Bite";

		public final static String Facebook_Login_Appkey = "840080019363949";
		public final static String Facebook_Login_Display_Name = "EFID";

		public final static String Apsalar_Api_Key = "efadmin";
		public final static String Apsalar_Secret = "iPR4HGwF";
	}

	/**
	 * 多语言设置
	 */
	public final static class MultiLanguageType {
		/**
		 * 跟随系统
		 */
		public final static int Default = 0;
		/**
		 * 英语
		 */
		public final static int English = 1;
		/**
		 * 中文简体
		 */
		public final static int Chinese = 2;
	}

	public final static class RequestCode {
		public final static int WELCOME_NEXT = 1;
		public final static int EF_LOGIN = 2;
		public final static int CHUNK_LEARNING = 3; // Chunk Learning pages:
													// ChunkLearnActivity - >
													// ChunkLearnDetailActivity
													// - > ChunkPracticeActivity
		public final static int MY_PROFILE = 4;
		public final static int EF_REGISTER = 5; // 注册页面
		public final static int WALKTHROUGH = 6;
		public final static int CHUNK_PREVIEW = 7; // chunk预览
		public final static int SETTINGS = 8; // 设置页面
		public final static int OPEN_SPLASH = 9;
		public final static int LOCATION_SETTING = 10;
		public final static int TERMS_CONDITION = 11; // 服务条款
	}

	public final static class ResultCode {
		public final static int Welcome_Finish = 1;
		public final static int LOGIN_SUCCESS = 2;
		public final static int APP_EXIT = 3;
		public final static int CHUNK_LEARNING_COMPLETED = 4;
		public final static int LOG_OUT = 5;
		public final static int REGISTER_SUCCESS = 6; // 注册成功
		public final static int WALKTHROUGH_LOADED = 7; // Walkthrough加载结束
		public final static int CHUNK_PREVIEW_COMPLETED = 8; // chunk预览结束
		public final static int CHUNK_PRACTICE_QUIT = 9; // 退出CHUNK
															// Practice（balloon/multi-choice）
		public final static int SPLASH_LOADED = 10;
		public final static int TERMS_ACCEPT = 11; // 接受服务条款
		public final static int TERMS_NOT_ACCEPT = 12; // 不接受服务条款
	}

	public final static class CacheKeys {
		public final static String RootStorage = "EF_Bella";
		public final static String Storage_Cache = "cache";
		public final static String Storage_Avatar = "avatar";
		public final static String Storage_Log = "log";
		public final static String Storage_Crop = "crop";
		public final static String Storage_DownloadChunk = "download";
		public final static String Storage_Course = "course";
		public final static String Storage_Course_Preview = "course_preview";
		public final static String Storage_Language = "resource";
        public final static String Storage_LanguageMaping = "language_mapping";

        public final static String Default_Avatart = "default_avatar";
		public final static String Storage_Log_Omniture = "omniture";
		public final static String Facebook_Access_Token = "access_token";
		public final static String Facebook_Access_Expires = "access_expires";
		public final static String Storage_UserProGress = "userprogress";
		public final static String Storage_NextCourse = "nextcourse";
		public final static String Storage_Recording = "recording";
		public final static String Storage_Record_guide = "record_guide";
		public final static String CACHE_DASHBOARD = "CACHE_DASHBOARD";
		public final static String TIME_START = "TIME_START";
		public final static String TIME_END = "TIME_END";
		public final static String CACHE_ACHIEVEMENT = "CACHE_ACHIEVEMENT";
		public final static String CACHE_SCORE = "CACHE_SCORE";
		public final static String CACHE_PROFILE = "CACHE_PROFILE";
	}

	public final static class BundleKeys {
		public final static String Chunk = "Chunk_Model_Json";
		public final static String Chunk_Conversation = "Chunk_Conversation_Json";
		public final static String Chunk_List_Type = "Chunk_List_Type";
		public final static String Multi_Choice_Type = "Multi_Choice_Type";
		public final static String Is_Chunk_Learning = "Is_Chunk_Learning";
		public final static String Chunk_Rehearse_List = "Chunk_Rehearse_List_Json";
		public final static String Person_Profile = "Person_Profile_Json";
		public final static String Get_Friend_List_Profile = "Get_Friend_List_Profile_Json";
		public final static String Profile_Is_From_Home = "Profile_Is_From_Home";
		public final static String Is_Chunk_For_Preview = "Is_Chunk_For_Preview"; // chunk是否是content预览类型
		public final static String Reset_Tutorial_Mode = "Reset_Tutorial_Mode";
		public final static String Notification_New_Chunk = "Notification_for_New_Chunk";
		public final static String Notification_Rehearse_Chunk = "Notification_for_Rehease_Chunk";
		public final static String Is_In_Tutorial_Model = "Is_In_Tutorial_Model";
		public final static String Tutorial_TYPE = "Tutorial_Type";
		public final static String Terms_Btnbar_Show = "Terms_Btnbar_Show";
		public final static String Course_id_list = "Course_id_list";
		public final static String Hide_Bottom_Lay = "HideBottom";
		public final static String BELLAID = "";
		public final static String LIST_REHEARSAL = "LIST_REHEARSAL";
		public final static String LIST_MASTERED = "LIST_MASTERED";
	}

	public final static String Default_Avatar = "avatar/default.png";
}
