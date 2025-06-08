/**
 * 
 */
package com.honda.galc.openprotocol;

import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.*;

/**
 * This class represents an OPMessageDefinition file that
 * defines the field definitions for a single Open Protocol Message
 * 
 * @author Subu Kathiresan
 */
@XStreamAlias("OPMessage")
public class OPMessageDefinition
{
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id;
	
	@XStreamAlias("version")
	@XStreamAsAttribute
	private String _version;
	
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
	
	/** Need to allow bean to be created via reflection */
	public OPMessageDefinition() {}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return _id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) 
	{
		_id = id;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return _version;
	}
	
	/**
	 * @param id the version to set
	 */
	public void setVersion(String version) 
	{
		_version = version;
	}
	
	/**
	 * @param fixedLength the fixedLength to set
	 */
	public void setFixedLength(boolean fixedLength) 
	{
		_fixedLength = fixedLength;
	}

	/**
	 * @return the fixedLength
	 */
	public boolean isFixedLength() 
	{
		return _fixedLength;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) 
	{
		_delimiter = delimiter;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() 
	{
		return _delimiter;
	}

	/**
	 * @return the hasFieldNumbers
	 */
	public boolean hasFieldNumbers() 
	{
		return _hasFieldNumbers;
	}

	/**
	 * @param fieldNumbers the hasFieldNumbers to set
	 */
	public void setHasFieldNumbers(boolean hasFieldNumbers) 
	{
		_hasFieldNumbers = hasFieldNumbers;
	}
	
	/**
	 * 
	 * @param className
	 */
	public void setClassName(String className)
	{
		_className = className;
	}
	
	/**
	 * 
	 */
	public String getClassName()
	{
		return _className;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<OPMessageFieldFormat> fields)
	{
		_fields = fields;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<OPMessageFieldFormat> getFields()
	{
		return _fields;
	}
}
