package com.admin.media.election.client.handlers;

import com.admin.media.election.shared.InboundModel;

public interface InboundDisplayWidgetHandler {
	void onDetailClicked(InboundModel model);
	void onApproved();
	void onRejected();
}
