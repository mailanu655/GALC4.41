/**
 * 
 */
package com.honda.galc.service;

import java.util.List;

import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiDefectResult;

/**
 * @author VCC44349
 *
 */
public interface QiHeadlessDefectService extends IService {
	
	boolean deleteDefect(String extSysName, Long extSysKey);

	boolean repairDefect(QiRepairDefectDto dto);

	Boolean[] repairDefects(List<QiRepairDefectDto> dto);
	
	boolean createDefect(QiCreateDefectDto dto);

	Boolean[] repairDefects(List<QiRepairDefectDto> dtoList, boolean updateDefectStatus);

	boolean repairDefect(QiRepairDefectDto dto, boolean isUpdateDefectStatus);

	DefectStatus getDefectStatus(String productId);

	Boolean[] createDefects(List<QiCreateDefectDto> dtoList);

	List<QiDefectResult> getDefectsForProduct(String productId);

	boolean repairActualProblem(QiRepairDefectDto dto);

}
