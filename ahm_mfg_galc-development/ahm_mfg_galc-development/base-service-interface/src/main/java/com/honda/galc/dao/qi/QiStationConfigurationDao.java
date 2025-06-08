package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiEntryStationConfigManagementDaoImpl Class description</h3>
 * <p>
 * QiEntryStationConfigManagementDaoImpl contains methods 
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
public interface QiStationConfigurationDao extends IDaoService<QiStationConfiguration,QiStationConfigurationId>{
	/**
	 * This method is used to find all entries and settings based on process point.
	 * @param processpointId
	 * @return QiEntryStationConfigManagement
	 */
	public List<QiStationConfiguration> findAllByProcessPointId(String processPointId);
	
	public long countByProcessPointId(String processPointId);
	
	/**
	 * This method finds Default Defect status as configured on Station Configuration Screen based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiStationConfiguration findValueByProcessPointAndPropKey(String processPointId,String PropertyKey) ;
	public int deleteByProcessPoint(String processPointId);
	
}
