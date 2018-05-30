package com.admin.media.election.client.form;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.events.PollsEvent;
import com.admin.media.election.client.form.handlers.PollsFormHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.shared.PollModel;
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

public class PollsFormWidget extends PopupPanel {
	private PollModel model;
	private PollsFormHandler handler;
	private String constituencyName;
	private ArrayList<String> yearArrayList;
	private int id,constituencyId;

	private static PollsFormWidgetUiBinder uiBinder = GWT
			.create(PollsFormWidgetUiBinder.class);

	interface PollsFormWidgetUiBinder extends UiBinder<Widget, PollsFormWidget> {
	}

	@UiField DivElement titleLabel;
	@UiField ListBox yearList;
	@UiField SimplePanel searchContainer;
	@UiField TextBox pollNameWidget;
	@UiField RadioButton isActive, isNotActive;
	@UiField Button actionBtn;
	@UiField Anchor cancelLink;
	
	public PollsFormWidget() {
		add(uiBinder.createAndBindUi(this));
		initPopup();
		initEvent();
		//initConstituencyBox();
		initYearList();
	}
	
	public PollsFormWidget(PollModel model){
		add(uiBinder.createAndBindUi(this));
		this.model = model;
		
		id = model.getId();
		constituencyId = model.getConstituencyId();
		
		initPopup();
		initEvent();
		initYearList();
		doInsertion();
	}

	private void doInsertion(){
		titleLabel.setInnerText("Edit - Polling Station");
		Label consName = new Label(model.getConstituencyName());
		consName.getElement().setAttribute("style", "padding-top: 8px;font-size: 19px;font-weight: bold");
		searchContainer.setWidget(consName);
		pollNameWidget.setText(model.getName());
		if(model.getStatus().equals("A")){
			isActive.setValue(true);
		}else if(model.getStatus().equals("D")){
			isNotActive.setValue(true);
		}
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
					handler.onPollsSaveClicked();
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onPollsCancelClicked();
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

//	private void initConstituencyBox(){
//		SystemVariable.getInstance().getRPC().getAllParentConstituencies(new AsyncCallback<HashMap<Integer,String>>() {
//			
//			@Override
//			public void onSuccess(final HashMap<Integer, String> result) {
//				if(result != null){
//					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
//					for(Integer id : result.keySet()){
//						String parentConstituency = result.get(id);
//						oracle.add(parentConstituency);
//					}
//					
//					SuggestBox constituencySearch = new SuggestBox(oracle, new GenericSearchBox());
//					constituencySearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//						
//						@Override
//						public void onSelection(SelectionEvent<Suggestion> event) {
//							constituencyName = event.getSelectedItem().getReplacementString();
//							constituencyId = fetchId(constituencyName, result);
//						}
//					});
//					searchContainer.setWidget(constituencySearch);
//				}
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//
//	}
	
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
	
	public void setPollFormWidgetHandler(PollsFormHandler handler){
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
		int year = Integer.parseInt(yearList.getValue(yearList.getSelectedIndex()));
		
		int parentId = constituencyId;
		String pollName = pollNameWidget.getText().toLowerCase().trim();
		String isDeclared = "";
		if(isNotActive.getValue()){
			isDeclared = "D";
		}else{
			isDeclared = "A";
		}
				
		if(id == 0){
			//Add new Polls
			PollModel model = new PollModel(0, pollName, constituencyId, name, ""+year, isDeclared);
			SystemVariable.getInstance().getRPC().getSavePoll(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new PollsEvent());
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
			PollModel model = new PollModel(id, pollName, constituencyId, name, ""+year, isDeclared);
			SystemVariable.getInstance().getRPC().getPollUpdate(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new PollsEvent());
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
