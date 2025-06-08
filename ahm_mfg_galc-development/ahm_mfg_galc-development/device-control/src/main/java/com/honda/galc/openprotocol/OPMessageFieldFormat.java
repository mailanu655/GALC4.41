/**
 * 
 */
package com.honda.galc.openprotocol;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class represents an OPMessageFieldFormat for a field in
 * the OPMessageDefinition control file
 * 
 * @author Subu Kathiresan
 */
public class OPMessageFieldFormat
{
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id;
	
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String _name;
	
	@XStreamAlias("type")
	@XStreamAsAttribute
	private String _type;
	
	@XStreamAlias("length")
	@XStreamAsAttribute
	private String _length;
	
	@XStreamAlias("defaultValue")
	@XStreamAsAttribute
	private String _defaultValue;

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
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		_type = type;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return _type;
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
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue)
	{
		_defaultValue = defaultValue;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue()
	{
		return _defaultValue;
	}
}
