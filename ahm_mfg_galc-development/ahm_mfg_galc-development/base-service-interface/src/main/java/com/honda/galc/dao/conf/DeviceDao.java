package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.service.IDaoService;

import java.util.List;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface DeviceDao extends IDaoService<Device, String> {


    public List<Device> findAllByProcessPointId(String ppId);

    /**
     * Finds all devices ordered by CLIENT_ID.
     */
    public List<Device> findAllOrderByClientId();

    public List<Device> findAllByDivisionId(String divisionId);
    
    public List<Device> findAllByDeviceTypeId(int deviceTypeId);
    
    public List<Device> findAllByDeviceType(DeviceType type);

	public String findProcessPointId(String clientId);

	/**
	 * Find all input devices for a given process point
	 * @param ppId
	 * @return
	 */
	public List<Device> findAllInputDeviceByProcessPointId(String ppId);
	
	public List<Device> findAllByDeviceIdFilter(String filter);
}
