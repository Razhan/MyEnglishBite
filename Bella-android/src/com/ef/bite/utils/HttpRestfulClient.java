package com.ef.bite.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ef.bite.AppConst;
import com.ef.bite.model.SMSRecord;
import com.ef.bite.model.ServerErrorLog;
import com.parse.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpRestfulClient {

    public final static int TIME_OUT = 13000;
	public static int Header_Num = 3;// 0:baidu 1:91 2:anzhuo 3:woo 4:wandoujia

	/*
	 * 调用Restful web service API
	 */
	public static Object Get(String url, Type returnType) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		try {
			String jsonString = null;
			HttpGet httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet, localContext);

            sendToParse(response.getStatusLine().getStatusCode(), url);

			HttpEntity entity = response.getEntity();
			jsonString = getUTF8ContentFromEntity(entity);
			Object obj = JsonSerializeHelper.JsonDeserialize(jsonString,
					returnType);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 返回Json
	 */
	public static String Get(String url, Map<String, String> headerMap) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		try {
			String jsonString = null;
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Content-type", "application/json; charset=utf-8");
			if (headerMap != null)
				for (String key : headerMap.keySet())
					httpGet.addHeader(key, headerMap.get(key));
			response = httpClient.execute(httpGet, localContext);

            sendToParse(response.getStatusLine().getStatusCode(), url);

            HttpEntity entity = response.getEntity();
			jsonString = getUTF8ContentFromEntity(entity);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object Post(String url, List<BasicNameValuePair> params,
			Type returnType) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type",
					"application/json; charset=utf-8");
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String jsonString = getUTF8ContentFromEntity(entity);
			Object obj = JsonSerializeHelper.JsonDeserialize(jsonString,
                    returnType);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 */
	public static String Post(String url, List<BasicNameValuePair> params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type",
					"application/json; charset=utf-8");
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			String jsonString = getUTF8ContentFromEntity(entity);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object Post(String url, JSONObject params, Type returnType) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		HttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type",
					"application/json; charset=utf-8");
			if (params != null)
				httpPost.setEntity(new StringEntity(params.toString(),
						HTTP.UTF_8));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			String jsonString = getUTF8ContentFromEntity(entity);
			if (jsonString != null) {
				Object obj = JsonSerializeHelper.JsonDeserialize(jsonString,
						returnType);
				return obj;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Post with Json parameter
	 * 
	 * @param url
	 * @param jsonParams
	 * @param bella_id
	 * @param token
	 * @param device_id
	 * @return
	 */
	public static String JsonPost(String url, String jsonParams,
			Map<String, String> headerMap) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		HttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type",
					"application/json; charset=utf-8");
			if (headerMap != null)
				for (String key : headerMap.keySet())
					httpPost.addHeader(key, headerMap.get(key));
			if (jsonParams != null)
				httpPost.setEntity(new StringEntity(jsonParams, HTTP.UTF_8));
			response = httpClient.execute(httpPost, localContext);

			sendToParse(response.getStatusLine().getStatusCode(), url);

			HttpEntity entity = response.getEntity();
			return getUTF8ContentFromEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param actionUrl
	 * @param newName
	 * @param fStream
	 * @return
	 */
	public static String uploadFile(String url, String filePath,
			Map<String, String> headerMap) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(url);
			if (headerMap != null)
				for (String key : headerMap.keySet())
					httppost.addHeader(key, headerMap.get(key));
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			/* example for setting a HttpMultipartMode */
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			File file = new File(filePath);
			if (file.exists()) {
				FileBody bin = new FileBody(file);
				builder.addPart("picture", bin);
			}
			httppost.setEntity(builder.build());
			response = httpClient.execute(httppost, localContext);

            sendToParse(response.getStatusLine().getStatusCode(), url);

            HttpEntity resEntity = response.getEntity();
			return getUTF8ContentFromEntity(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param url
	 * @param fileStream
	 * @param contentType
	 * @return
	 */
	public static String uploadFile(String url, InputStream fileStream,
			String contentType) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(url);
			InputStreamEntity reqEntity = new InputStreamEntity(fileStream, -1);
			reqEntity.setContentType(contentType);
			httppost.setEntity(reqEntity);
			response = httpClient.execute(httppost, localContext);
			HttpEntity resEntity = response.getEntity();
			return getUTF8ContentFromEntity(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getUTF8ContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		String response = "";
		String UTF8 = "utf8";
		int BUFFER_SIZE = 8192;
		InputStream in = entity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, UTF8),
				BUFFER_SIZE);
		String str;
		while ((str = br.readLine()) != null) {
			response += str;
		}
		return response;
	}

    private static void sendToParse(int responseCode, final String url) {

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(200);
        list.add(408);

        if (list.contains(responseCode)) {
            return;
        }

        ServerErrorLog log = new ServerErrorLog();

        log.put("appVersion", AppConst.GlobalConfig.App_Version);
        log.put("bellaID", AppConst.CurrUserInfo.UserId);
        log.put("deviceModel", AppConst.GlobalConfig.Device_Model);
        log.put("errorCode", responseCode);
        log.put("phoneVersion", AppConst.GlobalConfig.OS_Version);
        log.put("platform", AppConst.GlobalConfig.OS);
        log.put("endPoint", url);

        log.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    final Map<String,String> params = new HashMap<String,String>();
                    Date time = new Date();
                    params.put("api", url);
                    ParseCloud.callFunctionInBackground("sendSMS", params);
                }
            }
        });

    }

}
