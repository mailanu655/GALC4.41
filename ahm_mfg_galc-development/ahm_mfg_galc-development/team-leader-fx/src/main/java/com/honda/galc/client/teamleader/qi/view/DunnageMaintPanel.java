package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.DunnageMaintController;
import com.honda.galc.client.teamleader.qi.model.DunnageMaintModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * 
 * <h3>DunnageMaintPanel Class description</h3>
 * <p> DunnageMaintPanel description </p>
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
 * @author L&T Infotech<br>
 * April 20, 2017
 *
 *
 */
public class DunnageMaintPanel extends  QiAbstractTabbedView<DunnageMaintModel,DunnageMaintController>{

	private ObjectTablePane<Map<String,Object>> DunnageTablePane;
	double  screenWidth;
	double  screenHeight;
	LoggedButton clearButton;
	LoggedButton shipButton;
	LoggedButton printButton;
	public ObjectTablePane<Map<String,Object>> getDunnageTablePane() {
		return DunnageTablePane;
	}
	public void setDunnageTablePane(ObjectTablePane<Map<String,Object>> dunnageTablePane) {
		DunnageTablePane = dunnageTablePane;
	}

	public LabeledUpperCaseTextField getDunnageTextField() {
		return dunnageTextField;
	}

	public void setDunnageTextField(LabeledUpperCaseTextField dunnageTextField) {
		this.dunnageTextField = dunnageTextField;
	}

	public LabeledUpperCaseTextField getBlockTextField() {
		return blockTextField;
	}

	public void setBlockTextField(LabeledUpperCaseTextField blockTextField) {
		this.blockTextField = blockTextField;
	}

	private LabeledUpperCaseTextField dunnageTextField;
	private LabeledUpperCaseTextField blockTextField;

	private String cssFontStyle; 


	public DunnageMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	public void onTabSelected() {
	}

	public String getScreenName() {
		return "Dunnage";
	}

	public void reload() {
	}
	
	public void reload(String dunnageNumber) {
		DunnageTablePane.setData(getModel().selectDunnageProductsData(dunnageNumber));
	}
	
	@Override
	public void start() {
	}

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		screenWidth= primaryScreenBounds.getWidth();
		screenHeight= primaryScreenBounds.getHeight();
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		MigPane migPane = new MigPane();				

