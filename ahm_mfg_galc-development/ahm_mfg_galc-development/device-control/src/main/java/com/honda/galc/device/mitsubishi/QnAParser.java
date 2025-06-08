package com.honda.galc.device.mitsubishi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.DeviceFormat;

/**
 * 
 * <h3>QnAParser</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnAParser description </p>
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
public class QnAParser extends QnAMessageBase{
	
	public QnAParser(Logger logger) {
		this.logger = logger;
	}
	
	public QnAParser() {
	}
	

	public Object decode(DeviceFormat deviceFormat, byte[] bytes){
		return decode(deviceFormat, bytes, null);
	}
	
	private Object decode(DeviceFormat deviceFormat, ByteBuffer bf, QnASubCommand subCommand){
		
		Object received = null;
		QnADevice qnaDevice = QnADevice.valueOf(getDevice(deviceFormat.getTagValue().trim()));
		QnADeviceDataType qnaType = QnADeviceDataType.valueOf(deviceFormat.getDeviceDataType().toString());
		
		bf.order(ByteOrder.LITTLE_ENDIAN);
		switch(qnaType){
		case FLOAT:
			float f = bf.getFloat(11);
			logMessage("recevied float:" + f);
			received = new Float(f);
			break;
			
		case STRING:
			byte[] bytes = new byte[(int)bf.getShort(7) -2];
			bf.position(11);
			bf.get(bytes);

			received = new String(bytes);
			logMessage("received string:" + received);
			break;
			
		case SHORT:
			short s = bf.getShort(11);
			received = new Short(s);
			logMessage("received string:" + s);
			break;
			
		case INTEGER:
			received = bf.getInt(11);
			logMessage("received string:" + received);
			break;
			
		case BOOLEAN:

			QnASubCommand subcmd = subCommand;
			if(subcmd == null){
				subcmd = qnaDevice.isBitDevice() ? QnASubCommand.bitUnit : QnASubCommand.wordUnit;
			}
			
			if(subcmd == QnASubCommand.bitUnit) {

				byte b = bf.get(11);
				return b > 0 ? true : false;
				
			} else {
		
				byte b = bf.get(11);
				return (b & (1 << deviceFormat.getOffset())) != 0;
				
			}
		}
		return received;
	}

	private void logMessage(String msg) {
		if(logger != null)
			logger.info(msg);
		else
			System.out.println(msg);
		
	}

	public Object decode(DeviceFormat deviceFormat, byte[] bytes, QnASubCommand subCommand) {
		ByteBuffer bf = ByteBuffer.wrap(bytes);
		bf.order(ByteOrder.LITTLE_ENDIAN);
		return decode(deviceFormat, bf, subCommand);
	}
	
	public static byte[] getReadPacketData(byte[] read) {
		int size = read.length - 11;
		byte[] data = new byte[size];
		System.arraycopy(read, 11, data, 0, size);
		return data;
	}

	public String getHeadDevice(byte[] received) {
		// TODO Auto-generated method stub
		return null;
	}
}
