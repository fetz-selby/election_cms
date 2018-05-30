package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.AdminEvent;
import com.google.gwt.event.shared.EventHandler;

public interface AdminEventHandler extends EventHandler{
	void onAdminEventClicked(AdminEvent event);
}
