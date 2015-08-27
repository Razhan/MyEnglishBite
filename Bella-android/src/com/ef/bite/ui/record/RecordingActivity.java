package com.ef.bite.ui.record;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;
import com.czt.mp3recorder.MP3Recorder;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.UserScoreBiz;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.UploadRecordingTask;
import com.ef.bite.lang.Closure;
import com.ef.bite.ui.chunk.BaseChunkActivity;
import com.ef.bite.ui.guide.RecordGuideAvtivity;
import com.ef.bite.ui.main.MainActivity;
import com.ef.bite.utils.FileStorage;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.DonutProgress;
import com.ef.bite.widget.HeaderView;
import com.ef.bite.widget.PagesIndicator;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Record user's practice and upload it
 * Created by yang on 15/3/19.
 */
public class RecordingActivity extends BaseChunkActivity {

    private File mRecAudioFile;// Recording output file
    // private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private FileStorage recStorage;
    private static String PREFIX = "rec";// The prefix name of recording file
    private static int DURATION = 10;// Recording limit time,default is 10s
    private static int CREDITS = 15;// Reward credits
    private Button recBtn;// Recording button
    private Button playBtn;// Replay button
    private Button submitBtn;// Submit button
    private TextView titleView;
    private TextView tipsView;
    private TextView skipView;
    private TextView scoreView;
    private DonutProgress recProgress;
    private Timer progressTimer;
    private Timer counterTimer;
    private ImageView audioView;
    private ProgressDialog progressDialog;
    private MP3Recorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chunk_recording);
        super.onCreate(savedInstanceState);
        recStorage = new FileStorage(this, AppConst.CacheKeys.Storage_Recording);
        setupViews();
        openGuide();
        tracking();

    }


    // Initialize views
    private void setupViews() {

        PagesIndicator indicator = (PagesIndicator) findViewById(R.id.indicator);
        indicator.setIndicator(4, 4);

        HeaderView headerView = (HeaderView) findViewById(R.id.header);
        progressbar = headerView.getProgressbar();

        titleView = (TextView) findViewById(R.id.tv_title);
        tipsView = (TextView) findViewById(R.id.tv_tips);
        tipsView.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "recording_view_record"));
        scoreView = (TextView) findViewById(R.id.tv_score);
        audioView = (ImageView) findViewById(R.id.iv_audio);
        recProgress = (DonutProgress) findViewById(R.id.recorder_progress);
        recBtn = (Button) findViewById(R.id.btn_rec);
        playBtn = (Button) findViewById(R.id.btn_play);
        submitBtn = (Button) findViewById(R.id.btn_post);
        submitBtn.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "record_txt_post"));
        skipView = (TextView) findViewById(R.id.tv_skip);
        skipView.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
                "recoding_view_skip"));

        recBtn.setOnClickListener(startRecListener);
        playBtn.setOnClickListener(startPlayListener);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadRecording();
            }
        });
        if (mChunkModel != null) {
            titleView.setText(String.format(JsonSerializeHelper
                    .JsonLanguageDeserialize(mContext,
                            "recording_view_instruction"), mChunkModel
                    .getCoursename()));
        }
        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUsersRecordAct();
                finish();
            }
        });

        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // On recording start
    View.OnClickListener startRecListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecStart();
            MobclickTracking.OmnitureTrack.ActionTrackingRecording(1);
        }
    };

    // On recording stop
    View.OnClickListener stopRecListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecStop();
        }
    };

    // on player start
    View.OnClickListener startPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlayStart();
        }
    };

    // on player stop
    View.OnClickListener stopPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlayStop();
        }
    };

    /**
     * Initialize MediaRecorder
     */
    // private void initMediaRecorder() {
    // try {
    // // Kill old recording TempFile
    // recStorage.clearAll();
    // // Create the recording file
    // mRecAudioFile = File.createTempFile(PREFIX, ".aac",
    // recStorage.getStorageDir());
    // // Instantiate the MediaRecorder object
    // mMediaRecorder = new MediaRecorder();
    // // Setup the microphone
    // mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    // // Setup the format of the output file
    // mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
    // // Setup the audio file encoding
    // mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    // // Setup the path of the output file
    // mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
    // // Prepare
    // mMediaRecorder.prepare();
    // // Start
    // mMediaRecorder.start();
    // } catch (IOException e) {
    // tipsView.setText("Sorry,your phone was not able to record any sound");
    // e.printStackTrace();
    // }
    // }
    private void initMp3Recorder() {
        try {
            recStorage.clearAll();
            mRecAudioFile = File.createTempFile(PREFIX, ".mp3",
                    recStorage.getStorageDir());
            if (mRecAudioFile == null) {
                return;
            }
            mMediaRecorder = new MP3Recorder(mRecAudioFile);
            mMediaRecorder.start();

        } catch (IOException e) {
            tipsView.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                    mContext, "recording_view_no_sound"));
            MobclickTracking.OmnitureTrack.ActionTrackingRecording(0);
            e.printStackTrace();
        }
    }

    private void initMediaPlayer() {
        if (mRecAudioFile == null) {
            return;
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mRecAudioFile.getAbsolutePath());
            mMediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            onPlayStop();
                        }
                    });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playRec() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.start();
    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * draw progress circle
     */
    private void startProgress(long duration, final Closure callback) {
        if (duration <= 0) {
            recProgress.setProgress(0);
            return;
        }
        recProgress.setProgress(0);
        progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recProgress.setProgress(recProgress.getProgress() + 1);
                        if (recProgress.getProgress() >= 360) {
                            progressTimer.cancel();
                            if (callback != null) {
                                callback.execute(null);
                            }
                        }
                    }
                });
            }
        }, 500, duration / 360);
    }

    /**
     * update counter
     */
    private void startCounter() {
        counterTimer = new Timer();
        counterTimer.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tipsView.setText("" + (DURATION - count));
                        count++;
                        if (DURATION == count) {
                            counterTimer.cancel();
                        }
                    }
                });
            }
        }, 500, 1000);
    }

    private void stopProgress() {
        if (progressTimer != null) {
            progressTimer.cancel();
        }
        if (counterTimer != null) {
            counterTimer.cancel();
        }
    }

    private void onRecStart() {
        recBtn.setOnClickListener(stopRecListener);
        playBtn.setVisibility(View.GONE);
        audioView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);
        switchProgressColor(true);
        onPlayStop();
        startProgress(1000 * DURATION, new Closure() {
            @Override
            public void execute(Object result) {
                onRecStop();// do something after counting stop
            }
        });
        startCounter();
        // initMediaRecorder();
        initMp3Recorder();
        updateDecibelStatus();
    }

    private void onRecStop() {
        recBtn.setOnClickListener(startRecListener);
        audioView.setVisibility(View.INVISIBLE);
        stopProgress();
        stopMp3Record();
        // stopRecord();
        if (mRecAudioFile != null) {
//            MobclickTracking.OmnitureTrack
//                    .ActionTrackingRecordingSuccessful(mContext);
            tipsView.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                    mContext, "recoding_view_complete"));// completed
            // information
            playBtn.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * to stop record
     */
    // private void stopRecord() {
    // if (mMediaRecorder != null) {
    // // Stop recording
    // mMediaRecorder.stop();
    // // Release MediaRecorder
    // mMediaRecorder.release();
    // mMediaRecorder = null;
    // }
    // }
    private void stopMp3Record() {
        if (mMediaRecorder != null) {
            // Stop recording
            mMediaRecorder.stop();
            // Release MediaRecorder
            mMediaRecorder = null;
        }
    }

    private void onPlayStart() {
        playBtn.setBackgroundResource(R.drawable.ic_stop);
        playBtn.setOnClickListener(stopPlayListener);
        switchProgressColor(false);
        initMediaPlayer();
        startProgress(mMediaPlayer.getDuration(), null);
        playRec();
    }

    private void onPlayStop() {
        playBtn.setBackgroundResource(R.drawable.ic_play);
        playBtn.setOnClickListener(startPlayListener);
        stopProgress();
        startProgress(0, null);
        stopMediaPlayer();
    }

    /**
     * Audio decibel meter
     */
    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateDecibelStatus();
        }
    };

    private int BASE = 1;// the reference amplitude values
    private int DELAY = 100;// Sampling interval time

    private void updateDecibelStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getVolume() / BASE;
            double db = 0;// decibel
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            updateAudioView(db);
            // Log.d("---dB", "dB：" + db);
            mHandler.postDelayed(mUpdateMicStatusTimer, DELAY);
        } else {
            updateAudioView(0);
        }
    }

    private void updateAudioView(double db) {
        if (db > 45) {
            audioView.setBackgroundResource(R.drawable.audio_level_3);
        } else if (db > 35) {
            audioView.setBackgroundResource(R.drawable.audio_level_2);
        } else if (db > 25) {
            audioView.setBackgroundResource(R.drawable.audio_level_1);
        } else {
            audioView.setBackgroundResource(R.drawable.audio_level_0);
        }
    }

    private void switchProgressColor(boolean isActived) {
        recProgress.setFinishedStrokeColor(getResources().getColor(
                isActived ? R.color.bella_prgress_fg
                        : R.color.bella_prgress_fg_2));
        recProgress.setUnfinishedStrokeColor(getResources().getColor(
                isActived ? R.color.bella_prgress_bg
                        : R.color.bella_prgress_bg_2));
        recBtn.setBackgroundResource(isActived ? R.drawable.mic_active
                : R.drawable.mic_inactive);
    }

    private void uploadRecording() {
        if (mRecAudioFile == null) {
            toast("No recording file");
            return;
        }
        if (mChunkModel == null) {
            toast("No Course file");
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
                mContext, "record_msg_uploading"));
        progressDialog.setCancelable(false);
        progressDialog.show();
        UploadRecordingTask task = new UploadRecordingTask(this, mRecAudioFile,
                mChunkModel.getChunkCode(),
                String.valueOf(getDuration() / 1000), "15",
                new PostExecuting<Boolean>() {
                    @Override
                    public void executing(Boolean result) {
                        progressDialog.dismiss();
                        if (result) {
                            toast(JsonSerializeHelper.JsonLanguageDeserialize(
                                    mContext, "record_msg_upload_completed"));
                            increasingScore(CREDITS);//
                            addScore(CREDITS);

                            MobclickTracking.OmnitureTrack.ActionTrackingRecordingSuccessful(mContext);

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
//                                    openUsersRecordAct();
                                    startActivity(new Intent(mContext, MainActivity.class).putExtra("com.parse.Data", getStringExtra("com.parse.Data")));
                                    finish();
                                }
                            }, 500);
                        } else {
                            toast(JsonSerializeHelper.JsonLanguageDeserialize(
                                    mContext, "record_msg_upload_failed"));
                        }
                    }
                });
        task.execute();
    }

    /**
     * Return recording duration
     *
     * @return
     */
    private int getDuration() {
        initMediaPlayer();
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgress();
        stopMp3Record();
        // stopRecord();
        stopMediaPlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Increasing score animation
     *
     * @param increasedScore
     */
    protected void increasingScore(int increasedScore) {
        // Score Increasing Text initialized
        scoreView.setVisibility(View.VISIBLE);
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.balloon_label_size));
        scoreView.setText("+" + Integer.toString(increasedScore));
        int cur_color = getResources()
                .getColor(R.color.bella_color_orange_dark);
        scoreView.setTextColor(cur_color);
        // Start user level increasing animation
        mLevel.startIncreasingScore(increasedScore, cur_color);
        // Start the increasing text animation
        scoreView.setVisibility(View.VISIBLE);
        ValueAnimator disappearAnim = ValueAnimator.ofFloat(1, 0);
        disappearAnim
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float alpha = (Float) animation.getAnimatedValue();
                        scoreView.setAlpha(alpha);
                    }
                });
        disappearAnim.setDuration(1000);
        disappearAnim.start();
    }

    /**
     * update score in DB
     *
     * @param score
     */
    private void addScore(int score) {
        UserScoreBiz userScoreBiz = new UserScoreBiz(this);
        userScoreBiz.increaseScore(score);
    }

    private void tracking() {
        MobclickTracking.OmnitureTrack.AnalyticsTrackState(
                ContextDataMode.RecordingValues.pageNameValue,
                ContextDataMode.RecordingValues.pageSiteSubSectionValue,
                ContextDataMode.RecordingValues.pageSiteSectionValue, this);
    }

    private void openGuide() {
        if (PreferencesUtils.getBoolean(this,
                AppConst.CacheKeys.Storage_Record_guide)) {
            return;
        }

        PreferencesUtils.putBoolean(this,
                AppConst.CacheKeys.Storage_Record_guide, true);
        startActivity(new Intent(this, RecordGuideAvtivity.class));

    }

    private void openUsersRecordAct() {
        Intent intent = new Intent(RecordingActivity.this,
                UserRecordingActivity.class);
        intent.putExtra(AppConst.BundleKeys.Chunk, mChunkModel);
        intent.putExtra(AppConst.BundleKeys.BELLAID,
                AppConst.CurrUserInfo.UserId);
        startActivity(intent);
    }
}
