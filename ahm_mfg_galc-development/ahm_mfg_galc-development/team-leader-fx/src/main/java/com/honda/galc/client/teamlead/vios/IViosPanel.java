package com.honda.galc.client.teamlead.vios;

import javafx.stage.Stage;
/**
 * <h3>IViosPanel Class description</h3>
 * <p>
 * Interface for Vios Panel
 * </p>
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public interface IViosPanel {
	
	public void setErrorMessage(String msg);
	
	public void clearErrorMessage();
	
	public Stage getStage();
	
	public void setInfoMessage(String msg);
	
}
