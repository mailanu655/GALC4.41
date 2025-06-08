package com.honda.galc.dao.jpa.qics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
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
 /**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
added queries for Iqs and Regression Code Maintenance Screens
 */
public class DefectDescriptionDaoImpl extends BaseDaoImpl<DefectDescription,DefectDescriptionId> implements DefectDescriptionDao{
	
	private final static String FIND_ALL_BY_PART = "select e from DefectDescription e,DefectTypeDescription t where e.id.defectTypeName = t.id.defectTypeName and" +
			" e.id.secondaryPartName = t.id.secondaryPartName and e.id.partGroupName = :partGroupName and e.id.inspectionPartName = :inspectionPartName" +
			" and e.id.inspectionPartLocationName = :inspectionPartLocationName and t.id.defectGroupName = :defectGroupName";
	
	private final static String FIND_ALL_BY_IMAGE0 = "select e from DefectDescription e,InspectionPartDescription p where " +
		    " e.twoPartDefectFlag = 0 and p.descriptionId = :descriptionId and e.id.inspectionPartName = p.id.inspectionPartName and " +
		    " e.id.inspectionPartLocationName = p.id.inspectionPartLocationName and e.id.partGroupName = p.id.partGroupName"; 

	private final static String FIND_ALL_BY_IMAGE1 = "select e from DefectDescription e,InspectionTwoPartDescription p where " +
    " e.twoPartDefectFlag = 0 and p.descriptionId = :descriptionId and e.id.inspectionPartName = p.id.inspectionPartName and " +
    " e.id.inspectionPartLocationName = p.id.inspectionPartLocationName and e.id.partGroupName = p.id.partGroupName"; 

	
	private static final String FIND_INSPECTION_PART_NAMES_BY_PART_GROUP_NAME = "select distinct inspection_part_name from gal322TBX where (part_group_name=?1) order by  inspection_part_name";

	private static final String FIND_INSPECTION_PART_LOCATION_NAMES_BY_PART_GROUP_NAME_AND_INSPECTION_PART_NAME = "select distinct inspection_part_location_name " + 
		"from gal322TBX where (part_group_name=?1) and ( inspection_part_name = ?2) order by  inspection_part_location_name";
	
	private static final String FIND_SECONDARY_PART_NAMES = "select distinct secondary_part_name from gal322tbx where (two_part_defect_flag=0) and (part_group_name=?1) " + 
		"and (inspection_part_name=?2) and (inspection_part_location_name=?3) and (defect_type_name=?4) order by  secondary_part_name";

	private static final String FIND_PART_DEFECT_TYPE_NAMES = "select distinct defect_type_name from gal322tbx where (two_part_defect_flag=0) " + 
		"and (part_group_name=?1) and (inspection_part_name=?2) and (inspection_part_location_name=?3) order by defect_type_name";

	
	private static final String FIND_ALL_DEFECT_DESCS_IN_CLAUSE ="select e from DefectDescription e where e.id.partGroupName = :partGroupName and e.id.inspectionPartName in (:selectedParts) " +
	"and e.id.inspectionPartLocationName in (:selectedLocations) and e.id.defectTypeName in (:selectedDefects) and e.id.secondaryPartName in (:selectedSecParts) and e.id.twoPartPairPart in (:selectedTwoParts) " +
	"and e.id.twoPartPairLocation in (:selectedTwoLocations)";

	
	
	public List<DefectDescription> findAllByDefectGroup(String partGroupName,
			String inspectPartName, String inspectionPartLocationName,
			String defectGroupName) {
		Parameters parameters = Parameters.with("partGroupName", partGroupName);
		parameters.put("inspectionPartName", inspectPartName)
		          .put("inspectionPartLocationName", inspectionPartLocationName)
		          .put("defectGroupName",defectGroupName);
		return findAllByQuery(FIND_ALL_BY_PART, parameters);
	}

	public List<DefectDescription> findAllBy(String partGroupName,
			String inspectPartName, String inspectionPartLocationName) {
		
		Parameters parameters = Parameters.with("id.partGroupName", partGroupName);
		parameters.put("id.inspectionPartName", inspectPartName)
		          .put("id.inspectionPartLocationName", inspectionPartLocationName);
		return findAll(parameters);
		
	}
    

	public List<DefectDescription> findAllByDefectType(String defectTypeName,
			String secondaryPartName) {
	
		Parameters parameters = Parameters.with("id.defectTypeName", defectTypeName);
		parameters.put("id.secondaryPartName", secondaryPartName);
		return findAll(parameters);
		
	}

	private List<DefectDescription> findAllByDescriptionId0(int descriptionId) {
		
		Parameters params = Parameters.with("descriptionId", descriptionId);
		return findAllByQuery(FIND_ALL_BY_IMAGE0, params);
		
	}
	
	private List<DefectDescription> findAllByDescriptionId1(int descriptionId) {
		
		Parameters params = Parameters.with("descriptionId", descriptionId);
		return findAllByQuery(FIND_ALL_BY_IMAGE1, params);
		
	}

