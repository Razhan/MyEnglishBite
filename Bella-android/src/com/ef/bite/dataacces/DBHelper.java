package com.ef.bite.dataacces;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private String tag = "DBHelper";
	private static DBHelper instance;
	private static final String DATABASE_NAME = "hair_portal.db";
	private static final int DATABASE_VERSION = 1;

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public synchronized static DBHelper Instance(Context context) {
		if (instance == null) {
			instance = new DBHelper(context);
			return instance;
		} else
			return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(tag, "First time to open sqlite database!");
		db.execSQL("CREATE TABLE IF NOT EXISTS BaseHaierApp (id integer PRIMARY KEY, name nvarchar(255), type_id integer,enabled integer, has_new integer, badge_number integer, image text, icon text, image_local nvarchar(500), icon_local nvarchar(500), location_index integer, isLive integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS NativeHaierApp(appId integer PRIMARY KEY, schema nvarchar(500))");
		db.execSQL("CREATE TABLE IF NOT EXISTS WebPageHaierApp (appId integer PRIMARY KEY, isHtml integer, url nvarchar(500), html text)");
		db.execSQL("CREATE TABLE IF NOT EXISTS BrowserHaierApp (appId integer PRIMARY KEY, url nvarchar(500))");
		db.execSQL("CREATE TABLE IF NOT EXISTS ContractHaierApp (appId integer PRIMARY KEY, contract_name nvarchar(50), contract_phone nvarchar(50), contract_email nvarchar(50))");
		db.execSQL("CREATE TABLE IF NOT EXISTS UserInfo (userId integer PRIMARY KEY, userName nvarchar(255), photoUrl nvarcha(255), photoLocal nvarchar(255))");
		db.execSQL("CREATE TABLE IF NOT EXISTS UserLogin (userId integer PRIMARY KEY, isLogin integer, token nvarchar(255), lastesLoginTime integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS GlobalSettings (userId integer PRIMARY KEY, isAppsInitialized integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(tag, "Upgrade database version from " + oldVersion + " to "
				+ newVersion);

	}

}
