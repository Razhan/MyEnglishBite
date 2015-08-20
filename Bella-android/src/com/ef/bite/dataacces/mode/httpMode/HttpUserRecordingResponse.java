package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.dataacces.mode.UserRecord;

public class HttpUserRecordingResponse extends HttpBaseMessage {
	public UserRecord data;

	public HttpUserRecordingResponse() {
		// TODO Auto-generated constructor stub
	}

	public UserRecord getData() {
		return data;
	}

	public void setData(UserRecord data) {
		this.data = data;
	}

}
