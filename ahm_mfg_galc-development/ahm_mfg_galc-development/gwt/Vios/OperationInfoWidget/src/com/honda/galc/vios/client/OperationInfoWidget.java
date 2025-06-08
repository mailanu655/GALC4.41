package com.honda.galc.vios.client;


import com.smartgwt.client.data.DataSource;

import com.smartgwt.client.types.Alignment;  
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;  
import com.smartgwt.client.widgets.layout.HLayout;  
import com.smartgwt.client.widgets.layout.VLayout;
  
import com.google.gwt.user.client.Window;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;

import com.honda.galc.vios.shared.WidgetBase;



public class OperationInfoWidget extends WidgetBase {
	
	private DataSource dataSourceInfo = new DataSource();
	private String WS_URLinfo = "/RestWeb/GenericPddaDaoService/getUnitOfOperationDetails" ;

	private String WS_URLimage ="/RestWeb/GenericPddaDaoService/getUnitImages" ;
 
	private String width = "";
	private String height = "";
	String fontsize = "";
	Img img = new Img();
  
    public void onModuleLoad() {
    	
    	//get request parameter
    	String fromlist = Window.Location.getParameter("fromlist");
    	width = Window.Location.getParameter("width");//widget width
    	height = Window.Location.getParameter("height");//widget height
    	fontsize = Window.Location.getParameter("fontsize");//fontsize
    	String pddaMaintenanceId = Window.Location.getParameter("pddaMaintenanceId");
    	//set datasource from restful service
    	dataSourceInfo.setDataFormat(DSDataFormat.JSON);
    	dataSourceInfo.setDataURL(WS_URLinfo+"?(int)"+pddaMaintenanceId);
    	dataSourceInfo.setClientOnly(true);
	    
	    Img backimg = new Img();
	    backimg.setWidth(40);
	    backimg.setHeight(40);
	    backimg.setSrc("back_256.png");
	    backimg.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	Window.Location.assign("javascript:history.back()");
            }
        });
	    
	    ListGrid QualityPoint = getOperationGrid("Quality Point:","qualityPoint");
	    QualityPoint.setDataSource(dataSourceInfo);
	    //QualityPoint.setBorder("1px solid #ababab;border-bottom:0px;");
	    
	    ListGrid SpecialControl = getOperationGrid("Special Control:","controlMethod");
	    //SpecialControl.setBorder("1px solid #ababab;border-bottom:0px;border-top:0px;");
	    SpecialControl.setDataSource(dataSourceInfo);
	    
	    ListGrid ReactionPlan = getOperationGrid("Reaction Plan:","reactionPlan");
	    //ReactionPlan.setBorder("1px solid #ababab;border-top:0px;");
	    ReactionPlan.setDataSource(dataSourceInfo);
	    
	    ListGrid WorkingPoint = getOperationGrid("Working Point:","workPtDescText");
	    //WorkingPoint.setBorder("1px solid #ababab;");
	    WorkingPoint.setDataSource(dataSourceInfo);

	    ListGrid ImpactPoint = getOperationGrid("Impact Point:","impactPoint");
	    //ImpactPoint.setBorder("1px solid #ababab;");
	    ImpactPoint.setDataSource(dataSourceInfo);
	    
	    ListGrid ControlMethod = getOperationGrid("Control Method:","controlMethod");
	    //ControlMethod.setBorder("1px solid #ababab;");
	    ControlMethod.setDataSource(dataSourceInfo);
	    
	    ListGrid AuxiliaryMaterial = getOperationGrid("Auxiliary Material:","AuxMatDescText");
	    //AuxiliaryMaterial.setBorder("1px solid #ababab;border-bottom:0px;");
	    AuxiliaryMaterial.setDataSource(dataSourceInfo);

	    ListGrid TightTorque = getOperationMultiGrid("Tight Torque:","trqCharVal","Tool:","tool");
	    //TightTorque.setBorder("1px solid #ababab;border-top:0px;");
	    TightTorque.setDataSource(dataSourceInfo);
	    
	    	
        VLayout mainView = new VLayout(10); 
        mainView.setLayoutMargin(10);
        mainView.setMembersMargin(10);
        //mainView.setHeight100();
        //mainView.setAutoHeight();
        mainView.setHeight(height);
        
        VLayout innerView1 = new VLayout();
        innerView1.setLayoutMargin(0);
        innerView1.setMembersMargin(0);
        
        VLayout innerView2 = new VLayout();
        innerView2.setLayoutMargin(0);
        innerView2.setMembersMargin(0);
        
        final HLayout imgView = new HLayout(10);
        imgView.setHeight100();
        imgView.setWidth(width);
        imgView.setAlign(Alignment.CENTER);
        //imgView.setHeight(100);
        mainView.setWidth(width);  
  
        if (null!=fromlist)
        	mainView.addMember(backimg);

        innerView1.addMember(QualityPoint);
        innerView1.addMember(SpecialControl);
        innerView1.addMember(ReactionPlan);
        innerView1.addMember(WorkingPoint);
        innerView1.addMember(ImpactPoint);
        innerView1.addMember(ControlMethod);
        innerView1.addMember(AuxiliaryMaterial);
        innerView1.addMember(TightTorque);
        mainView.addMember(innerView1);
        
        //mainView.addMember(WorkingPoint);
        //mainView.addMember(ImpactPoint);
        //mainView.addMember(ControlMethod);
        
        //innerView2.addMember(AuxiliaryMaterial);
        //innerView2.addMember(TightTorque);
        //mainView.addMember(innerView2);
        
        //only for torque station
        mainView.setAlign(Alignment.CENTER);

        Resource imageresource = new Resource(WS_URLimage+"?(int)"+pddaMaintenanceId);
        //Resource imageresource = new Resource(WS_URLimage+"?(int)"+58957952);

        
    	imageresource.get().send(new JsonCallback() {
    	    @Override
    	    public void onFailure(Method method, Throwable exception) {
    	        // TODO Auto-generated method stub
    	    }
    	    @Override
    	    public void onSuccess(Method method, JSONValue response) {
    	        //add the Base64 prefix.
    	    	JSONArray schemaArray = response.isArray();
    	    	//response.isArray().
    	    	
    	    	for (int i = 0; i < schemaArray.size(); i++) {
    	    		JSONObject jo = schemaArray.get(i).isObject();
	    	        String image = "data:image/jpeg;base64," + jo.get("unitImage").toString().replace("\"", "");
	    	        img = new Img(image,(int) (Double.parseDouble(width)/5),(int) (Double.parseDouble(width)/5));
	    	        //img.setWidth((int) (Double.parseDouble(width)/4));
	    	        //img.setHeight((int) (Double.parseDouble(width)/4));
	    	        img.setImageType(ImageStyle.STRETCH);
	    	        imgView.addMember(img);
    	    	}
    	    }
    	});
        mainView.addMember(imgView);
        mainView.draw();  
    }  

    private ListGrid getOperationGrid(String title,String content){
        final ListGrid operationGrid = new ListGrid()  {
        	@Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                String style = super.getCellCSSText(record, rowNum, colNum);
                style = "font-size: "+fontsize+"%;";
                if (   colNum % 2 == 0 ) {
                        style = style+"font-weight: bold;";  
                }
                return style;
            }
        };
  
        operationGrid.setWidth(width);  
        operationGrid.setAutoHeight();
        operationGrid.setShowAllRecords(true);
        operationGrid.setShowHeader(false);
        //operationGrid.setAutoFitData(Autofit.BOTH);
        operationGrid.setAutoFetchData(true);
  
        
        ListGridField titleField = new ListGridField("title", "title");
        titleField.setWidth("20%");
        titleField.setAlign(Alignment.RIGHT);
        titleField.setEmptyCellValue(title);
        
        ListGridField contentsField = new ListGridField(content, content);
        contentsField.setWidth("80%");

        operationGrid.setFields(titleField,contentsField);
        operationGrid.setWrapCells( true );
        operationGrid.setFixedRecordHeights( false );
        operationGrid.setAlternateRecordStyles(false);
        operationGrid.setAutoFitData(Autofit.VERTICAL);
        operationGrid.setLeaveScrollbarGap(false); 
        
        return operationGrid;

    }
    
    private ListGrid getOperationMultiGrid(String title1,String content1,String title2,String content2){
        final ListGrid operationGrid = new ListGrid()  {
        	@Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                String style = super.getCellCSSText(record, rowNum, colNum);
                style = "font-size: "+fontsize+"%;";
                if (   colNum % 2 == 0 ) {
                        style = style+"font-weight: bold;";  
                }
                return style;
            }
        };
  
        operationGrid.setWidth(width);  
        operationGrid.setAutoHeight();
        operationGrid.setShowAllRecords(true);
        operationGrid.setShowHeader(false);
        operationGrid.setAutoFitData(Autofit.VERTICAL);
        operationGrid.setLeaveScrollbarGap(false); 

        operationGrid.setAlternateRecordStyles(false);
        operationGrid.setAutoFetchData(true);
        
        ListGridField titleField1 = new ListGridField("title1", "title1");
        titleField1.setWidth("20%");
        titleField1.setAlign(Alignment.RIGHT);
        titleField1.setEmptyCellValue(title1);
        
        ListGridField contentsField1 = new ListGridField(content1, content1);
        contentsField1.setWidth("50%");

        ListGridField titleField2 = new ListGridField("title2", "title2");
        titleField2.setWidth("10%");
        titleField2.setAlign(Alignment.RIGHT);
        titleField2.setEmptyCellValue(title2);
        
        ListGridField contentsField2 = new ListGridField(content2, content2);
        contentsField2.setWidth("20%");

        operationGrid.setFields(titleField1,contentsField1,titleField2,contentsField2);
        operationGrid.setWrapCells( true );
        operationGrid.setFixedRecordHeights( false );
        
        return operationGrid;

    }

}  