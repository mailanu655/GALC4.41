package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QIPartLocCombMaintDAOImpl</code> is an implementation class for QIPartLocCombMaintDAO interface.
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
 * <TD>15/06/2016</TD>
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

public class QiPartLocationCombinationDaoImpl extends BaseDaoImpl<QiPartLocationCombination, Integer> implements QiPartLocationCombinationDao {
	
	private static final String FIND_BY_FILTER_DATA = 	"select e from QiPartLocationCombination e where " +
			"(e.inspectionPartName like :searchString or " +
			"e.inspectionPartLocationName like :searchString or " +
			"e.inspectionPartLocation2Name like :searchString or " +
			"e.inspectionPart2Name like :searchString or " +
			"e.inspectionPart2LocationName like :searchString or " +
			"e.inspectionPart2Location2Name like :searchString or " +
			"e.inspectionPart3Name like :searchString or " +
			"e.active= :isActive) and e.active in (:statusList) and " +
			"(e.productKind= :productKind) order by e.inspectionPartName";
	
	private static final String FIND_FULL_PART_NAME = "select * from galadm.qi_part_location_combination_tbx where REPLACE(REPLACE(REPLACE(inspection_part_name || ' ' || COALESCE" +
		       "(inspection_part_location_name, '') || ' ' || COALESCE" +
		       "(inspection_part_location2_name, '') || ' ' || COALESCE" +
		       "(inspection_part2_name,'')|| ' ' || COALESCE" +
		       "(inspection_part2_location_name, '') || ' ' || COALESCE" +
		       "(inspection_part2_location2_name, '') || ' ' || COALESCE" +
		       "(inspection_part3_name, ''),' ','{}'),'}{',''),'{}',' ') like ?1 and active in (?2,?3) and product_kind=?4 order by inspection_part_name";
	
	private static final String UPDATE_PART_LOC_COMB_STATUS ="update GALADM.QI_PART_LOCATION_COMBINATION_TBX  set ACTIVE = ?1 , UPDATE_USER = ?2 " +
			"where PART_LOCATION_ID = ?3";
	
	private static final String CHECK_PART_IN_PART_COMBINATION = 	"select e from QiPartLocationCombination e where " +
			"(e.inspectionPartName = :partName or  " +
			"e.inspectionPart2Name = :partName or  " +
			"e.inspectionPart3Name = :partName ) and e.productKind= :productKind";
	
	private static final String CHECK_LOCATION_IN_PART_COMBINATION = 	"select e from QiPartLocationCombination e where " +
			"(e.inspectionPartLocationName = :locationName or  " +
			"e.inspectionPartLocation2Name = :locationName or  " +
			"e.inspectionPart2LocationName = :locationName or  " +
			"e.inspectionPart2Location2Name = :locationName ) and e.productKind= :productKind";
	
	private static final String FIND_FULL_PART_NAME_BY_FILTER = "select * from (select part_location_id, inspection_part_name, "
			+ "REPLACE(REPLACE(REPLACE(inspection_part_name || ' ' || COALESCE"
			+ "(inspection_part_location_name, '') || ' ' || COALESCE"
			+ "(inspection_part_location2_name, '') || ' ' || COALESCE" 
			+ "(inspection_part2_name,'')|| ' ' || COALESCE"
			+ "(inspection_part2_location_name, '') || ' ' || COALESCE"
			+ "(inspection_part2_location2_name, '') || ' ' || COALESCE"
			+ "(inspection_part3_name, ''),' ','{}'),'}{',''),'{}',' ') as full_part_desc "
			+ "from galadm.qi_part_location_combination_tbx where "
			+ "active = ?2 and product_kind = ?3) "
			+ "where full_part_desc like ?1 order by full_part_desc";
	
