package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConstituencyModel implements IsSerializable{
	private int id, year, regVotes, rejVotes, totalVotes, castedVotes, parentId, seatWonId;
	private String name, isDeclared, seatWonName;
	
	public ConstituencyModel(){}
	public ConstituencyModel(int id, int year, int regVotes, int rejVotes, int totalVotes, int castedVotes, int parentId, int seatWonId, String name, String isDeclared){
		this.id = id;
		this.year = year;
		this.regVotes = regVotes;
		this.rejVotes = rejVotes;
		this.totalVotes = totalVotes;
		this.castedVotes = castedVotes;
		this.parentId = parentId;
		this.seatWonId = seatWonId;
		this.name = name;
		this.isDeclared = isDeclared;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRegVotes() {
		return regVotes;
	}

	public void setRegVotes(int regVotes) {
		this.regVotes = regVotes;
	}

	public int getRejVotes() {
		return rejVotes;
	}

	public void setRejVotes(int rejVotes) {
		this.rejVotes = rejVotes;
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}

	public int getCastedVotes() {
		return castedVotes;
	}

	public void setCastedVotes(int castedVotes) {
		this.castedVotes = castedVotes;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getSeatWonId() {
		return seatWonId;
	}

	public void setSeatWonId(int seatWonId) {
		this.seatWonId = seatWonId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsDeclared() {
		return isDeclared;
	}

	public void setIsDeclared(String isDeclared) {
		this.isDeclared = isDeclared;
	}
	public String getSeatWonName() {
		return seatWonName;
	}
	public void setSeatWonName(String seatWonName) {
		this.seatWonName = seatWonName;
	}
	
}
