package com.honda.galc.oif.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/** * * 
* @version 1
* @author Gangadhararao Gadde 
* @since Aug 08, 2017
*/
@PropertyBean(componentId = "OIF_IN_HOUSE_PRIORITY_PLAN")
public interface InHouseSchedulePropertyBean extends IProperty {
	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInterfaceId();
	
	/**
	 * Specifies length of each line in incoming message for the task
	 */
	@PropertyBeanAttribute(defaultValue = "130")
	public int getMessageLineLength();
	
	/**
	 * Specifies mapping of data in incoming string
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getParseLineDefs();
	
	/**
	 * Specifies the flag to generate MBPN Sub assemble ID
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isGenerateProductionId();

	/**
	 * Specifies the flag to initialize tail is it is missing (for example for new lines/locations)
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getInitializeTail();

	/**
	 * Specifies the flag to order lots using next production lot
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean getUseNextProductionLot();

	/**
	 * Specifies increment for next sequence
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getSequenceIncrement();



	/**
	 * Specifies MBPN Sub Assemble Id seq length
	 */
	@PropertyBeanAttribute(defaultValue = "4")
	public int getSeqLength();
	
	/**
	 * specifies transaction timeout
	 */
	@PropertyBeanAttribute(defaultValue = "300")
	public int getTransactionTimeout();

	/**
	 * specifies demand types to be placed on hold
	 */
	@PropertyBeanAttribute(defaultValue = "MP")
	public String getNotHoldDemandTypes();
	
	/**
	 * specifies if we are to do automatic holds or not
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutomaticHold();
	
	/**
	 * specifies if we need to put Remake lots on hold based on Remake Flag
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isReplicateRemakeLots();
}
