package com.ef.bite.dataacces.mode.httpMode;

public class HttpAppResourceResponse extends HttpBaseMessage {

	public HttpAppResourceData data;

	public static class HttpAppResourceData {
		public String package_url;
		public boolean has_update;
	}

}
