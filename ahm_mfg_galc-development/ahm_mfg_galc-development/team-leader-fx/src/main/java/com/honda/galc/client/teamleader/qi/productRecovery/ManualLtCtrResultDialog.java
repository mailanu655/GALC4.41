package com.honda.galc.client.teamleader.qi.productRecovery;

import static com.honda.galc.service.ServiceFactory.getService;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.DefaultFieldRender;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.ManualLotControlRepairConstants;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.PartValidationStrategy;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.PartCheckUtil;
import com.honda.galc.util.ProductResultUtil;
import com.honda.galc.util.SubproductUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.miginfocom.layout.CC;

/**
 * 
 * <h3>ManualLtCtrResultDialog</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLtCtrResultEnterDataPanel description </p>
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

 *
 */
public class ManualLtCtrResultDialog extends FxDialog implements EventHandler<ActionEvent>{
	

	private static final int MAX_NUMBER_OF_TORQUES = 30;
	private static final int MAX_NUMBER_OF_STRING_VALUE = 6;
	private static final String HL_STATUS_COMPELTED = "COMPLETED";
	private static final String REPAIRED = "Repaired";
	private static final String REBROADCAST_AT_REPAIR = "REBROADCAST_AT_REPAIR";
	private static final String PRODUCT_ID_LBL = "Product ID";
	private int torqueIndex = -1;
	private int stringValueIndex = -1;
	
	private LoggedTextArea partNameTextArea;
	private LoggedLabel labelTorque;
	private LoggedLabel labelHLCompleted;
	private List<UpperCaseFieldBean> torqueTextFieldList;
	private List<UpperCaseFieldBean> stringValueFieldList;
	private List<LoggedTextArea> measurementNameList;
	private UpperCaseFieldBean partSnField;
	private UpperCaseFieldBean hlCompletedField;
	private LoggedButton buttonExit;
	private LoggedButton buttonSave;
	private LoggedButton buttonReset;
	private LoggedButton buttonHlCompleted;
	private LoggedButton buttonTorqueValue;
	
	private BaseProduct myProduct;
	private PartResult partResult;
	private String authorizedUser;
	private PartSpec partSpec;
	private Text text =  new Text(StringUtils.EMPTY);
	private boolean partSnNg;
	private boolean resetScreen;
	private List<? extends ProductBuildResult> installedPartList;
	private ManualLotControlRepairPropertyBean property;
	private SubproductUtil subproductUtil;	
	private BaseProduct product;
	private ManualLotControlRepairPanel parent;
	private List<PartResult> resultList ;
	
	private IManualLtCtrResultEnterViewManager manager;
    private boolean torqueValueButtonEnabled=true;
	
	String message;
	Label errorMsgLbl;
	
	
	
	public ManualLtCtrResultDialog(String title,BaseProduct product,List<PartResult> partResults,ManualLotControlRepairPanel parent, ManualLotControlRepairPropertyBean property,ProductTypeData productTypeData, IManualLtCtrResultEnterViewManager viewManager) {
        super("Enter Result",parent.getApplicationId());
        this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
        this.manager = viewManager;
        this.partResult = partResults.get(0);
        this.setMyProduct(product);
		this.resultList = partResults.size() > 1 ? partResults : null;
		this.parent=parent;
		this.property=property;
		this.product = product;
		initComponents();
		initListeners();
    }

	protected void init(){
		getButtonSave().setDisable(true);
		getButtonReset().setDisable(true);
		setEnabledHeadLess(false, false);
		setVisibleHeadLess(false);
		setEnabledInstalledPart(false);
		setVisibleInstalledPart(false);
		disableStringValueFields();
		disableMeasurementNameFields();
		setEnabledTorqueFields(false, MAX_NUMBER_OF_TORQUES);
		setVisibleTorqueFields(false, MAX_NUMBER_OF_TORQUES);
		enableDisableTorqueValueButton();
	}

