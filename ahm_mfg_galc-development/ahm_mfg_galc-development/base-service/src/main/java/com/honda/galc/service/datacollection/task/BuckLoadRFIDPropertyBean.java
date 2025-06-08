package com.honda.galc.service.datacollection.task;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

// TODO: Auto-generated Javadoc
/**
 * <h3>BuckLoadRFIDPropertyBean</h3>
 * <h3>The class defines the DB properties
 * used by {@link BuckLoadRFIDTask}</h3> <h4></h4> <h4>
 * Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>.
 *
 * @see BuckLoadRFIDTask
 * @author Hale Xie Sept 1st, 2014
 */
@PropertyBean
public interface BuckLoadRFIDPropertyBean extends IProperty {

	/**
	 * Gets the product id tag.
	 *
	 * @return the product id tag
	 */
	@PropertyBeanAttribute(defaultValue = "PRODUCT_ID")
	public String getProductIdTag();

	/**
	 * Gets the rfid part name.
	 *
	 * @return the rfid part name
	 */
	@PropertyBeanAttribute(defaultValue = "Buck Load RFID")
	public String getRfidPartName();

	/**
	 * Gets the rfid part id.
	 *
	 * @return the rfid part id
	 */
	@PropertyBeanAttribute(defaultValue = "A0000")
	public String getRfidPartId();

	/**
	 * Gets the rfid tag name.
	 *
	 * @return the rfid tag name
	 */
	@PropertyBeanAttribute(defaultValue = "RFID")
	public String getRfidTagName();

}
