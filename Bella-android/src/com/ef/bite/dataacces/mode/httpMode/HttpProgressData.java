package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

public class HttpProgressData {
    public Integer score;
    public String synced_at;
    public List<Progress> progress_list;
    public List<Rehearsal> rehearsal_list;

    public static class Rehearsal {
        public String bella_id;
        public String activity_id;
        public Integer score;
        public String status;
        public String started_at;
        public String ended_at;
        public String collected_at;
        public String synced_at;
    }

    public static class Progress {

        public String bella_id;
        public String lesson_id;
        public Integer score;
        public String status;
        public String started_at;
        public String ended_at;
        public String collected_at;
        public String synced_at;
    }

}
