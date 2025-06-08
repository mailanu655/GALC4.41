package com.honda.galc.client.teamleader.qi.productRecovery;

import java.util.List;
/**
 * 
 * <h3>PartResult</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartResult description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author L&T Infotech
 * Aug 28, 2017
 */

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.ManualLotControlRepairConstants;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
public class ManualLotControlRepairPanel extends ApplicationMainPane{
	private ObjectTablePane<PartResult> partStatusPanel;
	private LoggedButton enterResultButton;
	private LoggedButton removeResultButton;
	private LoggedButton refreshButton;
	private LoggedButton setResultButton;
	private LoggedButton enterMultiResultButton;
	protected ManualLotControlRepairPropertyBean property;
	private ManualLotControlRepairController controller;
	protected ProductType currentProductType;
	private ProductTypeData productTypeData;
	private Stage stage;
	private String productId;
	private BaseProduct product;


	public ManualLotControlRepairPanel(MainWindow window, String productId, BaseProduct product,List<ProductTypeData> productTypeDataList,ProductType productType) {
		super(window);
		stage = ClientMainFx.getInstance().getStage(getApplicationId());
		this.productId=productId;
		this.setProduct(product);
		currentProductType=productType;
		getProductTypeData(productTypeDataList);
		initialize();
		initView();
		controller.initialize();
		reload(productId);
	}

	public ManualLotControlRepairPanel(MainWindow window, List<ProductTypeData> productTypeDataList,ProductType productType) {
		super(window);
		stage = ClientMainFx.getInstance().getStage(getApplicationId());
		currentProductType=productType;
		getProductTypeData(productTypeDataList);
		initialize();
		initView();
		controller.initialize();
	}

