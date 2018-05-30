package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.AgentFormWidget;
import com.admin.media.election.client.handlers.AgentDisplayWidgetHandler;
import com.admin.media.election.client.handlers.ConfirmWindowPopupHandler;
import com.admin.media.election.client.handlers.HasAgentDisplayWidgetHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.shared.AgentModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AgentDisplayWidget extends Composite implements HasAgentDisplayWidgetHandler{

	private AgentModel model;
	private AgentDisplayWidgetHandler handler;
	
	private AgentDisplayWidget self = this;
	
	private static AgentDisplayWidgetUiBinder uiBinder = GWT
			.create(AgentDisplayWidgetUiBinder.class);

	interface AgentDisplayWidgetUiBinder extends
			UiBinder<Widget, AgentDisplayWidget> {
	}
	@UiField DivElement nameField, consField, msisdnField, pollField, statusField, yearField, codeField;
	@UiField Anchor editLink, deleteLink;

	public AgentDisplayWidget(AgentModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initRowWidget();
		initListeners();
		doInsertion();
	}
	
	private void doInsertion(){
		nameField.setInnerText(model.getName());
		consField.setInnerText(model.getConstituencyName());
		msisdnField.setInnerText(model.getMsisdn());
		codeField.setInnerText(model.getCode());
		pollField.setInnerText(model.getPollName());
		statusField.setInnerText(model.getStatus());
		yearField.setInnerText(model.getYear());
	}
	
	private void initRowWidget(){
		editLink.setText("edit");
		editLink.setHref("javascript:void(0)");
		
		deleteLink.setText("delete");
		deleteLink.setHref("javascript:void(0)");
	}
	
	private void initListeners(){
		editLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doEditing(model);
				if(handler != null){
					handler.onEditClicked(model);
				}
			}
		});
		
		deleteLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showConfirmationWindow();
			}
		});
	}

	private void showConfirmationWindow(){
		ConfirmationWidowPopup popup = new ConfirmationWidowPopup();
		popup.setConfirmWindowPopupHandler(new ConfirmWindowPopupHandler() {
			
			@Override
			public void onYesClicked() {
				doRemoval();
				if(handler != null){
					handler.onDeleteClicked(model);
				}
			}
			
			@Override
			public void onNoClicked() {
				
			}
		});
	}
	
	private void doRemoval(){
		SystemVariable.getInstance().getRPC().removeAgent(model.getId(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					self.removeFromParent();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private void doEditing(AgentModel model){
		AgentFormWidget form = new AgentFormWidget(model);
		form.center();
	}
	
	public AgentModel getModel(){
		return model;
	}

	@Override
	public void setAgentDisplayWidgetHandler(AgentDisplayWidgetHandler handler) {
		this.handler = handler;
	}

}
