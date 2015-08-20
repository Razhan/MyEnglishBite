package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.utils.JsonSerializeHelper;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HttpStudyPlans extends HttpBaseMessage {

    public List<PlanItem> data;

    public static class PlanItem {
        public String plan_id;
        public String name;
        public String data;

        public List<CourseItem> getCourseList() {
            List<CourseItem> items = new ArrayList<CourseItem>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    CourseItem item = new CourseItem();
                    item.course_id = object.optString("course_id");
                    item.order = object.optString("order");
                    items.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }

        public List<String> getCourseIdList() {
            List<String> items = new ArrayList<String>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.optString("course_id");
                    items.add(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }
    }


    public static class CourseItem implements Serializable{
        public String course_id;
        public String order;
    }

}
