package com.honda.galc.service.datacollection;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.Tag;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.DataMappingPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>InstalledPartHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartHelper description </p>
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
 * <TD>Mar 12, 2012</TD>
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
 * @since Mar 12, 2012
 */

public class InstalledPartHelper extends DeviceHelperBase implements IDeviceHelper{
	private static final String REPAIRED = "repaired";
	private boolean isBuildResultRepaired;

	public InstalledPartHelper(Device device, DataMappingPropertyBean property, 
			String processPointId, Logger logger, String associateNo) {
		super(device, property, processPointId, logger, associateNo);
	}

	/**
	 * Get Installed parts from input device data by property of MbpnInstalledParts. 
	 * 
	 * @param installPartNames
	 * @return
	 */
	public List<ProductBuildResult> getBuildResultsMbpnProduct(List<String> installPartNames) {
		List<ProductBuildResult> list = new ArrayList<ProductBuildResult>();

		for(partIndex = 0; partIndex < installPartNames.size(); partIndex++){
			mappingPartName = installPartNames.get(partIndex);
			if("".equals(mappingPartName)){
				continue;
			}
			InstalledPart installedPart = getInstalledPartForMbpnProduct();
			if(installedPart != null) list.add(installedPart);
		}
		
		getLogger().info("InstalledParts from device:", System.getProperty("line.separator"), ProductBuildResult.toLogString(list));
		return list;
	}
	
	private InstalledPart getInstalledPartForMbpnProduct() {
		getLogger().info("get installed part for current rule:", currentRule == null ? "null" : currentRule.toString());
		
		InstalledPart part = null;
		isBuildResultRepaired = false;
		
		// get install part of mbpn
		nextPartIndex++;
		part = extract(InstalledPart.class);
		
		// get measurement of mbpn install part
		List<Measurement> torqueListForMbpn = getTorqueListForMbpnProduct(mappingPartName);
		if(torqueListForMbpn != null && torqueListForMbpn.size() > 0){
			if(part == null) part = new InstalledPart();
			part.setMeasurements(torqueListForMbpn);
		}
		
		if(part != null){
			part.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			
			// RGALCPROD-1118
			part.setProcessPointId(this.processPointId);
			if(!associateNo.equals(""))
				part.setAssociateNo(associateNo);
			else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
					PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
				if(part.getAssociateNo()==null) part.setAssociateNo(this.processPointId);

			part.setId(new InstalledPartId(getValidProductId(), mappingPartName));
			part.setProductType(productType);
		}
	
		return part;
	}
	
	private List<Measurement> getTorqueListForMbpnProduct(String partName) {
		return getMeasurementList(partName);
	}
	
	/**
	 * Get Installed parts from input device data by lot control rules. 
	 * 
	 * @param rules
	 * @return
	 */
	public List<ProductBuildResult> getBuildResults(List<LotControlRule> rules) {
		List<ProductBuildResult> list = new ArrayList<ProductBuildResult>();
		
		/**
		 * Device driven and not validate product Id
		 */
		if(property.isDeviceDriven() && rules == null) rules = deduceLotControlRules(); 
		
		if(rules == null || rules.size() == 0) return list;

		for(partIndex = 0; partIndex < rules.size(); partIndex++){
			currentRule = rules.get(partIndex);
			mappingPartName = currentRule.getId().getPartName();
			InstalledPart installedPart = getInstalledPart();
			if(installedPart != null)  {
				installedPart.setQicsDefect(currentRule.isQicsDefect());
				list.add(installedPart);
			}
		}
		
		getLogger().info("InstalledParts from device:", System.getProperty("line.separator"), ProductBuildResult.toLogString(list));
		return list;
	}
	
	private InstalledPart getInstalledPart() {
		getLogger().info("get installed part for current rule:", currentRule == null ? "null" : currentRule.toString());
		
		InstalledPart part = null;
		isBuildResultRepaired = false;
		if(!(isDynamicDataMapping()&& currentRule.getSerialNumberScanType() == PartSerialNumberScanType.NONE)){
			nextPartIndex++;
			part = extract(InstalledPart.class);
		}
		
		//Only read and count if there is measurement data
		if (currentRule != null && currentRule.getParts() != null && currentRule.getParts().size() > 0 && currentRule.getParts().get(0).getMeasurementCount() > 0) {
			List<Measurement> torqueListForRule = getTorqueListForRule(currentRule);
			if (torqueListForRule != null && torqueListForRule.size() > 0) {
				if (part == null) part = new InstalledPart();					
				part.setMeasurements(torqueListForRule);
				if(isBuildResultRepaired)
					part.setInstalledPartReason(REPAIRED);
			}
		}
		if(part != null){
			if(part.getActualTimestamp() == null) {
				part.setActualTimestamp(getActualTimestamp());
			}
			
			// RGALCPROD-1118
			part.setProcessPointId(this.processPointId);
			if(!associateNo.equals(""))
				part.setAssociateNo(associateNo);
			else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
					PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
				if(part.getAssociateNo() == null) part.setAssociateNo(this.processPointId);
			
			part.setId(new InstalledPartId(getValidProductId(), mappingPartName));
			part.setProductType(productType);
		}
	
		return part;
	}
	
