package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.AppConst;

public class HttpProgressNextRequest {

	public String bella_id = "";
	public String plan_id = "";
	public String current_course_id = "";
	public int next_courses_count;
	public String system = AppConst.GlobalConfig.OS;
	public String app_version = "";
	public String source_language = "";
	public String target_language = "";

	public HttpProgressNextRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getBella_id() {
		return bella_id;
	}

	public void setBella_id(String bella_id) {
		this.bella_id = bella_id;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getCurrent_course_id() {
		return current_course_id;
	}

	public void setCurrent_course_id(String current_course_id) {
		this.current_course_id = current_course_id;
	}

	public int getNext_courses_count() {
		return next_courses_count;
	}

	public void setNext_courses_count(int next_courses_count) {
		this.next_courses_count = next_courses_count;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getSource_language() {
		return source_language;
	}

	public void setSource_language(String source_language) {
		this.source_language = source_language;
	}

	public String getTarget_language() {
		return target_language;
	}

	public void setTarget_language(String target_language) {
		this.target_language = target_language;
	}

}
