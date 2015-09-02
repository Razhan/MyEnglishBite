package com.ef.bite.widget;

import android.content.Context;
import com.ef.bite.R;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginInputLayout extends LinearLayout {

	EditText mEdit;
	ImageView mCancel;
	TextView mError;
	
	public LoginInputLayout(Context context) {
		super(context);
		getViews();
	}
	
	public LoginInputLayout(Context context,  AttributeSet attrs) {
		super(context, attrs);
		getViews();
	}
	
	public LoginInputLayout(Context context,  AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getViews();
	}

	private void getViews(){
		inflate(getContext(), R.layout.widget_login_input, this);
		mEdit = (EditText) findViewById(R.id.widget_login_edittext);
		mCancel = (ImageView) findViewById(R.id.widget_login_cancel);
		mError = (TextView) findViewById(R.id.widget_login_error);
	}
	
	/**
	 * 初始化
	 */
	public void initialize(String hint, int inputType, boolean isFocus){
		mEdit.setHint(hint);
		mEdit.setInputType(inputType);
		if(isFocus)
			mEdit.requestFocus();
		mCancel.setVisibility(View.GONE);
		mEdit.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				String input = s.toString();
				if(input==null || input.isEmpty())
					mCancel.setVisibility(View.GONE);
				else{
					mCancel.setVisibility(View.VISIBLE);
					cleanError();
				}
			}
		});
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEdit.setText("");
				mCancel.setVisibility(View.GONE);
				mEdit.requestFocus();
			}
		});
	}

    public void initializeWithFriend(String hint, int inputType, boolean isFocus, final View view){
        mEdit.setHint(hint);
        mEdit.setInputType(inputType);
        if(isFocus)
            mEdit.requestFocus();
        mCancel.setVisibility(View.GONE);
        mEdit.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(input==null || input.isEmpty()) {
                    mCancel.setVisibility(View.GONE);
                    view.setClickable(true);
                }
                else{
                    mCancel.setVisibility(View.VISIBLE);
                    cleanError();
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setText("");
                mCancel.setVisibility(View.GONE);
                mEdit.requestFocus();
            }
        });
    }
	
	/**
	 * 获得输入的文本
	 * @return
	 */
	public String getInputString(){
		return mEdit.getText().toString();
	}
	
	/**
	 * 设置输入文本
	 * @param text
	 */
	public void setInputString(String text){
		if(text!=null)
			mEdit.setText(text);
	}
	
	/**
	 * 设置错误显示内容
	 * @param error
	 */
	public void setError(String error){
		mError.setText(error);
		mError.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 清空错误内容
	 */
	public void cleanError(){
		mError.setVisibility(View.INVISIBLE);
	}

}
