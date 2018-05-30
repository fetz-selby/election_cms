package com.admin.media.election.client.inbounds.view;

import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.inbounds.presenter.InboundsPresenter;
import com.admin.media.election.client.widgets.TinyScrollPanel;
import com.admin.media.election.shared.CandidateModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class InboundsView extends Composite implements InboundsPresenter.Display{

	private static InboundsViewUiBinder uiBinder = GWT
			.create(InboundsViewUiBinder.class);

	interface InboundsViewUiBinder extends UiBinder<Widget, InboundsView> {
	}

	@UiField TinyScrollPanel detailsPanel;
	@UiField SimplePanel consSearchBox, pollsSearchBox;
	@UiField ListBox yearListBox;
	@UiField Button searchButton;
	@UiField DivElement infoRow, aRowCounterDiv, rRowCounterDiv, pRowCounterDiv;
	
	public InboundsView() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		detailsPanel.setTinySize("810px", "300px");
	}

	@Override
	public HasAddNewButtonHandlers getAddNewHandler() {
		return null;
	}

	@Override
	public void showAddNewPopup() {
		//CandidateFormPopup form = new CandidateFormPopup(new CandidateFormWidget());
	}

	@Override
	public void addRow(CandidateModel model) {
//		CandidateDisplayWidget widget = new CandidateDisplayWidget(model);
//		widget.setCandidateDisplayWidgetHandler(new CandidateDisplayWidgetHandler() {
//			
//			@Override
//			public void onSaveCompletion() {
//				detailsPanel.clear();
//			}
//			
//			@Override
//			public void onEditClicked(CandidateModel model) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onDeleteClicked(CandidateModel model) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		//detailsPanel.add(widget);
	}

	@Override
	public void load() {
		detailsPanel.load();
	}

	@Override
	public void clear() {
		detailsPanel.clear();
	}

	@Override
	public HasChangeHandlers getYearListHandler() {
		return yearListBox;
	}

	@Override
	public HasOneWidget getConsSearchPanel() {
		return consSearchBox;
	}

	@Override
	public void addToYearList(String name, String value) {
		yearListBox.addItem(name, value);
	}

	@Override
	public String getSelectedYear() {
		return yearListBox.getItemText(yearListBox.getSelectedIndex());
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}

	@Override
	public int getSelectionIndex() {
		return	yearListBox.getSelectedIndex();
	}

	@Override
	public void addToPanel(IsWidget widget) {
		detailsPanel.add(widget);
	}

	@Override
	public HasOneWidget getPollsSearchPanel() {
		return pollsSearchBox;
	}

	@Override
	public void setApprovedCounter(int counter) {
		aRowCounterDiv.setInnerText(""+counter);
	}

	@Override
	public void setRejectedCounter(int counter) {
		rRowCounterDiv.setInnerText(""+counter);
	}

	@Override
	public void setPendingCounter(int counter) {
		pRowCounterDiv.setInnerText(""+counter);
	}

	@Override
	public void showDisplay() {
		infoRow.setAttribute("style", "display:block;");
	}
}
