package com.honda.vios.service;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCOperationCheckerDao;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationId;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.UnitPartDto;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.service.MfgDataLoaderService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;



public class ViosMaintenanceServiceImpl implements ViosMaintenanceService {

	@Transactional
	@Override
	public void deleteViosUnitNoConfig(MCViosMasterOperationId masterOpId) {
		ServiceFactory.getDao(MCViosMasterOperationDao.class).removeByKey(masterOpId);
		ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).deleteAllBy(masterOpId.getViosPlatformId(),
				masterOpId.getUnitNo());
	}

	@Transactional
	@Override
	public void deleteViosUnitNoConfigWithoutCheckers(MCViosMasterOperationId masterOpId) {
		ServiceFactory.getDao(MCViosMasterOperationDao.class).removeByKey(masterOpId);
	}
	
	@Transactional
	@Override
	public int getAllUnitCheckers(MCViosMasterOperationId masterOpId) {
		List<MCViosMasterOperationChecker>  unitCheckers = ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).findAllBy(masterOpId.getViosPlatformId(), masterOpId.getUnitNo());
		return unitCheckers.size();
	}

	@Transactional
	@Override
	public void deleteByPlatform(String viosPlatform) {
		ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).deleteByPlatform(viosPlatform);
	}
	
	
	@Transactional
	@Override
	public void deleteViosPartConfig(MCViosMasterOperationPartId masterOpPartId) {
		ServiceFactory.getDao(MCViosMasterOperationPartDao.class).removeByKey(masterOpPartId);
		ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).deleteAllBy(masterOpPartId.getViosPlatformId(),
				masterOpPartId.getUnitNo(), masterOpPartId.getPartNo());
	}
	
	@Transactional
	@Override
	public void deleteViosPartConfigWithoutCheckers(MCViosMasterOperationPartId masterOpPartId) {
		ServiceFactory.getDao(MCViosMasterOperationPartDao.class).removeByKey(masterOpPartId);
	}


	@Transactional
	@Override
	public int getAllPartCheckers(MCViosMasterOperationPartId masterOpPartId) {
		List<MCViosMasterOperationPartChecker> masterOpPartsCheckers = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).findAllBy(masterOpPartId.getViosPlatformId(), masterOpPartId.getUnitNo(), masterOpPartId.getPartNo());
		return masterOpPartsCheckers.size();
	}
	
	@Transactional
	@Override
	public void deleteViosMeasConfig(MCViosMasterOperationMeasurementId masterOpMeasId) {
		ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).removeByKey(masterOpMeasId);
		ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class).deleteAllBy(
				masterOpMeasId.getViosPlatformId(), masterOpMeasId.getUnitNo(),
				masterOpMeasId.getMeasurementSeqNum());

		ServiceFactory.getDao(MCMeasurementCheckerDao.class).deleteAllMeasurementRevision(masterOpMeasId.getViosPlatformId(), masterOpMeasId.getUnitNo(),
				masterOpMeasId.getMeasurementSeqNum());
		
	}
	
	public int getAllUnitMeasurements(MCViosMasterOperationMeasurementId id) {
		List<MCViosMasterOperationMeasurementChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class).findAllBy(id.getViosPlatformId(), id.getUnitNo(), id.getMeasurementSeqNum());
		return checkerList.size();
	}
	@Transactional
	@Override
	public void deleteViosMeasConfigWithoutCheckers(MCViosMasterOperationMeasurementId masterOpMeasId) {
		ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).removeByKey(masterOpMeasId);
		ServiceFactory.getDao(MCOperationMeasurementDao.class).deleteMeasurementRevision(masterOpMeasId.getMeasurementSeqNum(), masterOpMeasId.getUnitNo(), masterOpMeasId.getViosPlatformId());
		
	}

	@Override
	@Transactional
	public void updateOpPartRevisions(MCViosMasterOperationPart masterOpPart) {
		if (masterOpPart != null && StringUtils.isNotBlank(masterOpPart.getOperationName())) {
			try {
				List<MCOperationPartRevision> partRevisions = getDao(MCOperationPartRevisionDao.class)
						.findAllActivePartsByOperationName(masterOpPart.getOperationName());
				if (!partRevisions.isEmpty()) { // Getting reference and MFG
												// parts from operation part
												// revision
					Map<Integer, MCOperationPartRevision> mfgParts = new HashMap<Integer, MCOperationPartRevision>();
					Map<Integer, MCOperationPartRevision> refParts = new HashMap<Integer, MCOperationPartRevision>();
					List<Integer> partIdNumList = new ArrayList<Integer>();
					for (MCOperationPartRevision partRevision : partRevisions) {
						Integer partId = Integer.valueOf(partRevision.getId().getPartId().substring(1,
								partRevision.getId().getPartId().length()));
						partIdNumList.add(partId);
						if (PartType.MFG.equals(partRevision.getPartType())) {
							mfgParts.put(partId, partRevision);
						}
						if (partRevision.getPartNo().equalsIgnoreCase(masterOpPart.getId().getPartNo())) {
							if (PartType.REFERENCE.equals(partRevision.getPartType())) {
								refParts.put(partId, partRevision);
							}
						}
					}
					if (!mfgParts.isEmpty()) { // Default MFG
						MCOperationPartRevision defaultMFgPart = mfgParts.get(Collections.min(partIdNumList));
						if (StringUtils.isNotBlank(masterOpPart.getId().getPartNo())) {
							// This is a MFG part
							for (MCOperationPartRevision refPart : refParts.values()) {
								boolean ifMfgPartExists = false;
								for (MCOperationPartRevision mfgPart : mfgParts.values()) {
									if (StringUtils.equals(refPart.getPartNo(), mfgPart.getPartNo())) {
											//&& StringUtils.equals(refPart.getPartSectionCode(),
													//mfgPart.getPartSectionCode())
											//&& StringUtils.equals(refPart.getPartItemNo(), mfgPart.getPartItemNo())) {
										try {// MFG Part Exists
											ifMfgPartExists = true; // Update
																	// part mask
											updatePartDetails(masterOpPart.getPartMask(),masterOpPart.getPartCheck() ,mfgPart);
										} catch (Exception e) {
											Logger.getLogger().error(e, "An exception occured while updating Mfg Part");
											e.printStackTrace();
										}
									}
								}
								if (!ifMfgPartExists) { // MFG Part Does Not
														// Exist Create MFG Part
									try {
										refPart.setPartCheck(masterOpPart.getPartCheck());
										ServiceFactory.getService(MfgDataLoaderService.class).createMfgPart(
												masterOpPart.getOperationName(),
												new PartDto(masterOpPart.getId().getPartNo(),
														masterOpPart.getPartMask()),
												defaultMFgPart, refPart);

									} catch (Exception e) {
										Logger.getLogger().error(e,
												"An exception occured while creating default Mfg Part");
										e.printStackTrace();
									}
								}

							}
						} else { // This is a default MFG part
							try {
								updatePartDetails(masterOpPart.getPartMask(),masterOpPart.getPartCheck(), defaultMFgPart);
							} catch (Exception e) {
								Logger.getLogger().error(e, "An exception occured while updating default Mfg Part");
								e.printStackTrace();
							}
						}
					}
					// NALC-1473: Update the Active Revision Part Mask
					MCOperationPartRevision activeRevision = partRevisions.get(0);
					boolean isPartMaskUpdated = masterOpPart.getPartMask() != null && !masterOpPart.getPartMask().equals(activeRevision.getPartMask());
					boolean isPartCheckUpdated = masterOpPart.getPartCheck() != null && masterOpPart.getPartCheck() != activeRevision.getPartCheck();
					if (isPartMaskUpdated || isPartCheckUpdated) {
						updatePartDetails(masterOpPart.getPartMask(), masterOpPart.getPartCheck() , partRevisions.get(0));
					}
				}
			} catch (Exception e) {
				Logger.getLogger().error(e, "An exception occured while creating updating Part");
				e.printStackTrace();
			}
		}
	}

	public void updatePartDetails(String partMask, PartCheck partCheck, MCOperationPartRevision mfgPart) {
		MCOperationPartRevision operationPartRevision = mfgPart;
		operationPartRevision.setPartMask(partMask);
		operationPartRevision.setPartCheck(partCheck);
		getDao(MCOperationPartRevisionDao.class).update(operationPartRevision);
	}

	
	
	public String uploadMcOpMeasurement (MCViosMasterOperationMeasurement obj, int measSeq) {
	try {	
		
		List<McOperationDataDto> partRevisions = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findAllActiveByOperationName(obj.getOperationName());
		boolean isEmpty = true;
		for (McOperationDataDto partRevision : partRevisions) {
			isEmpty = false;
			String partId = StringUtils.trimToEmpty(partRevision.getPartId());
			int partRev = partRevision.getPartRevision();
			String optName = StringUtils.trimToEmpty(obj.getOperationName());
			String numOfBolts = StringUtils.trimToEmpty(String.valueOf(obj.getNumOfBolts()));
			String min_limit = StringUtils.trimToEmpty(String.valueOf(obj.getMinLimit()));
			String max_limit = StringUtils.trimToEmpty(String.valueOf(obj.getMaxLimit()));
			String maxAttempt = StringUtils.trimToEmpty(String.valueOf(obj.getMaxAttempts()));
			String pset = StringUtils.trimToEmpty(obj.getDeviceMsg());
			String tool = StringUtils.trimToEmpty(obj.getDeviceId());
			ServiceFactory.getService(MfgDataLoaderService.class).addMeasurementByQty(optName, partId, new Integer(partRev), new Integer(numOfBolts), min_limit, max_limit,new Integer(maxAttempt), pset, tool, measSeq);
		   }
		if (isEmpty) {
			Logger.getLogger().info(obj.getOperationName(), "Operation does not exist or not of measurement type");
		}
		} catch (Exception e) {
			Logger.getLogger().error(e, "An exception occured while addingm measurement");
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@Override
	public void uploadViosMasterPart(MCViosMasterOperationPart part)  {
		updateOpPartRevisions(part);
		ServiceFactory.getDao(MCViosMasterOperationPartDao.class).saveEntity(part);
	}
	
	@Transactional
	@Override
	public void uploadViosMasterMeas(MCViosMasterOperationMeasurement meas)  {
		uploadMcOpMeasurement(meas, -1);
		ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).uploadMeasurementByQty(meas);
		
	}
	
	
	@Transactional
	@Override
	public void uploadViosMasterOperation(MCViosMasterOperation opt) {
	 ServiceFactory.getDao(MCViosMasterOperationDao.class).saveEntity(opt);
	 ServiceFactory.getDao(MCOperationRevisionDao.class).saveEntity(opt);
    	
		
	}

	@Transactional
	@Override
	public void uploadViosMasterOperationChecker(MCViosMasterOperationChecker optChecker) {
		 ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).saveEntity(optChecker);
    	 ServiceFactory.getDao(MCOperationCheckerDao.class).saveEntity(optChecker);
	}

	@Transactional
	@Override
	public void updateViosMasterMeas(MCViosMasterOperationMeasurement meas) {
		ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).saveEntity(meas);
		uploadMcOpMeasurement(meas, meas.getId().getMeasurementSeqNum());
	}

	@Transactional
	@Override
	public void uploadViosMasterMeasChecker(MCViosMasterOperationMeasurementChecker measChecker) {
		 ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class).saveEntity(measChecker);
		 ServiceFactory.getDao(MCMeasurementCheckerDao.class).saveEntity(measChecker);
    }

	
	@Transactional
	@Override
	public void deleteByProcessPointId(String processPointId, String viosPlatform) {
		 ServiceFactory.getDao(MCViosMasterProcessDao.class).deleteByProcessPointId(processPointId, viosPlatform);
	}

	@Transactional
	@Override
	public void uploadViosMasterPartChecker(MCViosMasterOperationPartChecker partChecker) {
		ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).saveEntity(partChecker);
   	   	ServiceFactory.getDao(MCPartCheckerDao.class).saveEntity(partChecker);
		
	}
	
	@Transactional
	@Override
	public void uploadViosMasterPlatform(MCViosMasterPlatform mcplatform) {
		ServiceFactory.getDao(MCViosMasterPlatformDao.class).saveEntity(mcplatform);
   	 }


	
	@Transactional
	@Override
	public void deleteAndInsertAllMasterMeansChecker(List<MCViosMasterOperationMeasurementChecker> meansCheckerList,
		MCViosMasterOperationMeasurement opMeas) {
		ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class).deleteAndInsertAll(opMeas.getId().getViosPlatformId(), opMeas.getId().getUnitNo(), opMeas.getId().getMeasurementSeqNum(), meansCheckerList);
		ServiceFactory.getService(MCMeasurementCheckerDao.class).deleteAndInsertAllMeasurementCheckerRevision(opMeas.getId().getViosPlatformId(), opMeas.getId().getUnitNo(),opMeas.getId().getMeasurementSeqNum(), meansCheckerList);

	}

	@Transactional
	@Override
	public void deleteAndInsertAllMasterOperationChecker(List<MCViosMasterOperationChecker> masterOperationCheckerList,
			MCViosMasterOperation masterOperation) {
		ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).deleteAndInsertAll(masterOperation.getId().getViosPlatformId(), masterOperation.getId().getUnitNo(),  masterOperationCheckerList);
		ServiceFactory.getDao(MCOperationCheckerDao.class).deleteAndInsertAllOperatioRevCheckers(masterOperation.getId().getViosPlatformId(), masterOperation.getId().getUnitNo(),  masterOperationCheckerList);
	}
	
	@Transactional
	@Override
	public void deleteAndInsertAllMasterPartChecker(List<MCViosMasterOperationPartChecker> partCheckerList,
			MCViosMasterOperationPart partChecker) {
		ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).deleteAndInsertAll(partChecker.getId().getViosPlatformId(), partChecker.getId().getUnitNo(), partChecker.getId().getPartNo(), partCheckerList);
		ServiceFactory.getDao(MCPartCheckerDao.class).deleteAndInsertAllPartRevisionCheckers(partChecker.getId().getViosPlatformId(), partChecker.getId().getUnitNo(),partChecker.getId().getPartNo(), partCheckerList);
	}		
	
	@Transactional
	@Override
	public void deleteByOperation(String viosPlatform, Set<String> operationSet) {
		for (String unitNo : operationSet) {
			ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).deleteAllBy(viosPlatform, unitNo);
			ServiceFactory.getDao(MCOperationCheckerDao.class).deleteAllRevOperation(viosPlatform, unitNo);
		}
	}
	
	@Transactional
	@Override
	public void deleteByPartNumber(String viosPlatform, Set<UnitPartDto> partCheckerSet) {
		for (UnitPartDto unitPartDAO : partCheckerSet) {
			ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).deleteAllBy(viosPlatform,unitPartDAO.getUnitNo(), unitPartDAO.getPartNo());
			ServiceFactory.getDao(MCPartCheckerDao.class).deleteAllPartRev(viosPlatform, unitPartDAO.getUnitNo(), unitPartDAO.getPartNo());
		}
	}
	
	@Transactional
	@Override
	public void deleteAllMeasChecker(String viosPlatform, Set<String> operationSet) {
		for (String unitNo : operationSet) {
			ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class).deleteAllBy(viosPlatform, unitNo, 0);
			ServiceFactory.getDao(MCMeasurementCheckerDao.class).deleteAllRevOperation(viosPlatform, unitNo);
		}
	}
	
	@Transactional
	@Override
	public void deleteMBPNData(String viosPlatformId, MCViosMasterMBPNMatrixData mcmbpnMasterData){
		
		if(mcmbpnMasterData != null) {
			ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).deleteMBPNMaster(mcmbpnMasterData, viosPlatformId);
			ServiceFactory.getDao(MCOperationMatrixDao.class).deleteMBPNOpMatrixData(mcmbpnMasterData, viosPlatformId, false);
			ServiceFactory.getDao(MCOperationPartMatrixDao.class).deleteMBPNPartMatrixData(mcmbpnMasterData, viosPlatformId, false);
		}
		
	}

	@Transactional
	@Override
	public void updateMBPNMatrixData(MCViosMasterMBPNMatrixData entity, String viosPlatform) {
		if(entity != null) {
		ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).updateMBPNMasterRevision(entity, viosPlatform);
		}
	}

}
