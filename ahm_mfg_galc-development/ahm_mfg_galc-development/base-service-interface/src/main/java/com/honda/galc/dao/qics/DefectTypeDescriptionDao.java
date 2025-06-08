package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.DefectTypeDescriptionId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>DefectTypeDescriptionDao Class description</h3>
 * <p> DefectTypeDescriptionDao description </p>
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
 * @author Jeffray Huang<br>
 * Apr 1, 2011
 *
 *
 */
/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public interface DefectTypeDescriptionDao extends IDaoService<DefectTypeDescription, DefectTypeDescriptionId> {

	List<DefectTypeDescription> findAllBy(String defectGroupName,String defectTypeName,String secondaryPartName);
	
	List<DefectTypeDescription> findAllByDefectGroupName(String defectGroupName);
	
	public List<Object[]> findAllTwoPartPairDefectGroups();
}
