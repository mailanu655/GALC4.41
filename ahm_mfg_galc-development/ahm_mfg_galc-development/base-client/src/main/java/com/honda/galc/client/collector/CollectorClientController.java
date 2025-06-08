package com.honda.galc.client.collector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.script.ClientScriptInterpreter;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.IoService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>CollectorClientController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorClientController description </p>
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
 * <TD>Dec 2, 2011</TD>
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
 * @since Dec 2, 2011
 */

public class CollectorClientController implements ActionListener, DataContainerListener{
    private static final String HTML_TEMPLATE = "htmlTemplate";
	private static final String EI_DEVICE = "eiDevice";
    private String lineSeparator = System.getProperty("line.separator");  
	@SuppressWarnings("rawtypes")
	List<IRender> renders = new ArrayList<IRender>();
	ClientScriptInterpreter interpreter;
	Map<Object, Object> result;
	DataContainer returnDc;
	String renderType;
	CollectorMain main;
	Properties htmlProperties;
	StringBuilder htmlTemplate = new StringBuilder();
	DeviceDataConverter dataConverter;
	Class<? extends IoService> clz = null;
	protected boolean resetFlag = true;
	DataInputDialog inputDataDialog;
	
	public CollectorClientController() {
		super();
		init();
	}

	public CollectorClientController(CollectorMain collectorMain) {
		this.main = collectorMain;
		init();
	}

	void init(){
		AnnotationProcessor.process(this);
		
		initInterpreter();
		initComponents();
		initConnections();
		registerDeviceData();
		
		createRenders();
		resetScreen();
	}

	private void initInterpreter() {
		interpreter = new ClientScriptInterpreter(main.getApplicationContext(), main.getLogger());
		String tmplate = getProperty().getRenderTemplate();
		if(!StringUtils.isEmpty(tmplate))
			loadTemplate(tmplate);
		
		String properties = getProperty().getRenderProperties();
		if(!StringUtils.isEmpty(properties))
			loadProperties(properties);
	}
	
	private void loadTemplate(String tmplate) {
		try {
			InputStream in = getClass().getResourceAsStream(tmplate);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			 try {
				 String line = null;
	             do {
	                line = reader.readLine();
	                htmlTemplate.append(line + lineSeparator);
	             }while (line != null);
	          }catch(IOException e) {
	            getLogger().error(e, "Exception to read html template.");
	          }finally{
	        	  if(in != null) in.close();
	          }
		} catch (Exception e) {
			main.getLogger().warn(e, "Failed to load template:" + tmplate);
		}
		
	}
	
	private void loadProperties(String properties) {
		try {
			htmlProperties = new Properties();
			InputStream in = getClass().getResourceAsStream(properties);
			try {
				htmlProperties.load(in);
			} catch (Exception e) {
				getLogger().error(e, "Exception to read html properties");
			} finally{
				if(in != null) in.close();
			}
			
			
		} catch (Exception e) {
			main.getLogger().warn(e, "Failed to load properties:" + properties);
		}
		
	}

	protected void resetScreen() {
		result = null;
		returnDc = null;
		main.getCollectorPanel().refresh();
		main.getStatusMessagePanel().setErrorMessageArea(null);
	}


	private void initComponents() {
		main.getCollectorPanel().getProductPane().getProductLookupButton().setText(getProperty().getProductIdLabel());
		main.getCollectorPanel().getProductPane().getProductSpecLabel().setText(getProperty().getProductSpecLabel());
	}
	
	private void initConnections() {
		getProductIdField().addActionListener(this);
		getCancelButton().addActionListener(this);
		
	}
	
