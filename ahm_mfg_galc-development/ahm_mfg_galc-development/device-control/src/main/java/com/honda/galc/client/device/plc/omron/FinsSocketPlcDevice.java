package com.honda.galc.client.device.plc.omron;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.client.device.exceptions.NetworkCommunicationException;
import com.honda.galc.client.device.exceptions.ResponseTimeoutException;
import com.honda.galc.client.device.exceptions.TooManyRequestsInQueueException;
import com.honda.galc.client.device.exceptions.UnknownResponseException;
import com.honda.galc.client.device.plc.ByteOrder;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.IPlcMemory;
import com.honda.galc.client.device.plc.omron.messages.FinsClockReadRequest;
import com.honda.galc.client.device.plc.omron.messages.FinsInitializeRequest;
import com.honda.galc.client.device.plc.omron.messages.FinsMemoryReadRequest;
import com.honda.galc.client.device.plc.omron.messages.FinsMemoryWriteRequest;
import com.honda.galc.client.device.plc.omron.messages.FinsResponse;
import com.honda.galc.client.device.property.PlcDevicePropertyBean;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.CharSets;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Oct 26, 2011
 */
public class FinsSocketPlcDevice extends AbstractDevice 
	implements IPlcSocketDevice {

    private static final int READ_MEMORY_SERVICE_ID = 1;
    private static final int WRITE_MEMORY_SERVICE_ID = 2;
    private static final int READ_CLOCK_SERVICE_ID = 3;
    
    private static final long ONE_MINUTE = 60000;
    
	private String _hostName = "";
	private int _port = -1;
	private int _responseTimeout = 2000;
	private int _connectionTimeout = 2000;
	
	private volatile int _destinationNode = 0;
	private volatile int _sourceNode = 0;
	private volatile boolean _finalizingSocket = false;
	private volatile boolean _initialized = false;
	private volatile boolean _suspendQueueProcessing = false;
	private volatile BufferedReader _reader = null;
	private volatile BufferedWriter _writer = null;
	private volatile Socket _socket = null;
	private volatile FinsResponseReader _responseReader = null;
	
	private volatile Random _random = new Random(); 
	private volatile LinkedBlockingQueue<FinsMemoryRequestQueueItem> _sendQueue = new LinkedBlockingQueue<FinsMemoryRequestQueueItem>();

	public FinsSocketPlcDevice () {
	}
	
	public FinsSocketPlcDevice (String hostName, int port) {
		setHostName(hostName);
		setPort(port);
	}
	
	/**
	 * sets the device properties
	 */
	public void setDeviceProperty(DevicePropertyBean devicePropertyBean) {
		PlcDevicePropertyBean property = null;
		try {
			property = (PlcDevicePropertyBean) devicePropertyBean;
			setId(property.getDeviceId());
			setResponseTimeout(property.getResponseTimeout());
			setConnectionTimeout(property.getConnectionTimeout());
		
			// retrieve hostname and port from device table gal253tbx
			DeviceDao deviceDao = ServiceFactory.getDao(DeviceDao.class);
			Device device = deviceDao.findByKey(getId());
			if (device != null) {
				setHostName(device.getEifIpAddress());
				setPort(device.getEifPort());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not set FinsSocketPlcDevice property: " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		
		// if not defined in device table, look at terminal properties
		if (getHostName().equals("") || getPort() == -1) {
			setHostName(property.getIpAddress());
			setPort(property.getPort());
		}
	}
	
	/**
	 * starts the FinsSocketPlcDevice
	 */
	private void startDevice() {
		try{
			startFinsInitializerDaemon();
			startMemoryRequestDaemon();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not start FinsSocketPlcDevice " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
	}

	/**
	 * initialize communication with the device 
	 * by sending a FINS initialize message
	 * 
	 * @throws Exception
	 */
	private void initializeFins() {
		getLogger().info("Attempting to Initialize FINS communication with PLC for device: " + getId());
		FinsInitializeRequest initializeRequest = new FinsInitializeRequest();
		try {
			setSuspendQueueProcessing(true);
			finalizeIOStreams();
			
			// write FINS initialize message
			getSocketWriter().write(StringUtil.toCharArray(initializeRequest.getMessageRequest()));
			getSocketWriter().flush();
			getLogger().info(">>" + StringUtil.toHexString(initializeRequest.getMessageRequest()) + ">>");
			
			// read FINS initialize response
			processFinsReply(getResponseReader().getResponse(true, getResponseTimeout()));
			
			setSuspendQueueProcessing(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not initialize FINS communications with PLC: " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
	}
	
	/**
	 * Dequeues from the send queue, invokes processMemoryRequest
	 */
	private void startMemoryRequestDaemon() {
		Thread memoryRequestThread = new Thread() {
			public void run() {
				while (isActive()) {
					try {
						if (!isSuspendQueueProcessing() && isInitialized()) {
							processMemoryRequest(getSendQueue().take());
						} else {
							Thread.sleep(10);
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
						getLogger().error("Unable to take item from the plc send queue " + (ex.getMessage() != null ? ex.getMessage() : ""));
					}
				}
			}
		};

		// start in a new thread immediately
		memoryRequestThread.start();
	}
	
	/**
	 * processes a single FinsMemoryRequestQueueItem. sends and receives data to/from PLC
	 * @param queueItem
	 */
	private void processMemoryRequest(FinsMemoryRequestQueueItem queueItem) {
		queueItem.setProcessingStarted(true);
		queueItem.setExpirationTime(System.currentTimeMillis() + getResponseTimeout());
		try {
			getSocketWriter().write(StringUtil.toCharArray(queueItem.getSendMessage()));
			getSocketWriter().flush();
			if (queueItem.isLogFinsTraffic())
				getLogger().info(">>" + StringUtil.toHexString(queueItem.getSendMessage()) + ">>");
		} catch (Exception ex) {
			setConnected(false);
			ex.printStackTrace();
			getLogger().error("Could not write bytes to output stream " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}	
		
		try {
			queueItem.setReceivedMessage(getResponseReader().getResponse(queueItem.isLogFinsTraffic(), getResponseTimeout()));
		} catch (Exception ex) {
			setConnected(false);
			ex.printStackTrace();
			getLogger().error("Could not read bytes from input stream " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
	}

	/**
	 * Daemon process that constantly checks if FINS communication is
	 * initialized.  If not initialized, starts the initialize process
	 */
	private void startFinsInitializerDaemon() {
		Thread finsInitializerThread = new Thread() {
			public void run() {
				while (isActive()) {
					try {
						if (!isInitialized()) {
							initializeFins();
						} else {
							Thread.sleep(10);
						}							
					} catch (Exception ex) {
						ex.printStackTrace();
						getLogger().error("Unable to Initialize FINS communication to PLC");
					}
				}
			}
		};

		finsInitializerThread.start();
	}

	public StringBuilder readMemory(IPlcMemory memory) throws ResponseTimeoutException, NetworkCommunicationException {
		return readMemory(memory, true);
	}
	
	/**
	 * reads a plc memory location
	 */
	public StringBuilder readMemory(IPlcMemory memory, boolean logFinsTraffic)
		throws ResponseTimeoutException, NetworkCommunicationException {
		
		FinsMemoryReadRequest readRequest = new FinsMemoryReadRequest(memory, getDestinationNode(), getSourceNode(), READ_MEMORY_SERVICE_ID);
		getLogger().debug("Sending Memory READ request.");
		try {
			StringBuilder receivedStr = sendAndReceive(memory, readRequest.getMessageRequest(), logFinsTraffic);
			if (receivedStr.length() > 0) {
				return getDataFromFinsResponse(memory, receivedStr);
			}
		} catch (NetworkCommunicationException nex) {
			throw nex;
		} catch (ResponseTimeoutException rex) {
			throw rex;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not send READ memory request: " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return null;
	}

	private StringBuilder getDataFromFinsResponse(IPlcMemory memory, StringBuilder receivedStr) {
		StringBuilder retVal = processFinsReply(receivedStr);
		if (retVal != null) {
			if ((memory.getByteOrder() == ByteOrder.low) && (memory.getBitAddress() == -1)){
				return new StringBuilder(retVal.subSequence(1, retVal.length()));
			} 
		}
		return retVal;
	}

	/**
	 * reads the time from the PLC clock
	 */
	public StringBuilder readClock() {
		return readClock(true);
	}
	
	/**
	 * reads the time from the PLC clock
	 */
	public StringBuilder readClock(boolean logFinsTraffic) throws ResponseTimeoutException, NetworkCommunicationException {
		 FinsClockReadRequest clockReadRequest = new FinsClockReadRequest(getDestinationNode(), getSourceNode(), READ_CLOCK_SERVICE_ID);
	       getLogger().debug("Sending clock READ request.");
		try {
			return processFinsReply(sendAndReceive(null, clockReadRequest.getMessageRequest(), logFinsTraffic));
		} catch (NetworkCommunicationException nex) {
			throw nex;
		} catch (ResponseTimeoutException rex) {
			throw rex;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not send READ clock request: " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return null;
	}
	
	public boolean writeMemory(IPlcMemory memory, StringBuilder data) throws ResponseTimeoutException, NetworkCommunicationException {
		return writeMemory(memory, data, true);
	}
	
	/**
	 * writes to a plc memory location
	 */
	public boolean writeMemory(IPlcMemory memory, StringBuilder data, boolean logFinsTraffic) throws ResponseTimeoutException, NetworkCommunicationException {
		FinsMemoryWriteRequest readRequest = new FinsMemoryWriteRequest(memory, getDestinationNode(), getSourceNode(), WRITE_MEMORY_SERVICE_ID, data);
		getLogger().debug("Sending Memory WRITE request.");
		StringBuilder response = new StringBuilder();
		try {
			response = sendAndReceive(memory, readRequest.getMessageRequest(), logFinsTraffic);
			if (processFinsReply(response) != null)
				return true;
		} catch (NetworkCommunicationException nex) {
			throw nex;
		} catch (ResponseTimeoutException rex) {
			throw rex;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Exception in WRITE memory request: " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		
		getLogger().error("Could not send WRITE memory request");
		return false;
	}

	/**
	 * processes the PLC Fins reply 
	 * 
	 * @param reply
	 * @return
	 */
	private StringBuilder processFinsReply(StringBuilder reply) {
		FinsResponse finsResponse = null;
		try {
			FinsResponseBuilder finsResponseBuilder = new FinsResponseBuilder(new FinsResponseParser());
			finsResponse = finsResponseBuilder.buildFinsResponse(reply);
		}catch (UnknownResponseException ex) {
			getLogger().error("Unknown Fins Response received: " + reply);
			return null;
		}catch (Exception ex) {
			getLogger().error("Exception in Fins Response: " + reply);
			return null;
		}
		
		StringBuilder response = finsResponse.getData();
		switch (finsResponse.getFinsCommand()) {
			case INITIALIZE:
				setDestinationNode(finsResponse.getDestinationNode());
				setSourceNode(finsResponse.getSourceNode());
				setInitialized(true);
				setConnected(true);
				getLogger().info("SourceNode obtained from " + getId() + " plc: " + finsResponse.getSourceNode());
				getLogger().info("DestinationNode obtained from " + getId() + " plc: " + finsResponse.getDestinationNode());
				getLogger().debug("Successfully processed Initialize reply");
				break;
			case MEMORY_READ:
				getLogger().debug("Successfully processed Memory READ reply");
				return response;
			case MEMORY_WRITE:
				getLogger().debug("Successfully processed Memory WRITE reply");
				return response;
			case CLOCK_READ:
				getLogger().debug("Successfully processed Clock READ reply");
				break;
			case UNKNOWN:
				getLogger().debug("Unknown reply received");
				break;
		}
		return null;
	}
	
	public StringBuilder sendAndReceive(IPlcMemory memory, StringBuilder message) 
		throws ResponseTimeoutException, NetworkCommunicationException  {
		
		return sendAndReceive(memory, message, true);
	}

	/**
	 * 
	 * @param bytes
	 * @throws ResponsetimedOutException 
	 * @throws Exception
	 */
	public StringBuilder sendAndReceive(IPlcMemory memory, StringBuilder message, boolean logFinsTraffic) {
		
		if(!isConnected()) {
			throw new NetworkCommunicationException(StringUtil.replaceNonPrintableCharacters(message));
		}
		
		FinsMemoryRequestQueueItem queueItem = createRequestQueueItem(memory, message, logFinsTraffic);
		while (System.currentTimeMillis() < queueItem.getExpirationTime()) {
			if (queueItem.getReceivedMessage().length() > 0) {
				return queueItem.getReceivedMessage();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setConnected(false);
		String errMsg = new Integer(queueItem.getKey()).toString() + " " + StringUtil.replaceNonPrintableCharacters(queueItem.getSendMessage());
		if (queueItem.isProcessingStarted())
			throw new ResponseTimeoutException(errMsg);
		else
			throw new TooManyRequestsInQueueException(errMsg);
	}

	private FinsMemoryRequestQueueItem createRequestQueueItem(IPlcMemory memory, StringBuilder message, boolean logFinsTraffic) {
		FinsMemoryRequestQueueItem queueItem = new FinsMemoryRequestQueueItem(getRandomGen().nextInt(), memory, message, logFinsTraffic);
		queueItem.setRequestReceivedTime(System.currentTimeMillis());
		queueItem.setExpirationTime(System.currentTimeMillis() + ONE_MINUTE);
		getSendQueue().offer(queueItem);
		getLogger().debug("Current send queue size: " + getSendQueue().size() + " after adding " + queueItem.getKey());
		return queueItem;
	}

	/**
	 * finalizes IO streams associated with the plc device.  Only
	 * one instance of this method will execute at a given time,
	 * duplicate calls will be blocked and discarded.
	 */
	private void finalizeIOStreams() {
		// disregard excessive calls to this method
		boolean discard = false;
		while (_finalizingSocket) {
			try {
				discard = true;
				Thread.sleep(10);
			} catch (Exception ex) {}
		}
		
		if (discard)
			return;
		
		_finalizingSocket = true;
		getLogger().info("started finalizing IO streams");
		
		try {
			if (_reader != null)
				_reader.close();
		} catch (Exception ex) {}

		try {
			if (_writer != null) {
				_writer.flush();
			}
		} catch (Exception ex) {}
		
		try {
			if (_writer != null) {
				_writer.close();
			}
		} catch (Exception ex) {}

		try {
			if (_socket != null || !_socket.isClosed())
				_socket.close();
		} catch (Exception ex) {}

		_responseReader = null;
		_socket = null;
		_writer = null;
		_reader = null;
		_finalizingSocket = false;
		
		getLogger().info("finished finalizing IO streams");
	}

	/**
	 * @return the socket
	 */
	protected Socket getSocket() {
		try {
			if (_socket == null || _socket.isClosed() || !_socket.isConnected() || !_socket.isBound()) {
				getLogger().info("Attempting to create new socket connection to " + getHostName() + ":" + getPort());	
				_socket = TCPSocketFactory.getSocket(getHostName(), getPort(), getConnectionTimeout());
				getLogger().info("Successfully created new socket connection to " + getHostName() + ":" + getPort());
			}
		} catch (Exception ex) {
			_socket = null;
			ex.printStackTrace();
			getLogger().error("Could not create a new socket connection " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return _socket;
	}
	
	/**
	 * returns socket reader
	 * 
	 * @return
	 */
	protected BufferedReader getSocketReader() {
		try {
			if (_reader == null) {
                _reader = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), CharSets.ISO_8859_1));
				getLogger().info("Successfully created new socket reader");				
			}
		} catch (Exception ex) {
			_reader = null;
			ex.printStackTrace();
			getLogger().error("Could not create a new socket reader " + (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return _reader;
	}
	
	/**
	 * returns socket writer
	 * 
	 * @return
	 */
	protected BufferedWriter getSocketWriter() {
		try {
			if (_writer == null) {
                _writer = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream(), CharSets.ISO_8859_1));
				getLogger().info("Successfully created new socket writer");	
			}
		} catch (Exception ex) {
			_writer = null;
			ex.printStackTrace();
			getLogger().error("Could not create a new socket writer " +  (ex.getMessage() != null ? ex.getMessage() : ""));
		}
		return _writer;
	}
	
	public LinkedBlockingQueue<FinsMemoryRequestQueueItem> getSendQueue() {
		if (_sendQueue == null)
			_sendQueue = new LinkedBlockingQueue<FinsMemoryRequestQueueItem>();

		return _sendQueue;
	}

	public void setConnected(boolean connectionStatus) {
		boolean notify = _isConnected != connectionStatus;
		_isConnected = connectionStatus;
		if (!connectionStatus)
			setInitialized(false);
		if(notify) 
			notifyDeviceStatus();		
	}
	
	private Random getRandomGen() {
		return _random;
	}

	private FinsResponseReader getResponseReader() {
		if (_responseReader == null) {
			_responseReader = new FinsResponseReader(getSocketReader(), getLogger());
		}
		return _responseReader;
	}
	
	@Override
	public void activate() {
		super.activate();
		startDevice();
	}
	
	public boolean isInitialized() {
		return _initialized;
	}

	private void setInitialized(boolean isInitialized) {
		_initialized = isInitialized;
	}
	
	private boolean isSuspendQueueProcessing() {
		return _suspendQueueProcessing;
	}

	private void setSuspendQueueProcessing(boolean suspendQueueProcessing) {
		_suspendQueueProcessing = suspendQueueProcessing;
	}
	
	private int getResponseTimeout() {
		return _responseTimeout;
	}
	
	private void setResponseTimeout(int responseTimeout) {
		_responseTimeout = responseTimeout;
	}

	private int getConnectionTimeout() {
		return _connectionTimeout;
	}
	
	private void setConnectionTimeout(int connectionTimeout) {
		_connectionTimeout = connectionTimeout;
	}
	
	public String getHostName() {
		return _hostName;
	}
	
	private void setHostName(String hostName) {
		_hostName = hostName;
	}
	
	public int getPort() {
		return _port;
	}
	
	private void setPort(int port) {
		_port = port;
	}

	public int getDestinationNode() {
		return _destinationNode;
	}
	
	public void setDestinationNode(int destinationNode) {
		_destinationNode = destinationNode;
	}

	public int getSourceNode() {
		return _sourceNode;
	}

	public void setSourceNode(int sourceNode) {
		_sourceNode = sourceNode;
	}
}
