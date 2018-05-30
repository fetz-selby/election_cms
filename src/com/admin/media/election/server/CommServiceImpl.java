package com.admin.media.election.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.CommService;
import com.admin.media.election.server.tools.ElectionNameShortener;
import com.admin.media.election.server.tools.ElectionNameShortener.ElectionNameShortenerEventHandler;
import com.admin.media.election.server.tools.SeatSetter;
import com.admin.media.election.server.tools.SeatSetter.SeatSetterEventHandler;
import com.admin.media.election.server.utils.Utils;
import com.admin.media.election.shared.AgentModel;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.Constants;
import com.admin.media.election.shared.ConstituencyModel;
import com.admin.media.election.shared.InboundModel;
import com.admin.media.election.shared.PartyModel;
import com.admin.media.election.shared.PollModel;
import com.admin.media.election.shared.UserModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

@SuppressWarnings("serial")
public class CommServiceImpl extends RemoteServiceServlet implements CommService{
	private Connection con = DBConnection.getConnection();
	private UserModel tmpModel;
	private ArrayList<Integer> inboundsIdList = new ArrayList<Integer>();
	private boolean isSeatSettingSuccess = false, isNameShorteningSuccess = false;

	@Override
	public boolean isUserValid(String username, String password) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name,level,email,msisdn from users where email = ? and password = ?");
			prstmt.setString(1, username);
			prstmt.setString(2, Utils.getSHA256(password));
			System.out.println("hash => "+Utils.getSHA256(password));

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				while(results.next()){
					int id = results.getInt("id");
					String user = results.getString("name");
					String level = results.getString("level");
					String email = results.getString("email");
					String msisdn = results.getString("msisdn");

					System.out.println("id "+id+", name "+user+", level "+level+", email "+email+", msisdn "+msisdn);

					tmpModel = new UserModel(id, user, level, email, msisdn);
					return true;
				}
				con.close();
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public UserModel getUserModel() {
		return tmpModel;
	}

	@Override
	public ArrayList<CandidateModel> getCandidates() {
		Statement stmt = null;
		//		try{
		//			stmt = (Statement) con.createStatement();
		//			ResultSet results = stmt.executeQuery("");
		//			
		//			if(results != null){
		//				ArrayList<CandidateModel> orphans = new ArrayList<CandidateModel>();
		//				while(results.next()){
		//					int id = results.getInt("id");
		//					int age = results.getInt("age");
		//					String name = results.getString("name");
		//					String gender = results.getString("gender");
		//					String motherName = results.getString("m_name");
		//					String fatherName = results.getString("f_name");
		//					String regDate = results.getString("reg_ts");
		//					String birthDate = results.getString("birth_date");
		//					
		//					CandidateModel orphan = new CandidateModel(id, age, name, gender, motherName, fatherName, regDate, birthDate);
		//					orphans.add(orphan);
		//				}
		//				return orphans;
		//			}
		//		}catch(SQLException sql){
		//			sql.printStackTrace();
		//		}
		return null;
	}

