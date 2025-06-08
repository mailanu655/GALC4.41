/**
 * 
 */
package com.honda.galc.client.device.lotcontrol.immobi;

import java.util.ArrayList;
import java.util.TreeMap;

import com.thoughtworks.xstream.annotations.*;


/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
@XStreamAlias("ImmobiMessage")
public class ImmobiMessageDefinition {

	@XStreamAlias("id")
	@XStreamAsAttribute
	private String _id;

	@XStreamAlias("version")
	@XStreamAsAttribute
	private String _version;	

	@XStreamAlias("severity")
	@XStreamAsAttribute
	private String _severity;

	@XStreamAlias("description")
	@XStreamAsAttribute
	private String _description;

	@XStreamAlias("className")
	@XStreamAsAttribute
	private String _className;

	@XStreamImplicit(itemFieldName="field")
	private ArrayList<ImmobiMessageFieldFormat> _fields = new ArrayList<ImmobiMessageFieldFormat>();
	private TreeMap<String, ImmobiMessageFieldFormat> _fieldsMap = null;

	/** Need to allow bean to be created via reflection */
	public ImmobiMessageDefinition() {}

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
	 * @return the severity
	 * 
	  */
	public String getSeverity()
	{
		return _severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) 
	{
		_severity = severity;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return _description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) 
	{
		_description = description;
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
	public void setFields(ArrayList<ImmobiMessageFieldFormat> fields)
	{
		_fields = fields;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<ImmobiMessageFieldFormat> getFields()
	{
		return _fields;
	}
	
	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public ImmobiMessageFieldFormat getField(String fieldName) {
		return getFieldsMap().get(fieldName);
	}
	
	/**
	 * 
	 * @return
	 */
	private TreeMap<String, ImmobiMessageFieldFormat> getFieldsMap() {
		if (_fieldsMap == null) {
			_fieldsMap = new TreeMap<String, ImmobiMessageFieldFormat>();
			for (ImmobiMessageFieldFormat field: getFields()) {
				_fieldsMap.put(field.getName(), field);
			}
		}
		
		return _fieldsMap;
	}
}
