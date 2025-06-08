package com.honda.galc.visualoverview.client.widgets;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DisplayedLayersListGrid extends ListGrid {

	public ListGridField layerIdField;
	public ListGridField removeField;
    public DisplayedLayersListGrid() {
    	super();
        setShowEdges(false);
        setBorder("0px");
        setShowHeader(false);
        setCanDragRecordsOut(true);
        setLeaveScrollbarGap(false);
        setDragDataAction(DragDataAction.COPY);
        setEmptyMessage("<br><br>Drag &amp; drop here");
		setCanAcceptDroppedRecords(true);
		setFastCellUpdates(false);
		setAddDropValues(true);
		removeField = new ListGridField("removeAction");
		removeField.setType(ListGridFieldType.ICON);
		removeField.setCellIcon(GWT.getModuleBaseURL() + "remove.png");
		removeField.setCanEdit(false);

        layerIdField = new ListGridField("layerId");

        setFields(layerIdField, removeField);
    }
    
    @Override 
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		return "font-size:100%";
    	
    }
    

}
