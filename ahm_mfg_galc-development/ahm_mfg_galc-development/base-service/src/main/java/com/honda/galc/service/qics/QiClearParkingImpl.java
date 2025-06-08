
package com.honda.galc.service.qics;


import com.honda.galc.service.QiClearParking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
@Service
public class QiClearParkingImpl implements QiClearParking {
	
	@Autowired
	private QiRepairAreaSpaceDao dto;

	public boolean removeVinFromQicsParking(String productId) {
		
		if (dto!=null) {
		dto.clearNAQParking(productId);
		} else {
			//dto is null
		}
		
		return false;
	}
}