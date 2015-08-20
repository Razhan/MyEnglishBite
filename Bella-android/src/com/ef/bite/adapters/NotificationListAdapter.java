package com.ef.bite.adapters;

import java.util.Date;

import com.ef.bite.R;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.business.task.AddFriendTask;
import com.ef.bite.business.task.CheckIsMyFriendTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpIsMyFriend;
import com.ef.bite.dataacces.mode.httpMode.HttpNotification;
import com.ef.bite.dataacces.mode.httpMode.HttpNotification.Content;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.HighLightStringHelper;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.widget.RoundedImageView;

public class NotificationListAdapter extends
		BaseListAdapter<HttpNotification.NotificationData> {
	private HttpNotification.Content content;
	private Context mcontext;

	public NotificationListAdapter(Context context,
			List<HttpNotification.NotificationData> dataList) {
		super(context, R.layout.friend_notification_list_item, dataList);
		this.mcontext = context;
	}

	@Override
	public void getView(View layout, int position,
			HttpNotification.NotificationData data) {
		if (data != null) {
			RoundedImageView avatar = (RoundedImageView) layout
					.findViewById(R.id.friend_notification_list_item_avatar);
			TextView name = (TextView) layout
					.findViewById(R.id.friend_notification_list_item_name);
			TextView time = (TextView) layout
					.findViewById(R.id.friend_notification_list_item_time);
			ImageView add = (ImageView) layout
					.findViewById(R.id.friend_notification_list_item_add);
			ImageView line = (ImageView) layout
					.findViewById(R.id.friend_list_item_line);

			if (data.notification_type
					.equals(HttpNotification.Notification_type_AddFriend)) {
				if (!data.content.isEmpty()) {
					String notify = data.content;
					name.setText(HighLightStringHelper.getHighLightString(
							mContext, notify));
				} else {
					name.setText("");
				}
			} else if (data.notification_type
					.equals(HttpNotification.Notification_type_ReView)) {
				if (data.content != null) {
					String notify = data.content;
					name.setText(HighLightStringHelper.getHighLightString(
							mContext, notify));
				} else {
					name.setText("");
				}
			} else {
				content = (Content) JsonSerializeHelper.JsonDeserialize(
						data.content, HttpNotification.Content.class);
				if (content != null) {
					name.setText(HighLightStringHelper.getHighLightString(
							mContext, content.text));
				} else {
					name.setText("");
				}
			}

			// if (data.notification_type
			// .equals(HttpNotification.Notification_type_ReView)) {
			// if (data.content != null) {
			// String notify = data.content;
			// name.setText(HighLightStringHelper.getHighLightString(
			// mContext, notify));
			// } else {
			// name.setText("");
			// }
			// } else {
			// content = (Content) JsonSerializeHelper.JsonDeserialize(
			// data.content, HttpNotification.Content.class);
			// if (content != null) {
			// name.setText(HighLightStringHelper.getHighLightString(
			// mContext, content.text));
			// } else {
			// name.setText("");
			// }
			// }

			time.setText(getTimeAgo(data.created_at));
			// ������涓�琛�涓���剧ず涓����绾�
			if (position >= this.getCount() - 1)
				line.setVisibility(View.GONE);
			else
				line.setVisibility(View.VISIBLE);
			AvatarHelper.LoadAvatar(avatar, data.from_bella_id, data.avatar);
			if (data.notification_type
					.equals(HttpNotification.Notification_type_AddFriend)) {
				checkIsFriend(add, data.from_bella_id);
			}
			if (data.notification_type
					.equals(HttpNotification.Notification_type_ReView)) {
				add.setImageResource(R.drawable.recordingnotification);
				add.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});

			} else {
				add.setImageResource(R.drawable.profile_caution);
				add.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(content.url));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mcontext.startActivity(intent);
					}
				});
			}

		}
		layout.setTag(data);
	}

	/**
	 * ��剧ず娣诲�������堕��:
	 * 
	 * @return
	 */
	private String getTimeAgo(Date addDate) {
		if (addDate == null)
			return "";
		long addedTime = addDate.getTime();
		final long ONE_MINUTE = 60 * 1000;
		final long ONE_HOUR = 60 * ONE_MINUTE;
		final long ONE_DAY = 24 * ONE_HOUR;
		final long ONE_MONTH = 30 * ONE_DAY;
		final long ONE_YEAR = 12 * ONE_MONTH;
		long currentTime = System.currentTimeMillis();
		long diffTime = currentTime - addedTime;
		if (diffTime <= 0)
			return String.format(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "friend_notification_minutes_ago"), 0);
		// return 0
		// + " "
		// + mContext
		// .getString(R.string.friend_notification_minutes_ago);
		else if (diffTime > 0 && diffTime < ONE_HOUR) { // ������
			int minutes = (int) (diffTime / ONE_MINUTE);
			return String.format(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "friend_notification_minutes_ago"), minutes);
			// return minutes
			// + " "
			// + mContext
			// .getString(R.string.friend_notification_minutes_ago);
		} else if (diffTime >= ONE_HOUR && diffTime < ONE_DAY) { // 灏����
			int hours = (int) (diffTime / ONE_HOUR);
			return String.format(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "friend_notification_hours_ago"), hours);
			// return hours
			// + " "
			// + mContext
			// .getString(R.string.friend_notification_hours_ago);
		} else if (diffTime >= ONE_DAY && diffTime < ONE_MONTH) {
			int days = (int) (diffTime / ONE_DAY);
			if (days > 1) {
				return String.format(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"friend_notification_days_ago"), days);
				// return days
				// + " "
				// + mContext
				// .getString(R.string.friend_notification_days_ago);
			} else {
				return String.format(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"friend_notification_day_ago"), days);
				// return days
				// + " "
				// + mContext
				// .getString(R.string.friend_notification_day_ago);
			}

		} else if (diffTime >= ONE_MONTH && diffTime < ONE_YEAR) {
			int months = (int) (diffTime / ONE_MONTH);
			return String.format(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "friend_notification_months_ago"), months);
			// return months
			// + " "
			// + mContext
			// .getString(R.string.friend_notification_months_ago);
		} else {
			int years = (int) (diffTime / ONE_YEAR);
			return String.format(JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "friend_notification_years_ago"), years);
			// return years
			// + " "
			// + mContext
			// .getString(R.string.friend_notification_years_ago);
		}
	}

	/**
	 * 妫���ユ��������濂藉��
	 * 
	 * @param addImg
	 * @param uid
	 * @param friend_id
	 */
	public void checkIsFriend(final ImageView addImg, final String friend_id) {
		CheckIsMyFriendTask checkTask = new CheckIsMyFriendTask(mContext,
				new PostExecuting<HttpIsMyFriend>() {
					@Override
					public void executing(HttpIsMyFriend result) {
						if (result == null || result.status == null) {
							return;
						}
						if (!result.status.equals("0")) {
							Toast.makeText(mContext, result.message,
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (result.isMyFriend) {
							addImg.setImageResource(R.drawable.friend_added);
							addImg.setClickable(false);
						} else {
							addImg.setImageResource(R.drawable.friend_to_add);
							addImg.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									addFriend(addImg, friend_id);
									MobclickTracking.OmnitureTrack
											.ActionInbox();
								}
							});
						}
					}
				});
		checkTask.execute(new String[] { AppConst.CurrUserInfo.UserId,
				friend_id });
	}

	/**
	 * 娣诲�������ㄦ�蜂负濂藉��
	 * 
	 * @param addImg
	 * @param friend_id
	 */
	public void addFriend(final ImageView addImg, final String friend_id) {
		AddFriendTask addTask = new AddFriendTask(mContext,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
						if (result == null || result.status == null) {
							Toast.makeText(
									mContext,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mContext,
													"add_frend_failed"),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!result.status.equals("0")) {
							Toast.makeText(
									mContext,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mContext,
													"add_frend_failed")
											+ " " + result.message,
									Toast.LENGTH_SHORT).show();
							return;
						}
						Toast.makeText(
								mContext,
								JsonSerializeHelper.JsonLanguageDeserialize(
										mContext, "add_friend_success"),
								Toast.LENGTH_SHORT).show();
						addImg.setImageResource(R.drawable.friend_added);
						addImg.setClickable(false);
					}
				});
		addTask.execute(new String[] { friend_id });
	}

}
