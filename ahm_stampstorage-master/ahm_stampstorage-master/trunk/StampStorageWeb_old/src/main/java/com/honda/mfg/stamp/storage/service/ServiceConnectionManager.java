package com.honda.mfg.stamp.storage.service;

import com.honda.io.socket.SocketFactory;
import com.honda.mfg.stamp.conveyor.domain.ServiceRole;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public final class ServiceConnectionManager implements ServiceConnectionManagerInterface {

	
	/**
	 * @param schedulingInterval
	 */
	private ServiceConnectionManager(int schedulingInterval) {
		this();
		this.schedulingInterval = schedulingInterval;
	}

	private int schedulingInterval = 30;
	
	public int getSchedulingInterval() {
		return schedulingInterval;
	}

	public void setSchedulingInterval(int schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}

	boolean connected = false;
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public void run() {
		processServiceRoles();
	}

	private void processServiceRoles()  {
		
		ServiceRole thisRole = null;
		List serviceRoleList = ServiceRole.findServiceRolesByCurrentActiveNot(false).getResultList();
		if(serviceRoleList.size() > 0) {thisRole = (ServiceRole)serviceRoleList.get(0);}
		if(shouldISwitch(thisRole, serviceRole))  {
			//change ip
			serviceRole = thisRole;
			serviceSocketFactory.setHostName(serviceRole.getIp());
			serviceSocketFactory.setPortNumber(serviceRole.getPort());			
		}
	}
	
	public void init()  {
		//scheduler moved out to spring config
	}
	
	@EventSubscriber(eventClass = ConnectionEventMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void setConnectionState(ConnectionEventMessage msg) {
        LOG.info("received connection event: " + msg.isConnected());
        setConnected(msg.isConnected());
        if(!isConnected())  {processServiceRoles();}
    }
	
	private boolean shouldISwitch(ServiceRole thisRole, ServiceRole thatRole)  {
		boolean shouldI = false;
		if (thisRole == null)  {shouldI = false;}
		else if(thisRole.getCurrentActive())  {
			if(serviceRole == null)  {shouldI = true;}
				// not connected, switch
			else if(thisRole.getId() != serviceRole.getId())  {shouldI = true;}
				//  active service changed, switch
		}
		return shouldI;
	}
	
	private ServiceConnectionManager() {
        AnnotationProcessor.process(this);
	};
	
	public static ServiceConnectionManager getInstance(int schedulingInterval)  {
		if(instance == null)  {
			instance = new ServiceConnectionManager(schedulingInterval);
			instance.init();
		}
		return instance;
	}
	
	public String getCurrentIp()  {
		String currentIp = "";
		if(serviceRole != null)  {
			currentIp = serviceRole.getIp();
		}
		return currentIp;
	}
	public int getCurrentPort()  {
		int currentPort = 0;
		if(serviceRole != null)  {
			currentPort = serviceRole.getPort();
		}
		return currentPort;
	}
	private ServiceRole serviceRole = null;
	
	/**
	 * did this service miss a ping from the service
	 */
	private boolean missedPing = false;
	private boolean isMissedPing() {
		return missedPing;
	}

	private void setMissedPing(boolean missedPing) {
		this.missedPing = missedPing;
	}
	// missedPing
	
	private void notifyAllSwitch()  {
	}

	@Autowired
	public SocketFactory serviceSocketFactory;
	
	private static ServiceConnectionConfig serviceConnectionConfig  = null;
	private static ServiceConnectionManager instance = null;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceConnectionManager.class);
}
