package com.admin.media.election.client.system;

import com.admin.media.election.client.CommServiceAsync;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;

public class SystemVariable {
	private static SystemVariable systemV = new SystemVariable();
	private HandlerManager eventBus;
	private CommServiceAsync rpc;
	
	private SystemVariable(){}
	
	public static SystemVariable getInstance(){
		return systemV;
	}
	
	public void setHandlerManager(HandlerManager eventBus){
		this.eventBus = eventBus;
	}
	
	public void fireSystemEvent(GwtEvent<?> event){
		if(eventBus != null){
			eventBus.fireEvent(event);
			History.fireCurrentHistoryState();
		}
	}
	
	public void setRPC(CommServiceAsync rpc){
		this.rpc = rpc;
	}
	
	public CommServiceAsync getRPC(){
		return rpc;
	}
	
	public void refreshPage(){
		History.fireCurrentHistoryState();
	}
}
