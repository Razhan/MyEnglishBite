package com.ef.bite.dataacces;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.*;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.StringUtils;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The ChunksHolder using for parse chunks from local storage and hold all chunk
 * Created by yang on 15/2/28.
 */
public class ChunksHolder {
    private List<Chunk> chunks;
    private Context mContext;
    private static String LANG_EN = "en";
    private static ChunksHolder INSTANCE = new ChunksHolder();

    public static ChunksHolder getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }


    /**
     * load all courses chunk
     */
    public void loadAllChunks() {
        FileStorage courseStorage = new FileStorage(mContext,
                AppConst.CacheKeys.Storage_Course);
        chunks = (List<Chunk>) loadChunksFromStorage(courseStorage.getStorageFolder());
    }


    /**
     * load all preview courses chunk
     */
    public List<Chunk> loadPreviewChunks() {
        FileStorage courseStorage = new FileStorage(mContext,
                AppConst.CacheKeys.Storage_Course_Preview);
        return (LinkedList<Chunk>) loadChunksFromStorage(courseStorage.getStorageFolder());
    }


    /**
     * load particular chunk from list
     *
     * @param id
     * @param lang
     * @param chunks
     * @return
     */
    public Chunk getSpecifyChunk(String id, String lang, List<Chunk> chunks) {
        if (chunks == null || chunks.size() == 0) {
            return null;
        }
        for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
            Chunk chunk = iterator.next();
            if (StringUtils.isEquals(chunk.getChunkCode(), id)) {
//                if (lang.contains(LANG_EN)) {
//                    if(chunk.getLanguage().contains(LANG_EN)){
//                        return chunk;
//                    }
//                } else if (!StringUtils.isEquals(chunk.getLanguage(), LANG_EN)) {
//                    return chunk;
//                }
                return chunk;
            }
        }
        return null;
    }

    public Chunk getSpecifyChunk(String id) {
        return getSpecifyChunk(id, AppLanguageHelper.getSystemLaunguage(mContext), chunks);
    }

    /**
     * load chunks from storage
     *
     * @param chunkPath
     */
    private List<Chunk> loadChunksFromStorage(String chunkPath) {
        String[] chunkDirs;
        List<Chunk> chunkList = new LinkedList<Chunk>();

        try {
            chunkDirs = new File(chunkPath).list();
            for (String dir : chunkDirs) {
                Log.e("chunk", dir);
                String[] chunks = new File(chunkPath, dir).list();
                if (chunks != null && chunks.length > 0) {
                    for (String chunk : chunks) {
                        Log.e("file", chunk);
                        if (chunk.contains("source.json")) {
                            InputStream is = new FileInputStream(new File(
                                    chunkPath + File.separator + dir
                                            + File.separator + chunk));
                            Chunk mChunk;
                            //从文件转Chunk
                            try {
                                mChunk = Chunk.parseChunk(loadChunkStream(chunkPath + File.separator
                                        + dir, is));
                                if (mChunk != null) {
                                    chunkList.add(mChunk);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                saveCrashInfo2File(ex);
                            }


                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return chunkList;
    }

    private JSONObject loadChunkStream(String baseDir,
                                       InputStream is) {
        try {
            if (is == null)
                return null;
            InputStreamReader inputStreamReader = new InputStreamReader(is,
                    "UTF-8");
            char[] buffer = new char[is.available()];
            String jsonString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStreamReader.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, buffer.length));
            }
            jsonString = stringBuffer.toString();
            JSONObject chunkjson = new JSONObject(jsonString);
            chunkjson.put("filePath", baseDir + File.separator);
            return chunkjson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * Json文件转Json对象
//     * Created by yang
//     * @param baseDir
//     * @param is
//     * @return
//     */
//    private JSONObject streamToJson(String baseDir, InputStream is) {
//        try {
//            if (is == null)
//                return null;
//            String jsonString;
//            jsonString = streamToString(baseDir, is);
//            JSONObject chunkjson = new JSONObject(jsonString);
//            chunkjson.put("fileName", baseDir + File.separator);
//            return chunkjson;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String streamToString(String baseDir, InputStream is) {
//        try {
//            if (is == null)
//                return null;
//            InputStreamReader inputStreamReader = new InputStreamReader(is,
//                    "UTF-8");
//            char[] buffer = new char[is.available()];
//            StringBuffer stringBuffer = new StringBuffer();
//            while ((inputStreamReader.read(buffer)) != -1) {
//                stringBuffer.append(new String(buffer, 0, buffer.length));
//            }
//            return stringBuffer.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    public static final String TAG = "CrashHandler";
    private Map<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * save error information into storage
     *
     * @param ex
     * @return Gaving a file name for parsing the file to the server
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + File.separator
                        + AppConst.CacheKeys.RootStorage
                        + File.separator
                        + AppConst.CacheKeys.Storage_Log;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path
                        + File.separator + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();

            }
            return fileName;

        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }


}
