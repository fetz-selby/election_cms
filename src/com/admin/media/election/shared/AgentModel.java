package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgentModel implements IsSerializable{
	private String name, msisdn, pollName, constituencyName, year, status, code;
	private int id, pollId, consId;
	
	public AgentModel(){}
	public AgentModel(int id, int poll_id, int consId, String msisdn, String name, String pollName, String constituencyName, String year, String status, String code){
		this.id = id;
		this.pollId = poll_id;
		this.consId = consId;
		this.msisdn = msisdn;
		this.name = name;
		this.pollName = pollName;
		this.constituencyName = constituencyName;
		this.year = year;
		this.status = status;
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getPollName() {
		return pollName;
	}
	public void setPollName(String pollName) {
		this.pollName = pollName;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPoll_id(int poll_id) {
		this.pollId = poll_id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getConsId() {
		return consId;
	}
	public void setConsId(int consId) {
		this.consId = consId;
	}
	
}
