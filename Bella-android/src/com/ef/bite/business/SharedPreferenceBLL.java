package com.ef.bite.business;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP存储公共方法
 * @author Allen.Zhu
 *
 */
public class SharedPreferenceBLL {

	private Context mContext;
	protected SharedPreferences mSharedPreference;
	protected SharedPreferences.Editor mEditor; 
	
	public final static String SP_KEY_CHUNK_MIGRATION = "chunk_migration_version";
	
	public SharedPreferenceBLL(Context context){
		mContext = context;
		mSharedPreference = mContext.getSharedPreferences("Global_Storage_Config", Context.MODE_PRIVATE);
		mEditor = mSharedPreference.edit();
	}
	
	/**
	 * 获得当前chunk migration的版本
	 * @return
	 */
	public int getChunkMigrationVersion(){
		try{
			return mSharedPreference.getInt("sp_chunk_migration_version",0);
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 设置当前的chunk migraton版本
	 * @param version
	 */
	public void setChunkMigrationVersion(int version){
		try{
			mEditor.putInt(SP_KEY_CHUNK_MIGRATION,version);
			mEditor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
