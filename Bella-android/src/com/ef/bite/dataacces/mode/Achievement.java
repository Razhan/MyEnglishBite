package com.ef.bite.dataacces.mode;

/**
 * Model of user's progress. It be created when a lesson be learned but can not deliver to server
 * Created by yang on 15/6/16.
 */
public class Achievement {
    public static String TYPE_COMPLETE_LEARN = "complete_learn";
    public static String TYPE_LOSE_PRACTICE = "lose_practice";
    public static String TYPE_WIN_PRACTICE = "win_practice";

    String bella_id;
    String plan_id;
    String course_id;
    int score;
    String start_client_date;
    String end_client_date;
    String update_progress_type;

    public String getBella_id() {
        return bella_id;
    }

    public void setBella_id(String bella_id) {
        this.bella_id = bella_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getStart_client_date() {
        return start_client_date;
    }

    public void setStart_client_date(String start_client_date) {
        this.start_client_date = start_client_date;
    }

    public String getEnd_client_date() {
        return end_client_date;
    }

    public void setEnd_client_date(String end_client_date) {
        this.end_client_date = end_client_date;
    }

    public String getUpdate_progress_type() {
        return update_progress_type;
    }

    public void setUpdate_progress_type(String update_progress_type) {
        this.update_progress_type = update_progress_type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
