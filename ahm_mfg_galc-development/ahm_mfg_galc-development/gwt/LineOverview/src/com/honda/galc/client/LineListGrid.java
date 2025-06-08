package com.honda.galc.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.SortNormalizer;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;

public class LineListGrid extends ListGrid {
	

	private static final int DEFECTS = 4;
	private static final int PARTS = 5;
	private static final int MEASUREMENTS = 6;
	private static final int HISTORY = 7;
	private static final String WEB_SERVICE_DEFECTS_DETAIL = "http://" + Window.Location.getHost() + "/RestWeb/DefectResultDao/findAllOutstandingByProductId?";
	private static final String WEB_SERVICE_PARTS_DETAIL = "http://" + Window.Location.getHost() + "/RestWeb/InstalledPartDao/getMissingPartDetails?";
	private static final String WEB_SERVICE_MEASUREMENTS_DETAIL = "http://" + Window.Location.getHost() + "/RestWeb/InstalledPartDao/getBadMeasurements?";
	private static final String WEB_SERVICE_HISTORY_DETAIL = "http://" + Window.Location.getHost() + "/RestWeb/ProductResultDao/findByProductId?";
	
	private DataSource detailDataSource = new DataSource();
	private DetailListGrid detailGrid;
    private DataSourceTextField processPointNameField = new DataSourceTextField("processPointName", "Process Point");
    private DataSourceTextField partNameField = new DataSourceTextField("rulePartName", "Part Name", 50);
	
