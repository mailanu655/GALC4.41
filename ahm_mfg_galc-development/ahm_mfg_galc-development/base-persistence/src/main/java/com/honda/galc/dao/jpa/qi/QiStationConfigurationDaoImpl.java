package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiEntryStationConfigManagementDaoImpl Class description</h3>
 * <p>
 * QiEntryStationConfigManagementDaoImpl contains methods to find QiEntryStationConfigManagement by processPointId and propertyValue
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
 *        Oct 25,2016
 * 
 */
public class QiStationConfigurationDaoImpl extends BaseDaoImpl<QiStationConfiguration, QiStationConfigurationId> implements QiStationConfigurationDao{
	
	private static String FIND_DEFAULT_DEFECT_STATUS_BY_PROCESS_POINT ="select e from QiStationConfiguration e " +
			"where e.id.processPointId= :processPointId and e.id.propertyKey= :propertyKey and e.active=1 ";
	
	/**
	 * This method is used to find all entries and settings based on process point.
	 * @param processpointId
	 * @return QiEntryStationConfigManagement
	 */	
	public List<QiStationConfiguration> findAllByProcessPointId(String processPointId) {
		return findAll(Parameters.with("id.processPointId", StringUtils.trimToEmpty(processPointId)));
	}
	/**
	 * This method is used to find count of settings based on process point.
	 * @param processpointId
	 * @return long
	 */	
	public long countByProcessPointId(String processPointId) {
		return count(Parameters.with("id.processPointId", StringUtils.trimToEmpty(processPointId)));
	}
	/**
	 * This method finds Default Defect status as configured on Station Configuration Screen based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiStationConfiguration findValueByProcessPointAndPropKey(String processPointId,String PropertyKey) {
		return findFirstByQuery(FIND_DEFAULT_DEFECT_STATUS_BY_PROCESS_POINT, Parameters.with("processPointId", processPointId).put("propertyKey", PropertyKey));
	}

	@Transactional
	public int deleteByProcessPoint(String processPointId) {

		return delete(Parameters.with("id.processPointId", StringUtils.trimToEmpty(processPointId)));
	}
}
