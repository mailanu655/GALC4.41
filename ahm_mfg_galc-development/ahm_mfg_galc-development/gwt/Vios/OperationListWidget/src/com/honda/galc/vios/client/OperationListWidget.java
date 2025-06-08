package com.honda.galc.vios.client;

import java.util.ArrayList;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;  
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.google.gwt.user.client.Window;
import com.honda.galc.vios.shared.WidgetBase;

public class OperationListWidget extends WidgetBase {
	
	private DataSource dataSource = new DataSource();
	private final String WEB_SERVICE = "/RestWeb/GenericPddaDaoService/getAllOperationsForProcessPoint" ;
	private static ListGridRecord[] records;
    public void onModuleLoad() {
    	//get request parameter
    	String PPID = Window.Location.getParameter("PPID");//Process Point ID
    	final String fontsize = Window.Location.getParameter("fontsize");//fontsize
    	String ProductID = Window.Location.getParameter("ProductID");//Process Point ID

    	String column = Window.Location.getParameter("column");//widget width
    	if (column == null ) column = "unitSeqNo,unitNo,unitOperationDesc,unitBasePartNo";
    	final String width = Window.Location.getParameter("width");//widget width
    	//final String width = "800";
    	final String height = Window.Location.getParameter("height");//widget height
    	//final String height ="300";
    	final String backgroundcolor = Window.Location.getParameter("backgroundcolor");//widget background color
    	final String textcolor = Window.Location.getParameter("textcolor");//Text color
    	final String rowoddcolor = Window.Location.getParameter("rowoddcolor");//Alternating row color
    	final String rowevencolor = Window.Location.getParameter("rowevencolor");//Alternating row color
        final ListGrid operationGrid = new ListGrid()  {
			@Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                String style = "font-size: "+fontsize+"%;";
                if (null!=backgroundcolor) style = style + "background-color:rgb("+backgroundcolor+");";
                if (null!=rowoddcolor && rowNum % 2 == 0 )  style = style + "background-color:rgb("+rowoddcolor+");";
                if (null!=rowevencolor && rowNum % 2 != 0 )  style = style + "background-color:rgb("+rowevencolor+");";
                if (null!=textcolor)   style = style + "color:rgb("+textcolor+");";
                if (this.getFieldName(colNum).equals("partFlg")&&record.getAttribute("partFlg").equals("P") ) {
               		style = style+"color:blue;font-weight:bold;font-size: 1.1em;"; //	
                }
                return style;
            }
        };
  
        operationGrid.setWidth(width); 
        operationGrid.setHeight(height);
        operationGrid.setShowAllRecords(true);
        operationGrid.setShowAllColumns(true);
        operationGrid.setScrollbarSize(48);
       
        operationGrid.setHeaderBaseStyle("Grid-Header");
        operationGrid.setCanHover(false);
        
        ArrayList<ListGridField> gridFields = new ArrayList<ListGridField>();
		ListGridField mainIDField = new ListGridField("pddaMaintenanceId", "",0);
		mainIDField.setHidden(true);
        gridFields.add(mainIDField);

        String[] columnitems = column.split(",");
		for(String str : columnitems) {
			if (null!=str) {
				if ( str.equals("unitSeqNo") ) {
			        ListGridField seqField = new ListGridField("unitSeqNo", "Seq.",40);
			        gridFields.add(seqField);
				}
				if ( str.equals("unitNo") ) {
			        ListGridField numberField = new ListGridField("unitNo", "Unit No.",80);
			        numberField.setCellAlign(Alignment.CENTER);
			        gridFields.add(numberField);
			        
					//Display the PartFlg "P"
					ListGridField partFlgField = new ListGridField("partFlg", " ",15);
			        gridFields.add(partFlgField);
				}

		        if ( str.equals("unitOperationDesc") ) {
			        ListGridField nameField = new ListGridField("unitOperationDesc", "Unit of Operation");
			        gridFields.add(nameField);
				}
				if ( str.equals("unitBasePartNo") ) {
					ListGridField basenoField = new ListGridField("unitBasePartNo", "Base No.",70);
					gridFields.add(basenoField);
				}
			}
		}
		operationGrid.setFields(gridFields.toArray(new ListGridField[gridFields.size()]));
		operationGrid.setWrapCells( true );
		operationGrid.setFixedRecordHeights( false );
        operationGrid.setCanResizeFields(true);
        operationGrid.setShowHeaderContextMenu(false);
        operationGrid.setShowHeaderMenuButton(false);
        operationGrid.setShowSortArrow(SortArrow.NONE);
        operationGrid.setCanSort(false);
        operationGrid.setAutoFitData(Autofit.HORIZONTAL);
        operationGrid.setAutoFetchData(true);
        operationGrid.setLeaveScrollbarGap(false); 
        
