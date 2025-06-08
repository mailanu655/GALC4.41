package com.honda.galc.dao.jpa.qi;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationRepairMethodDao;
import com.honda.galc.entity.qi.QiStationRepairMethod; 
import com.honda.galc.entity.qi.QiStationRepairMethodId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationRepairMethodDaoImpl</code> is an implementation class for QiStationRepairMethodDao interface.
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

public class QiStationRepairMethodDaoImpl extends BaseDaoImpl<QiStationRepairMethod,QiStationRepairMethodId> implements QiStationRepairMethodDao{
	
	private static final String UPDATE_REPAIR_METHODS_BY_REPAIR_METHOD = "update GALADM.QI_STATION_REPAIR_METHOD_TBX  set REPAIR_METHOD = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_METHOD = ?3";
	
	public List<QiStationRepairMethod> findAllByRepairMethod(String repairMethodName) {						
		return findAll(Parameters.with("id.repairMethod", repairMethodName));
	}
	
	@Transactional
	public void removeRepairMethodFromStations(String oldRepairMethodName) {		
		delete(Parameters.with("id.repairMethod", oldRepairMethodName));
	}
	
	@Transactional
	public void updateAllByRepairMethod(String repairMethodName, String oldRepairMethodName, String updateUser) {
		Parameters params = Parameters.with("1", repairMethodName)
				.put("2", updateUser)
				.put("3", oldRepairMethodName);			
		executeNativeUpdate(UPDATE_REPAIR_METHODS_BY_REPAIR_METHOD, params);
	}
	
}

