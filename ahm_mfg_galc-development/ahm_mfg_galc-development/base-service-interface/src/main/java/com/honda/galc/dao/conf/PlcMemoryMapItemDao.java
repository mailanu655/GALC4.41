/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Dec 21, 2011
 */
public interface PlcMemoryMapItemDao extends IDaoService<PlcMemoryMapItem, String> {
	public List<PlcMemoryMapItem> findAllByPrefix(String metricPattern);
}
