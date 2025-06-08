package com.honda.galc.service.printing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>KnuckleLabelPrintingUtil Class description</h3>
 * <p> KnuckleLabelPrintingUtil description </p>
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
 * Dec 2, 2010
 *
 *
 */
public class KnuckleShippingPrintingUtil {
	

	private static String KNUCKLE_SHIPPING_PRINTER = "KNUCKLE_SHIPPING_PRINTER";
	private static String SHIPPING_PLANT1 ="01";
	
	
	private static PropertyHelper propertyHelper;
	
	private BuildAttributeCache buildAttributeCache;

	
	public KnuckleShippingPrintingUtil() {
		buildAttributeCache = new BuildAttributeCache();
		propertyHelper = new PropertyHelper(KNUCKLE_SHIPPING_PRINTER);
	}

	public DataContainer print(List<SubProductShipping> shippingLots,List<SubProductShippingDetail> shippingDetails) {
		return print(shippingLots,shippingDetails,PreProductionLot.PROCESS_LOCATION_KNUCKLE);
	}
	
	public DataContainer print(List<SubProductShipping> shippingLots,List<SubProductShippingDetail> shippingDetails,String processLocation) {
		
		if(shippingDetails == null || shippingDetails.isEmpty()) return null;
		
		DataContainer dc = new DefaultDataContainer();
		
		String shippingPlant = shippingDetails.get(0).getId().getKdLotNumber().substring(4,6);
		boolean isLeftSide = shippingDetails.get(0).getId().getSubId().equals(Product.SUB_ID_LEFT);
		String caseNumber = shippingPlant.equals(SHIPPING_PLANT1)? 
				(isLeftSide ? propertyHelper.getProperty("P1_CASE_NUMBER_LEFT") : propertyHelper.getProperty("P1_CASE_NUMBER_RIGHT")) : 
				(isLeftSide ? propertyHelper.getProperty("P2_CASE_NUMBER_LEFT") : propertyHelper.getProperty("P2_CASE_NUMBER_RIGHT"));
				
		dc.put("PLANT", "PL" + shippingPlant);
		dc.put("CASE_NUMBER", caseNumber);
		
		
		
		List<String> kdLots = new ArrayList<String>();
		
		Map<KeyValue<String,String>,Integer> counts = new HashMap<KeyValue<String,String>,Integer>();

		
		String description = "";
		String kdLot ="";
		for(SubProductShippingDetail item : shippingDetails) {
			
			kdLot = item.getId().getKdLotNumber().substring(13,16);
			
			SubProductShipping subProductShipping = findShippingLot(shippingLots, item.getProductionLot());
			if(subProductShipping == null) continue;
			if(!kdLots.contains(item.getId().getKdLotNumber())) kdLots.add(item.getId().getKdLotNumber());
			
			String partNumber = getPartNumber(subProductShipping.getProductSpecCode(), item.getId().getSubId());
			String partMark = getPartMark(subProductShipping.getProductSpecCode(), item.getId().getSubId());
		
			KeyValue<String,String> keyValue = new KeyValue<String,String>(partNumber,partMark);
			
			int count = counts.containsKey(keyValue)? counts.get(keyValue) + 1 : 1;
			counts.put(keyValue, count);
			description = item.getId().getSubId().equals(Product.SUB_ID_LEFT) ? "LEFT" : "RIGHT";

		}
		
		dc.put("KD_LOTS",getKdLotString(kdLots));
		
		String partNumberString ="";
		String partMarkString = "";
		String descriptions ="";
		String pickQtys = "";
		boolean isFirst = true;
		
		for(Entry<KeyValue<String,String>,Integer> item: counts.entrySet()) {
			if(isFirst) isFirst = false;
			else{
				partNumberString +=",";
				partMarkString +=",";
				descriptions +=",";
				pickQtys +=",";
			}
			partNumberString += StringUtils.rightPad(item.getKey().getKey(),11);
			partMarkString += StringUtils.rightPad(item.getKey().getValue(),2);
			
			descriptions += StringUtils.rightPad(description, 5);
			pickQtys += StringUtils.rightPad("" + item.getValue(),2);
		}
		
		// fill in blank if the number of items is less than 9
		for(int i = counts.size(); i < 9 ;i++ ) {
		
			partNumberString +="," + StringUtils.rightPad("-", 11);
			partMarkString +="," + StringUtils.rightPad("-", 11);
			descriptions += "," + StringUtils.rightPad("-", 5);
			pickQtys += "," + StringUtils.rightPad("-", 2);
			
		}
		
		int loadCount = getKnucklesLoaded(shippingDetails);
		boolean isCartFull =  loadCount == getCartSize() 
			|| loadCount == (getKdLotSize(shippingLots)% getCartSize());
		
		dc.put("PART_NUMBERS", partNumberString);
		dc.put("PART_MARKS", partMarkString);
		dc.put("DESCRIPTIONS", descriptions);
		dc.put("PICK_QTY", pickQtys);
		dc.put("PACKING_ID", caseNumber + "-" + kdLot);	
		dc.put("CART_COMPLETE",isCartFull? 1: 0);
		
		String printerTag = PreProductionLot.PROCESS_LOCATION_KNUCKLE_KR.equals(processLocation)
							?"PRINTER_QUEUE_NAME_KR" :"PRINTER_QUEUE_NAME"; 

		createPrintingUtil(propertyHelper.getProperty(printerTag)).print(dc);
		
		return dc;
	}
	
