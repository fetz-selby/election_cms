package com.admin.media.election.client.party.presenter;

import java.util.ArrayList;

import com.admin.media.election.client.CommServiceAsync;
import com.admin.media.election.client.handlers.AddNewButtonHandler;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.shared.PartyModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PartyPresenter implements Presenter{
	private Display view;
	private CommServiceAsync rpc;
	
	public interface Display extends IsWidget{
		Widget asWidget();
		HasAddNewButtonHandlers getAddNewHandler();
		void showAddNewPopup();
		void addToPanel(PartyModel model);
		void load();
		void clear();
	}
	
	public PartyPresenter(CommServiceAsync rpc, Display view){
		this.view = view;
		this.rpc = rpc;
	}
	
	private void bind(){
		view.getAddNewHandler().setAddNewButtonHandler(new AddNewButtonHandler() {
			
			@Override
			public void onButtonClicked() {
				view.showAddNewPopup();
			}
		});
		
		rpc.getParties(new AsyncCallback<ArrayList<PartyModel>>() {
			
			@Override
			public void onSuccess(ArrayList<PartyModel> result) {
				if(result != null){
					for(PartyModel party : result){
						view.addToPanel(party);
					}
					view.load();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void go(HasWidgets screen) {
		screen.clear();
		bind();
		screen.add(view.asWidget());
	}
	
	
}