	private static final String FIND_FULL_PART_NAME_BY_EXPANDED_FILTER =
			"select distinct PART_LOCATION_ID, INSPECTION_PART_NAME, full_part_desc from (select A.part_location_id, A.inspection_part_name, "
			+ "REPLACE(REPLACE(REPLACE(A.inspection_part_name || ' ' || "
			+ "COALESCE (A.inspection_part_location_name, '') || ' ' || "
			+ "COALESCE (A.inspection_part_location2_name, '') || ' ' || " 
			+ "COALESCE (A.inspection_part2_name,'') || ' ' || "
			+ "COALESCE (A.inspection_part2_location_name, '') || ' ' ||"
			+ "COALESCE (A.inspection_part2_location2_name, '') || ' ' ||"
			+ "COALESCE (A.inspection_part3_name, '')"
			+ ",' ','{}'),'}{',''),'{}',' ') as full_part_desc,"
			+ " COALESCE (B.DEFECT_TYPE_NAME, '') || ' ' || "
			+ " COALESCE (B.DEFECT_TYPE_NAME2, '') as defect_type, "
			+ " CASE WHEN (B.REPORTABLE is null or B.REPORTABLE = 0) THEN 'FALSE' ELSE 'TRUE' END as reportable_flag, "
			+ " REPLACE(REPLACE(REPLACE( "
			+ " COALESCE (C.IQS_VERSION, '') || ' ' || "
			+ " COALESCE (C.IQS_CATEGORY, '') || ' ' || "
			+ " COALESCE (C.IQS_QUESTION,'') "
			+ " ,' ','{}'),'}{',''),'{}',' ') as iqs_parts, "
			+ " D.THEME_NAME as theme "
			+ " from qi_part_location_combination_tbx A "
			+ " join QI_REGIONAL_DEFECT_COMBINATION_TBX B on A.PART_LOCATION_ID=B.PART_LOCATION_ID "
			+ " left join QI_IQS_TBX C on B.IQS_ID = C.IQS_ID "
			+ " left join QI_THEME_TBX D on B.THEME_NAME=D.THEME_NAME "
			+ " where "
			+ " A.active = ?1 and A.product_kind = ?2 <and_reportable> )"
			+ " <additional_where_clause> order by full_part_desc ";
	
	private static final String FIND_ACTIVE_PART_LOC_COMB = 	"select e from QiPartLocationCombination e where " +
			"(e.inspectionPartName like :searchString or " +
			"e.inspectionPartLocationName like :searchString or " +
			"e.inspectionPartLocation2Name like :searchString or " +
			"e.inspectionPart2Name like :searchString or " +
			"e.inspectionPart2LocationName like :searchString or " +
			"e.inspectionPart2Location2Name like :searchString or " +
			"e.inspectionPart3Name like :searchString) and " +
			"e.active= 1 and " +
			"e.productKind= :productKind order by e.inspectionPartName";

	private static final String FIND_UNASSIGNED_FULL_PART_NAME_BY_FILTER = "select plc.part_location_id, inspection_part_name, "
			+ "REPLACE(REPLACE(REPLACE(inspection_part_name || ' ' || COALESCE"
			+ "(inspection_part_location_name, '') || ' ' || COALESCE"
			+ "(inspection_part_location2_name, '') || ' ' || COALESCE" 
			+ "(inspection_part2_name,'')|| ' ' || COALESCE"
			+ "(inspection_part2_location_name, '') || ' ' || COALESCE"
			+ "(inspection_part2_location2_name, '') || ' ' || COALESCE"
			+ "(inspection_part3_name, ''),' ','{}'),'}{',''),'{}',' ') as full_part_desc "
			+ " from galadm.qi_part_location_combination_tbx plc "
			+ " JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX pdc ON plc.PART_LOCATION_ID = pdc.PART_LOCATION_ID "
			+ " where plc.active = ?1 and plc.product_kind = ?2 "
			+ " AND (pdc.IQS_ID IS NULL OR pdc.IQS_ID = 0 OR pdc.THEME_NAME IS NULL)";
		
