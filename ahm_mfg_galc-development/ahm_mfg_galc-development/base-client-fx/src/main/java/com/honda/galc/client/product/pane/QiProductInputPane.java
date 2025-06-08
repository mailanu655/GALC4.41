package com.honda.galc.client.product.pane;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiInspectionPartDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HomeScreenForUpcInputPane</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Shweta Kadav
 */
public class QiProductInputPane extends AbstractProductInputPane implements EventHandler<ActionEvent> {

	private static final long serialVersionUID = 1L;
	
	private long maxQty = 0;
	private static final int MAX_UPC_COUNT_FOR_A_DAY = 99999;
	
	private Mbpn mbpn;
	private QiPropertyBean property;
	
	private String processPointId;
	private String selectedAssociateId;
	private static final String A = "a";
	private static final String B = "b";
	private static final String C = "c";

	private LoggedTextField lotTextField;
	private TextField productIdTextField;
	private LoggedTextField quantityTextField;
	
	private LoggedButton enterPartButton;
	
	private ComboBox<String> mtcComboBox;
	private ComboBox<Mbpn> descComboBox;
	private ComboBox<QiInspectionPartDto> partComboBox;
	private ComboBox<QiStationEntryDepartment> entryDeptComboBox;
	
	
	public QiProductInputPane(ProductController productController) {
		super(productController);
		initView(productController.getProductTypeData());
		getMtcModelAndDescDataByPartName();
		getMbpnDescByMainNo();
		EventBusUtil.register(this);
	}
	

	public void initView(ProductTypeData productTypeData) {

		GridPane defectDataContainer = createTopPanelGrid();
		
		HBox secondHalf = new HBox();
		secondHalf.setPadding(new Insets(10,20,0,1200));

		LoggedLabel entryDeptLbl = createLabel("label","Entry Dept",new Insets(0,0,0,0));
		LoggedLabel deptAsteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"),new Insets(5,0,0,70));
		
		defectDataContainer.add(entryDeptLbl, 0, 1);
		defectDataContainer.add(deptAsteriskLbl, 0, 1);
		
		LoggedLabel partLbl = createLabel("label","Part",new Insets(0,0,0,0));
		LoggedLabel partAsteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"),new Insets(5,0,0,30));
		
		defectDataContainer.add(partLbl, 1, 1);
		defectDataContainer.add(partAsteriskLbl, 1, 1);
		
		LoggedLabel mtcModelLbl = createLabel("label","MTC Model",new Insets(0,10,0,0));
		LoggedLabel mtcAsteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"),new Insets(5,0,0,70));
		
		defectDataContainer.add(mtcModelLbl, 2, 1);
		defectDataContainer.add(mtcAsteriskLbl, 2, 1);
		
		LoggedLabel descriptionLbl = createLabel("label","Desc",new Insets(0,10,0,0));
		LoggedLabel descriptionAsteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"),new Insets(5,0,0,35));
		
		defectDataContainer.add(descriptionLbl, 3, 1);
		defectDataContainer.add(descriptionAsteriskLbl, 3, 1);
		
		LoggedLabel lotLbl = createLabel("label","Lot",new Insets(0,10,0,0));
		defectDataContainer.add(lotLbl, 4, 1);
		
		LoggedLabel qtyLbl = createLabel("label","Quantity",new Insets(0,10,0,0));
		LoggedLabel qtyAsteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"),new Insets(5,0,0,55));
		
		defectDataContainer.add(qtyLbl, 5, 1);
		defectDataContainer.add(qtyAsteriskLbl, 5, 1);
		
		entryDeptComboBox  = new ComboBox<QiStationEntryDepartment>();
		entryDeptComboBox.setPrefHeight(30);
		entryDeptComboBox.setMinHeight(30);
		entryDeptComboBox.setMaxHeight(30);


			List<QiStationEntryDepartment> deptList = getDao(QiStationEntryDepartmentDao.class).findAllEntryDeptInfoByProcessPoint(getProductController().getProcessPointId());
			if(deptList.size()>1){
				entryDeptComboBox.setItems(FXCollections.observableArrayList(deptList));
			}
		QiStationEntryDepartment qiStationEntryDepartment = getDao(QiStationEntryDepartmentDao.class).findDefaultEntryDeptByProcessPoint(getProductController().getProcessPointId());
		if(qiStationEntryDepartment!=null){
			entryDeptComboBox.getSelectionModel().select(qiStationEntryDepartment);
			entryDeptComboBox.setConverter(qiDivisionConverter());
		}

		defectDataContainer.add(entryDeptComboBox, 0, 2);

		partComboBox  = new ComboBox<QiInspectionPartDto>();
		partComboBox.setPrefHeight(30);
		partComboBox.setMinHeight(30);
		partComboBox.setMaxHeight(30);
		partComboBox.setItems(FXCollections.observableArrayList(getDao(QiBomQicsPartMappingDao.class).findAllByProcessPointId(ApplicationContext.getInstance().getProcessPointId())));
		partComboBox.setConverter(qiPartNameConverter());
		partComboBox.setOnAction(this);

		defectDataContainer.add(partComboBox, 1, 2);
		
		mtcComboBox  = new ComboBox<String>();
		mtcComboBox.setPrefHeight(30);
		mtcComboBox.setMinHeight(30);
		mtcComboBox.setMaxHeight(30);
		mtcComboBox.setOnAction(this);

		defectDataContainer.add(mtcComboBox, 2, 2);
		

		descComboBox  = new ComboBox<Mbpn>();
		descComboBox.setPrefHeight(30);
		descComboBox.setMinHeight(30);
		descComboBox.setMaxHeight(30);
		
		defectDataContainer.add(descComboBox, 3, 2);

		
		lotTextField = (UpperCaseFieldBean) UiFactory.createTextField("lot", 12, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.TOP_LEFT, true);

		defectDataContainer.add(lotTextField, 4, 2);
		
		
		quantityTextField = (UpperCaseFieldBean) UiFactory.createTextField("qty", 12, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.TOP_LEFT, true);
		QiStationConfiguration qiStationConfiguration = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProductController().getProcessPointId(),QiEntryStationConfigurationSettings.DEFAULT_UPC_QUANTITY.getSettingsName());
		if(qiStationConfiguration!=null){
			quantityTextField.setText(qiStationConfiguration.getPropertyValue());
		}else{
			quantityTextField.setText(QiEntryStationConfigurationSettings.DEFAULT_UPC_QUANTITY.getDefaultPropertyValue());
		}
		defectDataContainer.add(quantityTextField, 5, 2);
		
