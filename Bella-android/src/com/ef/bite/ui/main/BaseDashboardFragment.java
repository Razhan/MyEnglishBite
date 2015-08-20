package com.ef.bite.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.business.action.RehearseChunkOpenAction;
import com.ef.bite.business.task.GetUnlockChunkTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpDashboard;
import com.ef.bite.dataacces.mode.httpMode.HttpUnlockChunks;
import com.ef.bite.ui.chunk.ChunkLearnActivity;
import com.ef.bite.ui.chunk.ChunkListActivity;
import com.ef.bite.dataacces.ChunkLoader;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The basic states for dashboard
 * Created by yang on 15/5/8.
 */
public abstract class BaseDashboardFragment extends Fragment {
    protected View root;
    private RelativeLayout mLearnPhraseLayout;
    private RelativeLayout mPracticePhraseLayout;
    private RelativeLayout mMasteredPhraseLayout;
    private TextView mLearnCount;
    private TextView mPracticeCount;
    private TextView mMasteredCount;
    private TextView mLearnTitle;
    private TextView mPracticeTitle;
    private TextView masteredTitle;
    private TextView mLearnAvailableInfo;
    private TextView mPracticeAvaiableInfo;
    private HttpDashboard httpDashboard;
    private ChunkLoader chunkLoader;

    public void setupViews() {
        if (root == null) {
            return;
        }
        chunkLoader = new ChunkLoader(getActivity());

        mLearnPhraseLayout = (RelativeLayout) root
                .findViewById(R.id.home_screen_learn_layout);
        mPracticePhraseLayout = (RelativeLayout) root
                .findViewById(R.id.home_screen_practice_layout);
        mMasteredPhraseLayout = (RelativeLayout) root
                .findViewById(R.id.home_screen_mastered_layout);

        mLearnPhraseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpDashboard == null || httpDashboard.data ==null|| httpDashboard.data.new_lessons == null ||httpDashboard.data.new_lessons.isEmpty()) {
                    return;
                }

                final HttpDashboard.Lesson lesson = httpDashboard.data.new_lessons.get(0);
                chunkLoader.load(new ChunkLoader.Request(
                                lesson.course_package_url,
                                lesson.course_id,
                                lesson.course_version),
                        new ChunkLoader.OnFinishListener() {
                            @Override
                            public void doOnFinish(boolean isDone) {
                                Chunk chunk = chunkLoader.getChunk(lesson.course_id);
                                if (chunk != null) {
                                    Intent intent = new Intent(getActivity(), ChunkLearnActivity.class);
                                    intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
                                    getActivity().startActivity(intent);
//                                    new NewChunkOpenAction().open(getActivity(), chunk);
                                } else {
                                    if(NetworkChecker.isConnected(getActivity())){
                                        Toast.makeText(
                                                getActivity(),
                                                JsonSerializeHelper.JsonLanguageDeserialize(
                                                        getActivity(), "home_screen_no_new_chunks"),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                JsonSerializeHelper.JsonLanguageDeserialize(
                                                        getActivity(), "error_check_network_available"),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            }
        });
        mPracticePhraseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpDashboard == null) {
                    return;
                }
                final List<ChunkLoader.Request> requests = new ArrayList<ChunkLoader.Request>();
                for (HttpDashboard.Lesson lesson : httpDashboard.data.new_rehearsals) {
                    requests.add(new ChunkLoader.Request(
                            lesson.course_package_url,
                            lesson.course_id,
                            lesson.course_version));
                }

                chunkLoader.load(requests,
                        new ChunkLoader.OnFinishListener() {
                            @Override
                            public void doOnFinish(boolean isDone) {
                                List<Chunk> chunks = chunkLoader.getChunkList(requests);
                                addChunkStatus(chunks, httpDashboard);
                                if (chunks != null && chunks.size() > 0) {
                                    new RehearseChunkOpenAction().open(getActivity(), chunks);
                                } else {
                                    if(NetworkChecker.isConnected(getActivity())){
                                        Toast.makeText(
                                                getActivity(),
                                                JsonSerializeHelper.JsonLanguageDeserialize(
                                                        getActivity(), "home_screen_no_rehearse_chunks"),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                JsonSerializeHelper.JsonLanguageDeserialize(
                                                        getActivity(), "error_check_network_available"),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            }
        });
        mMasteredPhraseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChunkListActivity.class);
                intent.putExtra(AppConst.BundleKeys.Chunk_List_Type, 1);
                getActivity().startActivity(intent);
            }
        });

        mLearnCount = (TextView) root
                .findViewById(R.id.home_screen_learn_count);
        mPracticeCount = (TextView) root
                .findViewById(R.id.home_sreen_practice_count);
        mMasteredCount = (TextView) root
                .findViewById(R.id.home_sreen_mastered_count);
        mLearnTitle = (TextView) root
                .findViewById(R.id.home_screen_learn_title);
        mLearnTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                getActivity(), "home_screen_learn_title"));
        mPracticeTitle = (TextView) root
                .findViewById(R.id.home_screen_practice_title);
        mPracticeTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                getActivity(), "home_screen_practice_title"));
        masteredTitle = (TextView) root
                .findViewById(R.id.home_sreen_mastered_title);
        masteredTitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                getActivity(), "profile_phrasses"));
        mLearnAvailableInfo = (TextView) root
                .findViewById(R.id.home_screen_learn_available_info);
        mLearnAvailableInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                getActivity(), "home_screen_all_done_avaiable_soon"));
        mPracticeAvaiableInfo = (TextView) root
                .findViewById(R.id.home_screen_practice_available_info);
        mPracticeAvaiableInfo.setText(JsonSerializeHelper.JsonLanguageDeserialize(getActivity(),
                "home_screen_all_done_avaiable_soon"));
