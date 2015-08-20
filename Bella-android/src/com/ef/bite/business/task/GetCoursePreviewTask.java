package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;

import java.util.List;

/**
 * 获得用户的StudyPlan
 * 
 * @author Allen
 * 
 */
public class GetCoursePreviewTask extends BaseAsyncTask<String, Void, HttpCourseResponse> {

	private Context context;
    private List<HttpCourseRequest> httpCourseRequests;

	public GetCoursePreviewTask(Context context,List<HttpCourseRequest> httpCourseRequests, PostExecuting<HttpCourseResponse> executing) {
		super(context, executing);
		this.context = context;
        this.httpCourseRequests=httpCourseRequests;
	}

	/**
	 * params[0] uid
	 */
	@Override
	protected HttpCourseResponse doInBackground(String... params) {
        CourseServerAPI api = new CourseServerAPI(context);
        return api.getAllCourses(httpCourseRequests);
	}

}
