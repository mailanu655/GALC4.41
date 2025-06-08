package com.honda.galc.entity.enumtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public enum StrategyType {
	/*
	 * Add Strategy Type to appear in the Team Leader combo box selection
	 *   1. UNIQUE Name that you want to display to the associate in the combo box
	 *   2. Class path for the strategy type
	 *   3. Strategy will show on team leader drop down list if product type matches. null will apply 
	 *      the strategy for all products
	 */
	AT_LABEL_CHECK("AT Label Check", "com.honda.galc.client.datacollection.strategy.AntiTheftIdValidation"),
	CHECK_DIGIT_PART_SERIAL_NUMBER("Check Digit Part Serial Number", "com.honda.galc.client.datacollection.processor.CheckDigitPartSerialNumberProcessor"),
	CERT_PLATE_SCAN("Cert Plate Scan", "com.honda.galc.client.datacollection.processor.CertScanProcessor"),
	CON_ROD_WEIGHT_VALIDATION("ConRodWeightValidation", "com.honda.galc.client.datacollection.strategy.ConRodWeightValidation"),
	DUP_PART_SN_VALIDATION("Duplicate Part Validation","com.honda.galc.client.datacollection.strategy.PartSerialNumberDuplicateValidation"),
	ENGINE_LOAD_PLC_MONITOR("Engine Load PLC Monitor", "com.honda.galc.client.datacollection.processor.PlcDataReadyMonitorProcessor"),
	ENGINE_LOAD_PLC_UPDATE("Engine Load PLC Update", "com.honda.galc.client.datacollection.processor.EngineLoadPlcUpdateProcessor"),
	ENGINE_VIN_ASSIGNMENT("Engine - VIN Assignment", "com.honda.galc.client.datacollection.processor.EngineLoadSnProcessor"),
	ENGINE_VIN_CONFIRMATION("Engine VIN Confirmation", "com.honda.galc.client.datacollection.strategy.EngineSnConfirmationProcessor"),
	ENGINE_VIN_VERIFY("Engine - VIN Verify", "com.honda.galc.client.datacollection.processor.EngineLoadSnVerifyProcessor"),
	ENHANCED_LABEL_PRINT_PROCESSOR("Enhanced Label Print", "com.honda.galc.client.datacollection.processor.EnhancedLabelPrintProcessor"),
	HUB_PRESS("Hub Press", "com.honda.galc.client.datacollection.strategy.HubPressStrategy"),
	IPU_BATTERY_CONFIRMATION("IPU Battery Confirmation", "com.honda.galc.client.datacollection.processor.IpuConfirmBatteryProcessor"),
	IPU_CONFIRMATION("IPU Confirmation", "com.honda.galc.client.datacollection.processor.IpuConfirmProcessor"),
	IPU_DATA_OF_LINE("IPU Data Of Line", "com.honda.galc.client.datacollection.strategy.IpuDataOfLineStrategy"),
	IPU_DATA_OF_TESTER("IPU Data Of Tester", "com.honda.galc.client.datacollection.strategy.IpuDataOfTesterStrategy"),
	IPU_LABEL_DATE_MATCH("IPU ID to Label Date Comparison", "com.honda.galc.client.datacollection.strategy.IpuLabelDateMatchStrategy"),
	IPU_QA_TESTER_INSTALL_PART("IPU QA Tester Install part", "com.honda.galc.client.datacollection.processor.IpuQaTesterInstalledPartProcessor"),
	KEY_CYLINDER("Key Cylinder", "com.honda.galc.client.datacollection.processor.KeyCylinderVinProcessor"),
	KNUCKLE_BAR_VIN_MARRIAGE("Knuckle Bar Vin Marriage", "com.honda.galc.client.datacollection.processor.KnuckleBarVinMarriageProcessor"),
	MANUAL_MEASUREMENT("Manual Measurement", "com.honda.galc.client.datacollection.processor.MeasurementProcessor"),
	MISSION_INSTALL("Mission Install", "com.honda.galc.client.datacollection.strategy.MissionInstall"),
	MQ_NGT_MESSAGE("MQ NGT Message Send","com.honda.galc.client.datacollection.strategy.NGTMQsendMessage"),
	NO_ACTION("No Action Required", "com.honda.galc.client.datacollection.processor.NoActionReqdProcessor"),
	PART_SERIAL_NUMBER("Part Serial Number", "com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor"),
	PIN_STAMP("Pin Stamp", "com.honda.galc.client.datacollection.processor.PinStampPartSNProcessor"),
	PISTON_WEIGHT_VALIDATION("PistonWeightValidation", "com.honda.galc.client.datacollection.strategy.PistonWeightValidation"),
	PRODUCT_ID_VALIDATION("Product ID Validation", "com.honda.galc.client.datacollection.strategy.ProductIdValidation"),
	TORQUE("Torque", "com.honda.galc.client.datacollection.processor.TorqueProcessor"),
	VQ_SHIP_FIC_SCAN("VQ Ship FIC Scan", "com.honda.galc.client.datacollection.processor.VQShipFICProcessor"),
	EXT_SHIP_FIC_SCAN("Ext Ship FIC Scan", "com.honda.galc.client.datacollection.processor.ExtShipFICProcessor"),
	VQ_SHIP_KEY_SCAN("VQ Ship Key Scan", "com.honda.galc.client.datacollection.processor.VQShipKeyNumProcessor"),
	IQA("IQA Marriage", "com.honda.galc.client.datacollection.processor.IqaProcessor"),
	PART_SN_CHECK("IP Subline mask check", "com.honda.galc.client.datacollection.processor.PartMaskCheckProcessor"),
	REQUIRED_PART_VALIDATION("RequiredPartsValidation", "com.honda.galc.client.datacollection.processor.RequiredPartsValidationProcessor"),
	DEVICE_DATA_RESPONSE("DeviceDataResponse", "com.honda.galc.client.datacollection.strategy.DeviceDataResponseProcessor"),
	MISSION_VIN_ASSIGNMENT("Mission - VIN Assignment", "com.honda.galc.client.datacollection.processor.EngineLoadMissionSnProcessor"),
	PART_SERIAL_MATCH_CHECK("Part Serial Match Check", "com.honda.galc.client.datacollection.processor.PartSerialMatchCheckSerialNumProcessor"),
	PART_SN_CONFIRMATION("SN Confirmation", "com.honda.galc.client.datacollection.strategy.PartSerialNumberConfirmationProcessor"),
	IPU_EXPIRATION_CHECK("IPU Expiration Check", "com.honda.galc.client.datacollection.processor.IpuExpirationCheckProcessor"),
	BLOCK_BORE_CHECK("Block Bore Validation", "com.honda.galc.client.datacollection.strategy.BlockMeasureProcessor"),
	
	// legacy strategies
	LEGACY_IMMOBILIZER_REGISTRATION("Immobilizer Registration", "com.honda.global.galc.client.immobi.controller.ImmobiRuleProcessor"),
	LEGACY_IMMOBILIZER_KEY_ID_REGISTRATION("Immobilizer Key Id Registration", "com.honda.global.galc.client.immobi.controller.ImmobiKeyIdScanProcessor"),
	LEGACY_CERT_LABEL("Cert Label", " com.honda.global.galc.client.lotcontrol.controller.CertLabelRuleProcessor"),
	LEGACY_STANDARD("Standard", "com.honda.global.galc.client.lotcontrol.controller.StandardRuleProcessor");
	
	
	private String displayName;
	private String className;
	
	private StrategyType(String comboDisplayName, String className) {
		this.displayName = comboDisplayName;
		this.className = className;
	}

	public String getDisplayName() {
		return displayName;                                                  
	}
	
	public String getCanonicalStrategyClassName() {
		return className;                                                 
	}
	
	public static String getName(String comboDisplayName) {
		if (comboDisplayName == null) return null;
		for(StrategyType type:StrategyType.values()){
			if(StringUtils.trimToEmpty(comboDisplayName).equalsIgnoreCase(type.getDisplayName())) return type.name();
		}
		return comboDisplayName;
	}
	
	public static ArrayList<String> getComboDisplayNames(List<String> typeFilter) {
		ArrayList<String> names = new ArrayList<String>();
		for(StrategyType type:StrategyType.values()){
			if ((typeFilter.size() == 0) || typeFilter.contains(type.name()))
				names.add((type.getDisplayName()));
		}
		
		//add a empty item for remove strategy
		if(names.size() > 0) names.add(0, null);
		return names;
	}
}