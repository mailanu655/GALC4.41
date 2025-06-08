package com.honda.galc.dao.jpa.qics;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectGroupDao;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>DefectDescritionDaoImpl Class description</h3>
 * <p> DefectDescritionDaoImpl description </p>
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
public class DefectGroupDaoImpl extends BaseDaoImpl<DefectGroup,String> implements DefectGroupDao{

	private static final String FIND_DEFECT_GROUPS_BY_MODEL_CODES = "select e from DefectGroup e where e.modelCode in (:modelCode)";
	
	public List<DefectGroup> findAllByImageName(String imageName) {
		
		return findAll(Parameters.with("imageName", imageName));
		
	}

	public List<DefectGroup> findAllDefectGroupsByModelCodes(List<String> modelCodes) {
		Parameters parameters = Parameters.with("modelCode", modelCodes);
		List<DefectGroup> list = findAllByQuery(FIND_DEFECT_GROUPS_BY_MODEL_CODES, parameters);
		return list;
	}
	
}
