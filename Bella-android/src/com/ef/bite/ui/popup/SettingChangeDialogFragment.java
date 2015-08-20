package com.ef.bite.ui.popup;

import android.app.Activity;
import com.ef.bite.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.PostProfileTask;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * 设置项值修改的dialog
 * 
 * @author Allen.Zhu
 * 
 */
public class SettingChangeDialogFragment extends BaseDialogFragment {

	public final static int SETTINGS_NICKNAME = 0;
	public final static int SETTINGS_FIRST_NAME = 1;
	public final static int SETTINGS_LAST_NAME = 2;
	public final static int SETTINGS_EMAIL = 3;
	public final static int SETTINGS_PHONE = 4;
	public final static int SETTINGS_LOCATION = 5;

	private String mTitle;
	private String mItemValue;
	private AfterItemChangeListener mChangeListener;
	private int mChangeItemID;

	public SettingChangeDialogFragment(Activity activity, int itemId,
			String itemName, String itemValue,
			AfterItemChangeListener changeListener) {
		super(activity);
		mTitle = JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"settings_edit_item_title") + "\"" + itemName + "\"";
		mItemValue = itemValue;
		mChangeListener = changeListener;
		mChangeItemID = itemId;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mBuilder.setTitle(mTitle);
		final EditText edit = new EditText(mActivity);
		edit.setText(mItemValue);
		this.mBuilder.setView(edit);
		mBuilder.setPositiveButton(
				JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
						"settings_logout_dialog_ok"),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean is_to_update = true;
						final String text = edit.getText().toString().trim();
						if (text == null || text.isEmpty()) {
							Toast.makeText(
									mActivity,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mActivity,
													"settings_item_value_null"),
									Toast.LENGTH_SHORT).show();
							return;
						}
						final HttpProfile.ProfileData httpProfile = new HttpProfile.ProfileData();
						httpProfile.bella_id = AppConst.CurrUserInfo.UserId;
						if (mChangeItemID == SETTINGS_NICKNAME) {
							httpProfile.alias = text;
						} else if (mChangeItemID == SETTINGS_FIRST_NAME) {
							httpProfile.given_name = text;
						} else if (mChangeItemID == SETTINGS_LAST_NAME) {
							httpProfile.family_name = text;
						} else if (mChangeItemID == SETTINGS_PHONE) {
							httpProfile.phone = text;
						} else
							is_to_update = false;
						if (is_to_update) {
							PostProfileTask task = new PostProfileTask(
									mActivity,
									new PostExecuting<HttpBaseMessage>() {
										@Override
										public void executing(
												HttpBaseMessage result) {
											if (result != null
													&& result.status != null) {
												if (result.status.equals("0")) {
													if (mChangeListener != null)
														mChangeListener
																.afterChange(text);
												} else
													Toast.makeText(mActivity,
															result.message,
															Toast.LENGTH_SHORT)
															.show();
											} else
												Toast.makeText(
														mActivity,
														JsonSerializeHelper
																.JsonLanguageDeserialize(
																		mActivity,
																		"settings_item_fail_to_edit"),
														Toast.LENGTH_SHORT)
														.show();
										}
									});
							task.execute(new HttpProfile.ProfileData[] { httpProfile });
						} else
							Toast.makeText(
									mActivity,
									JsonSerializeHelper
											.JsonLanguageDeserialize(mActivity,
													"settings_select_wrong_item"),
									Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton(
				JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
						"settings_logout_dialog_cancel"),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		return mBuilder.create();
	}

	public interface AfterItemChangeListener {
		void afterChange(String changed);
	}

}
