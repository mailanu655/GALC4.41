package com.honda.galc.dao.jpa.qics;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.InspectionPartGroupDao;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>InspectionPartGroupDaoImpl Class description</h3>
 * <p> InspectionPartGroupDaoImpl description </p>
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
public class InspectionPartGroupDaoImpl extends BaseDaoImpl<PartGroup,String> implements InspectionPartGroupDao{

	private static final String FIND_PART_GROUPS_BY_MODEL_CODES = "select e from PartGroup e where e.modelCode in (:modelCode)";
	
	public List<PartGroup> findAllPartGroupsByModelCodes(List<String> modelCodes) {
		Parameters parameters = Parameters.with("modelCode", modelCodes);
		List<PartGroup> list = findAllByQuery(FIND_PART_GROUPS_BY_MODEL_CODES, parameters);
		return list;
	}



}
