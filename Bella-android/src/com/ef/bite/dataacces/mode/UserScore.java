package com.ef.bite.dataacces.mode;

public class UserScore {
	
	private String uid;
	
	private Integer score;
	
	private Integer isSyncWithServer;
	
	private long syncTime;
	
	private long createTime;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getIsSyncWithServer() {
		return isSyncWithServer;
	}
	public void setIsSyncWithServer(Integer isSyncWithServer) {
		this.isSyncWithServer = isSyncWithServer;
	}
	public long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public UserScore() {
		super();
	}
	
	
}
