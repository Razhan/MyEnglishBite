package com.ef.bite.dataacces.mode;

import java.util.List;

public class UserRecord {
	private String course_id;
	private String avatar_url;
	private String voice_url;
	private int voice_length;
	private String course_name;
	private int review_count;
	private String like_percentage;
	private String given_name;
	private String family_name;
	private String voice_file_name;
	private String alias;
	private List<VoiceReviewrs> voice_reviewers;
	

	public List<VoiceReviewrs> getVoice_reviewers() {
		return voice_reviewers;
	}

	public void setVoice_reviewers(List<VoiceReviewrs> voice_reviewers) {
		this.voice_reviewers = voice_reviewers;
	}

	public UserRecord() {
		// TODO Auto-generated constructor stub
	}

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voice_file_name) {
		this.voice_file_name = voice_file_name;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getVoice_url() {
		return voice_url;
	}

	public void setVoice_url(String voice_url) {
		this.voice_url = voice_url;
	}

	public int getVoice_length() {
		return voice_length;
	}

	public void setVoice_length(int voice_length) {
		this.voice_length = voice_length;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	public String getLike_percentage() {
		return like_percentage;
	}

	public void setLike_percentage(String like_percentage) {
		this.like_percentage = like_percentage;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
