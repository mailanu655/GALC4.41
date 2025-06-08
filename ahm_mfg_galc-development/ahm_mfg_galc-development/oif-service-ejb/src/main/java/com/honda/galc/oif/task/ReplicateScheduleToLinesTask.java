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
import com.honda.galc.dao.product.MbpnDao;
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
 * 
 * <h3>ReplicateScheduleToLinesTask Class description</h3>
 * <p> ReplicateScheduleToLinesTask description </p>
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
 * @author Zhiqiang Wang<br>
 * Feb 4, 2015
 *
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date apr 6, 2016
 */
public class ReplicateScheduleToLinesTask extends OifTask<Object> implements IEventTaskExecutable {

	private static final String FILTER_PREFIX = "FilterOn";
	private static final String FILTER_INCLUDE = "Include";
	private static final String FILTER_EXCLUDE = "Exclude";
	private static final String LAST_RUN_PRODUCTION_LOT = "LAST_RUN_PRODUCTION_LOT";
	private static final String NONE_METHOD = "NONE";
	private static final String CREATE_METHOD = "CREATE";
	private static final String DEFAULT_MBPN_COMBINATION = "DEFAULT_MBPN_COMBINATION";
	private static final String DEFAULT_MBPN_COMBINATION_CODE = "DEFAULT_MBPN_COMBINATION_CODE";

	private PreProductionLotDao preProductionLotDao;
	private ProductStampingSequenceDao productStampingSequenceDao;
	private ComponentPropertyDao componentPropertyDao;
	private MbpnProductDao mbpnProductDao;
	private MbpnDao mbpnDao;
	private ProductionLotMbpnSequenceDao productionLotMbpnSequenceDao;
	private BuildAttributeDao buildAttributeDao;
	private HashMap<String,PreProductionLot>rebuiltLots = new HashMap<String,PreProductionLot>();
	private PreProductionLot lastLotInTarget = null;
	private Double maxTargetPlanSeqNum = 0d;
	private boolean  generateSeqNum;
	

	ReplicateScheduleProperty propBean;

	private String productType = "";
	
	private enum RunningStatus {FINISHED, RUNNING};

	public ReplicateScheduleToLinesTask(String name) {
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
				propBean = PropertyService.getPropertyBean(ReplicateScheduleProperty.class, componentId);
				errorsCollector = new OifErrorsCollector(componentId);

				processPreProductionLots();
				 
				updateRunningStatusProperty(RunningStatus.FINISHED);
				updateLastProcessTimestamp(now);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Exception from schedule replication.");
			errorsCollector.error(ex.toString(), "Exception from schedule replication.");
			updateRunningStatusProperty(RunningStatus.FINISHED);
		} finally {
			errorsCollector.sendEmail();
		}
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

	private List<PreProductionLot> getUnProcessedSourceProductionLots(ComponentPropertyId lastProdLotCompPropId,String sourcePlanCode, boolean rebuildLots ) {

		ComponentProperty lastProdLotCompProp=getComponentPropertyDao().findByKey(lastProdLotCompPropId);
		List<PreProductionLot> completeSrcPreProdLotList;
		completeSrcPreProdLotList = getPreProductionLotDao().findReplicateSourceByFilters(sourcePlanCode, getFilters()); 
		if(lastProdLotCompProp==null || StringUtils.isBlank(lastProdLotCompProp.getPropertyValue()))
			return completeSrcPreProdLotList;
		String lastRunProductionLot=lastProdLotCompProp.getPropertyValue();
		HashMap<String,PreProductionLot> lotsMap=new HashMap<String,PreProductionLot> ();
		HashMap<String,String> lotNextLotMap=new HashMap<String,String> ();
		for(PreProductionLot preProductionLot:completeSrcPreProdLotList)
		{
			lotsMap.put(preProductionLot.getProductionLot(), preProductionLot);
			lotNextLotMap.put(preProductionLot.getProductionLot(), preProductionLot.getNextProductionLot());
		}
		completeSrcPreProdLotList.clear();
		boolean lastLot=false;
		while(!lastLot)//Create list of lots by sequence that need to be replicated
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
		
		if (rebuildLots) {  //Add source lots already on hold
			List<PreProductionLot>completeSrcHoldPreProdLotList = getPreProductionLotDao().findReplicateSourceOnHoldByFilters(sourcePlanCode, getFilters());
			completeSrcPreProdLotList.addAll(completeSrcHoldPreProdLotList);
		}
				
		return completeSrcPreProdLotList;
	}
	
	

	
	
