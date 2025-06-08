package com.honda.galc.property;

import java.util.Map;

@PropertyBean(componentId = "Default_IpuLabelDateMatchProperty")
public interface IpuLabelDateMatchPropertyBean extends ProductCheckPropertyBean{
	/**
	 * Part name for IPU ID 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "IPU ID PART NAME")
	public String getIpuIdPartName();
	
	
	/**
	 * Label part name to IPU part name map
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getLabelToIpuIdPartMap();
	
	/**
	 * Position of the 2 characters in IPU Product ID corresponding to month 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10,11")
	public Integer[] getIpuIdMonthChars();
	
	/**
	 * Position of the 2 characters in IPU Product ID corresponding to year 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "8,9")
	public Integer[] getIpuIdYearChars();
	
	/**
	 * Position of the 2 characters in Label SN corresponding to month 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "13,14")
	public Integer[] getLabelMonthChars();
	
	/**
	 * Position of the 2 characters in Label SN corresponding to year
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "19,20")
	public Integer[] getLabelYearChars();
	
	/**
	 * DateTime starting from 14th char till 25th char
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "13,25")
	public Integer[] getLabelDateTimeChars();
	
		/**
	 * Flag to check if the serial number in the VIN and label match
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckVinSerialNumber();

	/**
	 * Position of the serical number in the VIN
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "14,17")
	public Integer[] getVinSerialNumberPositions();

	/**
	 * Position of the serical number in the Label
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "27,30")
	public Integer[] getLabelSerialNumberPositions();
}
