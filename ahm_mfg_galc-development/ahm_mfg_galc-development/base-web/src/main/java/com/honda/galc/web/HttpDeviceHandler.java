package com.honda.galc.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.IoService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>HttpDeviceHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HttpDeviceHandler handles request from OPC EI request only. </p>
 * <p> This is implemented to facilitate the deploy and support of the existing OPC EI. </p> 
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
 * <TD>Feb 29, 2012</TD>
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
 * @since Feb 29, 2012
 */
public class HttpDeviceHandler extends AbstractHttpServiceHandler {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_SERVICE="DataCollectionService";
	private static final String DEVICE_SERVER = "Device_Server";
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	public HttpDeviceHandler() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	
	private void processRequest(HttpServletRequest request,	HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		Object requestObj = null;
		boolean isXml = isUseXml(request); //xml is always data container
		boolean isDataContainer; //input object is data container
		boolean isAsync;
		String serviceClass;
		
		try {
			requestObj = getRequestObject(request, isXml);
			getLogger().info("Received device request:", requestObj.toString(), " time(ms):" + (System.currentTimeMillis() - startTime));

			isDataContainer = requestObj instanceof DataContainer;
			isAsync = isDataContainer && isAsync(requestObj);
			if(isAsync)	writeAsyncResponse(isXml, response);

			serviceClass = getServiceClass(requestObj, isDataContainer);

			if(StringUtils.isEmpty(serviceClass))
				throw new TaskException("Configuration error: Invalid service class name.");

			//must be IoService for this Device Service Handler
			IoService serviceBean = (IoService)ApplicationContextProvider.getBean(serviceClass);

			Object returnObj = invokeService(serviceBean, requestObj, isDataContainer);
			
			sendResponse(returnObj, response, isDataContainer, isXml, isAsync);
			getLogger().info("Completed invoke service:", " time(ms):" + (System.currentTimeMillis() - startTime));

		} catch (Throwable e) {
			getLogger().error(e, "Exception to process requesst.");
			handleException(isXml,requestObj, response);
		}
		
	}


	private void sendResponse(Object returnObj, HttpServletResponse response,
			boolean isDataContainer, boolean isXml, boolean isAsync) throws IOException {
		if(!isDataContainer)
			writeResponse(response, returnObj);
		else if(!isAsync){
			writeDataContainerResponse(response, (DataContainer)returnObj, isXml);
		}
		
	}

	private void writeAsyncResponse(boolean isXml, HttpServletResponse response) {
		DataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.DATA, "1");
		data.put(DataContainerTag.CLIENT_ID, "ACK");
		
		try {
			writeDataContainerResponse(response, data, isXml);
		} catch (Exception e) {
			getLogger().error(e, " Failed to write async response ACK." );
		}
			
	}

	private String getServiceClass(Object reqObj, boolean isDataContainer) {
		String service = null;
		if(reqObj == null)
			throw new TaskException("Invalid request object.");

		String processPointId = isDataContainer ? (String)((DataContainer)reqObj).get(DataContainerTag.APPLICATION_ID) :
			((Device) reqObj).getIoProcessPointId();
		
		//Fill information may missing from Old GALC request
		if(isDataContainer && StringUtils.isEmpty(processPointId)){
			processPointId = getProcessPointId(((DataContainer)reqObj).getClientID());
			((DataContainer)reqObj).put(DataContainerTag.APPLICATION_ID, processPointId);
			((DataContainer)reqObj).put(DataContainerTag.PROCESS_POINT_ID, processPointId);
		}
		
		if(isDataContainer && !((DataContainer)reqObj).containsKey(DataContainerTag.PROCESS_POINT_ID))
			((DataContainer)reqObj).put(DataContainerTag.PROCESS_POINT_ID, processPointId);
		
		if(StringUtils.isEmpty(processPointId))
			throw new TaskException("Invalid process point id.");

		service = ServiceFactory.getDao(ApplicationTaskDao.class).findHeadlessTaskName(processPointId.trim());
		service = StringUtils.trim(service);
		
		return StringUtils.isEmpty(service) ?  DEFAULT_SERVICE : service;
	}
	

	private String getProcessPointId(String clientId) {
		return ServiceFactory.getDao(DeviceDao.class).findProcessPointId(clientId);
	}

	private Object getRequestObject(HttpServletRequest request, boolean isXml)
			throws IOException, ClassNotFoundException {
		if (isXml) {
			InputStream is = request.getInputStream();
			return DataContainerXMLUtil.readDeviceDataFromXML(is);
		} else {
			ObjectInputStream input = new ObjectInputStream(request.getInputStream());
			return input.readObject();
		}
	}
	
	private Object invokeService(IoService serviceBean, Object requestObj, boolean isDataContainer) throws IOException {
		
		long start = System.currentTimeMillis();
		
		Object returnOjb = isDataContainer ? serviceBean.execute((DataContainer)requestObj) 
				: serviceBean.execute((Device)requestObj);
		
		getLogger().info("Result service execution:", ObjectUtils.toString(returnOjb), " time(ms):" + (System.currentTimeMillis() - start));
		
		return returnOjb;
		
	}


	private void handleException(boolean isXml, Object requestObj, HttpServletResponse response) {
		try {
			if ((requestObj instanceof DataContainer) && !isAsync(requestObj)) {
				DataContainer returnDc = new DefaultDataContainer();
				Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(((DataContainer) requestObj).getClientID());
				String replyClientId = device == null ? "" : device.getReplyClientId();

				returnDc.put(DataContainerTag.CLIENT_ID, replyClientId);
				returnDc.put(DataContainerTag.PROCESS_COMPLETE, "0");

				
				writeDataContainerResponse(response, returnDc, isXml);
					
			} else {
				writeResponse(response, requestObj); //TODO - investigate
			}
			
		} catch (Exception e) {
			getLogger().error(e, " Error on handling exception.");
		}
		
	}
	
	
	// ------------ utility functions --------------------
	private void writeDataContainerResponse(HttpServletResponse response, DataContainer returnDc, boolean isXml)
	throws IOException {
		if(isXml)
			writeXmlResponse(response, returnDc);
		else
			writeResponse(response, returnDc);
	}

	private void writeXmlResponse(HttpServletResponse response,
			DataContainer returnDc) throws IOException {
		if(returnDc == null) return;
		
		response.setContentType("application/xml");
		OutputStream os = response.getOutputStream();
		try
		{
			DataContainerXMLUtil.convertToXML(returnDc, os);
		}
		catch (Exception ex)
		{
			getLogger().error(ex, " Exception to write data container XML:", this.getClass().getSimpleName());
		}
		os.flush();
		os.close();
	}
	
	private void writeResponse(HttpServletResponse response, Object responseObj)
	throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(response.getOutputStream());
		output.writeObject(responseObj);
		output.flush();
		output.close();
	}
	
	
	private boolean isAsync(Object requestObj) {
		return isAsyncMode((DataContainer)requestObj);
	}
	
	private boolean isAsyncMode(DataContainer dc) {
		String mode = (String)dc.remove(DataContainerTag.EI_SYNC_MODE);
		return !StringUtils.isEmpty(mode) && DataContainerTag.EI_ASYNC.equals(mode);
	}
	
	private boolean isUseXml(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.indexOf("/xml") >= 0;
	}
	
	@Override
	protected String getHandlerId() {
		return DEVICE_SERVER;
	}

}
