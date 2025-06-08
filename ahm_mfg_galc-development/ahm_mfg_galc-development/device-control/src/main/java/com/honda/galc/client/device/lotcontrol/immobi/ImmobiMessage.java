/**
 * 
 */
package com.honda.galc.client.device.lotcontrol.immobi;

import java.util.Hashtable;


import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
@XStreamAlias("ImmobiMessage")
public class ImmobiMessage {
	
	
	@XStreamAlias("LENGTH")
	private String _length = "";
	
	@XStreamAlias("COMMAND_CODE")
	private String _commandCode = "";
	
	@XStreamAlias("SUB_COMMAND_CODE") 
	private String _subCommandCode = "";
		
	@XStreamAlias("DATA_SEGMENT_LENGTH")
	private String _dataSegmentLength = "";	
	
	@XStreamAlias("SEGMENT_DATA")
	private String _segmentData = "";
	
	private String _severity = "";
	private String _description = "";
	private ImmobiMessageType _messageType = null;
	
	public static final int HEADER_SIZE = 9;
	public static final int LENGTH_FIELD_SIZE = 3;
	
	/**
	 * @param dataSegmentLength the dataSegmentLength to set
	 */
	public void setDataSegmentLength(String dataSegmentLength) {
		_dataSegmentLength = dataSegmentLength;
	}

	/**
	 * @return the dataSegmentLength
	 */
	public String getDataSegmentLength() {
		return _dataSegmentLength;
	}

	/**
	 * @param segmentData the segmentData to set
	 */
	public void setSegmentData(String segmentData) {
		_segmentData = segmentData;
	}

	/**
	 * @return the segmentData
	 */
	public String getSegmentData() {
		return _segmentData;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		_length = length;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return _length;
	}

	/**
	 * @param commandCode the commandCode to set
	 */
	public void setCommandCode(String commandCode) {
		_commandCode = commandCode;
	}

	/**
	 * @return the commandCode
	 */
	public String getCommandCode() {
		return _commandCode;
	}

	/**
	 * @param subCommandCode the subCommandCode to set
	 */
	public void setSubCommandCode(String subCommandCode) {
		_subCommandCode = subCommandCode;
	}

	/**
	 * @return the subCommandCode
	 */
	public String getSubCommandCode() {
		return _subCommandCode;
	}
	
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return _commandCode + _subCommandCode;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		_severity = severity;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return _severity;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		_description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return _description;
	}
	
	/**
	 * 
	 * @return
	 */
	public ImmobiMessageType getMessageType() {
		if (_messageType == null) {
			_messageType = ImmobiMessageType.getMessageType(_commandCode + _subCommandCode);
		}
		return _messageType;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		
		Hashtable<String, String> tags = new Hashtable<String, String>();
		
		tags.put("LENGTH", _length);
		tags.put("COMMAND_CODE", _commandCode);
		tags.put("SUB_COMMAND_CODE", _subCommandCode);
		
		if (_dataSegmentLength != null)
			tags.put("DATA_SEGMENT_LENGTH", _dataSegmentLength);
		
		if (_segmentData != null)
			tags.put("SEGMENT_DATA", _segmentData);
		
		return getMessageType().createStringMessage(tags);
	}
}
