/*
 * Created on Aug 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.honda.galc.client.common.component;

import java.awt.Color;

/**
 * <h3>GALCMessageArea</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>This interface defines the methods that a message area field 
 * should support to be used with a client side screen.</p>
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
 * <TD>martinek</TD>
 * <TD>Aug 3, 2004</TD>
 * <TD>insert version</TD>
 * <TD>@JM007</TD>
 * <TD>Initial release.</TD>
 * </TR>
 * </TABLE>
 * 
 */
public interface GALCMessageArea
{
	/**
	 * This method will update the text of the message area.
	 * @param MessageStr
	 */
	public void setErrorMessageArea(String MessageStr);
	
	/**
	 * This method will update the color and text of the
	 * message field.
	 * @param strMessage
	 * @param color
	 */
	public void setErrorMessageArea(String strMessage, Color color);

}
