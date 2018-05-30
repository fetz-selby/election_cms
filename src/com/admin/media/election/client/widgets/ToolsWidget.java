package com.admin.media.election.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ToolsWidget extends Composite {

	private static int counter;
	private ToolsWidgetEventHandler handler;
	private static ToolsWidgetUiBinder uiBinder = GWT
			.create(ToolsWidgetUiBinder.class);

	interface ToolsWidgetUiBinder extends UiBinder<Widget, ToolsWidget> {
	}

	public interface ToolsWidgetEventHandler{
		void onUpdateClicked();
	}
	
	@UiField DivElement progress, infoLabel, successLabel;
	@UiField Anchor updateBtn;
	
	public ToolsWidget(String info, String successinfo) {
		counter ++;
		initWidget(uiBinder.createAndBindUi(this));
		successLabel.setInnerText(successinfo);
		infoLabel.setInnerText(info);
		initElements();
		initEvent();
	}
	
	public void showProgress(boolean isShow){
		if(isShow){
			progress.setAttribute("style", "display:block");
			successLabel.setAttribute("style", "display:none");
		}else{
			progress.setAttribute("style", "display:none");
			successLabel.setAttribute("style", "display:block");
		}
	}
	
	private void initElements(){
		progress.setId("progress_"+counter);
		doProgressBar();
	}
	
	private void initEvent(){
		updateBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onUpdateClicked();
				}
			}
		});
	}
	
	
	
	@Override
	protected void onAttach() {
		super.onAttach();
		//initElements();
	}

	private void doProgressBar(){
		DeferredCommand.add(new Command() {
			
			@Override
			public void execute() {
				doNativeProgressBar(progress.getId());
			}
		});
	}
	
	private native void doNativeProgressBar(String id)/*-{
    $wnd.$("#"+id).progressbar({
      value: false
    });
	}-*/;

	
	public void setToolsWidgetEventHandler(ToolsWidgetEventHandler handler){
		this.handler = handler;
	}

}