	private void initComponents() {
		MigPane pane = new MigPane("insets 20", "grow", "");
		MigPane dataGrouping1 = new MigPane(StringUtils.EMPTY,"","align top");
		MigPane dataGrouping = new MigPane("wrap 6","","");
		pane.setPrefHeight(768.0);
		pane.setPrefWidth(1024.0);  
		
		torqueTextFieldList = new ArrayList<UpperCaseFieldBean>();
		stringValueFieldList = new ArrayList<UpperCaseFieldBean>();
		measurementNameList = new ArrayList<LoggedTextArea>();
		partSnField = UiFactory.createUpperCaseFieldBean("partSnField", 55, null, TextFieldState.EDIT, Pos.BASELINE_LEFT, new DefaultFieldRender());
		partSnField.getStyleClass().add("mlcr-enter-result-txt");
		hlCompletedField = UiFactory.createUpperCaseFieldBean("hlCompletedField", 20, null, TextFieldState.EDIT, Pos.BASELINE_LEFT, new DefaultFieldRender());
		hlCompletedField.getStyleClass().add("mlcr-enter-result-txt");
		hlCompletedField.setEditable(false);
		buttonSave = createButton(QiConstant.SAVE,this);
		buttonReset = createButton(QiConstant.RESET,this);
		buttonHlCompleted = createButton(ManualLotControlRepairConstants.COMPLETED,this);
		buttonHlCompleted.setPrefWidth(200.0);
		buttonHlCompleted.getStyleClass().add("mlcr-completed");
		buttonTorqueValue = createButton(ManualLotControlRepairConstants.VALUE,this);
		buttonExit = createButton(ManualLotControlRepairConstants.EXIT,this);
		labelTorque = UiFactory.createLabel("labelTorque", "Torque :");
		labelTorque.getStyleClass().add("mlcr-large-label");
		
		labelHLCompleted = UiFactory.createLabel("labelCompleted", "Completed :");
		
		errorMsgLbl=new Label();
		errorMsgLbl.getStyleClass().add( "display-message");
		errorMsgLbl.setStyle("-fx-text-fill: red");
		
		for(int i = 0; i < MAX_NUMBER_OF_TORQUES; i++){
			UpperCaseFieldBean uBean = UiFactory.createUpperCaseFieldBean("partSnField"+i, 50, null, TextFieldState.EDIT, Pos.BASELINE_LEFT,new DefaultFieldRender());
			uBean.getStyleClass().add("mlcr-enter-result-txt");
			uBean.setMinWidth(125);
			torqueTextFieldList.add(uBean);
			dataGrouping.add(torqueTextFieldList.get(i));
		}

		pane.add(createProductDetails(myProduct), "align center,span,wrap");
		dataGrouping1.add(labelTorque,new CC().minWidth("150").minHeight("200").alignX("left").gapTop("10"));
		dataGrouping1.add(dataGrouping,new CC().minWidth("770").minHeight("200").alignX("center"));
		dataGrouping1.add(buttonTorqueValue,new CC().minWidth("100").minHeight("200").alignX("right").gapTop("5"));
		pane.add(partSnField,"gap left 10,gap right 10,gap top 20,span,wrap");
		HBox completedRow = new HBox();
		completedRow.getChildren().addAll(buttonHlCompleted, hlCompletedField);
		completedRow.setSpacing(10.0);
		pane.add(completedRow, "align left,span,wrap");
		pane.add(dataGrouping1,"span,wrap");
		pane.add(createPanel2(),"align left,span,wrap");
		pane.add(errorMsgLbl, "span,wrap");

		MigPane stringPane = new MigPane("","center", "center");
		for(int i = 0; i < MAX_NUMBER_OF_STRING_VALUE; i++){
			UpperCaseFieldBean uBean = getStringvalueField(i);
			uBean.getStyleClass().add("mlcr-enter-result-txt");
			stringValueFieldList.add(uBean);
			measurementNameList.add(getMeasurementName(i));
			stringPane.add(stringValueFieldList.get(i), "gapright 10");
			stringPane.add(measurementNameList.get(i), "gapright 10");
		}
		pane.add(stringPane, "span,wrap");
		getRootBorderPane().setCenter(pane);
	}

	private Node createProductDetails(BaseProduct product)  {
		List<ProductNumberDef> productNumberDefList = ProductNumberDef.getProductNumberDef(getMyProduct().getProductType());
		String lblProductId = productNumberDefList.isEmpty() ? PRODUCT_ID_LBL : productNumberDefList.get(0).getName();
		String font_style_label = "-fx-font: 30 arial;";
		MigPane headerPane = new MigPane("insets 10", "[25%][75%,grow]", "[]10[]10[]");
		headerPane.setPrefHeight(0.9 * 768.0);
		headerPane.setPrefWidth(0.9 * 1024.0);  
		//Defining the Name text field
		final TextField labelProductId = UiFactory.createTextField("productIdLbl",20,TextFieldState.DISABLED);
		labelProductId.setText(lblProductId);
		labelProductId.setStyle(font_style_label);
		final TextField productIdText = new TextField();
		productIdText.setId("productId");
		productIdText.setDisable(true);
		productIdText.setText(product.getProductId());
		productIdText.getStyleClass().add("mlcr-large-green-bg");
		final TextField labelProductSpec = UiFactory.createTextField("productTypeLbl",20,TextFieldState.DISABLED);
		labelProductSpec.setText("MTOC");
		labelProductSpec.setStyle(font_style_label);
		final TextField productTypeText = new TextField();
		productTypeText.getStyleClass().add("mlcr-large-text");
		productTypeText.setDisable(true);
		productTypeText.setText(product.getProductSpecCode());
		final TextField labelPartName = UiFactory.createTextField("partNameLbl",20,TextFieldState.DISABLED);
		labelPartName.setText("Part");
		labelPartName.setStyle(font_style_label);
		final LoggedTextArea partNameText = UiFactory.createTextArea(partResult.getPartName());
		partNameText.getStyleClass().add("mlcr-large-label");
		partNameText.setPrefRowCount(2);
		partNameText.setEditable(false);
		partNameTextArea = partNameText;
		headerPane.add(labelProductId, "align left");
		headerPane.add(productIdText, "align left, grow, span, wrap");
		headerPane.add(labelProductSpec, "align left");
		headerPane.add(productTypeText, "align left, grow, span, wrap");
		headerPane.add(labelPartName, "align left");
		headerPane.add(partNameText, "align left, grow,wrap, span, wrap");
		return headerPane;
	}
	
	private LoggedButton createButton(String text,EventHandler<ActionEvent> handler){
		LoggedButton btn = createBtn(text, handler);
		btn.setPrefSize(150, 50);
		return btn;
	}

	private UpperCaseFieldBean getStringvalueField(int idx) {
		return UiFactory.createUpperCaseFieldBean("partSnField", 7, null, TextFieldState.EDIT, Pos.BASELINE_LEFT, new DefaultFieldRender());
	}

	private LoggedTextArea getMeasurementName(int i) {
		return UiFactory.createTextArea("Part Description Text");
	}

	private MigPane createPanel2() {
		MigPane dataGrouping = new MigPane(StringUtils.EMPTY,
				"[center]",//column
				"");
		dataGrouping.add(buttonSave);
		dataGrouping.add(buttonReset);
		dataGrouping.add(buttonExit);
		return dataGrouping;
	}

