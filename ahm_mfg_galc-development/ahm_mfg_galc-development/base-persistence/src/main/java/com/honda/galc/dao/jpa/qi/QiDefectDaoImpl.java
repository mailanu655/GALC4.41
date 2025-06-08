package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.DateFormatConstants;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDefectDao;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiPositionType;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIDefectDAOImpl Class description</h3>
 * <p> QIDefectDAOImpl description </p>
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
 * @author L&T Infotech<br>
 * April 20, 2016
 *
 *
 */

public class QiDefectDaoImpl extends BaseDaoImpl<QiDefect, String> implements QiDefectDao {

	private static String UPDATE_DEFECT_STATUS = "update QI_DEFECT_TBX  set ACTIVE = ?1 , UPDATE_USER = ?2, APP_UPDATE_TIMESTAMP = ?3 where DEFECT_TYPE_NAME= ?4";

	private static String FIND_DEFECT_BY_FILTER= "select e from QiDefect e where (" +
			"e.defectTypeName like :searchString or " +
			"e.defectTypeDescriptionShort like :searchString or " +
			"e.defectTypeDescriptionLong like :searchString or " +
			"e.defectCategoryName like :searchString or " +
			"e.active = :isActive or " +
			"e.primaryPosition = :position or " +
			"e.createUser like :searchString or " +
			"e.updateUser like :searchString ) and e.active in (:statusList) and e.productKind= :productKind order by e.defectTypeName"  ;

	private static String FIND_DEFECT_BY_FILTER_ON_DATE= "select e from QiDefect e where (" +
			"e.appCreateTimestamp like :searchString or " +
			"e.appUpdateTimestamp like :searchString or " +
			"e.defectTypeName like :searchString or " +
			"e.defectTypeDescriptionShort like :searchString or " +
			"e.defectTypeDescriptionLong like :searchString or " +
			"e.defectCategoryName like :searchString or " +
			"e.createUser like :searchString or " +
			"e.updateUser like :searchString) and e.active in (:statusList) and " +
			"e.productKind= :productKind order by e.defectTypeName"  ;

	private static String UPDATE_DEFECT = "update QI_DEFECT_TBX  set ACTIVE = ?1, DEFECT_TYPE_NAME= ?2 , " +
			"DEFECT_TYPE_DESCRIPTION_SHORT = ?3, DEFECT_TYPE_DESCRIPTION_LONG =?4 , DEFECT_CATEGORY_NAME =?5, " +
			"PRIMARY_POSITION =?6, UPDATE_USER = ?8, APP_UPDATE_TIMESTAMP = ?9 where DEFECT_TYPE_NAME= ?10";
	
	private static String FIND_ACTIVE_PRIMARY_DEFECT_BY_FILTER= "select e from QiDefect e where (" +
			"e.defectTypeName like :searchString or " +
			"e.defectTypeDescriptionShort like :searchString or " +
			"e.defectCategoryName like :searchString) and " +
			"e.active=1 and e.primaryPosition=1 and e.productKind= :productKind order by e.defectTypeName";
	
	private static String FIND_ACTIVE_SECONDARY_DEFECT_BY_FILTER= "select e from QiDefect e where (" +
			"e.defectTypeName like :searchString or " +
			"e.defectTypeDescriptionShort like :searchString or " +
			"e.defectCategoryName like :searchString) and " +
			"e.active=1 and e.primaryPosition=0 and e.productKind= :productKind order by e.defectTypeName";
	/**
	 * To Filter the table data
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the defect
	 * @return the number of rows based on filtered value
	 */

	public List<QiDefect> findDefectByFilter(String filterValue, String productKind, List<Short> statusList) {
		String regex = DateFormatConstants.REG_EXP_FOR_DATE_FORMAT;
		if(filterValue.matches(regex))
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("statusList", statusList)
					.put("productKind", productKind);
			return findAllByQuery(FIND_DEFECT_BY_FILTER_ON_DATE, params);
		}
		else
		{
			Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
					.put("isActive",((filterValue.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("position",((filterValue.equalsIgnoreCase(QiPositionType.PRIMARY.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiPositionType.SECONDARY.getName()))?(short)0:(short)2))
					.put("statusList", statusList)
					.put("productKind", productKind);
			return findAllByQuery(FIND_DEFECT_BY_FILTER, params);
		}

	}

	/**
	 * To Update Defect Status
	 * @param name - Defect Type Name
	 * @param active- 1=active, 0 =inactive
	 * @param user- User Id 
	 */
	@Transactional
	public void updateDefectStatus(String name, short active, String user) {
		Parameters params = Parameters.with("1", active)
				.put("2", user).put("3", new Date()).put("4",name);
		executeNativeUpdate(UPDATE_DEFECT_STATUS, params);
	}

	/**
	 * To Update Defect along with Defect type name
	 * @param qiDefect
	 * @param oldDefectName
	 */
	@Transactional
	public void updateDefect(QiDefect qiDefect, String oldDefectName) {
		Parameters params = Parameters.with("1", qiDefect.getActiveValue())
				.put("2", qiDefect.getDefectTypeName()).put("3",qiDefect.getDefectTypeDescriptionShort())
				.put("4", qiDefect.getDefectTypeDescriptionLong()).put("5",qiDefect.getDefectCategoryName())
				.put("6", qiDefect.getPrimaryPositionValue())
				.put("8", qiDefect.getUpdateUser()).put("9", new Date()).put("10",oldDefectName);
		executeNativeUpdate(UPDATE_DEFECT, params);
	}
	
	/**
	 * Returns list of active primary defects.
	 */
	public List<QiDefect> findActivePrimaryDefectByFilter(String filterValue, String productKind) {
		Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
				.put("productKind", productKind);
		return findAllByQuery(FIND_ACTIVE_PRIMARY_DEFECT_BY_FILTER, params);
	}
	/**
	 * Returns list of active secondary defects.
	 */
	public List<QiDefect> findActiveSecondaryDefectByFilter(String filterValue, String productKind) {
		Parameters params = Parameters.with("searchString", "%" +filterValue+ "%")
				.put("productKind", productKind);
		return findAllByQuery(FIND_ACTIVE_SECONDARY_DEFECT_BY_FILTER, params);
	}

}
