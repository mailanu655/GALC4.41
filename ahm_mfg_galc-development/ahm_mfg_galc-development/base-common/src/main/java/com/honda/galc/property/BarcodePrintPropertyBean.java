package com.honda.galc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean
public interface BarcodePrintPropertyBean extends IProperty {


	/**
	 * Specify the list of partLocation should be displayed separated by comma, 
	 * for instance FR [Front Right],FL [Front Left] etc
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String[] getPartLocation();
	
	
	/**
	 * Specify the list of Models should be displayed separated by comma, 
	 * for instance 5A,5B,5C etc
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getModelList();
	
	/**
	 * Specify the FORM Id used for the printer to pull the printer list from 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey = "BARCODE_FORM", defaultValue = "Knuckle_Barcode_Label")
	public String getBarcodeForm();
	
	/**
	 * Specify the default number to display in the screen 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getDefaultNumberOfLabels();
	
	/**
	 * Specify the maximum number of prints to be allowed to print 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "500")
	public int getMaxNumberOfLabels();
	
	/**
	 * Specify the delay between each prints
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "200")
	public int getPrintDelayBy();
}
