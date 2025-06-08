package com.honda.galc.test.common;

import java.sql.Timestamp;
import java.util.logging.SimpleFormatter;

public class LogFormatter extends SimpleFormatter {
    
    @Override
    public String format(java.util.logging.LogRecord record) {
        StringBuilder aBuilder = new StringBuilder();
        aBuilder.append(new Timestamp(record.getMillis()));
        aBuilder.append(" ");
        aBuilder.append(record.getLevel().getName());
        aBuilder.append(" [");
        aBuilder.append(record.getLoggerName());
        aBuilder.append("] ");
        aBuilder.append(record.getMessage());
        aBuilder.append('\n');
        return aBuilder.toString();
    }
    

}
