package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.dao.product.ScheduleReplicationDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ScheduleReplication;
import com.honda.galc.oif.property.ReplicateScheduleProperty2;
import com.honda.galc.service.ProductStampingSeqGenService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.StringUtil;
import com.ibm.icu.math.BigDecimal;

/**
 * 
 * <h3>ReplicateScheduleToLinesTask Class description</h3>
 * <p>
 * ReplicateScheduleToLinesTask description
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
 */
public class ReplicateScheduleToLinesTask2 extends OifTask<Object> implements IEventTaskExecutable {

	private static final String FILTER_PREFIX = "FilterOn";
	private static final String FILTER_INCLUDE = "Include";
	private static final String FILTER_EXCLUDE = "Exclude";
	private static final String LAST_RUN_PRODUCTION_LOT = "LAST_RUN_PRODUCTION_LOT";
	private static final String NONE_METHOD = "NONE";
	private static final String CREATE_METHOD = "CREATE";
	private static final String DEFAULT_MBPN_COMBINATION = "DEFAULT_MBPN_COMBINATION";
	private static final String DEFAULT_MBPN_COMBINATION_CODE = "DEFAULT_MBPN_COMBINATION_CODE";

	private static final String REPLICATION_LOT_PREFIX = "R";

	private PreProductionLotDao preProductionLotDao;
	private ProductStampingSequenceDao productStampingSequenceDao;
	private ComponentPropertyDao componentPropertyDao;
	private MbpnProductDao mbpnProductDao;
	private MbpnDao mbpnDao;
	private FrameDao frameDao;
	private ProductionLotMbpnSequenceDao productionLotMbpnSequenceDao;
	private DailyDepartmentScheduleDao dailyDepartmentScheduleDao;
	private BuildAttributeDao buildAttributeDao;
	private HashMap<String, PreProductionLot> rebuiltLots = new HashMap<String, PreProductionLot>();
	private PreProductionLot lastLotInTarget = null;
	private Double maxTargetPlanSeqNum = 0d;
	private boolean generateSeqNum;

	ReplicateScheduleProperty2 propBean;

	private ScheduleReplicationDao scheduleReplicationDao;
	private BomDao bomDao;
	private Map<String, String> sourcePlanCodeMap;
	private Map<String, String> sourceProcessLocationMap;
	private Map<String, String> destPlanCodeMap;
	private Map<String, String> destProcessLocationMap;

	private enum RunningStatus {
		FINISHED, RUNNING
	};

