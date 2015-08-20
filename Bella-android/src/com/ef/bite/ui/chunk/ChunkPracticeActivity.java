package com.ef.bite.ui.chunk;

import android.content.DialogInterface;
import android.content.Intent;

import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppSession;
import com.ef.bite.R;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ChunkPracticeListAdapter;
import com.ef.bite.animation.AudioPlayerMoveAnimation;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.dataacces.mode.Achievement;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.ui.guide.GuideReviewRecordingActivity;
import com.ef.bite.ui.popup.BaseDialogFragment.OnDismissListener;
import com.ef.bite.ui.popup.BasePopupWindow;
import com.ef.bite.ui.popup.ChunkDonePopWindow;
import com.ef.bite.ui.popup.LevelUpPopWindow;
import com.ef.bite.ui.popup.PracticeErrorPopWindow;
import com.ef.bite.ui.popup.QuitPracticePopWindow;
import com.ef.bite.ui.popup.ScoresUpDialogFragment;
import com.ef.bite.ui.record.ReviewActivity;
import com.ef.bite.utils.HighLightStringHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.ScoreLevelHelper;
import com.ef.bite.utils.TimeFormatUtil;
import com.ef.bite.widget.DotProgressbar;

public class ChunkPracticeActivity extends BaseMultiChoiceActivity {

	protected int curMultiChoiceIndex = 0; // 当前的Practice选项
	boolean isTimeout = false;
	private String lvl;
	private final static int MultiChoiceLearn_mean = 1;
	private final static int MultiChoiceLearn_use = 2;
	private final static int PhraseLearnedMessageValues = 3;
	private final static int LevelUpMessageValues = 4;
	private DotProgressbar progressbar;
	private int progress_count=4;

