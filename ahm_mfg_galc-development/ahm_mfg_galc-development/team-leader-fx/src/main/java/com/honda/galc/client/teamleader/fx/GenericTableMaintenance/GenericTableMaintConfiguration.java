
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class GenericTableMaintConfiguration {
	
		
		
	@XStreamImplicit(itemFieldName="column")
	private ArrayList<MaintenanceTableColumn> _displayColumns = new ArrayList<MaintenanceTableColumn>();

	
	public ArrayList<MaintenanceTableColumn> getDisplayColumns() {
		return _displayColumns;
	}
	
	public void setDisplayColumns(ArrayList<MaintenanceTableColumn> columns) {
		_displayColumns = columns;
	}

}
