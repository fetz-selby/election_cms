package com.admin.media.election.server.utils;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.admin.media.election.server.ServerGlobalResources;

@SuppressWarnings("serial")
public class ParamController extends GenericServlet{
	private String numberOfUsers, numberOfTabs, dbName, dbUser, dbPassword, dbPort, dbIP;
	
	public void init(ServletConfig servletConfig) throws ServletException{
	    this.numberOfUsers = servletConfig.getInitParameter("users");
	    this.numberOfTabs = servletConfig.getInitParameter("tabs");
	    this.dbName = servletConfig.getInitParameter("dbname");
	    this.dbPassword = servletConfig.getInitParameter("dbpassword");
	    this.dbUser = servletConfig.getInitParameter("dbuser");
	    this.dbPort = servletConfig.getInitParameter("dbport");
	    this.dbIP = servletConfig.getInitParameter("dbip");

	    System.out.println("Users => "+numberOfUsers);
	    System.out.println("Tabs => "+numberOfTabs);
	    System.out.println("DBName => "+dbName);
	    System.out.println("DBPassword => "+dbPassword);
	    System.out.println("DBUser => "+dbUser);
	    System.out.println("DBPort => "+dbPort);
	    System.out.println("DBIP => "+dbIP);

	    if(dbUser == null){
	    	dbUser = "sels";
	    } 
	    
	    if(dbPassword == null){
	    	dbPassword = "134119601Hello";
	    }
	    
	    if(dbPort == null){
	    	dbPort = "3306";
	    }
	    
	    if(dbIP == null){
	    	dbIP = "127.0.0.1";
	    }

	    ServerGlobalResources.getInstance().setUserCounts(numberOfUsers);
	    ServerGlobalResources.getInstance().setTabCounts(numberOfTabs);
	    ServerGlobalResources.getInstance().setDbName(dbName);
	    ServerGlobalResources.getInstance().setDbPassword(dbPassword);
	    ServerGlobalResources.getInstance().setDbUserName(dbUser);
	    ServerGlobalResources.getInstance().setDbPort(dbPort);
	    ServerGlobalResources.getInstance().setDbIp(dbIP);

	  }
	
	private String getTabCounts(String hash){
		//a29fe8a82fea7d1000
		String tabCountInHex = hash.substring(7, 8);
		String tabCountInBin = Integer.toBinaryString(4)+"";
		
		return null;
	}
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public String getNumberOfUsers() {
		return numberOfUsers;
	}

	public String getNumberOfTabs() {
		return numberOfTabs;
	}
	
}
