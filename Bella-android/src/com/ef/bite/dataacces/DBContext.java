package com.ef.bite.dataacces;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBContext{

	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
    public DBContext(Context context) {  
        dbHelper = DBHelper.Instance(context);
        database = dbHelper.getWritableDatabase();
    }  
	
    /*
     * 查询
     */
    public Cursor query(String sql)
    {
    	if(database!=null && database.isOpen())
    		return database.rawQuery(sql, null);
    	else{
    		database = dbHelper.getWritableDatabase();
    		return database.rawQuery(sql, null);
    	}
    }
    /*
     * 查询
     */
	public Cursor query(String sql,  String[] selectionArgs)
	{
		if(database!=null && database.isOpen())
    		return database.rawQuery(sql, selectionArgs);
    	else{
    		database = dbHelper.getWritableDatabase();
    		return database.rawQuery(sql, selectionArgs);
    	}
	}
	
	/*
	 * 执行
	 */
	public void execute(String sql, Object[] bindArgs)
	{
		if(database ==null || !database.isOpen())
			database = dbHelper.getWritableDatabase();
		database.execSQL(sql, bindArgs);
	}
	
	/*
	 * 执行
	 */
	public void execute(String sql)
	{
		if(database ==null || !database.isOpen())
			database = dbHelper.getWritableDatabase();
		database.execSQL(sql);
	}

	public void beginTransaction(){
		if(database != null)
			database.beginTransaction();
	}
	
	public void setTransactionSuccessful(){
		if(database != null)
			database.setTransactionSuccessful();
	}
	
	public void endTransaction(){
		if(database != null)
			database.endTransaction();
	}
	
	public void close(){
		if(database != null)
			database.close();
	}
}
