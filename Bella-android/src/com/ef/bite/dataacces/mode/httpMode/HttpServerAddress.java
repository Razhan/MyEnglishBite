package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

public class HttpServerAddress extends HttpBaseMessage {
	
	public HttpHostData data;
	
	public static class HttpHostData{
//		public String host;
//		public String market;

		public String domain;
		public boolean enable_forget_password;
		public List<String> login_providers;

    }
}
