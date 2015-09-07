package com.ef.bite.model;

import com.ef.bite.utils.JsonSerializeHelper;

public class ExistedLogin extends BaseJsonModel {

	public boolean IsLogin;

	public String UId;

//	public String Token;

//	public String UserName;

	public String Password;

	public String Alias;

	public String Avatar;

	public String FirstName;

	public String LastName;

	public String Phone;

	public String Email;

	public String Location;

	public String RegisterDate;

	public int Score;

	public int Level;

    public String Plan_ID;


	@Override
	public void parse(String json) {
		try {
			ExistedLogin login = (ExistedLogin) JsonSerializeHelper
					.JsonDeserialize(json, ExistedLogin.class);
//			this.UserName = login.UserName;
			this.Password = login.Password;
			this.IsLogin = login.IsLogin;
			this.UId = login.UId;
//			this.Token = login.Token;
			this.Avatar = login.Avatar;
			this.Alias = login.Alias;
			this.FirstName = login.FirstName;
			this.LastName = login.LastName;
			this.Phone = login.Phone;
			this.Email = login.Email;
			this.Location = login.Location;
			this.RegisterDate = login.RegisterDate;
            this.Plan_ID = login.Plan_ID;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String toJson() {
		return JsonSerializeHelper.JsonSerializer(this);
	}

}
