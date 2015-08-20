package com.ef.bite.ui.chunk;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;
import cn.trinea.android.common.util.PreferencesUtils;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.adapters.ChunkPracticeListAdapter;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.TutorialConfigBiz;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.UserServerAPI;
import com.ef.bite.dataacces.AchievementCache;
import com.ef.bite.dataacces.mode.Achievement;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.dataacces.mode.MulityChoiceQuestions;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.*;
import com.ef.bite.widget.AudioPlayerView;
import com.ef.bite.widget.HeaderView;
import com.ef.bite.widget.UserLevelView;
import com.litesuits.android.async.SimpleTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseMultiChoiceActivity extends BaseActivity implements
        ChunkPracticeListAdapter.OnCorrectListener,
        ChunkPracticeListAdapter.OnWrongListener {

    public final static int Multi_Choice_Type_Practice = 0; // 多选类型 - Practice
    public final static int Multi_Choice_Type_Rehearsal = 1; // 多选类型 - Rehearsal
    public final static int Multi_Choice_Type_Preview = 2; // 多选类型 - Content预览

    protected Chunk mChunkModel;
    protected List<Chunk> mRehearsChunkList; // Reheasal的chunk列表
    protected ImageView mGoBack;
    protected UserLevelView mLevel; // 用户级别控件
    protected TextView mScoreIncreasingText; // 加分的显示
    protected TextView mQuestionContent; // 问题
    protected TextView mChooseHeader;
    protected ListView mChoiceListView;
    // protected CirclePageIndicator mIndicator;
//	protected LinearLayout mIndicator;
    protected ChunkPracticeListAdapter mPracticeAdapter;
    protected AudioPlayerView mBigPlayer;
    protected AudioPlayerView mSmallPlayer;
    protected RelativeLayout mHeaderLayout;
    protected RelativeLayout mHeadBottomLayout;
    protected HeaderView headerView;
    protected View mLimitView;
    protected int headerHeight;

    protected int mMultiChoiceType = Multi_Choice_Type_Practice; // 当前多选的类型：
    protected int mCorrectNum = 0; // 选择正确的题数
    protected int curMultiChoiceIndex = 0; // 当前的Practice选项
    protected MulityChoiceQuestions curMultyChoice;
    protected int curTryTimes = 1; // 当前重做一个题的次数， 默认是1，即代表第一次做这个题
    protected List<MultiChoiceResult> mChoiceResultList = new ArrayList<MultiChoiceResult>(); // 多选题的结果
    protected ChunkBLL mChunkBLL = null;
    protected boolean enableMultiChoice = false; // 选择题是否可以选择
    protected boolean isTimerCanceled = false; // 是否倒计时取消了
    protected int minScoreIncreaseTextSize; // 显示分数增加的最小textsize
    protected int maxScoreIncreaseTextSize; // 显示分数增加的最大textsize
    // 关于动画
    protected final static int LIMIT_TIME = 20000; // 每一个multi-choice做题的限制时间 20秒
    protected long mLeftDownTime = LIMIT_TIME; // 动画的剩余时间
    protected long mAnimateStartTime = 0; // 动画开始时间
    private int curLimitViewHeight = 0; // 当前下
    private ValueAnimator moveAnimator = null;
    // 声音效果
    private SoundEffectUtils mSoundEffect = null;
    boolean isInterrupted = false;
    int interruptedResumeTimes = 0;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chunk_practice);
        Bundle bundle = getIntent().getExtras();

        mChunkModel = (Chunk) getSerializableExtra(AppConst.BundleKeys.Chunk);

        if (bundle != null
                && bundle.containsKey(AppConst.BundleKeys.Multi_Choice_Type))
            mMultiChoiceType = bundle
                    .getInt(AppConst.BundleKeys.Multi_Choice_Type);

        mRehearsChunkList = (List<Chunk>) getSerializableExtra(AppConst.BundleKeys.Chunk_Rehearse_List);

        mHeaderLayout = (RelativeLayout) this
                .findViewById(R.id.chunk_practice_header);
        headerView = (HeaderView) findViewById(R.id.chunk_practice_top_layout);
        mHeadBottomLayout = (RelativeLayout) this
                .findViewById(R.id.chunk_practice_bottom_layout);
        mLimitView = (View) this
                .findViewById(R.id.chunk_practice_limit_animation);
        mGoBack = headerView.getBackBtn();
        mLevel = headerView.getLevelView();
        mScoreIncreasingText = (TextView) this
                .findViewById(R.id.chunk_practice_score_increasing);
        mQuestionContent = (TextView) findViewById(R.id.chunk_practice_choice_question);
        mChooseHeader = (TextView) findViewById(R.id.chunk_practice_choice_info);
        if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "chunk_practice_choose_appropriate_answer") != null) {
            mChooseHeader.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                    mContext, "chunk_practice_choose_appropriate_answer"));
        }

        mChoiceListView = (ListView) findViewById(R.id.chunk_practice_choice_listview);
        mBigPlayer = (AudioPlayerView) findViewById(R.id.chunk_practice_audioplayer_big);
        mSmallPlayer = (AudioPlayerView) findViewById(R.id.chunk_practice_audioplayer_small);
        // mIndicator = (CirclePageIndicator)
        // findViewById(R.id.chunk_practice_indicator);
