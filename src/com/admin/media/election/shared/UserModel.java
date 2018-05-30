package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserModel implements IsSerializable{
	private String username, level, email, msisdn;
	private int id;
	
	public UserModel(){}
	public UserModel(int id, String username, String level, String email, String msisdn){
		this.id = id;
		this.username = username;
		this.level = level;
		this.email = email;
		this.msisdn = msisdn;
	}

	public String getUsername() {
		return username;
	}

	public String getLevel() {
		return level;
	}
	
	public String getEmail(){
		return email;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
