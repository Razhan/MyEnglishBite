package com.ef.bite.dataacces.mode.httpMode;

import com.google.gson.annotations.SerializedName;

public class HttpUnreadNotificationCount extends HttpBaseMessage {

	@SerializedName("data")
	public int count;
	
}
