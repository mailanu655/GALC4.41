package com.honda.galc.client.datacollection.observer;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProductLot;
import com.honda.galc.property.ExpectedProductPropertyBean;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;

/**
 * 
 * <h3>LotControlPersistenceBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlPersistenceBase description </p>
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
 * Mar 25, 2010
 *
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class LotControlPersistenceBase extends DataCollectionObserverBase 
implements ILotControlDbManager{
	protected ClientContext context;
	protected IExpectedProductManager expectedProductManger;
	
	public LotControlPersistenceBase(ClientContext context) {
		super();
		this.context = context;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
	}
	
	public ProductionLot getProductionLot(String productId){
		Frame frame = null;
		ProductionLot productionLot = null;
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		frame = frameDao.findByKey(productId);
		if(frame!= null  && frame.getProductionLot()!= null){
			ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
			productionLot = productionLotDao.findByKey(frame.getProductionLot().toString());
		}
		return productionLot;
	}
	
	public InProcessProduct findInProcessProductByKey(String productId){
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		InProcessProduct inProcessProduct = inProcessProductDao.findByKey(productId);
		return inProcessProduct;
	}
	
	public InProcessProduct findInProcessProductByNextProductId(String nextProductId){
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		InProcessProduct inProcessProduct = null;
		List<InProcessProduct> inProcessProducts = inProcessProductDao.findByNextProductId(nextProductId);
		if (inProcessProducts != null && !inProcessProducts.isEmpty()) {
			inProcessProduct = inProcessProducts.get(0);
		}
		return inProcessProduct;
	}
	
	public List<InProcessProduct> findPreviousProducts(String productId){
		List<InProcessProduct> previousProducts = new ArrayList<InProcessProduct>();
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		previousProducts = inProcessProductDao.findByNextProductId(productId);
		return previousProducts;
	}
	
	public ProductionLot getProductionLotByKey(String prodLot){
		ProductionLot productionLot = null;
		ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		productionLot = productionLotDao.findByKey(prodLot);
		return productionLot;
	}
	
	public List<LotControlRule> loadLotControlRules(String processPointId){
		List<LotControlRule> allRules;
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		try{
			allRules = lotControlRuleDao.findAllByProcessPoint(processPointId);
		}catch(Exception e){
			Logger.getLogger().warn(e, "Cannot load Lot control rules. Server is not available. Using cache instead!");
			return null;
		}
		return allRules;
	}

	public List<EngineSpec> loadEngineSpecs() {
		List<EngineSpec> engineSpecs;
		try{
			engineSpecs = getDao(EngineSpecDao.class).findAllProductSpecCodesOnly(context.getProperty().getProductType());
		}catch(Exception e){
			Logger.getLogger().warn(e, "failed to load Engine Spec.");
			return null;
		}
		return engineSpecs;
	}
	
	public List<FrameSpec> loadFrameSpecs(String modelYearCode){
		List<FrameSpec> frameSpecs;
		FrameSpecDao frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		try{
			frameSpecs = frameSpecDao.findAllByModelYearCode(modelYearCode);
		}catch(Exception e){
			Logger.getLogger().warn(e, "failed to load Frame Spec for model year " + modelYearCode);
			return null;
		}
		return frameSpecs;
	}
	
	public BaseProduct findProductBySn(String productId) {
		String productType = context.getProperty().getProductType();
		BaseProduct product = ProductTypeUtil.getProductDao(productType).findBySn(productId);
		return product;
	}
	
	public Frame findFrameByAfOnSequenceNumber(int afOnSequenceNumber) {
		Frame frame = null;
		List<Frame> frames = ServiceFactory.getDao(FrameDao.class).findByAfOnSequenceNumber(afOnSequenceNumber, PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getAppContext().getApplicationId()).getAfOnProcessPointId());
		if (frames != null && !frames.isEmpty()) {
			if (frames.size() == 1) {
				frame = frames.get(0);
			} else {
				Collections.sort(frames, new Comparator<Frame>() {
					public int compare(Frame o1, Frame o2) {
						Date d1 = (o1.getUpdateTimestamp() == null ? o1.getCreateTimestamp() : o1.getUpdateTimestamp());
						Date d2 = (o2.getUpdateTimestamp() == null ? o2.getCreateTimestamp() : o2.getUpdateTimestamp());
						return d2.compareTo(d1);
					}
				});
				frame = frames.get(0);
			}
		}
		return frame;
	}
	
	public List<ProductionLot> loadProductionLots() {
		List<ProductionLot> prodLots = new ArrayList<ProductionLot>();
		ProductionLotDao productionLotDao = getDao(ProductionLotDao.class);
		
		long count =  productionLotDao.count();
		int pageSize = 20000;
		for(int i = 0; i < count ; i += pageSize) {
			try{
				prodLots.addAll(productionLotDao.findAll(context.getProperty().getProcessLocation(), i, pageSize));
			}catch(Exception e){
				Logger.getLogger().warn(e, "Failed to load production lots.");
				return null;
			}
		}
		return prodLots;
	}
	
	public List<SubProductLot> loadSubProductLots(){
		List<SubProductLot> subProductLots = new ArrayList<SubProductLot>();
		SubProductDao subProductDao = getDao(SubProductDao.class);

		try{
			subProductLots.addAll(subProductDao.findProductionLots());
		}catch(Exception e){
			Logger.getLogger().warn(e, "Failed to load build attributes.");
			return null;
		}
		return subProductLots;
	}

	public List<InstalledPart> findDuplicatePartsByPartName(String partName, String partNumber) {
		return getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partName, partNumber);
	}
	
	public List<InstalledPart> findDuplicatePartsByProductId(String productId, String partNumber) {
		return getDao(InstalledPartDao.class).findAllByProductIdAndPartSerialNo(productId, partNumber);
	}
	
	public boolean isProcessed(String productId, List<String> partNames) {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		return installedPartDao.isProcessed(productId, partNames);
	}
	
	public synchronized void saveProperty(ComponentProperty property) {
		ComponentPropertyDao propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		propertyDao.save(property);
	}
	
	public synchronized void deleteProperty(ComponentProperty property) {
		ComponentPropertyDao propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		propertyDao.remove(property);
	}
	
	public synchronized void saveProperties(List<ComponentProperty> properties) {
		ComponentPropertyDao propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		propertyDao.saveAll(properties);
	}
	
	protected void setOnLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, true));
		context.setOnline();
	}
	
	protected void setOffLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}
	
	protected void setOnline(boolean online) {
		context.setOnLine(online);
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, online));
	}
	
	public List<String> getMissingRequiredPart(DataCollectionState state, String productId, String subId) {
		RequiredPartDao dao = ServiceFactory.getDao(RequiredPartDao.class);
		return dao.findMissingRequiredParts(state.getProductSpecCode(),context.getProcessPointId(), 
				ProductTypeCatalog.getProductType(context.getProperty().getProductType()), productId, subId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IExpectedProductManager getExpectedProductManger() {
		if (expectedProductManger == null) {
			String className = PropertyService.getPropertyBean(ExpectedProductPropertyBean.class, context.getProcessPointId()).getExpectedProductManager();
			Class clazz;
			try {
				clazz = Class.forName(className);
				if (!IExpectedProductManager.class.isAssignableFrom(clazz))
					throw new RuntimeException(className + " does not implement IExpectedProductManager");
				Class[] parameterTypes = {context.getClass()};
				Object[] parameters = {context};
				Constructor constructor = clazz.getConstructor(parameterTypes);
				expectedProductManger = (IExpectedProductManager) constructor.newInstance(parameters);
			} catch (Throwable e) {
				Logger.getLogger().error(e, "Failed to initialize expectedProductManger: " + className);
				expectedProductManger = null;
			}
		}
		return expectedProductManger;
	}

	public void setExpectedProductManger(
			IExpectedProductManager expectedProductManger) {
		this.expectedProductManger = expectedProductManger;
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		return getExpectedProductManger().getIncomingProducts(state);
	}

	public void getExpectedProductId(ProcessProduct state) {
		getExpectedProductManger().getExpectedProductId(state);
		if(context.isAfOnSeqNumExist())
			setAfOnSeqNumber(state);
		if(context.isProductLotCountExist() &&(context.getProperty().getProductType().equals(ProductType.FRAME.toString()) || 
				context.getProperty().getProductType().equals(ProductType.ENGINE.toString()) || 
				ProductTypeUtil.isMbpnProduct(context.getProperty().getProductType()))
				&& !StringUtils.isEmpty(state.getExpectedProductId())){
			BaseProduct product = ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findBySn(state.getExpectedProductId());
			if(product!= null && product.getProductionLot()!=null){
				setCountLotSize(state,product);

			}
		}
	}

	public void setCountLotSize(DataCollectionState state, BaseProduct product) {
		PreProductionLot prodLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(product.getProductionLot());
		long count = ServiceFactory.getDao(ProductResultDao.class).findTotalProductProcessed(product.getProductionLot(), context.getProcessPointId());
		List<String> pptList = new ArrayList<String>();pptList.add(context.getProcessPointId());
		List<Object> ResultCntList = ServiceFactory.getDao(ProductResultDao.class).getOffResultCnt(product.getProductId(), pptList);
		if(new Integer(ResultCntList.get(0).toString())>0){
			state.setProductCount(count);
		}else{
			state.setProductCount(count+1);
		}
		state.setLotSize(prodLot.getLotSize());
	}

	private void setAfOnSeqNumber(ProcessProduct state){
		if(context.getProperty().getProductType().equals(ProductType.FRAME.toString()) && 
				!StringUtils.isEmpty(state.getExpectedProductId())){
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			Frame frame = frameDao.findByKey(state.getExpectedProductId());
			if(frame!= null){
				if(!String.valueOf(frame.getAfOnSequenceNumber()).equalsIgnoreCase("null"))
					state.setAfOnSeqNo(String.valueOf(frame.getAfOnSequenceNumber()));
			}
		}
	}
	
	public void saveExpectedProductId(String nextProductId) {
		getExpectedProductManger().saveNextExpectedProduct(nextProductId);
	}
	
	public void saveExpectedProductId(String nextProductId, String lastProcessedProduct) {
		getExpectedProductManger().saveNextExpectedProduct(nextProductId, lastProcessedProduct);
	}
	
	public String getNextExpectedProductId(String productId){
		return getExpectedProductManger().getNextExpectedProductId(productId);
	}
	
	public boolean isProductIdAheadOfExpectedProductId(
			String expectedProductId, String productId) {
        return getExpectedProductManger().isProductIdAheadOfExpectedProductId(expectedProductId,productId);
	}
	
	public PreProductionLot findCurrentPreProductionLot(String processLocation) {
		try {
			PreProductionLotDao preproductionDao = ServiceFactory.getDao(PreProductionLotDao.class);
			return preproductionDao.findCurrentPreProductionLot(processLocation);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to find current pre-production lot.");
			return null;
		}
	}
	
	public List<ExpectedProduct> findAllExpectedProduct() {
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		return expectedProductDao.findAllForProcessPoint(context.getProcessPointId());
	}
	
	public void saveExpectedProduct(ExpectedProduct expected) {
		try{
			ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
			expectedProductDao.save(expected);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to save expected product.");
		}
	}
	
	protected void trackProduct(ProcessProduct state) {
		// if lc is integrated with qics, only qics performs tracking
		if (context.isQicsSupport()) {
			return;
		}
		
		//Don't track skipped expected - physically not pass the process point
		if(state.isSkippedProduct() && 
				(state.getProduct() == null || !state.getProduct().isValidProductId())) 
				return;
		
		ProcessPoint processPoint = getProcessPoint(state);
		if (processPoint == null) {
			Logger.getLogger().warn("WARN:", " invalid process point - skipped tracking.");
			return;
		}
		
		MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(processPoint.getProcessPointId().trim());
		if(qiMultiLineHelper.isMultiLine())  {
			processPoint = qiMultiLineHelper.getProcessPointToUse(findProductBySn(state.getProductId()));
		}
		
		ProductHistory productHistory = null;
		ProductType productType = ProductTypeCatalog.getProductType(context.getProperty().getProductType());
		if(state.getProductId() != null && state.getProduct().getBaseProduct() != null) {
			productHistory = ProductTypeUtil.createProductHistory(state.getProductId(), processPoint.getProcessPointId(), productType);
		} else if (state.getExpectedProductId() != null)  {
			//ok, track skipped product before product id input
			productHistory = ProductTypeUtil.createProductHistory(state.getExpectedProductId(), processPoint.getProcessPointId(), productType);
		} 
		
		if (productHistory != null) {
			productHistory.setAssociateNo(context.getUserId());
			getTrackingService().track(productType, productHistory);
		} else {
			Logger.getLogger().warn("WARN:", " skipped tracking for unknown product Id.");
		}
	}

	protected void invokeBroadcastService(ProcessProduct state) {
		// do not broadcast if the skip broadcast flag is on
		if (state.getStateBean().isSkipBroadcast()) {
			state.getStateBean().setSkipBroadcast(false);
			return;
		}
		// if lc is integrated with qics, only qics performs tracking
		if (context.isQicsSupport()) {
			return;
		}
		
		//Don't track skipped expected - physically not pass the process point
		if(state.isSkippedProduct() && 
				(state.getProduct() == null || !state.getProduct().isValidProductId())) 
				return;
		
		ProcessPoint processPoint = getProcessPoint(state);
		if (processPoint == null) {
			Logger.getLogger().warn("WARN:", " invalid process point - skipped broadcast service.");
			return;
		}
		
		if (state.getProductId() != null) {
			try{
				BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, state.getProductId());
				dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				broadcastService.broadcast(processPoint.getProcessPointId(), dc);
			}catch(Exception e){
				Logger.getLogger().warn(e, "Failed to invoke broadcast service");
			}
		}
	}
	
	protected boolean isBroadcastConfigured(ProcessProduct state) {
		ProcessPoint processPoint = getProcessPoint(state);
		if(processPoint == null) return false;
		List<BroadcastDestination> destinations = 
			ServiceFactory.getDao(BroadcastDestinationDao.class)
			.findAllByProcessPointId(processPoint.getProcessPointId());
		return !destinations.isEmpty();
	}
	
	public List<PreProductionLot> getUnSentPreproductionLot(){
		try{
			PreProductionLotDao dao = ServiceFactory.getDao(PreProductionLotDao.class);
			return dao.getUnSentLots(context.getProperty().getProcessLocation());
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to get un-sent preproduction lot.");
			return null;
		}
	}
	public PartLot findCurrentPartLot(String partName) {
		PartLotDao partLotDao = ServiceFactory.getDao(PartLotDao.class);
		return partLotDao.findInprogressPartLot(partName);
		
	}
	
	protected ProcessPoint getProcessPoint(DataCollectionState state) {
		ProcessPoint processPoint = null;
		try {
			ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
			processPoint = processPointDao.findByKey(state.getApplicationId());
		} catch (Exception ex) {}
		return processPoint;
	}
	
	protected ProductBuildResult findProductBuildResult(List<? extends ProductBuildResult> buildResults, String partName) {
		for(ProductBuildResult item : buildResults) {
			if(item.getPartName().equals(partName)) return item;
		}
		return null;
	}
	
	public void updateNextExpectedProductId(ProcessProduct state){
		getExpectedProductManger().getExpectedProductId(state);
	}
	
	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
	
	public LotControlRuleDao getLotControlRuleDao() {
		return ServiceFactory.getDao(LotControlRuleDao.class);
	}
	
	public ProductSequence findProductSequenceByProductId(String productId) {
		ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		ProductSequence productSequence = productSequenceDao.findByProductId(productId);
		return productSequence;
	}
	
	public ProductSequence findPreviousProductSequenceByProductId(String productId) {
		ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		ProductSequence previousProductSequence = null;
		ProductSequence productSequence = productSequenceDao.findByProductId(productId);
		if (productSequence != null) {
			previousProductSequence = productSequenceDao.findPrevProductId(productSequence);
		}
		return previousProductSequence;
	}
	
	protected TerminalPropertyBean getProperty(){
		return context.getProperty();
	}
	
	@Override
	public List<InstalledPart> findDuplicatePartsByPartNames(List<String> partNames, String partSerialNumber) {
			return getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partNames, partSerialNumber);
	}
}
