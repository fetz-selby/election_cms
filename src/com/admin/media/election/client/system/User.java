package com.admin.media.election.client.system;

import com.admin.media.election.shared.UserModel;

public class User {
	private static User user = new User();
	private static UserModel model;
	private User(){}
	
	public static User getUserInstance(UserModel model){
		if(User.model == null){
			User.model = model;
		}
		return user;
	}
	
	public static User getInstance(){
		return user;
	}
	
	public int getUserId(){
		return model.getId();
	}
	
	public String getUserName(){
		return model.getUsername();
	}
	
	public String getLevel(){
		return model.getLevel();
	}
	
	public String getEmail(){
		return model.getEmail();
	}
	
	public UserModel getUserModel(){
		return model;
	}
	
}
