package com.honda.galc.service.datacollection.task;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>The property bean defines the properties required by {@link LabelPrintDataTask}</h3> 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @author Hale Xie
 * @created Nov 14, 2014
 */
@PropertyBean(componentId = "Default_Label_Print")
public interface LabelPrintDataPropertyBean extends IProperty {
	
	/**
	 * Gets the prints the timestamp tag name.
	 *
	 * @return the prints the timestamp tag name
	 */
	@PropertyBeanAttribute(defaultValue = "PRINT_TIMESTAMP")
	String getPrintTimestampTagName();
	
	/**
	 * Gets the cert manufactured date tag name.
	 *
	 * @return the cert manufactured date tag name
	 */
	@PropertyBeanAttribute(defaultValue = "CERT_MANUFACTURED_DATE")
	String getCertManufacturedDateTagName();

	/**
	 * Gets the cert year tag name.
	 *
	 * @return the cert year tag name
	 */
	@PropertyBeanAttribute(defaultValue = "CERT_YEAR")
	String getCertYearTagName();

	/**
	 * Gets the cert month tag name.
	 *
	 * @return the cert month tag name
	 */
	@PropertyBeanAttribute(defaultValue = "CERT_MONTH")
	String getCertMonthTagName();

	/**
	 * Gets the cert type tag name.
	 *
	 * @return the cert type tag name
	 */
	@PropertyBeanAttribute(defaultValue = "CERT_TYPE")
	String getCertTypeTagName();
}
