/**
 * 
 */
package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.AfbDataDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.AfbData;
import com.honda.galc.entity.conf.AfbDataId;

/**
 * @author Subu Kathiresan
 * @date Jan 3, 2012
 */
public class AfbDataDaoImpl extends BaseDaoImpl<AfbData, AfbDataId> implements AfbDataDao {

	public List<AfbData> findAllByModel(String model) {
		// TODO add implementation
		return null;
	}

	public List<AfbData> findAllByModelType(String model, String type) {
		// TODO add implementation
		return null;
	}

	public List<AfbData> findAllByOption(String model) {
		// TODO add implementation
		return null;
	}

	public List<AfbData> findAllByType(String model) {
		// TODO add implementation
		return null;
	}
}
