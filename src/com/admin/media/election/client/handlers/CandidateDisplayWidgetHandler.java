package com.admin.media.election.client.handlers;

import com.admin.media.election.shared.CandidateModel;

public interface CandidateDisplayWidgetHandler {
	void onEditClicked(CandidateModel model);
	void onDeleteClicked(CandidateModel model);
	void onSaveCompletion();
}
