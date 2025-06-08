package com.honda.galc.openprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import com.honda.galc.common.exception.ResourceAccessException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;
import com.thoughtworks.xstream.XStream;

/**
 * @author Subu Kathiresan
 * Feb 24, 2009
 */
public class OPMessageHelper 
{
	public static final String RESOURCE_PREFIX = "OP";
	public static final String RESOURCE_PATH = "/resource/com/honda/galc/openprotocol/";
	private Logger logger;
	
	private static Hashtable<String, OPMessageDefinition> _messageFormats = new Hashtable<String, OPMessageDefinition>();
	
	public OPMessageHelper(Logger logger) {
		this.logger = logger;
	}

	/**
	 * 
	 * @param messageType
	 * @return
	 */
	public String createMessage(OPMessageType messageType) {
		return createMessage(StringUtil.padLeft(new Integer(messageType.value()).toString(), 4, '0'), new Hashtable());
	}

	/**
	 * 
	 * @param messageType
	 * @param tags
	 * @return
	 */
	public String createMessage(OPMessageType messageType, Hashtable tags) {
		return createMessage(StringUtil.padLeft(new Integer(messageType.value()).toString(), 4, '0'), tags);
	}

	/**
	 * 
	 * @param messageId
	 * @param tags
	 * @return
	 */
	public String createMessage(String messageId, Hashtable tags) {
		StringBuffer message = new StringBuffer();
		
		String messageHeader = "";
		String value = "";
	
		try {
			OPMessageDefinition format = getMessageFormat(messageId);	    
			if (format != null && format.getFields() != null && !format.getFields().isEmpty()) {			
				for (OPMessageFieldFormat field: format.getFields()) {
					if (format.hasFieldNumbers())
						message.append(field.getId());
					
					if (tags != null && tags.containsKey(field.getName()))
						value = tags.get(field.getName()).toString();
					else
						value = field.getDefaultValue();				
	
					message.append(StringUtil.padLeft(value, Integer.parseInt(field.getLength()), ' '));
				}
			}
			
			// create header after creating all the data fields
		    messageHeader = createMsgHeader(OPMessageType.getMessageType(messageId), message.length(), tags);
		}
		catch(Exception ex) {			
			log(ex.getMessage());
			ex.printStackTrace();	
			if(ex instanceof ResourceAccessException ) throw (ResourceAccessException) ex; 
		}
		
		// prepend message header before returning the message string
		return messageHeader + message.toString();
	}
	
	/**
	 * 
	 * @param message
	 */
	private void log(String message) {
		logger.info(message);
	}
	
	/**
	 * Loads a control file from the file system into memory
	 * 
	 * @param messageId					The message Id for which the format will be loaded						
	 * @return							<OPMessageFormat>
	 */
	public OPMessageDefinition getMessageFormat(String messageId) {
		try {
            if (_messageFormats.containsKey(messageId))
				return _messageFormats.get(messageId);
			else {	
				log("Loading message format " + RESOURCE_PREFIX + messageId);
				
				//De-Serialize properties xml file
				XStream xs = new XStream();
				xs.processAnnotations(OPMessageDefinition.class);
				xs.processAnnotations(OPMessageFieldFormat.class);
			
				OPMessageDefinition msgFormat = (OPMessageDefinition) xs.fromXML(readXMLFormatFromFile(messageId));
				_messageFormats.put(messageId, msgFormat);
				return msgFormat;
			}		
		}
		catch(Exception ex) {
			log(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param messageId
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String readXMLFormatFromFile(String messageId) throws UnsupportedEncodingException, IOException {
		InputStream in;
		BufferedReader bIn;
		StringBuffer bufXML = new StringBuffer();
		
		OPMessageDefinition opMessage = new OPMessageDefinition();
		String resourceFileName = RESOURCE_PATH + RESOURCE_PREFIX + messageId + ".xml";
		in = opMessage.getClass().getResourceAsStream(resourceFileName);
		if (in == null) throw new ResourceAccessException("Could not find resource file : " + resourceFileName);
		bIn = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = bIn.readLine()) != null)	{
		    bufXML.append(line);
		}
		
		return bufXML.toString();
	}
	

	/**
	 * 
	 * @param msgType
	 * @return
	 */
	public String createMsgHeader(OPMessageType msgType, int msgLength, Hashtable tags)
	{
		StringBuffer messageHeader = new StringBuffer();
		String value = "";
		tags.put("LENGTH", new Integer(msgLength + OPMessageType.HEADER_LENGTH).toString());
		tags.put("MESSAGE_ID", new Integer(msgType.value()).toString());
		
		OPMessageDefinition headerFormat = getMessageFormat(OPMessageParser.HEADER);
		if (headerFormat != null && headerFormat.getFields() != null && !headerFormat.getFields().isEmpty()) {			
			for (OPMessageFieldFormat field: headerFormat.getFields()) {
							
				if (tags != null && tags.containsKey(field.getName()))
					value = tags.get(field.getName()).toString();
				else
					value = "";				

				if(field.getDefaultValue() == null || field.getDefaultValue().length()==0){
					messageHeader.append(StringUtil.padLeft(value, Integer.parseInt(field.getLength()), ' '));
				}else{
					messageHeader.append(StringUtil.padLeft(value, Integer.parseInt(field.getLength()), OPMessageType.PAD_CHAR));
				}
			}
		}
				
		return messageHeader.toString();
	}
		
	/**
	 * @param messageFormats the messageFormats to set
	 */
	public void setMessageFormats(Hashtable<String, OPMessageDefinition> messageFormats) {
		_messageFormats = messageFormats;
	}

	/**
	 * @return the messageFormats
	 */
	public Hashtable<String, OPMessageDefinition> getMessageFormats() {
		return _messageFormats;
	}
}
