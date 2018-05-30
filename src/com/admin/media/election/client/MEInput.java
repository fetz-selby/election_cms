package com.admin.media.election.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MEInput implements EntryPoint {
	public void onModuleLoad(){
		final String LOGIN = "login", MAIN = "main";

		CommServiceAsync rpc = GWT.create(CommService.class);

		HandlerManager eventBus = new HandlerManager(null);

		if(RootPanel.get("pageId").getElement() != null){
			Element element = RootPanel.get("pageId").getElement();
			Hidden field = Hidden.wrap(element);
			GWT.log("Field value is "+field.getValue());
			String page = field.getValue();
			if(page.equals(LOGIN)){
				LoginController controller = new LoginController(RootPanel.get("loginBtn"), RootPanel.get("username"), RootPanel.get("password"), eventBus, rpc);
				controller.go(null);
			}else if(page.equals(MAIN)){
				if(isValid()){
					MainController controller = new MainController(RootPanel.get("cons_link"), RootPanel.get("can_link"), RootPanel.get("party_link"), RootPanel.get("polls_link"), RootPanel.get("agents_link"), RootPanel.get("inbounds_link"), RootPanel.get("tools_link"), RootPanel.get("da-content-top"), rpc, eventBus);
					controller.go(RootPanel.get("da-ex-flot"));
				}else{
					logout();
				}
				
			}

		}

	}
	
	public void logout(){
		String url = GWT.getHostPageBaseURL() + "index.html";

		if(!GWT.isProdMode()) {
			url += "?gwt.codesvr=127.0.0.1:9997";
		}
		
		Cookies.setCookie("app", "");
		Cookies.removeCookie("app");
		Window.Location.replace(url);
	}
	
	public boolean isValid() {
		if(Cookies.getCookie("app") != null && Cookies.getCookie("app").equals("47")){
			return true;
		}else{
			return false;
		}
	}
}
