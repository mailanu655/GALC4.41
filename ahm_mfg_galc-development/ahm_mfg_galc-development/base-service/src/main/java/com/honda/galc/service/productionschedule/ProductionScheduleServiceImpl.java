/**
 * 
 */
package com.honda.galc.service.productionschedule;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DownloadLotSequenceDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.DownloadLotSequenceId;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ProductionScheduleService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ProductionScheduleServiceImpl.java</h3> <h3>Class description</h3> <h4>
 * Description</h4>
 * <p>
 * ProductionScheduleServiceImpl.java description
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
 * @author KMaharjan-vfc01499 Apr 10, 2014
 * 
 */
public class ProductionScheduleServiceImpl implements ProductionScheduleService {

	protected PreProductionLotDao preProductionLotdao;
	protected DataContainer retList = new DefaultDataContainer();
	protected String processPointId = null;
	protected String processLocation = null;
	protected String demandType = null;
	protected String startLotNumber = null;
	protected String planCode = null;
	protected int stampedCount = 0;
	protected String productionLot = null;
	protected int noOfLots = 1;
	protected boolean isListUpdated = false;
	protected int lotsize = 0;
	protected String specCodeFromBuildAttr=null;

	private static final String REQUESTED_NO_OF_LOTS_UNAVAILABLE_CODE = "40";
	private static final String REQUESTED_NO_OF_LOTS_UNAVAILABLE_MSG = "Requested no. of lots not available.";
	private static final String RECV_PROD_DOES_NOT_MATCH_EXP_PROD_CODE = "41";
	private static final String RECV_PROD_DOES_NOT_MATCH_EXP_PROD_MSG = "Received product id does not match expected product id.";