	private int getKnucklesLoaded(List<SubProductShippingDetail> shippingDetails){
		int count =0;
		for(SubProductShippingDetail item : shippingDetails) {
			if(!StringUtils.isEmpty(item.getProductId())) count ++;
		}
		return count;
	}

	private SubProductShipping findShippingLot(List<SubProductShipping> shippingLots,String prodLot) {
    	
    	if(shippingLots.isEmpty()) return null;
    	
    	for(SubProductShipping item : shippingLots) {
    		
    		if(item.getProductionLot().equals(prodLot)) return item;
    	}
    	return null;
    }
	
	private int getKdLotSize(List<SubProductShipping> shippingLots) {
		int kdLotSize = 0;
		for(SubProductShipping shippingLot : shippingLots)
			kdLotSize += shippingLot.getSchQuantity() / 2;
		return kdLotSize;
	}

	
	private String getKdLotString(List<String> kdLots) {
		
		String kdLotString ="";
		
		boolean isFirst = true;
		
		for(String item: kdLots) {
			if(isFirst){
				isFirst = false;
				kdLotString = item.substring(0,item.length() -1);
			}
			else kdLotString +="&";
			kdLotString += item.substring(item.length() -1, item.length());
		}
		return kdLotString;
	}
	
	private PrintingUtil createPrintingUtil(String printQueueName) {
		PrintingUtil printUtil =  new PrintingUtil(printQueueName,	propertyHelper.getProperty("PRINTER_FORM_ID"));
		printUtil.setBroadcast(propertyHelper.getPropertyBoolean("USE_BROADCAST", false));
		return printUtil;
	}
	
	private String getPartMarkTag(String side) {
		return side.equals(Product.SUB_ID_LEFT) ?
				BuildAttributeTag.KNUCKLE_PART_MARK_LEFT:
				BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT;
	}
	
	private String getPartNumberTag(String side) {
		return side.equals(Product.SUB_ID_LEFT) ?
				BuildAttributeTag.KNUCKLE_LEFT_SIDE:
				BuildAttributeTag.KNUCKLE_RIGHT_SIDE;
	}
	
	private String getPartNumber(String productSpecCode, String side) {
		BuildAttribute buildAttribute = buildAttributeCache.findById(productSpecCode, getPartNumberTag(side));
		return buildAttribute == null ? "" : buildAttribute.getAttributeValue();
	}
	
	private String getPartMark(String productSpecCode, String side) {
		BuildAttribute buildAttribute = buildAttributeCache.findById(productSpecCode, getPartMarkTag(side));
		return buildAttribute == null ? "" : buildAttribute.getAttributeValue();
	}
	
	private int getCartSize() {
		return PropertyService.getPropertyInt("KNUCKLE SHIPPING","CART_SIZE", 15);
	}
}
