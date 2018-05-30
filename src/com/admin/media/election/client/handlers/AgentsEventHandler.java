package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.AgentsEvent;
import com.google.gwt.event.shared.EventHandler;

public interface AgentsEventHandler extends EventHandler{
	void onAgentEventClicked(AgentsEvent event);
}
