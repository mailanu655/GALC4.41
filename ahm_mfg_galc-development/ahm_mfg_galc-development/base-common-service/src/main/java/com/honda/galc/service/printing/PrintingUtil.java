package com.honda.galc.service.printing;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.EntityCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.net.SocketClient;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.PrintingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.QueueProcessor;

/**
 * 
 * <h3>PrintingUtil Class description</h3>
 * <p> PrintingUtil description </p>
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
 * <TD>Meghana G</TD>
 * <TD>Mar 8, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the access specifier for formId to make it accessible down the hierarchy.</TD>
 * </TR>  
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 16, 2010
 *
 *
 */
public class PrintingUtil extends QueueProcessor<DataContainer>{

	public static final String PRINT_TEMPLATE_PATH = "/resource/com/honda/galc/print/";

	private String clientId;
	protected String formId;
	
	private EntityCache<Device,String> devices ;
	
	private String sessionCookieValue;
	
	private boolean isBroadcast = false;
	
	private Map<String,PrintAttributeConvertor> convertors = new HashMap<String, PrintAttributeConvertor>();
	private BuildAttributeCache buildAttributeList;
	
	public PrintingUtil() {
		
		devices = new EntityCache<Device,String>(getDao(DeviceDao.class));
		buildAttributeList = new BuildAttributeCache();
		
		this.start();
		
	}
	
	
	public PrintingUtil(String clientId,String formId) {
		this.clientId = clientId;
		this.formId = formId;
		
		devices = new EntityCache<Device,String>(getDao(DeviceDao.class));
		
		if(StringUtils.isEmpty(clientId))
			throw new PrintingException("Printer name is not configured");
		
		if(StringUtils.isEmpty(formId)) {
			throw new PrintingException("Form id is not configured");
		}
			
		loadData();
		this.start();
	}
	
	private void loadData() {
		
		Device device = devices.findByKey(clientId);
		
		buildAttributeList = new BuildAttributeCache(clientId,formId);
		
		if(device == null || !device.getDeviceType().equals(DeviceType.PRINTER)) 
			throw new PrintingException("Device : " + clientId + " is not configured as a printer device");

	}
	
	

	public boolean isBroadcast() {
		return isBroadcast;
	}


	public void setBroadcast(boolean isBroadcast) {
		this.isBroadcast = isBroadcast;
	}


	public String getSessionCookieValue() {
		return sessionCookieValue;
	}

	public void setSessionCookieValue(String sessionCookieValue) {
		this.sessionCookieValue = sessionCookieValue;
	}

