package com.ef.bite.dataacces.mode.httpMode;

public class HttpVoiceRequest {

	private String voice_file_name;
	private String reviewer_bella_id;
	private int score;
	private String reviewee_bella_id;
	private String course_id;

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getReviewee_bella_id() {
		return reviewee_bella_id;
	}

	public void setReviewee_bella_id(String reviewee_bella_id) {
		this.reviewee_bella_id = reviewee_bella_id;
	}

	public HttpVoiceRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voice_file_name) {
		this.voice_file_name = voice_file_name;
	}

	public String getReviewer_bella_id() {
		return reviewer_bella_id;
	}

	public void setReviewer_bella_id(String reviewer_bella_id) {
		this.reviewer_bella_id = reviewer_bella_id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