	private static final String FIND_ALL_PART_LOC_COMB_MATCH = 	
			"select e from QiPartLocationCombination e where " +
			"trim(BOTH '_' FROM replace(replace("
			+ "replace(trim(e.inspectionPartName) || '_' || trim(nvl(C.inspectionPartLocationName,'')) || '_' || trim(nvl(C.inspectionPartLocation2Name,'')) || '_' " + 
			" || trim(nvl(C.inspectionPart2Name,'')) || '_' || trim(nvl(C.inspectionPart2LocationName,'')) || '_' || trim(nvl(C.inspectionPart2Location2Name,'_')) || '_' " +
			" || trim(nvl(C.inspectionPart3Name,'')) " +
			",'__','_'),'__','_'),'__','_')) = :partLoc " +
			"e.active= 1 and " +
			"e.productKind= :productKind order by e.inspectionPartName";

	private static final String CHECK_PART_LOC_COMB = "select * from galadm.qi_part_location_combination_tbx where " +
			"(COALESCE(inspection_part_name,'')=?1 and " +
			"COALESCE(inspection_part_location_name,'')=?2 and " +
			"COALESCE(inspection_part_location2_name,'')=?3 and " +
			"COALESCE(inspection_part2_name,'')=?4 and " +
			"COALESCE(inspection_part2_location_name,'')=?5 and " +
			"COALESCE(inspection_part2_location2_name,'')=?6 and " +
			"COALESCE(inspection_part3_name,'')=?7) and " +
			"product_kind=?8 and " +
			"part_location_id!=?9";
	
	private static final String FIND_ALL_PART1_BY_PRODUCT_KIND = "SELECT DISTINCT INSPECTION_PART_NAME FROM GALADM.QI_PART_LOCATION_COMBINATION_TBX " +
			"WHERE ACTIVE=1 AND PRODUCT_KIND=?1 " +
			"ORDER BY INSPECTION_PART_NAME";
	
	private static final String FIND_ALL_BY_IMAGE_SECTION_ID = "SELECT * FROM GALADM.QI_PART_LOCATION_COMBINATION_TBX a " +
			"JOIN GALADM.QI_IMAGE_SECTION_TBX b on a.PART_LOCATION_ID=b.PART_LOCATION_ID " +
			"WHERE b.IMAGE_SECTION_ID=?1";
	
	private static final String FIND_ALL_PART1_BY_PROCESSPOINT = "SELECT DISTINCT  c.INSPECTION_PART_NAME "+
			"FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX a "+
			"JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX b on a.REGIONAL_DEFECT_COMBINATION_ID=b.REGIONAL_DEFECT_COMBINATION_ID "+
			"JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX c on c.PART_LOCATION_ID=b.PART_LOCATION_ID "+
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX d on d.ENTRY_SCREEN=a.ENTRY_SCREEN "+
			"JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX e on e.ENTRY_MODEL=d.ENTRY_MODEL "+
			"JOIN GALADM.QI_STATION_ENTRY_SCREEN_TBX f on a.ENTRY_SCREEN=f.ENTRY_SCREEN "+
			"JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX g on a.ENTRY_SCREEN=g.ENTRY_SCREEN "+
			"LEFT OUTER JOIN GALADM.QI_PDDA_RESPONSIBILITY_TBX h ON a.PDDA_RESPONSIBILITY_ID=h.PDDA_RESPONSIBILITY_ID "+
			"JOIN GALADM.QI_IQS_TBX i ON i.IQS_ID=b.IQS_ID "+
			"JOIN GALADM.QI_ENTRY_MODEL_TBX j ON e.ENTRY_MODEL=j.ENTRY_MODEL AND j.ACTIVE=1 " +
			"WHERE e.MTC_MODEL=?1 AND f.PROCESS_POINT_ID=?2 "+
			"AND a.REGIONAL_DEFECT_COMBINATION_ID=g.REGIONAL_DEFECT_COMBINATION_ID "+
			"AND c.PRODUCT_KIND=?3 AND b.ACTIVE=1 "+
			"AND (b.IQS_ID IS NOT NULL AND  b.IQS_ID <> 0) AND COALESCE(b.THEME_NAME,'') != '' " +
			"AND f.DIVISION_ID =?4 "+
			"ORDER BY c.INSPECTION_PART_NAME";
	
