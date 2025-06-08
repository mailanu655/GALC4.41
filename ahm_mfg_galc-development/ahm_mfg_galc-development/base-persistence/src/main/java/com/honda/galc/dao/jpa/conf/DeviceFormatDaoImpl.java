package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.DeviceFormatId;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.Parameters;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class DeviceFormatDaoImpl extends BaseDaoImpl<DeviceFormat,DeviceFormatId> implements DeviceFormatDao {
    
	private static final String FIND_TAGS_BY_PREFIX = "select df from DeviceFormat df where df.id.clientId = :clientId and df.id.tag like :tagPattern";

	private static final String FIND_BY_TAGTYPE = "select df from DeviceFormat df where df.id.clientId = :clientId and df.tagType = :tagType";

	private static final String FIND_BY_TAGTYPE_AND_PREFIX = "select df from DeviceFormat df where df.id.clientId = :clientId and df.tagType = :tagType and df.id.tag like :tagPattern";
	
	private static final String FIND_DISTINCT_CLIENT_ID = "select distinct(df.client_id) from GAL257TBX df";

	public List<DeviceFormat> findAllByDeviceId(String deviceId) {
    	
    	return findAll(Parameters.with("id.clientId", deviceId),new String[]{"sequenceNumber"},true);
    }

	@Transactional
    public void removeByClientId(String clientId) {
		
		delete(Parameters.with("id.clientId", clientId));
	}

	public Object executeSqlQuery(String sql) {
		Object obj = findFirstByNativeQuery(sql, null, Object.class);
		return obj == null ? null : obj.toString();
	}

	@Transactional //(propagation=Propagation.REQUIRES_NEW)
	public void executeSqlUpdate(String sql) {
		executeNativeUpdate(sql);
	}

	public List<DeviceFormat> findAllByTagPrefix(String clientId, String pattern) {
		Parameters params = Parameters.with("clientId", clientId);
		params.put("tagPattern", pattern + "%");
		return findAllByQuery(FIND_TAGS_BY_PREFIX, params);
	}
	
	public List<DeviceFormat> findAllByTagType(String clientId, DeviceTagType tagType) {
		Parameters params = Parameters.with("clientId", clientId);
		params.put("tagType", tagType.getId());
		return findAllByQuery(FIND_BY_TAGTYPE, params);
	}
	
	public List<DeviceFormat> findAllByTagType(String clientId, String pattern, DeviceTagType tagType) {
		Parameters params = Parameters.with("clientId", clientId);
		params.put("tagType", tagType.getId());
		params.put("tagPattern", pattern + "%");
		return findAllByQuery(FIND_BY_TAGTYPE_AND_PREFIX, params);
	}
	
	public  List<Object[]> findDistinctClientId() {
		return executeNative(FIND_DISTINCT_CLIENT_ID);
	}		
}
