package com.admin.media.election.client.tools.view;

import com.admin.media.election.client.tools.presenter.ToolsPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolsView extends Composite implements ToolsPresenter.Display{

	private static ToolsViewUiBinder uiBinder = GWT
			.create(ToolsViewUiBinder.class);

	interface ToolsViewUiBinder extends UiBinder<Widget, ToolsView> {
	}
	
	@UiField SimplePanel seatContainer, nameContainer;

	public ToolsView() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){

	}

	@Override
	public void setSeatWidget(IsWidget widget) {
		seatContainer.setWidget(widget);
	}

	@Override
	public void setNameWidget(IsWidget widget) {
		nameContainer.setWidget(widget);
	}

}
