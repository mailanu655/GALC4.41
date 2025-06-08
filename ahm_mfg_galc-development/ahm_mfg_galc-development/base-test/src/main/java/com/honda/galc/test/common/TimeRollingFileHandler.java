package com.honda.galc.test.common;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.ErrorManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * 
 * <h3>Time Rolling File Handler</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is a Java Logging File Handler that will roll log files 
 * at specific hour in the day. Its a fairly simple file handler that
 * only handles file rolling and not much of anything else at this point.</p>
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
 * <TD>martinek</TD>
 * <TD>Sep 27, 2007</TD>
 * <TD></TD>
 * <TD>@JM200736</TD>
 * <TD>Added for OPC</TD>
 * </TR>
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>May 09, 2008</TD>
 * <TD>1.1</TD>
 * <TD>@GY20080509</TD>
 * <TD>Corrected month number in file name.</TD>
 * </TR>

 * <TR>
 * <TD>R. Lasenko</TD>
 * <TD>June 20, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL029</TD>
 * <TD>Adjusted error reports in nextFile()</TD>
 * </TR>
 * </TABLE>
 */
public class TimeRollingFileHandler extends StreamHandler {

	private BufferedOutputStream bos = null;
	private String baseFileName = null;
	private String currentFileName = null;
	private int rollHour = 0;
	private java.util.Calendar rollCalendar = null;
	
	private static final long DAY_IN_MILLIS = 24*60*60*1000;
	


	/**
	 * The standard construction for the TimeRollingFileHandler.
	 * @param baseFileName - A base file name for the log file.  When rolled, the 
	 * date and time will be appended to the file.
	 * @param rollHour -  A value 0-23 that indicates time log should be rolled.
	 * @throws IOException
	 */
	public TimeRollingFileHandler(String baseFileName, int rollHour) throws IOException {
		
		super();
		
		if (rollHour < 0) {
			rollHour = 0;
		}
		
		if (rollHour > 23) {
			rollHour = 0;
		}
		
		this.rollHour = rollHour;
		this.baseFileName = baseFileName;
		
		java.util.Calendar currentCalendar = new java.util.GregorianCalendar();
		
		int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

		rollCalendar = new java.util.GregorianCalendar();
		rollCalendar.set(Calendar.HOUR_OF_DAY, rollHour);
		rollCalendar.set(Calendar.MINUTE,0);
		rollCalendar.set(Calendar.SECOND,0);
		rollCalendar.set(Calendar.MILLISECOND, 0);
		rollCalendar.getTimeInMillis();
		
		
	
		//System.out.println("Roll at: "+rollCalendar.getTimeInMillis());
		
		
		
		if (rollHour <= currentHour) {
			// We are past the roll hour, so set to the next day
		    long millis = rollCalendar.getTimeInMillis();	
		    rollCalendar.setTimeInMillis(millis+DAY_IN_MILLIS);
		    rollCalendar.getTimeInMillis();
		    
		    //System.out.println("Next day Roll at: "+rollCalendar.getTimeInMillis());
			
		}
		
		// Initialize the file
		nextFile();
	}
	
	/**
    * Format and publish a <tt>LogRecord</tt>.
    *
    * @param  record  description of the log event
    */
   public synchronized void publish(LogRecord record) {
	if (!isLoggable(record)) {
	    return;
	}
	
	super.publish(record);
	flush();
	
	// See if we need to advance file
	try {
	   nextFile();
	}
	catch (IOException e) {
		reportError("", e, ErrorManager.OPEN_FAILURE);
	}
	
   }
   
   /**
    * Close all the files.
    *
    * @exception  SecurityException  if a security manager exists and if
    *             the caller does not have <tt>LoggingPermission("control")</tt>.
    */
   public synchronized void close() throws SecurityException {
	  super.close();
	  bos = null;
	  
	  
   }
	
	private synchronized void nextFile() throws IOException {
		
		Calendar currentCalendar = new GregorianCalendar();
		boolean roll = false;
		
		//System.out.println("\nCurrent calendar: "+currentCalendar.toString());
		//System.out.println("Roll calendar: "+rollCalendar.toString());
		//System.out.println(currentCalendar.after(rollCalendar));
		
		if (bos == null || currentCalendar.after(rollCalendar)) {
			
			//System.out.println("Rolling calendar");
			// Need to create new file
			if (bos != null) {
				roll = true;
				
				try {
				   bos.flush();
				}
				catch (Exception e) {
					reportError("",e,ErrorManager.FLUSH_FAILURE);
				}
				try {
				  bos.close();
				}
				catch (Exception e) {
					reportError("", e, ErrorManager.CLOSE_FAILURE);
				}
				bos = null;
			}
			else
			{
				roll = false;
			}
			
			try {
			   FileOutputStream fos = new FileOutputStream(buildFileName(),true);
			   bos = new BufferedOutputStream(fos);
			}
			catch (IOException e) {
				reportError("", e, ErrorManager.OPEN_FAILURE);
				bos = null;
				// setOutputStream(null);
				throw e;
			}
			
			setOutputStream(bos);
			
			if (roll) {
			   long millis1 = rollCalendar.getTimeInMillis();
			   rollCalendar.add(Calendar.DAY_OF_MONTH,1);
			   long millis2 = rollCalendar.getTimeInMillis();
			   //System.out.println("New roll: "+rollCalendar.toString());
			   //System.out.println(millis2 - millis1);
			   
			}
			
		}
		
		
	}
	
	public String buildFileName() {
		
		StringBuffer result = new StringBuffer(this.baseFileName);
		Calendar timestampCalendar = new GregorianCalendar();
		result.append(".");
		result.append(timestampCalendar.get(Calendar.YEAR));
		result.append("_");
		int month = timestampCalendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			result.append("0");
		}
		result.append(month);
		result.append("_");
		int dayOfMonth=timestampCalendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth < 10) {
			result.append("0");
		}
		result.append(dayOfMonth);
		result.append("__");
		int hour=timestampCalendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 10) {
			result.append("0");
		}
		result.append(hour);
		result.append("_");
		int minute = timestampCalendar.get(Calendar.MINUTE);
		if (minute < 10) {
			result.append("0");
		}
		result.append(minute);
		result.append("_");
		int second = timestampCalendar.get(Calendar.SECOND);
		if (second < 10) {
			result.append("0");
		}
		result.append(second);
		result.append("_");
		int millis = timestampCalendar.get(Calendar.MILLISECOND);
		if (millis < 10) {
			result.append("00");
		}
		else if (millis < 100) {
			result.append("0");
		}
		result.append(millis);
		result.append(".log");
		
		
		
		return result.toString();
		
	}
	
	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger("com.honda.test");
		logger.setUseParentHandlers(false);
		
		try {
		   TimeRollingFileHandler handler = new TimeRollingFileHandler("\\Temp\\myfile",20);
//		   handler.setFormatter(new JavaLoggingFormatter());
		   logger.addHandler(handler);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
			return;
		}
		
		for (int i = 0; i <20; i++) {
			
			logger.severe("Problem at "+System.currentTimeMillis());
			
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
