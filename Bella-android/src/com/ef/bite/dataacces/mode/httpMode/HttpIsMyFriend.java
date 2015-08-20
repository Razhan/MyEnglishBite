package com.ef.bite.dataacces.mode.httpMode;

import com.google.gson.annotations.SerializedName;

public class HttpIsMyFriend extends HttpBaseMessage {

	@SerializedName("data")
	public boolean isMyFriend;
}
