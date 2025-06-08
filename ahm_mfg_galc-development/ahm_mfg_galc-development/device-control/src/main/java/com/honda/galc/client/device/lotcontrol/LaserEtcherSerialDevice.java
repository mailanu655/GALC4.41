package com.honda.galc.client.device.lotcontrol;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.property.LaseretcherDevicePropertyBean;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.printing.PrintAttributeConvertor;

public class LaserEtcherSerialDevice extends AbstractPrintDevice implements Runnable {

	private int _baudRate = 9600;
	private int _dataBits = SerialPort.DATABITS_8;
	private String _eocMessage = "";
	private String ETCH_COMPLETED_RECEIVE_MSG = "LASER READY";
	private String ETCH_COMPLETED_DISPLAY_MSG = "Etching completed";
	private int _parity = SerialPort.PARITY_NONE;
	private String _portName = "";
	private int _stopBits = SerialPort.STOPBITS_1;
	private int _timeOut = 90000;
	private SerialPort _serialPort = null;
	private OutputStream _out = null;
	private InputStream _in = null;
	private String _vin = "";
	private String _seqNum = "";
	private HashMap<String,IPrintDeviceListener> _listeners = new HashMap<String,IPrintDeviceListener>();
	public IPrintDeviceListener master = null;
	public IPrintDeviceListener prevMaster = null;
	private volatile LinkedBlockingQueue<LaserEtchRequestItem> sendQueue = new LinkedBlockingQueue<LaserEtchRequestItem>();

	public LaserEtcherSerialDevice() {
	}

