package com.ef.bite.adapters;

import java.util.List;
import com.ef.bite.R;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.widget.RoundedImageView;
import com.ef.bite.widget.UserLevelView;

public class LeaderboardListAdapter extends BaseListAdapter<HttpGetFriendData> {

	public LeaderboardListAdapter(Activity context,
			List<HttpGetFriendData> dataList) {
		super(context, R.layout.leaderboard_list_item, dataList);
	}

	class ViewHolder {
		TextView mIndex;
		RoundedImageView mAvatar;
		TextView mName;
		UserLevelView mLevel;
	}

	@Override
	public void getView(View layout, int position, HttpGetFriendData data) {
		ViewHolder holder = null;
		if (layout.getTag() == null) {
			holder = new ViewHolder();
			holder.mIndex = (TextView) layout
					.findViewById(R.id.leaderboard_list_item_index);
			holder.mAvatar = (RoundedImageView) layout
					.findViewById(R.id.leaderboard_list_item_avatar);
			holder.mName = (TextView) layout
					.findViewById(R.id.leaderboard_list_item_name);
			holder.mLevel = (UserLevelView) layout
					.findViewById(R.id.leaderboard_list_item_level);
			layout.setTag(holder);
		} else
			holder = (ViewHolder) layout.getTag();
		holder.mIndex.setText(Integer.toString(position + 1)); // 从第二开始
		holder.mName.setText(data.alias == null ? "" : data.alias);
		if (data.bella_id.equals(AppConst.CurrUserInfo.UserId)) {// 用户是本人，名字加粗
			holder.mName.getPaint().setFakeBoldText(true);
		} else
			holder.mName.getPaint().setFakeBoldText(false);
		holder.mAvatar.setImageResource(R.drawable.default_avatar);
		AvatarHelper.LoadAvatar(holder.mAvatar, data.bella_id, data.avatar);
		holder.mLevel.initialize(data.score);
	}

}
