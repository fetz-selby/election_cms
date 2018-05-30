package com.admin.media.election.client.form;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.events.AgentsEvent;
import com.admin.media.election.client.form.handlers.AgentsFormHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.shared.AgentModel;
import com.admin.media.election.shared.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AgentFormWidget extends PopupPanel {
	private AgentModel model;
	private AgentsFormHandler handler;
	private String constituencyName, pollName;
	private ArrayList<String> yearArrayList;
	private int id, constituencyId, pollId;

	private static AgentFormWidgetUiBinder uiBinder = GWT
			.create(AgentFormWidgetUiBinder.class);

	interface AgentFormWidgetUiBinder extends UiBinder<Widget, AgentFormWidget> {
	}

	@UiField DivElement titleLabel;
	@UiField ListBox yearList;
	@UiField SimplePanel searchContainer, pollContainer;
	@UiField TextBox agentNameWidget, msisdnWidget, codeWidget;
	@UiField RadioButton isActive, isNotActive;
	@UiField Button actionBtn;
	@UiField Anchor cancelLink;
	
	public AgentFormWidget() {
		add(uiBinder.createAndBindUi(this));
		initPopup();
		initEvent();
		//initConstituencyBox();
		initYearList();
	}
	
	public AgentFormWidget(AgentModel model){
		add(uiBinder.createAndBindUi(this));
		this.model = model;
		
		id = model.getId();
		constituencyId = model.getConsId();
		pollId = model.getPollId();
		
		initPopup();
		initEvent();
		initYearList();
		doInsertion();
	}

	private void doInsertion(){
		titleLabel.setInnerText("Edit - Agent");
		Label consName = new Label(model.getConstituencyName());
		consName.getElement().setAttribute("style", "padding-top: 8px;font-size: 19px;font-weight: bold");
		
		Label pollName = new Label(model.getPollName());
		pollName.getElement().setAttribute("style", "padding-top: 8px;font-size: 19px;font-weight: bold");
		
		searchContainer.setWidget(consName);
		pollContainer.setWidget(pollName);
		agentNameWidget.setText(model.getName());
		msisdnWidget.setText(model.getMsisdn());
		codeWidget.setText(model.getCode());
		if(model.getStatus().equals("A")){
			isActive.setValue(true);
		}else if(model.getStatus().equals("D")){
			isNotActive.setValue(true);
		}
		
		yearList.setEnabled(false);
		
		
	}
	
	private void initPopup(){
		setStyleName("popup-style");
		setGlassStyleName("glassPanel");
		center();
		setAnimationEnabled(true);
		setGlassEnabled(true);
		setModal(true);
	}
	
	private void initEvent(){
		actionBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				gatherEntryAndSave();

				if(handler != null){
					handler.onAgentsSaveClicked();
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onAgentsCancelClicked();
				}
				hide();
			}
		});
		
		yearList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				initConsSearchWidget(yearList.getValue(yearList.getSelectedIndex()));				
			}
		});
		
	}
	
	private void initYearList(){
		SystemVariable.getInstance().getRPC().getAllYears(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					yearArrayList = new ArrayList<String>();
					for(Integer value : result.keySet()){
						yearList.addItem(""+value, ""+value);
						yearArrayList.add(""+value);
					}
					if(id > 0){
						yearList.setItemSelected(yearArrayList.indexOf(model.getYear()+""), true);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initConsSearchWidget(String year){
		SystemVariable.getInstance().getRPC().getAllConstituencies(year, new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(final HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					for(Integer id : result.keySet()){
						String constituency = result.get(id);
						oracle.add(constituency);
					}
					
					SuggestBox consSearch = new SuggestBox(oracle, new GenericSearchBox());
					consSearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							constituencyName = event.getSelectedItem().getReplacementString();
							constituencyId = fetchId(constituencyName, result);
							
							initPollsSearchWidget(constituencyId);
						}
					});
					searchContainer.setWidget(consSearch);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initPollsSearchWidget(int constituencyId){
		SystemVariable.getInstance().getRPC().getAllPollingStations(constituencyId, new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(final HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					for(Integer id : result.keySet()){
						String poll = result.get(id);
						oracle.add(poll);
					}
					
					SuggestBox pollsSearch = new SuggestBox(oracle, new GenericSearchBox());
					pollsSearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							pollName = event.getSelectedItem().getReplacementString();
							pollId = fetchId(pollName, result);
						}
					});
					pollContainer.setWidget(pollsSearch);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setAgentsFormWidgetHandler(AgentsFormHandler handler){
		this.handler = handler;
	}
	
	private int fetchId(String parentConstituency, HashMap<Integer, String> parentConstituencyMap){
		if(parentConstituencyMap != null){
			for(Integer id : parentConstituencyMap.keySet()){
				String tmpParentConstituency = parentConstituencyMap.get(id);
				if(tmpParentConstituency.equals(parentConstituency)){
					return id;
				}
			}
		}
		return 0;
	}
	
	private void gatherEntryAndSave(){
		String name = constituencyName;
		String year = yearList.getValue(yearList.getSelectedIndex());
		String pin = codeWidget.getText().toUpperCase().trim();
		int parentId = constituencyId;
		String agentName = agentNameWidget.getText().toLowerCase().trim();
		String msisdn = msisdnWidget.getText().toLowerCase().trim();
		String isDeclared = "";
		if(isNotActive.getValue()){
			isDeclared = "D";
		}else{
			isDeclared = "A";
		}
				
		if(id == 0){
			//Add new Polls
			//AgentModel model = new AgentModel(0, pollId, msisdn, agentName, pollName, name, year, isDeclared);
			
			AgentModel model = new AgentModel(0, pollId, constituencyId, msisdn, agentName, pollName, constituencyName, year, isDeclared, pin.isEmpty()?Constants.DEFAULT_PIN:pin);

			SystemVariable.getInstance().getRPC().getSaveAgent(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new AgentsEvent());
						SystemVariable.getInstance().refreshPage();
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			//Do updating
			
			AgentModel model = new AgentModel(id, pollId, constituencyId, msisdn, agentName, pollName, constituencyName, year, isDeclared, pin.isEmpty()?Constants.DEFAULT_PIN:pin);
			SystemVariable.getInstance().getRPC().getAgentUpdate(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new AgentsEvent());
					}					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

}
