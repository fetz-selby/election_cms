package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PartyModel implements IsSerializable{
	private String partyName, url, color;
	private int id;
	
	public PartyModel(){}
	public PartyModel(int id, String partyName, String url, String color){
		this.id = id;
		this.partyName = partyName;
		this.url = url;
		this.color = color;
	}
	
	public String getPartyName() {
		return partyName;
	}
	public String getUrl() {
		return url;
	}
	public String getColor() {
		return color;
	}
	public int getId() {
		return id;
	}
	
	
}
