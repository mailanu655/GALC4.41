/**
 * 
 */
package com.honda.galc.service;

import com.honda.galc.dto.rest.RepairDefectDto;

/**
 * @author VCC44349
 *
 */
public interface LotControlRepairService extends IService {
	
	boolean repairBuildResult(RepairDefectDto dto);

}
