package com.ef.bite.dataacces.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.MyDBHelper;
import com.ef.bite.dataacces.mode.UserProgressStatus;

public class UserProgressStatusDao {
	private SQLiteDatabase database;
	private MyDBHelper dbHelper;
    private ChunksHolder chunksHolder=ChunksHolder.getInstance();
    private ChunkDao chunkDao;

	public UserProgressStatusDao(Context context) {
		dbHelper = MyDBHelper.Instance(context);
		database = dbHelper.getWritableDatabase();
        chunkDao = new ChunkDao(context);
	}

	/*
	 * 获得指定chunk的progress状态
	 */
	public UserProgressStatus getUserProgress(String uid, String chunkCode) {
		Cursor cursor = null;
		try {
			cursor = database
					.rawQuery(
							"select * from UserProgressStatus where chunkCode=? and uid=?",
							new String[] { chunkCode, uid });
			if (cursor != null && cursor.moveToFirst()) {
				UserProgressStatus userProgressStatus = queryProgress(cursor);
				return userProgressStatus;
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	/**
	 * 同步服务器数据
	 * 
	 * @param chunkCode
	 * @param uid
	 * @param status
	 * @param learnTime
	 * @param syncTime
	 */
	public void updateSyncProgress(String chunkCode, String uid, int status,
			long createTime, long learnTime, long syncTime) {
		try {
			database.execSQL(
					"update UserProgressStatus set chunkStatus=?,isSyncWithServer=2, createTime=?, chunkLearnTime=?, syncTime=? where chunkCode=? and uid=?",
					new Object[] { status, createTime, learnTime, syncTime,
							chunkCode, uid });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 插入progress
	public void insertProgress(UserProgressStatus userProgressStatus) {

//		Cursor cursor = database.rawQuery(
//				"select * from ChunkDefinition where chunkCode=?",
//				new String[] { userProgressStatus.getChunkCode().toString() });
//
//		if (cursor.getCount() < 1) {
//			return;
//		}


       if (!chunkDao.isChunkExit(userProgressStatus.getChunkCode().toString())){
           return;
       }

        database.execSQL(
				"insert into UserProgressStatus values(?,?,?,?,?,?,?,?,0,?,?,?,?,0,?,?,?)",
				new String[] { null, userProgressStatus.getUid(),
						userProgressStatus.getChunkCode().toString(),
						userProgressStatus.getChunkStatus().toString(),
						userProgressStatus.getRehearseStatus().toString(),
						userProgressStatus.getIsSyncWithServer().toString(),
						String.valueOf(userProgressStatus.getSyncTime()),
						String.valueOf(userProgressStatus.getCreateTime()),
						String.valueOf(userProgressStatus.getR1CostTime()),
						String.valueOf(userProgressStatus.getR2CostTime()),
						String.valueOf(userProgressStatus.getR3CostTime()),
						String.valueOf(userProgressStatus.getChunkLearnTime()),
						String.valueOf(userProgressStatus.getR1Score()),
						String.valueOf(userProgressStatus.getR2Score()),
						String.valueOf(userProgressStatus.getR3Score()) });
	}

	// 设置chunk 状态
	public Boolean SetChunkStatus(String chunkID, String uid, Integer status,
			Integer rehearseStatus, Integer score, long costtime) {
		UserProgressStatus userProgressStatus = null;
		Cursor cursor = database
				.rawQuery(
						"select * from UserProgressStatus where chunkCode==? and uid=?",
						new String[] { chunkID, uid });
		if (cursor != null && cursor.moveToFirst()) {
			userProgressStatus = queryProgress(cursor);
			cursor.close();
		} else {
			return false;
		}

		switch (status) {
		case 1:
			if (userProgressStatus.getChunkStatus() == 0) {
				database.execSQL(
						"update UserProgressStatus set chunkstatus=? ,isSyncWithServer=1 where chunkCode=? and uid=?",
						new String[] { String.valueOf(status),
								userProgressStatus.getChunkCode().toString(),
								uid });
				return true;
			}
			break;
		case 2:
			if (userProgressStatus.getChunkStatus() == 1) {
				database.execSQL(
						"update UserProgressStatus set isSyncWithServer=1, chunkstatus=?, presentationScore=? where chunkCode=? and uid=?",
						new String[] { String.valueOf(status),
								String.valueOf(score),
								userProgressStatus.getChunkCode().toString(),
								uid });
				return true;
			}
			break;
		case 3:
			if (userProgressStatus.getChunkStatus() == 2) {
				database.execSQL(
						"update UserProgressStatus set chunkstatus=? , practiceScore=? ,chunkLearnTime=? ,isSyncWithServer=1 where chunkCode=? and uid=?",
						new String[] { String.valueOf(status),
								String.valueOf(score),
								String.valueOf(new Date().getTime()),
								userProgressStatus.getChunkCode(), uid });
				return true;
			} else if (userProgressStatus.getChunkStatus() == 3) {
				switch (rehearseStatus) {
				case 1:
					if (userProgressStatus.getRehearseStatus() == 0) {
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r1Score=?,r1CostTime=?,isSyncWithServer=1 where chunkCode=? and uid=? and chunkStatus=3",
								new String[] { String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode(), uid });
						return true;
					}
					break;
				case 2:
					if (userProgressStatus.getRehearseStatus() == 1) {
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r2Score=?,r2CostTime=? ,isSyncWithServer=1 where chunkCode=? and uid=? and chunkStatus=3",
								new String[] { String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode(), uid });
						return true;

					}
					break;
				case 3:
					if (userProgressStatus.getRehearseStatus() == 2) {
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r3Score=?,r3CostTime=? ,isSyncWithServer=1  where chunkCode=? and uid=? and chunkStatus=3",
								new String[] { String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode(), uid });
						return true;
					}
					break;

				}
			}

			break;
		}
		return false;
	}

	public long getMaxSyncTime(String uid) {
		Cursor cursor = null;
		try {
			cursor = database
					.rawQuery(
							"select max(syncTime) from UserProgressStatus where uid=? and isSyncWithServer=2",
							new String[] { uid });
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					long syncTime = cursor.getLong(0);
					return syncTime;
				}
			}
			return 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}

	/**
	 * 获得上次同步时间
	 * 
	 * @param uid
	 * @param chunkCode
	 * @return 小于0，代表
	 */
	public long getSyncTime(String uid, String chunkCode) {
		Cursor cursor = null;
		try {
			cursor = database
					.rawQuery(
							"select syncTime from UserProgressStatus where uid=? and chunkCode=?",
							new String[] { uid, chunkCode });
			if (cursor != null && cursor.moveToFirst()) {
				long syncTime = cursor.getLong(0);
				return syncTime;
			}
			return 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}

	public void setSyncTime(String uid, long syncTime) {
		database.execSQL(
				"update UserProgressStatus set isSyncWithServer=2 , syncTime=? where uid=? and isSyncWithServer=1",
				new String[] { String.valueOf(syncTime), uid });
	}

	public List<UserProgressStatus> getSyncProgress(String uid) {
		List<UserProgressStatus> userProgressStatuses = new ArrayList<UserProgressStatus>();
		Cursor cursor = database
				.rawQuery(
						"select * from UserProgressStatus where uid=? and isSyncWithServer=1",
						new String[] { uid });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				UserProgressStatus userProgressStatus = queryProgress(cursor);
				userProgressStatuses.add(userProgressStatus);
			}
			cursor.close();
			return userProgressStatuses;
		}
		return null;
	}

