package com.honda.galc.common.exception;

/**
 * <h3>SystemException is the common superclass for CoreFunctions of GALC.</h3>
 * <h4>Description</h4>
 * This class is the general class of CoreFunctions of GALC.<Br>
 * SystemException is common super class for com.honda.global.galc.system.* below  each package.<Br>
 * An exception which exctends SystemException are thrown when the CoreFunctions of GALC APIs are executed.<Br>
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
 * <TD>K.Sone</TD>
 * <TD>(2001/03/01 13:25:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/10/29 13:00:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Add Javadoc</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 12, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006</TD>
 * <TD>Support for addition information field</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 18, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006.1</TD>
 * <TD>Idiot!  Add serialization ID!</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Sep 20, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM016</TD>
 * <TD>Add additional info+detail constructor.</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author K.Sone
 */
public class SystemException extends BaseException {

	public static final long serialVersionUID = -1994631066864868318L; // @JM006.1
	
	/**
	 * Constructs a SystemException with the specified MessageID and null as its nested exception.
	 * @param aMessageID Message ID
	 */
	public SystemException(String aMessageID) {
		super(aMessageID);
	}
	/**
	 * Constructs a SystemException with the specified MessageID and null as its nested exception.
	 * @param aMessageID Message ID
	 * @param aDetail Nested exception
	 */
	public SystemException(String aMessageID, Throwable aDetail) {
		super(aMessageID, aDetail);
	}
	
	public String getAdditionalInformation() {
        return getMessage();
    }
}
