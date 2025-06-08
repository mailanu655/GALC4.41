package com.honda.galc.qics.mobile.client.tables;

import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.RecordLayout;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.SortDirection;
import com.smartgwt.mobile.client.widgets.tableview.TableView;


/**
 * VinIssuesTable displays a table where each row is a "defect".  An
 * DefectSelectedEvent is fired when the row is selected.
 *  
 * @author vfc01346
 *
 */
public class LogMessageTable extends TableView {
	
	   private static final DataSourceField 
	   	TITLE_FIELD = new DataSourceField("title", "Title"),
	    INFO_FIELD = new DataSourceField("info", "Info"),
	    DESCRIPTION_FIELD = new DataSourceField("description", "Description"),
	    ID_FIELD = new DataSourceField("id", "Id");
	   
        

		static {
	    	TITLE_FIELD.setType("string");
	    	INFO_FIELD.setType("string");
	    	DESCRIPTION_FIELD.setType("string");
	    	ID_FIELD.setType("string");
	    }
	    
	    public 	LogMessageTable() {

	    	setAlign(Alignment.CENTER);

		    this.setRecordLayout(RecordLayout.TITLE_ONLY);
	   
	    	setSelectionType(SelectionStyle.NONE);
	    	// Set to false util SC.say has scrollbars
	    	setShowNavigation(true);
	    	setShowIcons(false);
	    	
		    SortSpecifier sortSpecifier = new SortSpecifier(ID_FIELD.getName(), SortDirection.DESCENDING);
		    setSort(sortSpecifier);
	    }
	    

}
