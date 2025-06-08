package com.honda.galc.common.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.util.QueueProcessor;

/**
 * 
 * <h3>AbstractFileLogWriter Class description</h3>
 * <p> this class creates log files with the current date as part of log file name</p>
 * <p> This class logs a message string into the specified log file. Every time 
 * when it logs a message, it checks the current timestamp. If the time changes to a
 * new day, it creates a new log file with new date and logs the messages to the new file since.
 *  </p>
 * <p> this class is an abstract class of QueueProcessor. Subclass of this has to implement
 * processItem method to define how to process the object in the queue 
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
 * @author Jeffray Huang
 * Feb 4, 2010
 *
 */

public abstract class AbstractFileLogWriter<T> extends QueueProcessor<T> implements LogWriter<T>{
    protected FileOutputStream fileOut;
    protected final String baseDir;
    protected String currentFileNumber;
    protected String currentFileName;
    protected String currentDirName;
    protected String fileName;
    protected LogLevel logLevel = LogLevel.DEBUG;
    protected static boolean isShiftDirectory = false;
    protected static int [] shiftTimes = null;
    public static String LATEST = "last"; 
    private static Logger logger = Logger.getLogger("AbstractFileLogWriter");
        

/**
 * LogFileWriter constructor comment.
 */
    public AbstractFileLogWriter(String aPath, String aName) {
    	baseDir = aPath;

    	fileName = aName.replace(' ', '_')
                	.replace('-', '_')
                	.replace('\\', '_')
                	.replace('/', '_')
                	.replace('\'', '_');
        openFile();
        start();
    }
    
    public static void setShiftDirectory( boolean value, int [] shiftStartTimes ) {
        isShiftDirectory = value;
    	shiftTimes = shiftStartTimes;
    }
    
    
    protected String getPath() {
    	String path = getBaseDir();
    	if (isShiftDirectory) {
    		path= String.format("%s%tF-%2$ta%s/",path,System.currentTimeMillis(),getShiftLetter());
    	}
    	return path; 
    }
    
    protected String getBaseDir() {
    	String path = baseDir.endsWith("/") ? baseDir : baseDir + '/';
    	return path;
    }
    
    private String getShiftLetter() { 
    	if (shiftTimes == null) {
    		return "";
    	}
    
        Calendar shiftB = new GregorianCalendar();
        shiftB.set(Calendar.HOUR_OF_DAY,shiftTimes[2]);
        shiftB.set(Calendar.MINUTE,shiftTimes[3]);
        shiftB.set(Calendar.SECOND,00);
        
        GregorianCalendar now = new GregorianCalendar();
        if (now.after(shiftB)) {
        	return "-B";
        } else {
        	return "-A";
        }
    }
    
    public AbstractFileLogWriter(String aPath,String aName,LogLevel logLevel) {
        this(aPath,aName);
        this.logLevel = logLevel;
    	log("current log level is " + this.logLevel);
    }
    
    protected void checkForRollover() {

        String newFileNumber = String.format("%tF",System.currentTimeMillis());
        
        if (!newFileNumber.equals(currentFileNumber) || !getPath().equals(currentDirName)) {
        	resetFile();
        }

    }
    
    public void close() {
    	stopRunning();
    	closeFile();
    }
    
/**
 * Insert the method's description here.
 * Creation date: (7/18/01 4:25:40 PM)
 * @return java.lang.String
 */
    protected String getNewFileName() {

    	currentFileNumber = String.format("%tF",System.currentTimeMillis());

    	return (getPath() + fileName + "_" + currentFileNumber + ".log");
    }

    public void log(String aMessage) {

        checkForRollover();
        try {
            if (fileOut == null) {
                resetFile();
            }

            if (fileOut != null) {
                fileOut.write((aMessage + System.getProperty("line.separator")).getBytes());
                fileOut.flush();
            }
        }
        catch (IOException e) {
            logger.error("Error writing to file: " + currentFileName + " " + e);
            resetFile();
        }
    }
    
    private void openFile() {
    	try {
    		currentFileName = getNewFileName();
    		currentDirName = getPath();
    		if (isShiftDirectory) {
     		   String path = LATEST;
    		   File f = new File(getBaseDir(),path);
    		   f.delete();
    		   String  target = getPath().replaceAll(getBaseDir(),"");
    		   target = target.replaceAll("/$", "");
    		   createSymbolicLink(path,target);
    		}
    		
    		new File(currentDirName).mkdirs();
    		fileOut = new FileOutputStream(currentFileName, true);
    	   	log("Opening: " + currentFileName + " on "
                    + new java.sql.Timestamp(System.currentTimeMillis()));
    	} catch (Throwable e) {
    	    logger.error("Error opening file: " + currentFileName + " " + e);
    	}
    }
    
    private void createSymbolicLink(String newlink, String target) throws IOException, InterruptedException {
  	  String os = System.getProperty("os.name");
  	  
  	  if (os.compareToIgnoreCase("Linux") == 0 ||
  		  os.compareToIgnoreCase("AIX") == 0) {
  		  String methodName = "createSymbolicLink";
  		  String command =  String.format("ln -sf %s %s", target, newlink);
  		  File dir = new File(getBaseDir());
  		  logger.info(String.format("%s: Working dir=[%s], command=[%s]",methodName,dir, command));
  		  
          Process p = Runtime.getRuntime().exec(command,null,dir);
          String stderr = readAll(p.getErrorStream());
          String stdout= readAll(p.getInputStream());
          int rc = p.waitFor();
          
  		  logger.info(String.format("%s: <exit status> %d",methodName,rc));
  		  logger.info(String.format("%s: <stderr> %s",methodName,stderr));
  		  logger.info(String.format("%s: <stdout> %s",methodName,stdout));
  	  }
    }
    
    private String readAll(InputStream is) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader ir = new InputStreamReader(is);
        int c;
        while ((c=ir.read()) != -1 ) {
          sb.append((char)c);
        }
        return sb.toString();
    }
    
    private void closeFile() {
        if (fileOut != null) {
            try {
                fileOut.write(("Closing: on " + new Timestamp(System.currentTimeMillis()).toString() + System.getProperty("line.separator")).getBytes());
                fileOut.flush();
            } catch (IOException e) {
                logger.error("Error closing file: " + currentFileName + " " + e);
            } finally {
                if (fileOut != null) {
                   try {
                       fileOut.close();	
                       fileOut = null;
                   } catch (IOException e) {}
            	}
            }
        }
    }
    
    protected void resetFile() {
        closeFile();
        openFile();
    }
}
