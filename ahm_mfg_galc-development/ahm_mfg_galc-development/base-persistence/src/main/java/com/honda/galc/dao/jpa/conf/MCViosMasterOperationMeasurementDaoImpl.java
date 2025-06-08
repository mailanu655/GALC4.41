
package com.honda.galc.dao.jpa.conf;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.MCViosMasterOperationMeasurementDto;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationMeasurementDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperationMeasurement
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
public class MCViosMasterOperationMeasurementDaoImpl
		extends BaseDaoImpl<MCViosMasterOperationMeasurement, MCViosMasterOperationMeasurementId>
		implements MCViosMasterOperationMeasurementDao {

	private static final String FIND_ALL_FILTERED_MEAS = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_MEAS_TBX where VIOS_PLATFORM_ID=?1 "
			+ " AND (UPPER(UNIT_NO) LIKE ?2 "
			+ " OR  UPPER(PART_TYPE) LIKE ?2 OR UPPER(DEVICE_ID) LIKE ?2 OR UPPER(DEVICE_MSG) LIKE ?2 "
			+ " OR UPPER(MIN_LIMIT) LIKE ?2 OR UPPER(MAX_LIMIT) LIKE ?2 OR UPPER(MEAS_MAX_ATTEMPTS) LIKE ?2) "
			+ " ORDER BY UNIT_NO, OP_MEAS_SEQ_NUM";

	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT  UNIT_NO, COUNT(OP_MEAS_SEQ_NUM) AS OP_MEAS_SEQ_NUM , MIN_LIMIT, MAX_LIMIT, DEVICE_MSG, DEVICE_ID  FROM  galadm.MC_VIOS_MASTER_OP_MEAS_TBX "
			+ " WHERE VIOS_PLATFORM_ID = ?1 GROUP BY UNIT_NO, MIN_LIMIT, MAX_LIMIT, DEVICE_MSG, DEVICE_ID";
	
	@Override
	public List<MCViosMasterOperationMeasurement> findAllFilteredMeas(String viosPlatformId, String filter) {
		Parameters params = Parameters.with("1", viosPlatformId)
				.put("2", "%" + filter + "%");
		return findAllByNativeQuery(FIND_ALL_FILTERED_MEAS, params);
	}


	@Transactional
	public void removeByUnitNoAndPartNo(String platformNo, String unitNo, int measUnitSeq) {
		Parameters params = Parameters.with("id.viosPlatformId", platformNo).put("id.unitNo", unitNo).put("id.measurementSeqNum", measUnitSeq);
		delete(params);
	}

	@Override
	public List<MCViosMasterOperationMeasurement> findAllData(String viosPlatformId) {
		List<MCViosMasterOperationMeasurement> measObjList = new ArrayList<MCViosMasterOperationMeasurement>();
		try {
			Parameters params = Parameters.with("1", viosPlatformId);
			List<MCViosMasterOperationMeasurementDto> objList = findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params,
					MCViosMasterOperationMeasurementDto.class);
			for (MCViosMasterOperationMeasurementDto measDto : objList) {
				MCViosMasterOperationMeasurement meanObj = new MCViosMasterOperationMeasurement();
				MCViosMasterOperationMeasurementId meanObjId = new MCViosMasterOperationMeasurementId();
				meanObjId.setUnitNo(measDto.getUnitNo());
				meanObjId.setMeasurementSeqNum(1);
				meanObj.setId(meanObjId);
				meanObj.setMaxAttempts(meanObj.getMaxAttempts());
				meanObj.setMinLimit(measDto.getMinLimit());
				meanObj.setMaxLimit(measDto.getMaxLimit());
				meanObj.setDeviceMsg(measDto.getDeviceMsg());
				meanObj.setDeviceId(measDto.getDeviceId());
				meanObj.setNumOfBolts(measDto.getNoMeansSeq());
				measObjList.add(meanObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return measObjList;
	}

	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationMeasurement mcMeansObj) {
	try {
			mcMeansObj.getId().setPartType(PartType.MFG);
			removeByUnitNoAndPartNo(mcMeansObj.getId().getViosPlatformId(), mcMeansObj.getId().getUnitNo(), mcMeansObj.getId().getMeasurementSeqNum());
			insert(mcMeansObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MCViosMasterOperationMeasurement> findAllBy(String viosPlatformId, String unitNo) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo),
				new String[] { "id.measurementSeqNum" });
	}
	
	@Override
	public int getFirstMeasurementSequenceNumber(String viosPlatformId, String unitNo) {
		return findFirst(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo),
				new String[] { "id.measurementSeqNum" }, true).getId().getMeasurementSeqNum();
	}

	@Transactional
	@Override
	public void uploadMeasurementByQty(MCViosMasterOperationMeasurement mcViosMasterOpMeas) {
		try {
			deleteBy(mcViosMasterOpMeas.getId().getViosPlatformId(), mcViosMasterOpMeas.getId().getUnitNo());
			for (int i = 1; i <= mcViosMasterOpMeas.getNumOfBolts(); i++) {
				getDao(MCViosMasterOperationMeasurementDao.class)
						.insert(convertToMeasEntity(mcViosMasterOpMeas.getId().getViosPlatformId(),
								mcViosMasterOpMeas.getId().getUnitNo(), i, mcViosMasterOpMeas.getMinLimit(),
								mcViosMasterOpMeas.getMaxLimit(), mcViosMasterOpMeas.getMaxAttempts(),
								mcViosMasterOpMeas.getDeviceMsg(), mcViosMasterOpMeas.getDeviceId(),mcViosMasterOpMeas.getUserId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private MCViosMasterOperationMeasurement convertToMeasEntity(String vios, String unitNo, int measSeq,
			double minLimit, double maxLimit, int maxAtt, String pset, String tool, String userId) {
		MCViosMasterOperationMeasurement means = new MCViosMasterOperationMeasurement();
		MCViosMasterOperationMeasurementId id = new MCViosMasterOperationMeasurementId();
		id.setViosPlatformId(vios);
		id.setUnitNo(unitNo);
		id.setPartType(PartType.MFG);
		id.setMeasurementSeqNum(measSeq);
		means.setId(id);
		means.setDeviceId(tool);
		means.setDeviceMsg(pset);
		means.setMinLimit(minLimit);
		means.setMaxLimit(maxLimit);
		means.setMaxAttempts(maxAtt);
		means.setUserId(userId);
		return means;
	}

	@Transactional
	public void deleteBy(String viosplatformId, String unitNo) {
		Parameters param = Parameters.with("id.viosPlatformId", viosplatformId);
		param.put("id.unitNo", unitNo);
		delete(param);
	}

}
