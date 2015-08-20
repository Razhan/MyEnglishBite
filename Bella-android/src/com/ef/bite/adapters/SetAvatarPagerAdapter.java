package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SetAvatarPagerAdapter extends PagerAdapter {

	private List<Integer> mAvatarResIdList;
	private Activity mActivity;
	public SetAvatarPagerAdapter(Activity activity,  List<Integer> resIdList){
		mActivity = activity;
		mAvatarResIdList = resIdList;
	}
	
	@Override
	public int getCount() {
		return mAvatarResIdList==null?0:mAvatarResIdList.size();
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
		ImageView image = new ImageView(mActivity);
		image.setImageResource(mAvatarResIdList.get(position));
		container.addView(image);
		return image;
	}
}
