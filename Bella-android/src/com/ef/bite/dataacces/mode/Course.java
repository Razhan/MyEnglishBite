package com.ef.bite.dataacces.mode;

public class Course {
	public String course_id;
	public String course_name;
	public String course_version;
	public String package_url;
	public boolean has_update;

	public Course() {

	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getCourse_version() {
		return course_version;
	}

	public void setCourse_version(String course_version) {
		this.course_version = course_version;
	}

	public String getPackage_url() {
		return package_url;
	}

	public void setPackage_url(String package_url) {
		this.package_url = package_url;
	}

	public boolean isHas_update() {
		return has_update;
	}

	public void setHas_update(boolean has_update) {
		this.has_update = has_update;
	}

}
