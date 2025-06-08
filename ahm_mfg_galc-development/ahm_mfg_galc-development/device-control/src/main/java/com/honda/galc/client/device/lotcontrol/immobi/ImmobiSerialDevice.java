package com.honda.galc.client.device.lotcontrol.immobi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.client.device.property.ImmobiDevicePropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.DeviceType;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.util.StringUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
@XStreamAlias("device")
public class ImmobiSerialDevice extends AbstractDevice implements  Runnable {
	
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id = "";

	@XStreamAlias("name")
	private String _name = "";

	@XStreamAlias("baudrate")
	private int _baudRate = 9600;

	@XStreamAlias("databits")
	private int _dataBits = SerialPort.DATABITS_7;

	@XStreamAlias("eoichar")
	private String _eoiChar = "";

	@XStreamAlias("parity")
	private int _parity = SerialPort.PARITY_EVEN;

	@XStreamAlias("type")
	private String _deviceType = "";

	@XStreamAlias("portname")
	private String _portName = "";

	@XStreamAlias("stopbits")
	private int _stopBits = SerialPort.STOPBITS_2;

	@XStreamAlias("timeout")
	private int _timeOut = 90000;	

	@XStreamAlias("enabled")
	private boolean _enabled = false;	

	private volatile boolean _active = false;
	private volatile Hashtable<String, IImmobiDeviceListener> _listeners = null;	
	private ImmobiMessageHandler messageHandler = new ImmobiMessageHandler();

	private String _clientId = ""; 
	private String _applicationId = "";

	private ImmobiStateMachine _stateMachine = null;
	private SerialPort _serialPort = null;

	private OutputStream _out = null;
	private InputStream _in = null;

	private String _vin = "";
	private String _mtoc = "";
	private String _seqNum = "";

	public static final char STX = 0x02;
	public static final char ETX = 0x03;
	private static final int MTOC_LENGTH = 9;

	private volatile ImmobiStateMachineType stateMachineType = null;
	private volatile String firstKeyScan = null;
	private volatile String secondKeyScan = null;

