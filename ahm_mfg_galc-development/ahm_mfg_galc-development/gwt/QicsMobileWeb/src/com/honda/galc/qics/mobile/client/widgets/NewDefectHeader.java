package com.honda.galc.qics.mobile.client.widgets;

import com.honda.galc.qics.mobile.client.VinDefectsModel;
import com.honda.galc.qics.mobile.client.events.CancelDefectRequestEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

/**
 * Builds a header that is used when a new defect is being entered.
 * It is composed of two parts.  Part one is info on the entries made so far.
 * Part two is a toolstrip that contains a cancel button and a prompt.
 * 
 * @author vfc01346
 *
 */
public class NewDefectHeader extends Panel {


	
	public NewDefectHeader( VinDefectsModel vinDefectsModel, String prompt ) {
		
		VLayout vlayout = new VLayout();
		
		vlayout.addMember(new DefectInfoPanel( vinDefectsModel ));
		vlayout.addMember(buildToolStrip(prompt));
		
		addMember( vlayout );
	}
	
	private ToolStrip buildToolStrip(String prompt) {
		ToolStrip toolbar = new ToolStrip();
	    
	    ToolStripButton cancelButton = new ToolStripButton("Cancel New Defect Entry");
	    cancelButton.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	        	PubSubBus.EVENT_BUS.fireEvent(new CancelDefectRequestEvent());
	        }
	    });
	    
	    toolbar.addButton(cancelButton);
	    toolbar.addSpacer();
	    toolbar.addMember(new Header1(prompt));
	    return toolbar;
	}

}
