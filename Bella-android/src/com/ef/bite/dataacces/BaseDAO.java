package com.ef.bite.dataacces;

import android.content.Context;

public abstract class BaseDAO <T> {
	DBContext mDBContext;
	public BaseDAO(Context context){
		mDBContext = new DBContext(context);
	}
	
	public abstract  T getByID(int id);
	public abstract  T add(T entity);
	public abstract	 T update(T entity);
	public abstract  boolean delete(int id);
}
