package com.honda.galc.property;

public interface WETrackingPropertyBean extends RecipePropertyBean {
	@PropertyBeanAttribute(propertyKey="PLAN_CODE",defaultValue="")
	String getPlanCode();
	
	@PropertyBeanAttribute(propertyKey="ORDER_TYPE",defaultValue="KD_LOT")
	String getOrderType();
	
	@PropertyBeanAttribute(propertyKey="IGNORE_REMAKE",defaultValue="false")
	String getIgnoreRemakeFlag();

	/*
	 * If it is true, return next KD lot size.
	 * If it is false, return current KD lot size.
	 */
	@PropertyBeanAttribute(propertyKey="RETURN_NEXT_KD_LOT_QUANTITY",defaultValue="false")
	boolean isReturnNextKDLotQuantity();
		
	
	/* specifies if we have to always return DATA_COLLECTION_COMPLETE as true for every cycle
	 * 
	 */
	@PropertyBeanAttribute(defaultValue="false")
	boolean isAlwaysReplyComplete();
	
	
	
}
