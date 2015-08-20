package com.ef.bite.ui.user;

import java.util.ArrayList;
import com.ef.bite.R;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.BaseListAdapter;
import com.ef.bite.dataacces.ConfigSharedStorage;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.widget.ActionbarLayout;

public class LanguageSettingActivity extends BaseActivity {

	ActionbarLayout mActionbar;
	ListView mLanguageListView;
	List<LanguageItem> mLanguageList = new ArrayList<LanguageItem>();
	LanguageListAdapter mAdapter;
	ConfigSharedStorage mConfigStorage = null;
	ConfigModel configModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_language);
		mConfigStorage = new ConfigSharedStorage(mContext);
		mActionbar = (ActionbarLayout) findViewById(R.id.language_settings_actionbar);
		mLanguageListView = (ListView) findViewById(R.id.language_settings_listview);
		mActionbar.initiWithTitle(
				getString(R.string.settings_language_edit_title),
				R.drawable.arrow_goback_black, -1, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.activity_in_from_left,
								R.anim.activity_out_to_right);
					}
				}, null);
		configModel = mConfigStorage.get();
		getLanguageList(configModel);
		mAdapter = new LanguageListAdapter(mContext, mLanguageList);
		mLanguageListView.setAdapter(mAdapter);
		mLanguageListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LanguageItem selectItem = mLanguageList.get(position);
				if (!selectItem.IsSelected) {
					if (configModel == null) {
						configModel = new ConfigModel();
					}
					configModel.MultiLanguageType = position;
					mConfigStorage.put(configModel);
					AppConst.GlobalConfig.LanguageType = position;
					getLanguageList(configModel);
					mAdapter.notifyDataSetChanged();
					// 目标是中文
					if (position == AppConst.MultiLanguageType.Chinese
							&& !AppLanguageHelper.getSystemLaunguage(
									getApplicationContext()).equals(
									AppLanguageHelper.ZH_CN)) {
						AppLanguageHelper.updateLaunguage(
								getApplicationContext(),
								AppLanguageHelper.ZH_CN);
					} else if (position == AppConst.MultiLanguageType.English
							&& !AppLanguageHelper.getSystemLaunguage(
									getApplicationContext()).equals(
									AppLanguageHelper.EN)) { // 英文
						AppLanguageHelper.updateLaunguage(
								getApplicationContext(), AppLanguageHelper.EN);
					} else { // 系统
						AppLanguageHelper.updateLaunguage(
								getApplicationContext(),
								AppLanguageHelper.getSystemLanguage());
					}
					Toast.makeText(
							mContext,
							getString(R.string.settings_language_update_success),
							Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});

		// add omniture
		MobclickTracking.OmnitureTrack
				.AnalyticsTrackState(
						ContextDataMode.SettingsSystemLanguageValues.pageNameValue,
						ContextDataMode.SettingsSystemLanguageValues.pageSiteSubSectionValue,
						ContextDataMode.SettingsSystemLanguageValues.pageSiteSectionValue,
						mContext);
//		MobclickTracking.UmengTrack
//				.setPageStart(
//						ContextDataMode.SettingsSystemLanguageValues.pageNameValue,
//						ContextDataMode.SettingsSystemLanguageValues.pageSiteSubSectionValue,
//						ContextDataMode.SettingsSystemLanguageValues.pageSiteSectionValue,
//						mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.SettingsSystemLanguageValues.pageNameValue,
//						ContextDataMode.SettingsSystemLanguageValues.pageSiteSubSectionValue,
//						ContextDataMode.SettingsSystemLanguageValues.pageSiteSectionValue,
//						mContext);
	}

	private void getLanguageList(ConfigModel configModel) {
		mLanguageList.clear();
		mLanguageList.add(new LanguageItem(
				getString(R.string.settings_language_default), true));
		mLanguageList.add(new LanguageItem(
				getString(R.string.settings_language_english), false));
		mLanguageList.add(new LanguageItem(
				getString(R.string.settings_language_chinese), false));
		if (configModel == null
				|| configModel.MultiLanguageType == AppConst.MultiLanguageType.Default)
			mLanguageList.get(0).IsSelected = true;
		else
			mLanguageList.get(0).IsSelected = false;
		if (configModel.MultiLanguageType == AppConst.MultiLanguageType.English)
			mLanguageList.get(1).IsSelected = true;
		else
			mLanguageList.get(1).IsSelected = false;
		if (configModel.MultiLanguageType == AppConst.MultiLanguageType.Chinese)
			mLanguageList.get(2).IsSelected = true;
		else
			mLanguageList.get(2).IsSelected = false;
	}

	public class LanguageItem {

		public LanguageItem(String lang, boolean isSelect) {
			Language = lang;
			IsSelected = isSelect;
		}

		public String Language;

		public boolean IsSelected;
	}

	/**
	 * 
	 * @author Allen.Zhu
	 * 
	 */
	public class LanguageListAdapter extends BaseListAdapter<LanguageItem> {

		public LanguageListAdapter(Context context, List<LanguageItem> dataList) {
			super(context, R.layout.settings_language_list_item, dataList);
		}

		@Override
		public void getView(View layout, int position, LanguageItem data) {
			ViewHolder holder = null;
			if (layout.getTag() == null) {
				holder = new ViewHolder();
				holder.name = (TextView) layout
						.findViewById(R.id.language_settings_list_name);
				holder.select = (ImageView) layout
						.findViewById(R.id.language_settings_list_select);
			} else
				holder = (ViewHolder) layout.getTag();
			holder.name.setText(data.Language);
			if (data.IsSelected)
				holder.select.setVisibility(View.VISIBLE);
			else
				holder.select.setVisibility(View.GONE);
		}

		class ViewHolder {
			TextView name;
			ImageView select;
		}

	}

}
