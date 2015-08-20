package com.ef.bite.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MuseoTextView extends TextView {

	public MuseoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MuseoTextView(Context context){
		super(context);
	}
	
	public MuseoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public void setTypeface(Typeface tf, int style) {
         super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Museo300-Regular.otf"));
	}
}
