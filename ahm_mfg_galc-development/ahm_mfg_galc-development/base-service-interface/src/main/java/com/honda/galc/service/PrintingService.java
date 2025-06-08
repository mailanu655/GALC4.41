package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>TrackingService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TrackingService description </p>
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
 * @author Jeffray Huang
 * Oct 25, 2010
 *
 */

public interface PrintingService extends IService{
	
	public void print(String queueName, String formId,DataContainer dataContainer);
	
	public byte[] generateJasperReport(DataContainer dc);
}
