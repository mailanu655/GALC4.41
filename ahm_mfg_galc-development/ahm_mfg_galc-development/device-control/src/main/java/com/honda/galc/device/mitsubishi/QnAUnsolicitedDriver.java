package com.honda.galc.device.mitsubishi;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.honda.galc.device.DeviceFormatGroup;
import com.honda.galc.device.DevicePoint;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.net.ConnectionStatus;
/**
 * 
 * <h3>QnAUnsolicitedDriver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnAUnsolicitedDriver description </p>
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
 * Oct 28, 2010
 *
 */
public class QnAUnsolicitedDriver extends QnADriver{

    private QnASocketReader reader;
	
	public QnAUnsolicitedDriver() {
		super();
	}
	
	public byte[] receiveDataFromPlc() throws IOException, Throwable{
		return socket.receiveDataFromPlc();
	}

	public DevicePoint read(DeviceFormat deviceFormat) throws Exception {
		QnABinaryMessage qnaMessage = new QnABinaryMessage(deviceFormat);
		ByteBuffer readCmd = qnaMessage.createReadCommand();
		byte[] input = sendAndWait(readCmd.array());

		Object returnObject = parser.decode(deviceFormat, input, qnaMessage.getSubCommand());
		return DeviceUtil.converToDevicePoint(deviceFormat, returnObject);
	}

	public byte[] read(String address, QnASubCommand subcmd, int points, boolean islog) throws Exception {
		ByteBuffer readCmd = createReadCommand(address, subcmd, points);
		
		if(islog)
			getLogger().info("send: " + readCmd.array().length, ":", DeviceUtil.toHex(readCmd.array()) );
			
		return sendAndWait(readCmd.array());
	}
	
	public byte[] read(DeviceFormatGroup group, boolean islog) throws Exception {
		return read(group.getStartAddress(), QnASubCommand.wordUnit, group.getPoints(), islog);
	}

	public synchronized void write(DeviceFormat format, Object data) {
		QnABinaryMessage qnaMessage = new QnABinaryMessage(format);
		QnADeviceDataType dataType = QnADeviceDataType.valueOf(format.getDeviceDataType().toString());
		ByteBuffer writeCmd = qnaMessage.createWriteCommand(dataType, data);
		getLogger().info("sent:", DeviceUtil.toHex(writeCmd.array()));
		send(writeCmd.array());
	}

	public void start() {
		try {
			initialize();
			startDataChangeEventQueueHandler();
			startSockReader();
		} catch (Exception e) {
			getLogger().error(e, "Exception to start unsolicited driver.");
			e.printStackTrace();
		}
		
	}
	
	
	private void startSockReader() {
		reader = new QnASocketReader(this, property);
		Thread t = new Thread(reader);
		t.start();
	}

	public void stop() {
		reader.setRunning(false);
		stopChangeEventQueueHandler();
		
	}

	public void processReceived(byte[] received) {
		getLogger().info("received:",DeviceUtil.toHex(received));
		if(isResponse(received)){
			input = new byte[received.length];
			System.arraycopy(received, 0, input, 0, received.length);
			notifySendAndWait();
		} else if (isUnsolicitedPacket(received)){
			sendUnsolicitedReply();
			handleUnsolicitedPacket(received);
		}
		
		if(!isConnected())
			connected();
		
	}

	@Override
	protected void createMitshubishiSocket() {
		try {
			socket = new MitsubishiPlcSocket(getIpAddress(), getPort(), property.getConnectionTimeout(),
					0, getLogger());
			
			setDeviceStatus(true);
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to create Mitshubishi socekt on ", getIpAddress(), ":" + getPort());
		}
	}

	public void disConnected() {
		setConnected(false);
		notifyListeners(new ConnectionStatus(getId(), false));
		
	}
	
	public void connected() {
		setConnected(true);
		notifyListeners(new ConnectionStatus(getId(), true));
		
	}

	@Override
	public void deActivate() {
		super.deActivate();
		stop();
		socket.cleanup();
	}
}
