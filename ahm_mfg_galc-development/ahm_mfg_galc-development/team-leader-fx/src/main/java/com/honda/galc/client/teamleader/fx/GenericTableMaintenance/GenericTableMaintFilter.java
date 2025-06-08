
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
@XStreamAlias ("filter")
public class GenericTableMaintFilter
{
	@XStreamAlias ("id")
	@XStreamAsAttribute
	private String _id = "";
	
	@XStreamAlias ("seq")
	private int _seqNumber = -1;
	
	@XStreamAlias ("entity")
	private String _entity = "";
	
	@XStreamAlias ("column")
	private String _column = "";
	
	@XStreamAlias ("datatype")
	private String _dataType = "";
	
	@XStreamAlias ("label")
	private String _label = "";
	
	@XStreamAlias ("default")
	private String _defaultValue = "";
	
	@XStreamAlias ("paramtype")
	private String _paramType = "";
		
	@XStreamAlias ("values")
	private String _values = "";
	
	@XStreamAlias ("valuesource")
	private FilterValueSource _valueSource = FilterValueSource.FREEFORM;
	
	@XStreamAlias ("allownull")
	private boolean _isNullAllowed = false;
		
	@XStreamAlias ("allowfreeform")
	private boolean _isFreeFormTextAllowed = false;
	
	@XStreamAlias ("columnname")
	private String columnName = "";
	
	@XStreamAlias ("editable")
	private boolean editable = false;
	
	@XStreamAlias ("daoname")
	private String _daoName = "";
		
	

	private ArrayList<String> _selectedValues;
	
	public String getId() {
		return _id;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public int getSequenceNumber() {
		if (_seqNumber <= 0)
			return 9999;
		return _seqNumber;
	}
	
	public void setSequenceNumber(int seqNumber) {
		_seqNumber = seqNumber;
	}
	
	public String getColumn() {
		return _column;
	}
	
	public void setColumn(String column) {
		_column = column;
	}
	
	public String getDataType() {
		return _dataType;
	}
	
	public void setDataType(String dataType) {
		_dataType = dataType;
	}
	
	public String getEntity() {
		return _entity;
	}
	
	public void setEntity(String entity) {
		_entity = entity;
	}
	
	public boolean isNullAllowed() {
		return _isNullAllowed;
	}
	
	public void setNullAllowed(boolean isNullAllowed) {
		_isNullAllowed = isNullAllowed;
	}
	
	public boolean isFreeFormTextAllowed() {
		return _isFreeFormTextAllowed;
	}
	
	public void setFreeFormTextAllowed(boolean isFreeFormTextAllowed) {
		_isFreeFormTextAllowed = isFreeFormTextAllowed;
	}
	
	public String getLabel() {
		return _label;
	}
	
	public void setLabel(String label) {
		_label = label;
	}
	
	public String getDefaultValue() {
		return _defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		_defaultValue = defaultValue;
	}
	
	public String getParamType() {
		return _paramType;
	}
	
	public void setParamType(String paramType) {
		_paramType = paramType;
	}
	
	public String getValues() {
		return _values;
	}
	
	public void setValues(String values) {
		_values = values;
	}
	
	public FilterValueSource getFilterValueSource() {
		return _valueSource;
	}
	
	public void setFilterValueSource(FilterValueSource valueSource) {
		_valueSource = valueSource;
	}		
	
	public ArrayList<String> getSelectedValues() {
		return _selectedValues;
	}
	
	public void setSelectedValues(ArrayList<String> selectedValues) {
		_selectedValues = selectedValues;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnname) {
		this.columnName = columnname;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public String getDaoName() {
		return _daoName;
	}

	public void setDaoName(String name) {
		_daoName = name;
	}
}
