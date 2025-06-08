
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.util.ArrayList;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public interface ICustomWhereClauseBuilder {

	public ArrayList<String> getDisplayList();
	
	public String getWhereClauseString(ArrayList<String> selectedList);	
}
