package com.ef.bite.business.task;

import android.content.Context;

import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriends;

import org.json.JSONObject;

/**
 * Created by Ran on 9/1/2015.
 */
public class UpdateUserProfile extends BaseAsyncTask<JSONObject, Void, HttpBaseMessage> {
    private Context context;
    public UpdateUserProfile(Context context, PostExecuting<HttpBaseMessage> executing) {
        super(context, executing);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    protected HttpBaseMessage doInBackground(JSONObject... params) {
        UserServerAPI userServerAPI = new UserServerAPI(context);
        return userServerAPI.updateUserProfile(params[0]);
    }
}
