package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.InboundEvent;
import com.google.gwt.event.shared.EventHandler;

public interface InboundEventHandler extends EventHandler{
	void onInboudEventClicked(InboundEvent event);
}
