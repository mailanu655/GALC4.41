package com.honda.galc.net;

/**
 * 
 * <h3>ConnectionStatusListener Class description</h3>
 * <p> ConnectionStatusListener description </p>
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
 * Apr 19, 2010
 *
 */
public interface ConnectionStatusListener {
	
	public void statusChanged(ConnectionStatus status);
	
}
