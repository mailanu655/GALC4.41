package com.honda.galc.device.simulator.torque;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;
import com.honda.galc.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
 
/**
 * This class represents an OPMessage file that
 * defines the field definitions for a single Open Protocol Message
 * 
 * @author Subu Kathiresan
 * @date June 29, 2009
 */
@XStreamAlias("OPMessage")
public class OPMessage {
	
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id;
	
	@XStreamAlias("fixedLength")
	@XStreamAsAttribute
	private boolean _fixedLength;
	
	@XStreamAlias("delimiter")
	@XStreamAsAttribute
	private String _delimiter;
	
	@XStreamAlias("hasFieldNumbers")
	@XStreamAsAttribute
	private boolean _hasFieldNumbers; 
	
	@XStreamAlias("className")
	@XStreamAsAttribute
	private String _className;
	
	@XStreamImplicit(itemFieldName="field")
	private ArrayList<OPMessageFieldFormat> _fields = new ArrayList<OPMessageFieldFormat>();
	
	private static Hashtable<String, OPMessage> _messageFormats = new Hashtable<String, OPMessage>();
	public static final int HEADER_LENGTH = 20;
	public static final int FIELD_IND_LENGTH = 2;
	public static final String START_TAG = "<OPMessage>";
	public static final String END_TAG = "</OPMessage>";
	public static final String HEADER = "Header";	
	public static final String MESSAGE_ID = "MESSAGE_ID";
	    
	/** Need to allow bean to be created via reflection */
	public OPMessage() {}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this._id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return _id;
	}

	/**
	 * @param fixedLength the fixedLength to set
	 */
	public void setFixedLength(boolean fixedLength) {
		this._fixedLength = fixedLength;
	}

	/**
	 * @return the fixedLength
	 */
	public boolean isFixedLength() {
		return _fixedLength;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		_delimiter = delimiter;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return _delimiter;
	}
	
	/**
	 * @return the hasFieldNumbers
	 */
	public boolean hasFieldNumbers() {
		return _hasFieldNumbers;
	}

	/**
	 * @param fieldNumbers the hasFieldNumbers to set
	 */
	public void setHasFieldNumbers(boolean hasFieldNumbers) {
		_hasFieldNumbers = hasFieldNumbers;
	}
	
	/**
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		_className = className;
	}
	
	/**
	 * 
	 */
	public String getClassName() {
		return _className;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<OPMessageFieldFormat> fields) {
		_fields = fields;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<OPMessageFieldFormat> getFields() {
		return _fields;
	}

	/**
	 * @param messageFormats the messageFormats to set
	 */
	public static void setMessageFormats(Hashtable<String, OPMessage> messageFormats) {
		_messageFormats = messageFormats;
	}

	/**
	 * @return the messageFormats
	 */
	public static Hashtable<String, OPMessage> getMessageFormats() {
		return _messageFormats;
	}
	
	/**
	 * Loads a control file from the file system into memory
	 * 
	 * @param messageId					The message Id for which the format will be loaded						
	 * @return							<OPMessageFormat>
	 */
	public static OPMessage getMessageFormat(String messageId) {
		String controlFilePath = Utils.getPath(Utils.CONTROL_FILES_DIR + "OP" + messageId + ".xml");
		
		try	{
			if (_messageFormats.containsKey(messageId)) {
				return _messageFormats.get(messageId);
			} else {	
				Logger.log("Loading message format OP" + messageId);
				
				//De-Serialize properties xml file
				XStream xs = new XStream();
				xs.processAnnotations(OPMessage.class);
				xs.processAnnotations(OPMessageFieldFormat.class);
			
				OPMessage msgFormat = (OPMessage) xs.fromXML(new FileInputStream(controlFilePath));
				_messageFormats.put(messageId, msgFormat);
				return msgFormat;
			}		
		} catch(Exception ex) {
			Logger.log(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param messageId
	 * @param tags
	 * @return
	 */
	public static String createMessage(String messageId, Hashtable<?, ?> tags) {
		StringBuffer message = new StringBuffer();
		String messageHeader = "";
		String value = "";
		
		try {
			OPMessage format = getMessageFormat(messageId);			
			if (format.getFields() != null && !format.getFields().isEmpty()) {			
				for (OPMessageFieldFormat field: format.getFields()) {
					if (format.hasFieldNumbers())
						message.append(field.getId());
					
					if (tags != null && tags.containsKey(field.getName())) {
						value = tags.get(field.getName()).toString();
					} else {
						value = field.getDefaultValue();				
					}
					message.append(StringUtil.padLeft(value, Integer.parseInt(field.getLength()), ' ', true));
				}
			}
			
			// create header after creating all the data fields
			messageHeader = OPMessageType.createMsgHeader(OPMessageType.getMessageType(messageId), message.toString());
		} catch(Exception ex) {			
			log(ex.getMessage());
			ex.printStackTrace();			
		}
		
		// prepend message header before returning the message string
		return messageHeader + message.toString();
	}
	
	/**
	 * 
	 * @param messageType
	 * @param tags
	 * @return
	 */
	public static String createMessage(OPMessageType messageType, Hashtable<?, ?> tags) {
		return createMessage(StringUtil.padLeft(new Integer(messageType.value()).toString(), 4, '0', true), tags);
	}
	
	/**
	 * 
	 * @param messageType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String createMessage(OPMessageType messageType) {
		return createMessage(StringUtil.padLeft(new Integer(messageType.value()).toString(), 4, '0', true), new Hashtable());
	}
	
	/**
	 * 
	 * @param message
	 */
	private static void log(String message) {
		System.out.println(now("HH:mm:ss:SSS") + "  " + message);
	}
	
	/**
	 * @param dateFormat
	 * @return
	 */
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return sdf.format(cal.getTime());
	}
}
