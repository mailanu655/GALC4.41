package com.honda.galc.property;

/**
 * 
 * <h3>CarrierAssociationPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CarrierAssociationPropertyBean description </p>
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
 * <TD>Oct 30, 2018</TD>
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
 * @since Oct 30, 2018
 */
@PropertyBean(componentId ="Default_PaintOnProperties")
public interface CarrierAssociationPropertyBean extends SystemPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isTest();
	
	@PropertyBeanAttribute(defaultValue = "PRODUCT_CARRIER_TBX")
    String getProductCarrierTable();
	
	@PropertyBeanAttribute(defaultValue = "false")
    boolean isEnableAudioAlarm();

	@PropertyBeanAttribute(defaultValue = "") 
	String getTrackingArea();
	
	@PropertyBeanAttribute(defaultValue = "Carrier RFID") 
	String getCarrierIdLabel();

	@PropertyBeanAttribute(defaultValue = "Confirm")
	String getDoneButtonLabel();

	@PropertyBeanAttribute(defaultValue = "true")
	boolean isDoneFunctionKeyEnabled();
	
	@PropertyBeanAttribute(defaultValue = "60")
	int getPreferredProductIdLength();
	
	@PropertyBeanAttribute(defaultValue = "60")
	int getPreferredCarrierIdlength();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isResetByNextCar();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isNotifyLsm();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveLastProduct();

	@PropertyBeanAttribute(defaultValue = "")
	String getReplyClientId();

	@PropertyBeanAttribute(defaultValue = "")
	String getProductIdClientId();
	
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isCarrierAssociationRequired();
  
	/**
	 * Flag to allow text fields to never be read only
	 * @return
	 */
	@PropertyBeanAttribute (propertyKey="ALWAYS_EDITABLE", defaultValue = "false")
	boolean isEditableAlways();
}
