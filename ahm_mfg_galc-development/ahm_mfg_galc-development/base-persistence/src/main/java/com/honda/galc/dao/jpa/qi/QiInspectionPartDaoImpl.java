package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.DateFormatConstants;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiInspectionPartDao;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiFlag;
import com.honda.galc.entity.enumtype.QiPositionType;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QIPartMaintenanceDAOImpl Class description</h3>
 * <p>
 * QIPartMaintenanceDAOImpl contains methods for 'filter the data based on the all properties of Part' and update the 'status' of the Part and 'updateUser'
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
public class QiInspectionPartDaoImpl extends BaseDaoImpl<QiInspectionPart,String> implements QiInspectionPartDao{
	
	private static String FIND_ACTIVE_PRIMARY_INSPECTION_PARTS_BY_PRODUCT_KIND = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND PRIMARY_POSITION=1 AND PRODUCT_KIND=?1 ORDER BY INSPECTION_PART_NAME";
	
	private static String FIND_ACTIVE_SECONDARY_INSPECTION_PARTS_BY_PRODUCT_KIND = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND PRIMARY_POSITION=0 AND PRODUCT_KIND=?1 ORDER BY INSPECTION_PART_NAME";
	
	private static String FIND_FILTERED_ACTIVE_PRIMARY_INSPECTION_PARTS = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND PRIMARY_POSITION=1 AND " +
			"(INSPECTION_PART_NAME LIKE ?1 OR INSPECTION_PART_DESC_SHORT LIKE ?1 OR " +
			"INSPECTION_PART_DESC_LONG LIKE ?1 OR PART_CLASS LIKE ?1) AND PRODUCT_KIND=?2 ORDER BY INSPECTION_PART_NAME";
	
	private static String FIND_FILTERED_ACTIVE_SECONDARY_INSPECTION_PARTS = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND PRIMARY_POSITION=0 AND " +
			"(INSPECTION_PART_NAME LIKE ?1 OR INSPECTION_PART_DESC_SHORT LIKE ?1 OR " +
			"INSPECTION_PART_DESC_LONG LIKE ?1 OR PART_CLASS LIKE ?1) AND PRODUCT_KIND=?2 ORDER BY INSPECTION_PART_NAME";
	
	private static String FIND_ACTIVE_INSPECTION_PARTS_BY_PRODUCT_KIND = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND PRODUCT_KIND=?1 ORDER BY INSPECTION_PART_NAME";
	
	private static String FIND_FILTERED_ACTIVE_INSPECTION_PARTS = "SELECT * FROM GALADM.QI_INSPECTION_PART_TBX WHERE ACTIVE=1 AND " +
			"(INSPECTION_PART_NAME LIKE ?1 OR INSPECTION_PART_DESC_SHORT LIKE ?1 OR " +
			"INSPECTION_PART_DESC_LONG LIKE ?1 OR PART_CLASS LIKE ?1) AND PRODUCT_KIND=?2 ORDER BY INSPECTION_PART_NAME";
	
	private final static String FIND_PARTS_BY_FILTER = 	"select e from QiInspectionPart e where  e.productKind = :productKind and ( e.inspectionPartName like :searchString or " +
			"e.inspectionPartDescShort like :searchString or " +
			"e.inspectionPartDescLong like :searchString or " +
			"e.partClass like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.createUser like :searchString or " +
		    "e.updateUser like :searchString or " +
			"e.active = :isActive or " +
			"e.primaryPosition = :position or " +
	        "e.allowMultiple = :allowMultiple ) order by e.inspectionPartName";
	
	private final static String FIND_PARTS_BY_FILTER_ON_DATE = 	"select e from QiInspectionPart e where  e.productKind = :productKind  and " +
	        "(e.appCreateTimestamp like :searchString or e.appUpdateTimestamp like :searchString or " +
	        "e.inspectionPartName like :searchString or " +
			"e.inspectionPartDescShort like :searchString or " +
			"e.inspectionPartDescLong like :searchString or " +
			"e.partClass like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.createUser like :searchString or " +
		    "e.updateUser like :searchString) " +
	        "order by e.inspectionPartName";
	
