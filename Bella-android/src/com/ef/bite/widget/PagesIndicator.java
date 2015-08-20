package com.ef.bite.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ef.bite.R;

/**
 * Created by yang on 15/3/19.
 */
public class PagesIndicator extends LinearLayout {
	public PagesIndicator(Context context) {
		super(context);
	}

	public PagesIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setIndicator(int index, int total) {
		if (index > total) {
			return;
		}
		if (getChildCount() > 0) {
			removeAllViews();
		}
		for (int i = 1; i <= total; i++) {
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
			if (i == 1) {
				imageView.setImageResource(R.drawable.progress_begin);
			} else if (i == total) {
				if (index == total) {
					imageView.setImageResource(R.drawable.progress_end_event);
				} else {
					imageView.setImageResource(R.drawable.progress_end_default);
				}
			} else {
				imageView.setImageResource(R.drawable.progress_ing_default);
				if (i <= index) {
					imageView.setImageResource(R.drawable.progress_ing_event);
				}
			}
			setGravity(Gravity.CENTER);
			addView(imageView);
		}
	}

}
