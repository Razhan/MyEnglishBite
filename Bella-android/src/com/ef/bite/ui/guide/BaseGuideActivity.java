package com.ef.bite.ui.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ef.bite.R;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * Created by yang on 15/3/27.
 */
public abstract class BaseGuideActivity extends Activity {
	private ImageView screenView;
	private TextView descView;
	private Button nextBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		screenView = (ImageView) findViewById(R.id.iv_content);
		descView = (TextView) findViewById(R.id.tv_desc);
		nextBtn = (Button) findViewById(R.id.btn_next);

		descView.setText(JsonSerializeHelper.JsonLanguageDeserialize(this,
				"tutorial8"));
		nextBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(this,
				"popup_chunk_done_continue"));
		nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setScreenView(screenView);
		setDescView(descView);
	}

	/**
	 * filling up center image
	 * 
	 * @param screen
	 */
	abstract void setScreenView(ImageView screen);

	/**
	 * setup guide description
	 * 
	 * @param DescView
	 */
	abstract void setDescView(TextView DescView);

	public Button getNextBtn() {
		return nextBtn;
	}
}
