package com.honda.galc.system.config.web.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.net.URLEncoder;


import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.service.property.PropertyService;
import com.ibm.ejs.ras.RasHelper;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P></P>
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
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Sep 22, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL058</TD>
 * <TD>ENGINE-98 GALC Database Properties - issues with Refresh and Saving</TD>
 * </TR>
 * 
 * </TABLE>
 */
public class MultiServerPropertyRefresher {

	
	private static final String REFRESH_PROPERTIES_URLS_COMP_ID = "PROPERTY_REFRESH_URLS";
	private static final String COMPONENT_REFRESH="BaseWeb/RefreshProperties?componentID=";

	/**
	 * Private constructor to make the class utility
	 */
	private MultiServerPropertyRefresher() {
		// Private constructor to make the class utility
	}

	/**
	 * Access the URL to run associated action
	 * 
	 * @param argUrl - URL
	 * @throws GALCException - if anything goes wrong
	 * 
	 */
	private static void getPage(String argUrl) {
			
			try {
				URL url = new URL(argUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				int responseCode = connection.getResponseCode();
				if(responseCode != HttpURLConnection.HTTP_OK) {
					throw new ServiceException("Bad Response from : " +   argUrl + ". Response:" + responseCode);
				}
			} catch (MalformedURLException e) {
				throw new SystemException("Wrong URL: " + String.valueOf(argUrl), e);
			} catch (IOException e) {
				throw new SystemException("Error fetching URL: " + String.valueOf(argUrl), e);
			}
			
	}
	
	/**
	 * Refresh component properties on counter party server in cluster
	 * 
	 * @param componentId - properties component ID
	 * @return the name of counter party server in cluster (if found). <b>null</b> if the server is not found
	 * @throws GALCException - if anything goes wrong
	 * 
	 */
	public static boolean refreshOtherNodeProperties(String componentId){
		try {
			
			List<String> urls = getUrlNames();
			
			// componentId may have space chars , which needs to be encoded
			for(String url : urls) {
				getPage(url + URLEncoder.encode(componentId,"UTF-8"));
			}
			
			//double refresh
			Collection<String> serverUrls = PropertyService.getServerUrls().values();
			for(String shortUrl: serverUrls){
				getPage(shortUrl + COMPONENT_REFRESH + URLEncoder.encode(componentId,"UTF-8"));
			}
			
		} catch (Exception e) {
			// do nothing - it's a normal case when properties are not defined
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	private static String getShortServerName() {
		
		String shortServerName = "";
		try {

			// check for RasHelper available
			Class.forName("com.ibm.ejs.ras.RasHelper");

			// Query IBM WebSphere RasHelper whether it runs in server
			// RasHelper.getServerName() does not return null value
			String[] elems = RasHelper.getServerName().split("\\\\");
			if (elems != null && elems.length > 0) {
				shortServerName = elems[elems.length - 1];
			} else {
				shortServerName = "";
			}

		} catch (ClassNotFoundException e) {
			// Could not find RasHelper - cannot be running on server
			throw new SystemException("Could not find Server Name from RasHelper");
			
		}
		
		return shortServerName;
	}
	
	private static List<String> getUrlNames() {
		
		List<String> urls = new ArrayList<String>();
		
		String server = getShortServerName();
		
		if(StringUtils.isEmpty(server)) return urls;
		
	    String servers = PropertyService.getProperty(REFRESH_PROPERTIES_URLS_COMP_ID,server);
		
		if(StringUtils.isEmpty(servers)) return urls;
		
		String[] elems = servers.split("\\s*,\\s*");
		
		if(elems == null || elems.length <=0) return urls;
		
		for(String item : elems) {
			String url = PropertyService.getProperty(REFRESH_PROPERTIES_URLS_COMP_ID,item);
			urls.add(url);
		}
		
		return urls;
		
	}

}
