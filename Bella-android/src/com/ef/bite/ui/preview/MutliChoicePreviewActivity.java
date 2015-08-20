package com.ef.bite.ui.preview;

import android.content.Intent;
import com.ef.bite.R;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ef.bite.AppConst;
import com.ef.bite.AppSession;
import com.ef.bite.adapters.ChunkPracticeListAdapter;
import com.ef.bite.animation.AudioPlayerMoveAnimation;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.ui.chunk.BaseMultiChoiceActivity;
import com.ef.bite.ui.popup.BasePopupWindow;
import com.ef.bite.ui.popup.ChunkDonePopWindow;
import com.ef.bite.ui.popup.PracticeErrorPopWindow;
import com.ef.bite.utils.HighLightStringHelper;

public class MutliChoicePreviewActivity extends BaseMultiChoiceActivity {

	protected int mMultiChoiceType = Multi_Choice_Type_Preview;

	@Override
	protected void initEvents() {
		curMultyChoice = null;
		// 类型是Practice
		if (mMultiChoiceType == Multi_Choice_Type_Preview
				&& mChunkModel != null
				&& mChunkModel.getMulityChoiceQuestions() != null
				&& mChunkModel.getMulityChoiceQuestions().size() > 0
				&& mChunkBLL.getPraticeMultiChoiceList(mChunkModel) != null) {
			curMultyChoice = mChunkModel.getMulityChoiceQuestions().get(
					curMultiChoiceIndex);
			// mIndicator.setIndicator(curMultiChoiceIndex, mChunkModel
			// .getMulityChoiceQuestions().size());
//			mIndicator.removeAllViews();
//			mPageIndicat(mIndicator, curMultiChoiceIndex + 1, mChunkBLL
//					.getPraticeMultiChoiceList(mChunkModel).size());
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
								MutliChoicePreviewActivity.this);
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

	@Override
	protected void onQuit() {
		if (mMultiChoiceType == Multi_Choice_Type_Preview) {
			setResult(AppConst.ResultCode.CHUNK_PRACTICE_QUIT);
			finish();
			overridePendingTransition(R.anim.activity_in_from_left,
					R.anim.activity_out_to_right);
		}
	}

	/** 选择了错误的答案 **/
	@Override
	public void onWrong(MulityChoiceAnswers selectedAnwser) {
		cancelLimitAnimation();
		mSmallPlayer.stop();
		String errorHint = mPracticeAdapter.getSelectedAnswer() == null ? null
				: mPracticeAdapter.getSelectedAnswer().getHitString();
		PracticeErrorPopWindow errorPopup = new PracticeErrorPopWindow(
				MutliChoicePreviewActivity.this, errorHint,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// to learn
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
		errorPopup.open();
	}

	/** 选择了正确项 */
	@Override
	public void onCorrect(MulityChoiceAnswers selectedAnwser) {
		if (mMultiChoiceType == Multi_Choice_Type_Preview) {
			if (curMultiChoiceIndex < mChunkModel.getMulityChoiceQuestions()
					.size() - 1) {
				curMultiChoiceIndex++;
				curTryTimes = 1;
				enableMultiChoice = false;
				initEvents();
			} else {
				// stop the player
				cancelLimitAnimation();
				mSmallPlayer.stop();
				ChunkDonePopWindow donDialog = new ChunkDonePopWindow(
						MutliChoicePreviewActivity.this,
						mChunkModel.getChunkText(), mChunkModel.getChunkCode(),
						mContext);
				donDialog
						.setOnCloseListener(new BasePopupWindow.OnCloseListener() {
							@Override
							public void onClose() {
								// AppSession.getInstance().clear();
								// Intent intent = new Intent(mContext,
								// ChunkPreviewActivity.class);
								Intent intent = new Intent(mContext,
										CoursePreviewListActivity.class);
								startActivity(intent);
							}
						});
				donDialog.open();
			}
		}
	}

	@Override
	protected void onTimeout() {
		// TODO Auto-generated method stub

	}

}
