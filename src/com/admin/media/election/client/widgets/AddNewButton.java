package com.admin.media.election.client.widgets;

import com.admin.media.election.client.handlers.AddNewButtonHandler;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AddNewButton extends Composite implements HasAddNewButtonHandlers{

	private AddNewButtonHandler handler;
	private static AddNewButtonUiBinder uiBinder = GWT
			.create(AddNewButtonUiBinder.class);

	interface AddNewButtonUiBinder extends UiBinder<Widget, AddNewButton> {
	}
	
	@UiField DivElement btnElement;

	public AddNewButton() {
		initWidget(uiBinder.createAndBindUi(this));
		initListener();
	}
	
	private void initListener(){
		Element btn = btnElement.cast();
		DOM.sinkEvents(btn, Event.ONCLICK);
		DOM.setEventListener(btn, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onButtonClicked();
				}
			}
		});
	}

	@Override
	public void setAddNewButtonHandler(AddNewButtonHandler handler) {
		this.handler = handler;
	}

}
