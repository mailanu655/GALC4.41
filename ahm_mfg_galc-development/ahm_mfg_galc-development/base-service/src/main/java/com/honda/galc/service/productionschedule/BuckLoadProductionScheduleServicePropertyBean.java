package com.honda.galc.service.productionschedule;

import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

/**
 * <h3>BuckOnProductionScheduleService</h3> <h3>The class defines the DB properties used by {@link BuckLoadProductionScheduleServiceImpl} <code></h3>
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
 * .
 * 
 * @see BuckLoadProductionScheduleServiceImpl
 * @author Hale Xie August 20, 2014
 */
public interface BuckLoadProductionScheduleServicePropertyBean extends SystemPropertyBean{

	@PropertyBeanAttribute(defaultValue="true")
	public boolean isValidateProductId();
	
	@PropertyBeanAttribute
	public String getProcessLocation();
	
	@PropertyBeanAttribute(defaultValue="AFB", propertyKey="AFB_ATTRIBUTE_NAME")
	public String getAFBAttributeName();
	
	@PropertyBeanAttribute(defaultValue="PLAN_CODE")
	public String getPlanCodeTag();
	
	@PropertyBeanAttribute(defaultValue="KD_LOT", propertyKey="KD_LOT_TAG")
	public String getKDLotTag();
	
	@PropertyBeanAttribute(defaultValue="NEXT_VIN", propertyKey="NEXT_VIN_TAG")
	public String getNextVINTag();

	@PropertyBeanAttribute(defaultValue="LAST_VIN", propertyKey="LAST_VIN_TAG")
	public String getLastVINTag();

	@PropertyBeanAttribute(defaultValue="PRODUCT_SPEC_CODE")
	public String getProductSpecCodeTag();
	
	@PropertyBeanAttribute(defaultValue="FIF_CODES", propertyKey="FIF_CODES_TAG")
	public String getFIFCodesTag();
	
	@PropertyBeanAttribute(defaultValue="ALC_INFO_CODE")
	public String getInfoCodeTag();
	
	@PropertyBeanAttribute(defaultValue="AFB", propertyKey="AFB_TAG")
	public String getAFBTag();
	
	@PropertyBeanAttribute(defaultValue="PRODUCTION_LOT", propertyKey="PRODUCTION_LOT_TAG")
	public String getProductionLotTag();
	
	@PropertyBeanAttribute(defaultValue="VIN", propertyKey="VIN_TAG")
	public String getVINTag();
}
