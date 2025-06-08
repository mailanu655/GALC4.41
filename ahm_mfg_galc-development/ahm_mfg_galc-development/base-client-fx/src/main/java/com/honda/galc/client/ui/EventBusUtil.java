package com.honda.galc.client.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import javafx.application.Platform;

import org.apache.commons.lang.ClassUtils;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.honda.galc.client.ClientMainFx;

import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.INotificationService;
/**
 * 
 * <h3>EventBusUtil Class description</h3>
 * <p>
 * The EventBusUtil provides a wrapper around the google event bus class
 * allowing events to be posted on the JavaFx Application thread.
 * </p>
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
 * 
 * @author Suriya Sena Jan 22, 2014 - JavaFx migration
 * 
 */

public class  EventBusUtil  {

	private static Map<String,EventBus> eventBusList = new LinkedHashMap<String,EventBus>();
	private static List<Object> eventListenerList = new ArrayList<Object>();
	private static boolean isDebug = false;
	private static int     LISTENER_HIGHWATER = 20;

	private EventBusUtil() {}

	public static void register(Object o) {
		
		if(!eventListenerList.contains(o)){
			EventBus eventBus = getEventBus();
			if(null!=eventBus){
				eventBus.register(o);
				eventListenerList.add(o);
     			if (eventListenerList.size() > LISTENER_HIGHWATER) {
     				getLogger().info(String.format("LISTENER_HIGHWATER exceeded! %d event listeners registered, less than %d expected. This may indicate a memory leak, each class that registers with the event bus MUST also unregister when done.", eventListenerList.size(),LISTENER_HIGHWATER));
     				dumpListenerTable();
     			}
			}
		}

		if (isDebug) {
			getLogger().info(String.format("*** EventBusUtil.register(%s)",o.toString()));
			dumpListenerTable();
		}
	}
	
	private static EventBus findEventBus(String sourceEventId) {
		EventBus eventBus = null;
		if(null!=sourceEventId){
			eventBus = eventBusList.get(sourceEventId);
			if(eventBus == null){
				eventBus = new EventBus(new EventBusExceptionLogger(sourceEventId));
			}
			eventBusList.put(sourceEventId, eventBus);
		}
		
		return eventBus;
	}

	public static void unregister(Object o) {
		try {
			if (eventListenerList.contains(o)) {
				EventBus eventBus = getEventBus();
				if(null!=eventBus){
					eventBus.unregister(o);
					eventListenerList.remove(o);
				}
			}

			if (isDebug) {
				getLogger().info(String.format("*** EventBusUtil.unregister(%s)",(o != null ? o.toString() : "null" )));
				dumpListenerTable();
			}
		} catch (Exception ex) {
			getLogger().error(ex, "*** EventBus.unregister " + (ex.getMessage()));
		}
	}

	public static boolean isRegistered(Object o) {
		return eventListenerList.contains(o);
	}

	public static void publish(final IEvent event) {
	       
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	publishEvent(event);
            }
        });
    }
	
	private static void publishEvent(IEvent event) {
		try {
			EventBus eventBus = getEventBus();
			eventBus.post(event);
		} catch(Exception ex) {
			throw new ServiceException("Failed to publish event", ex);
		}

	}

	private static EventBus getEventBus() {
		if(ClientMainFx.getInstance()!=null){
			return findEventBus(ClientMainFx.getInstance().currentApplicationId);
		}else{
			return new EventBus(new EventBusExceptionLogger());
		}
	}
	
	public static void publishAndWait(final IEvent event) {
			publishEvent(event);
	}

	public static List<Object> findListenersOfType(Class<?> clazz) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object o : eventListenerList) {
			if (o.getClass() == clazz) { 
				list.add(o);
			}
		}
		return list;
	}

	public static void setDebug(boolean isDebug) {
		EventBusUtil.isDebug = isDebug; 
	}

	private static void dumpListenerTable() {
		getLogger().info( String.format("EventBus Registered Listeners (%d/%d)",eventListenerList.size(),LISTENER_HIGHWATER));
		for (Object obj : eventListenerList) {
			getLogger().info("  >> " + obj.toString());
		}
	}

	private static Logger getLogger() {
		Logger logger = Logger.getLogger(EventBusUtil.class.getSimpleName());
		logger.getLogContext().setApplicationInfoNeeded(true);
		return logger;
	}

	public static void publish(final Request request) {
		for (final Object listener: findNotificationListeners(request.getTargetClass())) {
			Platform.runLater(new Runnable() {
				public void run() { 
					try {
						request.invoke(listener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			});
		}
	}

	public static List<Object> findNotificationListeners(String notificationClass) {
		Class<?> clazz = getClass(notificationClass);
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object o : eventListenerList) {
			if (o instanceof INotificationService) {
				if (ClassUtils.isAssignable(o.getClass(), clazz)) {
					list.add(o);
				}
			}
		}
		return list;
	}

	public static Class<?> getClass(String targetClass) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(targetClass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clazz;
	}

	private static class EventBusExceptionLogger implements SubscriberExceptionHandler {
		private String sourceEventId;
		public EventBusExceptionLogger(){
			// no sourceEventId specified
		}
		public EventBusExceptionLogger(String sourceEventId) {
			this.sourceEventId = sourceEventId;
		}
		@Override
		public void handleException(Throwable throwable, SubscriberExceptionContext context) {
			getLogger().error(throwable, "sourceEventId: " + this.sourceEventId);
		}
	}
}