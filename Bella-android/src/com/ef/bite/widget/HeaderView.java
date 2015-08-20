package com.ef.bite.widget;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ef.bite.R;

/**
 * Header View
 * Created by yang on 15/5/28.
 */
public class HeaderView extends LinearLayout {
    private ViewGroup rootView;
    private ImageView helperView;
    private ImageButton backBtn;
    private UserLevelView levelView;
    private DotProgressbar  progressbar;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        rootView = (ViewGroup) inflater.inflate(R.layout.chunk_actionbar_layout, null);
        backBtn = (ImageButton) rootView.findViewById(R.id.chunk_actionbar_goback);
        helperView = (ImageView) rootView.findViewById(R.id.chunk_actionbar_teacher);
        progressbar = (DotProgressbar) rootView.findViewById(R.id.progressbar);
        levelView = (UserLevelView) rootView.findViewById(R.id.chunk_actionbar_level);
        addView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int)getContext().getResources().getDimension(R.dimen.action_bar_height)));
    }

    public ImageView getHelperView() {
        return helperView;
    }

    public ImageButton getBackBtn() {
        return backBtn;
    }

    public UserLevelView getLevelView() {
        return levelView;
    }

    public DotProgressbar getProgressbar() {
        return progressbar;
    }
}
