package com.honda.galc.device.simulator.torque;

import java.util.ArrayList;
import java.util.TreeMap;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.util.StringUtil;

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
	jobInfoAcknowledge(36),
	selectJob(38),
	toolDisable(42),
	toolEnable(43),
	vinDownloadRequest(50),
	lastTighteningResultDataSubscribe(60),
	lastTighteningResultUploadReply(61),
	lastTighteningResultDataAck(62),
	abortJob(127),
	keepAlive(9999);

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
		return getMessageType(msgHeader.substring(4,8));
	}
	
	/**
	 * 
	 */
	public static String getMessageId(String message)
	{	
		// parse out MID from the header
		return message.substring(4,8);
	}
	
	/**
	 * 
	 */
	public static String getMessageId(OPMessageType msgType)
	{
		// parse out MID from the header
		return StringUtil.padLeft((new Integer(msgType.value()).toString()), 4, '0', true);
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
	    	Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		return OPMessageType.unknown;
	}
	
	/**
	 * Returns true if the response is valid for the request
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean isValidReply(OPMessageType request, OPMessageType response)
	{
		for (String msgType: getReplyMessages(request))
		{
			if (response == getMessageType(msgType))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param msgType
	 * @return
	 */
	public static ArrayList<String> getReplyMessages(OPMessageType msgType)
	{
		try
		{
			String key = "OP" + StringUtil.padLeft(new Integer(msgType.value()).toString(), 4, '0', true);
			return OPReplies.getReplies().get(key);
		}
		catch(Exception ex)
		{
	    	Logger.log(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param msgType
	 * @return
	 */
	public static String createMsgHeader(OPMessageType msgType, String msg)
	{
		return StringUtil.padLeft(new Integer(msg.length() + 20).toString(), 4, '0', true) 
					+ StringUtil.padLeft(new Integer(msgType.value()).toString(), 4, '0', true)
					+ StringUtil.padLeft("", 12, ' ', true);
	}
}
