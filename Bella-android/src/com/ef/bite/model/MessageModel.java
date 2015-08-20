package com.ef.bite.model;


import android.graphics.Bitmap;
import android.view.View;

/**
 * Message Window 输入对象
 * @author Admin
 *
 */
public class MessageModel {

	public final static int MESSAGE_TYPE_TEXT = 0X0001;
	public final static int MESSAGE_TYPE_INPUT = 0X0002;
	public final static int MESSAGE_TYPE_IMAGE = 0X0003;
	public final static int MESSAGE_TYPE_AUDIO = 0X0004;
	
	public String Message;
	
	public String ButtonText;
	
	/**
	 * 0 - Text; 1 - Input; 2 - Image; 3 - Audio Player
	 */
	public int Type;
	
	public Bitmap Image;
	
	public String Audio; 
	
	public View.OnClickListener OnClick;
	
	public OnInputFinishListener OnInputFinish;
	
	public interface OnInputFinishListener{
		public void finish(String input);
	}
}
