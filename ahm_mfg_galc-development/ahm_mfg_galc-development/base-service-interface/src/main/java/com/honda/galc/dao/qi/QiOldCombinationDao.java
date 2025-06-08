package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.DataMigrationDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.entity.qi.QiOldCombination;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiOldCombinationDao Class description</h3>
 * <p>
 * QiOldCombinationDao is used to declare the methods to the store NGLC/OLD GALC Defect Combination
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *        MAY 14, 2017
 * 
 */
public interface QiOldCombinationDao extends IDaoService<QiOldCombination, String> {
	
	public List<DataMigrationDto> findAllOldCombinationByFilter(String partName);
	
	public List<DataMigrationDto> findAllNaqDefectCombinationByFilter(String filterValue);

}
