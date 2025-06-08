package com.honda.galc.client.dc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/*   
* @author Jiamei Li<br>
* Nov 4, 2014
*
*/
@PropertyBean
public interface PistonClearancePropertyBean extends IProperty {
	/**
	 * Define already installed cylinder bore part name  
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInstalledCylinderBoreName();
	
	/**
	 * piston part name that will be  installed
	 */
	@PropertyBeanAttribute(defaultValue = "PISTON")
	public String getPistonName();
	/**
	 * clearance part name  
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "CLEARANCE")
	public String getClearanceName();
	
}
