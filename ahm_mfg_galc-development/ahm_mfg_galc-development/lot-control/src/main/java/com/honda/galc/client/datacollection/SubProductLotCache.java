package com.honda.galc.client.datacollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductLot;
/**
 * 
 * <h3>SubProductLotCache</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SubProductLotCache for Knuckles product use only </p>
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
 * @author Paul Chou
 * Dec 21, 2010
 *
 */
public class SubProductLotCache extends PersistentCache{
	private static SubProductLotCache INSTANCE;
	private static final String persistentKey = "subProductLots";
	private SubProductLotCache() {
		super();
	}
	
	public  static SubProductLotCache getInstance(){
		if(INSTANCE == null)
			INSTANCE = new SubProductLotCache();
		
		return INSTANCE;
	}

	public void initSubProductionLots(List<PreProductionLot> unSentPreproductionLots, BuildAttributeCache buildAttributeCache) {
			List<SubProductLot> subProductLotList = new ArrayList<SubProductLot>();
			BuildAttribute buildAttribute;
			for(PreProductionLot lot : unSentPreproductionLots){
				buildAttribute = buildAttributeCache.findById(lot.getProductSpecCode(), BuildAttributeTag.KNUCKLE_LEFT_SIDE);
				subProductLotList.add(createSubProductLot(lot, SubProduct.SUB_ID_LEFT, buildAttribute));
				
				buildAttribute = buildAttributeCache.findById(lot.getProductSpecCode(), BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
				subProductLotList.add(createSubProductLot(lot, SubProduct.SUB_ID_RIGHT, buildAttribute));
			}
			
			put(persistentKey, subProductLotList);

		
	}
	
	private SubProductLot createSubProductLot(PreProductionLot lot, String subId, BuildAttribute buildAttribute) {
		return new SubProductLot(lot, subId, buildAttribute);
	}
	
	public List<SubProductLot> getSubProductLots() {
		return getList(persistentKey, SubProductLot.class);
	}
	
	public List<SubProductLot> getSubProductLots(String productionLot) {
		List<SubProductLot> list = new ArrayList<SubProductLot>();
		for(SubProductLot lot : getSubProductLots()){
			if(productionLot.equals(lot.getProductionLot()))
				list.add(lot);
		}
		
		Collections.sort(list);
		
		return list;
	}

	public Map<String, SubProductLot> getSubProductLotMap(String productionLot, String processPointId) {
		Map<String, SubProductLot> map = new HashMap<String, SubProductLot>();
		for(SubProductLot lot : getSubProductLots()){
			if(productionLot.equals(lot.getProductionLot()))
				map.put(processPointId + lot.getSubId(), lot);
		}
		
		if(map.size() != 2)
			Logger.getLogger().warn("WARN: Incorrect knuckles subProductLot map size:" + map.size());
		
		return map;
	}
	
}