	private boolean doesRequireSpecCode(String targetProcessLocation) {
		productType = getProperty("PRODUCT_TYPE{" + targetProcessLocation + "}");
		return Boolean.parseBoolean(getProperty("DOES_REQUIRE_SPEC_CODE{"+productType+"}", "FALSE"));
	}
	

	private String findTargetSpecCode(String sourceSpecCode, String targetProcessLocation) throws Exception {
		String targetSpecCode = "";
	
		
		productType = getProperty("PRODUCT_TYPE{" + targetProcessLocation + "}");

		boolean doesRequireSpecCode = Boolean.parseBoolean(getProperty("DOES_REQUIRE_SPEC_CODE{"+productType+"}", "TRUE"));
		if(!doesRequireSpecCode) return targetSpecCode;
		
		SubproductUtil subproductUtil = new SubproductUtil();
		targetSpecCode = subproductUtil.getSubproductProductSpecCode(productType, sourceSpecCode, componentId);

		if(!doesRequireSpecCode) {
			//Do nothing.  Subassembly is not a 1-for-1 part.
		} else if (doesRequireSpecCode && targetSpecCode.equalsIgnoreCase("")){
			throw new Exception("Subproduct Spec Code not setup for Source Product Spec Code " + sourceSpecCode);			
		}
		return targetSpecCode;
	}

	private Map<String, String> getFilters() {
		Map<String, String> filters = new HashMap<String, String>();
		List<ComponentProperty> configs = PropertyService.getProperties(componentId, FILTER_PREFIX + ".*");

		for(ComponentProperty config : configs) {

			String[] keys = config.getPropertyKey().split("\\.");
			String[] values = config.getPropertyValue().split(",");
			if (values.length == 0) 
				continue;
			String filterValue = StringUtils.trim(values[0]);
			if (filterValue.length() == 0) {
				continue;
			}
			for (int i = 1; i < values.length; i++) {
				String value = StringUtils.trim(values[i]);
				if (value.length() == 0) {
					continue;
				}
				filterValue += "," + value; 
			}

			if (filterValue.length() > 0) {
				if(FILTER_INCLUDE.equalsIgnoreCase(keys[1])) {
					filterValue = " in (" + filterValue + ")";
				} else if(FILTER_EXCLUDE.equalsIgnoreCase(keys[1])) {
					filterValue = " not in (" + filterValue + ")";
				}
				filters.put(keys[2] + "." + keys[3], filterValue);
			}
		}

		return filters;
	}

