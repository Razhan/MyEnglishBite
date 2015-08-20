package com.ef.bite.dataacces.mode.httpMode;

import java.util.Date;
import java.util.List;

/**
 * 读取通知的request
 * @author Allen
 *
 */
public class ReadNotificationHttpRequest {

	/** 当前通知接受者的id **/
	public String bella_id;
	
	/** 通知的ID列表 **/
	public List<String> notification_ids;
	
	/** 通知发送者 **/
	public String from_bella_id;
	
	/** 通知的内容 **/
	public String content;
	
	/** 通知类型，现阶段只有"add_friend" **/
	public String notification_type;
	
	/** 设置为true **/
	public boolean has_read = true;
	
	/** UTC time **/
	public Date readed_at;
}
