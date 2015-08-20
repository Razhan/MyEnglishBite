package com.ef.bite.business.task;

import java.util.List;

import android.content.Context;
import android.util.Log;

import cn.trinea.android.common.util.PreferencesUtils;

import com.ef.bite.AppConst;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;
import com.ef.bite.utils.JsonSerializeHelper;

public class GetCourseUrlTask extends
		BaseAsyncTask<List<HttpCourseRequest>, Void, HttpCourseResponse> {
	Context mContext;

	public GetCourseUrlTask(Context context,
			PostExecuting<HttpCourseResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	protected HttpCourseResponse doInBackground(
			@SuppressWarnings("unchecked") List<HttpCourseRequest>... arg0) {
		// TODO Auto-generated method stub
		CourseServerAPI api = new CourseServerAPI(mContext);
//		String courseString = JsonSerializeHelper.JsonSerializer(api
//				.getAllCourses(arg0[0]));
//		Log.i("coursestring", courseString);
//		PreferencesUtils.putString(mContext, AppConst.CacheKeys.Storage_NextCourse,
//				courseString);
		return api.getAllCourses(arg0[0]);
	}

}
