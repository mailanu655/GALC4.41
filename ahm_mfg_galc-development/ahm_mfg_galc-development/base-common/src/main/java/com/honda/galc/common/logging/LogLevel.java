package com.honda.galc.common.logging;

import java.io.Serializable;

import org.apache.logging.log4j.Level;

public enum LogLevel implements Serializable {
	TRACE("T","trace", Level.TRACE),
    DEBUG("D","debug", Level.DEBUG),
	GUI("G","gui", Level.forName("GUI", Level.INFO.intLevel() + 75)),
	DATABASE("B","database", Level.forName("DATABASE", Level.INFO.intLevel() + 50)),
    CHECK("C","check", Level.forName("CHECK", Level.INFO.intLevel() + 25)),
    INFO("I","info", Level.INFO),
    WARNING("W","warning", Level.WARN),
    ERROR("E","error", Level.ERROR),
    EMERGENCY("X","emergency", Level.FATAL);
	
    String type;
    String description;
    Level level;
    
    public static final String LOG_LEVEL = "LOG_LEVEL";	
    
    private LogLevel(String type,String description, Level level) {
        this.type = type;
        this.description = description;
        this.level = level;
    }
    
    public String getType(){
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Level getLevel() {
    	return level;
    }
    
    public boolean isHigher(LogLevel level) {
        return level == null ? true :this.ordinal() >= level.ordinal();
    }
  
    public static LogLevel getLogLevel(String description){
        for(LogLevel level:LogLevel.values()){
            if(level.getDescription().equalsIgnoreCase(description)) return level;
        }
        return null;
    }
    
    public static LogLevel getLogLevelFromType(String type) {
        for(LogLevel level:LogLevel.values()){
            if(level.getType().equalsIgnoreCase(type)) return level;
        }
        return null;
    }
}
