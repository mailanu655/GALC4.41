package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>KnucklesProperty</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnucklesProperty description </p>
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
 * Nov 27, 2010
 *
 */
@PropertyBean(componentId ="Default_Knuckle")
public interface KnucklePropertyBean extends PartLotPropertyBean, ViewProperty{
	
	@PropertyBeanAttribute(defaultValue = "5")
	public int getKnuckleLabelPrintingStartPosition();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUseSubproductPassingCount();

	@PropertyBeanAttribute(defaultValue = "MS0FK16001")
	public String getOifLastPassingProcessPoint();
	

}
