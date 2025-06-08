package com.honda.galc.device.mitsubishi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.honda.galc.client.device.property.QE71CommandsPropertyBean;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.entity.conf.DeviceFormat;

/**
 * 
 * <h3>QnABinaryMessage</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnABinaryMessage description </p>
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
public class QnABinaryMessage extends QnAMessageBase{
	public static final Integer COMMAND_SUBHEADER = 0x0050;
	public static final int RESPONSE_SUBHEADER = 0xD000;
	public static final byte NETWORK_NO = 0x00;
	public static final Integer PLC_NO = 0xFF;
	public static final Integer FIXED_VALUE1 = 0x03FF;
	public static final Integer FIXED_VALUE2 = 0x00;
	public static final Integer SUB_COMMAND = 0x0000; /*Word Unit*/
	public static final Integer CPU_MONITORING_TIMER = 0x0000;
	public static final byte BIT_UNIT_TRUE = 16;
	public static final byte BIT_UNIT_FALSE = 0;
	public static final String DATA_COLLECTION_COMPLETE_OK = "1";
	public static final String DATA_COLLECTION_COMPLETE_NG = "0";
	
	public static byte[] unsolicitedReply = {-32,00};//0xE0
	
	QE71CommandsPropertyBean property;
	private QnADevice qnaDevice;
	private QnADeviceDataType qnaType;
	private QnACommand qnaCommand;
	private QnASubCommand subCommand;
	private String address;
	private ByteBuffer buf;
	private int points = -1;
	private DeviceFormat deviceFormat = null;
	
	
	public QnABinaryMessage(String address, String type, QnACommand command) {
		super();
		this.address = address;
		this.qnaCommand = command;
		this.qnaType = QnADeviceDataType.valueOf(type.toUpperCase());
		
		initialize();
		
	}

	public QnABinaryMessage(String address, QnASubCommand subCommand, int points) {
		this.address = address;
		this.subCommand = subCommand;
		this.points = points;
		
		initialize();
	}
	

	public QnABinaryMessage(DeviceFormat format) {
		this.deviceFormat = format;
		
		initialize();
	}

	private void initialize() {
		if(deviceFormat != null)
		{
			this.address = deviceFormat.getTagValue().trim();
			this.qnaType = QnADeviceDataType.valueOf(deviceFormat.getDeviceDataType().toString());
		}
		qnaDevice = QnADevice.valueOf(getDevice(address));
	}
	
	public ByteBuffer createReadCommand(){
		qnaCommand = QnACommand.BatchRead;
		int command_data_length = getReadCommandDataLength();
		buf = ByteBuffer.allocate(command_data_length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		
		assembleCommonCommand(getRequestDataLength());
		
		return buf;
		
	}


	private void assembleCommonCommand(int requestDataLength) {
		setSubHeader();
		setNetworkNo();
		setPlcNo();
		setFixedValue1();
		setFixedValue2();
		setRequestDataLength(requestDataLength);
		setCpuMonitorTimer();
		setCommand();
		setSubcommand();
		setHeadDevice(getHeadDeviceValue());
		setHeadDeviceCode(qnaDevice.getBinaryCode());
		setDevicePoints();
	}
	

	private int getReadCommandDataLength() {
		int length = getQnAHeaderLength();
		length += getRequestDataLength();
		return length;
	}
	
	public ByteBuffer createWriteCommand(QnADeviceDataType qnaType, Object value ){
		qnaCommand = QnACommand.BatchWrite;
		int command_data_length = getWriteCommandDataLength();
		buf = ByteBuffer.allocate(command_data_length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		
		assembleCommonCommand(getRequestDataLength() + getWriteDataLength());
		addWriteData(qnaType, value);
		
		return buf;
		
	}

	private int getWriteCommandDataLength() {
		int length = getQnAHeaderLength();
		length += getRequestDataLength();
		length += getWriteDataLength();
		return length;
	}
	
	private void addWriteData(QnADeviceDataType qnaType, Object data) {

		try {
			switch (qnaType) {
			case BOOLEAN:
				Boolean v = (data instanceof Boolean)? (Boolean)data : convertBooleanData(data);
				if (!qnaDevice.isBitDevice()) {
					if (v) {
						//int s = 1 << getDeviceBooleanPoints();
						buf.putShort((short)1);
					} else
						buf.putShort((short) 0);//TODO?
				} else {
					buf.put( v ?  BIT_UNIT_TRUE : BIT_UNIT_FALSE);
				}
					break;
			case FLOAT:
				float f = (data instanceof Float)? (Float)data :Float.parseFloat(data.toString());
				buf.putFloat(f);
				break;
			case INTEGER:
				int intValue = (data instanceof Integer)? (Integer)data :Integer.parseInt(data.toString());
				buf.putInt(intValue);
				break;
			case SHORT:
				short s = (data instanceof Short)? (Short)data :Short.parseShort(data.toString());
				buf.putShort(s);
				break;
			case STRING:
				byte[] bytes =	data.toString().getBytes();
				buf.put(bytes);
				break;
			default:
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

	private Boolean convertBooleanData(Object data) {
		String stringValue = data.toString();
		if(DATA_COLLECTION_COMPLETE_OK.equals(stringValue) || "OK".equals(stringValue))
			return Boolean.TRUE;
		else if(DATA_COLLECTION_COMPLETE_NG.equals(stringValue) || "NG".equals(stringValue))
			return Boolean.FALSE;
		else
			return Boolean.valueOf(stringValue);
		    
			
	}

	
	private void setSubcommand() {

		buf.putShort(getSubComand().getCode().shortValue());

	}

	private QnASubCommand getSubComand() {
		if(subCommand == null){
			if(qnaDevice.isBitDevice() && qnaType == QnADeviceDataType.BOOLEAN)
				subCommand = QnASubCommand.bitUnit;
			else
				subCommand = QnASubCommand.wordUnit;
		}
		return subCommand;
	}

	private void setCommand() {
		buf.putShort(Integer.valueOf(qnaCommand.getCode()).shortValue());
		
	}

	private void setCpuMonitorTimer() {
		buf.putShort(CPU_MONITORING_TIMER.shortValue());
		
	}

	private void setRequestDataLength(int request_data_length) {
		buf.putShort(Integer.valueOf(request_data_length).shortValue());
		
	}

	private void setFixedValue2() {
		buf.put(FIXED_VALUE2.byteValue());
		
	}

	private void setFixedValue1() {
		buf.putShort(FIXED_VALUE1.shortValue());
		
	}

	private void setPlcNo() {
		buf.put(PLC_NO.byteValue());
		
	}

	private void setNetworkNo() {
		buf.put(NETWORK_NO);
		
	}

	private void setSubHeader() {
		buf.putShort(COMMAND_SUBHEADER.shortValue());
		
	}

	private int getRequestDataLength() {
		int length = 0;
		length += 2; //CPU Monitoring Timer
		length += 2; //Command
		length += 2; //SubCommand
		length += 3; //Head Device Data 
		length += 1; //Device Code
		length += 2; //Device Points

		return length;
	}

	private int getQnAHeaderLength() {
		int length = 0;
		length += 2; //SubHeader
		length += 1; //Net work length
		length += 1; //PLC No length
		length += 2; //Fixed Value 1
		length += 1; //Fixed Value 2
		length += 2; //Request Data Length
		return length;
	}

	private int getWriteDataLength() {
		int writeDataLength = 0;
		
		if(!qnaDevice.isBitDevice() || qnaType != QnADeviceDataType.BOOLEAN)
			writeDataLength = getDevicePoints() *2;
		else
			writeDataLength = getDevicePoints();

		return writeDataLength;
	}
	

	private int getHeadDeviceValue() {
		int firstDigitIndex = DeviceUtil.indexOfFirstDigit(address);
		int lastDigitIndex = address.indexOf(".");
		int headdevice;
		if(lastDigitIndex > 0)
			headdevice = Integer.parseInt(address.substring(firstDigitIndex, lastDigitIndex));
		else
			headdevice = Integer.parseInt(address.substring(firstDigitIndex));
		
		return headdevice;
	}

	private void setDevicePoints() {
		int device_points = getDevicePoints();
		buf.putShort(Integer.valueOf(device_points).shortValue()); 

	}

	/**
	 * Return the total device points based on device type. 
	 * For bit device 1 bit per unit, 16 bit per unit for word device
	 * @return
	 */
	private int getDevicePoints() {
		if(points > 0) return points;
		
		int device_points = getDataLength();
		if(qnaType == QnADeviceDataType.BOOLEAN && qnaDevice.isBitDevice()){
			device_points = 1;//TODO? 1 bitUnit is 4 bits; HERE should be how many boolean points M12, M13 then 2 points
		} else {			
			//16 bits for word unit, if it less than 16 then make it 16
			int tmp_device_points = device_points;
			device_points = tmp_device_points/16;
			
			//Round to a word unit for the remainder
			if(tmp_device_points%16 > 0)
				device_points++;
		}
		return device_points;
	}

	/**
	 * Return the total number of bits for the data type 
	 * @return
	 */
	private int getDataLength() {
		int device_points;
		
		if(qnaType == null) return this.points;
		
		switch(qnaType)
		{
		case STRING:
			device_points = getDeviceStringPoints()*8;//ASCII code
			break;
		case BOOLEAN:
			device_points = 1;//TODO? getDeviceBooleanPoints(); //boolean is bit
			break;
		default:
			device_points = qnaType.getBits();
		break;
		}
		return device_points;
	}

	public int getDeviceBooleanPoints() {
		if(qnaDevice.isBitDevice()) return 1;
		else
			return 1;
	}

	private void setHeadDeviceCode(byte binaryCode) {
		buf.put(binaryCode);
		
	}

	private void setHeadDevice(int headdevice) {
		//For binary, always little endian
		ByteBuffer headdevicebuf = ByteBuffer.allocate(4);
		headdevicebuf.order(ByteOrder.LITTLE_ENDIAN);
		
		headdevicebuf.putInt(0, headdevice);
		
		for(int i = 0; i < 3; i++)
			buf.put(headdevicebuf.get(i));
	}

	private int getDeviceStringPoints() {
		return deviceFormat.getLength();
	}

	public static void decodeAsciiMessage(String msg, String type){
		String command = msg.substring(22, 26);
		String device = msg.substring(30,36) + "00";
		String deviceCode = msg.substring(36,38);
		//String devicePoints = msg.substring(38,42);
		StringBuilder bs = new StringBuilder();
		
		ByteBuffer bytebuf = getBytesFromAsciiString(command);
		String commandString = QnACommand.getCommandByCode(bytebuf.getShort());
		bs.append("command:").append(commandString);
		
		ByteBuffer deviceCodeBuf = getBytesFromAsciiString(deviceCode);
		String deviceAsciiCode = QnADevice.getDeviceAsciiCodeByCode(deviceCodeBuf.get(0));
		
		//ByteBuffer pointsgBuf = getBytesFromAsciiString(devicePoints);
		ByteBuffer deviceBuf = getBytesFromAsciiString(device);
		bs.append(" device:").append(deviceAsciiCode).append(deviceBuf.getInt());
		
		if(commandString.equals(QnACommand.BatchWrite.toString())){
			String dataString = msg.substring(42);
			ByteBuffer dataStringBuffer = getBytesFromAsciiString(dataString);
			bs.append(" data:");
			if(type == null)
				bs.append(new String(dataStringBuffer.array()));
			else{
				QnADeviceDataType dataType = QnADeviceDataType.valueOf(type);
				Object dataObject = decodeData(dataType, dataStringBuffer);
				bs.append(dataObject);
			}
		}
		
	}

	private static Object decodeData(QnADeviceDataType dataType, ByteBuffer dataStringBuffer) {
			Object received = null;
			dataStringBuffer.order(ByteOrder.LITTLE_ENDIAN);
			switch(dataType){
			case FLOAT:
				float f = dataStringBuffer.getFloat();
				received = new Float(f);
				break;
			case STRING:
				received = new String(dataStringBuffer.array());
				break;
			case SHORT:
				short s = dataStringBuffer.getShort();
				received = new Short(s);
				break;
			case BOOLEAN:
				short bs = dataStringBuffer.getShort();
				received = bs;//TODO

				break;
			}
			return received;
	}

	private static ByteBuffer getBytesFromAsciiString(String str) {
		ByteBuffer tmpbuf = ByteBuffer.allocate(str.length()/2);
		tmpbuf.order(ByteOrder.LITTLE_ENDIAN);
		for(int i = 0; i < str.length(); i+=2){
			String tmp = str.substring(i, i+2);
			tmpbuf.put(Integer.valueOf(tmp, 16).byteValue());
		}
		tmpbuf.rewind();
		return tmpbuf;
	}

	public QnASubCommand getSubCommand() {
		return subCommand;
	}

}
