package com.honda.galc.client.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

@PropertyBean(componentId ="Default_CommonProperties")
public interface CommonTlPropertyBean extends SystemPropertyBean{
	
	/**
	 * A list of Strategy types that will shown on the TL Lot Control Rule Strategy column.
	 * The list is String separate by comma, example Hub Press,Mission Install,ProductIdValidation
	 * Strategy type configuration can use strategy display name or strategy simple class name.
	 * If no strategy is configured the strategy column will be disabled.
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getStrategyTypes();
	
	
	/**
	 * Used in Part tab on DC Maintenance Screen. If true, sync part spec
	 * and measurement spec with the gal206tbx (Part) table
	 */
	@PropertyBeanAttribute (propertyKey="SYNC206TABLE",defaultValue = "false")
	public boolean getSync206Table();

	/**
	 * Specify the product type that will enable the print button on skipped product panel
	 * Only print Knuckle for now
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "KNUCKLE")
	public String getPrintSkippedProductType();

	/**
	 * Method name used to sort process point in the process point input panel drop down list
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "getDisplayName")
	public String getProcessPointSortingMethodName();

		
	/**
	 * Method name used to get plant Code
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "T")
	public String getPlantCode();
	

	
	/**
	 * Batch size that can be used for collection processing.   
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "1000")
	public int getBatchSize();
	
	/**
	 * Used in Part tab on Part Maintenance Screen. If true, add GeneratePartSpec menu item to pop up menu
	 * 
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean getGeneratePartSpec();
	
	/**
	 * A list of product type names
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getProductTypeNames();

	/**
	 * Indicate the if application is able to create/edit sql 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isSqlAdmin();
	
	/**
	 * Options of displayed values for skipped product
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "PRODUCT_ID")
	public String getSkippedProductDisplayValue();
	
	/**
	 * Number of Digit for skipped multiple products
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "3")
	public int getSkippedProductLastNumberOfDigits();
	
	@PropertyBeanAttribute (defaultValue = "/resource/com/honda/galc/client/images/ok.gif")
	public String getOkImage();
	
	@PropertyBeanAttribute (defaultValue = "/resource/com/honda/galc/client/images/ng.gif")
	public String getNgImage();
	
	@PropertyBeanAttribute ()
	public String getOnPp();
	
	@PropertyBeanAttribute (defaultValue = " ")
	public String getShippedTrackingStatuses();
	
}
