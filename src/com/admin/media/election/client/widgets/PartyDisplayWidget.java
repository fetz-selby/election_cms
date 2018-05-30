package com.admin.media.election.client.widgets;

import com.admin.media.election.client.form.PartyFormWidget;
import com.admin.media.election.client.popup.PartyFormPopup;
import com.admin.media.election.shared.PartyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class PartyDisplayWidget extends Composite {
	private PartyModel model;
	private PartyDisplayWidget self = this;
	private static PartyDisplayWidgetUiBinder uiBinder = GWT
			.create(PartyDisplayWidgetUiBinder.class);

	interface PartyDisplayWidgetUiBinder extends
			UiBinder<Widget, PartyDisplayWidget> {
	}

	@UiField DivElement nameField, colorField;
	@UiField Image logoField;
	@UiField Anchor editLink;
	//@UiField Anchor editLink, deleteLink;

	
	public PartyDisplayWidget(PartyModel model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		initComponent();
		initEvents();
	}
	
	private void initComponent(){
		if(model != null){
			nameField.setInnerText(model.getPartyName());
			logoField.setUrl(model.getUrl());
			colorField.setAttribute("style", "background-color:"+model.getColor());
		}
	}
	
	private void initEvents(){
		editLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Edit clicked !!!");
				PartyFormPopup popup = new PartyFormPopup(new PartyFormWidget(model));
			}
		});
		
//		deleteLink.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				ConfirmationWidowPopup confirmpopup = new ConfirmationWidowPopup();
//				confirmpopup.setConfirmWindowPopupHandler(new ConfirmWindowPopupHandler() {
//					
//					@Override
//					public void onYesClicked() {
////						SystemVariable.getInstance().getRPC().removeParty(model.getId(), new AsyncCallback<Boolean>() {
////							
////							@Override
////							public void onSuccess(Boolean result) {
////								if(result){
////									self.removeFromParent();
////								}
////							}
////							
////							@Override
////							public void onFailure(Throwable caught) {
////								// TODO Auto-generated method stub
////							}
////						});
//						
//					}
//					
//					@Override
//					public void onNoClicked() {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		});
	}

}