	@Override
	public HashMap<Integer, String> getAllParentConstituencies() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet results = stmt.executeQuery("select id, name from parent_constituencies");
			if(results != null){
				HashMap<Integer, String> consMap = new HashMap<Integer, String>();
				while(results.next()){
					int id = results.getInt("id");
					String name = results.getString("name");

					consMap.put(id, name);
				}

				return consMap;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public HashMap<Integer, String> getAllParties() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet results = stmt.executeQuery("select id, name from parties");
			if(results != null){
				HashMap<Integer, String> partyMap = new HashMap<Integer, String>();
				while(results.next()){
					int id = results.getInt("id");
					String name = results.getString("name");

					partyMap.put(id, name);
				}


				return partyMap;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public HashMap<Integer, String> getAllYears() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet results = stmt.executeQuery("select value, alias from years");
			if(results != null){
				HashMap<Integer, String> yearMap = new HashMap<Integer, String>();
				while(results.next()){
					int value = results.getInt("value");
					String alias = results.getString("alias");

					yearMap.put(value, alias);
				}

				return yearMap;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public int getSaveConstituency(ConstituencyModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into constituencies (name,reg_votes,reject_votes,total_votes,casted_votes,year,is_declared,parent_id,seat_won_id,district_id) values (?,?,?,?,?,?,?,?,?,?) ");
			prstmt.setString(1, model.getName());
			prstmt.setInt(2, model.getRegVotes());
			prstmt.setInt(3, model.getRejVotes());
			prstmt.setInt(4, model.getTotalVotes());
			prstmt.setInt(5, model.getCastedVotes());
			prstmt.setInt(6, model.getYear());
			prstmt.setString(7, model.getIsDeclared());
			prstmt.setInt(8, model.getParentId());
			prstmt.setInt(9, model.getSeatWonId());
			prstmt.setInt(10, 0);

			int success = prstmt.executeUpdate();
			if(success > 0){
				con.close();
				return success;
			}

			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();

		}
		return 0;
	}

	@Override
	public ArrayList<ConstituencyModel> getConstituencies(String year,
			String isDeclared) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();


		try{
			prstmt = (PreparedStatement) con.prepareStatement("select constituencies.id,constituencies.name,constituencies.reg_votes,constituencies.reject_votes,constituencies.total_votes,constituencies.casted_votes,constituencies.year,constituencies.is_declared,constituencies.parent_id,constituencies.seat_won_id,parties.name as seat_won_name from constituencies, parties where constituencies.year = ? and constituencies.is_declared = ? and parties.id = constituencies.seat_won_id");
			prstmt.setString(1, year);
			prstmt.setString(2, isDeclared);

			ResultSet results = prstmt.executeQuery();
			if(results != null){
				ArrayList<ConstituencyModel> constituenciesList = new ArrayList<ConstituencyModel>();
				while(results.next()){
					int id = results.getInt("id");
					int regVotes = results.getInt("reg_votes");
					int rejVotes = results.getInt("reject_votes");
					int totalVotes = results.getInt("total_votes");
					int castedVotes = results.getInt("casted_votes");
					int tmpYear = results.getInt("year");
					int parentId = results.getInt("parent_id");
					int seatWonId = results.getInt("seat_won_id");
					String seatWonName = results.getString("seat_won_name");
					String tmpIsDeclared = results.getString("is_declared");
					String name = results.getString("name");

					ConstituencyModel constituency = new ConstituencyModel(id, tmpYear, regVotes, rejVotes, totalVotes, castedVotes, parentId, seatWonId, name, tmpIsDeclared);
					constituency.setSeatWonName(seatWonName);

					constituenciesList.add(constituency);
				}

				con.close();
				return constituenciesList;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public int getConstituencyUpdate(ConstituencyModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update constituencies set reg_votes = ?, reject_votes = ?, total_votes = ?, casted_votes = ?, year = ?, is_declared = ?, seat_won_id = ? where id = ?");
			prstmt.setInt(1, model.getRegVotes());
			prstmt.setInt(2, model.getRejVotes());
			prstmt.setInt(3, model.getTotalVotes());
			prstmt.setInt(4, model.getCastedVotes());
			prstmt.setString(5, model.getYear()+"");
			prstmt.setString(6, model.getIsDeclared());
			prstmt.setInt(7, model.getSeatWonId());
			prstmt.setInt(8, model.getId());

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public HashMap<Integer, String> getAllConstituencies(String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name from constituencies where year = ?");
			prstmt.setString(1, year);
			ResultSet results = prstmt.executeQuery();
			if(results != null){
				HashMap<Integer, String> constituencyMap = new HashMap<Integer, String>();
				while(results.next()){
					int id = results.getInt("id");
					String name = results.getString("name");

					constituencyMap.put(id, name);
				}
				con.close();
				return constituencyMap;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<CandidateModel> getCandidates(int constituencyId,
			String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select candidates.id,candidates.name,candidates.party_id,candidates.constituency_id,candidates.avatar,candidates.votes,candidates.group_type,candidates.year,parties.name as party,constituencies.name as constituency from candidates,parties,constituencies where constituencies.id = candidates.constituency_id and parties.id = candidates.party_id and candidates.constituency_id = ? and candidates.year = ?");
			prstmt.setInt(1, constituencyId);
			prstmt.setString(2, year);
			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<CandidateModel> candidates = new ArrayList<CandidateModel>();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("name");
					int partyId = result.getInt("party_id");
					int tmpConstituencyId = result.getInt("constituency_id");
					String avatar = getImageUrl(result.getBytes("avatar"));
					int votes = result.getInt("votes");
					String group = result.getString("group_type");
					String tmpYear = result.getString("year");
					String partyName = result.getString("party");
					String constituencyName = result.getString("constituency");

					CandidateModel candidate = new CandidateModel(id, name, partyId, tmpConstituencyId, votes, avatar, group, tmpYear, partyName, constituencyName);
					candidates.add(candidate);
				}

				con.close();
				return candidates;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	private String getImageUrl(byte[] blob){
		if(blob == null){
			return "data:image/png;base64,";
		}
		String base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(blob));
		base64 = "data:image/png;base64,"+base64;
		return base64;
	}

	@Override
	public boolean removeCandidate(int id) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("delete from agents where id = ?");
			prstmt.setInt(1, id);
			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean removeConstituency(int id) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("delete from constituencies where id = ?");
			prstmt.setInt(1, id);
			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<PartyModel> getParties() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select id,name,avatar,color from parties");
			if(result != null){
				ArrayList<PartyModel> parties = new ArrayList<PartyModel>();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("name");
					String url = getImageUrl(result.getBytes("avatar"));
					String color = result.getString("color");

					PartyModel party = new PartyModel(id, name, url, color);
					parties.add(party);
				}

				con.close();
				return parties;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean removeParty(int id) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("delete from parties where id = ?");
			prstmt.setInt(1, id);
			int success = prstmt.executeUpdate();
			if(success > 0){
				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isSeatSettingDone() {

		SeatSetter seatSetter = new SeatSetter();
		seatSetter.setSeatSetterEventHandler(new SeatSetterEventHandler() {

			@Override
			public void onSuccess() {
				isSeatSettingSuccess = true;
			}

			@Override
			public void onFailure() {
				isSeatSettingSuccess = false;
			}
		});
		seatSetter.start();

		return isSeatSettingSuccess;
	}

	@Override
	public boolean isNameShorteningDone() {
		ElectionNameShortener nameShortener = new ElectionNameShortener();
		nameShortener.setElectionNameShortenerEventHandler(new ElectionNameShortenerEventHandler() {

			@Override
			public void onSuccess() {
				isNameShorteningSuccess = true;
			}

			@Override
			public void onFailure() {
				isNameShorteningSuccess = false;
			}
		});
		nameShortener.start();
		return isNameShorteningSuccess;
	}

	@Override
	public int getSavePoll(PollModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into polls (name,cons_id,year,status) values (?,?,?,?) ");
			prstmt.setString(1, model.getName());
			prstmt.setInt(2, model.getConstituencyId());
			prstmt.setString(3, model.getYear());
			prstmt.setString(4, model.getStatus());

			int success = prstmt.executeUpdate();
			if(success > 0){
				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getPollUpdate(PollModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update polls set name = ?, year = ?, cons_id = ?, status = ? where id = ?");
			prstmt.setString(1, model.getName());
			prstmt.setString(2, model.getYear());
			prstmt.setInt(3, model.getConstituencyId());
			prstmt.setString(4, model.getStatus());
			prstmt.setInt(5, model.getId());

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean removePoll(int id) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update polls set status = 'D' where id = ?");
			prstmt.setInt(1, id);

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<PollModel> getPolls(int constituencyId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select poll.id,poll.name as poll,poll.cons_id,poll.year,cons.name as constituency from polls as poll, constituencies as cons where poll.cons_id = ? and poll.cons_id = cons.id and poll.year = ? and poll.status = ?");
			prstmt.setInt(1, constituencyId);
			prstmt.setString(2, year);
			prstmt.setString(3, "A");

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<PollModel> polls = new ArrayList<PollModel>();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("poll");
					int tmpConstituencyId = result.getInt("cons_id");
					String tmpYear = result.getString("year");
					String constituencyName = result.getString("constituency");

					PollModel poll = new PollModel(id, name, tmpConstituencyId, constituencyName, tmpYear, "A");
					polls.add(poll);
				}

				con.close();
				return polls;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public int getSaveAgent(AgentModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into agents (name,msisdn,pin,poll_id,year,status,cons_id) values (?,?,?,?,?,?,?)");
			prstmt.setString(1, model.getName());
			prstmt.setString(2, model.getMsisdn());
			prstmt.setString(3, model.getCode());
			prstmt.setInt(4, model.getPollId());
			prstmt.setString(5, model.getYear());
			prstmt.setString(6, model.getStatus());
			prstmt.setInt(7, model.getConsId());

			int success = prstmt.executeUpdate();
			if(success > 0){

				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getAgentUpdate(AgentModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update agents set name = ?, msisdn = ?, pin = ?, poll_id = ?, year = ?, status = ? where id = ?");
			prstmt.setString(1, model.getName());
			prstmt.setString(2, model.getMsisdn());
			prstmt.setString(3, model.getCode());
			prstmt.setInt(4, model.getPollId());
			prstmt.setString(5, model.getYear());
			prstmt.setString(6, model.getStatus());
			prstmt.setInt(7, model.getId());


			int success = prstmt.executeUpdate();
			if(success >= 0){

				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean removeAgent(int id) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update agents set status = 'D' where id = ?");
			prstmt.setInt(1, id);

			int success = prstmt.executeUpdate();
			if(success >= 0){

				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<AgentModel> getAgents(int constituencyId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select agent.id, agent.name, agent.msisdn, agent.pin, agent.poll_id, agent.year, agent.status, cons.name as constituency, cons.id as cons_id, poll.name as pollname from agents as agent, constituencies as cons, polls as poll where agent.cons_id = ? and agent.year = ? and poll.id = agent.poll_id and cons.id = agent.cons_id");
			prstmt.setInt(1, constituencyId);
			prstmt.setString(2, year);

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<AgentModel> agents = new ArrayList<AgentModel>();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("name");
					int tmpConstituencyId = result.getInt("cons_id");
					int pollId = result.getInt("poll_id");
					String tmpYear = result.getString("year");
					String code = result.getString("pin");
					String constituency = result.getString("constituency");
					String poll = result.getString("pollname");
					String status = result.getString("status");
					String msisdn = result.getString("msisdn");

					AgentModel model = new AgentModel(id, pollId, tmpConstituencyId, msisdn, name, poll, constituency, tmpYear, status, code);
					agents.add(model);
				}

				con.close();
				return agents;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<AgentModel> getAgentsWithPollId(int constituencyId,
			int pollId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select agent.id, agent.name, agent.msisdn, agent.pin, agent.poll_id, agent.year, agent.status, cons.name as constituency, cons.id as cons_id, poll.name as pollname from agents as agent, constituencies as cons, polls as poll where agent.cons_id = ? and agent.year = ? and poll.id = ? and poll.id = agent.poll_id and cons.id = agent.cons_id");
			prstmt.setInt(1, constituencyId);
			prstmt.setString(2, year);
			prstmt.setInt(3, pollId);

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<AgentModel> agents = new ArrayList<AgentModel>();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("name");
					int tmpConstituencyId = result.getInt("cons_id");
					int tmpPollId = result.getInt("poll_id");
					String tmpYear = result.getString("year");
					String code = result.getString("pin");
					String constituency = result.getString("constituency");
					String poll = result.getString("pollname");
					String status = result.getString("status");
					String msisdn = result.getString("msisdn");

					AgentModel model = new AgentModel(id, pollId, tmpConstituencyId, msisdn, name, poll, constituency, tmpYear, status, code);
					agents.add(model);
				}

				con.close();
				return agents;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public HashMap<Integer, String> getAllPollingStations(int constituencyId) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,name from polls where cons_id = ?");
			prstmt.setInt(1, constituencyId);
			ResultSet results = prstmt.executeQuery();
			if(results != null){
				HashMap<Integer, String> pollsMap = new HashMap<Integer, String>();
				while(results.next()){
					int id = results.getInt("id");
					String name = results.getString("name");

					pollsMap.put(id, name);
				}

				con.close();
				return pollsMap;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<InboundModel> getInbounds(int constituencyId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select inb.id,inb.agent_id,inb.agent_name,inb.msisdn,inb.approve,inb.poll_id,inb.cons_id,inb.type,inb.message,inb.time,poll.name as poll,cons.name as constituency from constituencies as cons, inbounds as inb, polls as poll where inb.cons_id = ? and inb.poll_id = poll.id and inb.cons_id = cons.id and poll.cons_id = cons.id");
			prstmt.setInt(1, constituencyId);

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<InboundModel> inbounds = new ArrayList<InboundModel>();
				while(result.next()){
					int id = result.getInt("id");
					int pollId = result.getInt("poll_id");
					int agent_id = result.getInt("agent_id");
					String agentName = result.getString("agent_name");
					String msisdn = result.getString("msisdn");
					String approve = result.getString("approve");
					String constituency = result.getString("constituency");
					String typeString = "";
					if(result.getString("type").trim().equalsIgnoreCase("m")){
						typeString = "Paliamentary";
					}else if(result.getString("type").trim().equalsIgnoreCase("p")){
						typeString = "Presidential";
					}
					String type = result.getString("type");
					String message = result.getString("message");
					String time = result.getString("time");
					String poll = result.getString("poll");

					InboundModel model = new InboundModel(id, pollId, constituencyId, typeString, type, msisdn, time, message, agentName, poll, approve, constituency);
					inbounds.add(model);

					if(!inboundsIdList.contains(id)){
						inboundsIdList.add(id);
					}
				}
				con.close();
				return inbounds;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<InboundModel> getInboundsWithPollId(int constituencyId,
			int pollId, String year) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();


		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select inb.id,inb.agent_id,inb.agent_name,inb.msisdn,inb.approve,inb.poll_id,inb.cons_id,inb.type,inb.message,inb.time,poll.name as poll,cons.name as constituency from constituencies as cons, inbounds as inb, polls as poll where inb.cons_id = ? and inb.poll_id = ? and inb.poll_id = poll.id and inb.cons_id = cons.id and poll.cons_id = cons.id");
			prstmt.setInt(1, constituencyId);
			prstmt.setInt(2, pollId);

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<InboundModel> inbounds = new ArrayList<InboundModel>();
				while(result.next()){
					int id = result.getInt("id");
					int agent_id = result.getInt("agent_id");

					String agentName = result.getString("agent_name");
					String msisdn = result.getString("msisdn");
					String approve = result.getString("approve");
					String constituency = result.getString("constituency");
					String typeString = "";
					if(result.getString("type").trim().equalsIgnoreCase("m")){
						typeString = "Paliamentary";
					}else if(result.getString("type").trim().equalsIgnoreCase("p")){
						typeString = "Presidential";
					}
					String type = result.getString("type");
					String message = result.getString("message");
					String time = result.getString("time");
					String poll = result.getString("poll");

					InboundModel model = new InboundModel(id,pollId, constituencyId, typeString, type, msisdn, time, message, agentName, poll, approve, constituency);
					inbounds.add(model);

					if(!inboundsIdList.contains(id)){
						inboundsIdList.add(id);
					}
				}

				con.close();
				return inbounds;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public int getInboundUpdate(InboundModel model, int userId) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();


		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("update inbounds set approve = ?, approved_by = ? where id = ?");
			prstmt.setString(1, model.getApproveState().trim());
			prstmt.setInt(2, model.getApproveState().equalsIgnoreCase(Constants.APPROVED)?userId:0);
			prstmt.setInt(3, model.getId());

			int success = prstmt.executeUpdate();
			if(success >= 0){

				updateConstituency(model.getPollId(), model.getConsId(), model.getType());
				con.close();
				return success;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean isAlreadyApproved(InboundModel model){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select id from inbounds where poll_id = ? and type = ? and approve = 'A'");
			prstmt.setInt(1, model.getPollId());
			prstmt.setString(2, model.getType());

			ResultSet result = prstmt.executeQuery();
			while(result.next()){
				con.close();
				return true;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public int rejectAllPollsExcept(int inboundId, int pollId, int consId, String type, String status) {
		if(setAllPollsToRejected(pollId, type) >= 0){
			PreparedStatement prstmt = null;
			con = DBConnection.getConnection();

			try{
				con.setCachePreparedStatements(false);
				prstmt = (PreparedStatement) con.prepareStatement("update inbounds set approve = ? where id = ?");
				prstmt.setString(1, status);
				prstmt.setInt(2, inboundId);

				int success = prstmt.executeUpdate();
				if(success >= 0){
					//Update the constituency from which the polling station originates
					con.close();
					updateConstituency(pollId, consId, type);
					return 1;
				}
				con.close();
			}catch(SQLException sql){
				sql.printStackTrace();
			}
			return 0;
		}

		return 0;
	}

	private int setAllPollsToRejected(int pollId, String type){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("update inbounds set approve = 'R' where poll_id = ? and type = ?");
			prstmt.setInt(1, pollId);
			prstmt.setString(2, type);

			int success = prstmt.executeUpdate();

			if(success >= 0){
				con.close();
				return 1;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return -1;
	}

	private int updateConstituency(int pollId, int consId, String type){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select id, poll_id, cons_id, message, approve from inbounds where approve = 'A' and cons_id = ? and type = ?");
			prstmt.setInt(1, consId);
			prstmt.setString(2, type);

			ResultSet result = prstmt.executeQuery();
			HashMap<Integer, String> pollMap = new HashMap<Integer, String>();
			while(result.next()){
				int tmpPollId = result.getInt("poll_id");
				String approve = result.getString("approve");
				if(pollMap.containsKey(tmpPollId)){
					continue;
				}

				if(approve.equalsIgnoreCase("A")){
					pollMap.put(tmpPollId, result.getString("message"));
				}
			}

			if(pollMap.size() > 0){
				doConstituencyUpadateProcessing(consId, type, pollMap);
			}else{
				doZeroHashing(consId, type);
			}
			con.close();
			return 1;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return 0;
	}

	private void doZeroHashing(int consId, String type){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update candidates set votes = 0 where constituency_id = ? and group_type = ?");
			prstmt.setInt(1, consId);
			prstmt.setString(2, type);

			int success = prstmt.executeUpdate();
			if(success >= 0){

				con.close();
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
	}

	private boolean doConstituencyUpadateProcessing(int consId, String type, HashMap<Integer, String> inboundMap){
		//inboundMap [{2753,'JFK:12 NYSE:9 APPL:34'},{2753,'JFK:12 NYSE:9 APPL:34'},{2753,'JFK:12 NYSE:9 APPL:34'},{2753,'JFK:12 NYSE:9 APPL:34'}]
		if(inboundMap.size() > 0){

			//Get all parties for a constituency

			//partyList [JFK, NYSE, APPL]
			ArrayList<String> partyList = getPartyAggregates(inboundMap);

			HashMap<String, ArrayList<Integer>> partyAndValueMap = new HashMap<String, ArrayList<Integer>>();

			for(String party : partyList){
				ArrayList<Integer> valueList = new ArrayList<Integer>();

				for(Integer pollingId : inboundMap.keySet()){

					String message = inboundMap.get(pollingId);

					//message JFK:12 NYSE:9 APPL:34
					String[] messageTokens = message.split("[\\s]+");
					for(String token : messageTokens){

						//JFK:12
						String tmpParty = token.split(Constants.INBOUND_PARTY_SEPARATOR)[0].toUpperCase().trim();
						if(tmpParty.equalsIgnoreCase(party.trim())){
							valueList.add(Integer.parseInt(token.split(Constants.INBOUND_PARTY_SEPARATOR)[1]));
						}
					}

				}

				partyAndValueMap.put(party, valueList);
			}

			HashMap<Integer, String> allParties = getAllParties();
			HashMap<String, Integer> allPartiesHash = new HashMap<String, Integer>();

			//Loop through parties
			for(Integer partyId : allParties.keySet()){
				String partyName = allParties.get(partyId);
				if(!allPartiesHash.containsKey(partyName)){
					allPartiesHash.put(partyName, partyId);
				}
			}

			//Insert into candidates
			for(String party : partyAndValueMap.keySet()){
				int votes = getVoteSum(partyAndValueMap.get(party));
				int partyId = allPartiesHash.get(party);
				String year = getConsituencyYear(consId);

				saveToCandidate(partyId, consId, type, year, votes);
			}

			return true;
		}

		return false;
	}

	private Integer getVoteSum(ArrayList<Integer> votes){
		int sum = 0;

		for(Integer vote : votes){
			sum += vote;
		}

		return sum;
	}

	private int saveToCandidate(int partyId, int constituencyId, String type, String year, int votes){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update candidates set votes = ? where party_id = ? and constituency_id = ? and group_type = ? and year = ?");
			prstmt.setInt(1, votes);
			prstmt.setInt(2, partyId);
			prstmt.setInt(3, constituencyId);
			prstmt.setString(4, type);
			prstmt.setString(5, year);

			int success = prstmt.executeUpdate();
			if(success >= 0){

				con.close();
				return 1;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	private ArrayList<String> getPartyAggregates(HashMap<Integer, String> hash){
		if(hash != null){
			ArrayList<String> list = new ArrayList<String>();
			for(Integer pollId : hash.keySet()){
				//message NPP:3223 NDC:1233 PNC:234
				String message = hash.get(pollId);

				//Do break down
				String[] messageTokens = message.split("[\\s]+");
				for(String token : messageTokens){
					String partyName = token.split(Constants.INBOUND_PARTY_SEPARATOR)[0].toUpperCase();
					if(!list.contains(partyName)){
						list.add(partyName);
					}
				}
			}

			return list;
		}
		return null;
	}

	private String getConsituencyYear(int constituencyId){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select year from constituencies where id = ?");
			prstmt.setInt(1, constituencyId);

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				String year = "";
				while(result.next()){
					year = result.getString("year");
				}

				con.close();
				return year;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isInboundChange() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet results = stmt.executeQuery("select id from inbounds");
			if(results != null){
				while(results.next()){
					int id = results.getInt("id");

					if(!inboundsIdList.contains(id)){
						con.close();
						return true;
					}
				}
				con.close();
				return false;
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public double isAppValid() {
		Statement stmt = null;
		con = DBConnection.getConnection();

		try{
			stmt = (Statement) con.createStatement();
			ResultSet results = stmt.executeQuery("select id, datediff(expire_date,curdate()) as days_left from app where status = 'A' order by id desc limit 1");
			if(results != null){
				while(results.next()){
					//int id = results.getInt("id");
					double daysLeft = results.getDouble("days_left");

					return daysLeft;
				}
				con.close();
			}
			con.close();
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

}
