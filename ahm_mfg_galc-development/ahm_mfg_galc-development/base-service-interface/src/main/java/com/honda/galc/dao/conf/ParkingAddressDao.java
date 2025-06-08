package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.ParkingAddress;
import com.honda.galc.entity.conf.ParkingAddressId;
import com.honda.galc.service.IDaoService;

public interface ParkingAddressDao extends IDaoService<ParkingAddress, ParkingAddressId>{
	
	public int updateVinToNull(String vin);
	public List<ParkingAddress> getParkingAddress(String productId);

}
