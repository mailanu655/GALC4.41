/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.product.HoldReasonMapping;
import com.honda.galc.service.IDaoService;

/**
 * @author hat0926
 *
 */
public interface HoldReasonMappingDao extends IDaoService<HoldReasonMapping, Integer>{
	List<HoldReasonMapping> findAllByReasonId(int reasonId);
	List<HoldReasonMappingDto> findAllByDivision(String divisionId);
	List<HoldReasonMappingDto> findAllMappedReasons();
	List<HoldReasonMappingDto> findAllByLine(String lineId);
	List<HoldReasonMappingDto> findAllByAction(String actionId);
	List<HoldReasonMappingDto> findAllByLineAndAction(String lineId,String actionId);
	List<HoldReasonMapping> findAllByLineReasonAndAction(String lineId,int reasonId, String actionId);
	List<HoldReasonMappingDto> findAllByDivisionAndAction(String divisionId,String actionId);
	List<HoldReasonMapping> findAllByHoldReason(String holdReason);
	
}
