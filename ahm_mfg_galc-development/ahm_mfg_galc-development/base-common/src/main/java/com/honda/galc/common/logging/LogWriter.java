package com.honda.galc.common.logging;

/**
 * 
 * <h3>LogWriter Class description</h3>
 * <p> LogWriter class is an interface class </p>
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
 * Feb 4, 2010
 *
 */
public interface LogWriter<T> {
    public void log(T item);
    public void close();
}
