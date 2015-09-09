package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFBImageResponse;

/**
 * Created by Ran on 9/9/2015.
 */
public class GetFBImageTask extends BaseAsyncTask<Void, Void, HttpGetFBImageResponse> {
    private Context context;

    public GetFBImageTask(Context context, PostExecuting<HttpGetFBImageResponse> executing) {
        super(context, executing);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    protected HttpGetFBImageResponse doInBackground(Void... params) {
        UserServerAPI userServerAPI = new UserServerAPI(context);
        return userServerAPI.getFBImage();
    }

}
