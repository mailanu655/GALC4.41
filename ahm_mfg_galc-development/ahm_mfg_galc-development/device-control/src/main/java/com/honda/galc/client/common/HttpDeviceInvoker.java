package com.honda.galc.client.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.AbstractRequestInvoker;
import com.honda.galc.net.ServiceMonitor;

/**
 * 
 * <h3>HttpDeviceInvoker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HttpDeviceInvoker description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jul 25, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jul 25, 2012
 */
public class HttpDeviceInvoker extends AbstractRequestInvoker<Device>{

	private HttpURLConnection urlConnection = null;
	private String url;

	public HttpDeviceInvoker(String url) {
		super();
		this.url = url;
	}


	@Override
	public ObjectInputStream getInputStream() throws IOException {
		return new ObjectInputStream(urlConnection.getInputStream());
	}

	@Override
	public ObjectOutputStream getOutputStream() throws IOException {
		return new ObjectOutputStream(urlConnection.getOutputStream());
	}

	@Override
	public String getServerAddress() {
		return url;
	}

	public void initConnection() throws ServiceTimeoutException{
		try {
			URL hostUrl = new URL(url);
			urlConnection = (HttpURLConnection)hostUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setReadTimeout(0);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
		} catch (Exception e) {
			throw createServiceTimeoutException(e);
		}    
	}

	@Override
	public Device invoke (Device device){
		try{
			Device returnDev = (Device)super.invoke(device);
			if(returnDev == null){
				Logger.getLogger().error("Error occured on server, return null from server.");
				returnDev = device;
				returnDev.addException("Error occured on server.");
			}
			
			return returnDev;
		}catch(ServiceTimeoutException e) {
			ServiceMonitor.getInstance().startMonitorHttpService();
			Logger.getLogger().error(e, "Exception to call application server for device:", device.getClientId());
			device.addException(e.getMessage());
		}catch(Throwable t){
			Logger.getLogger().error(t, "Exception to call application server for device:", device.getClientId());
			device.addException(t.getMessage());
		}
		
		return device;
	}
	
	public static Device invoke(String url, Device device){
		HttpDeviceInvoker invoker = new HttpDeviceInvoker(url);
		return invoker.invoke(device);
	}

}
