package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;

/**
 * 
 * <h3>IoService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IoService description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 7, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 7, 2012
 */

public interface IoService extends IService{
	
	/**
	 * execute service request from IO device
	 * @param device
	 * @return
	 */
	Device execute(Device device);
	
	/**
	 * execute service request from IO device in the format of data container
	 * This is added for backwards compatible with existing GALC
	 * @param data
	 * @return
	 */
	DataContainer execute(DataContainer data);
}
