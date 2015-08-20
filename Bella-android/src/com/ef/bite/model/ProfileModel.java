package com.ef.bite.model;

import com.ef.bite.utils.JsonSerializeHelper;

public class ProfileModel extends BaseJsonModel {
	
	public ProfileModel(){}
	
	/**
	 * 
	 * @param uid
	 * @param name
	 * @param avatar
	 * @param score
	 * @param chunkNum
	 * @param friendsNum
	 * @param isFriend
	 */
	public ProfileModel(String uid, String name, String avatar, int score, int chunkNum ,int friendsNum, boolean isFriend){
		this.UID = uid;
		this.Alias = name;
		this.AvatarPath = avatar;
		this.Score = score;
		this.FriendsNum = friendsNum;
		this.ChunkNum = chunkNum;
		this.IsFriend = isFriend; 
	}

	/**
	 * 用户ID
	 */
	public String UID;
	
	/**
	 * 用户名
	 */
	public String Alias;
	
	/**
	 * 头像
	 */
	public String AvatarPath;
	
	/**
	 * 积分
	 */
	public int Score;
	
	/**
	 * 掌握的chunk数量
	 */
	public int ChunkNum;
	
	/**
	 * 朋友数
	 */
	public int FriendsNum;
	
	/**
	 * 是否与当前用户属于朋友关系
	 */
	public boolean IsFriend;
	
	/**
	 * 是否从Homescreen打开的profile
	 */
	public boolean IsOpenFromHomeScreen;

	@Override
	public void parse(String json) {
		try{
			ProfileModel model = (ProfileModel)JsonSerializeHelper.JsonDeserialize(json, ProfileModel.class);
			this.UID = model.UID;
			this.Alias = model.Alias;
			this.AvatarPath = model.AvatarPath;
			this.Score = model.Score;
			this.ChunkNum = model.ChunkNum;
			this.FriendsNum = model.FriendsNum;
			this.IsFriend = model.IsFriend;
			this.IsOpenFromHomeScreen = model.IsOpenFromHomeScreen;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public String toJson() {
		return JsonSerializeHelper.JsonSerializer(this);
	}
}
