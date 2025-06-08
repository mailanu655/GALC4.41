package com.honda.galc.logserver;

import com.honda.galc.common.logging.AbstractFileLogWriter;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;

/**
 * 
 * <h3>FileLogWriter Class description</h3>
 * <p> FileLogWriter is a queue processor of LogRecord object. It takes 
 * LogRecord object from queue and parses the object into a log string and saves
 * the log string into log file. </p>
 * <p> LogRecord objects with the log level lower than specified log level 
 * are filtered out and are not saved into the log file.
 * <p> Each LogRecord can be configured as being saved in a single line, 
 * or multiple lines. With multiple lines, more detailed information including
 * the exception stack trace(if exists) is saved into log file
 * <p> Each line will be truncated update <b>"maxChars"</b> length   
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

public class FileLogWriter extends AbstractFileLogWriter<LogRecord> {
    
    protected boolean isSingleLine = true;
    // maximum characters per line
    protected int maxChars = -1;
    
    public FileLogWriter(String path, String name) {
        super(path, name);
    }
    
    public FileLogWriter(String path,String name,LogLevel logLevel) {
        super(path,name,logLevel);
    }    
    
    public FileLogWriter(String path,String name,boolean isSingleLine) {
        super(path,name);
        this.isSingleLine = isSingleLine;
    }
    
    public FileLogWriter(String path,String name,boolean isSingleLine, int maxChars) {
        super(path,name);
        this.isSingleLine = isSingleLine;
        this.maxChars = maxChars;
    }
    
    
    @Override
    public void processItem(LogRecord item) {
        if(!item.isMultipleLine() || isSingleLine) log(item.getSingleLineMessage(maxChars));
        else log(item.getMultiLineMessage());
    }
    
    public void log(LogRecord item) {
        if(item.needLog(logLevel))
            enqueue(item);
    }

}
