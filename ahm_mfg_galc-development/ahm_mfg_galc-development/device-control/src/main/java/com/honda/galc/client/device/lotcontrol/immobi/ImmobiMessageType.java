/**
 * 
 */
package com.honda.galc.client.device.lotcontrol.immobi;

import java.util.Hashtable;
import java.util.TreeMap;

import com.honda.galc.common.logging.Logger;



/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public enum ImmobiMessageType {
	vin("001000"),
	vin_ack("004001"),
	vin_ng("006001"),
	mtoc("002000"),
	mtoc_ack("004002"),
	mtoc_ng("006002"),
	mtoc_ok("005002"),
	key("010000"),
	key_ack("004004"),
	key_ng("006005"),
	key_ok("005005"),
	reg_done("003000"),
	reg_ok("005003"),
	reg_ng("006003"),
	abort("007000"),
	err("008000"), 
	unknown("000000");	
	

	private static final int COMMAND_ID_START_POS = 3;
	private static final int COMMAND_ID_END_POS = 9;
    
	
	private String _commandId = "";
	private static TreeMap<String, ImmobiMessageType> _commandIdMap;

	static 
	{
		// Create a map of all enum values with messageId as keys
		_commandIdMap = new TreeMap<String, ImmobiMessageType>();
		
		for (ImmobiMessageType msgType: ImmobiMessageType.values()) {
			_commandIdMap.put(msgType.getCommandId(), msgType);
		}
	}
	
	/**
	 * 
	 */
	ImmobiMessageType(String commandId)
	{
		_commandId = commandId;
	}

	/**
	 * 
	 * @return
	 */
	public String getCommandId()
	{
		return _commandId;
	}
	
	/**
	 * 
	 */
	public static String getCommandId(String message)
	{	
		// parse out commandId from the message
		return message.substring(COMMAND_ID_START_POS, COMMAND_ID_END_POS);
	}
	
	/**
	 * 
	 * @param commandId
	 * @return
	 */
	public static ImmobiMessageType getMessageType(String commandId)
	{
		try
		{
			if (_commandIdMap.containsKey(commandId))
				return _commandIdMap.get(commandId);
		}
		catch(Exception ex)
		{
	    	Logger.getLogger().warn(ex.getMessage());
			ex.printStackTrace();
		}
		
		return ImmobiMessageType.unknown;
	}
	
	/**
	 * 
	 * @param msgType
	 * @return
	 */
	public ImmobiMessage createMessage() {
		return ImmobiMessageHelper.convertToBean(this, null);
	}
	
	/**
	 * 
	 * @param messageId
	 * @param tags
	 * @return
	 */
	public String createStringMessage(Hashtable<String, String> tags) {
		return ImmobiMessageHelper.createStringMessage(toString().toUpperCase(), tags);
	}
}
