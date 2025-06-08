package com.honda.galc.qics.mobile.client.widgets;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.honda.galc.qics.mobile.client.widgets.images.ImageResources;

/**
 * This panel pops up a spinning wait image. It also uses a "glass" to block the 
 * user from accessing the UI.  It is designed to work well with multiple
 * asynchronous requestors.   To start the waiting do 
 * <code> WaitPanel.getInstance().start()</code>.
 * To end the waiting do 
 * <code> WaitPanel.getInstance().end()</code>.
 * 
 * Make sure you "end()" everything you "start()"!
 * 
 * @author vfc01346
 *
 */
public class WaitPanel  {
	
	private PopupPanel popupPanel;
	private int count = 0;
	private static volatile WaitPanel waitPanel = null;

	public synchronized static WaitPanel getInstance() {
		if ( waitPanel == null ) {
			waitPanel = new WaitPanel();
		}
		return waitPanel;
	}
	
	private WaitPanel() {
	}
	
	public synchronized void start() {
		count++;
		if ( count > 0 && popupPanel == null ) {
			open();
		}
	}
	
	public synchronized void end() {
		count--;
		if ( count == 0 && popupPanel != null) {
			close();
		}
	}
	
	private void open() {
		// Create a modal dialog box that will not auto-hide
		this.popupPanel = new PopupPanel(false, true);
		
		HorizontalPanel panel = new HorizontalPanel();
	    panel.add(new Image(ImageResources.INSTANCE.loader()));
	    panel.add(new Label("Please wait"));
	    popupPanel.add(panel);
		
    	// Enable the glass panel so user cannot get to buttons
		this.popupPanel.setGlassEnabled(true); 
    	// Center the popup and make it visible
		this.popupPanel.center(); 	
	}
	
	private void close() {
		this.popupPanel.hide();
		this.popupPanel = null;
	}

}