	public ReplicateScheduleToLinesTask2(String name) {
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
				propBean = PropertyService.getPropertyBean(ReplicateScheduleProperty2.class, componentId);
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

	private RunningStatus findRunningStatusProperty() {
		ComponentPropertyId runningStatusCompPropId = new ComponentPropertyId(componentId, "RUNNING_STATUS");
		ComponentProperty runningStatusProperty = getComponentPropertyDao().findByKey(runningStatusCompPropId);
		if (runningStatusProperty == null) {
			runningStatusProperty = new ComponentProperty();
			runningStatusProperty.setId(runningStatusCompPropId);
			runningStatusProperty.setPropertyValue(RunningStatus.FINISHED.toString());
			getComponentPropertyDao().save(runningStatusProperty);
		}
		return RunningStatus.valueOf(runningStatusProperty.getPropertyValue());
	}

	private void updateRunningStatusProperty(RunningStatus status) {
		ComponentPropertyId runningStatusCompPropId = new ComponentPropertyId(componentId, "RUNNING_STATUS");
		ComponentProperty runningStatusProperty = getComponentPropertyDao().findByKey(runningStatusCompPropId);
		if (runningStatusProperty == null) {
			runningStatusProperty = new ComponentProperty();
			runningStatusProperty.setId(runningStatusCompPropId);
		}
		runningStatusProperty.setPropertyValue(status.toString());
		getComponentPropertyDao().save(runningStatusProperty);
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
	
	private FrameDao getFrameDao() {
		if (frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		return frameDao;
	}

	private ProductionLotMbpnSequenceDao getProductionLotMbpnSequenceDao() {
		if (productionLotMbpnSequenceDao == null)
			productionLotMbpnSequenceDao = ServiceFactory.getDao(ProductionLotMbpnSequenceDao.class);
		return productionLotMbpnSequenceDao;
	}

	private ScheduleReplicationDao getScheduleReplicationDao() {
		if (scheduleReplicationDao == null)
			scheduleReplicationDao = ServiceFactory.getDao(ScheduleReplicationDao.class);
		return scheduleReplicationDao;

	}

	private BomDao getBomDao() {
		if (bomDao == null)
			bomDao = ServiceFactory.getDao(BomDao.class);
		return bomDao;
	}
	
	private DailyDepartmentScheduleDao getDailyDepartmentScheduleDao() {
		if(dailyDepartmentScheduleDao == null)
		return ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		return dailyDepartmentScheduleDao;
	}
	
	private void processPreProductionLots() {
		sourcePlanCodeMap = propBean.getSourcePlanCode();
		sourceProcessLocationMap = propBean.getSourceProcessLocation();
		destPlanCodeMap = propBean.getDestPlanCode();
		destProcessLocationMap = propBean.getDestProcessLocation();
		String propKey = "";
		for (Map.Entry<String, String> entry : sourcePlanCodeMap.entrySet()) {
			propKey = entry.getKey();
			removeOrphanedChildLot(propKey);
			removeReplicationRecords(propKey);
			replicateLots(propKey);
			reorderSchedule(propKey);
		}
	}

	private void removeOrphanedChildLot(String propKey) {

		String sendStatus = PreProductionLotSendStatus.WAITING.getId() + "";

		String sourcePlanCode = "";
		String sourceProcessLocation = "";
		String destPlanCode = "";
		String destProcessLocation = "";

		sourcePlanCode = sourcePlanCodeMap.get(propKey);
		sourceProcessLocation = sourceProcessLocationMap.get(propKey);
		destPlanCode = destPlanCodeMap.get(propKey);
		destProcessLocation = destProcessLocationMap.get(propKey);

		List<PreProductionLot> preProductionLotsList = getPreProductionLotDao().findIncompleteChildLots(destPlanCode, destProcessLocation, sendStatus, sourcePlanCode, sourceProcessLocation);
		logger.info("PreProductionLot records: ====> " + preProductionLotsList);

		if (preProductionLotsList != null) {
			for (PreProductionLot preProductionLot : preProductionLotsList) {

				deleteProducts(preProductionLot.getProductionLot());

				logger.info("Deleting child lot records for production lot: ====> " + preProductionLot.getProductionLot());
				getPreProductionLotDao().delete(preProductionLot.getProductionLot());
			}
		}
	}
	
	private void removeReplicationRecords(String propKey) {
		int daysToLookBack = PropertyService.getPropertyInt(this.componentId, "DAYS_TO_LOOK_BACK", 7);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		Timestamp lookBackDate = Timestamp.valueOf(currentTimestamp.toLocalDateTime().minusDays(daysToLookBack));

		String destPlanCode = propBean.getDestPlanCode().get(propKey);
		String destprocessLocation = propBean.getDestProcessLocation().get(propKey);

		List<PreProductionLot> replicatedLotList = getPreProductionLotDao().findReplicatedLot(destPlanCode, destprocessLocation, lookBackDate);
		logger.info("replicated lot list: ====> " + replicatedLotList.size());

		if (!replicatedLotList.isEmpty()) {
			for (PreProductionLot replicatedLot : replicatedLotList) {
				if (!getBomDao().isBomPartNoValid(replicatedLot.getProductSpecCode())) {
					preProductionLotDao.removeReplicateLots(destprocessLocation, replicatedLot.getProductionLot());
					logger.info("child lot deleted " + replicatedLot.getProductionLot());
				}
			}
		} else {
			logger.info("replicatedLotList is empty, no replicatedLots to process.");
		}
	}

	private void replicateLots(String propKey) {
		int daysToLookBack = PropertyService.getPropertyInt(this.componentId, "DAYS_TO_LOOK_BACK", 7);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		Timestamp lookBackDate = Timestamp.valueOf(currentTimestamp.toLocalDateTime().minusDays(daysToLookBack));
		
		String processLocation = propBean.getSourceProcessLocation().get(propKey);
		String srcePlanCode = propBean.getSourcePlanCode().get(propKey);
		String destprocessLocation = propBean.getDestProcessLocation().get(propKey);

		List<PreProductionLot> preProductionLotsList = getPreProductionLotDao().findReplicatedLot(srcePlanCode, processLocation, lookBackDate);
		logger.info("Lots to replicate: ====> " + preProductionLotsList.size());

		List<String> errorMessages = new ArrayList<>();

		for (PreProductionLot preProductionLot : preProductionLotsList) {
			logger.info("Processing preProductionLot ====> " + preProductionLot);
			Map<String, List<ScheduleReplication>> partsByProcLoc = getPartsNosbyProcessLoc();
			String parentSequence = String.valueOf(preProductionLot.getSequence()); 
			String sequence = "0.01";
			
			if(parentSequence.indexOf('.')!=-1){ 
				sequence = "0." + parentSequence.substring(parentSequence.indexOf('.') + 1, parentSequence.length());
			}
			
			for (String destPocLoc : partsByProcLoc.keySet()) {
				logger.info("Processing destPocLoc ====> " + destPocLoc);
				List<ScheduleReplication> partNos = partsByProcLoc.get(destPocLoc);

				List<PreProductionLot> updateNextProductionLots = new ArrayList<>();

				for (ScheduleReplication partNo : partNos) {
					logger.info("Processing partNo ====> " + partNo.getId().getDestSpecCode());
					String destSpecCode = partNo.getId().getDestSpecCode().trim();

					if (destSpecCode.length() != 1) {
						destSpecCode = destSpecCode.trim().replace("*", "");
					}

					Bom bom = null;
					String newSpecCode = "";
					if (destSpecCode.equals("*")) {
						logger.info("New Spec code :::: " + preProductionLot.getProductSpecCode());
						newSpecCode = preProductionLot.getProductSpecCode();
					} else {
						List<String> partNoSubStr = new ArrayList<String>();
						partNoSubStr.add(destSpecCode);
						bom = getBomDao().findProductSpecToBeReplicate(preProductionLot.getProductionLot(), getPartsToExclude(), partNoSubStr);
						if (bom != null) {
							newSpecCode = bom.getId().getPartNo().trim();
							logger.info("New Spec code :::: " + bom.getId().getPartNo());
						} else {
							logger.info("Bom is null, cannot add new spec code");
						}
					}

					String lotNumber = preProductionLot.getProductionLot().substring(8, preProductionLot.getProductionLot().length());

					if (newSpecCode.isEmpty()) {
						logger.info("New spec code :::: " + newSpecCode);
						continue;
					}

					PreProductionLot childLot = getChildLot(lotNumber, newSpecCode, destPocLoc);

					if (childLot == null) {
						// Child lot not found
						logger.info("No child lot found for the spec code");

						sequence = String.format("%.2f", Double.valueOf(sequence) + 0.01);
						childLot = createChildLot(preProductionLot, destPocLoc, newSpecCode, sequence, partNo.getProdDateOffset(), propKey);

						if (!updateNextProductionLots.contains(childLot)) {
							updateNextProductionLots.add(childLot);
						}

						if (!newSpecCode.equals(preProductionLot.getProductSpecCode())) {
							createMbpnProducts(childLot, partNo.getSubAssyProdIdFormat());
							logger.info("Created MBPN Products for child lot ::::: " + childLot);
						} else {
							createStampingRecords(childLot, preProductionLot, newSpecCode, processLocation);
							logger.info("Created Stamping sequence record for child lot ::::: " + childLot);
						}

					} else {
						sequence = String.format("%.2f", Double.valueOf(sequence) + 0.01);
						
						if (!updateNextProductionLots.contains(childLot)) {
							updateNextProductionLots.add(childLot);
						}
						
						List<String> newSpecCodes = new ArrayList<String>();
						// check for the Product Spec Change
						logger.info("child lot found " + childLot.getProductionLot()+ " processeding to product spec check");
						if (!preProductionLot.getProductSpecCode().equals(childLot.getProductSpecCode())) {
							logger.info("preProductionLot spec code " + preProductionLot.getProductSpecCode()+ " Child lot spec code " + childLot.getProductSpecCode() + " is not equal");
							if (childLot.getSendStatus().getId() == PreProductionLotSendStatus.WAITING.getId()) {
								if (partNo.getId().getDestSpecCode().equals("*")) {
									logger.info("New Spec code :::: " + partNo.getId().getDestSpecCode());
									newSpecCodes.add(preProductionLot.getProductSpecCode());
								} else {
									List<String> partNoSubStr = new ArrayList<String>();
									partNoSubStr.add(destSpecCode);
									bom = getBomDao().findProductSpecToBeReplicate(preProductionLot.getProductionLot(), getPartsToExclude(), partNoSubStr);
									if (bom != null) {
										newSpecCodes.add(bom.getId().getPartNo().trim());
										logger.info("New Spec code :::: " + bom.getId().getPartNo());
									} else {
										logger.info("Bom is null, cannot add new spec code");
									}
								}
								boolean childLotUpdated = false;
								if (!newSpecCodes.isEmpty()) {
									for (String specCode : newSpecCodes) {
										logger.info("SpecCode ======> " + specCode);
										logger.info("Child product spec code ::: " + childLot.getProductSpecCode().length() + " New Spec Code length :::" + specCode.length());
										if (!childLot.getProductSpecCode().equalsIgnoreCase(specCode)) {
											logger.info("Updating product spec code for child lot "
													+ childLot.getProductionLot() + " old spec code "
													+ childLot.getProductSpecCode() + " New spec code " + newSpecCodes);
											childLot.setProductSpecCode(specCode);
											childLotUpdated = true;
										}
										if (childLotUpdated) {
											getPreProductionLotDao().update(childLot);
											logger.info("Updated child lot ::::: " + childLot);
											deleteProducts(childLot.getProductionLot());
											logger.info("Deleted Products for child lot ::::: " + childLot);
											
											if (!specCode.equals(preProductionLot.getProductSpecCode())) {
												createMbpnProducts(childLot, partNo.getSubAssyProdIdFormat());
												logger.info("Created MBPN Products for child lot ::::: " + childLot);
											} else {
												createStampingRecords(childLot, preProductionLot, specCode, processLocation);
												logger.info("Created Stamping Records for child lot ::::: " + childLot);
											}
										}
									}
								}
							} else {
								logger.info("The Status of the Child Spec code " + childLot.getProductSpecCode() + " is in in progress or complete");
								errorMessages.add("The Status of the Child Spec code " + childLot.getProductSpecCode() + " is in in progress or complete");
							}
						}
						// Check for the Lot Size change
						if (childLot.getLotSize() != preProductionLot.getLotSize()) {
							logger.info("Child Lot size " + childLot.getLotSize() + " preProductionLot size " + preProductionLot.getLotSize() + " is not equal");
							if (childLot.getSendStatus().getId() == PreProductionLotSendStatus.WAITING.getId()) {
								childLot.setLotSize(preProductionLot.getLotSize());
								getPreProductionLotDao().update(childLot);
								logger.info("Updated child lot ::::: " + childLot);
								deleteProducts(childLot.getProductionLot());
								logger.info("Deleted products for child lot ::::: " + childLot);
								
								if (!childLot.getProductSpecCode().equals(preProductionLot.getProductSpecCode())) {
									createMbpnProducts(childLot, partNo.getSubAssyProdIdFormat());
									logger.info("Created MBPN Products for child lot ::::: " + childLot);
								} else {
									createStampingRecords(childLot, preProductionLot, childLot.getProductSpecCode(), processLocation);
									logger.info("Created Stamping Records for child lot ::::: " + childLot);
								}
								
							} else {
								logger.info("The Status of the Child Lot size " + childLot.getLotSize() + " is in in progress or complete");
								errorMessages.add("The Status of the Child Lot size " + childLot.getLotSize() + " is in in progress or complete");
							}
						}

					}

				}
				if (!updateNextProductionLots.isEmpty())
					updateNextProductionLot(updateNextProductionLots, preProductionLot, destPocLoc, propKey);
			}
		}
		adjustLinkedList(preProductionLotsList, destprocessLocation);

		if (!errorMessages.isEmpty()) {
			errorsCollector.setErrorList(errorMessages);
			errorsCollector.sendEmail();
		}
	}
	
	private void reorderSchedule(String propKey) {
		boolean reorderSchedule = PropertyService.getPropertyBoolean(this.componentId, "REORDER_SCHEDULE", true);

		String sourceProcessLocation = propBean.getSourceProcessLocation().get(propKey);
		String sourcePlanCode = propBean.getSourcePlanCode().get(propKey);
		String destProcessLocation = propBean.getDestProcessLocation().get(propKey);
		String destPlanCode = propBean.getDestPlanCode().get(propKey);

		if(reorderSchedule) {
			
			//Get parent lot order 
			List<PreProductionLot> parentLotOrder = getPreProductionLotDao().findParentLotOrder(sourcePlanCode, sourceProcessLocation, sourcePlanCode, sourceProcessLocation);
			logger.info("fetched Parent Lot Order ::::::: "+ parentLotOrder);

			for(PreProductionLot parent : parentLotOrder) {
				double seq = Double.valueOf(BigDecimal.valueOf(Double.valueOf(parent.getSequence())).add(BigDecimal.valueOf(0.01))+"");

				logger.info("Parent sequencet Number ::::::::::: "+ parent.getSequence());
				logger.info("Reordering start for Parent seq ::::: " + seq + " :: paretn Lot id :::: "+ parent.getProductionLot());
				
				//reordering
				List<PreProductionLot> childLots = getPreProductionLotDao().findByProcessLocationAndLotNumber(destProcessLocation, parent.getLotNumber());
				for(PreProductionLot chidLot : childLots) {
						//generate seq num of child
						logger.info("Generated Child lot Sequence from parent seq =" + seq);

						logger.info("Child lot Sequence from Database" + chidLot.getSequence());
						
						if(chidLot.getSequence()!=seq) {
							chidLot.setSequence(seq);
							logger.info("Assign new sequence number " + seq + " to Child lot");
						}
						seq = Double.valueOf(BigDecimal.valueOf(Double.valueOf(seq)).add(BigDecimal.valueOf(0.01))+"");
				}
				logger.info("Child lots needs to update in database ::::: " + childLots);
				//update seq number of child lot in database 
				getPreProductionLotDao().updateAll(childLots);
			}
		}
		List<String> errorMessages = new ArrayList<>();
		//check duplicates
		List<PreProductionLot> duplicateLots = getPreProductionLotDao().findDuplicateSchedule(destPlanCode, destProcessLocation);
		
		logger.info("Duplicate Lot size :"+ duplicateLots!=null?duplicateLots.size():null);

		if(duplicateLots.size() > 0) {
			errorMessages.add("duplicate child for parent lot");
		}
		
		if (!errorMessages.isEmpty()) {
			errorsCollector.setErrorList(errorMessages);
			errorsCollector.sendEmail();
			logger.info("Send mail alert for duplicate record");
		}
	}

	private void deleteProducts(String preProductionLot) {

		logger.info("Deleting product stamping sequence records for production lot: ====> " + preProductionLot);
		getProductStampingSequenceDao().deleteProductionLot(preProductionLot);
		logger.info("Deleting MBPN product records for production lot: ====> " + preProductionLot);
		getMbpnProductDao().deleteProductionLot(preProductionLot);
	}

	private Map<String, List<ScheduleReplication>> getPartsNosbyProcessLoc() {

		Map<String, String> sourcePlanCodeMap = propBean.getSourcePlanCode();
		Map<String, String> sourceProcessLocationMap = propBean.getSourceProcessLocation();
		Map<String, String> destProcessLocationMap = propBean.getDestProcessLocation();

		String sourceProcessLocation = "";
		String destProcessLocation = "";

		String propKey = "";
		for (Map.Entry<String, String> entry : sourcePlanCodeMap.entrySet()) {
			propKey = entry.getKey();
			sourceProcessLocation = sourceProcessLocationMap.get(propKey);
			destProcessLocation = destProcessLocationMap.get(propKey);
		}

		logger.info("Trying to retrive records for source loction " + sourceProcessLocation + " and dest location " + destProcessLocation);
		List<ScheduleReplication> schduleReplicationLIst = getScheduleReplicationDao().findBySourceLocAndDestLoc(sourceProcessLocation, destProcessLocation);
		Map<String, List<ScheduleReplication>> destProcAndPartNosMap = new HashMap<String, List<ScheduleReplication>>();
		for (ScheduleReplication scheduleReplication : schduleReplicationLIst) {
			String destProcLoc = scheduleReplication.getId().getDestProcLoc();

			if (!destProcAndPartNosMap.containsKey(destProcLoc)) {
				destProcAndPartNosMap.put(destProcLoc, new ArrayList<ScheduleReplication>());
			}
			destProcAndPartNosMap.get(destProcLoc).add(scheduleReplication);
		}
		return destProcAndPartNosMap;
	}

	private List<MbpnProduct> createMbpnProducts(PreProductionLot targetPreProductionLot, String subAssyProdIdFormat) {
		String targetSpecCode = targetPreProductionLot.getProductSpecCode();
		String targetProcessLocation = targetPreProductionLot.getProcessLocation();
		
		Mbpn targetMbpn = getMbpnDao().findByKey(targetSpecCode);
		String mbpnString = "";
		if(targetSpecCode.length() < 18) {
			mbpnString = StringUtil.padRight(targetSpecCode, 18, ' ', false);
		}
		
		if(targetMbpn == null) {
			///create Mbpn record
			Mbpn mbpn = new Mbpn();
				mbpn = new Mbpn();
				mbpn.setMbpn(mbpnString);
				mbpn.setMainNo(mbpnString.substring(0, 5));
				mbpn.setClassNo(mbpnString.substring(5, 8));
				mbpn.setPrototypeCode(mbpnString.substring(8, 9));
				mbpn.setTypeNo(mbpnString.substring(9, 13));
				mbpn.setSupplementaryNo(mbpnString.substring(13, 15));
				mbpn.setTargetNo(mbpnString.substring(15, 17));
				mbpn.setProductSpecCode(targetSpecCode);
				mbpn.setMaskId(getMaskId(mbpnString));
				mbpn.setHesColor("");
				mbpnDao.save(mbpn);
				
				errorsCollector.infoMessage("New MBPN Record has been created...");
				errorsCollector.sendEmail();
		}
		
		if (subAssyProdIdFormat != null && !subAssyProdIdFormat.trim().equals("*") && !getMaskId(mbpnString).isEmpty()) {
			List<ProductStampingSequence> targetProductStampingSequenceList = ServiceFactory.getService(ProductStampingSeqGenService.class).createStampingSequenceList(targetMbpn, targetSpecCode, targetProcessLocation, targetPreProductionLot, propBean, subAssyProdIdFormat);
			if (targetProductStampingSequenceList!=null && !targetProductStampingSequenceList.isEmpty()) {
				getDao(ProductStampingSequenceDao.class).saveAll(targetProductStampingSequenceList);
				targetPreProductionLot = ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(targetPreProductionLot, targetProductStampingSequenceList.get(0).getId().getProductID(), targetSpecCode, targetProcessLocation);
			}
		}
		return null;
	}
	
	private void createStampingRecords(PreProductionLot childLot, PreProductionLot preProductionLot, String newSpecCode, String processLocation) {

		List<Frame> products = getFrameDao().findAllByProductionLot(preProductionLot.getProductionLot());
		List<ProductStampingSequence> pssList = new ArrayList<ProductStampingSequence>();

		int i = 0;
		for (Frame product : products) {
			ProductStampingSequence pss = createProductStampingSequence(product.getProductId(), childLot.getProductionLot(), i + 1);
			i = i+1;
			pssList.add(pss);
		}

		if (!pssList.isEmpty()) {
			getDao(ProductStampingSequenceDao.class).saveAll(pssList);
			ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(childLot, pssList.get(0).getId().getProductID(), newSpecCode, processLocation);
		}
	
	}

	private PreProductionLot createChildLot(PreProductionLot preProductionLot, String destPocLoc, String childSpecCode, String sequence, int prodDateOffset, String propKey) {
		PreProductionLot child = new PreProductionLot();
		BeanUtils.copyProperties(preProductionLot, child);
		List<Date> prodDates = getDailyDepartmentScheduleDao().findAllByOffSet(prodDateOffset, preProductionLot.getPlanOffDate(), preProductionLot.getProcessLocation());
		logger.info("Trying to retrive plan off date for child lot whose parent plan off date " + preProductionLot.getPlanOffDate() + " Offset value " + prodDateOffset);
		if(!prodDates.isEmpty()) {
			logger.info("Setting plan off date to the child lot " + prodDates.get(prodDates.size()-1));
			child.setPlanOffDate(prodDates.get(prodDates.size()-1));
		} else {
			logger.info("No plan off date retrived ");
		}
		
		child.setProductionLot(buildChildLot(preProductionLot.getProductionLot(), sequence, destPocLoc, propKey));
		child.setProductSpecCode(childSpecCode);
		int seq = (int) preProductionLot.getSequence();
		child.setSequence(Double.valueOf(BigDecimal.valueOf(seq).add(BigDecimal.valueOf(Double.valueOf(sequence))) + ""));
		child.setNextProductionLot(null);
		child.setPlanCode(propBean.getDestPlanCode().get(propKey));
		child.setProcessLocation(destPocLoc);
		if (!childSpecCode.equals(preProductionLot.getProductSpecCode())) {
			child.setMbpn(childSpecCode);
		}
		child.setStampedCount(0);
		child.setSentTimestamp(null);
		child.setSendStatus(PreProductionLotSendStatus.WAITING);
		child.setUpdateTimestamp(null);

		return getPreProductionLotDao().save(child);
	}

	private String buildChildLot(String preProductionLot, String sequence, String destPocLoc, String propKey) {
		String sourcePlanCode = propBean.getSourcePlanCode().get(propKey);
		String sourceProcessLocation = propBean.getSourceProcessLocation().get(propKey);
		sequence = sequence.substring(sequence.indexOf('.') + 1, sequence.length());
		String childLotPrefix = REPLICATION_LOT_PREFIX + sequence + sourcePlanCode.substring(3, 6) + destPocLoc;
		return preProductionLot.replace(sourcePlanCode.substring(0, 6)  + sourceProcessLocation, childLotPrefix);
	}

	private PreProductionLot getChildLot(String parentLotNumber, String prodSpecCode, String destProcLoc) {
		logger.info("Trying to retrive child lot with parent lot number " + parentLotNumber + " and product spec code " + prodSpecCode);
		List<PreProductionLot> preProductionLotList = getPreProductionLotDao().findBylotnumberAndProdSpecCodeAndDestProcLoc(parentLotNumber, prodSpecCode, destProcLoc);

		for (PreProductionLot prodLot : preProductionLotList) {
			if (prodLot.getProductionLot().startsWith(REPLICATION_LOT_PREFIX)) {
				return prodLot;
			}
		}
		return null;
	}

	private List<String> getPartsToExclude() {
		return PropertyService.getPropertyList(this.componentId, "PARTS_TO_EXCLUDE");
	}

	private void updateNextProductionLot(List<PreProductionLot> updateNextProductionLotList, PreProductionLot parentLot, String destProcLocation, String propKey) {
		logger.info("Updated next production lot of child lot for parent = " + parentLot.getProductionLot());

		for (int i = 0; i < updateNextProductionLotList.size() - 1; i++) {
			String nextProdLot = updateNextProductionLotList.get(i + 1).getProductionLot();
			PreProductionLot childLot = updateNextProductionLotList.get(i);
			childLot.setNextProductionLot(nextProdLot);
		}

		PreProductionLot lastChild = updateNextProductionLotList.get(updateNextProductionLotList.size() - 1);
		logger.info("Updated next production lot of last child lot  " + lastChild.getProductionLot());

		if (parentLot.getNextProductionLot() == null) {
			lastChild.setNextProductionLot(null);
		} else {
			String nextParentLot = parentLot.getNextProductionLot();
			String nxtLotFirstChildLot = buildChildLot(nextParentLot, "0.01", destProcLocation, propKey);
			PreProductionLot childLotObjectInDb = getPreProductionLotDao().findByKey(nxtLotFirstChildLot);
			if (childLotObjectInDb != null)
				lastChild.setNextProductionLot(nxtLotFirstChildLot);
		}

		getPreProductionLotDao().updateAll(updateNextProductionLotList);
		logger.info("Updated next production lot successfully of all childs of parent " + parentLot.getProductionLot());
		logger.info("Updated next production lot of child lot: RETURN...");
	}

	private void adjustLinkedList(List<PreProductionLot> parentLots, String sourceProcessLoc) {
		logger.info("adjustLinkedList: ENTRY");
		logger.info("adjustLinkedList: parentlots size " + parentLots.size());
		for (int i = 0; i < parentLots.size() - 1; i++) {
			PreProductionLot currentParentLot = parentLots.get(i);
			String lotNo = parentLots.get(i).getLotNumber();
			List<PreProductionLot> childLots = getPreProductionLotDao().findByProcessLocationAndLotNumber(sourceProcessLoc, lotNo);

			PreProductionLot lastChild = null;
			if (!childLots.isEmpty()) {
				lastChild = childLots.get(childLots.size() - 1);
			}
			
			boolean isUpdateCildLot = false;
			
			if (currentParentLot.getNextProductionLot() == null) {
				if (lastChild != null) {
					lastChild.setNextProductionLot(null);
					isUpdateCildLot = true;
				}
			} else if (lastChild != null && lastChild.getNextProductionLot() == null) {
				logger.info("adjustLinkedList: Updated next production lot of last child..." + lastChild.getProductionLot());
				if (currentParentLot.getNextProductionLot() != null) {

					PreProductionLot nextParentLot = getPreProductionLotDao().findByKey(currentParentLot.getNextProductionLot());

					if(nextParentLot!=null) {
					String nextParentlotNo = nextParentLot.getLotNumber();

					List<PreProductionLot> nextParentLotChildLots = getPreProductionLotDao().findByProcessLocationAndLotNumber(sourceProcessLoc, nextParentlotNo);

					if (nextParentLotChildLots!=null && !nextParentLotChildLots.isEmpty()) {
						// first child of next parent
						PreProductionLot firstChildLot = nextParentLotChildLots.get(0);
						lastChild.setNextProductionLot(firstChildLot.getProductionLot());
						isUpdateCildLot = true;
					}
				  }
				}
			}
			if(isUpdateCildLot) {
				getPreProductionLotDao().update(lastChild);
				logger.info("adjustLinkedList: sussessfully updated NextProductionLot number of child lot " + lastChild.getProductionLot());
			}
		}
		logger.info("adjustLinkedList: END");
	}
	
	private String getMaskId(String mbpnString){
		List<Mbpn> mbpns = getMbpnDao().findAllDescByMainNoAndClassNo(mbpnString.substring(0, 5), mbpnString.substring(5, 8));
		String maskId = "";
		for(Mbpn tempMbpn: mbpns){
			if((tempMbpn.getMbpn().substring(0, 11)).equalsIgnoreCase(mbpnString.substring(0, 11))){
				maskId = tempMbpn.getMaskId();
				break;
			}
		}
		return maskId;
	}
	
	private ProductStampingSequence createProductStampingSequence(String Product_id,String productLot,int seq) {
		ProductStampingSequenceId productStampingSequeneId	=	new ProductStampingSequenceId ();
		productStampingSequeneId.setProductID(Product_id);
		productStampingSequeneId.setProductionLot(productLot);
		ProductStampingSequence	productStampingSequence = new ProductStampingSequence ();
		productStampingSequence.setId(productStampingSequeneId);
		productStampingSequence.setSendStatus(0);
		productStampingSequence.setStampingSequenceNumber(seq);
		return productStampingSequence;
	}
}
