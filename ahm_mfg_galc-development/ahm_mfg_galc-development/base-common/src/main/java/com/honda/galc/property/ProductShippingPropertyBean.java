package com.honda.galc.property;

/**
 * 
 * 
 * <h3>ProductShippingPropertyBean Class description</h3>
 * <p> ProductShippingPropertyBean description </p>
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
 *
 * </TABLE>
 *   
 * @author Kamlesh Maharjan<br>
 * July 26, 2023
 *
 *
 */
public interface ProductShippingPropertyBean extends IProperty{
	
	/**  
	 * authorization group 
	 */
	public String getAuthorizationGroup();
	 
	@PropertyBeanAttribute(propertyKey ="BLOCK_DUNNAGE_LIMIT")
	public int getBlockDunnageLimit(); 
	
	@PropertyBeanAttribute(propertyKey ="HEAD_DUNNAGE_LIMIT")
	public int getHeadDunnageLimit();
			
	@PropertyBeanAttribute(propertyKey ="CONROD_DUNNAGE_LIMIT")
	public int getConrodDunnageLimit();
			
	@PropertyBeanAttribute(propertyKey ="CRANKSHAFT_DUNNAGE_LIMIT")
	public int getCrankshaftDunnageLimit();
			
	@PropertyBeanAttribute(propertyKey ="TOTAL_DUNNAGE_LIMIT")
	public int getTotalDunnageLimit();
	
	@PropertyBeanAttribute(propertyKey ="ALLOW_MIXED_PRODUCTS",defaultValue ="false")
	public boolean isAllowMixedProducts();
			
	@PropertyBeanAttribute(propertyKey ="LIST_FONT_SIZE",defaultValue ="30")
	public int getListFontSize();
	
	@PropertyBeanAttribute(propertyKey ="BLOCK_DUNNAGE_SIZE")
	public int getBlockDunnageSize();
			
	@PropertyBeanAttribute(propertyKey ="HEAD_DUNNAGE_SIZE")
	public int getHeadDunnageSize();		
	
	@PropertyBeanAttribute(propertyKey ="CONROD_DUNNAGE_SIZE")
	public int getConrodDunnageSize();
	
	@PropertyBeanAttribute(propertyKey ="CRANKSHAFT_DUNNAGE_SIZE")
	public int getCrankshaftDunnageSize();
	
	@PropertyBeanAttribute(propertyKey ="HEAD_CHECK_TYPES")
	public String[] getHeadCheckTypes();
	
	@PropertyBeanAttribute(propertyKey ="BLOCK_CHECK_TYPES")
	public String[]  getBlockCheckTypes();
	
	@PropertyBeanAttribute(propertyKey ="CONROD_CHECK_TYPES")
	public String[] getConrodCheckTypes();

	@PropertyBeanAttribute(propertyKey ="CRANKSHAFT_CHECK_TYPES")
	public String[]  getCrankshaftCheckTypes();
	
	@PropertyBeanAttribute(propertyKey ="HEAD_INSTALLED_PARTS",defaultValue = "")
	public String[] getHeadInstalledParts();
	
	@PropertyBeanAttribute(propertyKey ="BLOCK_INSTALLED_PARTS",defaultValue = "")
	public String[] getBlockInstalledParts();
	
	@PropertyBeanAttribute(propertyKey ="CONROD_INSTALLED_PARTS",defaultValue = "")
	public String[] getConrodInstalledParts();

	@PropertyBeanAttribute(propertyKey ="CRANKSHAFT_INSTALLED_PARTS",defaultValue = "")
	public String[] getCrankshaftInstalledParts();
	
	@PropertyBeanAttribute(propertyKey ="ACTIVE_TRAILER_COUNT",defaultValue ="10")
	public int getActiveTrailerCount();
			
	@PropertyBeanAttribute(propertyKey ="ATTRIBUTE_FOR_SHIP_MODEL_BLOCKED",defaultValue ="SHIP_MODEL_BLOCKED_")
	public String getBuildAttrbuteKey();
	
	@PropertyBeanAttribute(propertyKey ="PRODUCT_TYPES",defaultValue ="head,block")
	public String[] getProductTypes();
		
	@PropertyBeanAttribute(propertyKey ="CLEAR_DUNNAGE",defaultValue ="true")
	public boolean isClearDunnage();
	
	@PropertyBeanAttribute(propertyKey = "PARTIAL_DUNNAGE_AUTH_GROUP")
	public String getPartialDunnageAuthorizationGroup();
	
	/**
	 * Specifies the dunnage ID mask for dunnage number validation
	 */
	@PropertyBeanAttribute(propertyKey = "DUNNAGE_ID_MASK", defaultValue = "")
	public String getDunnageIdMask();
	
}
