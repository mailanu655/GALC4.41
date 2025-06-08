package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionPartDescriptionId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>InspectionPartDescriptionDao Class description</h3>
 * <p> InspectionPartDescriptionDao description </p>
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
public interface InspectionPartDescriptionDao extends IDaoService<InspectionPartDescription, InspectionPartDescriptionId> {

	List<InspectionPartDescription> findAllByDescriptionId(int descriptionId);
	
	List<InspectionPartDescription> findAllByPart(String partGroupName,String inspectionPartName,String inspectionPartLocation);
	
	List<InspectionPartDescription> findAllByPartGroupName(String partGroupName);
	
}
