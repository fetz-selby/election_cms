package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PollModel implements IsSerializable{
	private String name, year, constituencyName, status;
	private int id, constituencyId;
	
	public PollModel(){}
	
	public PollModel(int id, String name, int constituencyId, String constituencyName, String year, String status){
		this.id = id;
		this.name = name;
		this.constituencyId = constituencyId;
		this.constituencyName = constituencyName;
		this.year = year;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConstituencyId() {
		return constituencyId;
	}

	public void setConstituencyId(int constituencyId) {
		this.constituencyId = constituencyId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getConstituencyName() {
		return constituencyName;
	}

	public void setConstituencyName(String constituencyName) {
		this.constituencyName = constituencyName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
