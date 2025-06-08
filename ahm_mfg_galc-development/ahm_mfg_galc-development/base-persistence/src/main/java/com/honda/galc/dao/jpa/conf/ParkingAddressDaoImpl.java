package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ParkingAddressDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ParkingAddress;
import com.honda.galc.entity.conf.ParkingAddressId;
import com.honda.galc.service.Parameters;

public class ParkingAddressDaoImpl extends BaseDaoImpl<ParkingAddress,ParkingAddressId> implements ParkingAddressDao{

	
	private final String UPDATE_VIN ="UPDATE GAL205TBX SET GAL205TBX.VIN=null where GAL205TBX.VIN=?1";

	@Transactional
	public int updateVinToNull(String vin) {
		Parameters params = Parameters.with("1", vin);		
		return executeNativeUpdate(UPDATE_VIN, params);
	}
	
	public List<ParkingAddress> getParkingAddress(String productId) {
		Parameters param=Parameters.with("vin",productId);
		return findAll(param);
	}


}
