package com.ef.bite.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.widget.RoundedImageView;

public class FriendListAdapter extends BaseListAdapter<HttpGetFriendData> {
	
	public FriendListAdapter(Activity context, List<HttpGetFriendData> dataList) {
		super(context, R.layout.friend_list_item, dataList);
	}

	@Override
	public void getView(View layout, int position, HttpGetFriendData data) {
		RoundedImageView  avatar = (RoundedImageView )layout.findViewById(R.id.friend_list_item_avatar);
		TextView name = (TextView)layout.findViewById(R.id.friend_list_item_name);
		if(data!=null){
			AvatarHelper.LoadAvatar(avatar, data.bella_id, data.avatar);
			name.setText(data.alias!=null?data.alias:"");
		}
		// font setting
		FontHelper.applyFont(mContext, name, FontHelper.FONT_Museo300);
	}
}
