package com.admin.media.election.client.form;

import com.admin.media.election.client.form.handlers.PartyFormHandler;
import com.admin.media.election.client.handlers.CustomColorPickerHandler;
import com.admin.media.election.client.widgets.CustomColorPickerWidget;
import com.admin.media.election.shared.PartyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PartyFormWidget extends Composite {
	private PartyFormHandler handler;
	private Hidden idswitchHidden, idHidden, checkerHidden;
	private PartyModel model;
	private boolean isImageAttached = false, hasId = false;
	private static PartyFormWidgetUiBinder uiBinder = GWT
			.create(PartyFormWidgetUiBinder.class);

	interface PartyFormWidgetUiBinder extends UiBinder<Widget, PartyFormWidget> {
	}

	@UiField Image logo;
	@UiField TextBox partyName, color;
	@UiField SimplePanel hiddenUploadPanel;
	@UiField FormPanel previewForm;
	@UiField FileUpload logoLink;
	@UiField Button actionBtn;
	@UiField Anchor cancelLink;
	@UiField FlowPanel hiddenPanel;
	
	public PartyFormWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		initForm();
		initEvents();
	}

	public PartyFormWidget(PartyModel model){
		initWidget(uiBinder.createAndBindUi(this));
		initForm();
		initEvents();
		this.model = model;
		doInit();
	}
	
	private void doInit(){
		logo.setUrl(model.getUrl());
		partyName.setText(model.getPartyName());
		color.setText(model.getColor());
		hasId = true;
	}
	
	private void initEvents(){
		logoLink.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				previewForm.submit();
			}
		});
		
		actionBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					if(isValidGatheredInfo()){
						handler.onSubmitClicked();
					}
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}
			}
		});
		
		color.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				CustomColorPickerWidget colorPicker = new CustomColorPickerWidget(color);
				colorPicker.setCustomColorPickerHandler(new CustomColorPickerHandler() {
					
					@Override
					public void onOkClicked(String hexCode) {
						color.setText(hexCode);
					}
					
					@Override
					public void onCancelClicked(String hexCode) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	private boolean isValidGatheredInfo(){
		
		if(isImageAttached){
			checkerHidden.setValue("on");
		}else{
			checkerHidden.setValue("off");
		}
		
		if(partyName.getText().length() < 2){
			return false;
		}
		
		if(hasId){
			idswitchHidden.setValue("on");
			idHidden.setValue(""+model.getId());
		}else{
			idswitchHidden.setValue("off");
		}
		
		if(!color.getText().startsWith("#")){
			color.setValue("#"+color.getText());
		}
		
		switchBackFormPanel();
		return true;
	}
	
	private void initForm(){
		previewForm.setAction(GWT.getModuleBaseURL()+"preview");
		previewForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		previewForm.setMethod(FormPanel.METHOD_POST);
		previewForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String imageUrl = event.getResults();
				
				if(imageUrl.length() > 0){
					GWT.log("image url is "+imageUrl);
					logo.setUrl(getProcessedImage(imageUrl));
					isImageAttached = true;
				}
				
			}
		});
		
		idswitchHidden = new Hidden();
		checkerHidden = new Hidden();
		idHidden = new Hidden();
		
		idswitchHidden.setName("idswitch");
		idHidden.setName("id");
		checkerHidden.setName("checker");
		
		hiddenPanel.add(idswitchHidden);
		hiddenPanel.add(checkerHidden);
		hiddenPanel.add(idHidden);
	}
	
	private String getProcessedImage(String unProcessedImage){
		HTML html = new HTML(unProcessedImage);
		GWT.log("Object text is "+html.getText());
			
		return html.getText();
	}
	
	private void switchBackFormPanel(){
		hiddenUploadPanel.setWidget(logoLink);
		previewForm.removeFromParent();
	}
	
	public void setPartyFormWidgetHandler(PartyFormHandler handler){
		this.handler = handler;
	}
}
