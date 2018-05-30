package com.admin.media.election.client.handlers;

import com.admin.media.election.shared.PollModel;


public interface PollsDisplayWidgetHandler {
	void onEditClicked(PollModel model);
	void onDeleteClicked(PollModel model);
	void onSaveCompletion();
}