	private final static String UPDATE_PART_STATUS  ="update QI_INSPECTION_PART_TBX  set ACTIVE = ?1, UPDATE_USER = ?2, APP_UPDATE_TIMESTAMP = ?3 where INSPECTION_PART_NAME= ?4" ;
	private final static String INACTIVATE_PART ="update QI_PART_LOCATION_COMBINATION_TBX  set ACTIVE = 0 ,UPDATE_USER = ?4"+ " where INSPECTION_PART_NAME= ?1 or INSPECTION_PART2_NAME=?2 or INSPECTION_PART3_NAME=?3 " ;
	private final static String FIND_ACTIVE_PART_NAME_BY_FILTER = "select e from QiInspectionPart e where e.inspectionPartName like :partName and e.productKind =  :productKind and e.active = 1 order by e.inspectionPartName";
	
	private final static String FIND_PARTS_BY_STATUS_OR_FILTER = "select e from QiInspectionPart e where ( e.inspectionPartName like :searchString or " +
			"e.inspectionPartDescShort like :searchString or " +
			"e.inspectionPartDescLong like :searchString or " +
			"e.partClass like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.createUser like :searchString or " +
		    "e.updateUser like :searchString or " +
			"e.primaryPosition = :position or " +
			"e.active = :isActive or " +
	        "e.allowMultiple = :allowMultiple ) and e.active = :isStatusActive  and e.productKind = :productKind order by e.inspectionPartName";
	
	private final static String FIND_PARTS_BY_STATUS_OR_FILTER_ON_DATE = 	"select e from QiInspectionPart e where  e.productKind = :productKind and e.active= :isActive and " +
	        "(e.appCreateTimestamp like :searchString or e.appUpdateTimestamp like :searchString or " +
	        "e.inspectionPartName like :searchString or " +
			"e.inspectionPartDescShort like :searchString or " +
			"e.inspectionPartDescLong like :searchString or " +
			"e.partClass like :searchString or " +
			"e.hierarchy like :searchString or " + 
			"e.createUser like :searchString or " +
		    "e.updateUser like :searchString) " +
	        "order by e.inspectionPartName";
	
	private final static String UPDATE_PART = "update QI_INSPECTION_PART_TBX  set ACTIVE = ?1, INSPECTION_PART_NAME = ?2 , " +
			"INSPECTION_PART_DESC_SHORT = ?3, INSPECTION_PART_DESC_LONG =?4 , " +
			"PRIMARY_POSITION =?5, HIERARCHY =?6, PART_CLASS=?7 ,ALLOW_MULTIPLE=?8, UPDATE_USER = ?9,APP_UPDATE_TIMESTAMP = ?10 where INSPECTION_PART_NAME= ?11";
	
	/**
	 * This method is used to filter the data based on the all properties of Part
	 * @param namedQuery
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findPartsByFilter(String filterData,String productKind) {
		Parameters params;
		if(filterData.matches(DateFormatConstants.REG_EXP_FOR_DATE_FORMAT))
		{
			params = Parameters.with("productKind", productKind)
					.put("searchString", "%" +filterData+ "%");
			return findAllByQuery(FIND_PARTS_BY_FILTER_ON_DATE, params);
		}
		else
		{
			params = Parameters.with("productKind", productKind)
					.put("searchString", "%"+filterData+"%")
					.put("isActive", ((filterData.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterData.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("position", ((filterData.equalsIgnoreCase(QiPositionType.PRIMARY.getName()))?(short)1:(filterData.equalsIgnoreCase(QiPositionType.SECONDARY.getName()))?(short)0:(short)2))
					.put("allowMultiple", ((filterData.equalsIgnoreCase(QiFlag.YES.getName()))?(short)1:(filterData.equalsIgnoreCase(QiFlag.NO.getName()))?(short)0:(short)2));
				return findAllByQuery(FIND_PARTS_BY_FILTER,params);
		}
		
	}
	
	/**
	 * This method is used to update the 'status' of the Part and 'updateUser'
	 * @param name
	 * @param active
	 * @param updateUser
	 */
	@Transactional
	public void updatePartStatus(String name, short active ,String updateUser) {
		
		Parameters params = Parameters.with("1", active).put("2",updateUser).put("3", new Date()).put("4", name);
		executeNativeUpdate(UPDATE_PART_STATUS,params);
	}
	
