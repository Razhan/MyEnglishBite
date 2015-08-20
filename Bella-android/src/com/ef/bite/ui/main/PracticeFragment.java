package com.ef.bite.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ef.bite.R;
import com.ef.bite.business.task.GetUnlockChunkTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.ChunkLoader;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpUnlockChunks;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * This fragment show when user have some lessons of rehearsal
 * Created by yang on 15/6/11.
 */
public class PracticeFragment extends BaseDashboardFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home_sreen_practice_highlight, container, false);
        setupViews();
        return root;
    }

    @Override
    public void setupViews() {
        super.setupViews();
        FontHelper.applyFont(getActivity(), getmPracticeTitle(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmLearnCount(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmMasteredCount(),
                FontHelper.FONT_Museo500);

        if(getHttpDashboard()!=null && getHttpDashboard().data.new_lesson_count>0){
            getmPracticeCount().setTextColor(getResources().getColor(
                    R.color.bella_color_orange_light));
        }else {
            getmLearnCount().setTextColor(getResources().getColor(
                    R.color.bella_color_black_dark));
            getmLearnCount().setAlpha(0.5f);
            getmMasteredCount().setAlpha(0.5f);
        }

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
