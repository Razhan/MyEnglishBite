package com.ef.bite.ui.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.model.TutorialConfig;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.record.ReviewActivity;
import com.ef.bite.utils.JsonSerializeHelper;

public class GuideReviewRecordingActivity extends BaseActivity {

	private ViewPager mViewPager;
	private GuideReviewPageAdapter guideReviewPageAdapter;

	private TextView guide_content;
	private Button guide_button_start;

	private Chunk mChunkModel;

	private int HideBottomLay = 1; // 0:隱藏 1:現實

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_review_recording_layout);

		mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);
		HideBottomLay = getIntent().getExtras().getInt(
				AppConst.BundleKeys.Hide_Bottom_Lay, 1);

		// tracking
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.GuideReviewRecordValue.pageNameValue,
				ContextDataMode.GuideReviewRecordValue.pageSiteSubSectionValue,
				ContextDataMode.GuideReviewRecordValue.pageSiteSectionValue,
				mContext);

		initView();
		initPage();
		initOnclickListener();
	}

	private void initOnclickListener() {
		// TODO Auto-generated method stub
		guide_button_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, ReviewActivity.class);
				intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
				intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning, true);
				intent.putExtra(AppConst.BundleKeys.Hide_Bottom_Lay, 1);
				startActivity(intent);
				finish();

				TutorialConfigSharedStorage tutorialConfigSharedStorage = new TutorialConfigSharedStorage(
						mContext, AppConst.CurrUserInfo.UserId);
				TutorialConfig tutorialConfig = new TutorialConfig();
				tutorialConfig.Tutorial_Review_Record = true;
				tutorialConfigSharedStorage.put(tutorialConfig);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) this.findViewById(R.id.guide_viewpager);
		guide_content = (TextView) this.findViewById(R.id.guide_content);
		guide_button_start = (Button) this
				.findViewById(R.id.guide_button_start);
		guide_content.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "tutorial7"));
		guide_button_start.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "popup_chunk_done_continue"));
	}

	private void initPage() {
		guideReviewPageAdapter = new GuideReviewPageAdapter(mContext);
		mViewPager.setAdapter(guideReviewPageAdapter);
	}

	private class GuideReviewPageAdapter extends PagerAdapter {

		private Context mContext;

		public GuideReviewPageAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return (view == object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView image = new ImageView(mContext);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			image.setImageResource(R.drawable.guidereviewrecording_img);
			image.setScaleType(ScaleType.FIT_XY);
			container.addView(image, params);
			return image;
		}

	}

}
