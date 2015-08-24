package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.LoginServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpVersionCheckRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpVersionCheckResponse;

/**
 * Created by Ran on 8/21/2015.
 */
public class CheckVersionTask extends BaseAsyncTask<HttpVersionCheckRequest, Void, HttpVersionCheckResponse> {
    private Context context;

    public CheckVersionTask(Context context, PostExecuting<HttpVersionCheckResponse> executing) {
        super(context, executing);
        this.context = context;
    }

    @Override
    protected HttpVersionCheckResponse doInBackground(HttpVersionCheckRequest... params) {
        LoginServerAPI loginServerAPI = new LoginServerAPI(context);
        return loginServerAPI.getAppVersion(params[0]);
    }
}
