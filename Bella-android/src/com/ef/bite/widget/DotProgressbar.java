package com.ef.bite.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ef.bite.R;

/**
 * The dots as progressbar
 * Created by yang on 15/5/28.
 */
public class DotProgressbar extends LinearLayout {
    private ViewGroup rootView;
    private LinearLayout backgroundView;

    private int total_count = 5;
    private int current_count = 2;

    public DotProgressbar(Context context) {
        super(context);
        setupViews();
    }

    public DotProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        rootView = (ViewGroup) inflater.inflate(R.layout.progress_dots, null);
        backgroundView = (LinearLayout) rootView.findViewById(R.id.progress_background);
        addView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
    }

    public void init(int total,int current) {
        total_count=total;
        current_count=current;
        draw();
    }

    private void draw(){
        backgroundView.removeAllViews();
        for (int i = 0; i < total_count; i++) {
            addDots(i<current_count);
            if(i<total_count-1) {
                addLine(!(i < current_count-1));
            }
        }
    }

    private void addDots(boolean isSolid) {
        ImageView dotView = new ImageView(getContext());
        dotView.setImageResource(isSolid ? R.drawable.progress_circle: R.drawable.progress_circle_ring);
        dotView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        backgroundView.addView(dotView);
    }

    private void addLine(boolean isDash) {
        ImageView lineView = new ImageView(getContext());
        lineView.setImageResource(R.drawable.progress_line);
        lineView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        if(isDash){
        lineView.setPadding(7,0,7,0);
        }
        backgroundView.addView(lineView);
    }
}
