package com.honda.galc.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * 
 * <h3>GtsDefaultPropertyBean Class description</h3>
 * <p> GtsDefaultPropertyBean description </p>
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
 * @author Jeffray Huang<br>
 * May 11, 2015
 *
 *
 */
@PropertyBean(componentId = "DEFAULT_GTS")
public interface GtsDefaultPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue = "ESTS")
	public String getTrackingArea();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getCarrierArea();

	@PropertyBeanAttribute(defaultValue = "UNKNOWN")
	public String getUnknownCarrier();
	
	/**
	 * by default, there is no empty carrier
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getEmptyCarrier();
	
	/**
	 * Carrier Font Size
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "14")
	public int getCarrierFontSize();

	/**
	 * Carrier Border Width
	 */
	@PropertyBeanAttribute(defaultValue = "1.0")
	public double getCarrierBorderWidth();

	/**
	 * Gate Icon Size
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "30")
	public int getGateIconSize();
	
	/**
	 * Image Height
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "35")
	public int getImageHeight();
	
	/** 
	 * Image Width
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getImageWidth();
	
	/*
	 * Label Size Height
	 */
	@PropertyBeanAttribute(defaultValue = "15")
	public int getLabelHeight();
	
	/*
	 * Label Size Width
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getLabelWidth();
	
	/*
	 * Lane Segment Width
	 */
	@PropertyBeanAttribute(defaultValue = "3")
	public Double getLaneSegmentWidth();
	
	/**                                                                                              
	 * flag to enable / disable issue physical move request to PLC                                   
	 * @return                                                                                       
	 */                                                                                              
	@PropertyBeanAttribute(propertyKey ="ISSUE_MOVE_REQUEST_ENABLED",defaultValue = "TRUE")          
	public boolean isIssueMoveRequestEnabled();                                                      
	
	/**
	 * Gate close timedelay
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="GATE_TIMEOUT",defaultValue = "3000")
	public int getGateTimeoutDelay();
	
	/**
     * get the tracking control user security group name
     * the user inside this security group can fix the tracking
     * and open/close gate
     * @return
     */
    
	@PropertyBeanAttribute(propertyKey ="TRACKING CONTROL SECURITY GROUP NAME",defaultValue = "3000")
	public String getTrackingControlSecurityGroupName();
	
	@PropertyBeanAttribute(propertyKey ="ALLOW_DUPLICATED_ASSOCIATION",defaultValue = "FALSE")
	public boolean isAllowDuplicatedAssociation();
	
	@PropertyBeanAttribute(propertyKey ="ALLOW_DUPLICATED_CARRIER",defaultValue = "FALSE")
	public boolean isAllowDuplicatedCarrier();
	
	@PropertyBeanAttribute(propertyKey ="ALLOW_GUI_CARRIER_PRODUCT_ASSOCIATION",defaultValue = "TRUE")
	public boolean isAllowGUICarrierProductAssociation();
	
	@PropertyBeanAttribute(propertyKey ="CHECK_NAQ_DEFECT",defaultValue = "FALSE")
	public boolean isCheckNAQDefect();
	
	@PropertyBeanAttribute(propertyKey ="CHECK_DEFECT_REPAIRED",defaultValue = "FALSE")
	public boolean isCheckDefectRepaired();
	
	/*
	 * Dead rail map - Example P1 PBS 
	 * DEAD_RAIL_MAP{MP-qG-qL} = H,H
	 * DEAD_RAIL_MAP{MP_qL_qG} = H,H
	 * DEAD_RAIL_MAP{MP-qL-qQ} = E, 
	 * DEAD_RAIL_MAP{MP-qP-qL} = E,H
	 * DEAD_RAIL_MAP{MP-qR-qL} = E,H
	 * 
	 */
	@PropertyBeanAttribute(propertyKey ="DEAD_RAIL_MAP")
	public Map<String, String[]> getDeadRailMap(Class<?> clazz);
	
	@PropertyBeanAttribute(propertyKey ="TOGGLE_LABEL_MAP")
	public Map<String, String> getToggleLabelMap();
	
}
