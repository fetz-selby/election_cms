package com.admin.media.election.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
public class ImagePreviewServlet extends HttpServlet{

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
				List<FileItem> items = upload.parseRequest(req);
				for(FileItem item : items){
					if(item.isFormField()){
						continue;
					}else{
						processPreview(item, resp);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//super.doPost(req, resp);
	}
	
	private void processPreview(FileItem item, HttpServletResponse resp){
		if(item != null){
			String image = getImageUrl(item.get());
			try {
				resp.getWriter().print(image);
				resp.flushBuffer();
				System.out.print("Base image sent is"+image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getImageUrl(byte[] blob){
		String base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(blob));
		base64 = "data:image/png;base64,"+base64;
	    return base64;
	}
}
