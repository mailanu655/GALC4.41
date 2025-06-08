package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.HttpDeviceInvoker;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.teamleader.property.ManualOnOffPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.IProperty;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.on.MbpnProductOnService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.MbpnSpecCodeUtil;
import com.honda.galc.util.ProductSpecUtil;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ManualProductOnOffView</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * @author Karol Wozniak
 * @created Oct 7, 2014
 */
public class ManualProductOnOffView extends MainWindow implements PopupMenuListener {

	private static final long serialVersionUID = 1L;
	
	protected static final String PRODUCT_ON_MASK = "ProductOnMask";
	protected static final String ON = "ON";
	protected static final String OFF = "OFF";
	
	private static String INCOMPLETE_PROD_LOT_SQL = "SELECT a.* FROM gal212tbx a WHERE a.SEND_STATUS = 2 AND a.plan_code =?1 " + 
			"AND a.LOT_SIZE > (SELECT COUNT(*) FROM GAL216TBX WHERE PRODUCTION_LOT = a.PRODUCTION_LOT and SEND_STATUS = 2)";
	
	private static String NOT_STAMPED_PRODUCTS = "SELECT * FROM GAL216TBX WHERE PRODUCTION_LOT = ?1 and SEND_STATUS < 2 ORDER BY STAMPING_SEQUENCE_NO ";

	
	private JComboBox productTypeComboBox;
	private JComboBox processTypeComboBox;
	private JComboBox processPointComboBox;
	private JComboBox modelCodeComboBox;
	
	private JTextField productIdTextField;
	
	private JLabel modelOrProdLotLabel;
	
	private JComboBox preProductionLotComboBox;
	private JPanel modelOrLotPanel;

	private JButton saveButton;
	private JButton cancelButton;

	private IProperty propertyBean;

	private Map<ProductType, Collection<String>> modelCodeMap;
	private Map<ProductType, List<ProductNumberDef>> productNumberTypeMap;
	
	private List<MbpnProductType> mbpnProductTypes;

	// === ON/OFF process points by product === //
	private Map<ProductType, List<ProcessPoint>> onProcessPoints;
	private Map<ProductType, List<ProcessPoint>> offProcessPoints;
	private String productId;

	public ManualProductOnOffView(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		initialize();
	}

	protected void initialize() {
		setSize(1024, 768);
		setPropertyBean(PropertyService.getPropertyBean(ManualOnOffPropertyBean.class, getApplication().getApplicationId()));
		createComponents();
		initLayout();
		initData();
		mapActions();
		initState();
	}

	// === listeners mapping === //
	protected void mapActions() {
		getProductTypeComboBox().addActionListener(this);
		getProcessTypeComboBox().addActionListener(this);
		getProcessPointComboBox().addActionListener(this);
		getModelCodeComboBox().addActionListener(this);
		preProductionLotComboBox.addActionListener(this);
		getProductIdTextField().addActionListener(this);
		getCancelButton().addActionListener(this);
		getSaveButton().addActionListener(this);

		getProductTypeComboBox().addPopupMenuListener(this);
		getProcessTypeComboBox().addPopupMenuListener(this);
		getProcessPointComboBox().addPopupMenuListener(this);
		preProductionLotComboBox.addPopupMenuListener(this);

		getProductIdTextField().getDocument().addDocumentListener(new InputNumberChangeListener(null, getProductIdTextField()) {
			@Override
			protected void processChange() {
				ManualProductOnOffView.this.processChange(getTextField());
			}
		});
	}

