package com.honda.galc.visualoverview.client.widgets;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ViewListGrid extends ListGrid {

    public ViewListGrid() {
    	super();
        setShowEdges(false);
        setBorder("0px");
        setShowHeader(false);
        setCanDragRecordsOut(true);
        setLeaveScrollbarGap(false);
        setDragDataAction(DragDataAction.COPY);
        setEmptyMessage("<br><br>Drag &amp; drop here");

        ListGridField layerIdField = new ListGridField("layerId");

        setFields(layerIdField);
    }
    
    @Override 
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		return "font-size:100%";
    	
    }
    

}
