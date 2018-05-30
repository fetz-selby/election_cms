package com.admin.media.election.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TinyScrollPanel extends Composite {
	private boolean isHorizontal = false;
	private static TinyScrollPanelUiBinder uiBinder = GWT
			.create(TinyScrollPanelUiBinder.class);

	interface TinyScrollPanelUiBinder extends UiBinder<Widget, TinyScrollPanel> {
	}

	@UiField DivElement scrollbar1Field, scrollbarField, trackField, thumbField, endField, viewportField, overviewField;
	@UiField FlowPanel container;
	@UiField HTMLPanel mainTiny;
	
	public TinyScrollPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	public void add(IsWidget widget){
		if(widget != null){
			container.add(widget);
		}
	}
	
	public void removeWidget(Widget widget){
		container.remove(widget);
	}
	
	private void initComponents(){
		activateMainElement(false);
		scrollbar1Field.setAttribute("id", "scrollbar1");
		scrollbarField.setAttribute("class", "scrollbar");
		trackField.setAttribute("class", "track");
		thumbField.setAttribute("class", "thumb");
		endField.setAttribute("class", "end");
		viewportField.setAttribute("class", "viewport");
		overviewField.setAttribute("class", "overview");
	}
	
	private void activateMainElement(boolean isActive){
		if(isActive){
			mainTiny.getElement().setAttribute("style", "display:block");
		}else{
			mainTiny.getElement().setAttribute("style", "display:none");
		}
	}
	
	public void setTinySize(String width, String height){
		scrollbar1Field.setAttribute("style", "width:"+width);
		viewportField.setAttribute("style", "width:"+getComputedSize(width)+"px; height:"+height);
		//viewportField.setAttribute("style", "width:"+width+"; height:"+height);

		scrollbarField.setAttribute("style", "height:"+height);
		container.setWidth(width);
	}
	
	public void clear(){
		container.clear();
	}
	
	public void setHorizontal(){
		isHorizontal = true;
	}
	
	public void setVertical(){
		isHorizontal = false;
	}
	
	private int getComputedSize(String width){
		final int offset = 20;
		
		width = width.substring(0, width.length()-2);
		GWT.log("Computed width is "+width);
		return Integer.parseInt(width)-offset;
	}
	
	@SuppressWarnings("deprecation")
	public void load(){
		activateMainElement(true);
		DeferredCommand.add(new Command() {
			@Override
			public void execute() {
				initTiny(isHorizontal);
			}
		});
	}
	
	private native void initTiny(boolean isHorizontal)/*-{
	//$wnd.$(document).ready(function(){
		if(isHorizontal){
			$wnd.$('#scrollbar1').tinyscrollbar({ axis: 'x'});
		}else{
			$wnd.$('#scrollbar1').tinyscrollbar();
		}
	//});
	}-*/;

}
