package com.ef.bite.widget;


import android.content.Context;
import com.ef.bite.R;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;

/**
 * 设置项布局
 * @author Allen
 *
 */
public class SettingItemLayout extends RelativeLayout {
	
	public final static int SETTINGS_ITEM_TYPE_TEXT = 0;
	public final static int SETTINGS_ITEM_TYPE_Avatar = 1;
	public final static int SETTINGS_ITEM_TYPE_SWITCH = 2;
	
	private RelativeLayout mLayout;					// 设置项
	private TextView mName;							// 项名显示
	private TextView mTextValue;					// 文本值
	private RoundedImageView mImageValue;		// 图像值
	private ImageView mArraw;						// 是否可点击
	private ImageView mSwitch;						// 开关器
	private ImageView mLine;						// 底线

	private int mSettingType = SETTINGS_ITEM_TYPE_TEXT;
	private int img_id_switch_on = R.drawable.setting_switch_on;
	private int img_id_switch_off = R.drawable.setting_switch_off;
	private boolean mSwtichValue = false;				// 开关是否点亮
	private Context context;
	
	
	public SettingItemLayout(Context context) {
		super(context);
		getViews();
	}
	
	public SettingItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViews();
    }
 
    public SettingItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getViews();
    }
    
    private void getViews(){
    	inflate(getContext(), R.layout.widget_settings_list_item, this);
    	mLayout = (RelativeLayout)findViewById(R.id.setting_item_layout);
    	mName = (TextView)findViewById(R.id.setting_item_name);
    	mTextValue = (TextView)findViewById(R.id.setting_item_text_value);
    	mImageValue = (RoundedImageView)findViewById(R.id.setting_item_image_value);
    	mArraw = (ImageView)findViewById(R.id.setting_item_arraw);
    	mSwitch = (ImageView)findViewById(R.id.setting_item_switch);
    	mLine = (ImageView)findViewById(R.id.setting_item_line);
    	FontHelper.applyFont(getContext(), mLayout, FontHelper.FONT_OpenSans);
    }
    
    /**
     * 初始化Text类型
     */
    public void initiWithText(String name, String textValue, boolean showArrow, View.OnClickListener listener){
    	mImageValue.setVisibility(View.GONE);
    	mSwitch.setVisibility(View.GONE);
    	mArraw.setVisibility(View.VISIBLE);
    	mName.setVisibility(View.VISIBLE);
    	mTextValue.setVisibility(View.VISIBLE);
    	mLine.setVisibility(View.VISIBLE);
    	// 修改值
    	mSettingType = SETTINGS_ITEM_TYPE_TEXT;
    	mName.setText(name == null ? "":name);
    	mTextValue.setText(textValue == null ? "":textValue);
    	mArraw.setVisibility(showArrow ? View.VISIBLE:View.INVISIBLE);
    	if(listener!=null)
    		setOnClickListener(listener);
    	if(!showArrow){
    		mTextValue.setAlpha((float) 0.5);
    		mLayout.setBackgroundResource(R.color.white);
    	}
    }
    
    /**
     * 初始化Avatar类型
     */
    public void initiWithAvatar(String name, String userId, String avatarUrl ,boolean showArrow, View.OnClickListener listener){
    	mTextValue.setVisibility(View.GONE);
    	mSwitch.setVisibility(View.GONE);
    	mArraw.setVisibility(View.VISIBLE);
    	mName.setVisibility(View.VISIBLE);
    	mImageValue.setVisibility(View.VISIBLE);
    	mLine.setVisibility(View.VISIBLE);
    	// 修改值
    	mSettingType = SETTINGS_ITEM_TYPE_Avatar;
    	mName.setText(name == null ? "":name);
    	if(userId!=null)
    		AvatarHelper.LoadAvatar(mImageValue, userId, avatarUrl);
    	mArraw.setVisibility(showArrow ? View.VISIBLE:View.INVISIBLE);
    	if(listener!=null)
    		setOnClickListener(listener);
    	if(!showArrow)
    		mLayout.setBackgroundResource(R.color.white);
    }
    
    /***
     * 更新Avatar头像
     * @param uid
     * @param bitmap
     */
    public void updateAvatar(String uid, Bitmap bitmap){
    	AvatarHelper.UpdateAvatar(mImageValue, uid, bitmap);
    }
    
    /***
     * 初始化Switch类型
     */
    public void initWithSwitch(String name, boolean switchValue, final SwitchListener listener){
    	mArraw.setVisibility(View.GONE);
    	mTextValue.setVisibility(View.GONE);
    	mImageValue.setVisibility(View.GONE);
    	mName.setVisibility(View.VISIBLE);
    	mSwitch.setVisibility(View.VISIBLE);
    	mLine.setVisibility(View.VISIBLE);
    	// 设置值
    	mSettingType = SETTINGS_ITEM_TYPE_SWITCH;
    	mName.setText(name == null ? "":name);
    	mSwtichValue = switchValue;
    	mSwitch.setImageResource(mSwtichValue ? img_id_switch_on:img_id_switch_off);
    	setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSwtichValue = !mSwtichValue;
				mSwitch.setImageResource(mSwtichValue ? img_id_switch_on:img_id_switch_off);
				if(listener!=null)
					listener.onSwitch(mSwtichValue);
			}
		});
    	mLayout.setBackgroundResource(R.color.white);
    }
    /**
     * 设置是否显示底线
     * @param show
     */
    public void showBottomLine(boolean show){
    	mLine.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }
    
    /** 获得设置项的类型 **/
    public int getSettingType(){
    	return mSettingType;
    }
    /** 获得swtich的值  **/
    public boolean getSwtichValue(){
    	return mSwtichValue;
    }
    
    public interface SwitchListener{
    	void onSwitch(boolean value);
    }
}