	public void init(DataContainer dc) {
		retList.clear();
		processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name().trim());
		processLocation = dc.getString(TagNames.PROCESS_LOCATION.name().trim());
		startLotNumber = dc.getString(TagNames.LOT_NUMBER.name()).trim();
		noOfLots = Integer
				.valueOf(dc.getString(TagNames.NUMBER_OF_LOTS.name()));
	}

	public DataContainer getNextProductionScheduleByLotNumber(
			DefaultDataContainer dc) {
		try {
			init(dc);
			HashMap<String, Object> tempMap = null;
			List<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();

			getLogger().info("collector received data for getnextProductionScheduleByLotNumber:", dc.toString());
			if (!validProcessLocation()) {
				addError("40",
						"Datacontainer contains invalid Process location");
				return retList;
			}
			// from 489 table
			Map<String, List<String>> filterBuildAttributes = new HashMap<String, List<String>>(); 
			List<String> attributeIDsList = PropertyService.getPropertyList(
					processPointId, "FILTER_BUILD_ATTRIBUTE", ",");
			if (!attributeIDsList.isEmpty()) {
				for (String attributeId : attributeIDsList) {
					filterBuildAttributes.put(attributeId, PropertyService
							.getPropertyList(processPointId, attributeId, ","));
				}
			}
			
			if(isCheckSubProductSpec()){
				specCodeFromBuildAttr = PropertyService.getProperty(processPointId, "SPEC_CODE_FROM_BUILD_ATTR_MAP", "");
				if(StringUtils.isEmpty(specCodeFromBuildAttr)){
					addError("40",
							"Required Property SPEC_CODE_FROM_BUILD_ATTR_MAP is not setup for this process");
					return retList;
				}
			}

			DownloadLotSequence ep = ServiceFactory.getDao(
					DownloadLotSequenceDao.class)
					.findAllByProcessPointAndProcessLocation(processLocation,
							processPointId);
			
			if (ep == null) {
				ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class)
						.findById(processPointId);
				if (pp != null) {
					ep = new DownloadLotSequence();
					DownloadLotSequenceId id = new DownloadLotSequenceId();
					id.setProcessLocation(processLocation);
					id.setProcessPointId(processPointId);
					ep.setId(id);
					ServiceFactory.getDao(DownloadLotSequenceDao.class)
							.save(ep);
				} else {
					addError("40",
							"Datacontainer contains invalid Process Point");
					return retList;
				}
			}

			getLogger().info("Calculating the Production Schedule List");
			if (noOfLots == 0 && startLotNumber.equals("")) {
				PreProductionLot firstLot = getFirstAvailableProductionLot(processLocation);
				if (firstLot != null) {
					tempMap = createOutput(firstLot);
					temp.add(tempMap);
					retList.put("PRODUCTION_SCHEDULE_LIST", temp);
				} else {
					addError("42", "Empty Table");
					return retList;
				}
			}else if(noOfLots == 0 && !startLotNumber.equals("")){
				if (ep.getEndProductionLot() != null) {
					String currentProductionLot="";
					try{
						currentProductionLot = getPreProductionLotDao()
							.getCurrentPreProductionLotByLotNumber(
									startLotNumber, processLocation)
							.getProductionLot();
					}catch(Exception e){
						addError("42", "Invalid Lot Number");
						return retList;
					}
					if (isCheckExpectedProductionLot(processPointId)) {
						if (!currentProductionLot.equals(ep
								.getEndProductionLot())) {
							addError("40",
									"Received Lot Number does not match with End Production Lot");
							return retList;
						}
					}
					
					PreProductionLot currentLot = getPreProductionLotDao()
							.getCurrentPreProductionLotByLotNumber(startLotNumber,
									processLocation);
					if (currentLot == null) {
						addError("42", "Invalid Lot Number");
						return retList;
					}
					tempMap = createOutput(currentLot);
					temp.add(tempMap);
					retList.put("PRODUCTION_SCHEDULE_LIST", temp);
				}
			}else{			
				PreProductionLot currentLot = getPreProductionLotDao()
						.getCurrentPreProductionLotByLotNumber(startLotNumber,
								processLocation);
				getLogger().info("currentLot : "+ currentLot);
				if (currentLot == null) {
					addError("42", "Invalid Lot Number");
					return retList;
				}
				PreProductionLot iterLot = currentLot;
				int counter = 0;
				boolean isMatch = false;
				while (counter != noOfLots) {
					isMatch = false;
					PreProductionLot tempLot = null;
					if (noOfLots < 0) {
						// -ve no. of Lots. look for the previous Production Lot
						tempLot = iterLot != null ? getPreProductionLotDao()
								.findParent(iterLot.getProductionLot()) : null;
					} else if (noOfLots > 0) {
						// +ve no. of Lots. look for the next Production Lot
						tempLot = iterLot != null ? getPreProductionLotDao()
								.findNext(iterLot.getProductionLot()) : null;
					}
					if (tempLot != null) {

						iterLot = tempLot;
						if (tempLot.getHoldStatus() != 0) {

							// check for build attribute
							Set<BuildAttribute> buildAttributeSet = new HashSet<BuildAttribute>(); // from 259 table
							if (!filterBuildAttributes.isEmpty()) {
								Set<String> filterAttributeIDSet = filterBuildAttributes
										.keySet();
								BuildAttributeCache bac = new BuildAttributeCache();
								for (String attributeId : filterAttributeIDSet) { // for each attribute id from the 489 table
									BuildAttribute ba = bac.findById(
											tempLot.getProductSpecCode(),
											attributeId); // get the build attribute from 259 table
									if (ba != null)
										buildAttributeSet.add(ba);
								}
								if (!buildAttributeSet.isEmpty()) {
									for (BuildAttribute ba : buildAttributeSet) {
										String attributeID = ba.getId()
												.getAttribute();
										String attributeVal = ba
												.getAttributeValue();
										List<String> filterAttributeVals = filterBuildAttributes
												.get(attributeID);
										if (filterAttributeVals
												.contains(attributeVal)) {
											isMatch = true;
											break;
										}
									}
								}
							}

							if (isMatch)
								continue;

							tempMap = createOutput(tempLot);
							if (tempMap == null) {
								return retList;
							}
							temp.add(tempMap);
							if (noOfLots < 0) {
								counter--;
							} else if (noOfLots > 0) {
								counter++;
							}
						}
					} else {
						// requested lots not available
						addError(REQUESTED_NO_OF_LOTS_UNAVAILABLE_CODE,
								REQUESTED_NO_OF_LOTS_UNAVAILABLE_MSG);
						return retList;
					}
				}

				retList.put("PRODUCTION_SCHEDULE_LIST", temp);
			}
			if (ep != null) {
				ep.setEndProductionLot(tempMap.get(
						TagNames.PRODUCTION_LOT.name()).toString());
				ServiceFactory.getDao(DownloadLotSequenceDao.class).save(ep);
				retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(),
						LineSideContainerValue.COMPLETE);
			}

		} catch (Exception ex) {
			getLogger().error(ex, "Exception to collect data for ",
					this.getClass().getSimpleName());
			addError("42",
					"Exception: Production Schedule Download execute exception");
		}
		getLogger().info("Production Schedule Download Successful: " + retList);
		return retList;
	}

	/**
	 * Description
	 * 
	 * @return	boolean 
	 */
	private boolean isCheckSubProductSpec() {
		return PropertyService.getPropertyBoolean(processPointId, "CHECK_SUB_PRODUCT_SPEC", false);
	}

	private boolean validProcessLocation() {
		int count = 0;
		try {
			count = getPreProductionLotDao().countByPlantLineLocation(
					PropertyService.getSiteName(),
					PropertyService.getAssemblyLineId(), processLocation);
		} catch (Exception e) {
			return false;
		}
		if (processLocation.equals("") && count == 0)
			return false;
		return true;
	}

	public PreProductionLot getFirstAvailableProductionLot(
			String processLocation) {
		PreProductionLot preProdLot = null;
		PreProductionLot tempPreProdLot = null;
		PreProductionLot retPreProdLot = null;
		preProdLot = getPreProductionLotDao()
				.findLastPreProductionLotByProcessLocation(processLocation);
		if (preProdLot != null) {
			retPreProdLot = preProdLot;
			int sendStatus = preProdLot.getSendStatusId();
			while (sendStatus < 2) {
				tempPreProdLot = getPreProductionLotDao().findParent(
						preProdLot.getProductionLot());
				retPreProdLot = preProdLot;
				if (tempPreProdLot == null)
					break;
				sendStatus = tempPreProdLot.getSendStatusId();
				preProdLot = tempPreProdLot;
			}
			getLogger().info(
					"Successful getFirstAvailableProductionLot(): "
							+ retPreProdLot);
			return retPreProdLot;
		} else
			return null;
	}

	public DataContainer getNextProductionSchedule(DefaultDataContainer dc) {
		init(dc);
		getLogger().info("collector received data for getNextProductionSchedule:", dc.toString());
		String currentStartProductId = dc.getString(TagNames.START_PRODUCT_ID
				.name());
		HashMap<String, Object> tempMap = null;
		List<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
		
		if(isCheckSubProductSpec()){
			specCodeFromBuildAttr = PropertyService.getProperty(processPointId, "SUB_ASSEMBLY_SPEC_CODE", "");
			if(StringUtils.isEmpty(specCodeFromBuildAttr)){
				addError("40",
						"Required Property SUB_ASSEMBLY_SPEC_CODE is not setup for this process");
				return retList;
			}
		}		
		
		ExpectedProduct ep = ServiceFactory.getDao(ExpectedProductDao.class)
				.findForProcessPointAndProduct(processPointId,
						currentStartProductId);
		if (ep == null) {
			// received product id does not match expected product id
			addError(RECV_PROD_DOES_NOT_MATCH_EXP_PROD_CODE,
					RECV_PROD_DOES_NOT_MATCH_EXP_PROD_MSG);
			return retList;
		}
		getLogger().info("Calculating the Production Schedule List");
		PreProductionLot currentLot = getPreProductionLotDao()
				.getCurrentPreProductionLotByStartProductId(
						currentStartProductId);

		if (noOfLots == 0) {
			tempMap = createOutput(currentLot);
			retList.put("PRODUCTION_SCHEDULE_LIST", tempMap);
			return retList;
		}
		PreProductionLot iterLot = currentLot;
		int counter = 0;
		while (counter < noOfLots) {
			PreProductionLot tempLot = iterLot != null ? getPreProductionLotDao()
					.findNext(iterLot.getProductionLot()) : null;
			if (tempLot != null) {

				iterLot = tempLot;
				if (tempLot.getHoldStatus() != 0) {
					tempMap = createOutput(tempLot);

					temp.add(tempMap);
					counter++;
				}
			} else {
				// requested lots not available
				addError(REQUESTED_NO_OF_LOTS_UNAVAILABLE_CODE,
						REQUESTED_NO_OF_LOTS_UNAVAILABLE_MSG);
				return retList;
			}
			retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(),
					LineSideContainerValue.COMPLETE);
		}
		retList.put("PRODUCTION_SCHEDULE_LIST", temp);
		// update 135 table
		ep.setProductId(tempMap.get(TagNames.START_PRODUCT_ID.name())
				.toString());
		ServiceFactory.getDao(ExpectedProductDao.class).save(ep);
		getLogger().info("Production Schedule Download Successful: " + retList);
		return retList;
	}

	public HashMap<String, Object> createOutput(PreProductionLot productionLot) {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap.put(TagNames.PRODUCTION_LOT.name(),
					productionLot.getProductionLot());
			tempMap.put(TagNames.PRODUCT_SPEC_CODE.name(),
					productionLot.getProductSpecCode());
			tempMap.put(TagNames.START_PRODUCT_ID.name(),
					productionLot.getStartProductId());
			tempMap.put(TagNames.LOT_SIZE.name(), productionLot.getLotSize());
			tempMap.put(TagNames.LOT_NUMBER.name(),
					productionLot.getLotNumber());
			tempMap.put(TagNames.KD_LOT.name(),
					productionLot.getKdLot());
			tempMap.put(TagNames.DEMAND_TYPE.name(),
					productionLot.getDemandType());
			// get the mission type i.e. TRANSMISSION field from GAL133TBX
			EngineSpec es = ServiceFactory.getDao(EngineSpecDao.class)
					.findByKey(productionLot.getProductSpecCode());
			String missionType = es != null && es.getTransmission() != null ? es
					.getTransmission() : "N";
			tempMap.put(TagNames.MISSION_TYPE.name(), missionType);
			
			//Get SubAssy Spec Code from BuildAttribute if configured
			String subProductSpecCode = "";
			BuildAttributeCache bc  = new BuildAttributeCache();
			if(isCheckSubProductSpec()){				
				subProductSpecCode = bc.findAttributeValue(productionLot.getProductSpecCode(), specCodeFromBuildAttr);
				if(StringUtils.isEmpty(subProductSpecCode)) {
					getLogger().info("Build Attribute("+specCodeFromBuildAttr+") is not set for this spec: " + productionLot.getProductSpecCode());
					throw new Exception("Build Attribute("+specCodeFromBuildAttr+") is not set for this spec: " + productionLot.getProductSpecCode());
				}
				//With the change to allow for comma delimited value in the build attribute need to change these to check if it is a comma delimited list.  If it is a comma delimited list then we should get the first spec in the list.  This would allow it to assign a spec and should match when doing the marriage validation.
				if(subProductSpecCode.contains(Delimiter.COMMA)) {
					String[] split = subProductSpecCode.split(Delimiter.COMMA);
					subProductSpecCode=split[0];
				}
				tempMap.put(TagNames.SUBASSY_SPEC_CODE.name(),	subProductSpecCode);
			}
			//get other build attribute values if configured
			List<String> attributeIDsList = PropertyService.getPropertyList(processPointId, "OTHER_BUILD_ATTRIBUTES", ",");
			if (!attributeIDsList.isEmpty()) {
				for (String attributeId : attributeIDsList) {
					tempMap.put(attributeId,bc.findAttributeValue(productionLot.getProductSpecCode(), attributeId));
				}
			}
			
			if(isMergedLot(processPointId) && isCheckSubProductSpec()){
				PreProductionLot mergedProductionLot1 = getPreProductionLotDao().getMergedSubAssyPreProductionLotByBuildAttribute(
						productionLot,specCodeFromBuildAttr,isNextProductionLot(processPointId));
				if (mergedProductionLot1!=null) {
					tempMap.put(TagNames.LAST_MERGED_LOT_NUMBER.name(),	mergedProductionLot1.getLotNumber());
					tempMap.put(TagNames.LOT_SIZE.name(), mergedProductionLot1.getLotSize());
					tempMap.put(TagNames.LAST_MERGED_PRODUCTION_LOT.name(),mergedProductionLot1.getProductionLot());					
				} else {
					tempMap.put(TagNames.LAST_MERGED_LOT_NUMBER.name(),productionLot.getLotNumber());
					tempMap.put(TagNames.LAST_MERGED_PRODUCTION_LOT.name(),productionLot.getProductionLot());
				}
 			}else if(isMergedLot(processPointId)){
				PreProductionLot mergedProductionLot = getPreProductionLotDao().getMergedPreProductionLotBySpecCode(
						productionLot,isNextProductionLot(processPointId));
				if (mergedProductionLot!=null) {
					tempMap.put(TagNames.LAST_MERGED_LOT_NUMBER.name(),	mergedProductionLot.getLotNumber());
					tempMap.put(TagNames.LOT_SIZE.name(), mergedProductionLot.getLotSize());
					tempMap.put(TagNames.LAST_MERGED_PRODUCTION_LOT.name(),mergedProductionLot.getProductionLot());					
				} else {
					tempMap.put(TagNames.LAST_MERGED_LOT_NUMBER.name(),productionLot.getLotNumber());
					tempMap.put(TagNames.LAST_MERGED_PRODUCTION_LOT.name(),productionLot.getProductionLot());
				}
 			}else {
 				tempMap.put(TagNames.LAST_MERGED_LOT_NUMBER.name(),productionLot.getLotNumber());
 				tempMap.put(TagNames.LAST_MERGED_PRODUCTION_LOT.name(),productionLot.getProductionLot());
 			}
			
		} catch (Exception e) {
			addError("41", "Error creating the output");
			return null;
		}
		return tempMap;
	}

	public PreProductionLotDao getPreProductionLotDao() {
		if (preProductionLotdao == null)
			preProductionLotdao = ServiceFactory
					.getDao(PreProductionLotDao.class);
		return preProductionLotdao;
	}

	private boolean isCheckExpectedProductionLot(String pPID) {
		return PropertyService.getPropertyBoolean(pPID,
				"CHECK_EXPECTED_PRODUCTION_LOT", false);
	}

	private boolean isMergedLot(String pPID) {
		return PropertyService.getPropertyBoolean(pPID,
				"MERGED_LOT", false);
	}

	private boolean isNextProductionLot(String pPID) {
		return PropertyService.getPropertyBoolean(pPID,
				"NEXT_PRODUCTION_LOT", true);
	}

	public void addError(String code, String msg) {
		getLogger().error(msg);
		retList.put(TagNames.ERROR_CODE.name(), code);
		retList.put("ERROR_MSG", msg);
		retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(),
				LineSideContainerValue.NOT_COMPLETE);
	}

	public Logger getLogger() {
		if (processPointId == null || processPointId.equals(""))
			return Logger.getLogger("ProductionScheduleDownload");
		else
			return Logger.getLogger(processPointId);
	}
	
	public DataContainer updatePreproductionLot(DefaultDataContainer dc){
		try {
			retList.clear();
			
			processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name().trim());
			getLogger().info("collector received data for updatePreProductionLot from process("+processPointId +"): "+ dc.toString());
			
			startLotNumber = dc.getString(TagNames.LOT_NUMBER.name()).trim();
			lotsize = Integer.valueOf(dc.getString(TagNames.LOT_SIZE.name()));
			
			planCode = dc.getString(TagNames.PLAN_CODE.name().trim());
			if(StringUtils.isEmpty(planCode)) planCode=PropertyService.getProperty(processPointId, TagNames.PLAN_CODE.name().trim());
			
			processLocation = dc.getString(TagNames.PROCESS_LOCATION.name().trim());
			if(StringUtils.isEmpty(processLocation)) processLocation=PropertyService.getProperty(processPointId, TagNames.PROCESS_LOCATION.name().trim());
			
			stampedCount = Integer.valueOf(dc.getString(TagNames.STAMPED_COUNT.name()));
			if(stampedCount <= 0){
				addError("40", "Invalid Stamped Count. Should be greater than 0.");
				return retList;
			}
			
			PreProductionLot currentLot = getPreProductionLotDao().getCurrentPreProductionLotByLotNumber(startLotNumber, processLocation);
			if(currentLot == null){
				addError("40","Preproduction Lot is empty, so not update pre-production lot.");
				return retList;
			}
			
			if (currentLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
				String msg = "Can not be processed because Lot " + currentLot.getProductionLot() + " is On Hold.";
				addError("40",msg);
				return retList;
			}

			if(currentLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS){
				updateStampCount(currentLot);
				if(isLotCompleted(currentLot)) completeLot(currentLot);
			} else if(currentLot.getSendStatus() == PreProductionLotSendStatus.WAITING || currentLot.getSendStatus() == PreProductionLotSendStatus.SENT) {
				updateStampCount(currentLot);
				if(isLotCompleted(currentLot)) {	
					completeLot(currentLot);
				} else {
					startLot(currentLot);
				}				
			} else {
				updateStampCount(currentLot);
			}
			if(retList.containsKey(TagNames.ERROR_CODE.name())) return retList;
			if(lotsize != currentLot.getLotSize()){
				currentLot.setLotSize(lotsize);
				getPreProductionLotDao().update(currentLot);
			}
			currentLot.setStampedCount(stampedCount);
			
			retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			retList.put(TagNames.REMAINING_COUNT.name(), currentLot.getLotSize()-stampedCount);
			doBroadcast(currentLot);
		}catch(Exception e){
			retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}		
		return retList;
	}
	
	protected void updateStampCount(PreProductionLot lot){
		if(lot.getSendStatus() == PreProductionLotSendStatus.DONE){
			addError("40","Preprodution Lot:"+ lot.getProductionLot()+ " is already completed.");
			return;
		}
		
		getPreProductionLotDao().updateStampedCount(lot.getProductionLot(), stampedCount);
	}

	protected boolean isLotCompleted(PreProductionLot preProductionLot) {
		return stampedCount >= getLotSize(preProductionLot); 
	}
	
	private int getLotSize(PreProductionLot preProductionLot) {
		return preProductionLot.getLotSize() < lotsize ? lotsize : preProductionLot.getLotSize();
	}
		
	protected void startLot(PreProductionLot lot) {
		if(lot.getLotSize() > 1)
			getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.INPROGRESS.getId());
		else if(lot.getLotSize() == 1)
			getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());
	}
	
	protected void completeLot(PreProductionLot lot) {
		getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());		
	}
	
	/**
	 * Description
	 * @param currentLot 
	 * 
	 * @param void 
	 */
	private void doBroadcast(PreProductionLot currentLot) {
		try {
				for(BroadcastDestination destination: getService(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId, true)) {					
					getLogger().info("Beginning broadcast for: "+destination.getDestinationId());
					DataContainer dc = new DefaultDataContainer();
						dc.put(DataContainerTag.PRODUCTION_LOT, currentLot.getProductionLot());
						dc.put("STAMPED_COUNT",currentLot.getStampedCount());
						dc.put("LOT_SIZE",currentLot.getLotSize());
						dc.put("PROCESS_LOCATION",currentLot.getProcessLocation());
						dc.put("UPDATED_TIMESTAMP",currentLot.getUpdateTimestamp());
						dc.put("NEXT_PRODUCTION_LOT",currentLot.getNextProductionLot());
						dc.put("PLANT_CODE",currentLot.getPlantCode());
						dc.put("LOT_NUMBER",currentLot.getLotNumber());
						dc.put("LINE_NO",currentLot.getLineNo());
						dc.put("PRODUCT_SPEC_CODE",currentLot.getProductSpecCode());
						dc.put("PLAN_CODE",currentLot.getPlanCode());
						dc.put("KD_LOT_NUMBER",currentLot.getKdLotNumber());
						dc.put("DEMAND_TYPE",currentLot.getDemandType());
						dc.put("PROCESS_POINT_ID",processPointId);
					getLogger().info(String.format("Broadcasting %s at %s", destination.getDestinationId(), processPointId));
					getService(BroadcastService.class).broadcast(destination, processPointId, dc);
				}

			} catch (Exception e) {
				getLogger().error(e, "Failed to broadcast due to: " + e.toString());			
			} 
	}
	
	public DataContainer syncProductionLot(String processPointId, List<PreProductionLot> productionLots){
		try {			
			retList.clear();
			
			isListUpdated = false;			
			this.processPointId = processPointId;
			getLogger().info("collector received data for SyncProductionLot from process("+processPointId +"): "+ productionLots.toString());
			
			planCode=PropertyService.getProperty(processPointId, TagNames.PLAN_CODE.name().trim());
			processLocation=PropertyService.getProperty(processPointId, TagNames.PROCESS_LOCATION.name().trim());
			demandType =PropertyService.getProperty(processPointId, TagNames.DEMAND_TYPE.name().trim(),"SP");
			PreProductionLot previousLot = null;
			PreProductionLot currentLot = null;
			
			for(PreProductionLot productionLot : productionLots){
				getLogger().info("Lot Number: "+productionLot.getLotNumber() +", processLocation: "+processLocation);
				currentLot = getPreProductionLotDao().getCurrentPreProductionLotByLotNumber(productionLot.getLotNumber(), processLocation);
				if(currentLot==null && previousLot == null) {
					getLogger().info("First Lot can't be new: ", productionLot.getLotNumber());
					throw new Exception();
				}
				//firstTime run
				if(currentLot!=null && previousLot == null) {
					previousLot = currentLot;
					getLogger().info("Lot is good: "+ currentLot.getProductionLot());
					continue;
				}
				
				if(currentLot==null && previousLot!=null){				
					createNewLot(productionLot, previousLot);
					currentLot = getPreProductionLotDao().getCurrentPreProductionLotByLotNumber(productionLot.getLotNumber(), processLocation);
				}else if(currentLot!=null && !previousLot.getNextProductionLot().equals(currentLot.getProductionLot())){
					UpdateLot(currentLot,previousLot);
				}else{
					getLogger().info("Lot is good: "+currentLot.getProductionLot());					
				}
				previousLot = currentLot;
			}
			
			retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			retList.put(TagNames.DATA_UPDATED.name(), isListUpdated);
		}catch(Exception e){
			getLogger().error(e.toString());
			retList.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			retList.put(TagNames.DATA_UPDATED.name(), isListUpdated);
		}		
		return retList;
	}
	
	/**
	 * Description
	 * 
	 * @param currentLot
	 * @param previousLot	
	 * void 
	 * @throws Exception 
	 */
	private void UpdateLot(PreProductionLot currentLot,	PreProductionLot previousLot) throws Exception {
		String tempNextProductionLotId = previousLot.getNextProductionLot();
		if(currentLot.getNextProductionLot()==null){
			currentLot.setNextProductionLot(tempNextProductionLotId);
			currentLot.setHoldStatus(PreProductionLotStatus.RELEASE.getId());
			getPreProductionLotDao().update(currentLot);
		}else{
			String parentProductionLotId = getPreProductionLotDao().findParent(currentLot.getProductionLot()).getProductionLot();
			List<PreProductionLot> toHoldLots =new ArrayList<PreProductionLot>();
			while(true){
				PreProductionLot holdLot = getPreProductionLotDao().findByKey(parentProductionLotId);
				if(holdLot.getSendStatusId()==PreProductionLotSendStatus.DONE.getId() 
						&& !StringUtils.equals(tempNextProductionLotId, parentProductionLotId)){
					toHoldLots.clear();
					break;
				}
				holdLot.setNextProductionLot(null);
				holdLot.setHoldStatus(PreProductionLotStatus.HOLD.getId());			
				toHoldLots.add(holdLot);				
				if(StringUtils.equals(tempNextProductionLotId, parentProductionLotId)){
					break;
				}
				parentProductionLotId = getPreProductionLotDao().findParent(holdLot.getProductionLot()).getProductionLot();
			}
			
			if(!toHoldLots.isEmpty()){
				getPreProductionLotDao().updateAll(toHoldLots);
				getLogger().info("Skipped Lots put on hold: "+ toHoldLots.toString());
			}
			else throw new Exception("Lot are not in sync so no lots are updated");
		}
		
		previousLot.setNextProductionLot(currentLot.getProductionLot());
		getPreProductionLotDao().update(previousLot);
		getLogger().info("Lot updated "+currentLot.getProductionLot() +", "+previousLot.getProductionLot());
		isListUpdated = true;
	}

	/**
	 * Description
	 *  
	 * @param productionLot 
	 * @param previousLot 
	 * void
	 */
	private void createNewLot(PreProductionLot productionLot, PreProductionLot previousLot) {
		PreProductionLot newLot = new PreProductionLot();
		String productionLotString = createPrefix(previousLot) + productionLot.getLotNumber();
		newLot.setProductionLot(productionLotString);
		newLot.setLotSize(productionLot.getLotSize());
		newLot.setProductSpecCode(productionLot.getProductSpecCode());
		newLot.setNextProductionLot(previousLot.getNextProductionLot());
		newLot.setLotNumber(productionLot.getLotNumber());
		newLot.setProcessLocation(processLocation);
		newLot.setPlanCode(planCode);
		newLot.setLineNo(previousLot.getLineNo());
		newLot.setPlantCode(previousLot.getPlantCode());
		newLot.setKdLotNumber(rightPadding(previousLot.getPlantCode(), 4) + previousLot.getLineNo() + productionLot.getLotNumber());
		newLot.setDemandType(demandType);	
		newLot.setHoldStatus(PreProductionLotStatus.RELEASE.getId());
		
		getPreProductionLotDao().save(newLot);		
		getLogger().info("New Lot Created: ", newLot.toString());
		
		previousLot.setNextProductionLot(productionLotString);
		getPreProductionLotDao().update(previousLot);
		getLogger().info("NextproductionLot updated for: ", previousLot.getProductionLot());
		isListUpdated = true;
	}

	/**
	 * Description
	 * @param previousLot 
	 * 
	 * @return	String 
	 */
	private String createPrefix(PreProductionLot previousLot) {
		return rightPadding(previousLot.getPlantCode(), 4) + previousLot.getLineNo() + previousLot.getProcessLocation();
	}   
	
	public static String rightPadding(String str, int num) {
	    return String.format("%1$-" + num + "s", str);
	  }

}