		dunnageTextField = new LabeledUpperCaseTextField("Dunnage", "Dunnage",25, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,null);
		dunnageTextField.getLabel().setPadding(new Insets(10,15,0,10));
		dunnageTextField.setStyle(cssFontStyle);
		dunnageTextField.getControl().setOnAction(getController());
		dunnageTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(12));

		String productType=getModel().getProductType();

		blockTextField = new LabeledUpperCaseTextField(productType, "Block", 30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,null);
		blockTextField.getLabel().setPadding(new Insets(10,15,0,5));
		blockTextField.setStyle(cssFontStyle);
		blockTextField.getControl().setOnAction(getController());


		clearButton = createBtn("Clear",getController());
		clearButton.setMinWidth(100);
		shipButton = createBtn("Ship",getController());
		shipButton.setMinWidth(100);

		
		printButton = createBtn("Print",getController());
		printButton.setMinWidth(100);
		printButton.setDisable(true);
		
		DunnageTablePane = (ObjectTablePane<Map<String,Object>>) createDunnageTablePane(getModel().getApplicationContext().getProductTypeData().getProductType(),PropertyService.getPropertyBean(DunnagePropertyBean.class, getModel().getApplicationContext().getApplicationId()));
		DunnageTablePane.getTable().setRowFactory(new Callback<TableView<Map<String,Object>>, TableRow<Map<String,Object>>>() {
			public TableRow<Map<String,Object>> call(TableView<Map<String,Object>> tableView) {
				final TableRow<Map<String,Object>> row = new TableRow<Map<String,Object>>() {
					protected void updateItem(Map<String,Object> map, boolean paramBoolean) {
						super.updateItem(map, paramBoolean);
						if(!paramBoolean && map!=null) {
							if (!map.get("offedLabel").equals("OK") || !map.get("defect").equals("OK") || !map.get("hold").equals("OK")){
									setStyle("-fx-text-background-color: red;");
							}else{
								setStyle("-fx-text-background-color: black;");
							}
						} 
					}
				};
                  return row;
            }
     });
		migPane.add(dunnageTextField,"span 3");
		migPane.add(blockTextField,"span 3");
		migPane.add(clearButton,"span 2");
		migPane.add(shipButton,"span 1");
		migPane.add(printButton,"span 1");
		migPane.add(DunnageTablePane,"span 10");
		LoggedTableColumn<Map<String,Object>, Integer> column = new LoggedTableColumn<Map<String,Object>, Integer>();
		createSerialNumber(column);
		DunnageTablePane.getTable().getColumns().add(0, column);
		DunnageTablePane.getTable().getColumns().get(0).setText("#");
		DunnageTablePane.getTable().getColumns().get(0).setResizable(true);
		DunnageTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		DunnageTablePane.getTable().getColumns().get(0).setMinWidth(1);

		this.setTop(migPane);
		this.setCenter(DunnageTablePane);

	}
	
	public LoggedButton getClearButton() {
		return clearButton;
	}
	public void setClearButton(LoggedButton clearButton) {
		this.clearButton = clearButton;
	}
	public LoggedButton getShipButton() {
		return shipButton;
	}
	public void setShipButton(LoggedButton shipButton) {
		this.shipButton = shipButton;
	}
	public String getCssFontStyle() {
		return cssFontStyle;
	}

	public void setCssFontStyle(String cssFontStyle) {
		this.cssFontStyle = cssFontStyle;
	}

	private ObjectTablePane<Map<String,Object>> createDunnageTablePane(ProductType productType,DunnagePropertyBean property){
		ColumnMappingList columnMappingList ;
		if (ProductTypeUtil.isInstanceOf(productType, MbpnProduct.class)) {

			columnMappingList = ColumnMappingList.with("Product Id", "productId")
					.put("Spec Code", "currentProductSpecCode")
					.put("Order No", "currentOrderNo")
					.put("Container", "containerId")
					.put("Seq", "trackingSeq")
					.put("Status", "defect")
					.put("Hold", "onHold");
			Double[] columnWidth = new Double[] {
					0.10, 0.10, 0.10, 0.10, 0.08, 0.08, 0.08
			};
			List<Double> columnList = new ArrayList<Double>(); 
			columnList.addAll(Arrays.asList(columnWidth));
			if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
				columnMappingList.put("Passed OFF", "offedLabel");
				columnList.add(0.10);
			}
			if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
				columnMappingList.put("Shipped", "shippedLabel");
				columnList.add(0.08);
			}
			if (property.isInsertDunnageContent()) {
				columnMappingList.put("Matrix(R,C,L)", "matrix");
				columnList.add(0.10);
			}
			ObjectTablePane<Map<String,Object>> panel=new ObjectTablePane<Map<String,Object>>(columnMappingList,columnList.toArray(new Double[columnList.size()]));
			return panel;
		}
		else  if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
			columnMappingList = ColumnMappingList.with("Die Cast Number", "dcSerialNumber")
					.put("Machining Number", "mcSerialNumber")
					.put("Machine", "machineId")
					.put("Die", "dieId")
					.put("Model", "model")
					.put("Defect", "defect")
					.put("Hold", "hold");

			Double[] columnWidth = new Double[] {
					0.10, 0.10, 0.10, 0.10, 0.05, 0.05, 0.05
			};
			List<Double> columnList = new ArrayList<Double>(); 
			columnList.addAll(Arrays.asList(columnWidth));
			if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
				String label = getOffLabel(property);
				columnMappingList.put(label + " Off", "offedLabel");
				columnMappingList.put(label + " Off Date", "offDate");
				columnList.add(0.05);
				columnList.add(0.10);
			}
			if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
				columnMappingList.put("Shipped", "shippedLabel");
				columnList.add(0.10);
			}
			if (property.isInsertDunnageContent()) {
				columnMappingList.put("Matrix(R,C,L)", "matrix");
				columnList.add(0.15);
			}
			ObjectTablePane<Map<String,Object>> panel=new ObjectTablePane<Map<String,Object>>(columnMappingList,columnList.toArray(new Double[columnList.size()]));
			return panel;
		}

		else {
			columnMappingList = ColumnMappingList.with("Product Id", "productId")
					.put("Produc Spec", "productSpecCode")
					.put("Defect", "defect")
					.put("Hold", "hold");
		}
		Double[] columnWidth = new Double[] {
				0.15, 0.15, 0.10,0.10
		};
		List<Double> columnList = new ArrayList<Double>(); 
		columnList.addAll(Arrays.asList(columnWidth));

		if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
			String label = getOffLabel(property);
			columnMappingList.put(label + " Off", "offedLabel");
			columnMappingList.put(label + " Off Date", "offDate");
			columnList.add(0.15);
			columnList.add(0.15);
		}
		if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
			columnMappingList.put("Shipped", "shippedLabel");
			columnList.add(0.15);
		}
		ObjectTablePane<Map<String,Object>> panel=new ObjectTablePane<Map<String,Object>>(columnMappingList,columnList.toArray(new Double[columnList.size()]));
		return panel;
	}
	public static String getOffLabel(DunnagePropertyBean property) {
		String label = property.getOffLabel();
		if (label == null || label.trim().length() == 0) {
			String[] ids = property.getOffProcessPointIds();
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(ids[0]);
			label = processPoint.getDivisionName();
		}
		return label;
	}
	public LoggedButton getPrintButton() {
		return printButton;
	}
	public void setPrintButton(LoggedButton printButton) {
		this.printButton = printButton;
	}
	
}
