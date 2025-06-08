package com.honda.galc.service.broadcast;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>IPrintDataAssembler Class description</h3>
 * <p> IPrintDataAssembler description </p>
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
 * @author Jeffray Huang<br>
 * Aug 26, 2013
 *
 *
 */
public interface IPrintDataAssembler {
	public byte[] assembleData(DataContainer dc);
}
