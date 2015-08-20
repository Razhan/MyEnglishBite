package com.ef.bite.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.ef.bite.model.MapNodeModel;

/**
 * Not Used
 * @author Allen.Zhu
 *
 */
public class MapNodeView extends View implements View.OnClickListener{

	private MapNodeModel mNode;
	
	public MapNodeView(Context context, MapNodeModel node){
		super(context);
		mNode = node;
	}
	
	public MapNodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MapNodeView(Context context) {
		super(context);
	}
	
	public void setData(MapNodeModel node){
		this.mNode = node;
		invalidate();
	}
	
	public MapNodeModel getData(){
		return this.mNode;
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mNode!=null){
			drawBitmap(canvas, mNode.getBitmap(), mNode.getPositionX(), mNode.getPositionY());
		}
	}
	
	private void drawBitmap(Canvas canvas, Bitmap map, float x, float y){
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
	    float boardPosX = x - mapWidth/2;
	    float boardPosY = y - mapHeight/2;
	    canvas.drawBitmap(map, boardPosX, boardPosY, null);
	}

	@Override
	public void onClick(View v) {
		int index = (Integer)v.getTag();
		Toast.makeText(getContext(), "Map Node: " + mNode.getName() + " is selected!" , Toast.LENGTH_SHORT).show();
	}
}
