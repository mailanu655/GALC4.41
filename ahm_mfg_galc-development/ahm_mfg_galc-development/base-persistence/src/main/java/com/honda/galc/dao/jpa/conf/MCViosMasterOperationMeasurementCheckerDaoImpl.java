package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementCheckerId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationMeasurementCheckerDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperationMeasurementChecker
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
 * @author Hemant Kumar<br>
 *         Nov 20, 2018
 */
public class MCViosMasterOperationMeasurementCheckerDaoImpl
		extends BaseDaoImpl<MCViosMasterOperationMeasurementChecker, MCViosMasterOperationMeasurementCheckerId>
		implements MCViosMasterOperationMeasurementCheckerDao {

	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_MEAS_CHECKER_TBX WHERE VIOS_PLATFORM_ID = ?1";

	
	
	
	@Override
	public void deleteAllBy(String viosPlatformId, String unitNo, int measurementSeqNum) {
		if(measurementSeqNum == 0){
			removeByUnitNoAndPartNo(viosPlatformId, unitNo);
		} else {
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.measurementSeqNum",
				measurementSeqNum));
		}
	}

	@Override
	public List<MCViosMasterOperationMeasurementChecker> findAllBy(String viosPlatformId, String unitNo,
			int measurementSeqNum) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo)
				.put("id.measurementSeqNum", measurementSeqNum), new String[] { "id.checkSeq" });
	}

	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationMeasurementChecker mcViosmOpsMeasChecker) {
		try {
			 removeByUnitNoAndPartNo(mcViosmOpsMeasChecker.getId().getViosPlatformId(),
			 mcViosmOpsMeasChecker.getId().getUnitNo(), mcViosmOpsMeasChecker.getId().getMeasurementSeqNum(), mcViosmOpsMeasChecker.getId().getCheckSeq());
			 save(mcViosmOpsMeasChecker);
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void removeByUnitNoAndPartNo(String viosplatform, String unitNo) {
		Parameters params = Parameters.with("id.viosPlatformId", viosplatform).put("id.unitNo", unitNo);
		delete( params);
	}
	
	@Transactional
	public void removeByUnitNoAndPartNo(String viosplatform, String unitNo, int measurementSeq, int  checkSeq) {
		Parameters params = Parameters.with("id.viosPlatformId", viosplatform).put("id.unitNo", unitNo).put("id.measurementSeqNum", measurementSeq).put("id.checkSeq", checkSeq);
		delete(params);
	}

	@Override
	public List<MCViosMasterOperationMeasurementChecker> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterOperationMeasurementChecker.class);
	}

	@Override
	public List<MCViosMasterOperationMeasurementChecker> findAllBy(String viosPlatformId, String unitNo) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo),
				new String[] { "id.measurementSeqNum", "id.checkSeq" });
	}

	@Override
	public void deleteAndInsertAll(String viosPlatformId, String unitNo, int measurementSeqNum,
			List<MCViosMasterOperationMeasurementChecker> opMeasCheckerList) {
		
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.measurementSeqNum", measurementSeqNum));
		if(opMeasCheckerList.size() > 0)
			saveAll(opMeasCheckerList);
		
	}
	
	@Override
	public List<MCViosMasterOperationMeasurementChecker> findAllmeasurements(String viosPlatformId, String unitNo, int measSeq) {
		Parameters parameters = null;
		if(measSeq < 0) {
			parameters = Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo);
		} else {
			parameters = Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.measurementSeqNum", measSeq);
		}
		return findAll(parameters);
	}


}
