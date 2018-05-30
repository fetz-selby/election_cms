package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.AgentsEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AgentsEvent extends GwtEvent<AgentsEventHandler>{
	public static Type<AgentsEventHandler> TYPE = new Type<AgentsEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AgentsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AgentsEventHandler handler) {
		handler.onAgentEventClicked(this);
	}
}
