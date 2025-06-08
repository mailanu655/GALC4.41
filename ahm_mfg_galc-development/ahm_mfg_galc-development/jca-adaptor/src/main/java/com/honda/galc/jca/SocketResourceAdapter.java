package com.honda.galc.jca;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkException;
import javax.transaction.xa.XAResource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.honda.galc.dao.product.LetSpoolDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.service.ServiceFactory;

public class SocketResourceAdapter implements ResourceAdapter, ApplicationListener {

	private static BootstrapContext ctx = null;
	private static MessageEndpointFactory msgEndPointFactory = null;
	private static HashMap<Integer, SocketListener> socketServersMap = new HashMap<Integer, SocketListener>();

	public SocketResourceAdapter() {}

	public void start(BootstrapContext context) throws ResourceAdapterInternalException {
		getLogger().info("Entering SocketResourceAdapter.start()");
		ctx = context;
		getLogger().info("Exiting SocketResourceAdapter.start()");
	}

	public void stop() {
		getLogger().info("Entering SocketResourceAdaptor.stop()");
		Iterator<SocketListener> it = getSocketServersMap().values().iterator();
		while (it.hasNext()) {
			SocketListener listener = it.next();
			String msg = "Stopping SocketListener on port: " + listener.getSpool().getPortNumber() + " for Spool " + listener.getSpool();
			getLogger().info(msg);
			listener.release();
			it.remove();
		}
		getLogger().info("Active SocketListeners:" + getSocketServersMap());
		getLogger().info("Exiting SocketResourceAdaptor.stop()");
	}

	public void endpointActivation(MessageEndpointFactory endPointFactory, ActivationSpec activationSpec) throws ResourceException {
		getLogger().info("Invoked SocketResourceAdaptor.endpointActivation()");
		msgEndPointFactory = endPointFactory;
	}

	public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
		getLogger().info("Invoked SocketResourceAdaptor.endpointDeactivation()");
		stop();
	}

	public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException {
		return null;
	}
	
	private static Logger getLogger() {
		return Logger.getLogger(SocketResourceAdapter.class.getSimpleName());
	}
	
	public synchronized void startSocketListeners(List<LetSpool> spools) throws BeansException {
		for(LetSpool spool: spools) {
			try {
				if (!getSocketServersMap().containsKey(spool.getPortNumber())) {
					getLogger().info("Starting SocketListener on port: " 
							+ spool.getPortNumber()
							+ " for Spool "
							+ spool.getEnvName() 
							+ "/" 
							+ spool.getLineName());
					SocketListener listener = new SocketListener(ctx.getWorkManager(), msgEndPointFactory, spool);
					ctx.getWorkManager().scheduleWork(listener);
					getSocketServersMap().put(spool.getPortNumber(), listener);
				}
			} catch (WorkException e) {
				e.printStackTrace();
				getLogger().severe("WorkManager failed to start SocketListener");
			}
		}
		getLogger().info("Active SocketListeners:" + getSocketServersMap());		
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			getLogger().info("ContextRefreshedEvent received from: " + event.getSource());
			if (ApplicationContextProvider.getApplicationContext() != null) {
				try {
					startSocketListeners(getSpoolDao().findAll());
				} catch (Exception ex) {
					ex.printStackTrace();
					getLogger().severe("Unable to retrieve LET Spool data: " + StringUtils.trimToEmpty(ex.getMessage()));
				}
			}
		}
	}
	
	public HashMap<Integer, SocketListener> getSocketServersMap() {
		return socketServersMap;
	}
	
	public LetSpoolDao getSpoolDao() {
		return ServiceFactory.getDao(LetSpoolDao.class);
	}
}
