package com.ef.bite.ui.user;

import android.app.Activity;
import com.ef.bite.R;
import android.view.View;
import android.widget.TextView;

import com.ef.bite.ui.popup.BasePopupWindow;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * iOS样式的pop up window， 头像编辑：从系统相册选择或
 * 
 * @author Allen.Zhu
 * 
 */
public class AvataEditorPopupWindow extends BasePopupWindow {

	TextView mGallery;
	TextView mCamera;
	TextView mCancel;
	TextView popup_avatar_editor_title;
	View.OnClickListener mGalleryClick;
	View.OnClickListener mCameraClick;

	public AvataEditorPopupWindow(Activity activity,
			View.OnClickListener galleryClick, View.OnClickListener cameraClick) {
		super(activity, R.layout.popup_avatar_editor);
		mGalleryClick = galleryClick;
		mCameraClick = cameraClick;
	}

	@Override
	protected void initViews(View layout) {
		popup_avatar_editor_title = (TextView) layout
				.findViewById(R.id.popup_avatar_editor_title);
		popup_avatar_editor_title.setText(JsonSerializeHelper
				.JsonLanguageDeserialize(mActivity,
						"settings_avatar_edit_title"));
		mGallery = (TextView) layout
				.findViewById(R.id.popup_avatar_editor_gallary);
		mGallery.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"settings_avatar_take_from_camera"));
		mCamera = (TextView) layout
				.findViewById(R.id.popup_avatar_editor_camera);
		mCamera.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"settings_avatar_take_from_gallery"));
		mCancel = (TextView) layout
				.findViewById(R.id.popup_avatar_editor_cancel);
		mCancel.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"settings_avatar_edit_cancel"));
		mGallery.setOnClickListener(mGalleryClick);
		mCamera.setOnClickListener(mCameraClick);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AvataEditorPopupWindow.this.close();
			}
		});
	}
}
