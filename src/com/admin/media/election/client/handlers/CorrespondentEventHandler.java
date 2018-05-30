package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.CorrespondentEvent;
import com.google.gwt.event.shared.EventHandler;

public interface CorrespondentEventHandler extends EventHandler{
	void onCorrespondentEventClicked(CorrespondentEvent event);
}