//		mIndicator = (LinearLayout) findViewById(R.id.chunk_practice_indicator);
        mSmallPlayer.setMiniStatus(true);
        mSmallPlayer.setVisibility(View.INVISIBLE);
        mGoBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuit();
            }
        });
        // 字体设置
        FontHelper.applyFont(mContext, mQuestionContent,
                FontHelper.FONT_Museo300);
        FontHelper.applyFont(mContext, mChooseHeader, FontHelper.FONT_Museo300);
        mHeaderLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        headerHeight = mHeaderLayout.getHeight();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mHeaderLayout.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        } else {
                            mHeaderLayout.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                    }
                });
        if (mChunkBLL == null)
            mChunkBLL = new ChunkBLL(mContext);
        if (userScoreBiz == null)
            userScoreBiz = new UserScoreBiz(mContext);
        int score = userScoreBiz.getUserScore();
        int level = ScoreLevelHelper.getDisplayLevel(score);
        if (level == 0)
            mLevel.setVisibility(View.INVISIBLE);
        else {
            mLevel.initialize(score);
            mLevel.setVisibility(View.VISIBLE);
        }
        minScoreIncreaseTextSize = (int) getResources().getDimension(
                R.dimen.score_increasing_text_smaller_size);
        maxScoreIncreaseTextSize = (int) getResources().getDimension(
                R.dimen.score_increasing_text_bigger_size);

        if (mSoundEffect == null)
            mSoundEffect = new SoundEffectUtils(mContext);
        // 插入tutorial中断
        if (mMultiChoiceType == Multi_Choice_Type_Rehearsal) {
            TutorialConfigBiz tutorialBiz = new TutorialConfigBiz(mContext);
            isInterrupted = tutorialBiz
                    .interrupt(TutorialConfigBiz.TUTORIAL_TYPE_REHEARSE_CHUNK);
            if (!isInterrupted)
                initEvents();
        } else
            initEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInterrupted) {
            if (interruptedResumeTimes == 1)
                initEvents();
            interruptedResumeTimes++;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (moveAnimator != null && moveAnimator.isRunning()) {
            onQuit();
        }
        // MobclickTracking.UmengTrack
        // .setPageEnd(
        // ContextDataMode.MultipleChioce_Form1Values.pageNameValue,
        // ContextDataMode.MultipleChioce_Form1Values.pageSiteSubSectionValue,
        // ContextDataMode.MultipleChioce_Form1Values.pageSiteSectionValue,
        // mContext);
    }

    @Override
    public void onBackPressed() {
        onQuit();
    }

    @Override
    public void onDestroy() {
        if (mSmallPlayer != null)
            mSmallPlayer.stop();
        cancelLimitAnimation();
        super.onDestroy();
    }

    /**
     * 时间初始化
     */
    abstract protected void initEvents();

    /**
     * 倒计时截止时间到
     * <p/>
     * 剩余的毫秒时间
     */
    abstract protected void onTimeout();

    @Override
    public void onWrong(MulityChoiceAnswers selectedAnwser) {
    }

    @Override
    public void onCorrect(MulityChoiceAnswers selectedAnwser) {
    }

    /**
     * 退出
     */
    abstract protected void onQuit();

    /**
     * 开始
     *
     * @param reset
     */
    protected void startLimitAnimation(boolean reset) {
        // 如果剩余时间不足，则不再进行倒计时动画
        if (reset) {
            mLeftDownTime = LIMIT_TIME;
            curLimitViewHeight = 0;
            mSoundEffect.play(SoundEffectUtils.TIMER_SOFT_20SECONDS);
        } else {
            mSoundEffect.resume();
        }

        if (mLeftDownTime <= 0)
            return;
        moveAnimator = ValueAnimator.ofInt(curLimitViewHeight, headerHeight);
        moveAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        curLimitViewHeight = (Integer) animation
                                .getAnimatedValue();
                        ViewGroup.LayoutParams params = mLimitView
                                .getLayoutParams();
                        params.height = curLimitViewHeight;
                        mLimitView.setLayoutParams(params);
                        // set color
                        float rating = (float) curLimitViewHeight
                                / (float) headerHeight;
                        if (rating <= 0.333)
                            mLimitView.setBackgroundColor(getResources()
                                    .getColor(R.color.green));
                        else if (rating > 0.333 && rating < 0.666)
                            mLimitView.setBackgroundColor(getResources()
                                    .getColor(R.color.white));
                        else
                            mLimitView
                                    .setBackgroundColor(getResources()
                                            .getColor(
                                                    R.color.bella_color_orange_light));
                    }
                });
        moveAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isTimerCanceled = false;
                mAnimateStartTime = System.currentTimeMillis();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isTimerCanceled) {
                    mLeftDownTime = 0;
                    onTimeout();
                }
                ViewGroup.LayoutParams params = mLimitView.getLayoutParams();
                params.height = curLimitViewHeight;
                mLimitView.setLayoutParams(params);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isTimerCanceled = true;
                long runTime = System.currentTimeMillis() - mAnimateStartTime;
                mLeftDownTime = mLeftDownTime - runTime;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

        });
        moveAnimator.setDuration(mLeftDownTime);
        moveAnimator.start();
    }

    /**
     * 取消动画 *
     */
    protected void cancelLimitAnimation() {
        if (moveAnimator != null && moveAnimator.isRunning())
            moveAnimator.cancel();
        moveAnimator = null;
        mSoundEffect.pause();
    }

    /**
     * MultiChoice的成绩
     */
    protected class MultiChoiceResult {
        long totalTime; // 限时
        long costTime; // 用时
        int maxScore; // 选择题满分的分数
        int tryTimes; // 选择题做的次数
        boolean isCorrect = true; // 是否做正确
        String questionId; // 问题的ID： chunk code + index
        String questionText; // 问题的内容
        String answerId; // 答案ID
        String answerText; // 选择的答案
    }

    /**
     * 增加积分效果
     *
     * @param increasedScore
     */
    protected void increasingScore(int increasedScore) {
        // Score Increasing Text initialized
        mScoreIncreasingText.setVisibility(View.VISIBLE);
        mScoreIncreasingText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.balloon_label_size));
        mScoreIncreasingText.setText("+" + Integer.toString(increasedScore));
        long costtime = LIMIT_TIME - mLeftDownTime;
        int cur_color = -1;
        if (costtime < LIMIT_TIME / 3)
            cur_color = getResources().getColor(R.color.green);
        else if (costtime >= LIMIT_TIME / 3 && costtime <= LIMIT_TIME * 2 / 3)
            cur_color = getResources().getColor(R.color.white);
        else
            cur_color = getResources()
                    .getColor(R.color.bella_color_orange_dark);
        mScoreIncreasingText.setTextColor(cur_color);
        // Start user level increasing animation
        mLevel.startIncreasingScore(increasedScore, cur_color);
        // Start the increasing text animation
        mScoreIncreasingText.setVisibility(View.VISIBLE);
        ValueAnimator disappearAnim = ValueAnimator.ofFloat(1, 0);
        disappearAnim
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (Float) animation.getAnimatedValue();
                        mScoreIncreasingText.setAlpha(alpha);
                    }
                });
        disappearAnim.setDuration(1000);
        disappearAnim.start();
    }

    protected void mPageIndicat(LinearLayout layout, int index, int total) {
        // TODO Auto-generated method stub
        if (index > total) {
            return;
        }
        for (int i = 1; i <= total; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(40, 40));
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
            layout.setGravity(Gravity.CENTER);
            layout.addView(imageView);
        }
    }

    public void postUserAchievement(final Achievement achievement) {

        SimpleTask<HttpBaseMessage> task = new SimpleTask<HttpBaseMessage>() {

            @Override
            protected void onPreExecute() {
                if(StringUtils.isEquals(Achievement.TYPE_COMPLETE_LEARN,achievement.getUpdate_progress_type())){
                    dashboardCache.removeLesson(achievement.getCourse_id());
                }else {
                    dashboardCache.removeRehearsal(achievement.getCourse_id());
                }
            }

            @Override
            protected HttpBaseMessage doInBackground() {
                try {
                    UserServerAPI serverAPI = new UserServerAPI(mContext);
                    return serverAPI.postAchievement(new ArrayList<Achievement>(Arrays.asList(achievement)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HttpBaseMessage result) {
                if (result != null && StringUtils.isEquals(result.status, "0")) {

                } else {
                    AchievementCache.getInstance().setPersnalCache(achievement);
                }
            }
        };

        task.execute();

    }
}
