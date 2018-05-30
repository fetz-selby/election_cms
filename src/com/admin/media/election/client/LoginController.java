package com.admin.media.election.client;

import com.admin.media.election.client.interfaces.Presenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginController implements Presenter{
	private CommServiceAsync rpc;

	private RootPanel loginBtn, username, password;
	private Widget screen;
	private HandlerManager eventBus;
	private TextBox userTextBox;
	private PasswordTextBox passTextBox;


	public LoginController(RootPanel loginBtn, RootPanel username, RootPanel password, HandlerManager eventBus, CommServiceAsync rpc){
		this.loginBtn = loginBtn;
		this.username = username;
		this.password = password;
		this.eventBus = eventBus;
		this.rpc = rpc;
	}

	private void initNativeEvents(){
		rpc.isAppValid(new AsyncCallback<Double>() {

			@Override
			public void onSuccess(Double result) {
				if(result > 0){
					initApp(result);
				}else{
					lockApp();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initApp(double daysLeft){
		if(loginBtn.getElement() != null){
			Element btnEle = loginBtn.getElement();
			DOM.sinkEvents(btnEle, Event.ONCLICK);
			DOM.setEventListener(btnEle, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					GWT.log("The button just got clicked !!!, username is"+userTextBox.getText()+" , and password is "+passTextBox.getText());
					doUserValidation(userTextBox.getText(), passTextBox.getText());
				}
			});

			if(username.getElement() != null && password.getElement() != null){
				userTextBox = TextBox.wrap(username.getElement());
				passTextBox = PasswordTextBox.wrap(password.getElement());
			}

			Element daysLeftElement = DOM.getElementById("days_left");
			daysLeftElement.setInnerText(daysLeft+"");

		}
	}

	private void lockApp(){
		if(username.getElement() != null && password.getElement() != null){
			userTextBox = TextBox.wrap(username.getElement());
			passTextBox = PasswordTextBox.wrap(password.getElement());

			userTextBox.setText("Renew App");
			userTextBox.setEnabled(false);
			passTextBox.setEnabled(false);

			Element daysLeftElement = DOM.getElementById("days_left");
			daysLeftElement.setInnerText("Renew App!");
		}
	}

	private void doUserValidation(String username, String password){
		final int THRESHOLD = 3;
		if(username.length() > THRESHOLD && password.length() > THRESHOLD){
			rpc.isUserValid(username, password, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result != null){
						if(result){
							//Redirect User
							String url = GWT.getHostPageBaseURL() + "Main.html";

							if(!GWT.isProdMode()) {
								url += "?gwt.codesvr=127.0.0.1:9997";
							}
							Cookies.setCookie("app", "47");
							Window.Location.replace(url);

							GWT.log("Validation passed !!!");
						}else{
							//Show error message

						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("RPC failed !!!");
				}
			});
		}
	}


	@Override
	public void go(HasWidgets screen) {
		initNativeEvents();
		//this.screen = screen.asWidget();
	}


}