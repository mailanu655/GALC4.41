/**
 * 
 */
package com.honda.galc.device.simulator.torque;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author Kathiresan Subu
 *
 */
@XStreamAlias("Field")
public class OPMessageField 
{
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String _name = "";
	
	@XStreamAlias("value")
	@XStreamAsAttribute
	private String _value = "";
	
    /** Need to allow bean to be created via reflection */
    public OPMessageField() {}

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
