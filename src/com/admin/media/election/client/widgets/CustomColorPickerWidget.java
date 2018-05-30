package com.admin.media.election.client.widgets;

import net.auroris.ColorPicker.client.ColorPicker;

import com.admin.media.election.client.handlers.CustomColorPickerHandler;
import com.admin.media.election.client.handlers.HasCustomColorPickerWidgetHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomColorPickerWidget extends PopupPanel implements HasCustomColorPickerWidgetHandler{

	private CustomColorPickerWidget self = this;
	private CustomColorPickerHandler handler;
	private ColorPicker colorPicker;
	private static CustomColorPickerWidgetUiBinder uiBinder = GWT
			.create(CustomColorPickerWidgetUiBinder.class);

	interface CustomColorPickerWidgetUiBinder extends
			UiBinder<Widget, CustomColorPickerWidget> {
	}

	@UiField SimplePanel colorContainer;
	@UiField Button okBtn, cancelBtn;
	
	public CustomColorPickerWidget(Widget relativeWidget) {
		add(uiBinder.createAndBindUi(this));
		showRelativeTo(relativeWidget);
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		initComponents();
		initEvents();
	}
	
	private void initComponents(){
		colorPicker = new ColorPicker();
		colorContainer.setWidget(colorPicker);
	}
	
	private void initEvents(){
		okBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onOkClicked("#"+colorPicker.getHexColor());
				}
				self.hide();
			}
		});
		
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked("");
				}
				self.hide();
			}
		});
		
		this.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if(handler != null){
					handler.onOkClicked(colorPicker.getHexColor());
				}
			}
		});
	}

	@Override
	public void setCustomColorPickerHandler(CustomColorPickerHandler handler) {
		this.handler = handler;
	}
	
}
