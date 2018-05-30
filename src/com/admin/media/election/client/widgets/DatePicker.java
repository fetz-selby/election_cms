package com.admin.media.election.client.widgets;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.TextBox;

public class DatePicker extends TextBox{
	private String format = "";
	
	public DatePicker(){
		getElement().setAttribute("id", "datepicker");
		load();
	}
	public DatePicker(String format){
		this.format = format;
		getElement().setAttribute("id", "datepicker");
		load();
	}
	
	@SuppressWarnings("deprecation")
	private void load(){
		DeferredCommand.add(new Command() {
			
			@Override
			public void execute() {
				doNative(format);
			}
		});
	}
	
	private native void doNative(String format)/*-{
		if(format == ""){
			$wnd.$("#datepicker").datepicker({ dateFormat: "yy-mm-dd" });
			//$wnd.$("#datepicker").datepicker({ appendText: "(yyyy-mm-dd)" });
		}else{
			$wnd.$("#datepicker").datepicker({ dateFormat: format });
			//$wnd.$("#datepicker").datepicker({ appendText: format });
		}
		$wnd.$("#datepicker").datepicker( "option", "changeMonth", true );
		$wnd.$("#datepicker").datepicker( "option", "changeYear", true );
		$wnd.$("#datepicker").datepicker( "option", "maxDate", "+1d" );
	}-*/;

}
