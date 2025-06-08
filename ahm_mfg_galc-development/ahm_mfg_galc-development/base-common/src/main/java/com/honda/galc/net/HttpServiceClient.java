package com.honda.galc.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.common.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;


/**
 * 
 * <h3>RestServiceClient</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RestServiceClient description </p>
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
 * <TD>Sep 12, 2013</TD>
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
 * @since Sep 12, 2013
 */
public abstract class HttpServiceClient  {
	private String url;	
	private int httpConnTimeout;
	private int httpReadTimeout;
	
	protected Logger log = Logger.getLogger();
	public abstract int getResponseCode(); 
	
	public HttpServiceClient(String url) {
		this.url = url;
	}
	
	public HttpServiceClient(String url, int httpConnTimeout, int httpReadTimeout) {
		this.url = url;
		this.httpConnTimeout = httpConnTimeout;
		this.httpReadTimeout = httpReadTimeout;
	}
	 
	public DataContainer get(DataContainer data) throws IOException {
		return null; //TODO
	}
	
	public DataContainer send(DataContainer data) throws IOException {
		try {
			String jsonStr = HttpClient.post(url, prepareSend(data), getResponseCode(), httpConnTimeout, httpReadTimeout);
			return new GsonBuilder().create().fromJson(jsonStr,	DefaultDataContainer.class);
		} catch (Exception e) {
			log.error("Exception to invoke Rest Service." + e.toString());
		}
		return null;
	}
	
	private String prepareSend(DataContainer dc) {
		Map<String, DataContainer> dataMap = new HashMap<String, DataContainer>();
		dataMap.put(DefaultDataContainer.class.getName(), dc);
		
		Gson gson = new GsonBuilder().create();
				
		return gson.toJson(dataMap);
	}
	
	public int getHttpConnTimeout() {
		return httpConnTimeout;
	}

	public void setHttpConnTimeoutSec(int httpConnTimeout) {
		this.httpConnTimeout = httpConnTimeout;
	}

	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	public void setHttpReadTimeoutSec(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
	}
	
}
