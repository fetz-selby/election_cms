package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.ConstituencyFormWidget;
import com.admin.media.election.client.handlers.ConfirmWindowPopupHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.shared.ConstituencyModel;
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

public class ConstituencyDisplayWidget extends Composite {
	private ConstituencyModel model;
	private ConstituencyDisplayWidget self = this;
	private static ConstituencyDisplayWidgetUiBinder uiBinder = GWT
			.create(ConstituencyDisplayWidgetUiBinder.class);

	interface ConstituencyDisplayWidgetUiBinder extends
			UiBinder<Widget, ConstituencyDisplayWidget> {
	}

	@UiField DivElement nameField, regVotes, rejVotes, castVotes, seatWon, isDeclared;
	@UiField Anchor editLink, deleteLink;
	
	public ConstituencyDisplayWidget(ConstituencyModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initElement();
		initListeners();
	}

	private void initElement(){
		nameField.setInnerText(model.getName());
		regVotes.setInnerText(""+model.getRegVotes());
		rejVotes.setInnerText(""+model.getRejVotes());
		castVotes.setInnerText(""+model.getCastedVotes());
		seatWon.setInnerText(model.getSeatWonName());
		if(model.getIsDeclared().equals("Y")){
			isDeclared.setInnerText("Yes");
		}else{
			isDeclared.setInnerText("No");
		}
	}
	
	private void initListeners(){
		editLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ConstituencyFormWidget form = new ConstituencyFormWidget(model);
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
			}
			
			@Override
			public void onNoClicked() {
				
			}
		});
	}
	
	private void doRemoval(){
		SystemVariable.getInstance().getRPC().removeConstituency(model.getId(), new AsyncCallback<Boolean>() {
			
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
	
	public ConstituencyModel getConstituencyModel(){
		return model;
	}
}
