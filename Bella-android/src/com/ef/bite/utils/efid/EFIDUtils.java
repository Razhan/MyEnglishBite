package com.ef.bite.utils.efid;


import android.net.Uri;


public class EFIDUtils {

	private static final String baseurl = "http://b2cglobaluat.englishtown.com/mockupservice/oauth2";

	
	/**
	 * 
	 * @param response_type code (recommended) or token (discouraged)
	 * @param client_id A registered EFID client_id (see above)
	 * @param scope Space delimited set of permissions the application is requesting
	 * @param state A string that can contain useful information for the client app. EFID roundtrips the parameter so your application receives the same value it sent
	 * @param redirect_uri One of the redirect_uri values listed in the client info (see above). EFID will redirect to this location after authorization. Must be URL encoded.
	 * @return
	 */
	public static String BuildUrlForRequestingAnAuthorizationCode(String response_type,
			String client_id, String scope, String state, String redirect_uri) {

		String function = "auth";


		String url = Uri.parse(baseurl + "/" + function).buildUpon()
				.appendQueryParameter("response_type", response_type)
				.appendQueryParameter("client_id", client_id)
				.appendQueryParameter("scope", scope)
				.appendQueryParameter("state", state)
				.appendQueryParameter("redirect_uri", redirect_uri)
				.build().toString();
		return url;
	}
	
	
	public static String BuildUrlForRequestingAnAuthorizationCodeContiune(String contiune_url) {
		String function = "contiune";

		String url = Uri.parse(baseurl + "/" + function).buildUpon()
				.appendQueryParameter("contiune", contiune_url)
				.build().toString();
		return url;
	}
	
	public static String BuildUrlForRequestingAnAuthorizationCodeLogin() {
		String function = "login";

		String url = Uri.parse(baseurl + "/" + function).buildUpon()
				.build().toString();
		return url;
	}
	
	public static String BuildUrlForRequestingAnAuthorizationCodeJump(String state,String code) {
		String function = "jump";

		String url = Uri.parse(baseurl + "/" + function).buildUpon()
				.appendQueryParameter("state", state)
				.appendQueryParameter("code", code)
				.build().toString();
		return url;
	}
	
	//todo restful services(mockup)
}
