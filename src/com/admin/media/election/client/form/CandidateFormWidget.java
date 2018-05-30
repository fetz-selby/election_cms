package com.admin.media.election.client.form;

import java.util.ArrayList;
import java.util.HashMap;

import com.admin.media.election.client.form.handlers.CandidateFormHandler;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.widgets.GenericSearchBox;
import com.admin.media.election.shared.CandidateModel;
import com.admin.media.election.shared.Constants;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CandidateFormWidget extends Composite {
	private CandidateModel model;
	private CandidateFormHandler handler;
	private Hidden typeHidden, imgChecker, constituencyHidden, partyHidden, yearHidden, idSwitchHidden, idHidden;
	private HashMap<Integer, String> constituencyMap;
	private int constituencyId = 0, id = 0;
	private ArrayList<Integer> yearListArray, partyList;
	private boolean isImageAttached = false, hasId = false;
	
	private static OrphanFormUiBinder uiBinder = GWT
			.create(OrphanFormUiBinder.class);

	interface OrphanFormUiBinder extends UiBinder<Widget, CandidateFormWidget> {
	}

	@UiField FileUpload avatarLink;
	@UiField Image avatar;
	@UiField Anchor cancelLink;
	@UiField Button actionBtn;
	@UiField FlowPanel hiddenPanel;
	@UiField SimplePanel searchContainer, hiddenUploadPanel;
	@UiField TextBox canName, votes;
	@UiField ListBox party, yearList;
	@UiField RadioButton pRadio, mRadio;
	@UiField FormPanel previewForm;

	
	public CandidateFormWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		initForm();
		initEvent();
		loadYear();
		initComponent();
	}
	
	public CandidateFormWidget(CandidateModel model){
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;

		initForm();
		initEvent();
		loadYear();
		hasId = true;
	}
	
	private void doInit(){
		canName.setText(model.getName());
		votes.setText(""+model.getVotes());
		constituencyId = model.getConstituencyId();
		id = model.getId();
		
		int year = Integer.parseInt(model.getYear());
		if(yearListArray.contains(year)){
			int index = yearListArray.indexOf(year);
			yearList.setItemSelected(index, true);
		}
		
		int partyId = model.getPartyId();
		if(partyList.contains(partyId)){
			int index = partyList.indexOf(partyId);
			party.setItemSelected(index, true);
		}
		
		String type = model.getGroup();
		if(type.equals(Constants.PARLIAMENTARY)){
			mRadio.setValue(true);
		}else{
			pRadio.setValue(true);
		}
		
		String imageUrl = model.getAvatar();
		if(imageUrl.equals(Constants.NO_IMAGE)){
			avatar.setUrl(Constants.DEFAULT_IMAGE);
		}else{
			avatar.setUrl(imageUrl);
		}
	}
	
	private void initComponent(){
		votes.setText("0");
	}
	
	private void loadParty(){
		SystemVariable.getInstance().getRPC().getAllParties(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					party.addItem("Select Party", "0");
					partyList = new ArrayList<Integer>();
					partyList.add(0);
					for(Integer id : result.keySet()){
						String partyName = result.get(id);
						party.addItem(partyName, ""+id);
						partyList.add(id);
					}
					
					if(model != null){
						doInit();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void loadYear(){
		SystemVariable.getInstance().getRPC().getAllYears(new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					yearList.addItem("Select Year", "0");
					yearListArray = new ArrayList<Integer>();
					yearListArray.add(0);
					for(Integer year : result.keySet()){
						yearList.addItem(""+year, ""+year);
						yearListArray.add(year);
					}
					
					//Add the change-handler
					yearList.addChangeHandler(new ChangeHandler() {
						
						@Override
						public void onChange(ChangeEvent event) {
							String selectedValue = yearList.getValue(yearList.getSelectedIndex());
							if(Integer.parseInt(selectedValue) > 0){
								loadConstituencies(selectedValue);
							}
						}
					});
					loadParty();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void loadConstituencies(String year){
		SystemVariable.getInstance().getRPC().getAllConstituencies(year, new AsyncCallback<HashMap<Integer,String>>() {
			
			@Override
			public void onSuccess(HashMap<Integer, String> result) {
				if(result != null){
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
					constituencyMap = new HashMap<Integer, String>();
					
					for(Integer id : result.keySet()){
						String constituency = result.get(id);
						constituencyMap.put(id, constituency);
						oracle.add(constituency);
					}
					
					SuggestBox constituencySearch = new SuggestBox(oracle, new GenericSearchBox());
					constituencySearch.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							String searchConstituency = event.getSelectedItem().getReplacementString();
							constituencyId = getConstituencyId(searchConstituency);
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
	
	private int getConstituencyId(String constituency){
		for(Integer id : constituencyMap.keySet()){
			String tmpConstituency = constituencyMap.get(id);
			if(tmpConstituency.equals(constituency)){
				return id;
			}
		}
		
		return 0;
	}
	
	private void initEvent(){
		avatarLink.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				previewForm.submit();
			}
		});
		
		actionBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					if(isValidGatheredInfo()){
						handler.onSubmitClicked();
					}
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}
			}
		});
	}
	
	private void initForm(){
		previewForm.setAction(GWT.getModuleBaseURL()+"preview");
		previewForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		previewForm.setMethod(FormPanel.METHOD_POST);
		previewForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String imageUrl = event.getResults();
				
				if(imageUrl.length() > 0){
					GWT.log("image url is "+imageUrl);
					avatar.setUrl(getProcessedImage(imageUrl));
					isImageAttached = true;
				}
				
			}
		});
		
		typeHidden = new Hidden();
		imgChecker = new Hidden();
		constituencyHidden = new Hidden();
		partyHidden = new Hidden();
		yearHidden = new Hidden();
		idSwitchHidden = new Hidden();
		idHidden = new Hidden();
		
		typeHidden.setName("type");
		imgChecker.setName("checker");
		constituencyHidden.setName("consId");
		partyHidden.setName("partyId");
		yearHidden.setName("year");
		idSwitchHidden.setName("idswitch");
		idHidden.setName("id");
		
		hiddenPanel.add(typeHidden);
		hiddenPanel.add(imgChecker);
		hiddenPanel.add(constituencyHidden);
		hiddenPanel.add(partyHidden);
		hiddenPanel.add(yearHidden);
		hiddenPanel.add(idSwitchHidden);
		hiddenPanel.add(idHidden);
	}

	private String getProcessedImage(String unProcessedImage){
		HTML html = new HTML(unProcessedImage);
		GWT.log("Object text is "+html.getText());
			
		return html.getText();
	}
	
	private boolean isValidGatheredInfo(){
		if(mRadio.getValue()){
			typeHidden.setValue(Constants.PARLIAMENTARY);
		}else{
			typeHidden.setValue(Constants.PRESIDENTIAL);
		}
		
		if(constituencyId == 0){
			return false;
		}else{
			constituencyHidden.setValue(""+constituencyId);
		}
		
		if(party.getValue(party.getSelectedIndex()).equals("0")){
			return false;
		}else{
			partyHidden.setValue(party.getValue(party.getSelectedIndex()));
		}
		
		if(yearList.getValue(yearList.getSelectedIndex()).equals("0")){
			return false;
		}else{
			yearHidden.setValue(yearList.getValue(yearList.getSelectedIndex()));
		}
		
		if(isImageAttached){
			imgChecker.setValue("on");
		}else{
			imgChecker.setValue("off");
		}
		
		if(canName.getText().length() < 2){
			return false;
		}
		
		if(hasId){
			idSwitchHidden.setValue("on");
			idHidden.setValue(""+id);
		}else{
			idSwitchHidden.setValue("off");
		}
		
		switchBackFormPanel();
		return true;
	}
	
	private void switchBackFormPanel(){
		hiddenUploadPanel.setWidget(avatarLink);
		previewForm.removeFromParent();
	}
	
	public void setCandidateFormHandler(CandidateFormHandler handler){
		this.handler = handler;
	}
}
