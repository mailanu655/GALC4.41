package com.honda.galc.common.exception;


/**
 * <h3>TaskException is thrown when Task want to rollback transaction.</h3>
 * <h4>Description</h4>
 * Each Task operates each business logic, and Task knows need to rollback transaction at Business Logic.
 * So if Task want to rollback transaction, Task have to throw TaskException.
 * <P>
 * These exceptions are thrown as the result of the operation for Task.
 * A criterion of the throwing exception is when the process which
 * Task should do is not completed correctly.
 * Concretely, except when exceptions are accrued in the method of Task
 * or when process exceptions at Catch section and enabled to keep processing correctly,
 * throw uniformly TaskException.
 * When there is no exception but disabled to satisfy the application demand
 * (when SQL) it is thrown.<Br>
 * As for RuntimeException, Normally, it is programming error, so after processing
 * RuntimeException, enabled to throw it as TaskException.<Br>
 * When Catch SystemException by using System Function in Task, throw SystemException.<Br>
 * Controller which Catches TaskException return to the right befer input event Task.
 * <h4>Usage and Example</h4>
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
 * <TD>K.Sone</TD>
 * <TD>(2001/03/01 20:25:00)</TD>
 * <TD>0.1.0</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/11/01 20:00:00)</TD>
 * <TD>0.1.0</TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 3, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006</TD>
 * <TD>Added support for additional information field.</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 18, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006.1</TD>
 * <TD>Add serialization ID</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1.0
 * @author K.Sone
 */
public class TaskException extends BaseException {
	
	public static final long serialVersionUID = -2102308873405014858L;  // @JM006.1

	private String taskName = null;

    /**
     * Construct TaskException.
     */
    public TaskException() {
        super(null);
    }
    /**
     * Construct TaskException which has the specified message ID.
     * @param aMessageID Message ID
     */
    public TaskException(String message) {
        super(message);  // @JM006
    }
    
    public TaskException(String message,String taskName) {
        this(message);
        this.taskName = taskName;
    }
    
    /**
     * Construct TaskException which has the message ID and the nested exception. 
     * @param aMessageID Message ID
     * @param aDetail Nested exception
     */
    public TaskException(String message, Throwable aDetail) {
        super(message, aDetail);
    }
    
    
    /**
     * Construct TaskException which has the message ID, the nested exception,
     * and Task name of which accrues the exception. 
     * @param aMessageID Message ID
     * @param aDetail Nested exception
     * @param aTaskName Task name
     */
    public TaskException(String message, Throwable aDetail, String aTaskName) {
        super(message, aDetail);
        taskName = aTaskName;
    }
    
    public TaskException(String message, String aTaskName,Throwable aDetail) {
        super(message, aDetail);
        taskName = aTaskName;
    }
    /**
     * Get the Task name of which accrues the exception. 
     * @return Task name of which accrues the exception
     */
    public String getTaskName() {
        return taskName;
    }
    
    public String getAdditionalInformation() {
        return getTaskName();
    }
}
