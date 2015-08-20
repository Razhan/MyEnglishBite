package com.ef.bite.dataacces.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ef.bite.dataacces.mode.MyDBHelper;
import com.ef.bite.dataacces.mode.UserScore;

public class UserScoreDao {
	private SQLiteDatabase database;
	private MyDBHelper dbHelper;

	public UserScoreDao(Context context) {
		dbHelper = MyDBHelper.Instance(context);
		database = dbHelper.getWritableDatabase();
	}
//通过uid 得到userScore
	public UserScore getUserScore(String uid) {
		Cursor cursor = database.rawQuery(
				"select * from UserScore where uid=?", new String[] { uid });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				UserScore userScore = new UserScore();
				userScore.setCreateTime(cursor.getLong(cursor
						.getColumnIndex("createTime")));
				userScore
						.setUid(cursor.getString(cursor.getColumnIndex("uid")));
				userScore.setIsSyncWithServer(cursor.getInt(cursor
						.getColumnIndex("isSyncWithServer")));
				userScore
						.setScore(cursor.getInt(cursor.getColumnIndex("score")));
				userScore.setSyncTime(cursor.getLong(cursor
						.getColumnIndex("syncTime")));
				return userScore;
			}
			cursor.close();
		}
		return null;
	}

	public void setSync(String uid ,long syncTime) {
		database.execSQL("update UserScore set isSyncWithServer=2,syncTime=? where uid=?",
				new String[] {String.valueOf(syncTime), uid });
	}
	
	public void upDateUserScore(Integer increaseScore, String uid) {
		database.execSQL("update UserScore set score=?,isSyncWithServer=1 where uid=?",
				new String[] { increaseScore.toString(), uid });
	}

	public void insertUserScore(UserScore userScore) {
		
					database.execSQL(
							"insert into UserScore values(?,?,?,?,?)",
							new String[] {userScore.getUid(),userScore.getScore().toString(),
									userScore.getIsSyncWithServer().toString(),
								String.valueOf(userScore.getSyncTime()),String.valueOf(userScore.getCreateTime()) });
		
	}

	public long getMaxSyncTime(String uid) {
		Cursor cursor = database
				.rawQuery(
					//	"select syncTime from UserScore where isSyncWithServer=2 and uid=?"
						"select syncTime from UserScore where uid=?"
								, new String[]{uid});
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				long syncTime = cursor.getLong(0);
				return syncTime;
			}
		}
		return 0;
	}
	
	//获得没有上传的分数
	public UserScore getSyncScore(String uid) {
		Cursor cursor= database.rawQuery("select * from UserScore where isSyncWithServer=1 and uid=?" , new String[]{ uid });
		if(cursor!=null && cursor.moveToFirst()){
			UserScore userScore = new UserScore();
				userScore.setCreateTime(cursor.getLong(cursor
						.getColumnIndex("createTime")));
				userScore
						.setUid(cursor.getString(cursor.getColumnIndex("uid")));
				userScore.setIsSyncWithServer(cursor.getInt(cursor
						.getColumnIndex("isSyncWithServer")));
				userScore
						.setScore(cursor.getInt(cursor.getColumnIndex("score")));
				userScore.setSyncTime(cursor.getLong(cursor
						.getColumnIndex("syncTime")));
			cursor.close();
			return userScore;
		}
		return null;
	}
	
}
