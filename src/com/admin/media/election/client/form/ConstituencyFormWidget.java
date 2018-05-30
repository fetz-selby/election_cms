package com.admin.media.election.client.form;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.events.ConstituencyEvent;
import com.admin.media.election.client.form.handlers.ConstituencyFormWidgetHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.shared.ConstituencyModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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

public class ConstituencyFormWidget extends PopupPanel {
	private ConstituencyModel model;
	private ConstituencyFormWidgetHandler handler;
	private String constituencyName;
	private ArrayList<String> yearArrayList, seatArrayList;
	private int id,constituencyId;
	
	private static ConstituencyFormWidgetUiBinder uiBinder = GWT
			.create(ConstituencyFormWidgetUiBinder.class);

	interface ConstituencyFormWidgetUiBinder extends
			UiBinder<Widget, ConstituencyFormWidget> {
	}

	@UiField DivElement titleLabel;
	@UiField ListBox yearList, seatWon;
	@UiField SimplePanel searchContainer;
	@UiField TextBox regVotes, rejVotes, castVotes, totalVotes;
	@UiField RadioButton isDeclaredNo, isDeclaredYes;
	@UiField Button actionBtn;
	@UiField Anchor cancelLink;
	
	public ConstituencyFormWidget() {
		add(uiBinder.createAndBindUi(this));
		initPopup();
		initEvent();
		initConstituencyBox();
		initYearList();
		initSeatSelectionList();
	}
	
	public ConstituencyFormWidget(ConstituencyModel model){
		add(uiBinder.createAndBindUi(this));
		this.model = model;
		id = model.getId();
		initPopup();
		initEvent();
		initYearList();
		initSeatSelectionList();
		doInsertion();
	}
	
	private void doInsertion(){
		titleLabel.setInnerText("Edit - Constituency");
		Label consName = new Label(model.getName());
		consName.getElement().setAttribute("style", "padding-top: 8px;font-size: 19px;font-weight: bold");
		searchContainer.setWidget(consName);
		regVotes.setText(model.getRegVotes()+"");
		rejVotes.setText(model.getRejVotes()+"");
		castVotes.setText(model.getCastedVotes()+"");
		totalVotes.setText(model.getTotalVotes()+"");
		
		if(model.getIsDeclared().equals("Y")){
			isDeclaredYes.setValue(true);
		}else if(model.getIsDeclared().equals("N")){
			isDeclaredNo.setValue(true);
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
				if(handler != null){
					handler.onConstituencySaveClicked();
				}
				gatherEntryAndSave();
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onConstituencyCancelClicked();
				}
				hide();
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
	
	private void initSeatSelectionList(){
		SystemVariable.getInstance().getRPC().getAllParties(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					seatWon.addItem("Select Party", "0");
					seatArrayList = new ArrayList<String>();
					seatArrayList.add("Select Party");
					for(Integer id : result.keySet()){
						String partyName = result.get(id);
						seatWon.addItem(partyName, id+"");
						seatArrayList.add(partyName);
					}
					if(id > 0){
						seatWon.setItemSelected(seatArrayList.indexOf(model.getSeatWonName()), true);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initConstituencyBox(){
		SystemVariable.getInstance().getRPC().getAllParentConstituencies(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(final HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					for(Integer id : result.keySet()){
						String parentConstituency = result.get(id);
						oracle.add(parentConstituency);
					}
					
					SuggestBox constituencySearch = new SuggestBox(oracle, new GenericSearchBox());
					constituencySearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							constituencyName = event.getSelectedItem().getReplacementString();
							constituencyId = fetchId(constituencyName, result);
						}
					});
					searchContainer.setWidget(constituencySearch);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	public void setConstituencyFormWidgetHandler(ConstituencyFormWidgetHandler handler){
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
		int regVotes = 0;
		if(this.regVotes.getText().equals("")){
			regVotes = 0;
		}else{
			regVotes = Integer.parseInt(this.regVotes.getText());
		}
		
		int rejVotes = 0;
		if(this.rejVotes.getText().equals("")){
			rejVotes = 0;
		}else{
			rejVotes = Integer.parseInt(this.rejVotes.getText());
		}
		
		int totalVotes = 0;
		if(this.totalVotes.getText().equals("")){
			totalVotes = 0;
		}else{
			totalVotes = Integer.parseInt(this.totalVotes.getText());
		}
		
		int castedVotes = 0;
		if(this.castVotes.getText().equals("")){
			castedVotes = 0;
		}else{
			castedVotes = Integer.parseInt(this.castVotes.getText());
		}
		
		int parentId = constituencyId;
		int seatWonId = Integer.parseInt(seatWon.getValue(seatWon.getSelectedIndex()));
		String isDeclared = "";
		if(isDeclaredNo.getValue()){
			isDeclared = "N";
		}else{
			isDeclared = "Y";
		}
		
		
		if(id == 0){
			//Add new Constituency
			ConstituencyModel model = new ConstituencyModel(0, year, regVotes, rejVotes, totalVotes, castedVotes, parentId, seatWonId, name, isDeclared);
			SystemVariable.getInstance().getRPC().getSaveConstituency(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new ConstituencyEvent());
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
			ConstituencyModel model = new ConstituencyModel(id, year, regVotes, rejVotes, totalVotes, castedVotes, parentId, seatWonId, name, isDeclared);
			SystemVariable.getInstance().getRPC().getConstituencyUpdate(model, new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						hide();
						SystemVariable.getInstance().fireSystemEvent(new ConstituencyEvent());
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
