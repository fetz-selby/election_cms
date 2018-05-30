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
public class PartyUploadServlet extends HttpServlet {
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
		try {
			String name = "", color = "", checker = "", idSwitch = "", id = "";
			FileItem item2 = null;

			List<FileItem> items = upload.parseRequest(req);
			for (FileItem item : items) {
				if (item.isFormField()) {
					System.out.println("Field name is " + item.getFieldName()
							+ ", value is " + item.getString());
					if (item.getFieldName().trim().equals("party")) {
						name = item.getString();
					} else if (item.getFieldName().trim().equals("color")) {
						color = item.getString();
					} else if (item.getFieldName().trim().equals("idswitch")) {
						idSwitch = item.getString();
					} else if (item.getFieldName().trim().equals("id")) {
						id = item.getString();
					} else if (item.getFieldName().trim().equals("checker")) {
						checker = item.getString();
					}
				} else {
					item2 = item;
				}
				// doCompleteSave(item, resp);
			}
			if (checker.equals("off") && idSwitch.equals("off")) {
				System.out.println("1 gone");
				saveNewPartyWithOutImage(name, color, resp);
			} else if (item2 != null && idSwitch.equals("off")) {
				System.out.println("2 gone");
				saveNewPartyWithImage(name, color, item2, resp);
			} else if (checker.equals("off") && idSwitch.equals("on")) {
				System.out.println("3 gone");
				updatePartyWithOutImage(Integer.parseInt(id), name, color, resp);
			} else if (item2 != null && idSwitch.equals("on")) {
				System.out.println("4 gone");
				updatePartyWithImage(Integer.parseInt(id), name, color, item2,
						resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updatePartyWithImage(int id, String name, String color,
			FileItem item, HttpServletResponse resp) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try {
			prstmt = (PreparedStatement) con
					.prepareStatement("update parties set name = ?, color = ?, avatar = ? where id = ?");
			prstmt.setString(1, name);
			prstmt.setString(2, color);
			try {
				prstmt.setBinaryStream(3, item.getInputStream(), item.getSize());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			prstmt.setInt(4, id);

			int success = prstmt.executeUpdate();

			if (success > 0) {
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void updatePartyWithOutImage(int id, String name, String color,
			HttpServletResponse resp) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try {
			prstmt = (PreparedStatement) con
					.prepareStatement("update parties set name = ?, color = ? where id = ?");
			prstmt.setString(1, name);
			prstmt.setString(2, color);
			prstmt.setInt(3, id);

			int success = prstmt.executeUpdate();

			if (success > 0) {
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void saveNewPartyWithOutImage(String name, String color,
			HttpServletResponse resp) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try {
			prstmt = (PreparedStatement) con
					.prepareStatement("insert into parties (name,color) values (?,?)");
			prstmt.setString(1, name);
			prstmt.setString(2, color);

			int success = prstmt.executeUpdate();

			if (success > 0) {
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void saveNewPartyWithImage(String name, String color,
			FileItem item, HttpServletResponse resp) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try {
			prstmt = (PreparedStatement) con
					.prepareStatement("insert into parties (name,color,avatar) values (?,?,?)");
			prstmt.setString(1, name);
			prstmt.setString(2, color);
			try {
				prstmt.setBinaryStream(3, item.getInputStream(), item.getSize());
			} catch (IOException e) {
				e.printStackTrace();
			}

			int success = prstmt.executeUpdate();

			if (success > 0) {
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

}
