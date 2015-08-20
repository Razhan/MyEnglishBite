package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.ui.user.contactlistview.ContactItemInterface;

public class HttpGetFriendData implements ContactItemInterface {

     public String bella_id;
     public String alias;
     public String avatar;
     public int score;
     public int friend_count;
     public int rank;
	@Override
	public String getItemForIndex() {
		return alias;
	}
}
