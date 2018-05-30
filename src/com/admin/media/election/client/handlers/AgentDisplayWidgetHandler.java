package com.admin.media.election.client.handlers;

import com.admin.media.election.shared.AgentModel;

public interface AgentDisplayWidgetHandler {
	void onEditClicked(AgentModel model);
	void onDeleteClicked(AgentModel model);
	void onSaveCompletion();
}
