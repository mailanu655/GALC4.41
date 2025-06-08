/**
 * 
 */
package com.honda.galc.client.device.lotcontrol.immobi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;


import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;

import com.thoughtworks.xstream.XStream;
/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public class ImmobiMessageHelper {
	
	public static final String RESOURCE_PATH = "/resource/com/honda/global/galc/client/immobiprotocol/";	
	private static Hashtable<String, ImmobiMessageDefinition> _messageFormats = new Hashtable<String, ImmobiMessageDefinition>();

	public static final String START_TAG = "<ImmobiMessage>";
	public static final String END_TAG = "</ImmobiMessage>";
	
	/**
	 * 
	 * @param messageType
	 * @param tags
	 * @return
	 */
	public static ImmobiMessage convertToBean(ImmobiMessageType messageType, Hashtable<String, String> tags) {
		return convertToBean(createStringMessage(messageType.toString().toUpperCase(), tags));
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public static ImmobiMessage convertToBean(String message)
	{
		StringBuffer xmlMessage = new StringBuffer();
		ImmobiMessage immobiMsg = null;
		
		try
		{
			xmlMessage.append(START_TAG);	
			
			String command = ImmobiMessageType.getCommandId(message);
			String msgId = ImmobiMessageType.getMessageType(command).toString().toUpperCase();
			ImmobiMessageDefinition format = ImmobiMessageHelper.getMessageFormat(msgId);			
			parseDataFields(message, xmlMessage, format);
			
			xmlMessage.append(END_TAG);
			Logger.getLogger().info(xmlMessage.toString());
			
			immobiMsg = createXMLBean(xmlMessage, format);
			immobiMsg.setDescription(format.getDescription());
			immobiMsg.setSeverity(format.getSeverity());
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
			Logger.getLogger().error("Unable to convert to Bean: "+ex.getMessage());
		}
		
		return immobiMsg;
	}
	
	/**
	 * 
	 * @param message
	 * @param xmlMessage
	 * @param format
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static void parseDataFields(String message, StringBuffer xmlMessage, ImmobiMessageDefinition format) throws ClassNotFoundException
	{
		if (format.getFields() != null && !format.getFields().isEmpty()) {		
			int messageIndex = 0;
			for (ImmobiMessageFieldFormat field: format.getFields()) {

				int len = getFieldLength(format, field);	
				String val = null;
				int tempIndex = 0;
				try {				
					tempIndex = messageIndex;
					val = message.substring(messageIndex, messageIndex+=len);				
				}catch(IndexOutOfBoundsException e)
				{
					Logger.getLogger().error("Incorrect field length.  Extracting remaining characters " +e.getMessage());
					e.printStackTrace();
					val = message.substring(tempIndex);				
				}catch(Exception e)
				{
					Logger.getLogger().error("Unable to calculate value " +e.getMessage());
				}
				Logger.getLogger().info("values : "+field.getName()+ ":" + val);

				addTag(xmlMessage, field, val);
			}
		}
	}


	/**
	 * 
	 * @param xmlMessage
	 * @param field
	 * @param val
	 */
	private static void addTag(StringBuffer xmlMessage, ImmobiMessageFieldFormat field, String val)
	{
		xmlMessage.append("<" + field.getName() + ">" + val + "</" + field.getName() + ">");
	}
	
	/**
	 * 
	 * @param xmlMessage
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static ImmobiMessage createXMLBean(StringBuffer xmlMessage, ImmobiMessageDefinition format) throws ClassNotFoundException 
	{
		XStream xs = new XStream();
		xs.processAnnotations(ImmobiMessage.class);
		xs.processAnnotations(Class.forName(format.getClassName()));

		// invoke XStream de-serialization
		return (ImmobiMessage) xs.fromXML(xmlMessage.toString());
	}
	
	/**
	 * 
	 * @param messageId
	 * @param tags
	 * @return
	 */
	public static String createStringMessage(String messageId, Hashtable<String, String> tags) {
		StringBuffer message = new StringBuffer();
		String value = "";
		
		try
		{
			ImmobiMessageDefinition format = getMessageFormat(messageId);			
			if (format.getFields() != null && !format.getFields().isEmpty()) {			
				for (ImmobiMessageFieldFormat field: format.getFields()) {
					if (tags != null && tags.containsKey(field.getName()))
						value = tags.get(field.getName()).toString();
					else
						value = field.getValue();	
					
					int len = getFieldLength(format, field);
					message.append(StringUtil.padLeft(value,len, ' '));
				}
			}
		}
		catch(Exception ex) {			
			log(ex.getMessage());
			ex.printStackTrace();			
		}
		
		return message.toString();
	}

	/**
	 * 
	 * @param format
	 * @param field
	 * @return
	 */
	private static int getFieldLength(ImmobiMessageDefinition format, ImmobiMessageFieldFormat field) {
		int len = -1;
		if (field.getLength() != null)
			len = Integer.parseInt(field.getLength());
		else {
			// get field length from another field
			ImmobiMessageFieldFormat lengthField = format.getField(field.getLengthField());
			len = Integer.parseInt(lengthField.getValue()) - Integer.parseInt(lengthField.getLength());
		}
		return len;
	}
	
	/**
	 * 
	 * @param message
	 */
	private static void log(String message)
	{
		Logger.getLogger().info(message);
	}

	/**
	 * Loads a control file from the file system into memory
	 * 
	 * @param messageId					The message Id for which the format will be loaded						
	 * @return							<ImmobiMessageFormat>
	 */
	public static ImmobiMessageDefinition getMessageFormat(String messageId)
	{
		try
		{
            if (_messageFormats.containsKey(messageId))
				return _messageFormats.get(messageId);
			else
			{	
				log("Loading message format " + messageId);
				
				//De-Serialize properties xml file
				XStream xs = new XStream();
				xs.processAnnotations(ImmobiMessageDefinition.class);
				xs.processAnnotations(ImmobiMessageFieldFormat.class);
			
				ImmobiMessageDefinition msgFormat = (ImmobiMessageDefinition) xs.fromXML(readXMLFormatFromFile(messageId));
				_messageFormats.put(messageId, msgFormat);
				return msgFormat;
			}		
		}
		catch(Exception ex)
		{
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
	private static String readXMLFormatFromFile(String messageId) throws UnsupportedEncodingException, IOException 
	{
		InputStream in;
		BufferedReader bIn;
		StringBuffer bufXML = new StringBuffer();
		
		ImmobiMessageDefinition ImmobiMessage = new ImmobiMessageDefinition();
		in = ImmobiMessage.getClass().getResourceAsStream(RESOURCE_PATH + messageId.toUpperCase() + ".xml");
		bIn = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = bIn.readLine()) != null) {
		    bufXML.append(line);
		}
		
		return bufXML.toString();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String msg = "";
			
		String strMessage = "04300300003400000000000000000,0000000000000";
		ImmobiMessage message = convertToBean(strMessage);
		
		System.out.println(message.getCommandCode());
		System.out.println(ImmobiMessageHelper.createStringMessage("vin_ack", null));
		System.out.println(ImmobiMessageType.vin_ack.createStringMessage(null));
		System.out.println(ImmobiMessageType.vin_ack.createMessage().getSegmentData());
		
		msg = ImmobiMessageType.vin.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.vin_ack.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.vin_ng.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.mtoc.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.mtoc_ack.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.mtoc_ng.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.mtoc_ok.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.reg_ok.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.reg_done.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.reg_ng.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.abort.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
		
		msg = ImmobiMessageType.err.createStringMessage(new Hashtable<String, String>());
		System.out.println(msg);
	}
	
	
}
