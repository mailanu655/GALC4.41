/**
 * 
 */
package com.honda.galc.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @Date Apr 19, 2012
 *
 */
public class GalcStation {
	
	public static final String HOSTNAME_CMD = "hostname";

	private static volatile String hostName = "";
	private static volatile String localIp ="";
	
	/**
	 * returns the host name of the GALC station
	 * @return hostName
	 */
	public static synchronized String getHostName(String clientName, String logName) {
		try {
			getHostName();
			Logger.getLogger(logName).info(String.format("Host name retrieved for client %s is: ", clientName) + hostName);
		} catch (Exception ex) {
				ex.printStackTrace();
				Logger.getLogger(logName).error(String.format("Unable to retrieve Host name for client %s: ", clientName) 
						+ (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return hostName;
	}
	
	public static synchronized String getLocalIpAddress(String clientName, String logName) {
		if(StringUtils.isEmpty(localIp)) {
				localIp = fetchLocalIpAddress(clientName,logName);
				Logger.getLogger(logName).info(String.format("ip address retrieved for client %s is: ", clientName) + localIp);
		}
		return localIp;
	}
	
	/**
	 * This function uses the Network Interface API to get an address in
	 * both Windows and Linux. InetAddress looks at the /etc/hosts file
	 * in linux, which only contains 127.0.0.1.
	 * @return Returns the IPv4 address of the Client Host
	 */
	private static synchronized String fetchLocalIpAddress(String clientName, String logName) {
		NetworkInterface iface = null;
		try{
			for(Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();){
	    		iface = (NetworkInterface)ifaces.nextElement();
	    		InetAddress ia = null;
	    		for(Enumeration<InetAddress> ips = iface.getInetAddresses();ips.hasMoreElements();){
	    			ia = (InetAddress)ips.nextElement();
	    			if(!ia.isLoopbackAddress() && (!ia.isLinkLocalAddress()) && (ia instanceof Inet4Address)){
	    				return ia.getHostAddress();
	    			}
	    		}
	    	}
		}catch(Exception ex){
			ex.printStackTrace();
			Logger.getLogger(logName).error(String.format("Unable to retrieve local ip ddress for client %s: ", clientName) 
					+ (ex.getMessage() != null ? ex.getMessage() : ""));
		}
        return null;
	}
	
	/**
	 * returns the host name of the GALC station without logging
	 * @return hostName
	 */
	public static synchronized String getHostName() throws Exception {
		if (hostName.equals("")) { 
			Process process = Runtime.getRuntime().exec(HOSTNAME_CMD);
			byte[] byteArray = new byte[1024];		// hostname is not expected to be more than 1024 chars
			process.getInputStream().read(byteArray);
			hostName = new String(byteArray).trim();
			process.destroy();
		}
		return hostName;
	}
	
	public static boolean isPingable(String hostName) {
	    try {
	        InetAddress address = InetAddress.getByName(hostName);
            return address.isReachable(3000);
		} catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
}
