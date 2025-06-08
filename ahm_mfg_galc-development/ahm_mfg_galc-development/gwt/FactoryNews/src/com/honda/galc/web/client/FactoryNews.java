package com.honda.galc.web.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.HandleErrorCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.DataArrivedEvent;
import com.smartgwt.client.widgets.form.fields.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedEvent;
import com.smartgwt.client.widgets.grid.events.ViewStateChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Subu Kathiresan
 * @author Joseph Allen
 * @date June 04, 2012
 *
 * Entry point class for FactoryNews page
 */
public class FactoryNews implements EntryPoint {
	private static final String WEB_SERVICE_FACTORY_NEWS_UPDATE = "BaseWeb/FactoryNewsService/getUpdate.ws";
	private static final String WEB_SERVICE_INVENTORY = "BaseWeb/FactoryNewsService/getInventory.ws";
	private static final String WEB_SERVICE_ACTUAL_PRODUCTS = "BaseWeb/FactoryNewsService/getActualProducts.ws";
	private static final String WEB_SERVICE_PROCESS_POINTS = "BaseWeb/FactoryNewsService/getProcessPoints.ws";
	private static final String WEB_SERVICE_DETAIL_LINK_ENABLED = "BaseWeb/FactoryNewsService/isDetailLinkEnabled.ws";
	private static final String viewStateCookieName = "FactoryNewsListGridViewState";
	
	private Image factoryNewsLogo = new Image("pics/FactoryNewsLogo.jpg");
	private Panel headerPanel = null;
	private Panel footerPanel = null;
	private Label connectionLostLabel = new Label();
	private Label connectionLostSpacerLabel = new Label();
	private Label lastUpdatedLabel = new Label();   
	
	private ListGrid listGrid;
	private DataSource dataSource = new DataSource();
	private boolean errorEnabled = false;
	
	/** the flag if the detail link is enabled. By default , it is false */
	private boolean detailLinkEnabled = false;
	private com.smartgwt.client.widgets.Window inventoryWindow;
	private com.smartgwt.client.widgets.Window actualProductWindow;
	private ListGrid inventoryListGrid;
	private ListGrid actualProductListGrid;
	private DataSource inventoryDataSource;
	private DataSource actualProductDataSource;
	private DataSource processPointDataSource;
	private SelectItem processPointComboBox;
	private String divisionId;
	private String lineId;
	private String shift;
	
