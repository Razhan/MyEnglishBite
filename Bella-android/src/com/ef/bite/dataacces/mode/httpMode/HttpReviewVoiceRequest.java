package com.ef.bite.dataacces.mode.httpMode;

public class HttpReviewVoiceRequest {
	private String bella_id;
	private String course_id;
	private int start;
	private int rows;

	public HttpReviewVoiceRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getBella_id() {
		return bella_id;
	}

	public void setBella_id(String bella_id) {
		this.bella_id = bella_id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
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
