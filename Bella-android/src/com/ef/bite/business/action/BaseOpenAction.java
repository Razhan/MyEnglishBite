package com.ef.bite.business.action;

import android.content.Context;

/**
 * 跳转Activity基类
 * @param <T>
 */
public abstract class BaseOpenAction<T> {

	public abstract void open(Context context, T data);
}
