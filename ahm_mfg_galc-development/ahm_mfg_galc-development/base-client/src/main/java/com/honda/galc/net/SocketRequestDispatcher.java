package com.honda.galc.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.notification.service.ISubscriptionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.ToStringUtil;
import com.honda.global.galc.common.data.LegacyDataContainerListener;


/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SocketRequestDispatcher extends Thread {


	private int port = -1;
	private ServerSocket serverSocket = null;
	private boolean connectionFlg = false;
	private int maxConnection = 1;

	private Map<Class<? extends IDeviceData>,DeviceListener> deviceListeners = new HashMap<Class<? extends IDeviceData>,DeviceListener>();

	private DataContainerListener dataContainerListener = null;

	private LegacyDataContainerListener legacyDataContainerListener = null;

	private CopyOnWriteArrayList<KeyValue<String,INotificationService>> notificationServices = new CopyOnWriteArrayList<KeyValue<String,INotificationService>>();

	private ISubscriptionService subscriptionService;

	/**
	 * Specifying Port, create the new SocketRequestDispatcher object. 
	 * @param aPort Port
	 */
	public SocketRequestDispatcher(int aPort, ApplicationContext context) {

		port = aPort;
		subscriptionService = (ISubscriptionService)ApplicationContextProvider.getBean(ServiceFactory.NOTIFICATION_SERVICE_PROVIDER);

	}

	/**
	 * Wait for the connection request,
	 * and receive the transmitting DataContainer.
	 * <P>
	 * Create the new Thread(call start() method of this class),
	 * and wait for the connection request among there.
	 * @throws IOException When opening Socket if input/output error accrues
	 * @throws SecurityException When the security manager exists,
	 * and checkListen method of the security manager
	 * does not allow this operation. 
	 */
	public void accept() throws IOException, SecurityException {
		if(port <= 0) return;
		serverSocket = new ServerSocket(port);
		connectionFlg = true;
		this.start();
	}
	/**
	 * Notify the receiving event of DataContainer.
	 * Add DataContainerReceiveListener.
	 * @param aListener Adding listener
	 */

	public void registerListner(DataContainerListener dcListener) {
		this.dataContainerListener = dcListener;
	}

	public void registerListner(LegacyDataContainerListener dcListener) {
		this.legacyDataContainerListener = dcListener;
	}

	public void registerListner(DeviceListener deviceListener, List<IDeviceData> deviceDataList) {
		for(IDeviceData deviceData : deviceDataList) {
			deviceListeners.put(deviceData.getClass(),deviceListener);
		}

		getDeviceDataConverter().registerDeviceData(deviceDataList);

	}
	
	public boolean isSupportLegacy(){
		return this.legacyDataContainerListener != null;
	}

	public void registerListner(DeviceListener deviceListener, IDeviceData... deviceDataList) {
		for(IDeviceData deviceData : deviceDataList) {
			deviceListeners.put(deviceData.getClass(),deviceListener);
		}

		getDeviceDataConverter().registerDeviceData(deviceDataList);

	}
	public void reqisterSubscriptionService(ISubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public void registerNotifcationService(String notificationName,INotificationService notificationService) {
		if(findNotificationService(notificationName, notificationService) == null)
			this.notificationServices.add(new KeyValue<String,INotificationService>(notificationName,notificationService));
	}

	public void unregisterNotificationService(String notificationtName,INotificationService notificationService) {
		KeyValue<String,INotificationService> notificationKeyValue = findNotificationService(notificationtName, notificationService);
		if(notificationKeyValue != null) this.notificationServices.remove(notificationKeyValue);
	}

	/**
	 * Force close ServerSocket
	 * <P>
	 * and wait for the connection request among there.
	 * @throws IOException When opening Socket if input/output error accrues
	 */
	public void close() {
		try{
			if (serverSocket != null) {
				serverSocket.close();
			}
		}catch(IOException e) {

		}
	}

	private DeviceDataConverter getDeviceDataConverter() {

		return ClientMain.getInstance().getDeviceDataConverter();

	}

	/**
	 * Setup the Port which receives the Client requests.
	 * @param aPort Port
	 */
	protected void finalize() {
		try {
			if (serverSocket != null)
				serverSocket.close();
		}
		catch (IOException e) {
		}
	}

	/**
	 * Get the maximum connection numbers in the same time of Client. 
	 * @return The maximum connection numbers in the same time of Client.
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	public DataContainer dispatchDeviceData(DataContainer dc) throws Exception {
		if(this.dataContainerListener != null) return dispatchToDataContainerListener(dc);
		if(!this.deviceListeners.isEmpty()) return dispatchToDeviceListener(dc);
		throw new Exception();
	}

	public DataContainer dispatchToDataContainerListener(DataContainer dc) {
		return dataContainerListener.received(dc);
	}

	public void dispatchToLegacyDataContainerListener(com.honda.global.galc.common.data.DataContainer dc) {
		legacyDataContainerListener.received(dc);
	}

	public synchronized Object dispatchRequest(final Request request) {
		
		Logger.getLogger().info("Received notification: " + ToStringUtil.generateToString(request,false));
		Class<?> aClass = getClass(request.getTargetClass());
		if(aClass == null) {
			// log 
			return null;
		}

		String name = aClass.getSimpleName();
		if(ClassUtils.isAssignable(aClass, ISubscriptionService.class)) {
			return invokeSubscription(request);
		} 
		
		// if Notification handler is provided, invoke it
		if (request instanceof NotificationRequest) {
			NotificationRequest nRequest = (NotificationRequest) request;
			if (!StringUtils.isEmpty(nRequest.getNotificationHandlerClass())) {
				return invoke(nRequest, ReflectionUtils.createInstance(getClass(nRequest.getNotificationHandlerClass())));
			}
		}
		
		if(ClassUtils.isAssignable(aClass, INotificationService.class) && !(request instanceof NotificationRequest)) {
			for(KeyValue<String, INotificationService> notificationKeyValue : notificationServices) {
				if(notificationKeyValue.getKey().equals(name) && aClass.isAssignableFrom(notificationKeyValue.getValue().getClass())) {
					return invoke(request, notificationKeyValue.getValue());
				}
			}
		}

		publishNotification(name, request);
		return null;
	}

	private void publishNotification(final String topicName, final Request request) {
		EventBus.publish(topicName, request);
	}

	private Object invokeSubscription(Request request){
		if(subscriptionService == null){
			// no subscription service implemented
			return false;
		}

		return invoke(request, subscriptionService);
	}

	private Class<?> getClass(String className) {
		try {
			return  Class.forName(className);
		} catch (ClassNotFoundException e) {}
		return null;
	}

	private Object invoke(final Request request, Object obj){
		
		try {
			return request.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return null;
	}

	public DataContainer dispatchToDeviceListener(DataContainer dc) {

		IDeviceData deviceData = getDeviceDataConverter().convert(dc);
		DeviceListener deviceListener = this.deviceListeners.get(deviceData.getClass()); 
		IDeviceData returnData = deviceListener.received(dc.getClientID(), deviceData);

		return getDeviceDataConverter().convertReply(dc.getClientID(),returnData);
	}

	private KeyValue<String,INotificationService> findNotificationService(String notificationName, INotificationService notification) {
		for(KeyValue<String,INotificationService> notificationKeyValue : notificationServices) {
			if(notificationName.equals(notificationKeyValue.getKey()) && notification == notificationKeyValue.getValue()) return notificationKeyValue;
		}
		return null;
	}

	/**
	 * Process contents of Thread which receives the connection requests. 
	 */
	public void run() {
		Socket socket = null;
		if (serverSocket == null) {
			return;
		}

		while (connectionFlg) {
			try {
				socket = serverSocket.accept();
				new SocketRequestHandler(socket,this).start();
			}
			catch (Exception e) {}
		}    
	}
}
