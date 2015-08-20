package com.ef.bite.dataacces.mode;

public class TraceMode {
	private Integer id;
	private String uid;
	private String groupId;
	private String event;
	private String value;
	private Integer isSyncWithServer;
	private long createTime;
	private long synctime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public String getGroupId(){
		return groupId;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getIsSyncWithServer() {
		return isSyncWithServer;
	}
	public void setIsSyncWithServer(Integer isSyncWithServer) {
		this.isSyncWithServer = isSyncWithServer;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getSynctime() {
		return synctime;
	}
	public void setSynctime(long synctime) {
		this.synctime = synctime;
	}
}
