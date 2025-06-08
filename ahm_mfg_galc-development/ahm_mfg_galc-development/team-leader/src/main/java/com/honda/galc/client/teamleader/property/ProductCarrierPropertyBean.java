package com.honda.galc.client.teamleader.property;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="Default_ProductCarrier")
public interface ProductCarrierPropertyBean extends CommonTlPropertyBean{
	/**
	 * Specify the process point that will use to track the product
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getTrackingProcessPoint();
	
	/**
	 * Specify the process point that will use to track the product when Rack is ready to ship
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getReadyToShipTrackingProcessPoint();
	
	/**
	 * Specify the Maximum number products that can be married to a carrier
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "4")
	public int getNumberOfProductsInRack();

	/**
	 * CHeck if different Product Spec allowed in Carrier
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "false")
	public boolean isAllowMultiProductSpec();
	
	/**
	 * Specify the Maximum length of the carrier ID
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "0")
	public int getCarrierIdLength();
	
	/**
	 * Specify the mask format for the Carrier ID
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "*")
	public String getCarrierIdMask();
	
	/**
	 * CHeck if user allowed to delete the product from Carrier
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "TRUE")
	public boolean isAllowDelete();

	/**
	 * Specify the valid process point seperated by comma 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getValidLines();
}
