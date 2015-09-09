package com.ef.bite.business.task;

import com.ef.bite.AppConst;
import android.content.Context;
import android.util.Log;
import com.ef.bite.lang.Closure;
import com.ef.bite.utils.StringUtils;

import java.util.List;

public class GetServerAddressTask {
    private Context context;
    private Closure callback;
    private final static int RETRY_TIMES = 3;
    private int count = 0;

    public GetServerAddressTask(Context context, Closure callback) {
        this.context = context;
        this.callback=callback;
    }

    public void execute(){
        getHostURL();
    }

    private void getHostURL() {
        HostCompetition competition = new HostCompetition(context);
        competition.setOnFinishListener(new HostCompetition.onFinishListener() {
            @Override
            public void onFinish(String host, boolean password, List<String> studyplans) {
                if (StringUtils.isBlank(host)) {
                    if(count<3){
//                        Log.v("EF_HOST","try :"+count);
                        count++;
                        getHostURL();
                    }else {
                        doOnFinish(getDefaultHost(), false, null);
                    }
                }else {
                    doOnFinish(host, password, studyplans);
//                    Log.v("EF_HOST", "getHostFromServer:"+host);
                }
            }
        });
        competition.start();
    }

    private void doOnFinish(String host, boolean password, List<String> studyplans){
        setServerAddress(host, password, studyplans);
        callback.execute(null);
    }

    private String getDefaultHost() {
//        Log.v("EF_HOST","getDefaultHost");
        String country = context.getResources().getConfiguration().locale
                .getCountry().toLowerCase();
        if (country.equals("cn")) {
            return AppConst.EFAPIs.HOST_CN;
        } else {
            return AppConst.EFAPIs.HOST_COM;
        }
    }

    private void setServerAddress(String host, boolean password, List<String> studyplans) {
//        Log.v("EF_HOST","setServerAddress:"+host);
//        AppConst.EFAPIs.BaseHost = "http://" + host;
        AppConst.EFAPIs.BaseHost = host;
        AppConst.EFAPIs.BaseAddress = AppConst.EFAPIs.BaseHost + "/api/bella/";
        AppConst.GlobalConfig.ForgetPassWord = password;
        AppConst.GlobalConfig.StudyPlans = studyplans;

//        Log.v("EF_HOST", "BaseAddress :" + AppConst.EFAPIs.BaseAddress);
    }

}
