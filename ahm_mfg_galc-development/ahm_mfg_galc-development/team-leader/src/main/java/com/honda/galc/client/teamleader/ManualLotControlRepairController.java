package com.honda.galc.client.teamleader;

import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.Arguments;
import com.honda.galc.client.NonCachedApplicationContext;
import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.model.PartResultTableModel;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.teamleader.property.ManualOnOffPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DCZoneDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.InstalledPartHistoryDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartLinkDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.datacollection.HeadlessDataMapping;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.dto.PartHistoryDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartNameVisibleType;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartHistory;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.entity.product.PartLink;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.ProductionInfoPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.engine.EngineMarriageService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.LotControlRuleUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductResultUtil;
import com.honda.galc.util.SubproductUtil;
import com.honda.galc.util.check.PartResultData;


/**
 * 
 * <h3>ManualLotControlRepairController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairController description </p>
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
 * @author Paul Chou
 * Aug 18, 2010
 *z
 * 
 * @author Gangadhararao Gadde
 * @date Oct 04, 2016
 * ver 2
 */

public abstract class ManualLotControlRepairController<T extends BaseProduct, B extends ProductBuildResult> 
implements ActionListener, ListSelectionListener{

	protected ApplicationContext appContext;
	private ManualLotControlRepairPanel view;
	protected ManualLotControlRepairPropertyBean property;
	protected List<B> productBuildResulits;
	protected PartResult partResult;
	private int selectedIndex;
	protected IManualLtCtrResultEnterViewManager resultEnterViewManager;
	protected List<PartResult> lotControlPartResultData;
	protected List<PartResult> displayLotControlPartResultData;
	protected boolean hasDuplicateParts;
	protected BaseProductSpec productSpec;
	protected T product;
	private DeviceDao deviceDao;
	protected EngineDao engineDao = null;
	protected EngineLoadUtility engineUtil;
	
	private String subId;
	private MainWindow mainWin;
	protected List<Object[]> rulesAndProcessPoints;
	protected abstract void loadProductBuildResults();
	private ProductType productType;
	protected ProductTypeData productTypeData;
	boolean shippingAndExceptionStatusFlag;
	boolean ownerShipped;
	private String selectedDivisionId;
	private String selectedLineId;
	private String selectedStatusId;
	private String selectedZoneId;
	private Set<String> terminalProcessPoints;
	private List<Division> divisions;
	private List<Line> lines;
	private List<Zone> zones;
	private List<InstalledPartStatus> statuses;
	private List<PartHistoryDto> partHistoryList;
	private ManualLotControlRepairSubView subProductView;
	private boolean isInstalled = false;
	protected List<PartResult> resultList = null;
	protected List<? extends ProductBuildResult> parentInstalledPartList;
	private SubproductUtil subproductUtil;
	private SubproductPropertyBean subProductProperty;
	protected String subProductErrorMessage = "";
	protected boolean isRepaired = false;
	private PartSpec currentPartSpec;
	public InstalledPartDao installedPartDao;
	
	private static final String ALLOW_REPAIR_AFTER_SHIPPING = "ALLOW_REPAIR_AFTER_SHIPPING";
	private boolean setPart= false;
	private List<PartLink> partLinks;
	
	public ManualLotControlRepairController(ApplicationContext applicationContext,
			Application application, ManualLotControlRepairPanel repairPanel) 
    {
		this.appContext = applicationContext;
		this.view = repairPanel;
		
		initialize();
	}
	
	public ManualLotControlRepairController(MainWindow mainWin, ManualLotControlRepairPanel 
			repairPanel, IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		
		this(mainWin, repairPanel);
		this.resultEnterViewManager = resultEnterViewManager;
		
	}
	
	public ManualLotControlRepairController(MainWindow mainWin, ManualLotControlRepairPanel repairPanel) {
		this.mainWin = mainWin;
		this.appContext = mainWin.getApplicationContext();
		this.view = repairPanel;
		this.subProductView = getSubProductView();
		
		initialize();
	}

	private void initialize() {
		productType = view.getCurrentProductType();
		property = view.getProperty();
		productTypeData = view.getProductTypeData();
		
		lotControlPartResultData = new ArrayList<PartResult>();
		selectedLineId = property.getLineId();
		selectedDivisionId = property.getDivisionId();
		selectedZoneId = property.getZoneId();
		terminalProcessPoints = new HashSet<String>();
		hasDuplicateParts = false;
		initConnections();
		reset();
	}

	protected void reset() {
		mainWin.getStatusMessagePanel().clearErrorMessageArea();
		getView().getProductIdPanel().refresh();
		
		new PartResultTableModel(null,getColumns(), getView().getPartStatusTable());
		getView().disableButtons();
		
		getView().getProductIdField().requestFocus();
		getView().getResetButton().setEnabled(false);
		getView().getRefreshButton().setEnabled(false);
		getView().getProductIdPanel().getProductLookupButton().setEnabled(true);
		getView().getSubProductPartNameComboBox().getComponent().removeAllItems();
		getView().getFilterButton().setEnabled(false);
		getView().getSelectedDivisionLabel().setText(selectedDivisionId);
		getView().getSelectedZoneLabel().setText(selectedZoneId);
		getView().getSelectedLineLabel().setText(selectedLineId);
		enableOperationButtons(false, null);
		shippingAndExceptionStatusFlag = false;
		
		if(getView().subPanel) {
			if(getView().getResetButton().isFocusOwner() && !isInstalled){
				mainWin.dispose();
				isInstalled = true;
				if(isRepaired) {
					updateParentPartResult();
					getView().parentView.getController().updateChangedRows();
				}
			}
		} else {
			// check if the product is mbpn
			if(getView().getResetButton().isFocusOwner() && isRepaired && !isInstalled) {
				isInstalled = true;
				if(ProductTypeUtil.isMbpnProduct(product.getProductType().name())){
					updateParentPartResultNotRecursive();

				}
			}
		}
	}

	private void updateParentPartResultNotRecursive() {
		InstalledPart mbpnParentPart = findParentInstalledPart(product.getProductId(), product.getProductType().name());

		LotControlRule parentPartLotControlRule = findParentPartLotControlRule(mbpnParentPart);
		if(parentPartLotControlRule == null) {
			getLogger().info("Not able to find MBPN parent part Lot Control Rule.");
			return; 
		}

		PartSpec partSpec = findPartSpec(parentPartLotControlRule, product);
		if(partSpec == null) {
			getLogger().info("Not able to find Part Spec for ", product.getProductId());
		}

		subproductUtil = new SubproductUtil(new PartSerialNumber(product.getProductId()), parentPartLotControlRule, partSpec);
		PartResult parentPartResult = new PartResult();
		parentPartResult.setLotControlRule(parentPartLotControlRule);
		parentPartResult.setBuildResult(mbpnParentPart);
		parentPartResult.setProcessPointId(mbpnParentPart.getProcessPointId());
		boolean isRepaired = validateSubproduct(parentPartLotControlRule, parentPartResult);
		mbpnParentPart.setPartName(mbpnParentPart.getPartName());
		mbpnParentPart.setInstalledPartReason(isRepaired?InstalledPartStatus.REPAIRED.name().toLowerCase():"");
		mbpnParentPart.setInstalledPartStatus(isRepaired?InstalledPartStatus.OK:InstalledPartStatus.NG);
		ServiceFactory.getDao(InstalledPartDao.class).save(mbpnParentPart);
	}

	protected InstalledPart findParentInstalledPart(String productId, String pType) {
		List<InstalledPart> mbpnParentParts = ServiceFactory.getDao(InstalledPartDao.class).findMbpnParentInstalledPart(productId, pType);
		if(mbpnParentParts == null || mbpnParentParts.size() ==0) {
			getLogger().info("Not able to find MBPN parent installed part");
			return null;
		} else if(mbpnParentParts.size() > 1) {
			getLogger().warn("Warning: more than one parent installed parts found:" + mbpnParentParts.size());
		}

		return mbpnParentParts.get(0);
	}

	private PartSpec findPartSpec(LotControlRule rule, BaseProduct product) {
		for(PartSpec pSpec : rule.getParts()) {
			if(CommonPartUtility.verification(product.getProductId(), pSpec.getPartSerialNumberMask(),
					PropertyService.getPartMaskWildcardFormat(), product)) {
				
				if(StringUtils.trim(pSpec.getPartNumber()).equals(product.getProductSpecCode()))
					return pSpec;
			}
		}
		
		
		return null;
	}

	private LotControlRule  findParentPartLotControlRule(InstalledPart mbpnParentPart) {
		try {
			List<LotControlRule> allRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(mbpnParentPart.getProcessPointId());
			ProductTypeUtil util =ProductTypeUtil.getTypeUtil(ProductTypeCatalog.getProductType(mbpnParentPart.getProductType()));
			BaseProductSpec parentSpec = util.getProductSpecDao().findByProductId(mbpnParentPart.getProductId());
			List<LotControlRule> rules = LotControlPartUtil.getLotControlRuleByProductSpec(parentSpec, allRules);
			for(LotControlRule r : rules) {
				if(r.getPartNameString().equals(mbpnParentPart.getPartName()))
					return r;
			}
		}catch(Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}
	
	protected BaseProduct findParentProduct(BaseProduct product) {
		if(product != null && StringUtils.isNotEmpty(product.getProductId())) {
			InstalledPart pPart = findParentInstalledPart(product.getProductId(), product.getProductType().name());
			if(pPart == null) return null;
			
   	        ProductType type = ProductType.FRAME;
			if(StringUtils.isNotEmpty(pPart.getProductType())){
			   	type = ProductTypeCatalog.getProductType(pPart.getProductType());
			}
			
			ProductTypeUtil util = ProductTypeUtil.getTypeUtil(type);
			return util.getProductDao().findByKey(pPart.getProductId());			
		} 
		return null;
	}
	

	private void initConnections() {
		getView().getResetButton().addActionListener(this);
		getView().getRefreshButton().addActionListener(this);
		getView().getRemoveResultButton().addActionListener(this);
		getView().getEnterResultButton().addActionListener(this);
		getView().getMultiResultsButton().addActionListener(this);
		getView().getProductIdField().addActionListener(this);
		getView().getHistoryButton().addActionListener(this);
		
		if(getProperty().isEnableSetResultNg())
			getView().getSetResultNgButton().addActionListener(this);
		
		getView().getPartStatusTable().getSelectionModel().addListSelectionListener(this);
		getView().getSubProductPartNameComboBox().getComponent().addActionListener(this);
		getView().getFilterButton().addActionListener(this);

		getView().getSelectedDivisionLabel().setText(selectedDivisionId);
		getView().getSelectedLineLabel().setText(selectedLineId);
		getView().getSelectedZoneLabel().setText(selectedZoneId);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getView().getEnterResultButton()){
			enterResult();
		} else if(e.getSource() == getView().getMultiResultsButton()) {
			enterMultipleResults();
		} else if(e.getSource() == getView().getRemoveResultButton()){
			removePartResult();
		} else if(e.getSource() == getView().getSetResultNgButton()){
			setResultNg();
		} else if(e.getSource() == getView().getResetButton()){
			reset();
			if(view.getCurrentProductType() != null)
				EventBus.publish(new ProductProcessEvent(ProductProcessEvent.State.COMPLETE, view));
		} else if(e.getSource() == getView().getProductIdField()){
			loadProductId(false);
		} else if(e.getSource() == getView().getRefreshButton()){
			loadProductId(true);
		} else if (e.getSource() == getView().getHistoryButton()) {
			view.showPartHistoryDialog();
		}
		else if(e.getSource().equals(getView().getSubProductPartNameComboBox().getComponent())) subProductPartNameSelected();
		else if (e.getSource() == getView().getFilterButton()) {
			ManualLotControlRepairFilterDialog dialog = new ManualLotControlRepairFilterDialog(getView().getMainWindow(), this);
			dialog.setSize(850, 700);
	    	dialog.setLocationRelativeTo(getView());
	    	dialog.setVisible(true);
		}
	}	
	
	/**
	 * Description- method to load the subproduct panel
	 * 	void 
	 */
	private void loadSubProductInfo() {		
		ProductType subProductType = ProductTypeCatalog.getProductType(partResult.getLotControlRule().getPartName().getSubProductType());
		if(partResult.getBuildResult() == null) {
			ProductBuildResult buildResult = ProductTypeUtil.getTypeUtil(productType).createBuildResult(product.getProductId(),partResult.getPartName());
			partResult.setBuildResult(buildResult);
		}
		subProductView = new ManualLotControlRepairSubView(this.appContext,mainWin.getApplication(),subProductType,view);
		getSubProductView().getViewContentPanel().getProductIdField().setText(partResult.getPartSerialNumber());	
		getSubProductView().setVisible(true);
		getSubProductView().getViewContentPanel().getController().setSubProductProductTypeData(subProductType);
		getSubProductView().getViewContentPanel().getController().loadProductId(true);		
		
	}

	/**
	 * Method to load the productTypeData for the Installed Part Product
	 * 
	 * @return	 
	 */
	private void setSubProductProductTypeData(ProductType productTypeName) {		
		for(ProductTypeData type : mainWin.getApplicationContext().getProductTypeDataList()){
			if(type.getProductTypeName().equals(productTypeName.toString())){
				productTypeData = type;
				break;
			}
		}
	}

	public void setResultNg() {		
		try {
			if(isPartReadOnly())
			{
				MessageDialog.showInfo(view, "The selected part is Read Only and cannot modify the result for this Part.");
				return;
			}
			if(partResult.getBuildResult() == null) {
				ProductBuildResult buildResult = ProductTypeUtil.getTypeUtil(productType).createBuildResult(product.getProductId(),partResult.getPartName());
				partResult.setBuildResult(buildResult);
			}
			
			//set installed part status NG - currently support installed part only
			partResult.getBuildResult().setInstalledPartStatus(InstalledPartStatus.NG);
			partResult.getInstalledPart().setMeasurementsNg(getMeasurementCount(partResult));
			
			List<InstalledPart> resultList = new ArrayList<InstalledPart>();
			resultList.add((InstalledPart)partResult.getBuildResult());
			ProductResultUtil.saveAll(appContext.getApplicationId(),resultList);
			
		} catch (Exception e) {
			MessageDialog.showError(view, "Failed to delete data:" + e.getMessage());
			return;
		}

		updateCurrentRow();
		
	}

	protected PartName fetchLatestPartNameData(LotControlRule rule) {
		return ServiceFactory.getDao(PartNameDao.class).findPartNameByLotCtrRule(rule);
	}

	/**
	 * Description
	 * method to perform the following processes:
	 * checker to check the subroduct, if everything is good then update the parent product status and reason
		1. Checker for subproduct
		2. get collected data where subproduct is installed
		3. save the part
	 * 	void 
	 */
	protected void updateParentPartResult() {
		PartResult parentPartResult = getView().parentView.getController().partResult;
		LotControlRule rule = parentPartResult.getLotControlRule();
		currentPartSpec = (rule.getParts() != null && rule.getParts().size() > 0) ? rule.getParts().get(0) : null;
		subproductUtil = new SubproductUtil(new PartSerialNumber(parentPartResult.getPartSerialNumber()), rule, currentPartSpec);				
		parentInstalledPartList = getCollectedBuildResult(parentPartResult,rule);	
		
		doParentUpdate();
		if(!StringUtils.isEmpty(subProductErrorMessage))
			JOptionPane.showMessageDialog(null, subProductErrorMessage, "Sub Product Repair Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Validate and confirm the correct Subproduct
	 * Perform ProductCheck for the Subproduct if any
	 * @param rule
	 * @param part
	 */
	protected boolean validateSubproduct(LotControlRule rule, PartResult parentPartResult) {
		if (subproductUtil.isPartSubproduct()) {
			if(!verifyPartSerialNumber(parentPartResult.getInstalledPart(), rule))
				return false;
			subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, parentPartResult.getProcessPointId());
			BaseProduct subProduct = null;
			try{
				subProduct = subproductUtil.findSubproduct();
			}catch(Exception e){
				getLogger().error(e.getMessage());
			}
			if(subProduct ==null){
				subProduct = this.product;
			}
			BaseProduct parentProduct = ProductTypeUtil.getProductDao(parentPartResult.getLotControlRule().getPartName().getProductType()).findBySn(parentPartResult.getBuildResult().getProductId());
			String installProcessPoint="";
			if (subProduct == null) {
				subProductErrorMessage = "Part: " + parentPartResult.getPartName() + " not updated because invalid Subproduct";
				getLogger().info(subProductErrorMessage);
				return false;
			}

			if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct, parentProduct.getProductSpecCode())) {
				subProductErrorMessage ="Part: " + parentPartResult.getPartName() + " not updated because Spec Code of Subproduct does not match expected Spec Code.";
				getLogger().info(subProductErrorMessage);
				return false;
			} else { // this is the actual part spec 
				if(null != subproductUtil.getMatchedPartSpec()) {
					currentPartSpec = subproductUtil.getMatchedPartSpec(); 
					parentPartResult.getInstalledPart().setPartId(currentPartSpec.getId().getPartId());
				}
			}
			try {				
				try{
					if(!subProductProperty.isUseMainNoFromPartSpec())
						installProcessPoint =subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());
					else{
						installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo(subProduct.getProductSpecCode().trim()));
					}
				}catch(Exception e){
					installProcessPoint = "";
					subProductErrorMessage ="Property not define for INSTALL_PROCESS_POINT_MAP";
					getLogger().info(subProductErrorMessage);
					return false;
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
					subProductErrorMessage =msg.toString();
					getLogger().info(subProductErrorMessage);
					return false;
				}
			} catch (Exception e) {
				subProductErrorMessage ="Part: " + parentPartResult.getPartName() + " not updated because "+e.getMessage();
				getLogger().info(subProductErrorMessage);
				return false;
			}
		}	
		return true;
	}
	
	private boolean verifyPartSerialNumber(InstalledPart part, LotControlRule rule) {
		boolean result = false;
		if (part.getPartSerialNumber() == null || StringUtils.isEmpty(part.getPartSerialNumber())) {
			part.setPartSerialNumber(product.getProductId());
		}
		currentPartSpec = CommonPartUtility.verify(part.getPartSerialNumber(),
				rule.getParts(), PropertyService.getPartMaskWildcardFormat());
		if (currentPartSpec != null) {
			String parsePartSn = CommonPartUtility.parsePartSerialNumber(
					currentPartSpec, part.getPartSerialNumber());
			if (parsePartSn != null)
				part.setPartSerialNumber(parsePartSn);
			part.setPartId(currentPartSpec.getId().getPartId());
			result = true;
		} else {
			subProductErrorMessage = "Part SN: " + part.getPartSerialNumber()
					+ " mask check (" + rule.getPartMasks() + ") result: " + result;
			getLogger().info(subProductErrorMessage);
			result = false;
		}

		if (part.getInstalledPartStatus() != InstalledPartStatus.NG)
			part.setInstalledPartStatus(result ? InstalledPartStatus.OK: InstalledPartStatus.NG);

		part.setValidPartSerialNumber(result);
		
		if (rule.isUnique()) {
			List<String> verifyPartSerialNumberUniq = verifyPartSerialNumberUniq(part, rule);
			if (!verifyPartSerialNumberUniq.isEmpty()) {
				this.subProductErrorMessage = "Part SN: " + part.getPartSerialNumber()
					+ " mask check (" + rule.getPartMasks() + ") is already installed on:\n" + verifyPartSerialNumberUniq;
				getLogger().info(subProductErrorMessage);
				return false;
			}
		}
		return result;
	}
	
	private List<String> verifyPartSerialNumberUniq(InstalledPart part,	LotControlRule rule) {
		List<String> duplicatedList = new ArrayList<String>();
		
		List<InstalledPart> list = getInstalledPartDao().findAllByPartNameAndSerialNumber(part.getId().getPartName(), part.getPartSerialNumber());
		for(InstalledPart installedPart : list){
			if(!installedPart.getId().getProductId().equals(part.getId().getProductId())){
				duplicatedList.add(installedPart.getId().getProductId());
			}
		}
		
		if(duplicatedList.size() > 0){			
			subProductErrorMessage = "Part name:"+ part.getId().getPartName()+ " part Sn:"+ part.getPartSerialNumber()+
					" is alread installed on "+ duplicatedList.toString();
			getLogger().info(subProductErrorMessage);
			part.setInstalledPartStatus(InstalledPartStatus.NG);
			part.setValidPartSerialNumber(false);
		} else {
			getLogger().info("passed duplicated part check!");
		}
		return duplicatedList;
	}
	
	public InstalledPartDao getInstalledPartDao() {
		if(installedPartDao == null)
			installedPartDao = getDao(InstalledPartDao.class);

		return installedPartDao;
	}
	
	private String getMainNo(String spec){
		return MbpnDef.MAIN_NO.getValue(spec);
	}
	
	protected List<? extends ProductBuildResult> getCollectedBuildResult(PartResult parentPartResult, LotControlRule rule) {
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		if(resultList == null) {
			InstalledPart installedPart = createInstalledPart(parentPartResult,rule);
			parentPartResult.setBuildResult(installedPart);
			list.add(installedPart);
		} else {
			for(PartResult result : resultList) {
				InstalledPart installedPart = createInstalledPart(result,rule);
				result.setBuildResult(installedPart);
				list.add(installedPart);
			}
		}
		return list;
	}	
	
	protected InstalledPart createInstalledPart(PartResult parentPartResult,LotControlRule rule) {
		boolean isPartRepaired = validateSubproduct(rule, parentPartResult);
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(parentPartResult.getBuildResult().getProductId());
		id.setPartName(parentPartResult.getLotControlRule().getPartNameString());
		installedPart.setId(id);
		installedPart.setPartId(parentPartResult.getLotControlRule().getParts().get(0) == null ? "" : getInstalledPartId(parentPartResult));		
		installedPart.setInstalledPartReason(isPartRepaired?InstalledPartStatus.REPAIRED.name().toLowerCase():"");
		installedPart.setInstalledPartStatus(isPartRepaired?InstalledPartStatus.OK:InstalledPartStatus.NG);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(appContext.getUserId());
		installedPart.setPartSerialNumber(product.getProductId());
		installedPart.setProductType(property.getProductType());
		installedPart.setProcessPointId(parentPartResult.getProcessPointId());
		return installedPart;
	}

	private String getInstalledPartId(PartResult parentPartResult) {
		if(null != subproductUtil && null != subproductUtil.getMatchedPartSpec())
			return subproductUtil.getMatchedPartSpec().getId().getPartId();
		
		return parentPartResult.getLotControlRule().getParts().get(0).getId().getPartId();
	}

	protected void doParentUpdate() {
		ProductResultUtil.saveAll(appContext.getApplicationId(), (List<InstalledPart>)parentInstalledPartList);

		Logger.getLogger().info("Saved data into database by user:" +  
				System.getProperty("line.separator") + parentInstalledPartList.get(0));
	}
	
	protected void enterResult() {
		if(isPartReadOnly())
		{
			MessageDialog.showInfo(view, "The selected part is Read Only and cannot enter result for this Part.");
			return;
		}
		if(partResult.getBuildResult() != null &&
				partResult.getStatus() == InstalledPartStatus.OK &&
				isMeasurementStatusOk()){
			
			MessageDialog.showInfo(view, "The selected part installation status is ok, no need to repair.");
			Logger.getLogger().debug("The selected part installation status is ok, no need to repair dialog was displayed");
			return;
		}
		if(partResult != null && partResult.isSubProduct() && getProperty().isEnableSubProductView()
			&& (partResult.getInstalledPart() == null || !partResult.getInstalledPart().getProductId().equalsIgnoreCase(partResult.getInstalledPart().getPartSerialNumber()))) {
				loadSubProductInfo();
		}else{		
			List<PartResult> toBeFixed = new ArrayList<PartResult>();
			toBeFixed.add(partResult);
		
			try {
				resultEnterViewManager.subScreenOpen(view, toBeFixed);
			} catch (Exception e) {
				Logger.getLogger().error(e, "Exception to open data input screen.");
				mainWin.setErrorMessage(e.getMessage());
			}
		}
	}

	protected void enterMultipleResults() {
		if(partResult.getLotControlRule().getPartName().getPartVisible()==PartNameVisibleType.READ_ONLY.getId())
		{
			MessageDialog.showInfo(view, "The selected part is Read Only and cannot enter result for this Part.");
			return;
		}
		
		PartResultTableModel model = (PartResultTableModel)getView().getPartStatusTable().getModel();
		List<PartResult> allResults = model.getItems();
		List<PartResult> toBeFixed = new ArrayList<PartResult>();
		for(PartResult pr : allResults) {
			if(pr.isHeadLess() && pr.isQuickFix() && partResult.getProcessPointId() != null
					&& partResult.getProcessPointId().equalsIgnoreCase(pr.getProcessPointId()) && !InstalledPartStatus.OK.equals(pr.getStatus())) {
				toBeFixed.add(pr);
			}
		}
	
		if(partResult.getBuildResult() != null &&
				partResult.getStatus() == InstalledPartStatus.OK &&
				isMeasurementStatusOk()){
			
			MessageDialog.showInfo(view, "The selected part installation status is ok, no need to repair.");
			Logger.getLogger().debug("The selected part installation status is ok, no need to repair dialog was displayed");
			return;
		}
			
		try {
			resultEnterViewManager.subScreenOpen(view, toBeFixed);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to open data input screen.");
			mainWin.setErrorMessage(e.getMessage());
		}
	}

	protected boolean isMeasurementStatusOk() {
		return (partResult.getLotControlRule().getParts().size() == 0 ||
				partResult.getLotControlRule().getParts().get(0).getMeasurementCount() == 0 ||
				MeasurementStatus.OK == partResult.getStatusMeasure());
	}

	protected void removePartResult() {
		String productId = null;
		
		if(isPartReadOnly())
		{
			MessageDialog.showInfo(view, "The selected part is Read Only and cannot remove result for this Part.");
			return;
		}
		if(partResult.getBuildResult() == null || partResult.getBuildResult().getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)) {
			MessageDialog.showInfo(view, "No data to remove.");
			return;
		}
		
		ProductType productType = view.getCurrentProductType();
		if(productType.equals(ProductType.FRAME)||productType.equals(ProductType.ENGINE)||productType.equals(ProductType.MBPN)) {
			String partName = partResult.getPartName();
			productId = partResult.getInstalledPart().getProductId();
			partLinks = ServiceFactory.getDao(PartLinkDao.class).findAllByParentPartName(partName);
			if(!partLinks.isEmpty()) {
				StringBuilder partLinkStr = new StringBuilder();
				
				for(int i=0;i<partLinks.size();i++) {
					partLinkStr.append(partLinks.get(i).getId().getChildPartName());
					if(i!=(partLinks.size()-1))
					partLinkStr.append(", ");
				}
	
				MessageDialog.showInfo(view,"The following Parts will be marked NG. \n "+partLinkStr.toString());
			    setPart = true;
			}
		}
			
		if(!MessageDialog.confirm(view, "Confirm remove data ?")) return;
		
		try {
			deleteDefect();
			removeInstalledPart();
			
			if(setPart) {		
				for(int i=0;i<partLinks.size();i++) {
					List<InstalledPart> installedChildParts = getInstalledPartDao().findLinkedParts(partLinks.get(i).getId().getChildPartName(),productId);
					for(InstalledPart childPart: installedChildParts) {
						childPart.setInstalledPartStatus(InstalledPartStatus.NG);
						childPart.setInstalledPartReason("NG-PART LINK("+ partLinks.get(i).getId().getParentPartName()+")");
						getInstalledPartDao().update(childPart);
					}			
				}
			}
		} catch (Exception e) {
			MessageDialog.showError(view, "Failed to delete data:" + e.getMessage());
			return;
		}

		updateCurrentRow();
		
	}
	
	protected void deleteDefect() {
		//mark defect as deleted if existing
		if (partResult.getLotControlRule().isQicsDefect()) {
			List<String> productIdList = new ArrayList<String>();
			List<String> partNameList = new ArrayList<String>();
			productIdList.add(partResult.getBuildResult().getProductId());
			partNameList.add(partResult.getBuildResult().getPartName());
			List<Long> defectRefIds= ProductTypeUtil.getProductBuildResultDao(getProductType()).findDefectRefIds(productIdList, partNameList);
			try {
				ServiceFactory.getService(QiHeadlessDefectService.class).deleteDefect(QiExternalSystem.LOT_CONTROL.name(), defectRefIds.get(0));
			} catch (Exception ex) {
				Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
			}		
		}
	}

	protected void removeInstalledPart() {
		
		removeInstalledPart(partResult);
	}

	protected void removeInstalledPart(PartResult result) {
					
		if(result.getInstalledPart() == null || result.getInstalledPart().getMeasurements() == null)
			return;

		if(partResult.isSubProduct() && partResult.getLotControlRule().getPartName().getSubProductType().equals(ProductType.ENGINE.name())) {
			try {

				getEngineMarriageService().deassignEngineAndFrame(
						Arrays.asList(result.getInstalledPart()),
						appContext.getProcessPointId().toString(),
						appContext.getApplicationId().toString());
				
				Logger.getLogger().info("Frame " + result.getInstalledPart().getProductId() + " and engine " +   partResult.getBuildResult().getPartSerialNumber() + " was successfully deassigned by " + appContext.getUserId());
			} catch (EntityNotFoundException e) {
				Logger.getLogger().error(e, "Product not found in database.");
				MessageDialog.showError(view, "Could not find frame or engine in DB.");
				return;
			} catch (Exception e) {
				Logger.getLogger().error(e, "Could not deassociate engine " + partResult.getBuildResult().getPartSerialNumber() + " and frame " + result.getInstalledPart().getProductId());
				MessageDialog.showError(view, "Could not deassociate engine " + partResult.getBuildResult().getPartSerialNumber() + " and frame " + result.getInstalledPart().getProductId());
				return;
			}
		}
		
		removeMeasurementData(result);
		removeInstalledPartData(result);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				System.getProperty("line.separator") + partResult.getInstalledPart());
	}

	protected void removeInstalledPartData(PartResult result) {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		result.getInstalledPart().setPartSerialNumber(null);
		result.getInstalledPart().setInstalledPartStatus(InstalledPartStatus.REMOVED);
		result.getInstalledPart().setActualTimestamp(null);
		result.getInstalledPart().setAssociateNo(appContext.getUserId());
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		installedParts.add(result.getInstalledPart());
		installedPartDao.saveAll(installedParts);
	}

	protected void removeMeasurementData(PartResult result) {
		for(Measurement measurement : result.getInstalledPart().getMeasurements()){
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementValue(0.0);
			measurement.setPartSerialNumber(null);
			measurement.setMeasurementName(null);
			measurement.setMeasurementStringValue(null);
			measurement.setActualTimestamp(null);
			measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
		}
	}
	
	//Returns list of strings containing comma separated values for each installed part measurement
	private List<String> getPartHistoryMeasurements(List<InstalledPartHistory> iph, List<MeasurementAttempt> ma){
		
		if (ma == null || ma.isEmpty()) return new ArrayList<String>();
		
		//sort part history by timestamp
		Collections.sort(iph, new Comparator<InstalledPartHistory>() {
			public int compare(InstalledPartHistory iph1, InstalledPartHistory iph2) {
				return iph2.getId().getActualTimestamp().compareTo(iph1.getId().getActualTimestamp());
			}
		});
		
		//sort measurement attempts by timestamp
		Collections.sort(ma, new Comparator<MeasurementAttempt>() {
			public int compare(MeasurementAttempt m1, MeasurementAttempt m2) {
				return m2.getActualTimestamp().compareTo(m1.getActualTimestamp());
			}
		});
		
		//get the greatest measurement sequence number
		int maxMeasurementSequence = 0;
		for (MeasurementAttempt m : ma) {
			if (maxMeasurementSequence < m.getId().getMeasurementSequenceNumber()) {
				maxMeasurementSequence = m.getId().getMeasurementSequenceNumber();
			}
		}
		
		Map<InstalledPartHistory, Map<Integer, MeasurementAttempt>> partHistoryMap = new HashMap<>();
		for (InstalledPartHistory installedPartHistory : iph) {
			partHistoryMap.put(installedPartHistory, null);
		}
		Map<Integer, MeasurementAttempt> attempt = new HashMap<>();
		int count = 1, index = 0;
		for (int i = 0; i < ma.size() + 1; i++) {
			if (count > maxMeasurementSequence || i == ma.size()) {
				partHistoryMap.put(iph.get(index), attempt);
				index++;
				attempt = new HashMap<>();
				count = 1;
			}
			if (index == iph.size()) break;
			
			int maSequence = ma.get(i).getId().getMeasurementSequenceNumber();
			if (!attempt.containsKey(maSequence)) {
				attempt.put(maSequence, ma.get(i));
				count++;
			}
		}
		
		//Generate Part Measurement results by iterating through installed parts and adding the measurement results by sequence number
		List<String> partMeasurements = new ArrayList<String>();
		for (InstalledPartHistory installedPartHistory : iph) {
			String measurements = "";
			MeasurementAttempt previousAttempt = null;
			for (MeasurementAttempt currentAttempt : partHistoryMap.get(installedPartHistory).values()) {
				if (previousAttempt == null) {
					previousAttempt = currentAttempt;
					continue;
				}
				if (currentAttempt.getId().getMeasurementSequenceNumber() == previousAttempt.getId().getMeasurementSequenceNumber()) {
					previousAttempt = currentAttempt;
				} else {
					measurements += previousAttempt.getMeasurementValue() + ",";
					previousAttempt = currentAttempt;
				}
			}
			if (previousAttempt != null)
				measurements += previousAttempt.getMeasurementValue();
			partMeasurements.add(measurements);
		}
		return partMeasurements;
	}
		
	protected PartHistoryDto setPartHistoryDto(InstalledPartHistory iph, String measurements) {
		PartHistoryDto partHistoryDto = new PartHistoryDto();
		partHistoryDto.setStatus(InstalledPartStatus.getType(iph.getInstalledPartStatusId()));
		partHistoryDto.setProductId(iph.getId().getProductId());
		partHistoryDto.setPartName(iph.getId().getPartName());
		partHistoryDto.setPartSn(iph.getPartSerialNumber());
		partHistoryDto.setMeasurements(measurements);
		partHistoryDto.setProcessPointId(iph.getProcessPointId());
		partHistoryDto.setTimestamp(iph.getId().getActualTimestamp());
		partHistoryDto.setAssociateNo(iph.getAssociateNo());
		return partHistoryDto;
	}
		
	protected List<PartHistoryDto> loadPartHistoryData() {
		try {
			partHistoryList = new ArrayList<PartHistoryDto>();
			PartResultTableModel model = (PartResultTableModel)getView().getPartStatusTable().getModel();
	        partResult = model.getItem(selectedIndex);
	        String productId = view.getProductIdField().getText();
			List<InstalledPartHistory> iph = ServiceFactory.getDao(InstalledPartHistoryDao.class).findAllByProductIdAndOperationName(productId, partResult.getPartName());
			
			Collections.sort(iph, new Comparator<InstalledPartHistory>() {
				public int compare(InstalledPartHistory m1, InstalledPartHistory m2) {
					return m1.getCreateTimestamp().compareTo(m2.getCreateTimestamp());
				}
			});
			
			if (iph.size() < 1) return null;
			
			List<MeasurementAttempt> ma = ServiceFactory.getDao(MeasurementAttemptDao.class).findAllByProductIdAndOperationName(productId, iph.get(0).getId().getPartName());
			List<String> measurements = getPartHistoryMeasurements(iph, ma);
 			
	 		for (int i = iph.size() - 1; i >= 0; i--) {
	 			if (iph.get(i).getInstalledPartStatusId() == -9)
	 				partHistoryList.add(setPartHistoryDto(iph.get(i), ""));
	 			else
	 				partHistoryList.add(setPartHistoryDto(iph.get(i), measurements.isEmpty() ? "" : measurements.get(i)));
	 		}
	 		return partHistoryList;
		} catch (Exception e) { 
			MessageDialog.showError(view, "Failed to Generate History for Part: "+partResult.getPartName());
			e.printStackTrace(); 
		}
		return null;
	}
	
	public void loadProductId(boolean refresh) {
		try {
			
			//clear errors
			if(!ownerShipped) {
				mainWin.clearMessage();
			}
			view.getProductIdPanel().getProductLookupButton().setEnabled(false);
			
			String productId = getView().getProductIdField().getText();
			
			if(view.isRemoveIEnabled()){
				productId = removeLeadingVinChars(productId);
			}
			if(StringUtils.isEmpty(productId) && getView().subPanel) {
				isRepaired = true;
				return;
			}
			
			if(!property.isProductIdCheckDisabled()) {
				checkProductId(productId);
			}
			
			product = checkProductOnServer(productId);
			if(product == null){
				if(getProperty().isInvokeOnProcess()){
					launchOnApplication(findOnApplicationId(), productId);
					return;
				} else 
					view.getProductIdField().requestFocus();
					view.getProductIdPanel().getProductLookupButton().setEnabled(true);
					throw new TaskException("Invalid product:" + productId);
			}
			
			getView().getProductIdField().setText(product.getProductId());
			view.setCursor(new Cursor(Cursor.WAIT_CURSOR));	
			hasDuplicateParts = false;
			loadProductBuildResultStatus(refresh);
			
			if(ownerShipped || checkShippedAndException(product)) {
				shippingAndExceptionStatusFlag = true;
				view.getRemoveResultButton().setEnabled(false);
				view.getEnterResultButton().setEnabled(false);
				view.getMultiResultsButton().setEnabled(false);
				isRepaired=false;
				if(view.getCurrentProductType() != null) {
				List<ProductTypeData> datas = getDao(ProductTypeDao.class).findAllByOwnerProduct(productTypeData.getProductTypeName());
				for(ProductTypeData productType : datas)
					EventBus.publish(new ShipScrapEvent(productType.getProductType(), true));
				}
			} 
			
			
			if (view.getCurrentProductType() != null) {
					EventBus.publish(new ProductProcessEvent(ProductProcessEvent.State.VALID_PRODUCT, view));
				getView().mclrSubProducts();
			}
			getView().getProductIdField().setStatus(true);
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			
			isInstalled = false;
			
			
		} catch (TaskException e) {
			setError(e.getMessage());
			Logger.getLogger().warn(e.getMessage());
			
			getView().getProductIdField().setStatus(false);
			if (view.getCurrentProductType() != null)
				EventBus.publish(new ProductProcessEvent(ProductProcessEvent.State.ERROR, view));
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String findOnApplicationId() {
		Map<String, String> onApplicationIds = getProperty().getOnRecoveryApplicationIds();
		if(onApplicationIds == null || onApplicationIds.get(getProductType().name()) == null){
			throw new TaskException("Invadlid configuration for invoke Product On process.");
		}

		//get the map key string in the format:<PRODUCT_TYPE>{_<DIVISION_ID>}{_<LINE_ID>}
		StringBuilder keyBuilder = new StringBuilder(getProductType().name());
		if(!StringUtils.isEmpty(getProperty().getDivisionId()))
			keyBuilder.append(Delimiter.UNDERSCORE).append(getProperty().getDivisionId());
		if(!StringUtils.isEmpty(getProperty().getLineId()))
			keyBuilder.append(Delimiter.UNDERSCORE).append(getProperty().getLineId());

		return onApplicationIds.get(keyBuilder.toString());
	}

	
	private void launchOnApplication(String appId, String productId) {
		try{
			if(StringUtils.isEmpty(StringUtils.trimToEmpty(productId)) || StringUtils.isEmpty(appId)) 
			{
				getLogger().warn("Incomplete information to launch On application:", "application Id:", appId, "product Id:", productId);
				return;
			}
			
			Application application = appContext.getApplication(appId);
			if(application == null) {
				getLogger().warn("Invalid configuration: Can not find On application - application Id:", appId);
				return;
			} 

			MainWindow clientScreen = (MainWindow)createtClientMainWindow(application);
			Class<?>	clientScreenClass = clientScreen.getClass();
			
			clientScreen.setVisible(true);
			getLogger().info("Opened window " ,application.getApplicationId() + ":" + clientScreenClass.getSimpleName());
			clientScreen.repaint();
			
			if(isManuallOnOff(clientScreenClass)){
				initManuallOnOff(productId, (ManualProductOnOffView)clientScreen);
				
			}
			
		}catch(Exception e){
			Throwable throwable = e;
			if(e instanceof InvocationTargetException) throwable = e.getCause();
			String message = "Unable to open window "+ appId +" due to " + throwable.toString();
			throw new SystemException(message,throwable);
		}

	}

	@SuppressWarnings("unchecked")
	private MainWindow createtClientMainWindow(Application application) throws Exception {
		Class[] constructParamCls = new Class[2];
		constructParamCls[0] = ApplicationContext.class;
		constructParamCls[1] = Application.class;
		Object[] constructParamObj = new Object[2];
		
		Arguments args = appContext.getArguments().copy();
		args.getAdditionalParams().put("-closeAfterSave", "TRUE");
		ApplicationContext newAppContext = NonCachedApplicationContext.create(args, application.getApplicationId());
		newAppContext.setUserId(appContext.getUserId()==null?"":appContext.getUserId());
		
		constructParamObj[0] = newAppContext;
		constructParamObj[1] = application;

		Class<?>	clientScreenClass = Class.forName(application.getScreenClass());
		Constructor clientScreenConstructor = clientScreenClass.getConstructor(constructParamCls);

		return (MainWindow)clientScreenConstructor.newInstance(constructParamObj);
	}

	private void initManuallOnOff(String productId, ManualProductOnOffView view) {
		
		//If find Product Id mask then set the product spec
		ProductIdMask prodIdMask = deduceModelCodeFromDatabase(view, productId);
		if(prodIdMask != null){
			
			ComboBoxModel model = view.getProcessPointComboBox().getModel();
			if(model.getSize() > 1) {
				for(int i=0; i < model.getSize(); i++){
					ProcessPoint pp = (ProcessPoint)(model.getElementAt(i));
					if(prodIdMask.getId().getProcessPointId().equalsIgnoreCase(pp.getProcessPointId()))
						view.getProcessPointComboBox().setSelectedIndex(i);
				}
			}
								
			if(!StringUtils.isBlank(prodIdMask.getProductSpecCode())){
				ComboBoxModel model2 = view.getModelCodeComboBox().getModel();
				for(int k=0; k < model2.getSize(); k++){
					if(prodIdMask.getProductSpecCode().equals(StringUtils.trim((String)model2.getElementAt(k))))
						view.getModelCodeComboBox().setSelectedIndex(k);
				}
			}
		}			
		view.getProductIdTextField().setText(productId);
		view.getProductIdTextField().postActionEvent();
	}

	private ProductIdMask deduceModelCodeFromDatabase(ManualProductOnOffView onOffView, String productId) {
		ManualOnOffPropertyBean propertyBean = onOffView.getPropertyBean();
		String[] onProcessPoints = propertyBean.getOnProcessPoints();
		if(onProcessPoints != null && onProcessPoints.length > 0 ){
			List<ProductIdMask> onPpidMasks = ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProcessPointIds(Arrays.asList(onProcessPoints));
			for(ProductIdMask prdIdMsk : onPpidMasks){
				if(CommonPartUtility.verification(productId, prdIdMsk.getId().getProductIdMask(), PropertyService.getPartMaskWildcardFormat()))
					return prdIdMsk;
			}
		}
		
		return null;
	}

	private boolean isManuallOnOff(Class<?> clientScreenClass) {
		return ManualProductOnOffView.class.isAssignableFrom(clientScreenClass);
	}

	protected boolean checkShippedAndException(BaseProduct product){
		String lastPassingProcessPointId = product.getLastPassingProcessPointId();
		List<String> shippingProcessPointIds = getShippingProcessPointIds(product);
		List<ExceptionalOut> exceptionalList = getDao(ExceptionalOutDao.class).findAllByProductId(product.getProductId());
		boolean canRepairAfterShipping = PropertyService.getProperty(this.getAppContext().getTerminal().getHostName(), ALLOW_REPAIR_AFTER_SHIPPING, "FALSE").equalsIgnoreCase("TRUE");
		if(shippingProcessPointIds != null && shippingProcessPointIds.contains(lastPassingProcessPointId) && !canRepairAfterShipping){
			view.getMainWindow().setWarningMessage("The "+ productTypeData.getProductIdLabel()+" "+ product.getProductId() +" is Shipped");
			return true;
		}
		else if (exceptionalList != null && !(exceptionalList.isEmpty())) {
			view.getMainWindow().setWarningMessage("This "+ productTypeData.getProductIdLabel()+" "+ product.getProductId() +" is either Scrapped or Exceptional out");
			return true;
		}
		return false;
	}
	
	protected List<String> getShippingProcessPointIds(BaseProduct product) {
		return  getShippingProcessPointIds(product.getLastPassingProcessPointId(),productTypeData.getProductTypeName());
	}
	protected List<String> getShippingProcessPointIds(String lastPassingProcessPointId, String productType){
		ProductionInfoPropertyBean property = PropertyService.getPropertyBean(ProductionInfoPropertyBean.class);
		
		if(property == null || property.getShippedPpIds(String[].class) == null) {
			return null;
		} else {
			String[] shippingProcessPointIds = property.getShippedPpIds(String[].class).get(productType);
			
			return (shippingProcessPointIds == null || shippingProcessPointIds.length == 0) ? null : Arrays.asList(shippingProcessPointIds);
		}
	}
	

	@SuppressWarnings("unchecked")
	protected T checkProductOnServer(String productId) {
		try {
			
			ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(getProductType());
			return (T)productDao.findByKey(productId);
		} catch (Exception e) {
			String msg = "failed to load " + getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
		
	}
	
	protected void loadProductBuildResultStatus(boolean refresh) {

		// Lot control rules are loaded in
		// 1: first time to load lot control rules
		// 2: product spec code changed
		if (isNewProductSpec(product)) {
			productSpec = loadProductSpec(product);

			if (productSpec == null)
				throw new TaskException("Product Spec is not defined:"+ product.getProductSpecCode());

			loadLotControlRules();
		} else {
			if (!StringUtils.isEmpty(product.getSubId()) && !refresh && isNewSubId(product)) {
				subId = product.getSubId();
				assembleLotControl();
			}
			cleanInstalledPartData();
		}
		
		loadProductBuildResults();
		renderProductBuildResult();
	}

	protected void renderProductBuildResult() {
		if(hasDuplicateParts){
			filterPartResultListByStatus(displayLotControlPartResultData);
			new PartResultTableModel(displayLotControlPartResultData,getColumns(), view.getPartStatusTable());
		}else{
			filterPartResultListByStatus(lotControlPartResultData);
			new PartResultTableModel(lotControlPartResultData,getColumns(), view.getPartStatusTable());
		}
		
		renderProductSpecField();
		renderSeqField();
		enableButtonsAfterRenderBuildResults();
	}

	protected void filterPartResultListByStatus(List<PartResult> partResultList){
		List<String> statusIds = CommonUtil.splitStringList(StringUtils.trimToEmpty(getSelectedStatusId()));
		if (!statusIds.isEmpty()){
			List<PartResult> filterOutList = new ArrayList<PartResult>();
			for (PartResult pr : partResultList){
				if (!statusIds.contains(StringUtils.trimToEmpty(pr.getStatus().toString()))) {
					filterOutList.add(pr);
				}
			}
			partResultList.removeAll(filterOutList);
		}
	}
	
	protected void enableButtonsAfterRenderBuildResults() {
		view.getResetButton().setEnabled(true);
		view.getRefreshButton().setEnabled(true);
		if(property.isLcRepairFilterEnabled())	view.getFilterButton().setEnabled(true);
	}

	protected void renderProductSpecField() {
		view.getProductSpecField().setText(getProductSpecCode(product));
	}
	
	protected void renderSeqField() {
		Integer seq = null;
		if(product instanceof Frame){
			seq = ((Frame)product).getAfOnSequenceNumber();
		}
		view.getSeqField().setText(seq == null?"":seq.toString());
		view.getSeqField().setVisible(true);
		view.getProductIdPanel().getSeqLabel().setVisible(true);
	}
	
	/**
	 * Default implementation for Engine, Frame and Knucles
	 * @param product
	 */
	protected void loadInstalledParts(List<InstalledPart> installedPartList) {
		
		getMeasurementsForInstalledParts(installedPartList);
		addInstalledPartResult(installedPartList);
		
	}
	
	protected void getMeasurementsForInstalledParts(List<InstalledPart> installedParts) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		List<Measurement> allMeasurements = measurementDao.findAllByProductId(product.getProductId());
		for(InstalledPart part : installedParts){
			List<Measurement> measuremnts = new ArrayList<Measurement>();
			for(Measurement measurement : allMeasurements){
				if(part.getId().getPartName().equals(measurement.getId().getPartName()))
					measuremnts.add(measurement);
			}
			part.setMeasurements(measuremnts);
		}
		
	}



	@SuppressWarnings("unchecked")
	protected BaseProductSpec loadProductSpec(BaseProduct product) {
		BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(productTypeData.getProductType());
		return productSpecDao.findByProductSpecCode(getProductSpecCode(product), getProductType().name());
	}

	private String getProductSpecCode(BaseProduct product) {
		return product.getProductSpecCode();
	}

	private boolean isNewSubId(BaseProduct product) {
		if(product.getSubId() == null) return false;
		return !product.getSubId().equals(subId);
	}

	private boolean isNewProductSpec(BaseProduct product) {
		if(productSpec == null) return true;
		return !product.getProductSpecCode().equals(productSpec.getProductSpecCode());
	}

	@SuppressWarnings("unchecked")
	protected void loadLotControlRules() {
		
		LotControlRuleId ruleId = new LotControlRuleId((ProductSpec)productSpec);
		
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		rulesAndProcessPoints = lotControlRuleDao.findAllRulesAndProcessPointsAssemble(ruleId, productTypeData.getProductTypeName());
		
		assembleLotControl();
		
		Logger.getLogger().debug("number of part:" + lotControlPartResultData.size());
		
	}

	private List<Object[]> addDeviceDrivenRulesAndProcessPoints() {
		List<Object[]> deviceDrivenRulesAndProcessPoints = new ArrayList<Object[]>();
		HeadLessPropertyBean headlessProperty = null;
		List<ProcessPoint> headlessProcessPointList = ServiceFactory.getDao(ProcessPointDao.class).findDeviceDrivenHeadlessProcessPoints();
		for(ProcessPoint pp : headlessProcessPointList){
			headlessProperty=PropertyService.getPropertyBean(HeadLessPropertyBean.class, pp.getProcessPointId()); 
			if(!headlessProperty.getProductType().equals(productType.name()) || headlessProperty.isLotControl()) continue;
			if((headlessProperty.isDeviceDriven() || 
					headlessProperty.getPlcDataMapping().equals(HeadlessDataMapping.MAP_BY_PART_NAME.name()))){
				
				List<Device> deviceList = getDeviceDao().findAllInputDeviceByProcessPointId(pp.getProcessPointId());
				if(deviceList == null || deviceList.size() == 0) return deviceDrivenRulesAndProcessPoints;
				
				for(Device device : deviceList){
					List<LotControlRule> deduceLotControlRules = LotControlRuleUtil.deduceLotControlRules(device, productType.name(), getProductName(headlessProperty.getProducts()), Logger.getLogger());
					if(deduceLotControlRules != null && deduceLotControlRules.size() > 0)
						for(LotControlRule r : deduceLotControlRules){
							deviceDrivenRulesAndProcessPoints.add(new Object[]{r, pp});
						}
				}
			}		
		}
		
		return deviceDrivenRulesAndProcessPoints;
		
		
	}

	private String getProductName(String products) {
		if(!StringUtils.isEmpty(products)) {
			List<String> list = CommonUtil.toList(products);
			if(list.size() > 0) return list.get(0);
		}
			
		return StringUtils.EMPTY;
	}

	private DeviceDao getDeviceDao() {
		if(deviceDao == null)
			deviceDao = ServiceFactory.getDao(DeviceDao.class);
			
		return deviceDao;
	}

	protected void assembleLotControl() {
		
		lotControlPartResultData.clear();
		String requiredParts=getRequiredPartLists();
		filterRules(rulesAndProcessPoints,requiredParts);			
	
		
		for(PartResult partResult : lotControlPartResultData){
			
			if(partResult.getLotControlRule().getPartByProductSpecs().size() == 0) continue;
			List<LotControlRule> theMostMatchedRules = LotControlPartUtil.getLotControlRuleByProductSpec(productSpec, partResult.getLotControlRules());
			if(theMostMatchedRules.size() > 0 ){
				partResult.setLotControlRule(theMostMatchedRules.get(0));
			}
		}
		
		//Repair Device Driven Data 
		if(property.isRepairDeviceDrivenData()) {
			List<Object[]> drivenRulesAndProcessPoints = addDeviceDrivenRulesAndProcessPoints();
			if(drivenRulesAndProcessPoints.size() > 0){
				filterRules(drivenRulesAndProcessPoints,requiredParts);
				getRulesAndProcessPoints().addAll(drivenRulesAndProcessPoints);
			}
		}
	}

	private void filterRules(List<Object[]> rulesAndPpIds, String requiredParts) {
		
		if(rulesAndPpIds == null || rulesAndPpIds.size() == 0) return;
		
		if (getTerminalProcessPoints().isEmpty()) {
			loadTerminalProcessPoints();
		}
		
		List<String> divisionIds = CommonUtil.splitStringList(StringUtils.trimToEmpty((getSelectedDivisionId())));
		List<String> lineIds = CommonUtil.splitStringList(StringUtils.trimToEmpty(getSelectedLineId()));
		List<String> processPointsFromZones = getProcessPointsFromZones(getSelectedZoneId());
		
		for(Object[] objects : rulesAndPpIds){
			LotControlRule rule = (LotControlRule)objects[0];
			
			if(ruleFilterOutCriteria(rule)) continue;
			if(filterOutNoScanNoMeasurementRules(rule)) continue;
			
			ProcessPoint processPoint = (ProcessPoint) objects[1];
			if (!divisionIds.isEmpty() && !divisionIds.contains(processPoint.getDivisionId())) {
				continue;
			}

			if (!lineIds.isEmpty() && !lineIds.contains(processPoint.getLineId())) {
				continue;
			}
			
			if (!StringUtils.isEmpty(getSelectedZoneId()) && !processPointsFromZones.contains(processPoint.getProcessPointId())) {
				continue;
			}
			
			if(!StringUtils.isEmpty(property.getFilterByInstalledParts()) && !CommonUtil.isInList(((ProcessPoint)objects[1]).getProcessPointId(), property.getFilterByInstalledParts()))
				continue;//filter out installed process point
			
			if(property.getFilterByRequiredParts().length>0 && !CommonUtil.isInList(rule.getPartName().getPartName().toString(), requiredParts))
				continue;//filter out required parts

			if(rule.getPartName().getPartVisible()==PartNameVisibleType.HIDDEN.getId())
				continue;

			PartResult partResult = findByLotControlRule(rule);
			if (partResult == null) {
				boolean headless = !getTerminalProcessPoints().contains(rule.getId().getProcessPointId());
				lotControlPartResultData.add(new PartResult(rule, (ProcessPoint) objects[1], headless));
			} else {
				partResult.add(rule);
			}
		}
	}

	protected boolean ruleFilterOutCriteria(LotControlRule rule) {
		return !StringUtils.isEmpty(rule.getSubId()) && !rule.getSubId().equals(product.getSubId());
	}
	
	private String getRequiredPartLists(){
		StringBuilder requiredParts=new StringBuilder();
		if(property.getFilterByRequiredParts().length>0){
			boolean firstString=true;				
			for(String requiredPartProcessPoint :property.getFilterByRequiredParts()){
				if(isValidProcesPoint(requiredPartProcessPoint)){
					List<RequiredPart> requiredPartsList = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPointAndProdSpec(requiredPartProcessPoint,productSpec);
					for(RequiredPart rp:requiredPartsList){
						if(!firstString){
							requiredParts.append(",");
						}
						requiredParts.append(rp.getId().getPartName());
						firstString=false;
						}
					}
				}
			}
		return requiredParts.toString();
	}
	
	private boolean isValidProcesPoint(String processPoint){
		if(StringUtils.isEmpty(processPoint)) return false;
		ProcessPoint pp=ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint);
		return pp==null?false:true;
	}
	
	protected void assembleLotControlDiecast() {
		Set<String> processPoints = new HashSet<String>();
		for(Object[] objects : rulesAndProcessPoints){
			ProcessPoint processPoint = (ProcessPoint)objects[1];
			if(!processPoints.contains(processPoint.getProcessPointId())){
				processPoints.add(processPoint.getProcessPointId());
				String partName = getPartNameProperty(processPoint);
				if(!StringUtils.isEmpty(partName))
					lotControlPartResultData.add(new PartResult(createDummyRule(partName, processPoint.getProcessPointId()), processPoint));
			}
		}
	}
	
	/**
	 * Create a dummy lot control rule for the Head/Block part overral status
	 * @param processPointId 
	 * @param processPoint
	 * @return
	 */
	private LotControlRule createDummyRule(String partName, String processPointId) {
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId((ProductSpec)productSpec);
		id.setProcessPointId(processPointId);
		id.setPartName(partName);
		rule.setId(id);
		
		return rule;
	}

	private String getPartNameProperty(ProcessPoint processPoint) {
		HeadLessPropertyBean bean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPoint.getProcessPointId());
		return bean.getOverallStatusPartName();
	}
	

	private void cleanInstalledPartData() {
		partResult = null;
		for(PartResult result: lotControlPartResultData){
			result.setBuildResult(null);
		}
		
	}

	private PartResult findByLotControlRule(LotControlRule r) {
		for(PartResult result : lotControlPartResultData)
			if(result.getProcessPointId().equals(r.getId().getProcessPointId()) &&
					result.getPartName().equals(r.getId().getPartName()))
				return result;

		return null;
	}

	

	protected void addInstalledPartResult(List<InstalledPart> installedParts) {
		List<PartResult> filterOutList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){
			
			if(partResult.isPartMark() ||
				LotControlPartUtil.isExcludedToSave(partResult.getPartName(), getExcludedToSaveParts())){
				
				filterOutList.add(partResult);
				continue;
			}
			
			InstalledPart installedPart = getInstalledPart(partResult.getLotControlRule(), installedParts);
			partResult.setBuildResult(installedPart);
		}
		
		lotControlPartResultData.removeAll(filterOutList);
		handleDuplicateParts();
		if(isLimitRulesByDivision()){
			filterToLimitByDivision();
		}
		
	}
	
	protected void loadProductBuildResults(List<? extends ProductBuildResult> productBuildResulits) {
		List<PartResult> filterOutList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){
			
			if(LotControlPartUtil.isExcludedToSave(partResult.getPartName(), getExcludedToSaveParts())){
				
				filterOutList.add(partResult);
				continue;
			}
			
			ProductBuildResult installedPart = getProductBuildResult(partResult.getLotControlRule(), productBuildResulits);
			partResult.setBuildResult(installedPart);
		}
		
		lotControlPartResultData.removeAll(filterOutList);
		handleDuplicateParts();
		if(isLimitRulesByDivision()){
			filterToLimitByDivision();
		}
	}
	
	private void filterToLimitByDivision() {
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		List<PartResult> resultList = new ArrayList<PartResult>();
		List<PartResultData> partResultData = productCheckUtil.installedPartCheck(null, false,false);
		boolean isMissingAndRequired= false;
		for(PartResult partResult: lotControlPartResultData){
			isMissingAndRequired= false;
			for(PartResultData resultData: partResultData){
				if(partResult.getPartName().trim().equalsIgnoreCase(resultData.part_Name.trim())){
					isMissingAndRequired= true;
					break;
				}
			}
			
			if(!isMissingAndRequired && !partResultData.isEmpty()){
				if(partResult.getStatus() == InstalledPartStatus.NC ){
					resultList.add(partResult);
				}
			}
		}
		
		if(resultList.size() > 0 ){
			if(hasDuplicateParts) displayLotControlPartResultData.removeAll(resultList);
			else lotControlPartResultData.removeAll(resultList);
		}
	}

	private boolean isLimitRulesByDivision() {
		return PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false);
	}

	private ProductBuildResult getProductBuildResult(LotControlRule lotControlRule,
			List<? extends ProductBuildResult> productBuildResulits) {
		
		for(ProductBuildResult part : productBuildResulits){

			if(part.getPartName().trim().equalsIgnoreCase(lotControlRule.getId().getPartName().trim())){
						return part;
			}

		}
		return null;
	}

	private String getExcludedToSaveParts() {
		return PropertyService.getProperty("Default_LotControl", "EXCLUDE_PARTS_TO_SAVE");
	}

	private InstalledPart getInstalledPart(LotControlRule lotControlRule, List<InstalledPart> installedParts) {
		for(InstalledPart part : installedParts){

			if(part.getId().getPartName().trim().equalsIgnoreCase(lotControlRule.getId().getPartName().trim())){
				return part;
			}
		}
		return null;
	}

	protected void checkProductId(String productId) {
		if(!productTypeData.isNumberValid(productId)){	
			view.getProductIdPanel().getProductLookupButton().setEnabled(true);
			throw new TaskException("Invalid product id length:" + productId.length());
		}
	}
	
	private void setError(String msg) {
		mainWin.getStatusMessagePanel().setErrorMessageArea(msg);
	}

	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()){
			if(!shippingAndExceptionStatusFlag)
			enableOperationButtons(getProperty().isRepairEnabled(), null);
			return;
		}
		
    	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    	if (!lsm.isSelectionEmpty()) {
            selectedIndex = lsm.getMinSelectionIndex();
            PartResultTableModel model = (PartResultTableModel)getView().getPartStatusTable().getModel();
            partResult = model.getItem(selectedIndex);
            Logger.getLogger().debug("Part Result: "+partResult.getPartName()+" is selected");
            if(!shippingAndExceptionStatusFlag)
            enableOperationButtons(getProperty().isRepairEnabled(), partResult);
        }
	}
	
	protected void enableOperationButtons(boolean enabled, PartResult selectedResult) {
		getView().getRemoveResultButton().setEnabled(enabled);
		getView().getHistoryButton().setEnabled(enabled);
		getView().getEnterResultButton().setEnabled(enabled);
		boolean enableMultiResultButton = false;
		if(enabled && selectedResult != null) {
			enableMultiResultButton = getProperty().isMultipleRepairEnabled() && selectedResult.isHeadLess() && selectedResult.isQuickFix();
		}
		getView().getMultiResultsButton().setEnabled(enableMultiResultButton);
		if(property.isEnableSetResultNg()) view.getSetResultNgButton().setEnabled(enabled);	
	}
	
	public void updateCurrentRow(){
		PartResultTableModel model = (PartResultTableModel)view.getPartStatusTable().getModel();
		model.fireTableRowsUpdated(selectedIndex , selectedIndex);
		isRepaired = true;
	}
	
	public void updateChangedRows() {
		((AbstractTableModel) view.getPartStatusTable().getModel()).fireTableDataChanged();
		isRepaired = true;
	}
	
	protected void loadTerminalProcessPoints() {
		TerminalDao terminalDao = ServiceFactory.getService(TerminalDao.class);
		List<Terminal> terminals = terminalDao.findAllLotControlTerminals();
		getTerminalProcessPoints().clear();
		for (Terminal t : terminals) {
			getTerminalProcessPoints().add(t.getLocatedProcessPointId());
		}
	}

	//Getter & Setters
	public ApplicationContext getAppContext() {
		return appContext;
	}

	public ManualLotControlRepairPanel getView() {
		return view;
	}
	
	public ManualLotControlRepairSubView getSubProductView() {
		return subProductView;
	}

	public BaseProductSpec getProductSpec() {
		return productSpec;
	}
	
	public ProductType getProductType() {
		if(productType == null){
			productType = ProductTypeCatalog.getProductType(property.getProductType());
		}
		return productType;
	}
	
	public String getProductTypeString(){
		return getProperty().getProductType();
		
	}
	

	public ManualLotControlRepairPropertyBean getProperty() {
		return property;
	}
	
	public void subProductPartNameSelected() {
		InstalledPart installedPart = (InstalledPart) getView()
				.getSubProductPartNameComboBox().getComponent()
				.getSelectedItem();
		if (installedPart == null)
			return;
		ProductProcessEvent productProcessEvent = new ProductProcessEvent(
				installedPart.getPartSerialNumber(), productType, ProductProcessEvent.State.LOAD);
		EventBus.publish(productProcessEvent);
	}
	
	private List<Object[]> getRulesAndProcessPoints() {
		if(rulesAndProcessPoints == null)
			rulesAndProcessPoints = new ArrayList<Object[]>();
		return rulesAndProcessPoints;
	}
	
	public String getSelectedDivisionId() {
		return selectedDivisionId;
	}
		
	public String getSelectedLineId() {
		return selectedLineId;
	}
	
	public void setSelectedDivisionId(String selectedDivisionId) {
		this.selectedDivisionId = selectedDivisionId;
	}
	
	public void setSelectedLineId(String selectedLineId) {
		this.selectedLineId = selectedLineId;
	}

	public String getSelectedStatusId() {
		return selectedStatusId;
	}

	public void setSelectedStatusId(String selectedStatusId) {
		this.selectedStatusId = selectedStatusId;
	}
	
	public String getSelectedZoneId() {
		return selectedZoneId;
	}

	public void setSelectedZoneId(String selectedZoneId) {
		this.selectedZoneId = selectedZoneId;
	}
	
	private List<String> getProcessPointsFromZones(String zone) {
		List<String> zones = CommonUtil.splitStringList(StringUtils.trimToEmpty(zone));
		List<String> processPoints = new ArrayList<String>();
		for (String z: zones) {
			List<DCZone> dcZones = ServiceFactory.getDao(DCZoneDao.class).findAllByZoneId(z);
			for (DCZone dcZone : dcZones)
				processPoints.add(dcZone.getProcessPointId());	
		}
		return processPoints;
	}

	public void filterPartResults() {
		String division = view.getSelectedDivisionLabel().getText();
		String line = view.getSelectedLineLabel().getText();
		String zone = view.getSelectedZoneLabel().getText();
		String status = view.getSelectedStatusLabel().getText();
		
		if (!StringUtils.equals(division, getSelectedDivisionId()) || (!StringUtils.equals(line, getSelectedLineId()))
				|| !StringUtils.equals(status, getSelectedStatusId()) || !StringUtils.equals(zone, getSelectedZoneId())
				) {
			Logger.getLogger().info(" filtering Part results by: " + division + " , " + line + " , " + zone + " and " + status);
			setSelectedDivisionId(division);
			setSelectedLineId(line);
			setSelectedZoneId(zone);
			setSelectedStatusId(status);
			assembleLotControl();
			loadProductId(true);
		}
	}
	
	protected int getMeasurementCount(PartResult pr) {
		if(pr.getLotControlRule() != null && pr.getLotControlRule().getParts() != null && pr.getLotControlRule().getParts().size() > 0){
			return pr.getLotControlRule().getParts().get(0).getMeasurementCount();
		}
		
		return 0;
	};

	protected Set<String> getTerminalProcessPoints() {
		return terminalProcessPoints;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	
	public List<Zone> getZones() {
		return this.zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
	
	public List<InstalledPartStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<InstalledPartStatus> statuses) {
		this.statuses = statuses;
	}

	protected String[] getColumns(){
		
		String[] columns = property.getMlcrColumns();
		
		return columns;
	}
	
	public String removeLeadingVinChars(String productId){
		String leadingVinChars = property.getLeadingVinCharsToRemove();
		if(StringUtils.isNotBlank(leadingVinChars)){
		String[] vinChars = leadingVinChars.trim().split(",");
		
			for(String c:vinChars){
				if (productId.toUpperCase().startsWith(c)) {
					return productId.substring(c.length());
				}
			}
		}
		return productId;
	}
	
	protected void handleDuplicateParts(){
		Map<String,PartResult> mapByPartNameInstalledPart = new HashMap<String, PartResult>();
		List<PartResult> duplicatePartList = new ArrayList<PartResult>();
		for(PartResult partResult: lotControlPartResultData){
			if(mapByPartNameInstalledPart.keySet().contains(partResult.getPartName())){
				ProductBuildResult installedPart = partResult.getBuildResult();
				if((partResult.getProcessPointId() != null  && installedPart != null && installedPart.getProcessPointId() != null )&& 
						(partResult.getProcessPointId().equalsIgnoreCase(installedPart.getProcessPointId()))){
					duplicatePartList.add(mapByPartNameInstalledPart.get(partResult.getPartName()));
					mapByPartNameInstalledPart.put(partResult.getPartName(), partResult);
				}else{
					duplicatePartList.add(partResult); 
				}
			}else{	
				mapByPartNameInstalledPart.put(partResult.getPartName(), partResult);
			}
		}
		
		if(duplicatePartList.size() > 0){
			displayLotControlPartResultData = new ArrayList<PartResult>();
			displayLotControlPartResultData.addAll(lotControlPartResultData);
			displayLotControlPartResultData.removeAll(duplicatePartList);
			hasDuplicateParts = true;
		}
		
	}
	
	public boolean isPartReadOnly()
	{
		return (partResult.getLotControlRule().getPartName().getPartVisible()==PartNameVisibleType.READ_ONLY.getId());
	}

	protected boolean filterOutNoScanNoMeasurementRules(LotControlRule rule) {
		return rule.isNoScan() && rule.getParts().size() > 0 && rule.getParts().get(0).getMeasurementCount() == 0;
	}
	
	public EngineDao getEngineDao() {
		return (engineDao == null) ? engineDao = ServiceFactory.getDao(EngineDao.class) : engineDao;
	}
	
	protected EngineLoadUtility getEngineLoadUtil() {
		return (engineUtil == null) ? engineUtil = new EngineLoadUtility() : engineUtil;
	}
	
	public EngineMarriageService getEngineMarriageService() {
		return ServiceFactory.getService(EngineMarriageService.class);
	}
}
