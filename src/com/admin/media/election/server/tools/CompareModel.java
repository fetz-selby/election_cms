package com.admin.media.election.server.tools;

public class CompareModel {
	private int constituencyId, partyId, votes;
	public CompareModel(int constituencyId, int partyId, int votes){
		this.constituencyId = constituencyId;
		this.partyId = partyId;
		this.votes = votes;
	}
	public int getConstituencyId() {
		return constituencyId;
	}
	public void setConstituencyId(int constituencyId) {
		this.constituencyId = constituencyId;
	}
	public int getPartyId() {
		return partyId;
	}
	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
}
