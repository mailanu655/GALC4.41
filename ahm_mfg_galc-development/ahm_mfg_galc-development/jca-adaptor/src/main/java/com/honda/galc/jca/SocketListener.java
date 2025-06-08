package com.honda.galc.jca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class SocketListener implements Runnable, Work {

	private static final String LOGGER_ID = "JcaAdaptor";
	
	private ServerSocket serverSocket;
	private WorkManager workManager;

	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private EndpointPool endpointPool;
	private LetSpool spool;

	SocketListener (WorkManager workManager, MessageEndpointFactory messageEndpointFactory, LetSpool spool) {
		this.workManager = workManager;
		this.spool = spool;
		endpointPool = new EndpointPool(messageEndpointFactory);
	}
	
	final boolean isRunning() {
		return isRunning.get();
	}

	final void setRunning(Boolean running) {
		isRunning.set(running);
	}
	
	public void release() {
		setRunning(false);
		try {
			if (serverSocket!=null){
				serverSocket.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {
		if (serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed()) {
			return;
		}

		setRunning(true);
		try {
			getLogger().info("Attempting to listen for LET messages at " + spool.getPortNumber());
			serverSocket = new ServerSocket(spool.getPortNumber());
			getLogger().info("Started listening for LET messages at " + spool.getPortNumber());
			
			while (isRunning()) {
				waitForRequestAndProcess();
			}
		} catch (Exception ex) {
			if (isRunning()) {
				getLogger().error(ex, "Error while accepting a socket request and scheduling work on the request. See linked exception");
			}
			setRunning(false);
		}
	}

	private void waitForRequestAndProcess() throws IOException, UnavailableException, WorkException {
		final Socket socket = serverSocket.accept();
		logSocketInfo(socket);
		try{
			SocketMessage socketMessage = new SocketMessageImpl(socket, "ISO-8859-1", spool);
			SocketMessageEndpoint messageEndpoint = endpointPool.getEndpoint();
			
			scheduleWork(new SocketProcessor(socketMessage, messageEndpoint));
		}catch ( UnavailableException ex){
			getLogger().error(ex, "No more endpoints avaiable in the pool");
		}
	}

	private void scheduleWork(SocketProcessor socketProcessor) throws WorkException {
		ExecutionContext executionContext = null;
		workManager.scheduleWork(socketProcessor, WorkManager.IMMEDIATE, executionContext, endpointPool);
	}

	public void logSocketInfo(Socket socket) {
		try {
			getLogger().info("LET socket connection Accepted from " + socket.getInetAddress());
			getLogger().info("getKeepAlive         " + socket.getKeepAlive());
			getLogger().info("getLocalPort         " + socket.getLocalPort());
			getLogger().info("getOOBInline         " + socket.getOOBInline());
			getLogger().info("getReceiveBufferSize " + socket.getReceiveBufferSize());
			getLogger().info("getReuseAddress      " + socket.getReuseAddress());
			getLogger().info("getSendBufferSize    " + socket.getSendBufferSize());
			getLogger().info("getSoLinger          " + socket.getSoLinger());
			getLogger().info("getSoTimeout         " + socket.getSoTimeout());
			getLogger().info("getTcpNoDelay        " + socket.getTcpNoDelay());
			getLogger().info("getTrafficClass      " + socket.getTrafficClass());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
		
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
	
	protected LetPropertyBean getLetPropertyBean() {
		return PropertyService.getPropertyBean(LetPropertyBean.class);
	}

	public LetSpool getSpool() {
		return spool;
	}
}