	CellFormatter numberCellFormatter = new CellFormatter() {  
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			if (value instanceof Integer) {
				if((Integer) value < 0){
					return "<b><font color = 'red'>" + value + "</font></b>";
				}
			}
			return value.toString();
		}  
	}; 

	CellFormatter lineNameCellFormatter = new CellFormatter() {  
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			return "<b><font color = '#006699'>" + value + "</font></b>";
		}  
	}; 

	CellFormatter divisionNameCellFormatter = new CellFormatter() {  
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			return "";
		}  
	}; 
	
	/** The cell formatter for the actual columns of 1/2/3 shift and actual total column. */
	CellFormatter actualCellFormatter = new CellFormatter() {  
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			return value == null ? "" : "<div class='clickableText'>" + value + "</div>";
		}  
	}; 
	
	/** The cell formatter for the current inventory column. */
	CellFormatter inventoryCellFormatter = new CellFormatter() {  
		@Override
		public String format(Object value, final ListGridRecord record, int rowNum,
				int colNum) {
			if(record.getIsGroupSummary()){
				return value == null ? "" : value.toString();
			}
			else{
				return value == null ? "" : "<div class='clickableText'>" + value + "</div>";
			}
		}
	}; 
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//check the factory new property to determine if the detail links should be shown.
		String url = GWT.getHostPageBaseURL() + WEB_SERVICE_DETAIL_LINK_ENABLED;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						detailLinkEnabled = Boolean.parseBoolean(response.getText().trim());
					} else {
						SC.logWarn("Failed to load factory news configuration.");
					}
					initialize();
				}

				@Override
				public void onError(Request request, Throwable exception) {
					SC.logWarn("Failed to load factory news configuration.");
					initialize();
				}
			});
		} catch (RequestException e) {
			SC.logWarn("Failed to load factory news configuration.");
			initialize();
		}
		
	}
	
	
	/**
	 * Initialize the factory news page.
	 */
	private void initialize(){
		addEventHandlers();
		decorateLastUpdatedLabel();
		
	    headerPanel = getHeaderPanel();
	    footerPanel = getFooterPanel();
	    RootPanel.get().add(headerPanel);
	    RootPanel.get().add(getListGrid());
	    RootPanel.get().add(footerPanel);
	    
		startGridAsyncUpdateThread();
	}
	
	/**
	 * Creates a header panel
	 * @return
	 */
	public Panel getHeaderPanel(){
		FlowPanel header = new FlowPanel();
		header.add(factoryNewsLogo);
		return header;
	}
	
	private void addEventHandlers() {
		// add RPC callback error handler 
		RPCManager.setHandleErrorCallback(new HandleErrorCallback() {
			public void handleError(DSResponse response, DSRequest request) {
			}
		});
		
		// add window resize handler
	    Window.addResizeHandler(new ResizeHandler() {
	    	Timer resizeTimer = new Timer() {  
	    		@Override
	    		public void run() {
	    			resizeListGrid();
	    		}
	    	};

	    	@Override
	    	public void onResize(ResizeEvent event) {
	    		resizeTimer.schedule(250);
	    	}
	    });

	}

	public Panel getFooterPanel(){
		FlowPanel footer = new FlowPanel();
		footer.add(verticalSpacer(3));
		footer.add(lastUpdatedLabel);
		return footer;
	}
	
	/**
	 * Creates the ListGrid table to display the factory news data
	 * @return
	 */
	public ListGrid getListGrid() {
	    ListGridField divisionNameField = new ListGridField("divisionName", "Division");
	    divisionNameField.setCellFormatter(divisionNameCellFormatter);
	    
	    ListGridField seqField = new ListGridField("sequenceNumber", "Seq");
	    seqField.setHidden(true);
	    
	    ListGridField lineNameField = new ListGridField("lineName", "Line");
	    lineNameField.setCellFormatter(lineNameCellFormatter);

	    ListGridField planField = new ListGridField("plan", "Plan");
	    ListGridField targetField = new ListGridField("target", "Target");
	    ListGridField actualThirdField = new ListGridField("actual3rd", "Actual 3rd");
	    ListGridField actualFirstField = new ListGridField("actual1st", "Actual 1st");
	    ListGridField actualSecondField = new ListGridField("actual2nd", "Actual 2nd");
	    ListGridField actualTotalField = new ListGridField("actualTotal", "Actual Total");
	    
	    ListGridField differenceField = new ListGridField("difference", "Difference");
	    differenceField.setCellFormatter(numberCellFormatter);
	    
	    ListGridField currentInventoryField = new ListGridField("currentInventory", "Current Inventory");
	    currentInventoryField.setSummaryFunction(SummaryFunctionType.SUM);
	    
	    ListGridField nextLineNameField = new ListGridField("nextLineName", "Next Line");
	    nextLineNameField.setCellFormatter(lineNameCellFormatter);
	    
	    Map<String, String> lineParamMap = new HashMap<String, String>();
	    String urlParam = com.google.gwt.user.client.Window.Location.getParameter("plantName"); 
	    if(urlParam != null && urlParam.equals("")) {
	    	lineParamMap.put("plantName", urlParam);
	    }
	    
	    dataSource.setDataFormat(DSDataFormat.JSON);
	    dataSource.setDataURL(GWT.getHostPageBaseURL() + WEB_SERVICE_FACTORY_NEWS_UPDATE);
	    dataSource.setDefaultParams(lineParamMap);
	    listGrid = new ListGrid();
	    listGrid.setFields(divisionNameField, seqField, lineNameField, planField, targetField, actualThirdField, actualFirstField, actualSecondField,
	    		actualTotalField, differenceField, currentInventoryField, nextLineNameField);
	    
	    listGrid.setTop(0);
	    listGrid.setLeft(0);
		listGrid.setDataSource(dataSource);
		listGrid.setAutoFetchData(true);
		listGrid.setHeight((int)((Window.getClientHeight() - headerPanel.getOffsetHeight() - footerPanel.getOffsetHeight()) * .9));
		listGrid.setAutoFitData(Autofit.HORIZONTAL);
		listGrid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
        lastUpdatedLabel.setContents("Last Updated at:   " + new Date()); 
        listGrid.setAutoFitFieldWidths(true);
        listGrid.setLeaveScrollbarGap(false);
        listGrid.setGroupByField("divisionName");  
        listGrid.setGroupStartOpen(GroupStartOpen.ALL); 
        listGrid.setCanCollapseGroup(false);
        listGrid.setShowGroupSummary(true);  
        listGrid.setShowGroupSummaryInHeader(false);  
        listGrid.setAlternateRecordStyles(false);
        listGrid.addViewStateChangedHandler(getViewStateChangedHandler());
        for (ListGridField field: listGrid.getFields()) {
        	field.setRequired(true);
        	field.setAlign(Alignment.CENTER);
        	field.setCellAlign(Alignment.CENTER);
        }

	    if(detailLinkEnabled){
	    	prepareFactoryNewsListGridFields(listGrid);
	    }
        listGrid.draw();
        setUserTableDefaults();
        
        return listGrid;
	}
	
	/**
	 * Loads the user's settings from stored cookies
	 */
	private void setUserTableDefaults(){
		//Set the ViewState 
		String viewStateCookie = Cookies.getCookie(viewStateCookieName);
        if(viewStateCookie != null && !viewStateCookie.equals("")){
        	listGrid.setViewState(viewStateCookie);
        }
	}
	
	/**
	 * Stores user's changes to the ListGrid layout in a cookie
	 * @return
	 */
	private ViewStateChangedHandler getViewStateChangedHandler(){
		return new ViewStateChangedHandler(){
			@Override
			public void onViewStateChanged(ViewStateChangedEvent event) {
				Date expDate = new Date();
				expDate.setYear(expDate.getYear() + 100);	// set cookie to expire after 100 years from now	
				Cookies.setCookie(viewStateCookieName, listGrid.getViewState(), expDate);
			}
        };
	}
	
	/**
	 * Updates the Factory news data periodically uses the Timer class to simulate threading
	 */
	private void startGridAsyncUpdateThread() {
		Timer t = new Timer() {
			public void run() {
				try {
					dataSource.fetchData(new Criteria(), new DSCallback() {          

						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if (response.getStatus() == RPCResponse.STATUS_TRANSPORT_ERROR) {
								if(!errorEnabled){
									enableErrorMessage();
								}
								return;
							}
							if(errorEnabled){
								disableErrorMessage();
							}
							listGrid.invalidateCache();
							listGrid.setData(response.getData());					
							lastUpdatedLabel.hide();
							lastUpdatedLabel.setContents("Last Updated at:   " + new Date());  
							lastUpdatedLabel.animateShow(AnimationEffect.FADE, new AnimationCallback() {
								@Override
								public void execute(boolean earlyFinish) {
									lastUpdatedLabel.show();
								}
							}, 8000);
						}     
					});
				} catch (Exception ex) {
					System.out.println("Unable to retrieve data from server");
				}
			}
		};

		// Schedule the timer to run once every seconds.
		t.scheduleRepeating(20000);
	}
	   
	/**
	 * Creates an error label if the client should lost connection to the server
	 */
	private void createErrorLabels(){
		connectionLostLabel = new Label("Connection to server lost. Try checking your network connection. If the problem persists, please contact IS dept.");
		connectionLostLabel.setBorder("5px solid red");
		connectionLostLabel.setBackgroundColor("seashell");
		connectionLostLabel.setWidth(listGrid.getOffsetWidth());
		
		connectionLostSpacerLabel = verticalSpacer(3);
	}
	
	/**
	 * Enables the error message to display if the client loses connection to the server
	 */
	private void enableErrorMessage(){
		createErrorLabels();
		errorEnabled = true;
		headerPanel.add(connectionLostLabel);
		headerPanel.add(connectionLostSpacerLabel);
		resizeListGrid();
	}
	
	/**
	 * Disables the error message to display if the client re-establishes the connection to the server
	 */
	private void disableErrorMessage(){
		headerPanel.remove(connectionLostLabel);
		headerPanel.remove(connectionLostSpacerLabel);
		errorEnabled = false;
		resizeListGrid();
	}
	
	/**
	 * decorate the last updated date label
	 */
	private void decorateLastUpdatedLabel() {  
	    lastUpdatedLabel.setLeft(0);   
	    lastUpdatedLabel.setTop(0);   
	    lastUpdatedLabel.setAutoHeight();
	    lastUpdatedLabel.setAutoWidth();
	    lastUpdatedLabel.setBackgroundColor("white");   
	    lastUpdatedLabel.setWrap(false);
	} 
	
	/**
	 * adds vertical space to the panel
	 * 
	 * @param height
	 * @return
	 */
	private Label verticalSpacer(int height) {
		Label emptyLabel = new Label();
		emptyLabel.setLeft(0);   
		emptyLabel.setTop(0);   
		emptyLabel.setHeight(height);
		emptyLabel.setBackgroundColor("white"); 
		return emptyLabel;
	}
	
	private void resizeListGrid(){
		listGrid.setHeight((int)((Window.getClientHeight() - headerPanel.getOffsetHeight() - footerPanel.getOffsetHeight()) * .9));
	}
	
	
	/**
	 * add the links for the  actual columns of 1/2/3 shift and total and the current inventory column for factory news list grid.
	 *
	 * @param factoryNewListGrid the factory new list grid
	 */
	private void prepareFactoryNewsListGridFields(ListGrid factoryNewListGrid) {
		ListGridField actualFirstField = factoryNewListGrid.getField("actual1st");
		ListGridField actualSecondField = factoryNewListGrid.getField("actual2nd");
		ListGridField actualThirdField = factoryNewListGrid.getField("actual3rd");
		ListGridField actualTotalField = factoryNewListGrid.getField("actualTotal");
		ListGridField currentInventoryField = factoryNewListGrid.getField("currentInventory");
		
		actualFirstField.setCellFormatter(actualCellFormatter);
		actualFirstField.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				showActualProductWindow(event.getRecord(), "01", event.getField().getTitle());
			}
		});
		
		actualSecondField.setCellFormatter(actualCellFormatter);
		actualSecondField.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				showActualProductWindow(event.getRecord(), "02", event.getField().getTitle());
			}
		});
		
		actualThirdField.setCellFormatter(actualCellFormatter);
		actualThirdField.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				showActualProductWindow(event.getRecord(), "03", event.getField().getTitle());
			}
		});
		
		actualTotalField.setCellFormatter(actualCellFormatter);
		actualTotalField.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				showActualProductWindow(event.getRecord(), "",  event.getField().getTitle());
			}
		});
		
		currentInventoryField.setCellFormatter(inventoryCellFormatter);
		currentInventoryField.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				showInventoryWindow(event.getRecord());  
			}
		});
	}
	
	/**
	 * Show a modal window for inventory details.
	 *
	 * @param record the clicked factory news record
	 */
	private void showInventoryWindow(Record record) {
		divisionId = record.getAttribute("divisionId");
		lineId = record.getAttribute("lineId");
		
		if (inventoryWindow == null) {
			createInventoryWindow();
		}
		
		Map<String, String> inventoryDataSourceParams = new HashMap<String, String>();
		inventoryDataSourceParams.put("divisionId", divisionId.trim());
		inventoryDataSourceParams.put("lineId", lineId.trim());
		inventoryDataSource.setDefaultParams(inventoryDataSourceParams);
		inventoryListGrid.fetchData();
		String windowTitle = record.getAttribute("divisionName")+ " - " + record.getAttribute("lineName") + ": Inventory Detail";
        inventoryWindow.setTitle(windowTitle);  
        inventoryWindow.show();
	}


	private void createInventoryWindow() {
		inventoryWindow = new com.smartgwt.client.widgets.Window();
		
		String url = GWT.getHostPageBaseURL() + WEB_SERVICE_INVENTORY;
		inventoryDataSource = new DataSource(url);	
		inventoryDataSource.setDataFormat(DSDataFormat.JSON);
		inventoryDataSource.setCacheAllData(true);
		inventoryDataSource.setAutoCacheAllData(false);
		ListGridField productIdField = new ListGridField("productId", "Product ID");
		ListGridField productSpecCodeField = new ListGridField("productSpecCode", "Product Spec Code");
		
		inventoryListGrid = new ListGrid();
		inventoryListGrid.setDataSource(inventoryDataSource);
		inventoryListGrid.setFields(productIdField, productSpecCodeField);
		inventoryListGrid.setWidth100();
		inventoryListGrid.setHeight100();
		inventoryListGrid.setAutoFetchData(false);
		inventoryListGrid.setCanAutoFitFields(false);
		for (ListGridField field: inventoryListGrid.getFields()) {
        	field.setAlign(Alignment.CENTER);
        	field.setCellAlign(Alignment.CENTER);
    	}
		
		Button closeButton = new Button("Close");
		closeButton.setHeight(20);
		closeButton.setWidth(60);
		
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closeInventoryWindow();
			}
		});
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);  
		layout.setLayoutMargin(5);  
        
		layout.addMember(inventoryListGrid);
		layout.addMember(closeButton);
		layout.setDefaultLayoutAlign(Alignment.CENTER);
       
        inventoryWindow.setWidth(600);
        inventoryWindow.setHeight(500);
        int top = listGrid.getAbsoluteTop() + 40;
        int left = listGrid.getAbsoluteLeft() + listGrid.getVisibleWidth()/2 - inventoryWindow.getWidth()/2;
        inventoryWindow.setTop(top);
        inventoryWindow.setLeft(left);
        
        inventoryWindow.setIsModal(true);
        inventoryWindow.setShowModalMask(true);  
        inventoryWindow.setCanDragReposition(true);  
        inventoryWindow.setCanDragResize(true);
        inventoryWindow.addItem(layout);
        
        inventoryWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				closeInventoryWindow();
			}
		});
	}
	
	private void closeInventoryWindow() {
		inventoryDataSource.invalidateCache();
		inventoryListGrid.setData(new Record[0]);
		inventoryWindow.hide();
	}
	
	/**
	 * Show a modal window for actual product details
	 *
	 * @param record the clicked factory news record
	 * @param shift the shift: 01, 02, 03 or "" (empty string) for actual total(all of the 3 shift)
	 * @param fieldTitle the actual field title
	 */
	private void showActualProductWindow(final Record record, String targetShift, String fieldTitle) {
		
		this.divisionId = record.getAttribute("divisionId");
		this.lineId = record.getAttribute("lineId");
		this.shift= targetShift;
		
		if (actualProductWindow == null) {
			createActualProductWindow();
		}
        
		Map<String, String> processPointDataSourceParams = new HashMap<String, String>();
		processPointDataSourceParams.put("divisionId", divisionId.trim());
		processPointDataSourceParams.put("lineId", lineId.trim());
		processPointDataSource.setDefaultParams(processPointDataSourceParams);
		processPointDataSource.invalidateCache();
		processPointComboBox.invalidateDisplayValueCache();
		processPointComboBox.fetchData();
        String windowTitle = record.getAttribute("divisionName")+ " - " + record.getAttribute("lineName") +" - "+ fieldTitle + ": Actual Product Detail";
        actualProductWindow.setTitle(windowTitle);  
        
        actualProductWindow.show();
	}


	private void createActualProductWindow() {
		actualProductWindow = new com.smartgwt.client.widgets.Window();
		actualProductListGrid = new ListGrid();
		//create process point combo box
		processPointDataSource = new DataSource();
		processPointDataSource.setDataFormat(DSDataFormat.JSON);
		String processPointDataSourceUrl = GWT.getHostPageBaseURL() + WEB_SERVICE_PROCESS_POINTS;
		processPointDataSource.setDataURL(processPointDataSourceUrl);
		processPointDataSource.setCacheAllData(true);
		processPointDataSource.setAutoCacheAllData(false);
		processPointComboBox = new SelectItem();  
		processPointComboBox.setName("processPoint");  
		processPointComboBox.setTitle("Process Point");
		processPointComboBox.setOptionDataSource(processPointDataSource);
		processPointComboBox.setDisplayField("processPointName");
		processPointComboBox.setValueField("processPointId");
		processPointComboBox.setAutoFetchData(false);
		processPointComboBox.setWidth(250);
		//When the process point data is loaded and SelectItem can select the first record, but it does't fire the select changed event. 
		//In such case, we need to automatically load the actual product details for the first process point.
		processPointComboBox.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				if(!event.getData().isEmpty()){
					Record processPointRecord = event.getData().get(0);
					processPointComboBox.setValue(processPointRecord.getAttribute("processPointId"));
					refreshActualProductListGrid(actualProductListGrid, divisionId, lineId, shift,
							processPointRecord.getAttribute("processPointId"));
				}
			}
		});
		processPointComboBox.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				refreshActualProductListGrid(actualProductListGrid,divisionId, lineId, shift,
						processPointComboBox.getSelectedRecord().getAttribute("processPointId"));
			}
		});
		
		//Create search bar
		DynamicForm searchForm = new DynamicForm();
		searchForm.setWidth100();
		searchForm.setAutoHeight();
		searchForm.setItems(processPointComboBox);
		
		//Create product list
		actualProductListGrid.setWidth100();
		actualProductListGrid.setHeight100();
		String url = GWT.getHostPageBaseURL() + WEB_SERVICE_ACTUAL_PRODUCTS;
		actualProductDataSource = new DataSource(url);
		actualProductDataSource.setDataFormat(DSDataFormat.JSON);
		actualProductDataSource.setCacheAllData(true);
		actualProductDataSource.setAutoCacheAllData(false);
		DataSourceTextField productIdField = new DataSourceTextField();
		productIdField.setValueXPath("id/productId");
		productIdField.setName("productId");
		productIdField.setTitle("Product ID");
		DataSourceTextField productSpecCodeField = new DataSourceTextField("productSpecCode", "Product Spec Code");
		actualProductDataSource.setFields(productIdField, productSpecCodeField);
		
		ListGridField productIdListGridField = new ListGridField("productId", "Product ID");
		ListGridField productSpecCodeListGridField = new ListGridField("productSpecCode", "Product Spec Code");
		actualProductListGrid.setDataSource(actualProductDataSource);
		actualProductListGrid.setFields(productIdListGridField, productSpecCodeListGridField);
		actualProductListGrid.setAutoFetchData(false);
		actualProductListGrid.setCanAutoFitFields(false);
		for (ListGridField field: actualProductListGrid.getFields()) {
        	field.setAlign(Alignment.CENTER);
        	field.setCellAlign(Alignment.CENTER);
    	}
		
		Button closeButton = new Button("Close");
		closeButton.setHeight(20);
		closeButton.setWidth(60);
		
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closeActualProductWindow();
			}
		});
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);  
		layout.setLayoutMargin(5);  
		layout.addMember(searchForm);
		layout.addMember(actualProductListGrid);
		layout.addMember(closeButton);
		layout.setDefaultLayoutAlign(Alignment.CENTER);
		
		actualProductWindow.setWidth(600);
        actualProductWindow.setHeight(500);
        int top = listGrid.getAbsoluteTop() + 40;
        int left = listGrid.getAbsoluteLeft() + listGrid.getVisibleWidth()/2 - actualProductWindow.getWidth()/2;
        actualProductWindow.setTop(top);
        actualProductWindow.setLeft(left);
        
        actualProductWindow.setIsModal(true);
        actualProductWindow.setShowModalMask(true);  
        actualProductWindow.setCanDragReposition(true);  
        actualProductWindow.setCanDragResize(true);
        
        actualProductWindow.addItem(layout);  
        actualProductWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				closeActualProductWindow();
			}
		});
	}
	
	private void closeActualProductWindow() {
		actualProductDataSource.invalidateCache();
		actualProductListGrid.setData(new Record[0]);
		actualProductWindow.hide();
	}
	
	/**
	 * Refresh actual product list grid.
	 *
	 * @param actualProductListGrid the actual product list grid
	 * @param divisionId the division id
	 * @param lineId the line id
	 * @param shift the shift
	 * @param processPointId the process point id
	 */
	private void refreshActualProductListGrid(
			final ListGrid actualProductListGrid,
			final String divisionId,
			final String lineId,
			final String shift,
			final String processPointId) {
		Map<String, String> actualProductDataSourceParams = new HashMap<String, String>();
		actualProductDataSourceParams.put("divisionId", divisionId.trim());
		actualProductDataSourceParams.put("lineId", lineId.trim());
		actualProductDataSourceParams.put("shift", shift.trim());
		actualProductDataSourceParams.put("processPointId", processPointId.trim());
		
		actualProductListGrid.getDataSource().setDefaultParams(actualProductDataSourceParams);
		actualProductListGrid.getDataSource().invalidateCache();
		actualProductListGrid.setData(new Record[0]);
		actualProductListGrid.fetchData();
	}

}   

