package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.DateFormatConstants;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiInspectionLocationDao;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiPositionType;
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.service.Parameters;
/**
 * 
 * 
 * <h3>QiInspectionLocationDaoImpl Class description</h3>
 * <p>
 * QiInspectionLocationDaoImpl contains methods for 'filter the data based on the all properties of Location' and update the 'status' of the Location and 'updateUser'
 *  </p>
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
 * @author LnTInfotech<br>
 *         April 21, 2016
 * 
 */
public class QiInspectionLocationDaoImpl extends BaseDaoImpl<QiInspectionLocation,String> implements QiInspectionLocationDao {

	private static String UPDATE_LOCATION_STATUS = "update QI_INSPECTION_LOCATION_TBX  set ACTIVE = ?1 , UPDATE_USER = ?2 ,APP_UPDATE_TIMESTAMP = ?3 where INSPECTION_PART_LOCATION_NAME= ?4";

	private static String UPDATE_PART_LOCATION_COMB ="update QI_PART_LOCATION_COMBINATION_TBX  set ACTIVE = 0 , UPDATE_USER=?2 where INSPECTION_PART_LOCATION_NAME= ?1 or INSPECTION_PART_LOCATION2_NAME=?1 or INSPECTION_PART2_LOCATION_NAME=?1 or INSPECTION_PART2_LOCATION2_NAME=?1" ;

	private static String FIND_LOCATION_BY_FILTER= "select e from QiInspectionLocation e where (" +
			"e.inspectionPartLocationName like :searchString or " +
			"e.inspectionPartLocDescShort like :searchString or " +
			"e.inspectionPartLocDescLong like :searchString or " +
			"e.active = :isActive or " +
			"e.hierarchy like :searchString or " + 
			"e.primaryPosition = :position or " +
			"e.createUser like :searchString or " +
			"e.updateUser like :searchString ) and e.productKind= :productKind order by e.inspectionPartLocationName"  ;

	private static String FIND_LOCATION_BY_FILTER_ON_DATE= "select e from QiInspectionLocation e where " +
			"(e.appCreateTimestamp like :searchString or " +
			"e.appUpdateTimestamp like :searchString or " +
			"e.inspectionPartLocationName like :searchString or " +
			"e.inspectionPartLocDescShort like :searchString or " +
			"e.inspectionPartLocDescLong like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.createUser like :searchString or " +
			"e.updateUser like :searchString ) and " +
			"e.productKind= :productKind order by e.inspectionPartLocationName"  ;

	private static String FIND_ACTIVE_INSPECTION_LOCATIONS_BY_PRODUCT_KIND = "SELECT * FROM GALADM.QI_INSPECTION_LOCATION_TBX WHERE ACTIVE=1 AND PRODUCT_KIND=?1 ORDER BY INSPECTION_PART_LOCATION_NAME";

	private static String FIND_FILTERED_ACTIVE_INSPECTION_LOCATIONS = "SELECT * FROM GALADM.QI_INSPECTION_LOCATION_TBX WHERE ACTIVE=1 AND " +
			"(INSPECTION_PART_LOCATION_NAME LIKE ?1 OR " +
			"INSPECTION_PART_LOC_DESC_SHORT LIKE ?1 OR " +
			"INSPECTION_PART_LOC_DESC_LONG LIKE ?1) AND PRODUCT_KIND=?2 ORDER BY INSPECTION_PART_LOCATION_NAME";

	private static String FIND_ACTIVE_PRIMARY_INSPECTION_LOCATIONS_BY_PRODUCT_KIND = "SELECT * FROM GALADM.QI_INSPECTION_LOCATION_TBX WHERE ACTIVE=1 AND PRIMARY_POSITION=1 AND PRODUCT_KIND=?1 ORDER BY INSPECTION_PART_LOCATION_NAME";

	private static String FIND_FILTERED_ACTIVE_PRIMARY_INSPECTION_LOCATIONS = "SELECT * FROM GALADM.QI_INSPECTION_LOCATION_TBX WHERE ACTIVE=1 AND " +
			"(INSPECTION_PART_LOCATION_NAME LIKE ?1 OR " +
			"INSPECTION_PART_LOC_DESC_SHORT LIKE ?1 OR " +
			"INSPECTION_PART_LOC_DESC_LONG LIKE ?1) AND PRIMARY_POSITION=1 AND PRODUCT_KIND=?2 ORDER BY INSPECTION_PART_LOCATION_NAME";

	private static String UPDATE_LOCATION = "update QI_INSPECTION_LOCATION_TBX  set ACTIVE = ?1, INSPECTION_PART_LOCATION_NAME = ?2 , " +
			"INSPECTION_PART_LOC_DESC_SHORT = ?3, INSPECTION_PART_LOC_DESC_LONG =?4 , " +
			"PRIMARY_POSITION =?5, HIERARCHY =?6, UPDATE_USER = ?7 , APP_UPDATE_TIMESTAMP = ?8 where INSPECTION_PART_LOCATION_NAME= ?9";

	private static String FIND_LOCATION_BY_STATUS_OR_FILTER= "select e from QiInspectionLocation e where (" +
			"e.inspectionPartLocationName like :searchString or " +
			"e.inspectionPartLocDescShort like :searchString or " +
			"e.inspectionPartLocDescLong like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.primaryPosition = :position or " +
			"e.active = :isActive or " +
			"e.createUser like :searchString or " +
			"e.updateUser like :searchString ) and e.active = :status  and e.productKind= :productKind order by e.inspectionPartLocationName"  ;
	
