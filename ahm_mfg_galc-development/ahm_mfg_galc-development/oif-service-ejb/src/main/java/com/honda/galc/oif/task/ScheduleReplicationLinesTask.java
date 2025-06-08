package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequenceId;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.service.ProductStampingSeqGenService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.SubproductUtil;

/**
 * @author Lalit Shahani
 *
 */
public class ScheduleReplicationLinesTask extends OifTask<Object> implements IEventTaskExecutable {

	private static final String LAST_RUN_PRODUCTION_LOT = "LAST_RUN_PRODUCTION_LOT";
	private static final String REBUILD_SCHEDULE = "REBUILD_SCHEDULE";
	private static final String NONE_METHOD = "NONE";
	private static final String CREATE_METHOD = "CREATE";
	private static final String DEFAULT_MBPN_COMBINATION = "DEFAULT_MBPN_COMBINATION";
	private static final String DEFAULT_MBPN_COMBINATION_CODE = "DEFAULT_MBPN_COMBINATION_CODE";
	private PreProductionLotDao preProductionLotDao;
	private ProductStampingSequenceDao productStampingSequenceDao;
	private ComponentPropertyDao componentPropertyDao;
	private MbpnProductDao mbpnProductDao;
	private ProductionLotMbpnSequenceDao productionLotMbpnSequenceDao;
	private BuildAttributeDao buildAttributeDao;
	private HashMap<String,PreProductionLot>rebuiltLots = new HashMap<String,PreProductionLot>();
	private PreProductionLot lastLotInTarget = null;
	private Double maxTargetPlanSeqNum = 0d;
	private boolean  generateSeqNum;
	int noOfSavedLots = 0;
	int noOfSourceLots = 0;
	private ReplicateScheduleProperty propBean;

	private enum RunningStatus {FINISHED, RUNNING};

	private String productType = "";

	private String noReplication = "NOREP";

	private enum RebuildSchedule {OFF, ONCE, ALWAYS};

	public ScheduleReplicationLinesTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {

		try {
			Timestamp now = getCurrentTime(true);
			if (findRunningStatusProperty().equals(RunningStatus.RUNNING)) {
				logger.warn("Instance of Schedule Replication is already running.");				
			} else {
				updateRunningStatusProperty(RunningStatus.RUNNING);
				refreshProperties();
				getReplicatePropertyService();
				errorsCollector = new OifErrorsCollector(componentId);
				replicateSchedule();
				updateRunningStatusProperty(RunningStatus.FINISHED);
				updateLastProcessTimestamp(now);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Exception from schedule replication.");
			errorsCollector.error(ex.toString(), "Exception from schedule replication.");
			updateRunningStatusProperty(RunningStatus.FINISHED);
		} finally {
			errorsCollector.sendEmail();
		}
	}

	private ReplicateScheduleProperty getReplicatePropertyService() {
		return propBean == null ? propBean = PropertyService.getPropertyBean(ReplicateScheduleProperty.class, componentId) : propBean;
	}
	
	private void replicateSchedule() {

		try {
			Map<String,String> sourcePlanCodeMap=getReplicatePropertyService().getSourcePlanCode();
			Map<String,String> sourceProcessLocationMap=getReplicatePropertyService().getSourceProcessLocation();
			Map<String,String> targetPlanCodeMap=getReplicatePropertyService().getTargetPlanCode();
			Map<String,String> targetProcessLocationMap=getReplicatePropertyService().getTargetProcessLocation();
			generateSeqNum=getReplicatePropertyService().isGenerateSeqNum();
			Map<String,String> useProductionLotMbpnSequenceMap=getReplicatePropertyService().getUseProductionLotMbpnSequence();


			if (sourcePlanCodeMap.isEmpty() || sourceProcessLocationMap.isEmpty() || targetPlanCodeMap.isEmpty() || targetProcessLocationMap.isEmpty()) {
				logger.error("SOURCE_PLAN_CODE, SOURCE_PROCESS_LOCATION, TARGET_PLAN_CODE and TARGET_PROCESS_LOCATION property map is not set up.");
				return;

			}
			for (Map.Entry<String,String> sourcePlanCodeMapEntry : sourcePlanCodeMap.entrySet()) {

				String sourcePlanCodeMapKey = sourcePlanCodeMapEntry.getKey();
				String sourcePlanCode = sourcePlanCodeMapEntry.getValue();
				String sourceProcessLocation = sourceProcessLocationMap.get(sourcePlanCodeMapKey);
				String targetPlanCodesString = targetPlanCodeMap.get(sourcePlanCodeMapKey);
				String targetProcessLocationsString = targetProcessLocationMap.get(sourcePlanCodeMapKey);
				String createIdsMethod = getProperty("CREATE_ID_METHOD{"+sourcePlanCodeMapKey+"}", "REPLICATION");
				final boolean useProductionLotMbpnSequence = (useProductionLotMbpnSequenceMap != null) && (useProductionLotMbpnSequenceMap.containsKey(sourcePlanCodeMapKey) && Boolean.valueOf(useProductionLotMbpnSequenceMap.get(sourcePlanCodeMapKey)));

				if (sourcePlanCode==null || sourceProcessLocation == null || targetPlanCodesString == null || targetProcessLocationsString == null || createIdsMethod==null) {
					logger.error("Property SOURCE_PLAN_CODE, SOURCE_PROCESS_LOCATION, TARGET_PLAN_CODE ,CREATE_ID_METHOD or TARGET_PROCESS_LOCATION is not set up for "+sourcePlanCode);
					continue;	
				}

				String[] targetPlanCodes = StringUtils.trim(targetPlanCodesString).split(",");
				String[] targetProcessLocations = StringUtils.trim(targetProcessLocationsString).split(",");

				if (targetPlanCodes.length == 0 || targetProcessLocations.length == 0) {
					logger.error("Property TARGET_PLAN_CODE or TARGET_PROCESS_LOCATION is not set up correctly.");
					continue;
				}

				ComponentPropertyId lastProdLotCompPropId = new ComponentPropertyId(componentId, LAST_RUN_PRODUCTION_LOT+"{"+sourcePlanCodeMapKey+"}");

				String rebuild = findRebuildScheduleProperty(sourcePlanCodeMapKey);

				if(!rebuild.equalsIgnoreCase(RebuildSchedule.OFF.toString())){

					rebuildSchedule(targetPlanCodes,targetProcessLocations,lastProdLotCompPropId,sourceProcessLocation);
					logger.info("Rebuilt Property for : " + sourcePlanCodeMapKey + " is set as : " +rebuild);
					if(rebuild.equalsIgnoreCase(RebuildSchedule.ONCE.toString())) {
						updateRebuildScheduleProperty(RebuildSchedule.OFF.toString(), sourcePlanCodeMapKey);
					}
				}

				List<PreProductionLot> sourcePreProductionLots=getUnProcessedSourceProductionLots(lastProdLotCompPropId,sourcePlanCode,sourcePlanCodeMapKey);

				if (sourcePreProductionLots == null || sourcePreProductionLots.size() == 0) {
					logger.warn("No valid Pre Production Lot to be replicated.");
					continue;
				}else {
					noOfSourceLots = sourcePreProductionLots.size();
				}

				String targetPlanCode = null;
				String targetProcessLocation = null;

				for (int i = 0; i < targetPlanCodes.length; i++) {

					targetPlanCode = StringUtils.trim(targetPlanCodes[i]);
					targetProcessLocation = StringUtils.trim(targetProcessLocations[i]);

					if (targetPlanCode.length() == 0 || targetProcessLocation.length() == 0) {
						continue;
					}

					if(!doesPlanCodeHaveTail(targetPlanCode)) {
						continue;
					}

					generateSchedule(targetPlanCode,targetProcessLocation,sourcePreProductionLots,sourceProcessLocation,createIdsMethod,useProductionLotMbpnSequence,lastProdLotCompPropId);
				}

			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Exception from schedule replication.");
			errorsCollector.error(ex.toString(), "Exception from schedule replication.");
		} finally {
			errorsCollector.getRunHistory().setSuccessCount(noOfSavedLots);
			errorsCollector.getRunHistory().setFailedCount(noOfSourceLots-noOfSavedLots);
			errorsCollector.sendEmail();
		}
	}

	private void generateSchedule(String targetPlanCode, String targetProcessLocation, List<PreProductionLot> sourcePreProductionLots, String sourceProcessLocation,String createIdsMethod, boolean useProductionLotMbpnSequence, ComponentPropertyId lastProdLotCompPropId) {

		HashMap<String,String> sourceTargetLotMap=new HashMap<String,String> ();
		PreProductionLot sourcePreProductionLot = null;
		PreProductionLot targetPreProductionLot = null;
		List<ProductStampingSequence> targetProductStampingSequences = new ArrayList<ProductStampingSequence>();
		List<ProductStampingSequence> sourceProductStampingSequences = null;
		Map<String, List<ProductStampingSequence>> sourceProductStampingSequenceMap = new HashMap<String, List<ProductStampingSequence>>();

		String targetProductionLot = null;

		boolean foundFirstNonHoldLot = false;
		PreProductionLot lastLotNotOnHold = lastLotInTarget;
		List<String> notHoldDemandTypes = Arrays.asList(getReplicatePropertyService().getNotHoldDemandTypes().split(","));
		if(generateSeqNum)
		{
			maxTargetPlanSeqNum=getPreProductionLotDao().findMaxSequence(targetPlanCode);
			if(maxTargetPlanSeqNum == null || maxTargetPlanSeqNum == 0d) {
				maxTargetPlanSeqNum = 1d;  
			} else {
				maxTargetPlanSeqNum += 1;
			}
		}
		for (int j = 0; j < sourcePreProductionLots.size(); j++) {
			sourcePreProductionLot = sourcePreProductionLots.get(j);
			targetProductionLot = buildProductionLotNumber(sourcePreProductionLot.getProductionLot(), sourceProcessLocation, targetProcessLocation);
			sourceTargetLotMap.put(targetProductionLot, sourcePreProductionLot.getProductionLot());
			targetPreProductionLot = populateTargetPreProdLot( sourcePreProductionLot,  sourceProcessLocation, 
					targetProcessLocation, targetPlanCode,targetProductionLot);

			if (checkPreProductionLotExists(targetPreProductionLot,createIdsMethod)) {
				logger.info("TARGET PreProduction Lot: "+targetPreProductionLot+ " has already been created." );
				continue;
			}

			 if((getReplicatePropertyService().isAutomaticHold() && !notHoldDemandTypes.contains(targetPreProductionLot.getDemandType())) || 
						targetPreProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId() || 
						(getReplicatePropertyService().isReplicateRemakeLots() && targetPreProductionLot.getRemakeFlag() != null && targetPreProductionLot.getRemakeFlag().equalsIgnoreCase("Y"))) {
						
				targetPreProductionLot.setNextProductionLot(null);
				targetPreProductionLot.setHoldStatus(PreProductionLotStatus.HOLD.getId());
			}

			if (createIdsMethod.equalsIgnoreCase(NONE_METHOD)) {
				
				if(this.doesRequireSpecCode(targetProcessLocation)) {
					String targetSpecCode = findTargetSpecCode(sourcePreProductionLot.getProductSpecCode(), targetProcessLocation);
					if(StringUtils.isEmpty(targetSpecCode)) {
						continue;
					} else {
						targetPreProductionLot.setProductSpecCode(targetSpecCode);
						targetPreProductionLot.setStartProductId("");
						targetPreProductionLot.setMbpn(targetPreProductionLot.deriveMbpn());
						targetPreProductionLot.setHesColor(targetPreProductionLot.deriveHesColor());
					}
				}
				
				if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId() && foundFirstNonHoldLot == false) {
					foundFirstNonHoldLot = true;
					if (lastLotInTarget != null) {
						lastLotInTarget.setNextProductionLot(targetPreProductionLot.getProductionLot());
					}
					lastLotNotOnHold = targetPreProductionLot;
				} else if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId()) {
					if (lastLotNotOnHold != null) {
						lastLotNotOnHold.setNextProductionLot(targetPreProductionLot.getProductionLot());
						getPreProductionLotDao().save(lastLotNotOnHold);
					}
					lastLotNotOnHold = targetPreProductionLot;
				} else if (targetPreProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
					if (lastLotNotOnHold != null) {
						lastLotNotOnHold.setNextProductionLot(null);
						getPreProductionLotDao().save(targetPreProductionLot);
					}
				}

				logger.info("Create_ID_Method is None for TARGET Process location: "+ targetProcessLocation + " Created Lot : " + targetPreProductionLot); 

			} else if (createIdsMethod.equalsIgnoreCase(CREATE_METHOD)) {

				String targetSpecCode = findTargetSpecCode(sourcePreProductionLot.getProductSpecCode(), targetProcessLocation);
				if (!targetSpecCode.equalsIgnoreCase("") && !targetSpecCode.equalsIgnoreCase(noReplication)) {

					List<ProductStampingSequence> targetProductStampingSequenceList = ServiceFactory.getService(ProductStampingSeqGenService.class).createStampingSequenceList(targetSpecCode, targetProcessLocation, targetPreProductionLot,componentId);
					if(!targetProductStampingSequenceList.isEmpty()) {
						targetPreProductionLot = ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(targetPreProductionLot, targetProductStampingSequenceList.get(0).getId().getProductID(),targetSpecCode, targetProcessLocation);

						targetProductStampingSequences.addAll(targetProductStampingSequenceList);

						if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId() && foundFirstNonHoldLot == false) {
							foundFirstNonHoldLot = true;
							if (lastLotInTarget != null) {
								lastLotInTarget.setNextProductionLot(targetPreProductionLot.getProductionLot());
							}
							lastLotNotOnHold = targetPreProductionLot;
						} else if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId()) {
							if (lastLotNotOnHold != null) {
								lastLotNotOnHold.setNextProductionLot(targetPreProductionLot.getProductionLot());
								getPreProductionLotDao().save(lastLotNotOnHold);
							}
							lastLotNotOnHold = targetPreProductionLot;
						} else if (targetPreProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
							if (lastLotNotOnHold != null) {
								lastLotNotOnHold.setNextProductionLot(null);
								getPreProductionLotDao().save(targetPreProductionLot);
							}
						}
						saveProdLotProductStampingSeqList(targetProductStampingSequences,targetPreProductionLot);

						logger.info("In CREATE SOURCE PreProductionLot:" + sourcePreProductionLot + ", TARGET PreProductionLot: "+targetPreProductionLot+ " and Starting ID: "+targetProductStampingSequenceList.get(0).getId().getProductID());

					}else {
						updateLastNotHoldLot(lastLotNotOnHold);
						String error = "Can't find the correct MBPN spec code : " + targetSpecCode + " Please configure the build attributes correctly and run OIF_REPLICATE_ORDER again.";
						logger.error(error);
						errorsCollector.error(error);
						break;
					}
				}else if(targetSpecCode.equalsIgnoreCase(noReplication)){
					logger.info("Skipping Record : " + sourcePreProductionLot + " Build Attribute is setup as : " + targetSpecCode);
				}else {
					updateLastNotHoldLot(lastLotNotOnHold);
					String error = "Build Attribute is not setup for product spec code  " + sourcePreProductionLot.getProductSpecCode() + " Please configure the build attributes correctly and run OIF_REPLICATE_ORDER again.";
					logger.error(error);
					errorsCollector.error(error);
					break;
				}
			} else {
				if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId() && foundFirstNonHoldLot == false) {
					foundFirstNonHoldLot = true;
					if (lastLotInTarget != null) {
						lastLotInTarget.setNextProductionLot(targetPreProductionLot.getProductionLot());
					}
					lastLotNotOnHold = targetPreProductionLot;
				} else if (targetPreProductionLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId()) {
					if (lastLotNotOnHold != null) {
						lastLotNotOnHold.setNextProductionLot(targetPreProductionLot.getProductionLot());
						getPreProductionLotDao().save(lastLotNotOnHold);
					}
					lastLotNotOnHold = targetPreProductionLot;
				} else if (targetPreProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
					if (lastLotNotOnHold != null) {
						lastLotNotOnHold.setNextProductionLot(null);
						getPreProductionLotDao().save(targetPreProductionLot);
					}
				}

