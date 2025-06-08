package com.honda.galc.property;


/**
 * 
 * <h3>RecipePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipePropertyBean description </p>
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
 * <TD>Jan 2, 2013</TD>
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
 * @since Jan 2, 2013
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 10, 2015
 * Recipe Download Service
 */
@PropertyBean(componentId ="Default_Recipe")
public interface RecipePropertyBean extends ProductOnHlPropertyBean, DataMappingPropertyBean{
	
	
	/**
	 * A list of process point ids that specifies all the lot control rules defined under 
	 * these process points will be down-loaded in a single transaction. 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRecipeProcessPointIds();
	
	/**
	 * A list of process point ids that specifies all the lot control rules defined under 
	 * MBPN processes. These process points will be down-loaded in a single transaction. 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRecipeMbpnProcessPointChildIds();

	/**
	 * Down recipe next product of the same sub id
	 * for example: 
	 *    for Knuckle, 
	 *      false means L, R, L, R, ...
	 *      true  means L, L, ...
	 *      
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isFirstOfSameSubid();


	/**
	 * Indicate that all fields in the down-load recipe are characters.
	 * false: means mixed data type for down-loading recipe
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAllStringRecipe();

	/**
	 * indicate device data mapping
	 * Two mapping types are support: MAP_BY_PART_NAME and MAP_BY_RULE_INDEX 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "MAP_BY_PART_NAME")
	public String getPlcDataMapping();

	/**
	 * Indicate if Kd Lot information is required in down load recipe
	 * KD lot information will not retrieved and put into context if not required.
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isDownloadLotInfo();

	/**
	 * One set of Lot Control Rules for all Sub IDs
	 * for example: if Knuckle L and Knuckle R share the same rule, then this should be true
	 *              If Knuckle L and Knuckle R have different rule, then this should be false 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isOneRuleForSubIds();

	/**
	 * Size of the cart, default to 15 for knuckle
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "15")
	public int getCartSize();
	
	//Use Product Sequence or InprocessProduct table for next product
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isNextProductIdStampingSeqBased();
	
	
	
	/**
	 * Map for Minimum cure time, the key is the cure time check product result
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1")
	public String getProductResultCureTimeMin();
	
	/**
	 * Map for Maximum cure time, the key is the cure time check product result
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public String getProductResultCureTimeMax();
	
	/**
	 * The flag to indicate if getting information about current product or next product
	 *
	 * @return true if it is getting the information of next product
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUseNextProductId();

	/**
	 * Validate the request VIN/Product is the last downloaded VIN/Product
	 * Notes: This is not apply to multiple products
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidateSkippingProduct();

	
	/**
	 * Filter unwanted lot control rules  
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLotControlRuleFilter();
	
	
	/**
	 * Build Result Process Points
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getBuildResultProcessPoints();
	
	/**
	 * Only selected part in the filter will be retrieved, 
	 * otherwise  all build results for all parts will be retrieved 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getBuildResultPartFilter();
	
	/**
	 * Tag separated by comma. Only listed tag(s) will be retrieved.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getBuildResultTag();
	
	/**
	 * Validate if Product passed On process 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isValidateProductPassedAfOn();
	
	/**
	 * Validate if Product was already produced by check build result
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isValidateProducedByBuildResult();

	
}