	@Override
	protected void initEvents() {
		Log.i("ChunkPracticeActivity", "ChunkPracticeActivity");
		isTimeout = false;
		curMultyChoice = null;
		progressbar=headerView.getProgressbar();
		progressbar.setVisibility(View.VISIBLE);
		progressbar.init(5, progress_count);

		// 类型是Practice
		if (mMultiChoiceType == Multi_Choice_Type_Practice
				&& mChunkModel != null
				&& mChunkModel.getMulityChoiceQuestions() != null
				&& mChunkModel.getMulityChoiceQuestions().size() > 0
				&& mChunkBLL.getPraticeMultiChoiceList(mChunkModel) != null) {
			curMultyChoice = mChunkBLL.getPraticeMultiChoiceList(mChunkModel)
					.get(curMultiChoiceIndex);
			if (curMultiChoiceIndex == 0) {
				BI_Tracking(MultiChoiceLearn_mean);
			} else {
				BI_Tracking(MultiChoiceLearn_use);
			}

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
								ChunkPracticeActivity.this);
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
				} else if (curTryTimes > 1) {
					// 背景颜色设置为红色
					ViewGroup.LayoutParams limitViewParam = mLimitView
							.getLayoutParams();
					limitViewParam.height = headerHeight;
					mLimitView.setLayoutParams(limitViewParam);
					mLimitView.setBackgroundColor(getResources().getColor(
							R.color.bella_color_orange_light));
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
		String errorHint = "";
		PracticeErrorPopWindow errorPopup = null;
		if (isTimeout && selectedAnwser == null) {
			errorHint = JsonSerializeHelper.JsonLanguageDeserialize(mContext,
					"chunk_practice_error_popup_timeout");
			errorPopup = new PracticeErrorPopWindow(ChunkPracticeActivity.this,
					null, null, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// try again
							enableMultiChoice = true;
							curTryTimes++;
							initEvents();
						}
					});
			errorPopup.setTitle(errorHint);
		} else {
			// 选择结果
			MultiChoiceResult result = new MultiChoiceResult();
			result.totalTime = LIMIT_TIME;
			result.costTime = LIMIT_TIME - mLeftDownTime;
			result.tryTimes = curTryTimes;
			result.maxScore = curMultyChoice.getScore();
			result.questionId = mChunkModel.getChunkCode() + "_"
					+ curMultiChoiceIndex;
			result.questionText = curMultyChoice.getContent();
			result.answerId = result.questionId
					+ "_"
					+ (selectedAnwser == null ? "" : Integer
							.toString(selectedAnwser.getOrder()));
			result.answerText = selectedAnwser == null ? "" : selectedAnwser
					.getAnswer();
			result.isCorrect = false;
			mChoiceResultList.add(result);
			errorHint = mPracticeAdapter.getSelectedAnswer() == null ? null
					: mPracticeAdapter.getSelectedAnswer().getHitString();
			errorPopup = new PracticeErrorPopWindow(ChunkPracticeActivity.this,
					errorHint, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							setResult(AppConst.ResultCode.CHUNK_PRACTICE_QUIT);
							finish();
						}
					}, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// try again
							enableMultiChoice = true;
							curTryTimes++;
							initEvents();
						}
					});
		}

		errorPopup.open();
	}

	int userScore = 0;
	int totalScoreForPractice = 0;

	/** 选择了正确项 */
	@Override
	public void onCorrect(MulityChoiceAnswers selectedAnwser) {
		progress_count++;
		cancelLimitAnimation();
		mSmallPlayer.stop();
		// 属于practice
		// 未到底， 暂存选择结果
		MultiChoiceResult result = new MultiChoiceResult();
		result.totalTime = LIMIT_TIME;
		result.costTime = LIMIT_TIME - mLeftDownTime;
		result.tryTimes = curTryTimes;
		result.maxScore = curMultyChoice.getScore();
		result.questionId = mChunkModel.getChunkCode() + "_"
				+ curMultiChoiceIndex;
		result.questionText = curMultyChoice.getContent();
		result.answerText = selectedAnwser == null ? "" : selectedAnwser
				.getAnswer();
		result.answerId = result.questionId
				+ "_"
				+ (selectedAnwser == null ? "" : Integer
						.toString(selectedAnwser.getOrder()));
		result.isCorrect = true;
		mChoiceResultList.add(result);
		// increase score animation
		if (result.isCorrect) {
			int increase = ScoreLevelHelper.getMultiChoiceScore(
					result.maxScore, result.totalTime, result.costTime,
					result.tryTimes);
			increasingScore(increase);
		}
		if (curTryTimes <= 1) // 表示第一次就选择正确
			mCorrectNum++;
		if (mMultiChoiceType == Multi_Choice_Type_Practice) {
			if (curMultiChoiceIndex < mChunkBLL.getPraticeMultiChoiceList(
					mChunkModel).size() - 1) {
				curMultiChoiceIndex++;
				curTryTimes = 1;
				enableMultiChoice = false;
				initEvents();
			} else {
				// 更新Chunk状态
				userScore = userScoreBiz.getUserScore();

				// update user score
				for (MultiChoiceResult _result : mChoiceResultList) {
					if (_result.isCorrect)
						totalScoreForPractice += ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_USE;
				}
				totalScoreForPractice=totalScoreForPractice+ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_USE;
				userScoreBiz.increaseScore(totalScoreForPractice);
				// Assemble achievement
				Achievement achievement = new Achievement();
				achievement.setBella_id(AppConst.CurrUserInfo.UserId);
				achievement.setPlan_id("");
				achievement.setCourse_id(mChunkModel.getChunkCode());
				achievement.setScore(totalScoreForPractice);
				achievement.setStart_client_date(PreferencesUtils.getString(this, AppConst.CacheKeys.TIME_START));
				achievement.setEnd_client_date(TimeFormatUtil.getUTCTimeStr());
				achievement.setUpdate_progress_type(Achievement.TYPE_COMPLETE_LEARN);
				// Post to server
				postUserAchievement(achievement);
				ChunkDonePopWindow donDialog = new ChunkDonePopWindow(
						ChunkPracticeActivity.this, mChunkModel.getChunkText(),
						mChunkModel.getChunkCode(), mContext);
				donDialog
						.setOnCloseListener(new BasePopupWindow.OnCloseListener() {
							@Override
							public void onClose() {
								// 积分增加dialog
								ScoresUpDialogFragment scoreUpDilaog = new ScoresUpDialogFragment(
										ChunkPracticeActivity.this,
										ScoreLevelHelper
												.getDisplayLevel(userScore),
										ScoreLevelHelper
												.getCurrentLevelScore(userScore),
										ScoreLevelHelper
												.getCurrentLevelExistedScore(userScore),
										totalScoreForPractice, mContext);
								// 检查是否levelup
								final int up2Level = totalScoreForPractice > ScoreLevelHelper
										.getLevelUpScore(userScore) ? ScoreLevelHelper
										.getDisplayLevel(userScore
												+ totalScoreForPractice) : 0;
								scoreUpDilaog
										.setOnDismissListener(new OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {
												if (up2Level <= 0) {
													openMainActivity();
												} else {
													LevelUpPopWindow levelUp = new LevelUpPopWindow(
															ChunkPracticeActivity.this,
															up2Level);
													levelUp.setOnDismissListener(new LevelUpPopWindow.OnDismissListener() {
														@Override
														public void onDismiss() {
															openMainActivity();
														}
													});
													levelUp.open();
													lvl = String
															.valueOf(up2Level);
													BI_Tracking(LevelUpMessageValues);

												}
											}
										});
								scoreUpDilaog.show(getSupportFragmentManager(),
										"scores up");
							}
						});
				donDialog.open();
				BI_Tracking(PhraseLearnedMessageValues);

			}
		}
	}

	/**
	 * 退出
	 */
	@Override
	protected void onQuit() {
		cancelLimitAnimation();
		QuitPracticePopWindow dialog = new QuitPracticePopWindow(
				ChunkPracticeActivity.this);
		dialog.setOnQuitListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(AppConst.ResultCode.CHUNK_PRACTICE_QUIT);
				finish();
				if (mMultiChoiceType == Multi_Choice_Type_Practice)
					overridePendingTransition(R.anim.activity_in_from_left,
							R.anim.activity_out_to_right);
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
	protected void onTimeout() {
		mSmallPlayer.stop();
		if (!isTimeout) {
			isTimeout = true;
			onWrong(null);
		}
	}


	/**
	 * chunk学完以后回到主页面
	 */
	private void openMainActivity() {
		AppConst.GlobalConfig.IsChunkPerDayLearned = true;
		// new HomeScreenOpenAction().open(mContext, null);
		// Intent intent = new Intent(mContext, ReviewActivity.class);
		AppSession.getInstance().clear();
		TutorialConfigSharedStorage tutorialConfigSharedStorage = new TutorialConfigSharedStorage(
				mContext, AppConst.CurrUserInfo.UserId);
		if (tutorialConfigSharedStorage.get().Tutorial_Review_Record) {
			Intent intent = new Intent(mContext, ReviewActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning, true);
			intent.putExtra(AppConst.BundleKeys.Hide_Bottom_Lay, 1);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(mContext,
					GuideReviewRecordingActivity.class);
			intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
			intent.putExtra(AppConst.BundleKeys.Hide_Bottom_Lay, 1);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);
		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.MultipleChioce_LearnMeaningValues.pageNameValue,
							ContextDataMode.MultipleChioce_LearnMeaningValues.pageSiteSubSectionValue,
							ContextDataMode.MultipleChioce_LearnMeaningValues.pageSiteSectionValue,
							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.MultipleChioce_learnUseValues.pageNameValue,
							ContextDataMode.MultipleChioce_learnUseValues.pageSiteSubSectionValue,
							ContextDataMode.MultipleChioce_learnUseValues.pageSiteSectionValue,
							mContext);
			break;
		case 3:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.PhraseLearnedMessageValues.pageNameValue,
							ContextDataMode.PhraseLearnedMessageValues.pageSiteSubSectionValue,
							ContextDataMode.PhraseLearnedMessageValues.pageSiteSectionValue,
							mContext);
			break;
		case 4:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.LevelUpMessageValues.pageNameValue
									+ lvl,
							ContextDataMode.LevelUpMessageValues.pageSiteSubSectionValue,
							ContextDataMode.LevelUpMessageValues.pageSiteSectionValue,
							mContext);
			break;
		}
	}
}
