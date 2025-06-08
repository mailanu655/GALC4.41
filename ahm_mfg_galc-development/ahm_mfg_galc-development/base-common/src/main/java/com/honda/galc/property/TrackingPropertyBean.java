package com.honda.galc.property;

import com.honda.galc.property.IProperty;
/**
 * 
 * <h3>TrackingPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TrackingPropertyBean description </p>
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
 * @author Paul Chou
 * Sep 29, 2010
 *
 */
@PropertyBean(componentId ="Default_Tracking")
public interface TrackingPropertyBean extends IProperty{
	
	@PropertyBeanAttribute (defaultValue = "")
	String getShippingProcessPointIds();

	@PropertyBeanAttribute (defaultValue = "false")
	boolean isTrackingHistoryUnique();

	@PropertyBeanAttribute (defaultValue = "com.honda.galc.service.tracking.BaseProductSequenceManager")
	String getProductSequenceManager();
		
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isProcessCount();

	@PropertyBeanAttribute (defaultValue = "false")
	boolean isProcessCountByModel();
	
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isProcessCountByProductionLot();
	
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isProcessCountByProductSpec();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingProcessPointIdOnSuccess();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingProcessPointIdOnFailure();	
	
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isCreateNewThreadForTracking();

	/**
	 * Indicate if update product tracking status
	 * Default to false - not update the tracking status at all if product is shipped
	 * when true, the system will not update product last passing process point and tracking status 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false") 
	boolean isUpdateProductTrackingStatusAfterShipped();
	
	/**
	 * Status used to decide forward tracking process point;
	 * default to over all build results; use for example CHECK_RESULT for validation
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "OVERALL_CHECK_RESULT") 
	String getForwardTrackingStatus();
	
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isUpdateNewLcTracking();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getLcTrackingUrl();	
}
