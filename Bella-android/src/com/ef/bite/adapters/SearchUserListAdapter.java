package com.ef.bite.adapters;

import java.util.List;
import com.ef.bite.R;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.HighLightStringHelper;

public class SearchUserListAdapter extends BaseListAdapter<HttpGetFriendData> {

	String mKey;						// 高亮搜索关键字
	
	public SearchUserListAdapter(Activity activity, String searchKey ,List<HttpGetFriendData> userList ){
		super(activity, R.layout.add_friend_search_user_list_item, userList);
		mKey = searchKey;
	}


	@Override
	public void getView(View layout, int position, HttpGetFriendData data) {
		ImageView avatar = (ImageView)layout.findViewById(R.id.search_friend_list_item_avatar);
		TextView name = (TextView)layout.findViewById(R.id.serach_friend_list_item_name);
		AvatarHelper.LoadAvatar(avatar, data.bella_id, data.avatar);
		ImageView line = (ImageView)layout.findViewById(R.id.search_friend_list_item_line);
		if(data.alias!=null && !data.alias.isEmpty())
			name.setText(HighLightStringHelper.getHighLightString(mContext, data.alias, mKey));
		else
			name.setText("");
		if(position >= this.getCount() - 1)
			line.setVisibility(View.INVISIBLE);
		else
			line.setVisibility(View.VISIBLE);
	}

}
