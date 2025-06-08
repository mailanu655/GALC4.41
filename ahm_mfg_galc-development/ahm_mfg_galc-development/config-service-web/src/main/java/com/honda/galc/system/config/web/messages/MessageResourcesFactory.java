
package com.honda.galc.system.config.web.messages;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResourcesFactory;

/**
 * <h3></h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Jan 31, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class MessageResourcesFactory extends PropertyMessageResourcesFactory {

	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MessageResourcesFactory() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.util.MessageResourcesFactory#createResources(java.lang.String)
	 */
	public MessageResources createResources(String configParm) {
		return new MessageProperties(this, configParm);
	}

}
