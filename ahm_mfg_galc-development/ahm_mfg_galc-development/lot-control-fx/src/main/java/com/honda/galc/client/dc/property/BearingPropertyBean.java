package com.honda.galc.client.dc.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPropertyBean</code> is ... .
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
public interface BearingPropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue = "5")
	public int getMainBearingCount();

	@PropertyBeanAttribute(defaultValue = "4")
	public int getConrodCount();

	@PropertyBeanAttribute(defaultValue = "4,5,7,9")
	public Integer[] getMainBearingCountValues();

	@PropertyBeanAttribute(defaultValue = "4,6,8")
	public Integer[] getConrodCountValues();

	@PropertyBeanAttribute(defaultValue = "A,B,C,D,E,F")
	public String[] getMainBearingColumnValues();

	@PropertyBeanAttribute(defaultValue = "1,2,3,4,5,6")
	public String[] getMainBearingRowValues();

	@PropertyBeanAttribute(defaultValue = "1,2,3,4,5,6")
	public String[] getConrodBearingColumnValues();

	@PropertyBeanAttribute(defaultValue = "A,B,C,D,E,F")
	public String[] getConrodBearingRowValues();
	
	@PropertyBeanAttribute(defaultValue = "Conrod")
	public String[] getConrodBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "Main Upper,Main Lower")
	public String[] getMainBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "Conrod,Main Upper,Main Lower")
	public String[] getBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBlockMeasurementNumeric();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isConrodMeasurementNumeric();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankConrodMeasurementNumeric();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCrankMainMeasurementNumeric();
	
	@PropertyBeanAttribute(defaultValue="15")
	public int getFontSize();
	
	/**
	 * This property configures mapping between Bearing select and Bearing pick operations
	 */
	@PropertyBeanAttribute
	public Map<String, String> getBearingPickOperations();
	
	/**
	 * This property represents titles for Bearing select and Bearing pick operations
	 */
	@PropertyBeanAttribute
	public Map<String, String> getOperationTitles();
	
	/**
	 * Switch for turning on/off block validation
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isEnableBlockValidation();
	
	/**
	 * Switch between L4 or V6 depending on the engine model
	 * If 'false', use CONROD_COUNT and MAIN_BEARING_COUNT properties
	 * If 'true', use GAL106TBX (BearingMatrix) to get Main Bearing Count and Conrod Count
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getUseBearingMatrixForBearingAndConrodCounts();
}
