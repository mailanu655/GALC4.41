package com.honda.galc.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Enumaration for product check types. 
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
/* @ver 0.2
* @author Gangadhararao Gadde
*/
public enum ProductCheckType {
	//=== defects checks ===// 
	OUTSTANDING_DEFECTS_DO_NOT_EXIST_CHECK("Outstanding Defects Do Not Exist"),
	OUTSTANDING_DEFECTS_CHECK("Outstanding Defects"),
	ENGINE_BLOCK_OUTSTANDING_DEFECTS_CHECK("Block Outstanding Defects"),
	ENGINE_HEAD_OUTSTANDING_DEFECTS_CHECK("Head Outstanding Defects"),
	DEFECTS_EXIST_CHECK("Defects Exists"),

	//=== defect status checks ===// 
	OUTSTANDING_DEFECT_STATUS_CHECK("Outstanding Defect Status"),
	ENGINE_BH_OUTSTANDING_DEFECT_STATUS_CHECK("Engine (Block, Head) Outstanding Defect Status"),
	ENGINE_REPAIRED_DEFECT_STATUS_CHECK("Engine Repaired Defect Status"),

	//=== installed parts checks ===//
	OUTSTANDING_PARTS_CHECK("Outstanding Parts"),
	OUTSTANDING_PARTS_BY_PROCESS_POINTS_CHECK("Outstanding Parts (by Process Points)"),
	PARTS_EXIST_CHECK("Parts Exist"),
	
	//=== installed parts status checks ===//
	INSTALLED_PARTS_STATUS_CHECK("Installed Parts Status"),
	INSTALLED_PARTS_BY_PROCESS_POINTS_STATUS_CHECK("Installed Parts Status (by Process Points)"),

	INSTALLED_SEALANTS_STATUS_CHECK("Installed Sealants Status"),
	MISSION_NOT_INSTALLED_CHECK("Mission Not Installed"),
	
	//=== engine marraige status ===//
	ENGINE_BLOCK_NOT_MARRIED_CHECK("Engine has invalid Block marriage"),
	ENGINE_HEAD_NOT_MARRIED_CHECK("Engine has invalid Head marriage"),
	HEAD_MARRIED_CHECK("Head is married to Engine"),
	
	//=== hold checks ===//
	EXTERNAL_PRODUCT_ON_HOLD_CHECK("External Product On Hold"),
	PRODUCT_ON_HOLD_CHECK("Product On Hold"),
	PRODUCT_ON_NOW_HOLD_CHECK("Product On 'Now' Hold"),
	PRODUCT_ON_AT_SHIPPING_HOLD_CHECK("Product On 'At Shipping' Hold"),
	ENGINE_BLOCK_ON_HOLD_CHECK("Block On Hold"), 
	ENGINE_HEAD_ON_HOLD_CHECK("Head On Hold"),
	
	//=== price tag checks ===//
	FRAME_PRICE_EMPTY_CHECK("Price Missing on Product"),
	FRAME_PRICE_NOT_EMPTY_CHECK("Price Not Missing on Product"),	
	
	//=== Price By Production Date Checks===//
	PRICE_CHECK("Check price is missing on Frame"),
	PRICE_AVAILABLE_CHECK("Check price is available on Frame"),
	
	//=== Purchase Contract Checks ===//
	PURCHASE_CONTRACT_CHECK("Check Purchase Contract is missing on Frame"),
	PURCHASE_CONTRACT_EXIST_CHECK("Check Purchase Contract exists for Frame"),
	
	//=== hold status checks ===//
	PRODUCT_ON_HOLD_STATUS_CHECK("Product On Hold Status"),
	ENGINE_BH_ON_HOLD_STATUS_CHECK("Engine (Block, Head) On Hold Status"),
	ENGINE_ON_HOLD_CHECK("Engine On Hold"),

	//=== required parts checks ===//
	OUTSTANDING_REQUIRED_PARTS_CHECK("Outstanding Required Parts"), 
	OUTSTANDING_REQUIRED_PARTS_DO_NOT_EXIST_CHECK("Outstanding Required Parts Do Not Exist"),
	
	//=== misc ===//
	BLOCK_MC_NUMBER_DOES_NOT_EXIST_CHECK("Block MC Number Does Not Exist"),
	HEAD_MC_NUMBER_DOES_NOT_EXIST_CHECK("Head MC Number Does Not Exist"),
	MC_NUMBER_DOES_NOT_EXIST_CHECK("MC Number Does Not Exist"),

