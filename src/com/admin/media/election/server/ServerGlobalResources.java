package com.admin.media.election.server;

public class ServerGlobalResources {
	private String userCounts, tabCounts, dbName, dbPassword, dbUserName, port, ip;
	
	private static ServerGlobalResources instance = new ServerGlobalResources();
	
	public static ServerGlobalResources getInstance(){
		return instance;
	}

	public String getUserCounts() {
		return userCounts;
	}

	public void setUserCounts(String userCounts) {
		this.userCounts = userCounts;
	}

	public String getTabCounts() {
		return tabCounts;
	}

	public void setTabCounts(String tabCounts) {
		this.tabCounts = tabCounts;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getPort() {
		return port;
	}

	public void setDbPort(String port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setDbIp(String ip) {
		this.ip = ip;
	}
}
