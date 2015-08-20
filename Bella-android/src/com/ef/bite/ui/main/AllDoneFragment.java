package com.ef.bite.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ef.bite.R;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * This fragment show when no any new or rehearsal lesson
 * Created by yang on 15/6/11.
 */
public class AllDoneFragment extends BaseDashboardFragment {
    private TextView goodJob;
    private TextView goodJobInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home_sreen_all_done, container, false);
        setupViews();
        return root;
    }

    @Override
    public void setupViews() {
        super.setupViews();
        goodJob = (TextView) root.findViewById(R.id.home_screen_done_good);
        goodJob.setText(JsonSerializeHelper.JsonLanguageDeserialize(getActivity(), "home_screen_all_done_good_job"));
        goodJobInfo = (TextView) root.findViewById(R.id.home_screen_done_info);
        goodJobInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(getActivity(), "home_screen_all_done_stay_tuned"));
        FontHelper.applyFont(getActivity(), getmLearnCount(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmPracticeCount(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmMasteredCount(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), goodJob,
                FontHelper.FONT_Museo500);

//        ImageButton unlockBtn = (ImageButton) root.findViewById(R.id.home_screen_practice_unlock);
        RelativeLayout unlockBtn = (RelativeLayout) root.findViewById(R.id.home_screen_learn_layout);
        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnlockChunk();
            }
        });

    }
}
