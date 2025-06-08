package com.honda.galc.common.exception;

/**
 * 
 * <h3>ReflectionException Class description</h3>
 * <p> ReflectionException description </p>
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
 * May 2, 2011
 *
 *
 */
public class ReflectionException extends SystemException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReflectionException(String aMessage) {
		super(aMessage);
	}

	
	public ReflectionException(String aMessage, Throwable aDetail) {
		super(aMessage, aDetail);
	}


}
