package com.admin.media.election.server;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class DBConnection {
	private static Connection con;
	//private String url = "jdbc:mysql://localhost:3306/";
	private static final String MYSQL_DRIVER = "jdbc:mysql://";

	private static DBConnection dbc = new DBConnection();
	
	private DBConnection(){
	}
	
	//jdbc:mysql://173.194.105.1:3306/election2?user=root&autoReconnect=true
	
	public static Connection getConnection(){
		dbc.establishConnection();
		return con;
	}
	
	private void establishConnection(){
		try{
            Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String url = MYSQL_DRIVER+ServerGlobalResources.getInstance().getIp()+":"+ServerGlobalResources.getInstance().getPort()+"/";
			con = (Connection)DriverManager.getConnection(url+ServerGlobalResources.getInstance().getDbName(), ServerGlobalResources.getInstance().getDbUserName(), ServerGlobalResources.getInstance().getDbPassword());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
