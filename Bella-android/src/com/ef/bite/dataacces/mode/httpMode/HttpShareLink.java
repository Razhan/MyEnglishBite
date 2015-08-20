package com.ef.bite.dataacces.mode.httpMode;

public class HttpShareLink extends HttpBaseMessage {

	public HttpShareLinkData data;
	
	public class HttpShareLinkData{
		
		public String message;
		
		public String url;
		
	}
}
