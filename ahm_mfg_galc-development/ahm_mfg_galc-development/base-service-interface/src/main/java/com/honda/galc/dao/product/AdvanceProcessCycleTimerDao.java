
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.AdvanceProcessCycleTimer;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.product.StragglerId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author vfc91343
 * @date May 15, 2018
 */
public interface AdvanceProcessCycleTimerDao extends IDaoService<AdvanceProcessCycleTimer, String> {

	//public List<AdvanceProcessCycleTimer> findAll();
	public List<AdvanceProcessCycleTimer> findByLineId(String lineId);
	public Integer getNextId();
}
