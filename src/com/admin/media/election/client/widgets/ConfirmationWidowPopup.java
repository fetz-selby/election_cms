package com.admin.media.election.client.widgets;

import com.admin.media.election.client.handlers.ConfirmWindowPopupHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmationWidowPopup extends PopupPanel {
	private ConfirmWindowPopupHandler handler;
	
	private static ConfirmationWidowPopupUiBinder uiBinder = GWT
			.create(ConfirmationWidowPopupUiBinder.class);

	interface ConfirmationWidowPopupUiBinder extends
			UiBinder<Widget, ConfirmationWidowPopup> {
	}

	@UiField Button yesBtn, noBtn;
	
	public ConfirmationWidowPopup() {
		add(uiBinder.createAndBindUi(this));
		initComponent();
		initEvents();
	}

	private void initComponent(){
		center();
		setGlassEnabled(true);
		setModal(true);
		setAnimationEnabled(true);
	}
	
	private void initEvents(){
		yesBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onYesClicked();
					hide();
				}
			}
		});
		
		noBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onNoClicked();
					hide();
				}
			}
		});
	}
	
	public void setConfirmWindowPopupHandler(ConfirmWindowPopupHandler handler){
		this.handler = handler;
	}
	
}
