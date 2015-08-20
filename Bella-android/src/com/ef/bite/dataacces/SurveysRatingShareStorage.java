package com.ef.bite.dataacces;

import android.content.Context;

import com.ef.bite.model.RatingModel;

public class SurveysRatingShareStorage extends BaseSharedStorage<RatingModel> {

	public SurveysRatingShareStorage(Context context) {
		super(context,"Local_Surveys_Rating");
		// TODO Auto-generated constructor stub
		mPreferenceKey = "local_surveys_rating";
	}

	@Override
	public RatingModel get() {
		// TODO Auto-generated method stub
		try {
			String ratingJson = mSharedPreference.getString(mPreferenceKey,
					null);
			if (ratingJson == null || ratingJson.isEmpty())
				return null;
			RatingModel ratingModel = new RatingModel();
			ratingModel.parse(ratingJson);
			return ratingModel;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(RatingModel t) {
		// TODO Auto-generated method stub
		try {
			if (t == null)
				return;
			String ratingJson = t.toJson();
			mEditor.putString(mPreferenceKey, ratingJson);
			mEditor.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