	protected void setStatusInputData( Object obj,Field field, Object inputValue){
		InstalledPartStatus convertInstalledPartStatus = convertInstalledPartStatus(inputValue.toString());
		setInputValue(obj, field, Integer.valueOf(convertInstalledPartStatus.getId()));
		
		if(convertInstalledPartStatus != InstalledPartStatus.OK) {
			if((inputValue.getClass().isAssignableFrom(Integer.class) || 
			   inputValue.getClass().isAssignableFrom(Short.class))	&& 
			   Integer.parseInt(inputValue.toString()) == 9) {
				setInputValue(obj, field, InstalledPartStatus.OK.getId());
				markRepaired(obj);
			} else {
				super.setStatusInputData(obj, field, inputValue);
			}
		}
	}
	
	protected void markRepaired(Object obj) {
		String setMethodName = "setInstalledPartReason";
		if(obj.getClass().isAssignableFrom(InstalledPart.class)) {
			setValue(obj, REPAIRED, setMethodName);
		} else {
			isBuildResultRepaired = true;
		}
	}

	private List<Measurement> getTorqueListForRule(LotControlRule rule) {
		if(rule.getParts() != null && rule.getParts().size() > 0){
			return getTorqueList(rule.getId().getPartName(), rule.getParts().get(0).getMeasurementCount());
		} else 
			return isDynamicDataMapping() ? null : getMeasurementList(rule.getId().getPartName());
	}
	
	private List<Measurement> getTorqueList(String partName, int measurementCount) {
		List<Measurement> list = new ArrayList<Measurement>();
		for(itemIndex = 1; itemIndex <= measurementCount; itemIndex++){
			nextTorqueIndex++;
			error = null;
			Measurement measurement = extract(Measurement.class);
			if(measurement == null) break;
			setMeasurementValues(partName, measurement);			
			list.add(measurement);
		}
		itemIndex = null;
		return list;
	}

	private void setMeasurementValues(String partName, Measurement measurement) {
		measurement.setId(new MeasurementId(getValidProductId(), partName, itemIndex));
		if(measurement.getActualTimestamp() == null) {
			measurement.setActualTimestamp(getActualTimestamp());
		}
		if(error != null) measurement.setMeasurementStatus(MeasurementStatus.ERR);
	}

	private List<Measurement> getMeasurementList(String partName) {
		List<Measurement> list = new ArrayList<Measurement>();
		for(itemIndex = 1;; itemIndex++){
			error = null;
			Measurement measurement = extract(Measurement.class);
			if(measurement == null) break;
			setMeasurementValues(partName, measurement);
			list.add(measurement);
		}
		return list;
	}
	
	private Timestamp getActualTimestamp() {
		Timestamp actualTimestamp = getActualTimestampFromDevice();
		return (actualTimestamp != null) ? actualTimestamp : 
					new Timestamp(System.currentTimeMillis());
	}

	@Override
	Class<?> getBuildResultIdClass() {
		return InstalledPartId.class;
	}

	@Override
	String getPartNamesProperty() {
		return property.getPartNames();
	}

	public void emitBuildResultTag(List<InstalledPart> installedParts, String tags, Map<Object, Object> context){
		for(InstalledPart part : installedParts){
			mappingPartName = part.getPartName();
			nextPartIndex++;
			emit(InstalledPart.class, part, tags, context);
			
			if(part.getMeasurements() == null) continue;
			
			itemIndex = 0;
			for(Measurement measurement : part.getMeasurements()){
				nextTorqueIndex++;
				itemIndex++;
				emit(Measurement.class, measurement, tags, context);
			}
		}
	}

	private void emit(Class<?> clazz, Object obj, String tags, Map<Object, Object> context) {
		Map<Tag, Field> tagMap = getMappedFields(clazz);
		for(Tag tag : tagMap.keySet()){
		    if(CommonUtil.isInList(tag.name(), tags)){
		    	emit(tag.name(), obj, tagMap.get(tag), context );
		    } else if(CommonUtil.isInList(tag.alt(), tags)){
		    	emit(tag.alt(), obj, tagMap.get(tag), context );
		    }
		}
	}
}