//        update(httpDashboard);
    }

    private void addChunkStatus(List<Chunk> chunks, HttpDashboard httpDashboard) {
        if(chunks==null||httpDashboard==null){
            return;
        }
        for (Chunk chunk : chunks) {
            for (int i = 0; i < httpDashboard.data.new_rehearsals.size(); i++) {
                HttpDashboard.Lesson lesson = httpDashboard.data.new_rehearsals.get(i);
                if (chunk!=null && lesson!=null && StringUtils.isEquals(StringUtils.nullStrToEmpty(chunk.getChunkCode()), StringUtils.nullStrToEmpty(lesson.course_id))) {
                    chunk.setRehearsalStatus(lesson.rehearsal_status);
                    break;
                }
            }
        }
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public RelativeLayout getmLearnPhraseLayout() {
        return mLearnPhraseLayout;
    }

    public RelativeLayout getmPracticePhraseLayout() {
        return mPracticePhraseLayout;
    }

    public RelativeLayout getmMasteredPhraseLayout() {
        return mMasteredPhraseLayout;
    }

    public TextView getmLearnCount() {
        return mLearnCount;
    }

    public TextView getmPracticeCount() {
        return mPracticeCount;
    }

    public TextView getmMasteredCount() {
        return mMasteredCount;
    }

    public TextView getmLearnTitle() {
        return mLearnTitle;
    }

    public TextView getmPracticeTitle() {
        return mPracticeTitle;
    }

    public TextView getMasteredTitle() {
        return masteredTitle;
    }

    public TextView getmLearnAvailableInfo() {
        return mLearnAvailableInfo;
    }

    public TextView getmPracticeAvaiableInfo() {
        return mPracticeAvaiableInfo;
    }

    public void update(HttpDashboard httpDashboard) {
        this.httpDashboard = httpDashboard;
        if (httpDashboard == null) {
            return;
        }

        getmLearnAvailableInfo().setText(getAvailableLeftTimeText(
                (long) httpDashboard.data.new_lesson_unlocking_seconds * 1000, true));
        getmPracticeAvaiableInfo().setText(getAvailableLeftTimeText(
                (long) httpDashboard.data.new_rehearsal_unlocking_seconds * 1000, false));
        getmLearnCount().setText(httpDashboard.data.new_lesson_count + "");
        getmPracticeCount().setText(httpDashboard.data.new_rehearsals.size() + "");
        getmMasteredCount().setText(httpDashboard.data.phrase_count + "");
    }

    public HttpDashboard getHttpDashboard() {
        return httpDashboard;
    }

    public void setHttpDashboard(HttpDashboard httpDashboard) {
        this.httpDashboard = httpDashboard;
    }

    long ONE_MINITUE = 60 * 1000;
    long ONE_HOUR = 60 * ONE_MINITUE;
    long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 显示将来可用的具体剩余时间
     *
     * @param availableTime
     * @param isLearn       是否是learn，或者practice
     * @return
     */
    public String getAvailableLeftTimeText(Long availableTime, boolean isLearn) {
        // String TimeText = null;
        try {
            if (availableTime == null)
                return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
                        getActivity(), "home_screen_learn_available_tomorrow")
                        : JsonSerializeHelper.JsonLanguageDeserialize(getActivity(),
                        "home_screen_practice_available_tomorrow");
            if (availableTime >= ONE_DAY)
                return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
                        getActivity(), "home_screen_learn_available_tomorrow")
                        : JsonSerializeHelper.JsonLanguageDeserialize(getActivity(),
                        "home_screen_practice_available_tomorrow");
            if (availableTime < ONE_DAY && availableTime >= ONE_HOUR) {
                int leftHour = (int) (availableTime / ONE_HOUR);
                return String.format(
                        isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
                                getActivity(), "home_screen_learn_available_hours")
                                : JsonSerializeHelper.JsonLanguageDeserialize(
                                getActivity(),
                                "home_screen_practice_available_hour"),
                        leftHour);
            }
            if (availableTime < ONE_HOUR && availableTime > 0) {
                int leftMinutes = (int) (availableTime / ONE_MINITUE);
                return String
                        .format(isLearn ? JsonSerializeHelper
                                        .JsonLanguageDeserialize(getActivity(),
                                                "home_screen_learn_available_tomorrow")
                                        : JsonSerializeHelper
                                        .JsonLanguageDeserialize(getActivity(),
                                                "home_screen_practice_available_minutes"),
                                leftMinutes);
            }
            if (availableTime <= 0) {
                return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
                        getActivity(), "home_screen_learn_available_now")
                        : JsonSerializeHelper.JsonLanguageDeserialize(getActivity(),
                        "home_screen_practice_available_now");
            }
            return isLearn ? JsonSerializeHelper.JsonLanguageDeserialize(
                    getActivity(), "home_screen_learn_available_tomorrow")
                    : JsonSerializeHelper.JsonLanguageDeserialize(getActivity(),
                    "home_screen_practice_available_tomorrow");
        } catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
            return "0";
        }

    }

    public void getUnlockChunk(){
        new GetUnlockChunkTask(getActivity(), new PostExecuting<HttpUnlockChunks>() {
            @Override
            public void executing(HttpUnlockChunks httpUnlockChunks) {
                if(httpUnlockChunks!=null && httpUnlockChunks.status.equals("0")){
                    if(httpUnlockChunks.data.size()<1){
                        return;
                    }

                    final HttpDashboard.Lesson lesson = httpUnlockChunks.data.get(0);
                    chunkLoader.load(new ChunkLoader.Request(lesson.course_package_url,lesson.course_id,lesson.course_version), new ChunkLoader.OnFinishListener() {
                        @Override
                        public void doOnFinish(boolean isDone) {
                            Chunk chunk = chunkLoader.getChunk(lesson.course_id);
                            if (chunk != null) {
                                Intent intent = new Intent(getActivity(), ChunkLearnActivity.class);
                                intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
                                getActivity().startActivity(intent);
//                                    new NewChunkOpenAction().open(getActivity(), chunk);
                            } else {
                                if(NetworkChecker.isConnected(getActivity())){
                                    Toast.makeText(
                                            getActivity(),
                                            JsonSerializeHelper.JsonLanguageDeserialize(
                                                    getActivity(), "home_screen_no_new_chunks"),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(
                                            getActivity(),
                                            JsonSerializeHelper.JsonLanguageDeserialize(
                                                    getActivity(), "error_check_network_available"),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
            }
        }).execute(AppConst.CurrUserInfo.UserId);
    }


}