	private static String FIND_LOCATION_BY_STATUS_OR_FILTER_ON_DATE= "select e from QiInspectionLocation e where " +
			"(e.appCreateTimestamp like :searchString or " +
			"e.appUpdateTimestamp like :searchString  or " +
			"e.inspectionPartLocationName like :searchString  or " +
			"e.inspectionPartLocDescShort like :searchString  or " +
			"e.inspectionPartLocDescLong like :searchString  or " +
			"e.hierarchy like :searchString  or " + 
			"e.createUser like :searchString  or " +
			"e.updateUser like :searchString  ) and " +
			"e.productKind= :productKind and e.active= :isActive order by e.inspectionPartLocationName"  ;
	/**
	 * This method is used to filter the data based on the all properties of Location
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */
	public List<QiInspectionLocation> findLocationByFilter(String filterValue, String productKind) {
		String regex = DateFormatConstants.REG_EXP_FOR_DATE_FORMAT;
		if(filterValue.matches(regex))
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("productKind", productKind);
			return findAllByQuery(FIND_LOCATION_BY_FILTER_ON_DATE, params);
		}
		else
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("isActive",((filterValue.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("position",((filterValue.equalsIgnoreCase(QiPositionType.PRIMARY.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiPositionType.SECONDARY.getName()))?(short)0:(short)2))
					.put("productKind", productKind);
			return findAllByQuery(FIND_LOCATION_BY_FILTER, params);
		}
	}

	/**
	 * This method is used to update the 'status' of the Location and 'updateUser'
	 * @param name - Location Name
	 * @param active- 1=active, 0 =inactive
	 * @param user- User Id
	 */

	@Transactional
	public void updateLocationStatus(String name, short active, String user) {
		Parameters params = Parameters.with("1", active)
				.put("2", user).put("3",new Date()).put("4",name);
		executeNativeUpdate(UPDATE_LOCATION_STATUS, params);
	}

	/**
	 * This method is used to find active Inspection Locations.
	 * @param productKind- Product Kind of the Inspection Location
	 * @return the number of rows having active Inspection Locations based on product kind
	 */
	public List<QiInspectionLocation> findActiveInspectionLocationsByProductKind(String productKind) {
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ACTIVE_INSPECTION_LOCATIONS_BY_PRODUCT_KIND, params, QiInspectionLocation.class);
	}

	/**
	 * This method is used to find filtered active Inspection Locations.
	 * @param filterData- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows which are having active Inspection Locations based on filtered value 
	 */
	public List<QiInspectionLocation> findFilteredActiveInspectionLocations(String filterData, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_FILTERED_ACTIVE_INSPECTION_LOCATIONS, params, QiInspectionLocation.class);
	}

	/**
	 * This method is used to find active and primary Inspection Locations.
	 * @param productKind- Product Kind of the Inspection Location
	 * @return the number of rows which are having active and primary Inspection Locations
	 */

	public List<QiInspectionLocation> findActivePrimaryInspectionLocationsByProductKind(String productKind) {
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ACTIVE_PRIMARY_INSPECTION_LOCATIONS_BY_PRODUCT_KIND, params, QiInspectionLocation.class);
	}

	/**
	 * This method is used to find filtered active and primary Inspection Locations.
	 * @param filterData- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */

	public List<QiInspectionLocation> findFilteredActivePrimaryInspectionLocations(String filterData, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_FILTERED_ACTIVE_PRIMARY_INSPECTION_LOCATIONS, params, QiInspectionLocation.class);
	}

	/**
	 * This method is used to inActivate associated Part location Combination .
	 * @param locationName- Location Name 
	 */

	@Transactional
	public void inactivateLocation(String locationName,String userName){
		Parameters params = Parameters.with("1", locationName).put("2", userName);
		executeNativeUpdate(UPDATE_PART_LOCATION_COMB, params);
	}

	/**
	 * To Update Location along with Location name
	 * @param qiInspectionLocation
	 * @param oldLocName
	 */
	@Transactional
	public void updateLocation(QiInspectionLocation qiInspectionLocation, String oldLocName) {
		Parameters params = Parameters.with("1", qiInspectionLocation.getActiveValue())
				.put("2", qiInspectionLocation.getInspectionPartLocationName()).put("3",qiInspectionLocation.getInspectionPartLocDescShort())
				.put("4", qiInspectionLocation.getInspectionPartLocDescLong())
				.put("5", qiInspectionLocation.getPrimaryPositionValue()).put("6",qiInspectionLocation.getHierarchy())
				.put("7", qiInspectionLocation.getUpdateUser()).put("8",new Date()).put("9",oldLocName);
		executeNativeUpdate(UPDATE_LOCATION, params);
	}
	/**
	 * This method is used to filter the data based on the all properties of Location and Status
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */
	public List<QiInspectionLocation> findLocationByFilter(String filterValue, String status ,String productKind) {
		String regex = DateFormatConstants.REG_EXP_FOR_DATE_FORMAT;
		if(filterValue.matches(regex))
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("productKind", productKind).put("isActive", ((status.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(status.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2));
			return findAllByQuery(FIND_LOCATION_BY_STATUS_OR_FILTER_ON_DATE, params);
		}
		else
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("position", ((filterValue.equalsIgnoreCase(QiPositionType.PRIMARY.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiPositionType.SECONDARY.getName()))?(short)0:(short)2))
					.put("isActive", ((filterValue.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("status",((status.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(status.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("productKind", productKind);
			return findAllByQuery(FIND_LOCATION_BY_STATUS_OR_FILTER, params);
		}
	}
}
