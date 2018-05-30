package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.InboundEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class InboundEvent extends GwtEvent<InboundEventHandler>{

	public static Type<InboundEventHandler> TYPE = new Type<InboundEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<InboundEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InboundEventHandler handler) {
		handler.onInboudEventClicked(this);
	}

}
