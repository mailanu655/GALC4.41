package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>DefectDescriptionDao Class description</h3>
 * <p> DefectDescriptionDao description </p>
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
 /**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
added queries for IQS and Regression Code Maintenance Screens
 */
public interface DefectDescriptionDao extends IDaoService<DefectDescription, DefectDescriptionId> {

	List<DefectDescription> findAllBy(String partGroupName,String inspectPartName,String inspectionPartLocationName);
	
	
	List<DefectDescription> findAllByDefectGroup(String partGroupName,String inspectPartName,String inspectionPartLocationName,String defectGroupName);
	
	List<DefectDescription> findAllByDefectType(String defectTypeName,String secondaryPartName);
	
	List<DefectDescription> findAllByDefectType(String defectTypeName);
	
	List<DefectDescription> findAllByDescriptionId(int descriptionId);
	
	List<String> findInspectionPartNamesByPartGroupName( String partGroupName );
	
	List<String> findInspectionPartLocationNamesByInspectionPartNameAndPartGroupName( String partGroupName, String inspectionPartName  );
	
	List<String> findPartDefectTypeNames( String partGroupName, String inspectionPartName, String inspectionPartLocationName  ) ;
	
	List<String> findSecondaryPartNames( String partGroupName, String inspectionPartName, String inspectionPartLocationName, String defectTypeName  ) ;
	
	List<DefectDescription> findAllByIqsCategoryItemName(String iqsCategoryName,String IqsItemName);
	
	List<DefectDescription> findAllByRegressionCode(String regressionCode);
	
	List<DefectDescription> findAllByDefectProperties(
			String responsibleDept, String responsibleZone,
			String iqsCategoryName, String iqsItemName, String regressionCode);
	
	List<DefectDescription> findAllByTwoPartPair(String partGroupName,String inspectPartName,String inspectionPartLocationName,String twoPartPairPart,String twoPartPairLocation);
	
	public List<DefectDescription> findAllDefectDescByInClause(String partGroupName,List<String> selectedParts,List<String> selectedLocations,List<String> selectedDefects,List<String> selectedSecParts,List<String> selectedTwoParts,List<String> selectedTwoLocations) ;

	
}