	//replace process location in source pre production lot with new process location
	private String buildProductionLotNumber(String sourceProductionLot, String sourceProcessLocation, String targetProcessLocation) {
		if (sourceProductionLot != null) {
			return sourceProductionLot.replaceFirst(sourceProcessLocation, targetProcessLocation);
		} else {
			return null;
		}
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
	
	private MbpnDao getMbpnDao() {
		if (mbpnDao == null)
			mbpnDao = ServiceFactory.getDao(MbpnDao.class);
		return mbpnDao;
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
	
	/**
	 * Gets the mbpn substring based on sub assembly rule configuration.
	 * 
	 * @param startProductIdPrefix
	 *            the start product id prefix
	 * @param targetSpecCode
	 *            the target spec code
	 * @param ruleName
	 *            the rule name
	 * @return the mbpn substring
	 */
	
	private void  processPreProductionLots()
	{
		int noOfSavedLots = 0;
		int noOfSourceLots = 0;
		try {
			Map<String,String> sourcePlanCodeMap=propBean.getSourcePlanCode();
			Map<String,String> sourceProcessLocationMap=propBean.getSourceProcessLocation();
			Map<String,String> targetPlanCodeMap=propBean.getTargetPlanCode();
			Map<String,String> targetProcessLocationMap=propBean.getTargetProcessLocation();
			Map<String,String> useProductionLotMbpnSequenceMap=propBean.getUseProductionLotMbpnSequence();
			generateSeqNum=propBean.isGenerateSeqNum();
			
			HashMap<String,String> sourceTargetLotMap=new HashMap<String,String> ();

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
				boolean rebuildLots = false;

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
				

				List<PreProductionLot> targetPreProductionLots = new ArrayList<PreProductionLot>();
				List<ProductStampingSequence> targetProductStampingSequences = new ArrayList<ProductStampingSequence>();
				List<ProductStampingSequence> sourceProductStampingSequences = null;				

				//	sources are ordered by next_production_lot link
				ComponentPropertyId lastProdLotCompPropId = new ComponentPropertyId(componentId, LAST_RUN_PRODUCTION_LOT+"{"+sourcePlanCodeMapKey+"}");
				
				if( getProperty("REBUILD_SCHEDULE{"+sourcePlanCodeMapKey+"}", "FALSE").equalsIgnoreCase("TRUE")){
					rebuildLots = true;
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
								currentLot = getPreProductionLotDao().findByProductionLotAndPlanCode(firstLot.getNextProductionLot(), targetPlanCode);  //Always start with next lot that is not current lot, can not guarantee client will be refreshed if job is ran.
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
								firstLot.setNextProductionLot(null);  //This is the new tail.
								getPreProductionLotDao().save(firstLot);
								rebuildSourceTargetLotMap.put(firstLot.getProductionLot(), buildProductionLotNumber(firstLot.getProductionLot(), targetPlanProcessLocation, sourceProcessLocation));
								updateLastProductionLotProperty(firstLot,lastProdLotCompPropId,rebuildSourceTargetLotMap);		
							}

							//Rebuild lots already on hold
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
				
				List<PreProductionLot> sourcePreProductionLots=getUnProcessedSourceProductionLots(lastProdLotCompPropId,sourcePlanCode,rebuildLots);
				
				if (sourcePreProductionLots == null || sourcePreProductionLots.size() == 0) {
					logger.warn("No valid Pre Production Lot to be replicated.");
					continue;
				}else 	noOfSourceLots = sourcePreProductionLots.size();
				

				Map<String, List<ProductStampingSequence>> sourceProductStampingSequenceMap = new HashMap<String, List<ProductStampingSequence>>();
				if (!createIdsMethod.equalsIgnoreCase(NONE_METHOD)&& !createIdsMethod.equalsIgnoreCase(CREATE_METHOD))
				{
					for (PreProductionLot sourcePreProductionLot : sourcePreProductionLots) {
						sourceProductStampingSequences = getProductStampingSequenceDao()
								.findAllByProductionLot(sourcePreProductionLot.getProductionLot()); 
						sourceProductStampingSequenceMap.put(sourcePreProductionLot.getProductionLot(), sourceProductStampingSequences);
					}
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
					
					PreProductionLot sourcePreProductionLot = null;
					PreProductionLot targetPreProductionLot = null;
					String targetProductionLot = null;
					//replicate 212 records
					boolean foundFirstNonHoldLot = false;
					PreProductionLot lastLotNotOnHold = lastLotInTarget;
					List<String> notHoldDemandTypes = Arrays.asList(propBean.getNotHoldDemandTypes().split(","));
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
						
						if (checkPreProductionLotExists(targetPreProductionLot)) {
							logger.info("TARGET PreProduction Lot: "+targetPreProductionLot+ " has already been created." );
							continue;
						}
						
						if((propBean.isAutomaticHold() && !notHoldDemandTypes.contains(targetPreProductionLot.getDemandType())) || targetPreProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
							targetPreProductionLot.setNextProductionLot(null);
							targetPreProductionLot.setHoldStatus(PreProductionLotStatus.HOLD.getId());
						}
						
						
						PreProductionLot ppLot = getPreProductionLotDao().findByKey(sourcePreProductionLot.getProductionLot());
						if (ppLot != null) {
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
	    								getPreProductionLotDao().save(lastLotNotOnHold);
	    							}
	    						}
								targetPreProductionLots.add(targetPreProductionLot);
								logger.info("Create_ID_Method is None for TARGET Process location: "+ targetProcessLocation); 
							} else if (createIdsMethod.equalsIgnoreCase(CREATE_METHOD)) {
								///createIds()
								String targetSpecCode = findTargetSpecCode(sourcePreProductionLot.getProductSpecCode(), targetProcessLocation);
								if (!targetSpecCode.equalsIgnoreCase("")) {
									List<ProductStampingSequence> targetProductStampingSequenceList = ServiceFactory.getService(ProductStampingSeqGenService.class).createStampingSequenceList(targetSpecCode, targetProcessLocation, targetPreProductionLot,componentId);	
									targetPreProductionLot = ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(targetPreProductionLot, targetProductStampingSequenceList.get(0).getId().getProductID(), 
											targetSpecCode, targetProcessLocation);
									targetPreProductionLots.add(targetPreProductionLot);
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
		    								getPreProductionLotDao().save(lastLotNotOnHold);
		    							}
		    						}
		    						
		                            logger.info("In CREATE SOURCE PreProductionLot:" + sourcePreProductionLot + ", TARGET PreProductionLot: "+targetPreProductionLot+ 
	                            		    " and Starting ID:"+targetProductStampingSequenceList.get(0).getId().getProductID());
								}
							} else {  //Default is to REPLICATE, link existing to tail to first not-hold lot
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
	    								getPreProductionLotDao().save(lastLotNotOnHold);
	    							}
	    						}
								targetPreProductionLots.add(targetPreProductionLot);
								//replicate 216 records	
								targetProductStampingSequences.addAll(ServiceFactory.getService(ProductStampingSeqGenService.class).replicateStampingSequence(sourceProductStampingSequenceMap, targetProductionLot, sourcePreProductionLot, targetPreProductionLot ));
							}
						}
					}
					//Fix any gaps in the linked list caused by using filter or parts are not one-for-one.
					final boolean useProductionLotMbpnSequence = (useProductionLotMbpnSequenceMap != null) && (useProductionLotMbpnSequenceMap.containsKey(sourcePlanCodeMapKey) && Boolean.valueOf(useProductionLotMbpnSequenceMap.get(sourcePlanCodeMapKey)));
					for (int j=0; j < targetPreProductionLots.size(); j++) {
						getPreProductionLotDao().save(targetPreProductionLots.get(j));
						saveProdLotProductStampingSeqList(targetProductStampingSequences,targetPreProductionLots.get(j));
						updateLastProductionLotProperty(targetPreProductionLots.get(j),lastProdLotCompPropId,sourceTargetLotMap);
						if (useProductionLotMbpnSequence) {
							createProductionLotMbpnSequence(targetPreProductionLots.get(j));
						}
						noOfSavedLots++;
					}
					
					if (targetPreProductionLots.size() > 0) {
						if (lastLotInTarget != null) {
							getPreProductionLotDao().update(lastLotInTarget);
						}
						if (lastLotNotOnHold != null) {
							lastLotNotOnHold.setNextProductionLot(null);
							getPreProductionLotDao().update(lastLotNotOnHold);
						}
					} else {
						logger.info("Schedule Replication completed successfully.  No lots were configured to be replicated.");
					}
					targetPreProductionLots.clear();
					targetProductStampingSequences.clear();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Exception from schedule replication.");
			errorsCollector.error(ex.toString(), "Exception from schedule replication.");
		} finally {
			errorsCollector.getRunHistory().setSuccessCount(noOfSavedLots);
			errorsCollector.getRunHistory().setFailedCount(noOfSourceLots-noOfSavedLots);
			errorsCollector.sendEmail();
		}

	}

	public PreProductionLot getFirstLot(String targetPlanCode) {
		PreProductionLot firstLot=null;
		if(propBean.isCurrentLotOrderBySeqNum())
			firstLot = getPreProductionLotDao().findCurrentPreProductionLotByPlanCode(targetPlanCode);
		else
			firstLot= getPreProductionLotDao().findCurrentLotByPlanCodeOrderByLinkedList(targetPlanCode);
		return firstLot;
	}
	
	private boolean checkPreProductionLotExists(PreProductionLot targetPreProductionLot) {
		PreProductionLot ppLot = getPreProductionLotDao().findByKey(targetPreProductionLot.getProductionLot());
		if (ppLot != null) {
			List<ProductStampingSequence> productStampingSeq =  getProductStampingSequenceDao().findAllByProductionLot(targetPreProductionLot.getProductionLot());
			if (productStampingSeq.size() > 0) {
				return true;
			}
		}
		return false;
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
	
		
	
	boolean doesPlanCodeHaveTail(String planCode) {
		boolean hasTail = true;
		lastLotInTarget = getPreProductionLotDao().findLastLotByPlanCode(planCode);
		if (lastLotInTarget == null) {
			List<PreProductionLot> allTargetPreProductionLots = getPreProductionLotDao().findAllByPlanCode(planCode);
			if (allTargetPreProductionLots != null && allTargetPreProductionLots.size() > 0) { 
				// if there is any 212 record for target plan code, send out error, otherwise create first 212 record for target plan code
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
}
