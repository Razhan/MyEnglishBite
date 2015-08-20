package com.ef.bite.apptentive;

import android.os.Bundle;
import com.ef.bite.R;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.apptentive.android.sdk.Apptentive;
import com.apptentive.android.sdk.ApptentiveActivity;

public class SurveyActivity extends ApptentiveActivity {

	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interactions_main);

		if (getIntent().getExtras().getInt("1", 0) == 1) {
			Log.i("usefulnessSurvey_zh", "usefulnessSurvey_zh");
			Apptentive.engage(SurveyActivity.this, "usefulnessSurvey_zh-Hans");
		}

		button1 = (Button) this.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Apptentive.engage(SurveyActivity.this,
						"usefulnessSurvey_zh-Hans");
			}
		});
	}

}
