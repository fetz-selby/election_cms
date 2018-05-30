package com.admin.media.election.client.polls.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.CommServiceAsync;
import com.admin.media.election.client.form.PollsFormWidget;
import com.admin.media.election.client.form.handlers.PollsFormHandler;
import com.admin.media.election.client.handlers.AddNewButtonHandler;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.handlers.PollsDisplayWidgetHandler;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.client.system.User;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.client.widgets.PollsDisplayWidget;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.PollModel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public class PollsPresenter implements Presenter{

	private CommServiceAsync rpc;
	private Display view;
	private HandlerManager eventBus;
	private HashMap<Integer, String> consMap;
	private int constituencyId = 0;
	
	public interface Display extends IsWidget{
		Widget asWidget();
		HasAddNewButtonHandlers getAddNewHandler();
		HasClickHandlers getSearchButton();
		HasChangeHandlers getYearListHandler();
		HasOneWidget getConsSearchPanel();
		String getSelectedYear();
		int getSelectionIndex();
		void addToYearList(String name, String value);
		void showAddNewPopup();
		void addRow(CandidateModel model);
		void load();
		void clear();
		void addToPanel(IsWidget widget);
		void setRowCounter(int totalRow);
	}
	
	
	public PollsPresenter(User user, HandlerManager eventBus, CommServiceAsync rpc, Display view){
		this.view = view;
		this.rpc = rpc;
		this.eventBus = eventBus;
	}
	
	private void bind(){
		initYearList();
		initSearchButton();
		
		view.getAddNewHandler().setAddNewButtonHandler(new AddNewButtonHandler() {
			
			@Override
			public void onButtonClicked() {
				PollsFormWidget form = new PollsFormWidget();
				form.setPollFormWidgetHandler(new PollsFormHandler() {
					
					@Override
					public void onPollsSaveClicked() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onPollsCancelClicked() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	private void addToRow(PollModel model){
		PollsDisplayWidget widget = new PollsDisplayWidget(model);
		widget.setPollsDisplayWidgetHandler(new PollsDisplayWidgetHandler() {
			
			@Override
			public void onSaveCompletion() {
				view.getSearchButton().fireEvent(new ClickEvent(){});
			}
			
			@Override
			public void onEditClicked(PollModel model) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDeleteClicked(PollModel model) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.addToPanel(widget);
	}
	
	private void initSearchButton(){
		view.getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int year = Integer.parseInt(view.getSelectedYear());
				if(constituencyId > 0 && year > 0){
					rpc.getPolls(constituencyId, view.getSelectedYear(), new AsyncCallback<ArrayList<PollModel>>() {
						
						@Override
						public void onSuccess(ArrayList<PollModel> result) {
							if(result != null){
								view.setRowCounter(result.size());
								view.clear();
								for(PollModel poll : result){
									//view.addRow(candidate);
									addToRow(poll);
								}
								Scheduler.get().scheduleDeferred(new ScheduledCommand() {
									
									@Override
									public void execute() {
										view.load();
									}
								});
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
	}
	
	
	private void initConsSearchWidget(String year){
		rpc.getAllConstituencies(year, new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					consMap = new HashMap<Integer, String>();
					for(Integer id : result.keySet()){
						String constituency = result.get(id);
						oracle.add(constituency);
						consMap.put(id, constituency);
					}
					
					SuggestBox consSearch = new SuggestBox(oracle, new GenericSearchBox());
					consSearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							String text = event.getSelectedItem().getReplacementString();
							constituencyId = getConstituencyId(text);
						}
					});
					view.getConsSearchPanel().setWidget(consSearch);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private int getConstituencyId(String consName){
		if(consMap != null){
			for(Integer id : consMap.keySet()){
				String constituency = consMap.get(id);
				if(consName.trim().equals(constituency)){
					return id;
				}
			}
		}
		return 0;
	}
	
	private void initYearList(){
		rpc.getAllYears(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					view.addToYearList("Select Year", "0");
					for(Integer id: result.keySet()){
						view.addToYearList(""+id, result.get(id));
					}
					initYearChangeHandler();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initYearChangeHandler(){
		view.getYearListHandler().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(!view.getSelectedYear().equalsIgnoreCase("Select Year")){
					initConsSearchWidget(view.getSelectedYear());
				}else{
					loadDummy();
				}
			}
		});
	}
	
	private void loadDummy(){
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("");
		SuggestBox consSearch = new SuggestBox(oracle, new GenericSearchBox());
		view.getConsSearchPanel().setWidget(consSearch);
	}

	@Override
	public void go(HasWidgets screen) {
		screen.clear();
		bind();
		screen.add(view.asWidget());		
	}

}
