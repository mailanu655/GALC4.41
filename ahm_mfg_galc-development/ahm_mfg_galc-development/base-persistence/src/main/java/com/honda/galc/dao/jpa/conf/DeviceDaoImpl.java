package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.service.Parameters;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class DeviceDaoImpl extends BaseDaoImpl<Device,String> implements DeviceDao {

    private static final long serialVersionUID = 1L;
    private final String FIND_PROCESS_POINT_ID = "SELECT T.IO_PROCESS_POINT_ID FROM galadm.gal253tbx T WHERE T.CLIENT_ID =?1 FOR READ ONLY";
    private final String FIND_ALL_INPUT_DEVICE= "select d from Device d where d.ioProcessPointId=:ioProcessPointId and d.clientId " +
    		"not in (select d1.replyClientId from Device d1 where d1.ioProcessPointId=:ioProcessPointId)";
    
    private final String FIND_ALL_BY_FILTER = "SELECT * FROM GALADM.GAL253TBX WHERE CLIENT_ID LIKE ?1";

	
    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<Device> findAllByProcessPointId(String ppId) {
	
    	return findAll(Parameters.with("ioProcessPointId", ppId));
    	
	}

    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<Device> findAllOrderByClientId() {
        return findAll(null, new String[] {"clientId"});
    }

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<Device> findAllByDivisionId(String divisionId) {
        
        return findAll(Parameters.with("divisionId", divisionId), new String[] {"clientId"});
        
    }
    
    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<Device> findAllByDeviceTypeId(int deviceTypeId) {
    	return findAll(Parameters.with("deviceTypeId" ,deviceTypeId));
    }
    
    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<Device> findAllByDeviceType(DeviceType type) {
    	return findAllByDeviceTypeId(type.getId());
    }


	public String findProcessPointId(String clientId) {
		Parameters params = Parameters.with("1", clientId);
		return findFirstByNativeQuery(FIND_PROCESS_POINT_ID, params, String.class);
	}


	public List<Device> findAllInputDeviceByProcessPointId(String ppId) {
		Parameters params = Parameters.with("ioProcessPointId", ppId);
		return findAllByQuery(FIND_ALL_INPUT_DEVICE, params);
	}


	@Override
	public List<Device> findAllByDeviceIdFilter(String filter) {
		Parameters params = Parameters.with("1", "%"+filter+"%");
		return findAllByNativeQuery(FIND_ALL_BY_FILTER, params);
	}

}
