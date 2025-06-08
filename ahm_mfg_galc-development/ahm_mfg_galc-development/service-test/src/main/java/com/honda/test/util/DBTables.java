package com.honda.test.util;

import java.util.HashMap;
import java.util.Map;

public class DBTables {
	public static Map<String,String> processTables = new HashMap<String, String>();
	public static Map<String,String> productTables = new HashMap<String, String>();
	public static Map<String,String> dataCollectionTables = new HashMap<String, String>();
	public static Map<String,String> trackingTables = new HashMap<String, String>();
	public static Map<String,String> engineShippingTables = new HashMap<String, String>();
	public static Map<String,String> gtsTables = new HashMap<String, String>();
	
	static {
		processTables.put("Site", "GAL117TBX");
		processTables.put("Plant", "GAL211TBX");
		processTables.put("Division", "GAL128TBX");
		processTables.put("Line", "GAL195TBX");
		processTables.put("Process Point", "GAL214TBX");
		processTables.put("Feature", "FEATURE_TBX");
		processTables.put("Notification Provider", "NOTIFICATION_PROVIDER_TBX");
		processTables.put("Notification Subscriber", "NOTIFICATION_SUBSCRIBER_TBX");
		processTables.put("Notification", "NOTIFICATION_TBX");
		processTables.put("Broadcast Destination", "GAL111TBX");
		processTables.put("Template", "TEMPLATES_TBX");
		
		
		
		
		processTables.put("GPCS Division", "GAL238TBX");
		processTables.put("Application","GAL241TBX");
		processTables.put("ApplicationByTermial","GAL242TBX");
		processTables.put("Application Task","GAL243TBX");
		processTables.put("Task Spec","GAL244TBX");
		processTables.put("Terminal","GAL234TBX");
		processTables.put("Device","GAL253TBX");
		processTables.put("DeviceFormat","GAL257TBX");
		processTables.put("Print Attribute","GAL258TBX");
		processTables.put("Build Attribute","GAL259TBX");
		processTables.put("Component Property","GAL489TBX");
		
		productTables.put("Product Type", "PRODUCT_TYPE_TBX");
		productTables.put("Frame", "GAL143TBX");
		productTables.put("Frame Spec", "GAL144TBX");
		productTables.put("Engine", "GAL131TBX");
		productTables.put("Engine Spec", "GAL133TBX");
		productTables.put("Pre Production Lot", "GAL212TBX");
		productTables.put("Production Lot", "GAL217TBX");
		productTables.put("Product Stamping Sequence","GAL216TBX");
		productTables.put("Daily Department Schedule","GAL226TBX");
		productTables.put("Defect Result","GAL125TBX");
		productTables.put("Hold Result","GAL147TBX");
		productTables.put("Exceptional Out","GAL136TBX");
		productTables.put("Expected Product","GAL135TBX");
		productTables.put("Block Load","BLOCK_LOAD_TBX");
		productTables.put("Block","BLOCK_TBX");
		productTables.put("Block build result","BLOCK_BUILD_RESULTS_TBX");
		productTables.put("Block history","BLOCK_HISTORY_TBX");
		productTables.put("Priority Plan","TGA3051_TBX");
		
		
		
		
		dataCollectionTables.put("Part Name", "GAL261TBX");
		dataCollectionTables.put("Part Spec","PART_SPEC_TBX");
		dataCollectionTables.put("Mesuarement Spec","MEASUREMENT_SPEC_TBX");
		dataCollectionTables.put("PartByProductSpec","GAL245TBX");
		dataCollectionTables.put("Lot Control Rule","GAL246TBX");
		dataCollectionTables.put("Part Install Result","GAL185TBX");
		dataCollectionTables.put("Measurement","GAL198TBX");
		dataCollectionTables.put("Part Install Result History","GAL185_HIST_TBX");
		dataCollectionTables.put("MeasurementMeasurement Histoty","GAL198_HIST_TBX");
		
		trackingTables.put("InProcessProduct", "GAL176TBX");
		trackingTables.put("Product Result", "GAL215TBX");
		trackingTables.put("Counter", "GAL240TBX");
		trackingTables.put("Counter by Model Group","GAL118TBX");
		trackingTables.put("Counter by Production Lot","GAL120TBX");
		trackingTables.put("Counter by Product Spec","GAL119TBX");
		
		engineShippingTables.put("Vanning Schedule","VANNING_SCHEDULE_TBX");
		engineShippingTables.put("Trailer","TRAILER_TBX");
		engineShippingTables.put("Trailer Info","TRAILER_INFO_TBX");
		engineShippingTables.put("Quorum","QUORUM_TBX");
		engineShippingTables.put("Quorum Detail","QUORUM_DETAIL_TBX");
		
		gtsTables.put("GtsArea","GTS_AREA_TBX");
		gtsTables.put("GtsCarrier","GTS_CARRIER_TBX");
		gtsTables.put("GtsClientList","GTS_CLIENT_LIST_TBX");
		gtsTables.put("GtsColorMap","GTS_COLOR_MAP_TBX");
		gtsTables.put("GtsDecisionPoint","GTS_DECISION_POINT_TBX");
		gtsTables.put("GtsDPCondition","GTS_DP_CONDITION_TBX");
		gtsTables.put("GtsIndicator","GTS_INDICATOR_TBX");
		gtsTables.put("GtsLabel","GTS_LABEL_TBX");
		gtsTables.put("GtsLaneCarrier","GTS_LANE_CARRIER_TBX");
		gtsTables.put("GtsLaneSegmentMap","GTS_LANE_SEGMENT_MAP_TBX");
		gtsTables.put("GtsLaneSegment","GTS_LANE_SEGMENT_TBX");
		gtsTables.put("GtsLane","GTS_LANE_TBX");
		gtsTables.put("GtsMoveCondition","GTS_MOVE_CONDITION_TBX");
		gtsTables.put("GtsMove","GTS_MOVE_TBX");
		gtsTables.put("GtsNode","GTS_NODE_TBX");
		gtsTables.put("GtsProduct","GTS_PRODUCT_TBX");
		gtsTables.put("GtsShape","GTS_SHAPE_TBX");
		gtsTables.put("GtsZone","GTS_ZONE_TBX");
		
	}
}
