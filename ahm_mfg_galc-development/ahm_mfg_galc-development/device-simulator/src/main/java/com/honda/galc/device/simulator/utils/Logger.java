/**
 * 
 */
package com.honda.galc.device.simulator.utils;

import java.io.FileWriter;


/**
 * @author Kathiresan Subu
 *
 */
public class Logger
{
	private static FileWriter _fOut = null;
	public static final String LOG_FILE_NAME = "GALC_Simulator.log";
	private static final String PADDING = "                                                       ";
	private static int indent = 0;
	private static int indentIncrement = 4;
	
	static
	{
		try
		{
			_fOut = new FileWriter(Utils.LOG_FILES_DIR + LOG_FILE_NAME, true);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}	
	
	public static void indent() {
		indent+=indentIncrement;
	}
	
	public static void unindent() {
		indent-= indentIncrement;
		if (indent < 0) {
			indent = 0;
		}
	}	
	
	/**
	 * 
	 */
	public synchronized static void log(String message)
	{
		log(_fOut, message);
	}
	
	/**
	 * 
	 */
	public synchronized static void log(FileWriter fOut, String message)
	{
		try
		{
			message = String.format("%1$tH:%1$tM:%1$tS:%tL %s%s%s",System.currentTimeMillis(),PADDING.substring(0,indent),message, System.getProperty("line.separator"));				
			System.out.print(message);
			fOut.write(message);
			fOut.flush();
		}
		catch(Exception ex)
		{
	    	System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static FileWriter getLogger()
	{
		return _fOut;
	}
}
