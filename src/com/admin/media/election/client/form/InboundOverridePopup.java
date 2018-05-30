package com.admin.media.election.client.form;

import com.admin.media.election.shared.Constants;
import com.admin.media.election.shared.InboundModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class InboundOverridePopup extends PopupPanel {

	private InboundOverridePopupEventHandler handler;
	private InboundModel model;
	private static InboundOverridePopupUiBinder uiBinder = GWT
			.create(InboundOverridePopupUiBinder.class);

	interface InboundOverridePopupUiBinder extends
			UiBinder<Widget, InboundOverridePopup> {
	}
	
	public interface InboundOverridePopupEventHandler{
		void onComfirmInvoked();
		void onCancelInvoked();
	}

	@UiField DivElement contentMsg,overrideMsg;
	@UiField Button btn, cancelLink;
	
	public InboundOverridePopup(InboundModel model) {
		add(uiBinder.createAndBindUi(this));
		this.model = model;
		initEvents();
		initComponents();
		initPopup();
	}
	
	private void initComponents(){
		btn.setText(Constants.INBOUND_YES);
		cancelLink.setText(Constants.INBOUND_NO);
		contentMsg.setInnerText(Constants.INBOUND_CONTENT_MSG);
		overrideMsg.setInnerText(Constants.INBOUND_OVERRIDE_MSG);
	}
	
	private void initPopup(){
		//setStyleName("popup-style");
		setGlassStyleName("glassPanel");
		setModal(true);
		setAutoHideEnabled(false);
		center();
	}
	
	private void initEvents(){
		btn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onComfirmInvoked();
				}
				hide();
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelInvoked();
				}
				hide();
			}
		});
	}

	public void setInboundOverridePopupEventHandler(InboundOverridePopupEventHandler handler){
		this.handler = handler;
	}
	
}
