package com.ef.bite.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.DashboardFriendsSharedStorage;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.utils.JsonSerializeHelper;
import com.google.gson.reflect.TypeToken;

/**
 * 本地的Dashboard朋友栏的业务处理
 * @author Allen.Zhu
 *
 */
public class LocalDashboardBLL {
	
	Context mContext;
	public LocalDashboardBLL(Context context){
		mContext = context;
	}

	/**
	 * 从缓存获得dashboard的朋友列表
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HttpGetFriendData> getCachedDashboardFriends(String uid){
		try{
			DashboardFriendsSharedStorage cache = new DashboardFriendsSharedStorage(mContext,uid);
			String json = cache.get();
			if(json==null || json.isEmpty())
				return null;
			return (List<HttpGetFriendData>)JsonSerializeHelper.JsonDeserialize(json,new TypeToken<List<HttpGetFriendData>>(){}.getType() );
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 保存dashboard朋友列表到缓存中
	 * @param friends
	 * @param uid
	 */
	public void cacheDashboardFriends(List<HttpGetFriendData> friends, String uid){
		try{
			String json = JsonSerializeHelper.JsonSerializer(friends);
			if(json!=null){
				DashboardFriendsSharedStorage cache = new DashboardFriendsSharedStorage(mContext,uid);
				cache.put(json);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 重新把本地的score和dashboard列表一起计算：
	 * 1. 在列表中更新当前用户的积分
	 * 2. 根据分数重新计算dashboard排名:
	 * 		a. 如果当前用户的排名比第一名个大，则自己是第一
	 * @param dashboardList
	 * @param uid
	 * @param latestScore
	 * @return
	 */
	public List<HttpGetFriendData> calDashboardRankWithLatestScore(List<HttpGetFriendData> dashboardList, String uid, int latestScore){
		List<HttpGetFriendData> newDashboardList = new ArrayList<HttpGetFriendData>();
		if(dashboardList==null || dashboardList.size() <= 0 ){
			HttpGetFriendData myData = new HttpGetFriendData();
			myData.rank = 1;
			myData.alias = AppConst.CurrUserInfo.Alias;
			myData.avatar = AppConst.CurrUserInfo.Avatar;
			myData.bella_id = AppConst.CurrUserInfo.UserId;
			myData.score = latestScore;
			myData.friend_count = 0;
			newDashboardList.add(myData);
			return newDashboardList;
		}
		int originRank = 0;		// 当前用户原本的排名	
		HttpGetFriendData myData = null;
		// 更新当前用户的最新积分
		for(int index=0;index<dashboardList.size();index++){
			HttpGetFriendData curFriend = dashboardList.get(index);
			if(curFriend==null)
				return dashboardList;
			if(curFriend.bella_id.equals(uid)){ 	// 发现当前用户
				if(curFriend.score > latestScore)	// 如果网络积分比本地积分还要大，则无需重新计算,直接退出
 					return dashboardList;
				curFriend.score = latestScore;
				myData = curFriend;
				break;
			}
			else
				originRank ++ ;
		}
		// 本来就是第一位，无需重新计算排名
		if(originRank == 0) 
			return dashboardList;
		// 如果当前用户不存在，只返回自己
		if(myData == null){
			myData = new HttpGetFriendData();
			myData.rank = 1;
			myData.alias = AppConst.CurrUserInfo.Alias;
			myData.avatar = AppConst.CurrUserInfo.Avatar;
			myData.bella_id = AppConst.CurrUserInfo.UserId;
			myData.score = latestScore;
			myData.friend_count = 0;
			newDashboardList.add(myData);
			return newDashboardList;
		}
		
		
		boolean exchanged = false;
		// 重新排序，找所有比自己排名靠前用户；
		// 一直到第一个积分比自己小的用户时，排名变成和改用户一样，而该用户的排名降一，；后面的所有的排名都降一
		for(int index=0; index<originRank;index++){
			HttpGetFriendData curFriend = dashboardList.get(index);
			if(curFriend==null)
				continue;
			if(!exchanged && curFriend.score < latestScore){
				myData.rank = curFriend.rank;
				newDashboardList.add(myData);
				curFriend.rank ++ ;
				newDashboardList.add(curFriend);
				exchanged = true;
			}else if(exchanged){
				curFriend.rank ++;
				newDashboardList.add(curFriend);
			}else
				newDashboardList.add(curFriend);
		}
		// 如果没有交换，把自己添加上去
		if(!exchanged)
			newDashboardList.add(myData);
		
		// 排在自己后面的人是不变的
		for(int index=originRank+1; index< dashboardList.size(); index++ ){
			HttpGetFriendData curFriend = dashboardList.get(index);
			if(curFriend!=null)
				newDashboardList.add(curFriend);
		}
		return newDashboardList;
	}
}
