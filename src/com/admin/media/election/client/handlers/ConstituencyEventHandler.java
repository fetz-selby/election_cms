package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.ConstituencyEvent;
import com.google.gwt.event.shared.EventHandler;

public interface ConstituencyEventHandler extends EventHandler{
	void onConstituencyClicked(ConstituencyEvent event);
}
