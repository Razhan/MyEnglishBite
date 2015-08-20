package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

public class HttpAvatarData extends HttpBaseMessage {

	public List<AvatarData> data;
	
	public class AvatarData{
		public String avatar;
	}
}
