package com.ef.bite.ui.chunk;

import android.os.Bundle;
import com.ef.bite.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.TutorialConfigBiz;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.ScoreLevelHelper;
import com.ef.bite.widget.AudioPlayerView;
import com.ef.bite.widget.DotProgressbar;
import com.ef.bite.widget.HeaderView;
import com.ef.bite.widget.UserLevelView;

public class BaseChunkActivity extends BaseActivity implements OnClickListener {

	protected Chunk mChunkModel;

	protected RelativeLayout mActionBarLayout;
	protected ImageButton mGoBack;
	protected UserLevelView mLevel;
	protected RelativeLayout mBottomLayout;
	protected AudioPlayerView mBottomPlayer;
	protected ImageButton mGoAHead;
	protected DotProgressbar progressbar;

	protected boolean isNextEnable = true;
	UserScoreBiz mUScoreBLL;
	boolean isChunkForPreview = false; // chunk是否是content预览类型
	boolean interrupt = false; // 被tutorial中断
	int resumeTimes = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();

        mChunkModel= (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);

		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Is_Chunk_For_Preview)) {
			isChunkForPreview = bundle
					.getBoolean(AppConst.BundleKeys.Is_Chunk_For_Preview);
		}
		setLayoutResourceId();

		HeaderView headerView= (HeaderView) findViewById(R.id.header);
		progressbar= headerView.getProgressbar();

//		mActionBarLayout = (RelativeLayout) this
//				.findViewById(R.id.chunk_actionbar_layout);
		mGoBack = headerView.getBackBtn();
		mLevel = headerView.getLevelView();
		mBottomLayout = (RelativeLayout) this
				.findViewById(R.id.chunk_bottom_layout);
		mBottomPlayer = (AudioPlayerView) this
				.findViewById(R.id.chunk_bottom_audioplayer);
		mGoAHead = (ImageButton) this.findViewById(R.id.chunk_bottom_go_ahead);
		mGoAHead.setOnClickListener(this);
		if (mUScoreBLL == null)
			mUScoreBLL = new UserScoreBiz(mContext);

		TutorialConfigBiz tutorialBiz = new TutorialConfigBiz(mContext);
		interrupt = tutorialBiz
				.interrupt(TutorialConfigBiz.TUTORIAL_TYPE_LERN_CHUNK);
		if (!interrupt)
			initComponents();

		MobclickTracking.OmnitureTrack.CreateContext(mContext);
	}

	protected void onResume() {
		super.onResume();
		int score = mUScoreBLL.getUserScore();
		int level = ScoreLevelHelper.getDisplayLevel(score);
		if (level == 0)
			mLevel.setVisibility(View.INVISIBLE);
		else {
			mLevel.initialize(score);
			mLevel.setVisibility(View.VISIBLE);
		}
		if (interrupt && resumeTimes == 1) {
			initComponents();
		}
		resumeTimes++;
		MobclickTracking.OmnitureTrack.ResumeCollectingLifecycleData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickTracking.OmnitureTrack.PauseCollectingLifecycleData();
	}

	protected void setLayoutResourceId() {
	}

	protected void initComponents() {
	}

	protected void impNextClick() {
	}

	protected void enableNext(boolean enable) {
		isNextEnable = enable;
		if (isNextEnable) {
			// mBottomLayout.setBackgroundColor(getResources().getColor(R.color.bella_color_black_dark));
			mGoAHead.setBackground(getResources().getDrawable(
					R.drawable.button_go_ahead_enable));
			mGoAHead.setEnabled(true);
		} else {
			// mBottomLayout.setBackgroundColor(getResources().getColor(R.color.bella_color_black_light));
			mGoAHead.setBackground(getResources().getDrawable(
					R.drawable.button_go_ahead_diable));
			mGoAHead.setEnabled(false);
		}
	}

	protected void setBottomPlayer(String audiofile) {
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mGoBack.getId()) {
			finish();
			MobclickTracking.OmnitureTrack.ActionDialogue(1);
//			MobclickTracking.UmengTrack.ActionDialogue(1, mContext);
		} else if (v.getId() == mGoAHead.getId()) {
			if (isNextEnable == true)
				impNextClick();
		}
	}

}
