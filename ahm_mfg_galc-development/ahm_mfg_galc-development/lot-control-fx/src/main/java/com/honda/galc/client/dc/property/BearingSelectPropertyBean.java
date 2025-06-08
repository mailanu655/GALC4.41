package com.honda.galc.client.dc.property;

import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingSelectPropertyBean</code> is ... .
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
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
@PropertyBean
public interface BearingSelectPropertyBean extends BearingPropertyBean {

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBlockMeasurementsCollected();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBlockMeasurementsEditable();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodMeasurementsCollected();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBlockMeasurementsDisplayReversed();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankMeasurementsDisplayReversed();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodMeasurementsDisplayReversed();
	
	@PropertyBeanAttribute(defaultValue = "BLOCK MC")
	public String getInstalledBlockPartName();
	
	@PropertyBeanAttribute(defaultValue = "MC")
	public NumberType getInstalledBlockPartNameSnType();
		
	@PropertyBeanAttribute(defaultValue = "[ABCD]")
	public String getBlockMeasurementPattern();
	
	@PropertyBeanAttribute(defaultValue = "[1234]")
	public String getConrodMeasurementPattern();
	
	@PropertyBeanAttribute(defaultValue = "[ABCDEF]")
	public String getCrankConrodMeasurementPattern();
	
	@PropertyBeanAttribute(defaultValue = "[123456]")
	public String getCrankMainMeasurementPattern();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public int getBlockMeasurementMaxLength();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public int getCrankMainMeasurementMaxLength();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public int getCrankConrodMeasurementMaxLength();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public int getConrodMeasurementMaxLength();
}
