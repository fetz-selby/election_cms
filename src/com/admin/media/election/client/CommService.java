package com.admin.media.election.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.shared.AgentModel;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.ConstituencyModel;
import com.admin.media.election.shared.InboundModel;
import com.admin.media.election.shared.PartyModel;
import com.admin.media.election.shared.PollModel;
import com.admin.media.election.shared.UserModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("exchange")
public interface CommService extends RemoteService{
	double isAppValid();
	boolean isUserValid(String username, String password);
	UserModel getUserModel();
	ArrayList<CandidateModel> getCandidates();
	ArrayList<PartyModel> getParties();
	ArrayList<CandidateModel> getCandidates(int constituencyId, String year);
	ArrayList<PollModel> getPolls(int constituencyId, String year);
	ArrayList<AgentModel> getAgents(int constituencyId, String year);
	ArrayList<AgentModel> getAgentsWithPollId(int constituencyId, int pollId, String year);
	ArrayList<InboundModel> getInbounds(int constituencyId, String year);
	ArrayList<InboundModel> getInboundsWithPollId(int constituencyId, int pollId, String year);
	ArrayList<ConstituencyModel> getConstituencies(String year, String isDeclared);
	HashMap<Integer, String> getAllParentConstituencies();
	HashMap<Integer, String> getAllConstituencies(String year);
	HashMap<Integer, String> getAllPollingStations(int constituencyId);
	HashMap<Integer, String> getAllParties();
	HashMap<Integer, String> getAllYears();
	int getSaveConstituency(ConstituencyModel model);
	int getConstituencyUpdate(ConstituencyModel model);
	int getSavePoll(PollModel model);
	int getSaveAgent(AgentModel model);
	int getPollUpdate(PollModel model);
	int getAgentUpdate(AgentModel model);
	int getInboundUpdate(InboundModel model, int userId);
	boolean removeCandidate(int id);
	boolean removePoll(int id);
	boolean removeAgent(int id);
	boolean removeConstituency(int id);
	boolean removeParty(int id);
	boolean isSeatSettingDone();
	boolean isNameShorteningDone();
	boolean isAlreadyApproved(InboundModel model);
	
	boolean isInboundChange();
	
	int rejectAllPollsExcept(int inboundId, int pollId, int consId, String type, String state);
}
