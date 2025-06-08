package com.honda.galc.qics.mobile.client.widgets;

import java.util.List;

import com.honda.galc.qics.mobile.client.tables.StringSelectTable;
import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

public class StringSelectPanel extends ScrollablePanel {


	public StringSelectPanel( String title, List<String> choices, DoneCallback<String> callback ) {
		super(title);

		VLayout layout = new VLayout();
	    layout.setWidth("100%");
	    layout.setAlign(Alignment.LEFT);
	    
	    StringSelectTable stringSelectTable = new StringSelectTable(choices, callback );
	
	    layout.addMember( stringSelectTable );
	    addMember( layout );
	}
}
