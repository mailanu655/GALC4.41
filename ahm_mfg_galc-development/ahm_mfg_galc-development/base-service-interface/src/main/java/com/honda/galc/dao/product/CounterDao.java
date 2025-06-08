package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.CounterId;
import com.honda.galc.service.IDaoService;

public interface CounterDao extends IDaoService<Counter, CounterId> {

	int incrementCounter(CounterId id);

	/**
	 * get sum of passing counter by shift for all process points on a production date
	 * @param productionDate
	 * @return
	 */
	public List<Counter> findCounts(Date productionDate);

    /**
     * get the all the counter for the division on the production date
     * @param productionDate
     * @param divisionId
     * @return
     */
	public List<Counter> findAllByDivisionIdAndProductionDate(Date productionDate, String divisionId);
	
	public Long findCount(Date productionDate, String processPoint);

	/**
	 * Get a sum of the passing count for a specific process point for current production date.
	 * @param processPointId
	 * @return
	 */
	public int getPassingBodyCountForCurrentDate(String processPointId); 

	/**
	 * Get a sum of the passing count for a specific process point and shift for a date.
	 * @param processPointId
	 * @return
	 */
	public Integer getCountByDateShiftAndProcess(Date productionDate, String shift, String processPointId);
}
