package com.honda.galc.dao.jpa.qics;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectTypeDescriptionDao;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.DefectTypeDescriptionId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>DefectTypeDescriptionDaoImpl Class description</h3>
 * <p> DefectTypeDescriptionDaoImpl description </p>
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
 * Feb 28, 2011
 *
 *
 */
/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public class DefectTypeDescriptionDaoImpl extends BaseDaoImpl<DefectTypeDescription,DefectTypeDescriptionId> implements DefectTypeDescriptionDao{

	private static final String FIND_ALL_TWO_PART_PAIR_DEFECT_GROUPS = "select distinct defect_group_name from gal127tbx a, gal126tbx b where a.defect_type_name = b.defect_type_name and b.two_part_defect_flag = 1";
	
	public List<DefectTypeDescription> findAllBy(String defectGroupName,
			String defectTypeName, String secondaryPartName) {
		Parameters params = new Parameters(); 
		if(!StringUtils.isEmpty(defectGroupName)) params.put("id.defectGroupName",defectGroupName);
		if(!StringUtils.isEmpty(defectTypeName)) params.put("id.defectTypeName",defectTypeName);
		if(!StringUtils.isEmpty(secondaryPartName)) params.put("id.secondaryPartName",secondaryPartName);
		
		
		return findAll(params);
	}

	public List<DefectTypeDescription> findAllByDefectGroupName(
			String defectGroupName) {
		
		return findAll(Parameters.with("id.defectGroupName", defectGroupName),new String[]{"id.defectTypeName"},true);
	}
	
	
	public List<Object[]> findAllTwoPartPairDefectGroups()
	{
		return findAllByNativeQuery(FIND_ALL_TWO_PART_PAIR_DEFECT_GROUPS, null,Object[].class);
	}



}
