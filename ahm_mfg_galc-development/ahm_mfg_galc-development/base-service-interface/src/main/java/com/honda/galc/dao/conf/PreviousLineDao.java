package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.PreviousLineId;
import com.honda.galc.service.IDaoService;


public interface PreviousLineDao extends IDaoService<PreviousLine, PreviousLineId> {
	
	public List<PreviousLine> findAllByLineId(String lineId);
	
	/**
	 * Returns all PreviousLine records for the Line assigned to the given ProcessPoint.
	 */
	public List<PreviousLine> findAllByProcessPointId(String processPointId);
}