	public void print(String clientId, String formId,Product product) {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT, product);
		print(dc);

	}

	public void print(ProductSpec productSpec) {

		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_SPEC,productSpec);
		print(dc);

	}

	public void print(DataContainer dataContainer) {
		
		
		print(clientId,formId,dataContainer);
		
	}
	
	public void print(String clientId, String formId, DataContainer dataContainer) {
		
		Device device = getDevice(clientId);
		String mappedFormId = mapFormId(formId,getProductSpecCode(formId, dataContainer));
		
		PrintAttributeConvertor convertor = getConvertor(mappedFormId);
		DataContainer dc = convertor.make(dataContainer );
		
		dc.put(DataContainerTag.PROCESS_POINT_ID, device.getIoProcessPointId());
		dc.put(DataContainerTag.PRINTER_TYPE, device.getEifIpAddress());
		dc.put(DataContainerTag.PRINTER_DESTINATION, device.getEifPort());
		String mappedQueueName = mapQueueName(clientId,getProductSpecCode(formId, dataContainer));
		dc.put(DataContainerTag.QUEUE_NAME, mappedQueueName);
		dc.put(DataContainerTag.FORM_ID, mappedFormId);
		
		// enqueue the printing
		enqueue(dc);
		
		
	}
	
	public void print(List<? extends BaseProduct> products) {
		
		String sessionCookieValue = ServiceFactory.createSession();
		
		try{
		
			doPrint(products);
			
		}finally{
		
			ServiceFactory.destroySession(sessionCookieValue);
			
		}
		
	}
	
	public void print(String clientId, String formId, List<?extends BaseProduct> products) {
		
		String sessionCookieValue = ServiceFactory.createSession();
		
		try{
		
			doPrint(clientId,formId,products);
			destroySession(sessionCookieValue);
			
		}catch(Exception ex) {
		
			ServiceFactory.destroySession(sessionCookieValue);
			throw new SystemException("failed to print products. formId : " + formId + "  printer : " + clientId,ex);
			
		}
	}
	
	public void destroySession(String sessionCookieValue) {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.SESSION_KEY, sessionCookieValue);
		enqueue(dc);
		
	}
	
	private Device getDevice(String clientId) {
		
		Device device = devices.findByKey(clientId);
		
		if(device == null || !device.getDeviceType().equals(DeviceType.PRINTER)) 
			throw new PrintingException("Device : " + clientId + " is not configured as a printer device");
		
		return device;
		
	}
	
	private void doPrint(List<? extends BaseProduct> products) {
		
		doPrint(clientId,formId,products);
	}
	
	
	private void doPrint(String clientId, String formId,List<? extends BaseProduct> products) {
		
		for(BaseProduct product : products) {
			
			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT, product);
			dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
			if(product instanceof Product) {
				dc.put(DataContainerTag.PRODUCT_SPEC_CODE, ((Product)product).getProductSpecCode());
			}
			dc.put(product.getClass(), product);
			
			print(clientId,formId,dc);
			
		}	
	}

	
	public BuildAttributeCache getBuildAttributeCache() {
		return buildAttributeList;
	}
	
	
	private boolean isResourceTemplate(String formId) {
		InputStream in = getClass().getResourceAsStream(PRINT_TEMPLATE_PATH  + formId + ".tmpl" );
		boolean flag = in != null;
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
				throw new PrintingException("Could not close template file ", e);
			}
		}
		return flag;
	}
	
	
	private PrintAttributeConvertor getConvertor(String formId) {
		
		for(Map.Entry<String, PrintAttributeConvertor> item : convertors.entrySet()) {
			if(item.getKey().equals(formId)) return item.getValue();
		}
		
		PrintAttributeConvertor convertor = new PrintAttributeConvertor(formId);
		convertors.put(formId, convertor);
		
		return convertor;
		
	}
	

	private String mapAttribute(String attribute, String productSpecCode) {
		
		BuildAttribute buildAttribute = buildAttributeList.findById(productSpecCode,attribute);
		
		return buildAttribute == null ? attribute : buildAttribute.getId().getAttribute();
		
	}
	
	/**
	 * map the original queue name to a new queue name based on product spec code
	 * @param queueName
	 * @param productSpecCode
	 * @return
	 */
	private String mapQueueName(String queueName,String productSpecCode ) {
		
		return mapAttribute(queueName,productSpecCode);
		
	}
	
	/**
	 * map the original formId to a new form id based on product spec code
	 * @param formId
	 * @param productSpecCode
	 * @return
	 */
	private String mapFormId(String formId, String productSpecCode) {
		
		return mapAttribute(formId,productSpecCode);
		
	}
	
	private String getProductSpecCode(String formId, DataContainer dc) {
		
		if(dc.containsKey(DataContainerTag.PRODUCT_SPEC)) {
			ProductSpec productSpec = (ProductSpec) dc.get(DataContainerTag.PRODUCT_SPEC);
			if(productSpec != null) return productSpec.getProductSpecCode();
		}
		
		if(dc.containsKey(DataContainerTag.PRODUCT_SPEC_CODE)) {
			
			String productSpecCode = (String)dc.get(DataContainerTag.PRODUCT_SPEC_CODE);
			if(productSpecCode != null) return productSpecCode;
			
		}
		
		return formId;
			
	}

	@Override
	public void processItem(DataContainer dc) {
		
		if(ServiceFactory.isServerSide() || !dc.containsKey(DataContainerTag.PRINTER_DESTINATION) || ((Integer)dc.get(DataContainerTag.PRINTER_DESTINATION)) == 0){ 
			if(dc.containsKey(DataContainerTag.SESSION_KEY)) {
				ServiceFactory.destroySession(dc.getString(DataContainerTag.SESSION_KEY));
			}else 
				printRemote(dc);
		}else 
			printLocal(dc);
		
	}
	
	private void printRemote(DataContainer dc) {
		
		String mappedQueueName = StringUtils.trim((String)dc.get(DataContainerTag.QUEUE_NAME));
		String mappedFormId = StringUtils.trim((String)dc.get(DataContainerTag.FORM_ID));
		
		if(!isBroadcast) {
			getService(PrintingService.class,sessionCookieValue).print(mappedQueueName, mappedFormId,dc);
		}else {		
			getService(BroadcastService.class).broadcast((String)dc.get(DataContainerTag.PROCESS_POINT_ID), dc);
		}
		Logger.getLogger().info("sent print job name : " + mappedQueueName + " with form id : " + mappedFormId + " to printing service" );
		delay();
	}
	
	private void printLocal(DataContainer dc) {
		
		String formId = (String)dc.get(DataContainerTag.FORM_ID);
		IPrinterDataAssembler printDataAssembler = 
			isResourceTemplate(formId) ? new TemplatePrinterDataAssembler(true)
								 : new ListPrinterDataAssembler();
			
		String ip = (String)dc.get(DataContainerTag.PRINTER_TYPE);
		int port = (Integer)dc.get(DataContainerTag.PRINTER_DESTINATION);
		SocketClient printSocket;
		try{
			printSocket = new SocketClient(ip,port,10);
		}catch (Exception e ) {
		   return ;	
		}
		
		printDataAssembler.writeOutputFile(printSocket.getOutputStream(), dc);
		
		Logger.getLogger().info("Sent form : " + formId + " to the network printer at " + ip + ":" + port);
		
		delay();
		
	}
	
	private void delay() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