	private static final String FIND_ALL_PART1_PART2 = "SELECT DISTINCT c.INSPECTION_PART_NAME FROM GALADM.QI_STATION_ENTRY_SCREEN_TBX QSES " + 
			"JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX EMG ON QSES.DIVISION_ID=?1 AND QSES.PROCESS_POINT_ID=?2 AND EMG.ENTRY_MODEL=QSES.ENTRY_MODEL " +
			"AND EMG.MTC_MODEL=?3 AND EMG.IS_USED=1 JOIN GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX LDC ON LDC.ENTRY_MODEL=QSES.ENTRY_MODEL AND LDC.ENTRY_SCREEN=QSES.ENTRY_SCREEN " + 
			"AND LDC.IS_USED=1 JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC ON LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID " + 
			"JOIN (SELECT INSPECTION_PART_NAME, PART_LOCATION_ID FROM GALADM.QI_PART_LOCATION_COMBINATION_TBX WHERE PRODUCT_KIND=?4 " +
			"UNION SELECT INSPECTION_PART2_NAME, PART_LOCATION_ID FROM GALADM.QI_PART_LOCATION_COMBINATION_TBX WHERE INSPECTION_PART2_NAME IS NOT NULL " +
			"AND INSPECTION_PART2_NAME !=''  AND PRODUCT_KIND='AUTOMOBILE') c ON c.PART_LOCATION_ID=RDC.PART_LOCATION_ID ORDER BY c.INSPECTION_PART_NAME";
	
	private static final String FIND_BY_ALL_PART_LOC = "SELECT DISTINCT " + 
			"trim(BOTH '_' FROM replace(replace("
			+ "replace(trim(C.INSPECTION_PART_NAME) || '_' || trim(nvl(C.INSPECTION_PART_LOCATION_NAME,'')) || '_' || trim(nvl(C.INSPECTION_PART_LOCATION2_NAME,'')) || '_' " + 
			" || trim(nvl(C.INSPECTION_PART2_NAME,'')) || '_' || trim(nvl(C.INSPECTION_PART2_LOCATION_NAME,'')) || '_' || trim(nvl(C.INSPECTION_PART2_LOCATION2_NAME,'_')) || '_' " +
			" || trim(nvl(C.INSPECTION_PART3_NAME,'')) " +
			",'__','_'),'__','_'),'__','_')) " +
			" || trim(T '@' FROM '@' || trim(BOTH '_' FROM trim(nvl(RDC.DEFECT_TYPE_NAME,'')) || '_' || trim(nvl(RDC.DEFECT_TYPE_NAME2,'')) ))"
			+  " as allPartLoc " +
			
			" FROM GALADM.QI_STATION_ENTRY_SCREEN_TBX QSES " + 
			" JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX EMG ON QSES.DIVISION_ID=?1 AND QSES.PROCESS_POINT_ID=?2 AND EMG.ENTRY_MODEL=QSES.ENTRY_MODEL " +
			" AND EMG.MTC_MODEL=?3 AND EMG.IS_USED=1 JOIN GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX LDC ON LDC.ENTRY_MODEL=QSES.ENTRY_MODEL AND LDC.ENTRY_SCREEN=QSES.ENTRY_SCREEN " + 
			" AND LDC.IS_USED=1 JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC ON LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID " + 
			" JOIN "
			+ " (SELECT INSPECTION_PART_NAME, L1.PART_LOCATION_ID,INSPECTION_PART_LOCATION_NAME,INSPECTION_PART_LOCATION2_NAME,"
			+ " INSPECTION_PART2_NAME,INSPECTION_PART2_LOCATION_NAME,"
			+ " INSPECTION_PART2_LOCATION2_NAME,INSPECTION_PART3_NAME "
			+ " FROM GALADM.QI_PART_LOCATION_COMBINATION_TBX L1 "
			+ " WHERE L1.PRODUCT_KIND=?4) C "			
			+ " ON c.PART_LOCATION_ID=RDC.PART_LOCATION_ID ORDER BY allPartLoc";
		
