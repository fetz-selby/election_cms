package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.CandidatesEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CandidatesEvent extends GwtEvent<CandidatesEventHandler>{

	public static Type<CandidatesEventHandler> TYPE = new Type<CandidatesEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CandidatesEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CandidatesEventHandler handler) {
		handler.onCandidateClicked(this);
	}

}
