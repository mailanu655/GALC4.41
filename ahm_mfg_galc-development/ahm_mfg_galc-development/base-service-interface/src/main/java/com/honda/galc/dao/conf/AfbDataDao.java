/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.AfbData;
import com.honda.galc.entity.conf.AfbDataId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Jan 3, 2012
 */
public interface AfbDataDao extends IDaoService<AfbData, AfbDataId> {
	
	public List<AfbData> findAllByModel(String model);
	public List<AfbData> findAllByModelType(String model, String type);
	public List<AfbData> findAllByType(String model);
	public List<AfbData> findAllByOption(String model);
}
