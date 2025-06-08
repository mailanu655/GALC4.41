/**
 * 
 */
package com.honda.galc.service;

import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
/**
 * @author VCC74577
 *
 */
public interface QiClearParking extends IService {
	boolean removeVinFromQicsParking(String productId);

	
}
