package com.ef.bite.dataacces.mode.httpMode;

/**
 * Created by Ran on 9/9/2015.
 */
public class HttpGetFBImageResponse extends HttpBaseMessage {

    /**
     * data : {"image_url":"http://bella-image.oss-cn-qingdao.aliyuncs.com/banner/facebook_invite_image_cn.jpg"}
     * message : null
     * status : 0
     */
    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * image_url : http://bella-image.oss-cn-qingdao.aliyuncs.com/banner/facebook_invite_image_cn.jpg
         */
        private String image_url;

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getImage_url() {
            return image_url;
        }
    }
}
