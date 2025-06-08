package com.honda.galc.device.mitsubishi;

import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.property.MitshubishiDevicePropertyBean;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DeviceData;
import com.honda.galc.device.DeviceFormatGroup;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.DevicePlcData;
import com.honda.galc.device.DevicePoint;
import com.honda.galc.device.DeviceType;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.device.DriverBase;
import com.honda.galc.device.DriverDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.device.IPlcData;
import com.honda.galc.device.PlcDataBlock;
import com.honda.galc.device.PlcDataChangeEventQueueHandler;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.property.DevicePropertyBean;

/**
 * 
 * <h3>QnADriver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnADriver description </p>
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
 * Nov 5, 2010
 *
 */

public abstract class QnADriver extends DriverBase implements IDeviceDriver{
	protected MitsubishiPlcSocket socket = null;
	protected MitshubishiDevicePropertyBean property;
	private String ipAddress;
	private int port;
	protected QnAParser parser;
	private List<DriverDevice> deviceList = new ArrayList<DriverDevice>();
	private LinkedBlockingQueue<IPlcData> queue;
	private PlcDataChangeEventQueueHandler plcDataChangeEventQueueHandler;
	
	
	public QnADriver() {
		super();
	}

	protected void initialize() {
		setId(property.getDeviceId());
		this.ipAddress = property.getIpAddress();
		this.port = property.getPort();
		queue = new LinkedBlockingQueue<IPlcData>();
		parser = new QnAParser(getLogger());
		
		getLogger().info(this.getClass().getSimpleName(), " connect to ", this.ipAddress, ":" + this.port );

		createMitshubishiSocket();
		
		startDeviceMonitor();
	
	}

	protected void createMitshubishiSocket() {
		try {
			socket = new MitsubishiPlcSocket(ipAddress, port, property.getConnectionTimeout(),
					property.getConnectionTimeout(), getLogger());
			
			setDeviceStatus(true);
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to create Mitshubishi socekt on ", ipAddress, ":" + port);
			setDeviceStatus(false);
		}
	}

	protected void setDeviceStatus(boolean connected) {
		setConnected(connected);
		setEnabled(connected);
		if(connected) activate();
	}

	private void startDeviceMonitor() {
		final Timer timer = new Timer();
		
		if(getInetAddress() == null){
			getLogger().error("ERROR: Failed to get Inet Address for ", ipAddress, " device monitor is not start!!!");
			return;
		}
		
		TimerTask timerTask = new TimerTask() {
			public void run() {
				initThread("MonitorThread");
				while (isActive()) {
					try {
						boolean reachable = checkDevice();
						if(reachable != isConnected()){
							notifyListeners(new ConnectionStatus(getId(), reachable));
							setConnected(reachable);
							getLogger().warn("Mitshubishi Plc:", getId(), " online-status changed. reachable:" + reachable);
						}

						Thread.sleep(property.getPingInterval());
					} catch (Exception ex) {
						getLogger().error(ex,"Could not perform device monitor check");
					}
				}
				timer.cancel();
				getLogger().info("Exiting monitor thread." + getId());
			}
			
		};

		//start after 500ms
		timer.schedule(timerTask, 500);

	}

	protected ByteBuffer createReadCommand(String address, QnASubCommand subcmd,
			int points) {
		QnABinaryMessage qnaMessage = new QnABinaryMessage(address, subcmd, points);
		return qnaMessage.createReadCommand();
		
	}
	
	public void handleUnsolicitedPacket(byte[] receiveDataFromPlc) {
		if(receiveDataFromPlc.length >= 16){
			String address = getUnsolicitedAddress(receiveDataFromPlc);
			byte[] data = getUnsolicitedData(receiveDataFromPlc);
			addToQueue(new PlcDataBlock(address, data));
			getLogger().info("received unsolicited data:" + DeviceUtil.toHex(receiveDataFromPlc));
		} else {
			getLogger().warn("received incomplete Unsolicited data, length:" + receiveDataFromPlc.length);
		}
	}


	private byte[] getUnsolicitedData(byte[] receiveDataFromPlc) {
		int size = receiveDataFromPlc.length - 14;
		byte[] data = new byte[size];
		System.arraycopy(receiveDataFromPlc, 14, data, 0, size);
		return data;
	}

	private String getUnsolicitedAddress(byte[] receiveDataFromPlc) {
		
		getLogger().info("Received Unsolicited:" + DeviceUtil.toHex(receiveDataFromPlc));
		ByteBuffer bf = ByteBuffer.wrap(receiveDataFromPlc);
		bf.order(ByteOrder.LITTLE_ENDIAN);
		int address = (bf.get(8) & 0xFF) + (bf.get(9) << 8 & 0xFF00)+ (bf.get(10) << 16);
		byte deviceCode = bf.get(11);
		String deviceByCode = QnADevice.getQnADeviceByCode(deviceCode).toString();
		
		if(deviceByCode == null){
			getLogger().warn("Failed to get device code for unsolicited data:" + deviceCode);
		}
		
		return deviceByCode + address;
	}
	
	public void write(Device device, IDeviceData data) {
		for(DeviceFormat format : device.getDeviceDataFormats()){
			write(format, getValue(format, data));
		}
	}

	protected boolean isResponse(byte[] receiveDataFromPlc) {
		return receiveDataFromPlc[0] == (byte)0xD0; //-48
	}
	
	protected boolean isUnsolicitedPacket(byte[] receiveDataFromPlc) {
		return receiveDataFromPlc[0] == 0x60; //96
	}

	public String getIpAddress() {
		
		if(ipAddress == null) return property.getIpAddress();
		return ipAddress;
	}


	public int getPort() {
		
		if(port == 0) return property.getPort();
		return port;
	}


