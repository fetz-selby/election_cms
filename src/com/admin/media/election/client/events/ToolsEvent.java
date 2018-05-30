package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.ToolsEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ToolsEvent extends GwtEvent<ToolsEventHandler>{

	public static Type<ToolsEventHandler> TYPE = new Type<ToolsEventHandler>();

	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ToolsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ToolsEventHandler handler) {
		handler.onToolsClicked(this);
	}

}
