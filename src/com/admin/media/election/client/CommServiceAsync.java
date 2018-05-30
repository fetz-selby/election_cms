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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommServiceAsync {

	void getUserModel(AsyncCallback<UserModel> callback);

	void isUserValid(String username, String password,
			AsyncCallback<Boolean> callback);


	void getCandidates(AsyncCallback<ArrayList<CandidateModel>> callback);

	void getAllParentConstituencies(
			AsyncCallback<HashMap<Integer, String>> callback);

	void getAllParties(AsyncCallback<HashMap<Integer, String>> callback);

	void getAllYears(AsyncCallback<HashMap<Integer, String>> callback);

	void getSaveConstituency(ConstituencyModel model,
			AsyncCallback<Integer> callback);

	void getConstituencies(String year, String isDeclared,
			AsyncCallback<ArrayList<ConstituencyModel>> callback);

	void getConstituencyUpdate(ConstituencyModel model,
			AsyncCallback<Integer> callback);

	void getAllConstituencies(String year,
			AsyncCallback<HashMap<Integer, String>> callback);

	void getCandidates(int constituencyId, String year,
			AsyncCallback<ArrayList<CandidateModel>> callback);

	void removeAgent(int id, AsyncCallback<Boolean> callback);

	void removeConstituency(int id, AsyncCallback<Boolean> callback);

	void getParties(AsyncCallback<ArrayList<PartyModel>> callback);

	void removeParty(int id, AsyncCallback<Boolean> callback);

	void isSeatSettingDone(AsyncCallback<Boolean> callback);

	void isNameShorteningDone(AsyncCallback<Boolean> callback);

	void getSavePoll(PollModel model, AsyncCallback<Integer> callback);

	void getPollUpdate(PollModel model, AsyncCallback<Integer> callback);

	void removePoll(int id, AsyncCallback<Boolean> callback);

	void getPolls(int constituencyId, String year,
			AsyncCallback<ArrayList<PollModel>> callback);

	void getSaveAgent(AgentModel model, AsyncCallback<Integer> callback);

	void getAgentUpdate(AgentModel model, AsyncCallback<Integer> callback);

	void getAgents(int constituencyId, String year,
			AsyncCallback<ArrayList<AgentModel>> callback);

	void getAgentsWithPollId(int constituencyId, int pollId, String year,
			AsyncCallback<ArrayList<AgentModel>> callback);

	void getAllPollingStations(int constituencyId,
			AsyncCallback<HashMap<Integer, String>> callback);

	void getInbounds(int constituencyId, String year,
			AsyncCallback<ArrayList<InboundModel>> callback);

	void getInboundsWithPollId(int constituencyId, int pollId, String year,
			AsyncCallback<ArrayList<InboundModel>> callback);

	void getInboundUpdate(InboundModel model, int userId,
			AsyncCallback<Integer> callback);

	void isAlreadyApproved(InboundModel model, AsyncCallback<Boolean> callback);

	void rejectAllPollsExcept(int inboundId, int pollId, int consId,
			String type, String state, AsyncCallback<Integer> callback);

	void isInboundChange(AsyncCallback<Boolean> callback);

	void removeCandidate(int id, AsyncCallback<Boolean> callback);

	void isAppValid(AsyncCallback<Double> callback);

}
