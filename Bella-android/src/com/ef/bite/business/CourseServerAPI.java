package com.ef.bite.business;

import java.util.List;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.*;
import com.ef.bite.utils.HttpFileDownload;
import com.ef.bite.utils.HttpRestfulClient;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.TraceHelper;
import org.json.JSONObject;

/**
 * 课程\资源相关接口， 当前一个course即一个chunk 课程下载、课程同步、课程版本管理等功能
 * 
 * @author Allen.Zhu
 * 
 */
public class CourseServerAPI extends BaseServerAPI {

	private Context context;

	public CourseServerAPI(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 下载课程
	 * 
	 * @return
	 */
	public void downloadCourses(String savePath, String source_url) {
		HttpFileDownload download = new HttpFileDownload(source_url);
		download.Save(savePath);
	}

	/**
	 * 获得所有的source列表
	 * 
	 * @return
	 */
	public HttpCourseResponse getAllCourses(List<HttpCourseRequest> request) {
		if (request == null || request.size() == 0)
			return null;
		try {
			String param = JsonSerializeHelper.JsonSerializer(request);
			String jsonResp = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "course", param,
					this.headerMap);
			return (HttpCourseResponse) JsonSerializeHelper.JsonDeserialize(
					jsonResp, HttpCourseResponse.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 从服务器获取解锁课程
	 * 
	 * @return
	 */
	public HttpProgressNextResponse getProgressNext(
			HttpProgressNextRequest request) {
		if (request == null)
			return null;
		try {
			String param = JsonSerializeHelper.JsonSerializer(request);
			String jsonResp = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "progress/next", param,
					this.headerMap);
			return (HttpProgressNextResponse) JsonSerializeHelper
					.JsonDeserialize(jsonResp, HttpProgressNextResponse.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


    public HttpRehearsalListResponse getRehearsalList(HttpRehearsalListRequest request) {
        try {
            String dataString = JsonSerializeHelper.JsonSerializer(request);
            String userProfile = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "view/rehearsal/list", dataString,
					headerMap);
            HttpRehearsalListResponse response = (HttpRehearsalListResponse) JsonSerializeHelper
                    .JsonDeserialize(userProfile, HttpRehearsalListResponse.class);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            TraceHelper.tracingErrorLog(context, ex.getMessage());
            return null;
        }
    }

	/**
	 * unlock new chunk
	 *
	 * @param uid
	 * @return
	 */
	public HttpUnlockChunks getUnlockChunk(String uid) {
		try {
			JSONObject params = new JSONObject();
			params.put("bella_id", uid);
			params.put("plan_id", "");
			params.put("unlock_count", "1");
			String jsonString = HttpRestfulClient.JsonPost(
					AppConst.EFAPIs.BaseAddress + "2/learn/unlock",params.toString(),
					headerMap);
			HttpUnlockChunks httpResponse = (HttpUnlockChunks) JsonSerializeHelper
					.JsonDeserialize(jsonString, HttpUnlockChunks.class);
			return httpResponse;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
