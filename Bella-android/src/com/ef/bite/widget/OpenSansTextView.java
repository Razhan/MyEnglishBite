package com.ef.bite.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class OpenSansTextView extends TextView {

	public OpenSansTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public OpenSansTextView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public OpenSansTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}


	public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans.ttf"));
	}
}
