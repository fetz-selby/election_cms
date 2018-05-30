package com.admin.media.election.client;

import com.admin.media.election.client.agent.presenter.AgentPresenter;
import com.admin.media.election.client.agent.view.AgentView;
import com.admin.media.election.client.candidates.presenter.CandidatePresenter;
import com.admin.media.election.client.candidates.view.CandidateView;
import com.admin.media.election.client.constituency.presenter.ConstituencyPresenter;
import com.admin.media.election.client.constituency.view.ConstituencyView;
import com.admin.media.election.client.events.AdminEvent;
import com.admin.media.election.client.events.AgentsEvent;
import com.admin.media.election.client.events.CandidatesEvent;
import com.admin.media.election.client.events.ConstituencyEvent;
import com.admin.media.election.client.events.CorrespondentEvent;
import com.admin.media.election.client.events.InboundEvent;
import com.admin.media.election.client.events.PartyEvent;
import com.admin.media.election.client.events.PollsEvent;
import com.admin.media.election.client.events.ToolsEvent;
import com.admin.media.election.client.handlers.AdminEventHandler;
import com.admin.media.election.client.handlers.AgentsEventHandler;
import com.admin.media.election.client.handlers.CandidatesEventHandler;
import com.admin.media.election.client.handlers.ConstituencyEventHandler;
import com.admin.media.election.client.handlers.CorrespondentEventHandler;
import com.admin.media.election.client.handlers.InboundEventHandler;
import com.admin.media.election.client.handlers.PartyEventHandler;
import com.admin.media.election.client.handlers.PollsEventHandler;
import com.admin.media.election.client.handlers.ToolsEventHandler;
import com.admin.media.election.client.inbounds.presenter.InboundsPresenter;
import com.admin.media.election.client.inbounds.view.InboundsView;
import com.admin.media.election.client.interfaces.Presenter;
import com.admin.media.election.client.party.presenter.PartyPresenter;
import com.admin.media.election.client.party.view.PartyView;
import com.admin.media.election.client.polls.presenter.PollsPresenter;
import com.admin.media.election.client.polls.view.PollsView;
import com.admin.media.election.client.system.SystemVariable;
import com.admin.media.election.client.system.User;
import com.admin.media.election.client.tools.presenter.ToolsPresenter;
import com.admin.media.election.client.tools.view.ToolsView;
import com.admin.media.election.client.widgets.UserOptionWidget;
import com.admin.media.election.shared.Constants;
import com.admin.media.election.shared.UserModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class MainController implements Presenter, ValueChangeHandler<String>{
	
	private RootPanel consLink, canLink, partyLink, userOptionArea, toolsLink, pollsLink, agentsLink, inboundLink;
	private CommServiceAsync rpc;
	private HandlerManager eventBus;
	private HasWidgets screen;
	private User user;

	public MainController(RootPanel consLink, RootPanel canLink, RootPanel partyLink, RootPanel pollsLink, RootPanel agentsLink, RootPanel inboundLink, RootPanel toolsLink, RootPanel userOptionArea, CommServiceAsync rpc, HandlerManager eventBus){
		this.consLink = consLink;
		this.canLink = canLink;
		this.partyLink = partyLink;
		this.toolsLink = toolsLink;
		this.pollsLink = pollsLink;
		this.agentsLink = agentsLink;
		this.inboundLink = inboundLink;
		this.userOptionArea = userOptionArea;
		this.rpc = rpc;
		this.eventBus = eventBus;
		
		initEvents();
		doUserSetup();
	}
	
	private void initEvents(){
		if(consLink.getElement() != null && canLink.getElement() != null && partyLink.getElement() != null){
			
			DOM.sinkEvents(consLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(consLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					GWT.log("Cons clicked !!!");
					eventBus.fireEvent(new ConstituencyEvent());
				}
			});
			
			DOM.sinkEvents(canLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(canLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					GWT.log("Can clicked !!!");
					eventBus.fireEvent(new CandidatesEvent());
				}
			});
			
			DOM.sinkEvents(partyLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(partyLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new PartyEvent());
					GWT.log("Party clicked !!!");
				}
			});
			
			DOM.sinkEvents(toolsLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(toolsLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new ToolsEvent());
					GWT.log("Tools clicked !!!");
				}
			});
			
			DOM.sinkEvents(pollsLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(pollsLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new PollsEvent());
					GWT.log("Polls clicked !!!");
				}
			});
			
			DOM.sinkEvents(agentsLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(agentsLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new AgentsEvent());
					GWT.log("Agents clicked !!!");
				}
			});
			
			DOM.sinkEvents(inboundLink.getElement(), Event.ONCLICK);
			DOM.setEventListener(inboundLink.getElement(), new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new InboundEvent());
					GWT.log("Inbound clicked !!!");
				}
			});
		}
	}
	
	private void doUserSetup(){
		rpc.getUserModel(new AsyncCallback<UserModel>() {
			
			@Override
			public void onSuccess(UserModel result) {
				GWT.log("Username is "+result.getUsername());
				if(result != null){
					user = User.getUserInstance(result);
					initUserOption(user.getUserName());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void initUserOption(String username){
		if(userOptionArea != null){
			userOptionArea.clear();
			UserOptionWidget userOption = new UserOptionWidget(username);
			userOptionArea.add(userOption);
		}
	}
	
	private void bind(){
		
		
		History.addValueChangeHandler(this);
		History.newItem(Constants.CONSTITUENCY);
		
		eventBus.addHandler(CorrespondentEvent.TYPE, new CorrespondentEventHandler() {
			
			@Override
			public void onCorrespondentEventClicked(CorrespondentEvent event) {
				History.newItem(Constants.CORRESPONDENT);
			}
		});
		
		eventBus.addHandler(CandidatesEvent.TYPE, new CandidatesEventHandler() {
			
			@Override
			public void onCandidateClicked(CandidatesEvent event) {
				History.newItem(Constants.CANDIDATE);
			}
		});
		
		
		eventBus.addHandler(ConstituencyEvent.TYPE, new ConstituencyEventHandler() {
			
			@Override
			public void onConstituencyClicked(ConstituencyEvent event) {
				History.newItem(Constants.CONSTITUENCY);
			}
		});
		
		eventBus.addHandler(PartyEvent.TYPE, new PartyEventHandler() {
			
			@Override
			public void onPartyClicked(PartyEvent event) {
				History.newItem(Constants.PARTY);
			}
		});
		
		eventBus.addHandler(AdminEvent.TYPE, new AdminEventHandler() {
			
			@Override
			public void onAdminEventClicked(AdminEvent event) {
				History.newItem(Constants.ADMIN);
			}
		});
		
		eventBus.addHandler(ToolsEvent.TYPE, new ToolsEventHandler() {
			
			@Override
			public void onToolsClicked(ToolsEvent event) {
				History.newItem(Constants.TOOLS);
			}
		});
		
		eventBus.addHandler(PollsEvent.TYPE, new PollsEventHandler() {
			
			@Override
			public void onPollsClicked(PollsEvent event) {
				History.newItem(Constants.POLLS);
			}
		});
		
		eventBus.addHandler(AgentsEvent.TYPE, new AgentsEventHandler() {
			
			@Override
			public void onAgentEventClicked(AgentsEvent event) {
				History.newItem(Constants.AGENTS);
			}
		});
		
		eventBus.addHandler(InboundEvent.TYPE, new InboundEventHandler() {
			
			@Override
			public void onInboudEventClicked(InboundEvent event) {
				History.newItem(Constants.INBOUND);
			}
		});
		
		SystemVariable.getInstance().setHandlerManager(eventBus);
		SystemVariable.getInstance().setRPC(rpc);
	}
	
	public void logout(){
		String url = GWT.getHostPageBaseURL() + "index.html";

		if(!GWT.isProdMode()) {
			url += "?gwt.codesvr=127.0.0.1:9997";
		}
		
		Cookies.setCookie("app", "");
		Cookies.removeCookie("app");
		Window.Location.replace(url);
	}
	
	@Override
	public void go(HasWidgets screen) {
		if(Cookies.getCookie("app") != null && Cookies.getCookie("app").equals("47")){
			this.screen = screen;
			bind();
		}else{
			logout();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		Presenter presenter = null;
		if(token.equals(Constants.ADMIN)){
			
		}else if(token.equals(Constants.CANDIDATE)){
			presenter = new CandidatePresenter(user, eventBus, rpc, new CandidateView());
		}else if(token.equals(Constants.CONSTITUENCY)){
			presenter = new ConstituencyPresenter(eventBus, rpc, new ConstituencyView());
		}else if(token.equals(Constants.PARTY)){
			presenter = new PartyPresenter(rpc, new PartyView());
		}else if(token.equals(Constants.TOOLS)){
			presenter = new ToolsPresenter(eventBus, rpc, new ToolsView());
		}else if(token.equals(Constants.POLLS)){
			presenter = new PollsPresenter(user, eventBus, rpc, new PollsView());
		}else if(token.equals(Constants.AGENTS)){
			presenter = new AgentPresenter(user, eventBus, rpc, new AgentView());
		}else if(token.equals(Constants.INBOUND)){
			presenter = new InboundsPresenter(user, eventBus, rpc, new InboundsView());
		}else if(token.equals(Constants.LOGOUT)){
			logout();
		}
		
		if(presenter != null){
			presenter.go(screen);
		}
	}

}
