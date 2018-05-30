package com.admin.media.election.client.popup;

import com.admin.media.election.client.form.CandidateFormWidget;
import com.admin.media.election.client.form.handlers.CandidateFormHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.PopupPanel;

public class CandidateFormPopup extends PopupPanel implements CandidateFormHandler, SubmitCompleteHandler, HasFormResponseHandler{
	private FormPanel form;
	private CandidateFormWidget formWidget;
	private FormResponseHandler handler;
	
	public CandidateFormPopup(CandidateFormWidget formWidget){
		this.formWidget = formWidget;
		this.formWidget.setCandidateFormHandler(this);
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
		form.setAction(GWT.getModuleBaseURL()+"uploadcandidate");
		form.addSubmitCompleteHandler(this);
		form.add(formWidget);
		add(form);
	}
	
	@Override
	public void onSubmitClicked() {
		form.submit();
	}

	@Override
	public void onCancelClicked() {
		hide();
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		hide();
		if(handler != null){
			handler.onSaveComplete();
		}
		//SystemVariable.getInstance().refreshPage();
	}

	@Override
	public void setFormResponseHandler(FormResponseHandler handler) {
		this.handler = handler;
	}

}
