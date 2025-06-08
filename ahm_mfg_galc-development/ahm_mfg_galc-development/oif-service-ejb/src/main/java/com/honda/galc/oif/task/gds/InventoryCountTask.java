package com.honda.galc.oif.task.gds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>InventoryCountTask Class description</h3>
 * <p> InventoryCountTask description </p>
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
 * Nov 19, 2012
 *
 *
 */

public class InventoryCountTask extends AbstractDataCalculationTask{

	public InventoryCountTask(String name) {
		super(name);
	}

	@Override
	public void processCalculation() {
		List<List<List<String>>> allProcessPoints = getAllProcessPoints("INVENTORY_");
		Map<String,List<InventoryCount>> inventoryCountMap = new HashMap<String,List<InventoryCount>>();
		
		for(List<List<String>> processPointLists : allProcessPoints){
			Map<String,Integer> countMap = new HashMap<String,Integer>();
			Map<String,Integer> holdMap = new HashMap<String,Integer>();
			String deptName = "";
			String processPointNames = "";
			for(List<String> processPointIds : processPointLists) {
				for(String processPointId : processPointIds) {
					String productType = getProductType(processPointId);
					String divisionId = getDepartmentName(processPointId);
					if(!StringUtils.isEmpty(divisionId)) deptName = divisionId;
					if(processPointNames.length() != 0) processPointNames +=".";
					processPointNames += getProcessPointName(processPointId); 
					List<InventoryCount> allCounts;
					if(inventoryCountMap.containsKey(productType)) allCounts = inventoryCountMap.get(productType);
					else {
						allCounts = ProductTypeUtil.findAllInventoryCounts(productType);
						inventoryCountMap.put(productType, allCounts);
					}
					
					List<InventoryCount> counts = getInventoryCounts(processPointId, allCounts);
					for(InventoryCount count : counts) {
						int oldCount = 0;
						int oldHold = 0;
						if (countMap.containsKey(count.getPlant())){
							oldCount = countMap.get(count.getPlant());
							oldHold = holdMap.get(count.getPlant());
						}
						countMap.put(count.getPlant(), oldCount + count.getCount());
						holdMap.put(count.getPlant(), oldHold + count.getHoldCount());
						
					}
				}
			}
			
			List<String> destinationPlants = getDestinationPlants(deptName);
			if(!destinationPlants.isEmpty()) {
				for(String plantName : destinationPlants) {
					plantName = StringUtils.trim(plantName);
					int count = countMap.containsKey(plantName) ? countMap.get(plantName) : 0;
					int holdCount = holdMap.containsKey(plantName) ? holdMap.get(plantName) : 0;
					updateValue(deptName + "\\Current Inventory" + "\\" + processPointNames + "\\" + plantName, count);
					updateValue(deptName + "\\Current Holds" + "\\" + processPointNames + "\\" + plantName, holdCount);
				}
			}else {
				int count = 0;
				int holdCount = 0;
				for(String plant : countMap.keySet()) {
					count += countMap.get(plant);
				}
				for(String plant : holdMap.keySet()) {
					holdCount += holdMap.get(plant);
				}
				updateValue(deptName + "\\Current Inventory" + "\\" + processPointNames, count);
				updateValue(deptName + "\\Current Holds" + "\\" + processPointNames, holdCount);
			}
			
		}
	}

	private List<InventoryCount> getInventoryCounts(String processPointId, List<InventoryCount> allCounts) {
		List<InventoryCount> counts = new ArrayList<InventoryCount>();
		for(InventoryCount count : allCounts) {
			if(StringUtils.equalsIgnoreCase(count.getProcessPointId(),processPointId))
				counts.add(count);
		}
		return counts;
	}
	
	private List<String> getDestinationPlants(String divisionId) {
		String plantNames = getProperty("DESTINATION_PLANTS_" + divisionId);
		if(StringUtils.isEmpty(plantNames)) return new ArrayList<String>();
		return parseString(plantNames, Delimiter.COMMA);
	}

}
