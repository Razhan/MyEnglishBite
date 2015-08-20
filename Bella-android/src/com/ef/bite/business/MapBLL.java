package com.ef.bite.business;

import java.util.List;

import android.content.Context;

import com.ef.bite.model.MapNodeModel;

public class MapBLL {

	public MapBLL(Context context){ }
	
	/**
	 * 获得所有的MapNode
	 * @return
	 */
	public List<MapNodeModel> getMapNodeList(){
		return null;
	}
	
	/**
	 * 根据ID获得MapNode
	 * @param id
	 * @return
	 */
	public MapNodeModel getMapNodeById(int id){
		return null;
	}
	
	/**
	 * 
	 * @param nodeList
	 */
	public void setMapNodeList(List<MapNodeModel> nodeList){
		
	}
	
	/**
	 * 设置MapNode的lock状态
	 * @param id
	 * @param locked
	 * @return
	 */
	public boolean setMapLocked(int id, boolean locked){
		MapNodeModel currentNode = getMapNodeById(id);
		if(currentNode !=null){
			currentNode.setIsLocked(locked);
			return true;
		}
		return false;
	}
}
