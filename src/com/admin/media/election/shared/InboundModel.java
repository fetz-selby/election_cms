package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InboundModel implements IsSerializable{
	private String typeString,msisdn,time,message,sender,approveState,poll,constituency,type;
	private int id,pollId,consId,approvedById;
	
	public InboundModel(){}
	
	public InboundModel(int id, int pollId, int consId, String typeString, String type, String msisdn, String time, String message, String sender, String poll, String approveState, String constituency){
		this.id = id;
		this.pollId = pollId;
		this.consId = consId;
		this.typeString = typeString;
		this.type = type;
		this.msisdn = msisdn;
		this.time = time;
		this.message = message;
		this.sender = sender;
		this.approveState = approveState;
		this.poll = poll;
		this.constituency = constituency;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getApproveState() {
		return approveState;
	}

	public void setApproveState(String approveState) {
		this.approveState = approveState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPoll() {
		return poll;
	}

	public void setPoll(String poll) {
		this.poll = poll;
	}

	public String getConstituency() {
		return constituency;
	}

	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPollId() {
		return pollId;
	}

	public void setPollId(int pollId) {
		this.pollId = pollId;
	}

	public int getConsId() {
		return consId;
	}

	public void setConsId(int consId) {
		this.consId = consId;
	}

	public int getApprovedById() {
		return approvedById;
	}

	public void setApprovedById(int approvedById) {
		this.approvedById = approvedById;
	}
	
}
