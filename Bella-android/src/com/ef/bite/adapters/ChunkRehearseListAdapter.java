package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.ChunkBiz;
import com.ef.bite.dataacces.ChunkLoader;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpRehearsalListResponse;
import com.ef.bite.ui.chunk.ChunkLearnActivity;
import com.ef.bite.ui.record.RecordingActivity;
import com.ef.bite.ui.record.UserRecordingActivity;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;
import com.ef.bite.utils.StringUtils;
import com.ef.bite.widget.RehearseProgressView;

public class ChunkRehearseListAdapter extends
        BaseListAdapter<HttpRehearsalListResponse.courseInfo> {
    private ChunkBLL chunkBLL;
    private ChunkLoader chunkLoader;
    private boolean isMastered;

    public ChunkRehearseListAdapter(Activity context,
                                    List<HttpRehearsalListResponse.courseInfo> dataList,
                                    boolean isMastered) {
        super(context, R.layout.chunk_list_rehearse_list_item, dataList);
        chunkBLL = new ChunkBLL(context);
        chunkLoader = new ChunkLoader(context);
        this.isMastered = isMastered;
    }

    class ViewHolder {
        TextView content;
        RehearseProgressView progress;
        ImageView masteredIcon;
        ImageView line;
        ImageView micView;
        ViewGroup likeView;
        TextView percentView;
    }

    @Override
    public void getView(View layout, final int position,
                        final HttpRehearsalListResponse.courseInfo data) {
        ViewHolder holder = null;
        if (layout.getTag() == null) {
            holder = new ViewHolder();
            holder.content = (TextView) layout
                    .findViewById(R.id.chunk_list_rehearse_list_item_content);
            holder.progress = (RehearseProgressView) layout
                    .findViewById(R.id.chunk_list_rehearse_list_item_progress);
            holder.masteredIcon = (ImageView) layout
                    .findViewById(R.id.chunk_list_mastered_list_item_icon);
            holder.line = (ImageView) layout
                    .findViewById(R.id.chunk_list_rehearse_list_item_line);
            holder.micView = (ImageView) layout.findViewById(R.id.iv_recording);
            holder.likeView = (ViewGroup) layout.findViewById(R.id.ll_like);
            holder.percentView = (TextView) layout
                    .findViewById(R.id.tv_percent);
            layout.setTag(holder);
        } else {
            holder = (ViewHolder) layout.getTag();
        }

        if (position == mDataList.size() - 1) {// the last item
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }

        if (data != null && !StringUtils.isBlank(data.course_name)) {
            holder.content.setText(data.course_name);
        }
        // switch to Rehearsal or Mastered view
        holder.progress.setVisibility(isMastered ? View.GONE : View.VISIBLE);
        holder.masteredIcon.setVisibility(isMastered ? View.VISIBLE : View.GONE);
        if (!isMastered) {
            holder.masteredIcon.setVisibility(View.GONE);
            // Rehearsal Status
            if (data.status == ChunkBiz.REHEARSE_R1)
                holder.progress.setProgress(1, 4);
            else if (data.status == ChunkBiz.REHEARSE_R2)
                holder.progress.setProgress(2, 4);
            else if (data.status == ChunkBiz.REHARSE_R3)
                holder.progress.setProgress(3, 4);
            else if (data.status == ChunkBiz.REHEARSE_MARSTERED){
                holder.progress.setVisibility(View.GONE);
                holder.masteredIcon.setVisibility(View.VISIBLE);
            }

        }

        // font setting
        FontHelper
                .applyFont(mContext, holder.content, FontHelper.FONT_OpenSans);

        // switch to mic or like view
        boolean isRecorded = data.has_record.equalsIgnoreCase("true");
        holder.micView.setVisibility(isRecorded ? View.GONE : View.VISIBLE);
        holder.likeView.setVisibility(isRecorded ? View.VISIBLE : View.GONE);
        if (holder.likeView.getVisibility() == View.VISIBLE) {
            try {
                holder.percentView.setText((int) (Double.parseDouble(StringUtils.isBlank(data.like_percentage) ? "0" : data.like_percentage) * 100) + "%");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            // set listener for likeView
            holder.likeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chunkLoader.load(new ChunkLoader.Request(
                                    data.course_package_url,
                                    data.course_id,
                                    data.course_version),
                            new ChunkLoader.OnFinishListener() {
                                @Override
                                public void doOnFinish(boolean isDone) {
                                    Chunk chunk = chunkLoader.getChunk(data.course_id);
                                    if (chunk != null) {
                                        Intent intent = new Intent(mContext,
                                                UserRecordingActivity.class);
                                        intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
                                        intent.putExtra(AppConst.BundleKeys.BELLAID,
                                                AppConst.CurrUserInfo.UserId);
                                        mContext.startActivity(intent);

                                    } else {
                                        if (NetworkChecker.isConnected(mContext)) {
                                            Toast.makeText(
                                                    mContext,
                                                    JsonSerializeHelper.JsonLanguageDeserialize(
                                                            mContext, "record_msg_no_course"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    mContext,
                                                    JsonSerializeHelper.JsonLanguageDeserialize(
                                                            mContext, "error_check_network_available"),
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });

                }
            });
        } else {
            // set listener for micView
            holder.micView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    chunkLoader.load(new ChunkLoader.Request(
                                    data.course_package_url,
                                    data.course_id,
                                    data.course_version),
                            new ChunkLoader.OnFinishListener() {
                                @Override
                                public void doOnFinish(boolean isDone) {
                                    Chunk chunk = chunkLoader.getChunk(data.course_id);
                                    if (chunk != null) {
                                        mContext.startActivity(new Intent(mContext,
                                                RecordingActivity.class).putExtra(
                                                AppConst.BundleKeys.Is_Chunk_Learning, false)
                                                .putExtra(AppConst.BundleKeys.Chunk, chunk));

                                    } else {
                                        if (NetworkChecker.isConnected(mContext)) {
                                            Toast.makeText(
                                                    mContext,
                                                    JsonSerializeHelper.JsonLanguageDeserialize(
                                                            mContext, "record_msg_no_course"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    mContext,
                                                    JsonSerializeHelper.JsonLanguageDeserialize(
                                                            mContext, "error_check_network_available"),
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                }
            });
        }

    }
}
