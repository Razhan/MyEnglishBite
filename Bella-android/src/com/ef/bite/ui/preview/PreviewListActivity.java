package com.ef.bite.ui.preview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.adapters.StudyPlanListAdapter;
import com.ef.bite.business.task.GetStudyPlanTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.httpMode.HttpStudyPlans;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.StringUtils;
import com.ef.bite.widget.ActionbarLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 15/2/4.
 */
public class PreviewListActivity extends BaseActivity {
    private RadioGroup switcher;
    private ActionbarLayout mActionbar;
    private ListView listView;
    private HttpStudyPlans studyPlans;
    private StudyPlanListAdapter studyPlanListAdapter;
    private List<HttpStudyPlans.PlanItem> previewList=new ArrayList<HttpStudyPlans.PlanItem>();
    private List<HttpStudyPlans.PlanItem> approvedList=new ArrayList<HttpStudyPlans.PlanItem>();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_list);
        sertupViews();
    }

    private void sertupViews() {
        mActionbar = (ActionbarLayout) findViewById(R.id.preview_actionbar);
        mActionbar.initiWithTitle(
                "Study Plan",
                R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
        switcher = (RadioGroup) findViewById(R.id.preview_switcher);
        switcher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group.setBackground(getResources().getDrawable(checkedId == R.id.preview_rbtn_left ? R.drawable.toggle_left : R.drawable.toggle_right));
                setupListView(checkedId == R.id.preview_rbtn_left ?previewList:approvedList);
            }
        });
        getStudyPlan();
    }

    /**
     * 设置列表
     * @param itemList
     */
    private void setupListView(List<HttpStudyPlans.PlanItem> itemList){
        listView= (ListView) findViewById(R.id.preview_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(PreviewListActivity.this,CoursePreviewListActivity.class)
                        .putStringArrayListExtra(AppConst.BundleKeys.Course_id_list, (ArrayList<String>) ((HttpStudyPlans.PlanItem)view.getTag()).getCourseIdList()));
            }
        });
        studyPlanListAdapter=new StudyPlanListAdapter(this,itemList);
        listView.setAdapter(studyPlanListAdapter);
    }

    /**
     * 获得用户的StudyPlan
     */
    private void getStudyPlan() {
        if(!NetworkChecker.isConnected(this)){
            Toast.makeText(mContext, "No network found.", Toast.LENGTH_SHORT).show();
            return;
        }
        progress = new ProgressDialog(this);
        progress.setMessage("Loading study plan...");
        progress.show();
        GetStudyPlanTask studyPlanTask = new GetStudyPlanTask(mContext,
                new PostExecuting<HttpStudyPlans>() {
                    @Override
                    public void executing(HttpStudyPlans result) {
                        if (result != null && result.status != null
                                && result.status.equals("0")
                                && result.data != null) {
                            studyPlans=result;
                            splitter();
                            switchTab();
                        }
                        progress.dismiss();

                    }
                });
        studyPlanTask.execute(new String[]{AppConst.CurrUserInfo.UserId});
//        studyPlanTask.execute(new String[]{"6936147c-4847-49fc-9b25-199195630e01"});
    }

    /**
     * 类型过滤器
     */
    private void splitter(){
        for (int i = 0; i < studyPlans.data.size(); i++) {
            if(StringUtils.isEquals(studyPlans.data.get(i).plan_id ,"approve")){
                approvedList.add(studyPlans.data.get(i));
            }else {
                previewList.add(studyPlans.data.get(i));
            }
        }
    }

    private void switchTab(){
        switcher.check(previewList.size()>0?R.id.preview_rbtn_left:R.id.preview_rbtn_right);
    }


}
