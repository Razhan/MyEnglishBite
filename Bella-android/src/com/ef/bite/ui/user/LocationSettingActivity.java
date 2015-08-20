package com.ef.bite.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.BaseListAdapter;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.business.task.PostProfileTask;
import com.ef.bite.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.bite.dataacces.mode.httpMode.HttpProfile;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.LocationHelper;
import com.ef.bite.widget.ActionbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationSettingActivity extends BaseActivity {

	ActionbarLayout mActionbar;
	ListView mLocationListView;
	ProgressDialog mProgress;
	LocationListAdapter mLocationAdapter;
	List<LocationItem> mLocationList = new ArrayList<LocationItem>();
	int selectPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_location);
		mActionbar = (ActionbarLayout) findViewById(R.id.location_settings_actionbar);
		mLocationListView = (ListView) findViewById(R.id.location_settings_listview);
		mActionbar.initiWithTitle(
				getString(R.string.settings_location_edit_title),
				R.drawable.arrow_goback_black, 0, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.activity_in_from_left,
								R.anim.activity_out_to_right);
					}
				}, null);
		loadCountryList();
		loadListView();

		// add Omniture
		MobclickTracking.OmnitureTrack.AnalyticsTrackState(
				ContextDataMode.SettingsLocationValues.pageNameValue,
				ContextDataMode.SettingsLocationValues.pageSiteSubSectionValue,
				ContextDataMode.SettingsLocationValues.pageSiteSectionValue,
				mContext);
//		MobclickTracking.UmengTrack.setPageStart(
//				ContextDataMode.SettingsLocationValues.pageNameValue,
//				ContextDataMode.SettingsLocationValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsLocationValues.pageSiteSectionValue,
//				mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack.setPageEnd(
//				ContextDataMode.SettingsLocationValues.pageNameValue,
//				ContextDataMode.SettingsLocationValues.pageSiteSubSectionValue,
//				ContextDataMode.SettingsLocationValues.pageSiteSectionValue,
//				mContext);
	}

	/**
	 * 加载国家列表
	 */
	private void loadCountryList() {
		Map<String, String> countryMap = LocationHelper.loadCountryJson(
				mContext, AppLanguageHelper.getSystemLaunguage(mContext));
		int index = 0;
		for (Map.Entry<String, String> entry : countryMap.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			boolean isSelect = false;
			if (AppConst.CurrUserInfo.Location != null)
				isSelect = key.toLowerCase().equals(
						AppConst.CurrUserInfo.Location.toLowerCase());
			if (isSelect)
				selectPosition = index;
			LocationItem item = new LocationItem(key, value, isSelect);
			mLocationList.add(item);
			index++;
		}
	}

	private void loadListView() {
		mLocationAdapter = new LocationListAdapter(mContext, mLocationList);
		mLocationListView.setAdapter(mLocationAdapter);
		mLocationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocationItem selectItem = mLocationList.get(position);
				if (selectPosition != position) {
					attempChangeCountry(position,
							selectItem.CountryCode.toLowerCase());
				}
			}
		});
	}

	/***
	 * 更新国家代码
	 * 
	 * @param position
	 * @param country_code
	 */
	private void attempChangeCountry(final int position,
			final String country_code) {
		mProgress = new ProgressDialog(this);
		mProgress
				.setMessage(getString(R.string.settings_location_progress_changing));
		mProgress.show();
		HttpProfile.ProfileData httpProfile = new HttpProfile.ProfileData();
		httpProfile.bella_id = AppConst.CurrUserInfo.UserId;
		PostProfileTask task = new PostProfileTask(mContext,
				new PostExecuting<HttpBaseMessage>() {
					@Override
					public void executing(HttpBaseMessage result) {
						mProgress.dismiss();
						if (result != null && result.status != null) {
							if (result.status.equals("0")) {
								Toast.makeText(
										mContext,
										getString(R.string.settings_location_change_success),
										Toast.LENGTH_SHORT).show();
								LocationItem originItem = mLocationList
										.get(selectPosition);
								LocationItem newItem = mLocationList
										.get(position);
								originItem.IsSelected = false;
								newItem.IsSelected = true;
								selectPosition = position;
								AppConst.CurrUserInfo.Location = country_code
										.toLowerCase();
								profileCache.save();

								loadListView();
							} else
								Toast.makeText(mContext, result.message,
										Toast.LENGTH_SHORT).show();
						} else
							Toast.makeText(mContext,
									getString(R.string.fail_to_get_result),
									Toast.LENGTH_SHORT).show();
					}
				});
		task.execute(new HttpProfile.ProfileData[] { httpProfile });
	}

	public class LocationItem {

		public LocationItem(String country_code, String country,
				boolean isSelected) {
			this.Country = country;
			this.IsSelected = isSelected;
			this.CountryCode = country_code;
		}

		public String Country;

		public String CountryCode;

		public boolean IsSelected;
	}

	public class LocationListAdapter extends BaseListAdapter<LocationItem> {

		public LocationListAdapter(Context context, List<LocationItem> dataList) {
			super(context, R.layout.settings_language_list_item, dataList);
		}

		@Override
		public void getView(View layout, int position, LocationItem data) {
			ViewHolder holder = null;
			if (layout.getTag() == null) {
				holder = new ViewHolder();
				holder.name = (TextView) layout
						.findViewById(R.id.language_settings_list_name);
				holder.select = (ImageView) layout
						.findViewById(R.id.language_settings_list_select);
			} else
				holder = (ViewHolder) layout.getTag();
			holder.name.setText(data.Country);
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
