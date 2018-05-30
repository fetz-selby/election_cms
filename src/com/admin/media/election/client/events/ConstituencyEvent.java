package com.admin.media.election.client.events;

import com.admin.media.election.client.handlers.ConstituencyEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ConstituencyEvent extends GwtEvent<ConstituencyEventHandler>{

	public static Type<ConstituencyEventHandler> TYPE = new Type<ConstituencyEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ConstituencyEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ConstituencyEventHandler handler) {
		handler.onConstituencyClicked(this);
	}

}
