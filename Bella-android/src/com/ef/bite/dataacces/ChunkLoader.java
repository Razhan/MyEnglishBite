package com.ef.bite.dataacces;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.trinea.android.common.util.ToastUtils;
import com.ef.bite.AppConst;
import com.ef.bite.business.CourseServerAPI;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.KeyGenerator;
import com.ef.bite.utils.ZipUtil;
import com.litesuits.android.async.AsyncTask;
import com.litesuits.android.async.SimpleTask;
import com.litesuits.android.async.TaskExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The ChunkLoader using for load chunk from local and server.
 * Loading a chunk need a Request
 * Created by yang on 15/6/15.
 */
public class ChunkLoader {
    private Context mContext;
    private ChunksHolder chunksHolder = ChunksHolder.getInstance();
    private ProgressDialog progress;
    private Chunk mChunk;
    private int count;

    public ChunkLoader(Context context) {
        this.mContext = context;
        chunksHolder.loadAllChunks();
    }

    public void load(Request request, final OnFinishListener onFinishListener) {
        List<Request> requests = new ArrayList<Request>();
        requests.add(request);
        load(requests, onFinishListener);
    }

    public void load(final List<Request> requestList, final OnFinishListener onFinishListener) {
        final OnFinishListener listener = onFinishListener;

        if(requestList==null||requestList.size()==0){
            Log.v("EF", "Request list is empty");
            if (listener != null) {
                listener.doOnFinish(false);
            }
            return;
        }
        count=0;
        progress = new ProgressDialog(mContext);
        progress.setCancelable(false);
        progress.setMessage(JsonSerializeHelper
                .JsonLanguageDeserialize(mContext,
                        "loading_data"));

        SimpleTask<Boolean> lastTask = new SimpleTask<Boolean>() {
            @Override
            protected Boolean doInBackground() {
                chunksHolder.loadAllChunks();
                return requestList.size()==count;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                progress.dismiss();
                if (listener != null) {
                    listener.doOnFinish(result);
                }
            }
        };

        TaskExecutor.CyclicBarrierExecutor executor = TaskExecutor.newCyclicBarrierExecutor();

        for (Request request : requestList) {
            mChunk = chunksHolder.getSpecifyChunk(request.getChunkID());

            if (request.getUrl() == null) {
                Toast.makeText(mContext, "Chunk url is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getChunkID() == null) {
                Toast.makeText(mContext, "Chunk id url is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mChunk != null && !isUpdated(mChunk.getVersion(), request.getVersion())) {
                count++;
            } else {
                executor.put(getTask(request));
            }
        }

        executor.start(lastTask);
    }


    /**
     * 获取下载任务
     */
    private SimpleTask<Boolean> getTask(final Request request) {
        SimpleTask<Boolean> task = new SimpleTask<Boolean>() {

            @Override
            protected void onPreExecute() {
                if(!progress.isShowing()){
                    progress.show();
                }
            }

            @Override
            protected Boolean doInBackground() {
                try {
                    return download(request);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                    count++;
                }
            }
        };
        return task;
    }


    private boolean download(Request request) {
        CourseServerAPI api = new CourseServerAPI(mContext);
        FileStorage downloadStorage = new FileStorage(mContext,
                AppConst.CacheKeys.Storage_DownloadChunk);
        FileStorage courseStorage = new FileStorage(mContext,
                AppConst.CacheKeys.Storage_Course);

        // 分配时间戳为文件名
//        String key = KeyGenerator.getKeyFromDateTime();
        String key = request.getChunkID();

        // 创建文件
        File saveFile = new File(downloadStorage.getStorageFolder(), key);
        // 通过url开始下载文件
        api.downloadCourses(saveFile.getAbsolutePath(), request.getUrl());
        if (downloadStorage.get(key) == null) { // 下载失败
            return false;
        }
        // 删除已有课程
        courseStorage.delete(request.getChunkID());

        if (!ZipUtil.decompress(saveFile.getAbsolutePath(),
                courseStorage.getStorageFolder(), "utf-8")) {
            return false;
        }
        return true;
    }

    private boolean isUpdated(int oldVersion, int newVersion) {
        return oldVersion < newVersion;
    }

    public interface OnFinishListener {
        void doOnFinish(boolean isDone);
    }

    public Chunk getChunk(String id){
      return chunksHolder.getSpecifyChunk(id);
    }

    public List<Chunk> getChunkList(List<Request> requests){
        List<Chunk> chunks=new ArrayList<Chunk>();
        for (Request request : requests) {
            chunks.add(chunksHolder.getSpecifyChunk(request.getChunkID())) ;
        }
        return chunks;
    }

    public Request getNewRequest(){
        return new Request();
    }

    static public class Request {
        private String url;
        private String chunkID;
        private int version;

        public Request() {
        }

        public Request(String url, String chunkID, int version) {
            this.url = url;
            this.chunkID = chunkID;
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getChunkID() {
            return chunkID;
        }

        public void setChunkID(String chunkID) {
            this.chunkID = chunkID;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

}


