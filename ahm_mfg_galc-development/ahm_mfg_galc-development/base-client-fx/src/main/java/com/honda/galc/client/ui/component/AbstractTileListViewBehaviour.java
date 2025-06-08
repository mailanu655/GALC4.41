package com.honda.galc.client.ui.component;
/**
 * <h3>AbstractTileListViewBehaviour Class description</h3>
 * <p> AbstractTileListViewBehaviour is an Abstract class to set behaviour and add selection listener to the control </p>
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
 * @author L&T Infotech<br>
 * Mar 31, 2017
 *
 */
public abstract class AbstractTileListViewBehaviour<T extends Object> {
	
	public abstract void setBehaviour(T item);
	public abstract void addListener(T item);
	
}
