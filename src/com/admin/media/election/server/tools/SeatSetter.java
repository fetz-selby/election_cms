package com.admin.media.election.server.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.server.DBConnection;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class SeatSetter {
	private SeatSetterEventHandler handler;
	private static final int DEFAULT_PARTY = 23;
	private Connection con = DBConnection.getConnection();
	private HashMap<Integer, String> partyMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> constMap = new HashMap<Integer, String>();
	private HashMap<Integer, ArrayList<CompareModel>> compareMap = new HashMap<Integer, ArrayList<CompareModel>>();
	private HashMap<Integer, Integer> answerMap = new HashMap<Integer, Integer>();
	
	public interface SeatSetterEventHandler{
		void onSuccess();
		void onFailure();
	}
	
	public void start(){
		startProcessing();
	}
	
	private void startProcessing(){
		ArrayList<Integer> consList = getAllConstituenciesGrab();
		initPartyMap();
		for(Integer consId : consList){
			doConstituencyGrouping(consId);
		}
		System.out.println("*** Grouping done ***");
		doVotesCompare();
	}
	
	private ArrayList<Integer> getAllConstituenciesGrab(){
		PreparedStatement prstmt = null;
		try{
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("select id, name from constituencies where year = ?");
			prstmt.setString(1, "2012");
			
			ResultSet result = prstmt.executeQuery();
			if(result!=null){
				constMap.clear();
				ArrayList<Integer> consList = new ArrayList<Integer>();
				while(result.next()){
					int id = result.getInt("id");
					String consName = result.getString("name");
					
					consList.add(id);
					constMap.put(id, consName);
				}
				System.out.println("*** All constituencies Fetched ***");
				return consList;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}
	
	private void doConstituencyGrouping(int consId){
		PreparedStatement prstmt = null;
		try{
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("select constituency_id,votes,party_id from candidates where group_type = ? and year = ? and constituency_id = ?");
			prstmt.setString(1, "M");
			prstmt.setString(2, "2012");
			prstmt.setInt(3, consId);
			
			ResultSet result = prstmt.executeQuery();
			if(result != null){
				ArrayList<CompareModel> compareList = new ArrayList<CompareModel>();
				while(result.next()){
					int tmpConsId = result.getInt("constituency_id");
					int votes = result.getInt("votes");
					int partyId = result.getInt("party_id");
					
					CompareModel cm = new CompareModel(tmpConsId, partyId, votes);
					compareList.add(cm);
				}
				compareMap.put(consId, compareList);
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
	}
	
	private void doVotesCompare(){
		answerMap.clear();
		for(Integer consId : compareMap.keySet()){
			int defaultPartyId = DEFAULT_PARTY;
			int maxVotes = 0;
			
			for(CompareModel model : compareMap.get(consId)){
				if(model.getVotes() > maxVotes){
					maxVotes = model.getVotes();
					defaultPartyId = model.getPartyId();
				}
			}
			
			System.out.println("==== "+constMap.get(consId)+" ==> "+partyMap.get(defaultPartyId)+" ====");
			answerMap.put(consId, defaultPartyId);
		}
		
		System.out.println(" ############# Now starting to save #############");
		
		doSaving();
	}
	
	private void initPartyMap(){
		Statement stmt = null;
		try{
			stmt = (Statement)con.createStatement();
			ResultSet result = stmt.executeQuery("select id,name from parties");
			if(result != null){
				partyMap.clear();
				while(result.next()){
					int id = result.getInt("id");
					String name = result.getString("name");
					
					partyMap.put(id, name);
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
	}
	
	private void doSaving(){
		for(Integer consId : answerMap.keySet()){
			if(!isUpdateSuccessful(consId, answerMap.get(consId))){
				System.out.println("^^^ Error could not save for "+constMap.get(consId)+", with party "+partyMap.get(answerMap.get(consId)));
				if(handler != null){
					handler.onFailure();
				}
				System.exit(1);
			}
			
		}
		if(handler != null){
			handler.onSuccess();
		}
	}
	
	private boolean isUpdateSuccessful(int consId, int partyId){
		PreparedStatement prstmt = null;
		
		try{
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("update constituencies set seat_won_id = ?, is_declared = ? where id = ?");
			prstmt.setInt(1, partyId);
			if(partyId == DEFAULT_PARTY){
				prstmt.setString(2, "N");
			}else{
				prstmt.setString(2, "Y");
			}
			prstmt.setInt(3, consId);
			
			int success = prstmt.executeUpdate();
			if(success > 0){
				System.out.println("*** Save successful ["+constMap.get(consId)+"]"+" ==> "+partyMap.get(partyId)+" ***");
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}
	
	public void setSeatSetterEventHandler(SeatSetterEventHandler handler){
		this.handler = handler;
	}
}
