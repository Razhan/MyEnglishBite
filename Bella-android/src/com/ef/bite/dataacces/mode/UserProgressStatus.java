package com.ef.bite.dataacces.mode;

public class UserProgressStatus {

	private String uid;

	private String chunkCode;
	private Integer chunkStatus;
	private Integer rehearseStatus;
	
	private Integer practiceScore;
	private Integer isSyncWithServer;

	private long syncTime;

	private long createTime;

	private Integer presentationScore;

	private long r1CostTime;
	
	private long r2CostTime;
	
	private long r3CostTime;
	
	private long chunkLearnTime;
	
	private Integer r1Score;
	private Integer r2Score;
	private Integer r3Score;
	
	
	
	
	public Integer getPracticeScore() {
		return practiceScore;
	}

	public void setPracticeScore(Integer practiceScore) {
		this.practiceScore = practiceScore;
	}

	public Integer getR1Score() {
		return r1Score;
	}

	public void setR1Score(Integer r1Score) {
		this.r1Score = r1Score;
	}

	public Integer getR2Score() {
		return r2Score;
	}

	public void setR2Score(Integer r2Score) {
		this.r2Score = r2Score;
	}

	public Integer getR3Score() {
		return r3Score;
	}

	public void setR3Score(Integer r3Score) {
		this.r3Score = r3Score;
	}

	public String getChunkCode() {
		return chunkCode;
	}

	public void setChunkCode(String chunkCode) {
		this.chunkCode = chunkCode;
	}

	public long getR1CostTime() {
		return r1CostTime;
	}

	public void setR1CostTime(long r1CostTime) {
		this.r1CostTime = r1CostTime;
	}

	public long getR2CostTime() {
		return r2CostTime;
	}

	public void setR2CostTime(long r2CostTime) {
		this.r2CostTime = r2CostTime;
	}

	public long getR3CostTime() {
		return r3CostTime;
	}

	public void setR3CostTime(long r3CostTime) {
		this.r3CostTime = r3CostTime;
	}

	public long getChunkLearnTime() {
		return chunkLearnTime;
	}

	public void setChunkLearnTime(long chunkLearnTime) {
		this.chunkLearnTime = chunkLearnTime;
	}


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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


	

	public Integer getPresentationScore() {
		return presentationScore;
	}

	public void setPresentationScore(Integer presentationScore) {
		this.presentationScore = presentationScore;
	}

	public UserProgressStatus() {
		super();
	}

	public Integer getChunkStatus() {
		return chunkStatus;
	}

	public void setChunkStatus(Integer chunkStatus) {
		this.chunkStatus = chunkStatus;
	}

	public Integer getRehearseStatus() {
		return rehearseStatus;
	}

	public void setRehearseStatus(Integer rehearseStatus) {
		this.rehearseStatus = rehearseStatus;
	}

	

}
