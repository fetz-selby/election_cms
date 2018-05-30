package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.CorrespondentEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CorrespondentEvent extends GwtEvent<CorrespondentEventHandler>{

	public static Type<CorrespondentEventHandler> TYPE = new Type<CorrespondentEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CorrespondentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CorrespondentEventHandler handler) {
		handler.onCorrespondentEventClicked(this);
	}

}
