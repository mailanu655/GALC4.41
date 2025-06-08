package com.honda.galc.client.datacollection.processor;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.observer.LotControlPersistenceManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.datacollection.view.ErrorDialogManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AristaRfidData;
import com.honda.galc.device.dataformat.CellBasedProductId;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdRefresh;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * <h3>Class description</h3>
 * Base class for product ID processing.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jun. 31, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150731</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public abstract class BaseProductSnProcessor extends ProcessorBase implements IProductIdProcessor{

	public static final String PROCESS_PRODUCT = "Process product:";
	public static final String MESSAGE_ID = "PRODUCT"; 
	public static final String LOT_CONTROL_RULE_NOT_DEFINED = "LOT_CONTROL_RULE_NOT_DEFINED";
	protected ProductBean product;
	protected DataCollectionState state;
	protected TerminalPropertyBean property;

	abstract public String getProductSpecCode(String productId);
	abstract protected BaseProductSpec findProductSpec(String productSpec);


	/**
	 * Constructors
	 * @param fsm
	 */
	public BaseProductSnProcessor(ClientContext context) {
		super(context);
		init();
	}

	public void init() {
		product = new ProductBean();
		property = context.getProperty();
		state = getController().getState();
	}

	public void registerDeviceListener(DeviceListener listener) {
		//Register device listener to receive product Id from PLC
		//ONLY if configured. 
		if(property.isProductIdFromPlc() || property.isProductIdRefreshFromPlc())
			registerListener(EiDevice.NAME, listener, getProcessData());
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new CellBasedProductId()); 
		list.add(new ProductIdRefresh());
		list.add(new AristaRfidData());
		return list;
	}

	public synchronized boolean execute(ProductId productId) {

		Logger.getLogger().debug("ProductIdProcessor : Enter confirmProductId");
		try {
			Logger.getLogger().info(PROCESS_PRODUCT + productId);
			confirmProductId(productId);	
			getController().getFsm().clearMessage();						
			getController().getFsm().productIdOk(product);
			Logger.getLogger().debug("ProductIdProcessor : Enter confirmProductId ok");

			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te, te.getMessage());
			if (!te.getTaskName().equals(LOT_CONTROL_RULE_NOT_DEFINED))
				product.setValidProductId(false);
			getController().getFsm().productIdNg(product, te.getTaskName(), te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			getController().getFsm().error(new Message(MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getCause().toString()));
		} 

		Logger.getLogger().debug("ProductIdProcessor : Exit confirmProductId ng");
		return false;

	}
	
	public synchronized boolean execute(CellBasedProductId productId) {
		return execute((ProductId) productId);
	} 

	public synchronized boolean execute(ProductIdRefresh productIdRefresh) {
		if(!(state instanceof ProcessProduct)) return false;
		context.getDbManager().updateNextExpectedProductId((ProcessProduct)state);
		String productId = state.getExpectedProductId();
		if(StringUtils.isEmpty(productId)) {
			getController().getFsm().error(new Message("MSG01", "Next Expected Product is null"));
			return false;
		}
		return execute(new ProductId(state.getExpectedProductId()));
	}


	protected void confirmProductId(ProductId productId)
			throws SystemException, TaskException, IOException
	{
		checkProductIdNotNull(productId);	

		//2016-02-11 - BAK - Remove "I" from product scan
		if (context.isRemoveIEnabled()) {
			if (getController().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) {				
				productId.setProductId(context.removeLeadingVinChars(productId.getProductId()));				
			}
		}	

		product.setProductId(productId.getProductId());		
		
//		checkProductIdLength();

		if (context.isPreviousLineCheckEnabled()) {
			performPreviousLineCheck();			
		}
		
		try {
			this.confirmProdId(context.isOnLine());
		
			if(context.isCheckExpectedProductId())
				checkExpectedProduct(productId);
			
			boolean checkResult = true;
			if (!getController().getClientContext().getProperty().isSkipProductChecks()) {
				checkResult = executeCheck(product.getBaseProduct(), context.getAppContext().getProcessPoint());
			}
			product.setValidProductId(checkResult);
				
			if(checkResult){
				executeProductWarnChecks(product.getBaseProduct(), context.getAppContext().getProcessPoint());
			}	
			
			this.checkProduct(context.isOnLine());
			

			if(product.getKdLotNumber() != null) {
				Logger.getLogger().info("Product KD lot number:", product.getKdLotNumber());
			}


		} catch (TaskException te) {
			throw te;
		} catch (ServiceTimeoutException se) {
			handleServerOffLineException(se);
		} catch (ServiceInvocationException sie) {
			handleServerOffLineException(sie);
		} catch (Exception e){
			throw new TaskException(e.getClass().toString(), this.getClass().getSimpleName());
		}
	}

	protected void performPreviousLineCheck() {
		if (getProductCheckUtil().invalidPreviousLineCheck()) {
			String trackingStatus = ProductTypeUtil.findProduct(context.getProperty().getProductType(), getProductCheckUtil().getProduct().getProductId()).getTrackingStatus();
			String lineName=trackingStatus==null?null:ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus).getLineName();
			String msg="Product "+getProductCheckUtil().getProduct().getProductId() +" came from an unexpected line : "+lineName;
			handleException(msg);
		}		
	}

	protected void handleServerOffLineException(ServiceException se) {
		Logger.getLogger().info(se, "Server OffLine detected.");
		setOffLine();
		this.confirmProdId(false);
		try {
			this.checkProduct(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setOnLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, true));
		context.setOnline();
	}

	protected void setOffLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}

	protected void checkProcessedProductOnLocalCache() {

		if(property.isCheckProcessedProduct())
			checkProcessedProductOnLocalCache(product.getProductId());

		//Can't check required part on local cache - incomplete data

	}

	@SuppressWarnings("unchecked")
	protected void checkProcessedProductOnLocalCache(String productId) {
		List<Integer> keys = InstalledPartCache.getInstance().getKeys();
		for(Integer key : keys){
			List<InstalledPart> installedParts = InstalledPartCache.getInstance().get(key, List.class);

			if(installedParts == null) continue;

			for(InstalledPart part : installedParts){
				if(part.getInstalledPartStatus() == InstalledPartStatus.REMOVED) continue;
				if(productId.equals(part.getId().getProductId()) && isProcessedPart(part.getId().getPartName()))
					handleException("Product:" + product.getProductId() + " already processed!");
			}
		}

	}

	protected boolean isProcessedPart(String partName) {
		List<LotControlRule> lotControlRules = state.getLotControlRules();
		for(int i = 0; i < lotControlRules.size(); i++)
		{
			LotControlRule r = lotControlRules.get(i);
			if(partName.equals(r.getPartName().getPartName()))
				return true;

		}
		return false;
	}

	protected void loadLotControlRule(){
		if(isNeedToLoadLotControlRules(product))
		{
			String productSpec = product.getProductSpec();
			SortedArrayList<LotControlRule> rules = new SortedArrayList<LotControlRule>("getSequenceNumber");
			if(!StringUtils.isBlank(productSpec)) {
				BaseProductSpec productSpecDef = findProductSpec(productSpec);
				if (productSpecDef == null) {
					throw new TaskException("No product spec definition exists for " + productSpec);
				}
				rules.addAll(getLotControlRulesForProductSpec(productSpecDef, product));
			}
			if(isModelChanged(productSpec)){
				Logger.getLogger().info("Product model changed. New product spec:" + productSpec +
						" old product spec:" + getController().getState().getProductSpecCode());
				LotControlAudioManager.getInstance().playModelChangedSound();
				state.setSpecChanged(true);
				
			}

			if ((getController().getClientContext().getProperty().isDestinationChangeAlarm()) &&
					(getController().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString()))) {
				if (isDestinationChanged(product)) {
					Logger.getLogger().info(
							"Product destination changed. New product spec:"
									+ productSpec
									+ " old product spec:"
									+ getController().getState()
									.getProductSpecCode());
					LotControlAudioManager.getInstance()
					.playDestinationChangedSound();
				}
			}

			getController().getFsm().initLotControlRules(productSpec, rules);

			if(rules.size() == 0 && !context.getProperty().getFsmType().equals(FsmType.REPAIR.toString()))
				throw new TaskException("Lot Control Rule for " + productSpec + " is not defined.", LOT_CONTROL_RULE_NOT_DEFINED);
		}
	}

	protected boolean isModelChanged(String productSpec) {
		if(property.isCheckModelChangeWithPreviousProcessed()) {
			return !StringUtils.isEmpty(getController().getState().getProductSpecCode()) && 
					!getController().getState().getProductSpecCode().equals(productSpec);
		} else {
			ProductCheckUtil util = new ProductCheckUtil(product.getBaseProduct(), context.getAppContext().getProcessPoint());
			return util.checkSpecChange();
		}
	}

	protected boolean isDestinationChanged(ProductBean product) {	
		String currentDestination = "", nextDestination = "";
		String prodSpecCode = getController().getState().getProductSpecCode();

		// Condition for the the first productId scanned when client is launched
		if (prodSpecCode == null) {
			prodSpecCode = getPreviousProductSpecCode(product.getProductId());
		}
		
		FrameSpec frameSpec;
		
		frameSpec = context.getFrameSpec(product.getProductSpec());
		if(frameSpec != null) nextDestination = frameSpec.getSalesModelTypeCode();
		
		frameSpec = context.getFrameSpec(prodSpecCode);
		if(frameSpec != null) currentDestination = frameSpec.getSalesModelTypeCode();
		
		return (!(currentDestination.equals(nextDestination)));
	}

	protected boolean isNeedToLoadLotControlRules(ProductBean product) {
		return isNeedToLoadNewRules(product) || isNeedToReloadRuleForSubId(product);
	}

	protected boolean isNeedToReloadRuleForSubId(ProductBean product) {
		return !StringUtils.isEmpty(product.getSubId());
	}

	private boolean isNeedToLoadNewRules(ProductBean product) {
		return getController().getState().getProductSpecCode() == null || 
				!getController().getState().getProductSpecCode().equals(product.getProductSpec()) ||
				getController().getState().getLotControlRules().size() == 0;
	}

	protected List<LotControlRule> getLotControlRulesForProductSpec(BaseProductSpec spec, ProductBean product){

		if(!isNeedToReloadRuleForSubId(product)){
			return LotControlPartUtil.getLotControlRuleByProductSpec(spec, context.getAllRules());
		} else {
			List<LotControlRule> appliableRules = filterLotControlRules(product.getSubId());
			return LotControlPartUtil.getLotControlRuleByProductSpec(spec, appliableRules);
		}
	}

	protected List<LotControlRule> filterLotControlRules(String subId) {
		List<LotControlRule> result = new ArrayList<LotControlRule>();
		for(LotControlRule rule : context.getAllRules()){
			if(StringUtils.isEmpty(rule.getSubId()) || subId.equals(rule.getSubId()))
				result.add(rule);
		}
		return result;
	}

	protected void checkProductIdNotNull(ProductId productid) {
		if(productid == null || productid.getProductId() == null)
			handleException("Received product Id is null!");
	}

	public void checkExpectedProduct(ProductId productId) {
		String expectedProductId = state.getExpectedProductId();
		if(StringUtils.isEmpty(expectedProductId)) {
			productId.setProductId(productId.getProductId());
			state.setExpectedProductId(productId.getProductId());
		} else if (!expectedProductId.equals(productId.getProductId())){
			LotControlAudioManager.getInstance().playNGSound();
			if(productId.isInputData()) {
				Logger.getLogger().info("Mismatched Product Id requested from Device was received.");	
				unexpectedProductReceived(productId, expectedProductId);
				return;
			}
			
			if (isProductIdAheadOfExpectedProductId(productId, expectedProductId)) {
				processProductIdAheadOfExpectedProductId(productId, expectedProductId);
			} else {
				processProductIdBehandOfExpectedProductId(productId, expectedProductId);
			}
		} 	
	}
	
	protected void processProductIdBehandOfExpectedProductId(ProductId productId, String expectedProductId) {
		if (showErrorMessageDialog("The product id entered is not the expected product id.\nDo you want to process this product id out of sequence?", false))  {
			Logger.getLogger().info(String.format("Confirmed request to process product out of sequence. Processing product id [%s] and skipping [%s].",productId.getProductId(),expectedProductId));				
			if(!authorize())
				unexpectedProductReceived(productId, expectedProductId);
			if(!property.isResetNextExpectedProduct()){
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getStateBean().setResetNextExpected(false);				
			}
			EventBus.publish(new Event(this, productId.getProductId(), EventType.CHANGED));
		} else {
			Logger.getLogger().info("Declined request to process product out of sequence.");
			unexpectedProductReceived(productId, expectedProductId);
		}
	}
	protected void processProductIdAheadOfExpectedProductId(ProductId productId, String expectedProductId) {
		if (showErrorMessageDialog("The product id entered is not the expected product id.\nDo you want to advance to this product id?",false))  {
			if(!authorize())
				unexpectedProductReceived(productId, expectedProductId);
			if(property.isCheckOutOfSequence()){
				String noGoodProductId = updateNoGoodProduct(productId);
				if(null != noGoodProductId && !noGoodProductId.equals("")) {
					String message = "Please contact TC for product ID " +noGoodProductId;
					Logger.getLogger().info("User Scanned ID: "+expectedProductId+" System found No Good ID: "+noGoodProductId);
					showErrorMessageDialog(message, true);
				}
			}
			Logger.getLogger().info(String.format("Confirmed request to process product out of sequence. Processing product id [%s] and skipping [%s].",productId.getProductId(),expectedProductId));				
			productId.setProductId(productId.getProductId());
			state.setExpectedProductId(productId.getProductId());
			EventBus.publish(new Event(this, productId.getProductId(), EventType.CHANGED));
		} else {
			Logger.getLogger().info("Declined request to process product out of sequence.");	
			unexpectedProductReceived(productId, expectedProductId);
		}
	}

	/*This method updates the installed part status to NG in gal185tbx table when Unexpected VIN is processed*/
	@SuppressWarnings("rawtypes")
	public String updateNoGoodProduct(ProductId productId){
		String currentProductId = productId.getProductId();
		int installedPartStatus = 0;
		boolean checkPrev = true;
		String noGoodProductId = "";
		BaseProduct baseProduct = null;
		BaseProductSpecDao productSpecDao = null;
		ProductSpec productSpec = null;
		List<String> installedPartNames = new ArrayList<String>();
		List<String> partNames = new ArrayList<String>();
		List<InstalledPart> installedPartForFirstPrevProduct = new ArrayList<InstalledPart>();
		// Get 1st Previous Product Id
		String firstPrevProdId = getPreviousProductId(currentProductId);
		if(checkProcessed(firstPrevProdId)){
			return noGoodProductId;
		}
		Logger.getLogger().info("First Previuos Product Id of scanned product Id [" + currentProductId + "] is "+ firstPrevProdId);
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);

		if(null != firstPrevProdId){
			// To Get product spec of 1st Previous Product Id
			baseProduct = ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findBySn(firstPrevProdId);
			productSpecDao = ProductTypeUtil.getProductSpecDao(context.getProperty().getProductType());
			productSpec =  (ProductSpec)productSpecDao.findByProductSpecCode(baseProduct.getProductSpecCode(), context.getProperty().getProductType());
			// Get all parts from Lot Control Rule for this vin (firstPrevProdId) and for this process_point_id
			List<LotControlRule> lotControlRules = lotControlRuleDao.findAllByProcessPointAndProductSpec(context.getProcessPointId().trim(), productSpec);
			// To Check part names available in gal185tbx table
			partNames = new ArrayList<String>();
			for(LotControlRule installedPart : lotControlRules){
				partNames.add(installedPart.getPartNameString());
			}
			installedPartForFirstPrevProduct = installedPartDao.findAllByProductIdAndPartNames(firstPrevProdId, partNames);
		}
		else{
			return noGoodProductId;
		}

		if((installedPartForFirstPrevProduct == null) || installedPartForFirstPrevProduct.size() == 0) {
			Logger.getLogger().info("No rows found for First Previuos Product Id [" + firstPrevProdId + "]. Looking for one more previous product.");
			while (checkPrev) {
				String nextPrevProdId = getPreviousProductId(firstPrevProdId);
				if(null != nextPrevProdId){
					// To Get product spec for Product Id  (nextPrevProdId)
					baseProduct = ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findBySn(nextPrevProdId);
					productSpecDao = ProductTypeUtil.getProductSpecDao(context.getProperty().getProductType());
					productSpec =  (ProductSpec)productSpecDao.findByProductSpecCode(baseProduct.getProductSpecCode(), context.getProperty().getProductType());
					// Get all parts from Lot Control Rule for this vin (nextPrevProdId) and for this process_point_id
					List<LotControlRule> prevLotControlRules = lotControlRuleDao.findAllByProcessPointAndProductSpec(context.getProcessPointId().trim(), productSpec);
					// To Check part names available in gal185tbx table for this vin (nextPrevProdId)
					installedPartNames = new ArrayList<String>();
					for(LotControlRule installedPart : prevLotControlRules){
						installedPartNames.add(installedPart.getPartNameString());
					}
					List<InstalledPart> installedPartForNextPrevProduct = installedPartDao.findAllByProductIdAndPartNames(nextPrevProdId, installedPartNames);
					if((installedPartForNextPrevProduct == null) || (installedPartForNextPrevProduct.size() == 0)){
						Logger.getLogger().info("No rows found for Product Id [" + nextPrevProdId + "]. Looking for one more previous product.");
						firstPrevProdId = nextPrevProdId;
						checkPrev = true;
					} else{
						Logger.getLogger().info("Rows found for Product Id [" + nextPrevProdId + "]. This is the affected Product.");
						noGoodProductId = nextPrevProdId;
						checkPrev = false;
					}
				}
				else{
					checkPrev = false;
				}

			}
			if(StringUtils.isNotEmpty(noGoodProductId)){
				installedPartDao.updateInstalledPartStatus(noGoodProductId, installedPartNames, installedPartStatus);
			}

		}
		return noGoodProductId;
	}

	/*This method gets the previous Product*/
	public InProcessProduct getPreviousProduct(String productId) {
		LotControlPersistenceManager obj = new LotControlPersistenceManager(context);
		List<InProcessProduct> prevVinList = obj.findPreviousProducts(productId);
		if(prevVinList !=null && prevVinList.size() > 0){
			InProcessProduct previousProduct = prevVinList.iterator().next();
			return previousProduct;
		}
		return null;
	}

	public String getPreviousProductId(String productId) {
		InProcessProduct prevProduct = getPreviousProduct(productId);
		if (prevProduct == null) return null;
		return prevProduct.getProductId();
	}

	public String getPreviousProductSpecCode(String productId) {
		InProcessProduct prevProduct = getPreviousProduct(productId);
		if (prevProduct == null) return null;
		return prevProduct.getProductSpecCode();
	}

	/*This method check the processing of product in 215tbx table*/
	public boolean checkProcessed(String productId){
		ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		List<ProductResult> prevProductResultList = productResultDao.findAllByProductAndProcessPoint(productId, context.getProcessPointId());
		return prevProductResultList != null && !prevProductResultList.isEmpty();
	}

	protected void unexpectedProductReceived(ProductId productId, String expectedProductId) {
		handleException(String.format("The product id [%s] was received when [%s] was expected!",productId.getProductId(),expectedProductId));
	}

	protected boolean authorize() {
		return (property.isNeedAuthorizedUserToDisableExpected()) ?
				login() : true; 
	}

	protected boolean isProductIdAheadOfExpectedProductId(ProductId productId,
			String expectedProductId) {
		return context.getDbManager().isProductIdAheadOfExpectedProductId(expectedProductId,productId.getProductId());
	}

	public void checkProcessedProduct(String productId) {
		if(context.getDbManager().isProcessed(productId, getPartNamesList())) {		
			if(!property.isResetNextExpectedProduct()){
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getStateBean().setResetNextExpected(true);				
			}
			handleException("Product:" + product.getProductId() + " already processed!");
		}
			
	}

	private List<String> getPartNamesList() {
		List<String> list = new ArrayList<String>();
		List<LotControlRule> lotControlRules = state.getLotControlRules();
		for(int i = 0; i < lotControlRules.size(); i++)
		{
			LotControlRule r = lotControlRules.get(i);
			list.add(r.getPartName().getPartName());
		}

		return list;
	}

	/**
	 * Verify product Id length.
	 * The length of a EIN is different from that of a VIN
	 * @return
	 */
	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	protected void checkProductIdLength(){
		if(product.getProductId().length() != property.getMaxProductSnLength()){
			String msg = "Invalid Product Id: " + product.getProductId() + ", length invalid.";
			handleException(msg);
		}

	}
	
	protected void confirmProdId(Boolean isOnLine) {
		if (isOnLine) {
			Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");
			BaseProduct aproduct = getProductFromServer();
			product.setProductSpec(aproduct.getProductSpecCode());
			product.setProductionLot(aproduct.getProductionLot());
			product.setSubId(aproduct.getSubId());
		} else {
			product.setProductSpec(getProductSpecCode(product.getProductId()));
			product.setSubId(getSubId());
		}
	}
	
	protected void checkProduct(Boolean isOnLine) throws Exception {
		loadLotControlRule();
		if (isOnLine) {
			validateProduct(product.getBaseProduct());
			doRequiredPartCheck();
			Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		} else if(property.isCheckProcessedProduct()) {
			checkProcessedProductOnLocalCache();
		}
	}

	protected BaseProduct getProductFromServer() {
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(product.getProductId());
		if(aproduct == null) {
			handleException("Product: " + product.getProductId() + " not exist.");
		} else {
			String prodSpec = aproduct.getProductSpecCode();
			ProductTypeData ptd = context.getAppContext().getProductTypeData();
			if (ptd != null && ptd.getProductSpecCodeDefs().size() > 0 && ProductSpecCodeDef.MODEL == ptd.getProductSpecCodeDefs().get(0)) {
				prodSpec =  ProductSpecCodeDef.MODEL_YEAR_WILDCARD + StringUtils.trimToEmpty(prodSpec);
			}
			product.setProductSpec(prodSpec);
			product.setBaseProduct(aproduct);
		}
		return aproduct;
	}

	protected void validateProduct(BaseProduct aproduct) {
		//Check hold status here if required.
		//checkFrameHoldStatus();


		if(property.isCheckProcessedProduct()) {

			//Check cache first then database
			checkProcessedProductOnLocalCache();
			checkProcessedProduct(aproduct.getProductId());
		}
		if(context.isProductLotCountExist() && (context.getProperty().getProductType().equals(ProductType.FRAME.toString()) || 
				context.getProperty().getProductType().equals(ProductType.ENGINE.toString()) || 
				ProductTypeUtil.isMbpnProduct(context.getProperty().getProductType()))&& aproduct.getProductionLot()!=null){
			context.getDbManager().setCountLotSize(state,aproduct);
		}
	}


	protected void handleException(String info) {
		throw new TaskException(info, MESSAGE_ID);
	}

	/**
	 * Check if all required part have been installed.
	 * @throws Exception 
	 */
	protected void doRequiredPartCheck() throws Exception {
		//Can't perform this task without server  
		if(context.isOnLine() && property.isCheckRequiredPart() ){

			if(checkRequiredPart()) return;

			String msg = product.getMissingRequiredPartMessage();
			if(property.isFailOnMissingRequiredPart()){
				Logger.getLogger().warn(msg, "Required part check failed.");
				throw new TaskException(msg,"checkRequiredPart");
			} else {
				getController().getFsm().message(new Message("MissingRequiredPart", msg, MessageType.ERROR));
				Logger.getLogger().warn(msg);
			}
		}
	}

	protected boolean checkRequiredPart(){

		try{
			List<String> missingRequiredParts = context.getDbManager().getMissingRequiredPart(state, product.getProductId(), product.getSubId());

			if(missingRequiredParts == null || missingRequiredParts.size() == 0) 
				return true;

			product.setMissingRequiredParts(missingRequiredParts);
			product.setMissingRequiredPart(true);
		} catch (Exception e){
			Logger.getLogger().warn(e, "Exception was thrown when checking required part.");
		}

		return false;
	}

	protected boolean executeCheck(BaseProduct product, ProcessPoint processPoint) {
		String[] checkTypes = getProductCheckPropertyBean().getProductNotProcessableCheckTypes();
		if (checkTypes == null || checkTypes.length == 0) {
			return true;
		}
		Map<String, Object> checkResults = ProductCheckUtil.check(product, processPoint, checkTypes);
		if (checkResults == null || checkResults.isEmpty()) {
			return true;
		}
		String message =  "Failed Product Checks: \n" + ProductCheckUtil.formatTxt(checkResults);
		Logger.getLogger().error(message);

		if (getProductCheckPropertyBean().isFailedAckRequired()) {
			// play NG sound
			try {
				LotControlAudioManager.getInstance().playNGSound();
			} catch (Exception e) {
				Logger.getLogger().error(e);
			}

			// prepare the failed acknowledgement message
			StringBuilder messageBuilder = new StringBuilder();
			for (String check : checkResults.keySet()) {
				Object result = checkResults.get(check);
				// add the check to the message only if it failed
				if (StringUtils.isNotBlank(ProductCheckUtil.formatTxt(result, ""))) {
					if (messageBuilder.length() != 0) {
						messageBuilder.append(", ");
					}
					messageBuilder.append(check);
				}
			}
			// show the error acknowledgement dialog
			ErrorDialogManager mgr = new ErrorDialogManager();
			mgr.showDialog(context.getFrame(), "FAILED PRODUCT CHECKS: " + messageBuilder.toString(), LotControlConstants.FAILED_PRODUCT_CHECKS_ACK, property);
		}
		handleException(message);
		return false;
	}
	
	protected void executeProductWarnChecks(BaseProduct product, ProcessPoint processPoint) {
		String[] configuredChecks = property.getProductWarnCheckTypes();
		if (configuredChecks == null ||configuredChecks.length == 0) {
			return;
		}
		Map<String, Object> checkResults = ProductCheckUtil.check(product, processPoint, configuredChecks);
		if (checkResults == null || checkResults.isEmpty()) {
			return;
		}
		String message =  "Product Check - Warning: \n" + ProductCheckUtil.formatTxt(checkResults);
		Logger.getLogger().warn(message);

		// show the warning alert
		String missingOrIncorrectParts = ProductCheckUtil.formatMissingPart(checkResults);
		MessageDialog.showColoredMessageDialog(context.getFrame(), missingOrIncorrectParts, LotControlConstants.FAILED_PRODUCT_WARN_CHECKS, JOptionPane.WARNING_MESSAGE, Color.YELLOW);
	}

	public <T extends ProductSpec> String getProductSpecCode(String productId, List<T> productSpecs) {
		for(ProductionLot prodLot: context.getProductionLots()) {
			ProductSpec productSpec = findProductSpec(productSpecs, prodLot.getProductSpecCode());

			if(productSpec == null) continue;

			if(prodLot.isInLot(productId, productSpec.getProductNoPrefixLength())) 
				return prodLot.getProductSpecCode();
		}
		throw new TaskException("Invalid product:" + productId + " is not included in production lots.");
	}

	protected String getSubId() {
		return null;
	}
	protected String getAfOnSequenceNumber() {
		return null;
	}

	protected boolean showErrorMessageDialog(String msg, boolean displayOnly) {
		if (displayOnly) {
			MessageDialog.showError(msg);
			return true;
		} else {
			if (property.isShowErrorDialog()) {
				ErrorDialogManager mgr = new ErrorDialogManager();
				String message = property.getProductIdLabel()+" ("+product.getProductId() +") scanned Out of Sequence. \n";
				String request = mgr.showDialog(context.getFrame(),  message, LotControlConstants.OUT_OF_SEQUENCE,
						property);
				return Boolean.valueOf(request);
			} else {
				return MessageDialog.confirm(context.getFrame(), msg);
			}
		}
	}

	protected ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId());
	}
	
	protected ProductCheckUtil getProductCheckUtil(){
		return  new ProductCheckUtil(getProductFromServer(), (ProcessPoint)ServiceFactory.getDao(ProcessPointDao.class).findByKey(context.getProcessPointId().trim()));
	}
}
