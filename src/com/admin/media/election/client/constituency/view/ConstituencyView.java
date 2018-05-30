package com.admin.media.election.client.constituency.view;

import com.admin.media.election.client.constituency.presenter.ConstituencyPresenter;
import com.admin.media.election.client.form.ConstituencyFormWidget;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.widgets.AddNewButton;
import com.admin.media.election.client.widgets.ConstituencyDisplayWidget;
import com.admin.media.election.client.widgets.TinyScrollPanel;
import com.admin.media.election.shared.ConstituencyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ConstituencyView extends Composite implements ConstituencyPresenter.Display{

	private static ConstituencyViewUiBinder uiBinder = GWT
			.create(ConstituencyViewUiBinder.class);

	interface ConstituencyViewUiBinder extends
			UiBinder<Widget, ConstituencyView> {
	}
	
	@UiField AddNewButton addNewBtn;
	@UiField ListBox yearList, isDeclaredList;
	@UiField Button search;
	@UiField TinyScrollPanel detailsPanel;

	public ConstituencyView() {
		initWidget(uiBinder.createAndBindUi(this));
		detailsPanel.setTinySize("810px", "400px");
	}

	@Override
	public HasAddNewButtonHandlers getAddNewHandler() {
		return addNewBtn;
	}

	@Override
	public HasClickHandlers getSearchHandler() {
		return search;
	}

	@Override
	public String getYearValue() {
		return yearList.getValue(yearList.getSelectedIndex());
	}

	@Override
	public String getIsDeclaredValue() {
		return isDeclaredList.getValue(isDeclaredList.getSelectedIndex());
	}

	@Override
	public void showAddNewPopup() {
		ConstituencyFormWidget form = new ConstituencyFormWidget();
	}

	@Override
	public void addToPanel(ConstituencyModel model) {
		ConstituencyDisplayWidget displayWidget = new ConstituencyDisplayWidget(model);
		detailsPanel.add(displayWidget);
	}

	@Override
	public void addToYear(String name, String value) {
		yearList.addItem(name, value);
	}

	@Override
	public void addToIsDeclared(String name, String value) {
		isDeclaredList.addItem(name, value);
	}

	@Override
	public void clearPanel() {
		detailsPanel.clear();
	}

	@Override
	public void load() {
		detailsPanel.load();
	}

}
