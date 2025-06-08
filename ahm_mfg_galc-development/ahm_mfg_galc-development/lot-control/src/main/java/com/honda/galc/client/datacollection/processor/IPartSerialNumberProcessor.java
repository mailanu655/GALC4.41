package com.honda.galc.client.datacollection.processor;

import com.honda.galc.device.dataformat.PartSerialNumber;
/**
 * 
 * <h3>IPartSerialNumberProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IPartSerialNumberProcessor description </p>
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
 * @author Paul Chou
 * Apr 1, 2010
 *
 */
public interface IPartSerialNumberProcessor extends
		IDataCollectionTaskProcessor<PartSerialNumber>{
	
	public boolean execute(PartSerialNumber partSerialNumber);

}
