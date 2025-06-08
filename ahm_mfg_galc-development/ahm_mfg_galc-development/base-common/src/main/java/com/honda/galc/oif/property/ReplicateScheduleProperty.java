package com.honda.galc.oif.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * @author Gangadhararao Gadde
 * @date apr 6, 2016
 */
public interface ReplicateScheduleProperty extends InHouseSchedulePropertyBean, IProperty {

	
	// specifies the source plan codes
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getSourcePlanCode();
	
	// specifies the source process locations
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getSourceProcessLocation();
	
	// specifies the Target plan codes
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getTargetPlanCode();
	
	// specifies the Target Process Locations
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getTargetProcessLocation();
	
	// specifies whether to rebuild schedule
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getRebuildSchedule();

	// specifies whether to create records in PRODUCTION_LOT_MBPN_SEQUENCE_TBX during schedule replication
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getUseProductionLotMbpnSequence();
	
	// Generate the sequence number if source lot seq num is null
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isGenerateSeqNum();
	
	// Use Sequence number for ordering the PreProduction lots in order to identify the first current Pre-Production Lot by plan code 
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCurrentLotOrderBySeqNum();
	
/*	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMbpnPartPrefix();*/
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getMbpnPartPrefix(Class<?> clasz);

	@PropertyBeanAttribute(defaultValue = "")
	public String getMbpnPartEnum();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getSubAssembleIdRule(Class<?> clasz);

}
