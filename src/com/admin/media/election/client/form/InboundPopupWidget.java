package com.admin.media.election.client.form;

import com.admin.media.election.client.events.InboundEvent;
import com.admin.media.election.client.form.InboundOverridePopup.InboundOverridePopupEventHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.system.User;
import com.admin.media.election.shared.Constants;
import com.admin.media.election.shared.InboundModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class InboundPopupWidget extends PopupPanel{

	private InboundModel model;
	private static InboundPopupWidgetUiBinder uiBinder = GWT
			.create(InboundPopupWidgetUiBinder.class);

	interface InboundPopupWidgetUiBinder extends
	UiBinder<Widget, InboundPopupWidget> {
	}

	@UiField Label consLabel, senderLabel, mobileLabel, timeLabel, pollLabel;
	@UiField RadioButton rejectRadio, pendingRadio, approveRadio;
	@UiField LabelElement typeResult;
	@UiField FlowPanel resultPanel;
	@UiField Button actionBtn;
	@UiField Anchor cancelLink;

	public InboundPopupWidget(InboundModel model) {
		add(uiBinder.createAndBindUi(this));
		this.model = model;
		doInsertion();
		initEvents();
		initPopup();
	}

	private void doInsertion(){
		consLabel.setText(model.getConstituency());
		senderLabel.setText(model.getSender());
		mobileLabel.setText(model.getMsisdn());
		timeLabel.setText(model.getTime());
		pollLabel.setText(model.getPoll());
		typeResult.setInnerText(model.getType().equalsIgnoreCase(Constants.PARLIAMENTARY)?"Parliamentary":"Presidential"+" Results");

		if(model.getApproveState().equalsIgnoreCase(Constants.PENDING)){
			pendingRadio.setValue(true);
		}else if(model.getApproveState().equalsIgnoreCase(Constants.APPROVED)){
			approveRadio.setValue(true);
		}else if(model.getApproveState().equalsIgnoreCase(Constants.REJECTED)){
			rejectRadio.setValue(true);
		}

		doResultLoad();
	}

	private void doResultLoad(){
		String[] resultTokens = model.getMessage().split("[\\s]+");
		for(String result : resultTokens){
			Element resultPanel = getResultPanel(result.split(":")[0], result.split(":")[1]);
			this.resultPanel.getElement().appendChild(resultPanel);
		}
	}

	private Element getResultPanel(String label, String value){
		Element div = DOM.createElement("div");
		div.setClassName("result-container");

		Element labelDiv = DOM.createElement("div");
		labelDiv.setClassName("label-style");
		labelDiv.setInnerText(label);

		Element valueDiv = DOM.createElement("div");
		valueDiv.setClassName("value-style");
		valueDiv.setInnerText(value);

		div.appendChild(labelDiv);
		div.appendChild(valueDiv);

		return div;
	}

	private void initEvents(){
		pendingRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				model.setApproveState(Constants.PENDING);
			}
		});

		rejectRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				model.setApproveState(Constants.REJECTED);				
			}
		});

		approveRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				model.setApproveState(Constants.APPROVED);				
			}
		});

		cancelLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				InboundPopupWidget.this.hide();
			}
		});

		actionBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doCheckIfExist();
			}
		});
	}

	private void doCheckIfExist(){
		SystemVariable.getInstance().getRPC().isAlreadyApproved(model, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if(result){
					//Show already exits info
					InboundOverridePopup popup = new InboundOverridePopup(model);
					popup.setInboundOverridePopupEventHandler(new InboundOverridePopupEventHandler() {

						@Override
						public void onComfirmInvoked() {
							//Window.alert("id => "+model.getId()+", consId => "+model.getConsId()+", pollId => "+model.getPoll()+", status => "+model.getApproveState());
							SystemVariable.getInstance().getRPC().rejectAllPollsExcept(model.getId(), model.getConsId(), model.getPollId(), model.getType(), model.getApproveState(), new AsyncCallback<Integer>() {

								@Override
								public void onSuccess(Integer result) {
									hide();
									SystemVariable.getInstance().fireSystemEvent(new InboundEvent());
								}

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}
							});
						}

						@Override
						public void onCancelInvoked() {
							// TODO Auto-generated method stub

						}
					});
				}else{
					doUpdate();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void doUpdate(){
		SystemVariable.getInstance().getRPC().getInboundUpdate(model, User.getInstance().getUserId(), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				if(result > 0){
					InboundPopupWidget.this.hide();
					SystemVariable.getInstance().fireSystemEvent(new InboundEvent());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initPopup(){
		setStyleName("popup-style");
		setGlassStyleName("glassPanel");
		this.setAutoHideEnabled(false);
		this.center();
	}

}