	public void releaseControl(){
		master=prevMaster;
		if (prevMaster!=null){
			prevMaster.controlGranted(this.getId());
		}
		prevMaster=null;
	}
	
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		LaseretcherDevicePropertyBean property = (LaseretcherDevicePropertyBean) propertyBean;
		super.setDeviceProperty(propertyBean);
		_baudRate = property.getBaudrate();
		_dataBits = property.getDatabits();
		_eocMessage = property.getEocmessage();
		_portName = property.getPortname();
		_parity = property.getParity();
		_timeOut = property.getTimeout();
		_stopBits = property.getStopbits();
		_isConnected = property.isConnected();
	}

	public void run() {
		CommPortIdentifier portIdentifier =null;
		try {
			initThread("LaserEtcherSerialDevice_Main");
			try{
				portIdentifier = CommPortIdentifier.getPortIdentifier(getPortName());
			} catch(NoSuchPortException ex){
				ex.printStackTrace();
				getLogger().check("Not able to identify com port");
			} catch(Exception ex){
				ex.printStackTrace();
				getLogger().error("exception occured at com port");
			}
			getLogger().info("Attempting to connect to serial port " + getPortName(), "run");

			if (portIdentifier.isCurrentlyOwned())
				getLogger().check("Port " + getPortName() + "already in use!", "run");
			else {
				
				// points who owns the port and connection timeout
				setSerialPort((SerialPort) portIdentifier.open("LaserEtcherSerialDevice", getTimeOut()));
				
				// setup connection parameters
				getSerialPort().setSerialPortParams(getBaudRate(), getDataBits(), getStopBits(), getParity());

				// set end of input character
				getSerialPort().setEndOfInputChar(" ".getBytes()[0]);
				getLogger().check("Successfully connected to serial port " + getPortName(), "run");
				_active = true;
				// start receive listener
				receive();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			_active = false;
		} finally {
			closeSerialPort();
		}
	}

	public boolean registerListener(String applicationId, IPrintDeviceListener listener) {
		synchronized (getListeners()) {
			// add if already not registered
			if (!getListeners().containsKey(applicationId))
				getListeners().put(applicationId,listener);
		}
		return true;
	}

	public boolean unregisterListener(String applicationId, IPrintDeviceListener listener) {
		synchronized (getListeners()) {
			// delete if already registered
			if (getListeners().containsKey(applicationId))
				getListeners().remove(applicationId);
		}
		setConnected(false);
		return true;
	}

	public void notifyListeners(final PrintDeviceStatusInfo status) {
		getLogger().info("notify listeners");
		synchronized (getListeners()) {
			Iterator iter = getListeners().keySet().iterator();
			while(iter.hasNext()) {
				final IPrintDeviceListener listener = getListeners().get(iter.next());
				if(listener == master){
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");

						try {
							listener.handleStatusChange(status);
							getLogger().info("Successfully notified status change to listener: " + listener.getId());
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex.getMessage(), "notifyListeners");
							getLogger().error(ex, "Torque status change handling unsuccessful for "	+ listener.getId());
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
		}
	}

	protected void initThread(String threadName) {
		Thread.currentThread().setName(threadName);
	}

	public HashMap<String, IPrintDeviceListener> getListeners() {
		return _listeners;
	}

	public boolean closeSerialPort() {
		boolean retVal = false;

		try {
			getLogger().info("Attempting to release serial port " + getPortName(), "closeSerialPort");
			if (getSerialPort() != null)
				getSerialPort().close();

			getLogger().info("Serial port " + getPortName() + " successfully released!", "closeSerialPort");
			retVal = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().info(ex.getMessage(), "closeSerialPort");
			getLogger().info("Serial port " + getPortName() + " could not be released!");
		}
		return retVal;
	}

	public synchronized boolean send(String message) {

		try {
			message = message.replaceAll("\r\n", "\r");
			message = message.replaceAll("\n", "\r");
			byte[] bytes = message.getBytes();
			getLogger().info(">>" + new String(bytes, 0, bytes.length) + ">>", "send");

			getOutStream().write(bytes);
			getOutStream().flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void receive() {
		StringBuffer strBuf = new StringBuffer();

		try {
			int b;
			getLogger().info("Inside receive");
			getLogger().info("active"+ isActive());
			while (isActive()) {
				// if stream is not bound read() returns -1
				while ((b = getInStream().read()) != -1)
					strBuf.append(((char) b));

				// Thread.sleep(10);
				if (strBuf.length() > 1) {
					// Remove the end of input char
					strBuf.deleteCharAt(strBuf.length() - 1);
					getLogger().info("<<" + strBuf.toString() + "<<", "receive");

					PrintDeviceStatusInfo statusInfo = createStatusInfo(strBuf);
					getLogger().info("LaserEtcherDeviceStatusInfo" + statusInfo);
					if (!sendQueue.isEmpty()) {
						sendQueue.take().setReceivedMessage(strBuf.toString());
					}
					notifyListeners(statusInfo);
					strBuf = new StringBuffer();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Exception caught receiving message in " + this.getClass().getName());
		}
	}

	private PrintDeviceStatusInfo createStatusInfo(StringBuffer strBuf) {
		PrintDeviceStatusInfo statusInfo = new PrintDeviceStatusInfo();
		statusInfo.setMessage(strBuf.toString());
		if (strBuf.toString().equals(ETCH_COMPLETED_RECEIVE_MSG) ) {
			statusInfo.setDisplayMessage(ETCH_COMPLETED_DISPLAY_MSG);
		} else {
			statusInfo.setDisplayMessage(strBuf.toString());
		}
		statusInfo.setVIN(getVin());
		statusInfo.setSeqNumber(getSeqNum());
		statusInfo.setDeviceId(getId());
		
		if (strBuf.toString().equals(getEocMessage())) {
			statusInfo.setMessageSeverity(DeviceMessageSeverity.success);
		} else {
			statusInfo.setMessageSeverity(DeviceMessageSeverity.info);
		}
		return statusInfo;
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
	public void deActivate() {
		_active = false;
	}

	public int getCurrentState() {
		return 0;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean isConnected() {
		return _isConnected;
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

	public void setEocMessage(String eocMessage) {
		_eocMessage = eocMessage;
	}

	public String getEocMessage() {
		return _eocMessage;
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

	public void setSerialPort(SerialPort serialPort) {
		_serialPort = serialPort;
	}

	public SerialPort getSerialPort() {
		return _serialPort;
	}

	public void setOutStream(OutputStream out) {
		_out = out;
	}

	public String getVin() {
		return _vin;
	}

	public void setVin(String vin) {
		_vin = vin;
	}

	public String getSeqNum() {
		return _seqNum;
	}

	public void setSeqNum(String seqNum) {
		_seqNum = seqNum;
	}

	private OutputStream getOutStream() {
		try {
			if (_out == null) {
				_out = getSerialPort().getOutputStream();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().info("Unable to open output stream to serial port " + getPortName(), "getOutStream");
		}
		return _out;
	}

	public void setInStream(InputStream in) {
		_in = in;
	}

	private InputStream getInStream() {
		try {
			if (_in == null)
				_in = getSerialPort().getInputStream();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().info("Unable to open input stream to serial port " + getPortName(), "run");
		}

		return _in;

	}

	public boolean requestControl(String applicationId,IPrintDeviceListener listener) {
		listener = getListeners().get(applicationId);
		if (master!=null && master != listener) {
			prevMaster = master;
			prevMaster.controlRevoked(this.getId());
		}
		master=listener;
		return true;
		
	}
	
	public String getprintData(DataContainer dc) {
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(getLogger());
		return printAttributeConvertor.getPrintData(dc);
	}

	public boolean print(String printData, int printQuantity, String productId) {
		boolean retVal = false;
		try{
			long expirationTime = System.currentTimeMillis() +_timeOut; 
			LaserEtchRequestItem etchItem = new LaserEtchRequestItem(printData);
			sendQueue.offer(etchItem);
			
			send(printData);
			while (System.currentTimeMillis() < expirationTime) {
				if (etchItem.getReceivedMessage().length() > 0) {
					if (StringUtils.trimToEmpty(etchItem.getReceivedMessage()).equals(ETCH_COMPLETED_RECEIVE_MSG)) {
						retVal = true;
					}
					break;
				}
				try {
					Thread.sleep(10);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			sendQueue.remove(etchItem);
			return retVal;
			
		}catch(Exception ex){
			ex.printStackTrace();
			getLogger().info("Etching not successful");
			return false;
		}
	}
}
