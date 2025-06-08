package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsCarrierDao Class description</h3>
 * <p> GtsCarrierDao description </p>
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
 * Nov 28, 2014
 *
 *
 */
public interface GtsCarrierDao extends IDaoService<GtsCarrier, GtsCarrierId>{
	
	List<GtsCarrier> findAllByProductId(String carrierArea, String productId);
	
	GtsCarrier findByCarrierId(String carrierArea, int carrierId);
	
	List<GtsCarrier> findAllByCarrierStatus(String carrierArea, int carrierStatus);
	
	List<GtsCarrier> findAllByLane(String trackingArea, String laneId, String carrierArea);
	
	List<GtsCarrier> findAll(String trackingArea, String carrierArea);
	
	List<GtsCarrier> findAllNoProduct(String trackingArea, String carrierArea);
}
