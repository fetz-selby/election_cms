package com.admin.media.election.client.tools.presenter;

import com.admin.media.election.client.CommServiceAsync;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.client.widgets.ToolsWidget;
import com.admin.media.election.client.widgets.ToolsWidget.ToolsWidgetEventHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class ToolsPresenter implements Presenter{

	private HandlerManager eventBus;
	private CommServiceAsync rpc;
	private Display view;
	
	public interface Display extends IsWidget{
		Widget asWidget();
		void setSeatWidget(IsWidget widget);
		void setNameWidget(IsWidget widget);
	}
	
	public ToolsPresenter(HandlerManager eventBus, CommServiceAsync rpc, Display view){
		this.eventBus = eventBus;
		this.rpc = rpc;
		this.view = view;
	}
	
	private void bind(){
		final ToolsWidget seatTool = new ToolsWidget("Please click to update the seats", "Seats has been updated successfully");
		seatTool.setToolsWidgetEventHandler(new ToolsWidgetEventHandler() {
			
			@Override
			public void onUpdateClicked() {
				seatTool.showProgress(true);
				doSeatSettingCall(seatTool);
			}
		});
		
		final ToolsWidget nameTool = new ToolsWidget("Please click to shorten the long candidate names", "Candidates names has been shorten successfully");
		nameTool.setToolsWidgetEventHandler(new ToolsWidgetEventHandler() {
			
			@Override
			public void onUpdateClicked() {
				nameTool.showProgress(true);
				doNameShorteningCall(nameTool);
			}
		});
		
		view.setSeatWidget(seatTool);
		view.setNameWidget(nameTool);
	}
	
	private void doSeatSettingCall(final ToolsWidget widget){
		rpc.isSeatSettingDone(new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					widget.showProgress(false);
				}else{
					Window.alert("Seat Setting failed!");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Seat Setting failed!");
			}
		});
	}
	private void doNameShorteningCall(final ToolsWidget widget){
		rpc.isNameShorteningDone(new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					widget.showProgress(false);
				}else{
					Window.alert("Name shortening failed!");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Name shortening failed!");
			}
		});
	}
	
	@Override
	public void go(HasWidgets screen) {
		screen.clear();
		bind();
		screen.add(view.asWidget());		
	}

}
