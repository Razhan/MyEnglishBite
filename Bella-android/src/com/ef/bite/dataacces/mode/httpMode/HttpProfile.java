package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.utils.StringUtils;

public class HttpProfile extends HttpBaseMessage {
	
	public ProfileData data;

	public static class ProfileData {
		public String bella_id;
		public String alias;
		public String avatar_url;
		public String family_name;
		public String given_name;
        public String market_code;
		public String phone;
		public int score;
		public int Level;
		public int remaining_score_to_next_level;
		public int recording_count;
		public int recording_like_count;
		public double level_up_progress_pct;
	}
}
