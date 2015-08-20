package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.mode.httpMode.*;

public class GetRehearsalListTask extends
		BaseAsyncTask<HttpRehearsalListRequest, Void, HttpRehearsalListResponse> {
	private Context context;

	public GetRehearsalListTask(Context context,
                                PostExecuting<HttpRehearsalListResponse> executing) {
		super(context, executing);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected HttpRehearsalListResponse doInBackground(HttpRehearsalListRequest... request ) {
        CourseServerAPI courseServerAPI = new CourseServerAPI(context);
		if(request!=null && request.length > 0)
			return courseServerAPI.getRehearsalList(request[0]);
		else
			return null;
	}

}