	public Integer getidBychunkCode(String chunkCode, String uid) {
		Cursor cursor = database
				.rawQuery(
						"select id from UserProgressStatus where chunkCode=? and uid=?",
						new String[] { chunkCode, uid });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
			cursor.close();
		}

		return null;
	}

	public Boolean SetSyncChunkStatus(String ChunkCode, String uid,
			Integer status, Integer rehearseStatus, Integer score, long costtime) {
		UserProgressStatus userProgressStatus = new UserProgressStatus();
		Cursor cursor = null;
		try {
			cursor = database
					.rawQuery(
							"select * from UserProgressStatus where chunkCode=? and uid=?",
							new String[] { ChunkCode, uid });
			if (cursor != null && cursor.moveToFirst()) {
				userProgressStatus = queryProgress(cursor);
				cursor.close();
			} else {
				return false;
			}

			switch (status) {
			case 1:
				if (userProgressStatus.getChunkStatus() == 0) {
					database.execSQL(
							"update UserProgressStatus set chunkstatus=? ,isSyncWithServer=2 where chunkCode=? and uid=?",
							new String[] {
									String.valueOf(status),
									userProgressStatus.getChunkCode()
											.toString(), uid });
					return true;
				}
				break;
			case 2:
				if (userProgressStatus.getChunkStatus() == 1) {
					database.execSQL(
							"update UserProgressStatus set isSyncWithServer=2, chunkstatus=?, presentationScore=? where chunkCode=? and uid=?",
							new String[] {
									String.valueOf(status),
									String.valueOf(score),
									userProgressStatus.getChunkCode()
											.toString(), uid });
					return true;
				}
				break;
			case 3:
				if (userProgressStatus.getChunkStatus() == 2) {
					database.execSQL(
							"update UserProgressStatus set chunkstatus=? , practiceScore=? ,chunkLearnTime=? ,isSyncWithServer=2 where chunkCode=? and uid=?",
							new String[] {
									String.valueOf(status),
									String.valueOf(score),
									String.valueOf(new Date().getTime()),
									userProgressStatus.getChunkCode()
											.toString(), uid });
					return true;
				} else if (userProgressStatus.getChunkStatus() == 3) {
					switch (rehearseStatus) {
					case 1:
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r1Score=?,r1CostTime=?,isSyncWithServer=2 where chunkCode=? and uid=? and chunkStatus=3",
								new String[] {
										String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode()
												.toString(), uid });
						return true;
					case 2:
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r2Score=?,r2CostTime=? ,isSyncWithServer=2 where chunkCode=? and uid=? and chunkStatus=3",
								new String[] {
										String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode()
												.toString(), uid });
						return true;
					case 3:
						database.execSQL(
								"update UserProgressStatus set rehearseStatus=?,r3Score=?,r3CostTime=? ,isSyncWithServer=2  where chunkCode=? and uid=? and chunkStatus=3",
								new String[] {
										String.valueOf(rehearseStatus),
										String.valueOf(score),
										String.valueOf(costtime),
										userProgressStatus.getChunkCode()
												.toString(), uid });
						return true;
					}
				}
				break;
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
	}

	/**
	 * 如果rehearse做错了,推迟rehearse
	 */

	public boolean setRehearseFailed(String uid, String chunkId) {
		UserProgressStatus userProgressStatus = null;
		Cursor cursor = database
				.rawQuery(
						"select * from UserProgressStatus where chunkCode=? and uid=?",
						new String[] { chunkId, uid });
		if (cursor != null && cursor.moveToFirst()) {
			userProgressStatus = queryProgress(cursor);
			cursor.close();
		} else {
			return false;
		}
		if (userProgressStatus.getChunkStatus() == 3) {
			switch (userProgressStatus.getRehearseStatus()) {
			case 0:
				database.execSQL(
						"update UserProgressStatus set chunkLearnTime=?,isSyncWithServer=1 where chunkCode=? and uid=? and chunkStatus=3",
						new String[] { String.valueOf(new Date().getTime()),
								userProgressStatus.getChunkCode(), uid });
				break;
			case 1:

				database.execSQL(
						"update UserProgressStatus set r1CostTime=? , isSyncWithServer=1 where chunkCode=? and uid=? and chunkStatus=3",
						new String[] { String.valueOf(new Date().getTime()),
								userProgressStatus.getChunkCode(), uid });

				break;
			case 2:

				database.execSQL(
						"update UserProgressStatus set r2CostTime=? ,isSyncWithServer=1  where chunkCode=? and uid=? and chunkStatus=3",
						new String[] { String.valueOf(new Date().getTime()),
								userProgressStatus.getChunkCode(), uid });
				break;
			}
		}

		return true;
	}

	private UserProgressStatus queryProgress(Cursor cursor) {
		if (cursor == null)
			return null;
		UserProgressStatus userProgressStatus = new UserProgressStatus();
		userProgressStatus.setChunkLearnTime(cursor.getLong(cursor
				.getColumnIndex("chunkLearnTime")));
		userProgressStatus.setPresentationScore(cursor.getInt(cursor
				.getColumnIndex("presentationScore")));
		userProgressStatus.setCreateTime(cursor.getLong(cursor
				.getColumnIndex("createTime")));
		userProgressStatus.setIsSyncWithServer(cursor.getInt(cursor
				.getColumnIndex("isSyncWithServer")));
		userProgressStatus.setR1CostTime(cursor.getLong(cursor
				.getColumnIndex("r1CostTime")));
		userProgressStatus.setR2CostTime(cursor.getLong(cursor
				.getColumnIndex("r2CostTime")));
		userProgressStatus.setR3CostTime(cursor.getLong(cursor
				.getColumnIndex("r3CostTime")));
		userProgressStatus.setRehearseStatus(cursor.getInt(cursor
				.getColumnIndex("rehearseStatus")));
		userProgressStatus.setChunkStatus(cursor.getInt(cursor
				.getColumnIndex("chunkStatus")));
		userProgressStatus.setR1Score(cursor.getInt(cursor
				.getColumnIndex("r1Score")));
		userProgressStatus.setR2Score(cursor.getInt(cursor
				.getColumnIndex("r2Score")));
		userProgressStatus.setR3Score(cursor.getInt(cursor
				.getColumnIndex("r3Score")));
		userProgressStatus.setSyncTime(cursor.getLong(cursor
				.getColumnIndex("syncTime")));
		userProgressStatus.setPracticeScore(cursor.getInt(cursor
				.getColumnIndex("practiceScore")));
		userProgressStatus.setChunkCode(cursor.getString(cursor
				.getColumnIndex("chunkCode")));
		return userProgressStatus;
	}
}