	ENGINE_FIRING_TEST_CHECK("Engine Firing Test Required"),
	ENGINE_ECU_VALUE_CHECK("ECU Value"),
	
	QICS_AE_OFF_NOT_COMPLETE_CHECK("AE OFF Not Complete"),
	MH_OFF_NOT_COMPLETE_CHECK("MH OFF Not Complete"),
	HCM_HEAD_MH_OFF_NOT_COMPLETE_CHECK("MH OFF Not Complete"),
	INVALID_PREVIOUS_LINE_CHECK("Invalid Previous Line"),
	INSTALLED_PARTS_INSPECTION_CHECK("Installed Parts Inspection Check"),
	INSTALLED_PARTS_INSPECTION_ALL_PROCESSES_CHECK("Installed Parts Inspection All Processes Check"),
    LET_CHECK("Let Check"),
    LET_CHECK_MICROSERVICE("Let Check Microservice"),
	NEXT_EXPECTED_PRODUCT_CHECK("Next Expected Product Check"),
    PROCESS_POINT_INSTALLED_PART_CHECK("Process Point Installed Part Check"),
    PRODUCT_HISTORY_NOT_EXIST_CHECK("Missing Product Results"),
    PRODUCT_HISTORY_EXIST_CHECK("Product Results already exist"),
    PRODUCT_RESULT_EXIST_CHECK("Product Result Exist"),
    PRODUCT_RESULT_MISSING_CHECK("Product Result Missing"),
    PREV_PROD_RESULT_MISSING_CHECK("Previous Product Result Missing"),
    PRODUCT_SHIPPED_CHECK("Product already shipped"),
	ENGINE_NOT_INSTALLED_CHECK("Engine Not Installed"),
	ENGINE_NUMBER_EMPTY_CHECK("Missing Engine Number"),
	ENGINE_NUMBER_NOT_EMPTY_CHECK("Engine Number Not Empty Check"),
	MISSION_NUMBER_EMPTY_CHECK("Missing Mission Number"),
	ENGINE_NOT_ASSIGNED_CHECK("Engine Not Assigned Check"),
	DUPLICATE_ENGINE_ASSIGNMENT_CHECK("Duplicate Engine Assignment"),
	ON_OFF_PRODUCT_HISTORY_COUNT_CHECK("On Off Product History Count Check"),
	MISSION_DOCKING_CHECK("Mission Docking Check"),
	ENGINE_DOCKING_CHECK("Engine Docking Check"),
	MODEL_CHANGE_CHECK("Model Change Check"),
	INSTALLED_ENGINE_CHECK("Installed Engine Check"),
	INCOMPLETE_ENGINE_DATA_CHECK("Incomplete Engine Data"),
	
	//===engine status check === //
	ENGINE_CURE_COMPLETE_CHECK("Engine status"),
	INVALID_PRODUCT_CHECK("Invalid Product Check"),
	PRODUCT_STATE_CHECK("Product State Check"),
	AT_CHECK("At Check"),
	AT_CHECK_LIST("At Check List"),
    NOT_AT_CHECK("Not At Check"),
    NOT_AT_CHECK_LIST("Not At Check List"),
	EXPECTED_PRODUCT_CHECK("Expected Product Check"),
	PRODUCT_EXIST_CHECK("Product Exist Check"),
	VALID_BLOCK_ID_NUMBER_CHECK("Validate Block Id Number"),
	VALID_HEAD_ID_NUMBER_CHECK("Validate Head Id Number"),
	VALID_CRANKSHAFT_ID_NUMBER_CHECK("Validate Crankshaft Id Number"),
    VALID_CONROD_ID_NUMBER_CHECK("Validate Conrod Id Number"),
	HEAD_BLOCK_NOT_MC_ON_CHECK("Product Not Machining On Check"),
	CHECK_DESTINATION_CHANGE("Destination Change Check"),
	CHECK_ENGINE_TYPE("Engine Type Check"),
	PRODUCT_ALREADY_PROCESSED_CHECK("Product Already Processed Check"),
	PROCESSED_PRODUCT_CHECK("Processed Product Check"),
	DEVICE_DATA_STATUS_CHECK("Check Device Input Data Status Value"),
	BUILD_RESULT_EXIST_CHECK("Check Build Result Exist In Database"),
	BUILD_RESULT_NOT_EXIST_CHECK("Check Build Result Not Exist In Database"),
	IS_DIECAST_MARRIED_CHECK("Check if a Diecast associated with a product"),
	DIECAST_MARRIED_CHECK("Check if a Diecast associated with a product"),
	VALID_OTHER_PLANT_PRODUCT_CHECK("Check if Diecast is valid number from a different plant."),
	INSTALLED_PART_CURE_TIME_CHECK("Installed Part Cure Time Check"),
	INSTALLED_PART_CURE_TIME_OK_CHECK("Installed Part Cure Passed Time Check"),
	
