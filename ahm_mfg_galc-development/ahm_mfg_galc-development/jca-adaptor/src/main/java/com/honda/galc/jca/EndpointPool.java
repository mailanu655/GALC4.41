package com.honda.galc.jca;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import com.honda.galc.common.logging.Logger;

import com.honda.galc.property.JcaAdaptorPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class EndpointPool implements WorkListener {
	
	private static final String LOGGER_ID = "JcaAdaptor";

	private final BlockingQueue<SocketMessageEndpoint> endpoints;
	private final TimeUnit timeUnit;

	private MessageEndpointFactory endpointFactory;
	private int poolSize;
	private int maxEndPoints = 20;
	private long timeout = 4000;

	public EndpointPool(MessageEndpointFactory endpointFactory) {
		this.endpointFactory = endpointFactory;
		this.timeUnit = TimeUnit.MILLISECONDS;
		setMaxEndPoints();
		setTimeOut();
		endpoints = new ArrayBlockingQueue<SocketMessageEndpoint>(maxEndPoints);
		getLogger().info("Socket endpoints configured, maxEndPoints = " + maxEndPoints + ", timeout=" + timeout + "(" + timeUnit.toString()+")");
	}
	
	public EndpointPool(MessageEndpointFactory endpointFactory, int maxEndPoints, long timeout, TimeUnit timeUnit) {
		this.endpointFactory = endpointFactory;
		this.maxEndPoints = maxEndPoints;
		this.timeout = timeout;
		this.timeUnit = timeUnit;
		endpoints = new ArrayBlockingQueue<SocketMessageEndpoint>(maxEndPoints);
		getLogger().info("Socket endpoints configured, maxEndPoints = " + maxEndPoints + ", timeout=" + timeout + "(" + timeUnit.toString()+")");
	}

	private SocketMessageEndpoint addIfUnderMaximumPoolSize() throws UnavailableException {
		SocketMessageEndpoint messageEndpoint = null;
		if (poolSize < maxEndPoints) {
			messageEndpoint = (SocketMessageEndpoint) endpointFactory.createEndpoint(null);
			poolSize++;
			getLogger().info("Allocated endpoint, poolsize " + poolSize + " of " + maxEndPoints);
		}
		return messageEndpoint;
	}

	public SocketMessageEndpoint getEndpoint() throws UnavailableException {
		SocketMessageEndpoint messageEndpoint = endpoints.poll();
		if (messageEndpoint == null) {
			messageEndpoint = addIfUnderMaximumPoolSize();
		}
		if (messageEndpoint == null) {
			try {
				messageEndpoint = endpoints.poll(timeout, timeUnit);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
        if ( messageEndpoint == null ){
            throw new UnavailableException( "No more endpoints available, maxEnpoints=" + maxEndPoints);
        }
		return messageEndpoint;
	}

	public void release(WorkEvent workEvent) {
		Work work = workEvent.getWork();
		SocketProcessor socketProcessor = (SocketProcessor) work;
		SocketMessageEndpoint socketMessageEndpoint = socketProcessor.getMessageEndpoint();
		endpoints.offer(socketMessageEndpoint);
	}

	public void workCompleted(WorkEvent workEvent) {
		release(workEvent);
	}

	private void setMaxEndPoints() {
		try {
			this.maxEndPoints = getJcaAdaptorPropertyBean().getMaxConnections();
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Max end points property is not accessible, using default " + maxEndPoints);
		}
	}
	
	private void setTimeOut() {
		try {
			this.timeout = getJcaAdaptorPropertyBean().getConnectionTimoutInMilliSeconds();
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Timeout property is not accessible, using default " + timeout);
		}
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
	
	protected JcaAdaptorPropertyBean getJcaAdaptorPropertyBean() {
		return PropertyService.getPropertyBean(JcaAdaptorPropertyBean.class);
	}

	public void workAccepted(WorkEvent workEvent) {}
	
	public void workRejected(WorkEvent workEvent) {
		getLogger().error(workEvent.getException() , "The job has been rejected or intererupted, closing the Adaptor");
		release(workEvent);
	}

	public void workStarted(WorkEvent workEvent) {}
}
