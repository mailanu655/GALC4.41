package com.honda.galc.client.device.lotcontrol.immobi;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public class ImmobiMessageFieldFormat {
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id;
	
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String _name;
	
	@XStreamAlias("dataType")
	@XStreamAsAttribute
	private String _dataType;
	
	@XStreamAlias("length")
	@XStreamAsAttribute
	private String _length;
	
	@XStreamAlias("lengthField")
	@XStreamAsAttribute
	private String _lengthField;
	
	@XStreamAlias("value")
	@XStreamAsAttribute
	private String _value;

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		_id = id;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return _id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType)
	{
		_dataType = dataType;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType()
	{
		return _dataType;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(String length)
	{
		_length = length;
	}

	/**
	 * @return the length
	 */
	public String getLength()
	{
		return _length;
	}

	/**
	 * @param lengthField the lengthField to set
	 */
	public void setLengthField(String lengthField)
	{
		_lengthField = lengthField;
	}

	/**
	 * @return the lengthField
	 */
	public String getLengthField()
	{
		return _lengthField;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		_value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return _value;
	}
}
