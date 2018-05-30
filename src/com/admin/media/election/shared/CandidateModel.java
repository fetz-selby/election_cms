package com.admin.media.election.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CandidateModel implements IsSerializable{
	private int id, partyId, votes, constituencyId;
	private String name, avatar, group, year, party, constituency;
	
	public CandidateModel(){}
	
	public CandidateModel(int id, String name, int partyId, int constituencyId, int votes, String avatar, String group, String year, String party, String constituency){
		this.id = id;
		this.partyId = partyId;
		this.votes = votes;
		this.constituencyId = constituencyId;
		this.name = name;
		this.avatar = avatar;
		this.group = group;
		this.year = year;
		this.party = party;
		this.constituency = constituency;
	}

	public int getId() {
		return id;
	}

	public int getPartyId() {
		return partyId;
	}

	public int getVotes() {
		return votes;
	}

	public int getConstituencyId() {
		return constituencyId;
	}

	public String getName() {
		return name;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getGroup() {
		return group;
	}

	public String getYear() {
		return year;
	}

	public String getParty() {
		return party;
	}

	public String getConstituency() {
		return constituency;
	}

}
