package com.honda.galc.client.product.mvc;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.mvc.IModel;
import com.honda.galc.client.product.IExpectedProductManager;
import com.honda.galc.client.product.ProductClientConfig;
import com.honda.galc.client.product.pane.AbstractProductInputPane;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.TrainingDataDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StragglerService;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;

import javafx.application.Platform;
import net.sf.ehcache.Cache;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessView</code> is ... .
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
 * @author Jeffray Huang
 */
public class ProductModel implements IModel{

	private ApplicationContext applicationContext;

	private ProductPropertyBean property;

	private BaseProduct lastProduct;

	private BaseProduct product;

	private String expectedProductId;

	private ProductionLot productionLot;

	private BaseProductSpec productSpec;

	private boolean isBypassExpectedProduct = false;

	private boolean isSkipped = false;

	private boolean sendToFinal;

	private IExpectedProductManager expectedProductManager;

	private List<? extends ProductHistory> productHistories;

	private ProductHistory productHistory;

	private boolean trainingMode = false;

	private boolean disPlayCCP = false;

	private QiStationEntryDepartment qiStationEntryDept;

	private Map<String, Object> mbpnProductMap = new HashMap<String, Object>();

	private List<TrainingDataDto> trainingDataDtosList;

	private Map<String, Object> productItemCheckResults;

	private List<ProductSearchResult> productList;
	
	private List<BaseProduct> processedProducts;

	private String currentApplicationId;

	private PersistentCache clientCache;

	private volatile boolean isLastProductSkipped = false;
	
	private boolean isProcessedFromScrappedTable = false;
	
	private Map<QiRepairResultDto, Integer> cachedDefectsForTraingMode = new HashMap<QiRepairResultDto, Integer>();
	
		
	public ProductModel() {
	}

	public ProductModel(ApplicationContext applicationContext) {
		setApplicationContext(applicationContext);
		this.clientCache = createClientCache(applicationContext.getProcessPointId());
	}

	public ProductModel(ApplicationContext applicationContext, String applicationId) {
		setApplicationContext(applicationContext);
		currentApplicationId = applicationId;
		this.clientCache = createClientCache(applicationContext.getProcessPointId());
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ProductPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}