		enterPartButton = UiFactory.createButton("Enter Part", Fonts.SS_DIALOG_BOLD(15), true);
		enterPartButton.setOnAction(this);

		defectDataContainer.add(enterPartButton, 6, 2);
		getChildren().add(defectDataContainer);
		setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");

	}


	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == enterPartButton){
			generateUpcMbpnProductId(event);
		}
		
	}

	public QiPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(QiPropertyBean.class, ApplicationContext.getInstance().getProcessPointId());
		}
		return property;
	}

	private void generateUpcMbpnProductId(ActionEvent event) {
		if(validate()){

			Date today = new Date(System.currentTimeMillis());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		    String  date = DATE_FORMAT.format(today);
		    String yearMonthDay = getYearMonthDay();
		    
		    String productId = generateProductIdForUpc(yearMonthDay);
		     
		    List<MbpnProduct> mbpnProducts = createMbpnProductList(productId,getDao(MbpnProductDao.class).findAllByCreateTimeStamp(yearMonthDay, date));
			
		    if(!mbpnProducts.isEmpty()){
				if(checkAllPreRequisite(productId)){
					getDao(MbpnProductDao.class).saveAll(mbpnProducts);
					productId = mbpnProducts.get(0).getProductId();
				}else{
					return;
				}
			}

			HashMap<String,Object> map = getEventDataMap(mbpnProducts);
			
			if (!(productId == null || StringUtils.isEmpty(productId))) {
				getProductIdField().setText(productId);
				EventBusUtil.publishAndWait(new ProductEvent(map, ProductEventType.PRODUCT_INPUT_RECIEVED));
			}
		}

	}

	private boolean checkAllPreRequisite(String productId) {
		boolean isAllDataAvailable = false;
			if(isEntryDepartmentConfigured() && isWriteUpDepartmentConfigured() && isDefectStatusConfigured() && isAssociateIdSelected()){
				isAllDataAvailable = isEntryScreenConfigured();
			}
			return isAllDataAvailable;
	}
	
	private boolean isAssociateIdSelected() {
		QiStationConfiguration qiEntryStationConfigManagement = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProductController().getProcessPointId(),QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getSettingsName());
		if(qiEntryStationConfigManagement!=null ){
			if(!qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO)){
				if(selectedAssociateId == null || selectedAssociateId.isEmpty() || selectedAssociateId.equalsIgnoreCase(QiConstant.NEW)){
					EventBusUtil.publish(new StatusMessageEvent("Please Select Associate Id ", StatusMessageEventType.ERROR));
					return false;
				}
			}
		}else if(!QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.NO)){
			if(selectedAssociateId == null || selectedAssociateId.isEmpty() || selectedAssociateId.equalsIgnoreCase(QiConstant.NEW)){
				EventBusUtil.publish(new StatusMessageEvent("Please Select Associate Id ", StatusMessageEventType.ERROR));
				return false;
			}
		}
		
	
		return true;
	}
	
	private boolean isWriteUpDepartmentConfigured() {
		List<String> writeUpDeptLists =  getDao(QiStationWriteUpDepartmentDao.class).findAllWriteUpDeptByProcessPoint(getProductController().getProcessPointId());
		if(writeUpDeptLists == null || writeUpDeptLists.isEmpty()){
			EventBusUtil.publish(new StatusMessageEvent("Write up department not configured for Process point :"+getProductController().getProcessPointId(), StatusMessageEventType.ERROR));
			return false;
		}
		return true;
	}


	private boolean isEntryScreenConfigured() {
		String productKind  = PropertyService.getPropertyBean(ApplicationPropertyBean.class).getProductKind();
		String productType  = PropertyService.getPropertyBean(ApplicationPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).getProductType();
		Object[] keyTokens = {"QiStationEntryScreenDao.findAllEntryScreenByProcessPoint", productKind, getPartComboBox().getSelectionModel().getSelectedItem().getInspectionPartName(), getProductController().getProcessPointId(), getMtcComboBox().getSelectionModel().getSelectedItem(), getEntryDeptComboBox().getSelectionModel().getSelectedItem().getId().getDivisionId(), false, productType};
		String key = StringUtils.join(keyTokens, Delimiter.DOT);		
		
		List<QiDefectEntryDto> entryScreenList = null;
		if (getProductController().getModel().getClientCache().containsKey(key)) {
			 entryScreenList = getProductController().getModel().getClientCache().getList(key, QiDefectEntryDto.class);
		} else {
			entryScreenList = getDao(QiStationEntryScreenDao.class).findAllEntryScreenByProcessPoint(getPartComboBox().getSelectionModel().getSelectedItem().getInspectionPartName(), getProductController().getProcessPointId(), getMtcComboBox().getSelectionModel().getSelectedItem(), getEntryDeptComboBox().getSelectionModel().getSelectedItem().getId().getDivisionId(), false);
			getProductController().getModel().getClientCache().put(key, entryScreenList);
		}
		if(entryScreenList == null || entryScreenList.isEmpty()){
			EventBusUtil.publish(new StatusMessageEvent("Entry screen not configured for process point "+getProductController().getProcessPointId(), StatusMessageEventType.ERROR));
			return false;
		}
		return true;
	}


	private boolean isDefectStatusConfigured() {
		QiStationConfiguration qiEntryStationConfigManagement = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProductController().getProcessPointId(), QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS);
		if(qiEntryStationConfigManagement ==null){
			EventBusUtil.publish(new StatusMessageEvent("Defect Status not configured for Process Point  :"+getProductController().getProcessPointId(), StatusMessageEventType.ERROR));
			return false;
		}
		return true;
	}


	private boolean isEntryDepartmentConfigured() {
		List<QiStationEntryDepartment> deptList = getDao(QiStationEntryDepartmentDao.class).findAllEntryDeptInfoByProcessPoint(getProductController().getProcessPointId());
		if(deptList == null || deptList.isEmpty()){
			EventBusUtil.publish(new StatusMessageEvent("Entry Department not configured for Process point :"+getProductController().getProcessPointId(), StatusMessageEventType.ERROR));
			return false;
		}

		return true;
	}


	private HashMap<String, Object> getEventDataMap(List<MbpnProduct> mbpnProducts) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("inspectionPartName", getPartComboBox().getSelectionModel().getSelectedItem().getInspectionPartName());
		map.put("products", mbpnProducts);
		map.put("department", getEntryDeptComboBox().getSelectionModel().getSelectedItem().getId().getDivisionId());
		map.put("quantity", getQuantityTextField().getText());
		map.put("mtcModel", getMtcComboBox().getSelectionModel().getSelectedItem());
		return map;
	}



	private List<MbpnProduct> createMbpnProductList(String productId, List<MbpnProduct> products) {
		
		int productIdSuffix = 0;
		String  formatDate ="";
		
		MbpnProduct product = null;
		List<MbpnProduct> mbpnProducts = new ArrayList<MbpnProduct>();
		
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd");
	    String  date = DATE_FORMAT.format(today);
	    
	    if (null != products && !products.isEmpty()) {
		    for (int i = 0; i < products.size(); i++) {
		    	product = products.get(i);
		    	formatDate = DATE_FORMAT.format(product.getCreateTimestamp());
		    	if (formatDate.equals(date)) {
		    		String selectedId = product.getProductId();
			    	try {
			    		//generated UPC format: MainNo(5) + SupplierNo(2) + Year(2) + Month(1) + Date(2) + Sequence(5) = 17 chars
			    		productIdSuffix = Integer.valueOf(selectedId.substring(selectedId.length() - 5));
			    		break;
			    	} catch (NumberFormatException e) {
			    		//non generated UPC code
			    		continue;
			    	}
			    } else {
			    	break;
			    }
		    }
	    }
	     
	     if(productIdSuffix+Integer.parseInt(quantityTextField.getText()) > MAX_UPC_COUNT_FOR_A_DAY){
	    	 EventBusUtil.publish(new StatusMessageEvent("Maximum  "+MAX_UPC_COUNT_FOR_A_DAY+ " products for a day is allowed", StatusMessageEventType.ERROR));
	     }else{
	    	 String lineId = getDao(ProcessPointDao.class).findByKey(ApplicationContext.getInstance().getProcessPointId()).getLineId();
	    	 for (int i=1;i<=Integer.parseInt(quantityTextField.getText());i++){
	 			MbpnProduct mbpnProduct = new MbpnProduct();
	 			mbpnProduct.setTrackingStatus(lineId);
	 			mbpnProduct.setLastPassingProcessPointId(ApplicationContext.getInstance().getProcessPointId());
	 			mbpnProduct.setProductId(productId+String.valueOf(String.format("%05d", productIdSuffix+1)));
	 			mbpnProduct.setCurrentProductSpecCode(mbpn.getProductSpecCode());
	 			mbpnProduct.setCurrentOrderNo(getLotTextField().getText());

	 			productIdSuffix++;
	 			mbpnProducts.add(mbpnProduct);
	 		}
	     }
		
		return mbpnProducts;
	}



	private String generateProductIdForUpc(String yearMonthDay) {
		String mainNo = getPartComboBox().getSelectionModel().getSelectedItem().getMainPartNo().trim();
		String supplierNo = property.getSupplierNo();
		return mainNo + supplierNo + yearMonthDay;
	}

	private String getYearMonthDay() {
		Date today = new Date(System.currentTimeMillis());
		
		SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yy");
		String year = YEAR_FORMAT.format(today);

		String month = getMonth(today);

		SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd");
		String day = DAY_FORMAT.format(today);
		return year + month + day;
	}

	private String getMonth(Date today) {
		SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("M");
		String month = MONTH_FORMAT.format(today);
		if(month.equals("10")){
			month = A;
		}else if(month.equals("11")){
			month = B;
		}else if(month.equals("12")){
			month = C;
		}
		return month;
	}	

	private boolean validate() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
		property = getProperty();
		if(getEntryDeptComboBox().getSelectionModel().getSelectedItem() == null){
			EventBusUtil.publish(new StatusMessageEvent("Please select Entry Department", StatusMessageEventType.ERROR));
			return false;
		}if(getPartComboBox().getSelectionModel().getSelectedItem() == null){
			EventBusUtil.publish(new StatusMessageEvent("Please select Part", StatusMessageEventType.ERROR));
			return false;
		}if(getMtcComboBox().getSelectionModel().getSelectedItem() == null){
			EventBusUtil.publish(new StatusMessageEvent("Please select Mtc Model", StatusMessageEventType.ERROR));
			return false;
		}if(getDescComboBox().getSelectionModel().getSelectedItem() == null){
			EventBusUtil.publish(new StatusMessageEvent("Please select Description", StatusMessageEventType.ERROR));
			return false;
		}if(null!=getLotTextField() && StringUtils.isNotEmpty(getLotTextField().getText())){
			if(getDao(PreProductionLotDao.class).findByKey(getLotTextField().getText()) == null){
				if(property.isAllowToAddNewLot()){
					String newlotNumber=getLotTextField().getText().trim();
					String processLocation = property.getProcessLocation();
					String planCode = property.getPlanCode();
					if(processLocation==null || planCode==null) {
						EventBusUtil.publish(new StatusMessageEvent("Invalid Process Location or Plan Code set in Property", StatusMessageEventType.ERROR));
						return false;
					}
					GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByDivision(entryDeptComboBox.getSelectionModel().getSelectedItem().getId().getDivisionId());
					PreProductionLot lastLot=getDao(PreProductionLotDao.class).findLastLotByPlanCode(planCode);
				 	
					PreProductionLot newProductionLot = new PreProductionLot(newlotNumber);
					newProductionLot.setProcessLocation(processLocation);
					newProductionLot.setPlantCode(gpcsDivision.getGpcsPlantCode());
					newProductionLot.setLineNo(gpcsDivision.getGpcsLineNo());
					newProductionLot.setPlanCode(planCode);
					newProductionLot.setHoldStatus(PreProductionLotStatus.RELEASE.getId());
					newProductionLot.setNextProductionLot(null);
					newProductionLot.setSendStatus(PreProductionLotSendStatus.INPROGRESS);
					getDao(PreProductionLotDao.class).save(newProductionLot);
					
					if(lastLot!=null) {
						lastLot.setNextProductionLot(newlotNumber);
						getDao(PreProductionLotDao.class).update(lastLot);
					}
				 }
				else{				
				EventBusUtil.publish(new StatusMessageEvent("Invalid Production Lot", StatusMessageEventType.ERROR));
				return false;
				}
			}

		}if(getQuantityTextField().getText() == null){
			getQuantityTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(5));
			EventBusUtil.publish(new StatusMessageEvent("Please Enter Quantity", StatusMessageEventType.ERROR));
			return false;
		}if(!isNumeric(getQuantityTextField().getText())) {
			EventBusUtil.publish(new StatusMessageEvent("Enter numeric value ", StatusMessageEventType.ERROR));
			return false;
		}if(Integer.parseInt(getQuantityTextField().getText()) <= 0){
			getQuantityTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(5));
			EventBusUtil.publish(new StatusMessageEvent("Quantity should be greater than zero", StatusMessageEventType.ERROR));
			return false;
		}if(getQuantityTextField().getText().equalsIgnoreCase("0")){
			EventBusUtil.publish(new StatusMessageEvent("Quantity should be greater than zero", StatusMessageEventType.ERROR));
			return false;
		}if(!isValidMaxQty()){
			EventBusUtil.publish(new StatusMessageEvent("Quantity can not be greater than "+maxQty, StatusMessageEventType.ERROR));
			return false;
		}if (!validateSpecCode(getPartComboBox().getSelectionModel().getSelectedItem().getMainPartNo(),getMtcComboBox().getSelectionModel().getSelectedItem())) {
			EventBusUtil.publish(new StatusMessageEvent("Product Spec code not found for "+getPartComboBox().getSelectionModel().getSelectedItem().getMainPartNo(), StatusMessageEventType.ERROR));
			return false;
		}
		
		String mainNo = getPartComboBox().getSelectionModel().getSelectedItem().getMainPartNo();
		if (StringUtils.isBlank(mainNo) || mainNo.length() != 5) {
			EventBusUtil.publish(new StatusMessageEvent("Main Part No: "+ mainNo + " is invalid. It must be 5 characters. ", StatusMessageEventType.ERROR));
			return false;
		}
		
		
		String supplierNo = property.getSupplierNo();
		supplierNo = StringUtils.trimToEmpty(supplierNo);
		if (StringUtils.isBlank(supplierNo) || supplierNo.length() != 2) {
			EventBusUtil.publish(new StatusMessageEvent("Supplier No: "+ supplierNo + " is invalid. It must be 2 characters. ", StatusMessageEventType.ERROR));
			return false;
		}
		
		return true;
	}
	
	private boolean isValidMaxQty() {
		
		QiStationConfiguration qiStationConfiguration = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(ClientMainFx.getInstance().getApplicationContext().getProcessPointId(),QiEntryStationConfigurationSettings.MAX_UPC_QUANTITY.getSettingsName());
		if(qiStationConfiguration!=null){
			maxQty = Long.parseLong(qiStationConfiguration.getPropertyValue());
		}else{
			maxQty = Long.parseLong(QiEntryStationConfigurationSettings.MAX_UPC_QUANTITY.getDefaultPropertyValue());
		}
		if(Long.parseLong(getQuantityTextField().getText()) > maxQty){
			return false;
		}
		return true;
		
		
	}


	private boolean validateSpecCode(String mainPartNo, String classNo) {
		Mbpn selectedMbpn = getDescComboBox().getSelectionModel().getSelectedItem();
		mbpn = getDao(MbpnDao.class).findSpecCodeByMainNoClassNoAndHesColour(mainPartNo, classNo, selectedMbpn.getHesColor());
		if(null!=mbpn && null!=mbpn.getProductSpecCode()){
			return true;
		}
		return false;
	}



	private void getMbpnDescByMainNo() {
		mtcComboBox.valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue ov, String t, String newValue) {
				
				descComboBox.setItems(FXCollections.observableArrayList(getDao(
						MbpnDao.class).findNonBlankDescByMainNoAndClassNo(
						partComboBox.getSelectionModel().getSelectedItem()
								.getMainPartNo(),
						mtcComboBox.getSelectionModel().getSelectedItem())));
				descComboBox.setConverter(qiDescConverter());
			}

		});
		
	}

	public boolean isNumeric(String text) {  
	    return text.matches("[-+]?\\d*\\.?\\d+");  
	}

	private void getMtcModelAndDescDataByPartName() {
		partComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPartDto>() {
			public void changed(ObservableValue ov, QiInspectionPartDto t, QiInspectionPartDto newValue) {
				mtcComboBox.getItems().clear();
				mtcComboBox.getItems().addAll(getDao(MbpnDao.class).findAllClassNoByMainNo(partComboBox.getSelectionModel().getSelectedItem().getMainPartNo().trim(), 
						getProductController().getProcessPointId(), entryDeptComboBox.getSelectionModel().getSelectedItem().getId().getDivisionId()));
			}
		});
	}

	private StringConverter<Mbpn> qiDescConverter() {
		return new StringConverter<Mbpn>() {

			@Override
			public Mbpn fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Mbpn arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getDescription();
				}
			}

		};
	}



	private StringConverter<QiInspectionPartDto> qiPartNameConverter() {
		return new StringConverter<QiInspectionPartDto>() {

			@Override
			public QiInspectionPartDto fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(QiInspectionPartDto arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getInspectionPartName();
				}
			}

		};
	}



	private StringConverter<QiStationEntryDepartment> qiDivisionConverter() {
		return new StringConverter<QiStationEntryDepartment>() {

			@Override
			public QiStationEntryDepartment fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(QiStationEntryDepartment arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getId().getDivisionId();
				}
			}

		};
	}



	private LoggedLabel createLabel(String id, String label,
			 Insets insets) {
	
		LoggedLabel lbl = UiFactory.createLabel(id,label);
		lbl.getStyleClass().add("display-label");
		lbl.setPadding(insets);
		
		return lbl;
	}
	
	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel, Insets insets){
		loggedLabel=UiFactory.createLabel("label","*");
		loggedLabel.setStyle("-fx-text-fill: red;");
		loggedLabel.setPadding(insets);
		return loggedLabel;
	}
	/**
	 * @return the productIdTextField
	 */
	public TextField getProductIdTextField() {
		return getProductIdField();
	}
	/**
	 * @param productIdTextField the productIdTextField to set
	 */
	public void setProductIdTextField(TextField productIdTextField) {
		this.productIdTextField = productIdTextField;
	}

	@Override
	public TextField getProductIdField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = (UpperCaseFieldBean)UiFactory.createTextField("productIdTextField", getProductIdFieldLength(),
					UiFactory.getIdle().getInputFont(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			
			this.productIdTextField.setMaxWidth(400);
			this.productIdTextField.setFocusTraversable(true);
		}
		return this.productIdTextField;
	}



	@Override
	public TextInputControl getExpectedProductIdField() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setProductSequence() {
		// TODO Auto-generated method stub
		
	}



	/**
	 * @return the lotTextField
	 */
	public LoggedTextField getLotTextField() {
		return lotTextField;
	}



	/**
	 * @param lotTextField the lotTextField to set
	 */
	public void setLotTextField(LoggedTextField lotTextField) {
		this.lotTextField = lotTextField;
	}


	/**
	 * @return the enterPartButton
	 */
	public LoggedButton getEnterPartButton() {
		return enterPartButton;
	}



	/**
	 * @param enterPartButton the enterPartButton to set
	 */
	public void setEnterPartButton(LoggedButton enterPartButton) {
		this.enterPartButton = enterPartButton;
	}



	/**
	 * @return the partComboBox
	 */
	public ComboBox<QiInspectionPartDto> getPartComboBox() {
		return partComboBox;
	}



	/**
	 * @param partComboBox the partComboBox to set
	 */
	public void setPartComboBox(ComboBox<QiInspectionPartDto> partComboBox) {
		this.partComboBox = partComboBox;
	}



	/**
	 * @return the entryDeptComboBox
	 */
	public ComboBox<QiStationEntryDepartment> getEntryDeptComboBox() {
		return entryDeptComboBox;
	}



	/**
	 * @param entryDeptComboBox the entryDeptComboBox to set
	 */
	public void setEntryDeptComboBox(
			ComboBox<QiStationEntryDepartment> entryDeptComboBox) {
		this.entryDeptComboBox = entryDeptComboBox;
	}



	/**
	 * @return the mtcComboBox
	 */
	public ComboBox<String> getMtcComboBox() {
		return mtcComboBox;
	}



	/**
	 * @param mtcComboBox the mtcComboBox to set
	 */
	public void setMtcComboBox(ComboBox<String> mtcComboBox) {
		this.mtcComboBox = mtcComboBox;
	}



	/**
	 * @return the descComboBox
	 */
	public ComboBox<Mbpn> getDescComboBox() {
		return descComboBox;
	}



	/**
	 * @param descComboBox the descComboBox to set
	 */
	public void setDescComboBox(ComboBox<Mbpn> descComboBox) {
		this.descComboBox = descComboBox;
	}



	/**
	 * @param property the property to set
	 */
	public void setProperty(QiPropertyBean property) {
		this.property = property;
	}
	
	private GridPane createTopPanelGrid() {
		GridPane topPanelGridPane = new GridPane();
		topPanelGridPane.setHgap(25);
		topPanelGridPane.setVgap(5);
		topPanelGridPane.setAlignment(Pos.BASELINE_CENTER);
		topPanelGridPane.getColumnConstraints().addAll(setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT));
		return topPanelGridPane;
	}
	
	private ColumnConstraints setColumnConstraints(HPos hpos) {
		ColumnConstraints column = new ColumnConstraints();
		column.setHalignment(hpos);
		return column;
	}

	@Subscribe()
	public void onAssociateSelectEvent(EntryDepartmentEvent event) {
		 if(null!=event &&  event.getEventType().equals(QiConstant.ASSOCIATE_ID_SELECTED)) {
			 selectedAssociateId = (String) event.getTargetObject();
		 }
	}


	@Override
	public LoggedTextField getQuantityTextField() {
		return quantityTextField;
	}


	public void setQuantityTextField(LoggedTextField quantityTextField) {
		this.quantityTextField = quantityTextField;
	}

	private int getProductIdFieldLength() {
		List<ProductNumberDef> list = getProductController().getProductTypeData().getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		return length;
	}
	
}

