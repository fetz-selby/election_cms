package com.admin.media.election.client.handlers;

import com.admin.media.election.client.events.CandidatesEvent;
import com.google.gwt.event.shared.EventHandler;

public interface CandidatesEventHandler extends EventHandler{
	void onCandidateClicked(CandidatesEvent event);
}
