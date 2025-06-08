package com.honda.galc.client.teamleader;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.teamleader.property.ManualDcMcOnOffPropertyBean;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>ManualDcMcOnOffView Class description</h3>
 * <p>
 * ManualDcMcOnOffView description
 * </p>
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
 * @author Jeffray Huang<br>
 *         Jul 21, 2011
 * 
 * 
 */
public class ManualDcMcOnOffView extends ManualProductOnOffView implements ItemListener {

	private static final long serialVersionUID = 1L;

	private JComboBox deptComboBox;
	private JCheckBox noneHomeProductCheckBox;
	private JTextField mcNumberTextField;

	private Map<ProductType, List<ProductNumberDef>> dcNumberTypes;
	private Map<ProductType, List<ProductNumberDef>> mcNumberTypes;

	// === ON/OFF process points by product === //
	private Map<ProductType, List<ProcessPoint>> dcOnProcessPoints;
	private Map<ProductType, List<ProcessPoint>> dcOffProcessPoints;
	private Map<ProductType, List<ProcessPoint>> mcOnProcessPoints;
	private Map<ProductType, List<ProcessPoint>> mcOffProcessPoints;

	// === process model === //
	private DieCast product;

	public ManualDcMcOnOffView(ApplicationContext appContext, Application application) {
		super(appContext, application);
	}

	@Override
	protected void initialize() {
		setSize(1024, 768);
		setPropertyBean(PropertyService.getPropertyBean(ManualDcMcOnOffPropertyBean.class, getApplication().getApplicationId()));
		createComponents();
		initLayout();
		initData();
		mapActions();
		initState();
	}

