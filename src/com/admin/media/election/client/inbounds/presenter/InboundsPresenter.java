package com.admin.media.election.client.inbounds.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.CommServiceAsync;
import com.admin.media.election.client.handlers.HasAddNewButtonHandlers;
import com.admin.media.election.client.handlers.InboundDisplayWidgetHandler;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.client.system.User;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.client.widgets.InboundsDisplayWidget;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.Constants;
import com.admin.media.election.shared.InboundModel;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public class InboundsPresenter implements Presenter{
	private CommServiceAsync rpc;
	private Display view;
	private HandlerManager eventBus;
	private HashMap<Integer, String> consMap, pollMap;
	private Timer inboundChangeChecker;
	private boolean isTimerSet = false;
	private int constituencyId = 0, pollId = 0;
	private int approve = 0, reject = 0, pending = 0;

	public interface Display extends IsWidget{
		Widget asWidget();
		HasAddNewButtonHandlers getAddNewHandler();
		HasClickHandlers getSearchButton();
		HasChangeHandlers getYearListHandler();
		HasOneWidget getConsSearchPanel();
		HasOneWidget getPollsSearchPanel();
		String getSelectedYear();
		int getSelectionIndex();
		void addToYearList(String name, String value);
		void showAddNewPopup();
		void addRow(CandidateModel model);
		void load();
		void clear();
		void addToPanel(IsWidget widget);
		void setApprovedCounter(int counter);
		void setRejectedCounter(int counter);
		void setPendingCounter(int counter);
		void showDisplay();
	}

	public InboundsPresenter(User user, HandlerManager eventBus, CommServiceAsync rpc, Display view){
		this.view = view;
		this.rpc = rpc;
		this.eventBus = eventBus;
	}

	private void bind(){
		initYearList();
		initSearchButton();
	}

	private void addToRow(InboundModel model){
		InboundsDisplayWidget widget = new InboundsDisplayWidget(model);
		widget.setInboundsDisplayWidgetHandler(new InboundDisplayWidgetHandler() {

			@Override
			public void onDetailClicked(InboundModel model) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onApproved() {
				view.getSearchButton().fireEvent(new ClickEvent(){});				
			}

			@Override
			public void onRejected() {
				view.getSearchButton().fireEvent(new ClickEvent(){});				
			}
		});

		view.addToPanel(widget);
	}

	private void initSearchButton(){
		view.getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doCall();
				startPeriodicCheck();
			}
		});
	}

	private void doCall(){
		int year = Integer.parseInt(view.getSelectedYear());
		if(constituencyId > 0 && year > 0 && pollId > 0){
			doSearchWithPollId();
		}else if(constituencyId > 0 && year > 0){
			doSearchWithoutPollId();
		}
	}

	private void startPeriodicCheck(){
		if(!isTimerSet){
			inboundChangeChecker = new Timer() {

				@Override
				public void run() {
					rpc.isInboundChange(new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							if(result){
								doCall();
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
				}
			};

			inboundChangeChecker.scheduleRepeating(30*1000);
			if(!inboundChangeChecker.isRunning()){
				inboundChangeChecker.run();
			}
			isTimerSet = true;
		}
	}

	private void doSearchWithoutPollId(){
		rpc.getInbounds(constituencyId, view.getSelectedYear(), new AsyncCallback<ArrayList<InboundModel>>() {

			@Override
			public void onSuccess(ArrayList<InboundModel> result) {
				if(result != null){
					view.clear();
					resetValues();
					for(InboundModel inbound : result){
						//view.addRow(candidate);
						addToRow(inbound);
						doRowValueSetting(inbound);
					}
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {

						@Override
						public void execute() {
							view.load();
							doFinalRowValues();
							view.showDisplay();
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


	private void doSearchWithPollId(){
		rpc.getInboundsWithPollId(constituencyId, pollId, view.getSelectedYear(), new AsyncCallback<ArrayList<InboundModel>>() {

			@Override
			public void onSuccess(ArrayList<InboundModel> result) {
				if(result != null){
					view.clear();
					resetValues();
					for(InboundModel inbound : result){
						//view.addRow(candidate);
						addToRow(inbound);
						doRowValueSetting(inbound);
					}
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {

						@Override
						public void execute() {
							view.load();
							doFinalRowValues();
							view.showDisplay();
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

	private void doRowValueSetting(InboundModel model){
		if(model.getApproveState().trim().equalsIgnoreCase(Constants.APPROVED)){
			approve ++;
		}else if(model.getApproveState().trim().equalsIgnoreCase(Constants.REJECTED)){
			reject ++;
		}else if(model.getApproveState().trim().equalsIgnoreCase(Constants.PENDING)){
			pending ++;
		}
	}

	private void resetValues(){
		approve = 0;
		reject = 0;
		pending = 0;
	}

	private void doFinalRowValues(){
		view.setApprovedCounter(approve);
		view.setRejectedCounter(reject);
		view.setPendingCounter(pending);
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

							initPollsSearchWidget(constituencyId);
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

	private void initPollsSearchWidget(int constituencyId){
		rpc.getAllPollingStations(constituencyId, new AsyncCallback<HashMap<Integer,String>>() {

			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					pollMap = new HashMap<Integer, String>();
					for(Integer id : result.keySet()){
						String poll = result.get(id);
						oracle.add(poll);
						pollMap.put(id, poll);
					}

					SuggestBox pollsSearch = new SuggestBox(oracle, new GenericSearchBox());
					pollsSearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							String text = event.getSelectedItem().getReplacementString();
							pollId = getPollingId(text);
						}
					});
					view.getPollsSearchPanel().setWidget(pollsSearch);
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

	private int getPollingId(String pollName){
		if(pollMap != null){
			for(Integer id : pollMap.keySet()){
				String poll = pollMap.get(id);
				if(pollName.trim().equals(poll)){
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