		return property;
	}

	public void setProperty(ProductPropertyBean property) {
		this.property = property;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public String getProductId() {
		return product.getProductId();
	}

	public String getDivisionId(){
		return getProcessPoint().getDivisionId();
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public ProductTypeData getProductTypeData() {
		return applicationContext.getProductTypeData();
	}

	public String getProductType() {
		return applicationContext.getApplicationPropertyBean().getProductType();
	}

	public ProcessPoint getProcessPoint() {
		return applicationContext.getProcessPoint();
	}

	public String getServerUrl() {
		return applicationContext.getArguments().getServerURL();
	}

	public String getProcessPointId() {
		return getProcessPoint().getProcessPointId();
	}

	public BaseProductSpec getProductSpec() {
		if(productSpec == null && null!=product && null!=product.getProductSpecCode()) {
			productSpec = ProductTypeUtil.getProductSpecDao(getProductType())
					.findByProductSpecCode(product.getProductSpecCode(),product.getProductType().toString());
		}
		return productSpec;
	}

	public void setProductSpec(ProductSpec productSpec) {
		this.productSpec = productSpec;
	}

	@SuppressWarnings("deprecation")
	public ProductionLot getProductionLot() {
		if(productionLot == null) {
			productionLot = getDao(ProductionLotDao.class).findByKey(product.getProductionLot());
		}
		return productionLot;
	}

	public IExpectedProductManager getExpectedProductManager() {
		if(expectedProductManager == null)
			expectedProductManager = createExpectedProductManager();
		return expectedProductManager;
	}

	@SuppressWarnings("unchecked")
	private IExpectedProductManager createExpectedProductManager() {
		String className = getProperty().getExpectedProductManager();
		Class clazz;
		try {
			clazz = Class.forName(className);
			if(!IExpectedProductManager.class.isAssignableFrom(clazz))
				getLogger().error( className + " is not expeced product manager");
			Class[] parameterTypes = {this.getClass(),Logger.class};
			Object[] parameters = {this,applicationContext.getLogger()};
			Constructor constructor = clazz.getConstructor(parameterTypes);
			return (IExpectedProductManager) constructor.newInstance(parameters);
		} catch (Throwable e) {
			getLogger().error(e,"Failed to create expected product manager:" + className);
		}
		return null;
	}

	protected void setExpectedProductManager(IExpectedProductManager expectedProductManager) {
		this.expectedProductManager = expectedProductManager;
	}

	public void setProductionLot(ProductionLot productionLot) {
		this.productionLot = productionLot;
	}

	public List<? extends ProductHistory> getProductHistory() {
			productHistories = ProductTypeUtil.getProductHistoryDao(getProductType()).findAllByProductId(product.getProductId());
		return productHistories;
	}

	public void setProductHistory(List<ProductHistory> productHistories) {
		this.productHistories = productHistories;
	}

	public BaseProduct findProduct(String productId) {
		BaseProduct product = ProductTypeUtil.getProductDao(getProductType()).findBySn(productId);
		return product;
	}
	
	public List<? extends BaseProduct> findProducts(List<String> productIds, int startPos, int pageSize) {
		return ProductTypeUtil.getProductDao(getProductType()).findProducts(productIds, startPos, pageSize);
	}

	public Logger getLogger() {
		return applicationContext.getLogger();
	}

	public void invokeTracking() {
		ProcessPoint processPoint = getActualProcessPoint();

		invokeTracking(processPoint);
		invokeAdditionalTracking();
	}
	
	public void invokeBulkTracking() {
		ProcessPoint processPoint = getActualProcessPoint();

		invokeBulkTracking(processPoint);
		invokeAdditionalTracking();
	}

	//get actual process point in case it is multi line station
	public ProcessPoint getActualProcessPoint() {
		ProcessPoint actualPP = getApplicationContext().getProcessPoint();
		MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(getProcessPointId().trim());
		if(qiMultiLineHelper.isMultiLine())  {
			ProcessPoint newPP = qiMultiLineHelper.getProcessPointToUse(getProduct());
			//couldn't determine station configuration, just use the current station
			if(newPP != null && !newPP.getProcessPointId().trim().equalsIgnoreCase(getProcessPointId().trim()))  {
				actualPP = newPP;
			}
		} else if(!StringUtils.isBlank(currentApplicationId))  {
			ProcessPoint newPP = getDao(ProcessPointDao.class).findById(currentApplicationId);
			if(newPP != null)  {
				actualPP = newPP;
			}
		}
		return actualPP;
	}

	public void invokeTracking(ProcessPoint processPoint) {

		if(ProductClientConfig.checkIsUpcStation() || isBulkProcess())  {
			invokeUPCTracking(processPoint);
		} else if (isBulkProcess()) {
			invokeBulkTracking(processPoint);
		}
		else if (processPoint != null && (processPoint.isTrackingPoint() || processPoint.isPassingCount())) {
			final ProductHistory productHistory = getProductHistoryForTrack(getProduct().getProductId());
			if (productHistory != null) {
				productHistory.setProcessPointId(processPoint.getProcessPointId());
			}
			Runnable track = new Runnable() {
				@Override
				public void run() {
					getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
				}
			};
			if (getProperty().isAsyncInvokeTracking()){
				Platform.runLater(track);
			} else {
				track.run();
			}
		}
	}	
	
	public void invokeBulkTracking(ProcessPoint processPoint) {
		if (processPoint != null && (processPoint.isTrackingPoint() || processPoint.isPassingCount())) {
			final List<ProductHistory> productHistoryList = new ArrayList<ProductHistory>();
			if(getProcessedProducts() != null && !getProcessedProducts().isEmpty())  {
				for(BaseProduct baseProduct : getProcessedProducts())  {
					ProductHistory hist = getProductHistoryForTrack(baseProduct.getProductId());
					if(hist != null)  productHistoryList.add(hist);
				}

					if(!productHistoryList.isEmpty())  {
						for(ProductHistory pHist : productHistoryList)  {
							pHist.setProcessPointId(processPoint.getProcessPointId());
						}
					}

					Runnable track = new Runnable() {
						@Override
						public void run() {
							if(productHistoryList != null && !productHistoryList.isEmpty())  {
								for(ProductHistory productHistory : productHistoryList)  {
									getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
								}
							}
						}
					};
					if (getProperty().isAsyncInvokeTracking()){
						Platform.runLater(track);
					} else {
						track.run();
					}
			}
		}
	}
	
	public Division findDivision(String divisionId) {
		return ServiceFactory.getDao(DivisionDao.class).findByDivisionId(divisionId);
	}
	
	public Line findLine(String lineId) {
		return ServiceFactory.getDao(LineDao.class).findByKey(lineId);
	}
	
	public ProcessPoint findProcessPoint(String processPointId) {
		return ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
	}

	public void invokeUPCTracking(ProcessPoint processPoint) {

		if (processPoint != null && (processPoint.isTrackingPoint() || processPoint.isPassingCount())) {
			final List<ProductHistory> productHistories = new ArrayList<ProductHistory>();
			if(mbpnProductMap != null && !mbpnProductMap.isEmpty())  {
				List<MbpnProduct> mbpnProducts = (List<MbpnProduct>) mbpnProductMap.get("products");
				if(mbpnProducts != null && !mbpnProducts.isEmpty())  {
					for(MbpnProduct mbpn : mbpnProducts)  {
						ProductHistory hist = getProductHistoryForTrack(mbpn.getProductId());
						if(hist != null)  productHistories.add(hist);
					}
				}
			}

			if(productHistories.isEmpty())  {
				ProductHistory pHist = getProductHistoryForTrack(getProduct().getProductId());
				productHistories.add(pHist);
			}

			if(!productHistories.isEmpty())  {
				for(ProductHistory pHist : productHistories)  {
					pHist.setProcessPointId(processPoint.getProcessPointId());
				}
			}

			Runnable track = new Runnable() {
				@Override
				public void run() {
					if(productHistories != null && !productHistories.isEmpty())  {
						for(ProductHistory productHistory : productHistories)  {
							getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
						}
					}
				}
			};
			if (getProperty().isAsyncInvokeTracking()){
				Platform.runLater(track);
			} else {
				track.run();
			}
		}
	}

	private void invokeAdditionalTracking() {

		ProcessPoint processPoint = getActualProcessPoint();
		Logger.getLogger().info("inside invokeAdditionTracking");

		String forwardToProcessPointId = getForwardToProcessPointId(processPoint.getId());
		if (StringUtils.isBlank(forwardToProcessPointId)) {
			return;
		}
		ProcessPoint forwardToProcessPoint = getDao(ProcessPointDao.class).findById(forwardToProcessPointId);
		if (forwardToProcessPoint == null) {
			Logger.getLogger().warn("Additional Tracking Invalid ProcessPointId :" + forwardToProcessPointId);
			return;
		}
		invokeTracking(forwardToProcessPoint);
	}

	protected String getForwardToProcessPointId(String processPointId) {

		Map<String, Object> checkResults = getProductItemCheckResults();
		if (checkResults == null) {
			return null;
		}
		TrackingPropertyBean property = PropertyService.getPropertyBean(TrackingPropertyBean.class, processPointId);
		if (isSendToFinal()) {
			return property.getTrackingProcessPointIdOnSuccess();
		}
		boolean forwardTrackingStatus = getForwardTrackingStatus(property.getForwardTrackingStatus(), checkResults);
		if (forwardTrackingStatus) {
			return property.getTrackingProcessPointIdOnSuccess();
		} else {
			return property.getTrackingProcessPointIdOnFailure();
		}
	}

	private boolean getForwardTrackingStatus(String statusName, Map<String, Object> checkResults) {
		if (TagNames.OVERALL_CHECK_RESULT.name().equals(statusName)) {
			return checkResults.size() == 0;	
		} else {
			return checkResults.get(statusName) == null;
		}
	}


	private ProductHistory getProductHistoryForTrack(String productid) {
		ProcessPoint processPoint = getApplicationContext().getProcessPoint();
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(productid, processPoint.getProcessPointId(), getProductType());

		if (productHistory != null) { 
			productHistory.setAssociateNo(getApplicationContext().getUserId());
			//TODO::productHistory.setApproverNo(model.getApplicationContext().getApproverNo());
			productHistory.setApproverNo("");
			productHistory.setProcessPointId(processPoint.getProcessPointId());
			productHistory.setProductId(productid);
		}
		return productHistory;
	}

	protected void invokeBroadcastService(CheckPoints checkPoint) {
		if(ProductClientConfig.checkIsUpcStation())  {
			invokeUPCBroadcast(checkPoint);
		}
		else  {
			invokeBroadcastService(getProduct().getProductId(), checkPoint);
		}
	}

	protected void invokeBroadcastService(String broadcastProductId, CheckPoints checkPoint) {

		ProcessPoint processPoint = getApplicationContext().getProcessPoint();
		if (processPoint == null) {
			return;
		}
		String productId = getProduct().getProductId();

		// if productId was passed as a parameter, use that
		if (!StringUtils.isEmpty(broadcastProductId)) {
			productId = broadcastProductId;
		}

		try {
			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, productId);
			dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
			getService(BroadcastService.class).broadcast(processPoint.getProcessPointId(), dc, checkPoint);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to invoke broadcast service for " + productId + " and " + processPoint);
		}
	}

	protected void invokeUPCBroadcast(CheckPoints checkPoint) {
		final List<String> allProductIds = new ArrayList<String>();
		if(mbpnProductMap != null && !mbpnProductMap.isEmpty())  {
			List<MbpnProduct> mbpnProducts = (List<MbpnProduct>) mbpnProductMap.get("products");
			if(mbpnProducts != null && !mbpnProducts.isEmpty())  {
				for(MbpnProduct mbpn : mbpnProducts)  {
					allProductIds.add(mbpn.getProductId());
				}
			}
		}
		if(allProductIds.isEmpty())  {
			allProductIds.add(getProduct().getProductId());
		}

		try{
			for(String productId : allProductIds)  {
				invokeBroadcastService(productId, checkPoint);
			}
		}catch(Exception e){
			Logger.getLogger().warn(e, "Failed to invoke broadcast service");
		}
	}
	
	public void invokeBroadCast(String processPoint, DataContainer dc, CheckPoints checkPoint) {
		try {
			getService(BroadcastService.class).broadcast(processPoint, dc, checkPoint);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to invoke broadcast service for " + dc.getString(DataContainerTag.VIN) + " and " + dc.getString(DataContainerTag.PROCESS_POINT_ID));
		}
	}

	protected DataContainer getProductDataContainer(String productId, String userId)  {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		dc.put(DataContainerTag.USER_ID, userId);
		return dc;
	}

	public void invokeStragglerService(final ProcessPoint ppt,final BaseProduct pdt) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getService(StragglerService.class).identifyStragglers(ppt, pdt);
			}
		});
	}

	public boolean isSkipped() {
		return isSkipped;
	}

	public void setSkipped(boolean isSkipped) {
		this.isSkipped = isSkipped;
	}

	public boolean isBypassExpectedProduct() {
		return isBypassExpectedProduct;
	}

	public void setBypassExpectedProduct(boolean isBypassExpectedProduct) {
		this.isBypassExpectedProduct = isBypassExpectedProduct;
	}

	public String getExpectedProductId() {
		if (getExpectedProductManager() != null) {
			expectedProductId = getExpectedProductManager().getExpectedProductId();
		}
		return expectedProductId;
	}

	public boolean isModelChanged(String productSpec) {
		return (lastProduct != null) && !lastProduct.getProductSpecCode().equals(productSpec);
	}

	/*
	 * returns true when model code changed
	 * */
	public boolean isYearModelCodeChanged(String productSpecCode){

		if(lastProduct == null)
			return true;

		return !(StringUtils.startsWith(lastProduct.getProductSpecCode(), productSpecCode.substring(0, 4)));
	}

	public void broadcastDataContainer(String COMPLETE_FLG) {

		DataContainer dc = new DefaultDataContainer();
		dc.put("COMPLETE_FLG", COMPLETE_FLG);
		dc.put("PRODUCT_ID", getProductId());
		dc.put("ASSOCIATE_NO", getApplicationContext().getUserId());
		//TODO::dc.put("APPROVER_NO", getApplicationContext().getApproverNo());
		dc.put("APPROVER_NO", "");
		getService(BroadcastService.class).broadcast(getApplicationContext().getProcessPoint().getProcessPointId(),1, dc);
	}

	public void broadcastAfterlogin() {
		DataContainer dc = new DefaultDataContainer();
		dc.put("COMPLETE_FLG", "4");
		getService(BroadcastService.class).broadcast(getApplicationContext().getProcessPoint().getProcessPointId(),1, dc);
	}

	public boolean isDisplayCCP() {

		if( !getProperty().isDisplayCcp() ) {
			disPlayCCP = false;
			return false;
		}
		if (isModelChanged(getProduct().getProductSpecCode())||getLastProduct() == null) {
			disPlayCCP = true;
			return true;
		}
		if (getProperty().isStragglerAsModelChange()){
			if (getProperty().isInvokeStragglerService()){
				List<Straggler> currentProductstragglerList=ServiceFactory.getDao(StragglerDao.class).findStragglerProductList(product.getProductId(), getProcessPoint().getProcessPointId());	
				if(currentProductstragglerList.size()>0) {
					disPlayCCP = true; 
					return true;
				}
			} else {
				List<ProductResult> stragglerList=ServiceFactory.getDao(ProductResultDao.class).getStraggler(getProcessPoint().getProcessPointId(), product.getProductionLot());
				if (stragglerList.size()>0) {
					disPlayCCP = true;
					return true;
				}
			}
		}
		disPlayCCP = false;
		return false;
	}

	public BaseProduct getLastProduct() {
		return lastProduct;
	}

	public void setLastProduct(BaseProduct lastProduct) {
		this.lastProduct = lastProduct;
	}

	public boolean isTrainingMode() {
		return this.trainingMode;
	}

	public void setDisPlayCCP(boolean disPlayCCP) {
		this.disPlayCCP = disPlayCCP;
	}

	public boolean displayCCP() {
		return this.disPlayCCP;
	}

	public void setTrainingMode(boolean trainingMode) {
		this.trainingMode = trainingMode;
	}


	public Map<String, Object> getProductItemCheckResults() {
		return productItemCheckResults;
	}

	public void setProductItemCheckResults(
			Map<String, Object> productItemCheckResults) {
		this.productItemCheckResults = productItemCheckResults;
	}

	@Override
	public void reset() {
		this.lastProduct = this.product;
		this.product = null;
		this.productSpec = null;
		this.isSkipped = false;
		this.productionLot = null;
		this.productHistories = null;
		this.productItemCheckResults = null;
		this.sendToFinal = false;
		//this.productList = null;
	}

	/**
	 * @return the qiStationEntryDept
	 */
	public QiStationEntryDepartment getQiStationEntryDept() {
		return qiStationEntryDept;
	}

	/**
	 * @param qiStationEntryDept the qiStationEntryDept to set
	 */
	public void setQiStationEntryDept(QiStationEntryDepartment qiStationEntryDept) {
		this.qiStationEntryDept = qiStationEntryDept;
	}

	/**
	 * @return the mbpnProductMap
	 */
	public Map<String, Object> getMbpnProductMap() {
		return mbpnProductMap;
	}

	/**
	 * @param mbpnProductMap the mbpnProductMap to set
	 */
	public void setMbpnProductMap(Map<String, Object> mbpnProductMap) {
		this.mbpnProductMap = mbpnProductMap;
	}


	public List<TrainingDataDto> getTrainingDataDtosList() {
		return trainingDataDtosList;		
	}

	public void setTrainingDataDtosList(List<TrainingDataDto> trainingDataDtosList) {
		this.trainingDataDtosList = trainingDataDtosList;
	}

	public QiStationConfiguration findValueByProcessPointAndPropKey(){
		return ServiceFactory.getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProcessPointId(),QiEntryStationConfigurationSettings.DEFAULT_UPC_QUANTITY.getSettingsName());
	}

	public void getUpcQuantity(AbstractProductInputPane clientPane){
		if(null!=clientPane){
			LoggedTextField qtyTextFeild = clientPane.getQuantityTextField();
			if(null!=qtyTextFeild){
				QiStationConfiguration qiStationConfiguration = findValueByProcessPointAndPropKey();
				if(qiStationConfiguration!=null){
					qtyTextFeild.setText(qiStationConfiguration.getPropertyValue());
				}else{
					qtyTextFeild.setText(QiEntryStationConfigurationSettings.DEFAULT_UPC_QUANTITY.getDefaultPropertyValue());
				}
			}

		}
	}

	public int updateGdpDefects() {
		return getDao(QiDefectResultDao.class).updateGdpDefects(getProductId(),getApplicationContext().getUserId());
	}

	public boolean isSendToFinal() {
		return sendToFinal;
	}

	public void setSendToFinal(boolean sendToFinal) {
		this.sendToFinal = sendToFinal;
	}


	public PersistentCache getClientCache() {
		return clientCache;
	}

	/**
	 * @return the isLastProductSkipped
	 */
	public boolean isLastProductSkipped() {
		return isLastProductSkipped;
	}

	/**
	 * @param isLastProductSkipped the isLastProductSkipped to set
	 */
	public void setLastProductSkipped(boolean isLastProductSkipped) {
		this.isLastProductSkipped = isLastProductSkipped;
	}

	public PersistentCache createClientCache(String processPointId) {
		String cacheName = "ClientCache";
		ProductPropertyBean property= PropertyService.getPropertyBean(ProductPropertyBean.class, processPointId);
		int maxElementsInMemory = property.getCacheSizeMem();
		int maxElementsOnDisk = property.getCacheSizeDisk();
		int timeToLive = property.getCacheTimeToLive();
		int timeToIdle = property.getCacheTimeToIdle();
		Cache cache = new Cache(cacheName, 
				maxElementsInMemory, // max elements in memory
				MemoryStoreEvictionPolicy.LRU, // memory store eviction policy
				true, // overflowToDisk
				CacheFactory.getPath(), // disk store path
				true, // eternal
				timeToLive, // time to live in seconds
				timeToIdle, // time to idle in seconds
				false, // persist the cache to disk between JVM restarts
				120, // disk expiry thread interval in seconds
				null, // registered event listeners
				null, // bootstrap cache loader
				maxElementsOnDisk, // max elements on disk
				10, // disk spool buffer size in Mb
				true); // clear on flush
		CacheFactory.addCache(cache);
		PersistentCache persistentCache = new PersistentCache(cache.getName());
		return persistentCache;
	}
	
	public void clearProcessedProducts() {
		 processedProducts.clear();
	}
	
	public boolean isBulkProcess() {
		return !(processedProducts == null || processedProducts.isEmpty());
	}

	public List<ProductSearchResult> getProductList() {
		if(productList == null) { 
			productList = new ArrayList<ProductSearchResult>();}
		return productList;
	}

	public void setProductList(List<ProductSearchResult> productList) {
		this.productList = productList;
	}	

	public List<BaseProduct> getProcessedProducts() {
		return processedProducts;
	}

	public boolean isSingleProduct() {
		return processedProducts != null && processedProducts.size() == 1;
	}

	public void setProcessedProducts(List<BaseProduct> processedProducts) {
		this.processedProducts = processedProducts;
	}

	public Map<QiRepairResultDto, Integer> getCachedDefectsForTraingMode() {
		return cachedDefectsForTraingMode;
	}

	public void setCachedDefectsForTraingMode(Map<QiRepairResultDto, Integer> cachedDefectsForTraingMode) {
		this.cachedDefectsForTraingMode = cachedDefectsForTraingMode;
	}	
	
	public boolean isProcessedFromScrappedTable() {
		return isProcessedFromScrappedTable;
	}

	public void setProcessedFromScrappedTable(boolean isProcessedFromScrappedTable) {
		this.isProcessedFromScrappedTable = isProcessedFromScrappedTable;
	}
	
	public List<QiRepairResultDto> findAllRepairEntryDefectsByDefectId(long defectResultId) {
		return getDao(QiRepairResultDao.class).findAllRepairEntryDefectsByDefectResultId(defectResultId);
	}
}
