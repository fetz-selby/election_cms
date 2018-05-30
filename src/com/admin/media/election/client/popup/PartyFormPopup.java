package com.admin.media.election.client.popup;

import com.admin.media.election.client.form.PartyFormWidget;
import com.admin.media.election.client.form.handlers.PartyFormHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.PopupPanel;

public class PartyFormPopup extends PopupPanel implements PartyFormHandler, SubmitCompleteHandler {
	private FormPanel form;
	private PartyFormWidget formWidget;
	
	public PartyFormPopup(PartyFormWidget formWidget){
		this.formWidget = formWidget;
		this.formWidget.setPartyFormWidgetHandler(this);
		initComponents();
		setStyleName("popup-style");
		setGlassStyleName("glassPanel");
		center();
		setGlassEnabled(true);
		setModal(true);
	}
	
	private void initComponents(){
		form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL()+"uploadparty");
		form.addSubmitCompleteHandler(this);
		form.add(formWidget);
		add(form);
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		hide();
		SystemVariable.getInstance().refreshPage();
	}

	@Override
	public void onSubmitClicked() {
		form.submit();
	}

	@Override
	public void onCancelClicked() {
		hide();		
	}
	
}
