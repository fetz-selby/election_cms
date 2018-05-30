package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.PollsEvent;
import com.google.gwt.event.shared.EventHandler;

public interface PollsEventHandler extends EventHandler{
	void onPollsClicked(PollsEvent event);
}
