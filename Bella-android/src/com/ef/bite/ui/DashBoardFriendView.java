package com.ef.bite.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import com.ef.bite.R;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.ui.user.AddFriendActivity;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.widget.RoundedImageView;
import com.ef.bite.widget.UserLevelView;

@SuppressLint("InflateParams")
public class DashBoardFriendView {
	View layout;
	Context context;
	LayoutInflater inflator;
	View.OnClickListener listener;
	boolean isSelected = false;
	HolderView holder = null;
	HttpGetFriendData profileModel;
	/** 类型：0-朋友；1-邀请 **/
	int dashboardType = 0;

	public DashBoardFriendView(Context context) {
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	public void setOnClickListener(View.OnClickListener listener) {
		this.listener = listener;
		if (layout != null && holder != null) {
			layout.setOnClickListener(this.listener);
			holder.mLevel.setOnClickListener(this.listener);
		}
	}

	public boolean isSelected() {
		return isSelected;
	}

	public HttpGetFriendData getProfile() {
		return this.profileModel;
	}

	/**
	 * 设置是否选中的状态
	 * 
	 * @param selected
	 */
	@SuppressWarnings("deprecation")
	public void setSelected(boolean selected) {
		if (holder == null)
			return;
		if (!selected) {
			if (android.os.Build.VERSION.SDK_INT < 16)
				holder.mAvatar.setAlpha(1);
			else
				holder.mAvatar.setAlpha(1f);
			holder.mLevel.setVisibility(View.VISIBLE);
			isSelected = false;
		} else {
			if (android.os.Build.VERSION.SDK_INT < 16) // android 版本判断
				holder.mAvatar.setAlpha(100);
			else
				holder.mAvatar.setAlpha(0.5f);
			holder.mLevel.setVisibility(View.GONE);
			isSelected = true;
		}
	}

	/** Get friend view **/
	public View getView(HttpGetFriendData profileModel) {
		this.profileModel = profileModel;
		if (layout != null)
			return layout;
		layout = inflator.inflate(R.layout.home_screen_friend_list_item, null,
				false);
		holder = new HolderView();
		holder.mAvatar = (RoundedImageView) layout
				.findViewById(R.id.friend_list_item_avatar);
		holder.mAvatarBG = (RoundedImageView) layout
				.findViewById(R.id.friend_list_item_avatar_bg);
		holder.mNo1Ribbon = (ImageView) layout
				.findViewById(R.id.friend_list_item_no1_ribbon);
		holder.mLevel = (UserLevelView) layout
				.findViewById(R.id.friend_list_item_level);
		holder.mIndex = (TextView) layout
				.findViewById(R.id.friend_list_item_index);
		holder.mName = (TextView) layout
				.findViewById(R.id.friend_list_item_name);
		holder.mIndex.setText("#" + Integer.toString(profileModel.rank));
		holder.mLevel.initialize(profileModel.score);
		holder.mName.setText(profileModel.alias != null ? profileModel.alias
				: "");
		AvatarHelper.LoadAvatar(holder.mAvatar, profileModel.bella_id,
				profileModel.avatar);
		if (profileModel.rank == 1) { // 第一名
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mAvatar
					.getLayoutParams();
			params.setMargins(4, 4, 4, 4);
			holder.mAvatar.setLayoutParams(params);
			holder.mNo1Ribbon.setVisibility(View.VISIBLE);
			holder.mAvatarBG.setVisibility(View.VISIBLE);
		} else { // 非第一名
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mAvatar
					.getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			holder.mAvatar.setLayoutParams(params);
			holder.mNo1Ribbon.setVisibility(View.INVISIBLE);
			holder.mAvatarBG.setVisibility(View.INVISIBLE);
		}
		// Apply the font
		FontHelper.applyFont(context, layout, FontHelper.FONT_Museo300);
		return layout;
	}

	/** Get Invite view **/
	public View getInviteView() {
		if (layout != null)
			return layout;
		layout = inflator.inflate(R.layout.home_screen_friend_invite_item,
				null, false);
		ImageButton inviteButton = (ImageButton) layout
				.findViewById(R.id.home_screen_friend_invint);
		inviteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddFriendActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				// tracking
//				TraceHelper.tracingAction(context, TraceHelper.PAGE_MAIN,
//						TraceHelper.ACTION_CLICK, null,
//						TraceHelper.TARGET_INVITE_FRIEND);
			}
		});
		return layout;
	}

	class HolderView {
		RoundedImageView mAvatar; // 好友头像
		RoundedImageView mAvatarBG; // 头像背景
		ImageView mNo1Ribbon; // 第一名的彩带
		UserLevelView mLevel; // 好友的level
		TextView mIndex; // 排名
		TextView mName;
	}


}
