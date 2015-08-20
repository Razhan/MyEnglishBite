package com.ef.bite.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ef.bite.R;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * This fragment show when user have new lesson
 * Created by yang on 15/6/11.
 */
public class LearnFragment extends BaseDashboardFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home_sreen_learn_highlight, container, false);
        setupViews();
        return root;
    }

    @Override
    public void setupViews() {
        super.setupViews();
        getmLearnAvailableInfo().setText(JsonSerializeHelper.JsonLanguageDeserialize(getActivity(), "home_screen_everyday_learn_new"));
        FontHelper.applyFont(getActivity(), getmLearnTitle(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmPracticeCount(),
                FontHelper.FONT_Museo500);
        FontHelper.applyFont(getActivity(), getmMasteredCount(),
                FontHelper.FONT_Museo500);
        if(getHttpDashboard()!=null && getHttpDashboard().data.new_rehearsal_count >0){
            getmPracticeCount().setTextColor(getResources().getColor(
                    R.color.bella_color_orange_light));
        }else {
            getmPracticeCount().setTextColor(getResources().getColor(
                    R.color.bella_color_black_dark));
            getmPracticeCount().setAlpha(0.5f);
        }
    }
}