	public void setEnabledHeadLess(boolean enabled, boolean isStatusOnly) {
		getPartNameTextArea().setDisable(!enabled);
		getButtonHlCompleted().setDisable(!enabled);
		getHlCompletedField().setDisable(!enabled && isStatusOnly);
	}

	public void setVisibleHeadLess(boolean enabled) {
		getPartNameTextArea().setVisible(true);
		getButtonHlCompleted().setVisible(enabled);
		getHlCompletedField().setVisible(enabled);
	}


	public void setEnabledInstalledPart(boolean enabled){
		getPartNameTextArea().setDisable(!enabled);
		getPartSnField().setDisable(!enabled);
	}

	private void disableStringValueFields() {
		for(LengthFieldBean fd : stringValueFieldList)
			fd.setVisible(false);
	}

	public void setVisibleInstalledPart(boolean enabled){
		getPartNameTextArea().setVisible(enabled);
		getPartSnField().setVisible(enabled);
	}

	private void disableMeasurementNameFields() {
		for(LoggedTextArea ta : measurementNameList)
			ta.setVisible(false);
	}

	public void setEnabledTorqueFields(boolean enabled, int count){
		labelTorque.setDisable(!enabled);
		buttonTorqueValue.setDisable(!enabled);
		for(int i = 0; i < count; i++){
			torqueTextFieldList.get(i).setDisable(!enabled);
		}
	}

	public void setVisibleTorqueFields(boolean enabled, int count){
		labelTorque.setVisible(enabled);
		buttonTorqueValue.setVisible(enabled);
		for(int i = 0; i < count; i++){
			torqueTextFieldList.get(i).setVisible(enabled);
		}
	}

	

	public void enableOperationButtons(boolean enabled){
		getButtonSave().setDisable(!enabled);;	
		getButtonReset().setDisable(!enabled);
		getButtonSave().requestFocus();
	}

	public void enableHeadLess(boolean statusOnly) {
		setEnabledHeadLess(true, statusOnly);
		setVisibleHeadLess(true);
		hlCompletedField.requestFocus();

	}

	public void enableInstalledPart() {
		setEnabledInstalledPart(true);
		setVisibleInstalledPart(true);
		getPartSnField().requestFocus();

	}

	public void enableTorqueFields(int count) {
		setEnablePartNameLabel(true);  //always show the part name
		setEnabledTorqueFields(true, count);
		setVisibleTorqueFields(true, count);
		getTorqueTextFieldList().get(0).requestFocus();
		enableDisableTorqueValueButton();
	}

	public void enableDisableTorqueValueButton() {
		if (!isTorqueValueButtonEnabled()) {
			getButtonTorqueValue().setDisable(true);
			getButtonTorqueValue().setVisible(false);
		}
	}
	
	public boolean isTorqueValueButtonEnabled() {
		return torqueValueButtonEnabled;
	}

	public void setTorqueValueButtonEnabled(boolean torqueValueButtonEnabled) {
		this.torqueValueButtonEnabled = torqueValueButtonEnabled;
	}

	private void setEnablePartNameLabel(boolean enabled) {
		getPartNameTextArea().setVisible(enabled);
		getPartNameTextArea().setDisable(!enabled);
		
	}
	public void enableStringValueFields(List<MeasurementSpec> list) {
		labelTorque.setDisable(false);
		for(int i = 0; i < list.size(); i++){
			stringValueFieldList.get(i).setDisable(true);
			stringValueFieldList.get(i).setVisible(true);
			measurementNameList.get(i).setVisible(true);
			measurementNameList.get(i).setText(list.get(i).getMeasurementName());
		}

		stringValueFieldList.get(0).requestFocus();
	}

	public UpperCaseFieldBean getCurrentTorque(int index) {

		if(index < torqueTextFieldList.size())
			return torqueTextFieldList.get(index);

		return null;
	}

	public LengthFieldBean getCurrentStringValueField(int index) {
		return getCurrentStringValueField(index);
	}

	public boolean isTorqueField(Object source) {
		for(Object field : torqueTextFieldList)
			if(field == source) return true;

		return false;
	}

	public boolean isStringValueField(Object source) {
		for(Object field : stringValueFieldList)
			if(field == source) return true;

		return false;
	}

	public LoggedTextArea getPartNameTextArea() {
		return partNameTextArea;
	}

	public void setPartNameTextArea(LoggedTextArea partNameTextArea) {
		this.partNameTextArea = partNameTextArea;
	}

	public LoggedLabel getLabelTorque() {
		return labelTorque;
	}


	/** This method will set font size of the components/labels 
	 * in the node according to screen size.
	 * 
	 * @param node
	 */
	public static void setFontSize(Node node){
		node.setStyle(String.format("-fx-font-size: %dpx; ", (int)(0.01 * getScreenWidth())));
	}

	/** This method will return the screen width.
	 * 
	 * @return screen width
	 */
	public static double getScreenWidth() {
		return  Screen.getPrimary().getVisualBounds().getWidth();
	}


