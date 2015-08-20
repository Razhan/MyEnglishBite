package com.ef.bite.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.PresentationConversation;
import com.ef.bite.ui.chunk.ChunkLearnDetailActivity;
import com.ef.bite.utils.AssetResourceHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.HighLightStringHelper;
import com.ef.bite.widget.AudioPlayerView;
import com.ef.bite.widget.GifImageView;
import com.ef.bite.widget.GifMovieView;

public class ChunkLearnListAdapter extends
		BaseListAdapter<PresentationConversation> {
	private boolean mHighlight;
	private Chunk mChunk;
	private AudioPlayerView mAudioView;
	private List<PresentationConversation> mdataList;
	boolean isSwipSuppported = false; // 是否支持来回的分段切换播放音频
	private boolean textview_source_status = true;
	private boolean isGifStatus;
	private boolean closeAllGif = true;
	private int mPosition;

    private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    private boolean isFirstTime = true;


    public ChunkLearnListAdapter(Context context,
			List<PresentationConversation> dataList, Chunk chunk) {
		super(context, R.layout.chunk_learn_dialogue_list_item, dataList);
		mChunk = chunk;
		this.mdataList = dataList;
	}

	public ChunkLearnListAdapter(Context context, AudioPlayerView audioView,
			List<PresentationConversation> dataList, Chunk chunk) {
		super(context, R.layout.chunk_learn_dialogue_list_item, dataList);
		mChunk = chunk;
		mAudioView = audioView;
		this.mdataList = dataList;
	}

	public void setHighLight(boolean isHighLight) {
		mHighlight = isHighLight;
		this.notifyDataSetChanged();
	}

    //new added
    public int findRecord(int position) {
        if(map.containsKey(position)) {
            int value = map.get(position) == 0 ? 0: 1;
            map.put(position, 1 - value);

        } else {
            map.put(position, 0);
        }

        return map.get(position);
    }

    public void doubleClick() {
        isFirstTime = false;
        this.notifyDataSetChanged();
    }


	public void setSwipSupported(boolean support) {
		isSwipSuppported = support;
	}

	public void StopGif(int position, boolean status) {
		this.isGifStatus = status;
		this.mPosition = position;
        this.closeAllGif = true;
        this.isFirstTime = true;

        this.notifyDataSetChanged();
	}

	private Bitmap getBitmapAvater(int position) {
		Bitmap bitmap = BitmapFactory.decodeFile(mdataList.get(position)
				.getCharacterAvater());
		return bitmap;
	}

	/**
	 * 更多显示隐藏
	 * 
	 * true:隐藏 false:显示
	 */
	public void setTranslationMorn(boolean isShow, int position) {
		this.textview_source_status = isShow;
		this.mPosition = position;
        this.closeAllGif = true;
		this.notifyDataSetChanged();
	}

	public void closeTranslationGif(boolean isShow) {
		this.closeAllGif = isShow;
		this.notifyDataSetChanged();
	}

	public boolean getTranslationMornStatus() {
		return this.textview_source_status;
	}

	private static class ViewHolder {
		TextView textview;
		TextView textview_source;
		ImageView speakerA;
		ImageView speakerB;
		ImageView left_talk;
		ImageView right_talk;
		ImageView moreView;
		GifMovieView voicegifplay;
	}

	@Override
	public void getView(View layout, final int position,
			final PresentationConversation data) {
		final ViewHolder holder;
		if (layout.getTag() == null) {
			holder = new ViewHolder();
			holder.textview = (TextView) layout
					.findViewById(R.id.dialogue_list_item);
			holder.textview_source = (TextView) layout
					.findViewById(R.id.textView_source);
			holder.speakerA = (ImageView) layout
					.findViewById(R.id.dialogue_list_item_speakera);
			holder.speakerB = (ImageView) layout
					.findViewById(R.id.dialogue_list_item_speakerb);
			holder.left_talk = (ImageView) layout.findViewById(R.id.left_talk);
			holder.right_talk = (ImageView) layout
					.findViewById(R.id.right_talk);
			holder.moreView = (ImageView) layout
					.findViewById(R.id.imageview_more);
			holder.voicegifplay = (GifMovieView) layout
					.findViewById(R.id.voicegifplay);
			layout.setTag(holder);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		//
		// if (mPosition == position && isGifStatus) {
		// // holder.voicegifplay.startGifFromAsset("gif/wechat_audio.gif",
		// // true);
		// holder.voicegifplay.setMovieResource(R.drawable.wechat_audio);
		// }
		//
		// if (mPosition == position && !isGifStatus) {
		//
		// holder.voicegifplay = null;
		// }
		FontHelper.applyFont(mContext, holder.textview, FontHelper.FONT_OpenSans);

        if (!isFirstTime) {
            if (mPosition == position) {
                holder.textview_source.setVisibility(View.GONE);
                holder.moreView.setVisibility(View.VISIBLE);
                holder.voicegifplay.setVisibility(View.INVISIBLE);
            }

            return;
        }


		if (!textview_source_status && mPosition == position) {
			holder.textview_source.setVisibility(View.VISIBLE);
			holder.moreView.setVisibility(View.GONE);
			holder.voicegifplay.setMovieResource(R.drawable.wechat_audio);
//			if (mPosition != position) {
//				holder.textview_source.setVisibility(View.GONE);
//				holder.voicegifplay
//						.setMovieResource(R.drawable.wechat_audio_off);
//			}
		} else {
//			holder.textview_source.setVisibility(View.GONE);
//			holder.moreView.setVisibility(View.VISIBLE);
			holder.voicegifplay.setMovieResource(R.drawable.wechat_audio_off);


		}

		if (!closeAllGif) {
			holder.voicegifplay.setVisibility(View.INVISIBLE);
		}

		// if (position % 2 == 0) {
		// speakerB.setVisibility(View.GONE);
		// if (data.getCharacterAvater().equals("male.png"))
		// speakerA.setImageResource(R.drawable.dialogue_speaker_a);
		// else if (data.getCharacterAvater().equals("female.png"))
		// speakerA.setImageResource(R.drawable.dialogue_speaker_b);
		// } else {
		// speakerA.setVisibility(View.GONE);
		// if (data.getCharacterAvater().equals("male.png"))
		// speakerB.setImageResource(R.drawable.dialogue_speaker_a);
		// else if (data.getCharacterAvater().equals("female.png"))
		// speakerB.setImageResource(R.drawable.dialogue_speaker_b);
		// }

		if (position % 2 == 0) {
			holder.speakerA.setVisibility(View.VISIBLE);
			holder.speakerB.setVisibility(View.GONE);
			holder.left_talk.setVisibility(View.VISIBLE);
			holder.right_talk.setVisibility(View.GONE);
			holder.speakerA.setImageBitmap(getBitmapAvater(position));
		} else {
			holder.speakerA.setVisibility(View.GONE);
			holder.speakerB.setVisibility(View.VISIBLE);
			holder.left_talk.setVisibility(View.GONE);
			holder.right_talk.setVisibility(View.VISIBLE);
			holder.speakerB.setImageBitmap(getBitmapAvater(position));
		}
		String sentence = data.getSentence();
		String content_src = data.getContent_src();
		if (mHighlight) {
			if (isSwipSuppported) {
				holder.textview.setText(HighLightStringHelper
						.getHighLightString(mContext, sentence));
				holder.textview_source.setText(HighLightStringHelper
						.getHighLightString(mContext, content_src));
			} else {
				holder.textview.setMovementMethod(LinkMovementMethod
						.getInstance());
				holder.textview.setText(HighLightStringHelper
						.getClickableHighLightString(mContext, sentence,
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (mChunk != null) {
											Intent intent = new Intent(
													mContext,
													ChunkLearnDetailActivity.class);
											intent.putExtra(
													AppConst.BundleKeys.Chunk,
													mChunk);
											intent.putExtra(
													AppConst.BundleKeys.Is_Chunk_Learning,
													true);
											intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											((FragmentActivity) mContext)
													.startActivityForResult(
															intent,
															AppConst.RequestCode.CHUNK_LEARNING);
											((FragmentActivity) mContext)
													.overridePendingTransition(
															R.anim.activity_in_from_right,
															R.anim.activity_out_to_left);
										}
									}
								}));
				holder.textview.setDuplicateParentStateEnabled(true);
			}
		} else {
			String show = sentence.replace("<h>", "");
			show = show.replace("</h>", "");
			holder.textview.setText(show);

			String show_src = content_src.replace("<h>", "");
			show_src = show_src.replace("</h>", "");
			holder.textview_source.setText(show_src);
		}
		FontHelper.applyFont(mContext, layout, FontHelper.FONT_OpenSans);
	}
}