	LEGACY_SHIP_CHECK("Legacy check"),
	RECURSIVE_INSTALLED_PART_CHECK("Recursive installed part check"),
	NOT_FIXED_DEFECTS("Not Fixed Defects"),	
	NOT_FIXED_DEFECTS_BY_ENTRY_DEPARTMENT("Not Fixed Defects By Entry Department"),
	ALL_DEFECTS_FIXED_CHECK("All Defects Fixed"),
	LAST_PROCESS_POINT_CHECK("Check the last passing process point for a product"),
	CHECK_SPEC_CHANGE("Check Product Spec Changed"),
	CHECK_SPEC_CHANGE_LIST("Check Product Spec Changed List"),
	CHECK_SPEC_CHANGE_AND_HOLD("Product Spec Change Check and Hold Product On Spec Change"),
	CHECK_SPEC_CHANGE_AND_HOLD_LIST("Product Spec Change Check and Hold Product On Spec Change List"),
	CHECK_SCRAPPED_EXCEPTIONAL_OUT("Product Scrapped/Exceptional Check"),
	PRODUCT_SCRAPPED_CHECK("Product Scrapped Check"),
	EXTERNAL_REQUIRED_PART_CHECK("External Required Part Check"),
	LET_DATA_CHECK("Let Data Check"),
	CHECK_IPP_TAGS("IPP Tags Assigned to Product"),
	NOT_ASSOCIATED_WITH_CARRIER_CHECK("Check product id is associated with carrier"),
	CHECK_ENGINE_MISMATCH_AND_HOLD("Product On Hold due to Engine mismatch"),
	PRODUCTION_LOT_CHECK("Production Lot"),
	KD_LOT_CHECK("KD Lot"),
	LOT_CHANGE_CHECK("LOT CHANGE"),
	UNIQUE_PARTS_CHECK("Unique Parts"),
	DESTINATION_CHECK("Destination"),
	TRACKING_STATUS_CHECK("Tracking Status"),
	STRAGGLER_CHECK("Straggler check"),
	REMAKE_CHECK("Remake check"),
	QI_KICKOUT_CHECK("NAQ Kickout Check"),
	KICKOUT_EXIST_CHECK("Kickout Exist Check"),
	QI_DEFECT_CHECK("NAQ Defect Check"),
	QI_DEFECT_EXIST_CHECK("NAQ Defect Exist Check"),
	KICKOUT_CHECK("Kickout Check"),
	KICKOUT_LOCATION_CHECK("Kickout Location Check"),
	QI_NON_REPAIRABLE_DEFECT_CHECK("Non-Repairable Check"),
	
	VIN_BOM_SHIP_STATUS_CHECK("Vin Bom Ship Status Check"),
	CHECK_PMQA_RESULTS("Check PMQA Results"),
	PMQA_DEFECTS_BY_CATEGORY_CHECK("PMQA Defects By Category Check"),
	OUTSTANDING_REQUIRED_PARTS_CHECK_FOR_MULTIPLE_PRODUCT_TYPES("Outstanding RequiredParts Check For Multiple Product Types"),
	INSTALLED_PRODUCT_CHECK("Installed Product Check");
	
	
	private String name;
	private String msg;
	
	
	
	
	private ProductCheckType(String message) {
		this.name = message;
		this.msg = "";
	}

	public String getName() {
		return name;
	}

	public static ProductCheckType[] values(String[] ids) {

		ProductCheckType[] values = {};
		List<ProductCheckType> list = new ArrayList<ProductCheckType>();
		if (ids == null || ids.length == 0) {
			return values;
		}
		for (String id : ids) {
			if (id == null || id.trim().length() == 0) {
				continue;
			}
			ProductCheckType type = ProductCheckType.valueOf(id.trim());
			list.add(type);
		}
		values = list.toArray(new ProductCheckType[list.size()]);
		return values;
	}

	public static ProductCheckType get(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		id = StringUtils.trim(id);
		for (ProductCheckType pt : ProductCheckType.values()) {
			if (StringUtils.equals(pt.name(), id)) {
				return pt;
			}
		}
		return null;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
