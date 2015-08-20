package com.ef.bite.business.action;

import android.content.Context;
import android.content.Intent;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.model.ProfileModel;
import com.ef.bite.ui.user.ProfileActivity;

/**
 * 打开UserProfile的Activity
 * @author Admin
 *
 */
public class UserProfileOpenAction extends BaseOpenAction<ProfileModel>{

	@Override
	public void open(Context context, ProfileModel data) {
		Intent intent = new Intent(context,ProfileActivity.class);
		intent.putExtra(AppConst.BundleKeys.Person_Profile, data.toJson());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	
	public void open(Context context, HttpGetFriendData user, boolean isFriend){
		ProfileModel profile = new ProfileModel(user.bella_id, user.alias, user.avatar, user.score, 0, user.friend_count, isFriend); 
		open(context, profile);
	}

}
