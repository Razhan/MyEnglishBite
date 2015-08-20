package com.ef.bite.dataacces;

import android.content.Context;

import com.ef.bite.model.TutorialConfig;
import com.ef.bite.utils.JsonSerializeHelper;

public class TutorialConfigSharedStorage extends
		BaseSharedStorage<TutorialConfig> {

	public TutorialConfigSharedStorage(Context context, String uid) {
		super(context, "Tutorial_Config");
		mPreferenceKey = "tutorial_config_" + uid;
	}

	@Override
	public TutorialConfig get() {
		try {
			String json = mSharedPreference.getString(mPreferenceKey, null);
			if (json != null) {
				Object obj = JsonSerializeHelper.JsonDeserialize(json,
						TutorialConfig.class);
				return obj != null ? (TutorialConfig) obj : null;
			} else
				return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(TutorialConfig t) {
		try {
			String json = JsonSerializeHelper.JsonSerializer(t);
			if (json != null) {
				mEditor.putString(mPreferenceKey, json);
				mEditor.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
