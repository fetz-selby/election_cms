package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.PollsFormWidget;
import com.admin.media.election.client.handlers.ConfirmWindowPopupHandler;
import com.admin.media.election.client.handlers.HasPollsDisplayWidgetHandler;
import com.admin.media.election.client.handlers.PollsDisplayWidgetHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.shared.PollModel;
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

public class PollsDisplayWidget extends Composite implements HasPollsDisplayWidgetHandler{

	private PollModel model;
	private PollsDisplayWidgetHandler handler;
	
	private PollsDisplayWidget self = this;
	
	private static PollsDisplayWidgetUiBinder uiBinder = GWT
			.create(PollsDisplayWidgetUiBinder.class);

	interface PollsDisplayWidgetUiBinder extends
			UiBinder<Widget, PollsDisplayWidget> {
	}
	
	@UiField DivElement nameField, consField, statusField, yearField;
	@UiField Anchor editLink, deleteLink;
	
	public PollsDisplayWidget(PollModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initRowWidget();
		initListeners();
		doInsertion();
	}
	
	private void doInsertion(){
		nameField.setInnerText(model.getName());
		consField.setInnerText(model.getConstituencyName());
		statusField.setInnerText(""+model.getStatus());
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
		SystemVariable.getInstance().getRPC().removePoll(model.getId(), new AsyncCallback<Boolean>() {
			
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
	
	private void doEditing(PollModel model){
		PollsFormWidget form = new PollsFormWidget(model);
		form.show();
//		CandidateFormPopup popup = new CandidateFormPopup(form);
//		popup.setFormResponseHandler(new FormResponseHandler() {
//			
//			@Override
//			public void onSaveComplete() {
//				if(handler != null){
//					handler.onSaveCompletion();
//				}
//			}
//		});
	}
	
	public PollModel getModel(){
		return model;
	}
	
	@Override
	public void setPollsDisplayWidgetHandler(PollsDisplayWidgetHandler handler) {
		// TODO Auto-generated method stub
		
	}

}