	// === listeners mapping === //
	@Override
	protected void mapActions() {
		super.mapActions();
		getDeptComboBox().addActionListener(this);
		getMcNumberTextField().addActionListener(this);
		getNoneHomeProductCheckBox().addActionListener(this);
		getNoneHomeProductCheckBox().addItemListener(this);
		getDeptComboBox().addPopupMenuListener(this);
		getMcNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(null, getMcNumberTextField()) {
			@Override
			protected void processChange() {
				ManualDcMcOnOffView.this.processChange(getTextField());
			}
		});
	}

	// === Actionlistener === //
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == getDeptComboBox()) {
				departmentChanged();
			} else if (e.getSource() == getMcNumberTextField()) {
				processMcNumberInput();
			} else {
				super.actionPerformed(e);
			}
		} catch (TaskException ex) {
			Logger.getLogger().error(ex);
			setErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			Logger.getLogger().error(ex);
			setErrorMessage("Exception occured: \n" + ex.getMessage());
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getNoneHomeProductCheckBox()) {
			noneHomeProductSelectionChanged();
		}
	}

	// === handlers === //
	@Override
	protected void productTypeChanged() {
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		getDeptComboBox().removeAllItems();
		if (productType == null) {
			getDeptComboBox().setEnabled(false);
			return;
		}
		List<String> list = getDepartments(productType);
		if (!list.isEmpty()) {
			getDeptComboBox().setModel(new DefaultComboBoxModel(list.toArray()));
		}
		getDeptComboBox().setEnabled(true);
		initSelection(getDeptComboBox());
	}

	protected void departmentChanged() {
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		String department = (String) getDeptComboBox().getSelectedItem();
		getProcessTypeComboBox().removeAllItems();
		if (StringUtils.isBlank(department)) {
			getProcessTypeComboBox().setEnabled(false);
			return;
		}
		List<String> list = getProcessTypes(productType, department);
		if (!list.isEmpty()) {
			getProcessTypeComboBox().setModel(new DefaultComboBoxModel(list.toArray()));
		}
		getProcessTypeComboBox().setEnabled(true);
		initSelection(getProcessTypeComboBox());
	}

	@Override
	protected void processPointChanged() {
		clearMessage();
		getSaveButton().setEnabled(false);
		getNoneHomeProductCheckBox().setEnabled(false);
		getNoneHomeProductCheckBox().setSelected(false);
		getModelCodeComboBox().removeAllItems();
		getModelCodeComboBox().setEnabled(false);
		String department = (String) getDeptComboBox().getSelectedItem();
		String process = (String) getProcessTypeComboBox().getSelectedItem();
		setProduct(null);
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		getMcNumberTextField().setText("");
		getDcNumberTextField().setText("");

		if (processPoint == null) {
			getNoneHomeProductCheckBox().setEnabled(false);
			TextFieldState.DISABLED.setState(getDcNumberTextField());
			TextFieldState.DISABLED.setState(getMcNumberTextField());
			return;
		}

		TextFieldState.EDIT.setState(getDcNumberTextField());
		TextFieldState.DISABLED.setState(getMcNumberTextField());
		JTextField defaultInputField = getDcNumberTextField();

		if ("MC".equals(department) && "ON".equals(process)) {
			getNoneHomeProductCheckBox().setEnabled(true);
			getNoneHomeProductCheckBox().setSelected(getPropertyBean().isNoneHomeProductDefault());
		} else if ("MC".equals(department) && "OFF".equals(process)) {
			TextFieldState.EDIT.setState(getMcNumberTextField());
			defaultInputField = getMcNumberTextField();
		}

		final JTextField focusField = defaultInputField;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				focusField.requestFocus();
			}
		});
		if ("ON".equals(process)) {
			getModelCodeComboBox().setEnabled(true);
			Collection<String> modelCodes = getModelCodeMap().get(productType);
			if (modelCodes != null && !modelCodes.isEmpty()) {
				getModelCodeComboBox().setModel(new DefaultComboBoxModel(modelCodes.toArray()));
			}
			initSelection(getModelCodeComboBox());
		}
	}

	protected void noneHomeProductSelectionChanged() {
		if (getNoneHomeProductCheckBox().isSelected()) {
			TextFieldState.EDIT.setState(getMcNumberTextField());
		} else {
			getMcNumberTextField().setText("");
			TextFieldState.DISABLED.setState(getMcNumberTextField());
		}
		if (TextFieldState.READ_ONLY.isInState(getDcNumberTextField())) {
			TextFieldState.EDIT.setState(getDcNumberTextField());
		}
		getDcNumberTextField().requestFocus();
	}

	// === input processing === //
	@Override
	protected void processProductIdInput() {
		processDcNumberInput();
	}

	protected void processDcNumberInput() {
		setProduct(null);
		String dc = getDcNumberTextField().getText();
		dc = StringUtils.trim(dc);
		getDcNumberTextField().setText(dc);
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();

		String department = (String) getDeptComboBox().getSelectedItem();
		String processType = (String) getProcessTypeComboBox().getSelectedItem();

		if ("DC".equals(department) && "ON".equals(processType)) {
			processDcForDcOn(productType, dc);
		} else if ("DC".equals(department) && "OFF".equals(processType)) {
			processDcForDcOff(productType, dc);
		} else if ("MC".equals(department) && "ON".equals(processType) && !getNoneHomeProductCheckBox().isSelected()) {
			processDcForMcOn(productType, dc);
		} else if ("MC".equals(department) && "ON".equals(processType) && getNoneHomeProductCheckBox().isSelected()) {
			processDcForMcOnNoneHome(productType, dc);
		} else if ("MC".equals(department) && "OFF".equals(processType)) {
			processDcForMcOff(productType, dc);
		}
	}

	protected void processMcNumberInput() {
		String sn = getMcNumberTextField().getText();
		sn = StringUtils.trim(sn);
		getMcNumberTextField().setText(sn);

		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();

		String department = (String) getDeptComboBox().getSelectedItem();
		String process = (String) getProcessTypeComboBox().getSelectedItem();

		if ("MC".equals(department) && "ON".equals(process) && !getNoneHomeProductCheckBox().isSelected()) {
			processMcForMcOn(productType, sn);
		} else if ("MC".equals(department) && "ON".equals(process) && getNoneHomeProductCheckBox().isSelected()) {
			processMcForMcOnNoneHome(productType, sn);
		} else if ("MC".equals(department) && "OFF".equals(process)) {
			processMcForMcOff(productType, sn);
		}
	}

	// === DC Number === //
	protected boolean processDcForDcOn(ProductType productType, String sn) {
		if (!isValidDcNumber(productType, sn)) {
			return false;
		}
		DiecastDao<?> productDao = (DiecastDao<?>) ProductTypeUtil.getProductDao(productType);
		DieCast product = (DieCast) productDao.findByDCSerialNumber(sn);
		if (product != null) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			String productName = product.getProductType() != null ? product.getProductType().getProductName() : productType.name();
			setErrorMessage(String.format("%s already exists for DC Number %s", productName, sn));
			getDcNumberTextField().selectAll();
			return false;
		}
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected boolean processDcForDcOff(ProductType productType, String sn) {
		if (!isValidDcNumber(productType, sn)) {
			return false;
		}
		DieCast product = findProduct(getDcNumberTextField(), productType, NumberType.DC, sn);
		if (product == null) {
			return false;
		}

		ProcessPoint historyProcessPoint = geExistingHistoryProcessPoint(product);
		if (historyProcessPoint != null) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			setErrorMessage(String.format("OFF record already exists for ProcessPointId: %s", historyProcessPoint.getProcessPointId()));
			getDcNumberTextField().selectAll();
			return false;
		}

		setProduct(product);
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected boolean processDcForMcOn(ProductType productType, String sn) {
		if (!isValidDcNumber(productType, sn)) {
			return false;
		}
		DieCast product = findProduct(getDcNumberTextField(), productType, NumberType.DC, sn);
		if (product == null) {
			return false;
		}
		if (StringUtils.isNotBlank(product.getMcSerialNumber())) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			setErrorMessage(String.format("%s DC(%s) has already assigned MC: %s", productType.getProductName(), sn, product.getMcSerialNumber()));
			getDcNumberTextField().selectAll();
			return false;
		}
		setProduct(product);
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		TextFieldState.EDIT.setState(getMcNumberTextField());
		getMcNumberTextField().requestFocus();
		return true;
	}

	protected boolean processDcForMcOff(ProductType productType, String sn) {
		if (!isValidDcNumber(productType, sn)) {
			return false;
		}
		DieCast product = findProduct(getDcNumberTextField(), productType, NumberType.DC, sn);
		if (product == null) {
			return false;
		}
		if (StringUtils.isBlank(product.getMcSerialNumber())) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			setErrorMessage(String.format("%s DC(%s) does not have assigned MC number, please assigne MC number first and then process MC OFF", productType.getProductName(), sn));
			getDcNumberTextField().selectAll();
			return false;
		}

		ProcessPoint historyProcessPoint = geExistingHistoryProcessPoint(product);
		if (historyProcessPoint != null) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			setErrorMessage(String.format("OFF record already exists for ProcessPointId: %s", historyProcessPoint.getProcessPointId()));
			getDcNumberTextField().selectAll();
			return false;
		}

		setProduct(product);
		getMcNumberTextField().setText(product.getMcSerialNumber());
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		TextFieldState.READ_ONLY.setState(getMcNumberTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected boolean processDcForMcOnNoneHome(ProductType productType, String sn) {

		if (!isValidDcNumber(productType, sn)) {
			return false;
		}

		DiecastDao<?> productDao = (DiecastDao<?>) ProductTypeUtil.getProductDao(productType);
		DieCast product = (DieCast) productDao.findByDCSerialNumber(sn);
		if (product != null) {
			TextFieldState.ERROR.setState(getDcNumberTextField());
			setErrorMessage(String.format("%s already exists for DC Number %s", productType.getProductName(), sn));
			getDcNumberTextField().selectAll();
			return false;
		}
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		if (TextFieldState.READ_ONLY.isInState(getMcNumberTextField())) {
			getSaveButton().setEnabled(true);
			getSaveButton().requestFocus();
		} else {
			getMcNumberTextField().requestFocus();
		}
		return true;
	}

	protected boolean processMcForMcOn(ProductType productType, String sn) {
		if (!isValidMcNumber(productType, sn)) {
			return false;
		}
		DiecastDao<?> productDao = (DiecastDao<?>) ProductTypeUtil.getProductDao(productType);
		DieCast product = (DieCast) productDao.findByMCSerialNumber(sn);
		if (product != null) {
			TextFieldState.ERROR.setState(getMcNumberTextField());
			setErrorMessage(String.format("MC %s is already assigned to DC %s", sn, product.getDcSerialNumber()));
			getMcNumberTextField().selectAll();
			return false;
		}
		TextFieldState.READ_ONLY.setState(getMcNumberTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected boolean processMcForMcOff(ProductType productType, String sn) {
		if (!isValidMcNumber(productType, sn)) {
			return false;
		}
		DieCast product = findProduct(getMcNumberTextField(), productType, NumberType.MC, sn);
		if (product == null) {
			return false;
		}
		ProcessPoint historyProcessPoint = geExistingHistoryProcessPoint(product);
		if (historyProcessPoint != null) {
			TextFieldState.ERROR.setState(getMcNumberTextField());
			setErrorMessage(String.format("OFF record already exists for ProcessPointId: %s", historyProcessPoint.getProcessPointId()));
			getMcNumberTextField().selectAll();
			return false;
		}
		setProduct(product);
		getDcNumberTextField().setText(product.getDcSerialNumber());
		TextFieldState.READ_ONLY.setState(getDcNumberTextField());
		TextFieldState.READ_ONLY.setState(getMcNumberTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected boolean processMcForMcOnNoneHome(ProductType productType, String sn) {
		if (!isValidMcNumber(productType, sn)) {
			return false;
		}
		DiecastDao<?> productDao = (DiecastDao<?>) ProductTypeUtil.getProductDao(productType);
		DieCast product = (DieCast) productDao.findByMCSerialNumber(sn);
		if (product != null) {
			TextFieldState.ERROR.setState(getMcNumberTextField());
			setErrorMessage(String.format("%s already exists for MC Number %s", productType.getProductName(), sn));
			getMcNumberTextField().selectAll();
			return false;
		}
		TextFieldState.READ_ONLY.setState(getMcNumberTextField());

		if (TextFieldState.READ_ONLY.isInState(getDcNumberTextField())) {
			getSaveButton().setEnabled(true);
			getSaveButton().requestFocus();
		} else {
			getDcNumberTextField().requestFocus();
		}
		return true;
	}

	// === initialization === //
	@Override
	protected void createComponents() {
		super.createComponents();

		this.dcNumberTypes = new LinkedHashMap<ProductType, List<ProductNumberDef>>();
		this.mcNumberTypes = new LinkedHashMap<ProductType, List<ProductNumberDef>>();
		this.dcOnProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();
		this.dcOffProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();
		this.mcOnProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();
		this.mcOffProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();

		this.deptComboBox = createComboBox("deptComboBox");
		this.noneHomeProductCheckBox = createCheckBox("None Home Product");
		this.mcNumberTextField = createTextField("mcNumberTextField");
	}

	@Override
	protected void initLayout() {

		JPanel panel = new JPanel(new MigLayout("insets 30 20 0 20", "[fill,max][fill,max,][fill,max,][fill,max,][fill][fill]", "[max][max][max][max][max][max]"));
		panel.add(createTitleBar(getPropertyBean().getTitle()), "span 6, wrap");

		panel.add(createLabel("Product"));
		panel.add(getProductTypeComboBox(), "wrap");

		panel.add(createLabel("Department"));
		panel.add(getDeptComboBox(), "wrap");

		panel.add(createLabel("Process"));
		panel.add(getProcessTypeComboBox(), "wrap");
		panel.add(createLabel("Station"));
		panel.add(getProcessPointComboBox(), "span, wrap");

		panel.add(getNoneHomeProductCheckBox(), "cell 1 5, span, wrap");

		panel.add(createLabel("DC Number"));
		panel.add(getDcNumberTextField(), "span 6, wrap");

		panel.add(createLabel("MC Number"));
		panel.add(getMcNumberTextField(), "span 6, wrap");

		panel.add(createLabel("Model"));
		panel.add(getModelCodeComboBox());

		panel.add(getSaveButton(), "width 150!,  cell 5 8, aligny bottom");
		panel.add(getCancelButton(), "wrap, aligny bottom");

		getProductTypeComboBox().setRenderer(new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName"));
		getProcessPointComboBox().setRenderer(new PropertyPatternComboBoxRenderer<ProcessPoint>(ProcessPoint.class, "%s(%s)", "processPointName", "processPointId"));
		getProductTypeComboBox().setEnabled(true);
		getCancelButton().setEnabled(true);
		setClientPanel(panel);
	}

	@Override
	protected void initData() {
		initProcessPoints(getDcOnProcessPoints(), getPropertyBean().getDcOnProcessPoints(), "DcOn");
		initProcessPoints(getDcOffProcessPoints(), getPropertyBean().getDcOffProcessPoints(), "DcOff");
		initProcessPoints(getMcOnProcessPoints(), getPropertyBean().getMcOnProcessPoints(), "McOn");
		initProcessPoints(getMcOffProcessPoints(), getPropertyBean().getMcOffProcessPoints(), "McOff");
		initNumberTypes(getDcNumberTypes(), NumberType.DC);
		initNumberTypes(getMcNumberTypes(), NumberType.MC);
		initModelCodes();
		getProductTypeComboBox().setModel(new DefaultComboBoxModel(getProductTypes().toArray()));
	}

	@Override
	protected void populateStationList() {

		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		String department = (String) getDeptComboBox().getSelectedItem();
		String processType = (String) getProcessTypeComboBox().getSelectedItem();

		getProcessPointComboBox().removeAllItems();
		if (productType == null || StringUtils.isEmpty(department) || StringUtils.isEmpty(processType)) {
			return;
		}
		List<ProcessPoint> processPoints = null;
		if ("DC".equals(department) && "ON".equals(processType)) {
			processPoints = getDcOnProcessPoints().get(productType);
		} else if ("DC".equals(department) && "OFF".equals(processType)) {
			processPoints = getDcOffProcessPoints().get(productType);
		} else if ("MC".equals(department) && "ON".equals(processType)) {
			processPoints = getMcOnProcessPoints().get(productType);
		} else if ("MC".equals(department) && "OFF".equals(processType)) {
			processPoints = getMcOffProcessPoints().get(productType);
		}
		if (processPoints == null || processPoints.isEmpty()) {
			return;
		}
		getProcessPointComboBox().setModel(new DefaultComboBoxModel(processPoints.toArray()));
		initSelection(getProcessPointComboBox());
	}

	// === utility methods === //
	protected boolean isValidDcNumber(ProductType productType, String sn) {
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		List<String> masks =  getDcNumberMask(processPoint.getProcessPointId());
		return isValidInputNumber(getDcNumberTextField(), productType, NumberType.DC, getDcNumberTypes().get(productType), masks);
	}

	protected boolean isValidMcNumber(ProductType productType, String sn) {
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		List<String> masks =  getMcNumberMask(processPoint.getProcessPointId());
		return isValidInputNumber(getMcNumberTextField(), productType, NumberType.MC, getMcNumberTypes().get(productType), masks);
	}
	
	@Override
	protected DieCast findProduct(JTextField inputField, ProductType productType, NumberType numberType, String sn) {
		return (DieCast) super.findProduct(inputField, productType, numberType, sn);
	}

	@Override
	protected String getProcessName() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.capitalize(getDeptComboBox().getSelectedItem().toString().toLowerCase()));
		sb.append(StringUtils.capitalize(getProcessTypeComboBox().getSelectedItem().toString().toLowerCase()));
		return sb.toString();
	}

	@Override
	protected List<ProductType> getProductTypes() {
		Set<ProductType> productTypes = new TreeSet<ProductType>();
		productTypes.addAll(getDcOnProcessPoints().keySet());
		productTypes.addAll(getDcOffProcessPoints().keySet());
		productTypes.addAll(getMcOnProcessPoints().keySet());
		productTypes.addAll(getMcOffProcessPoints().keySet());
		List<ProductType> list = new ArrayList<ProductType>(productTypes);
		if (list.size() > 1) {
			list.add(0, null);
		}
		return list;
	}

	protected List<String> getDepartments(ProductType productType) {
		List<String> list = new ArrayList<String>();
		if (productType == null) {
			return list;
		}
		List<ProcessPoint> dcOnPps = getDcOnProcessPoints().get(productType);
		List<ProcessPoint> dcOffPps = getDcOffProcessPoints().get(productType);
		List<ProcessPoint> mcOnPps = getMcOnProcessPoints().get(productType);
		List<ProcessPoint> mcOffPps = getMcOffProcessPoints().get(productType);

		if (dcOnPps != null && !dcOnPps.isEmpty() || dcOffPps != null && !dcOffPps.isEmpty()) {
			list.add("DC");
		}
		if (mcOnPps != null && !mcOnPps.isEmpty() || mcOffPps != null && !mcOffPps.isEmpty()) {
			list.add("MC");
		}
		return list;
	}

	protected List<String> getProcessTypes(ProductType productType, String department) {
		List<String> list = new ArrayList<String>();
		if (productType == null || StringUtils.isBlank(department)) {
			return list;
		}
		List<ProcessPoint> onPps = null;
		List<ProcessPoint> offPps = null;
		if ("DC".equals(department)) {
			onPps = getDcOnProcessPoints().get(productType);
			offPps = getDcOffProcessPoints().get(productType);
		} else if ("MC".equals(department)) {
			onPps = getMcOnProcessPoints().get(productType);
			offPps = getMcOffProcessPoints().get(productType);
		}
		if (onPps != null && !onPps.isEmpty()) {
			list.add("ON");
		}
		if (offPps != null && !offPps.isEmpty()) {
			list.add("OFF");
		}
		return list;
	}

	// === Service API === //
	@Override
	protected Device getServiceDevice(ProcessPoint processPoint) {
		String tagName = TagNames.PRODUCT_ID.name();
		Device device = findDevice(processPoint.getProcessPointId(), tagName);
		if (device == null) {
			throw new TaskException("There is no Device defined with " + tagName + " deviceFormat");
		}
		setProductIdDataFormat(device);
		setModelDataFormat(device);
		setMcNumberFormat(device);
		return device;
	}

	protected Device setMcNumberFormat(Device device) {
		String mcNumber = getMcNumberTextField().getText().trim();
		if (!StringUtils.isBlank(mcNumber)) {
			DeviceFormat deviceFormat = getDeviceFormat(device, TagNames.MC_NUMBER.name());
			deviceFormat.setValue(mcNumber);
		}
		return device;
	}

	// === get/set === //
	protected Map<ProductType, List<ProcessPoint>> getDcOnProcessPoints() {
		return dcOnProcessPoints;
	}

	protected Map<ProductType, List<ProcessPoint>> getDcOffProcessPoints() {
		return dcOffProcessPoints;
	}

	protected Map<ProductType, List<ProcessPoint>> getMcOnProcessPoints() {
		return mcOnProcessPoints;
	}

	protected Map<ProductType, List<ProcessPoint>> getMcOffProcessPoints() {
		return mcOffProcessPoints;
	}

	protected JComboBox getDeptComboBox() {
		return deptComboBox;
	}

	protected JTextField getDcNumberTextField() {
		return getProductIdTextField();
	}

	protected JTextField getMcNumberTextField() {
		return mcNumberTextField;
	}

	protected Map<ProductType, List<ProductNumberDef>> getDcNumberTypes() {
		return dcNumberTypes;
	}

	protected Map<ProductType, List<ProductNumberDef>> getMcNumberTypes() {
		return mcNumberTypes;
	}

	@Override
	protected ManualDcMcOnOffPropertyBean getPropertyBean() {
		return (ManualDcMcOnOffPropertyBean) super.getPropertyBean();
	}

	protected DieCast getProduct() {
		return product;
	}

	protected void setProduct(DieCast product) {
		this.product = product;
	}

	protected JCheckBox getNoneHomeProductCheckBox() {
		return noneHomeProductCheckBox;
	}
	
	protected List<String> getDcNumberMask(String processPointId) {
		HeadLessPropertyBean propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);
		String[] prop = propertyBean.getDcNumberMask();
		if (prop != null && prop.length > 0) {
			return Arrays.asList(prop);
		}
		return new ArrayList<String>();		
	}
	
	protected List<String> getMcNumberMask(String processPointId) {
		HeadLessPropertyBean propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);
		String[] prop = propertyBean.getMcNumberMask();
		if (prop != null && prop.length > 0) {
			return Arrays.asList(prop);
		}
		return new ArrayList<String>();				
	}	
}
