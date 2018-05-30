package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.PollsEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PollsEvent extends GwtEvent<PollsEventHandler>{
	public static Type<PollsEventHandler> TYPE = new Type<PollsEventHandler>();

	@Override
	protected void dispatch(PollsEventHandler handler) {
		handler.onPollsClicked(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PollsEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
}
