package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.DateFormatConstants;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiRepairMethodDao;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiRepairMethodDaoImpl</code> is an implementation class for QiRepairMethodDao interface.
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
public class QiRepairMethodDaoImpl extends BaseDaoImpl<QiRepairMethod, String> implements QiRepairMethodDao{

	private static final String FIND_BY_FILTER_DATA = 	"select e from QiRepairMethod e where (e.repairMethod like :searchString or"
			+" e.active = :isActive or e.repairMethodDescription like :searchString or e.createUser like :searchString or e.updateUser like :searchString)"
			+" and e.active in (:statusList) order by e.repairMethod";
	
	private static final String FIND_REPAIR_METHOD_BY_FILTER_ON_DATE = 	"select e from QiRepairMethod e where (e.repairMethod like :searchString or"
			+" e.repairMethodDescription like :searchString or e.createUser like :searchString or e.updateUser like :searchString or e.appCreateTimestamp like :searchString or e.appUpdateTimestamp like :searchString)"
			+" and e.active in (:statusList) order by e.repairMethod";
	
	private static final String UPDATE_REPAIR_METHOD = "update GALADM.QI_REPAIR_METHOD_TBX  set ACTIVE = ?1, REPAIR_METHOD= ?2 , " +
			"REPAIR_METHOD_DESCRIPTION = ?3,  " +
			" UPDATE_USER = ?4, APP_UPDATE_TIMESTAMP = ?5 where REPAIR_METHOD= ?6";
	
	private static final String FIND_REPAIR_METHOD_BY_FILTER_DATA = "select e from QiRepairMethod e where " +
			"(e.repairMethod like :searchString or " +
			"e.repairMethodDescription like :searchString) and e.active = 1 order by e.repairMethod";
	
	private static final String FIND_REPAIR_METHOD_BY_QICS_STATION =  "select a.* from QI_REPAIR_METHOD_TBX a, " +
			"QI_STATION_REPAIR_METHOD_TBX b where a.REPAIR_METHOD=b.REPAIR_METHOD and b.PROCESS_POINT_ID=?1";
	
	private static final String FIND_ASSIGNED_REPAIR_METHOD_BY_DIVISION_APPLICATION_COMPONENT = "select  distinct a.* from galadm.QI_REPAIR_METHOD_TBX a " +
			",galadm.QI_STATION_REPAIR_METHOD_TBX b where a.REPAIR_METHOD=b.REPAIR_METHOD and b.PROCESS_POINT_ID " +
			"in (select distinct p.process_point_id from galadm.gal214tbx p, galadm.gal241tbx q where p.process_point_id = q.application_id and q.application_id in (select component_id from galadm.gal489tbx where property_key='IS_NAQ_STATION' and property_value='TRUE') and DIVISION_ID =?1)";
	
	private static final String FIND_REPAIR_METHOD_BY_FILTER_ON_SELECTED_DIVISION = "select  distinct a.* from galadm.QI_REPAIR_METHOD_TBX a " +
			",galadm.QI_STATION_REPAIR_METHOD_TBX b where (a.REPAIR_METHOD like ?1 or a.REPAIR_METHOD_DESCRIPTION like ?1) and a.REPAIR_METHOD=b.REPAIR_METHOD and b.PROCESS_POINT_ID " +
			"in (select distinct p.process_point_id from galadm.gal214tbx p, galadm.gal241tbx q where p.process_point_id = q.application_id and q.application_id in (select component_id from galadm.gal489tbx where property_key='IS_NAQ_STATION' and property_value='TRUE') and DIVISION_ID =?2)";
	
	
	/**
	 * To Filter the table data
	 * @param filterValue- Input from the user in filter
	 * @return the number of rows based on filtered value
	 */
	
	public List<QiRepairMethod> findRepairMethodsByFilter(String filter, List<Short> statusList) {
		String regex = DateFormatConstants.REG_EXP_FOR_DATE_FORMAT;
		if(filter.matches(regex))
		{
			Parameters params = Parameters.with("searchString", "%" +filter+ "%")
					.put("statusList", statusList);
			return findAllByQuery(FIND_REPAIR_METHOD_BY_FILTER_ON_DATE, params);
		}
		else
		{
			Parameters params = Parameters.with("searchString","%"+filter+"%")
					.put("isActive",((filter.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filter.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
					.put("statusList",statusList);
			return findAllByQuery(FIND_BY_FILTER_DATA, params);
		}
	}

	
	/**
	 * To Update Repair Method along with Repair Method Name
	 * @param qiRepairMethod
	 * @param oldRepairMethodName
	 */
	
	@Transactional
	public void updateRepairMethod(QiRepairMethod qiRepairMethod, String oldRepairMethodName) {
		Parameters params = Parameters.with("1", qiRepairMethod.getActive())
				.put("2", qiRepairMethod.getRepairMethod()).put("3",qiRepairMethod.getRepairMethodDescription())
				.put("4", qiRepairMethod.getUpdateUser()).put("5",new Date()).put("6",oldRepairMethodName);
		executeNativeUpdate(UPDATE_REPAIR_METHOD, params);
	}

	/**
	 * This method is called by the findFilteredRepairMethod() method to execute filter query.
	 * @param filterData: Filter data for Repair Method.
	 * @return List of Repair Method.
	 */
	public List<QiRepairMethod> findFilteredRepairMethods(String filterData) {
		Parameters params = Parameters.with("searchString", "%"+filterData+"%");
		return findAllByQuery(FIND_REPAIR_METHOD_BY_FILTER_DATA, params);
	}

	/**
	 * This method is used to find all Repair method based on Qics station(Process Point).
	 * @param qicsStaion.
	 * @return List of RepairMethod.
	 * **/
	public List<QiRepairMethod> findAllRepairMethodsByQicsStation(String qicsStaion) {
		Parameters params = Parameters.with("1", qicsStaion);
		return findAllByNativeQuery(FIND_REPAIR_METHOD_BY_QICS_STATION, params);
	}
	
	/**
	 * This method is used to find all Assigned Repair method based on Division.
	 * @param qicsStaion.
	 * @return List of RepairMethod.
	 * **/
	public List<QiRepairMethod> findAllAssignedRepairMethodsByDivision(String division) {
		Parameters params = Parameters.with("1", division);
		return findAllByNativeQuery(FIND_ASSIGNED_REPAIR_METHOD_BY_DIVISION_APPLICATION_COMPONENT, params);
	}
	

	/**
	 * This method is used to find all Active Repair method.
	 * @param .
	 * @return List of RepairMethod.
	 * **/
	public List<QiRepairMethod> findAllActiveRepairMethods() {
		Parameters params = Parameters.with("active", (short)1);
		return findAll(params, new String[]{"repairMethod"}, true);
	}
	
	/**
	 * This method is used to execute filter query for selected repair method
	 * @param filterData: Filter data for Repair Method,division name.
	 * @return List of selected Repair Method.
	 */
	public List<QiRepairMethod> findFilteredRepairMethodsForSelectedDivision(String filterData,String division) {
		Parameters params = Parameters.with("1", "%"+filterData+"%")
									.put("2",division);
		return findAllByNativeQuery(FIND_REPAIR_METHOD_BY_FILTER_ON_SELECTED_DIVISION, params);
	}
}
