package com.admin.media.election.client.constituency.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.admin.media.election.client.CommServiceAsync;
import com.admin.media.election.client.handlers.AddNewButtonHandler;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.shared.ConstituencyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class ConstituencyPresenter implements Presenter{

	private Display view;
	private CommServiceAsync rpc;
	private HandlerManager eventBus;
	
	public interface Display extends IsWidget{
		Widget asWidget();
		HasAddNewButtonHandlers getAddNewHandler();
		HasClickHandlers getSearchHandler();
		String getYearValue();
		String getIsDeclaredValue();
		
		void showAddNewPopup();
		void addToPanel(ConstituencyModel model);
		void addToYear(String name, String value);
		void addToIsDeclared(String name, String value);
		
		void load();
		void clearPanel();
	}
	
	public ConstituencyPresenter(HandlerManager eventBus, CommServiceAsync rpc, Display view){
		this.eventBus = eventBus;
		this.rpc = rpc;
		this.view = view;
	}
	
	private void bind(){
		initYearList();
		initIsDeclaredList();
		
		view.getAddNewHandler().setAddNewButtonHandler(new AddNewButtonHandler() {
			
			@Override
			public void onButtonClicked() {
				view.showAddNewPopup();
			}
		});
		
		view.getSearchHandler().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doDataGathering();
			}
		});
	}
	
	private void initYearList(){
		rpc.getAllYears(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					for(Integer year : result.keySet()){
						view.addToYear(""+year, ""+year);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initIsDeclaredList(){
		String[] selectables = {"Select State", "Declared", "NOT Declared"};
		String[] selectableValues = {"", "Y", "N"};
		
		for(int i = 0; i < selectables.length; i++){
			view.addToIsDeclared(selectables[i], selectableValues[i]);
		}
	}
	
	private void doDataGathering(){
		String year = view.getYearValue();
		String isDeclared = view.getIsDeclaredValue();
		
		GWT.log("Year is "+year+", and isDeclared is "+isDeclared);
		
		rpc.getConstituencies(year, isDeclared, new AsyncCallback<ArrayList<ConstituencyModel>>() {
			
			@Override
			public void onSuccess(ArrayList<ConstituencyModel> result) {
				if(result != null){
					view.clearPanel();
//					for(ConstituencyModel model : result){
//						view.addToPanel(model);
//						GWT.log("Cons added !!!");
//					}
					doArrange(result);
					view.load();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void doArrange(ArrayList<ConstituencyModel> modelList){
		HashMap<String, ConstituencyModel> consMap = new HashMap<String, ConstituencyModel>();
		TreeSet<String> consSet = new TreeSet<String>();
		
		for(ConstituencyModel model : modelList){
			String consName = model.getName().trim();
			consSet.add(consName);
			consMap.put(consName, model);
		}
		
		for(String cons : consSet){
			view.addToPanel(consMap.get(cons));
		}
	}
	
	@Override
	public void go(HasWidgets screen) {
		screen.clear();
		bind();
		screen.add(view.asWidget());
	}

}
