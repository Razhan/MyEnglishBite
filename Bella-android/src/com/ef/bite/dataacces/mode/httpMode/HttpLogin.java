package com.ef.bite.dataacces.mode.httpMode;

public class HttpLogin extends HttpBaseMessage {

	public HttpLogin() {
		data = new LoginData();
	}

	public LoginData data;

	public class LoginData {
		public String access_token;
		public String bella_id;
		public String alias;
		public String avatar_url;
		public String family_name;
		public String given_name;
		public String phone;
		public boolean is_new_user;
    }
}
