package com.honda.galc.openprotocol;

import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.openprotocol.model.AbstractOPMessage;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.SpindleStatus;
import com.thoughtworks.xstream.XStream;

/**
 * @author Subu Kathiresan
 * Feb 15, 2009
 */
public class OPMessageParser 
{
	private static final String MSG_MULTI_SPINDLE = "0101";
	private static final String INTEGER = "Integer";
	public static final int HEADER_LENGTH = 20;
	public static final int FIELD_IND_LENGTH = 2;
	public static final String HEADER = "Header";	
	public static final String MESSAGE_ID = "MESSAGE_ID";
	public static final String START_TAG = "<OPMessage>";
	public static final String END_TAG = "</OPMessage>";
	public static final String SPINDLE_STATUS = "SpindleStatus";
	private OPMessageHelper opMessageHelper;
	private int startOfSpindleStatus = -1;
	private Logger logger;
	
	public OPMessageParser(Logger logger) {
		this.logger = logger;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public AbstractOPMessage convertToBean(String message)
	{
		StringBuffer xmlMessage = new StringBuffer();
		AbstractOPMessage opMsg = null;
		
		try
		{
			xmlMessage.append(START_TAG);	
			
			String msgId = parseHeader(xmlMessage, message);
			OPMessageDefinition format = getOpMessageHelper().getMessageFormat(msgId);			
			parseDataFields(message.substring(HEADER_LENGTH), xmlMessage, format);
			
			xmlMessage.append(END_TAG);
			logger.debug(xmlMessage.toString());
			
			opMsg = createXMLBean(xmlMessage, format);
			
			if (opMsg != null)
				opMsg.setMessageString(message);
			
			if(isMultiSpindleResultUpload(msgId)){
				processMultiSpindleStatus(message, (MultiSpindleResultUpload)opMsg, msgId);
			}
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
			logger.emergency(ex.getMessage());
			logger.emergency(ex,"Unable to convert to Bean");			
		}
		
		return opMsg;
	}

	/**
	 * 
	 * @param message
	 * @param opMsg
	 * @param msgId
	 */
	private void processMultiSpindleStatus(String message, MultiSpindleResultUpload opMsg, String msgId) {
		try {
			OPMessageDefinition multiSpindleResultFormat = getOpMessageHelper().getMessageFormat(msgId + SPINDLE_STATUS);
			List<SpindleStatus> spindleStatusList = parseMessageList(message.substring(getStartOfSpindleStatus()),
					multiSpindleResultFormat, SpindleStatus.class);
			
			opMsg.setSpindleStatusList(spindleStatusList);
			
		} catch (Exception e) {
			logger.error(e, "Exception on process multi spindle status.");
		}
		
	}
	
	/**
	 * 
	 * @param <T>
	 * @param msgString
	 * @param messageDef
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> parseMessageList(String msgString, OPMessageDefinition messageDef, Class<T> clazz)
	throws ClassNotFoundException {
		
		XStream xs = new XStream();
		StringBuffer xmlMessage = new StringBuffer();
		int msgLength = getMessageLength(messageDef);
		
		xmlMessage.append("<list>");
		while(msgString.length() >= msgLength){
			xmlMessage.append("<" + clazz.getName() + ">");
			parseDataFields(msgString, xmlMessage, messageDef);
			msgString = msgString.substring(msgLength);
			xmlMessage.append("</" + clazz.getName() + ">");
		}
		xmlMessage.append("</list>");
		
		xs.processAnnotations(clazz);
		return (List<T>)xs.fromXML(xmlMessage.toString());
	}

	/**
	 * 
	 * @param messageDef
	 * @return
	 */
	private int getMessageLength(OPMessageDefinition messageDef) {
		
		int total = 0;
		for(OPMessageFieldFormat field : messageDef.getFields()){
			total += Integer.parseInt(field.getLength());
		}
		
		return total;
	}

	/**
	 * 
	 * @return
	 */
	private int getStartOfSpindleStatus() {
		
		if(startOfSpindleStatus == -1){
			OPMessageDefinition format = getOpMessageHelper().getMessageFormat(MSG_MULTI_SPINDLE);
			
			startOfSpindleStatus = 0;
			for(OPMessageFieldFormat field : format.getFields()){
				startOfSpindleStatus = startOfSpindleStatus + Integer.parseInt(field.getLength());
				
				if(format.hasFieldNumbers())
					startOfSpindleStatus += FIELD_IND_LENGTH;
			}
			
			startOfSpindleStatus += HEADER_LENGTH;
		}
		
		return startOfSpindleStatus;
	}

	/**
	 * 
	 * @param msgId
	 * @return
	 */
	private boolean isMultiSpindleResultUpload(String msgId) {
		return msgId.equals(MSG_MULTI_SPINDLE);
	}

	/**
	 * returns the message id after parsing the header of 
	 * an Open Protocol string message
	 * 
	 * @param message
	 * @return
	 */
	private  String parseHeader(StringBuffer xmlMessage, String message)
	{
		String messageId = "";
		
		try
		{
			OPMessageDefinition format = getOpMessageHelper().getMessageFormat(HEADER);	
			int messageIndex = 0;
			
			if (format.getFields() != null && !format.getFields().isEmpty())
			{			
				for (OPMessageFieldFormat field: format.getFields())
				{
					int len = Integer.parseInt(field.getLength());
					String val = message.substring(messageIndex, messageIndex+=len);
					addTag(xmlMessage, field, val);
					
					if (field.getName().equalsIgnoreCase(MESSAGE_ID))
						messageId = val;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.emergency(ex.getMessage());
			logger.emergency(ex,"Unable to parse OP Messsage header");	
		}
		
		return messageId;
	}

	/**
	 * 
	 * @param message
	 * @param xmlMessage
	 * @param format
	 * @return
	 * @throws ClassNotFoundException
	 */
	private void parseDataFields(String message, StringBuffer xmlMessage, OPMessageDefinition format) throws ClassNotFoundException
	{
		if (format.getFields() != null && !format.getFields().isEmpty())
		{		
			int messageIndex = 0;
			for (OPMessageFieldFormat field: format.getFields())
			{
				int len = Integer.parseInt(field.getLength());
				
				if (format.hasFieldNumbers())
					messageIndex+=FIELD_IND_LENGTH;
				
				if(len > 0){
					String val = message.substring(messageIndex, messageIndex+=len);
					addTag(xmlMessage, field, val);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param xmlMessage
	 * @param field
	 * @param val
	 */
	private void addTag(StringBuffer xmlMessage, OPMessageFieldFormat field, String val)
	{
		if(field.getType().equals(INTEGER))
			val = Integer.valueOf(val).toString();
				
		val = replaceEntityReferences(val);
		xmlMessage.append("<" + field.getName() + ">" + val + "</" + field.getName() + ">");
	}
	
	/**
	 * 
	 * @param xmlMessage
	 * @return
	 * @throws ClassNotFoundException
	 */
	private AbstractOPMessage createXMLBean(StringBuffer xmlMessage, OPMessageDefinition format) throws ClassNotFoundException 
	{
		XStream xs = new XStream();
		xs.processAnnotations(AbstractOPMessage.class);
		xs.processAnnotations(Class.forName(format.getClassName()));
		// invoke XStream de-serialization
		return (AbstractOPMessage) xs.fromXML(xmlMessage.toString());
	}
	
	/**
	 * 
	 * @param strVal
	 */
	private String replaceEntityReferences(String strVal)
	{
		try
		{
			// replace all character entity references, so that XML parsers can parse the data
			strVal = strVal.replaceAll("&", "&amp;");
			strVal = strVal.replaceAll("<", "&lt;");
			strVal = strVal.replaceAll(">", "&gt;");
			strVal = strVal.replaceAll("'", "&apos;");
			strVal = strVal.replaceAll("\"", "&quot;");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.warn(ex,ex.getMessage());
		}
		return strVal;
	}

	public OPMessageHelper getOpMessageHelper() {
		if(opMessageHelper == null)
			opMessageHelper = new OPMessageHelper(logger);
		
		return opMessageHelper;
	}
}