	public ImmobiSerialDevice()	{}

	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		ImmobiDevicePropertyBean property = (ImmobiDevicePropertyBean) propertyBean;		
		_baudRate = property.getBaudrate();
		_dataBits = property.getDatabits();
		_portName = property.getPortname();
		_parity = property.getParity();
		_timeOut = property.getTimeout();
		_stopBits = property.getStopbits();
	}

	public boolean processVIN(String vin, String mtoc, String seqNum) throws Exception
	{
		try	{
			setVIN(vin);
			setMTOC(mtoc.trim());
			setSeqNum(seqNum);
			ImmobiMessage replyMsg = send(ImmobiMessageType.vin, _vin);
			setState(ImmobiStateMachine.READY_FOR_SEND_VIN, replyMsg);

		}
		catch(Exception ex)
		{
			Logger.getLogger().warn("Did not process VIN: " + vin);
			throw ex;
		}
		return true;
	}


	public ImmobiStateMachine getStateMachine() 
	{
		if (_stateMachine == null)
		{
			_stateMachine = new ImmobiStateMachine();
		}

		return _stateMachine;
	}


	public void setStateMachine(ImmobiStateMachine stateMachine) {
		_stateMachine = stateMachine;
	}


	public String getId()
	{
		return _id;
	}


	public void run() 
	{   
		try {
			initThread("ImmobiSerialDevice_Main");			
			_active = true;

			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(getPortName());
			logMessage("Attempting to connect to serial port " + getPortName(), "run");

			if (portIdentifier.isCurrentlyOwned())
				logMessage("Port " + getPortName() + "already in use!", "run");
			else {
				setSerialPort((SerialPort) portIdentifier.open("ImmobiSerialDevice", getTimeOut()));
				getSerialPort().setSerialPortParams(getBaudRate(), getDataBits(), getStopBits(), getParity());

				getSerialPort().setEndOfInputChar(" ".getBytes()[0]);
				logMessage("Successfully connected to serial port " + getPortName(), "run");

				if(!messageHandler.isAlive()) messageHandler.start();
				receive();
			}
		}
		catch(Exception ex)	{
			ex.printStackTrace();			
		}
		finally {
			_active = false;
			closeSerialPort();			
		}		
	}

	public void send(String message)
	{
		try {

			logMessage(">>" + message + ">>", "send");

			message = getSTX() + message + getETX();
			byte[] bytes = message.getBytes();        
			getOutStream().write(bytes);
			getOutStream().flush();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	public void receive()
	{
		StringBuffer strBuf = new StringBuffer();

		try {
			int b;

			while (isActive()) {
				while((b = getInStream().read()) != -1) {
					strBuf.append(((char) b));

					if (Integer.valueOf((char) b) == ETX) {
						final String recdMsg = strBuf.toString().replaceAll(getETX(), "").replaceAll(getSTX(), "");
						logMessage("<<" + recdMsg + "<<", "receive");
						strBuf = new StringBuffer();
						if (!StringUtils.isEmpty(recdMsg.trim())) {
							ImmobiMessageItem msgItem = new ImmobiMessageItem(this, recdMsg, getLogger());
							if (messageHandler.enqueue(msgItem)) {
								logMessage("Valid message received: " + recdMsg + " added to queue", "receive");
							} else {
								logMessage("Valid message received: " + recdMsg + " unable to add to queue", "receive");
							}
						} else {
							logMessage("Empty message received: Not added to queue", "receive");
						}
					}
				}

				Thread.sleep(10);                
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void handleMessage(ImmobiMessage msg) {	
		
		getStateMachine().setDisplayMessage(msg.getDescription());
		String errMsg = msg.getSegmentData();
		ImmobiMessage replyMsg = null;
		try
		{
			switch (msg.getMessageType())
			{
			case vin_ack:	
				setState((getCurrentState() | ImmobiStateMachine.VIN_ACK) & ~ImmobiStateMachine.VIN_NG ,msg);
				if(msg.getSegmentData().trim().equals(getVIN())) {
					replyMsg = send(ImmobiMessageType.mtoc, _mtoc);
					setState((getCurrentState() | ImmobiStateMachine.SEND_MTOC),replyMsg);
					return;
				} else {
					errMsg = "VIN: " + errMsg + " does not match";
					send(ImmobiMessageType.vin_ng, msg.getSegmentData().trim());
				}
				break;

			case mtoc_ack:
				if(getStateMachineType().equals(ImmobiStateMachineType.WITHOUT_KEYSCAN))
				{
					setState((getCurrentState() | ImmobiStateMachine.MTOC_ACK) & ~ImmobiStateMachine.MTOC_NG,msg);
					if(msg.getSegmentData().trim().equals(getMTOC().trim())) {
						replyMsg = send(ImmobiMessageType.mtoc_ok, null);
						setState((getCurrentState() | ImmobiStateMachine.SEND_MTOC_OK),replyMsg);
						return;
					} else {
						errMsg = "MTOC: " + errMsg + " does not match";
						send(ImmobiMessageType.mtoc_ng, msg.getSegmentData().trim());
						}
				}else
				{
					setState((getCurrentState() | ImmobiStateMachine.MTOC_ACK) & ~ImmobiStateMachine.MTOC_NG,msg);		
					String keyStr=StringUtil.padRight(StringUtil.padRight(getFirstKeyScan(), 198, ' ', true)+","+StringUtil.padRight(getSecondKeyScan(), 198, ' ', true)+",", 596, ' ', true);				
					if(msg.getSegmentData().trim().equals(getMTOC().trim()))
					{
						replyMsg = send(ImmobiMessageType.key, keyStr);
					    setState((getCurrentState() | ImmobiStateMachine.SEND_KEY_ID),replyMsg);
					    return;
					}else {
						errMsg = "MTOC: " + errMsg + " does not match";
						send(ImmobiMessageType.mtoc_ng, msg.getSegmentData().trim());
					}								
				}
				break;

			case key_ack:	
				setState((getCurrentState() | ImmobiStateMachine.KEY_ID_ACK) & ~ImmobiStateMachine.KEY_ID_NG ,msg);
				replyMsg = send(ImmobiMessageType.key_ok, null);
				setState((getCurrentState() | ImmobiStateMachine.SEND_KEY_ID_OK),replyMsg);
				return;

			case reg_done:	
				setState((getCurrentState() | ImmobiStateMachine.REG_DONE) & ~ImmobiStateMachine.REG_NG & ~ImmobiStateMachine.ERR,msg);
				if (isRegOK(msg)) {
					replyMsg = send(ImmobiMessageType.reg_ok, null);
					setState((getCurrentState() | ImmobiStateMachine.SEND_REG_OK),replyMsg);
					return;
				} else {
					errMsg = "VIN/MTO: " + errMsg.trim() + " does not match";
					send(ImmobiMessageType.reg_ng, msg.getSegmentData().trim());
				}
				break;

			case err:
			case abort:
				errMsg = "Abort/Error message received. Please retry VIN " + getVIN();
				break;

			default:
				break;		
			}

			abort(errMsg,msg);
		}
		catch(Exception ex)
		{
			Logger.getLogger().error(ex.getMessage());
			ex.printStackTrace();			
		}
	}

	private ImmobiMessage send(ImmobiMessageType msgType, String segmentData) {

		ImmobiMessage immobiMsg = msgType.createMessage();

		if (segmentData != null) {
			immobiMsg.setLength(StringUtil.padLeft(Integer.toString(ImmobiMessage.HEADER_SIZE + ImmobiMessage.LENGTH_FIELD_SIZE	+ segmentData.length() ),ImmobiMessage.LENGTH_FIELD_SIZE ,'0' ));
			immobiMsg.setDataSegmentLength(StringUtil.padLeft(Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE), ImmobiMessage.LENGTH_FIELD_SIZE,'0'));
			immobiMsg.setSegmentData(segmentData);
		}

		send(immobiMsg.toString());
		return immobiMsg;
	}

	private boolean isRegOK(ImmobiMessage msg) {		
		boolean retVal = false;
		if (getStateMachineType().equals(ImmobiStateMachineType.WITHOUT_KEYSCAN)&&getCurrentState() == ImmobiStateMachine.READY_FOR_SEND_REG_OK 
				|| getStateMachineType().equals(ImmobiStateMachineType.WITH_KEYSCAN)&&getCurrentState() == ImmobiStateMachine.READY_FOR_SEND_REG_OK_WITH_KEYSCAN) {
			if(msg.getSegmentData().trim().contains("," + getMTOC().substring(0, MTOC_LENGTH-1)) && 
					msg.getSegmentData().trim().startsWith(getVIN()))
				retVal = true;
		} else {
			getStateMachine().setDisplayMessage("StateMachine: Not ready to send REG OK");
		}

		return retVal;
	}


	public void abort(String abortMessage, ImmobiMessage msg) {
		try {		
			setState(ImmobiStateMachine.ABORT, msg);
			getStateMachine().setDisplayMessage("");
			getStateMachine().setErrorMessage(abortMessage);
		} catch(Exception ex) {
			Logger.getLogger().error(ex.getMessage());
			ex.printStackTrace();		
		}
	}

	public boolean closeSerialPort() {
		boolean retVal = false;

		try {
			logMessage("Attempting to release serial port " + getPortName(), "closeSerialPort");
			if (getSerialPort() != null)
				getSerialPort().close();

			logMessage("Serial port " + getPortName() + " successfully released!", "closeSerialPort");
			retVal = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			logMessage(ex.getMessage(), "closeSerialPort");
			logMessage("Serial port " + getPortName() + " could not be released!" + ex.getMessage(), "closeSerialPort");
		}

		return retVal;
	}

	private OutputStream getOutStream()
	{
		try {
			if (_out == null)
				_out = getSerialPort().getOutputStream();
		} catch(Exception ex) {
			ex.printStackTrace();
			logMessage("Unable to open output stream to serial port " + getPortName() + ex.getMessage(), "getOutStream");
		}
		return _out;
	}

	private InputStream getInStream()
	{
		try {
			if (_in == null)
				_in = getSerialPort().getInputStream();
		} catch(Exception ex) {
			ex.printStackTrace();
			logMessage("Unable to open input stream to serial port " + getPortName() + ex.getMessage(), "getInStream");
		}

		return _in;
	}


	public static void listAvailableSerialPorts()
	{
		Enumeration ports = CommPortIdentifier.getPortIdentifiers();
		Logger.getLogger().info("Ports available: ");

		while(ports.hasMoreElements())
			Logger.getLogger().info(((CommPortIdentifier)ports.nextElement()).getName());
	}

	public void setSerialPort(SerialPort serialPort)
	{
		_serialPort = serialPort;
	}

	public SerialPort getSerialPort()
	{
		return _serialPort;
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public synchronized void activate() {

		int i =0;
		(new Thread(this)).start();
		while(!_active&& i< 50)

		{	
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			i++;
		}	

		if(!_active)
		{
			try {
				throw new Exception("Serial Port is not active");
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void deActivate() 
	{
		_active = false;
	}

	public String getApplicationId() 
	{
		return getClientId() + "_" + getId() ;
	}

	public String getClientId() 
	{
		return _clientId;
	}

	public DeviceType getType()
	{
		return Enum.valueOf(DeviceType.class, _deviceType);
	}

	public void setType(DeviceType deviceType)
	{
		_deviceType = deviceType.toString();
	}

	public String getName() 
	{
		return _name;
	}

	public boolean isActive() 
	{
		return _active;
	}

	public void setApplicationId(String applicationId) 
	{
		_applicationId = applicationId;		
	}

	public void setClientId(String clientId) 
	{
		_clientId = clientId;
	}

	public void setId(String id) 
	{
		_id = id;
	}

	public void setName(String name)
	{
		_name = name;		
	}

	public boolean registerListener(IImmobiDeviceListener listener)
	{
		synchronized(getListeners())
		{
			if (!getListeners().containsKey(listener.getListenerId()))
				getListeners().put(listener.getListenerId(), listener);
		}
		return true;		
	}

	public boolean unregisterListener(IImmobiDeviceListener listener)
	{
		synchronized(getListeners())
		{
			if (getListeners().containsKey(listener.getListenerId()))
				getListeners().remove(listener.getListenerId());
		}
		return true;
	}

	public void notifyListeners(ImmobiDeviceStatusInfo status) 
	{
		synchronized (getListeners()) {
			Collection<IImmobiDeviceListener> listeners = getListeners().values();
			Iterator iterator = listeners.iterator();

			while(iterator.hasNext()) {
				final IImmobiDeviceListener listener = (IImmobiDeviceListener) iterator.next();
				final ImmobiDeviceStatusInfo info = status;

				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");

						try	{
							listener.handleStatusChange(info);
							logMessage("Successfully notified status change to listener: " + listener.getListenerId(), "notifyListeners");
						} catch(Exception ex) {
							ex.printStackTrace();
							logMessage(ex.getMessage(), "notifyListeners");
							logMessage("Immobi status change handling unsuccessful for " + listener.getListenerId() + ": " + ex.getMessage(), "notifyListeners");
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();					
			}
		}
	}

	public Hashtable<String, IImmobiDeviceListener> getListeners()
	{		
		if (_listeners == null)
			_listeners = new Hashtable<String, IImmobiDeviceListener>();

		return _listeners;
	}

	public void initThread(String threadName)
	{
		Thread.currentThread().setName(threadName);
		Logger.getLogger( getApplicationId());
	}

	private void logMessage(String msg, String methodName) {
		Logger.getLogger().info(msg);
	}

	public int getCurrentState() {
		return getStateMachine().getCurrentState();
	}

	public void setState(int state, ImmobiMessage immobiMsg) {
		setState(state);	
		ImmobiDeviceStatusInfo statusInfo = new ImmobiDeviceStatusInfo();                    
		statusInfo.setMessageSeverity(DeviceMessageSeverity.valueOf(immobiMsg.getSeverity()));
		statusInfo.setState(state);
		statusInfo.setVIN(getVIN());
		statusInfo.setSeqNumber(getSeqNum());
		statusInfo.setDisplayMessage(immobiMsg.getDescription());
		notifyListeners(statusInfo); 
	}

	public void setState(int state) {
		getStateMachine().setCurrentState(state);	
	}

	public void setPortName(String portName) {
		_portName = portName;
	}

	public String getPortName() {
		return _portName;
	}

	public void setBaudRate(int baudRate) {
		_baudRate = baudRate;
	}

	public int getBaudRate() {
		return _baudRate;
	}

	public void setDataBits(int dataBits) {
		_dataBits = dataBits;
	}

	public int getDataBits() {
		return _dataBits;
	}

	public void setParity(int parity) {
		_parity = parity;
	}

	public int getParity() {
		return _parity;
	}

	public void setStopBits(int stopBits) {
		_stopBits = stopBits;
	}

	public int getStopBits() {
		return _stopBits;
	}

	public void setTimeOut(int timeOut) {
		_timeOut = timeOut;
	}

	public int getTimeOut() {
		return _timeOut;
	}

	public void setEndOfInputChar(String eoiChar) {
		_eoiChar = eoiChar;
	}

	public String getEndOfInputChar() {
		return _eoiChar;
	}



	public String getVIN() {
		return _vin;
	}


	public void setVIN(String vin) {
		_vin = vin;
	}


	public String getMTOC() {
		return _mtoc;
	}


	public void setMTOC(String mtoc) {
		_mtoc = mtoc;
	}


	public String getSeqNum() {
		return _seqNum;
	}


	public void setSeqNum(String seqNum) {
		_seqNum = seqNum;
	}


	public String getETX()
	{
		return Character.toString(ETX);
	}


	public String getSTX()
	{
		return Character.toString(STX);
	}

	public ImmobiStateMachineType getStateMachineType() {
		return stateMachineType;
	}

	public void setStateMachineType(ImmobiStateMachineType stateMachineType) {
		this.stateMachineType = stateMachineType;
	}

	public String getFirstKeyScan() {
		return firstKeyScan;
	}

	public void setFirstKeyScan(String firstKeyScan) {
		this.firstKeyScan = firstKeyScan;
	}

	public String getSecondKeyScan() {
		return secondKeyScan;
	}

	public void setSecondKeyScan(String secondKeyScan) {
		this.secondKeyScan = secondKeyScan;
	}

	public boolean isConnected() {
		return _isConnected;
	}
	
	public ImmobiMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(ImmobiMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
}

