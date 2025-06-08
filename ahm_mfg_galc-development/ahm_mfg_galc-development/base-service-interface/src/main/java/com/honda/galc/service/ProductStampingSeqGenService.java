package com.honda.galc.service;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.oif.property.ReplicateScheduleProperty2;



/** * * 
* @version 1
* @author Gangadhararao Gadde 
* @since Aug 08, 2017
*/
public interface ProductStampingSeqGenService extends IService{

	
	public List<ProductStampingSequence> createStampingSequenceList(String targetSpecCode, String targetProcessLocation, PreProductionLot targetPreProductionLot,String componentId) ;
	public List<ProductStampingSequence> createStampingSequenceList(Mbpn targetMbpn, String targetSpecCode, String targetProcessLocation, PreProductionLot targetPreProductionLot,ReplicateScheduleProperty2 propBean, String subAssyProdIdFormat) ;
	public List<ProductStampingSequence> replicateStampingSequence(Map<String, List<ProductStampingSequence>> sourceProductStampingSequenceMap, String targetProductionLot, PreProductionLot sourcePreProductionLot, PreProductionLot targetPreProductionLot );
	public PreProductionLot updateTargetPreProdLot(PreProductionLot targetPreProductionLot, String productId, String targetSpecCode, String targetProcessLocation);
}
