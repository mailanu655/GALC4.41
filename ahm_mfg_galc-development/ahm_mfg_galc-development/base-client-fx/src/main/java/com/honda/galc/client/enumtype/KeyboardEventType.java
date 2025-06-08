package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * <h3>Class description</h3>
 * KeyboardEventType Class Description
 * Event Types used by the Virtual Keyboard. 
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
 * @author L&T Infotech
 */
public enum KeyboardEventType implements IdEnum<ProductEventType> {

	SHOW_KEYBOARD   		    (0),
	HIDE_KEYBOARD	    		(1);

	private final int id;


	private KeyboardEventType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static KeyboardEventType getType(int id) {
		return EnumUtil.getType(KeyboardEventType.class, id);
	}
}

