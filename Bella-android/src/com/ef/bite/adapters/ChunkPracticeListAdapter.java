package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.dataacces.mode.MulityChoiceQuestions;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.SoundEffectUtils;

public class ChunkPracticeListAdapter extends BaseAdapter {

	private OnCorrectListener mCorrectListener;
	private OnWrongListener mWrongListener;
	private Activity mActivity;
	private MulityChoiceQuestions mMultiChoice;
	private List<MulityChoiceAnswers> mAnwserList;
	private LayoutInflater mInflater;
	private int mSelectedIndex = -1;
	private View mSelectedView = null;
	private boolean clickable = true;
	private boolean enableChoice = false;
	private boolean falselected = false;
	private MulityChoiceAnswers curSelectedAnwser;
	
	
    final Handler handler = new Handler(); 
    
    Runnable runcorrect = new Runnable(){
        @Override
        public void run() {
            if(mCorrectListener!=null)
                mCorrectListener.onCorrect(curSelectedAnwser);
        }
    };
    Runnable runwrong = new Runnable(){
        @Override
        public void run() {
            if(mWrongListener!=null)
                mWrongListener.onWrong(curSelectedAnwser);
        }
    };

	
	public ChunkPracticeListAdapter(Activity activity, MulityChoiceQuestions multichoice){
		mActivity = activity;
		mMultiChoice = multichoice;
		if(mMultiChoice!=null )
			mAnwserList = mMultiChoice.getRandomAnswers();
		mInflater = activity.getLayoutInflater();
	}
	
	@Override
	public int getCount() {
		return mAnwserList==null? 0:mAnwserList.size();
	}

	@Override
	public Object getItem(int location) {
		return mAnwserList==null? null:mAnwserList.get(location);
	}

	@Override
	public long getItemId(int location) {
		return location;
	}

	@Override
	public View getView(int location, View currentView, ViewGroup parent) {
		if(currentView == null)
			currentView = mInflater.inflate(R.layout.chunk_practice_choice_list_item, null, false);
		final TextView content = (TextView)currentView.findViewById(R.id.chunk_practice_choicelist_content);
		final Button indexButton = (Button)currentView.findViewById(R.id.chunk_practice_choicelist_index);
		final ImageView line = (ImageView)currentView.findViewById(R.id.chunk_practice_choicelist_line);
		final int position = location;
		final MulityChoiceAnswers item = mAnwserList.get(location);
		if(item!=null){
			content.setText(item.getAnswer());
			indexButton.setText(Integer.toString(position + 1));
			if(location == mAnwserList.size()-1)
				line.setVisibility(View.INVISIBLE);
			else
				line.setVisibility(View.VISIBLE);
			currentView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if(falselected || !clickable || !enableChoice)
						return;
					clickable = false;
					clearSelection();
					mSelectedIndex = position;
					mSelectedView = view;
					ImageView resultImage = (ImageView)view.findViewById(R.id.chunk_practice_choicelist_result);
					indexButton.setVisibility(View.INVISIBLE);
					resultImage.setVisibility(View.VISIBLE);
					curSelectedAnwser = item;
					if(item.getIsCorrect() == 1){  // Correct
						resultImage.setImageResource(R.drawable.chunk_choice_correct);
						content.setTextColor(mActivity.getResources().getColor(R.color.bella_color_green_light));
						handler.postDelayed(runcorrect, 300);
						// Sound play effect
						SoundEffectUtils soundEffect = new SoundEffectUtils(mActivity);
						soundEffect.play(SoundEffectUtils.RIGHT_ANWSER);
					}
					else
					{
						resultImage.setImageResource(R.drawable.chunk_choice_wrong);
						content.setTextColor(mActivity.getResources().getColor(R.color.bella_color_red));
                        handler.postDelayed(runwrong, 300);
                        // Sound play effect
                        SoundEffectUtils soundEffect = new SoundEffectUtils(mActivity);
						soundEffect.play(SoundEffectUtils.WRONG_ANWSER);
					}
				}
			});
		
		}
		// 字体设置
		FontHelper.applyFont(mActivity, content, FontHelper.FONT_OpenSans);
		return currentView;
	}
	
	/**
	 * 获得已选中的item
	 * @return
	 */
	public MulityChoiceAnswers getSelectedAnswer(){
		return curSelectedAnwser;
	}
	
	/**
	 * 清理选中的项
	 */
	public void clearSelection(){
		if(mSelectedView!=null){
			TextView content = (TextView)mSelectedView.findViewById(R.id.chunk_practice_choicelist_content);
			Button indexButton = (Button)mSelectedView.findViewById(R.id.chunk_practice_choicelist_index);
			ImageView resultImage = (ImageView)mSelectedView.findViewById(R.id.chunk_practice_choicelist_result);
			content.setTextColor(mActivity.getResources().getColor(R.color.bella_color_black_dark));
			indexButton.setVisibility(View.VISIBLE);
			resultImage.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 设置正确选择的监听事件
	 * @param listener
	 */
	public void setOnCorrectListener(OnCorrectListener listener){
		mCorrectListener = listener;
	}
	
	public void setOnWrongListener(OnWrongListener listener){
		mWrongListener = listener;
	}
	
	/**
	 * 设置是否允许选择
	 * @param enable
	 */
	public void enableChoice(boolean enable){
		if(enable && !enableChoice){
			clearSelection();
			this.notifyDataSetChanged();
		}
		enableChoice = enable;
	}
	
	/**
	 *	选择了正确的选项后触发的事件
	 */
	public interface OnCorrectListener{
		public void onCorrect(MulityChoiceAnswers selectedAnwser);
	}
	
	/**
	 * 选择错误的选项后触发的事件
	 *
	 */
	public interface OnWrongListener{
		public void onWrong(MulityChoiceAnswers selectedAnwser);
	}
}
