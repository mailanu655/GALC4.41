package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationRepairMethod;
import com.honda.galc.entity.qi.QiStationRepairMethodId;
import com.honda.galc.service.IDaoService;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationRepairMethodDao</code> is a DAO interface to implement database interaction for Repair Method Process Point.
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
public interface QiStationRepairMethodDao extends IDaoService<QiStationRepairMethod, QiStationRepairMethodId>{

	public List<QiStationRepairMethod> findAllByRepairMethod(String repairMethodName);
	public void removeRepairMethodFromStations(String oldRepairMethodName);
	public void updateAllByRepairMethod(String repairMethodName,String oldRepairMethodName,String updateUser);
}
