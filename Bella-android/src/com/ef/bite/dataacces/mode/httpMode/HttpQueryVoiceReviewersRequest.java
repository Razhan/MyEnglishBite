package com.ef.bite.dataacces.mode.httpMode;

public class HttpQueryVoiceReviewersRequest {
	
	private String voice_file_name;
	private int start;
	private int rows;

	public HttpQueryVoiceReviewersRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voice_file_name) {
		this.voice_file_name = voice_file_name;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

}
