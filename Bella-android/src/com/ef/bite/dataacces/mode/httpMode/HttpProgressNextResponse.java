package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

public class HttpProgressNextResponse extends HttpBaseMessage {

	public List<ProgressNext> data;

	public static class ProgressNext {
		public String course_id;
		public int order;
		public String unlocking_date;
	}
}
