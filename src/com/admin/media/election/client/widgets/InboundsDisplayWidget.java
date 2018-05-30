package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.InboundPopupWidget;
import com.admin.media.election.client.handlers.HasInboundsDisplayWidgetHandler;
import com.admin.media.election.client.handlers.InboundDisplayWidgetHandler;
import com.admin.media.election.client.utils.Utils;
import com.admin.media.election.shared.InboundModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class InboundsDisplayWidget extends Composite implements HasInboundsDisplayWidgetHandler{

	private InboundModel model;
	private InboundDisplayWidgetHandler handler;
	
	private static InboundsDisplayWidgetUiBinder uiBinder = GWT
			.create(InboundsDisplayWidgetUiBinder.class);

	interface InboundsDisplayWidgetUiBinder extends
			UiBinder<Widget, InboundsDisplayWidget> {
	}

	@UiField DivElement nameField, typeField, msisdnField, pollField, timeField, approveField, messageField;
	@UiField Anchor detailsLink;
	
	public InboundsDisplayWidget(InboundModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initRowWidget();
		initListeners();
		doInsertion();
	}
	

	private void doInsertion(){
		nameField.setInnerText(model.getSender());
		msisdnField.setInnerText(model.getMsisdn());
		typeField.setInnerText(model.getType());
		timeField.setInnerText(model.getTime());
		pollField.setInnerText(model.getPoll());
		messageField.setInnerText(Utils.getTruncatedText(model.getMessage(), 20));
		
		if(model.getApproveState().trim().equalsIgnoreCase("p")){
			approveField.appendChild(getPendingIcon());
		}else if(model.getApproveState().trim().equalsIgnoreCase("a")){
			approveField.appendChild(getApprovedIcon());
		}else if(model.getApproveState().trim().equalsIgnoreCase("r")){
			approveField.appendChild(getRejectedIcon());
		}
		
	}
	
	private Element getApprovedIcon(){
		
//		<i class="fa fa-check"></i>
		
		Element img = DOM.createElement("i");
		img.setClassName("fa fa-check fa-lg icon-color-success");
		return img;
	}
	
	private Element getRejectedIcon(){
		Element img = DOM.createElement("i");
		img.setClassName("fa fa-times fa-lg icon-color-fail");
		return img;
	}
	
	private Element getPendingIcon(){
		Element img = DOM.createElement("i");
		img.setClassName("fa fa-spinner fa-pulse fa-lg");
		return img;
	}
	
	private void initRowWidget(){
		detailsLink.setText("view");
		detailsLink.setHref("javascript:void(0)");
	}
	
	private void initListeners(){
		detailsLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doDetailView(model);
				if(handler != null){
					handler.onDetailClicked(model);
				}
			}
		});
	}
	
	private void doDetailView(InboundModel model){
		InboundPopupWidget info = new InboundPopupWidget(model);
		info.center();
	}
	
	public InboundModel getModel(){
		return model;
	}

	@Override
	public void setInboundsDisplayWidgetHandler(
			InboundDisplayWidgetHandler handler) {
		this.handler = handler;
	}

}
