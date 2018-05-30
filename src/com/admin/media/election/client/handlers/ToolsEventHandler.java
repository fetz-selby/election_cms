package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.ToolsEvent;
import com.google.gwt.event.shared.EventHandler;

public interface ToolsEventHandler extends EventHandler{
	void onToolsClicked(ToolsEvent event);
}