	private void initialize() {
		property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class, 
				window.getApplication().getApplicationId());
		controller = createController();
	}

	protected ManualLotControlRepairController createController() {
		if(ProductTypeUtil.isMbpnProduct(getProductType().name())){
			controller = new ManualLotControlRepairMbpnController(window, this,
					new ManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication()));
		}
		else{
			switch (getProductTypeData().getProductType()) {
			case ENGINE :
				controller = new ManualLotControlRepairEngineController(window,this, 
						new EngineManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case FRAME :
				controller = new ManualLotControlRepairFrameController(window,this, 
						new FrameManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case KNUCKLE :
				controller = new ManualLotControlRepairKnuckleController(window,this, 
						new KnuckleManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case HEAD :
				controller = new ManualLotControlRepairHeadController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case BLOCK :
				controller = new ManualLotControlRepairBlockController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case CONROD :
				controller = new ManualLotControlRepairConrodController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case CRANKSHAFT :
				controller = new ManualLotControlRepairCrankshaftController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case IPU :
				controller = new ManualLotControlRepairIpuController(window,this, 
						new IpuManualLtCtrResultEntereViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case FIPUCASE   :
				controller = new ManualLotControlRepairFrontIpuCaseController(window,this, 
						new FrontIpuCaseManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			case RIPUCASE   :
				controller = new ManualLotControlRepairRearIpuCaseController(window,this, 
						new RearIpuCaseManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication(), currentProductType));
				break;
			default :
				controller = new ManualLotControlRepairBaseController(window,this, 
						new ManualLtCtrResultEnterViewManager(window.getApplicationContext(), window.getApplication()));
			}
		}
		return controller;
	}

	public void reload(String productId) {
		clearErrorMessage();
		getController().loadProductId(false, productId);
	}

	@SuppressWarnings("unchecked")
	public void initView() {
		VBox outerPane = new VBox();
		HBox buttonContainer = new HBox();
		partStatusPanel = createRepairAreaTablePane();
		enterResultButton = createBtn(ManualLotControlRepairConstants.ENTER_RESULT, getController(), true);
		enterMultiResultButton = createBtn(ManualLotControlRepairConstants.ENTER_MULTIPLE_RESULT, getController(), true);
		removeResultButton = createBtn(ManualLotControlRepairConstants.REMOVE_RESULT,getController(), true);
		refreshButton = createBtn(QiConstant.REFRESH,getController(), true);
		setResultButton = createBtn(ManualLotControlRepairConstants.SET_RESULT_NG,getController(), true);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 10));
		buttonContainer.setSpacing(150);
		buttonContainer.getChildren().addAll(enterResultButton,enterMultiResultButton, removeResultButton,setResultButton, refreshButton);
		outerPane.getChildren().addAll(createTitiledPane("Part Status Panel", partStatusPanel),buttonContainer);
		this.setCenter(outerPane);
	}

	private ObjectTablePane<PartResult> createRepairAreaTablePane(){
		ColumnMappingList columnMappingList=new ColumnMappingList();
		for(String columns : property.getMlcrColumns()){
			if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.SHIP_STATUS))
				columnMappingList.put(columns, "shipStatus");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.STATUS))
				columnMappingList.put(columns, "qiStatus");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.STATUS_MEASURE))
				columnMappingList.put(columns, "qiStatusMeasure");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.PART_NAME_WINDOW_LABEL))
				columnMappingList.put(columns, "partNameWindowLabel");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.PROCESS_POINT_NAME))
				columnMappingList.put(columns, "processPointName");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.PART_NAME))
				columnMappingList.put(columns, "partName");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.PART_SERIAL_NUMBER))
				columnMappingList.put(columns, "partSerialNumber");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.MEASUREMENT_RESULT))
				columnMappingList.put(columns, "measurementResult");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.TIME_STAMP))
				columnMappingList.put(columns, "actualDate");
			else if(columns.equalsIgnoreCase(ManualLotControlRepairConstants.PROCESS_POINT))
				columnMappingList.put(columns, "processPointId");
			else if(columns.equalsIgnoreCase(QiConstant.REPAIRED))
				columnMappingList.put(columns, "repaired");
		}		
		Double[] columnWidth = new Double[] {
				0.05, 0.05, 0.05, 0.12, 0.12, 0.12, 0.1, 0.12, 0.1, 0.12, 0.1
		}; 
		ObjectTablePane<PartResult> panel = new ObjectTablePane<PartResult>(columnMappingList,columnWidth);
		panel.setId("part-status-panel");
		panel.setConstrainedResize(false);
		return panel;
	}

	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		return titledPane;
	}

	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler , boolean isDisabled)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setOnAction(handler);
		btn.getStyleClass().add("station-btn");
		btn.setPrefWidth(320);
		btn.setDisable(isDisabled);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		return btn;
	}

	public ProductType getProductType(){
		return currentProductType ;
	}

	public ProductTypeData getProductTypeData(List<ProductTypeData> list){
		if(productTypeData == null){
			for(ProductTypeData type :list){
				if(type.getProductTypeName().equals(getProductType().toString())){
					productTypeData = type;
					break;
				}
			}
		}
		return productTypeData;
	}

	public ProductTypeData getProductTypeData() {
		return productTypeData;
	}

	public void setProductTypeData(ProductTypeData productTypeData) {
		this.productTypeData = productTypeData;
	}

	public ObjectTablePane<PartResult> getPartStatusPanel() {
		return partStatusPanel;
	}

	public LoggedButton getEnterResultButton() {
		return enterResultButton;
	}


	public LoggedButton getRemoveResultButton() {
		return removeResultButton;
	}


	public LoggedButton getEnterMultiResultButton() {
		return enterMultiResultButton;
	}

	public LoggedButton getRefreshButton() {
		return refreshButton;
	}

	public LoggedButton getSetResultButton() {
		return setResultButton;
	}

	public ManualLotControlRepairPropertyBean getProperty() {
		return property;
	}

	public Stage getStage() {
		return stage;
	}

	public boolean isRemoveIEnabled(){
		Boolean removeIEnabled  = PropertyService.getPropertyBoolean("DEFAULT_PRODUCT_CLIENT", "REMOVE_I_ENABLED", false);
		return removeIEnabled;
	}

	public ManualLotControlRepairController getController() {
		return controller;
	}

	public String getProductId() {
		return productId;
	}

	/**
	 * This method is used to clear the exception display message.
	 */
	public void clearDisplayMessage() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
		this.productId = product.getProductId();
	}
}