	private void registerDeviceData() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DataCollectionComplete());
		getEiDevice().reqisterDeviceData(list);
		
		if(getProperty().isUseOpcEi()){
			getEiDevice().registerDataContainerListener(this);
		}
	}

	@SuppressWarnings("unchecked")
	private void createRenders() {
		Map<String, String> renderMap = getProperty().getRenders();
		renderType = getProperty().getRenderType();
		
		if(renderMap == null || renderMap.size() == 0) return; //Ok if not defined.
		
		//start from 1
		IRender<Map<String, String>> render = CollectorClientRenderFactory.getInstance().createRender(renderType, interpreter);
		render.setProperty(renderMap);
		renders.add(render);
	}
	
	public boolean renderResult(){
		
		if(!renderProductIdField()) return false;

		main.getStatusMessagePanel().setErrorMessageArea(null);
		getCancelButton().setVisible(true);
		
		injectTemplateData();
		interpreter.initInterpreter(result);
		
		if(renderType.equals("html"))
			renderHtmlResult();
		
		if(!getProperty().isEnableCancel())
		{
			if(resetFlag)
				resetScreen();
		}
		
		return true;
	}
	
	private void processAction() {
		Map<String, String> actionMap = getProperty().getActions();
		
		try {
			interpreter.set(EI_DEVICE, getEiDevice());
			interpreter.setScripts(actionMap);
			interpreter.processScripts();
			
		} catch (Exception e) {
		    main.getLogger().error(e, "Exception to process Actions:" + actionMap);
		    main.getStatusMessagePanel().setErrorMessageArea("Error: process actions error. " + getExceptionMessage(e));
		}
	}

	private void injectTemplateData() {
		
		if(htmlProperties != null && htmlProperties.size() >  0){
			for (Object key : htmlProperties.keySet()) {
				result.put(key, htmlProperties.get(key));
			}
		}
		
		if(htmlTemplate.length() > 0)
			result.put(HTML_TEMPLATE, htmlTemplate);
	}

	private boolean renderProductIdField() {
		
		if(!getProperty().isShowProductId()) return true;
		
		if(result == null || result.size() == 0){ 
			getLogger().warn("Result is empty.");
			productCheckError("Exception on Server.");
			return false;
		} else {
			Boolean validProduct = (Boolean) result.get(TagNames.VALID_PRODUCT_ID.name());
			if(validProduct == null || !validProduct){
				StringBuffer errorMsg = new StringBuffer("Invalid Product. ");
				if(StringUtils.isNotBlank(result.get(TagNames.INFO_MSG.name()).toString())){
					errorMsg.append(result.get(TagNames.INFO_MSG.name()).toString());
				}
				getLogger().warn(errorMsg.toString());
				productCheckError(errorMsg.toString());
				return false;
			}
			
			getProductIdField().setStatus(validProduct);
		}
		
		getProductSpecField().setText((String)result.get(TagNames.PRODUCT_SPEC_CODE.name()));
		return true;
	}

	private void productCheckError(String msg) {
		getProductIdField().setStatus(false);
		main.getStatusMessagePanel().setErrorMessageArea(msg);
		return;
	}

	protected void renderHtmlResult() {
		StringBuilder sb = new StringBuilder();
		for(IRender<?> render : renders)
			sb.append(render.render());
		
		main.getCollectorPanel().getResultPane().setVisible(true);
		main.getCollectorPanel().getResultPane().setCaretPosition(0);
		main.getCollectorPanel().getResultPane().setText(sb.toString());
		getLogger().debug("html contents:" + sb.toString());
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getProductIdField())
			productIdReceivedFromTerminal(getProductIdField().getText());
		else if(e.getSource() == getCancelButton())
			resetScreen();
	}

	
	protected void productIdReceivedFromTerminal(String productId) {
		getLogger().info("Received product:", productId);
		
		Device device = getDevice(main.getApplicationContext().getProcessPointId());
		device.getDeviceFormat(TagNames.PRODUCT_ID.name()).setValue(productId);
		
		if(getProperty().getDataInputMap() != null && getProperty().getDataInputMap().size() > 0) {
			DataInputDialog dialog = getInputDataDialog(device);
			dialog.open(device);
			
			if(dialog.isCancelled()) {
				main.getCollectorPanel().getProductPane().getProductIdField().selectAll();
				main.getCollectorPanel().getProductPane().getProductIdField().requestFocus();
				return;
			}
		}
		
		productIdReceived(device.toRequestDataContainer());
		
	}

	protected void productIdReceived(DataContainer dc) {
		try {
			
			if(StringUtils.isEmpty(dc.getString(TagNames.PROCESS_POINT_ID.name())))
				dc.put(TagNames.PROCESS_POINT_ID.name(), main.getApplicationContext().getProcessPointId());
			
			result = invokeDataCollectionService(dc);
		} catch (Exception e) {
			getProductIdField().setStatus(false);
			main.getStatusMessagePanel().setErrorMessageArea("Exception " + getExceptionMessage(e));
			getLogger().error(e, " Exception on invoking data collection service.");
			return;
		}
		
		processServiceResponse();
	}

	@SuppressWarnings("unchecked")
	protected void processServiceResponse() {
		returnDc = new DefaultDataContainer();
		List<String> tagList = (List<String>)result.get(TagNames.TAG_LIST.name());
		if(tagList != null && tagList.size() > 0){
			for(String key : tagList)
				returnDc.put(key, result.get(key));
		}
		
		returnDc.put(TagNames.PROCESS_POINT_ID.name(), result.get(TagNames.PROCESS_POINT_ID.name()));
		returnDc.put(TagNames.CLIENT_ID.name(), result.get(TagNames.CLIENT_ID.name()));
		getLogger().info("Data collection return:" + (returnDc == null ? "null" : returnDc.toString()));
		
		try {
			if(!renderResult()) return;
		} catch (Exception e) {
			getLogger().error(e, " Exception on rendering result.");
			main.getStatusMessagePanel().setErrorMessageArea("Error:" + getExceptionMessage(e));
		}
		
		Runnable r = new Runnable() {
			public void run()
			{
				processAction();
			}
		};

		SwingUtilities.invokeLater(r);
	}
	
	private void serviceRequestReceived(DataContainer dc) {
		try {
			getLogger().info("Received data:", dc.toString());
			result = invokeService(dc);
		} catch (Exception e) {
			if(getProperty().isShowProductId()) getProductIdField().setStatus(false);
			main.getStatusMessagePanel().setErrorMessageArea("Exception " + getExceptionMessage(e));
			getLogger().error(e, " Exception on invoking data collection service.");
			return;
		}
		
		processServiceResponse();
	}
	
	protected Logger getLogger() {
		return main.getLogger();
	}

	protected String getExceptionMessage(Exception e) {
		return e.getMessage() + (e.getCause() == null ? "" : ":" + e.getCause());
	}

	protected Map<Object, Object> invokeDataCollectionService(DataContainer dc) {
		DataCollectionService service = ServiceFactory.getService(DataCollectionService.class);
		
		return service.execute(dc);
	}

	protected Device getDevice(String clientId) {
		return ServiceFactory.getDao(DeviceDao.class).findByKey(StringUtils.isEmpty(clientId)?  main.getApplicationContext().getProcessPointId() : clientId);
	}
	
	private Map<Object, Object> invokeService(DataContainer dc) {
		
		Class<? extends IoService> clz = getServiceClass(dc);
		IoService Ioservice = ServiceFactory.getService(clz);
		
		if(StringUtils.isEmpty(dc.getString(TagNames.PROCESS_POINT_ID.name())))
			dc.put(TagNames.PROCESS_POINT_ID.name(), main.getApplicationContext().getProcessPointId());
		
		if(StringUtils.isEmpty(dc.getClientID()))
				dc.put(TagNames.CLIENT_ID.name(), main.getApplicationContext().getProcessPointId());
		return Ioservice.execute(dc);
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends IoService> getServiceClass(DataContainer dc) {
		if(clz == null){
			clz = DataCollectionService.class;

			try {
				String serviceClzName = null;
				if (dc.containsKey(TagNames.SERVICE_NAME.name()))
					serviceClzName = (String) dc.get(TagNames.SERVICE_NAME.name());
				else
					serviceClzName = ServiceFactory.getDao(	ApplicationTaskDao.class).findHeadlessTaskName(
							main.getApplicationContext().getProcessPointId());
				
				clz = (Class<? extends IoService>)Class.forName(serviceClzName);
				
			} catch (Exception e) {
				getLogger().warn(e, "Exception to get service class - default service class is used.");
			}
		}
		
		return clz;
		
	}

	public EiDevice getEiDevice() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null)
			eiDevice.start();
		
		return eiDevice;
	}

	public UpperCaseFieldBean getProductIdField() {
		return main.getCollectorPanel().getProductPane().getProductIdField();
	}
	
	protected JButton getCancelButton() {
		return main.getCollectorPanel().getCancelButton();
	}

	
	public JTextField getProductSpecField() {
		return main.getCollectorPanel().getProductPane().getProductSpecField();
	}
	
	protected JEditorPane getResultPane() {
		return main.getCollectorPanel().getResultPane();
		
	}

	public CollectorClientPropertyBean getProperty() {
		return main.getProperty();
	}

	public DataContainer received(final DataContainer dc) {
		
		Device device = getDevice(dc);
		
		if(device.getDeviceFormat(TagNames.SERVICE_REQUEST.name()) != null){
			serviceRequestReceived(dc);
		} else if(device.getDeviceFormat(TagNames.RESET.name())!= null){
			resetScreen();
		} else {
			
			DeviceFormat deviceFormat = device.getDeviceFormat(TagNames.PRODUCT_ID.name());
			if(deviceFormat != null)
				getProductIdField().setText((String)deviceFormat.getValue());
			
			
			productIdReceived(dc);
		}
		
		return returnDc;
	}

	private Device getDevice(DataContainer dc) {
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(dc.getClientID());
		device.populate(dc);
		
		return device;
	}

	public DataInputDialog getInputDataDialog(Device device) {
		if(inputDataDialog == null)
		{			
			inputDataDialog = new DataInputDialog(this.main.getCollectorPanel().getMainWin(), device, getProperty());
		}
		return inputDataDialog;
	}

	@EventSubscriber(eventClass = ProgressEvent.class)
	public void startAutoUpdated(ProgressEvent event) {
		if(event.getProgress() != 100) return;
		if(!getProperty().isAutoUpdate()) return;
		
		Thread t = new Thread(){
			public void run() {
				
				Device device = getDevice(main.getApplicationContext().getProcessPointId());
				DataContainer dc = device.toRequestDataContainer();
				
				while(true){
					serviceRequestReceived(dc);
					
					try {
						Thread.sleep(getProperty().getUpdateInterval());
					} catch (Exception e) {
						getLogger().warn(e, "Exception on auto-update.");
					}
				}
			}
		};
		
		t.start();
		
	}

	
	
}