	public List<DefectDescription> findAllByDescriptionId(int descriptionId) {
		
		List<DefectDescription> defectDescriptions = new ArrayList<DefectDescription>();
		
		defectDescriptions.addAll(findAllByDescriptionId0(descriptionId));
		
		defectDescriptions.addAll(findAllByDescriptionId1(descriptionId));
		
		return defectDescriptions;
		
	}

	public List<DefectDescription> findAllByDefectType(String defectTypeName) {
		
		Parameters parameters = Parameters.with("id.defectTypeName", defectTypeName);
		return findAll(parameters);
		
	}
	
	public List<String> findInspectionPartNamesByPartGroupName( String partGroupName ) {
		return this.findAllByNativeQuery(FIND_INSPECTION_PART_NAMES_BY_PART_GROUP_NAME, 
				Parameters.with("1", partGroupName), 
				String.class);
	}
	
	public List<String> findInspectionPartLocationNamesByInspectionPartNameAndPartGroupName( String partGroupName, String inspectionPartName  ) {
		Parameters parameters = Parameters.with("1", partGroupName);
		parameters.put("2", inspectionPartName);
		return this.findAllByNativeQuery(FIND_INSPECTION_PART_LOCATION_NAMES_BY_PART_GROUP_NAME_AND_INSPECTION_PART_NAME, 
				parameters, 
				String.class);
	}
	
	public List<String> findPartDefectTypeNames( String partGroupName, String inspectionPartName, String inspectionPartLocationName  ) {
		Parameters parameters = Parameters.with("1", partGroupName);
		parameters.put("2", inspectionPartName);
		parameters.put("3", inspectionPartLocationName);
		return this.findAllByNativeQuery(FIND_PART_DEFECT_TYPE_NAMES, 
				parameters, 
				String.class);
	}
	
	public List<String> findSecondaryPartNames( String partGroupName, String inspectionPartName, String inspectionPartLocationName, String defectTypeName  ) {
		Parameters parameters = Parameters.with("1", partGroupName);
		parameters.put("2", inspectionPartName);
		parameters.put("3", inspectionPartLocationName);
		parameters.put("4", defectTypeName);
		return this.findAllByNativeQuery(FIND_SECONDARY_PART_NAMES, 
				parameters, 
				String.class);
	}
	
	public List<DefectDescription> findAllByIqsCategoryItemName(String iqsCategoryName,String iqsItemName)
	{
		
		Parameters parameters = Parameters.with("iqsCategoryName", iqsCategoryName).put("iqsItemName", iqsItemName);
		return findAll(parameters);
	}
	
	public List<DefectDescription> findAllByRegressionCode(String regressionCode)
	{
		
		Parameters parameters = Parameters.with("regressionCode", regressionCode);
		return findAll(parameters);
	}
	
	public List<DefectDescription> findAllByDefectProperties(
			String responsibleDept, String responsibleZone,
			String iqsCategoryName, String iqsItemName, String regressionCode) {
		Parameters params = new Parameters();
		if (!StringUtils.isEmpty(responsibleDept)) params.put("responsibleDept", responsibleDept);
		if (!StringUtils.isEmpty(responsibleZone)) params.put("responsibleZone", responsibleZone);
		if (!StringUtils.isEmpty(iqsCategoryName)) params.put("iqsCategoryName", iqsCategoryName);
		if (!StringUtils.isEmpty(iqsItemName)) params.put("iqsItemName", iqsItemName);
		if (!StringUtils.isEmpty(regressionCode)) params.put("regressionCode", regressionCode);

		return findAll(params);
	}
	
	public List<DefectDescription> findAllByTwoPartPair(String partGroupName,String inspectPartName,String inspectionPartLocationName,String twoPartPairPart,String twoPartPairLocation)
	{
		Parameters parameters = Parameters.with("id.partGroupName", partGroupName);
		parameters.put("id.inspectionPartName", inspectPartName)
		          .put("id.inspectionPartLocationName", inspectionPartLocationName)
		          .put("id.twoPartPairPart", twoPartPairPart)
		          .put("id.twoPartPairLocation", twoPartPairLocation);
		return findAll(parameters);
	}
	
	public List<DefectDescription> findAllDefectDescByInClause(String partGroupName,List<String> selectedParts,List<String> selectedLocations,List<String> selectedDefects,List<String> selectedSecParts,List<String> selectedTwoParts,List<String> selectedTwoLocations) {
		Parameters params = Parameters.with("partGroupName", partGroupName).put("selectedParts", selectedParts)
				.put("selectedLocations", selectedLocations)
				.put("selectedDefects", selectedDefects)
				.put("selectedSecParts", selectedSecParts)
				.put("selectedTwoParts", selectedTwoParts)
				.put("selectedTwoLocations", selectedTwoLocations);
		return findAllByQuery(FIND_ALL_DEFECT_DESCS_IN_CLAUSE, params);
	}
	
	
}
