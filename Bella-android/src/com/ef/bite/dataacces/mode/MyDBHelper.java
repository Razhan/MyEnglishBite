package com.ef.bite.dataacces.mode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
	private String tag = "DBHelper";
	private static MyDBHelper instance;
	private static final String DATABASE_NAME = "efDataBase.db";
	private static final int DATABASE_VERSION = 2;
	private int DATABASE_OLD_VERSION = 1;
	private int DATABASE_OLD_VERSION2 = 2;

	private MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public synchronized static MyDBHelper Instance(Context context) {
		if (instance == null) {
			instance = new MyDBHelper(context);
			return instance;
		} else
			return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(tag, "First time to open sqlite database!");
		db.execSQL("CREATE TABLE IF NOT EXISTS ChunkDefinition(id integer primary key autoincrement,chunkCode integer,chunkText varchar(1000),explanation varchar(1000),audioFileName varchar(1000),language varchar,pronounce varchar,errorWords varchar,version integer,isPreinstall integer);");
		db.execSQL("CREATE TABLE IF NOT EXISTS MulityChoiceQuestions(id integer primary key autoincrement ,chunkid integer REFERENCES ChunkDefinition(id),audioFile varchar(1000),score integer,content varchar(1000),limitTime bigint,type integer,orderNum integer,random integer,header varchar,multiType integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS UserScore(uid varchar(100) primary key,score integer,isSyncWithServer integer,syncTime bigint,createTime bigint );");
		db.execSQL("CREATE TABLE IF NOT EXISTS ChunkPresentation(id integer primary key autoincrement,audioFile varchar(1000),score integer,chunkid integer REFERENCES ChunkDefinition(id))");
		db.execSQL("CREATE TABLE IF NOT EXISTS PresentationConversation(id integer primary key autoincrement,presentationID integer references ChunkPresentation(id),sentence varchar(1000),characterAvater varchar(1000),startTime integer,endTime integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS MulityChoiceAnswers(id integer primary key autoincrement,questionID integer references mulityChoiceQuestions(id),orderNum integer, answer varchar(1000),hit varchar(1000),isCorrect integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS UserProgressStatus(id integer primary key autoincrement,uid varchar(1000),chunkCode varchar(50),chunkStatus integer,rehearseStatus integer,isSyncWithServer integer,syncTime bigint ,createTime bigint,presentationScore integer,r1CostTime bigint,r2CostTime bigint,r3CostTime bigint,chunkLearnTime bigtime,practiceScore integer,r1Score integer,r2Score integer,r3Score integer);");
		db.execSQL("CREATE TABLE IF NOT EXISTS HintDefinition(id integer primary key autoincrement,chunkid integer references ChunkDefinition(id),content varchar(1000),example varchar(1000))");
		db.execSQL("CREATE TABLE IF NOT EXISTS EventTrace( id integer primary key, uid varchar, groupid varchar, event varcahr,value varchar,isSyncWithServer integer,createTime bihint,synctime bigint)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(tag, "Upgrade database version from " + oldVersion + " to "
				+ newVersion);
		if (newVersion == DATABASE_VERSION
				&& oldVersion == DATABASE_OLD_VERSION) {
			migrationToVersion2(db);
		}
		// if (newVersion == DATABASE_VERSION
		// && oldVersion == DATABASE_OLD_VERSION2) {
		//
		// }
	}

	/**
	 * 迁移数据库到version 2
	 */
	private void migrationToVersion2(SQLiteDatabase db) {
		try {
			db.execSQL("ALTER TABLE PresentationConversation ADD COLUMN startTime integer");
			db.execSQL("ALTER TABLE PresentationConversation ADD COLUMN endTime integer");
			db.execSQL("ALTER TABLE ChunkDefinition ADD COLUMN version integer");
			db.execSQL("ALTER TABLE ChunkDefinition ADD COLUMN isPreinstall integer DEFAULT 1");
		} catch (Throwable ex) {
			Log.e("Migration", "Fail to migrate database to version 2!");
			Log.e("Migration", ex.getMessage(), ex);
		}
	}

}
