package com.honda.galc.common.exception;
/**
 * <h3>PropertyException</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> This is the exception thrown by the property loader classes.</p>
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
 * <TD>J. Martinek</TD>
 * <TD>October 10th, 2004</TD>
 * <TD>insert version</TD>
 * <TD>@OIM282</TD>
 * <TD>Initial release.</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 18, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006.1</TD>
 * <TD>Add serialization ID</TD>
 * </TR>
 * </TABLE>
 *  
 * @modelguid {B2229B7D-996F-41ED-871B-8F8D8929EC55}
 */
public class PropertyException extends SystemException {

	private static final long serialVersionUID = -6441027147632745090L;

	/**
	 * Constructs a PropertyException with the specified Message and a nested exception
	 * with the same message.
	 * @param aMessage Message
	 */
	public PropertyException(String aMessage) {
		this(aMessage, new Exception(aMessage));
	}
	
	/**
	 * Constructs a PropertyException with the specified Message and null as its nested exception.
	 * @param aMessage Message
	 * @param aThrowable Nested exception
	 */
	public PropertyException(String aMessage, Throwable aThrowable) {
		super(aMessage, aThrowable);
	}
	
	public PropertyException(String aMessageID, String messageDetails)
	{
		
		this(aMessageID, new Exception(messageDetails));
		
			
	}	
}

