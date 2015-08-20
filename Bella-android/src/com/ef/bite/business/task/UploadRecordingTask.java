package com.ef.bite.business.task;

import android.content.Context;
import com.ef.bite.AppConst;
import com.ef.bite.utils.*;
import org.json.JSONObject;
import java.io.File;

public class UploadRecordingTask extends BaseAsyncTask<String, Void, Boolean> {
    private File recFile;
    private Context context;
    private String chunkId;
    private String duration;
    private String score;

    /**
     * Upload recording file
     *
     * @param context
     * @param executing
     */
    public UploadRecordingTask(Context context,
                               File recFile, String chunkId, String duration, String score,
                               PostExecuting<Boolean> executing) {
        super(context, executing);
        this.context = context;
        this.recFile = recFile;
        this.chunkId = chunkId;
        this.duration = duration;
        this.score = score;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            String url = AppConst.EFAPIs.BaseAddress + "voice/upload/" + "?bella_id=%s&course_id=%s&voice_length=%s&score=%s";
            url = String.format(url, AppConst.CurrUserInfo.UserId, chunkId, duration, score);
            String result = HttpRestfulClient.uploadFile(url, recFile.getPath(), null);
            if (result != null) {
//                Log.v("POST RECORDING---",result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optInt("status", -1) == 0) {
                    return true;
                }

            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
