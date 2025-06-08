package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>DefectGroupDao Class description</h3>
 * <p> DefectGroupDao description </p>
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
public interface DefectGroupDao extends IDaoService<DefectGroup, String> {

	public List<DefectGroup> findAllByImageName(String imageName);
	
	public List<DefectGroup> findAllDefectGroupsByModelCodes(List<String> modelCodes);
	
}
