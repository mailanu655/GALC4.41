package com.honda.galc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.PrintAttributeFormatter;

public class ProductionLotUtil implements PrintAttributeFormatter {

	@SuppressWarnings("unchecked")
	public String execute(DataContainer dc) {
	
		Map<String,String> productLotDetail = (dc.get(TagNames.DATA_LIST) != null) ? (Map<String,String>)dc.get(TagNames.DATA_LIST) : new HashMap<String,String>();
		String productionLot = (String) dc.get(TagNames.PRODUCTION_LOT.name());
		if(productLotDetail.containsKey(productionLot)){
			//Has lot in map
			return productLotDetail.get(productionLot);
		}
		else {
			//Map does not have lot
			List<Object> objects=(List<Object>) dc.get(TagNames.DATA.name());
			List<String> prodLots=new ArrayList<String>();
			for(Object entry  : objects) {
				Object item = (entry instanceof Object[]) ? ((Object[])entry)[0] : entry;
				String preProductionLot = (String)ReflectionUtils.invoke(item, "getProductionLot", new Object[0]);
				prodLots.add(preProductionLot);
			}
			if(!(prodLots.isEmpty())){
			List<Object[]> productStampingSeq=ServiceFactory.getDao(ProductStampingSequenceDao.class).findAllProductIdByProductionLotList(prodLots);
			for(Object[] prodLot:productStampingSeq){
					productLotDetail.put(prodLot[0].toString(), prodLot[1].toString());
				
			}
			dc.put(TagNames.DATA_LIST, productLotDetail);
			
			return productLotDetail.get(productionLot);
			
		}
	}
		return "";
	}		
}
