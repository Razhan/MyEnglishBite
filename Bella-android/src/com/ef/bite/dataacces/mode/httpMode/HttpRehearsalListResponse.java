package com.ef.bite.dataacces.mode.httpMode;

import java.io.Serializable;
import java.util.List;

public class HttpRehearsalListResponse extends HttpBaseMessage {


    public Data data;

    public static class Data {

        public int in_progress_count;

        public int mastered_count;

        public List<courseInfo> available_now_courses;

        public List<courseInfo> available_future_courses;

        public List<courseInfo> mastered_courses;

    }


    public static class courseInfo implements Serializable{
        public int status;
        public String course_id;
        public String course_name;
        public String like_percentage;
        public String has_record;
        public int course_version;
        public String course_package_url;
    }
}
