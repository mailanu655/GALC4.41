package com.honda.galc.client.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.net.AbstractRequestInvoker;
import com.honda.galc.net.ServiceMonitor;
/**
 * 
 * <h3>HttpDataContainerInvoker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HttpDataContainerInvoker description </p>
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
public class HttpDataContainerInvoker extends AbstractRequestInvoker<DataContainer> {

	private HttpURLConnection urlConnection = null;
	private String url;

	public HttpDataContainerInvoker(String url) {
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
	
	public void initConnectionXml() throws ServiceTimeoutException{
		try {
			URL hostUrl = new URL(url);
			urlConnection = (HttpURLConnection)hostUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setReadTimeout(0);
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type","application/xml");
  		urlConnection.connect();
		} catch (Exception e) {
			throw createServiceTimeoutException(e);
		}    
	}

	@Override
	public DataContainer invoke (DataContainer dc){
		try{
			DataContainer returnDc = (DataContainer) super.invoke(dc);
			if(returnDc == null){
				Logger.getLogger().error("Error occured on server, return null from server.");
				returnDc = dc;
				returnDc.put(TagNames.EXCEPTION.name(), "Error occured on server.");
			}
			return returnDc;
		}catch(ServiceTimeoutException e) {
			ServiceMonitor.getInstance().startMonitorHttpService();
			Logger.getLogger().error(e, "Exception to call application server for device:", dc.getClientID());
			dc.put(TagNames.EXCEPTION.name(), e.getMessage());
		}catch(Throwable t){
			Logger.getLogger().error(t, "Exception to call application server for device:", dc.getClientID());
			dc.put(TagNames.EXCEPTION.name(), t.getMessage());
		}
		
		return dc;

	}
	
	public DataContainer invokeByXml (DataContainer dc){
		initConnectionXml(); 

		try {
			DataContainerXMLUtil.outputXML(dc, urlConnection.getOutputStream());
			getOutputStream().flush();
			return DataContainerXMLUtil.readDeviceDataFromXML(urlConnection.getInputStream());
			
		} catch (IOException e) {
				e.printStackTrace();
			return new DefaultDataContainer();
		}finally{
            close();
        }
			
	}	
	
	public static DataContainer invoke (String url, DataContainer dc){
		HttpDataContainerInvoker invoker = new HttpDataContainerInvoker(url);
		return invoker.invoke(dc);
	}

}
