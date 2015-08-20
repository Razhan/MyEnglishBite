package com.ef.bite.ui.chunk;

import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ChunkPracticeListAdapter;
import com.ef.bite.animation.AudioPlayerMoveAnimation;
import com.ef.bite.dataacces.mode.Achievement;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.ui.popup.BaseDialogFragment.OnDismissListener;
import com.ef.bite.ui.popup.*;
import com.ef.bite.utils.HighLightStringHelper;
import com.ef.bite.utils.ScoreLevelHelper;
import com.ef.bite.utils.TimeFormatUtil;
import com.ef.bite.widget.DotProgressbar;

public class ChunkRehearsalActivty extends BaseMultiChoiceActivity {

	protected int mMultiChoiceType = Multi_Choice_Type_Rehearsal;
	boolean isTimeout = false;
	private final static int MultiChoicePractice_mean = 1;
	private final static int MultiChoicePractice_use = 2;
	private DotProgressbar progressbar;
	private int progress_count=0;

	@Override
	protected void initEvents() {
		progressbar = headerView.getProgressbar();
		progressbar.setVisibility(View.VISIBLE);
		isTimeout = false;
		curMultyChoice = null;
		if (mMultiChoiceType == Multi_Choice_Type_Rehearsal
				&& mRehearsChunkList != null && mRehearsChunkList.size() > 0) { // 类型是Rehearsal
			mChunkModel = mRehearsChunkList.get(curMultiChoiceIndex);
			curMultyChoice = mChunkBLL.getRehearseMultiChoice(mChunkModel);
			// mIndicator.setIndicator(curMultiChoiceIndex,
			// mRehearsChunkList.size());
//			mIndicator.removeAllViews();
//			mPageIndicat(mIndicator, curMultiChoiceIndex + 1, mChunkBLL
//					.getPraticeMultiChoiceList(mChunkModel).size());
			progress_count++;
			progressbar.init(mChunkBLL.getPraticeMultiChoiceList(mChunkModel).size(), progress_count);

			BI_Tracking(MultiChoicePractice_use);

			Log.i("mMultiChoiceType", String.valueOf(mMultiChoiceType));
		}

		if (curMultyChoice != null) {
			// restore the audio player status
			cancelLimitAnimation();
			mSmallPlayer.stop();
			mSmallPlayer.setVisibility(View.INVISIBLE);
			ViewGroup.LayoutParams params = mLimitView.getLayoutParams();
			params.height = 0;
			mLimitView.setLayoutParams(params);
			if (curMultyChoice.getAudioFile() != null
					&& !curMultyChoice.getAudioFile().isEmpty()
					&& !curMultyChoice.getAudioFile().trim().isEmpty()) {
				mBigPlayer.setVisibility(View.VISIBLE);
				mQuestionContent.setVisibility(View.GONE);
				mBigPlayer.prepareInAsset(curMultyChoice.getAudioFile());
				mSmallPlayer.prepareInAsset(curMultyChoice.getAudioFile());
				mBigPlayer.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mQuestionContent.setVisibility(View.VISIBLE);
						if (curTryTimes == 1) // 第一次需要计时
							startLimitAnimation(true);
						AudioPlayerMoveAnimation animation = new AudioPlayerMoveAnimation(
								ChunkRehearsalActivty.this);
						animation.movePlayer(mBigPlayer, mSmallPlayer, true);
						mSmallPlayer.start();
						mPracticeAdapter.enableChoice(true); // 允许被选择
					}
				});
			} else {
				enableMultiChoice = true;
				mBigPlayer.setVisibility(View.GONE);
				mQuestionContent.setVisibility(View.VISIBLE);
				if (curTryTimes == 1) { // 第一次需要计时
					new Handler().postDelayed(new Runnable() {
						public void run() {
							startLimitAnimation(true);
						}
					}, 1000);
				}
			}
			mQuestionContent.setText(HighLightStringHelper.getBoldString(
					mContext, curMultyChoice.getContent()));
			mChooseHeader.setText(curMultyChoice.getHeader());
			mPracticeAdapter = new ChunkPracticeListAdapter(this,
					curMultyChoice);
			mPracticeAdapter.setOnCorrectListener(this);
			mPracticeAdapter.setOnWrongListener(this);
			mPracticeAdapter.enableChoice(enableMultiChoice);
			mChoiceListView.setAdapter(mPracticeAdapter);
		}

	}

	/** 选择了错误的项 **/
	@Override
	public void onWrong(MulityChoiceAnswers selectedAnwser) {
		cancelLimitAnimation();
		mSmallPlayer.stop();
		String errorHint = null;
		// 未到底， 暂存选择结果
		MultiChoiceResult result = new MultiChoiceResult();
		result.totalTime = LIMIT_TIME;
		result.costTime = LIMIT_TIME - mLeftDownTime;
		result.tryTimes = curTryTimes; // 在reheasal的时候只要try time>1，代表该题做错了
		result.maxScore = curMultyChoice.getScore();
		result.isCorrect = false;
		result.questionId = mRehearsChunkList.get(curMultiChoiceIndex)
				.getChunkCode() + "_" + curMultyChoice.getOrder();
		result.questionText = curMultyChoice.getContent();
		result.answerId = result.questionId
				+ "_"
				+ (selectedAnwser == null ? "" : Integer
						.toString(selectedAnwser.getOrder()));
		result.answerText = selectedAnwser == null ? "" : selectedAnwser
				.getAnswer();
		mChoiceResultList.add(result);
		// Assemble achievement
		Achievement achievement = new Achievement();
		achievement.setBella_id(AppConst.CurrUserInfo.UserId);
		achievement.setPlan_id("");
		achievement.setCourse_id(mChunkModel.getChunkCode());
		achievement.setScore(0);
		achievement.setStart_client_date(PreferencesUtils.getString(this, AppConst.CacheKeys.TIME_START));
		achievement.setEnd_client_date(TimeFormatUtil.getUTCTimeStr());
		achievement.setUpdate_progress_type(Achievement.TYPE_LOSE_PRACTICE);
		// post result to server
		postUserAchievement(achievement);
		PracticeErrorPopWindow errorPopup = null;
		if (!isTimeout) {
			errorHint = mPracticeAdapter.getSelectedAnswer() == null ? null
					: mPracticeAdapter.getSelectedAnswer().getHitString();
			cancelLimitAnimation();
			errorPopup = new PracticeErrorPopWindow(ChunkRehearsalActivty.this,
					errorHint, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (curMultiChoiceIndex < mRehearsChunkList.size() - 1) {
								// 未到底，
								curMultiChoiceIndex++;
								curTryTimes = 1;
								initEvents();
							} else
								onLastChoiceSelected();
						}
					});
		} else {
			errorHint = getString(R.string.chunk_practice_error_popup_timeout);
			errorPopup = new PracticeErrorPopWindow(ChunkRehearsalActivty.this,
					null, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (curMultiChoiceIndex < mRehearsChunkList.size() - 1) {
								// 未到底，
								curMultiChoiceIndex++;
								curTryTimes = 1;
								initEvents();
							} else
								onLastChoiceSelected();
						}
					});
			errorPopup.setTitle(errorHint);
		}
		errorPopup.open();

	}

	/** 选择了正确项 */
	@Override
	public void onCorrect(MulityChoiceAnswers selectedAnwser) {
		cancelLimitAnimation();
		mSmallPlayer.stop();
		// 属于practice
		// 未到底， 暂存选择结果
		MultiChoiceResult result = new MultiChoiceResult();
		result.totalTime = LIMIT_TIME;
		result.costTime = LIMIT_TIME - mLeftDownTime;
		result.tryTimes = curTryTimes;
		result.maxScore = curMultyChoice.getScore();
		result.isCorrect = true;
		result.questionId = mRehearsChunkList.get(curMultiChoiceIndex)
				.getChunkCode() + "_" + curMultyChoice.getOrder();
		result.questionText = curMultyChoice.getContent();
		result.answerId = result.questionId
				+ "_"
				+ (selectedAnwser == null ? "" : Integer
						.toString(selectedAnwser.getOrder()));
		result.answerText = selectedAnwser == null ? "" : selectedAnwser
				.getAnswer();
		mChoiceResultList.add(result);
		// Assemble achievement
		Achievement achievement = new Achievement();
		achievement.setBella_id(AppConst.CurrUserInfo.UserId);
		achievement.setPlan_id("");
		achievement.setCourse_id(mChunkModel.getChunkCode());
		achievement.setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM);
		achievement.setStart_client_date(PreferencesUtils.getString(this, AppConst.CacheKeys.TIME_START));
		achievement.setEnd_client_date(TimeFormatUtil.getUTCTimeStr());
		achievement.setUpdate_progress_type(Achievement.TYPE_WIN_PRACTICE);
		// post result to server
		postUserAchievement(achievement);
		// 增加积分
		if (result.isCorrect) {
			int rehearseScore = ScoreLevelHelper.getMultiChoiceScore(
					result.maxScore, result.totalTime, result.costTime,
					result.tryTimes);
			increasingScore(rehearseScore);
		}
		if (curTryTimes <= 1) // 表示第一次就选择正确
			mCorrectNum++;
		if (mMultiChoiceType == Multi_Choice_Type_Rehearsal) { // 属于rehearsal
			if (curMultiChoiceIndex < mRehearsChunkList.size() - 1) {
				// 未到底，
				curMultiChoiceIndex++;
				curTryTimes = 1;
				initEvents();
			} else
				onLastChoiceSelected();
		}

	}

	int userScore;
	int totalIncreasedScore;

	/**
	 * 最后一题做完
	 */
	private void onLastChoiceSelected() {
		// stop the player
		cancelLimitAnimation();
		mSmallPlayer.stop();
		// 更新Chunk Rehearsal状态
		totalIncreasedScore = 0;
		for (int index = 0; index < mChoiceResultList.size(); index++) {
			Chunk chunk = mRehearsChunkList.get(index);
			MultiChoiceResult result = mChoiceResultList.get(index);
			if (result.isCorrect) { // Reheasal正确
//				mChunkBLL.updateChunkStatus(chunk.getChunkCode(),
//						ChunkBiz.CHUNK_STATUS_PRACTICED_NOT_REHARSE,
//						chunk.getRehearsalStatus() + 1, rehearseScore);
				totalIncreasedScore += ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_FORM;
			} else { // Reheasal失败
//				mChunkBLL.setRehearseFailed(chunk.getChunkCode());
			}

		}
		// 增加用户积分
		userScoreBiz.increaseScore(totalIncreasedScore);
		userScore = userScoreBiz.getUserScore();

		RehearsalResultDialogFragment resultDialog = new RehearsalResultDialogFragment(
				ChunkRehearsalActivty.this, mCorrectNum,
				mChoiceResultList == null ? 0 : mChoiceResultList.size());
		resultDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// 没有获得任何积分,直接退出
				if (totalIncreasedScore <= 0) {
					finish();
				}
				ScoresUpDialogFragment scoreUpDilaog = new ScoresUpDialogFragment(
						ChunkRehearsalActivty.this,
						ScoreLevelHelper.getDisplayLevel(userScore),
						ScoreLevelHelper.getCurrentLevelScore(userScore),
						ScoreLevelHelper.getCurrentLevelExistedScore(userScore),
						totalIncreasedScore, mContext);
				// 检查是否levelup
				final int up2Level = totalIncreasedScore > ScoreLevelHelper
						.getLevelUpScore(userScore) ? ScoreLevelHelper
						.getDisplayLevel(userScore + totalIncreasedScore) : 0;
				scoreUpDilaog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						if (up2Level <= 0)
							finish();
						else {
							LevelUpPopWindow levelUp = new LevelUpPopWindow(
									ChunkRehearsalActivty.this, up2Level);
							levelUp.setOnDismissListener(new LevelUpPopWindow.OnDismissListener() {
								@Override
								public void onDismiss() {
									finish();
								}
							});
							levelUp.open();
						}
					}
				});
				scoreUpDilaog.show(getSupportFragmentManager(), "scores up");
			}
		});
		resultDialog.show(getSupportFragmentManager(), "rehearsal result");
	}

	/**
	 * 超时
	 */
	@Override
	protected void onTimeout() {
		mSmallPlayer.stop();
		if (!isTimeout) {
			isTimeout = true;
			onWrong(null);
		}
	}

	/**
	 * 退出
	 */
	protected void onQuit() {
		cancelLimitAnimation();
		QuitPracticePopWindow dialog = new QuitPracticePopWindow(
				ChunkRehearsalActivty.this, QuitPracticePopWindow.Quit_Rehearse);
		dialog.setOnQuitListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChoiceResultList == null || mChoiceResultList.size() <= 0) {
					finish();
				} else
					onLastChoiceSelected();
			}
		});
		dialog.setOnCancelListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startLimitAnimation(false);
			}
		});
		dialog.open();
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.MultipleChioce_PracticeMeaningValues.pageNameValue,
							ContextDataMode.MultipleChioce_PracticeMeaningValues.pageSiteSubSectionValue,
							ContextDataMode.MultipleChioce_PracticeMeaningValues.pageSiteSectionValue,
							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.MultipleChioce_PracticeUseValues.pageNameValue,
							ContextDataMode.MultipleChioce_PracticeUseValues.pageSiteSubSectionValue,
							ContextDataMode.MultipleChioce_PracticeUseValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}
}
