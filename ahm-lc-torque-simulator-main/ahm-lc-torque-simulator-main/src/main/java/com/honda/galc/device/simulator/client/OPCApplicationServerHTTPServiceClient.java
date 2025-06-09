package com.honda.galc.device.simulator.client;

import java.io.IOException;
import java.util.logging.Logger;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.net.HttpRequestInvoker;
import com.honda.galc.net.Request;
/**
 * 
 * <h3>OPCApplicationServerHTTPServiceClient</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OPCApplicationServerHTTPServiceClient description </p>
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
 * <TD>Jul 27, 2011</TD>
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
 * @since Jul 27, 2011
 */

public class OPCApplicationServerHTTPServiceClient implements EquipmentApplicationServerClient{
	private Logger logger = null; 
	private boolean useXML = false;
	private String url;
	private String targetClass = "DataCollectionService";
	
	
	public OPCApplicationServerHTTPServiceClient(String serverURL, Logger logger) {
		this.logger = logger;
		init(serverURL);
	}

	private void init(String serverURL) {
		String[] split = serverURL.split(" ");
		if(split.length >= 1) this.url = split[0].trim();
		if(split.length >= 2) this.targetClass = split[1].trim();
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
			
			return send(prepareSend(processPointId, client, data));
		} catch (Exception e) {
			logger.fine("Exception to call application server" + e.toString());
			return null;
		}
	}
	
	private DataContainer prepareSend(String processPointId, String client,
			DataContainer data) {
				
		data.setClientID(client);
		data.put(DataContainerTag.PROCESS_POINT_ID, processPointId);

		return data;
	}

	public DataContainer send(DataContainer dc) throws IOException {
		Object returnObj = new HttpRequestInvoker(url).invoke(new Request(targetClass, "execute", new Object[]{dc}));
		if(returnObj instanceof IOException) throw (IOException)returnObj;
		if(!dc.getClass().getSimpleName().equals(returnObj.getClass().getSimpleName())){ 
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
