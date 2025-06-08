package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;


@PropertyBean
public interface HoldReleasePropertyBean extends IProperty{
	
	/**
	 * Indicate process location.
	 */
	@PropertyBeanAttribute(defaultValue = "AF")
	public String getProcessLocation();
	
	/**
	 * Product types it handles
	 */
	@PropertyBeanAttribute(defaultValue ="Frame,Engine,AfOnSeq")
	public String[] getProductTypes();
			
	/**
	 * Indicate if it is sequence based or next preproduction lot based.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSequenceBased();

	/**
	 * Plan code for sequence based preproduction lots.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlanCode();
	
	/**
	 * Process point ID for returned products
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProductReturnProc();
	
	/**
	 * List of process point IDs for return to factory history
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getReturnToFactoryHist();
}
