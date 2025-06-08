/**
 * 
 */
package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Dec 21, 2011
 */
public class PlcMemoryMapItemDaoImpl extends BaseDaoImpl<PlcMemoryMapItem, String> implements PlcMemoryMapItemDao {

	private static final String FIND_METRICS_BY_PREFIX = "select p from PlcMemoryMapItem p where p.metricId like :metricPattern";

	public List<PlcMemoryMapItem> findAllByPrefix(String metricPattern) {
		return findAllByQuery(FIND_METRICS_BY_PREFIX, Parameters.with("metricPattern" , metricPattern + "%"));
	}
}