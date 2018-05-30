package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.PartyEvent;
import com.google.gwt.event.shared.EventHandler;

public interface PartyEventHandler extends EventHandler{
	void onPartyClicked(PartyEvent event);
}
