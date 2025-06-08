package com.honda.galc.openprotocol;

import java.util.Hashtable;
import java.util.TreeMap;

import com.honda.galc.util.StringUtil;

import static com.honda.galc.common.logging.Logger.getLogger;
/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 */

public enum OPMessageType 
{
	unknown(0),
	communicationStart(1),
	communicationStartAck(2),
	communicationStop(3),
	commandError(4),
	commandAccepted(5),
	paramSetSelectedSubscribe(14),
	paramSetSelected(15),
	paramSetSelectedAck(16),
	selectParamSet(18),
	toolDisable(42),
	toolEnable(43),
	requestVinDownload(50),
	lastTighteningResultDataSubscribe(60),
	lastTighteningResultUploadReply(61),
	lastTighteningResultDataAck(62), 
	lastTighteningPowerMACSResultDataSubscribe(105),
	lastTighteningPowerMACSResultUploadReply(106),
	lastTighteningPowerMACSResultDataAck(108),
	selectJob(38),
	multiSpindleResultSubscribe(100),
	multiSpindleResultUpload(101),
	multiSpindleResultUploadAck(102),
	abortJob(127),
	jobInfoAcknowledge(36),
	keepAlive(9999),
	readTimeUploadRequest(80),
	readTimeUploadReply(81),
	setTime(82);

	private static final int MESSAGE_ID_LENGTH = 4;
	public static final int HEADER_LENGTH = 20;
	private static final int MESSAGE_ID_START_POS = 4;
	private static final int MESSAGE_ID_END_POS = 16;
	public static final char PAD_CHAR = '0';
	
	private int _value;
	private static TreeMap<Integer, OPMessageType> _enumMap;

	static 
	{
		// Create a map of all enum values with messageId as keys
		_enumMap = new TreeMap<Integer, OPMessageType>();
		
		for (OPMessageType msgType: OPMessageType.values())
		{
			_enumMap.put(new Integer(msgType.value()), msgType);
		}
	}
	
	/**
	 * 
	 */
	OPMessageType(int messageId)
	{
		_value = messageId;
	}

	/**
	 * 
	 */
	public int value()
	{
		return _value;
	}

	/**
	 * 
	 */
	public static OPMessageType getMessageType(StringBuffer msgHeader)
	{
		// parse out MID from the header
		return getMessageType(msgHeader.substring(MESSAGE_ID_START_POS, MESSAGE_ID_END_POS));
	}
	
	/**
	 * 
	 */
	public static String getMessageId(String message)
	{	
		// parse out MID from the header
		return message.substring(MESSAGE_ID_START_POS, MESSAGE_ID_END_POS);
	}
	
	/**
	 * 
	 */
	public static String getMessageId(OPMessageType msgType)
	{
		// parse out MID from the header
		return StringUtil.padLeft((new Integer(msgType.value()).toString()), MESSAGE_ID_LENGTH, PAD_CHAR);
	}
	
	/**
	 * 
	 * @param messageId
	 * @return
	 */
	public static OPMessageType getMessageType(String messageId)
	{
		try
		{
			Integer msgId = Integer.parseInt(messageId);
			if (_enumMap.containsKey(msgId))
				return _enumMap.get(msgId);
		}
		catch(Exception ex)
		{
	    	log(ex.getMessage());
			ex.printStackTrace();
		}
		
		return OPMessageType.unknown;
	}
	
	
	/**
	 * 
	 * @param message
	 */
	private static void log(String message)
	{
		getLogger().info(message);
	}
}
