package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>ProductSequencePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSequencePropertyBean description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Apr 7, 2010
 *
 */
@PropertyBean(componentId ="Default_ProductSequence")
public interface ProductSequencePropertyBean  extends IProperty {

	/**
	 * Get Process Point Id entering product sequence
	 * @return
	 */
	public String getInProductSequenceId();
	
	/**
	 * Get Process Point Id Out product sequence
	 * @return
	 */
	public String getOutProductSequenceId();
	
	/**
	 * Default value for ExpectedProductManager ProductSequenceManager
	 * @return
	 */	
	@PropertyBeanAttribute (defaultValue = "com.honda.galc.client.datacollection.observer.ProductSequenceManager")
	public String getExpectedProductManager();
}
