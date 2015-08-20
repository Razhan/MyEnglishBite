package com.ef.bite.dataacces.mode.httpMode;

public class HttpVoiceShareRequest {
	String bella_id ;
	String course_id ;
	String voice_file_name ;
	String client_culture_code ;
	String system ;


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

	public String getVoice_file_name() {
		return voice_file_name;
	}

	public void setVoice_file_name(String voice_file_name) {
		this.voice_file_name = voice_file_name;
	}

	public String getClient_culture_code() {
		return client_culture_code;
	}

	public void setClient_culture_code(String client_culture_code) {
		this.client_culture_code = client_culture_code;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
