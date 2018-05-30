package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.PartyEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PartyEvent extends GwtEvent<PartyEventHandler>{

	public static Type<PartyEventHandler> TYPE = new Type<PartyEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PartyEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PartyEventHandler handler) {
		handler.onPartyClicked(this);
	}

}
