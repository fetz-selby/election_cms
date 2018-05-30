package com.admin.media.election.client.party.view;

import com.admin.media.election.client.form.PartyFormWidget;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.party.presenter.PartyPresenter;
import com.admin.media.election.client.popup.PartyFormPopup;
import com.admin.media.election.client.widgets.AddNewButton;
import com.admin.media.election.client.widgets.PartyDisplayWidget;
import com.admin.media.election.client.widgets.TinyScrollPanel;
import com.admin.media.election.shared.PartyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


public class PartyView extends Composite implements PartyPresenter.Display{

	private static PartyViewUiBinder uiBinder = GWT
			.create(PartyViewUiBinder.class);

	interface PartyViewUiBinder extends UiBinder<Widget, PartyView> {
	}

	@UiField AddNewButton addNewBtn;
	@UiField TinyScrollPanel detailsPanel;
	
	public PartyView() {
		initWidget(uiBinder.createAndBindUi(this));
		detailsPanel.setTinySize("810px", "400px");
	}

	@Override
	public HasAddNewButtonHandlers getAddNewHandler() {
		return addNewBtn;
	}

	@Override
	public void addToPanel(PartyModel model) {
		PartyDisplayWidget partyWidget = new PartyDisplayWidget(model);
		detailsPanel.add(partyWidget);
	}

	@Override
	public void clear() {
		detailsPanel.clear();
	}

	@Override
	public void load() {
		detailsPanel.load();
	}

	@Override
	public void showAddNewPopup() {
		PartyFormPopup popup = new PartyFormPopup(new PartyFormWidget());
	}

}
