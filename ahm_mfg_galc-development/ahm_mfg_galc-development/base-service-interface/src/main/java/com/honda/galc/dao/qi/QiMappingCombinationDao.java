package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiMappingCombinationId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiMappingCombinationDao Class description</h3>
 * <p>
 * QiMappingCombinationDao is used to declare the methods to store the result of Defect Combination association between
 * NA QICS Defect Combination and NGLC/OLD GALC Defect Combination
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
public interface QiMappingCombinationDao extends IDaoService<QiMappingCombination, QiMappingCombinationId> {

	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultById(Integer id);

	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultByFilter(Integer id,String filter);
	
	public QiMappingCombination findbyPartDefectComb(QiDefectResult qiDefectResult);
}
