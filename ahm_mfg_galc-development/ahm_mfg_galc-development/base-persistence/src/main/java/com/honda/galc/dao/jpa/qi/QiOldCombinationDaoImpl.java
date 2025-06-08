package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiOldCombinationDao;
import com.honda.galc.dto.qi.DataMigrationDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.entity.qi.QiOldCombination;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiOldCombinationDaoImpl</code> is an implementation class for QiOldCombinationDao interface.
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

public class QiOldCombinationDaoImpl extends BaseDaoImpl<QiOldCombination, String> implements QiOldCombinationDao {
	
	private final static String FIND_NGLC_DEFECT_COMBINATION_BY_FILTER= "SELECT DISTINCT e.COMBINATION as PART_DEFECT_DESC FROM QI_OLD_COMBINATION_TBX e WHERE " 
			 +" COMBINATION not in (select OLD_COMBINATION from galadm.QI_MAPPING_COMBINATION_TBX) and "
			+ " e.COMBINATION LIKE ?1"
			+ " ORDER BY e.COMBINATION";
	
	private final static String FIND_NAQ_DEFCT_DESCRIBTION= "SELECT DISTINCT (REPLACE(REPLACE(REPLACE(c.INSPECTION_PART_NAME || ' ' || COALESCE "
			 + " (c.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
		     + " (c.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
			 + " (c.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE " 
		     + " (c.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE  "
		     + "(c.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "
		     + "(c.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  "
		     + "(d.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  "
		     + " (d.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_DESC ,  "+
			" d.REGIONAL_DEFECT_COMBINATION_ID AS REGIONAL_DEFECT_COMBINATION_ID "+
			" FROM galadm.QI_PART_LOCATION_COMBINATION_TBX c "+
		    "JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX d ON (c.PART_LOCATION_ID = d.PART_LOCATION_ID) AND (c.PRODUCT_KIND = d.PRODUCT_KIND) "+
			" WHERE c.ACTIVE=1 and d.ACTIVE=1 and (c.INSPECTION_PART_NAME LIKE ?1 OR c.INSPECTION_PART_LOCATION_NAME LIKE ?1 OR c.INSPECTION_PART_LOCATION2_NAME LIKE ?1 OR c.INSPECTION_PART2_NAME LIKE ?1 "+
			" OR c.INSPECTION_PART2_LOCATION_NAME LIKE ?1 OR c.INSPECTION_PART2_LOCATION2_NAME LIKE ?1 OR c.INSPECTION_PART3_NAME LIKE ?1 OR d.DEFECT_TYPE_NAME LIKE ?1 "+
			" OR d.DEFECT_TYPE_NAME2 LIKE ?1 OR (REPLACE(REPLACE(REPLACE(c.INSPECTION_PART_NAME || ' ' || COALESCE "
			 + " (c.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
		     + " (c.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
			 + " (c.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE " 
		     + " (c.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE  "
		     + "(c.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "
		     + "(c.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  "
		     + "(d.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  "
		     + " (d.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) LIKE ?1 )  ORDER BY d.REGIONAL_DEFECT_COMBINATION_ID ASC";
	
	/**
	 * This method is used to filter data based on Part Name
	 * @param partName
	 * @return List<QiOldCombination>
	 */
	public List<DataMigrationDto> findAllOldCombinationByFilter(String partName) {
		Parameters params = Parameters.with("1","%"+partName+"%");
		return findAllByNativeQuery(FIND_NGLC_DEFECT_COMBINATION_BY_FILTER,params,DataMigrationDto.class);
	}
	
	/**
	 * To Filter the table data on basis of main part no
	 * @param filterValue - User inputs in the filter
	 * @return the number of rows based on filtervalue
	 */
	public List<DataMigrationDto> findAllNaqDefectCombinationByFilter(String filterValue) {
		Parameters params = Parameters.with("1", "%" +filterValue+ "%");
		return findAllByNativeQuery(FIND_NAQ_DEFCT_DESCRIBTION, params,DataMigrationDto.class);
	}

}