package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.CopyCheckerDetailsDto;
import com.honda.galc.entity.conf.MCPddaUnitRevision;
import com.honda.galc.entity.conf.MCPddaUnitRevisionId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCPddaUnitRevisionDao extends IDaoService<MCPddaUnitRevision, MCPddaUnitRevisionId> {
	MCPddaUnitRevision getLatestRevision(String operationName, long pddaPlatformId, String pddaReport);
	public List<MCPddaUnitRevision> findAllBy(String operationName, int opRevId, int pddaPltformId);
	public MCPddaUnitRevision findBy(String operationName, int opRevId, int pddaPltformId);
	
	public MCPddaUnitRevision findBy(String operationName, int opRevId);
	
	public List<CopyCheckerDetailsDto> findAllByUnitNoAndPddaDetails(String unitNo, String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode, String checker);
}
