package com.honda.galc.dao.jpa.conf;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterOperationPartDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationPartDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperationPart
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
public class MCViosMasterOperationPartDaoImpl extends
		BaseDaoImpl<MCViosMasterOperationPart, MCViosMasterOperationPartId> implements MCViosMasterOperationPartDao {

	private static final String FIND_ALL_FILTERED_PART = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_PART_TBX where VIOS_PLATFORM_ID=?1 AND (UPPER(UNIT_NO) LIKE ?2 OR UPPER(PART_NO) LIKE ?2 "
			+ " OR  UPPER(PART_TYPE) LIKE ?2 "
			+ " OR UPPER(PART_DESC) LIKE ?2 OR UPPER(PART_MASK) LIKE ?2) ORDER BY UNIT_NO, PART_NO";

	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_PART_TBX WHERE VIOS_PLATFORM_ID = ?1";

	private static final String DELETE_UNTT_NO_BY_PART_NO = "DELETE FROM "
			+ "galadm.MC_VIOS_MASTER_OP_PART_TBX WHERE VIOS_PLATFORM_ID = ?1 AND UNIT_NO = ?2 AND PART_NO = ?3";

	@Override
	public List<MCViosMasterOperationPart> findAllFilteredPart(String viosPlatformId, String filter) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", "%" + filter + "%");
		return findAllByNativeQuery(FIND_ALL_FILTERED_PART, params);
	}

	@Override
	public List<MCViosMasterOperationPart> findAllBy(String viosPlatformId, String unitNo) {
		Parameters params = Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.partType", PartType.MFG);
		return findAll(params);
	}

	@Override
	public List<MCViosMasterOperationPart> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterOperationPart.class);
	}

	@Transactional
	public void removeByPlatformIdAndUnitNoAndPartNo(String platformId, String unitNo, String partNo) {
		Parameters params = Parameters.with("1", platformId).put("2", unitNo).put("3", partNo);
		executeNativeUpdate(DELETE_UNTT_NO_BY_PART_NO, params);
	}

	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationPart mcMasterPart) {
		mcMasterPart.getId().setPartType(PartType.MFG);
		try {
			removeByPlatformIdAndUnitNoAndPartNo(mcMasterPart.getId().getViosPlatformId(),mcMasterPart.getId().getUnitNo(), mcMasterPart.getId().getPartNo());
			insert(mcMasterPart);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
