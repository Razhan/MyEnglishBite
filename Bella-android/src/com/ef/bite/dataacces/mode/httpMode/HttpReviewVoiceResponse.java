package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

import com.ef.bite.dataacces.mode.ReviewVoice;

public class HttpReviewVoiceResponse extends HttpBaseMessage {

	private List<ReviewVoice> data;

	public HttpReviewVoiceResponse() {

	}

	public List<ReviewVoice> getData() {
		return data;
	}

	public void setData(List<ReviewVoice> data) {
		this.data = data;
	}

}
