/*
 * Created on Jan 31, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.honda.galc.system.config.web.messages;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

import com.honda.galc.util.PropertyList;


/**
 * <h3>GALCLoggerMessageResources</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This class implements a MessageResources message provider that
 * sits on top the existing GALC Logger class.
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
 * <TD>Jeffray Huang</TD>
 * <TD>Aug 31, 2010</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class MessageProperties extends MessageResources {

	
	private static final long serialVersionUID = 1L;
	
	private static String MESSAGE_PROPERTY_PATH ="Message.properties";
	
	private PropertyList propertyList;
	/**
	 * @param arg0
	 * @param arg1
	 */
	public MessageProperties(MessageResourcesFactory arg0, String arg1) {
		super(arg0, arg1);
		propertyList = new PropertyList(MESSAGE_PROPERTY_PATH);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public MessageProperties(MessageResourcesFactory arg0,
			String configParm, boolean returnNullParm) {
		super(arg0, configParm, returnNullParm);
		
		propertyList = new PropertyList(MESSAGE_PROPERTY_PATH);

	}

	/* (non-Javadoc)
	 * @see org.apache.struts.util.MessageResources#getMessage(java.util.Locale, java.lang.String)
	 */
	public String getMessage(Locale locale, String key) {
		
		String result = propertyList.getProperty(key);
		if(result == null) result="Unknown message key: "+key;
		return result;
		
	}

	public String getMessage(Locale locale, String key, Object[] argList)
	{
		String result = "";
		
		String messageRoot = propertyList.getProperty(key);
			
		result = messageRoot;
			
		if ( (messageRoot != null) && 
			 (messageRoot.indexOf('{') >= 0) )
		{
			// Use the MessageFormat to put the additional information 
			// in the message.
			
			result = MessageFormat.format(messageRoot, argList);
		}
		
		if ((result == null || result.length() == 0) && !key.startsWith("errors."))
		{
		    result="Unknown message key: "+key;
		    
		}
		
		
		return result;
	}


}