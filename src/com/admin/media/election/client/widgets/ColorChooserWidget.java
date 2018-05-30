package com.admin.media.election.client.widgets;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.TextBox;

public class ColorChooserWidget extends TextBox{
	public ColorChooserWidget(){
		getElement().setAttribute("id", "colorbox");
		load();
	}
	
	@SuppressWarnings("deprecation")
	private void load(){
		DeferredCommand.add(new Command() {
			
			@Override
			public void execute() {
				doNativeColorInit();
				//doNativeColorInit2();
			}
		});
	}
	
	private native void doNativeColorInit()/*-{
		$wnd.$('#colorbox').ColorPicker({
			onSubmit: function(hsb, hex, rgb, el) {
				$wnd.$(el).val(hex);
				$wnd.$(el).ColorPickerHide();
			},
	
			onBeforeShow: function () {
				$wnd.$(this).ColorPickerSetColor(this.value);
			}
		})
		.bind('keyup', function(){
			$wnd.$(this).ColorPickerSetColor(this.value);
		});
	}-*/;
	
	private native void doNativeColorInit2()/*-{
			$wnd.$('#colorbox').ColorPicker({flat: true});
	}-*/;


}