        //set data
		RPCManager.setAllowCrossDomainCalls(true);
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(WEB_SERVICE+"?"+ProductID+"&"+PPID);
		operationGrid.setDataSource(dataSource);
		//operationGrid.setData(getRecords());
		//set up the link to unit info
        operationGrid.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
            	ListGridRecord record = (ListGridRecord) event.getRecord();
                Window.Location.assign("http://"+ Window.Location.getHost()+"/BaseWeb/OperationInfoWidget.html?width="+width+"&height="+height+"&fontsize="+fontsize+"&fromlist=true&pddaMaintenanceId="+record.getAttribute("pddaMaintenanceId"));
                //prevent default browser context menu
                event.cancel();
            }
        });
        
		operationGrid.draw();  
    }  
 
    
    
    
    
    
    //test data
    private  ListGridRecord[] getRecords() {  
        if (records == null) {  
            records = getNewRecords();    
        }    
        return records;    
    }    
    
    private  ListGridRecord createRecord(String pddaMaintenanceId, String seq,String number,String flg, String name, String basenumber) {  
        ListGridRecord record = new ListGridRecord();
        record.setAttribute("pddaMaintenanceId", pddaMaintenanceId);
        record.setAttribute("unitSeqNo", seq);  
        record.setAttribute("unitNo", number);  
        record.setAttribute("partFlg", flg);
        record.setAttribute("unitOperationDesc", name);  
        record.setAttribute("unitBasePartNo", basenumber);  
        return record;  
    }  
  
    private  ListGridRecord[] getNewRecords() {  
        return new ListGridRecord[]{  
                createRecord("59309977","1","201977","P", "READ BUILD SHEET", "60400"),    
                createRecord("59309978","2","281198","", "PICK UP SHEERING LOCK ASSEMBLY (ELEC) AND PLACE IN DELIVERY CART", "35100"),    
                createRecord("59309979","3","208001","P", "REMOVE IGNITION,REMOTE CIBTROL LOCK (RC LOCK),AND DOOR LOCK FROM BAG AND PLACE TO TABLE FOR LATER USE.",  "35010"),    
                createRecord("59309970","4","208001","P", "WRITE SEQUENCE NUMBER OF CURRENT UNIT ON LOCKSET BAG", "35010"),    
                createRecord("59309971","5","H00001","", "PLACE LEFT DOOR LOCK(X1) TO LEFT FRONT FLOOR OF UNIT WITH SAME SEQUENCE NUMBER ON ALL MODELS.", "35010"),    
                createRecord("59309972","6","H02008", "","PLACE RIGHT DOOR LOCK INTO RIGHT FRONT DOOR FLANGE(DX MODELS)", "35010"),    
                createRecord("59309973","7","H00001","", "SCAN VIN #BARCODE ON BUILT SHEET", "H02"),    
                createRecord("59309974","8","H02008","", "PLACE \"SH-AWD\" TYPE EMBLEM TO R/S TRUNK LID USING FIXTURE.", "35010"),    
                createRecord("59309975","9","H02008","", "SET CLIP ON TAILLIGHT HARNESS BRANCH TO L/S INNER TRUNK SKIN (X1).", "35010"),    
                createRecord("59309976","10","H02008","", "REMOVE L/S FLOOR HARNESS BRANCH FROM L/S CENTER CROSS MEMBER AND ROUTE THROUGH CARPET.", "35010"),    
                createRecord("59309977","11","H02008","", "TUCK CARPET UNDERNEATH REAR HEATER DUCT AT SEAT STIFFENER AREA.", "35010"),    
                createRecord("59309978","12","H02008","", "APPLY NOX RUST  TO L/S REAR DOOR UPPER AND LOWER HINGE FLANGES X4.", "35010"),    
                createRecord("59309979","13","H02008", "","APPLY NOX RUST  TO L/S FRONT DOOR HINGE NOTCH X1.", "35010"),    
                createRecord("59309970","14","H02008","", "GET SRS SIDE IMPACT WARNING LABEL AND PLACE TO RIGHT SIDE DOOR OPENING.", "35010"),    
                createRecord("59309971","15","H02008","", "APPLY NOX RUST TO HOLE IN REAR DOOR UPPER HINGE (LOWER HOLE IN UPPER HINGE LOCATION)", "35010"),    
                createRecord("59309972","16","H02008","", "PLACE \"SH-AWD\" TYPE EMBLEM TO R/S TRUNK LID USING FIXTURE.", "35010")    
        };  
    }

    
}  