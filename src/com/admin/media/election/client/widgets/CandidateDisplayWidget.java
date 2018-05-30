package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.CandidateFormWidget;
import com.admin.media.election.client.handlers.CandidateDisplayWidgetHandler;
import com.admin.media.election.client.handlers.ConfirmWindowPopupHandler;
import com.admin.media.election.client.handlers.HasCandidateDisplayWidgetHandler;
import com.admin.media.election.client.popup.CandidateFormPopup;
import com.admin.media.election.client.popup.FormResponseHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.Constants;
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

public class CandidateDisplayWidget extends Composite implements HasCandidateDisplayWidgetHandler{

	private CandidateModel model;
	private CandidateDisplayWidgetHandler handler;
	private static DisplayWidgetUiBinder uiBinder = GWT
			.create(DisplayWidgetUiBinder.class);
	private CandidateDisplayWidget self = this;

	interface DisplayWidgetUiBinder extends UiBinder<Widget, CandidateDisplayWidget> {
	}
	
	@UiField DivElement nameField, consField, partyField, typeField, votesField, yearField;
	@UiField Anchor editLink, deleteLink;
	
	
	public CandidateDisplayWidget(CandidateModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initRowWidget();
		initListeners();
		doInsertion();
	}
	
	private void doInsertion(){
		nameField.setInnerText(model.getName());
		consField.setInnerText(model.getConstituency());
		partyField.setInnerText(model.getParty());
		if(model.getGroup().equals(Constants.PARLIAMENTARY)){
			typeField.setInnerText("Paliamentarian");
		}else if(model.getGroup().equals(Constants.PRESIDENTIAL)){
			typeField.setInnerText("Presidential");
		}else{
			typeField.setInnerText("Unknown");
		}
		votesField.setInnerText(""+model.getVotes());
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
	
	private void doEditing(CandidateModel model){
		CandidateFormWidget form = new CandidateFormWidget(model);
		CandidateFormPopup popup = new CandidateFormPopup(form);
		popup.setFormResponseHandler(new FormResponseHandler() {
			
			@Override
			public void onSaveComplete() {
				if(handler != null){
					handler.onSaveCompletion();
				}
			}
		});
	}
	
	public CandidateModel getModel(){
		return model;
	}
	
	@Override
	public void setCandidateDisplayWidgetHandler(CandidateDisplayWidgetHandler handler) {
		this.handler = handler;
	}

}
