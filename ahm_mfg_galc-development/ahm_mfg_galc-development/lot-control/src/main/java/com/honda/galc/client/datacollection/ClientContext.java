package com.honda.galc.client.datacollection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.CellAction;
import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.observer.ILotControlDbManager;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MissionSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ProductionLotHelper;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.net.Request;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * <h3>ClientContext</h3>
 * <h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep 29, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
/** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ClientContext extends PersistentCache{
	private ApplicationContext appContext;
	private TerminalPropertyBean property;
	private boolean onLine = true; //server Online
	private boolean userOverrideOnline = true; //online mode set by user
	private Map<String, String> observers;
	private JFrame frame;
	private ILotControlDbManager dbManager;
	private boolean disabledExpectedProductCheck;
	private boolean remake;
	private boolean manualRefresh = false;
	public enum Observers{DB_MANAGER, DEVICE_MANAGER, AUDIO_MANAGER, VIEW_MANAGER};
	private PreProductionLot currentPreProductionLot;
	private BuildAttributeCache buildAttributeCache;
	private boolean partLot;
	private boolean qicsSupport;
	private IViewObserver viewManager;
	private FsmType fsmType;
	private boolean removeI=false;
	private CellAction cellAction;
	private FrameLinePropertyBean frameLinePropertyBean;


	public ViewManagerBase getCurrentViewManager() {
		//any view manager must extends view manager base
		if(viewManager == null)
			createViewManager();
		return (ViewManagerBase)viewManager;
	}
	
	public void setViewManager(IViewObserver mgr){
		viewManager = mgr;
	}
	
	public ClientContext() {
	}
	
	public ClientContext(ApplicationContext appContext) {
		this(appContext, null);
	}

	public ClientContext(ApplicationContext appContext, JFrame frame) {
		super();
		this.frame = frame;
		this.appContext = appContext;
		initialize();
	}

	private void initialize() {
		AnnotationProcessor.process(this);
		//Get properties
		getApplicationProperty();

		observers = property.getObservers();


		//retrieve all the lot control rules from server
		loadDataFromServer();
		
		//check if the rule has scan type of PART_LOT, PART_MASK, PROD_LOT, KD_LOT
		checkPartLotControl();
		
		//2016-02-11 - BAK - See if "I" needs to be removed from product scan
		ProductPropertyBean prodProp = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId());
		this.removeI = prodProp.isRemoveIEnabled();		
	}

	private void checkPartLotControl() {
		for(LotControlRule rule : getLotControlRules()) {
			if(rule.isPartLotScan()||rule.isPartMaskScan()||rule.isProdLotScan()||rule.isKdLotScan())
				partLot = true;
		}

	}
	private void loadDataFromServer() {
		if(ServiceFactory.isServerAvailable()) {

			EventBus.publish(new ProgressEvent(20, "Loading lot control rules..."));
			loadLotControlRules();

			EventBus.publish(new ProgressEvent(30, "Loading production lot ..."));
			loadProductionLots();

			EventBus.publish(new ProgressEvent(40, "Loading Product Specs ..."));
			loadProductSpecs();

			//ONLY load for knuckles 
			EventBus.publish(new ProgressEvent(60, "Loading Product Specific properties ..."));
			loadBuildAttrAndSubProductLots();
		}
	}

	private void loadBuildAttrAndSubProductLots() {
		if(property.getProductType().equals(ProductType.KNUCKLE.toString())){
			if(!isOnLine()) return;
			loadKnucklesSideBuildAttributes();
			SubProductLotCache.getInstance().initSubProductionLots(dbManager.getUnSentPreproductionLot(),getBuildAttributeCache());
		}
	}

	private void loadProductSpecs() {
		switch (getProductType()) {
		case FRAME :
			loadFrameSpecs();
			break;
		case ENGINE :
			loadEngineSpecs();
			break;
		case MISSION :
			loadMissionSpecs();
			break;
		default:
			loadProductSpecCodes();
		}
	}


	public ProductType getProductType() {
		return ProductTypeCatalog.getProductType(getProperty().getProductType());
		
	}
	
	private void loadFrameSpecs() {
		Set<String> modelYearCodes = findModelYearCodes();
		
		for(String modelYearCode : modelYearCodes) {
			loadFrameSpecs(modelYearCode);
		}
	}
	
	private Set<String> findModelYearCodes() {
		Set<String> modelYearCodes = new HashSet<String>();
		
		for(ProductionLot lot : getProductionLots()) {
			modelYearCodes.add(ProductSpecUtil.extractModelYearCode(lot.getProductSpecCode()));
		}
		
		return modelYearCodes;
	}

	private List<FrameSpec> loadFrameSpecs(String modelYearCode) {
		List<FrameSpec> frameSpecs = getDbManager().loadFrameSpecs(modelYearCode);

		List<FrameSpec> allFrameSpecs = getFrameSpecs();

		if(frameSpecs != null) {
			setOnLine(true);
			allFrameSpecs.addAll(frameSpecs);
			put("FrameSpecs",allFrameSpecs);
		}
		
		return allFrameSpecs;
	}

	@SuppressWarnings("unchecked")
	private void loadMissionSpecs() {
		List<MissionSpec> missionSpecs = (List<MissionSpec>) ProductTypeUtil.MISSION.getProductSpecDao().findAll();

		if(missionSpecs != null) {
			setOnLine(true);
			put("MissionSpecs", missionSpecs);
		}
	}

	private void loadProductSpecCodes() {
		List<ProductSpecCode> specs =  ServiceFactory.getDao(ProductSpecCodeDao.class).findAll();

		if(specs != null) {
			setOnLine(true);
			put("ProductSpecCodes", specs);
		}
	}

	private void loadKnucklesSideBuildAttributes() {
		getBuildAttributeCache().loadAttribute(BuildAttributeTag.KNUCKLE_LEFT_SIDE);
		getBuildAttributeCache().loadAttribute(BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
	}

	private void loadProductionLots(){
		List<ProductionLot> prodLots = getDbManager().loadProductionLots();
		if(prodLots != null){
			setOnLine(true);
			put("ProductionLots",prodLots);
		}
	}

	private void loadEngineSpecs(){	
		List<EngineSpec> engineSpecs = getDbManager().loadEngineSpecs();

		if(engineSpecs != null) {
			setOnLine(true);
			put("EngineSpecs",engineSpecs);
		}
	}

	private void getApplicationProperty() {
		property = PropertyService.getPropertyBean(TerminalPropertyBean.class, appContext.getApplicationId());
	}

	private void loadLotControlRules(){
		List<LotControlRule> rules = getDbManager().loadLotControlRules(appContext.getApplicationId());

		if(rules != null) {
			setOnLine(true);
			put(getLotControlRulesKey(), rules);
		}
	}

	public List<LotControlRule> getAllRules() {
		return getLotControlRules();
	}

	private List<LotControlRule> getLotControlRules() {
		return getList(getLotControlRulesKey(), LotControlRule.class);
	}

	public List<ProductionLot> getProductionLots(){
		return getList("ProductionLots",ProductionLot.class);
	}

	public List<EngineSpec> getEngineSpecs(){
		return getList("EngineSpecs",EngineSpec.class);
	}

	public List<MissionSpec> getMissionSpecs(){
		return getList("MissionSpecs", MissionSpec.class);
	}

	public List<ProductSpecCode> getProductSpecCodes(){
		return getList("ProductSpecCodes", ProductSpecCode.class);
	}

	public TerminalPropertyBean getProperty() {
		return property;
	}

	public void setProperty(TerminalPropertyBean property) {
		this.property = property;
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean online) {
		this.onLine = online;
	}

	public void setOffline() {
		setOnLine(false);
	}

	public void setOnline() {
		setOnLine(true);
	}

	public ILotControlDbManager getDbManager() {
		if(dbManager == null){
			String dbManagerName = observers.get(Observers.DB_MANAGER.toString());
			dbManager = (ILotControlDbManager)createObserver(dbManagerName);
		}
		return dbManager;
	}

	@SuppressWarnings("unchecked")
	public IObserver createObserver(String observerName) {
		Class claz;
		try {
			claz = Class.forName(observerName);
			Class[] parameterTypes = {ClientContext.class};
			Object[] parameters = {this};
			Constructor constructor = claz.getConstructor(parameterTypes);
			return (IObserver) constructor.newInstance(parameters);
		} catch (Throwable e) {
			throw new TaskException("Failed to create observer:" + observerName);
		}
	}

	public void update(LotControlRule lotControlRule) {
		List<LotControlRule> lotControlRules = getLotControlRules();
		LotControlRule rule = findLotControlRuleOnList(lotControlRule, lotControlRules);

		if(rule == null)
			lotControlRules.add(lotControlRule);

		put(getLotControlRulesKey(), lotControlRules);

	}

	private LotControlRule findLotControlRuleOnList(LotControlRule lotControlRule, List<LotControlRule> lotControlRules) { 
		for(LotControlRule rule : lotControlRules){
			if(rule.getId().equals(lotControlRule.getId()))
				return rule;
		}
		return null;
	}

	public void remove(LotControlRule lotControlRule) {
		List<LotControlRule> lotControlRules = getLotControlRules();
		for(LotControlRule rule : lotControlRules){
			if(rule.getId().equals(lotControlRule.getId()))
				lotControlRules.remove(rule);
		}

		put("LotControlRules",lotControlRules);
	}
	
	@EventTopicSubscriber(topic="IPropertyNotification")
	public void onPropertyChangedEvent(String event, Request request) {
        try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
   }

	public void execute(ComponentProperty componentProperty) {
		getApplicationProperty();
	}

	public void refresh(){
		loadBuildAttrAndSubProductLots();
	}
	
	public List<FrameSpec> getFrameSpecs() {
		return getCachedFrameSpecs();
	}

	private List<FrameSpec> getCachedFrameSpecs() {
		return getList("FrameSpecs",FrameSpec.class);
	}
	
	
	public FrameSpec getFrameSpec(String productSpecCode) {
		List<FrameSpec> frameSpecs = getCachedFrameSpecs();
		
		FrameSpec frameSpec = findFrameSpec(frameSpecs, productSpecCode);
		if(frameSpec == null) {
			frameSpecs = loadFrameSpecs(ProductSpecUtil.extractModelYearCode(productSpecCode));
			return findFrameSpec(frameSpecs, productSpecCode);
		}
		
		return frameSpec;
		
	}
	
	private FrameSpec findFrameSpec(List<FrameSpec> frameSpecs, String productSpecCode) {
		for(FrameSpec frameSpec :frameSpecs) {
			if(frameSpec.getProductSpecCode().equalsIgnoreCase(productSpecCode)) return frameSpec;
		}
		
		return null;
		
	}
	
	public List<Mbpn> getMbpnSpecs() {
		return getList("MbpnSpecs",Mbpn.class);
	}

	public JFrame getFrame() {
		return frame;
	}

	public String getUserId(){
		return StringUtils.isEmpty(appContext.getUserId()) ? 
				appContext.getApplicationId(): appContext.getUserId();
	}

	public Map<String, String> getObservers() {
		return observers;
	}

	public void setObservers(Map<String, String> observers) {
		this.observers = observers;
	}


	public boolean isDisabledExpectedProductCheck() {
		return disabledExpectedProductCheck;
	}

	public void setDisabledExpectedProductCheck(boolean disabledExpectedProductCheck) {
		this.disabledExpectedProductCheck = disabledExpectedProductCheck;
	}

	public boolean isCheckExpectedProductId() {
		return getProperty().isCheckExpectedProductId()  && !isDisabledExpectedProductCheck();
	}

	public boolean isAutoProcessExpectedProduct() {
		return getProperty().isAutoProcessExpectedProduct();
	}

	public boolean isAfOnSeqNumExist() {
		return getProperty().isAfOnSeqNumExist();
	}
	
	public boolean isProductLotCountExist() {
		return getProperty().isProductLotCountExist();
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}

	public boolean isUserOverrideOnline() {
		return userOverrideOnline;
	}

	public void setUserOverrideOnline(boolean overrideOnline) {
		this.userOverrideOnline = overrideOnline;
	}

	public boolean isOnlineMode(){
		return isOnLine() && isUserOverrideOnline();
	}

	public PreProductionLot getCurrentPreProductionLot() {
		if(currentPreProductionLot == null){
			currentPreProductionLot = getDbManager().findCurrentPreProductionLot(property.getProcessLocation());
		} 

		return currentPreProductionLot;
	}

	public void setCurrentPreProductionLot(PreProductionLot preproductionLot) {
		currentPreProductionLot = preproductionLot;
	}

	public boolean isRemake() {
		return remake;
	}

	public void setRemake(boolean remake) {
		this.remake = remake;
	}


	public ProductionLot findKnuckleProductLot(SubProduct remakeProduct) {
		for(ProductionLot lot : getProductionLots()){
			if(remakeProduct.getProductSpecCode().equals(lot.getProductSpecCode())){
				if(ProductionLotHelper.isInLot(lot, remakeProduct, property.getSnDigits()))
					return lot;
			}
		}
		return null;
	}

	public BuildAttribute getBuildAttribute(String productSpecCode, String attribute) {
		return getBuildAttributeCache().findById(productSpecCode, attribute);
	}

	public BuildAttribute findBuildAttributeByValue(String attributeValue) {

		for(BuildAttribute attribute : getBuildAttributeCache().getEntityList()){
			if(attribute.getAttributeValue().equals(attributeValue))
				return attribute;
		}

		return null;
	}

	public String getKdLot() {
		return getCurrentPreProductionLot() == null ? null : getCurrentPreProductionLot().getKdLot();
	}

	public String getProductionLot() {
		return getCurrentPreProductionLot() == null ? null : getCurrentPreProductionLot().getProductionLot();
	}

	public boolean isManualRefresh() {
		return manualRefresh;
	}

	public void setManualRefresh(boolean manualRefresh) {
		this.manualRefresh = manualRefresh;
	}

	public boolean isTrimProductId() {
		return property.isTrimProductId();
	}
	public boolean isPartLot() {
		return partLot;
	}

	private String getLotControlRulesKey() {
		return "LotControlRules" + "." + appContext.getApplicationId();
	}

	public boolean isQicsSupport() {
		return qicsSupport;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(appContext.getApplicationId());
	}

	public void setQicsSupport(boolean qicsSupport) {
		this.qicsSupport = qicsSupport;
	}

	public IViewObserver createViewManager() {
		if (viewManager == null) {
			String viewManagerClass = getViewManagerClass();

			if (StringUtils.isEmpty(viewManagerClass)) {
				Logger.getLogger().error(
						"Mandatory View Manager class is not configured!");
				// We are not return here, so
				// let exception throw later, view manager is mandatory for lot
				// control client
			}
			viewManager = (IViewObserver) createObserver(viewManagerClass);
		}
		return viewManager;
	}

	public String getViewManagerClass(){

		try {
			String viewManagerClass = PropertyService.getProperty(getProcessPointId(), Observers.VIEW_MANAGER.name());

			if (viewManagerClass == null) {
				viewManagerClass = property.getViewManager();
			}

			return viewManagerClass;

		} catch (Exception e) {
			Logger.getLogger().info(e, "Exception to get View Manager class.");
		}

		return null;
	}


	public FsmType getFsmType() {
		return fsmType;
	}


	public void setFsmType(FsmType fsmType) {
		this.fsmType = fsmType;
	}

	public boolean isPreviousLineCheckEnabled() {
		return getProperty().isPreviousLineCheckEnabled();
	}
	
	public boolean isRemoveIEnabled() {
		return this.removeI;
	}
	
	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
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
	
	private BuildAttributeCache getBuildAttributeCache() {
		if (buildAttributeCache == null) {
			buildAttributeCache = new BuildAttributeCache();
		}
		return buildAttributeCache;
	}

	public CellAction getCellAction() {
		if(cellAction == null)
			cellAction = CellAction.valueOf(getProperty().getCellAction());
		
		return cellAction;
	}
	
	public FrameLinePropertyBean getFrameLinePropertyBean() {
		if(frameLinePropertyBean == null)
			frameLinePropertyBean=PropertyService.getPropertyBean(FrameLinePropertyBean.class, getProcessPointId());
		return frameLinePropertyBean;
	}
	
	
}
