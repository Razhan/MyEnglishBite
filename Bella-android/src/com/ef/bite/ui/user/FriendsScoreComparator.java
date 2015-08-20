package com.ef.bite.ui.user;

import java.util.Comparator;

import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;

public class FriendsScoreComparator implements Comparator<HttpGetFriendData> {

	@Override
	public int compare(HttpGetFriendData lhs, HttpGetFriendData rhs) {
		return rhs.score - lhs.score;
	}

}