	public MitshubishiDevicePropertyBean getProperty() {
		return property;
	}


	public void setProperty(MitshubishiDevicePropertyBean property) {
		this.property = property;
	}


	public Object read(Device device) throws Exception {
		DriverDevice driverDevice = getDriverDevice(device.getClientId());
		
		if(driverDevice.isReadByGroup())
			return readDeviceByGroup(driverDevice);
		else
			return readDeviceByItem(driverDevice);
	}
	
	public void write(Device device, DataContainer dataContainer) {
		for(DeviceFormat format : device.getDeviceDataFormats()){
			if(format.getDeviceTagType() == DeviceTagType.DEVICE){
				write(format, getDeviceFormatDataFromContainer(dataContainer, format));
			}
		}
	}

	private Object readDeviceByItem(DriverDevice driverDevice) throws Exception {
		getLogger().info("Read device:",driverDevice.getDevice().getClientId(), " by Item.");
		DataContainer dc = new DefaultDataContainer();
		
		for(DeviceFormat format : driverDevice.getDevice().getDeviceDataFormats()){
			if(format.getDeviceTagType() == DeviceTagType.DEVICE){
				DevicePoint read = read(format);
				dc.put(read.getName(), read.getValue().toString());
			}
		}
		
		if(!StringUtils.isEmpty(driverDevice.getDevice().getAliasName())){
			return driverDevice.getDeviceData().convertPlcData(dc);
		} else
			return dc;

	}
	
	private Object readDeviceByGroup(DriverDevice device) throws Exception {
		getLogger().info("Read device:" + device.getDevice().getClientId() + " by group.");
		DataContainer dc = new DefaultDataContainer();
		
		List<PlcDataBlock> dataList = new ArrayList<PlcDataBlock>();
		for(DeviceFormatGroup group : device.getDeviceFormatGroupMap().values()){
			dataList.add(new PlcDataBlock(group.getId(), QnAParser.getReadPacketData(read(group, true))));
		}
		
		if(dataList.size() == 0 ) return dc;
		
		return device.getData(new DevicePlcData(device.getDevice().getClientId(), dataList));
	}


	public List<DriverDevice> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> devices) {
		for(Device dev : devices){
			DriverDevice driverDevice = new DriverDevice(dev, getLogger());
			if(!deviceList.contains(driverDevice)){
				deviceList.add(driverDevice);
			}
		}
	}
	
	protected DriverDevice getDriverDevice(String deviceId) {
		for(DriverDevice dev : deviceList){
			if(dev.getDevice().getClientId().equals(deviceId))
				return dev;
		}
		return null;
	}

	public void addToQueue(IPlcData data) {
		try {
			getQueue().put(data);
		} catch (Exception e) {
			getLogger().error(e, "Failed to add data to queue.");
			e.printStackTrace();
		}
		
	}

	public LinkedBlockingQueue<IPlcData> getQueue() {
		return queue;
	}

	
	@Override
	public void send(byte[] output) {
		socket.sendDataToPlc(output);
	}

	protected void startDataChangeEventQueueHandler() {
		plcDataChangeEventQueueHandler = new PlcDataChangeEventQueueHandler(this, getDriverDeviceList());
		Thread t = new Thread(plcDataChangeEventQueueHandler);
		t.start();
		
	}
	
	public List<DriverDevice> getDriverDeviceList(){
		return deviceList;
	}

	
	public void setDeviceData(List<DeviceData> deviceDataList){
		for(DeviceData deviceData: deviceDataList){
			deviceList.add(new DriverDevice(deviceData, getLogger()));
		}
	}

	public void registerDeviceListener(DeviceListener listener,
			List<IDeviceData> deviceDataList) {
		for(DriverDevice dev: deviceList){
			for(IDeviceData devdata : deviceDataList){
				if(dev.getDevice().getAliasName().equals(devdata.getClass().getSimpleName()))
					dev.registerListener(listener);
				
			}
		}
		
	}

	public void setDeviceProperty(DevicePropertyBean devicePropertyBean) {
		property = (MitshubishiDevicePropertyBean)devicePropertyBean;
		setId(property.getDeviceId());
	}
	
	protected void sendUnsolicitedReply() {
		getLogger().info("sent:", DeviceUtil.toHex(QnABinaryMessage.unsolicitedReply));
		send(QnABinaryMessage.unsolicitedReply);

	}
	
	private InetAddress getInetAddress() {
		if (inetAddress == null) {
			try {
				return inetAddress = InetAddress.getByName(ipAddress);
			} catch (Exception e) {
				getLogger().error(e, "Exception to get inet address for " + ipAddress);
			}
		}
		return inetAddress;
	}
	
	private boolean checkDevice() {
		try {
			return inetAddress.isReachable(10000);
		} catch (SocketException se){
			if("Permission denied: Can't send ICMP packet".equals(se.getMessage()))
					return true; //never mind
			else {
				getLogger().error(se, "Exception to check device ", inetAddress.getHostAddress());
				return false;
			}
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to check device ", inetAddress.getHostAddress());
			return false;
		}
	}
	
	public void stopChangeEventQueueHandler(){
		plcDataChangeEventQueueHandler.setStop(true);
	}
	
	//delegate methods
    public String getClassName(){
    	return getProperty().getClassName();
    }
	
	public long getScanRate(){
		return getProperty().getScanRate();
	}
	
	public long getPingInterval(){
		return getProperty().getPingInterval();
	}
	
	public DeviceType getType(){
		return DeviceType.valueOf(getProperty().getType());
	}

	public String getPingPoint(){
		return getProperty().getPingPoint();
	}

	public int getConnectionTimeout(){
		return getProperty().getConnectionTimeout();
	}
	
	public String getDeviceId(){
		return getProperty().getDeviceId();
	}
	


	
}
