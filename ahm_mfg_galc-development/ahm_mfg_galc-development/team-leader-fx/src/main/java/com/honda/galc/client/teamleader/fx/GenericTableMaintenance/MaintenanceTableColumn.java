
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableColumn;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class MaintenanceTableColumn extends TableColumn {

	@XStreamAlias ("name")
	private String _entityColName = "";
	
	
	@XStreamAlias ("datatype")
	private String _columnDataType = "java.lang.String";
	
	
	@XStreamAlias ("values")
	private List<String> _columnValues = new ArrayList<String>();
	
	public String getEntityColumnName() {
		return _entityColName;
	}
	
	public void setEntityColumnName(String name) {
		_entityColName = name;
	}
	
	
	public String getColumnDataType() {
		return _columnDataType;
	}
	
	public void setColumnDataType(String dataType) {
		_columnDataType = dataType;
	}

	public List<String> getColumnValues() {
		return _columnValues;
	}

	public void setColumnValues(List<String> values) {
		_columnValues = values;
	}
	
	

}
