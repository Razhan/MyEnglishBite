package com.ef.bite.ui.balloon;

/**
 * 矩阵点， 气球布局的基本点
 */
public class MatrixPoint{
	
	public MatrixPoint(int x, int y, int distX, int distY){
		this.X = x;
		this.Y = y;
		this.DistX =  distX;
		this.DistY =  distY;
	}
	
	/** X坐标上的第几个点   **/
	public int X = 0;
	/** Y坐标上的第几个点  **/
	public int Y = 0;
	/** X坐标上的距离	**/
	public int DistX;
	/** Y左边上的距离 **/
	public int DistY;
	/** 是否被占用   **/
	public boolean IsOccupancy = false;
}