	// === Actionlistener === //
	public void actionPerformed(ActionEvent e) {
		try {
			getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (e.getSource() == getProductTypeComboBox()) {
				productTypeChanged();
			} else if (e.getSource() == getProcessTypeComboBox()) {
				processTypeChanged();
			} else if (e.getSource() == getProcessPointComboBox()) {
				processPointChanged();
			} else if (e.getSource() == getModelCodeComboBox()) {
				modelCodeChanged();
			} else if (e.getSource() == preProductionLotComboBox) {
				productionLotChanged();
			} else if (e.getSource() == getProductIdTextField()) {
				processProductIdInput();
			} else if (e.getSource() == getCancelButton()) {
				cancelClicked();
			} else if (e.getSource() == getSaveButton()) {
				saveClicked();
			} else {
				super.actionPerformed(e);
			}
		} catch (TaskException ex) {
			Logger.getLogger().error(ex);
			setErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex);
			setErrorMessage("Exception occured: \n" + ex.getMessage());
		} finally {
			getRootPane().setCursor(Cursor.getDefaultCursor());
		}
	}

	// == PopupMenuListener === //
	public void popupMenuCanceled(PopupMenuEvent e) {
		popupMenuWillBecomeInvisible(e);
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JComboBox) {
			JComboBox cb = (JComboBox) obj;
			Object item = cb.getSelectedItem();
			if ((item instanceof String) && StringUtils.isBlank((String) item)) {
				return;
			}
			if (item == null) {
				return;
			}
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
		}
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

	}

	// === handlers === //
	protected void saveClicked() {
		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want to process " + getProductProcessName() + " ?", "", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.NO_OPTION == retCode) {
			return;
		}
		productId = getProductIdTextField().getText();
		boolean success = isProcessingProductOff() ?processOff() :processOn();
			
		if(shouldCloseBasedOnApplicationState() || (getPropertyBean().isMbpnProductOnServiceUsed() && success)){
			EventBus.publish(new ProductProcessEvent(productId, getProductType(), ProductProcessEvent.State.LOAD));
		}
	}
	
	private boolean isProcessingProductOff() {
		return OFF.equals(getProcessTypeComboBox().getSelectedItem());
	}
	
	private boolean shouldCloseBasedOnApplicationState() {
		return Boolean.TRUE.toString().equalsIgnoreCase(applicationContext.getArguments().getAdditionalParams().get("-closeAfterSave")) &&
		StringUtils.isEmpty(getProductIdTextField().getText()) && 
		!StringUtils.isEmpty(productId);
	}
	
	protected boolean processOff() {
		boolean success = false;
		try {
			ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
			ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
			String productId = getProductIdTextField().getText();
			ServiceFactory.getService(TrackingService.class).track(productType, productId, processPoint.getProcessPointId());
			success = true;
			resetScreen();
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception uccured when executing Tracking");
			setErrorMessage("Exception uccured when executing Tracking: \n" + e);
		}
		return success;
	}

	protected boolean processOn() {

		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		Device device = getServiceDevice(processPoint);

		boolean success = invokeService(device);

		if (success) {
			resetScreen();
		}
		return success;
	}

	protected void cancelClicked() {
		resetScreen();
	}

	protected void productTypeChanged() {
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		getProcessTypeComboBox().removeAllItems();
		if (productType == null) {
			getProcessTypeComboBox().setEnabled(false);
			return;
		}
		List<String> list = getProcessTypes(productType);
		if (!list.isEmpty()) {
			getProcessTypeComboBox().setModel(new DefaultComboBoxModel(list.toArray()));
		}
		getProcessTypeComboBox().setEnabled(true);
		initSelection(getProcessTypeComboBox());
	}

	protected void processTypeChanged() {
		String processType = (String) getProcessTypeComboBox().getSelectedItem();
		getProcessPointComboBox().removeAllItems();
		if (StringUtils.isBlank(processType)) {
			getProcessPointComboBox().setEnabled(false);
			return;
		}
		getProcessPointComboBox().setEnabled(true);
		populateStationList();
	}

	protected void processPointChanged() {
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		String process = (String) getProcessTypeComboBox().getSelectedItem();
		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		getModelCodeComboBox().removeAllItems();
		getModelCodeComboBox().setEnabled(false);
		preProductionLotComboBox.setEnabled(false);
		if (processPoint == null) {
			TextFieldState.DISABLED.setState(getProductIdTextField());
			return;
		}
		TextFieldState.EDIT.setState(getProductIdTextField());
		if (ON.equals(process)) {
			
			if(getPropertyBean().getMbpnPlanCodeMap() != null && getPropertyBean().getMbpnPlanCodeMap().containsKey(processPoint.getProcessPointId())) {
				String planCode =  getPropertyBean().getMbpnPlanCodeMap().get(processPoint.getProcessPointId());
				PreProductionLot[] lots= findIncompleteProductionLots(planCode);
				modelOrProdLotLabel.setText("Production Lot");
				preProductionLotComboBox.setModel(new DefaultComboBoxModel<PreProductionLot>(lots));
				preProductionLotComboBox.setEnabled(true);
				getModelCodeComboBox().setEnabled(false);
				((CardLayout) modelOrLotPanel.getLayout()).show(modelOrLotPanel, "preProdLot");
			}
			else {
				modelOrProdLotLabel.setText("Model");
				getModelCodeComboBox().setEnabled(true);
				preProductionLotComboBox.setEnabled(false);
				Collection<String> modelCodes = getFilteredModelCodeMap(processPoint, productType);
				if (modelCodes != null && !modelCodes.isEmpty()) {
					getModelCodeComboBox().setModel(new DefaultComboBoxModel(modelCodes.toArray()));
				}
				((CardLayout) modelOrLotPanel.getLayout()).show(modelOrLotPanel, "model");

				initSelection(getModelCodeComboBox());
			}
		} else {
			UiUtils.requestFocus(getProductIdTextField());
		}
	}
	
	private PreProductionLot[] findIncompleteProductionLots(String planCode) {

		List<PreProductionLot> preProductionLots = new ArrayList<PreProductionLot>();
		
		preProductionLots = getService(GenericDaoService.class).findAll(INCOMPLETE_PROD_LOT_SQL, Parameters.with("1", planCode), PreProductionLot.class);

		PreProductionLot[] arr = new PreProductionLot[preProductionLots.size()];
		return preProductionLots.toArray(arr);

	}
	
	private ProductStampingSequence[] findNotStampedProducts(String productionLot) {
		List<ProductStampingSequence> products = new ArrayList<ProductStampingSequence>();
		
		products = getService(GenericDaoService.class).findAll(NOT_STAMPED_PRODUCTS, Parameters.with("1", productionLot), ProductStampingSequence.class);

		ProductStampingSequence[] arr = new ProductStampingSequence[products.size()];
		return products.toArray(arr);

	}
	
	private Collection<String> getFilteredModelCodeMap(ProcessPoint processPoint, ProductType productType) {
		Collection<String> modelCodes = getModelCodeMap().get(productType);
		String mainNo = getMainNoForProcessPoint(processPoint);
		if (mainNo != null) {
			ArrayList<String> result = new ArrayList<String>();
			for(String eachModelCode : modelCodes) {
				if(eachModelCode == null || MbpnSpecCodeUtil.getMainNo(eachModelCode).equals(mainNo))
					result.add(eachModelCode);
			}
			return result;
		}
		return modelCodes;
	}

	private String getMainNoForProcessPoint(ProcessPoint processPoint) {
		Map<String,String> map = getPropertyBean().getMainNumberMap();
		if (map == null)
			return null;
		return map.get(processPoint.getProcessPointId());
	}
	
	protected void modelCodeChanged() {
		clearMessage();
		getSaveButton().setEnabled(false);
		TextFieldState.EDIT.setState(getProductIdTextField());
		Object modelCode = getModelCodeComboBox().getSelectedItem();
		if (modelCode != null && StringUtils.isNotBlank(modelCode.toString())) {
			UiUtils.requestFocus(getProductIdTextField());
		}
	}
	
	protected void productionLotChanged() {
		clearMessage();
		getSaveButton().setEnabled(false);
		TextFieldState.EDIT.setState(getProductIdTextField());
		Object modelCode = getModelCodeComboBox().getSelectedItem();
		if (modelCode != null && StringUtils.isNotBlank(modelCode.toString())) {
			UiUtils.requestFocus(getProductIdTextField());
		}

	}

	protected void processChange(JTextField field) {
		if (field == null) {
			return;
		}
		if (!field.isEditable()) {
			return;
		}
		if (!field.isEnabled()) {
			return;
		}
		if (getStatusMessagePanel().isError()) {
			clearMessage();
		}
		TextFieldState.EDIT.setState(field);
	}

	protected void resetScreen() {
		processPointChanged();
		getProductIdTextField().setText("");
	}

	// === input processing === //
	protected void processProductIdInput() {
		String id = getProductIdTextField().getText();
		id = StringUtils.trim(id);
		getProductIdTextField().setText(id);

		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		String processType = (String) getProcessTypeComboBox().getSelectedItem();

		if (ON.equals(processType)) {
			processProductIdForOn(productType, id);
		} else if (OFF.equals(processType)) {
			processProductIdForOff(productType, id);
		}
	}
	
	protected boolean processProductIdForOn(ProductType productType, String sn) {
		
		if(!isProductIdValid(productType, sn)) return false;

		ProductDao<?> productDao = ProductTypeUtil.getProductDao(productType);
		BaseProduct product = productDao.findBySn(sn);
		if (product != null) {
			TextFieldState.ERROR.setState(getProductIdTextField());
			String productName = product.getProductType() != null ? product.getProductType().getProductName() : productType.name();
			setErrorMessage(String.format("Product %s already exists for number %s", productName, sn));
			getProductIdTextField().selectAll();
			return false;
		}
		TextFieldState.READ_ONLY.setState(getProductIdTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}
	
	protected boolean isProductIdValid(ProductType productType, String sn) {
		
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		
		if(preProductionLotComboBox.isEnabled()) {
			PreProductionLot preProductionLot = (PreProductionLot) preProductionLotComboBox.getSelectedItem();
			
			return validateByProductIdNumDef(preProductionLot.getProductSpecCode(), sn);
		}else {
			Object modelCode = getModelCodeComboBox().getSelectedItem();
			if (modelCode == null || StringUtils.isBlank(modelCode.toString())) {
				JOptionPane.showMessageDialog(this, "Please select Model !", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				UiUtils.requestFocus(getModelCodeComboBox());
				return false;
			}		
			String spec = modelCode.toString().trim();
			
			if(getPropertyBean().isProductIdValidationFromProductIdNumDef()) {
				return validateByProductIdNumDef(spec, sn);
			}else {

				List<String> masks = getPropertyBean().isProductIdValidationFromBuildAttributes() 
						? getMasksFromBuildAttributes(spec)
						: getMasksFromProductIdMasks(productType, processPoint, spec); 
	
				if (masks.isEmpty()) {
					reportMissingProductIdMaskError(spec);
					return false;
				}
				if (!isValidInputNumber(getProductIdTextField(), productType, NumberType.IN, getProductNumberTypeMap().get(productType), masks)) {
					return false;
				}
				
				return true;
			}
		}
	}
	
	private boolean validateByProductIdNumDef(String productSpecCode, String sn) {
		MbpnProductType type = findMbpnProductType(productSpecCode);
		if(type == null || type.getProductIdNumberDefs() == null ) return true;
		
		String msg = "";
        
       	for(ProductIdNumberDef def : type.getProductIdNumberDefs()) {
    		 if(sn.length() == def.getLength() &&
    			   CommonPartUtility.verification(sn, def.getMask(), PropertyService.getPartMaskWildcardFormat()))
    			   return true;
    		 msg += " [" + def.getLength() + "," + def.getMask()+"]";
    	}
       	
       	TextFieldState.ERROR.setState(productIdTextField);
		setErrorMessage(String.format("MBPN Number '%s' must match mask '%s' ", sn,msg));
		productIdTextField.selectAll();
       	
       	return false;
	}
	
	

	private List<String> getMasksFromBuildAttributes(String spec) {
		List<String> result = new ArrayList<String>();
		BuildAttribute attribute = ServiceFactory.getDao(BuildAttributeDao.class).findById(PRODUCT_ON_MASK, spec);
		if(attribute != null)
			result.add(attribute.getAttributeValue());
		return result;		
	}

	private List<String> getMasksFromProductIdMasks(ProductType productType, ProcessPoint processPoint, String spec) {
		List<String> masks = new ArrayList<String>();
		List<ProductIdMask> productIdMasks = ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProcessPointAndProductType(processPoint.getProcessPointId(), productType.name());
		if (productIdMasks != null && !productIdMasks.isEmpty()) {
			ProductIdMask pidMask = ProductSpecUtil.getMatchedItem(spec, productIdMasks, ProductIdMask.class, ProductTypeUtil.isMbpnProduct(productType));
			if (pidMask != null) {
				masks.add(pidMask.getId().getProductIdMask());
				for (ProductIdMask mask : productIdMasks) {
					if (pidMask.getProductSpecCode().equals(mask.getProductSpecCode())) {
						if (!masks.contains(mask.getId().getProductIdMask())) {
							masks.add(mask.getId().getProductIdMask());
						}
					}
				}
			}
		}
		return masks;
	}

	private void reportMissingProductIdMaskError(String spec) {
		TextFieldState.ERROR.setState(getProductIdTextField());
		setErrorMessage("Missing ProductIdMask for spec :" + spec);
		getProductIdTextField().selectAll();
	}
	
	protected List<String> selectProductIdMask(String processPointId, String productTypeName) {
		List<String> list = new ArrayList<String>();
		List<ProductIdMask> productIdMasks = ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProcessPointAndProductType(processPointId, productTypeName);
		if (productIdMasks == null || productIdMasks.isEmpty()) {
			return list;
		}
		for (ProductIdMask mask : productIdMasks) {
			list.add(mask.getId().getProductIdMask().trim());
		}
		return list;
	}

	protected boolean processProductIdForOff(ProductType productType, String sn) {
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		List<String> masks =  selectProductIdMask(processPoint.getProcessPointId(), productType.getProductName());
		if (!isValidInputNumber(getProductIdTextField(), productType, NumberType.IN, getProductNumberTypeMap().get(productType), masks)) {
			return false;
		}
		BaseProduct product = findProduct(getProductIdTextField(), productType, NumberType.IN, sn);
		if (product == null) {
			return false;
		}

		ProcessPoint historyProcessPoint = geExistingHistoryProcessPoint(product);
		if (historyProcessPoint != null) {
			TextFieldState.ERROR.setState(getProductIdTextField());
			setErrorMessage(String.format("OFF record already exists for ProcessPointId: %s", historyProcessPoint.getProcessPointId()));
			getProductIdTextField().selectAll();
			return false;
		}

		TextFieldState.READ_ONLY.setState(getProductIdTextField());
		getSaveButton().setEnabled(true);
		getSaveButton().requestFocus();
		return true;
	}

	protected BaseProduct findProduct(JTextField inputField, ProductType productType, NumberType numberType, String sn) {
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(productType);
		BaseProduct product = null;
		if (productDao instanceof DiecastDao) {
			DiecastDao<?> diecastDao = (DiecastDao<?>) productDao;
			product = diecastDao.findBySn(sn, numberType);
		} else {
			product = productDao.findBySn(sn);
		}
		if (product == null) {
			TextFieldState.ERROR.setState(inputField);
			setErrorMessage(String.format("Product:%s does not exist for %s Number: %s", productType, numberType, sn));
			inputField.selectAll();
			return null;
		} else if (!productType.equals(product.getProductType())) {
			TextFieldState.ERROR.setState(inputField);
			setErrorMessage(String.format("Product:%s does not exist for %s Number:%s, product of type %s exists with this number", productType, numberType, sn, product.getProductType()));
			inputField.selectAll();
			return null;
		} else {
			return product;
		}
	}

	// === initialization === //
	protected void createComponents() {

		this.modelCodeMap = new HashMap<ProductType, Collection<String>>();
		this.productNumberTypeMap = new LinkedHashMap<ProductType, List<ProductNumberDef>>();
		this.onProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();
		this.offProcessPoints = new LinkedHashMap<ProductType, List<ProcessPoint>>();

		this.productTypeComboBox = createComboBox("productTypeComboBox");
		this.processTypeComboBox = createComboBox("processTypeComboBox");
		this.processPointComboBox = createComboBox("processPointComboBox");
		this.modelCodeComboBox = createComboBox("modelCodeComboBox");
		this.preProductionLotComboBox = createComboBox("preProductionLotComboBox");
		
		this.modelOrProdLotLabel = createLabel("Model");
		
		this.productIdTextField = createTextField("productIdTextField");
		this.saveButton = createButton("Save");
		this.cancelButton = createButton("Cancel");

		getProductTypeComboBox().setRenderer(new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName"));
		getProcessPointComboBox().setRenderer(new PropertyPatternComboBoxRenderer<ProcessPoint>(ProcessPoint.class, "%s(%s)", "processPointName", "processPointId"));
		preProductionLotComboBox.setRenderer(new PropertyPatternComboBoxRenderer<PreProductionLot>(PreProductionLot.class, "%s(%s)", "productionLot","productSpecCode"));

		getProductTypeComboBox().setEnabled(true);
		getCancelButton().setEnabled(true);
	}

	protected void initLayout() {
		JPanel panel = new JPanel(new MigLayout("insets 30 20 0 20", "[fill,max][fill,max,][fill,max,][fill,max,][fill]", "[max][max][max][max][max]"));
		panel.add(createTitleBar("Manual ON/OFF"), "span 5, wrap ");
		panel.add(createLabel("Product"));
		panel.add(getProductTypeComboBox(), "span 2,wrap");
		panel.add(createLabel("Process"));
		panel.add(getProcessTypeComboBox(), "span 2,wrap");
		panel.add(createLabel("Station"));
		panel.add(getProcessPointComboBox(), "span 2,wrap");
		panel.add(this.modelOrProdLotLabel);
		panel.add(modelOrLotPanel = createModelOrLotPanel(),"span 2,wrap");
		panel.add(createLabel("Product Id"));
		panel.add(getProductIdTextField(), "span 2,wrap");
		panel.add(getSaveButton(), "width 150!,  cell 1 9");
		panel.add(getCancelButton(), "width 150!,wrap");
		setClientPanel(panel);
	}

	protected void initData() {
		initProcessPoints(getOnProcessPoints(), getPropertyBean().getOnProcessPoints(), ON);
		initProcessPoints(getOffProcessPoints(), getPropertyBean().getOffProcessPoints(), OFF);
		initNumberTypes(getProductNumberTypeMap(), NumberType.IN);
		initModelCodes();
		getProductTypeComboBox().setModel(new DefaultComboBoxModel(getProductTypes().toArray()));
	}
	
	private JPanel createModelOrLotPanel() {
		JPanel panel = new JPanel(new CardLayout());
		panel.add(modelCodeComboBox,"model");
		panel.add(preProductionLotComboBox,"preProdLot");
		CardLayout layout = (CardLayout)panel.getLayout();
		layout.show(panel, "preProdLot");
		return panel;
	}
	
	protected void initState() {
		initSelection(getProductTypeComboBox());
	}

	protected void initProcessPoints(Map<ProductType, List<ProcessPoint>> processPointMap, String[] processPointIds, String processName) {
		if (processPointIds == null || processPointIds.length == 0) {
			return;
		}
		ProcessPointDao dao = ServiceFactory.getDao(ProcessPointDao.class);
		List<String> errorMsgs = new ArrayList<String>();
		ProductType defaultProductType = getProductType();

		for (String processPointId : processPointIds) {
			if (StringUtils.isBlank(processPointId)) {
				continue;
			}
			ProcessPoint pp = dao.findById(StringUtils.trim(processPointId));
			if (pp == null) {
				String msg = String.format("Config Error - invalid ProcessPointId:%s defined for process %s", processPointId, processName);
				Logger.getLogger().error(msg);
				errorMsgs.add(msg);
				continue;
			}
			
			ProductType productType = defaultProductType;
			
			if(getPropertyBean().getMainNumberMap() != null && getPropertyBean().getMainNumberMap().containsKey(processPointId)) {
				productType = ProductType.MBPN;
			} else {

				String productTypeName = PropertyService.getProperty(pp.getProcessPointId(), "PRODUCT_TYPE");
				if (StringUtils.isNotBlank(productTypeName)) {
					productType = ProductType.getType(StringUtils.trim(productTypeName));
					if (productType == null) {
						String msg = String.format("Config Error - invalid ProductType defined:%s for process point: %s", productTypeName, pp.getProcessPointId());
						Logger.getLogger().error(msg);
						errorMsgs.add(msg);
						continue;
					}
				}
			}

			List<ProcessPoint> processPoints = processPointMap.get(productType);
			if (processPoints == null) {
				processPoints = new ArrayList<ProcessPoint>();
				processPointMap.put(productType, processPoints);
			}

			if (!processPoints.contains(pp)) {
				processPoints.add(pp);
			}
		}
		if (errorMsgs.size() > 0) {
			setErrorMessage(toString(errorMsgs, "\n"));
		}
	}

	protected void initModelCodes() {
		List<ProductType> productTypes = getProductTypes();
		if (productTypes == null || productTypes.isEmpty()) {
			return;
		}
		for (ProductType productType : productTypes) {
			if (productType == null) {
				continue;
			}
			List<String> list = null;
			if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
				list = ProductTypeUtil.getProductSpecDao(productType).findAllModelCodes(productType.name());
			} else {
				List<? extends BaseProductSpec> specs = ProductTypeUtil.getProductSpecDao(productType).findAllProductSpecCodesOnly(productType.name());
				HashSet<String> set = new HashSet<String>();
				if (specs != null) {
					for (BaseProductSpec bps : specs) {
						set.add(bps.getProductSpecCode());
					}
				}
				list = new ArrayList<String>(set);
			}
			list = CommonUtil.removeNullItems(list);
			Collections.sort(list);
			list.add(0, null);
			getModelCodeMap().put(productType, list);
		}
	}

	protected void initNumberTypes(Map<ProductType, List<ProductNumberDef>> productNumberTypeMapping, NumberType numberType) {
		List<ProductType> productTypes = getProductTypes();
		ProductTypeDao dao = ServiceFactory.getDao(ProductTypeDao.class);
		for (ProductType pt : productTypes) {
			if (pt == null) {
				continue;
			}
			ProductTypeData data = dao.findByKey(pt.name());
			if (data == null || data.getProductNumberDefs() == null) {
				continue;
			}
			List<ProductNumberDef> defs = new ArrayList<ProductNumberDef>();
			for (ProductNumberDef pnd : data.getProductNumberDefs()) {
				if (pnd == null) {
					continue;
				}
				if (numberType.equals(pnd.getNumberType())) {
					defs.add(pnd);
				}
			}
			productNumberTypeMapping.put(pt, defs);
		}
	}

	protected void populateStationList() {

		ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
		String processType = (String) getProcessTypeComboBox().getSelectedItem();

		getProcessPointComboBox().removeAllItems();
		if (productType == null || StringUtils.isEmpty(processType)) {
			return;
		}
		List<ProcessPoint> processPoints = null;
		if (ON.equals(processType)) {
			processPoints = getOnProcessPoints().get(productType);
		} else if (OFF.equals(processType)) {
			processPoints = getOffProcessPoints().get(productType);
		}
		if (processPoints == null || processPoints.isEmpty()) {
			return;
		}
		getProcessPointComboBox().setModel(new DefaultComboBoxModel(processPoints.toArray()));
		initSelection(getProcessPointComboBox());
	}

	// === utility === //
	protected ProcessPoint geExistingHistoryProcessPoint(BaseProduct product) {
		for (int i = 0; i < getProcessPointComboBox().getItemCount(); i++) {
			ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getItemAt(i);
			if (processPoint == null) {
				continue;
			}
			String processPointId = processPoint.getProcessPointId();
			List<?> historyList = ProductTypeUtil.getProductHistoryDao(product.getProductType()).findAllByProductAndProcessPoint(product.getProductId(), processPointId);
			if (historyList != null && !historyList.isEmpty()) {
				return processPoint;
			}
		}
		return null;
	}

	protected String toString(List<String> list, String separator) {
		StringBuffer sb = new StringBuffer();
		if (list == null) {
			return sb.toString();
		}
		for (String str : list) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(str);
		}
		return sb.toString();
	}

	protected String getProductProcessName() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.capitalize(getProductTypeComboBox().getSelectedItem().toString().toLowerCase()));
		sb.append(getProcessName());
		return sb.toString();
	}

	protected String getProcessName() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.capitalize(getProcessTypeComboBox().getSelectedItem().toString().toLowerCase()));
		return sb.toString();
	}

	protected List<ProductType> getProductTypes() {
		Set<ProductType> productTypes = new TreeSet<ProductType>();
		productTypes.addAll(getOnProcessPoints().keySet());
		productTypes.addAll(getOffProcessPoints().keySet());
		List<ProductType> list = new ArrayList<ProductType>(productTypes);
		if (list.size() > 1) {
			list.add(0, null);
		}
		return list;
	}

	protected List<String> getProcessTypes(ProductType productType) {
		List<String> list = new ArrayList<String>();
		if (productType == null) {
			return list;
		}
		List<ProcessPoint> onPps = getOnProcessPoints().get(productType);
		List<ProcessPoint> offPps = getOffProcessPoints().get(productType);
		if (onPps != null && !onPps.isEmpty()) {
			list.add(ON);
		}
		if (offPps != null && !offPps.isEmpty()) {
			list.add(OFF);
		}
		return list;
	}

	protected boolean isValidInputNumber(JTextField textField, ProductType productType, NumberType numberType, List<ProductNumberDef> snDefs, List<String> masks) {
		String sn = textField.getText();
		if (isNumberBlank(productType, numberType, sn)) {
			return false;
		}
		if (!ProductNumberDef.isNumberValid(sn, snDefs)) {
			TextFieldState.ERROR.setState(textField);
			setErrorMessage(String.format("%s %s Number '%s' is invalid", productType.getProductName(), numberType, sn));
			textField.selectAll();
			return false;
		}
		if (!isNumberMatchMask(textField, productType, numberType, masks)) {
			return false;
		}		
		return true;
	}
	
	protected boolean isNumberBlank(ProductType productType, NumberType numberType, String sn) {
		if (StringUtils.isBlank(sn)) {
			setErrorMessage(String.format("%s %s Number can not be empty", productType, numberType));
			return true;
		}
		return false;
	}
	
	protected boolean isNumberMatchMask(JTextField textField, ProductType productType, NumberType numberType, List<String> masks) {
		if (masks == null || masks.isEmpty()) {
			return true;
		}
		String sn = textField.getText();
		String partMaskFormat = PropertyService.getPartMaskWildcardFormat();
		for (String mask : masks) {
			boolean match = CommonPartUtility.verification(sn, mask, partMaskFormat);
			if (match) {
				return true;
			}
		}
		TextFieldState.ERROR.setState(textField);
		setErrorMessage(String.format("%s %s Number must match mask :%s", productType, numberType, CommonPartUtility.parsePartMask(toString(masks, ","))));
		textField.selectAll();
		return false;
	}
	
	protected void initSelection(JComboBox element) {
		if (element == null) {
			return;
		}
		element.setSelectedIndex(element.getItemCount() == 1 ? 0 : -1);
		if (element.getSelectedIndex() == -1) {
			UiUtils.requestFocus(element);
		}
	}

	// === Service API === //
	protected boolean invokeService(Device device) {
		try {

			device = getPropertyBean().isMbpnProductOnServiceUsed()
					? ServiceFactory.getService(MbpnProductOnService.class).execute(device)
					: HttpDeviceInvoker.invoke(getServiceUrl(), device);

			DeviceFormat dccDeviceFormat = getReplyDeviceFormat(device, TagNames.DATA_COLLECTION_COMPLETE.name());
			Object value = dccDeviceFormat.getValue();

			int dcComplete = 0;
			if (value != null) {
				try {
					dcComplete = Integer.parseInt(value.toString().trim());
				} catch (Exception e) {
					//
				}
			}
			if (dcComplete == 1) {
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("Received from server DATA_COLLECTION_COMPLETE: ").append(dcComplete);
				DeviceFormat msgDeviceFormat = device.getReplyDeviceFormat(TagNames.ERROR_MESSAGE.name());
				if (msgDeviceFormat != null) {
					sb.append(", ERROR_MESSAGE:").append(msgDeviceFormat.getValue());
				}
				DeviceFormat excDeviceFormat = device.getReplyDeviceFormat(TagNames.EXCEPTION.name());
				if (excDeviceFormat != null) {
					sb.append(", EXCEPTION:").append(excDeviceFormat.getValue());
				}
				Logger.getLogger().error(sb.toString());
				setErrorMessage(sb.toString());
				return false;
			}
		} catch (Exception e) {
			String msg = "Exception to invoke service for device:" + device;
			Logger.getLogger().error(e, msg);
			setErrorMessage(msg + "\n" + e);
			return false;
		}
	}

	protected Device getServiceDevice(ProcessPoint processPoint) {
		String tagName = TagNames.PRODUCT_ID.name();
		Device device = findDevice(processPoint.getProcessPointId(), tagName);
		if (device == null) {
			throw new TaskException("There is no Device defined with " + tagName + " deviceFormat");
		}
		setProductIdDataFormat(device);
		setProductionLotDataFormat(device);
		setModelDataFormat(device);
		return device;
	}

	protected Device setProductIdDataFormat(Device device) {
		String tagName = TagNames.PRODUCT_ID.name();
		
		String value = productIdTextField.getText();
		
		if(StringUtils.isEmpty(value)) return device;
		
		DeviceFormat deviceFormat = getDeviceFormat(device, tagName);
		deviceFormat.setValue(value);
		return device;
	}
	
	protected Device setProductionLotDataFormat(Device device) {
		if(!preProductionLotComboBox.isEnabled()) return device;
		PreProductionLot preProdLot = (PreProductionLot)this.preProductionLotComboBox.getSelectedItem();
		if(preProdLot == null) return device;
		
		String tagName = TagNames.PRODUCTION_LOT.name();
		DeviceFormat modelDeviceFormat = getDeviceFormat(device, tagName);
		modelDeviceFormat.setValue(preProdLot.getProductionLot());
		return device;
	}

	protected Device setModelDataFormat(Device device) {
		Object modelCode = "";
		if(getModelCodeComboBox().isEnabled()) {
			modelCode = getModelCodeComboBox().getSelectedItem();
			if (modelCode == null || StringUtils.isBlank(modelCode.toString())) {
				return device;
			}
			
		}else {
			PreProductionLot preProdLot = (PreProductionLot)this.preProductionLotComboBox.getSelectedItem();
			if(preProdLot == null) return device;
			modelCode = preProdLot.getProductSpecCode();
		}
		String tagName = TagNames.PRODUCT_SPEC_CODE.name();
		if (ProductTypeUtil.isInstanceOf((ProductType) getProductTypeComboBox().getSelectedItem(), DieCast.class)) {
			tagName = TagNames.MODEL_CODE.name();
		}
		DeviceFormat modelDeviceFormat = getDeviceFormat(device, tagName);
		modelDeviceFormat.setValue(modelCode.toString().trim());
		return device;
	}

	protected DeviceFormat getDeviceFormat(Device device, String tag) {
		Logger.getLogger().info("get device format:" + device.getClientId() + " tag:" + tag);
		DeviceFormat deviceFormat = device.getDeviceFormat(tag);
		if (deviceFormat == null) {
			throw new TaskException("Invalid device configuration:" + device.getClientId() + ", missing DeviceFormat for tag:" + tag);
		}
		return deviceFormat;
	}

	protected DeviceFormat getReplyDeviceFormat(Device device, String tag) {
		Logger.getLogger().info("get reply device format:" + device.getClientId() + " tag:" + tag);
		DeviceFormat deviceFormat = device.getReplyDeviceFormat(tag);
		if (deviceFormat == null) {
			throw new TaskException("Invalid device configuration:" + device.getClientId() + ", missing ReplyDeviceFormat");
		}
		return deviceFormat;
	}

	protected List<Device> findDevices(String processPointId, String tag) {
		List<Device> list = ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(processPointId);
		List<Device> result = new ArrayList<Device>();
		if (list == null || list.isEmpty()) {
			return result;
		}
		for (Device d : list) {
			DeviceFormat deviceFormat = d.getDeviceFormat(tag);
			if (deviceFormat != null)
				result.add(d);
		}
		return result;
	}
	
	protected List<MbpnProductType> getMbpnProductTypes() {
		if(mbpnProductTypes == null)
			mbpnProductTypes = ServiceFactory.getDao(MbpnProductTypeDao.class).findAllByProductType(ProductType.MBPN.name());
		return mbpnProductTypes;
	}
	
	protected MbpnProductType findMbpnProductType(String productSpecCode) {
		String mainNo = MbpnSpecCodeUtil.getMainNo(productSpecCode);
		
		for(MbpnProductType type: getMbpnProductTypes()) {
			if(type.getId().getMainNo().equalsIgnoreCase(mainNo)) {
				return type;
			}
		}
		
		return null;
	}

	protected Device findDevice(String processPointId, String tagName) {
		List<Device> devices = findDevices(processPointId, tagName);
		for (Device eachDevice : devices) {
			DeviceFormat deviceFormat = eachDevice.getDeviceFormat(TagNames.PRODUCT_ON_DEVICE.name());
			if (deviceFormat != null)
				return eachDevice;
		}
		
		if(!devices.isEmpty())
			return devices.get(0);
		return null;
	}
	
	protected String getServiceUrl() {
		String serviceHandlerName = "HttpServiceHandler";
		String deviceHandlerName = "HttpDeviceHandler";
		String serviceUrl = getApplicationContext().getArguments().getServerURL();
		serviceUrl = serviceUrl.replace(serviceHandlerName, deviceHandlerName);
		return serviceUrl;
	}

	// === factory methods === //
	protected JLabel createTitleBar(String title) {
		JLabel label = new JLabel(title, JLabel.CENTER);
		label.setFont(Fonts.FONT_PLAIN("Arial", 36));
		return label;
	}

	protected JLabel createLabel(String text) {
		JLabel component = new JLabel(text);
		component.setFont(Fonts.DIALOG_BOLD_26);
		return component;
	}

	protected JTextField createTextField(String name) {
		JTextField component = new JTextField();
		component.setFont(new Font("Dialog", Font.BOLD, 32));
		component.setEnabled(false);
		component.setDocument(new UpperCaseDocument());
		component.setName(name);
		TextFieldState.DISABLED.setState(component);
		return component;
	}

	protected JComboBox createComboBox(String name) {
		JComboBox component = new JComboBox();
		component.setFont(Fonts.DIALOG_BOLD_26);
		component.setName(name);
		component.setEnabled(false);
		return component;
	}

	protected JCheckBox createCheckBox(String label) {
		JCheckBox component = new JCheckBox();
		component.setFont(Fonts.DIALOG_BOLD_16);
		component.setText(label);
		component.setName(label);
		component.setEnabled(false);
		return component;
	}

	protected JButton createButton(String label) {
		JButton component = new JButton(label);
		component.setFont(Fonts.DIALOG_BOLD_26);
		component.setEnabled(false);
		component.setName(label);
		return component;
	}

	// === get/set === //
	protected JComboBox getProductTypeComboBox() {
		return productTypeComboBox;
	}

	protected JComboBox getProcessTypeComboBox() {
		return processTypeComboBox;
	}

	protected JComboBox getProcessPointComboBox() {
		return processPointComboBox;
	}

	public JTextField getProductIdTextField() {
		return productIdTextField;
	}

	protected JButton getSaveButton() {
		return saveButton;
	}

	protected JButton getCancelButton() {
		return cancelButton;
	}

	protected void setPropertyBean(IProperty propertyBean) {
		this.propertyBean = propertyBean;
	}

	protected ManualOnOffPropertyBean getPropertyBean() {
		return (ManualOnOffPropertyBean) propertyBean;
	}

	protected Map<ProductType, List<ProcessPoint>> getOnProcessPoints() {
		return onProcessPoints;
	}

	protected Map<ProductType, List<ProcessPoint>> getOffProcessPoints() {
		return offProcessPoints;
	}

	protected Map<ProductType, List<ProductNumberDef>> getProductNumberTypeMap() {
		return productNumberTypeMap;
	}

	protected JComboBox getModelCodeComboBox() {
		return modelCodeComboBox;
	}

	protected Map<ProductType, Collection<String>> getModelCodeMap() {
		return modelCodeMap;
	}
	
	public String getProductId() {
		return productId;
	}
}
