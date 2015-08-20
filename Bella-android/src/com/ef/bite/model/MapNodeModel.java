package com.ef.bite.model;

import android.graphics.Bitmap;

public class MapNodeModel {
	private int id;
	private String name;
	private Bitmap bitmap;
	private float positionX;
	private float positionY;
	private boolean isLauncher;		// 是否是始发节点
	private boolean isLocked;
	
	public MapNodeModel(int id, String name, Bitmap bitmap, float x, float y, boolean isLauncher, boolean isLocked){
		this.id = id;
		this.name = name;
		this.bitmap = bitmap;
		this.positionX = x;
		this.positionY = y;
		this.isLauncher = isLauncher;
		this.isLocked = isLocked;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public float getPositionX(){
		return positionX;
	}
	
	public float getPositionY(){
		return positionY;
	}
	
	public boolean isLaucher(){
		return isLauncher;
	}
	
	public boolean isLocked(){
		return isLocked;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public void setPositionX(float positionX){
		this.positionX = positionX;
	}
	
	public void setPositionY(float positionY){
		this.positionY = positionY;
	}
	
	public void setIsLauncher(boolean isLauncher){
		this.isLauncher = isLauncher;
	}
	
	public void setIsLocked(boolean isLocked){
		this.isLocked = isLocked;
	}
}