	/**
	* This method is used to load all Part Location Combinations at filter level.
	* If the user types something in filter, data gets returned based on the filter value.
	* @param partLocCombFilter: Filter data for Part Location Combination.
	* @param productKind: Product Kind of Part Location Combination.
	* @return List of Part Location Combination.
	*/
	public List<QiPartLocationCombination> findFilteredPartLocComb(String partLocCombFilter, String productKind, List<Short> statusList) {

		List<QiPartLocationCombination> partLocCombList = findByFilterData(partLocCombFilter, productKind, statusList);
		
		/** 
		 *This method is unable to perform search on full part name as there is no such column in database.
		 *So to perform search on full part name , this method is called.
		 */
		if(partLocCombList.isEmpty())
		{
			partLocCombList = findFullPartName(partLocCombFilter, productKind, statusList);
		}
		return partLocCombList;
	}
	
	/**
	 * This method is called by the findAllFilteredPartLocComb() method to execute filter query.
	 * @param partLocCombFilter: Filter data for Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 * @return List of Part Location Combination.
	 */
	public List<QiPartLocationCombination> findByFilterData(String filterData, String productKind, List<Short> statusList) {
		Parameters params = Parameters.with("searchString", "%"+filterData+"%")
				.put("isActive", (filterData.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName())) ? (short) 1 : (filterData.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName())) ? (short) 0 : (short) 2)
				.put("statusList", statusList)
				.put("productKind", productKind);
		return findAllByQuery(FIND_BY_FILTER_DATA, params);
	}
	
	/**
	 * This method is used to perform filter on full part name.
	 * @param partLocCombFilter: Filter data for Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 * @return List of Part Location Combination.
	 */
	public List<QiPartLocationCombination> findFullPartName(String filterData, String productKind, List<Short> statusList) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", statusList.get(0))
				.put("3", statusList.get(1))
				.put("4", productKind);
		return findAllByNativeQuery(FIND_FULL_PART_NAME, params, QiPartLocationCombination.class);
	}
	
	/**
	 * This method is used to update status of Part Location Combination.
	 * @param partLocationId: ID for Part Location Combination.
	 * @param active: Status value of Part Location Combination.
	 * @param updateUser: Update user name of Part Location Combination.
	 * @return List of Part Location Combination.
	 */
	@Transactional
	public void updatePartLocCombStatus(Integer partLocationId, short active, String updateUser) {
		Parameters params = Parameters.with("1", active)
				.put("2", updateUser)
				.put("3", partLocationId);
		executeNativeUpdate(UPDATE_PART_LOC_COMB_STATUS, params);
	}
	
	/**
	 * This method is used to check presence of Inspection Part in Part Location Combination.
	 * @param partName: Part Name to be checked in Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 */
	public List<QiPartLocationCombination> checkPartInPartLocCombination(String partName, String productKind){
		Parameters params = Parameters.with("partName", partName)
				.put("productKind", productKind);
		return findAllByQuery(CHECK_PART_IN_PART_COMBINATION, params);
	}
	
	/**
	 * This method is used to check presence of Inspection Location in Part Location Combination.
	 * @param locationName: Location Name to be checked in Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 */
	public List<QiPartLocationCombination> checkLocationInPartLocCombination(String locationName, String productKind){
		Parameters params = Parameters.with("locationName", locationName)
				.put("productKind", productKind);
		return findAllByQuery(CHECK_LOCATION_IN_PART_COMBINATION, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<QiPartLocationCombinationDto> findFullPartNameByFilter(String filterData, short active, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%").put("2", active).put("3", productKind);
		List<Object[]> objectList = findResultListByNativeQuery(FIND_FULL_PART_NAME_BY_FILTER, params);
		List<QiPartLocationCombinationDto> dtoList = new ArrayList<QiPartLocationCombinationDto>();
		setDtoList(dtoList, objectList);
		return dtoList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<QiPartLocationCombinationDto> findFullPartNameByWhichFilter(String filterData, short active, int which, String productKind) {
		Parameters params = Parameters.with("1", active).put("2", productKind);
		final String partNameFilter = " where full_part_desc like ?3 ";
		final String defectTypeFilter = " where  defect_type like ?3 ";
		final String iqsFilter = " where iqs_parts like ?3 ";
		final String themeFilter = " where  theme like ?3 ";
		final String reportableFilter = " and B.REPORTABLE = 1 ";
		final String notReportableFilter = " and (B.REPORTABLE = 0 or B.REPORTABLE IS NULL) ";
		
		String myFilter = "%"+filterData+"%";
		String addlWhere = "", reportableClause = "";
		String sql = FIND_FULL_PART_NAME_BY_EXPANDED_FILTER;
		if(which == 1)  {  //PART_NAME
			addlWhere = partNameFilter;
		}
		else if(which == 2)  {  //defect_type
			addlWhere = defectTypeFilter;
		}
		else if(which == 3)  {  //iqs
			addlWhere = iqsFilter;
		}
		else if(which == 4)  {  //theme
			addlWhere = themeFilter;
		}
		else if(which == 5)  {  //reportable
			myFilter = "";
			reportableClause = reportableFilter;
		}
		else if(which == 6)  {  //non-reportable
			myFilter = "";
			reportableClause = notReportableFilter;
		}
		params.put("3", myFilter);
		sql = sql.replace("<and_reportable>", reportableClause).replace("<additional_where_clause>", addlWhere);
		List<Object[]> objectList = findResultListByNativeQuery(sql, params);
		List<QiPartLocationCombinationDto> dtoList = new ArrayList<QiPartLocationCombinationDto>();
		
		setDtoList(dtoList, objectList);
		return dtoList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<QiPartLocationCombinationDto> findUnassignedFullPartName(short active, String productKind) {
		Parameters params = Parameters.with("1", active).put("2", productKind);
		List<Object[]> objectList = findResultListByNativeQuery(FIND_UNASSIGNED_FULL_PART_NAME_BY_FILTER, params);
		List<QiPartLocationCombinationDto> dtoList = new ArrayList<QiPartLocationCombinationDto>();
		setDtoList(dtoList, objectList);
		return dtoList;
	}
	
	/**
	* This method is used to load all Part Location Combinations when screen gets loaded.
	* Also, if the user types something in filter, data gets returned based on the filter value.
	* @param partLocCombFilter: Filter data for Part Location Combination.
	* @param productKind: Product Kind for Part Location Combination.
	*/
	public List<QiPartLocationCombination> findActivePartLocCombByFilter(String partLocCombFilter, String productKind) {

		List<QiPartLocationCombination> partLocCombList = findActivePartLocComb(partLocCombFilter, productKind);
		
		return partLocCombList;
	}
	
	/**
	 * This method is called by the findActivePartLocCombByFilter() method to execute filter query for Part Defect Comb Screen.
	 * locationName
	 * @param filterData: Filter data for Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 */
	public List<QiPartLocationCombination> findActivePartLocComb(String filterData, String productKind) {
		Parameters params = Parameters.with("searchString", "%"+filterData+"%")
				.put("productKind", productKind);
		return findAllByQuery(FIND_ACTIVE_PART_LOC_COMB, params);
	}
	
	/**
	 * This method is used to check if the Part Location Combination already exist or not.
	 * @param part1: Part 1 of Part Location Combination.
	 * @param part1Loc1: Part 1 Loc 1 of Part Location Combination.
	 * @param part1Loc2: Part 1 Loc 2 of Part Location Combination.
	 * @param part2: Part 2 of Part Location Combination.
	 * @param part2Loc1: Part 2 Loc 1 of Part Location Combination.
	 * @param part2Loc2: Part 2 Loc 2 of Part Location Combination.
	 * @param part3: Part 3 of Part Location Combination.
	 * @param productKind: Product Kind of Part Location Combination.
	 */
	public boolean checkPartLocComb(String part1, String part1Loc1, String part1Loc2, String part2, String part2Loc1, String part2Loc2, String part3, String productKind, Integer partLocationId)
	{
		Parameters params = Parameters.with("1", part1)
				.put("2", part1Loc1)
				.put("3", part1Loc2)
				.put("4", part2)
				.put("5", part2Loc1)
				.put("6", part2Loc2)
				.put("7", part3)
				.put("8", productKind)
				.put("9", (partLocationId == null) ? 0 : partLocationId.intValue());
		return !findAllByNativeQuery(CHECK_PART_LOC_COMB, params, QiPartLocationCombination.class).isEmpty();
	}
	/**
	 * This method is used to find Part 1 by Product Kind
	 */
	public List<String> findAllPart1ByProductKind(String productKind) {
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ALL_PART1_BY_PRODUCT_KIND,params,String.class);
	}
	/**
	 * This method is used to find all PLC for given Image Section Id
	 */
	public List<QiPartLocationCombination> findAllByImageSectionId(int imageSectionId) {
		Parameters params = Parameters.with("1", imageSectionId);
		return findAllByNativeQuery(FIND_ALL_BY_IMAGE_SECTION_ID, params, QiPartLocationCombination.class);
	}

	public List<String> findAllPart1ByProcessPoint(String processPointId,
			String productKind, String mtcModel, String entryDept) {
		Parameters params = Parameters.with("1", mtcModel)
				.put("2", processPointId)
				.put("3", productKind)
				.put("4", entryDept);
		return findAllByNativeQuery(FIND_ALL_PART1_BY_PROCESSPOINT, params, String.class);
	}
	
	public List<String> findAllPart1AndPart2(String processPointId, String productKind, String mtcModel, String entryDept) {
		Parameters params = Parameters.with("1", entryDept)
				.put("2", processPointId)
				.put("3", mtcModel)
				.put("4", productKind);
		return findAllByNativeQuery(FIND_ALL_PART1_PART2, params, String.class);
	}
	
	public void setDtoList(List<QiPartLocationCombinationDto> dtoList, List<Object[]> list) {
		QiPartLocationCombinationDto dto = null;
		if (list != null && !list.isEmpty()) {
			for (Object[] obj : list) {
				dto = new QiPartLocationCombinationDto();
				dto.setPartLocationId(Integer.parseInt(obj[0].toString()));
				dto.setInspectionPartName(obj[1].toString());
				dto.setFullPartDesc(obj[2].toString());
				dtoList.add(dto);
			}
		}
	}
	
	@Override
	public List<String> findAllByAllPartLocation(String processPointId, String productKind, String mtcModel, String entryDept) {
		Parameters params = Parameters.with("1", entryDept)
				.put("2", processPointId)
				.put("3", mtcModel)
				.put("4", productKind);
		List<String> partLocationDefects = findAllByNativeQuery(FIND_BY_ALL_PART_LOC, params, String.class);
		return partLocationDefects;
	}
	@Override
	public List<QiPartLocationCombination> findAllByPartLocationMatch(String productKind, String partLoc) {
		Parameters params = Parameters.with("productKind", productKind)
				.put("partLoc", partLoc);
		List<QiPartLocationCombination> partLocationDefects = findAllByQuery(FIND_ALL_PART_LOC_COMB_MATCH, params);;
		return partLocationDefects;
	}
}
