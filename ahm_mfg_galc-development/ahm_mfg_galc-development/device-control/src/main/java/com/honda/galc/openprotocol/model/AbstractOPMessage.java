package com.honda.galc.openprotocol.model;

import java.util.Date;

import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.openprotocol.OPMessageType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 *
 * This abstract class has accessors for all fields in the header of an 
 * Open Protocol message.  Since headers for all Open Protocol messages have 
 * the same structure this class could be extended to implement a concrete 
 * value object for any Open protocol message 
 */
@XStreamAlias("OPMessage")
public abstract class AbstractOPMessage extends MeasurementInputData
{
	@XStreamAlias("LENGTH")	
	public String _msgLength = "";
	
	@XStreamAlias("MESSAGE_ID")	
	public String _msgId = "";
	
	@XStreamAlias("REVISION") 
	public String _revision = "";
	
	@XStreamAlias("NO_ACK_FLAG")  
	public String _noAckFlag = "";
	
	@XStreamAlias("STATION_ID")  
	public String _stationId = "";
	
	@XStreamAlias("SPINDLE_ID")  
	public String _spindleId = "";
	
	@XStreamAlias("RESERVED_FIELD")
	public String _reservedField = "";
	
	private Date _createdTime = new Date();
	private String _msgString = "";
	
	public int getMessageLength()
	{
		return Integer.parseInt(_msgLength);
	}
	
	public String getMessageId()
	{
		return _msgId;
	}
	
	public OPMessageType getMessageType()
	{
		return OPMessageType.getMessageType(_msgId);
	}
	
	public int getRevision()
	{
		return Integer.parseInt(_revision);
	}
	
	public boolean getIsAckRequired()
	{
		return Boolean.parseBoolean(_noAckFlag);
	}
	
	public String getReservedField()
	{
		return _reservedField;	
	}
	
	public String getStationId() {
		return _stationId;
	}

	public String getSpindleId() {
		return _spindleId;
	}

	public Date getCreatedTime()
	{
		return _createdTime;
	}
	
	public void setCreatedTime(Date createdTime)
	{
		_createdTime = createdTime;
	}
	
	public void setMessageString(String msgString)
	{
		_msgString = msgString;
	}
	
	public String getMessageString()
	{
		return _msgString;
	}
}

