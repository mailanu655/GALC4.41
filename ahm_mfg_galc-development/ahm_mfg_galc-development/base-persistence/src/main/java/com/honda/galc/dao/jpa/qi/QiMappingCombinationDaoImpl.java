package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiMappingCombinationDao;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiMappingCombinationId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiMappingCombinationDaoImpl</code> is an implementation class for QiMappingCombinationDao interface.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>05/14/2017</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public class QiMappingCombinationDaoImpl extends BaseDaoImpl<QiMappingCombination, QiMappingCombinationId> implements QiMappingCombinationDao {
	
	private final static String FIND_ASSOCIATED_DEFECT_COMBINATION_RESULTS_BY_FILTER= "select * from QI_MAPPING_COMBINATION_TBX mc "
			+ "where (mc.OLD_COMBINATION LIKE ?1 OR" +
			" (REPLACE(REPLACE(REPLACE(mc.INSPECTION_PART_NAME || ' ' || COALESCE "
					 + " (mc.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
				     + " (mc.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
					 + " (mc.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE " 
				     + " (mc.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE  "
				     + "(mc.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "
				     + "(mc.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  "
				     + "(mc.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  "
				     + " (mc.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) LIKE ?1 ) ";
	 
	/**
	 * This method is used to find the list ofAssociatedDefectCombinationResult
	 * @param filterString
	 * @return List<QiMappingCombination>
	 */
		
	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultById(Integer id) {
		Parameters params = Parameters.with("id.regionalDefectCombinationId", id);
		return findAll(params,new String[] {"inspectionPartName"});
	}

	/**
	 * This method is used to find the list ofAssociatedDefectCombinationResult
	 * @param filterString
	 * @return List<QiMappingCombination>
	 */
	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultByFilter(Integer id,String filter) {
		StringBuilder queryString=new StringBuilder();
		queryString.append(FIND_ASSOCIATED_DEFECT_COMBINATION_RESULTS_BY_FILTER);
		Parameters params = Parameters.with("1", "%"+filter+"%");
		if(id!=0){
			queryString.append(" AND  mc.REGIONAL_DEFECT_COMBINATION_ID="+id);	
		}
		queryString.append(" order by mc.INSPECTION_PART_NAME");
		if(StringUtils.isBlank(filter))
			return findAllByNativeQuery(queryString.toString(),params,QiMappingCombination.class,50);
		else
			return findAllByNativeQuery(queryString.toString(),params,QiMappingCombination.class);
	}

	/**
	 * This method is used to find the old part defect data based on new part defect combination
	 * @param qiDefectResult
	 * @return QiMappingCombination
	 */
	public QiMappingCombination findbyPartDefectComb(QiDefectResult qiDefectResult) {
		Parameters params = Parameters.with("inspectionPartName",qiDefectResult.getInspectionPartName() )
				.put("inspectionPartLocationName" , qiDefectResult.getInspectionPartLocationName())
				.put("inspectionPartLocation2Name" , qiDefectResult.getInspectionPartLocation2Name())
				.put("inspectionPart2Name" , qiDefectResult.getInspectionPart2Name())
				.put("inspectionPart2LocationName" , qiDefectResult.getInspectionPart2LocationName())
				.put("inspectionPart2Location2Name" , qiDefectResult.getInspectionPart2Location2Name())
				.put("inspectionPart3Name" , qiDefectResult.getInspectionPart3Name())
				.put("defectTypeName" , qiDefectResult.getDefectTypeName())
				.put("defectTypeName2" , qiDefectResult.getDefectTypeName2());
		return findFirst(params);
	}
}
