package com.ef.bite.dataacces.mode.httpMode;

public class HttpUserRecordingRequest {

	private String system;
	private String bella_id;
	private String course_id;
	private String direction;
	private int rows;
	private String course_source_culture_code;
	private String course_target_culture_code;
	
	public HttpUserRecordingRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getCourse_source_culture_code() {
		return course_source_culture_code;
	}

	public void setCourse_source_culture_code(String course_source_culture_code) {
		this.course_source_culture_code = course_source_culture_code;
	}

	public String getCourse_target_culture_code() {
		return course_target_culture_code;
	}

	public void setCourse_target_culture_code(String course_target_culture_code) {
		this.course_target_culture_code = course_target_culture_code;
	}

}
