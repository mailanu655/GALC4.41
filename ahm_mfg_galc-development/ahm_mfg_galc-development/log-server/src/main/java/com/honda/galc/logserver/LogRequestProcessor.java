package com.honda.galc.logserver;
import java.util.ArrayList;
import java.util.List;


import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.LogWriter;
import com.honda.galc.util.QueueProcessor;

/**
 * <h3>Class description</h3>
 * This class is responsible for handling log requests in
 * a waiting list (queue). It uses LogFileWriter to write log 
 * requests to files.
 * <h4>Description</h4>
 * 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Aug 2, 2007</TD>
 * <TD>1.0</TD>
 * <TD>GY20070802</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * <br>
 * @ver 1.0
 * @author Jeffray Huang
 */

public class LogRequestProcessor extends QueueProcessor<LogRecord> {
	private LogServerConfig logServerConfig;
	private List<LogWriter> writers = new ArrayList<LogWriter>();
	
	
	public LogRequestProcessor(LogServerConfig logServerConfig) {
	    super();
	    this.logServerConfig = logServerConfig;
	    initialWriters();
	    
	}
	
	private void initialWriters(){
	    // center log
		if(logServerConfig.isCenterLogging()){
		    writers.add(new CenterFileLogWriter(
		                    logServerConfig.getLogFilePath(),
		                    logServerConfig.getCenterLogName(),
		                    logServerConfig.getCenterLogRolloverTimes(),
		                    LogLevel.getLogLevel(logServerConfig.getCenterLogLevel())));
		}
	    // tivoli log
	    writers.add(new FileLogWriter(
                        logServerConfig.getLogFilePath(),
                        logServerConfig.getTivoliLogName(),
                        LogLevel.getLogLevel(logServerConfig.getTivoliLogLevel())));
	    
	    if(logServerConfig.isLogPerApp())
	        writers.add(new FileLogDispatcher(logServerConfig.getLogFilePath()));
 	}
	
	public void processItem(LogRecord item) {
	    for(LogWriter<LogRecord> writer : writers)
	        writer.log(item);
	}
	
	public void stopRunning() {
		super.stopRunning();
		
	      for(LogWriter writer : writers)
	            writer.close();

	}
}
