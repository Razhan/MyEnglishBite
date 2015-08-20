package com.ef.bite.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ef.bite.utils.FontHelper;

public abstract class BaseListAdapter<T> extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<T> mDataList;
	protected View mLayout;
	private int mLayoutResId;
	
	public BaseListAdapter(Context context, int layoutResId ,List<T> dataList){
		mContext = context;
		mDataList = dataList;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayoutResId = layoutResId;
	}

	@Override
	public int getCount() {
		return mDataList==null?0:mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList==null?null:mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(mLayoutResId, null);
		}
		final T data = mDataList.get(position);
		getView(convertView, position, data);
		// font setting
		FontHelper.applyFont(mContext, convertView, FontHelper.FONT_Museo300);
		return convertView;
	}
	
	
	public abstract void getView(View layout,final int position, final T data);

}
