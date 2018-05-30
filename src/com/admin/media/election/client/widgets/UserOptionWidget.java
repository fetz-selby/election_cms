package com.admin.media.election.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserOptionWidget extends Composite {

	private static UserOptionWidgetUiBinder uiBinder = GWT
			.create(UserOptionWidgetUiBinder.class);

	interface UserOptionWidgetUiBinder extends
	UiBinder<Widget, UserOptionWidget> {
	}

	@UiField DivElement userNameField;
	@UiField Anchor logoutLink;

	public UserOptionWidget(String username) {
		initWidget(uiBinder.createAndBindUi(this));
		userNameField.setInnerText(username);
		userNameField.setAttribute("style", "color: #999;");
		initEvent();
	}

	private void initEvent(){
		logoutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String url = GWT.getHostPageBaseURL() + "index.html";

				if(!GWT.isProdMode()) {
					url += "?gwt.codesvr=127.0.0.1:9997";
				}
				Cookies.setCookie("app", "");
				Cookies.removeCookie("app");
				Window.Location.replace(url);
			}
		});
	}

}
