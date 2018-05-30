package com.admin.media.election.server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.jdbc.Connection;

@SuppressWarnings("serial")
public class CandidateUploadServlet extends HttpServlet{
	private Connection con = DBConnection.getConnection();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try{
				String name = "", votes = "", consId = "", partyId = "", year = "", type = "", checker = "", idSwitch = "", id = "";
				FileItem item2 = null;
				
				List<FileItem> items = upload.parseRequest(req);
				for(FileItem item : items){
					if(item.isFormField()){
						System.out.println("Field name is "+item.getFieldName()+", value is "+item.getString());
						if(item.getFieldName().trim().equals("candidate")){
							name = item.getString();
						}else if(item.getFieldName().trim().equals("votes")){
							votes = item.getString();
						}else if(item.getFieldName().trim().equals("consId")){
							consId = item.getString();
						}else if(item.getFieldName().trim().equals("partyId")){
							partyId = item.getString();
						}else if(item.getFieldName().trim().equals("year")){
							year = item.getString();
						}else if(item.getFieldName().trim().equals("type")){
							type = item.getString();
						}else if(item.getFieldName().trim().equals("checker")){
							checker = item.getString();
						}else if(item.getFieldName().trim().equals("idswitch")){
							idSwitch = item.getString();
						}else if(item.getFieldName().trim().equals("id")){
							id = item.getString();
						}
					}else{
						item2 = item;
					}
					//doCompleteSave(item, resp);
				}
				if(checker.equals("off") && idSwitch.equals("off")){
					System.out.println("1 gone");
					doExtractionNew(name, votes, consId, partyId, year, type, resp);
				}else if(item2 != null && idSwitch.equals("off")){
					System.out.println("2 gone");
					doExtractionNew(item2, name, votes, consId, partyId, year, type, resp);
				}else if(checker.equals("off") && idSwitch.equals("on")){
					System.out.println("3 gone");
					doExtractionUpdate(id, name, votes, consId, partyId, year, type, resp);
				}else if(item2 != null && idSwitch.equals("on")){
					System.out.println("4 gone");
					doExtractionUpdate(id, item2, name, votes, consId, partyId, year, type, resp);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//super.doPost(req, resp);
	}
	
	
	private void doExtractionNew(FileItem item, String name, String votes, String consId, String partyId, String year, String type, HttpServletResponse resp){
		PreparedStatement prstmt;
		con = DBConnection.getConnection();
		
		System.out.println("Name is "+name+", votes is "+votes+", consId is "+consId+", partyId is "+partyId+", group type is "+type);
		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into candidates (name,votes,constituency_id,party_id,year,group_type,avatar) values (?,?,?,?,?,?,?)");
			prstmt.setString(1, name);
			prstmt.setInt(2, Integer.parseInt(votes));
			prstmt.setInt(3, Integer.parseInt(consId));
			prstmt.setInt(4, Integer.parseInt(partyId));
			prstmt.setString(5, year);
			prstmt.setString(6, type);
			try {
				prstmt.setBinaryStream(7, item.getInputStream(), item.getSize());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			int success = prstmt.executeUpdate();
			if(success > 0){
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
	}
	
	private void doExtractionNew(String name, String votes, String consId, String partyId, String year, String type, HttpServletResponse resp){
		PreparedStatement prstmt;
		con = DBConnection.getConnection();
	
		System.out.println("Name is "+name+", votes is "+votes+", consId is "+consId+", partyId is "+partyId+", group type is "+type);
		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into candidates (name,votes,constituency_id,party_id,year,group_type)values (?,?,?,?,?,?)");
			prstmt.setString(1, name);
			prstmt.setInt(2, Integer.parseInt(votes));
			prstmt.setInt(3, Integer.parseInt(consId));
			prstmt.setInt(4, Integer.parseInt(partyId));
			prstmt.setString(5, year);
			prstmt.setString(6, type);

			int success = prstmt.executeUpdate();
			
			System.out.println("Success is "+success);
			
			if(success > 0){
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
	}
	
	private void doExtractionUpdate(String id, FileItem item, String name, String votes, String consId, String partyId, String year, String type, HttpServletResponse resp){
		PreparedStatement prstmt;
		con = DBConnection.getConnection();
		
		try{
			prstmt = (PreparedStatement) con.prepareStatement("update candidates set name=?, votes=?, constituency_id=?, party_id=?, year=?, group_type=?, avatar=? where id=?");
			prstmt.setString(1, name);
			prstmt.setInt(2, Integer.parseInt(votes));
			prstmt.setInt(3, Integer.parseInt(consId));
			prstmt.setInt(4, Integer.parseInt(partyId));
			prstmt.setString(5, year);
			prstmt.setString(6, type);
			
			try {
				prstmt.setBinaryStream(7, item.getInputStream(), item.getSize());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			prstmt.setInt(8, Integer.parseInt(id));
			
			int success = prstmt.executeUpdate();
			if(success > 0){
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Upadated Successfully !!!");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
		
	}
	
	private void doExtractionUpdate(String id, String name, String votes, String consId, String partyId, String year, String type, HttpServletResponse resp){
		PreparedStatement prstmt;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("update candidates set name=?, votes=?, constituency_id=?, party_id=?, year=?, group_type=? where id=?");
			prstmt.setString(1, name);
			prstmt.setInt(2, Integer.parseInt(votes));
			prstmt.setInt(3, Integer.parseInt(consId));
			prstmt.setInt(4, Integer.parseInt(partyId));
			prstmt.setString(5, year);
			prstmt.setString(6, type);
			prstmt.setInt(7, Integer.parseInt(id));
			
			int success = prstmt.executeUpdate();
			if(success > 0){
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Upadated Successfully !!!");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		
	}

}
