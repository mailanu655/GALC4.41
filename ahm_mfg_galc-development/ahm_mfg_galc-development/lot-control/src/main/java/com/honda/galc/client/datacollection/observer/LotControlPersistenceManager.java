package com.honda.galc.client.datacollection.observer;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.ProductIdProcessor;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.datacollection.sync.DataCacheSyncManager;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.SkippedProductDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.SkippedProductStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductResultUtil;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.property.PropertyService;
import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * <h3>LotcontrolDataCollectionPersistentManager</h3>
 * <h4>
 * Save data collection data to database as required based on current state.
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
 * <TD>Aug.19, 2009</TD>R
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
public class LotControlPersistenceManager extends LotControlPersistenceBase
implements IPersistenceObserver {
	
	private boolean saveMeasurementHistory;
	private List<LotControlRule> lotControlRules;
	private int torqueCount = 0;
	protected ExpectedProduct expectedProdId;

	public LotControlPersistenceManager(ClientContext context) {
		super(context);
		
		initialize();
	}

	private void initialize() {
		saveMeasurementHistory = context.getProperty().isSaveMeasurementHistory();
		
		//start sync manager
		if(context.getProperty().isAutoSync()){
			Runnable r = new DataCacheSyncManager(context);
			Thread t = new Thread(r);
			t.start();
		}
	}
	
	public void processEntityList(List<EntityList<AbstractEntity>> masterList,String productId,String type) {
		try {
			Logger.getLogger().info("START :: LotControlPersistenceManager :: Started processing "+type+" Master List Entities for the product id " + productId);
			long startTime = System.currentTimeMillis();
			Iterator<EntityList<AbstractEntity>> masterEntityList = masterList.iterator();
			while (masterEntityList.hasNext()) {
				EntityList<AbstractEntity> entityList = masterEntityList.next();
				Logger.getLogger().debug("LotControlPersistenceManager :: Start :: operation :: " + entityList.getOperatedFor() + " :: for Product :: " + entityList.getProductId());
				if(entityList.getPartId() != null)
					Logger.getLogger().debug("Processing for :: " + entityList.getPartId());					
				for (AbstractEntity entity : entityList) {
					Logger.getLogger().debug("Processing entity :: " + entity.getEntityString());										
					entity.process();					
				}
				Logger.getLogger().debug("LotControlPersistenceManager :: End :: For operation :: " + entityList.getOperatedFor() + " for :: " + entityList.getProductId());
				entityList = null;
			}
			masterList = null;
			long diff = System.currentTimeMillis() - startTime;
			Logger.getLogger().info("END :: LotControlPersistenceManager :: "+type+" Master List Entities Successfully processed for the product id " + productId+" in "+diff+" ms");	
		} catch (Exception e) {
			Logger.getLogger().error("ERROR :: LotControlPersistenceManager :: An exception occured while processing DB operations for the Entity List " );		
			e.printStackTrace();
		}
	}


	public void saveCompleteData(ProcessProduct state) {
		
		if(context.getProperty().isSaveSkippedProduct()){
			trackSkippedProduct(state);
		}
		
		if(state.getProduct() == null) {
			Logger.getLogger().warn("Failed to save collected data for product id is null");
			return; 
		}
		
		try {
			if(!state.getProduct().isSkipped() &&  !state.getProduct().masterListEntitiesIsEmpty()){
				processEntityList(state.getProduct().getMasterEntityList(),state.getProduct().getProductId(),"");
				state.getProduct().clearMasterList();
			}else{
				Logger.getLogger().debug("No Entities to process from the Master Entity List for the product id " + state.getProduct().getProductId());
			}
				
		} catch (Exception e) {
			Logger.getLogger().error("ERROR :: LotControlPersistenceManager :: DB Operation Failed for the product id " + state.getProduct().getProductId());
			Logger.getLogger().error("Error Caught while processing Entity List" + e.getMessage());
			state.exception(new LotControlTaskException("Failed to update database.", this.getClass().getSimpleName()),true);
			e.printStackTrace();
		}finally{
			Logger.getLogger().debug("Clearing Master List in finally block");
			state.getProduct().clearMasterList();
		}		
		
		if(state.getProduct().getPartList().size() > 0){
			Logger.getLogger().debug("PersistentManager save InstalledPart on Server");
			saveCollectedData(state);
		}
		
		if(context.isOnLine() && context.getProperty().isAutoUpdateQics()) {
			if(context.getProperty().isUseQicsService())
				updateQics(state);
			else
				updateNAQics(state);
		}
		
		if(context.isOnLine() && isTrackingConfigured(state))
			trackProduct(state);
		
		try {
			if(!state.getProduct().isSkipped() &&  !state.getProduct().afterTrackingMasterListEntitiesIsEmpty()){
				processEntityList(state.getProduct().getAfterTrackingMasterEntityList(),state.getProduct().getProductId(),"After Tracking");			
			    state.getProduct().clearAfterTrackingMasterList();
			}else{
				Logger.getLogger().debug("No Entities to process from the After Tracking Master Entity List for the product id " + state.getProduct().getProductId());
			}
				
		} catch (Exception e) {
			Logger.getLogger().error("ERROR :: LotControlPersistenceManager :: DB Operation Failed for the product id " + state.getProduct().getProductId());
			Logger.getLogger().error("Error Caught while processing After Tracking Master Entity List" + e.getMessage());
			state.exception(new LotControlTaskException("Failed to update database.", this.getClass().getSimpleName()),true);
			e.printStackTrace();
		}finally{
			Logger.getLogger().debug("Clearing After Tracking Master List in finally block");
			state.getProduct().clearAfterTrackingMasterList();
		}	
		
		if(context.isOnLine() && isBroadcastConfigured(state))
			invokeBroadcastService(state);
		
		saveExpectedProduct(state);
		
		performAdditionalTracking(state);
		
	}
	
	protected void performAdditionalTracking(ProcessProduct state) {
		String forwardToProcessPointId = getForwardToProcessPointId(state);
		if (StringUtils.isNotBlank(forwardToProcessPointId)) {
			getTrackingService().track(context.getProductType(), state.getProductId(), forwardToProcessPointId);
		}	
	}
	
	protected String getForwardToProcessPointId(ProcessProduct state) {
		TrackingPropertyBean property = PropertyService.getPropertyBean(TrackingPropertyBean.class, context.getProcessPointId());
		boolean forwardTrackingStatus = getForwardTrackingStatus(state);
		if (forwardTrackingStatus) {
			return property.getTrackingProcessPointIdOnSuccess();
		} else {
			return property.getTrackingProcessPointIdOnFailure();
		}
	}
	
	private boolean getForwardTrackingStatus(ProcessProduct state) {
		List<InstalledPart> partList = state.getProduct().getPartList();
		for (InstalledPart part : partList) {
			if (!part.getInstalledPartStatus().equals(InstalledPartStatus.OK) || isSkippedProduct(state)) 
				return false;
		}
		return true;
	}
	
	private void updateNAQics(ProcessProduct state) {
		if(state.getProduct() != null && state.getProduct().isValidProductId()) {
			List<InstalledPart> naqBuildResults = getNaqBuildRestults(state);
			if(null != naqBuildResults && naqBuildResults.size() > 0)
				ServiceFactory.getService(QicsService.class).update(context.getProcessPointId(), context.getProductType(), naqBuildResults);
		}
		
	}

	private List<InstalledPart> getNaqBuildRestults(ProcessProduct state) {
		List<InstalledPart> resultList = new ArrayList<InstalledPart>();
		
		for(InstalledPart ip : state.getProduct().getPartList()) {
			if(ip.isQicsDefect())
				resultList.add(ip);
		}

		return resultList;
	}

	protected void trackSkippedProduct(ProcessProduct state) {
		if(!context.isOnLine()) {
			Logger.getLogger().info("Server off line - no skipped product saved.");
			return;
		}
		
		if(context.isDisabledExpectedProductCheck()){
			saveDisableExpected(state);
			return;
		}
		
		if(context.isCheckExpectedProductId() && isSkippedProduct(state)){
			if(context.getProperty().isSaveSkippedExpectedProductOnly() && state.isSkippedProduct()){
				saveSkippedProduct(state);
			} else {
				//TODO - no required now
			}
		}
		
	}
	
	private void updateQics(ProcessProduct state) {
		List<InstalledPart> skippedParts = getSkippedParts(state);
		if(skippedParts != null && skippedParts.size() > 0){
			QicsService service = ServiceFactory.getService(QicsService.class);
			service.update(context.getProcessPointId(), ProductTypeCatalog.getProductType(context.getProperty().getProductType()),
					skippedParts);
		} else
			Logger.getLogger().info("There is no qics defect. skip invoking Qics service.");

	}
	

	private List<InstalledPart> getSkippedParts(ProcessProduct state) {
		if(state.getProduct() == null || state.getProduct().getPartList() == null ||
				state.getProduct().getPartList().size() == 0) return null;
		
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		for(InstalledPart part : state.getProduct().getPartList()){
			if(part.isSkipped()) {
				prepareSkippedPartQicsData(part);
				list.add(part);
			}
		}
		
		return list;
	}

	private void prepareSkippedPartQicsData(InstalledPart part) {
		// Qics from Lot Control only create defect on Invalid Sn or Invalid Torque
		if(!part.isValidPartSerialNumber()) return;
		else if(isSkippedTorqueBeforeRunDown(part)){
			Measurement measurement = new Measurement();
			MeasurementId id = new MeasurementId(part.getProductId(), part.getPartName(), 1);
			measurement.setId( id);
			measurement.setMeasurementStatus(MeasurementStatus.NG);
			part.getMeasurements().add(measurement);
			
		}
		
	}

	private boolean isSkippedTorqueBeforeRunDown(InstalledPart part) {
		List<Measurement> torqueList = part.getMeasurements();
		if(torqueList.size() == 0) return true; //skipped before run down 1st torque
		else if(torqueList.get(torqueList.size() -1).getMeasurementStatus() == MeasurementStatus.OK){
			return true; //skipped before run down a torque
		}
		return false;

	}
	protected void saveExpectedProduct(ProcessProduct state) {
		if(context.isOnLine() && context.isCheckExpectedProductId() &&  (state.isSaveNextExpected())) {
			getExpectedProductManger().saveNextExpectedProduct(state);
		} 
		else if(!context.isOnLine())
			state.setExpectedProductId(null);
		
		saveLastExpected(state);
	}

	protected void saveLastExpected(ProcessProduct state) {
		
		/* Data for line side monitor
		 * if save last is configured and product is not yet saved into 135 (it could be saved if 
		 * checkExpected is enabled) THEN save the current product into 135 anyway
		*/
		if(context.isOnLine() && context.getProperty().isSaveLastProduct() && 
				!context.isCheckExpectedProductId()){

			if(state.getProduct().isValidProductId() || 
					ProductIdProcessor.LOT_CONTROL_RULE_NOT_DEFINED.equals(state.getMessage().getId()))
				getExpectedProductManger().saveNextExpectedProduct(state.getProductId());
		}
	}

	protected boolean isTrackingConfigured(DataCollectionState state) {
		ProcessPoint processPoint = getProcessPoint(state);
		
		if (processPoint != null) {
			return processPoint.getTrackingPointFlag() == 1 || processPoint.getPassingCountFlag() == 1;
		} else {
			return false;
		}
	}

	protected boolean isSkippedProduct(DataCollectionState state) {
		return state.isSkippedProduct() || state.getProduct().isSkipped();
	}

	protected void saveCollectedData(ProcessProduct state) {
		

		//skip product will not save build results
		if(isSkippedProduct(state) && !context.getProperty().isSaveBuildResultsForSkippedProduct()) return;
		
		List<InstalledPart> preparedList = prepareForSave(state);
		Logger.getLogger().debug("InstalledPart:" + state.getProduct().toString());

		try {
			if(context.isOnlineMode() && InstalledPartCache.getInstance().isEmpty()){
				saveInstalledPartOnServer(preparedList);
			} else {
				saveCollectedDataOnLocalCache(preparedList);
			}
			
		}catch(BaseException be){
			Logger.getLogger().error(be, "Failed to update database for " + state.getProductId());
			state.exception(new LotControlTaskException("Failed to update database.", this.getClass().getSimpleName()),true);
		}catch (Exception e) {
			Logger.getLogger().error(e, "Failed to  update database for " + state.getProductId());
			if(!(e instanceof ServiceTimeoutException || e instanceof ConnectException || e instanceof UndeclaredThrowableException)){
				throw new TaskException("Error: Failed to connect to database.","LotControlPersistenceManager",e);
			}
			else {
				setOffLine();
				saveCollectedDataOnLocalCache(preparedList);
			}
		}

	}

	private void saveInstalledPartOnServer(List<InstalledPart> partList) {
		
		LotControlPartUtil.filterOutExcludePart(partList, context.getProperty().getExcludePartsToSave());

		WebStartClient webStartClient = context.getAppContext().getWebStartClient();
		Feature webStartFeature = null;
		if(null != webStartClient) {
			webStartFeature = webStartClient.getFeature();
		}
		
		if(webStartFeature == null) {
			String processPointId = context.getAppContext().getApplicationId();
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
			Feature processPointFeature = processPoint.getFeature();
			for(InstalledPart part: partList) {
				if(processPointFeature != null) {
						part.setFeatureType(processPointFeature.getFeatureType());
						part.setFeatureId(processPointFeature.getFeatureId());
				}
				for(Measurement measurementPart: part.getMeasurements()) {
					if(processPointFeature != null) {
						measurementPart.setFeatureType(processPointFeature.getFeatureType());
						measurementPart.setFeatureId(processPointFeature.getFeatureId());
					}
				}
			}
		} else {
			for(InstalledPart part: partList) {
				part.setFeatureType(webStartFeature.getFeatureType());
				part.setFeatureId(webStartFeature.getFeatureId());
				for(Measurement measurementPart: part.getMeasurements()) {
						measurementPart.setFeatureType(webStartFeature.getFeatureType());
						measurementPart.setFeatureId(webStartFeature.getFeatureId());
				}
			}
		}
		
		ProductResultUtil.saveAll(context.getProcessPointId(), partList);
		
		for(InstalledPart part: partList) {
			Logger.getLogger().info("saved part: " + part.toShortString());
		}
		Logger.getLogger().info("Saved data into database on server.");
	}


	
	private void saveCollectedDataOnLocalCache(List<InstalledPart> preparedList) {
		
		InstalledPartCache.getInstance().saveInstalledPart(preparedList);
		
	}


	protected <T extends DataCollectionState> List<InstalledPart> prepareForSave(T state) {
		lotControlRules = state.getLotControlRules();
		ProductBean product = state.getProduct();
		List<InstalledPart> resultList = new ArrayList<InstalledPart>();
		int partIndex = 0;
		for(InstalledPart p : product.getPartList()){
			Logger.getLogger().debug("part:" + p.getId().getPartName());
			
			boolean torqueStatus = prepareTorquesForPart(p, partIndex);
			p.setAssociateNo(context.getUserId());
			p.setProcessPointId(context.getProcessPointId());
			p.setInstalledPartReason("");

			if(torqueStatus && p.isValidPartSerialNumber() && !p.isSkipped()) {
				p.setInstalledPartStatus(InstalledPartStatus.OK);
				p.setInstalledPartReason(getProperty().getInstalledPartReason());
			}
			else
				p.setInstalledPartStatus(InstalledPartStatus.NG);

			if(context.getProperty().isAutoUpdateQics() && !context.getProperty().isUseQicsService()) {
				LotControlRule r = lotControlRules.get(partIndex);
				p.setQicsDefect(r.isQicsDefect());
				if(p.isQicsDefect()) {
					InstalledPart partFromDb = getDao(InstalledPartDao.class).findById(p.getProductId(), p.getPartName());
					if(null != partFromDb) {
						p.setDefectRefId(partFromDb.getDefectRefId());
					}
				}
			}
			partIndex++;

			resultList.add(p);
		}
		for(InstalledPart p : product.getDerivedPartList()){
			Logger.getLogger().debug("part:" + p.getId().getPartName());
			
			boolean torqueStatus = prepareTorquesForDerivedParts(p, partIndex, state);
			p.setAssociateNo(context.getUserId());
			p.setProcessPointId(context.getProcessPointId());

			if(torqueStatus && p.isValidPartSerialNumber() && !p.isSkipped()) {
				p.setInstalledPartStatus(InstalledPartStatus.OK);
				p.setInstalledPartReason(getProperty().getInstalledPartReason());
			}
			else
				p.setInstalledPartStatus(InstalledPartStatus.NG);

			partIndex++;

			resultList.add(p);
		}
		return resultList;
	}
	


	@SuppressWarnings("null")
	private boolean prepareTorquesForDerivedParts(InstalledPart p, int partIndex, DataCollectionState state) {
		PartSpec partSpec= new PartSpec();
		if(p.getId() == null && p.getMeasurements() == null ) return true; //place holder for skipped part 
		
		Boolean torqueStatus = true;
		torqueCount = 0;
		if(saveMeasurementHistory)
			torqueStatus = prepareTorquesWithHistory(p, partIndex);
		else	
			torqueStatus = prepareTorques(p, partIndex);
		
		//false if collected less than required torques 
		PartSpecDao partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		List<PartSpec> partSpecs = partSpecDao.findAllByPartName(p.getPartName().trim());
		for(PartSpec pSpec: partSpecs){
			 partSpec = pSpec;
			 break;
		}
		if(partSpec!= null || torqueCount < partSpec.getMeasurementCount())
			torqueStatus = false;
			
		return torqueStatus;
		

	}

	private boolean prepareTorquesForPart(InstalledPart p, int partIndex) {
		
		if(p.getId() == null && p.getMeasurements() == null ) return true; //place holder for skipped part 
		
		Boolean torqueStatus = true;
		torqueCount = 0;
		if(saveMeasurementHistory)
			torqueStatus = prepareTorquesWithHistory(p, partIndex);
		else	
			torqueStatus = prepareTorques(p, partIndex);
		
		//false if collected less than required torques 
		if(torqueCount < lotControlRules.get(partIndex).getParts().get(0).getMeasurementCount())
			torqueStatus = false;
		
		return torqueStatus;
		

	}

	private boolean prepareTorquesWithHistory(InstalledPart p, int partIndex) {

		Boolean result = true;
		Measurement lastTorqueInSequence = null;
		if(p.getMeasurements() != null && p.getMeasurements().size() > 0) {

			for(Measurement m : p.getMeasurements())
			{
				if (m.getId().getMeasurementSequenceNumber() == torqueCount) {
					lastTorqueInSequence = m;
				}
				
				if(m.getId().getMeasurementSequenceNumber() > torqueCount){

					result = updateTorqueStatus(lastTorqueInSequence, result);
					
					lastTorqueInSequence = m;
					torqueCount++;
					
				}
			}

			//save the last torque in sequence
			result = updateTorqueStatus(lastTorqueInSequence, result);
		}
	
		return result;
	}

	private Boolean updateTorqueStatus(Measurement lastTorqueInSequence, Boolean torqueStatus) {
		if (lastTorqueInSequence != null) {
			Logger.getLogger().debug(LotControlPartUtil.toString(lastTorqueInSequence));
			
			if(lastTorqueInSequence.getMeasurementStatus() != MeasurementStatus.OK)
				return false;
		}
		return torqueStatus;
	}

	/**
	 * Prepare torque data to save into database to contain last torques only
	 * @param p
	 * @param partIndex
	 * @return
	 */
	private boolean prepareTorques(InstalledPart p,	int partIndex) {
		Boolean result = true;
		
		if(p.getMeasurements() != null && p.getMeasurements().size() > 0) {

			List<Measurement> torques = new ArrayList<Measurement>();
			torques.addAll(p.getMeasurements());
			p.getMeasurements().clear();
			Measurement lastTorqueInSequence = null;

			for(Measurement m : torques)
			{
				if (m.getId().getMeasurementSequenceNumber() == torqueCount) {
					lastTorqueInSequence = m;
				} else if(m.getId().getMeasurementSequenceNumber() > torqueCount){

					result = addLastTorqueAndUpdateStatus(p, result, lastTorqueInSequence);

					lastTorqueInSequence = m;
					torqueCount++;
					
				}
			}

			//save the last torque in sequence
			result = addLastTorqueAndUpdateStatus(p, result, lastTorqueInSequence);
		}
	
		return result;
	}

	private boolean addLastTorqueAndUpdateStatus(InstalledPart p, Boolean torqueStatus, Measurement lastTorqueInSequence) {
		
		if(lastTorqueInSequence != null) {
			
			Logger.getLogger().debug(LotControlPartUtil.toString(lastTorqueInSequence));
			
			p.getMeasurements().add(lastTorqueInSequence);
			
			if (lastTorqueInSequence.getMeasurementStatus() != MeasurementStatus.OK)
				torqueStatus = false;
		}
		
		return torqueStatus;
	}


	public synchronized BaseProduct confirmProductOnServer(String productId) {
		return (BaseProduct) ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findBySn(productId);
	}
	
	public void message(DataCollectionState state) {
		if (state.getMessage() == null)
			return;
		
		if(	StatusMessage.SERVER_ON_LINE.equals(state.getMessage().getId()) &&
				context.getProperty().isCheckExpectedProductId() &&
				StringUtils.isEmpty(state.getExpectedProductId()) &&
				state instanceof ProcessProduct) {
			getExpectedProductManger().getExpectedProductId((ProcessProduct)state);
		}
	}
	

	public void completePart(ProcessPart state) {
		
	}
	private void saveSkippedProduct(ProcessProduct state) {

		SkippedProductDao dao = ServiceFactory.getDao(SkippedProductDao.class);
		SkippedProduct skippedProduct = dao.findByKey(new SkippedProductId(getSkippedProductId(state), context.getProcessPointId()));
		
		if(skippedProduct == null){
			skippedProduct = getSkippedProduct(state);
			skippedProduct.setStatus(SkippedProductStatus.SKIPPED);
			Logger.getLogger().info("Save skipped product:", skippedProduct.getId().getProductId());
			
		} else {
			//this is the case the product has been processed by diabled expected and later on to skip the product now
			//we would take this as completed status
			if(skippedProduct.getStatus() == SkippedProductStatus.DISABLED){
				skippedProduct.setStatus(SkippedProductStatus.COMPLETED);
				EventBus.publish(skippedProduct);
				StringBuilder msg = new StringBuilder("Updated disabled product:").append(skippedProduct.getId().getProductId()).append(" to completed.");
				Logger.getLogger().info(msg.toString());
				//state.setMessage(new Message(null, msg.toString(),MessageType.INFO));
			}
		}
		
		skippedProduct.setSkipTimestamp(new Timestamp(System.currentTimeMillis()));
		dao.save(skippedProduct);
	}

	private void saveDisableExpected(ProcessProduct state) {
		SkippedProductDao dao = ServiceFactory.getDao(SkippedProductDao.class);
		SkippedProduct skippedProduct = dao.findByKey(new SkippedProductId(state.getProductId(), context.getProcessPointId()));
		if(skippedProduct == null){
			skippedProduct = getSkippedProduct(state);
			skippedProduct.setStatus(SkippedProductStatus.DISABLED);
			skippedProduct.setDisableTimestamp(new Timestamp(System.currentTimeMillis()));
			dao.save(skippedProduct);
			Logger.getLogger().info("Save disabled product:", skippedProduct.getId().getProductId());
		} else {
			skippedProduct.setStatus(SkippedProductStatus.COMPLETED);
			skippedProduct.setDisableTimestamp(new Timestamp(System.currentTimeMillis()));
			dao.update(skippedProduct);
			EventBus.publish(skippedProduct);
			Logger.getLogger().info("Updated skipped product:", skippedProduct.getId().getProductId(), " to completed");
		}
	}

	protected SkippedProduct getSkippedProduct(ProcessProduct state) {
		SkippedProduct product = new SkippedProduct(new SkippedProductId(getSkippedProductId(state), context.getProcessPointId()));
		product.setSubId(state.getExpectedSubId());
		product.setKdLotNumber(context.getKdLot());
		product.setProductionLot(context.getProductionLot());
		product.setProductSpecCode(getProductSpecCode(state));
		product.setProductType(context.getProperty().getProductType());
		return product;
	}
	
	protected String getSkippedProductId(DataCollectionState state){
		if(context.getProperty().isCheckExpectedProductId())
			return state.getExpectedProductId();
		else
			return state.getProductId();
	}

	protected String getProductSpecCode(ProcessProduct state) {
		if(!StringUtils.isEmpty(state.getProductSpecCode())) 
				return state.getProductSpecCode();
		else if(context.getCurrentPreProductionLot() != null)
			return context.getCurrentPreProductionLot().getProductSpecCode();
		else 
			return null;
	}
	
	public  List<? extends BaseProduct> findProductOnServer(String productId)
	{
		try
		{
			if(!context.getProperty().getProductType().equals(ProductType.ENGINE.toString()))
			{
				Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
				return null;
			}
			
			EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
			
			List<Engine> test = (List<Engine>) engineDao.findAllBySN(productId);
			return test;
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}
	
	public void initPart(ProcessPart state) {
	}
	
	public Product createProduct(Product product) {
		return product;
	}

	
	public List<? extends Product> findProductByPartName(String lineId,
			int prePrintQty, int maxPrintCycle, String ppid, String partName) {
		List<? extends Product> test = new ArrayList<Product>();
		if(context.getProperty().getProductType().equals(ProductType.ENGINE.toString())){
			EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
			test = (List<Engine>) engineDao.findByPartName(lineId, prePrintQty, maxPrintCycle, ppid, partName);
		}
		else if(context.getProperty().getProductType().equals(ProductType.IPU.toString())){
			SubProductDao subproductDao = ServiceFactory.getDao(SubProductDao.class);
			 test = (List<SubProduct>) subproductDao.findByPartName(lineId, prePrintQty, maxPrintCycle, ppid, partName);
		}
		else if(context.getProperty().getProductType().equals(ProductType.FRAME.toString())){
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			 test = (List<Frame>) frameDao.findByPartName(lineId, prePrintQty, maxPrintCycle, ppid, partName);
		}
		return test;
	}
	
	public void completeTorque(ProcessTorque state) {
	}
}
