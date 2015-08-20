package com.ef.bite.model;

import com.ef.bite.utils.JsonSerializeHelper;

public class RatingModel extends BaseJsonModel {
	public String date;
	public boolean isShow;
	public int daysNum;

	@Override
	public void parse(String json) {
		// TODO Auto-generated method stub
		RatingModel ratingModel = (RatingModel) JsonSerializeHelper
				.JsonDeserialize(json, RatingModel.class);
		this.date = ratingModel.date;
		this.isShow = ratingModel.isShow;
		this.daysNum = ratingModel.daysNum;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return JsonSerializeHelper.JsonSerializer(this);
	}

}