	public LineListGrid()
	{
		//IE8 doesn't handle the CSS for this real well, so we turn it off.
		if(!Window.Navigator.getUserAgent().contains("MSIE"))
		{
			setShowRollOver(true);
			setShowRollOverCanvas(true);
		}
		else
		{
			setShowRollOver(false);
			setShowRollOverCanvas(false);
		}
		
		setWidth100();  
        setHeight100();
        setAlternateRecordStyles(true);
		setShowHeaderContextMenu(false);
		setShowHeaderMenuButton(false);
		setShowSortArrow(SortArrow.NONE);
		setCanSort(false);
		setFastCellUpdates(true);
		setCanEdit(true);
		
		
        ListGridField seqField = new ListGridField("sequenceNumber", "Sequence");
        ListGridField afSeqField = new ListGridField("afOnSequenceNumber", "AF ON Seq");
        ListGridField nextAfSeqField = new ListGridField("nextAfOnSequenceNumber", "AF ON Seq");
        ListGridField productField = new ListGridField("productId", "Product ID");
        ListGridField productSpecField = new ListGridField("productSpecCode", "Product Spec Code");
        ListGridField productionLotKdField = new ListGridField("productionLotKd", "Production Lot");
        ListGridField nextProductionLotKdField = new ListGridField("nextProductionLotKd", " Next Production Lot"); 
        ListGridField nonRepairedDefectsField = new ListGridField("nonRepairedDefects", "Defects");
        ListGridField missingInstalledPartsField = new ListGridField("missingInstalledParts", "Missing Parts");
        ListGridField badInstalledField = new ListGridField("badInstalledParts", "Bad Measurements");
        ListGridField processPointField = new ListGridField("processPointName", "Last PP");
        ListGridField processSequenceField = new ListGridField("processSequence", "Process Sequence");
        ListGridField processPointIdField = new ListGridField("processPointId", "Process Point ID");
        
        
        processSequenceField.setHidden(true);
        nextProductionLotKdField.setHidden(true);
        nextAfSeqField.setHidden(true);
        seqField.setHidden(true);
        processPointIdField.setHidden(true);
        
        setFields(seqField, afSeqField, nextAfSeqField, productField, productSpecField, productionLotKdField, nextProductionLotKdField, nonRepairedDefectsField, missingInstalledPartsField, badInstalledField, processPointField, processSequenceField);
        
        addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				displayDetailsGrid(event.getRecord().getAttribute("productId"), event.getRecord().getAttribute("processPointId"), event.getColNum());
				
			}
        	
        });

	}
	
	public void displayDetailsGrid(String productId, String processPointId, int column)
	{
		
		switch(column)
		{
		case DEFECTS:
	        DataSourceTextField inspectionPartLocationField = new DataSourceTextField("inspectionPartLocationName", "Defect Name", 50);
	        DataSourceTextField inspectionPartNameField = new DataSourceTextField("inspectionPartName", "Part Name");
	        DataSourceTextField twoPartPairPartField = new DataSourceTextField("twoPartPairPart", "Two Pair Part");
	        DataSourceTextField twoPartPairLocationField = new DataSourceTextField("twoPartPairLocation", "Two Pair Part Location");
	        DataSourceTextField secondaryPartField = new DataSourceTextField("secondaryPartName", "Secondary Part");
	        DataSourceTextField defectTypeField = new DataSourceTextField("defectTypeName", "Defect");
	        DataSourceTextField entryDeptField = new DataSourceTextField("entryDept", "Entry Dept");
	        DataSourceTextField responsibleDeptField = new DataSourceTextField("responsibleDept", "Responsible Dept");
	        
	        inspectionPartLocationField.setValueXPath("id/inspectionPartLocationName");
	        inspectionPartNameField.setValueXPath("id/inspectionPartName");
	        twoPartPairPartField.setValueXPath("id/twoPartPairPart");
	        twoPartPairLocationField.setValueXPath("id/twoPartPairLocation");
	        secondaryPartField.setValueXPath("id/secondaryPartName");
	        defectTypeField.setValueXPath("id/defectTypeName");
	        
	        inspectionPartNameField.setHidden(true);
	        twoPartPairPartField.setHidden(true);
	        twoPartPairLocationField.setHidden(true);
	        secondaryPartField.setHidden(true);
	        defectTypeField.setHidden(true);
	        	        
			detailDataSource = new DataSource();
			detailDataSource.setDataFormat(DSDataFormat.JSON);
			detailDataSource.setDataURL(WEB_SERVICE_DEFECTS_DETAIL + productId);
			detailDataSource.setFields(inspectionPartLocationField, inspectionPartNameField, defectTypeField, entryDeptField, responsibleDeptField);
			
			detailGrid = new DetailListGrid();
	        detailGrid.setDataSource(detailDataSource);
	        
	        
	        detailGrid.setCellFormatter(new CellFormatter() {

				@Override
				public String format(Object value, ListGridRecord record,
						int rowNum, int colNum) {
					if(colNum == 0)
					{
						return record.getAttributeAsRecord("id").getAttribute("inspectionPartLocationName").trim() + " "
								+ record.getAttributeAsRecord("id").getAttribute("inspectionPartName").trim() + " "
								+ record.getAttributeAsRecord("id").getAttribute("twoPartPairLocation").trim() + " "
								+ record.getAttributeAsRecord("id").getAttribute("twoPartPairPart").trim() + " "
								+ record.getAttributeAsRecord("id").getAttribute("defectTypeName").trim() + " "
								+ record.getAttributeAsRecord("id").getAttribute("secondaryPartName").trim() + " ";
					}
					else
					{
						return value.toString();
					}
				}
	        	
	        });
			
			Dialog defectsDetailBox = getDialogBox();
			defectsDetailBox.addItem(detailGrid);
			defectsDetailBox.setWidth("45%");
			defectsDetailBox.setHeight("50%");
			detailGrid.setHeight100();
			defectsDetailBox.setTitle("VIN: " + productId + " - Defects");
			defectsDetailBox.show();
			
			break;
		case PARTS:
	        DataSourceTextField partConfirmField = new DataSourceTextField("partConfirmCheck", "Part Confirm");
			
			detailDataSource = new DataSource();
			detailDataSource.setDataFormat(DSDataFormat.JSON);
	        detailDataSource.setDataURL(WEB_SERVICE_PARTS_DETAIL + productId + "&" + processPointId);
			detailDataSource.setFields(processPointNameField, partNameField, partConfirmField);
			
			detailGrid = new DetailListGrid();
	        detailGrid.setDataSource(detailDataSource);

			Dialog partsDetailBox = getDialogBox();
	        partsDetailBox.addItem(detailGrid);
	        partsDetailBox.setWidth("45%");
	        partsDetailBox.setHeight("50%");
	        detailGrid.setHeight100();
        	partsDetailBox.setTitle("VIN: " + productId + " - Missing Parts");
			partsDetailBox.show();
			
			break;
		case MEASUREMENTS:
        	DataSourceTextField measurementSequenceField = new DataSourceTextField("measurrementSequenceNumber", "Seq Num");
        	DataSourceTextField measurementAttemptField = new DataSourceTextField("measurementAttempt", "Attempt");
        	DataSourceTextField measurementValueField = new DataSourceTextField("measurementValue", "Value");
        	DataSourceTextField measurementStatusField = new DataSourceTextField("measurementStatus", "Status");
			
        	detailDataSource = new DataSource();
			detailDataSource.setDataFormat(DSDataFormat.JSON);
        	detailDataSource.setDataURL(WEB_SERVICE_MEASUREMENTS_DETAIL + productId + "&" + processPointId);
			detailDataSource.setFields(processPointNameField, partNameField, measurementSequenceField, measurementAttemptField, measurementValueField, measurementStatusField);
			
			detailGrid = new DetailListGrid();
	        detailGrid.setDataSource(detailDataSource);
			
			Dialog measurementsDetailBox = getDialogBox();
			measurementsDetailBox.setWidth("40%");
			measurementsDetailBox.setHeight("50%");
	        measurementsDetailBox.addItem(detailGrid);
	        detailGrid.setHeight100();
        	measurementsDetailBox.setTitle("VIN: " + productId + " - Bad Measurements");
			measurementsDetailBox.show();
			
			break;
		case HISTORY:
        	DataSourceTextField actualField = new DataSourceTextField("actualTimestamp", "Actual Timestamp");
	        DataSourceTextField processNameField = new DataSourceTextField("processPointName", "Process Point");
	        
	        actualField.setValueXPath("id/actualTimestamp");
	        processNameField.setValueXPath("processPointName");
	        
	        detailDataSource = new DataSource();
			detailDataSource.setDataFormat(DSDataFormat.JSON);
			detailDataSource.setDataURL(WEB_SERVICE_HISTORY_DETAIL + productId);
			detailDataSource.setFields(actualField, processNameField);
			
			detailGrid = new DetailListGrid();
	        detailGrid.setDataSource(detailDataSource);
			
			
			Dialog historyDetailBox = getDialogBox();
			historyDetailBox.setHeight("40%");
			historyDetailBox.setWidth("40%");
	        historyDetailBox.addItem(detailGrid);
	        detailGrid.setHeight100();
        	historyDetailBox.setTitle("VIN: " + productId + " - Process Points");
			historyDetailBox.show();
			
			break;
		default:
			return;

		}
	}
	
	public Dialog getDialogBox()
	{
		Dialog dialogBox = new Dialog();
		dialogBox.setIsModal(true);
		dialogBox.setShowModalMask(true);
		dialogBox.setShowCloseButton(false);
		dialogBox.setDismissOnOutsideClick(true);

        return dialogBox;
	}
	
	public void alignCells(Alignment alignment)
	{
        for (ListGridField field : getFields()) {
        	field.setAlign(alignment);
        	field.setCellAlign(alignment);
        }
	}
	
	@Override
	protected String getCellCSSText(ListGridRecord record, final int rowNum,  final int colNum) {
		
		if(!Window.Navigator.getUserAgent().contains("MSIE"))
		{		
			if(Integer.parseInt(record.getAttribute("afOnSequenceNumber")) + 1 != Integer.parseInt(record.getAttribute("nextAfOnSequenceNumber")) && colNum == 1)
			{
				return "background-color:#FF4747";
			}
			else if(Integer.parseInt(record.getAttribute("nonRepairedDefects")) > 0 && colNum == 4)
			{
				return "background-color:#FF4747";
			}
			else if(Integer.parseInt(record.getAttribute("missingInstalledParts")) > 0 && colNum == 5)
			{
				return "background-color:#FF4747";
			}
			else if(Integer.parseInt(record.getAttribute("badInstalledParts")) > 0 && colNum == 6)
			{
				return "background-color:#FF4747";
			}
			else
			{
				if(record.getAttribute("productionLotKd") != record.getAttribute("nextProductionLotKd") && rowNum != 0)
				{
					return "background-color:#FFFF66";
				}
				else
				{
					return "background-color:white";
				}
			}
		}
		else
		{
			return "background-color:white";
		}
	}

}
