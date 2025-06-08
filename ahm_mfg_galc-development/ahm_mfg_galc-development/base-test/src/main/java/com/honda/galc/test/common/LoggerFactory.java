package com.honda.galc.test.common;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.honda.galc.test.db.SystemPropertyManager;

public class LoggerFactory {
    
    
    public static Logger createLogger(String name) {
    
        Logger logger = Logger.getLogger(name);
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);
        
        String fileName = SystemPropertyManager.getProperty("ClientDir") +"/logs/GALCTest";
            
        Handler handler;
        try {
            handler = new DailyRollingFileHandler(fileName,0);
            handler.setFormatter(new LogFormatter());
            handler.setLevel(Level.INFO);
            logger.addHandler(handler);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return logger;
    }
}
