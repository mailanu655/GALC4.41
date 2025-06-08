package com.honda.galc.device.simulator.client;

import java.io.IOException;
import java.util.logging.Logger;

import com.honda.galc.client.common.HttpDataContainerInvoker;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
/**
 * 
 * <h3>OPCApplicationServerHTTPClient</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OPCApplicationServerHTTPClient description </p>
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
 * <TD>May 28, 2012</TD>
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
 * @since May 28, 2012
 */
public class OPCApplicationServerHTTPClient implements EquipmentApplicationServerClient {
	private Logger logger = null; 
	private boolean useXML = false;
	private String url;
	
	
	public OPCApplicationServerHTTPClient(String serverURL, Logger logger) {
		this.logger = logger;
		init(serverURL);
	}

	private void init(String serverURL) {
		String[] split = serverURL.split(" ");
		if(split.length >= 1) this.url = split[0].trim();
	}

	public boolean isUseXML() {
		return useXML;
	}

	public void setLogger(Logger newLogger) {
		this.logger = newLogger;
		
	}

	public void setUseXML(boolean useXML) {
		this.useXML = useXML;
		
	}


	private DataContainer doSend(String processPointId, String client,
			DataContainer data) throws IOException{
		try {
			data.setClientID(client);
			data.put(DataContainerTag.PROCESS_POINT_ID, processPointId);
			
			return send(data);
			
		} catch (Exception e) {
			logger.fine("Exception to call application server" + e.toString());
			return null;
		}
	}

	public DataContainer send(DataContainer data) throws IOException {
		HttpDataContainerInvoker invoker = new HttpDataContainerInvoker(url);
		Object returnObj = isUseXML() ? invoker.invokeByXml(data) : invoker.invoke(data);
		
		if(returnObj instanceof IOException) throw (IOException)returnObj;
		if(!data.getClass().getSimpleName().equals(returnObj.getClass().getSimpleName())){ 
			throw new TaskException("unexpected return data type:" + returnObj.getClass().getSimpleName());
		}
		return prepareReply((com.honda.galc.data.DataContainer)returnObj);
	}
	

	private DataContainer prepareReply(com.honda.galc.data.DataContainer data) {
		DataContainer newContainer = new DefaultDataContainer();
		for(Object key : data.keySet()){
			newContainer.put(key, data.get(key));
		}
		return newContainer;
	}

	public DataContainer transmitStateless(String processPointId,
			String client, DataContainer data) throws IOException {
		return doSend(processPointId, client, data);
	}

	public DataContainer transmitStatelessAsync(String processPointId,
			String client, DataContainer data) throws IOException {
		return doSend(processPointId, client, data);
	}


	public DataContainer transmit(String processPointId, String client,
			DataContainer data) throws IOException {
		return doSend(processPointId, client, data);
	}

	public DataContainer transmitAsync(String processPointId, String client,
			DataContainer data) throws IOException {
		return doSend(processPointId, client, data);
	}

	
}
