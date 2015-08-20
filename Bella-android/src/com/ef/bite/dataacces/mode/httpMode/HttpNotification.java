package com.ef.bite.dataacces.mode.httpMode;

import java.util.Date;
import java.util.List;

public class HttpNotification extends HttpBaseMessage {
	
	public final static String Notification_type_AddFriend = "add_friend";
	public final static String Notification_type_AD = "";
	public final static String Notification_type_ReView = "voice_rating";

	public List<NotificationData> data;

	public class NotificationData {

		public String avatar;

		public String bella_id;

		public String notification_id;

		public String from_bella_id;

		public String content;

		public String notification_type;

		public boolean has_read;

		public Date readed_at;

		public Date created_at;

		public Date updated_at;

		public String course_id;

		private String url;

	}

	public class Content {
		public String text;
		public String url;
	}
}
