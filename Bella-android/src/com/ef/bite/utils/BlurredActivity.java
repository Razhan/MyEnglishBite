package com.ef.bite.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ef.bite.R;
import com.ef.bite.ui.BaseActivity;
import com.faradaj.blurbehind.BlurBehind;

public class BlurredActivity extends BaseActivity {

	private ImageView popup_badge_quit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_badge_info);

		popup_badge_quit = (ImageView) this.findViewById(R.id.popup_badge_quit);

		BlurBehind.getInstance().withAlpha(70)
				.withFilterColor(Color.parseColor("#000000"))
				.setBackground(this);

		popup_badge_quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, 0);
			}
		});

	}
}
