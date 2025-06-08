package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>ProductOnHlPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductOnHlPropertyBean description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jan 24, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 24, 2013
 */
@PropertyBean(componentId ="Default_HlProductOn")
public interface ProductOnHlPropertyBean extends SystemPropertyBean {
	/**
	 * Process Location
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessLocation();
	
	/**
	 * The flag to validate the previous process point id of a product.
	 *
	 * @return true if it is required to validate the previous process point id of a product.
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidatePreviousProcessPointId();
	
	/**
	 * The flag to validate whether the product has
	 * already been processed at the current process point
	 *
	 * @return true if required to perform this check.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProductAlreadyProcessedCheckEnabled();
	
	/**
	 * Specifies process point id for process point before this one on the line.
	 * It's OIF process point id for the recipe download station
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPreviousProcessPointId();
	
	/**
	 * Product Id tags, separate by ","
	 * for Knuckle always start in the sequence: Left Knuckle, Right Knuckle
	 */
	@PropertyBeanAttribute(defaultValue = "PRODUCT_ID")
	public String getProductIdTags();
	
	/**
	 * Instruction on take substring from a given Tag Value
	 * Key: is the tag name
	 * Value: contains start/end index separate by comma
	 * example: SUB_STRING{PRODUCT_ID} = 5,12
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getSubString();
	
	/**
	 * Build attributes names separate by ","
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getBuildAttributes();

	/**
	 * Product on specific tasks
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getTasks();
	
	/**
	 * invoke tracking service to track the next product
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAutoTracking();
	
	/**
	 * flag to automatically call broadcast service.
	 *
	 * @return true, if automatically call broadcasting
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoBroadcasting();
	
	/**
	 * Tracking Process Point Id used to re-direct tracking.
	 * Process Point Id will be used for tracking if this is not defined.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingProcessPointId();
	

	/**
	 * product names for multiple product
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProducts();

	/**
	 * Plan Code, must provide for headless on process point
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlanCode();

	/**
	 * Interval between sequence number
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getSequenceInterval();

	/**
	 * Indicate if to generate shipping info and shipping details at on station
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateShippingSchedule();
	

	/**
	 * Validate the existence of preproduction lot
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCheckPreproductionLot();

	/**
	 * Indicate collect build result at the on service
	 * Initial implementation for MBPN product only
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCollectData();
	
	/**
	 * Indicate if MC Product Structure should be created
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateProductStructure();
	
	/**
	 * Indicate if check of product spec from PRODUCT_ID_MASK_TBX is required or not
	 * Need for MbpnOn service if they want to input product spec for product from
	 * database according to product Id mask. 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckProductSpec();

	/**
	 * Product check types for checker
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductCheckTypes();
	
	/**
	 * Flag indicating if create product in on service
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUpdateProductProductionLot();

	/**
	 * Mbpn product sub types to be processed
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMbpnProductTypes();

	/**
	 * Build Attribute name for MBPN
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "MBPN")
	public String getMbpnAttributeName();
	
	/**
	 * Build Attribute name for MBPN
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "FRAME")
	public String getScheduleProductType();

	/**
	 * Flag to invoke notification in log and notify
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isNotifyClient();

	/**
	 * Flag to indicate if Parent MBPN lots should be set to DONE (2)
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCompleteParentLots();

	/**
	 * indicate if calculate next expected using exist Weld On algorithm 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isWeldOnNextExpected();
	
	/**
	 * indicate if Error Code needs to be sent if Build Attributes are missing for Weld Tracking Lot Request
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isErrorCodeMissingBuildAttributes();
	
	/**
	 * indicate if 'On Product' can have it's Product Spec Code updated
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isProductSpecCodeUpdateAllowed();
}
