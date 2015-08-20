package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

/**
 * 课程对象
 * 
 * @author Allen.Zhu
 * 
 */
public class HttpCourseResponse extends HttpBaseMessage {

	public List<HttpCourseData> data;

	public static class HttpCourseData {

		public String course_name;

		public String course_id;

		public Integer course_version;

		public String package_url;

		public Boolean has_update;
	}
}
