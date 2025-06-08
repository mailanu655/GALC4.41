package com.honda.galc.dao.jpa.qics;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.InspectionModelDao;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionModelId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>InspectionModelDaoImpl Class description</h3>
 * <p> InspectionModelDaoImpl description </p>
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
public class InspectionModelDaoImpl extends BaseDaoImpl<InspectionModel,InspectionModelId> implements InspectionModelDao{

	public static final String FIND_PART_GROUP_NAMES_BY_APPLICATION_ID_AND_MODEL_CODE = "select distinct gal178TBX.part_group_name " + 
		"FROM galadm.gal322tbx, galadm.gal178TBX WHERE gal178TBX.application_id=?1 AND gal178TBX.part_group_name = gal322tbx.part_group_name " + 
		"and gal178tbx.model_code = ?2 ORDER BY gal178TBX.part_group_name";
	
	private static final String FIND_ISPECTION_MODELS_BY_APPLICATION_ID_AND_MODEL_CODES = "select e from InspectionModel e where e.id.applicationId = :applicationId and e.id.modelCode in (:modelCode)";
	
	public List<InspectionModel> findAllByApplicationId(String applicationId) {
		
		return findAll(Parameters.with("id.applicationId", applicationId));
		
	}

	public List<InspectionModel> findAllByApplicationIdAndModelCode(
			String applicationId, String modelCode) {
		
		return findAll(Parameters.with("id.applicationId", applicationId).put("id.modelCode", modelCode));
	}

	public List<String> findPartGroupNamesByApplicationIdAndModelCode( String applicationId, String modelCode ) {
		Parameters parameters = Parameters.with("1", applicationId);
		parameters.put("2", modelCode);
		return this.findAllByNativeQuery(FIND_PART_GROUP_NAMES_BY_APPLICATION_ID_AND_MODEL_CODE, 
					parameters, 
					String.class);
		
	}

	public List<InspectionModel> findAllByApplicationIdAndModelCodes(
			String applicationId, List<String> modelCodes) {
		Parameters parameters = Parameters.with("applicationId", applicationId);
        parameters.put("modelCode", modelCodes);
		List<InspectionModel> list = findAllByQuery(FIND_ISPECTION_MODELS_BY_APPLICATION_ID_AND_MODEL_CODES, parameters);
		return list;
	}

}
