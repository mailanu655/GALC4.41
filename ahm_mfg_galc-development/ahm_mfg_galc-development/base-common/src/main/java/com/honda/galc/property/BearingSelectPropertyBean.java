package com.honda.galc.property;

import java.util.Map;

import com.honda.galc.data.ProductNumberDef.NumberType;

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
	public boolean isBlockMeasurementsDisplayReversed();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankSnCollect();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankSnCollectToogable();

	@PropertyBeanAttribute(defaultValue = "25")
	public int getCrankSnLength();

	/**
	 * Comma delimited, pair(s) of zero based index and length for token(s) that can be used to identify Crank Sn Type
	 * for example: "0,3"  
	 * It is optional.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getCrankSnTypeIx(); 
	
	/**
	 * <pre>
	 * Start location index for Crank Main Journal Meassurements embeded in Crank SN. 
	 * It can be mapped by crank sn type (key) that can be defined by crankSnTypeIx property. 
	 * It is optional, default is 16. 
	 * Usage :
	 * To override default, define property without key element'{...}' or with wild card '{*}' key element:
	 *       CRANK_MAIN_MEASUREMENTS_START_IX = new_value  or
     *       CRANK_MAIN_MEASUREMENTS_START_IX{*} = new_value 
	 * To add values for different Crank SN Types use curly brackets to indicate key (CrankSn Type): 
	 *       CRANK_MAIN_MEASUREMENTS_START_IX{50TCI} = new_value(20) 
	 *       where key '50TCI' is parsed from Crank SN according to 'CrankSnTypeIx' property.
	 * </pre>
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute()
	public Map<String, Integer> getCrankMainMeasurementsStartIx(Class<Integer> clazz);

	/**
	 * <pre>
	 * Start location index for Crank Conrod Journal Meassurements embeded in Crank SN. 
	 * It can be mapped by crank sn type (key) that can be defined by crankSnTypeIx property. 
	 * It is optional, default is 21. 
	 * Usage :
	 * To override default, define property without key element'{...}' or with wild card '{*}' key element:
	 *       CRANK_CONROD_MEASUREMENTS_START_IX = new_value  or
     *       CRANK_CONROD_MEASUREMENTS_START_IX{*} = new_value  or
	 * To add values for different Crank SN Types use curly brackets to indicate key (CrankSn Type): 
	 *       CRANK_CONROD_MEASUREMENTS_START_IX{50TCI} = new_value 
	 *       where key '50TCI' is parsed from Crank SN according to 'CrankSnTypeIx' property.
	 * </pre>
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute()
	public Map<String, Integer> getCrankConrodMeasurementsStartIx(Class<Integer> clazz);

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankMeasurementsDisplayReversed();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodMeasurementsCollected();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodSnCollect();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodSnCollectToogable();

	@PropertyBeanAttribute(defaultValue = "19")
	public int getConrodSnLength();

	@PropertyBeanAttribute(defaultValue = "16")
	public int getConrodMeasurementStartIx();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isConrodMeasurementsDisplayReversed();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAlarmInvalidCrankSn();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAlarmValidCrankSn();	
	
	@PropertyBeanAttribute(defaultValue = "BLOCK MC")
	public String getInstalledBlockPartName();
	
	@PropertyBeanAttribute(defaultValue = "MC")
	public NumberType getInstalledBlockPartNameSnType();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCrankSnCollected();
}
