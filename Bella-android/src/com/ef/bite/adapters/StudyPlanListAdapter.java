package com.ef.bite.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.ef.bite.R;
import com.ef.bite.dataacces.mode.httpMode.HttpStudyPlans;

import java.util.List;

public class StudyPlanListAdapter extends BaseListAdapter<HttpStudyPlans.PlanItem> {

    public StudyPlanListAdapter(Activity context, List<HttpStudyPlans.PlanItem> dataList) {
        super(context, R.layout.study_plan_list_item, dataList);
    }

    @Override
    public void getView(View view, int position, HttpStudyPlans.PlanItem data) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) view.findViewById(R.id.preview_list_item_title);
        viewHolder.title.setText(data.name);
        view.setTag(data);
    }

    final static class ViewHolder {
        TextView title;
    }
}

