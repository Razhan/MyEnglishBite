package com.ef.bite.dataacces.mode;

public class ReviewVoice {
	private String bella_id;
	private String course_id;
	private String avatar_url;
	private String family_name;
	private String given_name;
	private int like_count;
	private int unlike_count;
	private String voice_file_name;
	private int voice_length;
	private String voice_url;

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

	public String getAvatar() {
		return avatar_url;
	}

	public void setAvatar(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public int getUnlike_count() {
		return unlike_count;
	}

	public void setUnlike_count(int unlike_count) {
		this.unlike_count = unlike_count;
	}

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voice_file_name) {
		this.voice_file_name = voice_file_name;
	}

	public int getVoice_length() {
		return voice_length;
	}

	public void setVoice_length(int voice_length) {
		this.voice_length = voice_length;
	}

	public String getVoice_url() {
		return voice_url;
	}

	public void setVoice_url(String voice_url) {
		this.voice_url = voice_url;
	}

}