	/** This method will return the screen width.
	 * 
	 * @return screen width
	 */
	public static double getScreenHeight() {
		return  Screen.getPrimary().getVisualBounds().getHeight();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if(ManualLotControlRepairConstants.EXIT.equals(button.getText())) manager.subScreenClose(actionEvent);
			else if(QiConstant.SAVE.equals(button.getText())) manager.saveUpdate();
			else if(QiConstant.RESET.equals(button.getText())) manager.resetScreen();
			else if(ManualLotControlRepairConstants.COMPLETED.equals(button.getText())) manager.setHlStatus();
			else if(ManualLotControlRepairConstants.VALUE.equals(button.getText())) manager.addDefaultTorqueValues();
		}
	}
	
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.getStyleClass().add("popup-btn");
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		return btn;
	}
	
	
	private void validationForTextfield(){
		if(getTorqueTextFieldList() != null)
			for(int i=0;i<10;i++){
				getTorqueTextFieldList().get(i).addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(6));
			}
	}

	protected void confirmMissionTye(){};

	public void windowActivated(java.awt.event.WindowEvent e) {
		locateFocus();       
	}
	

	protected void validatePartSn() throws TaskException, Exception{
		validatePartSnLotControl();
		validatePartSnStrategy();
		if(getCurrentLotControlRule().isVerify()) validateSubProduct();
	}

	private void validateSubProduct() throws Exception {
		PartSerialNumber partnumber = new PartSerialNumber(getPartSnField().getText());
		LotControlRule rule = getCurrentLotControlRule();
		subproductUtil = new SubproductUtil(partnumber, getCurrentLotControlRule(),partSpec);

		if (subproductUtil.isPartSubproduct()) {
			BaseProduct subProduct = subproductUtil.findSubproduct();
			if (subproductUtil.findSubproduct() == null) {
				displayErrorMessage("Subproduct SN can not be found.", "Subproduct SN can not be found.");
				return;
			}

			if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct,parent.getController().getProductSpec().getProductSpecCode())) {
				displayErrorMessage("Spec Code of part does not match expected Spec Code.", "Spec Code of part does not match expected Spec Code.");
				return;
			}

			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			try {
				String installProcessPoint;
				if(!subProductProperty.isUseMainNoFromPartSpec())
					installProcessPoint =subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());
				else{
					installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo());
				}
				List<String> failedProductCheckList = subproductUtil.performSubProductChecks(rule.getPartName().getSubProductType(), subProduct, installProcessPoint);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != failedProductCheckList.size() - 1) {
							msg.append(", ");
						}
					}
					Logger.getLogger().info(msg.toString());
					throw new Exception (msg.toString());
				}
			} catch (Exception e) {
				throw new TaskException(e.getMessage());
			}
		}
	}

	private void validatePartSnStrategy() throws TaskException, Exception{
		String strategy = null;
		try{
			strategy = getCurrentLotControlRule().getStrategy();
		}
		catch(Exception e){
			Logger.getLogger().info(e, "strategy is not defined");
		}
		if(!StringUtils.isEmpty(strategy)){
			Method m = null;
			try{
				m = PartCheckUtil.class.getMethod(getValidationMethodName(strategy), new Class[] { LotControlRule.class, String.class, ProductBuildResult.class });
			}
			catch(Exception e){
				Logger.getLogger().info(e, "validate method is not defined");
			}
			if(m != null){
				String productId = parent.getProductId();
				InstalledPart installedPart = createInstalledPart();
				m.invoke(null, getCurrentLotControlRule(), productId, installedPart);
			}
		}
	}

	private String getValidationMethodName(String strategyName) { 
		PartValidationStrategy validationStrategy = Enum.valueOf(PartValidationStrategy.class, strategyName); 
		return validationStrategy.getMethodName(); 
	} 


	public void confirmMeasurement() {

		try {
			double doubleToqueVlue = getDoubleTorqueValue(torqueIndex);
			checkTorqueValue(doubleToqueVlue);
			completeTorqueValueCheck();

		} catch (BaseException te) {
			getTorqueTextFieldList().get(torqueIndex).setStatus(false);
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm torque value.");
			getTorqueTextFieldList().get(torqueIndex).setStatus(false);
		}
	}

	private void completeTorqueValueCheck() {
		getTorqueTextFieldList().get(torqueIndex).setStatus(true);

		torqueIndex++;
		if(torqueIndex < getCurrentPartMeasurementCount()){
			getTorqueTextFieldList().get(torqueIndex).setText(new Text(StringUtils.EMPTY));
		}
		else {
			torqueIndex = 0;
			if(getPartSpec().getStringMeasurementSpecs().size() ==0){
				enableOperationButtons(true);
			}
			else
				prepareStringValueColletion();
		}
	}

	private void checkTorqueValue(double doubleToqueVlue) {
		MeasurementSpec measurementSpec = getCurrentMeasurementSpec(torqueIndex);
		if(measurementSpec == null) return;
		if(measurementSpec.getMaximumLimit() == 0 && measurementSpec.getMinimumLimit() == 0) return; 
		if(doubleToqueVlue > measurementSpec.getMaximumLimit() || doubleToqueVlue < measurementSpec.getMinimumLimit()){
			displayErrorMessage("Mandatory field is empty", "Invalid torque value " + doubleToqueVlue + " Max:" +measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit());
			return;
		}

	}

	private MeasurementSpec getCurrentMeasurementSpec(int index) {
		if(getPartSpec().getNumberMeasurementSpecs() == null || getPartSpec().getNumberMeasurementSpecs().size() == 0) return null;
		return getPartSpec().getNumberMeasurementSpecs().get(index);
	}

	private PartSpec getPartSpec() {
		if(partSpec == null && 
				getCurrentLotControlRule().getParts() != null && 
				getCurrentLotControlRule().getParts().size() > 0
				)
			partSpec = getCurrentLotControlRule().getParts().get(0);

		return partSpec;
	}

	private double getDoubleTorqueValue(int index) {
		return new Double(getTorqueTextFieldList().get(index).getText().trim()).doubleValue();
	}

	public void saveUpdate() {
		try {
			clearDisplayMessage();
			LoggedButton saveBtn = getButtonSave();
			if(isUserauthenticationNeededToSave() && !login()) return;

			if(property.isSaveConfirmation() && !confirmUpdate()) return;

			installedPartList = getCollectedBuildResult();

			doSaveUpdate();
			if(resultList == null) {
				doSaveUpdateSubproduct();
				doBroadcast();
				parent.getController().loadProductBuildResultStatus(true);
			}else {
				doBroadcast();
				parent.getController().loadProductBuildResultStatus(true);
			}

			Stage stage = (Stage) saveBtn.getScene().getWindow();
			stage.close();

		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to save data into database:" + e.toString());
		}



	}

	private void doBroadcast() {
		String processPointId = getCurrentLotControlRule().getId().getProcessPointId();
		String broadcastDestinations = PropertyService.getProperty(processPointId, REBROADCAST_AT_REPAIR, "");

		if (broadcastDestinations != null && broadcastDestinations != "") {
			try {
				Logger.getLogger().info("Beginning broadcast from repair");

				List<String> broadcastDestinationList = java.util.Arrays.asList(broadcastDestinations.split(","));
				DataContainer dataContainer = new DefaultDataContainer();

				dataContainer.put(DataContainerTag.PRODUCT_ID, product.getProductId());
				dataContainer.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
				dataContainer.put(DataContainerTag.PRODUCT, product);
				dataContainer.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());

				for(BroadcastDestination destination: getService(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId, true)) {
					if (broadcastDestinationList.contains(destination.getDestinationId())) {
						Logger.getLogger().info(String.format("Broadcasting %s at %s", destination.getDestinationId(), processPointId));

						getService(BroadcastService.class).broadcast(destination, processPointId, dataContainer);
					}
				}

			} catch (Exception e) {
				Logger.getLogger().error(e, "Failed to broadcast from repair: " + e.toString());			
			} finally {
				Logger.getLogger().info("Completed broadcast from repair");
			}
		}
	}


	private void doSaveUpdateSubproduct() throws Exception {

		String subProductType = getCurrentLotControlRule().getPartName().getSubProductType();

		if (null != subProductType && !subProductType.equalsIgnoreCase("")){
			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			String installProcessPoint="";
			if(!subProductProperty.isUseMainNoFromPartSpec())
				installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getCurrentLotControlRule().getPartName().getSubProductType());
			else{
				installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo());
			}			
			subproductUtil.performSubproductTracking(subProductType, subproductUtil.findSubproduct(), installProcessPoint,"");
		}
	}

	@SuppressWarnings("unchecked")
	protected void doSaveUpdate() {
		//Generic method - Default for Engine, Frame and Knuckles
		ProductResultUtil.saveAll(parent.getApplicationId(), (List<InstalledPart>)installedPartList);

		Logger.getLogger().info("Saved data into database by user:" + getUser() + 
				ManualLotControlRepairConstants.NEW_LINE + installedPartList.get(0));
	}

	protected PartNameDao getPartNameDao() {
		return ServiceFactory.getDao(PartNameDao.class);
	}

	protected InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}

	protected String getUser() {
		if(!isUserauthenticationNeededToSave()) return getUserId();
		else
			return authorizedUser == null ? getUserId() : authorizedUser;
	}

	protected boolean isUserauthenticationNeededToSave() {
		try {

			String clientStr = property.getClientsNeedAuthenticateUserToSave();
			if (StringUtils.isEmpty(clientStr)) 
				return false;

			String[] clients = clientStr.split(",");
			for (int i = 0; i < clients.length; i++) {
				if (parent.getMainWindow().getApplicationContext().getTerminal().getHostName().equals(clients[i].trim()))
					return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}


	public boolean login(){
		authorizedUser = null;
		if(LoginDialog.login() != com.honda.galc.enumtype.LoginStatus.OK) return false;

		if(!ClientMainFx.getInstance().getAccessControlManager().isAuthorized(getAuthorizationGroup() )) {
			MessageDialog.showError("Terminating application! \nYou have no access permission of default application of this terminal");
			return false;
		}

		authorizedUser = ClientMainFx.getInstance().getAccessControlManager().getUserName();
		Logger.getLogger().info("User:" + authorizedUser + " logged in.");
		return true;

	}

	private String getAuthorizationGroup() {
		return property.getAuthorizationGroup();
	}

	protected List<? extends ProductBuildResult> getCollectedBuildResult() {
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		if(resultList == null) {
			InstalledPart installedPart = createInstalledPart();

			if(!partResult.isHeadLess() || !partResult.isQuickFix())
				getInputPartAndTorqueData(installedPart);

			partResult.setBuildResult(installedPart);
			list.add(installedPart);
		} else {
			for(PartResult result : resultList) {
				InstalledPart installedPart = createInstalledPart(result);
				result.setBuildResult(installedPart);
				list.add(installedPart);
			}
		}
		return list;
	}

	protected InstalledPart createInstalledPart() {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(parent.getProductId());
		id.setPartName(getCurrentLotControlRule().getPartName().getPartName());
		installedPart.setId(id);
		installedPart.setPartId(getPartSpec() == null ? "" : getPartSpec().getId().getPartId());
		installedPart.setInstalledPartReason(REPAIRED);
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(getUser());
		installedPart.setPartSerialNumber(getPartSnField().getText());
		installedPart.setProductType(property.getProductType());
		return installedPart;
	}


	protected InstalledPart createInstalledPart(PartResult result) {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(parent.getProductId());
		id.setPartName(result.getPartName());
		installedPart.setId(id);
		installedPart.setPartId(getPartSpec(result) == null ? "" : getPartSpec(result).getId().getPartId());
		installedPart.setInstalledPartReason(REPAIRED);
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(getUser());
		installedPart.setPartSerialNumber(getPartSnField().getText());
		installedPart.setProductType(property.getProductType());
		return installedPart;
	}

	private PartSpec getPartSpec(PartResult result) {
		return (result.getLotControlRule().getParts() != null && result.getLotControlRule().getParts().size() > 0) ?
				getCurrentLotControlRule().getParts().get(0) : null;
	}

	protected void getInputPartAndTorqueData(InstalledPart installedPart) {
		if (getPartSnField().isVisible())
			installedPart.setPartSerialNumber(getPartSnField().getText());

		if (getCurrentPartMeasurementCount() > 0)
			collectedMeasurementData(installedPart);

		if(getStrValueMeasurementSpecs() != null && getStrValueMeasurementSpecs().size() > 0)
			collectStringValueMeasurementData(installedPart);
	}


	protected LotControlRule getCurrentLotControlRule() {
		return partResult.getLotControlRule();
	}


	private void collectedMeasurementData(InstalledPart installedPart) {

		for(int i = 0; i < getCurrentPartMeasurementCount(); i++){
			Measurement measurement = new Measurement();
			MeasurementId id = new MeasurementId();
			id.setProductId(installedPart.getId().getProductId());
			id.setPartName(installedPart.getId().getPartName());

			id.setMeasurementSequenceNumber((getNumberMeasurementSpecs() != null && getNumberMeasurementSpecs().size() >=i) ? 
					getNumberMeasurementSpecs().get(i).getId().getMeasurementSeqNum() : i+1);
			measurement.setId(id);

			measurement.setMeasurementValue(getDoubleTorqueValue(i));
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			installedPart.getMeasurements().add(measurement);
		}

	}

	private void collectStringValueMeasurementData(InstalledPart installedPart) {

		if(getStrValueMeasurementSpecs() == null || getStrValueMeasurementSpecs().size() == 0) return;

		for(int i = 0; i < getStrValueMeasurementSpecs().size(); i++){
			Measurement measurement = new Measurement();
			MeasurementId id = new MeasurementId();
			id.setProductId(installedPart.getId().getProductId());
			id.setPartName(installedPart.getId().getPartName());
			id.setMeasurementSequenceNumber(getStrValueMeasurementSpecs().get(i).getId().getMeasurementSeqNum());
			measurement.setId(id);

			measurement.setMeasurementStringValue(getCurrentStringValueField(i).getText());
			measurement.setMeasurementName(getStrValueMeasurementSpecs().get(i).getMeasurementName());
			measurement.setMeasurementStatus(MeasurementStatus.OK);

			installedPart.getMeasurements().add(measurement.getId().getMeasurementSequenceNumber()-1,measurement);
		}



	}

	protected boolean confirmUpdate() {
		if(property.getDefaultYesButtonToSave()){
			return MessageDialog.confirm(this, "Do You Really Want To Update Database ?", true);
		}
		else{
			return MessageDialog.confirm(this, "Do You Really Want To Update Database ?");
		}
	}

	public void showPartCommentConfirmDialog() {}

	public void confirmPartSn() {
		try {
			clearDisplayMessage();
			validatePartSn();
			getPartSnField().setStatus(true);
			partSnNg = false;

			if(getCurrentPartMeasurementCount() > 0)
				prepareForTorqueCollection();
			else{
				enableOperationButtons(true);
			}

		} catch (BaseException te) {
			Logger.getLogger().info(te.getMessage(), "Failed to confirm part serial number.");
			getPartSnField().setStatus(false);
			displayErrorMessage("Failed to confirm part serial number.", te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm part serial number.");
			getPartSnField().setStatus(false);
			displayErrorMessage("Error to confirm part serial number.", e.getMessage());
		}
	}

	public void confirmStringValue() {
		try {
			validateStringValue();
			getStringValueFieldList().get(stringValueIndex).setStatus(true);

			stringValueIndex++;
			if(stringValueIndex < getStringValueCount()){
				getStringValueFieldList().get(stringValueIndex).setText(new Text(StringUtils.EMPTY));
			}
			else {
				stringValueIndex = 0;
				enableOperationButtons(true);
			}

		} catch (BaseException te) {
			Logger.getLogger().info(te.getMessage(), "Failed to confirm string value.");
			getStringValueFieldList().get(stringValueIndex).setStatus(false);
			displayErrorMessage("Failed to confirm string value.", te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Failed to confirm string value.");
			getStringValueFieldList().get(stringValueIndex).setStatus(false);
			displayErrorMessage("Failed to confirm string value.", e.getMessage());
		}
	}	


	private int getStringValueCount() {
		return getPartSpec().getStringMeasurementSpecs().size();
	}

	private void validateStringValue() {
		clearDisplayMessage();
		if(getStringValueFieldList().get(stringValueIndex).getText() == null)
			displayErrorMessage("Invalid input string.","Invalid input string.");
	}

	private void prepareForTorqueCollection() {
		torqueIndex = 0;
		enableTorqueFields(getCurrentPartMeasurementCount());

	}

	private void prepareStringValueColletion() {
		stringValueIndex = 0;
		enableStringValueFields(getStrValueMeasurementSpecs());

	}

	protected List<MeasurementSpec> getStrValueMeasurementSpecs() {
		return partResult.getStrValueMeasurements();
	}

	protected List<MeasurementSpec> getNumberMeasurementSpecs() {
		return partResult.getNumberMeasurements();
	}

	private int getCurrentPartMeasurementCount() {
		return partResult.getMeasurementCount();
	}

	protected void validatePartSnLotControl() {
		String partSn =getPartSnField().getText();
		if(getCurrentLotControlRule().isVerify()){
			partSpec = checkPartSnMask(partSn);
			if(partSpec == null) {
				displayErrorMessage("Verification error " + getPartSnMaskList(), "Verification error " + getPartSnMaskList());
			} else {
				String parsePartSn = CommonPartUtility.parsePartSerialNumber(partSpec,partSn);
				if (!partSn.equals(parsePartSn)) {
					getPartSnField().setText(parsePartSn);
					partSn = parsePartSn;
				}
			}
		} else {
			if(getPartSepcs().size() > 0)
				partSpec = getPartSepcs().get(0);

		}

		if(getCurrentLotControlRule().isUnique())
			checkDuplicatePart(partSn);

	}

	private List<String> getPartSnMaskList() {
		List<String> masks = new ArrayList<String>();
		for(PartSpec spec : getPartSepcs())
			masks.add(CommonPartUtility.parsePartMask(spec.getPartSerialNumberMask()));

		return masks;
	}

	private List<PartSpec> getPartSepcs() {
		if(getCurrentLotControlRule() == null || getCurrentLotControlRule().getPartName() == null)
		{
			displayErrorMessage("Exception: missing lot control rule or part name", "Exception: missing lot control rule or part name");
		} else if(getCurrentLotControlRule().getParts() == null){
			displayErrorMessage("Exception: missing part spec.","Exception: missing part spec.");
		}

		return getCurrentLotControlRule().getParts();
	}

	private PartSpec checkPartSnMask(String partSn) {
		int validDays = PropertyService.getPropertyBean(SystemPropertyBean.class,getCurrentLotControlRule().getId().getProcessPointId()).getExpirationDays();
		return CommonPartUtility.verify(partSn, getPartSepcs(),PropertyService.getPartMaskWildcardFormat(),getCurrentLotControlRule().isDateScan(),validDays, product);	
	}

	public void loadPartResult() {
		loadInstalledPartResult();
	}

	public void loadInstalledPartResult() {
		if(partResult.isHeadLess() && (partResult.isQuickFix() || partResult.isStatusOnly())){
			loadHeadLessResult();

		} else {
			loadHeadedResult();
		}
	}

	protected void checkDuplicatePart(String partSn) {
		List<InstalledPart> installedParts = ServiceFactory.getDao(InstalledPartDao.class).
				findAllByPartNameAndSerialNumber(getCurrentLotControlRule().getPartName().getPartName(), partSn);

		for(InstalledPart part : installedParts){
			if(!part.getId().getProductId().equals(parent.getProductId()))
				displayErrorMessage("Duplicate part # with product:" + part.getId().getProductId(), "Duplicate part # with product:" + part.getId().getProductId());
		}

	}

	protected void loadHeadedResult() {

		getPartNameTextArea().setText(getPartNameLabel());// +"   "+partmask);

		if(getCurrentLotControlRule().getSerialNumberScanType() != PartSerialNumberScanType.NONE){
			setVisibleInstalledPart(true);
			if(partResult.getInstalledPart() != null){
				String partmask =  getInstaledPartSpec() != null? CommonPartUtility.parsePartMask(getInstaledPartSpec().getPartSerialNumberMask()):" ";
				getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				text = new Text(partResult.getInstalledPart().getPartSerialNumber());
				getPartSnField().setText(text);

				if(getCurrentLotControlRule().isVerify()){
					if(!checkInstalledPartSerialNumber()){
						text = new Text(false);
						partSnNg = true;

						if(resetScreen) getPartSnField().setStatus(false);
						return;
					} 
				} 

				getPartSnField().setStatus(true);
				getPartSnField().setDisable(true);


			} else {
				LotControlRule lotControlRule =getCurrentLotControlRule();
				String partmask = CommonPartUtility.parsePartMask(lotControlRule.getPartMasks());
				getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				enableInstalledPart();
				partSnNg = true;
				return;
			}

		} 

		torqueIndex = 0;
		if(getCurrentPartMeasurementCount() > 0){
			if(partResult.getInstalledPart() != null){
				enableTorqueFields(getCurrentPartMeasurementCount());
				loadTorqueFields();

			} else {
				enableTorqueFields(getCurrentPartMeasurementCount());
			}
		}

		if(getStringValueCount() > 0){
			stringValueIndex = 0;
			enableStringValueFields(partResult.getStrValueMeasurements());
			if(partResult.getInstalledPart() != null)
				loadStringValueFields();
		}
	}

	private void loadStringValueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			getStringValueFieldList().get(i).setText(measurement.getMeasurementStringValue());

			if(getMeasurementNameList().get(i).getText().equals(measurement.getMeasurementName()))
				Logger.getLogger().error("Load string value:" + i + " MeasurementName:", 
						getMeasurementNameList().get(i).getText(), " MeasurementName from database:"
						,measurement.getMeasurementName() + measurement.getId().getMeasurementSequenceNumber(), " not equal.");

			if(!measurement.isStatus()){
				text = new Text(measurement.getMeasurementStringValue(), measurement.isStatus());
				stringValueIndex = i;
				return;
			}
		}

	}

	protected boolean checkInstalledPartSerialNumber() {
		PartSpec partSpec = getInstaledPartSpec();

		if(partSpec == null) return false;			

		if(CommonPartUtility.verification(partResult.getInstalledPart().getPartSerialNumber(), 
				partSpec.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(), product)){
			getPartSnField().setStatus(true);

			partSnNg = false;
			return true;
		}else {
			getPartSnField().setStatus(false);
			text = new Text(false);
			partSnNg = true;
			return false;
		}


	}

	private void loadTorqueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(!StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			String measurementValue = format.format(measurement.getMeasurementValue());
			getTorqueTextFieldList().get(i).setText(measurementValue);
			getTorqueTextFieldList().get(i).setStatus(measurement.isStatus());

			if(!measurement.isStatus()){
				text = new Text(measurementValue, measurement.isStatus());
				torqueIndex = i;
				return;
			}
		}
	}

	private PartSpec getInstaledPartSpec() {
		InstalledPart installedPart = partResult.getInstalledPart();
		for(PartSpec spec : getPartSepcs()){
			if(spec.getId().getPartName().equals(installedPart.getId().getPartName()) &&
					spec.getId().getPartId().equals(installedPart.getPartId()))
				return spec;
		}

		Logger.getLogger().warn("Failed to find PartSpec for installed part part:" + 
				installedPart.getId().getPartName() + " partId:" + installedPart.getPartId());

		return null;
	}

	protected void loadHeadLessResult() {
		if(resultList != null && resultList.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(resultList.get(0).getPartName());
			for(int i = 1; i < resultList.size(); i++) {
				builder.append(System.getProperty("line.separator"));
				builder.append(resultList.get(i).getPartName());
			}
			getPartNameTextArea().setText(builder.toString());
		} else {
			getPartNameTextArea().setText(getPartNameLabel());
		}
		getHlCompletedField().setText(text);
		enableHeadLess(partResult.isStatusOnly());
	}

	private String getPartNameLabel() {
		PartName partName = getCurrentLotControlRule().getPartName();
		return partName != null ? partName.getWindowLabel() : getCurrentLotControlRule().getPartNameString();
	}

	protected boolean hasComment() {
		if(partResult.getLotControlRule() == null 
				|| partResult.getLotControlRule().getParts() == null 
				|| partResult.getLotControlRule().getParts().size() ==0)
			return false;

		return !StringUtils.isEmpty(partResult.getLotControlRule().getParts().get(0).getComment());
	}

	public void locateFocus() {
		if (partSnNg) {
			((UpperCaseFieldBean)getPartSnField()).setText(text);
		} else if(torqueIndex > 0) {
			((UpperCaseFieldBean)getCurrentTorque(torqueIndex)).setText(text);
		} else if(stringValueIndex > 0){
			((UpperCaseFieldBean)getCurrentStringValueField(stringValueIndex)).setText(text);
		}

	}


	protected int getInstalledPartStatus()
	{
		int iStatus = 0;
		iStatus = getHlCompletedField().getText().equalsIgnoreCase(HL_STATUS_COMPELTED) ? 1 : 0;
		return iStatus; 
	}

	private String getMainNo(){
		return MbpnDef.MAIN_NO.getValue(partSpec.getPartNumber().trim());
	}


	public void subScreenClose() {
		LoggedButton cancelBtn = getButtonExit();
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * This method is used to display error message.
	 * @param loggerMsg
	 * @param errMsg
	 */
	public void displayErrorMessage(String loggerMsg, String errMsg)
	{
		Logger.getLogger().error(loggerMsg);
		errorMsgLbl.setText(errMsg);
	}
	
	public Label getErrorMsgLbl() {
		return errorMsgLbl;
	}

	public void setErrorMsgLbl(Label errorMsgLbl) {
		this.errorMsgLbl = errorMsgLbl;
	}

	/**
	 * This method is used to clear the exception display message.
	 */
	public void clearDisplayMessage() {
		errorMsgLbl.setText("");
	}
	
	
	/**
	 * This method is used to get the current logged in user ID.
	 * 
	 * @param userId
	 */
	public String getUserId(){
		return parent.getMainWindow().getApplicationContext().getUserId().toUpperCase();
	}
	
	/**
	 * This method is used to handle exceptions.
	 * @param loggerMsg
	 * @param errMsg
	 * @param parentScreen
	 * @param e
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		Logger.getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}
	
	public void initListeners() {
		validationForTextfield();
		getPartSnField().setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				manager.confirmPartSn();
			}

		});

		for(int i = 0 ; i < getCurrentPartMeasurementCount(); i++){
			if(i ==  getCurrentPartMeasurementCount()-1){
				getTorqueTextFieldList().get(i).setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						manager.confirmMeasurement();
					}
				});
			}
			else{
				final int index = i+1;
				getTorqueTextFieldList().get(i).setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						manager.confirmMeasurement();
						getTorqueTextFieldList().get(index).requestFocus();
					}
				});
			}
		}
	}
	
	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

	public UpperCaseFieldBean getPartSnField() {
		return partSnField;
	}

	public void setPartSnField(UpperCaseFieldBean partSnField) {
		this.partSnField = partSnField;
	}

	public UpperCaseFieldBean getHlCompletedField() {
		return hlCompletedField;
	}

	public void setHlCompletedField(UpperCaseFieldBean hlCompletedField) {
		this.hlCompletedField = hlCompletedField;
	}

	public LoggedButton getButtonExit() {
		return buttonExit;
	}


	public LoggedButton getButtonSave() {
		return buttonSave;
	}

	public LoggedButton getButtonReset() {
		return buttonReset;
	}

	public LoggedButton getButtonHlCompleted() {
		return buttonHlCompleted;
	}

	public List<UpperCaseFieldBean> getTorqueTextFieldList() {
		return torqueTextFieldList;
	}

	public LoggedLabel getLabelHLCompleted() {
		return labelHLCompleted;
	}

	public List<UpperCaseFieldBean> getStringValueFieldList() {
		return stringValueFieldList;
	}

	public LoggedButton getButtonTorqueValue() {
		return buttonTorqueValue;
	}

	public List<LoggedTextArea> getMeasurementNameList() {
		return measurementNameList;
	}

	public BaseProduct getMyProduct() {
		return myProduct;
	}

	public void setMyProduct(BaseProduct myProduct) {
		this.myProduct = myProduct;
	}
	
}
