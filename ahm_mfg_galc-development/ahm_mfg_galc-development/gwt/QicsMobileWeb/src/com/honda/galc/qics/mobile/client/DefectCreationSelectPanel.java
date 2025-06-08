package com.honda.galc.qics.mobile.client;

import java.util.List;

import com.honda.galc.qics.mobile.client.tables.StringSelectTable;
import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.widgets.NewDefectHeader;
import com.honda.galc.qics.mobile.client.widgets.VinInfoPanel;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

/**
 * The DefectCreationSelectPanel is used to select part groups, parts, locations, defects 
 * and secondary parts for the entry of a new defect.
 * 
 */
public class DefectCreationSelectPanel extends ScrollablePanel {

	
	private StringSelectTable table; 
	
    public DefectCreationSelectPanel( String title, String prompt, VinDefectsModel vinDefectsModel, List<String> 	stringList, DoneCallback<String> callback ){
		super(title);
		
		VLayout layout = new VLayout();
        layout.setWidth("100%");
        layout.setAlign(Alignment.LEFT);
        
		VinInfoPanel vinInfoPanel = new VinInfoPanel("Vin Info", vinDefectsModel );
        NewDefectHeader header = new NewDefectHeader(vinDefectsModel, prompt );
        table = new StringSelectTable(stringList, callback);
         
        layout.addMember( vinInfoPanel);
        layout.addMember( header );
        layout.addMember( table );
        addMember( layout );
	}
    



 
	
}
