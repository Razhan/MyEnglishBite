package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.ScoreLevelHelper;
import com.ef.bite.widget.RoundedImageView;

public class DashboardFriendsListAdapter extends BaseAdapter {

	Activity mActivity;
	List<HttpGetFriendData> mFriendList;
	LayoutInflater mInflater;
	
	public DashboardFriendsListAdapter(Activity activity, List<HttpGetFriendData> friendList){
		mActivity = activity;
		mFriendList = friendList;
		mInflater = mActivity.getLayoutInflater();
	}
	
	@Override
	public int getCount() {
		return mFriendList==null?1:mFriendList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return mFriendList==null?null: position == mFriendList.size()?null: mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == mFriendList.size()){
			return getInviteView();
		}else{
			HolderView holder = null;
			View layout = mInflater.inflate(R.layout.home_screen_friend_list_item, null, false);
				holder = new HolderView();
				holder.mAvatar = (RoundedImageView)layout.findViewById(R.id.friend_list_item_avatar);
				holder.mNo1Ribbon =(ImageView)layout.findViewById(R.id.friend_list_item_no1_ribbon);
				holder.mLevel = (Button)layout.findViewById(R.id.friend_list_item_level);
				holder.mIndex = (TextView)layout.findViewById(R.id.friend_list_item_index);
				HttpGetFriendData profileModel = mFriendList.get(position);
				holder.mIndex.setText(Integer.toString(profileModel.rank));
				holder.mLevel.setText("Lv.\n"+ScoreLevelHelper.getDisplayLevel(profileModel.score));
				AvatarHelper.LoadAvatar(holder.mAvatar, profileModel.bella_id, profileModel.avatar);
				if(position==0){ // 第一名
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.mAvatar.getLayoutParams();
					params.setMargins(4, 4, 4, 4);
					holder.mAvatar.setLayoutParams(params);
					holder.mNo1Ribbon.setVisibility(View.VISIBLE);
				}else{	// 非第一名
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.mAvatar.getLayoutParams();
					params.setMargins(0,0,0,0);
					holder.mAvatar.setLayoutParams(params);
					holder.mNo1Ribbon.setVisibility(View.GONE);
				}
				if(profileModel.bella_id.equals(AppConst.CurrUserInfo.UserId)){ // 如果是本人
					holder.mCorner.setVisibility(View.VISIBLE);
					if(android.os.Build.VERSION.SDK_INT < 16)  // android 版本判断
						holder.mAvatar.setAlpha(100);
					else
						holder.mAvatar.setAlpha(0.5f);
					holder.mLevel.setVisibility(View.GONE);
				}else{		// 非本人
					holder.mCorner.setVisibility(View.GONE);
					if(android.os.Build.VERSION.SDK_INT < 16)
						holder.mAvatar.setAlpha(1);
					else
						holder.mAvatar.setAlpha(1f);
					holder.mLevel.setVisibility(View.VISIBLE);
				}
				FontHelper.applyFont(mActivity,  layout , FontHelper.FONT_Museo300);
			return layout;
		}
	}
	
	View getInviteView(){
		View view = mInflater.inflate(R.layout.home_screen_friend_invite_item, null, false);
		ImageButton inviteButton = (ImageButton)view.findViewById(R.id.home_screen_friend_invint);
		inviteButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "Invite Friend!", Toast.LENGTH_SHORT).show();	
			}
		});
		FontHelper.applyFont(mActivity,  view , FontHelper.FONT_Museo300);
		return view;
	}
	
	class HolderView{
		ImageView mCorner;		// 向下的尖角
		RoundedImageView mAvatar;	// 好友头像
		ImageView mNo1Ribbon;	// 第一名的彩带
		Button mLevel;			// 好友的level
		TextView mIndex;		// 排名
	}

}
