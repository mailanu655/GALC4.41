package com.honda.galc.qics.mobile.client.tables;

import java.util.List;

import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.utils.RecordListUtil;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

/**
 * StringSelectTable displays a table where each row is a string in the supplied list.  An
 * callback is fired when the row is selected.
 *  
 * @author vfc01346
 *
 */
public class StringSelectTable extends TableView {
	
       private static final ListGridField FULL_FIELD = new ListGridField("-fullField");
       static {
	    	
	        FULL_FIELD.setCellFormatter(new CellFormatter() {
	            @Override
	            public String format(Object value, Record record, int rowNum, int fieldNum) {
	            	String val = "";
	            	if ( record != null && record.getAttribute("value") != null ) {
	            		val = record.getAttributeAsString("value");
	            	}
	                return "<span style='font-weight:normal'> <b>" + val + "</b> </span>";
	            }
	        });

	    }
	    
	    public 	StringSelectTable(List<String> stringList, final DoneCallback<String> callback ) {

	    	setTitleField(FULL_FIELD.getName());
	    	setAlign(Alignment.CENTER);

	    	setFields( FULL_FIELD );
		    	
	    	DataSourceField stringField = new DataSourceField("value", "Select one");
	    	stringField.setType("string");
  	
	    	setAlign(Alignment.CENTER);
	    	setFields( FULL_FIELD );
	    	
	   
	        setNavigationMode(NavigationMode.WHOLE_RECORD);
	    	setSelectionType(SelectionStyle.NONE);

	    	setShowNavigation(true);
	    	setShowIcons(true);
	    	 
	        addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
	            @Override
	            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
	            	if ( event != null && event.getRecord() != null ) {
		                Record r = event.getRecord();
		                String valueSelected = r.getAttribute("value");
		                PhGap.doTactalFeedback();
		                callback.onDone( valueSelected );
	            	}
	            }
	        });
	        
	        populate( stringList );	      
	    }
	    
	    public  void populate(List<String> stringList ) {
	    	RecordList recordList = RecordListUtil.buildRecordList(stringList, "value");
	    	this.setData(recordList);
	    	this. _refreshRows();
	    }
}
