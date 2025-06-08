package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HoldReasonDao;
import com.honda.galc.dao.product.HoldReasonMappingDao;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.product.HoldReason;
import com.honda.galc.entity.product.HoldReasonMapping;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldReasonDaoImpl</code> is ... .
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
 * @author Prasanna Parvathaneni
 */
public class HoldReasonMappingDaoImpl extends
		BaseDaoImpl<HoldReasonMapping, Integer> implements HoldReasonMappingDao {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL_MAPPED_REASONS = "select " + 
			"MAPPING.REASON_ID, " + 
			"MAPPING.REASON_MAPPING_ID, " + 
			"MAPPING.QC_ACTION_ID, " + 
			"MAPPING.DIVISION_ID, " + 
			"MAPPING.LINE_ID, " + 
			"REASON.HOLD_REASON, " + 
			"MAPPING.ASSOCIATE_ID, " + 
			"MAPPING.ASSOCIATE_NAME " + 
			"from HOLD_REASON_MAPPING_TBX MAPPING " + 
			"JOIN HOLD_REASON_TBX REASON ON REASON.REASON_ID = MAPPING.REASON_ID ";
	
	public static final String FIND_ALL_BY_DIVISION = FIND_ALL_MAPPED_REASONS + " WHERE MAPPING.DIVISION_ID = ?1";
	
	public static final String FIND_ALL_BY_LINE = FIND_ALL_MAPPED_REASONS + " WHERE MAPPING.LINE_ID = ?1";
	
	public static final String FIND_ALL_BY_ACTION = FIND_ALL_MAPPED_REASONS + " WHERE MAPPING.QC_ACTION_ID = ?1";
	
	public static final String FIND_ALL_BY_LINE_AND_ACTION = FIND_ALL_MAPPED_REASONS + " WHERE MAPPING.LINE_ID = ?1 AND MAPPING.QC_ACTION_ID = ?2";
	
	public static final String FINAL_ALL_BY_DIVISION_AND_ACTION = FIND_ALL_MAPPED_REASONS + " WHERE MAPPING.DIVISION_ID = ?1 AND MAPPING.QC_ACTION_ID = ?2";
	

	@Transactional
	public List<HoldReasonMapping> saveAll(List<HoldReasonMapping> entities) {
		for (HoldReasonMapping reasonMapping : entities) {
			if (reasonMapping != null) {
				List<HoldReasonMapping> list = findAllByLineReasonAndAction(
						reasonMapping.getLineId(), reasonMapping.getReasonId(), reasonMapping.getQcActionId());
				if (list == null || list.size() == 0) {
					insert(reasonMapping);
				}
			}
		}

		return entities;
	}

	@Transactional
	public HoldReasonMapping insert(HoldReasonMapping entity) {
		Integer max = max("reasonMappingId", Integer.class);
		int maxId = max == null ? 1 : max + 1;
		entity.setReasonMappingId(maxId);
		return super.insert(entity);
	}
	
	public List<HoldReasonMappingDto> findAllMappedReasons() {
		return findAllByNativeQuery(FIND_ALL_MAPPED_REASONS, null, HoldReasonMappingDto.class);
	}

	public List<HoldReasonMapping> findAllByReasonId(int reasonId) {
		return findAll(Parameters.with("reasonId", reasonId));
	}

	public List<HoldReasonMapping> findAllByHoldReason(String holdReason) {
		HoldReason reason = ServiceFactory.getDao(HoldReasonDao.class).findByHoldReason(holdReason);
		return findAllByReasonId(reason.getReasonId());
	}

	public List<HoldReasonMappingDto> findAllByDivision(String divisionId) {
		Parameters params = Parameters.with("1", divisionId);
		return findAllByNativeQuery(FIND_ALL_BY_DIVISION, params, HoldReasonMappingDto.class);
	}

	public List<HoldReasonMappingDto> findAllByLine(String lineId) {
		Parameters params = Parameters.with("1", lineId);
		return findAllByNativeQuery(FIND_ALL_BY_LINE, params, HoldReasonMappingDto.class);
	}

	public List<HoldReasonMappingDto> findAllByAction(String actionId) {
		Parameters params = Parameters.with("1", actionId);
		return findAllByNativeQuery(FIND_ALL_BY_ACTION, params, HoldReasonMappingDto.class);
	}

	public List<HoldReasonMapping> findAllByLineReasonAndAction(String lineId, int i, String actionId) {
		Parameters params = Parameters.with("reasonId", i).put("lineId", lineId);
		params.put("qcActionId", actionId);
		return findAll(params);
	}

	public List<HoldReasonMappingDto> findAllByLineAndAction(String lineId, String actionId) {
		Parameters params = Parameters.with("1", lineId).put("2", actionId);
		return findAllByNativeQuery(FIND_ALL_BY_LINE_AND_ACTION, params, HoldReasonMappingDto.class);
	}
	
	public List<HoldReasonMapping> findAllByLineReasonAndAction(String lineId, String reasonId, String actionId) {
		Parameters params = Parameters.with("lineId", lineId).put("reasonId", reasonId);
		params.put("qcActionId", actionId);
		return findAll(params);
	}

	public List<HoldReasonMappingDto> findAllByDivisionAndAction(String divisionId, String actionId) {
		Parameters params = Parameters.with("1", divisionId).put("2", actionId);
		return findAllByNativeQuery(FINAL_ALL_BY_DIVISION_AND_ACTION, params, HoldReasonMappingDto.class);
	}
}