package com.ef.bite.adapters;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class WalkthroughPagerAdapter extends PagerAdapter {

	List<Integer> mPageList;
	Context mContext;
	LayoutInflater mInflater;
	
	public WalkthroughPagerAdapter(Context context, List<Integer> pageList){
		mPageList = pageList;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return mPageList == null? 0 : mPageList.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    ((ViewPager) container).removeView((ImageView)object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView image = new ImageView(mContext);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		int imageId = mPageList.get(position);
		image.setImageResource(imageId);
		image.setScaleType(ScaleType.FIT_XY);
		container.addView(image, params);
		return image;
	}
	

}
