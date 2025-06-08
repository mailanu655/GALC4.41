package com.honda.galc.qics.mobile.client.widgets;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.VLayout;


/**
 * The Class ContentViewPanel displays HTML content is a 
 * white panel with rounded corners.
 */
public class ContentViewPanel extends ScrollablePanel {


    /** The info panel. */
    Panel infoPanel = new Panel();

	/**
	 * Instantiates a new panel to display some html.
	 *
	 * @param title the title
	 * @param htmlContent the html content
	 */
	public ContentViewPanel (String title, String htmlContent) {
		
		super( title );
		this.setWidth("100%");
		this.setAlign(Alignment.LEFT);
		
        final VLayout vlayout = new VLayout();
        vlayout.setWidth("100%");
        infoPanel.setClassName("sc-rounded-panel");
        infoPanel.setMargin(10);
        setInfo(htmlContent);
        vlayout.addMember(infoPanel);
		this.addMember( vlayout );
	  }
	
	
	/**
	 * Sets the info to display.
	 *
	 * @param htmlContent the new info
	 */
	public void setInfo( String htmlContent ) {
        infoPanel.setContents(htmlContent);
	}
}
