package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

import com.ef.bite.dataacces.mode.VoiceReviewrs;

public class HttpQueryVoiceReviewersResponse extends HttpBaseMessage{
	private List<VoiceReviewrs> data;
	public HttpQueryVoiceReviewersResponse() {
		// TODO Auto-generated constructor stub
	}
	public List<VoiceReviewrs> getData() {
		return data;
	}
	public void setData(List<VoiceReviewrs> data) {
		this.data = data;
	}
	
}
