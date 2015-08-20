package com.ef.bite.adapters;

import java.util.List;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.dataacces.mode.VoiceReviewrs;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.widget.RoundedImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ReviewHorizontalListAdapter extends BaseAdapter {

	private List<VoiceReviewrs> voiceReviewrs;
	private LayoutInflater mInflater;
	private Context context;

	public ReviewHorizontalListAdapter(Context context,
			List<VoiceReviewrs> voiceReviewrs) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.voiceReviewrs = voiceReviewrs;
		this.mInflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
		private RoundedImageView avatarView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return voiceReviewrs == null ? 0 : voiceReviewrs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return voiceReviewrs == null ? null : voiceReviewrs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.userrecording_reviewlist_item, null);
			viewHolder.avatarView = (RoundedImageView) convertView
					.findViewById(R.id.userreview_avatar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// AvatarHelper.LoadAvatar(viewHolder.avatarView,
		// AppConst.CurrUserInfo.UserId, voiceReviewrs.get(position)
		// .getAvatar_url());

		Picasso.with(context).load(voiceReviewrs.get(position).getAvatar_url())
				.into(viewHolder.avatarView);

		return convertView;
	}

}
