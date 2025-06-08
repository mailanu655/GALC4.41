package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.product.Ipu;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IpuDao</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Vivek Bettada
 * @created Mar 07, 2016
 */
public interface IpuDao extends BaseSubProductDao<Ipu> {
	
	List<String> getIpuQaDataCsv(Timestamp startTs, Timestamp endTs, List<String> ipuPartName, List<String> batteryPartName, boolean isVinRequired);
	List<Object[]> getIpuQaData(Timestamp startTs, Timestamp endTs, String ipuPartName, String batteryPartName);
	List<String> getIpuLetDataCsv(Timestamp startTs, Timestamp endTs, String pp, List<String> ipuPartNames, List<String> batteryPartNames);
	List<Object[]> getIpuLetData(Timestamp startTs, Timestamp endTs, String pp, String ipuPartName, String batteryPartName);
}
