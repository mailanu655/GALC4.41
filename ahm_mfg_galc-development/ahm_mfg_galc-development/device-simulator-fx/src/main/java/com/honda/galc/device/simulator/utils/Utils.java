/**
 * 
 */
package com.honda.galc.device.simulator.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.UUID;

import com.honda.galc.util.StringUtil;

/**
 * 
 * @author Subu Kathiresan
 *
 */
public class Utils {
	public static final long ONE_DAY_IN_MILLISECS = 86400000;
	
	public static final String NEW_LINE = System.getProperty("line.separator"); 	
	public static final String SUITES_DIR = "suites" + File.separator;
	public static final String CONTROL_FILES_DIR = "controlfiles//";
	public static final String LOG_FILES_DIR = "d:/logs" + File.separator;
	public static final String METRICS_FILES_DIR = "d:/logs" + File.separator + "metrics" + File.separator;
	
	public static char nullValue = 0;

	
	public static void writeMessageToOutStream(PrintWriter pOut, String sendMsg, String direction) {
        pOut.print(sendMsg + nullValue);
        pOut.flush();
        System.out.println("Sending message: " + direction + "   " + sendMsg + direction);
	}	
	
	/**
	 * Writes the prepared message on the client socket
	 * 
	 * @param sendMsg
	 */
	public static void writeMessageToOutStream(FileWriter fOut, PrintWriter pOut, String sendMsg, String direction) {
		Logger.log(fOut, direction + "   " + sendMsg + direction);
		
        pOut.print(sendMsg + nullValue);
        pOut.flush();
	}	
	
	/**
	 * 
	 * @param mask
	 * @return
	 */
	public static String replaceMaskWithRandomNumber(String mask)
	{
		StringBuffer strBuf = new StringBuffer();
		Random rndGenerator = new Random();
		
		for(char replaceChar: mask.toCharArray())
		{
			if (replaceChar == '#')
				strBuf.append(new Integer(rndGenerator.nextInt(10)).toString());
			else
				strBuf.append(replaceChar);				
		}
		return strBuf.toString();
	}
	
	/**
	 * Creates a URLConnection to the Application Server specified at the
	 * AppServer URL 
	 * 
	 * @param AppServerUrl	The AppServer url to connect to
	 * @return				A live URLConnection 
	 * @throws IOException	Thrown if there was a problem with creating the URLConnection
	 */
	public static URLConnection connectToAppServer(URL AppServerUrl) throws IOException 
	{
		URLConnection urlConnection = AppServerUrl.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setUseCaches(false);
		
		return urlConnection;
	}
	
	/**
	 * 
	 * @param part
	 * @param partMask 
	 * @return
	 */
	public static String generateSerialNumber(FileWriter fOut, String partMask)
	{
		partMask = Utils.replaceMaskWithRandomNumber(partMask);		
		partMask = partMask.replace("*", UUID.randomUUID().toString());
		partMask = partMask.replace("-", "");
		partMask = StringUtil.padRight(partMask, 15, ' ', true);		// max length of serial number is 30
		
		Logger.log(fOut, "Generated serial number " + partMask);
		return partMask;
	}
	
	public static boolean isEmpty(String text) {
		return text == null || text.length() ==0;
	}
	
	public static Boolean toBoolean(String text) {
		return text == null ? null : 
			text.equalsIgnoreCase("TRUE") || text.equalsIgnoreCase("YES") || text.equalsIgnoreCase("Y");
	}
	
	public static Boolean toBoolean(String text, boolean defaultValue) {
		Boolean value = toBoolean(text);
		return value == null ? defaultValue : value;
	}

	public static String getPath(String path) {
		return path;
	}
}


