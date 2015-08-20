package com.ef.bite.dataacces.mode;

/**
 * Push data include type and courseID
 * Created by yang on 15/4/16.
 */
public class PushData {

    //Type of Push
    public enum Type {
        new_lesson,
        new_rehearsal,
        recording_rate,
        empty;

        //return type
        public static Type toType(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return empty;
            }
        }
    }

    Type type;
    String course_id;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
