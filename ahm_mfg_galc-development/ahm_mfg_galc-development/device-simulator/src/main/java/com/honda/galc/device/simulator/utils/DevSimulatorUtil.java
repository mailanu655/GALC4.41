package com.honda.galc.device.simulator.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.entity.conf.DeviceFormat;


/**
 * <h3>DevSimulatorUtil</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
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
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.1</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class DevSimulatorUtil {
	
	private final static String LOCALHOST = "localhost";  //  @jve:decl-index=0:	
	private final static String LOCALHOSTIP = "127.0.0.1";  //  @jve:decl-index=0:
	
	
	/**
	 * Get IP for local host
	 * @return
	 */
	public static String getLocalHostIP()
	{
		String sHostIP;
		try{
		    sHostIP = InetAddress.getLocalHost().getHostAddress();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			return null;
		}

        return sHostIP;
	}
	
	/**
	 * Get local host name
	 * @return
	 */
	public static String getLocalHostName()
	{
		String sHostIP;
		try{
		    sHostIP = InetAddress.getLocalHost().getHostName();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			return null;
		}

        return sHostIP;
	}
	
	/**
	 * Check local IP
	 * @param sIP
	 * @return
	 */
	public static boolean isLocalIP(String sIP)
	{	   
		if(sIP.equals(LOCALHOST) || sIP.equals(LOCALHOSTIP) || sIP.equals(getLocalHostIP()))
			return true;
		else
			return false;  
		   
	}
	
	/**
	 * Check Server URL against excluding host list
	 * @param srvUrl
	 * @param exlHosts
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkUrl(String srvUrl, String[] exlHosts, String[]exlIpMasks) throws MalformedURLException
	{
		
		if(!checkExcludesHosts(srvUrl, exlHosts))
			return false;
		
		if(!checkExcludesIp(srvUrl, exlIpMasks))
			return false;
		
		return true;
	}

	private static boolean checkExcludesIp(String srvUrl, String[] exlIpMasks) throws MalformedURLException {
		String ip = getServerUrlIp(srvUrl);

		if (ip == null)	return false;

		for (int i = 0; i < exlIpMasks.length; i++) {
			Pattern pattern = Pattern.compile(exlIpMasks[i].trim());
			Matcher matcher = pattern.matcher(ip);
			if (matcher.matches())
				return false;
		}
		return true;
	}

	private static String getServerUrlIp(String srvUrl) throws MalformedURLException {
		Logger log = Logger.getLogger(DevSimulatorUtil.class);
        String hostip = null;
		
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,6}");
		Matcher matcher = pattern.matcher(srvUrl);
		if(matcher.matches())
		{
			hostip = srvUrl.split(":")[0];
		}
		else
		{
			try {
				URL url = new URL(srvUrl);
				hostip = url.getHost();

			} catch (MalformedURLException e) {
				log.info("Info: Invalid server url: " + srvUrl);
				throw e;
			}
		}
		return hostip;
	}

	private static boolean checkExcludesHosts(String srvUrl, String[] exlHosts) {
		if(exlHosts == null || exlHosts.length == 0)
			return true;
		
		for(int i = 0; i < exlHosts.length; i++)
    	{
    		if(srvUrl.contains(exlHosts[i].trim()))
    			return false;
    	}//for
		
		return true;
	}	
	
	/**
	 * Load properties from resource file
	 * @param resname
	 * @return
	 */
	public Properties loadProperties(String resname)
	{
		Logger log = Logger.getLogger(DevSimulatorUtil.class);
		Properties props = new Properties();
		String resname_org = resname;
		if (!resname.startsWith("/"))
		{
			resname = "/" + resname;

		}

		InputStream is = getClass().getResourceAsStream(resname);

		if (is == null)
		{

			try
			{
				is = new java.io.FileInputStream(resname_org);
			}
			catch (FileNotFoundException e)
			{
				is = null;
			}
		}

		if (is == null)
		{
			log.error("Error loading resouce: " + resname); 
		}

		try
		{
			props.load(is);	
		}
		catch (IOException e)
		{
			e.printStackTrace();
			log.error("Error loading the properties"); 
		}
		finally
		{
			try	{is.close();} catch (Exception e){e.printStackTrace();}
		}
		return props;
	}
	
	/**
	 * replace data container key with device data format tag value
	 * @param devFormat
	 * @param dc
	 * @param mustExist
	 * @return
	 */
	public static DataContainer convertDataContainerKey(List<DeviceFormat> deviceDataFormats,DataContainer dc, boolean mustExist)
	{
		Logger log = Logger.getLogger(DevSimulatorUtil.class);
		
		for(DeviceFormat format : deviceDataFormats) {
			if (format.isStaticType()|| 
					format.getTagType() != SimulatorConstants.NONE_TAG_TYPE) continue;
			String val = (String)dc.remove(format.getTag());
			if(val != null)
				dc.put(format.getTagValue(), val);
			else if(mustExist)
			{
				log.error("Error: null value in data container for key:" + format.getTagValue());
				return null;
			}
			
		}
		return dc;

	}

	
	

}
