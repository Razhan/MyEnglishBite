package com.ef.bite.dataacces.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ef.bite.dataacces.mode.MyDBHelper;
import com.ef.bite.dataacces.mode.TraceMode;

public class TraceModeDao {
	private SQLiteDatabase database;
	private MyDBHelper dbHelper;

	public TraceModeDao(Context context) {
		dbHelper = MyDBHelper.Instance(context);
		database = dbHelper.getWritableDatabase();
	}

	// 更新
	public void setSync(String uid, long syncTime) {
		database.execSQL(
				"update EventTrace set isSyncWithServer=2 , syncTime=? where uid=?",
				new String[] { String.valueOf(syncTime), uid });
	}

	/**
	 * 清理一定时间之前的tracking记录
	 * **/
	public void clearTrace(String uid, long timeAgo) {
		try {
			database.execSQL(
					"delete from EventTrace where isSyncWithServer=2 and uid=? and syncTime < ?",
					new String[] { uid, String.valueOf(timeAgo) });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void insertTrace(TraceMode traceMode) {
		database.execSQL(
				"insert into EventTrace values(?,?,?,?,?,?,?,?)",
				new String[] { null, traceMode.getUid(),
						traceMode.getGroupId(), traceMode.getEvent(),
						traceMode.getValue(),
						traceMode.getIsSyncWithServer().toString(),
						String.valueOf(traceMode.getCreateTime()),
						String.valueOf(traceMode.getSynctime()) });
	}

	public void updateTrace(Integer id) {
		database.execSQL(
				"update EventTrace set isSyncWithServer=2,synctime=? where id=?",
				new String[] { String.valueOf(new Date().getTime()),
						id.toString() });
	}

	/**
	 * 获得TopN个未同步的tracing记录
	 * 
	 * @param topN
	 * @param uid
	 * @return
	 */
	public List<TraceMode> getUnSyncTopN(Integer topN, String uid) {
		List<TraceMode> traceModes = new ArrayList<TraceMode>();
		Cursor cursor = database
				.rawQuery(
						"select * from EventTrace where uid=? and isSyncWithServer=1 limit ?",
						new String[] { uid, topN.toString() });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				TraceMode traceMode = new TraceMode();
				traceMode.setCreateTime(cursor.getLong(cursor
						.getColumnIndex("createTime")));
				traceMode.setEvent(cursor.getString(cursor
						.getColumnIndex("event")));
				traceMode.setIsSyncWithServer(cursor.getInt(cursor
						.getColumnIndex("isSyncWithServer")));
				traceMode.setSynctime(cursor.getLong(cursor
						.getColumnIndex("synctime")));
				traceMode.setValue(cursor.getString(cursor
						.getColumnIndex("value")));
				traceMode
						.setUid(cursor.getString(cursor.getColumnIndex("uid")));
				traceMode.setGroupId(cursor.getString(cursor
						.getColumnIndex("groupid")));
				traceModes.add(traceMode);
			}
			cursor.close();
			return traceModes;
		}
		return null;
	}

}
