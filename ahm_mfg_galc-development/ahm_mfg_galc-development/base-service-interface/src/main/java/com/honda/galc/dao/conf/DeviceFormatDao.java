package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.DeviceFormatId;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.IDaoService;

import java.util.List;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface DeviceFormatDao extends IDaoService<DeviceFormat, DeviceFormatId> {

    public List<DeviceFormat> findAllByDeviceId(String deviceId);

    public void removeByClientId(String clientId);
    
    /**
     * Execute native query
     * @param sql
     * @return
     */
	public Object executeSqlQuery(String sql);

	/**
	 * Execute query for a specific tag value
	 * @param sql
	 */
	public void executeSqlUpdate(String sql);

    public List<DeviceFormat> findAllByTagPrefix(String clientId, String tagPrefix);
    
    public List<DeviceFormat> findAllByTagType(String clientId, DeviceTagType tagType);
    
    public List<DeviceFormat> findAllByTagType(String clientId, String tagPrefix, DeviceTagType tagType);
    
    public List<Object[]> findDistinctClientId();

}
