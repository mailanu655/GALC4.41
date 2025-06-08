package com.honda.galc.client.datacollection.control;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.ClientContext.Observers;
import com.honda.galc.client.datacollection.fsm.FSMContext;
import com.honda.galc.client.datacollection.fsm.IDataCollectionFsm;
import com.honda.galc.client.datacollection.fsm.LotControlFSMBuilder;
import com.honda.galc.client.datacollection.observer.DataCollectionObserverBase;
import com.honda.galc.client.datacollection.observer.ILotControlDbManager;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.observer.LotControlDeviceManager;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.LotControlMainBase;
import com.honda.galc.client.device.scanless.ScanlessScannerMessage;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDataInput;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.net.Request;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>DataCollectionController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionController description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Jun 16, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class DataCollectionController {
	
	private IDataCollectionFsm fsm;
	private String applicationId = "";
	private ClientContext clientContext;
	private FSMContext fsmContext;
	private TerminalPropertyBean property;
	private ILotControlDbManager dbManager;
	private ReentrantLock reEntrantLock = null;
	
	private BlockingQueue<String> productIdQueue = new LinkedBlockingQueue<String>();
	private Map<String, ScanlessScannerMessage> scanlessScannerStatus = new HashMap<String, ScanlessScannerMessage>();
	
	private static String defaultApplicationId = "";
	
	
	private Map<Class<?>, IDataCollectionTaskProcessor<? extends IDeviceData>> processors = 
		Collections.synchronizedMap(new LinkedHashMap<Class<?>, IDataCollectionTaskProcessor<? extends IDeviceData>>());
	
	private Map<String, IDataCollectionTaskProcessor<? extends IDeviceData>> strategies = 
		Collections.synchronizedMap(new LinkedHashMap<String, IDataCollectionTaskProcessor<? extends IDeviceData>>());
	
	private volatile static ConcurrentHashMap<String, DataCollectionController> instances = 
		new ConcurrentHashMap<String, DataCollectionController>();
	
	private boolean clientStarted = false;

	private DataCollectionController(String applicationId) {
		super();
		this.applicationId = applicationId;
		setDefaultApplicationId(applicationId);
		initialize();
		AnnotationProcessor.process(this);
	}
	
	private void initialize() {
		//Create state machine
		reEntrantLock = new ReentrantLock(true);
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		fsmContext = new FSMContext(getProperty());
		fsmContext.setApplicationId(this.applicationId);
		LotControlFSMBuilder fsmBuilder = new LotControlFSMBuilder(fsmContext);
		fsm = fsmBuilder.buildFSM(IDataCollectionFsm.class, interfaces);
		getState().init(fsmContext);
	}
	
	/**
	 * use this static method if only one application/process point is
	 * configured for the terminal
	 */
	public static DataCollectionController getInstance() {
		return getInstance(getDefaultApplicationId());
	}

	/**
	 * use this static method if multiple applications/process points are
	 * configured for the terminal
	 * 
	 * @param applicationId
	 * @return
	 */
	public static DataCollectionController getInstance(String applicationId) {
		if(!instances.containsKey(applicationId)){
			instances.put(applicationId, new DataCollectionController(applicationId));
		}
		return instances.get(applicationId);
	}

	public void registerProcessors() {
		//The order matters - it impacts the registration on data converter
		processors.put(ProcessProduct.class, createProcessor(getProperty().getProductIdProcessor()));
		processors.put(ProcessPart.class, createProcessor(getProperty().getPartSnProcessor()));
		processors.put(ProcessTorque.class,createProcessor(getProperty().getTorqueProcessor()));
		processors.put(ProcessRefresh.class, createProcessor(getProperty().getRefreshProcessor()));
	}
	
	@SuppressWarnings({ "unchecked"})
	public IDataCollectionTaskProcessor<? extends IDeviceData> createProcessor(String processorName) {
		Class claz;
		try {
			claz = Class.forName(processorName);
			Class[] parameterTypes = {ClientContext.class};
			Object[] parameters = {clientContext};
			Constructor constructor = claz.getConstructor(parameterTypes);
			return (IDataCollectionTaskProcessor)constructor.newInstance(parameters);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to create processor:" + processorName);
			throw new TaskException("Failed to create processor:" + processorName);
		}
	}
	
	/**
	 * Returns current processor based on configuration.  If the current state
	 * uses strategy in lot control rule, a new instance of the processor from 
	 * the lot control rule will be initialized and returned.
	 * 
	 * @return
	 */
	public IDataCollectionTaskProcessor<? extends IDeviceData> getCurrentProcessor() {
		IDataCollectionTaskProcessor<? extends IDeviceData> processor = processors.get(getState().getClass());
		String strategy = getStrategy();
		
		if ((getState() instanceof ProcessPart
			|| getState() instanceof ProcessTorque) 
			&& !StringUtils.isEmpty(strategy)) {

			if(strategies.get(strategy) == null){
				processor = createProcessor(strategy);
				strategies.put(strategy, processor);
			} else 
				processor = strategies.get(strategy);
		} 
		processor.init();
		return processor;
	}
	
	private String getStrategy() {
		 try {
			return StrategyType.valueOf(getState().getCurrentLotControlRule().getStrategy()).getCanonicalStrategyClassName();
		} catch (Exception e) {
			//OK it may not yet have lot control rule loaded
			//Logger.getLogger().debug(e, "failed to get strategy - ok if lot control rule not loaded.");
			return null;
		}
	}
	
	/** Add observers for the headed station in the order 
	 * in which they should be notified 
	 * 
	 * @param lotControlMainBase 
	 * @throws SystemException
	 */
	public void addObservers(LotControlMainBase lotControlMainBase) throws SystemException {
		addBaseObservers();
		
		//add audio observer
		String audioMgr = getProperty().getObservers().remove(Observers.AUDIO_MANAGER.toString());
		if(!StringUtils.isEmpty(audioMgr)) 
			addObserver(audioMgr);
		
		addConfiguredObservers();
	
		//View Manager is the last
		addObserver((DataCollectionObserverBase) lotControlMainBase.getViewManager());
	}
	
	public void addObservers(IViewObserver viewManager) throws SystemException {

		Map<String, String> observers = property.getObservers();
		observers.remove(Observers.DB_MANAGER.toString());
		fsm.addObserver((IObserver)clientContext.getDbManager());
		
		//add device after DB observer
		String deviceMgr = observers.remove(Observers.DEVICE_MANAGER.toString());
		if(!StringUtils.isEmpty(deviceMgr)) addObserver(deviceMgr);
		
		//add audio after device observer
		String audioMgr = observers.remove(Observers.AUDIO_MANAGER.toString());
		if(!StringUtils.isEmpty(audioMgr)) addObserver(audioMgr);
		
		//Add any other observers 
		for( String observerClass : observers.values()){
			
			if(StringUtils.isEmpty(observerClass)) continue;
			
			addObserver(observerClass);
			
		}
	
		//View Manager is the last
		addObserver((DataCollectionObserverBase)viewManager);
	}
	
	public <T extends IObserver> T getObserver(Class<T> clzz){
		return fsm.getObserver(clzz);
	}
	
	/** Add observers for headless station in the order 
	 * in which they should be notified
	 * 
	 * @throws SystemException
	 */
	public void addObservers() throws SystemException {
		addBaseObservers();
		addConfiguredObservers();
	}

	/**
	 * add base observers
	 */
	private void addBaseObservers() {
		// add DB observer
		getProperty().getObservers().remove(Observers.DB_MANAGER.toString());
		fsm.addObserver((IObserver) clientContext.getDbManager());
		
		//add device observer
		String deviceMgr = getProperty().getObservers().remove(Observers.DEVICE_MANAGER.toString());
		if(!StringUtils.isEmpty(deviceMgr)) 
			addObserver(deviceMgr);
	}
	
	/** 
	 * add additional observers that are configured in the GAL489tbx table
	 */
	private void addConfiguredObservers() {
		//Add any other observers that are configured
		for(String observerClass : getProperty().getObservers().values()){
			if(StringUtils.isEmpty(observerClass.trim())) 
				continue;
			addObserver(observerClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addObserver(String observerClass) {
		Class clazz;
		IObserver observer;
		try {
			clazz = Class.forName(observerClass);
			Constructor constructor[] = clazz.getConstructors();
			
			if(constructor != null && constructor.length > 0)
				observer = clientContext.createObserver(observerClass);
			else
				observer = createObserverSingleton(observerClass);
			
			fsm.addObserver(observer);
		} catch (Throwable e) {
			Logger.getLogger().error(e, "Failed to create observer : " + observerClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	public IObserver createObserverSingleton(String observerName) {
		if(observerName == null || observerName.length() <= 0) return null;
		Class claz;
		try {
			claz = Class.forName(observerName);
			Class[] parameterTypes = null;
			Object[] parameters = null;
			Method method = claz.getMethod("getInstance", parameterTypes);
			return (IObserver) method.invoke(claz, parameters);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to create observer:" + observerName);
			throw new TaskException("Failed to create observer:" + observerName);
		}
	}

	public Object received(Object arg){
		if(getProperty().isQueueProductId() && arg instanceof ProductId) {
			String productId = ((ProductId)arg).getProductId();
			Logger.getLogger().info("Received Product Id : "  + productId);
			processProductIdWithQueue("", productId);
			return null;
		}else {
			return basicReceived(arg);
		}
	}
	
	private Object basicReceived(Object arg) {	
		if(!isClientStarted()){
			Logger.getLogger().warn("Data:", arg.getClass().getSimpleName(), 
					" was received before Lot Control Client start up complete, so the data:", 
					arg.getClass().getSimpleName(), " was dropped." );
			
			return null;
		}
		Logger.getLogger().debug("received with arg = "+ arg.getClass().getSimpleName() + " EDT="+javax.swing.SwingUtilities.isEventDispatchThread());
		reEntrantLock.lock();
		try{
			Logger.getLogger().debug("Entered received with arg = " + arg.getClass().getSimpleName());
			Object obj = processReceived(arg);
			Logger.getLogger().debug("Finished received with arg = " +
					"" +
					""+ arg.getClass().getSimpleName());
			return obj; 
		}finally{
			reEntrantLock.unlock();
			if(getProperty().isQueueProductId()) processProductIdQueue();
		}
	}

	private Object processReceived(Object arg) {
		Logger.getLogger().debug("processReceived "+ arg.getClass().getSimpleName());
		
		if(arg instanceof IDeviceDataInput) {
			IObserver observer = getObserver(Observers.DEVICE_MANAGER.toString());
			return ((LotControlDeviceManager)observer).processDeviceData((IDeviceDataInput)arg);
				
		} else if (arg instanceof IDeviceData) {
			return getCurrentProcessor().processReceived((IDeviceData) arg);
		} else if (arg instanceof Request) {
			return processRequest((Request)arg);
		} else {
			Logger.getLogger().error("Received unsupported data: " + arg.getClass().getSimpleName());
			return null;
		}
	}

	private Object processRequest(Request request) {
		try {
			return request.invoke(getFsm());
		} catch (Exception e) {
			Logger.getLogger().error(e, "Error while processing request " + request);
			getFsm().message(new Message(this.getClass().getSimpleName(), e.getCause() == null ? e.getClass().getSimpleName() : e.getCause().getClass().getSimpleName()));
		}
		return null;
	}
		
	public String getStateName(){
		return fsmContext.getState().getClass().getSimpleName();
	}
	
	public DataCollectionState getState(){
		return (DataCollectionState) fsmContext.getState();
	}
	
	public LotControlRule getCurrentLotControlRule() {
		return getState().getCurrentLotControlRule();
	}
	
	public Map<Class<?>, IDataCollectionTaskProcessor<? extends IDeviceData>> getProcessors() {
		return processors;
	}
	
	public IDataCollectionTaskProcessor<? extends IDeviceData> getProcessor(Class<?> state){
		return processors.get(state);
	}
	
	public IDataCollectionFsm getFsm() {
		return fsm;
	}

	public void setFsm(IDataCollectionFsm fsm) {
		this.fsm = fsm;
	}

	public ILotControlDbManager getDbManager() {
		return dbManager;
	}

	public void setDbManager(ILotControlDbManager dbManager) {
		this.dbManager = dbManager;
	}

	public ClientContext getClientContext() {
		return clientContext;
	}

	public void setClientContext(ClientContext clientContext) {
		this.clientContext = clientContext;
	}

	public void addObserver(DataCollectionObserverBase observer) {
		getFsm().addObserver(observer);
	}

	public void init() {
		getFsm().init();
	}

	public void cleanUp() {
		getFsm().cleanUp();
	}

	public void setClientStarted(boolean status) {
		this.clientStarted  = status;
	}

	public boolean isClientStarted() {
		return clientStarted;
	}
	
	private String getApplicationId() {
		return applicationId;
	}

	public TerminalPropertyBean getProperty() {
		if (property == null) {
			property = PropertyService.getPropertyBean(TerminalPropertyBean.class, getApplicationId());
		}
		return property;
	}

	@SuppressWarnings("unchecked")
	public IObserver getObserver(String className) {
		try {
			Class<? extends IObserver> clzz = (Class<? extends IObserver>)Class.forName(clientContext.getObservers().get(className));
			return getObserver(clzz);
		} catch (Exception e) {
			Logger.getLogger().warn(e, " Failed to get observer instance.");
		}
		return null;
		
	}
	
	@EventTopicSubscriber(topic="IProductPassedNotification")
	public void onProductPassedEvent(String event, Request request) {
        try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
   }
	
	public void execute(String processPointId, String productId) {
		
		Logger.getLogger().info("Notification received for processPointId : " + processPointId + " vin : " + productId);
		if(getProperty().isQueueProductId()) processProductIdWithQueue(processPointId, productId);
		else {
			basicReceived(new ProductId(productId));
		}
			
	}
	
	public void processProductIdWithQueue(String processPointId, String productId) {
		Logger.getLogger().info("Present State = "+ DataCollectionController.getInstance().getState()+ "," + "ProductId ="+ DataCollectionController.getInstance().getState().getProductId());
        
		productIdQueue.add(productId);
		
		Logger.getLogger().info("product id " + productId + " received is queued. Current Queue Size " + productIdQueue.size());
		
	    processProductIdQueue();

	}
	
	public void processProductIdQueue() {
		if(!productIdQueue.isEmpty() && DataCollectionController.getInstance().getState() instanceof ProcessProduct && !reEntrantLock.isLocked()) {
			try {
				
				String productId = productIdQueue.take();
				Logger.getLogger().info("product id " + productId + " received is de-queued. Current Queue Size " + productIdQueue.size());
				
				basicReceived(new ProductId(productId));
			}catch (Exception ex) {
				ex.printStackTrace();
				Logger.getLogger().error(ex, "exception when running received method");
			}
		}
	}
	
	public static String getDefaultApplicationId() {
		return defaultApplicationId;
	}

	public static void setDefaultApplicationId(String defaultApplicationId) {
		DataCollectionController.defaultApplicationId = defaultApplicationId;
	}
	
	public String getNextProductIdFromQueue() {
		String productId="";
		try {
			productId = !productIdQueue.isEmpty()?productIdQueue.take():"";
		Logger.getLogger().info("product id " + productId + " received is de-queued. Current Queue Size " + productIdQueue.size());
		}catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "exception when running received method");
		}
		return productId;
	}
	
	public void addProductIdToQueue(String productId) {
		
		try {
			if(!productIdQueue.contains(productId))
			  productIdQueue.add(productId);
		Logger.getLogger().info("product id " + productId + " received is de-queued. Current Queue Size " + productIdQueue.size());
		}catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "exception when running received method");
		}
		
	}
	
	public void addScanlessScannerStatus(ScanlessScannerMessage scanlessScannerMessage) {
		scanlessScannerStatus.put(scanlessScannerMessage.getToolId(),scanlessScannerMessage);
	}
	
	public void clearScanlessScannerStatus() {
		scanlessScannerStatus.clear();
	}
	
	public ScanlessScannerMessage getScannerStatus(String toolId) {
		return scanlessScannerStatus.get(toolId);
	}
}