	/**
	 * This method is used to inactivate the Part in Part Location Combination.
	 * @param partName
	 */
	@Transactional
	public void inactivatePart(String partName,String userId){
		Parameters params = Parameters.with("1", partName).put("2",partName).put("3", partName).put("4", userId);
		executeNativeUpdate(INACTIVATE_PART,params);
	}
	
	/**
	 * This method is used to find active and primary Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActivePrimaryInspectionPartsByProductKind(String productKind) {
		
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ACTIVE_PRIMARY_INSPECTION_PARTS_BY_PRODUCT_KIND, params, QiInspectionPart.class);
	}
	
	/**
	 * This method is used to find active and secondary Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActiveSecondaryInspectionPartsByProductKind(String productKind) {
		
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ACTIVE_SECONDARY_INSPECTION_PARTS_BY_PRODUCT_KIND, params, QiInspectionPart.class);
	}
	
	/**
	 * This method is used to find filtered active and primary Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActivePrimaryInspectionParts(String filterData, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_FILTERED_ACTIVE_PRIMARY_INSPECTION_PARTS, params, QiInspectionPart.class);
	}
	
	/**
	 * This method is used to find filtered active and secondary Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActiveSecondaryInspectionParts(String filterData, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_FILTERED_ACTIVE_SECONDARY_INSPECTION_PARTS, params, QiInspectionPart.class);
	}
	
	/**
	 * This method is used to find active Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActiveInspectionPartsByProductKind(String productKind) {
		Parameters params = Parameters.with("1", productKind);
		return findAllByNativeQuery(FIND_ACTIVE_INSPECTION_PARTS_BY_PRODUCT_KIND, params, QiInspectionPart.class);
	}
	
	/**
	 * This method is used to find filtered active Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActiveInspectionParts(String filterData, String productKind) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_FILTERED_ACTIVE_INSPECTION_PARTS, params, QiInspectionPart.class);
	}

	/**
	 * This method is used to filter data based on Part Name
	 * @param partName
	 * @return
	 */
	public List<QiInspectionPart> findActivePartNameByFilter(String partName, String productKind) {
		Parameters params = Parameters.with("partName","%"+partName+"%").put("productKind", productKind);
		return findAllByQuery(FIND_ACTIVE_PART_NAME_BY_FILTER,params);
	}
	
	/**
	 * This method is used to filter the data based on the all properties of Part
	 * @param namedQuery
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findPartsByFilter(String filterData,String status,String productKind) {
		Parameters params;
		if(filterData.matches(DateFormatConstants.REG_EXP_FOR_DATE_FORMAT))
		{
			params = Parameters.with("productKind", productKind)
					.put("isActive", ((status.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(status.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2)).put("searchString", "%" +filterData+ "%");
			return findAllByQuery(FIND_PARTS_BY_STATUS_OR_FILTER_ON_DATE, params);
		}
		else
		{
			params = Parameters.with("searchString", "%"+filterData+"%")
					.put("position", ((filterData.equalsIgnoreCase(QiPositionType.PRIMARY.getName()))?(short)1:(filterData.equalsIgnoreCase(QiPositionType.SECONDARY.getName()))?(short)0:(short)2))
					.put("isActive", ((filterData.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterData.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("allowMultiple", ((filterData.equalsIgnoreCase(QiFlag.YES.getName()))?(short)1:(filterData.equalsIgnoreCase(QiFlag.NO.getName()))?(short)0:(short)2))
					.put("isStatusActive", ((status.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(status.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("productKind", productKind);
				return findAllByQuery(FIND_PARTS_BY_STATUS_OR_FILTER,params);
		}
	}

	/**
	 * This method is used to update the PartName
	 */
	@Transactional
	public void updatePart(QiInspectionPart inspectionPart, String oldPartName){
		Parameters params = Parameters.with("1", inspectionPart.getActiveValue())
				.put("2", inspectionPart.getInspectionPartName()).put("3",inspectionPart.getInspectionPartDescShort())
				.put("4", inspectionPart.getInspectionPartDescLong())
				.put("5", inspectionPart.getPrimaryPositionValue()).put("6",inspectionPart.getHierarchy())
				.put("7", inspectionPart.getPartClass()).put("8",inspectionPart.getAllowMultipleValue())
				.put("9", inspectionPart.getUpdateUser()).put("10",new Date()).put("11",oldPartName);
		executeNativeUpdate(UPDATE_PART, params);
	}
}
