package com.honda.galc.qics.mobile.client.widgets;

import com.google.gwt.user.client.Window;
import com.honda.galc.qics.mobile.client.VinDefectsModel;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

/**
 * Creates a panel that shows the entries needed to create a new
 * defect as they are entered.
 * 
 * @author vfc01346
 *
 */
public class DefectInfoPanel extends Panel {
	
    final HLayout hlayout = new HLayout();

    public DefectInfoPanel( VinDefectsModel vinDefectsModel ) {
    	
    	// To make room, drop the group when we have other parts
    	if ( vinDefectsModel.getOtherPart() == null ) {
    		addOne( "Group",  vinDefectsModel.getPartGroup());
    	}
    	addOne( "Part", vinDefectsModel.getPart());
    	addOne( "Location", vinDefectsModel.getPartLocation());
    	addOne( "Defect", vinDefectsModel.getPartDefect());
    	addOne( "Other Part", vinDefectsModel.getOtherPart());
    	
    	addMember( hlayout );
    }

    private boolean limitedHeight() {
    	return Window.getClientHeight() < 400;
    }

    /**
     * Adds a disable button with two lines of text. The first line is
     * "<title>:" the second line is "<value>".
     * 
     * @param title
     * @param value
     */
    private void addOne( String title, String value ) {
    	String content = "";
    	String height = "32px";
    	int margin = 10;
    	if ( limitedHeight() ) {
    		// if small screen use only the value
    		content = value;
    	} else {
    		// full height
    		height = "48px";
    		content = "<span> <sm>" + title + ":</sm><br/>" + value + "</span>";
    	}
    	if ( value != null ) {
    		Button button = new Button(content);
    		button.disable();
    		button.setHeight(height);
    		button.setMargin(margin);
    		hlayout.addMember( button );
    	}
    }
}
