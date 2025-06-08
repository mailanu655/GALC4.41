package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionModelId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>InspectionModelDao Class description</h3>
 * <p> InspectionModelDao description </p>
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
public interface InspectionModelDao extends IDaoService<InspectionModel, InspectionModelId> {

	List<InspectionModel> findAllByApplicationId(String applicationId);
	
	List<InspectionModel> findAllByApplicationIdAndModelCode(String applicationId,String modelCode);
	
	List<InspectionModel> findAllByApplicationIdAndModelCodes(String applicationId,List<String> modelCodes);
	
	List<String> findPartGroupNamesByApplicationIdAndModelCode( String applicationId, String modelCode );
	
}
