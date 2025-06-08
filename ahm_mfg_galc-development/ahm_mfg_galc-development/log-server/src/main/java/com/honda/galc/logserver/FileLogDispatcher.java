package com.honda.galc.logserver;

import java.util.HashMap;
import java.util.Map;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.LogWriter;
import com.honda.galc.util.QueueProcessor;

public class FileLogDispatcher extends QueueProcessor<LogRecord> implements LogWriter<LogRecord>{

    
    private Map<String,FileLogWriter> writers = new HashMap<String,FileLogWriter>();
    private String filePath;
    
    public FileLogDispatcher(String filePath){
        this.filePath = filePath;
        start();
    }
    
    @Override
    public void processItem(LogRecord item) {
        String appName = item.getLogContext().getApplicationName();
        if (appName == null) {
        	appName =  "NULL-APPNAME";
        	System.err.println("ERROR - A message was received from an application with a null application name. Defaulting the name to NULL-APPNAME");
        }
        if(!writers.containsKey(appName)) createWriter(appName);
        FileLogWriter writer = writers.get(appName);
        if(writer != null) writer.log(item);
    }
    
    public void close() {
       for(FileLogWriter writer : writers.values())
           writer.close();
    }
    
    public void log(LogRecord item) {
        enqueue(item);
    }
    
    private void createWriter(String appName) {
        FileLogWriter writer = new FileLogWriter(filePath,appName,false);
        writers.put(appName, writer);
    }
}