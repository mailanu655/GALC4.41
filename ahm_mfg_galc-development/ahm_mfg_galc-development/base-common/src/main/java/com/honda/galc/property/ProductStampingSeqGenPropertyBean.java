package com.honda.galc.property;

import java.util.Map;



/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 8 , 2017
 * 
 */

public interface ProductStampingSeqGenPropertyBean extends IProperty {

	/**
	 * define PartSnPrefix list
	 */
	@PropertyBeanAttribute(defaultValue = "BMP,IPU,TDU,TMU")
	public String getMbpnPartEnum();

	/** define MBPN mainNo - MBPN PartSnPrefix   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getMbpnPartPrefix(Class<?> clasz);

	/** define MBPN Sub Assemble Id Rule   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getSubAssembleIdRule(Class<?> clasz);

	/** define MBPN Seq Length Map   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getSeqLengthMap();
	
	/** define ProductTypes for process Locations   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductType();


}