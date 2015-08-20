package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.AppConst;

public class HttpCourseRequest {

	 public String system = AppConst.GlobalConfig.OS;
	 
	 public String app_version = AppConst.GlobalConfig.App_Version;
	 
	 public String course_id;
	 
	 public String source_language = "zh-cn";
	 
	 public String target_language = "en";
	 
	 public Integer course_version;
}