				sourceProductStampingSequences = getProductStampingSequenceDao().findAllByProductionLot(sourcePreProductionLot.getProductionLot()); 
				sourceProductStampingSequenceMap.put(sourcePreProductionLot.getProductionLot(), sourceProductStampingSequences);
				targetProductStampingSequences.addAll(ServiceFactory.getService(ProductStampingSeqGenService.class).replicateStampingSequence(sourceProductStampingSequenceMap, targetProductionLot, sourcePreProductionLot, targetPreProductionLot ));
				saveProdLotProductStampingSeqList(targetProductStampingSequences,targetPreProductionLot);

				logger.info("Default create method SOURCE PreProductionLot:" + sourcePreProductionLot + ", TARGET PreProductionLot: "+targetPreProductionLot);
			}
			if (useProductionLotMbpnSequence) {
				createProductionLotMbpnSequence(targetPreProductionLot);
			}
			noOfSavedLots++;
		}

		if (noOfSavedLots > 0) {
			if (lastLotInTarget != null) {
				getPreProductionLotDao().update(lastLotInTarget);
			}
			if (lastLotNotOnHold != null) {
				updateLastNotHoldLot(lastLotNotOnHold);
				updateLastProductionLotProperty(lastLotNotOnHold,lastProdLotCompPropId,sourceTargetLotMap);
			}
			logger.info("Schedule Replication completed successfully. Total lots processed: "+ noOfSavedLots);
		} else {
			logger.info("Schedule Replication completed successfully.  No lots were configured to be replicated.");
		}

		targetProductStampingSequences.clear();
	}


	private void updateLastNotHoldLot(PreProductionLot lastLotNotOnHold) {

		lastLotNotOnHold.setNextProductionLot(null);
		getPreProductionLotDao().update(lastLotNotOnHold);
	}
	
	private boolean doesRequireSpecCode(String targetProcessLocation) {
		productType = getProperty("PRODUCT_TYPE{" + targetProcessLocation + "}");
		return Boolean.parseBoolean(getProperty("DOES_REQUIRE_SPEC_CODE{"+productType+"}", "FALSE"));
	}


	private void rebuildSchedule(String[] targetPlanCodes, String[] targetProcessLocations, ComponentPropertyId lastProdLotCompPropId, String sourceProcessLocation) {

		HashMap<String,String> rebuildSourceTargetLotMap=new HashMap<String,String> ();
		String targetPlanCode = "";
		String targetPlanProcessLocation = "";
		PreProductionLot firstLot = null;
		PreProductionLot currentLot = null;
		rebuiltLots.clear();
		for (int i = 0; i < targetPlanCodes.length; i++) {
			targetPlanCode = StringUtils.trim(targetPlanCodes[i]);
			targetPlanProcessLocation = StringUtils.trim(targetProcessLocations[i]);

			if(!doesPlanCodeHaveTail(targetPlanCode)) {
				continue;
			} else {							
				firstLot = getFirstLot(targetPlanCode);
				if (firstLot != null) {
					currentLot = getPreProductionLotDao().findByProductionLotAndPlanCode(firstLot.getNextProductionLot(), targetPlanCode);
					PreProductionLot nextLot = currentLot;
					while(currentLot != null) {
						nextLot = getPreProductionLotDao().findByProductionLotAndPlanCode(currentLot.getNextProductionLot(), targetPlanCode);
						if (currentLot.getSendStatus() == PreProductionLotSendStatus.WAITING){
							rebuiltLots.put(currentLot.getProductionLot(), currentLot);
							getProductStampingSequenceDao().deleteProductionLot(currentLot.getProductionLot());
							getMbpnProductDao().deleteProductionLot(currentLot.getProductionLot());					
							getPreProductionLotDao().delete(currentLot.getProductionLot());

						}
						currentLot = nextLot;
					}
					firstLot.setNextProductionLot(null); 
					getPreProductionLotDao().save(firstLot);
					rebuildSourceTargetLotMap.put(firstLot.getProductionLot(), buildProductionLotNumber(firstLot.getProductionLot(), targetPlanProcessLocation, sourceProcessLocation));
					updateLastProductionLotProperty(firstLot,lastProdLotCompPropId,rebuildSourceTargetLotMap);		
				}

				List <PreProductionLot> onHoldLots = getPreProductionLotDao().findAllOnHoldLotsByPlanCode(targetPlanCode);
				for (PreProductionLot onHoldLot : onHoldLots) {
					rebuiltLots.put(onHoldLot.getProductionLot(), onHoldLot);
					getProductStampingSequenceDao().deleteProductionLot(onHoldLot.getProductionLot());
					getMbpnProductDao().deleteProductionLot(onHoldLot.getProductionLot());					
					getPreProductionLotDao().delete(onHoldLot.getProductionLot());
				}					
			}
		}					
	}

	private PreProductionLot populateTargetPreProdLot(PreProductionLot sourcePreProductionLot, String sourceProcessLocation, 
			String targetProcessLocation, String targetPlanCode, String targetProductionLot ){
		PreProductionLot targetPreProductionLot = new PreProductionLot();
		BeanUtils.copyProperties(sourcePreProductionLot, targetPreProductionLot);

		targetPreProductionLot.setProductionLot(targetProductionLot);
		if (rebuiltLots.containsKey(targetPreProductionLot.getProductionLot()) && (targetPreProductionLot.getNextProductionLot() != null && !targetPreProductionLot.getNextProductionLot().equalsIgnoreCase(null))){
			targetPreProductionLot.setNextProductionLot(rebuiltLots.get(targetPreProductionLot.getProductionLot()).getNextProductionLot());			
			if(rebuiltLots.get(targetPreProductionLot.getProductionLot()).getSequence()==0.0 && generateSeqNum)
			{
				targetPreProductionLot.setSequence(maxTargetPlanSeqNum);
				maxTargetPlanSeqNum++;
			}else
			{
				targetPreProductionLot.setSequence(rebuiltLots.get(targetPreProductionLot.getProductionLot()).getSequence());
			}
		} else {
			targetPreProductionLot.setNextProductionLot(buildProductionLotNumber(sourcePreProductionLot.getNextProductionLot(), sourceProcessLocation, targetProcessLocation));
			if(sourcePreProductionLot.getSequence()==0.0 && generateSeqNum)
			{
				targetPreProductionLot.setSequence(maxTargetPlanSeqNum);
				maxTargetPlanSeqNum++;
			}else
			{
				targetPreProductionLot.setSequence(sourcePreProductionLot.getSequence());
			}
		}

		targetPreProductionLot.setPlanCode(targetPlanCode);
		targetPreProductionLot.setProcessLocation(targetProcessLocation);
		targetPreProductionLot.setStampedCount(0);
		targetPreProductionLot.setSentTimestamp(null);
		targetPreProductionLot.setSendStatus(PreProductionLotSendStatus.WAITING);
		targetPreProductionLot.setUpdateTimestamp(null);
		return targetPreProductionLot;
	}

	private List<PreProductionLot> getUnProcessedSourceProductionLots(ComponentPropertyId lastProdLotCompPropId,String sourcePlanCode, String sourcePlanCodeMapKey) {

		String replicateHoldLots = getProperty("REPLICATE_HOLD_LOTS{"+sourcePlanCodeMapKey+"}", "TRUE");

		ComponentProperty lastProdLotCompProp=getComponentPropertyDao().findByKey(lastProdLotCompPropId);

		List<PreProductionLot> completeSrcPreProdLotList;

		completeSrcPreProdLotList = getPreProductionLotDao().findAllByPlanCodeSort(sourcePlanCode);

		if(lastProdLotCompProp==null || StringUtils.isBlank(lastProdLotCompProp.getPropertyValue())) {
			logger.info("Return all the lots from GAL212TBX for Plan Code : "+sourcePlanCode);
			return completeSrcPreProdLotList;
		}else{
			String lastRunProductionLot=lastProdLotCompProp.getPropertyValue();	
			HashMap<String,PreProductionLot> lotsMap=new HashMap<String,PreProductionLot> ();
			HashMap<String,String> lotNextLotMap=new HashMap<String,String> ();
			for(PreProductionLot preProductionLot:completeSrcPreProdLotList)
			{
				lotsMap.put(preProductionLot.getProductionLot(), preProductionLot);
				lotNextLotMap.put(preProductionLot.getProductionLot(), preProductionLot.getNextProductionLot());
			}
			
			String firstLot = lotNextLotMap.get(lastRunProductionLot);
			
			completeSrcPreProdLotList.clear();
			if(firstLot == null && getPreProductionLotDao().findByKey(lastRunProductionLot).getHoldStatus()==0) {
				logger.info("Lot " + lastRunProductionLot + " is On Hold. Please update the last lot and rerun the job.");
				return completeSrcPreProdLotList;
			}else if(firstLot == null) {
				logger.info("Lot " + lastRunProductionLot + " is last lot in GAL212TBX for Plan Code : "+sourcePlanCode);				
				return completeSrcPreProdLotList;
			}

			boolean lastLot=false;
			
			while(!lastLot)
			{
				String nextLot=lotNextLotMap.get(lastRunProductionLot);
				if(nextLot==null)
				{
					lastLot=true;
				}else
				{
					completeSrcPreProdLotList.add(lotsMap.get(nextLot));
					lastRunProductionLot=nextLot;
				}		
			}
		}

		if (replicateHoldLots.equalsIgnoreCase("true")) {

			List<PreProductionLot>completeSrcHoldPreProdLotList = getPreProductionLotDao().findAllOnHoldLotsByPlanCode(sourcePlanCode);
			logger.info("Add Hold lots from GAL212TBX for Plan Code : "+sourcePlanCode);
			completeSrcPreProdLotList.addAll(completeSrcHoldPreProdLotList);
		}

		return completeSrcPreProdLotList;
	}

	private RunningStatus findRunningStatusProperty() {
		ComponentPropertyId runningStatusCompPropId = new ComponentPropertyId(componentId, "RUNNING_STATUS");
		ComponentProperty runningStatusProperty=getComponentPropertyDao().findByKey(runningStatusCompPropId);
		if(runningStatusProperty==null)
		{
			runningStatusProperty=new ComponentProperty();
			runningStatusProperty.setId(runningStatusCompPropId);
			runningStatusProperty.setPropertyValue(RunningStatus.FINISHED.toString());
			getComponentPropertyDao().save(runningStatusProperty);		
		}
		return RunningStatus.valueOf(runningStatusProperty.getPropertyValue());
	}

	private String findRebuildScheduleProperty(String sourcePlanCodeMapKey) {
		ComponentPropertyId rebuildScheduleCompPropId = new ComponentPropertyId(componentId, REBUILD_SCHEDULE+"{"+sourcePlanCodeMapKey+"}");
		ComponentProperty rebuildScheduleProperty=getComponentPropertyDao().findByKey(rebuildScheduleCompPropId);
		if(rebuildScheduleProperty==null)
		{
			rebuildScheduleProperty=new ComponentProperty();
			rebuildScheduleProperty.setId(rebuildScheduleCompPropId);
			rebuildScheduleProperty.setPropertyValue(RebuildSchedule.OFF.toString());
			getComponentPropertyDao().save(rebuildScheduleProperty);		
		}
		return rebuildScheduleProperty.getPropertyValue().trim().toString();
	}

	private void updateRebuildScheduleProperty(String status,String sourcePlanCodeMapKey) {
		ComponentPropertyId rebuildScheduleCompPropId = new ComponentPropertyId(componentId, REBUILD_SCHEDULE+"{"+sourcePlanCodeMapKey+"}");
		ComponentProperty rebuildScheduleProperty=getComponentPropertyDao().findByKey(rebuildScheduleCompPropId);
		if(rebuildScheduleProperty==null)
		{
			rebuildScheduleProperty=new ComponentProperty();
			rebuildScheduleProperty.setId(rebuildScheduleCompPropId);
		}
		rebuildScheduleProperty.setPropertyValue(status);
		getComponentPropertyDao().save(rebuildScheduleProperty);
	}


	private void updateRunningStatusProperty(RunningStatus status) {
		ComponentPropertyId runningStatusCompPropId = new ComponentPropertyId(componentId, "RUNNING_STATUS");
		ComponentProperty runningStatusProperty=getComponentPropertyDao().findByKey(runningStatusCompPropId);
		if(runningStatusProperty==null)
		{
			runningStatusProperty=new ComponentProperty();
			runningStatusProperty.setId(runningStatusCompPropId);
		}
		runningStatusProperty.setPropertyValue(status.toString());
		getComponentPropertyDao().save(runningStatusProperty);		
	}

	private void updateLastProductionLotProperty(PreProductionLot preProductionLot,ComponentPropertyId lastProdLotCompPropId, HashMap<String, String> sourceTargetLotMap) {
		ComponentProperty lastProdLotCompProperty=getComponentPropertyDao().findByKey(lastProdLotCompPropId);
		if(lastProdLotCompProperty==null)
		{
			lastProdLotCompProperty=new ComponentProperty();
			lastProdLotCompProperty.setId(lastProdLotCompPropId);
		}
		
		if(sourceTargetLotMap.get(preProductionLot.getProductionLot()) !=null ) {
			lastProdLotCompProperty.setPropertyValue(sourceTargetLotMap.get(preProductionLot.getProductionLot()));
			getComponentPropertyDao().save(lastProdLotCompProperty);
		}

	}
	private void saveProdLotProductStampingSeqList(List<ProductStampingSequence> targetProductStampingSequences,PreProductionLot preProductionLot) {

		List<ProductStampingSequence> prodLotProductStampList=new ArrayList<ProductStampingSequence>();
		for(ProductStampingSequence productStampingSequence:targetProductStampingSequences)
		{
			if(productStampingSequence.getProductionLot().trim().equals(preProductionLot.getProductionLot().trim()))
			{
				prodLotProductStampList.add(productStampingSequence);
			}
		}
		if(prodLotProductStampList.size()>0)
		{
			getProductStampingSequenceDao().saveAll(prodLotProductStampList);
		}

	}

	boolean doesPlanCodeHaveTail(String planCode) {
		boolean hasTail = true;
		lastLotInTarget = getPreProductionLotDao().findLastLotByPlanCode(planCode);
		if (lastLotInTarget == null) {
			List<PreProductionLot> allTargetPreProductionLots = getPreProductionLotDao().findAllByPlanCode(planCode);
			if (allTargetPreProductionLots != null && allTargetPreProductionLots.size() > 0) { 
				String error = "Target Plan Code: " + planCode + 
						" has existing records in GAL212TBX but next_production_lot=null can't be found. Please fix the error and run OIF_REPLICATE_ORDER again.";
				logger.error(error);
				errorsCollector.error(error);
				errorsCollector.getRunHistory().setStatus(OifRunStatus.TAIL_NOT_FOUND);
				hasTail = false;
			}
		}
		return hasTail;
	}

	private String buildProductionLotNumber(String sourceProductionLot, String sourceProcessLocation, String targetProcessLocation) {
		if (sourceProductionLot != null) {
			return sourceProductionLot.replaceFirst(sourceProcessLocation, targetProcessLocation);
		} else {
			return null;
		}
	}

	public PreProductionLot getFirstLot(String targetPlanCode) {
		PreProductionLot firstLot=null;
		if(getReplicatePropertyService().isCurrentLotOrderBySeqNum())
			firstLot = getPreProductionLotDao().findCurrentPreProductionLotByPlanCode(targetPlanCode);
		else
			firstLot= getPreProductionLotDao().findCurrentLotByPlanCodeOrderByLinkedList(targetPlanCode);
		return firstLot;
	}


	private boolean checkPreProductionLotExists(PreProductionLot targetPreProductionLot, String createIdsMethod) {
		PreProductionLot ppLot = getPreProductionLotDao().findByKey(targetPreProductionLot.getProductionLot());
		if (ppLot != null) {
			if(createIdsMethod.equalsIgnoreCase(NONE_METHOD)) return true;
			List<ProductStampingSequence> productStampingSeq =  getProductStampingSequenceDao().findAllByProductionLot(targetPreProductionLot.getProductionLot());
			if (productStampingSeq.size() > 0) {
				return true;
			}
		}
		return false;
	}

	private void createProductionLotMbpnSequence(PreProductionLot productionLot) {
		getProductionLotMbpnSequenceDao().deleteByProductionLot(productionLot.getProductionLot());
		List<String> mbpnCombinations = getMbpnCombination(productionLot);
		String mbpnCombinationCode = getMbpnCombinationCode(productionLot);
		logger.info("Creating " + ProductionLotMbpnSequence.class.getSimpleName() + " records for production lot " + productionLot.getProductionLot() + "; mbpn combination code = " + mbpnCombinationCode + ", mbpn combinations = " + mbpnCombinations);
		for (int i = 0; i < mbpnCombinations.size(); i++) {
			ProductionLotMbpnSequence productionLotMbpnSequence = new ProductionLotMbpnSequence();
			ProductionLotMbpnSequenceId productionLotMbpnSequenceId = new ProductionLotMbpnSequenceId();
			productionLotMbpnSequenceId.setProductionLot(productionLot.getProductionLot());
			productionLotMbpnSequenceId.setSequence(i+1);
			productionLotMbpnSequence.setId(productionLotMbpnSequenceId);
			productionLotMbpnSequence.setMbpn(mbpnCombinations.get(i));
			productionLotMbpnSequence.setCombinationCode(mbpnCombinationCode);
			getProductionLotMbpnSequenceDao().save(productionLotMbpnSequence);
		}
	}


	private List<String> getMbpnCombination(PreProductionLot productionLot) {
		BuildAttribute mbpnCombinationBuildAttribute = getBuildAttributeDao().findById(DEFAULT_MBPN_COMBINATION, productionLot.getProductSpecCode());
		if (mbpnCombinationBuildAttribute == null) {
			throw new TaskException("Unable to find attribute " + DEFAULT_MBPN_COMBINATION + " for production lot " + productionLot.getProductionLot() + " (" + productionLot.getProductSpecCode() + ")");
		}
		String mbpnCombinationCsv = mbpnCombinationBuildAttribute.getAttributeValue();
		String[] mbpnCombinationArray = mbpnCombinationCsv.split(Delimiter.COMMA);
		return Arrays.asList(mbpnCombinationArray);
	}

	private String getMbpnCombinationCode(PreProductionLot productionLot) {
		BuildAttribute mbpnCombinationCodeBuildAttribute = getBuildAttributeDao().findById(DEFAULT_MBPN_COMBINATION_CODE, productionLot.getProductSpecCode());
		if (mbpnCombinationCodeBuildAttribute == null) {
			throw new TaskException("Unable to find attribute " + DEFAULT_MBPN_COMBINATION_CODE + " for production lot " + productionLot.getProductionLot() + " (" + productionLot.getProductSpecCode() + ")");
		}
		return mbpnCombinationCodeBuildAttribute.getAttributeValue();
	}

	private String findTargetSpecCode(String sourceSpecCode, String targetProcessLocation){
		String targetSpecCode = "";

		productType = getProperty("PRODUCT_TYPE{" + targetProcessLocation + "}");
		SubproductUtil subproductUtil = new SubproductUtil();
		targetSpecCode = subproductUtil.getSubproductProductSpecCode(productType, sourceSpecCode, componentId);

		return targetSpecCode;
	}

	private PreProductionLotDao getPreProductionLotDao() {
		if (preProductionLotDao == null)
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		return preProductionLotDao;
	}

	private ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (productStampingSequenceDao == null)
			productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		return productStampingSequenceDao;
	}

	private ComponentPropertyDao getComponentPropertyDao() {
		if (componentPropertyDao == null)
			componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		return componentPropertyDao;
	}

	private MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null)
			mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		return mbpnProductDao;
	}

	private ProductionLotMbpnSequenceDao getProductionLotMbpnSequenceDao() {
		if (productionLotMbpnSequenceDao == null)
			productionLotMbpnSequenceDao = ServiceFactory.getDao(ProductionLotMbpnSequenceDao.class);
		return productionLotMbpnSequenceDao;
	}

	private BuildAttributeDao getBuildAttributeDao() {
		if (buildAttributeDao == null)
			buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		return buildAttributeDao;
	}


}
