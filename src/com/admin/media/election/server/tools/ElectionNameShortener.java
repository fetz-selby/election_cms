package com.admin.media.election.server.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.admin.media.election.server.DBConnection;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class ElectionNameShortener {
	private ElectionNameShortenerEventHandler handler;
	private Connection con = DBConnection.getConnection();
	private HashMap<Integer, String> nameMap;
	
	public interface ElectionNameShortenerEventHandler{
		void onSuccess();
		void onFailure();
	}
	
	public void start(){
		startProcess();
	}
	
	private void startProcess(){
		PreparedStatement prstmt = null;
		try{
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("select id,name from candidates");
			ResultSet result = prstmt.executeQuery();
			if(result != null){
				nameMap = new HashMap<Integer, String>();
				while(result.next()){
					String name = result.getString("name");
					int id = result.getInt("id");
					
					nameMap.put(id, name);
				}
				
				doNameShortening(nameMap);
				
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
	}
	
	private void doNameShortening(HashMap<Integer, String> nameMap){
		for(int id : nameMap.keySet()){
			String name = getShortName(nameMap.get(id));
			if(!isSaveSuccessful(id, name)){
				System.out.println("&&& Could not save &&&");
				if(handler != null){
					handler.onFailure();
				}
				System.exit(1);
			}
		}
		
		if(handler != null){
			handler.onSuccess();
		}
		System.out.println("*** Name shortening done ***");
	}
	
	private String getShortName(String name){
		String[] tokens = name.split("[\\s]*[-\\s][\\s]*");
		if(tokens.length == 2){
			return name;
		}else if(tokens.length == 3){
			return tokens[0]+" "+getFirstCharacter(tokens[1])+". "+tokens[2];
		}else if(tokens.length > 3){
			return tokens[0]+" "+getFirstCharacter(tokens[1])+". "+tokens[tokens.length - 1];
		}

		return name;
	}
	
	private String getFirstCharacter(String name){
		return (""+name.charAt(0)).toUpperCase();
	}
	
	private boolean isSaveSuccessful(int id, String name){
		PreparedStatement prstmt = null;
		try{
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("update candidates set name = ? where id = ?");
			prstmt.setString(1, name);
			prstmt.setInt(2, id);
			
			int success = prstmt.executeUpdate();
			if(success > 0){
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}
	
	public void setElectionNameShortenerEventHandler(ElectionNameShortenerEventHandler handler){
		this.handler = handler;
	}
}
