package com.honda.galc.service.msip.property.inbound;

import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
@PropertyBean()
public interface Gpp631PropertyBean extends BaseMsipPropertyBean {
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
	public boolean getIsGenerateProductionId();

	/**
	 * Specifies the flag to initialize tail is it is missing (for example for new lines/locations)
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getInitializeTail();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public String getIsAutomaticHold();
	
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
	 * define PartSnPrefix list
	 */
	@PropertyBeanAttribute(defaultValue = "BMP,IPU,TDU,TMU")
	public String getMbpnPartEnum();
	/**
	 * specifies demand types to be placed on hold
	 */
	@PropertyBeanAttribute(defaultValue = "MP")
	public String getNotHoldDemandTypes();

	/** define MBPN mainNo - MBPN PartSnPrefix   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getMbpnPartPrefix(Class<?> clasz);
	
	@PropertyBeanAttribute(defaultValue = "4")
	public int getSeqLength();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getSubAssembleIdRule();
	
	/** define MBPN Sub Assemble Id Rule   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getSubAssembleIdRule(Class<?> clasz);
	
	/**
	 * specifies transaction timeout
	 */
	@PropertyBeanAttribute(defaultValue = "300")
	public int getTransactionTimeout();
	
}
