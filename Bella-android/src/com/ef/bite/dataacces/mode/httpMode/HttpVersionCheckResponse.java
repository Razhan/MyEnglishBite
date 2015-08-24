package com.ef.bite.dataacces.mode.httpMode;

/**
 * Created by Ran on 8/21/2015.
 */
public class HttpVersionCheckResponse {
    public int version_code;
    public String apk_url;

    //1,表示可以选择更新;2,表示强制更新
    public int updateType;
    public long totalSize;
}
